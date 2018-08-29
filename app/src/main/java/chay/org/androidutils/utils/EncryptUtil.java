package chay.org.androidutils.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * Author:Chay
 * Time:2018/8/29 0029
 */
public class EncryptUtil {

    private static final String VIPARA = "0102030405060708";

    /**
     * 将字符串进行Md5加密
     *
     * @param inStr 需加密的字符串
     * @return 加密结果
     */
    public static String encryptMd5(String inStr) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp;
            if (PatternUtil.isContainsChinese(inStr)) {
                strTemp = inStr.getBytes("UTF-8");
            } else {
                strTemp = inStr.getBytes();
            }
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static String encryptMd5N(String inStr) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = inStr.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将文件进行Md5加密
     *
     * @param file 需加密的文件
     * @return 加密结果
     */
    public static String encryptMd5(File file) {
        String value = null;
        FileInputStream in = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int length = -1;
            while ((length = in.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                assert in != null;
                in.close();
            } catch (IOException ignored) {
            }
        }
        return value;
    }

    /**
     * AES加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     * @return 加密结果
     */

    public static String encryptAES(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return parseByte2HexStr(result); // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES解密
     *
     * @param contentStr 待解密内容
     * @param password   解密密钥
     * @return 解密结果
     */
    public static String decryptAES(String contentStr, String password) throws BadPaddingException {
        try {
            byte[] content = parseHexStr2Byte(contentStr);
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            return new String(result); // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     * @return 加密结果
     */
    public static String encryptAESIOS(String content, String password) throws BadPaddingException {
        try {

            byte[] key = transformStrToXXBitByteArr(password, 32);
            SecretKey secretKey = new SecretKeySpec(key, "AES");
            //Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
            //初始化，设置为加密模式
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            //执行操作
            byte[] result = cipher.doFinal(content.getBytes());
            return parseByte2HexStr(result); // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES解密
     *
     * @param contentStr 待解密内容
     * @param password   解密密钥
     * @return 解密结果
     */
    public static String decryptAESIOS(String contentStr, String password) throws BadPaddingException {
        try {
            byte[] content = parseHexStr2Byte(contentStr);
            byte[] key = transformStrToXXBitByteArr(password, 32);
            SecretKey secretKey = new SecretKeySpec(key, "AES");
            //Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, secretKey);// 初始化
            byte[] result = cipher.doFinal(content);
            return new String(result); // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     * @return 加密结果
     */

    public static String encryptAESAndroid(String content, String password) {
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
            SecretKeySpec key = new SecretKeySpec(transformStrToXXBitByteArr(password, 16), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            byte[] encryptedData = cipher.doFinal(content.getBytes("utf-8"));
            return parseByte2HexStr(encryptedData); // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES解密
     *
     * @param contentStr 待解密内容
     * @param password   解密密钥
     * @return 解密结果
     */
    public static String decryptAESAndroid(String contentStr, String password) throws BadPaddingException {
        try {
            byte[] content = parseHexStr2Byte(contentStr);
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
            SecretKeySpec key = new SecretKeySpec(transformStrToXXBitByteArr(password, 16), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            byte[] decryptedData = cipher.doFinal(content);
            return new String(decryptedData); // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将任意字符串转换为capacity个元素的byte数组
     * 长度超过32位的将被截断，不够32位的将被循环填充进数组
     *
     * @param key      源字符串
     * @param capacity 数组的容量
     * @return Byte数组
     * 入参的串只能是ASCII码值，不能含有中文
     */

    private static byte[] transformStrToXXBitByteArr(String key, int capacity) {
        if (key == null || key.equals("")) {
            return new byte[capacity];
        } else {
            char[] tempStr = key.toCharArray();
            byte[] resultByteArr = new byte[capacity];
            int count = capacity / key.length();
            int mod = capacity % key.length();
            //判断是否需要循环填充数组
            //需要循环
            if (count > 0) {
                for (int i = 0; i < capacity; i++) {
                    byte tempByte;
                    //判断，是否已经遍历一次
                    int flag = i / key.length();
                    //如果已经遍历了一次，取余数作为下标
                    if (flag > 0) {
                        tempByte = (byte) tempStr[i % key.length()];
                        resultByteArr[i] = tempByte;
                    } else {
                        //如果没有遍历结束，取i作为下标
                        tempByte = (byte) tempStr[i];
                        resultByteArr[i] = (byte) tempStr[i];
                    }
                }
                //不需要循环
            } else if (mod > 0) {
                for (int i = 0; i < capacity; i++) {
                    Byte tempChar = (byte) tempStr[i];
                    resultByteArr[i] = tempChar;
                }
            }
            return resultByteArr;
        }
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf buf
     * @return 转换结果
     */
    private static String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (byte aBuf : buf) {
            String hex = Integer.toHexString(aBuf & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr 16进制字符
     * @return 字节数组
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (byte aByte : bytes) {
            buf.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return buf.toString();
    }

    /**
     * SHA1加密
     *
     * @param str 加密字符串
     * @return 加密结果
     */
    public static String encryptSHA1(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encryptSign(String md5key) {
        // 要使用生成sign的字符
        String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h",
                "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
                "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
                "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z"
        };
        // 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
        String sTempSubString = md5key.substring(0, 8);
        // 这里需要使用 long 型来转换，因为 Inteper .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用 long ，则会越界
        long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
        String outChars = "";
        //循环获得每组6位的字符串
        for (int j = 0; j < 6; j++) {
            // 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
            //(具体需要看chars数组的长度   以防下标溢出，注意起点为0)
            long index = 0x0000003D & lHexLong;
            // 把取得的字符相加
            outChars += chars[(int) index];
            // 每次循环按位右移 5 位
            lHexLong = lHexLong >> 5;
        }
        return outChars;
    }

    /**
     * 用于存入数据库时加密对象
     *
     * @param intiObject 加密对象
     * @param password   加密密码
     * @return T
     */
    public static <T> T encryObject(T intiObject, String password) {
        Object copyObject = null;
        try {
            //利用反射新建一个对象
            copyObject = intiObject.getClass().newInstance();
            Field[] fields = intiObject.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                // if(field)
                //取值
                Object value;
                try {
                    if (Modifier.isStatic(field.getModifiers())) continue;
                    value = field.get(intiObject);
                    if (value instanceof String) {
                        //赋值
                        //Log.i("encryObject", "field==="+field+"====="+value+""+"========================================");
                        field.set(copyObject, EncryptUtil.encryptAESAndroid((String) value, password));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return (T) copyObject;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 用于存入数据库时解密对象
     *
     * @param encryObject 解密对象
     * @param password    加密密码
     * @return T
     */
    public static <T> T decryptObject(T encryObject, String password) {
        Object copyObject = null;
        try {
            //利用反射新建一个对象
            copyObject = encryObject.getClass().newInstance();
            Field[] fields = encryObject.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                //取值
                Object value;
                try {
                    if (Modifier.isStatic(field.getModifiers())) continue;
                    value = field.get(encryObject);
                    if (value instanceof String) {
                        //赋值
                        field.set(copyObject, EncryptUtil.decryptAESAndroid((String) value, password));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return (T) copyObject;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

}
