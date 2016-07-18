package com.cloudpurchase.cloudpurchase;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.base.BaseToast;
import com.cloudpurchase.net.HttpRequest;
import com.cloudpurchase.utils.Constants;
import com.cloudpurchase.utils.FileUtils;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.utils.RegularExpression;
import com.cloudpurchase.utils.RequestResultIn;
import com.cloudpurchase.utils.SecurityUtil;
import com.cloudpurchase.utils.SharedFileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 * 用户注册界面
 *
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mBack;
    private EditText mName;//要注册的账号
    private EditText mPwd;//输入的验证码
    private Button mGetCode;//获得验证码
    private Button mStart;//注册提交按钮
    private TextView mProtocol;//用户协议
    public int time = 60;//重新获取验证码倒计时
    private boolean flag = true;//是否可以重新获取验证码标志
    private BaseToast mBaseToast;
    private String URL = "http://sms.hspaas.com:8080/msgHttp/json/mt";
    private Random mRandom;
    private int mIntCode = 0;//生成的验证码  TODO 将来要讲初始值改为 -123456
    private FileUtils mFileUtils;
    private MyApplication mApplication;//全局application

    @Override
    public void initView() {
        setContentView(R.layout.activity_register);
        mApplication = (MyApplication) this.getApplicationContext();
        mBack = (ImageView) f(R.id.register_back);
        mName = (EditText) f(R.id.register_name);
        mPwd = (EditText) f(R.id.register_pwd);
        mGetCode = (Button) f(R.id.register_getCode);
        mStart = (Button) f(R.id.register_start);
        mProtocol = (TextView) f(R.id.register_protocol);
        mBaseToast=new BaseToast(this);
        mRandom = new Random();
        mFileUtils = new FileUtils(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void setOnclick() {
        mBack.setOnClickListener(this);
        mGetCode.setOnClickListener(this);
        mStart.setOnClickListener(this);
        mProtocol.setOnClickListener(this);
    }

    @Override
    public void initList() {

    }
    boolean checkFlag = false;//判断手机号是否已被注册
    @Override
    public void onClick(View v) {

        int id = v.getId();
        Intent mIntent = null;
        switch(id){
            case R.id.register_back:
                if(MyApplication.USER_IS_LOGIN_FLAG){
                    //如果登陆成功
                    mApplication.setmSeclectFrag("userFragment");
                    mIntent = new Intent(this,HomeActivity.class);//重新跳转至HomeActivity
                    startActivity(mIntent);
                }
                else{
                    //没有登陆
                    mIntent = new Intent(this,HomeActivity.class);//重新跳转至HomeActivity
                    startActivity(mIntent);
                }
                finish();
                break;
            //发送网络请求获取验证码
            case R.id.register_getCode:
                creatProgressDialog();
                if(flag){
                    String phone = mName.getText().toString().trim();
                    if(phone.equals("")){
                        cancelDialog();
                        return;
                    }
                    String url = Constants.REGISTER_CHAECK;
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("mobilephone",phone);
                    Toast.makeText(RegisterActivity.this,phone,Toast.LENGTH_SHORT).show();
                    HttpRequest.getHttpRequest().requestPOST(url, null, map, new RequestResultIn() {
                        @Override
                        public void requstSuccful(String result) {
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(result);
                                String code = obj.getString("code");
                                Toast.makeText(RegisterActivity.this,code,Toast.LENGTH_SHORT).show();
                                if(code.equals("0")){
                                    //使用正则表达式，判断是否输入时合法手机号
                                    String mobile = mName.getText().toString().trim();
                                    if(mobile.length() > 8 &&RegularExpression.isMobileNO(mobile)){
                                        thraed();
                                        time = 60;
                                        cancelDialog();
                                        mIntCode = mRandom.nextInt(100000)+100000;//生成一个 100000 到 100000 - 1 的随机数
                                        LogUtils.e("生成的验证码是："+mIntCode);
                                        Date mDate = new Date();
                                        long mTime = mDate.getTime();
                                        String sourceStr = String.valueOf(mTime);
                                        String mTimeMD5 = null;
                                        //进行加密处理（必须经过加密才可上传到服务器）
                                        mTimeMD5 = SecurityUtil.getMD532Str(MyApplication.SHORT_MESSAGE_INTERFACE_KEY+mobile+sourceStr);
                                        HashMap<String,String> mHashMap = new HashMap<String,String>();
                                        mHashMap.put("account",MyApplication.SHORT_MESSAGE_INTERFACE);
                                        mHashMap.put("password",mTimeMD5);
                                        mHashMap.put("mobile",mobile);
                                        mHashMap.put("content","【云购空间】您的验证码是# "+mIntCode+" #");
                                        mHashMap.put("timestamps",""+mTime);
                                        HttpRequest.getHttpRequest().requestPOST(URL, null , mHashMap, new RequestResultIn() {
                                            @Override
                                            public void requstSuccful(String result) {
                                                cancelDialog();
                                                Toast.makeText(RegisterActivity.this,result.toString(),Toast.LENGTH_LONG).show();
                                            }
                                            @Override
                                            public void requstError(VolleyError error) {
                                                cancelDialog();
                                                Toast.makeText(RegisterActivity.this,"请求失败"+error.toString(),Toast.LENGTH_SHORT).show();
                                            }
                                    });}else{
                                        mBaseToast.shortToast("请输入正确的手机号");
                                    }

                                }else if(code.equals("1")){
                                    checkFlag = true;
                                    cancelDialog();
                                    Toast.makeText(RegisterActivity.this,"手机号已被注册",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void requstError(VolleyError error) {
                            cancelDialog();
                        }
                    });

                }else{
                    mBaseToast.shortToast("请稍后再试");
                    cancelDialog();
                }
                break;
            case R.id.register_start:
                creatProgressDialog();
                if(mPwd.getText().length() == 0 || mName.getText().length() == 0){
                    mBaseToast.shortToast("确认是否有误");
                    cancelDialog();
                } else{
                    //判断用户输入的验证码是否与生成的验证码一致
                    if(mIntCode == Integer.parseInt(String.valueOf(mPwd.getText()))){
                        cancelDialog();
                        //将注册的账号进行加密后存入本地
//                        MyApplication.USER_PHONE = mName.getText().toString().trim();
//                        mFileUtils.saveTxt("USER_ID", EncryptionAndDecryptUtils.decode(MyApplication.USER_PHONE));
                        if(checkFlag){
                            cancelDialog();
                        }else{
                            String phone = mName.getText().toString().trim();
                            mIntent = new Intent(RegisterActivity.this,FindPasswordActivity.class);
                            mIntent.putExtra("phone",phone);
                            mIntent.putExtra("flag","true");
                            startActivity(mIntent);
                            SharedFileUtils util =
                                    new SharedFileUtils(RegisterActivity.this,"USER_INFO",RegisterActivity.MODE_PRIVATE);
                            util.saveStringFile("phone",phone);
                            //跳转到找回密码界面，此时找回密码界面加载是的是设置密码界面
                            cancelDialog();
                            finish();
                        }
                    }else {
                        cancelDialog();
                        mBaseToast.shortToast("确认验证码是否有误");
                    }
                }
                break;
            case R.id.register_protocol:
                mBaseToast.shortToast("用户协议");
                break;
        }
    }

    /*
    监听返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(MyApplication.USER_IS_LOGIN_FLAG){
                //如果登陆成功
                mApplication.setmSeclectFrag("userFragment");
                Intent mIntent = new Intent(this,HomeActivity.class);//重新跳转至HomeActivity
                startActivity(mIntent);
            }
            else{
                //没有登陆
                Intent mIntent = new Intent(this,HomeActivity.class);//重新跳转至HomeActivity
                startActivity(mIntent);
            }
            finish();
            return false;
        }else{
            return super.onKeyDown(keyCode, event);
        }
    }

    private void thraed() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                while(time != 0 ){
                    time = time-1;
                    handler.sendEmptyMessage(time);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread t = new Thread(run);
        t.start();
        if(time == 0){
            t.interrupt();//关闭线程
        }
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (time != 0) {
                flag = false;
                mGetCode.setBackgroundResource(R.mipmap.yzm_1_2x);
                mGetCode.setText(time + "s后重新获取");
            } else if (time == 0) {
                mGetCode.setText("获取验证码");
                mGetCode.setBackgroundResource(R.mipmap.yzm2x);
                flag = true;
            }
        }

    };
}