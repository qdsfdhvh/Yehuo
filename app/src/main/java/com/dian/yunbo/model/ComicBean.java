package com.dian.yunbo.model;

import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by Seiko on 2017/3/26. Y
 */

public class ComicBean implements ItemType {

    private String name;
    private String logo;
    private String hash;

    public void setName(String name) {this.name = name;}
    public String getName() {return name;}

    public void setLogo(String logo) {this.logo = logo;}
    public String getLogo() {return logo;}

    public void setHash(String hash) {this.hash = hash;}
    public String getHash() {return hash;}

    @Override
    public int itemType() {
        return RecyclerItemType.NORMAL.getValue();
    }

}
