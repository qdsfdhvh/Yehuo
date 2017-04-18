package com.dian.yunbo.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import dian.com.yunbo.R;

/**
 * Created by Seiko on 2017/3/26. Y
 */

public class ImageLoader {

    public static void load(Context context, ImageView iv, String url) {
        Drawable error;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            error = context.getDrawable(R.drawable.ic_error_black_24dp);
        } else {
            error = context.getResources().getDrawable(R.drawable.ic_error_black_24dp);
        }

        Glide.with(context).load(url).error(error).into(iv);
    }
}
