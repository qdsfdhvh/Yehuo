package com.dian.yunbo.view;

import java.util.List;

import com.dian.yunbo.model.SearchBean;

/**
 * Created by Seiko on 2017/3/27. Y
 */

public interface SearchItemView {

    void onSuccess(List<SearchBean> list, boolean isRef);

    void onFailed();

    void onMoreFailed();

}
