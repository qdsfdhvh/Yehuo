package com.dian.yunbo.model;

import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by Seiko on 2017/4/15. Y
 */

public class SitedBean implements ItemType {

    private String name;

    public void setName(String name) {this.name = name;}
    public String getName() {return name;}

    @Override
    public int itemType() {
        return RecyclerItemType.NORMAL.getValue();
    }

}
