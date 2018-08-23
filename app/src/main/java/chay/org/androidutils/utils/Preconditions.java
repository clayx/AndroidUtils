package chay.org.androidutils.utils;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.List;
import java.util.Map;

/**
 * Author:Chay
 * Time:2018/8/23 0023
 * <p>
 * NULL校检工具类
 * </p>
 **/

public class Preconditions {

    /**
     * 检查value是否为null，该方法直接返回value，因此可以内嵌使用checkNotNull。
     * 检查失败时抛出的异常 NullPointerException
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    /**
     * 返回给定的null string 转 空字符串。
     *
     * @param string 需要检测的 string
     * @return {@code string} 非空返回本身,反之.
     * @des 1.这些方法主要用来与混淆null/空的API进行交互.
     * 2.好的做法是积极地把null和空区分开，以表示不同的含义，
     * 在代码中把null和空同等对待是一种令人不安的坏味道。
     */
    public static String nullToEmpty(@Nullable String string) {
        return (string == null) ? "" : string;
    }

    /**
     * 返回给定的null string 转 默认字符串
     *
     * @param string     需要检测的 string
     * @param defaultStr 默认的 defaultStr
     * @return {@code string} 非空返回本身,反之返回defaultStr.
     */
    public static String nullToDefault(@Nullable String string, String defaultStr) {
        return TextUtils.isEmpty(string) ? defaultStr : string;
    }

    /**
     * 1.检查value是否为null，该方法直接返回value，因此可以内嵌使用checkNotNull.
     *
     * @param reference    需要检查的value参数
     * @param errorMessage 异常信息,如果参数为null,将会抛出
     *                     string using {@link String#valueOf(Object)}
     * @return 返回非空 value
     * @throws NullPointerException 检查失败时抛出的异常
     */
    public static <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    /**
     * @see {@link #nullToEmpty(String)}
     */
    @Nullable
    public static String emptyToNull(@Nullable String string) {
        return isNullOrEmpty(string) ? null : string;
    }

    /**
     * 如果string为空或长度为0 返回 {@code true}
     * <p>
     * 在不需要检查非空情况下,也可以规范性的使用 {@link String#isEmpty()}
     * string 也可 @see {@link TextUtils# isEmpty() }
     *
     * @param string
     * @return {@code true}
     */
    public static boolean isNullOrEmpty(@Nullable CharSequence string) {
        return TextUtils.isEmpty(string);
    }

    /**
     * 检查List
     *
     * @param list
     * @return
     */
    public static boolean isNullOrEmpty(@Nullable List list) {
        return list == null || list.size() == 0;
    }

    /**
     * 检查Map
     *
     * @param map
     * @return
     */
    public static boolean isNullOrEmpty(@Nullable Map map) {
        return map == null || map.size() == 0;
    }

    /**
     * 检查对象数组
     *
     * @param objs
     * @return
     */
    public static boolean isNullOrEmpty(@Nullable Object[] objs) {
        return objs == null || objs.length == 0;
    }

    /**
     * 检查对象
     *
     * @param obj
     * @return
     */
    public static boolean isNullOrEmpty(@Nullable Object obj) {
        return obj == null;
    }

}
