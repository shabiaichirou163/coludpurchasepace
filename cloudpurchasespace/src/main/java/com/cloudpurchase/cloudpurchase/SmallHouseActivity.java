package com.cloudpurchase.cloudpurchase;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudpurchase.base.BaseFragmentActivity;
import com.cloudpurchase.fragment.SmallAllowFragment;
import com.cloudpurchase.fragment.SmallOlderFragment;
import com.cloudpurchase.veiw.MyListView;

/**
 * 用户中心下 小房间界面
 *
 * 里面嵌套两个Fragment ->正在进行的，已经揭晓的
 *
 */
public class SmallHouseActivity extends BaseFragmentActivity implements View.OnClickListener {

    private ImageView mBack;
    private TextView mAllow;//正在进行
    private TextView mOver;//已经过期
    private FrameLayout mFrameLayout;// frameLayout
    private ImageView mImg1,mImg2;//按钮下的选中标志
    private FragmentManager mFragmentManager;//获取fragment管理器
    private FragmentTransaction mTransaction;
    private SmallAllowFragment mAllowFragment;//正在进行中的界面
    private SmallOlderFragment mOldrFragment;//已经揭晓的界面


    @Override
    public void initView() {
        setContentView(R.layout.activity_smallhouse);
        mBack = (ImageView) f(R.id.small_back);
        mAllow = (TextView) f(R.id.small_allow);
        mOver = (TextView) f(R.id.small_over);
        mFrameLayout = (FrameLayout) f(R.id.small_frameLayout);
        mImg1 = (ImageView) f(R.id.small_img1);
        mImg2 = (ImageView) f(R.id.small_img2);
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mAllowFragment = new SmallAllowFragment();
        mTransaction.add(R.id.small_frameLayout,mAllowFragment);
        mOldrFragment = new SmallOlderFragment();
        mTransaction.add(R.id.small_frameLayout,mOldrFragment);
        goneFragment();
        setAllBackground();
        mImg1.setVisibility(View.VISIBLE);
        mAllow.setTextColor(Color.rgb(240,58,101));
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
    public void onClick(View view) {
        int id = view.getId();
        mTransaction = mFragmentManager.beginTransaction();
        switch(id){
            case R.id.small_back:
                finish();
                break;
            case R.id.small_allow:
                setAllBackground();
                mImg1.setVisibility(View.VISIBLE);
                mAllow.setTextColor(Color.rgb(240,58,101));
                goneFragment();
                mTransaction.show(mAllowFragment);
                break;
            case R.id.small_over:
                setAllBackground();
                mImg2.setVisibility(View.VISIBLE);
                mOver.setTextColor(Color.rgb(240,58,101));
                goneFragment();
                mTransaction.show(mOldrFragment);
                break;
        }
        mTransaction.commit();
    }

    private void setAllBackground(){
        mImg1.setVisibility(View.GONE);
        mImg2.setVisibility(View.GONE);
        mAllow.setTextColor(Color.BLACK);
        mOver.setTextColor(Color.BLACK);
    }


    /*
    隐藏所有fragment方法
     */
    private void goneFragment(){
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (mAllowFragment != null){
            ft.hide(mAllowFragment);
        }
        if(mOldrFragment != null){
            ft.hide(mOldrFragment);
        }
        ft.commit();
    }

}
