package com.cloudpurchase.cloudpurchase;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudpurchase.base.BaseFragmentActivity;
import com.cloudpurchase.fragment.RedPackageListViewFragment;
import com.cloudpurchase.fragment.RedPackageListViewOverFragment;


/**
 * 红包详情界面
 */
public class RedPackageActivity extends BaseFragmentActivity implements View.OnClickListener {

    private ImageView mBack;
    private TextView mAllow;//可以使用按钮
    private TextView mOver;//已过期 按钮
    private LinearLayout mLinear;//Fragment
    private FragmentManager mFragmentManager;//获取fragment管理器
    private FragmentTransaction mTransaction;
    private RedPackageListViewFragment mAllowFragment;//可以使用的
    private RedPackageListViewOverFragment mOverFragment;//已经过期的
    private ImageView mImg1,mImg2;//两根红线儿

    @Override
    public void initView() {
        setContentView(R.layout.activity_red_package);
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mLinear = (LinearLayout) f(R.id.redPackage_linear);
        mAllowFragment = new RedPackageListViewFragment();
        mTransaction.add(R.id.redPackage_linear,mAllowFragment);
        mOverFragment = new RedPackageListViewOverFragment();
        mTransaction.add(R.id.redPackage_linear,mOverFragment);
        mBack = (ImageView) f(R.id.redPackage_back);
        mAllow = (TextView) f(R.id.redPackage_allow);
        mOver = (TextView) f(R.id.redPackage_over);
        mImg1 = (ImageView) f(R.id.redPackage_img1);
        mImg2 = (ImageView) f(R.id.redPackage_img2);
        goneFragment();
        goneImg();
        mImg1.setVisibility(View.VISIBLE);
        mTransaction.show(mAllowFragment);
        mTransaction.commit();
    }

    @Override
    public void setOnclick() {
        mBack.setOnClickListener(this);
        mAllow.setOnClickListener(this);
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
            case R.id.redPackage_back:
                finish();
                break;
            case R.id.redPackage_allow:
                goneImg();
                goneFragment();
                mImg1.setVisibility(View.VISIBLE);
                mTransaction.show(mAllowFragment);
                break;
            case R.id.redPackage_over:
                goneImg();
                goneFragment();
                mImg2.setVisibility(View.VISIBLE);
                mTransaction.show(mOverFragment);
                break;
        }
        mTransaction.commit();
    }

    /*
  隐藏所有fragment方法
   */
    private void goneFragment(){
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (mAllowFragment != null){
            ft.hide(mAllowFragment);
        }
        if(mOverFragment != null){
            ft.hide(mOverFragment);
        }
        ft.commit();
    }

    private void goneImg(){
        mImg1.setVisibility(View.GONE);
        mImg2.setVisibility(View.GONE);
    }

}
