package chay.org.androidutils.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import chay.org.androidutils.BuildConfig;
import chay.org.androidutils.application.ActivityLifecycleListener;

/**
 * Author:Chay
 * Time:2018/8/23 0023
 * <p>
 * 简单的BaseActivity，为了演示手机虚拟键的相关操作，目的：防止Activity被挟持
 * </p>
 **/
public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {

    public T binding;

    private HomeWatcherReceiver mHomeWatcherReceiver = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //基于Databind方式绑定xml和数据
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        //监听虚拟键的广播
        registerReceiver();
        setStrictMode();
    }

    public abstract int getLayoutId();

    /**
     * 设置手机开发者模式时使用严苛模式，对影响自己主线程的耗时操作进行处理
     */
    private void setStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()//对任意阻塞主线程操作都察觉
                    .penaltyDialog()//弹出违规提示窗
                    .penaltyLog()//打印日志
                    .build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()//对数据库cursor，Activity等可能内存泄露的监测
                    .penaltyDeath()//关闭
                    .penaltyLog()//打印日志
                    .build());
        }
    }

    //注册广播
    private void registerReceiver() {
        mHomeWatcherReceiver = new HomeWatcherReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomeWatcherReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHomeWatcherReceiver != null) {
            try {
                unregisterReceiver(mHomeWatcherReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
    虚拟键点击的广播
    PS：需要注意的是当点击多任务切换的时候，广播也会触发相应的事件，表明是人为操作。
    这个监听主要是防止第三方模拟透明界面，挟持APP
    使用方式：在BaseActivity注册，同时，在Application.ActivityLifecycleCallbacks进行相关监听处理，
    如果Activity被挟持了，那么就弹提示告诉用户，当前APP已到后台运行
     */
    public class HomeWatcherReceiver extends BroadcastReceiver {

        private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        private static final String SYSTEM_DIALOG_REASON_HOME_KEY_LONG = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {

            String intentAction = intent.getAction();
            if (TextUtils.equals(intentAction, Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (TextUtils.equals(SYSTEM_DIALOG_REASON_HOME_KEY, reason)) {
                    //表示按了home键,程序到了后台
                    ActivityLifecycleListener.user_click = true;
                } else if (TextUtils.equals(SYSTEM_DIALOG_REASON_HOME_KEY_LONG, reason)) {
                    //表示长按home键,显示最近使用的程序列表
                }
            }
        }

    }
}
