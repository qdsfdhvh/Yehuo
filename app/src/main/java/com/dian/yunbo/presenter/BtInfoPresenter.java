package com.dian.yunbo.presenter;

import android.content.Context;
import android.widget.Toast;
import com.dian.yunbo.api.SourceApi;
import com.dian.yunbo.model.BtInfoBean;
import com.dian.yunbo.sited.YhNode;
import com.dian.yunbo.sited.YhSource;
import com.dian.yunbo.sited.view.ISdViewModel;
import com.dian.yunbo.sited.view.SdSourceCallback;
import com.dian.yunbo.view.BtInfoItemView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seiko on 2017/3/27. Y
 */

public class BtInfoPresenter extends BasePresenter<BtInfoItemView> implements ISdViewModel {

    private YhSource source;
    private List<BtInfoBean> list;
    private Context mContext;

    public BtInfoPresenter(Context context) {
        mContext = context;
        source = SourceApi.getInstance().getBtinfo();
        list = new ArrayList<>();
    }

    public void loadData(String hash) {
        if (source == null) {
            Toast.makeText(mContext, "未发现种子解析插件", Toast.LENGTH_SHORT).show();
            itemView.onFailed();
            return;
        }

        list.clear();
        source.getNodeViewModel(this, source._btinfo, hash, new SdSourceCallback() {
            @Override
            public void run(Integer code) {
                if (code == 1 && list.size() > 0) {
                    itemView.onSuccess(list);
                } else {
                    itemView.onFailed();
                }
            }
        });
    }

    public void setSource(String type) {
        source = SourceApi.getInstance().getBtinfo(type);
    }

    @Override
    public void loadByJson(YhNode config, String... jsons) {
        for (String json : jsons) {
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement el : array) {
                JsonObject n = el.getAsJsonObject();
                String name = getString(n, "name");
                String size = getString(n, "size");
                String data = getString(n, "data");
                String url = getString(n, "url");

                BtInfoBean bean = new BtInfoBean();
                bean.setName(name);
                bean.setSize(size);
                bean.setData(data);
                bean.setUrl(url);
                list.add(bean);
            }
        }
    }
}
