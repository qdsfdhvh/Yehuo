package com.dian.yunbo.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import dian.com.yunbo.R;
import com.dian.yunbo.wiget.MNViderPlayer;

/**
 * Created by Seiko on 2017/3/26. Y
 */

public class VideoActivity extends AppCompatActivity {

    @BindView(R.id.custom_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.load_error)
    TextView load_error;
    @BindView(R.id.mn_videoplayer)
    MNViderPlayer mnViderPlayer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        //因为背景为黑色，修改loaderror字体颜色
        load_error.setTextColor(Color.WHITE);
//        UrlConfig.down(bean, this);
        Intent intent = this.getIntent();
        String name = intent.getStringExtra("name");
        String url = intent.getStringExtra("url");
        String cookie = intent.getStringExtra("cookie");
        onSuccess(name, url, cookie);
    }

    public void onSuccess(String name, String url, String cookies) {
        progressBar.setVisibility(View.GONE);

        mnViderPlayer.setIsNeedBatteryListen(true);
        mnViderPlayer.setIsNeedNetChangeListen(true);
        mnViderPlayer.playVideo(url, name, cookies);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mnViderPlayer != null) {
            mnViderPlayer.pauseVideo();
        }
    }

    @Override
    protected void onDestroy() {
        if (mnViderPlayer != null) {
            mnViderPlayer.destroyVideo();
            mnViderPlayer = null;
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        setImmersiveFullscreen(); //全屏
    }

    private void setImmersiveFullscreen() {
        Window window = getWindow();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            int uiOpts;
            uiOpts = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

            window.getDecorView().setSystemUiVisibility(uiOpts);
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //播放页禁止屏幕休眠
    }

}
