package com.dian.yunbo.model;

import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by Seiko on 2017/3/26. Y
 */

public class BtInfoBean implements ItemType {

    /**
      * name : [桜都字幕组][480P][EDGE（エッジ）]催眠クラス ～女子全員、知らないうちに妊娠してました～ 前編.mp4
      * size : 71.09MB
      * data : 653462616566386161333230346231646462313064613536303339623833613262366537613863322c32302c34333233
      */

    private String name;
    private String size;
    private String url;
    private String data;
    private String hash;
    private int index;

    public BtInfoBean() {

    }

    public BtInfoBean(String name, String data) {
        this.name = name;
        this.data = data;
    }

    public void setName(String name) {this.name = name;}
    public String getName() {return name;}

    public void setSize(String size) {this.size = size;}
    public String getSize() {return size;}

    public void setUrl(String url) {this.url = url;}
    public String getUrl() {return url;}

    public void setData(String data) {this.data = data;}
    public String getData() {return data;}

    public void setHash(String hash) {this.hash = hash;}
    public String getHash() {return hash;}

    public void setIndex(int index) {this.index = index;}
    public int getIndex() {return index;}

    @Override
    public int itemType() {
        return 0;
    }
}
