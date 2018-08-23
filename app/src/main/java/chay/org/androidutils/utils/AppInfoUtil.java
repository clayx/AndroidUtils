package chay.org.androidutils.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Author:Chay
 * Time:2018/8/23 0023
 * <p>
 * App信息工具类
 * </p>
 **/
public class AppInfoUtil {

    //系统版本号
    public static String osVersion;

    /**
     * 获取手机系统版本号
     *
     * @return
     */
    public static String getOsVersion() {
        if (!Preconditions.isNullOrEmpty(osVersion)) {
            return osVersion;
        }
        osVersion = android.os.Build.VERSION.RELEASE;
        return osVersion;
    }

    //手机型号
    public static String mobileType = "";

    /**
     * 获取手机的手机型号
     *
     * @return
     */
    public static String getSystemModel() {
        if (!Preconditions.isNullOrEmpty(mobileType)) {
            return mobileType;
        }
        mobileType = android.os.Build.MODEL;
        /*
            下面代码可以防止在某些型号带"+"的字符串，在特定的加解密条件下，变成" "的情况
            防止出现不必要的问题。
         */
        /**
         if (mobileType.contains("+")) {
         mobileType = mobileType.replace("+", "");
         }
         **/
        return mobileType;
    }


    //设备ID imei号
    public static String imeiId = "";

    public static String getDeviceId(Context context) {
        if (!Preconditions.isNullOrEmpty(imeiId)) {
            return imeiId;
        }
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return "";
        }
        imeiId = TelephonyMgr.getDeviceId();
        return imeiId;
    }

    //android系统6.0之后，对mac地址的获取添加了权限
    public static String macAddress = "";

    public static String getAdresseMAC(Context context) {
        if (!Preconditions.isNullOrEmpty(macAddress)) {
            return macAddress;
        }
        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        String marshmallowMacAddress = "02:00:00:00:00:00";

        if (wifiInf != null && marshmallowMacAddress.equals(wifiInf.getMacAddress())) {

            try {
                macAddress = getAdressMacByInterface();
                if (macAddress != null) {
                    return macAddress;
                } else {
                    macAddress = getAddressMacByFile(wifiMan);
                    return macAddress;
                }
            } catch (Exception e) {
                L.cLog().e("Erreur lecture propriete Adresse MAC ");
            }
        } else {
            if (wifiInf != null && wifiInf.getMacAddress() != null) {
                macAddress = wifiInf.getMacAddress();
                return macAddress;
            } else {
                return "";
            }
        }
        return "";
    }

    /**
     * 通过NetworkInterface获得Mac地址
     *
     * @return
     */
    private static String getAdressMacByInterface() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:", b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }

        } catch (Exception e) {
            L.cLog().e("Erreur lecture propriete Adresse MAC ");
        }
        return null;
    }

    /**
     * 通过WifiManager获取相关的Mac地址
     *
     * @param wifiMan
     * @return
     * @throws Exception
     */
    private static String getAddressMacByFile(WifiManager wifiMan) throws Exception {
        String ret;
        int wifiState = wifiMan.getWifiState();
        String fileAddressMac = "/sys/class/net/wlan0/address";

        //这会使APP自动去打开wifi开关，使用需注意
        wifiMan.setWifiEnabled(true);
        File fl = new File(fileAddressMac);
        FileInputStream fin = new FileInputStream(fl);
        ret = crunchifyGetStringFromStream(fin);
        fin.close();

        boolean enabled = WifiManager.WIFI_STATE_ENABLED == wifiState;
        wifiMan.setWifiEnabled(enabled);
        return ret;
    }

    /**
     * inputStream  -> String
     *
     * @param crunchifyStream
     * @return
     * @throws IOException
     */
    private static String crunchifyGetStringFromStream(InputStream crunchifyStream) throws IOException {
        if (crunchifyStream != null) {
            Writer crunchifyWriter = new StringWriter();

            char[] crunchifyBuffer = new char[2048];
            try {
                Reader crunchifyReader = new BufferedReader(new InputStreamReader(crunchifyStream, "UTF-8"));
                int counter;
                while ((counter = crunchifyReader.read(crunchifyBuffer)) != -1) {
                    crunchifyWriter.write(crunchifyBuffer, 0, counter);
                }
            } finally {
                crunchifyStream.close();
            }
            return crunchifyWriter.toString();
        } else {
            return "";
        }
    }

    //手机ip地址
    public static String ipAddress = "";

    /**
     * 获取手机ip地址
     *
     * @param context
     * @return
     */
    public static String getIPAddress(Context context) {
        if (!Preconditions.isNullOrEmpty(ipAddress)) {
            return ipAddress;
        }
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                ipAddress = inetAddress.getHostAddress();
                                return ipAddress;
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            ToastUtils.showTips(context, "当前无网络连接,请在设置中打开网络");

        }
        return "";
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * 获取版本号VersionCode
     *
     * @param activity 上下文
     * @return 版本号
     */
    public static String getAppVerdionCode(Context activity) {
        int version = 0;
        try {
            PackageManager packageManager = activity.getPackageManager();
            PackageInfo pInfo = packageManager.getPackageInfo(activity.getPackageName(), 0);
            version = pInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version + "";
    }

    public static String version;

    /**
     * 获取版本号VersionName
     */
    public static String getAppVersion(Context context) {
        if (!Preconditions.isNullOrEmpty(version)) {
            return version;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo pInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        L.cLog().i("getAppVersion version:" + version);
        return version;
    }

    //渠道
    public static String channel = null;

    /**
     * 获取相关渠道
     *
     * @param context
     * @param defaultChannel
     * @return
     */
    public static String getMarket(Context context, @NonNull String defaultChannel) {
        if (channel != null) {
            return channel;
        }
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            channel = appInfo.metaData.getString("UMENG_CHANNEL");
            if (TextUtils.isEmpty(channel)) {
                channel = defaultChannel;
            }
        } catch (Exception e) {
            channel = defaultChannel;
            e.printStackTrace();
        }
        return channel;
    }

}
