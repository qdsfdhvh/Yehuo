package com.dian.yunbo.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dian.yunbo.api.SourceApi;
import com.dian.yunbo.model.RecBean;
import com.dian.yunbo.sited.YhSource;
import com.dian.yunbo.view.SourceItemView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dian.com.yunbo.R;
import zlc.season.practicalrecyclerview.AbstractAdapter;
import zlc.season.practicalrecyclerview.AbstractViewHolder;

/**
 * Created by Seiko on 2017/4/17. Y
 */

public class InfoSearchFragment extends DialogFragment {

    @BindView(R.id.recView)
    RecyclerView recView;

    private SourceItemView mItemView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recview, container);
        ButterKnife.bind(this, view);
        setRecView();
        return view;
    }

    private void setRecView() {
        RecAdapter mAdapter = new RecAdapter();
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        recView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recView.setAdapter(mAdapter);
        List<RecBean> list = new ArrayList<>();
        for (YhSource source : SourceApi.getInstance().getSearchList()) {
            RecBean bean = new RecBean();
            bean.setName(source._search.title);
            list.add(bean);
        }
        mAdapter.addAll(list);
    }

    @OnClick(R.id.cancel)
    void Cancel() {
        onDestroyView();
    }

    public void setDataLoadCallBack(SourceItemView itemView) {
        this.mItemView = itemView;
    }

    //=================================
    public class RecAdapter extends AbstractAdapter<RecBean, RecViewHolder> {
        @Override
        protected RecViewHolder onNewCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecViewHolder(parent);
        }

        @Override
        protected void onNewBindViewHolder(RecViewHolder holder, int position) {
            holder.setData(get(position));
        }
    }

    //=================================
    public class RecViewHolder extends AbstractViewHolder<RecBean> {

        @BindView(R.id.title)
        TextView title;

        private RecBean data;

        public RecViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_rec);
            ButterKnife.bind(RecViewHolder.this, itemView);
        }

        @Override
        public void setData(RecBean data) {
            this.data = data;
            title.setText(data.getName());
            String index = SourceApi.getInstance().getSearch()._search.title;
            if (index.equals(data.getName())) {
                title.setTextColor(Color.RED);
            } else {
                title.setTextColor(Color.BLACK);
            }
        }

        @OnClick(R.id.layout)
        void toClick() {
            mItemView.run(data.getName());
            onDestroyView();
        }

    }
}
