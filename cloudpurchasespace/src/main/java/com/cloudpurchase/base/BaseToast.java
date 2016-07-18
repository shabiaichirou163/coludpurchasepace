package com.cloudpurchase.base;

import android.content.Context;
import android.widget.Toast;

/**
 * BaseToast,所有Toast的基类，
 *
 * 构造器接受Context对象，可以显示在任何Activity中
 *
 * @author WangLiang
 */
public class BaseToast {
    private Context context;
    private Toast toast;

    public BaseToast(Context context){
        this.context = context;
        toast = new Toast(context);
    }
    /*
       短时间显示Toast
        str：要显示的内容
     */
    public void shortToast(String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }

    /*
        长时间显示Toast
        str:要显示的内容
     */
    public void longToast(String str){
        Toast.makeText(context,str,Toast.LENGTH_LONG).show();
    }
}
