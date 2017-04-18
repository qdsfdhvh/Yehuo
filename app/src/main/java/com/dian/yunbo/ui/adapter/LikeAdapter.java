package com.dian.yunbo.ui.adapter;

import android.view.ViewGroup;
import com.dian.yunbo.model.LikeBean;
import com.dian.yunbo.ui.viewholder.LikeViewHolder;
import zlc.season.practicalrecyclerview.AbstractAdapter;

/**
 * Created by Seiko on 2017/4/8. Y
 */

public class LikeAdapter extends AbstractAdapter<LikeBean, LikeViewHolder> {

    @Override
    protected LikeViewHolder onNewCreateViewHolder(ViewGroup parent, int viewType) {
        return new LikeViewHolder(parent, this);
    }

    @Override
    protected void onNewBindViewHolder(LikeViewHolder holder, int position) {
        holder.setData(get(position));
    }

}
