package com.dian.yunbo.ui.adapter;

import android.view.ViewGroup;
import com.dian.yunbo.model.DownBean;
import com.dian.yunbo.ui.viewholder.DownViewHolder;
import zlc.season.practicalrecyclerview.AbstractAdapter;

/**
 * Created by Seiko on 2017/4/8. Y
 */

public class DownAdapter extends AbstractAdapter<DownBean, DownViewHolder> {

    @Override
    protected DownViewHolder onNewCreateViewHolder(ViewGroup parent, int viewType) {
        return new DownViewHolder(parent, this);
    }

    @Override
    protected void onNewBindViewHolder(DownViewHolder holder, int position) {
        holder.setData(get(position));
    }
}
