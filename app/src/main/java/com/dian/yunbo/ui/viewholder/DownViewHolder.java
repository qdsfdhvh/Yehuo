package com.dian.yunbo.ui.viewholder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.dian.yunbo.model.DownBean;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.File;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import dian.com.yunbo.R;
import zlc.season.practicalrecyclerview.AbstractAdapter;
import zlc.season.practicalrecyclerview.AbstractViewHolder;

import static com.dian.yunbo.Navigation.outDown;


/**
 * Created by Seiko on 2017/4/8. Y
 */

public class DownViewHolder extends AbstractViewHolder<DownBean> {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.day)
    TextView day;

    private Context mContext;
    private DownBean bean;
    private AbstractAdapter mAdapter;

    public DownViewHolder(ViewGroup parent, AbstractAdapter adapter) {
        super(parent, R.layout.item_hist);
        ButterKnife.bind(this, itemView);
        mContext = parent.getContext();
        mAdapter = adapter;
    }

    @Override
    public void setData(DownBean bean) {
        this.bean = bean;
        title.setText(bean.getName());
        day.setText(bean.getTime());
    }

    @OnClick(R.id.layout)
    void toInfo() {
        outDown(mContext, bean.getPath());
    }

    @OnLongClick(R.id.layout)
    boolean toDelete() {
        new AlertDialog.Builder(mContext)
                .setMessage("删除文件")
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
        return true;
    }

    private void delete() {
        if (!TextUtils.isEmpty(bean.getPath())) {
            mAdapter.remove(getAdapterPosition());
            new File(bean.getPath()).delete();
            new File(FileDownloadUtils.getTempPath(bean.getPath())).delete();
            Toast.makeText(mContext, "文件已删除", Toast.LENGTH_SHORT).show();
        }
    }
}
