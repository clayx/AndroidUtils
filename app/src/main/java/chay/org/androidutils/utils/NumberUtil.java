package chay.org.androidutils.utils;

import android.content.Context;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Author:Chay
 * Time:2018/8/23 0023
 * Description: 字符串转数字，避免异常
 */

public class NumberUtil {

    /**
     * 字符串转化成double
     *
     * @param number number字符串
     * @return
     */
    public static Double valueOfDouble(String number) {
        try {
            return Double.valueOf(number);
        } catch (Exception e) {
            //nothing
        }

        return 0.0;
    }

    /**
     * 字符串转化成float
     *
     * @param number number字符串
     * @return
     */
    public static Float valueOfFloat(String number) {
        try {
            return Float.valueOf(number);
        } catch (Exception e) {
            //nothing
        }

        return 0.0f;
    }

    /**
     * 字符串转化成int
     *
     * @param number number字符串
     * @return
     */
    public static Integer valueOfInt(String number) {
        try {
            return Integer.valueOf(number);
        } catch (Exception e) {
            //nothing
        }

        return 0;
    }

    /**
     * 获取Text在特定TetxView（字号大小确定）的长度
     *
     * @param context  上下文
     * @param text     文字
     * @param textSize 字号大小
     * @return
     */
    public static float getTextLength(Context context, String text, int textSize) {
        TextView textView = new TextView(context);
        textView.setTextSize(textSize);
        textView.setText(text);

        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(spec, spec);

        // new textpaint measureText
        TextPaint newPaint = new TextPaint();
        float newTextSize = context.getResources().getDisplayMetrics().scaledDensity * textSize;
        newPaint.setTextSize(newTextSize);

        // textView getPaint measureText
        TextPaint textPaint = textView.getPaint();
        float textPaintWidth = textPaint.measureText(text);
        return textPaintWidth;
    }

    /**
     * 获取固定小数位的String字符串
     *
     * @param num     num字符串
     * @param pattern 适配标准（ex："0.00",""0.0000）
     * @return
     */
    public static String getDoubleNum(String num, String pattern) {
        Double doubleNum = Double.parseDouble(num);
        DecimalFormat df = new DecimalFormat("0.00");
        String numStr = df.format(doubleNum);
        return numStr;
    }

    /**
     * 获取固定小数位的String字符串
     *
     * @param num     double num
     * @param pattern 适配标准（ex："0.00",""0.0000）
     * @return
     */
    public static String getDoubleNum(Double num, String pattern) {
        DecimalFormat df = new DecimalFormat("0.00");
        String numStr = df.format(num);
        return numStr;
    }

}
