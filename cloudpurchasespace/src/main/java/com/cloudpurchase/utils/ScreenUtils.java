package com.cloudpurchase.utils;

import android.app.Activity;

/**
 * Created by Oscar Hu on 16-6-1.
 */
public class ScreenUtils {
    /*
    获取宽度
     */
    public static int getWidth(Activity activity){
        return activity.getWindowManager().getDefaultDisplay().getWidth();
    }
    /*
   获取高度
    */
    public static int getHeight(Activity activity){
        return activity.getWindowManager().getDefaultDisplay().getHeight();
    }

}
