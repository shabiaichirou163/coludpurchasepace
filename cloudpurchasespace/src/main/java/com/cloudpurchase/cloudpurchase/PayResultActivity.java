package com.cloudpurchase.cloudpurchase;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cloudpurchase.adpater.PayResultAdpater;
import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.db.DBWrapper;
import com.cloudpurchase.entity.GoodsDetails;

import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 支付结果界面
 */
public class PayResultActivity extends BaseActivity implements View.OnClickListener{
    private ImageButton mBackBtn;//返回键
    private Button mLookBtn; //查看云购记录
    private Button mAgainBtn; //继续逛逛
    private DBWrapper mDbWrapper;
    private TextView mShoppingInfo;//显示购买商品件数和人次
    private ListView mShoppingInfoLst;//显示付款成功的商品
    private int mTotalPersonNum;
    private List<GoodsDetails> mGoodInfo;
    @Override
    public void initView() {
        setContentView(R.layout.activity_pay_result);
        mDbWrapper=new DBWrapper(this);
        mBackBtn= (ImageButton) findViewById(R.id.pay_result_back);
        mLookBtn= (Button) findViewById(R.id.pay_result_look);
        mAgainBtn= (Button) findViewById(R.id.pay_result_again);
        mShoppingInfo= (TextView) findViewById(R.id.pay_result_goods_info);
        mShoppingInfoLst= (ListView) findViewById(R.id.pay_result_shopping_goods_detail);
        getDBInfo();
        PayResultAdpater adpater=new PayResultAdpater(this,mGoodInfo);
        mShoppingInfoLst.setAdapter(adpater);
    }

    public  void getDBInfo(){
        mGoodInfo=mDbWrapper.selectData();
        for (int i=0;i<mGoodInfo.size();i++){
            mTotalPersonNum+=mGoodInfo.get(i).getPersonNum();
        }
        mShoppingInfo.setText("您成功参与了"+mGoodInfo.size()+"件商品,共"+mTotalPersonNum+"人次,信息如下:");
    }

    @Override
    public void setOnclick() {
        mBackBtn.setOnClickListener(this);
        mLookBtn.setOnClickListener(this);
        mAgainBtn.setOnClickListener(this);
    }

    @Override
    public void initList() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pay_result_back:
                this.finish();
                break;
            case R.id.pay_result_look:
                toOtherActivity(RecordActivtiy.class);
                //跳转至RecordActivtiy,待确定返回状况
                break;
            case R.id.pay_result_again:
                toOtherActivity(HomeActivity.class);
                this.finish();
                break;
        }
        if (mGoodInfo!=null){
            mDbWrapper.deleteData();
        }
    }

    @Override
    public void onBackPressed() {
        if (mGoodInfo!=null){
            mDbWrapper.deleteData();
        }
        this.finish();
    }
}
