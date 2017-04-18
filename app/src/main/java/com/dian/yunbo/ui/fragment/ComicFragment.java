package com.dian.yunbo.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import com.dian.yunbo.presenter.ComicPresenter;
import com.dian.yunbo.ui.BaseFragment;
import com.dian.yunbo.ui.adapter.MainAdapter;
import com.dian.yunbo.view.ComicItemView;
import java.util.List;
import butterknife.BindView;
import dian.com.yunbo.R;
import zlc.season.practicalrecyclerview.ItemType;
import zlc.season.practicalrecyclerview.PracticalRecyclerView;

/**
 * Created by Seiko on 2017/3/30. Y
 */

public class ComicFragment extends BaseFragment implements ComicItemView {

    @BindView(R.id.recView)
    PracticalRecyclerView recView;

    private MainAdapter mAdapter;
    private ComicPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_comic;
    }

    @Override
    public void initViews(Bundle save) {
        setConfig();
        setRecView();
        mPresenter.loadData();
    }

    private void setConfig() {
        mAdapter = new MainAdapter(getActivity());
        mPresenter = new ComicPresenter();
        mPresenter.setDataLoadCallBack(this);
    }

    private void setRecView() {
        recView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recView.setAdapterWithLoading(mAdapter);
        recView.setRefreshListener(new PracticalRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.clear();
                mPresenter.loadData();
            }
        });
    }

    @Override
    public void onSuccess(List<ItemType> list) {
        mAdapter.addAll(list);
    }

    @Override
    public void onFailed() {
        mAdapter.showError();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribeAll();
    }

}
