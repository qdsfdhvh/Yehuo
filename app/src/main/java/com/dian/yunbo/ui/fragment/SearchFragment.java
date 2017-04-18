package com.dian.yunbo.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.dian.yunbo.model.SearchBean;
import com.dian.yunbo.presenter.SearchPresenter;
import com.dian.yunbo.ui.BaseFragment;
import com.dian.yunbo.ui.adapter.SearchAdapter;
import com.dian.yunbo.utils.StringUtil;
import com.dian.yunbo.view.SearchItemView;
import com.dian.yunbo.view.SourceItemView;

import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import dian.com.yunbo.R;
import zlc.season.practicalrecyclerview.PracticalRecyclerView;
import static com.dian.yunbo.Navigation.showBtInfo;
import static com.dian.yunbo.Navigation.showDown;

/**
 * Created by Seiko on 2017/3/30. Y
 */

public class SearchFragment extends BaseFragment implements
        PracticalRecyclerView.OnLoadMoreListener,SearchItemView, SourceItemView {

    @BindView(R.id.search_text_layout)
    TextInputLayout mInputLayout;
    @BindView(R.id.search_keyword_input)
    AppCompatAutoCompleteTextView mEditText;
    @BindView(R.id.search_action_button)
    FloatingActionButton mActionButton;
    @BindView(R.id.recView)
    PracticalRecyclerView recView;
    @BindView(R.id.custom_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.load_error)
    TextView load_error;

    private SearchAdapter mAdapter;
    private SearchPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    public void initViews(Bundle save) {
        setHasOptionsMenu(true);
        progressBar.setVisibility(View.GONE);
        setConfig();
        setRecView();
    }

    private void setConfig() {
        mAdapter = new SearchAdapter();
        mPresenter = new SearchPresenter(getActivity());
        mPresenter.setDataLoadCallBack(this);
        /* 激活搜索按钮 */
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mActionButton != null && !mActionButton.isShown()) {
                    mActionButton.show();
                }
            }
        });
        /* 监听回车键 */
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mActionButton.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    private void setRecView() {
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recView.setAdapterWithLoading(mAdapter);
        recView.setLoadMoreListener(this);
    }

    @Override
    public void onLoadMore() {
        mPresenter.loadData(false);
    }

    @Override
    public void onSuccess(List<SearchBean> list, boolean isRef) {
        load_error.setVisibility(View.GONE);
        if (isRef) {
            progressBar.setVisibility(View.GONE);
            mAdapter.clear();
            recView.get().scrollToPosition(0);
        }
        mAdapter.addAll(list);
    }

    @Override
    public void onFailed() {
        progressBar.setVisibility(View.GONE);
        load_error.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMoreFailed() {
        mAdapter.loadMoreFailed();
    }

    @OnClick(R.id.search_action_button)
    void onSearchButtonClick() {
        String text = mEditText.getText().toString();
        if (TextUtils.isEmpty(text)) {
            mInputLayout.setError("内容不能为空");
        } else {
            mInputLayout.setError("");
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(),0);

            if (text.contains("magnet") || StringUtil.isChar(text)) {
                showBtInfo(getActivity(), text);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                mPresenter.loadData(text);
            }
        }
    }

    //=========================================
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_down:
                showDown(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab_button)
    void setSource() {
        InfoSearchFragment recFragment = new InfoSearchFragment();
        recFragment.setDataLoadCallBack(this);
        recFragment.show(((AppCompatActivity) getActivity()).getSupportFragmentManager(), "recSearch");
    }

    @Override
    public void run(String type) {
        mPresenter.setSource(type);
        if (mPresenter.isSearched()) {
            progressBar.setVisibility(View.VISIBLE);
            mAdapter.clear();
            mPresenter.loadData();
        }
    }

}
