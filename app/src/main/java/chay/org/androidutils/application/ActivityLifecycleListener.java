package chay.org.androidutils.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import chay.org.androidutils.MainActivity;
import chay.org.androidutils.customview.WindowToast;
import chay.org.androidutils.utils.L;

/**
 * Author:Chay
 * Time:2018/8/23 0023
 * <p>
 * 监听Activity的生命周期，计数判断当前应用是否处于前台/后台运行
 * </p>
 */

public class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks {

    public static int refCount = 0;//计数
    public static int antiHiJack = 0;//防止Activity劫持的计数

    public static boolean user_click = false;//用户按了home键或者返回键

    public static boolean isStackMainActivity = false;

    public static Activity topActivity;//当前栈顶的Activity

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        topActivity = activity;
        checkMainCreat(activity);
        SimpleApplication.ActivityCache.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        topActivity = activity;
        if (refCount == 0) {
            L.cLog().i(">>>>>>>>>>>>>>>>>>>切到前台  lifecycle");
        }
        refCount++;
        antiHiJack++;
        user_click = false;
        L.cLog().i("onActivityStarted refCount:" + refCount);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        topActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        topActivity = activity;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        topActivity = activity;
        refCount--;
        if (refCount < 0) {
            refCount = 0;
        }

        if (refCount == 0) {
            L.cLog().i(">>>>>>>>>>>>>>>>>>>切到后台  lifecycle");
        }
        L.cLog().i("onActivityStopped refCount:" + refCount);

        antiHiJack--;
        if (antiHiJack < 0) {
            antiHiJack = 0;
        }
        if (antiHiJack == 0) {
            if (!user_click) {
                WindowToast.makeText(activity, "程序已经放置后台运行", 3000).show();
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        topActivity = activity;

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        topActivity = activity;
        checkMainDestroy(activity);
        SimpleApplication.ActivityCache.remove(activity);
    }

    /**
     * 检查MainActivity是否创建，用于外链跳转
     *
     * @param activity
     */
    private void checkMainCreat(Activity activity) {
        if (activity instanceof MainActivity) {
            isStackMainActivity = true;
        }
    }

    /**
     * 检查MainActivity是否销毁，用于外链跳转
     *
     * @param activity
     */
    private void checkMainDestroy(Activity activity) {
        if (activity instanceof MainActivity) {
            isStackMainActivity = false;
        }
    }

}
