package com.dian.yunbo.model;

import java.io.File;
import zlc.season.practicalrecyclerview.ItemType;
import static com.dian.yunbo.utils.FileUtil.FormatTime;

/**
 * Created by Seiko on 2017/4/8. Y
 */

public class DownBean implements ItemType {

    private String name;
    private String path;
    private String time;

    public DownBean(File file) {
        this.name = file.getName();
        this.path = file.getPath();
        this.time = FormatTime(file);
    }

    public void setName(String name) {this.name = name;}
    public String getName() {return name;}

    public void setPath(String path) {this.path = path;}
    public String getPath() {return path;}

    public void setTime(String time) {this.time = time;}
    public String getTime() {return time;}

    @Override
    public int itemType() {
        return 0;
    }

}
