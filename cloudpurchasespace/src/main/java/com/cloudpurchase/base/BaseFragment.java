package com.cloudpurchase.base;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cloudpurchase.cloudpurchase.MyApplication;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.utils.LogUtils;

/**
 * 所有Fragment的基类
 *
 * 注意！引入Fragment包时，一定要选择V4包下的Fragment
 *
 * @author WangLiang
 */
public abstract class BaseFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = initView();
        initList();
        setOnclick();
        return v;
    }
    /*
     * 初始化控件
     * @return
     */
    public abstract View initView();


    public View f(View v,int id){
        return v.findViewById(id);
    }

    public abstract void initList();

    public abstract void setOnclick();

    /*
     * 获取MyApplication对象
     * @return MyApplication对象
     */
    public MyApplication getApplication(){
        return (MyApplication)getActivity().getApplication();
    }
    /*
     自定义ProgressDialog
     */
    private Dialog mProgressDialog;
       /*
        *显示ProgressDialog
        */
    public void creatProgressDialog(){
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.progressdialog_layout,null);
        //ImageView img= (ImageView) view.findViewById(R.id.progressdialog_img);
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                getActivity(), R.anim.progress_animation);
        // 使用ImageView显示动画
        //img.startAnimation(hyperspaceJumpAnimation);
        mProgressDialog=new Dialog(getActivity(),R.style.loading_dialog);
       // mProgressDialog.setCancelable(false);// 不可以用“返回键”取消
        mProgressDialog.setContentView(view);
        mProgressDialog.show();
    }
    /*
     *取消自定义ProgressDialog
     */
    public void cancelDialog(){
        if (null!=mProgressDialog){
            mProgressDialog.dismiss();
            mProgressDialog.cancel();
            mProgressDialog=null;
        }
    }

    /**
     * 不带参数跳转界面
     * @param cls
     */
    public void toOtherActivity(Class cls){
        Intent intent=new Intent(getActivity(),cls);
        startActivity(intent);
    }
    /*
     * 跳转界面携带数据
     */
    public void toOtherActivity(Class cls,Bundle bundle){
        Intent intent=new Intent(getActivity(),cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }



    public void toOtherActivityResult(Class cls){
        Intent intent=new Intent(getActivity(),cls);
        startActivityForResult(intent,1);
    }

}
