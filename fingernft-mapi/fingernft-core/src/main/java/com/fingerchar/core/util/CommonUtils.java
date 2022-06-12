package com.fingerchar.core.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机号码正则工具类
 * 校验规则来自： https://github.com/VincentSit/ChinaMobilePhoneNumberRegex
 */
public class CommonUtils {

    //手机号码检验正则
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^(?:\\+?86)?1(?:3\\d{3}|5[^4\\D]\\d{2}|8\\d{3}|7(?:[0-35-9]\\d{2}|4(?:0\\d|1[0-2]|9\\d))|9[0-35-9]\\d{2}|6[2567]\\d{2}|4(?:(?:10|4[01])\\d{3}|[68]\\d{4}|[579]\\d{2}))\\d{6}$");

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(\\w+((-\\w+)|(\\.\\w+))*)+\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");

    /**
     * 检验指定的字符串是否为符合格式的手机号码
     *
     * @param phoneNumber 手机号码字符串
     * @return
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return false;
        }
        Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
        return matcher.find();
    }

    public static boolean isEmail(String email) {
        if(StringUtils.isEmpty(email)) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.find();
    }

    public static void main(String[] args) {
        System.out.println(isEmail("alanhu@dbpower.com.hk"));
    }
}
