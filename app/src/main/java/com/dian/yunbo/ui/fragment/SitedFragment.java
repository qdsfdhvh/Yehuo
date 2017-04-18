package com.dian.yunbo.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.dian.yunbo.api.LocalApi;
import com.dian.yunbo.api.SourceApi;
import com.dian.yunbo.model.HeadBean;
import com.dian.yunbo.model.SitedBean;
import com.dian.yunbo.sited.YhSource;
import com.dian.yunbo.ui.BaseFragment;
import com.dian.yunbo.ui.adapter.SitedAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import dian.com.yunbo.R;
import zlc.season.practicalrecyclerview.ItemType;
import zlc.season.practicalrecyclerview.PracticalRecyclerView;

/**
 * Created by Seiko on 2017/4/15. Y
 */

public class SitedFragment extends BaseFragment {

    @BindView(R.id.recView)
    PracticalRecyclerView recView;

    private SitedAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sited;
    }

    @Override
    public void initViews(Bundle save) {
        mAdapter = new SitedAdapter(getActivity());
        recView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recView.setAdapterWithLoading(mAdapter);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<ItemType> list = new ArrayList<>();
                add(list, "漫画浏览", SourceApi.getInstance().getComicsList());
                add(list, "磁力搜索", SourceApi.getInstance().getSearchList());
                add(list, "种子信息", SourceApi.getInstance().getBtinfoList());
                mAdapter.addAll(list);
            }
        }, 500);

    }

    private void add(List<ItemType> list, String title, List<YhSource> sources) {
        if (sources != null && sources.size() > 0) {
            list.add(new HeadBean(title));
            for (YhSource source : sources) {
                SitedBean bean = new SitedBean();
                bean.setName(source.title);
                list.add(bean);
            }
        }
    }


}
