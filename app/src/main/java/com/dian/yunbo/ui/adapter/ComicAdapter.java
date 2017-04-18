package com.dian.yunbo.ui.adapter;

import android.view.ViewGroup;

import com.dian.yunbo.ui.viewholder.ComicViewHolder;
import com.dian.yunbo.model.ComicBean;
import zlc.season.practicalrecyclerview.AbstractAdapter;

/**
 * Created by Seiko on 2017/3/26. Y
 */

public class ComicAdapter extends AbstractAdapter<ComicBean, ComicViewHolder> {
    @Override
    protected ComicViewHolder onNewCreateViewHolder(ViewGroup parent, int viewType) {
        return new ComicViewHolder(parent);
    }

    @Override
    protected void onNewBindViewHolder(ComicViewHolder holder, int position) {
        holder.setData(get(position));
    }
}
