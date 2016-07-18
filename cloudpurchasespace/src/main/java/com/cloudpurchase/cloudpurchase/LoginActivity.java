package com.cloudpurchase.cloudpurchase;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.entity.UserFragmentEntity;
import com.cloudpurchase.net.HttpRequest;
import com.cloudpurchase.utils.Constants;
import com.cloudpurchase.utils.EncryptionAndDecryptUtils;
import com.cloudpurchase.utils.FileUtils;
import com.cloudpurchase.utils.JsonReslove;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.utils.QQLoginUtil;
import com.cloudpurchase.utils.RequestResultIn;
import com.cloudpurchase.utils.SecurityUtil;
import com.cloudpurchase.utils.SharedFileUtils;
import com.cloudpurchase.utils.WeiBoLoginUtils;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 用户登录界面
 *
 */

/**
 * 测试用账号 18835940313 密码123456
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private EditText mUserName;//登录账号
    private EditText mUsetPwd;//登录密码
    private ImageView mBack;//返回按钮
    private Button mLogin;//登录按钮
    private Button mRegister;//注册按钮
    private ImageView mWechat;//微信登录
    private ImageView mQQ;//QQ登录
    private ImageView mSina;//新浪登录
    private TextView mProtocol;//服务协议
    private TextView mPolicy;//隐私政策
    private String userNameStr;//获取用户名
    private String userPwdStrl;//获取密码
    private TextView retrieve;//忘记密码
    private FileUtils mFile;
    private IWXAPI api;

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
        mFile = new FileUtils(this);
        mUserName = (EditText) f(R.id.login_userName);
        mUsetPwd = (EditText) f(R.id.login_userPwd);
        mLogin = (Button) f(R.id.login_login);
        mRegister = (Button) f(R.id.login_register);
        mWechat = (ImageView) f(R.id.login_wechat);
        mQQ = (ImageView) f(R.id.login_qq);
        mSina = (ImageView) f(R.id.login_sina);
        mProtocol = (TextView) f(R.id.login_protocol);
        mPolicy = (TextView) f(R.id.login_policy);
        mBack = (ImageView) f(R.id.login_back);
        retrieve = (TextView) f(R.id.login_retrieve);
        mUserName.setText("13546499658");
        mUsetPwd.setText("123456");
    }

    @Override
    public void setOnclick() {
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mWechat.setOnClickListener(this);
        mQQ.setOnClickListener(this);
        mSina.setOnClickListener(this);
        mProtocol.setOnClickListener(this);
        mPolicy.setOnClickListener(this);
        mBack.setOnClickListener(this);
        retrieve.setOnClickListener(this);
    }

    @Override
    public void initList() {

        if(userNameStr != null && userPwdStrl != null){
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.login_login:
                creatProgressDialog();//添加动画
                if(mUserName.getText() != null && mUsetPwd != null & mUsetPwd.length() != 0){
                    userNameStr = mUserName.getText().toString();
                    userPwdStrl =  mUsetPwd.getText().toString();
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("mobilephone",userNameStr);
                    map.put("password",userPwdStrl);
                    HttpRequest.getHttpRequest().requestPOST(Constants.LOGIN, null, map, new RequestResultIn() {
                        @Override
                        public void requstSuccful(String result) {
                            LogUtils.e(result);
                            UserFragmentEntity bean =
                                    new JsonReslove(LoginActivity.this)
                                            .resultUserFragment(result);
                            if(bean.code == 0){
                                Toast.makeText(LoginActivity.this,MyApplication.USER_TOKEN,Toast.LENGTH_SHORT).show();
                                Bundle bundle=new Bundle();
                                bundle.putString("flag","login");
                                toOtherActivity(HomeActivity.class, bundle);
                                finish();
                                cancelDialog();//关闭动画
                                SharedFileUtils util =
                                        new SharedFileUtils(LoginActivity.this,"USER_INFO",RegisterActivity.MODE_PRIVATE);
                                util.saveStringFile("phone", String.valueOf(userNameStr));
                                util.saveStringFile("user_id", String.valueOf(bean.userId));
                                util.saveStringFile("headImgUrl",bean.headImgUrl);
                                util.saveStringFile("userMsg",bean.userMsg);
                                util.saveStringFile("pwd",userPwdStrl);
                                util.saveStringFile("integtal", String.valueOf(bean.integral));
                                util.saveStringFile("bigGolden", String.valueOf(bean.bigGolden));
                                util.saveStringFile("smallGolden", String.valueOf(bean.smallGolden));
                                util.saveStringFile("username",bean.username);
                                util.saveStringFile("nickName",bean.nickName);
                                util.saveStringFile("sex", String.valueOf(bean.sex));
                                util.saveStringFile("qq",bean.qq);
                                util.saveStringFile("microblog",bean.microblog);
                                util.saveStringFile("wechat",bean.wechat);
                                util.saveStringFile("mobilephone",bean.mobilephone);
                                util.saveStringFile("token",bean.token);
                                util.saveStringFile("userrank", String.valueOf(bean.userrank));
                                util.saveStringFile("rankName",bean.rankName);
                                MyApplication.USER_IS_LOGIN_FLAG = true;
                            }if(bean.code != 0){
                                //返回异常信息
                                String str = bean.msg;
                                Toast.makeText(LoginActivity.this,str,Toast.LENGTH_SHORT).show();
                                cancelDialog();//关闭动画
                            }
                        }
                        @Override
                        public void requstError(VolleyError error) {
                            cancelDialog();//关闭动画
                        }
                    });
                }

                break;
            case R.id.login_register:
                toOtherActivity(RegisterActivity.class);
                break;
            case R.id.login_wechat:
                creatProgressDialog();
                api = WXAPIFactory.createWXAPI( LoginActivity. this, MyApplication.WE_CHAT_APPID,true );
                api.registerApp(MyApplication.WE_CHAT_APPID);
                final SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "carjob_wx_login";
                api.sendReq(req);//发送微信登录的请求
                cancelDialog();
                break;
            case R.id.login_qq:
                QQLoginUtil login = new QQLoginUtil(getApplicationContext(),"tencents222222");
                login.login(this);
                break;
            case R.id.login_sina:
                getInstalledPack();
                if (isFlag){
                    Toast.makeText(this, "请确认是否安装新浪微博", Toast.LENGTH_SHORT).show();
                }
                new WeiBoLoginUtils(LoginActivity.this).initWeibo();
                break;
            case R.id.login_protocol:

                break;
            case R.id.login_policy:

                break;
            case R.id.login_retrieve:
                toOtherActivity(FindPasswordActivity.class);
                break;
            case R.id.login_back:
                finish();
                toOtherActivity(HomeActivity.class);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }*/
    }
    private boolean isFlag;
    public void getInstalledPack(){
        PackageManager  pm=this.getPackageManager();
        List<PackageInfo> list=pm.getInstalledPackages(0);
        for (PackageInfo packageInfo:list){
            String packageName=packageInfo.packageName;
            if (!packageName.contains("sina.weibo")){
                isFlag=true;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            toOtherActivity(HomeActivity.class);
        }
        return super.onKeyDown(keyCode, event);
    }
    /*
   查询短信验证码余额
    */
    private void balanceQuery(){
        String url = "http://sms.hspaas.com:8080/msgHttp/json/balance";
        Date mDate = new Date();
        long mTime = mDate.getTime();
        String sourceStr = String.valueOf(mTime);
        String mTimeMD5 = null;
        //进行加密处理（必须经过加密才可上传到服务器）
        mTimeMD5 = SecurityUtil.getMD532Str(MyApplication.SHORT_MESSAGE_INTERFACE_KEY + sourceStr);
        long timestamps = mDate.getTime();
        HashMap<String , String > map = new HashMap<String ,String >();
        map.put("account",MyApplication.SHORT_MESSAGE_INTERFACE);
        map.put("password",mTimeMD5);
        map.put("timestamps",String.valueOf(timestamps));
        HttpRequest.getHttpRequest().requestPOST(url, null, map, new RequestResultIn() {
            @Override
            public void requstSuccful(String result) {
                Toast.makeText(LoginActivity.this,result,Toast.LENGTH_SHORT ).show();
                LogUtils.e(result);
            }

            @Override
            public void requstError(VolleyError error) {
                Toast.makeText(LoginActivity.this, (CharSequence) error,Toast.LENGTH_SHORT ).show();
            }
        });
    }


}
