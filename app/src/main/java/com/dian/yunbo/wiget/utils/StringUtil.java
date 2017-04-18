package com.dian.yunbo.wiget.utils;

/**
 * Created by admin on 2016/7/20.
 */
public class StringUtil {

    public static long StringToLong(String str)
    {
        String lowerCase = str.toLowerCase();
        final String HEX_PREF = "0x";

        int radix = 10;     //默认十进制
        if(str.startsWith(HEX_PREF)){
            str = str.substring(HEX_PREF.length());
            radix = 16;
        }

        Long lval = Long.parseLong(str, radix);
        return lval;
    }

    public static int StringToInt(String str)
    {
        Long lval = StringToLong(str);
        return (int)lval.longValue();
    }

    public static boolean equalsIgnoreCase(String str0, String str1)
    {
        if(null == str0 && null == str1)
        {
            return true;
        }

        if( (null == str0 && null != str1) ||
            (null != str0 && null == str1))
        {
            return false;
        }

        return str0.equalsIgnoreCase(str1);
    }
}
