package com.cloudpurchase.cloudpurchase;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.VolleyError;
import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.db.DBWrapper;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.net.HttpImgLoader;
import com.cloudpurchase.net.HttpRequest;
import com.cloudpurchase.payutils.alipay.Alipay;
import com.cloudpurchase.payutils.alipay.PayResult;
import com.cloudpurchase.payutils.alipay.SignUtils;
import com.cloudpurchase.utils.Constants;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.utils.PayResultListener;
import com.cloudpurchase.utils.RequestResultIn;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 支付界面
 */
public class PayActivity extends BaseActivity implements View.OnClickListener,PayResultListener{
    private TextView mGoodsNum,mTotalPice,mRemainMoney,mTotalPice1;
    private LinearLayout mRemainLayout,mZFBLayout,mWXLayout,mYLLayout;
    private ImageView mRemainImg,mZFBImg,mWXImg,mYLImg;
    private Button mConfirmPayBtn;
    private ImageButton mBckBtn;
    private String mFlag="ZFB";//标示支付方式 "ZFB" 支付宝支付 默认   "WX" 微信支付  "remain" 余额支付
    private DBWrapper mDBWrapper;
    private int mPersonNum;
    private Alipay mAlipay;
    @Override
    public void initView() {
        setContentView(R.layout.activity_pay);
        mAlipay=new Alipay(this);
        mAlipay.setOnPayResultListener(this);
        mDBWrapper=new DBWrapper(this);
        mGoodsNum= (TextView) this.findViewById(R.id.activity_pay_speak_txt);
        mTotalPice= (TextView) this.findViewById(R.id.activity_pay_price);
        mRemainLayout= (LinearLayout) this.findViewById(R.id.activity_pay_remain_layout);
        mRemainMoney= (TextView) this.findViewById(R.id.activity_pay_remainpay_txt);
        mRemainImg= (ImageView) this.findViewById(R.id.activity_pay_remain_img);
        mZFBLayout= (LinearLayout) this.findViewById(R.id.activity_pay_zhifubao_layout);
        mZFBImg= (ImageView) this.findViewById(R.id.activity_pay_zhifubao_img);
        mWXLayout= (LinearLayout) this.findViewById(R.id.activity_pay_weixin_layout);
        mWXImg= (ImageView) this.findViewById(R.id.activity_pay_weixin_img);
        mYLLayout= (LinearLayout) this.findViewById(R.id.activity_pay_yinlian_layout);
        mYLImg= (ImageView) this.findViewById(R.id.activity_pay_yinlian_img);
        mTotalPice1= (TextView) this.findViewById(R.id.activity_pay_total_price);
        mConfirmPayBtn= (Button) this.findViewById(R.id.activity_pay_confirm_btn);
        mBckBtn= (ImageButton) this.findViewById(R.id.activity_pay_back_btn);
        getOrderPayType();
        getData();
    }

    /**
     * 获取订单支付方式
     */
    public void getOrderPayType(){
        String url= Constants.GET_ORDER_PAY_TYPE;
        HttpRequest.getHttpRequest().requestGET(url, null, new RequestResultIn() {
            @Override
            public void requstSuccful(String result) {
                LogUtils.e(result);
            }

            @Override
            public void requstError(VolleyError error) {

            }
        });
    }
    @Override
    public void setOnclick() {
        mBckBtn.setOnClickListener(this);
        mRemainLayout.setOnClickListener(this);
        mZFBLayout.setOnClickListener(this);
        mWXLayout.setOnClickListener(this);
        mYLLayout.setOnClickListener(this);
        mConfirmPayBtn.setOnClickListener(this);
    }

    @Override
    public void initList() {

    }

    /**
     * 获取跳转携带的参数
     * @param
     */
    public void getData(){
        List<GoodsDetails> good=mDBWrapper.selectData();
        for (int i=0;i<good.size();i++){
            mPersonNum+=good.get(i).getPersonNum();
        }
        mGoodsNum.setText("您将花费"+mPersonNum+"金币购买"+good.size()+"种商品,要如实完成支付哦");
        mTotalPice.setText(mPersonNum + "金币");
        mTotalPice1.setText(mPersonNum+"金币");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_pay_back_btn:
                this.setResult(1000);
                this.finish();
                break;
            case R.id.activity_pay_remain_layout:
                mFlag="remain";
                setPayWay();
                mRemainImg.setImageResource(R.mipmap.checked);
                break;
            case R.id.activity_pay_zhifubao_layout:
                mFlag="ZFB";
                setPayWay();
                mZFBImg.setImageResource(R.mipmap.z_admin_2x_checked);
                break;
            case R.id.activity_pay_weixin_layout:
                mFlag="WX";
                setPayWay();
                mWXImg.setImageResource(R.mipmap.z_admin_2x_checked);
                break;
            case R.id.activity_pay_yinlian_layout:
                mFlag="YL";
                setPayWay();
                mYLImg.setImageResource(R.mipmap.z_admin_2x_checked);
                break;
            case R.id.activity_pay_confirm_btn:
                switch (mFlag){
                    case "remain":
                        Toast.makeText(this, "你选择的支付方式是"+"余额付款",Toast.LENGTH_SHORT).show();
                        break;
                    case "ZFB":
                        Toast.makeText(this, "你选择的支付方式是"+"支付宝付款",Toast.LENGTH_SHORT).show();
                        mAlipay.aliPay();
                        break;
                    case "WX":
                        Toast.makeText(this, "你选择的支付方式是"+"微信付款",Toast.LENGTH_SHORT).show();
                        break;
                    case "YL":
                        Toast.makeText(this, "你选择的支付方式是"+"银联付款",Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }

    }
    public void setPayWay(){
        mRemainImg.setImageResource(R.mipmap.checkbox_normal);
        mZFBImg.setImageResource(R.mipmap.z_admin_2x);
        mWXImg.setImageResource(R.mipmap.z_admin_2x);
        mYLImg.setImageResource(R.mipmap.z_admin_2x);
    }

    @Override
    public void onBackPressed() {
        this.setResult(1000);
        this.finish();
    }

    /**
     * 支付成功
     */
    @Override
    public void paySucceed() {

    }
    /**
     * 支付确认中
     */
    @Override
    public void payAffirming() {

    }
    /**
     * 支付失败
     */
    @Override
    public void payFail() {

    }
}
