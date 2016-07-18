package com.cloudpurchase.cloudpurchase;

import android.widget.ImageView;
import android.widget.ListView;

import com.cloudpurchase.adpater.RechargeRecordAdapter;
import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.entity.RechargeRecordEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查看充值记录Actvity
 */
public class RechargeRecordActivity extends BaseActivity{

    private ImageView mBack;//返回按钮
    private ImageView mAdd;//充值
    private ListView mListView;
    private List<RechargeRecordEntity> list = new ArrayList<RechargeRecordEntity>();
    private RechargeRecordAdapter adapter;

    @Override
    public void initView() {
        setContentView(R.layout.activity_recharge_record);
        mBack = (ImageView) f(R.id.recharge_record_back);
        mAdd = (ImageView) f(R.id.recharge_record_add);
        mListView = (ListView) f(R.id.recharge_record_ListView);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);//隐藏下划线
    }

    @Override
    public void setOnclick() {

    }

    @Override
    public void initList() {
        initBean();
        adapter = new RechargeRecordAdapter(this,list);
    }

    private void initBean(){
        for(int i = 0 ; i < 15 ; i ++){
            RechargeRecordEntity bean = new RechargeRecordEntity();
            bean.mMode = "微信充值";
            bean.mTime = "2016年6月14日11:23:02";
            bean.mMoney = "200";
            bean.mFlag = "已付款";
            list.add(bean);
        }
    }

}
