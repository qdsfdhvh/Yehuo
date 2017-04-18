package com.dian.yunbo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dian.com.yunbo.R;
import com.dian.yunbo.model.RecyclerItemType;
import com.dian.yunbo.model.ComicBean;
import com.dian.yunbo.model.HeadBean;
import com.dian.yunbo.ui.viewholder.HeadViewHolder;
import com.dian.yunbo.ui.viewholder.MainViewHolder;
import zlc.season.practicalrecyclerview.AbstractAdapter;
import zlc.season.practicalrecyclerview.AbstractViewHolder;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by Seiko on 2017/3/29. Y
 */

public class MainAdapter extends AbstractAdapter<ItemType, AbstractViewHolder> {

    private LayoutInflater inflater;

    public MainAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    protected AbstractViewHolder onNewCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RecyclerItemType.NORMAL.getValue()) {
            return new MainViewHolder(parent);
        } else if (viewType == RecyclerItemType.TYPE1.getValue()) {
            View head = inflater.inflate(R.layout.item_head, parent, false);
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) head.getLayoutParams();
            params.setFullSpan(true);
            head.setLayoutParams(params);
            return new HeadViewHolder(head);
        }
        return null;
    }

    @Override
    protected void onNewBindViewHolder(AbstractViewHolder holder, int position) {
        if (holder instanceof  MainViewHolder) {
            ((MainViewHolder) holder).setData((ComicBean) get(position));
        } else if (holder instanceof HeadViewHolder) {
            ((HeadViewHolder) holder).setData((HeadBean) get(position));
        }
    }

}
