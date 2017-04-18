package com.dian.yunbo.ui.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.dian.yunbo.glide.ImageLoader;
import dian.com.yunbo.R;
import com.dian.yunbo.model.ComicBean;
import zlc.season.practicalrecyclerview.AbstractViewHolder;

import static com.dian.yunbo.Navigation.showBtInfo;

/**
 * Created by Seiko on 2017/3/26. Y
 */

public class ComicViewHolder extends AbstractViewHolder<ComicBean> {

    @BindView(R.id.name)
    TextView tv;
    @BindView(R.id.logo)
    ImageView iv;

    private Context context;
    private ComicBean bean;

    public ComicViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_comic);
        ButterKnife.bind(this, itemView);
        context = parent.getContext();
    }

    @Override
    public void setData(ComicBean bean) {
        this.bean = bean;
        ImageLoader.load(context, iv, bean.getLogo());
        tv.setText(bean.getName());
    }

    @OnClick(R.id.layout)
    void toHash() {
        showBtInfo(context, bean.getName(), bean.getHash());
    }
}
