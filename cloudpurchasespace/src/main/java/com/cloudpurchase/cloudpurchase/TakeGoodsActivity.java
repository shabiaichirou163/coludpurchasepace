package com.cloudpurchase.cloudpurchase;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudpurchase.base.BaseActivity;

/**
 * 幸运记录下查看物流信息界面
 *
 */
public class TakeGoodsActivity extends BaseActivity{

    private ImageView mBack;
    private ImageView mFlag1,mFlag2,mFlag3,mFlag4,mFlag5;//发货状态对应的标志
    private TextView mTime1,mTime2,mTime3,mTime4,mTime5;//发货状态对应的时间
    private TextView mTitle;//商品描述
    private TextView mType;//使用的物流
    private TextView mTypeNum;//运单号码
    private TextView mName;//收货人姓名
    private TextView mPhone;//收货人手机
    private TextView mAddress;//收货地址

    @Override
    public void initView() {
        setContentView(R.layout.activtity_take_goods);
        mBack = (ImageView) f(R.id.takeGoods_back);
        mFlag1 = (ImageView) f(R.id.takeGoods_flag1);
        mFlag2 = (ImageView) f(R.id.takeGoods_flag2);
        mFlag3 = (ImageView) f(R.id.takeGoods_flag3);
        mFlag4 = (ImageView) f(R.id.takeGoods_flag4);
        mFlag5 = (ImageView) f(R.id.takeGoods_flag5);
        mTime1 = (TextView) f(R.id.takeGoods_time1);
        mTime2 = (TextView) f(R.id.takeGoods_time2);
        mTime3 = (TextView) f(R.id.takeGoods_time3);
        mTime4 = (TextView) f(R.id.takeGoods_time4);
        mTime5 = (TextView) f(R.id.takeGoods_time5);
        mTitle = (TextView) f(R.id.takeGoods_goodsTitle);
        mType = (TextView) f(R.id.takeGoods_type);
        mTypeNum = (TextView) f(R.id.takeGoods_typeNum);
        mName = (TextView) f(R.id.takeGoods_name);
        mPhone = (TextView) f(R.id.takeGoods_phone);
        mAddress = (TextView) f(R.id.takeGoods_address);
        setFlag(true,true,true,false,false);
    }

    @Override
    public void setOnclick() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TakeGoodsActivity.this.finish();
            }
        });
    }

    @Override
    public void initList() {

    }

    /*
    一共有五个flag，将形参设为true 表示flag为完成状态

    默认都是false

    当flag为false时，timeFlag为隐藏状态
     */
    private void setFlag(boolean falg1,boolean falg2,boolean falg3,boolean falg4,boolean falg5){
        int visible = View.VISIBLE;
        int gone = View.GONE;
        if(falg1){
            mFlag1.setImageResource(R.mipmap.a_r3_2x);
            mTime1.setVisibility(visible);
        }else{
            mFlag1.setImageResource(R.mipmap.a_r1_2x);
            mTime1.setVisibility(gone);
        }
        if(falg2){
            mFlag2.setImageResource(R.mipmap.a_r3_2x);
            mTime2.setVisibility(visible);
        }else{
            mFlag2.setImageResource(R.mipmap.a_r1_2x);
            mTime2.setVisibility(gone);
        }
        if(falg3){
            mFlag3.setImageResource(R.mipmap.a_r3_2x);
            mTime3.setVisibility(visible);
        }else{
            mFlag3.setImageResource(R.mipmap.a_r1_2x);
            mTime3.setVisibility(gone);
        }
        if(falg4){
            mFlag4.setImageResource(R.mipmap.a_r3_2x);
            mTime4.setVisibility(visible);
        }else{
            mFlag4.setImageResource(R.mipmap.a_r1_2x);
            mTime4.setVisibility(gone);
        }
        if(falg5){
            mFlag5.setImageResource(R.mipmap.a_r3_2x);
            mTime5.setVisibility(visible);
        }else{
            mFlag5.setImageResource(R.mipmap.a_r1_2x);
            mTime5.setVisibility(gone);
        }
    }
}
