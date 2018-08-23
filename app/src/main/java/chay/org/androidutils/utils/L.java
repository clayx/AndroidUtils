package chay.org.androidutils;

import android.util.Log;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import chay.org.androidutils.utils.Preconditions;

/**
 * Author:Chay
 * Time:2018/8/23 0023
 * <p>
 * Log's Utils
 * This class is used simply , L.getInstance("").d("bilibili"); this can be printed the detail that
 * you want,but it has more simple way that you can use , that is L.e(tag,info);it just like Log.class.
 * PS：The v and d log in Huawei  may be  not printed , you can open Huawei log setting to set.
 * </p>
 */

public class L {

    public static boolean logFlag = BuildConfig.DEBUG;//根据配置来设置是否打印日志
    private final static int logLevel = Log.VERBOSE;
    public final static String tag = "[Bosera]";
    private String lName = "";
    private static String defaultLName = "@Customer@";//默认的logname
    public static L instance;
    private static boolean isShowThread = true; //是否展示线程信息

    private L(String lName) {
        this.lName = lName;
    }

    public static L getInstance(String lName) {
        if (instance == null) {
            synchronized (L.class) {
                if (instance == null) {
                    instance = new L(lName);
                }
            }
        }
        return instance;
    }

    //默认
    public static L cLog() {
        return getInstance(defaultLName);
    }

    /**
     * 获取当前函数名称
     *
     * @return
     */
    private String getFunctionName(boolean isShowThread) {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(this.getClass().getName())) {
                continue;
            }
            if (isShowThread) {
                return lName + "[ " + Thread.currentThread().getName() + ": "
                        + st.getFileName() + ":" + st.getLineNumber() + " "
                        + st.getMethodName() + "() ]";
            } else {
                return lName + "[ " + st.getFileName() + ":" + st.getLineNumber() + " "
                        + st.getMethodName() + "() ]";
            }
        }
        return null;
    }

    /**
     * 无参数日志 可以标记代码作用域是否执行了.
     */
    public void i() {
        i("");
    }

    /**
     * 拓展默认显示线程
     *
     * @param str
     */
    public void i(Object str) {
        i(str, isShowThread);
    }

    /**
     * The Log Level:i
     *
     * @param str
     * @param isShowThread 是否展示线程
     */
    public void i(Object str, boolean isShowThread) {
        if (logFlag) {
            if (logLevel <= Log.INFO) {
                String name = getFunctionName(isShowThread);
                if (name != null) {
                    Log.i(tag, name + " - " + str);
                } else {
                    Log.i(tag, str.toString());
                }
            }
        }

    }

    public void d(Object str) {
        d(str, isShowThread);
    }

    /**
     * The Log Level:d
     *
     * @param str
     */
    public void d(Object str, boolean isShowThread) {
        if (logFlag) {
            str = str == null ? "obj is null!!" : str;
            if (logLevel <= Log.DEBUG) {
                String name = getFunctionName(isShowThread);
                if (name != null) {
                    Log.d(tag, name + " - " + str);
                } else {
                    Log.d(tag, str.toString());
                }
            }
        }
    }

    public void v(Object str) {
        v(str, isShowThread);
    }

    /**
     * The Log Level:V
     *
     * @param str
     */
    public void v(Object str, boolean isShowThread) {
        if (logFlag) {
            if (logLevel <= Log.VERBOSE) {
                String name = getFunctionName(isShowThread);
                if (name != null) {
                    Log.v(tag, name + " - " + str);
                } else {
                    Log.v(tag, str.toString());
                }
            }
        }
    }

    public void w(Object str) {
        w(str, isShowThread);
    }


    /**
     * The Log Level:w
     *
     * @param str
     */
    public void w(Object str, boolean isShowThread) {
        if (logFlag) {
            if (logLevel <= Log.WARN) {
                String name = getFunctionName(isShowThread);
                if (name != null) {
                    Log.w(tag, name + " - " + str);
                } else {
                    Log.w(tag, str.toString());
                }
            }
        }
    }

    public void e(Object str) {
        e(str, isShowThread);
    }

    /**
     * The Log Level:e
     *
     * @param str
     */
    public void e(Object str, boolean isShowThread) {
        if (logFlag) {
            if (logLevel <= Log.ERROR) {
                String name = getFunctionName(isShowThread);
                if (name != null) {
                    Log.e(tag, name + " - " + str);
                } else {
                    Log.e(tag, str.toString());
                }
            }
        }
    }

    /**
     * The Log Level:e
     *
     * @param ex
     */
    public void e(Exception ex) {
        if (logFlag) {
            if (logLevel <= Log.ERROR) {
                Log.e(tag, "error", ex);
            }
        }
    }

    /**
     * The Log Level:e
     *
     * @param log
     * @param tr
     */
    public void e(String log, Throwable tr) {
        if (logFlag) {
            String line = getFunctionName(isShowThread);
            Log.e(tag, "{Thread:" + Thread.currentThread().getName() + "}"
                    + "[" + lName + line + ":] " + log + "\n", tr);
        }
    }

    /**
     * 单元测试中使用此方法打印日志
     *
     * @param tag
     * @param info
     */
    public static void logTest(String tag, String info) {
        System.out.println(tag + " --- " + info);
    }

    /**
     * use simply the log level :v
     *
     * @param tag
     * @param info
     */
    public static void v(String tag, String info) {
        if (logFlag) {
            Log.v(tag, info);
        }
    }

    /**
     * use simply the log level :d
     *
     * @param tag
     * @param info
     */
    public static void d(String tag, String info) {
        if (logFlag) {
            Log.d(tag, info);
        }
    }

    /**
     * use simply the Log level :i
     *
     * @param tag
     * @param info
     */
    public static void i(String tag, String info) {
        if (logFlag) {
            Log.i(tag, info);
        }
    }

    /**
     * use simply the Log level :w
     *
     * @param tag
     * @param info
     */
    public static void w(String tag, String info) {
        if (logFlag) {
            Log.w(tag, info);
        }
    }

    /**
     * use simply the log level :e
     *
     * @param tag
     * @param info
     */
    public static void e(String tag, String info) {
        if (logFlag) {
            Log.e(tag, info);
        }
    }

    //打印参数信息
    public static void printRequestParams(String url, Map<String, String> map) {
        if (Preconditions.isNullOrEmpty(map)) {
            L.cLog().i("[ url = " + url + " ]" + "params is null", false);
            return;
        }
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = Preconditions.nullToEmpty(entry.getKey());
            String value = Preconditions.nullToEmpty(entry.getValue());
            if (!entry.getKey().equals("imgbase64")
                    && !entry.getKey().equals("content")) {
                String contentvalue = URLEncoder.encode(value);
                builder.append(key + "=" + contentvalue + "&");
            } else {
                builder.append(key + "=" + value + "&");
            }
        }
        String[] sl = builder.toString().split("&");
        Arrays.sort(sl);
        String src = "";
        for (int i = 0; i < sl.length; i++) {
            src += sl[i] + "&";
        }
        src = src.substring(0, src.length() - 1);
        L.cLog().i("[ url = " + url + " ]" + "\n"
                + "paramsString = " + src, false);
    }

    //打印请求结果数据
    public static void printResponseInfo(String url, String response) {
        L.cLog().i("[ url = " + url + " ]" + "\n"
                + "response = " + response, false);
    }

}
