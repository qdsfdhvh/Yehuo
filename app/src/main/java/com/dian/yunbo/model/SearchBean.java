package com.dian.yunbo.model;

import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by Seiko on 2017/3/26. Y
 */

public class SearchBean implements ItemType {

    private String hash;
    private String title;
    private String size;
    private String day;
    private String data;

    public void setHash(String hash) {this.hash = hash;}
    public String getHash() {return hash;}

    public void setTitle(String title) {this.title = title;}
    public String getTitle() {return title;}

    public void setSize(String size) {this.size = size;}
    public String getSize() {return size;}

    public void setDay(String day) {this.day = day;}
    public String getDay() {return day;}

    public void setData(String data) {this.data = data;}
    public String getData() {return data;}

    @Override
    public int itemType() {
        return 0;
    }

}
