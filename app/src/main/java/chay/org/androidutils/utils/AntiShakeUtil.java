package chay.org.androidutils.utils;

/**
 * Author:Chay
 * Time:2018/8/23 0023
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
     * PS:此方法也可作为借口请求的防抖操作，防止错误操作导致接口请求多次
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
