package com.cloudpurchase.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 使用SharedPreferences 进行本地存储
 *
 */
public class SharedFileUtils {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    /**
     *
     * @param context
     * @param name 要操作的文件名
     * @param power 添加权限
     */
    public SharedFileUtils(Context context,String name,int power){
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(name,power);
        editor = sharedPreferences.edit();
    }

    /**
     * 保存一个文件
     * @param key key
     * @param saveValue 值
     */
    public void saveStringFile(String key, String saveValue){
        editor.putString(key,saveValue);
        editor.commit();
    }

    /**
     * 删除一个文件
     */
    public void delFile(){
        editor.clear();//清除方法
        editor.commit();
    }


    /**
     * 读取一个文件
     * @param key key
     * @param defaultInfo 默认值
     * @return
     */
    public String readStringFile(String key , String defaultInfo){
        String str = sharedPreferences.getString(key,defaultInfo);
        return str;
    }



}
