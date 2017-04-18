package com.dian.yunbo.ui.viewholder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.dian.yunbo.App;
import com.dian.yunbo.model.LikeBean;
import java.io.File;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import dian.com.yunbo.R;
import zlc.season.practicalrecyclerview.AbstractAdapter;
import zlc.season.practicalrecyclerview.AbstractViewHolder;

import static com.dian.yunbo.Navigation.showBtInfo;

/**
 * Created by Seiko on 2017/4/8. Y
 */

public class LikeViewHolder extends AbstractViewHolder<LikeBean> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.day)
    TextView day;

    private Context mContext;
    private LikeBean bean;
    private AbstractAdapter mAdapter;

    public LikeViewHolder(ViewGroup parent, AbstractAdapter adapter) {
        super(parent, R.layout.item_like);
        ButterKnife.bind(this, itemView);
        mContext = parent.getContext();
        mAdapter = adapter;
    }

    @Override
    public void setData(LikeBean bean) {
        this.bean = bean;
        title.setText(bean.getName());
        day.setText(bean.getTime());
    }

    @OnClick(R.id.layout)
    void toInfo() {
        showBtInfo(mContext, bean.getName(), bean.getHash());
    }

    @OnLongClick(R.id.layout)
    boolean toDelete() {
        new AlertDialog.Builder(mContext)
                .setMessage("删除任务及下载缓存")
                .setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAdapter.remove(getAdapterPosition());
                        File file = new File(App.getInstance().getLikePath() + bean.getHash());
                        if (file.exists()) {
                            file.delete();
                            Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                        }
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
}
