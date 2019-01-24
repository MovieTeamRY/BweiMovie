package com.bw.movie.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegularUtils {
    /**
     * 验证输入手机号码
     */
    public static boolean IsHandset(String str) {
        String regex = "^[1]+[3,5]+\\d{9}$";
        return match(regex, str);
    }

    /**
     * 验证输入密码条件(字符与数据同时出现)
     */
    public static boolean IsPassword(String str) {
        String regex = "[A-Za-z]+[0-9]";
        return match(regex, str);
    }

    /**
     * @param regex
     * 正则表达式字符串
     * @param str
     * 要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     11      * 正则表达式:验证用户名(不包含中文和特殊字符)如果用户名使用手机号码或邮箱 则结合手机号验证和邮箱验证
     12      */
     public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";
     /**
        * 正则表达式:验证密码(不包含特殊字符)
       */
     public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{1,12}$";

              /**
        * 正则表达式:验证手机号
        */
              public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

              /**
        * 正则表达式:验证邮箱
        */
              public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

              /**
        * 正则表达式:验证汉字(1-9个汉字)  {1,9} 自定义区间
        */
              public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5]{1,9}$";

              /**
        * 正则表达式:验证身份证
        */
              public static final String REGEX_ID_CARD = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";

              /**
        * 正则表达式:验证URL
        */
              public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

              /**
        * 正则表达式:验证IP地址
        */
              public static final String REGEX_IP_ADDR = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";

              /**
        * 校验用户名
        *
        * @param username
        * @return 校验通过返回true，否则返回false
        */
              public static boolean isUserName(String username) {
                 return Pattern.matches(REGEX_USERNAME, username);
             }

              /**
       * 校验密码
        *
        * @param password
        * @return 校验通过返回true，否则返回false
        */
              public static boolean isPassword(String password) {
                 return Pattern.matches(REGEX_PASSWORD, password);
             }

              /**
        * 校验手机号
        *
        * @param mobile
        * @return 校验通过返回true，否则返回false
        */
              public static boolean isMobile(String mobile) {
                 return Pattern.matches(REGEX_MOBILE, mobile);
             }

              /**
        * 校验邮箱
        *
        * @param email
        * @return 校验通过返回true，否则返回false
       */
              public static boolean isEmail(String email) {
                 return Pattern.matches(REGEX_EMAIL, email);
             }

              /**
        * 校验汉字
        *
        * @param chinese
        * @return 校验通过返回true，否则返回false
        */
              public static boolean isChinese(String chinese) {
                 return Pattern.matches(REGEX_CHINESE, chinese);
             }

              /**
        * 校验身份证
        *
        * @param idCard
        * @return 校验通过返回true，否则返回false
        */
              public static boolean isIDCard(String idCard) {
                 return Pattern.matches(REGEX_ID_CARD, idCard);
             }

             /**
       * 校验URL
       *
       * @param url
       * @return 校验通过返回true，否则返回false
       */
             public static boolean isUrl(String url) {
                 return Pattern.matches(REGEX_URL, url);
             }
     /**
       * 校验IP地址
       *
       * @param ipAddress
       * @return
       */
             public static boolean isIPAddress(String ipAddress) {
                 return Pattern.matches(REGEX_IP_ADDR, ipAddress);
             }
}
