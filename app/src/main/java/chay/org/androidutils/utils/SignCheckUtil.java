package chay.org.androidutils.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Author:Chay
 * Time:2018/8/23 0023
 *
 * @Des: 签名验证工具，验证app包签名和获取的keystore中的签名是否一致，
 * PS：获取keystore中签名，打开Terminal,cd到相关的keystore(jks文件)的目录，执行
 * keytool -list -v -keystore xxx.jks，获取到相关的sign信息
 */

public class SignCheckUtil {

    private static SignCheckUtil instance;

    private SignCheckUtil() {
    }

    public static SignCheckUtil getInstance() {
        if (instance == null) {
            synchronized (SignCheckUtil.class) {
                if (instance == null) {
                    instance = new SignCheckUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 获取应用的签名
     *
     * @param context   应该是对应包的上下文
     * @param encrption 加密方式（1.SHA1 2.MD5 3.SHA256）
     * @return
     */
    private String getCertificateFingerprint(Context context, String encrption) {
        //获取包管理器
        PackageManager pm = context.getPackageManager();

        //获取当前要获取 SHA1 值的包名，也可以用其他的包名，但需要注意，
        //在用其他包名的前提是，此方法传递的参数 Context 应该是对应包的上下文。
        String packageName = context.getPackageName();

        //返回包括在包中的签名信息
        int flags = PackageManager.GET_SIGNATURES;

        PackageInfo packageInfo = null;

        try {
            //获得包的所有内容信息类
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //签名信息
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();

        //将签名转换为字节数组流
        InputStream input = new ByteArrayInputStream(cert);

        //证书工厂类，这个类实现了出厂合格证算法的功能
        CertificateFactory cf = null;

        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //X509 证书，X.509 是一种非常通用的证书格式
        X509Certificate c = null;

        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String hexString = null;

        try {
            //加密算法的类，
            MessageDigest md = MessageDigest.getInstance(encrption);

            //获得公钥
            byte[] publicKey = md.digest(c.getEncoded());

            //字节到十六进制的格式转换
            hexString = byte2HexFormatted(publicKey);

        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return hexString;
    }

    //这里是将获取到得编码进行16 进制转换
    private String byte2HexFormatted(byte[] arr) {

        StringBuilder str = new StringBuilder(arr.length * 2);

        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1)
                h = "0" + h;
            if (l > 2)
                h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1))
                str.append(':');
        }
        return str.toString();
    }

    /**
     * 将空转成空串
     *
     * @param value 值
     * @return str
     */
    private String nullToString(String value) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }
        return value;
    }

    /**
     * 检测签名是否正确
     *
     * @return true 签名正常 false 签名不正常
     */
    public boolean check(Context context, String encrptionName, String realCer) {

        if (realCer != null) {
            String cer = nullToString(getCertificateFingerprint(context, encrptionName));
            realCer = realCer.trim();
            if (cer.equals(realCer)) {
                return true;
            }
        }

//        AnyHelper.showTips(context, "请前往官方渠道下载正版app");

        return false;
    }

    /**
     * 检查签名是否正确，默认为SHA1
     *
     * @param context 上下文
     * @param realCer 真正签名sign
     * @return 是否签名正确
     */
    public boolean checkDefault(Context context, String realCer) {
        return check(context, "SHA1", realCer);
    }
}
