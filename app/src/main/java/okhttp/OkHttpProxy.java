package okhttp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class OkHttpProxy {

    private static OkHttpClient mHttpClient;

    private static OkHttpClient init() {
        synchronized (OkHttpProxy.class) {
            if (mHttpClient == null) {
                mHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url, cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url);
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();
            }
        }
        return mHttpClient;
    }

    public static OkHttpClient getInstance() {return mHttpClient == null ? init() : mHttpClient;}

    public static void setInstance(OkHttpClient okHttpClient) {
        OkHttpProxy.mHttpClient = okHttpClient;
    }

    public static GetRequestBuilder get() {return new GetRequestBuilder();}

    public static PostRequestBuilder post() {return new PostRequestBuilder();}

}
