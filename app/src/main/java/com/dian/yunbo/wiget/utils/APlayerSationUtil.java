package com.dian.yunbo.wiget.utils;

import com.aplayer.aplayerandroid.APlayerAndroid;

/**
 * Created by LZ on 2016/7/16.
 */
public class APlayerSationUtil {
    public static boolean isStopByUserCall(String playRet) {
        long stopCode = StringUtil.StringToLong(APlayerAndroid.PlayCompleteRet.PLAYRE_RESULT_CLOSE);
        long currentCode = StringUtil.StringToLong(playRet);

        boolean isUseCallStop = (stopCode == currentCode);
        return isUseCallStop;
    }

}
