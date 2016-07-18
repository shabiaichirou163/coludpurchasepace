package com.cloudpurchase.cloudpurchase;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.cloudpurchase.base.BaseActivity;

import com.cloudpurchase.payutils.alipay.Alipay;
import com.cloudpurchase.payutils.alipay.wechatpay.Constants;
import com.cloudpurchase.payutils.alipay.wechatpay.WeChatPay;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


/**
 * 充值 Activity  充值 充值
 *
 */
public class RechargeActivtiy extends BaseActivity implements View.OnClickListener {

    private ImageView mBack;
    private RadioButton mTen;//十元
    private RadioButton mThirty;//三十元
    private RadioButton mFifty;//五十元
    private RadioButton mOneHundred;//一百元
    private RadioButton mTwoHundred;//二百元
    private RadioButton mFiveHundred;//伍佰元
    private LinearLayout mAlipay;//使用支付宝支付
    private LinearLayout mWeChat;//使用微信支付
    private LinearLayout mYinLian;//使用银联支付
    private ImageView mAlipayFalg;//是否选用支付宝支付的标志
    private ImageView mWeChatFalg;//是否选用微信支付的标志
    private ImageView mYinLianFalg;//是否选用银联支付地标志
    private Button mBtn;//提交按钮
    private int mPaymentFalg = -1;//支付标志，0 表示使用支付宝，1 表示使用微信
    private int mPaymentMoney = 10;//支付的金额，默认10元
    private IWXAPI api;

    @Override
    public void initView() {
        setContentView(R.layout.activity_recharge);
        mBack = (ImageView) f(R.id.recharge_back);
        mTen = (RadioButton) f(R.id.recharge_ten);
        mThirty = (RadioButton) f(R.id.recharge_thirty);
        mFifty = (RadioButton) f(R.id.recharge_fifty);
        mOneHundred = (RadioButton) f(R.id.recharge_oneHundred);
        mTwoHundred = (RadioButton) f(R.id.recharge_twoHundred);
        mFiveHundred = (RadioButton) f(R.id.recharge_fiveHundred);
        mAlipay = (LinearLayout) f(R.id.recharge_alipay);
        mWeChat = (LinearLayout) f(R.id.recharge_weChat);
        mYinLian = (LinearLayout) f(R.id.recharge_yinLian);
        mAlipayFalg = (ImageView) f(R.id.recharge_alipay_falg);
        mWeChatFalg = (ImageView) f(R.id.recharge_weChat_falg);
        mYinLianFalg = (ImageView) f(R.id.recharge_yinLian_falg);
        mBtn = (Button) f(R.id.recharge_btn);
        switchText();
        mTen.setTextColor(Color.rgb(237,24,58));
        mTen.setBackgroundResource(R.mipmap.btn_border);
        goneFalg();
        mAlipayFalg.setImageResource(R.mipmap.a_ico_mark_2x);
        mPaymentFalg = 0;
        initWeChat();
    }

    @Override
    public void setOnclick() {
        mBack.setOnClickListener(this);
        mTen.setOnClickListener(this);
        mThirty.setOnClickListener(this);
        mFifty.setOnClickListener(this);
        mOneHundred.setOnClickListener(this);
        mTwoHundred.setOnClickListener(this);
        mFiveHundred.setOnClickListener(this);
        mAlipay.setOnClickListener(this);
        mWeChat.setOnClickListener(this);
        mYinLian.setOnClickListener(this);
        mBtn.setOnClickListener(this);
    }


    @Override
    public void initList() {

    }

    /*
    切换RadioButton文字颜色
     */
    private void switchText(){
        mTen.setTextColor(Color.rgb(155,155,189));
        mThirty.setTextColor(Color.rgb(155,155,189));
        mFifty.setTextColor(Color.rgb(155,155,189));
        mOneHundred.setTextColor(Color.rgb(155,155,189));
        mTwoHundred.setTextColor(Color.rgb(155,155,189));
        mFiveHundred.setTextColor(Color.rgb(155,155,189));

        mTen.setBackgroundResource(R.mipmap.a_ad_moeny);
        mThirty.setBackgroundResource(R.mipmap.a_ad_moeny);
        mFifty.setBackgroundResource(R.mipmap.a_ad_moeny);
        mOneHundred.setBackgroundResource(R.mipmap.a_ad_moeny);
        mTwoHundred.setBackgroundResource(R.mipmap.a_ad_moeny);
        mFiveHundred.setBackgroundResource(R.mipmap.a_ad_moeny);
    }


    /*
    隐藏Falg
     */
    private void goneFalg(){
        mAlipayFalg.setImageResource(R.mipmap.a_admin_duihao2_2x);
        mWeChatFalg.setImageResource(R.mipmap.a_admin_duihao2_2x);
        mYinLianFalg.setImageResource(R.mipmap.a_admin_duihao2_2x);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.recharge_back:
                finish();
                break;
            case R.id.recharge_ten:
                mPaymentMoney = 10;
                switchText();
                mTen.setTextColor(Color.rgb(237,24,58));
                mTen.setBackgroundResource(R.mipmap.btn_border);
                break;
            case R.id.recharge_thirty:
                mPaymentMoney = 30;
                switchText();
                mThirty.setTextColor(Color.rgb(237,24,58));
                mThirty.setBackgroundResource(R.mipmap.btn_border);
                break;
            case R.id.recharge_fifty:
                mPaymentMoney = 50;
                switchText();
                mFifty.setTextColor(Color.rgb(237,24,58));
                mFifty.setBackgroundResource(R.mipmap.btn_border);
                break;
            case R.id.recharge_oneHundred:
                mPaymentMoney = 100;
                switchText();
                mOneHundred.setTextColor(Color.rgb(237,24,58));
                mOneHundred.setBackgroundResource(R.mipmap.btn_border);
                break;
            case R.id.recharge_twoHundred:
                mPaymentMoney = 200;
                switchText();
                mTwoHundred.setTextColor(Color.rgb(237,24,58));
                mTwoHundred.setBackgroundResource(R.mipmap.btn_border);
                break;
            case R.id.recharge_fiveHundred:
                mPaymentMoney = 500;
                switchText();
                mFiveHundred.setTextColor(Color.rgb(237,24,58));
                mFiveHundred.setBackgroundResource(R.mipmap.btn_border);
                break;
            case R.id.recharge_alipay:
                mPaymentFalg = 0;
                goneFalg();
                mAlipayFalg.setImageResource(R.mipmap.a_ico_mark_2x);
                break;
            case R.id.recharge_weChat:
                mPaymentFalg = 1;
                goneFalg();
                mWeChatFalg.setImageResource(R.mipmap.a_ico_mark_2x);
                break;
            case R.id.recharge_yinLian:
                mPaymentFalg = 2;
                goneFalg();
                mYinLianFalg.setImageResource(R.mipmap.a_ico_mark_2x);
                break;
            case R.id.recharge_btn:
                String str = null;
                if (mPaymentFalg == 0){
                    str = "支付宝";
                    new Alipay(this).aliPay();//调用支付宝支付
                }else if(mPaymentFalg == 1){
                    str = "微信";
                    new WeChatPay(this).pay();//调用微信支付
                }else if(mPaymentFalg == 2){
                    str = "银联";
                }
                Toast.makeText(this,"选择了："+ str+"支付,共计"+mPaymentMoney+"元",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /*
    初始化微信API
     */
    private void initWeChat(){
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
    }



}

