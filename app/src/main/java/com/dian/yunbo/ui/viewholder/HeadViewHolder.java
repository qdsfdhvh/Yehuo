package com.dian.yunbo.ui.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dian.com.yunbo.R;
import com.dian.yunbo.model.HeadBean;
import zlc.season.practicalrecyclerview.AbstractViewHolder;

import static com.dian.yunbo.Navigation.showMore;

/**
 * Created by Seiko on 2017/3/29. Y
 */

public class HeadViewHolder extends AbstractViewHolder<HeadBean> {

    @BindView(R.id.head_title)
    TextView title;

    private Context context;
    private HeadBean bean;

    public HeadViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
    }

    @Override
    public void setData(HeadBean bean) {
        this.bean = bean;
        title.setText(bean.getTitle());
    }

    @OnClick(R.id.head_more)
    void toMore() {
        showMore(context, bean.getTitle(), bean.getType());
    }

}
