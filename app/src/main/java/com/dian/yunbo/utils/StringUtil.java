package com.dian.yunbo.utils;

import java.util.regex.Pattern;

/**
 * Created by Seiko on 2017/3/27. Y
 */

public class StringUtil {
    public static boolean isChar(String str) {
        String regEx= "([A-Za-z0-9]{40})";
        return Pattern.compile(regEx).matcher(str).find();
    }

    public static String index(String text, String s1, String s2) {
        return text.substring(text.indexOf(s1) + s1.length(), text.indexOf(s2));
    }
}
