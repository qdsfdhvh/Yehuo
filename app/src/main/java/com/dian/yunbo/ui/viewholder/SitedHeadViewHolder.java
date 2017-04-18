package com.dian.yunbo.ui.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.dian.yunbo.model.HeadBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import dian.com.yunbo.R;
import zlc.season.practicalrecyclerview.AbstractViewHolder;

/**
 * Created by Seiko on 2017/4/15. Y
 */

public class SitedHeadViewHolder extends AbstractViewHolder<HeadBean> {

    @BindView(R.id.head_title)
    TextView title;

    private Context context;
    private HeadBean bean;

    public SitedHeadViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
    }

    @Override
    public void setData(HeadBean bean) {
        this.bean = bean;
        title.setText(bean.getTitle());
    }

}
