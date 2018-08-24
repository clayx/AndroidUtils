package chay.org.androidutils.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Administrator on 2018/3/12 0012.
 * <p>
 * IntentUtil 实现检查等相关操作
 * </p>
 */

public class IntentUtil {

    /**
     * Activity隐式跳转要通过resolver检查，防止ActivityNotFoundException
     * PS:安全检查需要判断
     *
     * @param context context
     * @param intent  intent
     */
    public static void startAction(Context context, Intent intent) {
        if (context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            context.startActivity(intent);
        } else {
            L.cLog().e("activity not found");
        }
    }

    /**
     * 跳转到Activity列
     *
     * @param context context
     * @param intent  intent数组
     */
    public static void startActivityList(Context context, Intent[] intent) {
        if (context == null) return;
        if (intent == null || intent.length == 0) return;
        try {
            for (int i = 0; i < intent.length; i++) {
                intent[i] = wrapIntent(intent[i], context);
            }
            context.startActivities(intent);
        } catch (Exception e) {
        }
    }

    public static void startActivity(Context context, Intent intent) {
        startActivity(context, intent, null);
    }

    public static void startActivity(Context context, Intent intent, Bundle bundle) {
        if (context == null) return;
        if (intent == null) return;
        try {
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            intent = wrapIntent(intent, context);
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    public static void startActivityForResult(Activity act, Intent intent, int requestCode) {
        if (act == null) return;
        if (intent == null) return;
        try {
            act.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
        }
    }

    /**
     * 对Intent 包装一下 ，如果是 非activity 级别的 需要进行 加一个  FLAG_ACTIVITY_NEW_TASK
     *
     * @param intent intent
     * @return intent
     */
    private static Intent wrapIntent(@NonNull Intent intent, @NonNull Context context) throws NullPointerException {
        if (context instanceof Activity || context instanceof FragmentActivity) {
            return intent;
        } else {
            final Intent wrapIntent = intent;
            wrapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            return wrapIntent;
        }
    }

    /**
     * 跳转到相应的本机应用市场，否则跳转到默认网页
     *
     * @param context        context
     * @param defaultLoadUrl 默认地址
     */
    public static void startMarket(Context context, String defaultLoadUrl) {
        //跳转到应用市场
        try {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            IntentUtil.startAction(context, intent);
        } catch (Exception e) {
            if (PatternUtil.isUrl(defaultLoadUrl)) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri content_url = Uri.parse(defaultLoadUrl);
                intent.setData(content_url);
                IntentUtil.startAction(context, intent);
            }
        }
    }

    /**
     * 跳转到相应的应用设置详情界面，方便用户设置权限
     *
     * @param context context
     */
    public static void startAppDetailSetting(Context context) {
        Intent localIntent;
        // vivo 点击设置图标>加速白名单>我的app
        //      点击软件管理>软件管理权限>软件>我的app>信任该软件
        localIntent = context.getPackageManager().getLaunchIntentForPackage("com.iqoo.secure");
        if (localIntent != null) {
            context.startActivity(localIntent);
            return;
        }

        // oppo 点击设置图标>应用权限管理>按应用程序管理>我的app>我信任该应用
        //      点击权限隐私>自启动管理>我的app
        localIntent = context.getPackageManager().getLaunchIntentForPackage("com.oppo.safe");
        if (localIntent != null) {
            context.startActivity(localIntent);
            return;
        }

        localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }

        IntentUtil.startAction(context, localIntent);
    }

}
