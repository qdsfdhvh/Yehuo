package com.dian.yunbo.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.dian.yunbo.App;
import com.dian.yunbo.model.HistBean;
import com.dian.yunbo.presenter.HistPresenter;
import com.dian.yunbo.ui.BaseFragment;
import com.dian.yunbo.ui.adapter.HistAdapter;
import com.dian.yunbo.utils.FileUtil;
import com.dian.yunbo.view.HistItemView;
import java.util.List;
import butterknife.BindView;
import dian.com.yunbo.R;
import zlc.season.practicalrecyclerview.PracticalRecyclerView;

/**
 * Created by Seiko on 2017/3/30. Y
 */

public class HistFragment extends BaseFragment implements HistItemView {

    @BindView(R.id.recView)
    PracticalRecyclerView recView;

    private HistAdapter mAdapter;
    private HistPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_hist;
    }

    @Override
    public void initViews(Bundle save) {
        setHasOptionsMenu(true);
        setConfig();
        setRecView();
        mPresenter.loadData();
    }

    private void setConfig() {
        mAdapter = new HistAdapter();
        mPresenter = new HistPresenter();
        mPresenter.setDataLoadCallBack(this);
    }

    private void setRecView() {
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recView.setAdapterWithLoading(mAdapter);
        recView.setRefreshListener(new PracticalRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadData();
            }
        });
    }

    @Override
    public void onSuccess(List<HistBean> list) {
        mAdapter.clear();
        mAdapter.addAll(list);
    }

    //=========================================================
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_hist, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                new AlertDialog.Builder(getActivity())
                        .setMessage("是否清空记录")
                        .setNegativeButton("是", (DialogInterface dif, int j) -> delAll())       //通知中间按钮
                        .setPositiveButton("否", (DialogInterface dif, int j) -> dif.dismiss())  //通知最右按钮
                        .create()
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void delAll() {
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
        FileUtil.deleteFile(App.getInstance().getHistPath());
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribeAll();
    }

}
