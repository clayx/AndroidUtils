package com.boshijj.utils;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2018/4/24 0024.
 * <p>
 * JsonUtils,便于解析json数据和转成json数据格式的String
 * </p>
 */

public class JsonUtils {

    /**
     * 将json数据解析成对象数据
     *
     * @param json json字符串
     * @param type 对象类型
     * @param <T>  泛型
     * @return 需要的对象
     */
    public static <T> T parse(String json, Class<T> type) {
        if (Preconditions.isNullOrEmpty(json)) {
            return null;
        }

        T t = new Gson().fromJson(json, type);
        if (Preconditions.isNullOrEmpty(t)) {
            return null;
        }
        return t;
    }

    /**
     * 将对象转换成json字符串
     *
     * @param t   需要转换的对象
     * @param <T> 泛型
     * @return 对象json字符串
     */
    public static <T> String parseObj(T t) {
        if (Preconditions.isNullOrEmpty(t)) {
            return "";
        }
        String json = new Gson().toJson(t);
        if (Preconditions.isNullOrEmpty(json)) {
            return "";
        }
        return json;
    }


}
