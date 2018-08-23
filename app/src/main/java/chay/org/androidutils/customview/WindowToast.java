package chay.org.androidutils.customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import chay.org.androidutils.R;
import chay.org.androidutils.utils.L;

/**
 * Author:Chay
 * Time:2018/8/23 0023
 * <p>
 * 通过WindowManager创建相关的Toast，主要是针对部分机型，在刚放置后台时，不会显示Toast的情况
 * </p>
 */

public class WindowToast {
    //定义变量
    private static final int MESSAGE_WHAT = 0;
    public static final double LENGTH_SHORT = 2000.0;//2s
    public static final double LENGTH_LONG = 4000.0;//4s
    private static WindowToast toastCustom;
    private static WindowManager mWindowManager;
    private static WindowManager.LayoutParams params;
    private double time;
    private static View mView;
    private static TextView textView;
    private static MyHandler mHandler;

    /**
     * 指定显示内容和时间
     *
     * @param context 可以是Activity或getApplicationContext()
     * @param text    需要提示的信息文字
     * @param time    单位:毫秒
     */
    private WindowToast(Context context, CharSequence text, double time) {
        //初始化
        this.time = time;
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }

        //将自定义View添加到自定义布局
        mView = customView(context, text);

        //设置布局参数
        setLayoutParams(-1);
    }

    /**
     * 指定显示内容，时间和动画ID
     *
     * @param context
     * @param text
     * @param time
     * @param resAnimId
     */
    private WindowToast(Context context, String text, double time, int resAnimId) {
        //初始化
        this.time = time;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        //将自定义View添加到自定义布局
        mView = customView(context, text);

        //设置布局参数
        setLayoutParams(resAnimId);
    }

    //设置布局参数
    private void setLayoutParams(int resAnimId) {
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.windowAnimations = resAnimId;//Animation.INFINITE
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        params.y = 100;//正数，数值越大，位置向上移动
        params.x = 0;
    }

    //自定义View
    private View customView(Context context, CharSequence text) {
        LinearLayout toastView = new LinearLayout(context);
        toastView.setOrientation(LinearLayout.VERTICAL);
        toastView.setBackgroundResource(R.drawable.text_view_border);
        textView = new TextView(context);
        textView.setText(text);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setTextSize(14);
        textView.setPadding(20, 5, 20, 5);
        toastView.addView(textView, 0);
        return toastView;
    }

    /**
     * 传递需要显示的参数
     *
     * @param context
     * @param text
     * @param time
     * @return
     */
    public static WindowToast makeText(Context context, CharSequence text, double time) {
        if (toastCustom == null) {
            toastCustom = new WindowToast(context, text, time);
        } else {
            setText(text);
        }
        return toastCustom;
    }

    /**
     * 需要显示文字和图片
     *
     * @param context
     * @param text
     * @param time
     * @return
     */
    public static WindowToast makeTextAndIcon(Context context, CharSequence text, double time, int resIconId) {
        if (toastCustom == null) {
            toastCustom = new WindowToast(context, text, time);
            LinearLayout toastView = (LinearLayout) toastCustom.getView();
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(resIconId);
            toastView.addView(imageView, 0);
        } else {
            setText(text);
        }
        return toastCustom;
    }

    /**
     * 控制显示文字和动画
     *
     * @param context
     * @param text
     * @param time
     * @param resAnimId :使用方法:包名.R.style.自定义动画的样式名称
     * @return
     */
    public static WindowToast makeTextAndAnim(Context context, CharSequence text, double time, int resAnimId) {
        if (toastCustom == null) {
            toastCustom = new WindowToast(context, text, time);
        } else {
            setText(text);
        }
        return toastCustom;
    }

    /**
     * 自定义View
     *
     * @param view
     * @return
     */
    public static WindowToast setView(View view) {
        mView = view;
        return toastCustom;
    }

    /**
     * 获取默认的显示的view
     *
     * @return
     */
    public static View getView() {
        return mView;
    }

    /**
     * 修改显示的文本
     *
     * @param message
     */
    private static void setText(CharSequence message) {
        textView.setText(message);
    }

    /**
     * 自定义显示位置
     *
     * @param gravity:传递参数，如: Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM
     * @param xOffset
     * @return yOffset
     */
    public static WindowToast setGravity(int gravity, int xOffset, int yOffset) {
        params.gravity = gravity;
        params.x = xOffset;
        params.y = yOffset;
        return toastCustom;
    }


    /**
     * 调用makeText之后再调用
     */
    public void show() {
        try {
            //防止多次点击，重复添加
            if (mHandler == null) {
                mHandler = new MyHandler();
                mWindowManager.addView(mView, params);
                mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, (long) (time));
            }
        } catch (Exception e) {
            L.cLog().e("WindowToast has exceptions when it shows,the exception is " + e.getMessage());
        }
    }

    /**
     * 取消View的显示
     */
    private static void cancel() {
        try {
            mWindowManager.removeViewImmediate(mView);
        } catch (Exception e) {
            L.cLog().e("WindowToast makeS WindowLeak");
        } finally {
            mView = null;
            toastCustom = null;
            mHandler = null;
            mWindowManager = null;
        }
    }

    //自定义Handler
    private static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_WHAT:
                    cancel();
                    break;
            }
        }
    }
}
