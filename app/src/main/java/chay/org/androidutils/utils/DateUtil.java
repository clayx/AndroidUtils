package chay.org.androidutils.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Author:Chay
 * Time:2018/8/23 0023
 * <p>
 * 日期转换工具类
 * </p>
 */

public class DateUtil {

    /**
     * 获取当前系统时间
     *
     * @return
     */
    public static String getDateFormat() {
        return getDateFormat("yyyy-MM-dd HH:mm");
    }

    /**
     * 比较日期大小
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 比较结果代码
     */
    public static int compareDate(String date1, String date2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                return -1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 将时间戳转换为时间
     *
     * @param time    时间戳
     * @param pattern 转换成时间的格式
     * @return timeString
     */
    public static String stampToDate(long time, String pattern) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = new Date(time);
        res = simpleDateFormat.format(date);
        return res;
    }


    /**
     * 获取当前时间
     *
     * @param pattern 格式
     * @return timeString
     */
    public static String getDateFormat(String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            String date = sdf.format(new Date());
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将旧时间转换成新格式的时间
     * PS:时间格式要和旧的格式一一对应
     *
     * @param oldDate 旧日期
     * @param formate 新格式日期
     * @return dateString
     */
    public static String formatDate(String oldDate, String formate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        String sDate = "";
        try {
            date = df.parse(oldDate);
            sDate = new SimpleDateFormat(formate).format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sDate;
    }

    /**
     * 方法描述 转换日期字符串的格式
     *
     * @param oldDate    描述 要转换的字符串 注意该字符串要与参数2格式同步：如201402对应yyyyMM,又如2014-12对应yyyy-MM
     * @param oldFormate 描述 旧数据格式
     * @param formate    描述 新数据格式
     * @return 方法返回参数说明
     */
    public static String formatDate(String oldDate, String oldFormate, String formate) {
        String sDate = "";
        try {
            if (Preconditions.isNullOrEmpty(oldDate)) {
                return "";
            }
            SimpleDateFormat df = new SimpleDateFormat(oldFormate);
            Date date = null;
            date = df.parse(oldDate);
            sDate = new SimpleDateFormat(formate).format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return oldDate;
        }
        return sDate;
    }

    /**
     * 方法描述 返回星期 1 2 3 4 5 6 7
     *
     * @param time 描述 时间戳
     * @return 方法返回参数说明
     */
    public static int getWeekByTime(String time) {
        Date d = new Date(Long.parseLong(time));
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w == 0) w = 7;
        return w;
    }

    /**
     * 方法描述:将格式yyyy-MM-dd HH:mm:ss的字符串转换为时间戳
     *
     * @param oldDate 描述
     * @return 方法返回参数说明
     */
    public static String formatDateToTime(String oldDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        String sDate = "";
        try {
            date = df.parse(oldDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() + "";
    }

    /**
     * 获取今天的几日
     *
     * @return 方法返回参数说明
     */
    public static String getTodayDay() {
        return getDateFormat("dd");
    }


    /**
     * 方法描述 根据时间戳 返回文本格式的星期 如“星期一”
     *
     * @param time 描述 时间字符串 如 20150106
     * @return 方法返回参数说明   文本格式的星期
     */
    public static String getWdkByTime(String time) {
        String week = "";
        try {
            int weekTmp = getWeekByTime(time);
            switch (weekTmp) {
                case 1:
                    week = "星期一";
                    break;
                case 2:
                    week = "星期二";
                    break;
                case 3:
                    week = "星期三";
                    break;
                case 4:
                    week = "星期四";
                    break;
                case 5:
                    week = "星期五";
                    break;
                case 6:
                    week = "星期六";
                    break;
                case 7:
                    week = "星期日";
                    break;
                default:
                    week = "";
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return week;
    }

    /**
     * 获取前几天的日期
     *
     * @param step   当前天差值，如果为正则向后，否则向前
     * @param patten 格式
     * @return 日期
     */
    public static String getStepDay(int step, String patten) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, step);
        Date frontDay = calendar.getTime();
        String frontDayStr = new SimpleDateFormat(patten).format(frontDay);
        return frontDayStr;
    }

}
