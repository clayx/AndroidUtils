package com.boshijj.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author:Chay
 * Time:2018/8/6 0006
 * <p>
 * View相关适配工具类
 * </p>
 **/
public class ViewFitUtil {

    private static float sNoncompatDensity;
    private static float sNoncompatScaledDensity;

    /**
     * 设置基础的适配标准
     *
     * @param activity
     * @param application
     * @param screenWidth
     */
    public static void setCustomDensity(@NonNull Activity activity, final Application application, int screenWidth) {
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        if (sNoncompatDensity == 0) {
            sNoncompatDensity = appDisplayMetrics.density;
            sNoncompatScaledDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (!Preconditions.isNullOrEmpty(newConfig) && newConfig.fontScale > 0) {
                        sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        final float targetDensity = appDisplayMetrics.widthPixels / screenWidth;
        final float targetScaledDensity = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

    /**
     * 没有边距的，设置高度方法
     */
    public static <T extends ViewGroup.LayoutParams> void setViewHeight(Context context, View view, double scaleType) {
        setViewHeight(context, view, 0, scaleType);
    }

    /**
     * 没有边距的，设置高度方法
     */
    public static <T extends ViewGroup.LayoutParams> void setViewHeight(Context context, View view, int width, int height) {
        setViewHeight(context, view, 0, width, height);
    }

    /**
     * 根据UI给的相关边距和比例，给相关的View这是比例宽高
     *
     * @param context   context
     * @param view      相关View
     * @param marginall 左右边距和
     * @param width     比例宽
     * @param height    比例高
     * @param <T>       LayoutParams类型
     */
    public static <T extends ViewGroup.LayoutParams> void setViewHeight(Context context, View view, int marginall, int width, int height) {
        double scaleType = (double) (height / width);
        setViewHeight(context, view, marginall, scaleType);
    }

    /**
     * 根据UI给的相关边距和比例，给相关的View这是比例宽高
     *
     * @param context   context
     * @param view      相关View
     * @param marginall 左右边距和
     * @param scaleType 宽高比
     * @param <T>       LayoutParams类型
     */
    public static <T extends ViewGroup.LayoutParams> void setViewHeight(Context context, View view, int marginall, double scaleType) {
        int screenWidth = ScreenUtil.getSreenWidth(context);
        int viewWidth = screenWidth - ScreenUtil.dip2px(context, marginall);
        int viewHeight = (int) ((scaleType) * viewWidth);
        T layoutParams = (T) view.getLayoutParams();
        layoutParams.width = viewWidth;
        layoutParams.height = viewHeight;
        view.setLayoutParams(layoutParams);
    }

}
