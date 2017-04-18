package com.dian.yunbo.sited;

import android.text.TextUtils;
import com.dian.yunbo.sited.view.YhView;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seiko on 2017/4/15. Y
 */

public class YhNode implements YhView {

    private YhAttrList attrs;
    private YhSource source;
    private boolean _isEmpty;

    public String name; //节点名称
    public String url;
    public String title; //标题
    public String lib;
    //parse
    public String parse;
    public String parseUrl; //解析出真正在请求的Url
    //http
    private String method;   //http method
    private String _encode;  //http 编码
    private String _ua;      //http ua
    public String _enckey;  //html需要enc解码
    //build
    public String buildArgs;
    public String buildUrl;
    public String buildRef;
    public String buildHeader;
    public String addHeader;

    private List<YhNode> _items; //下属项目
    private List<YhNode> _adds;  //下属数据节点

    public YhNode(YhSource source) {
        this.source = source;
        attrs = new YhAttrList();
    }

    public YhNode buildForNode(Element cfg) {
        _isEmpty = (cfg == null);


        if (cfg != null) {
            SetNodeMap(cfg);

            this.name   = cfg.getTagName();
            this.url    = attrs.getString("url");
            this.title  = attrs.getString("title");
            this.lib     = attrs.getString("lib");
            this.method = attrs.getString("method","get");
            this.parse  = attrs.getString("parse");
            this.parseUrl = attrs.getString("parseUrl");

            this._enckey = attrs.getString("enckey");
            this._encode = attrs.getString("encode");
            this._ua     = attrs.getString("ua");

            this.buildArgs   = attrs.getString("buildArgs");
            this.buildRef    = attrs.getString("buildRef");
            this.buildUrl    = attrs.getString("buildUrl");
            this.buildHeader = attrs.getString("buildHeader");
            this.addHeader = attrs.getString("addHeader");

            if (cfg.hasChildNodes()) {
                _items = new ArrayList<>();
                _adds  = new ArrayList<>();

                NodeList list = cfg.getChildNodes();
                for (int i=0, len=list.getLength(); i<len; i++){
                    Node n1 = list.item(i);
                    if(n1.getNodeType()==Node.ELEMENT_NODE) {
                        Element e1 = (Element) n1;

                        if (e1.getTagName().equals("item")) {
                            YhNode temp = new YhNode(this.source).buildForItem(e1, this);
                            _items.add(temp);
                        }
                        else if (e1.hasAttributes()) {
                            YhNode temp = new YhNode(this.source).buildForAdd(e1, this);
                            _adds.add(temp);
                        }
                        else {
                            attrs.set(e1.getTagName(), e1.getTextContent());
                        }
                    }
                }
            }
        }
        return this;
    }

    private YhNode buildForItem(Element cfg, YhNode p) {
        SetNodeMap(cfg);

        this.name = p.name;
        this.url   = attrs.getString("key");
        this.title = attrs.getString("title");//可能为null
        this.lib   = attrs.getString("lib");
        this._encode = attrs.getString("encode");
        return this;
    }

    private YhNode buildForAdd(Element cfg, YhNode p) {
        SetNodeMap(cfg);

        this.name  = cfg.getTagName();//默认为标签名
        this.url   = attrs.getString("url");
        this.title = attrs.getString("title");//可能为null
        this.method  = attrs.getString("method");
        this.parseUrl = attrs.getString("parseUrl");
        this.parse    = attrs.getString("parse");

        this._enckey = attrs.getString("enckey");
        this._encode = attrs.getString("encode");
        this._ua     = attrs.getString("ua");

        this.buildArgs   = attrs.getString("buildArgs");
        this.buildRef    = attrs.getString("buildRef");
        this.buildUrl    = attrs.getString("buildUrl");
        this.buildHeader = attrs.getString("buildHeader");
        return this;
    }

    private void SetNodeMap(Element cfg) {
        NamedNodeMap nnMap = cfg.getAttributes();
        for(int i=0, len=nnMap.getLength(); i<len; i++) {
            Node att = nnMap.item(i);
            attrs.set(att.getNodeName(), att.getNodeValue());
        }
    }

    public String encode() {
        if(TextUtils.isEmpty(_encode)) {
            return source.encode();
        }
        return _encode;
    }

    public String ua() {
        if(TextUtils.isEmpty(_ua)) {
            return source.ua();
        }
        return _ua;
    }

    public String method() {
        return method;
    }

    //==========================================

    public List<YhNode> items() {
        return _items;
    }

    public List<YhNode> adds() {
        return _adds;
    }

    @Override
    public String nodeName() {
        return name;
    }

    @Override
    public boolean isEmpty() {
        return _isEmpty;
    }

}
