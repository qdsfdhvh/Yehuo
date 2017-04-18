package com.dian.yunbo.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import com.dian.yunbo.model.DownBean;
import com.dian.yunbo.presenter.DownPresenter;
import com.dian.yunbo.ui.BackActivity;
import com.dian.yunbo.ui.adapter.DownAdapter;
import com.dian.yunbo.view.DownItemView;
import java.util.List;
import butterknife.BindView;
import dian.com.yunbo.R;
import zlc.season.practicalrecyclerview.PracticalRecyclerView;

/**
 * Created by Seiko on 2017/4/8. Y
 */

public class DownActivity extends BackActivity implements DownItemView {

    @BindView(R.id.recView)
    PracticalRecyclerView recView;

    private DownAdapter mAdapter;
    private DownPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_down;
    }

    @Override
    public String getLayoutTitle() {
        return "已下载";
    }

    @Override
    public void initViews(Bundle save) {
        setConfig();
        setRecView();
        mPresenter.loadData();
    }

    private void setConfig() {
        mAdapter = new DownAdapter();
        mPresenter = new DownPresenter();
        mPresenter.setDataLoadCallBack(this);
    }

    private void setRecView() {
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recView.setAdapterWithLoading(mAdapter);
        recView.setRefreshListener(new PracticalRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadData();
            }
        });
    }

    @Override
    public void onSuccess(List<DownBean> list) {
        mAdapter.clear();
        mAdapter.addAll(list);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribeAll();
    }

}
