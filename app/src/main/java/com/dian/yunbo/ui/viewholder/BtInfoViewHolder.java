package com.dian.yunbo.ui.viewholder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import dian.com.yunbo.R;
import com.dian.yunbo.App;
import com.dian.yunbo.Navigation;
import com.dian.yunbo.api.SourceApi;
import com.dian.yunbo.manager.DownloadStatus;
import com.dian.yunbo.model.BtInfoBean;
import com.dian.yunbo.sited.YhNode;
import com.dian.yunbo.sited.YhSource;
import com.dian.yunbo.sited.view.ISdViewModel;
import com.dian.yunbo.sited.view.SdSourceCallback;
import com.dian.yunbo.utils.FileUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadLargeFileListener;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.File;
import zlc.season.practicalrecyclerview.AbstractViewHolder;
import static com.dian.yunbo.Navigation.outDown;
import static com.dian.yunbo.Navigation.showVideo;

/**
 * Created by Seiko on 2017/3/26. Y
 */

public class BtInfoViewHolder extends AbstractViewHolder<BtInfoBean> implements ISdViewModel {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.size)
    TextView size;
    @BindView(R.id.down)
    Button down;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.outplay)
    Button outplay;
    @BindView(R.id.outdown)
    Button outdown;

    private Context  mContext;
    private BtInfoBean bean;
    private String path;
    private int status;
    private BaseDownloadTask mTask;
    private FileDownloadListener mListener;
    private int outplayID = 0;    //判断是否正在解析磁力
    private String downUrl;
    private String downCookie;

    public BtInfoViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_info);
        ButterKnife.bind(this, itemView);
        mContext = parent.getContext();
    }

    @Override
    public void setData(BtInfoBean bean) {
        this.bean =bean;
        name.setText(bean.getName());
        size.setText(bean.getSize());
        path = App.getInstance().getDownPath() + bean.getName().replaceAll("/", "_");
        if (FileUtil.isExits(path)) {
            status = DownloadStatus.STATE_DOWNLOADED;
            down.setText("打开");
        } else {
            status = DownloadStatus.STATE_NONE;
            loadListener();
        }
    }

    @OnClick({R.id.outdown, R.id.outplay, R.id.play, R.id.down})
    void toVideo(TextView v) {
        CharSequence text = v.getText();
        v.setText("等待");
        YhSource source = SourceApi.getInstance().getBtinfo();
        if (source == null) {
            Toast.makeText(mContext, "未发现种子解析插件", Toast.LENGTH_SHORT).show();
        } else {
            if (TextUtils.isEmpty(downUrl) || TextUtils.isEmpty(downCookie)) {
                if (outplayID == 0) {
                    outplayID = 1;
                    source.getNodeViewModel2(this, source._result, bean.getUrl(), new SdSourceCallback() {
                        @Override
                        public void run(Integer code) {
                            if (code == 1) {
                                setToDo(v);
                            } else {
                                v.setText(text);
                                Toast.makeText(mContext, "解析失败", Toast.LENGTH_SHORT).show();
                            }
                            outplayID = 0;
                        }
                    });
                }
            } else {
                setToDo(v);
            }
        }
    }

    private void setToDo(View v) {
        if (!TextUtils.isEmpty(downUrl) && !TextUtils.isEmpty(downCookie)) {
            switch (v.getId()) {
                case R.id.outdown:
                    toOutDown();
                    break;
                case R.id.outplay:
                    toOutVideo();
                    break;
                case R.id.play:
                    showVideo(mContext, bean.getName(), downUrl, downCookie);
                    break;
                case R.id.down:
                    getDown();
                    break;
            }
        }
    }

    private void toOutDown() {
        String url = "http://www.51gdj.com/jiexi/yunbo/api_download.php?do=url&cookie=" + downCookie + "&url=" + downUrl;
        Navigation.outDown(mContext, url);
        outdown.setText("直连");
    }

    private void toOutVideo() {
        Navigation.outVideo(mContext, downUrl, downCookie);
        outplay.setText("外部");
    }


    private void getDown() {
        switch (status) {
            case DownloadStatus.STATE_NONE:
            case DownloadStatus.STATE_PAUSED:
                start();
                break;
            case DownloadStatus.STATE_PROCRESS:
                pause();
                break;
            case DownloadStatus.STATE_DOWNLOADED:
                outDown(mContext, path);
                break;
        }
    }

    private void start() {
        mTask = createDown();
        mTask.start();
    }

    private void pause() {
        if (mTask != null) {
            mTask.pause();
        }
    }

    private BaseDownloadTask createDown() {
        return FileDownloader.getImpl().create(downUrl).addHeader("Cookie", downCookie)
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                .setPath(path)
                .setListener(mListener);
    }

    private void loadListener() {
        mListener = new FileDownloadLargeFileListener() {
            @Override
            protected void pending(BaseDownloadTask baseDownloadTask, long l, long l1) {

            }

            @Override
            protected void progress(BaseDownloadTask baseDownloadTask, long l, long l1) {
                status = DownloadStatus.STATE_PROCRESS;
                down.setText("停止");
                progress.setVisibility(View.VISIBLE);
                progress.setMax((int) l1);
                progress.setProgress((int) l);
            }

            @Override
            protected void paused(BaseDownloadTask baseDownloadTask, long l, long l1) {
                status = DownloadStatus.STATE_PAUSED;
                down.setText("继续");
//                Toast.makeText(mContext, "下载暂停", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void completed(BaseDownloadTask baseDownloadTask) {
                progress.setVisibility(View.GONE);
                down.setText("打开");
                status = DownloadStatus.STATE_DOWNLOADED;
                Toast.makeText(mContext, "下载完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void error(BaseDownloadTask baseDownloadTask, Throwable throwable) {
                Toast.makeText(mContext, "下载错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void warn(BaseDownloadTask baseDownloadTask) {
                Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
            }
        };
    }

    //=====================================================
    /** 长按删除缓存 */
    @OnLongClick(R.id.layout)
    boolean toDelete() {
        if (status != DownloadStatus.STATE_NONE) {
            new AlertDialog.Builder(mContext)
                    .setMessage("删除任务及下载缓存")
                    .setNegativeButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delete();
                        }
                    })     //通知中间按钮
                    .setPositiveButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })      //通知最右按钮
                    .create()
                    .show();
        }
        return true;
    }

    private void delete() {
        if (!TextUtils.isEmpty(path)) {
            new File(path).delete();
            new File(FileDownloadUtils.getTempPath(path)).delete();
            status = DownloadStatus.STATE_NONE;
            down.setText("下载");
            mTask = null;
            progress.setVisibility(View.GONE);
            Toast.makeText(mContext, "缓存已清空", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loadByJson(YhNode config, String... jsons) {
        for (String json : jsons) {
            JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
            downUrl = obj.get("url").getAsString();
            downCookie = obj.get("cookie").getAsString();
        }
    }
}
