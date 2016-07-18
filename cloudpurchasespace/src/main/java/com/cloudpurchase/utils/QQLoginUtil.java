package com.cloudpurchase.utils;

import android.app.Activity;
import android.content.Context;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * QQ登录工具类
 *
 */
public class QQLoginUtil {
    private Tencent mTencent;
    public static QQAuth mQQAuth;
    public static String mAppid;
    public String openID;//QQ号
    public String nickName;//QQ昵称


    public QQLoginUtil(Context context,String APP_ID){
        mAppid = APP_ID;//申请的APP_ID
        /*
        第一个参数就是上面所说的申请的APPID，
        第二个是全局的Context上下文，这句话实现了调用QQ登录
         */
        mQQAuth = QQAuth.createInstance(APP_ID,context);
        mTencent = Tencent.createInstance(mAppid, context);

    }
    public void login(Context context){
        IUiListener listennner = new IUiListener() {
            @Override
            public void onComplete(Object o) {

                LogUtils.e("登录成功");
                LogUtils.e(o.toString());
            }

            @Override
            public void onError(UiError uiError) {

                LogUtils.e("登录失败");
            }

            @Override
            public void onCancel() {
                LogUtils.e("登录取消");
            }
        };
//        mTencent.loginWithOEM((Activity) context,"all",listennner,"10000144","10000144","xxxx");
        mTencent.login((Activity) context,"all",listennner);
    }


    /*
    获取用户头像
     */
    public void getUserInfo(Context context){
        QQToken qqToken = mTencent.getQQToken();
        UserInfo info = new UserInfo(context, qqToken);
        info.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                //TODO  获取用户头像
                LogUtils.e("登录成功");
                LogUtils.e(o.toString());
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * onComplete：登录成功
     * onError：登录错误
     * onCancel:登录取消
     */
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            LogUtils.e("登录成功");
            LogUtils.e(o.toString());

        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {
            LogUtils.e("登录取消");
        }
    }
}
