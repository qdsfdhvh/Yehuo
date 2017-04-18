package com.dian.yunbo.ui.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dian.com.yunbo.R;
import com.dian.yunbo.model.BtInfoBean;
import com.dian.yunbo.model.SearchBean;
import zlc.season.practicalrecyclerview.AbstractViewHolder;
import static com.dian.yunbo.Navigation.showBtInfo;
import static com.dian.yunbo.Navigation.showVideo;

/**
 * Created by Seiko on 2017/3/26. Y
 */

public class SearchViewHolder extends AbstractViewHolder<SearchBean> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.size)
    TextView size;
    @BindView(R.id.day)
    TextView day;

    private Context context;
    private SearchBean bean;

    public SearchViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_search);
        ButterKnife.bind(this, itemView);
        context = parent.getContext();
    }

    @Override
    public void setData(SearchBean bean) {
        this.bean = bean;
        title.setText(bean.getTitle());
        size.setText(bean.getSize());
        day.setText(bean.getDay());
    }

    @OnClick(R.id.layout)
    void toHash() {
        showBtInfo(context, bean.getTitle(), bean.getHash());
    }
}
