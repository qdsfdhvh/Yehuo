package com.dian.yunbo.sited;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import com.dian.yunbo.sited.view.HttpCallback;
import com.dian.yunbo.sited.view.ISdViewModel;
import com.dian.yunbo.sited.view.SdSourceCallback;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import okhttp3.Call;

import static com.dian.yunbo.sited.Util.log;

/**
 * Created by Seiko on 2017/4/15. Y
 */

public class YhSource {

    public String title; //标题
    private Element root;
    private String _encode;//编码
    private String _ua;
    private String _cookies;

    public YhNode _comics;
    public YhNode _search;
    public YhNode _btinfo;
    public YhNode _result;

    private JsEngine js;
    public Call mCall;

    public YhSource(Application app, String xml) throws Exception {
        doInit(xml);
        doLoad(app);
    }

    private void doInit(String xml) throws Exception {
        YhAttrList attrs = new YhAttrList();
        root = Util.getXmlroot(xml);

        {
            NamedNodeMap temp = root.getAttributes();
            for (int i = 0, len = temp.getLength(); i < len; i++) {
                Node p = temp.item(i);
                attrs.set(p.getNodeName(), p.getNodeValue());
            }
        }

        {
            NodeList temp = root.getChildNodes();
            for (int i = 0, len = temp.getLength(); i < len; i++) {
                Node p = temp.item(i);

                if (p.getNodeType() == Node.ELEMENT_NODE && !p.hasAttributes() && p.hasChildNodes()) {
                    if(p.getChildNodes().getLength()==1) {
                        Node p2 = p.getFirstChild();
                        if (p2.getNodeType() == Node.TEXT_NODE) {
                            attrs.set(p.getNodeName(), p2.getNodeValue());
                        }
                    }
                }
            }
        }
    }

    private void doLoad(Application app) {
        String xmlMetaName = "meta";
        String xmlMainName = "main";
        String xmlScriptName = "script";
        //1.head
        YhNodeSet meta = new YhNodeSet(this).buildForNode(Util.getElement(root, xmlMetaName));
        YhNodeSet main = new YhNodeSet(this).buildForNode(Util.getElement(root, xmlMainName));
        title = meta.attrs.getString("title");
        _encode = meta.attrs.getString("encode");
        _ua = meta.attrs.getString("ua");
        //1.body
        _comics = main.get("comics");
        _search = main.get("search");
        _btinfo = main.get("btinfo");
        _result = main.get("result");
        //3.script :: 放后面
        js = new JsEngine(app, this);
        YhJscript script = new YhJscript(this, Util.getElement(root, xmlScriptName));
        script.loadJs(app, js);
        root = null;
    }

    public String parse(YhNode cfg, String url, String html) {
        log("parse-url", url);

        if (!TextUtils.isEmpty(cfg._enckey)) {
            html = Util.EncDecode(cfg._enckey, html);
        }
        log("parse-html", html == null ? "null" : html);

        if (TextUtils.isEmpty(cfg.parse) || "@null".equals(cfg.parse)) {
            return html;
        }
        String temp = js.callJs(cfg.parse, url, html);
        log("parse-json", temp == null ? "null" + "\r\n\n" : temp + "\r\n\n");

        return temp;
    }

    private String getUrl(YhNode cfg, String key, String page) {
        if (TextUtils.isEmpty(cfg.buildUrl)) {
            return cfg.url;
        } else {
            return js.callJs(cfg.buildUrl, cfg.url, key, page);
        }
    }

    //====================================
    public void getNodeViewModel2(ISdViewModel viewModel, YhNode cfg, String url, SdSourceCallback callback) {
        doGetNodeViewModel(viewModel, cfg, url, callback);
    }

    public void getNodeViewModel(ISdViewModel viewModel, YhNode cfg, String key, int page, SdSourceCallback callback) {
        String url = getUrl(cfg, key, page + "");
        url = url.replace("@key", Util.urlEncode(key, cfg.encode()));
        url = url.replace("@page", page + "");
        doGetNodeViewModel(viewModel, cfg, url, callback);
    }

    public void getNodeViewModel(ISdViewModel viewModel, YhNode cfg, String key, SdSourceCallback callback) {
        String url = getUrl(cfg, key, null);
        url = url.replace("@key", Util.urlEncode(key, cfg.encode()));
        doGetNodeViewModel(viewModel, cfg, url, callback);
    }

    private void doGetNodeViewModel(final ISdViewModel viewModel, final YhNode cfg, final String url, final SdSourceCallback callback) {
        if (TextUtils.isEmpty(url)) {
            callback.run(-3);
            return;
        }

        if (mCall != null) {
            mCall.cancel();
        }

        final HttpMessage msg = new HttpMessage();
        msg.url = url;
        msg.rebuild(cfg);
        msg.callback = new HttpCallback() {
            @Override
            public void run(Integer code, HttpMessage sender, String text) {
                if (code == 1) {
                    doParse_hasAddin(viewModel, cfg, msg.url, text);
                }
                callback.run(code);
            }
        };
        Util.http(this, msg);
    }

    private void doParse_hasAddin(ISdViewModel viewModel, YhNode cfg,  String url, String text) {
        String json = this.parse(cfg, url, text);
        if (json != null) {
            viewModel.loadByJson(cfg, json);
        }
    }

    //========================================
    public String cookies() {
        return _cookies;
    }

    public void setCookies(String cookies) {
        this._cookies = cookies;
    }

    public String ua() {
        if (TextUtils.isEmpty(_ua)) {
            return Util.defUA;
        } else {
            return _ua;
        }
    }

    public String encode() {
        return _encode;
    }

    //==================================
    public boolean isComics() {
        return _comics != null;
    }

    public boolean isSearch() {
        return _search != null;
    }

    public boolean isBtInfo() {
        return _btinfo != null;
    }

}
