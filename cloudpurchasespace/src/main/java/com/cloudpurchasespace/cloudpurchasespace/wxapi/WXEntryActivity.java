package com.cloudpurchasespace.cloudpurchasespace.wxapi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.cloudpurchase.base.BaseHandler;
import com.cloudpurchase.cloudpurchase.HomeActivity;
import com.cloudpurchase.cloudpurchase.MyApplication;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.net.HttpRequest;
import com.cloudpurchase.utils.EncryptionAndDecryptUtils;
import com.cloudpurchase.utils.FileUtils;
import com.cloudpurchase.utils.RequestResultIn;
import com.cloudpurchase.utils.SharedFileUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.LogRecord;

/**
 * 微信登录回调Activity
 *
 * 微信支付成功后回调地支付结果界面
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        creatProgressDialog();
        api = WXAPIFactory.createWXAPI(this, MyApplication.WE_CHAT_APPID,true);
        api.handleIntent(getIntent(), this);
    }
    private Dialog mProgressDialog;
    /*
      *显示ProgressDialog
      */
    public void creatProgressDialog(){
        View view= LayoutInflater.from(this).inflate(R.layout.progressdialog_layout,null);
        //ImageView img= (ImageView) view.findViewById(R.id.progressdialog_img);
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                this, R.anim.progress_animation);
        // 使用ImageView显示动画
        //img.startAnimation(hyperspaceJumpAnimation);
        mProgressDialog=new Dialog(this,R.style.loading_dialog);
        //mProgressDialog.setCancelable(false);// 不可以用“返回键”取消
        mProgressDialog.setContentView(view);
        mProgressDialog.show();
    }
    /*
     *取消自定义ProgressDialog
     */
    public void cancelDialog(){
        if (null!=mProgressDialog){
            mProgressDialog.dismiss();
            mProgressDialog.cancel();
            mProgressDialog=null;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                code = ((SendAuth.Resp) baseResp).code; //即为所需的code
                getUserCode(code);
                break;
        }

        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            String result = "支付结果" + String.valueOf(baseResp.errCode);
        }
    }
    private String str;
    /*
    appid：  应用唯一标识，在微信开放平台提交应用审核通过后获得
    secret：应用密钥AppSecret，在微信开放平台提交应用审核通过后获得
    code    ：填写第一步获取的code参数
    grant_type：固定值，填authorization_code
    https://api.weixin.qq.com/sns/oauth2/access_token?
    appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
     */
    private void getUserCode(String code){
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
                +"appid="+MyApplication.WE_CHAT_APPID+"" +
                "&secret="+MyApplication.WE_CHAT_SECRET+ "" +
                "&code="+code+
                "&grant_type=authorization_code";
        HttpRequest.getHttpRequest().requestGET(url, null, new RequestResultIn() {
            @Override
            public void requstSuccful(String result) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if(obj == null){
                        return;
                    }
                    String access_token =  obj.getString("access_token");//接口调用凭证
                    String expires_in =  obj.getString("expires_in");//access_token接口调用凭证超时时间，单位（秒）
                    String refresh_token =  obj.getString("refresh_token");//用户刷新access_token
                    String openid = obj.getString("openid");//授权用户唯一标识
                    String scope = obj.getString("scope");//用户授权的作用域，使用逗号（,）分隔

                    String getAppIDUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?" +
                            "appid="+MyApplication.WE_CHAT_APPID+
                            "&grant_type=refresh_token" +
                            "&refresh_token="+refresh_token;
                    getAppID(getAppIDUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requstError(VolleyError error) {

            }
        });
    }

    private void getAppID(String url){
        HttpRequest.getHttpRequest().requestGET(url, null, new RequestResultIn() {
            @Override
            public void requstSuccful(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    String access_token = obj.getString("access_token");
                    obj.getString("expires_in");
                    obj.getString("refresh_token");
                    String openid = obj.getString("openid");
                    obj.getString("scope");
                    String url ="https://api.weixin.qq.com/sns/userinfo?" +
                            "access_token="+access_token+
                            "&openid="+openid;
                    getUserMsg(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void requstError(VolleyError error) {

            }
        });
    }
    private void getUserMsg(String url){
        HttpRequest.getHttpRequest().requestGET(url, null, new RequestResultIn() {
            @Override
            public void requstSuccful(String result) {
                Message msg = new Message();
                msg.obj = result;
                handler.sendMessage(msg);
            }
            @Override
            public void requstError(VolleyError error) {

            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            JSONObject obj = null;
            try {
                obj = new JSONObject(result);
                String openid = obj.getString("openid");//普通用户的标识，对当前开发者帐号唯一
                String nickname = obj.getString("nickname");//普通用户昵称
                String sex = obj.getString("sex");//普通用户性别，1为男性，2为女性
                String province = obj.getString("province");//普通用户个人资料填写的省份
                String city= obj.getString("city");//普通用户个人资料填写的城市
                String country = obj.getString("country");//国家，如中国为CN
                String headimgurl = obj.getString("headimgurl");//用户头像
                String unionid = obj.getString("unionid");//用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
                SharedFileUtils utils = new SharedFileUtils(WXEntryActivity.this,"USER_INFO",Activity.MODE_PRIVATE);
                utils.saveStringFile("headImgUrl",headimgurl);
                utils.saveStringFile("nickName",nickname);
                utils.saveStringFile("sex",sex);
                MyApplication.USER_IS_LOGIN_FLAG = true;
                MyApplication.USER_NICKNAME = nickname;
                MyApplication.USER_SEX = sex;
                cancelDialog();
                WXEntryActivity.this.finish();
                Intent intent=new Intent(WXEntryActivity.this, HomeActivity.class);
                Bundle b=new Bundle();
                b.putString("flag","login");
                intent.putExtras(b);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.handleMessage(msg);
        }
    };

}