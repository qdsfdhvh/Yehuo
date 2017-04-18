package com.dian.yunbo.view;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by Seiko on 2017/3/30. Y
 */

public interface ComicItemView {

    void onSuccess(List<ItemType> list);

    void onFailed();

}
