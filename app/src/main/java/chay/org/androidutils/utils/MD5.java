package chay.org.androidutils.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Author:Chay
 * Time:2018/8/29 0029
 */
public class MD5 {
    /**
     * MD5 加密
     *
     * @param str 加密字符串
     * @return 加密结果
     */
    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuilder md5StrBuilder = new StringBuilder();

        for (byte aByteArray : byteArray) {
            if (Integer.toHexString(0xFF & aByteArray).length() == 1) {
                md5StrBuilder.append("0").append(Integer.toHexString(0xFF & aByteArray));
            } else {
                md5StrBuilder.append(Integer.toHexString(0xFF & aByteArray));
            }
        }

        return md5StrBuilder.toString();
    }


}
