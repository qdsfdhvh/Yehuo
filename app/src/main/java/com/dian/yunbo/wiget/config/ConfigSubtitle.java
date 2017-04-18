package com.dian.yunbo.wiget.config;

import com.aplayer.aplayerandroid.APlayerAndroid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dian.yunbo.wiget.intf.IConfigSubtitle;
import com.dian.yunbo.wiget.utils.BoolUtil;
import com.dian.yunbo.wiget.utils.StringUtil;

/**
 * Created by admin on 2016/7/14. Y
 */
public class ConfigSubtitle implements IConfigSubtitle {

    private APlayerAndroid mAPlayerAndroid = null;

    public ConfigSubtitle(APlayerAndroid aPlayerAndroid) {
        mAPlayerAndroid = aPlayerAndroid;
    }

    @Override
    public boolean isUsable() {
        String strUsable = mAPlayerAndroid.getConfig(APlayerAndroid.CONFIGID.SUBTITLE_USABLE);
        return BoolUtil.APlayerConfigValToBool(strUsable);
    }

    @Override
    public boolean isShow() {
        String strIsShow = mAPlayerAndroid.getConfig(APlayerAndroid.CONFIGID.SUBTITLE_SHOW);
        return BoolUtil.APlayerConfigValToBool(strIsShow);
    }

    @Override
    public boolean show(boolean isShow) {
        String strIsShow = BoolUtil.BoolToAPlayerConfigBool(isShow);
        int  setShowRetCode = mAPlayerAndroid.setConfig(APlayerAndroid.CONFIGID.SUBTITLE_SHOW, strIsShow);
        return BoolUtil.APlayerConfigValToBool(setShowRetCode);
    }

    @Override
    public String getExternalSupportType() {
        String subtitlSupportExternalType = mAPlayerAndroid.getConfig(APlayerAndroid.CONFIGID.SUBTITLE_EXT_NAME);
        return subtitlSupportExternalType;
    }

    @Override
    public String getExternalSubtitlePath() {
        String externalSubtitlePath = mAPlayerAndroid.getConfig(APlayerAndroid.CONFIGID.SUBTITLE_FILE_NAME);
        return externalSubtitlePath;
    }

    @Override
    public boolean setExternalSubtitlePath(String externalSubtitlePath) {
        int setRetCode = mAPlayerAndroid.setConfig(APlayerAndroid.CONFIGID.SUBTITLE_FILE_NAME, externalSubtitlePath);
        return BoolUtil.APlayerConfigValToBool(setRetCode);
    }

    @Override
    public List<String> getSubtitleList() {
        String strSubtitLanguage = mAPlayerAndroid.getConfig(APlayerAndroid.CONFIGID.SUBTITLE_LANGLIST);
        return SplitLanguageList(strSubtitLanguage);
    }

    @Override
    public int getCurrentSubtitlePos() {
        String strSubtitlePos = mAPlayerAndroid.getConfig(APlayerAndroid.CONFIGID.SUBTITLE_CURLANG);
        return (int) StringUtil.StringToLong(strSubtitlePos);
    }

    @Override
    public boolean setCurrentSubtitle(int pos) {
        String strPos = Integer.toString(pos);
        int setSubtitleRetCode = mAPlayerAndroid.setConfig(APlayerAndroid.CONFIGID.SUBTITLE_CURLANG, strPos);
        return BoolUtil.APlayerConfigValToBool(setSubtitleRetCode);
    }

    private static List<String> SplitLanguageList(String subtitLanguage) {
        List<String> subtitleLanguageList = null;
        do {
            //subtitLanguage.isEmpty()
            if(null == subtitLanguage || subtitLanguage.isEmpty())
                break;

            final String SPLIT_STR = ";";
            String[] subtitleLanguageArray = subtitLanguage.split(SPLIT_STR);
            if(null == subtitleLanguageArray)
                break;

            subtitleLanguageList = Arrays.asList(subtitleLanguageArray);
        }while (false);

        subtitleLanguageList = (null == subtitleLanguageList) ? new ArrayList<String>() : subtitleLanguageList;
        return subtitleLanguageList;
    }
}
