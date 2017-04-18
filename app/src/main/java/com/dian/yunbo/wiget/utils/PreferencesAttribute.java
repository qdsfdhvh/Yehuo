package com.dian.yunbo.wiget.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/7/12.
 */
public class PreferencesAttribute {
//    public static class PayerParam
//    {
//        public static final String HARD_WARE_DECODE_USE = "HARD_WARE_DECODE_USE";
//        public static final String HARD_WARE_DECODE_DETECT = "HARD_WARE_DECODE_DETECT";
//
//        public static final String VR_ENABLE = "VR_ENABLE";
//        public static final String VR_FOVY = "VR_FOVY";
//
//        public static final String NET_PRELOADING_PACKAGE = "NET_PRELOADING_PACKAGE";
//        public static final String NET_PRELOADING_MILLISECOND = "NET_PRELOADING_MILLISECOND";
//        public static final String NET_INTERVAL_TIME_BEFORE_WAITING = "NET_INTERVAL_TIME_BEFORE_WAITING";
//        public static final String NET_PACKE_BUFFER_THRESHOLD = "NET_PACKE_BUFFER_THRESHOLD";
//        public static final String NET_SEEKBUFFER_WAITTIME = "NET_SEEKBUFFER_WAITTIME";
//
//        public static final String HTTP_COOKIE = "HTTP_COOKIE";
//        public static final String HTTP_REFERER = "HTTP_REFERER";
//        public static final String HTTP_CUSTOM_HEADERS = "HTTP_CUSTOM_HEADERS";
//        public static final String HTTP_USER_AGENT= "HTTP_USER_AGENT";
//    }

    private static String BoolToString(Boolean bval)
    {
        return bval ? "1" : "0";
    }

    public static Map<String, String> getPreferencesAll(Context context)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Map<String, ?> peferences = sharedPreferences.getAll();
        Map<String, String> pramMap  = PreferencesAttribute.getPreferences(peferences);
        return pramMap;
    }

    private static Map<String, String> getPreferences(Map<String, ?> paramMap)
    {
        Map<String, String> retMap = new HashMap<String, String>();

        for (Map.Entry<String, ?> entry : paramMap.entrySet())
        {
            String strVal = "";
            Object obj = entry.getValue();

            if(obj instanceof Boolean)
            {
                strVal = BoolToString((Boolean) obj);
            }
            else
            {
                strVal = obj.toString();
            }

            retMap.put(entry.getKey(), strVal);
        }

        return retMap;
    }

}
