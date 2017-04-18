package com.dian.yunbo.sited;

import android.util.Base64;
import android.util.Log;

import com.dian.yunbo.sited.view.HttpCallback;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.io.StringReader;
import java.net.URLEncoder;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import okhttp.OkCallback;
import okhttp.OkHttpProxy;
import okhttp.OkTextParser;

/**
 * Created by Seiko on 2017/4/15. Y
 */

public class Util {
    public static final String defUA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240";

    public static void http(final YhSource source, final HttpMessage msg) {
        log("Util.http", msg.url);

        OkCallback callback1 = new OkCallback<String>(new OkTextParser(msg.encode)) {
            @Override
            public void onSuccess(String cookie, String s) {
                source.setCookies(cookie);
                msg.callback.run(1, msg, s);
            }

            @Override
            public void onFailure(Throwable e) {
                log("http.onFailure", e.getMessage(), e);

                msg.callback.run(-2, msg, null);
            }
        };

        try {
            int idx = msg.url.indexOf('#'); //去除hash，即#.*
            String url2;
            if (idx > 0)
                url2 = msg.url.substring(0, idx);
            else
                url2 = msg.url;

            if ("post".equals(msg.method)) {
                source.mCall = OkHttpProxy.post()
                        .url(url2)
                        .setParams(msg.form)
                        .addHeader("User-Agent", msg.ua)
                        .enqueue(callback1);
            } else {
                source.mCall = OkHttpProxy.get()
                        .url(url2)
                        .addHeaders(msg.header)
                        .addHeader("User-Agent", msg.ua)
                        .enqueue(callback1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            msg.callback.run(-2, msg, null);
        }
    }


    public static Element getElement(Element n, String tag) {
        NodeList temp = n.getElementsByTagName(tag);
        if (temp.getLength() > 0)
            return (Element) (temp.item(0));
        else
            return null;
    }

    public static Element getXmlroot(String xml) throws Exception {
        StringReader sr = new StringReader(xml);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dombuild = factory.newDocumentBuilder();

        return dombuild.parse(new InputSource(sr)).getDocumentElement();
    }

    public static String urlEncode(String str, String encode) {
        try {
            return URLEncoder.encode(str, encode);
        } catch (Exception ex) {
            return "";
        }
    }

    public static String EncDecode(String pwd, String s3) {
        try {
            Key key = new SecretKeySpec(pwd.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] arrby = cipher.doFinal(Base64.decode(s3, 0));
            return new String(arrby).trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void log(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void log(String tag, String msg, Throwable tr) {
        Log.v(tag, msg, tr);
    }

}
