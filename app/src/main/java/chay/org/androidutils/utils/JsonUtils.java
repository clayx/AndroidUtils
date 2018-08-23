package chay.org.androidutils.utils;

import com.google.gson.Gson;

/**
 * Author:Chay
 * Time:2018/8/23 0023
 * <p>
 * JsonUtils,便于解析json数据和转成json数据格式的String
 * PS:对于解析数据量比较小的，推荐使用阿里的fastjson，解析效率高；
 * 数据量比较大的，推荐使用gson。本工具类针对数据量大的，如需更换，
 * 只需gson包替换即可。
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
