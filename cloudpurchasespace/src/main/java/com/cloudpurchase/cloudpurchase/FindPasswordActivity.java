package com.cloudpurchase.cloudpurchase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.entity.UserFragmentEntity;
import com.cloudpurchase.net.HttpRequest;
import com.cloudpurchase.utils.Constants;
import com.cloudpurchase.utils.EncryptionAndDecryptUtils;
import com.cloudpurchase.utils.JsonReslove;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.utils.RegularExpression;
import com.cloudpurchase.utils.RequestResultIn;
import com.cloudpurchase.utils.SecurityUtil;
import com.cloudpurchase.utils.SharedFileUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 * 找回密码界面
 *
 * 通过addView() 使activity加载不同状态的布局
 *
 */
public class FindPasswordActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout myView;//切换视图的view
    private ImageView mBack;
    private View firstStep;//输入账号的view
    private View secondStep;//获取验证码的view
    private View thirdStep;//设置新密码的view

    private EditText mFirstStepET;//第一步中的输入框
    private Button mFirstStepBtn;//第一步中的“下一步”按钮

    private TextView mSecondEDNumber;//要找回的手机号
    private TextView mSecondBindingNum;//绑定的手机号
    private Button mSecondGetCode;//获取验证码按钮
    private Button mSecondReGerCode;//重新获取验证码
    private EditText mSecondEDCode;//验证码的输入框
    private LinearLayout mSencondLienar;//控制隐藏显示view
    private Button mSecondBtn;//第二步中的“下一步”按钮

    private EditText mThirdED;//设置新密码
    private EditText mThirdED2;//再次输入的密码
    private Button mThirdBtn;//第三步中的“确认”按钮

    private int time = 60;//重新获取验证码倒计时
    private boolean flag = true;//是否可以点击重新获取验证码按钮 flag

    private String URL = "http://sms.hspaas.com:8080/msgHttp/json/mt";//短信验证码
    private Random mRandom;
    private int mIntCode = 0;//生成的验证码  TODO 将来要讲初始值改为 -123456
    private String intentFlag;//如果是注册界面调起此界面则加载设置密码界面
    private String intentPhone;//从注册界面传递过来的手机号
    private Intent intent;
    /*
    界面初始化时，讲三个view进行解析并且添加到父级容器中
    在点击事件中判断显示某一个view
     */
    @Override
    public void initView() {
        setContentView(R.layout.activity_find_pwd);
        mBack = (ImageView) f(R.id.findPwd_back);
        myView = (LinearLayout) f(R.id.findPwd_view);
        firstStep = View.inflate(this,R.layout.view_findpwd_first_step,null);
        mFirstStepET = (EditText) f(firstStep,R.id.findPwd_first_editText);
        mFirstStepBtn = (Button) f(firstStep,R.id.findPwd_first_btn);

        secondStep = View.inflate(this,R.layout.view_findpwd_second_step,null);
        mSecondEDNumber = (TextView) f(secondStep,R.id.findPwd_second_EDNumber);
        mSecondBindingNum = (TextView) f(secondStep,R.id.findPwd_second_bindingNum);
        mSecondGetCode = (Button) f(secondStep,R.id.findPwd_second_getCode);
        mSecondReGerCode = (Button) f(secondStep,R.id.findPwd_second_reGetCode);
        mSecondEDCode = (EditText) f(secondStep,R.id.findPwd_second_EDCode);
        mSencondLienar = (LinearLayout) f(secondStep,R.id.findPwd_second_View);
        mSecondBtn = (Button) f(secondStep,R.id.findPwd_second_btn);

        thirdStep = View.inflate(this,R.layout.view_findpwd_third_step,null);
        mThirdED  = (EditText) f(thirdStep,R.id.findPwd_third_ED);
        mThirdED2 = (EditText) f(thirdStep,R.id.findPwd_third_ED2);
        mThirdBtn = (Button) f(thirdStep,R.id.findPwd_third_btn);
        intent = getIntent();
        intentFlag = intent.getStringExtra("flag");
        intentPhone = intent.getStringExtra("phone");
        Toast.makeText(this,"intentFlag:"+intentFlag,Toast.LENGTH_SHORT).show();
        Toast.makeText(this,"phone:"+intentPhone,Toast.LENGTH_SHORT).show();
        if(intent.getStringExtra("flag")!=null){
            //如果进入到这里，表示是从注册界面跳转过来
            TextView tv = (TextView) f(R.id.findPwd_titleTv);
            tv.setText("设置密码");
            myView.addView(thirdStep);//加载第三步 view
        }else{
            myView.addView(firstStep);//在此只将第一步的布局添加进去
        }
        mRandom = new Random();
    }

    @Override
    public void setOnclick() {
        mBack.setOnClickListener(this);
        mFirstStepBtn.setOnClickListener(this);
        mSecondGetCode.setOnClickListener(this);
        mSecondBtn.setOnClickListener(this);
        mThirdBtn.setOnClickListener(this);
        mSecondReGerCode.setOnClickListener(this);
    }

    @Override
    public void initList() {

    }
    String phone = null;
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.findPwd_back:
                finish();
                break;
            case R.id.findPwd_first_btn:
                //第一步按钮
                phone = mFirstStepET.getText().toString().trim();
                Log.e("112233",""+phone);
                if(phone.length() > 8 && !phone.equals("")
                        && RegularExpression.isMobileNO(phone)){
                    myView.removeView(firstStep);
                    myView.addView(secondStep);
                    mSencondLienar.setVisibility(View.GONE);
                    mSecondEDNumber.setText(phone);
                }else{
                    Toast.makeText(this,"请填写正确的手机号",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.findPwd_second_getCode:
                //第二步获取验证码
                creatProgressDialog();
                if(phone == null){
                    cancelDialog();
                }else{
                    mSecondGetCode.setVisibility(View.GONE);
                    mSencondLienar.setVisibility(View.VISIBLE);
                    cancelDialog();
                    if(flag){
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
                        mTimeMD5 = SecurityUtil.getMD532Str(MyApplication.SHORT_MESSAGE_INTERFACE_KEY+phone+sourceStr);
                        HashMap<String,String> mHashMap = new HashMap<String,String>();
                        mHashMap.put("account",MyApplication.SHORT_MESSAGE_INTERFACE);
                        mHashMap.put("password",mTimeMD5);
                        mHashMap.put("mobile",phone);
                        mHashMap.put("content","【云购空间】您的验证码是# "+mIntCode+" #");
                        mHashMap.put("timestamps",""+mTime);
                        HttpRequest.getHttpRequest().requestPOST(URL, null , mHashMap, new RequestResultIn() {
                            @Override
                            public void requstSuccful(String result) {
                                Toast.makeText(FindPasswordActivity.this,result.toString(),Toast.LENGTH_LONG).show();
                                cancelDialog();
                            }
                            @Override
                            public void requstError(VolleyError error) {
                                Toast.makeText(FindPasswordActivity.this,"请求失败"+error.toString(),Toast.LENGTH_SHORT).show();
                                cancelDialog();
                            }
                        });
                    }else{
                        Toast.makeText(this,"请稍后再试",Toast.LENGTH_SHORT).show();
                        cancelDialog();
                    }
                }
                break;
            case R.id.findPwd_second_reGetCode:
                //第二部重新获取验证码按钮
                creatProgressDialog();
                if(flag){
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
                    mTimeMD5 = SecurityUtil.getMD532Str(MyApplication.SHORT_MESSAGE_INTERFACE_KEY+phone+sourceStr);
                    HashMap<String,String> mHashMap = new HashMap<String,String>();
                    mHashMap.put("account",MyApplication.SHORT_MESSAGE_INTERFACE);
                    mHashMap.put("password",mTimeMD5);
                    mHashMap.put("mobile",phone);
                    mHashMap.put("content","【云购空间】您的验证码是# "+mIntCode+" #");
                    mHashMap.put("timestamps",""+mTime);
                    HttpRequest.getHttpRequest().requestPOST(URL, null , mHashMap, new RequestResultIn() {
                        @Override
                        public void requstSuccful(String result) {
                            Toast.makeText(FindPasswordActivity.this,result.toString(),Toast.LENGTH_LONG).show();
                            cancelDialog();
                        }
                        @Override
                        public void requstError(VolleyError error) {
                            Toast.makeText(FindPasswordActivity.this,"请求失败"+error.toString(),Toast.LENGTH_SHORT).show();
                            cancelDialog();
                        }
                    });
                }else{
                    Toast.makeText(this,"请稍后重试",Toast.LENGTH_SHORT).show();
                    cancelDialog();
                }
                break;
            case R.id.findPwd_second_btn:
                creatProgressDialog();
                //第二步"下一步按钮"
                if(mSecondEDCode.getText() == null){
                    Toast.makeText(this,"请填写验证码",Toast.LENGTH_SHORT).show();
                    cancelDialog();
                }else{
                    if(mIntCode == Integer.parseInt(String.valueOf(mSecondEDCode.getText()))){
                        myView.removeView(secondStep);
                        myView.addView(thirdStep);
                        cancelDialog();
                    }else{
                        Toast.makeText(this,"验证码有误",Toast.LENGTH_SHORT).show();
                        cancelDialog();
                    }
                }
                break;
            case R.id.findPwd_third_btn:
                //第三步 确认按钮
                /**
                 *   private EditText mThirdED;//设置新密码
                 private EditText mThirdED2;//再次输入的密码
                 */
                creatProgressDialog();
                if(!mThirdED.getText().toString().equals("")||!mThirdED2.getText().toString().equals("")){
                    //表示两个输入框不为空
                    cancelDialog();
                    if(mThirdED.getText().toString().equals(mThirdED2.getText().toString())){
                        //表示两个输入框内容相同
                        if(intent.getStringExtra("flag") != null){
                            creatProgressDialog();
                            //此时表示 是从注册界面呢跳转过来
                            final String pwd = mThirdED.getText().toString();
                            HashMap<String,String> map = new HashMap<String,String>();
                            map.put("mobilephone",intentPhone);
                            map.put("password",pwd);
                            map.put("sn",MyApplication.SerialNumber);
                            HttpRequest.getHttpRequest().requestPOST
                                    (Constants.RIGISTER, null, map, new RequestResultIn() {
                                        @Override
                                        public void requstSuccful(String result) {
                                            LogUtils.e(result);
                                            UserFragmentEntity bean =
                                                    new JsonReslove(FindPasswordActivity.this)
                                                            .resultUserFragment(result);
                                            if(bean.code == 0){
                                                Bundle bundle=new Bundle();
                                                bundle.putString("flag","login");
                                                toOtherActivity(HomeActivity.class, bundle);
                                                finish();
                                                SharedFileUtils util =
                                                        new SharedFileUtils(FindPasswordActivity.this,"USER_INFO",RegisterActivity.MODE_PRIVATE);
                                                util.saveStringFile("user_id", String.valueOf(bean.userId));
                                                util.saveStringFile("headImgUrl",bean.headImgUrl);
                                                util.saveStringFile("userMsg",bean.userMsg);
                                                util.saveStringFile("pwd",pwd);
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
                                                //此时表示找回密码设置新密码
                                                cancelDialog();
                                                finish();
                                                Bundle b=new Bundle();
                                                bundle.putString("flag","login");
                                                toOtherActivity(HomeActivity.class, b);//跳转到用户中心
                                            }if(bean.code != 0){
                                                //返回异常信息
                                                String str = bean.msg;
                                                Toast.makeText(FindPasswordActivity.this,str,Toast.LENGTH_SHORT).show();
                                                cancelDialog();
                                            }
                                        }

                                        @Override
                                        public void requstError(VolleyError error) {
                                            cancelDialog();
                                        }
                                    });
                        }else{
                            //TODO 找回密码时
                            cancelDialog();
                            //TODO 上传唯一ID换为手机号
                            HashMap<String,String> map = new HashMap<String,String>();
                            map.put("userId","");
                            finish();
                            MyApplication.USER_IS_LOGIN_FLAG = true;
                            Bundle bundle=new Bundle();
                            bundle.putString("flag","login");
                            toOtherActivity(HomeActivity.class, bundle);//跳转到用户中心
                        }
                    }else{
                        //表示两个输入框内容不相容
                        Toast.makeText(this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                        cancelDialog();
                    }
                }else{
                    //两个输入框为空
                    Toast.makeText(this,"请将密码填写完整",Toast.LENGTH_SHORT).show();
                    cancelDialog();
                }
                break;

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
                mSecondReGerCode.setBackgroundResource(R.mipmap.yzm_1_2x);
                mSecondReGerCode.setText(time + "s后重新获取");
            } else if (time == 0) {
                mSecondReGerCode.setText("获取验证码");
                mSecondReGerCode.setBackgroundResource(R.mipmap.yzm2x);
                flag = true;
            }
        }

    };
}
