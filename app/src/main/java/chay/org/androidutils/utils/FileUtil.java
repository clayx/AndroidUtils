package com.boshijj.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2018/4/18 0018.
 * <p>
 * 文件相关工具类
 * </p>
 */

public class FileUtil {

    /**
     * 保存文件
     *
     * @param bm
     * @throws IOException
     */
    public static void saveFile(Bitmap bm, String filePath) throws IOException {
        if (bm == null) {
            return;
        }
        File file = new File(filePath);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bos.flush();
        bos.close();
    }

    /**
     * 保存文件
     *
     * @param bm
     * @throws IOException
     */
    public static void saveFile(Bitmap bm, String dir, String name) throws IOException {
        AnyHelper.makeFilePath(dir, name);
        File file = new File(dir + name);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bos.flush();
        bos.close();
    }

    // 生成文件
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //通过Uri获取文件的真实路径
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 将友盟获取到的deviceToken写入txt，方便测试
     *
     * @param str
     */
    public static void getDeviceTokenToTxt(String str) {

        String filePath = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (hasSDCard) {
            filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "boseraToken.txt";
        } else
            filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + "boseraToken.txt";
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(str.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将X5的Log写入txt，方便查看
     *
     * @param str
     */
    public static void getX5LogForText(String str, String filename) {

        String filePath = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (hasSDCard) {
            filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "boseraWeb" + File.separator + filename;
        } else
            filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + "boseraWeb" + File.separator + filename;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(str.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
