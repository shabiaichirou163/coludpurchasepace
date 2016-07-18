package com.cloudpurchase.cloudpurchase;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.utils.ViewPagerUtils;

/**
 * app引导页面,用于用户首次打开引导
 *
 */
public class GuidActivity extends BaseActivity implements View.OnClickListener{
    private ViewPager mGuidVpg;
    private int mIcon[]={R.mipmap.bb,R.mipmap.cc,R.mipmap.bb,R.mipmap.cc,R.mipmap.bb};//引导图片
    private ViewPagerUtils mUtils;//ViewPager公用的工具类
    private Button mGuidBtn;
    private SharedPreferences mShare;//存储第一次打开数据
    private LinearLayout mPointLayout;//添加指示点布局
    @Override
    public void initView() {
        mShare=getSharedPreferences("data",MODE_PRIVATE);
        if (mShare.getBoolean("first",true)){
            SharedPreferences.Editor editor=mShare.edit();
            editor.putBoolean("first", false);
            editor.commit();
            setContentView(R.layout.activity_guid);
            mGuidVpg= (ViewPager) findViewById(R.id.guid_vpg);
            mPointLayout= (LinearLayout) this.findViewById(R.id.guid_point_layout);
            mGuidBtn= (Button) this.findViewById(R.id.guid_btn);
            mUtils=new ViewPagerUtils(this,mGuidVpg,mIcon,mPointLayout,mIcon.length,mGuidBtn,null);
            mUtils.event();
            mGuidBtn.setOnClickListener(this);
        }else {
            toOtherActivity(SelectActivity.class);
            this.finish();
        }
    }

    @Override
    public void setOnclick() {

    }

    @Override
    public void initList() {

    }

    @Override
    public void onClick(View v) {
        toOtherActivity(SelectActivity.class);
        this.finish();
    }

}
