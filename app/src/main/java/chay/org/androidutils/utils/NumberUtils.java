package com.boshijj.utils;

import android.content.Context;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Author: dengjiyun
 * Date: 2018/3/19
 * Description: 字符串转数字，避免异常
 */

public class NumberUtils {


    public static Double valueOfDouble(String number) {
        try {
            return Double.valueOf(number);
        } catch (Exception e) {
            //nothing
        }

        return 0.0;
    }

    public static Float valueOfFloat(String number) {
        try {
            return Float.valueOf(number);
        } catch (Exception e) {
            //nothing
        }

        return 0.0f;
    }

    public static Integer valueOfInt(String number) {
        try {
            return Integer.valueOf(number);
        } catch (Exception e) {
            //nothing
        }

        return 0;
    }

    public static float getTextLength(Context context, String text) {
        TextView textView = new TextView(context);
        textView.setTextSize(14);
        textView.setText(text);

        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(spec, spec);

        // getMeasuredWidth
        int measuredWidth = textView.getMeasuredWidth();

        // new textpaint measureText
        TextPaint newPaint = new TextPaint();
        float textSize = context.getResources().getDisplayMetrics().scaledDensity * 15;
        newPaint.setTextSize(textSize);
        float newPaintWidth = newPaint.measureText(text);

        // textView getPaint measureText
        TextPaint textPaint = textView.getPaint();
        float textPaintWidth = textPaint.measureText(text);
        return textPaintWidth;
    }

    public static String getDouble2Num(String num) {
        Double doubleNum = Double.parseDouble(num);
        DecimalFormat df = new DecimalFormat("0.00");
        String numStr = df.format(doubleNum);
        return numStr;
    }

    public static String getDouble2Num(Double num) {
        DecimalFormat df = new DecimalFormat("0.00");
        String numStr = df.format(num);
        return numStr;
    }

    public static String getDouble4Num(String num) {
        Double doubleNum = Double.parseDouble(num);
        DecimalFormat df = new DecimalFormat("0.0000");
        String numStr = df.format(doubleNum);
        return numStr;
    }

    public static String getDouble4Num(Double num) {
        DecimalFormat df = new DecimalFormat("0.0000");
        String numStr = df.format(num);
        return numStr;
    }
}
