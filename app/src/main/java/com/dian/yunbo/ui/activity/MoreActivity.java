package com.dian.yunbo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import butterknife.BindView;
import dian.com.yunbo.R;
import com.dian.yunbo.model.ComicBean;
import com.dian.yunbo.presenter.MorePresenter;
import com.dian.yunbo.ui.BackActivity;
import com.dian.yunbo.ui.adapter.ComicAdapter;
import com.dian.yunbo.ui.fragment.PageFragment;
import com.dian.yunbo.view.MoreItemView;
import com.dian.yunbo.view.PageItemView;
import zlc.season.practicalrecyclerview.ConfigureAdapter;
import zlc.season.practicalrecyclerview.PracticalRecyclerView;

/**
 * Created by Seiko on 2017/3/28. Y
 */

public class MoreActivity extends BackActivity implements PracticalRecyclerView.OnLoadMoreListener,
        View.OnClickListener, MoreItemView, PageItemView {

    @BindView(R.id.recView)
    PracticalRecyclerView recView;

    private Button mPrevPage;
    private Button mNextPage;
    private TextView mNowPage;
    private LinearLayout mProgress;


    private ComicAdapter mAdapter;
    private MorePresenter mPresenter;
    private String type;
    private int page;

    @Override
    public int getLayoutId() {
        return R.layout.activity_more;
    }

    @Override
    public String getLayoutTitle() {
        return null;
    }

    @Override
    public void initViews(Bundle save) {
        Intent intent = this.getIntent();
        String title = intent.getStringExtra("title");
        type = intent.getStringExtra("type");
        setTitle(title);
        setConfig();
        setRecView();
        mPresenter.loadData();
    }

    private void setConfig() {
        mAdapter = new ComicAdapter();
        mPresenter = new MorePresenter(type);
        mPresenter.setDataLoadCallBack(this);
    }

    private void setRecView() {
        recView.setAutoLoadEnable(false);
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.setAdapterWithLoading(mAdapter);
        recView.setLoadMoreListener(this);
        recView.configureView(new ConfigureAdapter() {
            @Override
            public void configureLoadMoreView(View loadMoreView) {
                super.configureLoadMoreView(loadMoreView);
                mProgress = (LinearLayout) loadMoreView.findViewById(R.id.loading);
                mPrevPage = (Button) loadMoreView.findViewById(R.id.prev_page);
                mNextPage = (Button) loadMoreView.findViewById(R.id.next_page);
                mNowPage = (TextView) loadMoreView.findViewById(R.id.now_page);
                mPrevPage.setOnClickListener(MoreActivity.this);
                mNextPage.setOnClickListener(MoreActivity.this);
                mNowPage.setOnClickListener(MoreActivity.this);
            }
        });
    }

    @Override
    public void onSuccess(List<ComicBean> list, int page) {
        mAdapter.clear();
        mAdapter.addAll(list);
        recView.get().scrollToPosition(0);
        mNowPage.setText(String.valueOf("第"+ page + "页"));
        setVisibility(View.VISIBLE, View.GONE);
        this.page = page;
        if (page == 1) {
            mPrevPage.setVisibility(View.INVISIBLE);
        } else {
            mPrevPage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailed() {
        mAdapter.loadMoreFailed();
        setVisibility(View.VISIBLE, View.GONE);
    }

    @Override
    public void onLoadMore() {
        mPresenter.loadData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.prev_page:
                toTurn(-1);
                return;
            case R.id.next_page:
                toTurn(1);
                return;
            case R.id.now_page:
                PageFragment pages = new PageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("page", page + "");
                pages.setArguments(bundle);
                pages.setDataLoadCallBack(this);
                pages.show(getSupportFragmentManager(), "pagedialog");
                break;
        }
    }

    private void toTurn(final int i) {
        mPresenter.setNum(i);
        setVisibility(View.GONE, View.VISIBLE);
        //手动触发加载更多
        mAdapter.manualLoadMore();
    }

    @Override
    public void toPage(int page) {
        mPresenter.setCount(page);
        setVisibility(View.GONE, View.VISIBLE);
        //手动触发加载更多
        mAdapter.manualLoadMore();
    }

    @Override
    protected void onDestroy() {
        mPresenter.unsubscribeAll();
        super.onDestroy();
    }

    private void setVisibility(int visible1, int visible2) {
        mNowPage.setVisibility(visible1);
        mPrevPage.setVisibility(visible1);
        mNextPage.setVisibility(visible1);
        mProgress.setVisibility(visible2);
    }

}
