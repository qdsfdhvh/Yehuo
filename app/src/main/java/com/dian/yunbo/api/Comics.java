package com.dian.yunbo.api;

import android.support.annotation.Nullable;

import com.dian.yunbo.utils.FileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;
import com.dian.yunbo.App;
import com.dian.yunbo.model.ComicBean;

/**
 * Created by Seiko on 2017/3/30. Y
 */

public class Comics {

    private List<ComicBean> comics;
    private Random random;
    private int size;
    private String ID;

    Comics(String filename) {
        ID = filename;
        random = new Random();
        comics = getJson(filename);
        if (comics != null) {
            size = comics.size();
        }
    }

    @Nullable
    private List<ComicBean> getJson(String filename) {
        try {
            InputStream byteStream = App.getContext().getAssets().open(filename);
            String bulider = FileUtil.readTextFromSDcard(byteStream);

            Type type = new TypeToken<List<ComicBean>>(){}.getType();
            return new Gson().fromJson(bulider, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ComicBean> sub(int start, int end) {
        if (comics != null) {
            return comics.subList(start, end);
        }
        return null;
    }

    public ComicBean get(int i) {
        if (comics != null) {
            return comics.get(i);
        }
        return null;
    }

    public ComicBean getRadsom() {
        if (comics != null) {
            return comics.get(random.nextInt(size - 1));
        }
        return null;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return ID.replace(".json", "");
    }
}
