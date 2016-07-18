package com.cloudpurchase.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.cloudpurchase.cloudpurchase.R;

/**
 * 处理所有Activity的基类
 *
 * @author  WangLiang
 */
public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initList();
        initView();
        setOnclick();
    }

    /*
     * 初始化控件
     */
    public abstract void initView();

    /*
     * 找id方法
     * @param id 要找的id
     * @return
     */
    public View f(int id){
        return findViewById(id);
    }

    /*
     * 找id方法
     * @param v 要找id的View
     * @param id  要找的id
     * @return
     */
    public View f(View v,int id){
        return v.findViewById(id);
    }

    /*
     * 为控件设置监听
     */
    public abstract void setOnclick();

    /*
     * 初始化数据
     */
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

    /*
   自定义ProgressDialog
   */
    private Dialog mProgressDialog;
    /*
      *显示ProgressDialog
      */
    public void creatProgressDialog(){
        View view= LayoutInflater.from(this).inflate(R.layout.progressdialog_layout,null);
        //ImageView img= (ImageView) view.findViewById(R.id.progressdialog_img);
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                this, R.anim.progress_animation);
        // 使用ImageView显示动画
        //img.startAnimation(hyperspaceJumpAnimation);
        mProgressDialog=new Dialog(this,R.style.loading_dialog);
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

}
