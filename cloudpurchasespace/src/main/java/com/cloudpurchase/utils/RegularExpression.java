package com.cloudpurchase.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 */
public class RegularExpression {
    /*
 正则表达式，验证邮箱
  */
    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    /*
    正则表达式，验证手机号
     */
    public static boolean isMobileNO(String mobiles){
        Pattern p = Pattern
                .compile("^((1[3-9][0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    /*
       正则表达式，验证QQ号
        */
    public static boolean isQQNO(String qq){
        Pattern p = Pattern
                .compile("[1-9][0-9]{4,}");
        Matcher m = p.matcher(qq);
        return m.matches();
    }
}
