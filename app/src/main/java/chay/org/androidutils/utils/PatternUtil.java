package chay.org.androidutils.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Author:Chay
 * Time:2018/8/23 0023
 * <p>
 * 正则校检工具类
 * </p>
 **/
public class PatternUtil {

    /**
     * URL检查
     *
     * @param pInput 要检查的字符串
     * @return boolean 返回检查结果
     */
    public static boolean isUrl(String pInput) {
        if (pInput == null) {
            return false;
        }
        String regEx = "^((https|http|ftp|rtsp|mms)?://)" + "+(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" + "|" + "([0-9a-z_!~*'()-]+\\.)*" + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." + "[a-z]{2,6})" + "(:[0-9]{1,4})?" + "((/?)|" + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(pInput);
        return matcher.matches();
    }

    /**
     * 简单密码校检
     *
     * @param pwd 密码
     * @return isTradePwd
     */
    public static boolean checkSimpleTradePwd(String pwd) {
        String regex = "^[a-zA-Z0-9]{6,20}$"; //6-20位字母，数字组合，不包含特殊字符
        Pattern p = Pattern.compile(regex);
        java.util.regex.Matcher matcher = p.matcher(pwd);
        return matcher.matches();
    }

    /**
     * 检查密码合法性，大于6位，长度小于20
     *
     * @param inputStr
     * @return
     */
    public static boolean checkPwdValid(String inputStr) {
        return !Preconditions.isNullOrEmpty(inputStr) && inputStr.length() >= 6 && inputStr.length() <= 20;
    }

    /**
     * 判断字串有4位小数
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringGold4Float(String str) throws
            PatternSyntaxException {
        String regEx =
                "^[0-9]+(.[0-9]{1,4})?$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.matches()) {
            return str;
        }
        return "";

    }

    /**
     * 判断字串只能包含中文和英文
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean stringFilter(String str) throws
            PatternSyntaxException {
        // 只允许字母和汉字
        String regEx =
                "[^a-zA-Z\u4E00-\u9FA5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();

    }

    /**
     * 检查手机号码合法性
     *
     * @param phoneNum
     * @return
     */
    public static boolean checkNormatPhoneNum(String phoneNum) {
        Pattern pattern = Pattern.compile("^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$");
        return pattern.matcher(phoneNum).matches();
    }

    /**
     * 检查邮箱是否合法性
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        Pattern pattern = Pattern.compile("^\\w{1,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    /**
     * 检查手机号码合法性(只针对位数进行检查，没有判断号段等问题)
     *
     * @param phoneNum
     * @return
     */
    public static boolean checkPhoneNum(String phoneNum) {
        Pattern pattern = Pattern.compile("^1([\\d]{10})$");
        Matcher matcher = pattern.matcher(phoneNum);
        return matcher.find();
    }

    /**
     * 检查字符是否有意义，为数字字母中文
     *
     * @param checkStr
     * @return
     */
    public static boolean checkMeaningfulCharset(String checkStr) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9\\u4e00-\\u9fa5]+$");
        return pattern.matcher(checkStr).matches();
    }

    /**
     * 检查字符是否符合15/18位身份证号
     *
     * @param idNum
     * @return
     */
    public static boolean checkIdNumCorrect(String idNum) {
        String reg = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$)";
        Pattern pattern = Pattern.compile(reg);
        return pattern.matcher(idNum).matches();
    }

}
