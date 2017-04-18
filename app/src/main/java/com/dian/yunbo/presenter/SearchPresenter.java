package com.dian.yunbo.presenter;

import android.content.Context;
import android.widget.Toast;
import com.dian.yunbo.api.SourceApi;
import com.dian.yunbo.model.SearchBean;
import com.dian.yunbo.sited.view.ISdViewModel;
import com.dian.yunbo.sited.view.SdSourceCallback;
import com.dian.yunbo.sited.YhNode;
import com.dian.yunbo.sited.YhSource;
import com.dian.yunbo.view.SearchItemView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seiko on 2017/3/27. Y
 */

public class SearchPresenter extends BasePresenter<SearchItemView> implements ISdViewModel {

    private int page;
    private String keyword;
    private YhSource source;
    private List<SearchBean> list;
    private Context mContext;

    public SearchPresenter(Context context) {
        mContext = context;
        source = SourceApi.getInstance().getSearch();
        list = new ArrayList<>();
    }

    public void loadData(String keyword) {
        this.keyword = keyword;
        this.page = 0;
        loadData(true);
    }

    public void loadData() {
        this.page = 0;
        loadData(true);
    }

    public void loadData(boolean isRef) {
        page++;
        list.clear();

        if (source == null) {
            Toast.makeText(mContext, "未发现搜索插件", Toast.LENGTH_SHORT).show();
            return;
        }

        source.getNodeViewModel(this, source._search, keyword, page, new SdSourceCallback() {
            @Override
            public void run(Integer code) {
                if (code == 1) {
                    itemView.onSuccess(list, isRef);
                } else if (!isRef){
                    itemView.onMoreFailed();
                } else {
                    itemView.onFailed();
                }
            }
        });
    }

    public void setSource(String type) {
        source = SourceApi.getInstance().getSearch(type);
    }

    @Override
    public void loadByJson(YhNode config, String... jsons) {
        for (String json : jsons) {
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement el : array) {
                JsonObject n = el.getAsJsonObject();
                String name = getString(n,"name");
                String hash = getString(n,"hash");

                SearchBean bean = new SearchBean();
                bean.setTitle(name);
                bean.setHash(hash);
                list.add(bean);
            }
        }
    }

    public boolean isSearched() {
        return keyword != null;
    }
}
