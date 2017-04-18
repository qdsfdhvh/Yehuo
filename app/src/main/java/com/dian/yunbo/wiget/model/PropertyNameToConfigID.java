package com.dian.yunbo.wiget.model;

import android.content.Context;
import android.util.Log;

import com.aplayer.aplayerandroid.APlayerAndroid;


import java.util.HashMap;
import java.util.Map;

import dian.com.yunbo.R;


/**
 * Created by admin on 2016/7/12.
 */
public class PropertyNameToConfigID {
    private static PropertyNameToConfigID mPropertyNameToConfigID = new PropertyNameToConfigID();
    private static Map<String, Integer> mNameToID = new HashMap<String, Integer>();
    private static final String DEBUG_TAG = Content.APLAYER_DEMO_LOG_PREF_TAG +
            PropertyNameToConfigID.class.getSimpleName();

    private PropertyNameToConfigID() {
    }

    public static PropertyNameToConfigID getInstance(Context context)
    {
        if(mNameToID.isEmpty())
        {
            initMap(context);
        }

        return mPropertyNameToConfigID;
    }

    public Integer getIDByName(String configName)
    {
        return mNameToID.get(configName);
    }

    private static void initMap(Context context)
    {
        insertMap(context, R.string.PREF_HARD_WARE_DECODE_USE, APlayerAndroid.CONFIGID.HW_DECODER_USE);
        insertMap(context, R.string.PREF_HARD_WARE_DECODE_DETEC, APlayerAndroid.CONFIGID.HW_DECODER_DETEC);
        insertMap(context, R.string.PREF_VR_ENABLE, APlayerAndroid.CONFIGID.VR_ENABLE);
        insertMap(context, R.string.PREF_VR_MODE, APlayerAndroid.CONFIGID.VR_MODEL);
        insertMap(context, R.string.PREF_VR_FOVY, APlayerAndroid.CONFIGID.VR_FOVY);
        insertMap(context, R.string.PREF_VR_INNER_TOUCH_ROTATE, APlayerAndroid.CONFIGID.VR_ENABLE_INNER_TOUCH_ROTATE);
        insertMap(context, R.string.PREF_NET_BUFFER_ENTER, APlayerAndroid.CONFIGID.NET_BUFFER_ENTER);
        insertMap(context, R.string.PREF_NET_BUFFER_LEAVE, APlayerAndroid.CONFIGID.NET_BUFFER_LEAVE);
        insertMap(context, R.string.PREF_NET_BUFFER_READ, APlayerAndroid.CONFIGID.NET_BUFFER_READ);
        insertMap(context, R.string.PREF_NET_BUFFER_READ_TIME, APlayerAndroid.CONFIGID.NET_BUFFER_READ_TIME);
        insertMap(context, R.string.PREF_NET_SEEKBUFFER_WAITTIME, APlayerAndroid.CONFIGID.NET_SEEKBUFFER_WAITTIME);
        insertMap(context, R.string.PREF_HTTP_COOKIE, APlayerAndroid.CONFIGID.HTTP_COOKIE);
        insertMap(context, R.string.PREF_HTTP_REFERER, APlayerAndroid.CONFIGID.HTTP_REFERER);
        insertMap(context, R.string.PREF_HTTP_USER_AGENT, APlayerAndroid.CONFIGID.HTTP_USER_AGENT);
        insertMap(context, R.string.PREF_HTTP_CUSTOM_HEADERS, APlayerAndroid.CONFIGID.HTTP_CUSTOM_HEADERS);

    }

    private static void insertMap(String paramName, int val)
    {
        mNameToID.put(paramName, new Integer(val));
    }

    private static void insertMap(Context context, int configID, int val)
    {
        String configName = context.getString(configID);
        if(null == configName)
        {
            Log.w(DEBUG_TAG, "PlayConfig id is invalid!");
            return;
        }

        insertMap(configName, val);
    }
}
