package com.dian.yunbo.ui.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dian.yunbo.model.SitedBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import dian.com.yunbo.R;
import zlc.season.practicalrecyclerview.AbstractViewHolder;

/**
 * Created by Seiko on 2017/4/15. Y
 */

public class SitedViewHolder extends AbstractViewHolder<SitedBean> {

    @BindView(R.id.title)
    TextView title;

    private Context mContext;
    private SitedBean bean;

    public  SitedViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_sited);
        ButterKnife.bind(this, itemView);
        mContext = parent.getContext();
    }

    @Override
    public void setData(SitedBean bean) {
        this.bean = bean;
        title.setText(bean.getName());
    }
}
