package com.dian.yunbo.wiget.intf;

/**
 * Created by LZ on 2016/9/24.
 */
public interface IRecord {
    boolean startRecords(String mediaOutPath);
    boolean isRecording();
    void endRecords();
}
