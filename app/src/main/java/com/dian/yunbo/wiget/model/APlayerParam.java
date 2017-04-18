package com.dian.yunbo.wiget.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by admin on 2016/7/11.
 */
public class APlayerParam implements Serializable {
    private String filePath;
    private Map<String, String> configParam;

    public APlayerParam(String filePath,  Map<String, String> configParam)
    {
        this.filePath = filePath;
        this.configParam = configParam;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Map<String, String> getConfigParam() {
        return configParam;
    }

    public void setConfigParam(Map<String, String> configParam) {
        this.configParam = configParam;
    }

}
