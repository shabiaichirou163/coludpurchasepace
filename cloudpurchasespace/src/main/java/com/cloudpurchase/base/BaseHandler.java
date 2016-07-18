package com.cloudpurchase.base;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.cloudpurchase.utils.LogUtils;

/**
 * BaseHandler类，处理整个应用程序的线程间交互信息
 *
 * @author WangLiang
 */
public class BaseHandler extends Handler {
    private LogUtils log;
    private Bundle bundle;
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        bundle = (Bundle) msg.obj;
        log = new LogUtils();
        if(msg.what == 202){
            LogUtils.e(bundle.getString("202"));
        }
        if(msg.what == 404){
            LogUtils.e(""+bundle.getString("404"));
        }
    }
}
