package com.dian.yunbo.ui.adapter;

import android.view.ViewGroup;
import com.dian.yunbo.model.SearchBean;
import com.dian.yunbo.ui.viewholder.SearchViewHolder;
import zlc.season.practicalrecyclerview.AbstractAdapter;

/**
 * Created by Seiko on 2017/3/26. Y
 */

public class SearchAdapter extends AbstractAdapter<SearchBean, SearchViewHolder> {

    @Override
    protected SearchViewHolder onNewCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchViewHolder(parent);
    }

    @Override
    protected void onNewBindViewHolder(SearchViewHolder holder, int position) {
        holder.setData(get(position));
    }

}
