package com.dian.yunbo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import com.dian.yunbo.model.HistBean;
import com.dian.yunbo.ui.activity.BtInfoActivity;
import com.dian.yunbo.ui.activity.DownActivity;
import com.dian.yunbo.ui.activity.MoreActivity;
import com.dian.yunbo.ui.activity.VideoActivity;
import com.dian.yunbo.utils.FileUtil;

/**
 * Created by Seiko on 2017/3/27. Y
 */

public class Navigation {

    public static void showBtInfo(Context from, String hash) {
        showBtInfo(from, "未知名称", hash);
    }

    public static void showBtInfo(Context from, String name, String hash) {
        if (hash.contains("magnet:?xt=urn:btih:")) {
            hash = hash.replace("magnet:?xt=urn:btih:", "");
        }
        //添加历史
        FileUtil.save(new HistBean(name, hash));

        Intent intent = new Intent(from, BtInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("hash", hash);
        intent.putExtras(bundle);
        from.startActivity(intent);
    }

    public static void showVideo(Context from, String name, String url, String cookie) {
        Intent intent = new Intent(from, VideoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("url", url);
        bundle.putString("cookie", cookie);
        intent.putExtras(bundle);
        from.startActivity(intent);
    }

    public static void showMore(Context from, String title, String type) {
        Intent intent = new Intent(from, MoreActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("type", type);
        intent.putExtras(bundle);
        from.startActivity(intent);
    }

    public static void showDown(Context from) {
        Intent intent = new Intent(from, DownActivity.class);
        from.startActivity(intent);
    }

    public static void copyText(Context from, String text) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) from.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", text);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        Toast.makeText(from, "复制成功", Toast.LENGTH_SHORT).show();
    }

    public static void outDown(Context from, String downUrl) {
        Intent intent = new Intent().setDataAndType(Uri.parse(downUrl), "video/*");
        from.startActivity(intent);
    }

    public static void outVideo(Context from, String url, String cookie) {
//        Log.d("outVideo", "url：" + url + "\ncookie：" + cookie);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
        Intent intent = new Intent();
        String[] headers = new String[]{"Cookie", cookie};
        intent.putExtra("headers", headers);
        intent.setDataAndType(Uri.parse(url), "video/*");
        from.startActivity(intent);
    }

}
