package com.cloudpurchase.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by oscar on 2016/7/9.
 * 格式化时间
 */
public class FormatTime {
    public static String getTime(Long totalTime) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
//        long totalTime=0;
//        try {
//            //long millionSeconds = sdf.parse(str).getTime();//毫秒
//            Long currentTime=System.currentTimeMillis();
//            //totalTime=millionSeconds-currentTime;
//            //totalTime=1000*60*6;
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = totalTime / dd;
        long hour = (totalTime - day * dd) / hh;
        long minute = (totalTime - day * dd - hour * hh) / mi;
        long second = (totalTime - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = (totalTime - day * dd - hour * hh - minute * mi - second * ss)/10;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        return strMinute + ":" + strSecond+ ":" + strMilliSecond;
    }
}
