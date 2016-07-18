
package com.cloudpurchase.utils;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * 微博登录工具类
 */

public class WeiBoLoginUtils{
    private AuthInfo mAuthInfo;
    private Context context;
    private Oauth2AccessToken token;
    private SsoHandler mSsoHandler;

    public WeiBoLoginUtils(Context context){
        this.context = context;
        /*
        创建授权对象
         */
        mAuthInfo = new AuthInfo(context,Constants.APP_KEY, Constants.REDIRECT_URL, null);
    }
    public void initWeibo(){
        mSsoHandler = new SsoHandler((Activity) context, mAuthInfo);
        mSsoHandler. authorizeClientSso(new AuthListener());
    }
    /**
     * 实现监听接口
     *
     * 授权成功后才可以取到返回值
     *
     */
    protected class AuthListener implements WeiboAuthListener {
        /*
         取消请求
         */
        @Override
        public void onCancel() {

        }
        /*
        请求成功返回值
         */
        @Override
        public void onComplete(Bundle bundle) {
            token = Oauth2AccessToken.parseAccessToken(bundle);//从Bundle 中解析Token
            if (token.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(context , token); //保存Token
            } else {
                // 当注册的应用程序签名不正确时，就会收到错误Code，请确保签名正确
                String code = bundle.getString("code", "");
            }
        }
        /*
        请求错误返回值
         */
        @Override
        public void onWeiboException(WeiboException e) {

        }
    }
}
