package com.boshijj.utils;

/**
 * Author:Chay
 * Time:2018/8/6 0006
 * <p>
 * 防抖判断工具类
 * </p>
 **/
public class AntiShakeUtil {

    private static final long MIN_DELAY_TIME_DEFAULT = 500;
    private static long lastClickTime;

    /**
     * 是否快速点击，默认防抖时间500毫秒
     *
     * @return
     */
    public static boolean isFastClick() {
        return isFastClick(MIN_DELAY_TIME_DEFAULT);
    }

    /**
     * 是否快速点击按钮操作，
     * 在写点击事件的时候，最好直接判断一下（可在onClick方法的最开始判断即可）
     *
     * @param shakeTime 防抖时间
     * @return
     */
    public static boolean isFastClick(long shakeTime) {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if (currentClickTime - lastClickTime >= shakeTime) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

}
