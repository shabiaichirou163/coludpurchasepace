package com.cloudpurchase.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * 所有FragmentActivity的基类
 *
 * @author WangLiang
 */
public abstract class BaseFragmentActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initList();
        setOnclick();
    }

    public abstract void initView();

    public View f(int id){
        return findViewById(id);
    }

    public View f(int id,View v){
        return v.findViewById(id);
    }

    public abstract void setOnclick();

    public abstract void initList();
    /*
 * 跳转界面
 */
    public void toOtherActivity(Class cls){
        Intent intent=new Intent(this,cls);
        startActivity(intent);
    }
    /*
    * 跳转界面
    */
    public void toOtherActivity(Class cls,Bundle bundle){
        Intent intent=new Intent(this,cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
