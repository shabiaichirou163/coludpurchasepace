package com.cloudpurchase.cloudpurchase;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudpurchase.base.BaseFragmentActivity;
import com.cloudpurchase.fragment.AllRecordListViewFragment;
import com.cloudpurchase.fragment.RecordListViewFragment;
import com.cloudpurchase.fragment.RecordOverListViewFragment;


/**
 * 云购记录界面 的HomeActivity
 *
 */
public class RecordActivtiy extends BaseFragmentActivity implements View.OnClickListener{

    private ImageView mBack;
    private TextView mRecord;//正在进行按钮
    private TextView mOver;//已经结束按钮
    private ImageView mImg2,mImg3;//按钮下的选中标志
    private LinearLayout mLinearLayout;//Fragment
//    private AllRecordListViewFragment mAllRecordFragment;//全部云购记录的Fragment
    private RecordListViewFragment mRecoredFragment;//正在进行的Fragment
    private RecordOverListViewFragment mOverFragment;//已经揭晓的Fragment
    private FragmentManager mFragmentManager;//获取fragment管理器
    private FragmentTransaction mTransaction;



    @Override
    public void initView() {
        setContentView(R.layout.activity_record);
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();

        mLinearLayout = (LinearLayout) f(R.id.record_linearLayout);
        mBack = (ImageView) f(R.id.record_back);
        mRecord = (TextView) f(R.id.record_record);
        mOver = (TextView) f(R.id.record_over);
        mImg2 = (ImageView) f(R.id.record_img2);
        mImg3 = (ImageView) f(R.id.record_img3);
//        mAllRecordFragment = new AllRecordListViewFragment();
//        mTransaction.add(R.id.record_linearLayout,mAllRecordFragment);
        mRecoredFragment = new RecordListViewFragment();
        mTransaction.add(R.id.record_linearLayout,mRecoredFragment);
        mOverFragment  = new RecordOverListViewFragment();
        mTransaction.add(R.id.record_linearLayout,mOverFragment);
        goneFragment();
        setAllBackground();
        mImg2.setVisibility(View.VISIBLE);
        mRecord.setTextColor(Color.rgb(240,58,101));
        mTransaction.show(mRecoredFragment);
        mTransaction.commit();
    }

    @Override
    public void setOnclick() {
        mBack.setOnClickListener(this);
        mRecord.setOnClickListener(this);
        mOver.setOnClickListener(this);
    }

    @Override
    public void initList() {

    }

    @Override
    public void onClick(View v) {
        mTransaction = mFragmentManager.beginTransaction();
        int id = v.getId();
        switch (id){
            case R.id.record_back:
                finish();
                break;
            case R.id.record_record:
                setAllBackground();
                mImg2.setVisibility(View.VISIBLE);
                mRecord.setTextColor(Color.rgb(240,58,101));
                goneFragment();
                mTransaction.show(mRecoredFragment);
                break;
            case R.id.record_over:
                setAllBackground();
                mImg3.setVisibility(View.VISIBLE);
                mOver.setTextColor(Color.rgb(240,58,101));
                goneFragment();
                mTransaction.show(mOverFragment);
                break;
        }
        mTransaction.commit();
    }

    private void setAllBackground(){
        mImg2.setVisibility(View.GONE);
        mImg3.setVisibility(View.GONE);
        mRecord.setTextColor(Color.BLACK);
        mOver.setTextColor(Color.BLACK);
    }
    /*
    隐藏所有fragment方法
     */
    private void goneFragment(){
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (mRecoredFragment != null){
            ft.hide(mRecoredFragment);
        }
        if(mOverFragment != null){
            ft.hide(mOverFragment);
        }
        ft.commit();
    }
}
