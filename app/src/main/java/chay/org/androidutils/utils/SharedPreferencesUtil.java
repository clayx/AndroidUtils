package chay.org.androidutils.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.Map;

/**
 * Author:Chay
 * Time:2018/9/3 0003
 * <p>
 * SharedPreferences工具类
 * </p>
 **/
public class SharedPreferencesUtil {

    //生成文件名
    private static final String SP_NAME = "chaysp";

    public static void save(Context context, String key, String value) {
        saveString(context, key, value);
    }

    public static void saveString(Context context, String key, String value) {
        saveString(context, null, key, value);
    }

    /**
     * 保存String
     *
     * @param context context
     * @param spName  文件名
     * @param key     key
     * @param value   value
     */
    public static void saveString(Context context, @Nullable String spName, String key, String value) {
        SharedPreferences sp =initSP(context, spName);
        sp.edit().putString(key, value).apply();

    }

    public static String get(Context context, String key, String defaultValue) {
        return getString(context, key, defaultValue);
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getString(context, null, key, defaultValue);
    }

    /**
     * 获取Stirng
     *
     * @param context      context
     * @param spName       文件名
     * @param key          key
     * @param defaultValue 默认值
     * @return
     */
    public static String getString(Context context, @Nullable String spName, String key, String defaultValue) {
        SharedPreferences sp =initSP(context, spName);
        return sp.getString(key, defaultValue);
    }

    public static void save(Context context, String key, int value) {
        saveInt(context, key, value);
    }

    public static void saveInt(Context context, String key, int value) {
        saveInt(context, null, key, value);
    }

    /**
     * 保存Int
     *
     * @param context context
     * @param spName  文件名
     * @param key     key
     * @param value   value
     */
    public static void saveInt(Context context, @Nullable String spName, String key, int value) {
        SharedPreferences sp =initSP(context, spName);
        sp.edit().putInt(key, value).apply();
    }

    public static int get(Context context, String key, int defaultValue) {
        return getInt(context, key, defaultValue);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return getInt(context, null, key, defaultValue);
    }


    /**
     * 获取Int
     *
     * @param context      context
     * @param spName       文件名
     * @param key          key
     * @param defaultValue 默认值
     * @return
     */
    public static int getInt(Context context, @Nullable String spName, String key, int defaultValue) {
        SharedPreferences sp =initSP(context, spName);
        return sp.getInt(key, defaultValue);
    }

    public static void save(Context context, String key, boolean value) {
        saveBoolean(context, key, value);
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        saveBoolean(context, null, key, value);
    }

    /**
     * 保存boolean
     *
     * @param context context
     * @param spName  文件名
     * @param key     key
     * @param value   value
     */
    public static void saveBoolean(Context context, @Nullable String spName, String key, boolean value) {
        SharedPreferences sp =initSP(context, spName);
        sp.edit().putBoolean(key, value).apply();
    }

    public static boolean get(Context context, String key, boolean defaultValue) {
        return getBoolean(context, key, defaultValue);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getBoolean(context, null, key, defaultValue);
    }

    /**
     * 获取boolean
     *
     * @param context      context
     * @param spName       文件名
     * @param key          key
     * @param defaultValue 默认值
     * @return
     */
    public static boolean getBoolean(Context context, @Nullable String spName, String key, boolean defaultValue) {
        SharedPreferences sp =initSP(context, spName);
        return sp.getBoolean(key, defaultValue);
    }

    public static void remove(Context context, String key) {
        remove(context, null, key);
    }

    /**
     * 移除某个key对应的值
     *
     * @param context context
     * @param spName  文件名
     * @param key     key
     */
    public static void remove(Context context, @Nullable String spName, String key) {
        SharedPreferences sp =initSP(context, spName);
        sp.edit().remove(key).apply();
    }

    public static void clear(Context context) {
        clear(context, null);
    }

    /**
     * 清除全部的key-value
     *
     * @param context context
     * @param spName  文件名
     */
    public static void clear(Context context, @Nullable String spName) {
        SharedPreferences sp =initSP(context, spName);
        sp.edit().clear().apply();
    }

    public static boolean contains(Context context, String key) {
        return contains(context, null, key);
    }

    /**
     * 判断某个key是否存在
     *
     * @param context
     * @param spName
     * @param key
     * @return
     */
    public static boolean contains(Context context, @Nullable String spName, String key) {
        SharedPreferences sp =initSP(context, spName);
        return sp.contains(key);
    }

    public static Map<String, ?> getAll(Context context) {
        return getAll(context, null);
    }

    /**
     * 获取文件中的所以key-value
     *
     * @param context context
     * @param spName  文件名
     * @return
     */
    public static Map<String, ?> getAll(Context context, @Nullable String spName) {
        SharedPreferences sp =initSP(context, spName);
        return sp.getAll();
    }

    /**
     * 初始化SharedPreferences
     *
     * @param context context
     * @param spName  文件名
     */
    private static SharedPreferences initSP(Context context, @Nullable String spName) {
        //如果文件名为空，则用默认文件名
        if (Preconditions.isNullOrEmpty(spName)) {
            spName = SP_NAME;
        }
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp;
    }

}
