package com.dian.yunbo.sited;

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

public class YhNodeSet implements YhView {

    private List<YhView> _items;
    public YhAttrList attrs;
    private YhSource source;
    private String name;

    public YhNodeSet(YhSource s) {
        source = s;
        _items = new ArrayList<>();
        attrs = new YhAttrList();
    }

    public YhNodeSet buildForNode(Element element) {
        if (element == null) {
            return this;
        }

        name = element.getTagName();
        _items.clear();
        attrs.clear();

        {
            NamedNodeMap temp = element.getAttributes();
            for (int i = 0, len = temp.getLength(); i < len; i++) {
                Node p = temp.item(i);
                attrs.set(p.getNodeName(), p.getNodeValue());
            }
        }


        {
            NodeList temp = element.getChildNodes();
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

        NodeList xList = element.getChildNodes();

        for (int i = 0, len = xList.getLength(); i < len; i++) {
            Node n1 = xList.item(i);
            if (n1.getNodeType() == Node.ELEMENT_NODE) {
                Element e1 = (Element) n1;

                if (e1.hasAttributes()) {//说明是Node类型
                    YhNode temp = new YhNode(source).buildForNode(e1);
                    this.add(temp);
                } else {//说明是Set类型
                    YhNodeSet temp = new YhNodeSet(source).buildForNode(e1);
                    this.add(temp);
                }
            }
        }

        return this;
    }

    protected void add(YhView node) {
        _items.add(node);
    }


    public YhNode get(String name) {
        for(YhView n : _items){
            if(name.equals(n.nodeName())) {
                return (YhNode) n;
            }
        }
        return null;
    }

    @Override
    public String nodeName() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return _items.size()==0;
    }

}
