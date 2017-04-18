package com.dian.yunbo.sited;

import android.text.TextUtils;

import com.dian.yunbo.sited.view.HttpCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Seiko on 2017/4/15. Y
 */

public class HttpMessage {

    public Map<String, String> header = new HashMap<>();
    public Map<String, String> form = new HashMap<>();

    public String url;
    public HttpCallback callback;
    public YhNode config;

    //可由cfg实始化
    public String encode;
    public String ua;
    public String method;

    HttpMessage() {

    }

    HttpMessage(YhNode cfg, String url) {
        this.config = cfg;
        this.url = url;
        rebuild(null);
    }

    void rebuild(YhNode cfg) {
        if (cfg != null) {
            this.config = cfg;
        }

        ua = config.ua();
        encode = config.encode();
        method = config.method();

        if (!TextUtils.isEmpty(config.addHeader)) {
            String[] kv2 = config.addHeader.split(":");
            header.put(kv2[0], kv2[1]);
        }
    }

}
