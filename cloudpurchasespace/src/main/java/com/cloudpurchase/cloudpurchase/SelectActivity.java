package com.cloudpurchase.cloudpurchase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.fragment.SelcetClassification;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.utils.ScreenUtils;
import com.cloudpurchase.veiw.MyImageView;
import com.cloudpurchase.veiw.RoundImageView;

import java.util.Timer;
import java.util.TimerTask;

/**
 *供用户选择的Activity（一元区，十元区，百元区，小房间，卖家区）
 *
 * 使用消息传参向 HomeActivity 发送不同的消息，HomeActivity通过判断消息，实现加载不同的Fragment布局
 *
 */
public class SelectActivity extends BaseActivity implements View.OnClickListener,View.OnTouchListener{
    private ImageButton mSLOneYuanArea;//一元专区
    private ImageButton mSLTenYuanArea;//十元专区
    private ImageButton mSLOneHundredYuanArea;//百元专区
    private ImageButton mSLSmallRoom;//小房间
    private ImageButton mSLSellerArea;//卖家区
    private ImageView rorateImg;
    private RelativeLayout mVg;
    private TextView mText1,mText2;
    private boolean mIsAnimation;//首次加载显示动画 其它不加载
    private RoundImageView mGoodImg;
    private int mScreenHight;//屏幕高度
    private MyApplication mApplication;//全局application
    @Override
    public void initView() {
        setContentView(R.layout.activity_select);
        mApplication = (MyApplication) this.getApplicationContext();
        rorateImg= (ImageView) findViewById(R.id.select_rotate);
        mSLOneYuanArea = (ImageButton) f(R.id.select_oneYuanArea);
        mSLTenYuanArea = (ImageButton) f(R.id.select_tenYuanArea);
        mSLOneHundredYuanArea = (ImageButton) f(R.id.select_oneHundredYuanArea);
        mSLSmallRoom = (ImageButton) f(R.id.select_smallRoom);
        mSLSellerArea = (ImageButton) f(R.id.select_sellerArea);
        mVg= (RelativeLayout) findViewById(R.id.select_activity_vg);
        mText1= (TextView) findViewById(R.id.select_activity_txt1);
        mText2= (TextView) findViewById(R.id.select_activity_txt2);
        mGoodImg= (RoundImageView) findViewById(R.id.imageView2);

        mScreenHight=ScreenUtils.getHeight(this);//获取屏幕高度
    }
    @Override
    public void setOnclick() {
        mSLOneYuanArea.setOnClickListener(this);
        mSLTenYuanArea.setOnClickListener(this);
        mSLOneHundredYuanArea.setOnClickListener(this);
        mSLSmallRoom.setOnClickListener(this);
        mSLSellerArea.setOnClickListener(this);
        mVg.setOnTouchListener(this);
    }
    @Override
    public void initList() {
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.select_oneYuanArea:
                mApplication.setmSeclectFrag("oneYuanArea");
                toOtherActivity(HomeActivity.class);
                break;
            case R.id.select_tenYuanArea:
                mApplication.setmSeclectFrag("tenYuanArea");
                toOtherActivity(HomeActivity.class);
                break;
            case R.id.select_oneHundredYuanArea:
                mApplication.setmSeclectFrag("oneHundredYuanArea");
                toOtherActivity(HomeActivity.class);
                break;
            case R.id.select_smallRoom:
                mApplication.setmSeclectFrag("smallRoom");
                toOtherActivity(HomeActivity.class);
                break;
            case R.id.select_sellerArea:
                mApplication.setmSeclectFrag("sellArea");
                toOtherActivity(HomeActivity.class);
                break;
        }
    }
    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            exitBy2Click();      //调用双击退出函数
        }
        return false;
    }
    /**
     * 双击退出
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出云购空间", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 3000); // 如果3秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    /**
     * 正方向旋转
     */
    public void createAnimation(){
        Animation animation=new RotateAnimation(startAngle,endAngle,Animation.RELATIVE_TO_SELF, 0f,Animation.RELATIVE_TO_SELF,1f);
        animation.setDuration(500);
        animation.setFillAfter(true);
        rorateImg.startAnimation(animation);
    }

    /**
     * 反方向旋转
     */
    public void createAnimationMinus(){
        Animation animation=new RotateAnimation(endAngle-90,startAngle-90,Animation.RELATIVE_TO_SELF, 0f,Animation.RELATIVE_TO_SELF,1f);
        animation.setDuration(500);
        animation.setFillAfter(true);
        rorateImg.startAnimation(animation);
    }

    /**
     * 设置屏幕触摸事件
     */

    private float startX,endX,startY,endY;
    private int startAngle=0,endAngle=90;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX=event.getX();
                startY=event.getY();
                break;
            case MotionEvent.ACTION_UP:
                endX=event.getX();
                endY=event.getY();
                doAnimation(endX - startX, endY - startY);
                break;
        }
        return true;
    }

    /**
     * 根据的结果判定动画移动
     * @param decX
     * @param decY
     */
    protected static final int MSG_ANIMATION = 1;
    public void doAnimation(float decX,float decY){
        if (decY>0&&decY>mScreenHight/30){
            createAnimation();
            startAngle+=90;
            endAngle+=90;
            if (startAngle==360){
                startAngle=0;
            }
            if (endAngle==450){
                endAngle=90;
            }
        }
        if (decY<0&&decY<(-mScreenHight/30)){
            createAnimationMinus();
            startAngle -= 90;
            endAngle -= 90;
            if (startAngle==-450){
                startAngle=-90;
            }
            if (endAngle==-360){
                endAngle=0;
            }
        }
        mHandler.sendEmptyMessageDelayed(MSG_ANIMATION, 450);
    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (mHandler.hasMessages(MSG_ANIMATION)){
                mHandler.removeMessages(MSG_ANIMATION);
            }
            switch (msg.what){
                case MSG_ANIMATION:
                    changeImg();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 切换图片
     */
    public void changeImg(){
        switch (startAngle/90){
            case 0:
            case 4:
            case -4:
                mGoodImg.setImageResource(R.mipmap.lead_pic);
                break;
            case 1:
            case -3:
                mGoodImg.setImageResource(R.mipmap.ip);
                break;
            case 2:
            case -2:
                mGoodImg.setImageResource(R.mipmap.ipad);
                break;
            case 3:
            case -1:
            case -5:
                mGoodImg.setImageResource(R.mipmap.ipad1);
                break;
        }
    }

    /**
     * 在windows获得焦点的时候获取控件坐标
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus&&!mIsAnimation){
            createIntoAnimation(mText1,0-mText1.getLeft()-mText1.getWidth(), 0, 0, 0);
            createIntoAnimation(mText2, ScreenUtils.getWidth(this), 0, 0, 0);
            createIntoAnimation(mSLOneYuanArea, mSLOneYuanArea.getLeft(),mSLOneYuanArea.getLeft(),0-mSLOneYuanArea.getTop(), 0);
            createIntoAnimation(mSLTenYuanArea, 0,0,0-mSLOneHundredYuanArea.getTop(),0);
            createIntoAnimation(mSLOneHundredYuanArea, 0,0,0-mSLOneHundredYuanArea.getTop(),0);
            createIntoAnimation(mSLSmallRoom, 0,0,0-mSLSmallRoom.getTop(), 0);
            createIntoAnimation(mSLSellerArea, mSLSellerArea.getLeft(), mSLSellerArea.getLeft(), 0 - mSLSellerArea.getTop(), 0);
        }
    }

    /**
     * 设置进入后文字和按钮动画
     */
    public void createIntoAnimation(View view,float startx,float endx,float starty,float endy){
        TranslateAnimation animation=new TranslateAnimation(startx,endx,starty,endy);
        animation.setFillAfter(true);
        animation.setDuration(1000);
        view.startAnimation(animation);
    }
    /**
     * 在activity暂停的时候停止播放动画
     * 只让其在每次首次进入播放
     */
    @Override
    protected void onPause() {
        mIsAnimation=true;
        super.onPause();
    }

}
