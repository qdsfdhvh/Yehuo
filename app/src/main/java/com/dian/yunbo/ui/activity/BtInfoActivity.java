package com.dian.yunbo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import dian.com.yunbo.R;
import com.dian.yunbo.model.BtInfoBean;
import com.dian.yunbo.model.LikeBean;
import com.dian.yunbo.presenter.BtInfoPresenter;
import com.dian.yunbo.ui.BackActivity;
import com.dian.yunbo.ui.adapter.BtInfoAdapter;
import com.dian.yunbo.ui.fragment.InfoRecFragment;
import com.dian.yunbo.utils.FileUtil;
import com.dian.yunbo.view.BtInfoItemView;
import com.dian.yunbo.view.SourceItemView;

import zlc.season.practicalrecyclerview.PracticalRecyclerView;

/**
 * Created by Seiko on 2017/3/26. Y
 */

public class BtInfoActivity extends BackActivity implements BtInfoItemView, SourceItemView {

    @BindView(R.id.recView)
    PracticalRecyclerView recView;
    @BindView(R.id.custom_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.load_error)
    TextView load_error;

    private BtInfoAdapter mAdapter;
    private BtInfoPresenter mPresenter;
    private String hash;
    private String name;

    @Override
    public int getLayoutId() {
        return R.layout.activity_btinfo;
    }

    @Override
    public String getLayoutTitle() {
        return "种子信息";
    }

    @Override
    public void initViews(Bundle save) {
        Intent intent = this.getIntent();
        name = intent.getStringExtra("name");
        hash = intent.getStringExtra("hash");
        setConfig();
        setRecView();
        loadData();
    }

    private void setConfig() {
        mAdapter = new BtInfoAdapter();
        mPresenter = new BtInfoPresenter(this);
        mPresenter.setDataLoadCallBack(this);
    }

    private void setRecView() {
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recView.setAdapter(mAdapter);
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        load_error.setVisibility(View.GONE);
        mPresenter.loadData(hash);
    }

    @Override
    public void onSuccess(List<BtInfoBean> list) {
        progressBar.setVisibility(View.GONE);
        mAdapter.addAll(list);
    }

    @Override
    public void onFailed() {
        progressBar.setVisibility(View.GONE);
        load_error.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @OnClick(R.id.fab_button)
    void setSource() {
        InfoRecFragment recFragment = new InfoRecFragment();
        recFragment.setDataLoadCallBack(this);
        recFragment.show(getSupportFragmentManager(), "recDialog");
    }

    @Override
    public void run(String type) {
        mPresenter.setSource(type);
        mAdapter.clear();
        loadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_like:
                //添加收藏
                if (FileUtil.save(new LikeBean(name, hash))) {
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
