package chay.org.androidutils.application;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Author:Chay
 * Time:2018/8/23 0023
 * <p>
 * 简单的Application，方便管理Atcivity
 * </p>
 **/
public class SimpleApplication extends Application {

    public static List<Activity> ActivityCache = new LinkedList();

    private ActivityLifecycleListener activityLifecycleListener;

    @Override
    public void onCreate() {
        super.onCreate();

        //注册自己的Activity的生命周期回调接口。
        activityLifecycleListener = new ActivityLifecycleListener();
        registerActivityLifecycleCallbacks(activityLifecycleListener);
    }

    //真机不会被调用
    @Override
    public void onTerminate() {
        unregisterActivityLifecycleCallbacks(activityLifecycleListener);
        super.onTerminate();
    }
}
