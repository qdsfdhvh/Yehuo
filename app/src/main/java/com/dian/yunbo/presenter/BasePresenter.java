package com.dian.yunbo.presenter;

import com.google.gson.JsonObject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Seiko on 2017/3/28. Y
 */

class BasePresenter<T> {

    CompositeDisposable mDisposables;
    T itemView;

    public void setDataLoadCallBack(T itemView) {
        this.itemView = itemView;
    }

    public void unsubscribeAll() {
        if (mDisposables != null) {
            mDisposables.clear();
        }
    }

    protected String getString(JsonObject data, String key) {
        return data.get(key) != null ? data.get(key).getAsString():"";
    }

    protected int getInt(JsonObject data, String key) {
        return data.get(key) != null ? data.get(key).getAsInt():0;
    }

}
