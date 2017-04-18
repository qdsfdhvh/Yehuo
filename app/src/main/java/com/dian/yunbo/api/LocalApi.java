package com.dian.yunbo.api;

import com.dian.yunbo.App;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seiko on 2017/3/28. Y
 */

public class LocalApi {

    private static LocalApi instance;
    private List<Comics> comics;

    public static LocalApi getInstance() {
        if (instance == null) {
            synchronized (LocalApi.class) {
                if (instance == null) {
                    instance = new LocalApi();
                }
            }
        }
        return instance;
    }

    private LocalApi() {
        comics = new ArrayList<>();
        for (String asset : getAssets()) {
            if (asset.contains("json")) {
                comics.add(new Comics(asset));
            }
        }
    }

    //===================================
    /** 获得某个Comics */
    public Comics getComic(final String type) {
        for (Comics comic : comics) {
            if (type.equals(comic.getID())) {
                return comic;
            }
        }
        return null;
    }

    public List<Comics> getComics() {
        return comics;
    }

    /* 读取assets文件夹下所有的json文件 */
    private String[] getAssets() {
        try {
            return App.getContext().getAssets().list("");
        }catch (Exception e) {
            e.printStackTrace();
            return new String[] {};
        }
    }
}
