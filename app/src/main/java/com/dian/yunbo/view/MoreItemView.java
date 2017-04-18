package com.dian.yunbo.view;

import java.util.List;

import com.dian.yunbo.model.ComicBean;

/**
 * Created by Seiko on 2017/3/26. Y
 */

public interface MoreItemView {

    void onSuccess(List<ComicBean> list, int page);

    void onFailed();

}
