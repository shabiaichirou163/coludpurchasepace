package com.cloudpurchase.utils;

import android.util.Log;

/**
 * LogUtils，统一打印Log信息
 *
 * 在创建时需要传入当前对象“this”，方便查看Log日志时知道Log信息的出处
 *
 * @author WangLiang
 *
 */
public class LogUtils {
    private static final String TAG="Cloudpurchase";
    private static boolean isTrue=true;
    public static void e(String msg){
        if(isTrue){
            Log.e(TAG, msg);
        }
    }
    public static void i(String msg){
        if(isTrue){
            Log.i(TAG, msg);
        }
    }

}

