package com.cloudpurchase.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 判断网络状态工具类
 *
 * 构造器接受一个上下文对象
 *
 * 需要在Minidest中添加权限：
 *     <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
 * @author Augustine
 */
public class IsNetUtils {
    private Context context;

    public IsNetUtils(Context context){
        this.context = context;
    }
    /*
     * 判断网络状态方法，、
     * return true:有网络
     * return false：无网络
     */
    public boolean isNet(){
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info;
        NetworkInfo.State state;
        info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(info != null){
            state = info.getState();
            if(state!=null){
                if(state == NetworkInfo.State.CONNECTED){
                    //有wifi
                    Log.w("","IsNetUtils--wifi");
                    return true;
                }
            }
        }
        info = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(info != null){
            state = info.getState();
            if(state!=null){
                if(state == NetworkInfo.State.CONNECTED){
                    //有移动网络
                    Log.w("","IsNetUtils--有移动网");
                    return true;
                }
            }
        }
        return false;
    }
}
