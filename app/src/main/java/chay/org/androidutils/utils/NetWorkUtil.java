package com.boshijj.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Administrator on 2018/4/17 0017.
 * <p>
 * 网络检查工具类
 * </p>
 */

public class NetWorkUtil {

    /**
     * 对网络连接状态进行判断
     *
     * @return true, 可用； false， 不可用
     */
    public static boolean isOpenNetwork(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }

        return false;
    }

}
