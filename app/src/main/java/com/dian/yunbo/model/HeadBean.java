package com.dian.yunbo.model;

import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by Seiko on 2017/3/29. Y
 */

public class HeadBean implements ItemType{

    private String title;
    private String type;

    public HeadBean() {

    }

    public HeadBean(String title) {
        this.title = title;
    }

    public void setTitle(String title) {this.title = title;}
    public String getTitle() {return title;}

    public void setType(String type) {this.type = type;}
    public String getType() {return type;}

    @Override
    public int itemType() {
        return RecyclerItemType.TYPE1.getValue();
    }

}
