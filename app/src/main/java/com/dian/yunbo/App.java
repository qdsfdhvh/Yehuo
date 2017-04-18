package com.dian.yunbo;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import com.liulishuo.filedownloader.FileDownloader;

/**
 * Created by Seiko on 2017/3/26. Y
 */

public class App extends Application {

    private static App mCurrent;
    private String _PATH;

    @Override
    public void onCreate() {
        super.onCreate();
        mCurrent = this;
        FileDownloader.init(this);

        _PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Yunbo/";
        if (!_PATH.endsWith("/")) {
            _PATH += "/";
        }
    }

    public static App getInstance() {return mCurrent;}

    public static Context getContext() {return mCurrent.getApplicationContext();}

//    public String getBasePath() {return _PATH;}

    public String getLikePath() {return _PATH + "Like/";}

    public String getHistPath() {return _PATH + "Hist/";}

    public String getDownPath() {return _PATH + "Download/";}

    public String getSitePath() {return _PATH + "Site/";}
}
