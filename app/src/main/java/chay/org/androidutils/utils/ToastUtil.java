package chay.org.androidutils.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Author:Chay
 * Time:2018/8/23 0023
 * <p>
 * ToastUtil 吐司工具类，全局只有一个Toast，不用计算每次吐司之间的时间，
 * 避免连续显示Toast时不能取消上一次的情况
 * </p>
 */

public class ToastUtil {

    public static Toast toast;

    private ToastUtil() {

    }

    public static void showLong(Context context, String message) {
        showToast(context, message, Toast.LENGTH_LONG);
    }

    public static void showShort(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    private static void showToast(Context context, String message, int duration) {

        if (toast == null) {
            toast = Toast.makeText(context, message, duration);
        } else {
            toast.setText(message);
            toast.setDuration(duration);
        }

        toast.show();

    }

    public static void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }

    /**
     * Toast提示（兼容项目用法）
     *
     * @param context 上下文
     * @param tips    Toast文案
     */
    public static void showTips(Context context, String tips) {
        ToastUtil.showShort(context, tips);
    }

}
