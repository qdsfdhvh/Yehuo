package com.dian.yunbo.ui.adapter;

import android.view.ViewGroup;
import com.dian.yunbo.model.HistBean;
import com.dian.yunbo.ui.viewholder.HistViewHolder;
import zlc.season.practicalrecyclerview.AbstractAdapter;

/**
 * Created by Seiko on 2017/3/29. Y
 */

public class HistAdapter extends AbstractAdapter<HistBean, HistViewHolder> {

    @Override
    protected HistViewHolder onNewCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistViewHolder(parent);
    }

    @Override
    protected void onNewBindViewHolder(HistViewHolder holder, int position) {
        holder.setData(get(position));
    }

}
