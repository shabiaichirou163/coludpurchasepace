package com.cloudpurchase.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cloudpurchase.adpater.AutoDisplayAdapter;
import com.cloudpurchase.cloudpurchase.R;

import java.util.List;


/**
 * Created by Administrator on 2016/6/2.
 * ViewPager Tools class
 * viewPager工具类  实现轮播和手动滑动
 *
 */
public class ViewPagerUtils implements ViewPager.OnPageChangeListener{
    private boolean mIsTrue;//设置是否轮播
    private ViewPager mViewPager;
    private int mCurrentIndex;//当前位置
    private int mIcon [];//图片资源集合
    private ImageView mImg [];//引导点集合
    private int mCount;//自定义图片的数量   轮播设定为Interger.maxValue  其它为mIcon.length
    private Button mGuidBtn;//引导页点击进入按钮
    /**
     * 请求更新显示的View。
     */
    protected static final int MSG_UPDATE_IMAGE  = 1;
    /**
     * 请求暂停轮播。
     */
    protected static final int MSG_KEEP_SILENT   = 2;
    /**
     * 请求恢复轮播。
     */
    protected static final int MSG_BREAK_SILENT  = 3;
    /**
     * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
     * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
     * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
     */
    protected static final int MSG_PAGE_CHANGED  = 4;

    //轮播间隔时间
    protected static final long MSG_DELAY = 3000;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
            if (mHandler.hasMessages(MSG_UPDATE_IMAGE)){
                mHandler.removeMessages(MSG_UPDATE_IMAGE);
            }
            if (mIsTrue) {
                switch (msg.what) {
                    case MSG_UPDATE_IMAGE://自动
                        mViewPager.setCurrentItem(mCurrentIndex);
                        mCurrentIndex++;
                        //准备下次播放
                        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                        break;
                    case MSG_KEEP_SILENT://手动  请求暂停
                        break;
                    case MSG_BREAK_SILENT://回复轮播
                        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                        break;
                    case MSG_PAGE_CHANGED://记录页面position
                        //记录当前的页号，避免播放的时候页面显示不正确。
                        mCurrentIndex = msg.arg1;
                        break;
                }
            }
        }
    };

    /*
     *ViewPager工具类构造方法
     */
    public ViewPagerUtils(Context context, ViewPager viewPager, int icon[], LinearLayout layout,int count,Button btn,List<String> url){
        this.mViewPager=viewPager;

        if (url!=null){
            mImg=new ImageView[url.size()];
        }else if (icon!=null){
            mIcon=new int[icon.length];
            mIcon=icon;
            mImg=new ImageView[icon.length];
        }

        for (int i=0;i<mImg.length;i++){
            mImg[i]=new ImageView(context);
            if (i==0){
                mImg[i].setImageResource(R.mipmap.point_select_2x);
            }else{
                mImg[i].setImageResource(R.mipmap.point_n_2x);
            }
            layout.addView(mImg[i], i);
            //设置点间距
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(15,0,0,0);
            mImg[i].setLayoutParams(lp);
        }
        mCount=count;
        AutoDisplayAdapter adapter=new AutoDisplayAdapter(context,mIcon,count,url);

        mViewPager.setAdapter(adapter);
        mGuidBtn=btn;
    }

    /*
     *监听ViewPager滑动事件
     */
    public void event(){
        if (mViewPager!=null){
            mViewPager.setOnPageChangeListener(this);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
      //配合Adapter的mCurrentIndex字段进行设置。
        if (mIsTrue) {
            mHandler.sendMessage(Message.obtain(mHandler, MSG_PAGE_CHANGED, position, 0));
        }
        for (int i = 0; i < mImg.length; i++) {
            switch (mCount){
                case Integer.MAX_VALUE:
                    if (position % mIcon.length == i) {
                        mImg[i].setImageResource(R.mipmap.point_select_2x);
                    } else {
                        mImg[i].setImageResource(R.mipmap.point_n_2x);
                    }
                    break;
                default:
                    if (position == i) {
                        mImg[i].setImageResource(R.mipmap.point_select_2x);
                    } else {
                        mImg[i].setImageResource(R.mipmap.point_n_2x);
                    }
                   if (null!=mGuidBtn){
                       if (position==mIcon.length-1){
                           mGuidBtn.setVisibility(View.VISIBLE);
                       }else {
                           mGuidBtn.setVisibility(View.INVISIBLE);
                       }
                   }
                    break;
            }
        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {
        if (state==ViewPager.SCROLL_STATE_DRAGGING&&mIsTrue){  //SCROLL_STATE_DRAGGING手动滑动时调用
            mHandler.sendEmptyMessage(MSG_KEEP_SILENT);
        }
        if (state == ViewPager.SCROLL_STATE_IDLE&&mIsTrue){
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE,MSG_DELAY);
        }
    }
    /*
     *线程发送消息使ViewPager轮播
     */
    public void viewPagerAutoScroll(boolean isTure){
        mIsTrue=isTure;
        new Thread(){
            @Override
            public void run() {
                if (mIsTrue) {
                    mHandler.sendEmptyMessage(MSG_UPDATE_IMAGE);
                }
            }
        }.start();
    }

}
