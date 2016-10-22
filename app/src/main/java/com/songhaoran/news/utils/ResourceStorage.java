package com.songhaoran.news.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/10/17 0017.
 */

public class ResourceStorage {

    private Context context;

    public ResourceStorage(Context context) {
        this.context = context;
    }

    /**
     * 获取url中的文件名
     */
    public String getFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 获取图片路径
     */
    public String getNewsImgDir() {
        File dir = new File(context.getExternalFilesDir(null), File.separator + "img" + File.separator);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.toString();
    }

    /**
     * 获取图片格式的方法
     */
    public String getFileFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Bitmap.CompressFormat getCompressFormat(String fileName) {
        String format = getFileFormat(fileName);
        if (format.equals("png")) {
            return Bitmap.CompressFormat.PNG;
        } else {
            return Bitmap.CompressFormat.JPEG;
        }
    }

    /**
     * 保存图片的方法
     */
    public void saveImg(String fileName, Bitmap bitmap) {
        File file = new File(getNewsImgDir(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(getCompressFormat(fileName), 100, outputStream);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
