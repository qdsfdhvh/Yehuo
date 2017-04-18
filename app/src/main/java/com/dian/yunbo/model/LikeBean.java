package com.dian.yunbo.model;

import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by Seiko on 2017/4/8. Y
 */

public class LikeBean implements ItemType {

    private String name;
    private String hash;
    private String time;

    public LikeBean(String name, String hash) {
        this.name = name;
        this.hash = hash;
    }

    public void setName(String name) {this.name = name;}
    public String getName() {return name;}

    public void setHash(String hash) {this.hash = hash;}
    public String getHash() {return hash;}

    public void setTime(String time) {this.time = time;}
    public String getTime() {return time;}

    @Override
    public int itemType() {
        return 0;
    }

}
