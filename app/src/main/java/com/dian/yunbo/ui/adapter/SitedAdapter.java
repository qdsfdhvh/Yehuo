package com.dian.yunbo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dian.yunbo.model.HeadBean;
import com.dian.yunbo.model.RecyclerItemType;
import com.dian.yunbo.model.SitedBean;
import com.dian.yunbo.ui.viewholder.SitedHeadViewHolder;
import com.dian.yunbo.ui.viewholder.SitedViewHolder;
import dian.com.yunbo.R;
import zlc.season.practicalrecyclerview.AbstractAdapter;
import zlc.season.practicalrecyclerview.AbstractViewHolder;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by Seiko on 2017/4/15. Y
 */

public class SitedAdapter extends AbstractAdapter<ItemType, AbstractViewHolder> {

    private LayoutInflater inflater;

    public SitedAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    protected AbstractViewHolder onNewCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RecyclerItemType.NORMAL.getValue()) {
            return new SitedViewHolder(parent);
        } else if (viewType == RecyclerItemType.TYPE1.getValue()) {
            View head = inflater.inflate(R.layout.item_sited_head, parent, false);
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) head.getLayoutParams();
            params.setFullSpan(true);
            head.setLayoutParams(params);
            return new SitedHeadViewHolder(head);
        }
        return null;
    }

    @Override
    protected void onNewBindViewHolder(AbstractViewHolder holder, int position) {
        if (holder instanceof SitedViewHolder) {
            ((SitedViewHolder) holder).setData((SitedBean) get(position));
        } else if (holder instanceof SitedHeadViewHolder) {
            ((SitedHeadViewHolder) holder).setData((HeadBean) get(position));
        }
    }
}
