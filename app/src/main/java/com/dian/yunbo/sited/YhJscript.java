package com.dian.yunbo.sited;

import android.app.Application;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import com.dian.yunbo.sited.view.HttpCallback;
import org.w3c.dom.Element;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import dian.com.yunbo.R;

/**
 * Created by Seiko on 2017/4/15. Y
 */

class YhJscript {

    private String code;
    private YhSource source;
    private YhNode require;

    YhJscript(YhSource source, Element node) {
        this.source = source;
        if (node != null) {
            code = Util.getElement(node, "code").getTextContent();
            require = new YhNode(source).buildForNode(Util.getElement(node, "require"));
        }
    }

    void loadJs(final Application app, final JsEngine js) {
        if (!require.isEmpty()) {
            for (YhNode n1 : require.items()) {
                //1.如果本地可以加载并且没有出错
                if (!TextUtils.isEmpty(n1.lib)) {
                    if (loadLib(app, js, n1.lib))
                        continue;
                }

                //2.尝试网络加载
                Log.v("SdJscript", n1.url);

                HttpMessage msg = new HttpMessage(n1, n1.url);
                msg.callback = new HttpCallback() {
                    @Override
                    public void run(Integer code, HttpMessage msg, String text) {
                        if (code == 1) {
                            js.loadJs(text);
                        }
                    }
                };
                Util.http(source, msg);
            }
        }

        if (!TextUtils.isEmpty(code)) {
            js.loadJs(code);
        }
    }

    private boolean loadLib(Application app, JsEngine js, String lib) {
        //for debug
        Resources asset = app.getResources();

        switch (lib) {
            case "md5":
                return tryLoadLibItem(asset, R.raw.md5, js);
            case "sha1":
                return tryLoadLibItem(asset, R.raw.sha1, js);
            case "base64":
                return tryLoadLibItem(asset, R.raw.base64, js);
            case "cheerio":
                return tryLoadLibItem(asset, R.raw.cheerio, js);
            default:
                return false;
        }
    }

    private boolean tryLoadLibItem(Resources asset, int resID, JsEngine js) {
        try {
            InputStream is = asset.openRawResource(resID);
            BufferedReader in = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String code = doToString(in);
            js.loadJs(code);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private String doToString(BufferedReader in) throws IOException {
        StringBuilder buffer = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }
}
