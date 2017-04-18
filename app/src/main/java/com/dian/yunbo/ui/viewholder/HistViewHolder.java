package com.dian.yunbo.ui.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dian.com.yunbo.R;
import com.dian.yunbo.model.HistBean;
import zlc.season.practicalrecyclerview.AbstractViewHolder;

import static com.dian.yunbo.Navigation.showBtInfo;

/**
 * Created by Seiko on 2017/3/29. Y
 */

public class HistViewHolder extends AbstractViewHolder<HistBean> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.day)
    TextView day;

    private Context mContext;
    private HistBean bean;

    public HistViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_hist);
        ButterKnife.bind(this, itemView);
        mContext = parent.getContext();
    }

    @Override
    public void setData(HistBean bean) {
        this.bean = bean;
        title.setText(bean.getName());
        day.setText(bean.getTime());
    }

    @OnClick(R.id.layout)
    void toInfo() {
        showBtInfo(mContext, bean.getName(), bean.getHash());
    }
}
