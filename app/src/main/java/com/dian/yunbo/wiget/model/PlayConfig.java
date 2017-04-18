package com.dian.yunbo.wiget.model;

import com.aplayer.aplayerandroid.APlayerAndroid;

import com.dian.yunbo.wiget.intf.IConfigAudio;
import com.dian.yunbo.wiget.intf.IConfigSubtitle;
import com.dian.yunbo.wiget.intf.IConfigVideo;
import com.dian.yunbo.wiget.intf.IRecord;


/**
 * Created by admin on 2016/7/14.
 */
public class PlayConfig {
    public static class PlayerListener {
        public APlayerAndroid.OnPlayCompleteListener playCompleteListener;
    }

    private IConfigVideo mIConfigVideo;
    private IConfigAudio mIConfigAudio;
    private IConfigSubtitle mISubtitle;
    private IRecord mIRecord;

    public PlayConfig(IConfigVideo configVideo, IConfigAudio configAudio, IConfigSubtitle configSubtitle, IRecord record) {
        this.mIConfigVideo = configVideo;
        this.mIConfigAudio = configAudio;
        this.mISubtitle = configSubtitle;
        this.mIRecord = record;
    }

    public IConfigVideo getConfigVideo() {
        return mIConfigVideo;
    }

    public IConfigAudio getConfigAudio() {
        return mIConfigAudio;
    }

    public IConfigSubtitle getSubtitle() {
        return mISubtitle;
    }

    public IRecord geRecord() {
        return mIRecord;
    }

}
