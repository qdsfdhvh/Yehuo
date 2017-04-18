package com.dian.yunbo.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dian.yunbo.model.LikeBean;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Locale;

import com.dian.yunbo.App;
import com.dian.yunbo.model.HistBean;

/**
 * Created by Seiko on 2017/3/29. Y
 */

public class FileUtil {

    //=====================================================
    /* 文件是否存在 */
    public static boolean isExits(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static String FormatTime(File file) {
        java.util.Date date = new java.util.Date(file.lastModified());
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd HH:mm", Locale.US);
        return df.format(date);
    }

    /* 时间排序 */
    public static class FileComparator implements Comparator<File> {
        @Override
        public int compare(File file1, File file2) {
            if (file1.lastModified() < file2.lastModified()) {
                return 1;// 最后修改的文件在前
            } else {
                return -1;
            }
        }
    }

    //=====================================================
    /** Gson保存 */
    public static void save(HistBean bean) {
        String path = App.getInstance().getHistPath() + bean.getHash();
        if (!save(path, bean)) {
            Log.d("FileUtil", "保存失败：" + bean.getHash());
        }
    }

    public static boolean save(LikeBean bean) {
        String path = App.getInstance().getLikePath() + bean.getHash();
        if (!save(path, bean)) {
            Log.d("FileUtil", "保存失败：" + bean.getHash());
            return false;
        }
        return true;
    }

    public static <T> boolean save(String path, T model) {
        String json = new Gson().toJson(model);
        return json != null && saveText2Sdcard(path, json);
    }

    //=========================================
    /** 保存 */
    public static boolean saveText2Sdcard(String fileName, String text) {
        File file = new File(fileName);
        File parentFile =  file.getParentFile();
        boolean isCreate = parentFile.exists();
        while (!isCreate) {
            isCreate = parentFile.mkdirs();
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(text);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();

            boolean isdelete = file.delete();
            while (!isdelete) {
                isdelete = file.delete();
            }

            return false;
        }
        return true;
    }


    //==========================================
    /** 读取 */
    public static String readTextFromSDcard(String fileName) {
        return readTextFromSDcard(new File(fileName));
    }

    @Nullable
    public static String readTextFromSDcard(File file) {
        if (!file.exists()) {
            return null;
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            int availableLength = fileInputStream.available();
            byte[] buffer = new byte[availableLength];
            fileInputStream.read(buffer);
            fileInputStream.close();

            return new String(buffer, "UTF-8");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String readTextFromSDcard(InputStream byteStream) {
        try {
            InputStreamReader isr = new InputStreamReader(byteStream,"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while((line = br.readLine()) != null){
                builder.append(line);
            }
            br.close();
            isr.close();

            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //==========================================
    /** 删除 */
    public static void deleteFile(String file) {deleteFile(new File(file));}

    private static void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (File file1:files) { // 遍历目录下所有的文件
                    deleteFile(file1); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
            if (file.getParentFile().listFiles().length == 0) {
                file.getParentFile().delete();
            }
        }
    }

    //==========================================
    /** 其他 */
    @NonNull
    public static String toString(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        return doToString(in);
    }

    @NonNull
    public static String toString(File is) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(is));
        return doToString(in);
    }

    @NonNull
    private static String doToString(BufferedReader in) throws IOException{
        StringBuilder buffer = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null){
            buffer.append(line);
        }
        return buffer.toString();
    }
}
