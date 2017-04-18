package com.dian.yunbo.view;

import java.util.List;

import com.dian.yunbo.model.BtInfoBean;

/**
 * Created by Seiko on 2017/3/27. Y
 */

public interface BtInfoItemView {

    void onSuccess(List<BtInfoBean> list);

    void onFailed();

}
