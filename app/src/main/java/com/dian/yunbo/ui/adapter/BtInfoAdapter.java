package com.dian.yunbo.ui.adapter;

import android.view.ViewGroup;

import com.dian.yunbo.model.BtInfoBean;
import com.dian.yunbo.ui.viewholder.BtInfoViewHolder;
import zlc.season.practicalrecyclerview.AbstractAdapter;

/**
 * Created by Seiko on 2017/3/26. Y
 */

public class BtInfoAdapter extends AbstractAdapter<BtInfoBean, BtInfoViewHolder> {
    @Override
    protected BtInfoViewHolder onNewCreateViewHolder(ViewGroup parent, int viewType) {
        return new BtInfoViewHolder(parent);
    }

    @Override
    protected void onNewBindViewHolder(BtInfoViewHolder holder, int position) {
        holder.setData(get(position));
    }
}
