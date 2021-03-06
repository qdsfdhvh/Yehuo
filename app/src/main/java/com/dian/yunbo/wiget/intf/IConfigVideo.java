package com.dian.yunbo.wiget.intf;

import android.view.View;

/**
 * Created by admin on 2016/7/14.
 */
public interface IConfigVideo {
    public interface IStopCompletCallback
    {
        public void onStopComplet();
    }

    public interface IOpenSuccessCallback
    {
        public void onStopComplet();
    }

    public static enum AspectRatioType
    {
        RationFullScreen,//屏幕自适应
        RatioNative,    //原始比例
        RatioCustom     //自定义比例
    }

    public boolean useHardWareDecode(boolean isUse);
    public boolean isUseHardWareDecode();
    public boolean isEnableHardWareDecode();
    public boolean stopPlayer(IStopCompletCallback stopCallback, boolean isOnlyCallOnStopSuccess);
    public boolean isPlayerStop();
    public boolean isUseVR();
    public boolean play();

    public int getPlayPostion();
    public boolean setPlayPostion(int mesc);
    public boolean open();

    //aspectRation = width / height
    public void setAspectRatioType(AspectRatioType aspectRatioType, String param);
    public AspectRatioType getAspectRatioType();
    public String getAspectRatioNative();
    public String getAspectRatioCustom();

    public View getDisplayView();
}
