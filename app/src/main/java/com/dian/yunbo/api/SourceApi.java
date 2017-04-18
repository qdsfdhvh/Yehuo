package com.dian.yunbo.api;

import com.dian.yunbo.App;
import com.dian.yunbo.sited.YhSource;
import com.dian.yunbo.utils.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seiko on 2017/4/15. Y
 */

public class SourceApi {

    //===============================================
    private static SourceApi instance;
    private final List<YhSource> _comicsList;
    private final List<YhSource> _searchList;
    private final List<YhSource> _btinfoList;
    private YhSource btinfo;
    private YhSource search;

    public static SourceApi getInstance() {
        if (instance == null) {
            synchronized (SourceApi.class) {
                if (instance == null) {
                    instance = new SourceApi();
                }
            }
        }
        return instance;
    }

    private SourceApi() {
        _comicsList = new ArrayList<>();
        _searchList = new ArrayList<>();
        _btinfoList = new ArrayList<>();
        File sites = new File(App.getInstance().getSitePath());
        if (sites.exists()) {
            for (File file : sites.listFiles()) {
                String sited = FileUtil.readTextFromSDcard(file.getPath());
                load(sited, false);
            }
        }
    }

    private void addSource(YhSource source, boolean isUpdate) {
        if (source != null) {
            if (source.isComics()) {
                toAdd(source, _comicsList, isUpdate);
            }
            if (source.isSearch()) {
                toAdd(source, _searchList, isUpdate);
            }
            if (source.isBtInfo()) {
                toAdd(source, _btinfoList, isUpdate);
            }
        }
    }

    private void toAdd(YhSource source, List<YhSource> list, boolean isUpdate) {
        if (list.size() > 0 && isUpdate) {
            for (YhSource s : list) {
                if (source.title.equals(s.title)) {
                    list.remove(s);
                }
            }
        }
        list.add(source);
    }

    public YhSource load(final String sited, final boolean isUpdate) {
        YhSource source = parse(sited);
        addSource(source, isUpdate);
        return source;
    }

    private YhSource parse(final String sited) {
        try {
            return new YhSource(App.getInstance(), sited);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<YhSource> getComicsList() {
        return _comicsList;
    }
    public List<YhSource> getSearchList() {
        return _searchList;
    }
    public List<YhSource> getBtinfoList() {
        return _btinfoList;
    }

    //==================================================
    public YhSource getSearch() {
        if (search == null) {
            if (_searchList != null && _searchList.size() > 0) {
                search = _searchList.get(0);
            }
        }
        return search;
    }

    public YhSource getSearch(String tpye) {
        for (YhSource s : _searchList) {
            if (tpye.equals(s._search.title)) {
                search = s;
                return search;
            }
        }
        return null;
    }

    //==================================================
    public YhSource getBtinfo() {
        if (btinfo == null) {
            if (_btinfoList != null && _btinfoList.size() > 0) {
                btinfo = _btinfoList.get(0);
            }
        }
        return btinfo;
    }

    public YhSource getBtinfo(String tpye) {
        for (YhSource s : _btinfoList) {
            if (tpye.equals(s._btinfo.title)) {
                btinfo = s;
                return btinfo;
            }
        }
        return null;
    }

}
