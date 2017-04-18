package com.dian.yunbo.wiget.config;


import com.aplayer.aplayerandroid.APlayerAndroid;

import com.dian.yunbo.wiget.intf.IRecord;

/**
 * Created by LZ on 2016/9/24.
 */
public class Record implements IRecord
{
    private APlayerAndroid mAPlayerAndroid = null;

    public Record(APlayerAndroid aPlayerAndroid) {
        mAPlayerAndroid = aPlayerAndroid;
    }

    @Override
    public boolean startRecords(String mediaOutPath) {
    	
        return mAPlayerAndroid.startRecord(mediaOutPath);
    }

    @Override
    public boolean isRecording() {
        return mAPlayerAndroid.isRecord();
    }

    @Override
    public void endRecords() {
    	mAPlayerAndroid.endRecord();
    }
}
