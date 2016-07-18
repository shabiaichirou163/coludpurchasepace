package com.cloudpurchase.veiw;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;


import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.utils.LogUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by oscar on 2016/6/28.
 * 自定义scorllView实现底部自动加载更多
 */
public class LoadMoreScrollView extends ScrollView {
    private List<Integer> mData=new ArrayList<Integer>();//记录scrollview高度
    private View mHeaderView,mFooterView;//头布局和底部布局
    private ImageView mHeaderImageView;
    /**
     * header 高度
     */
    private int mHeaderViewHeight;
    /**
     * footer高度
     */
    private int mFooterViewHeight;
    /**
     * header tip text
     */
    private TextView mHeaderTextView;
    /**
     * header refresh time
     */
    private TextView mHeaderUpdateTextView;
    /**
     * header progress bar
     */
    private ProgressBar mHeaderProgressBar;
    /**
     * 变为向下的箭头,改变箭头方向
     */
    private RotateAnimation mFlipAnimation;
    /**
     * 变为逆向的箭头,旋转
     */
    private RotateAnimation mReverseFlipAnimation;

    private int mStarty=-1;//记录初始y坐标

    private static final int PULL_REFRESH_STATE = 0;//下拉刷新状态
    private static final int PULL_RELEASE_STATE= 1;//松开刷新状态
    private static final int PULL_REFRESHING_STATE= 2;//正在刷新状态

    private int mCurrentState=PULL_REFRESH_STATE;//当前状态

    private boolean mIsScrolling;//正在加载更多时不能重新请求

    private ProgressBar  mProgress;//下拉加载对话框

    private TextView   mLoadTxt;//下拉加载的textView

    private LayoutInflater mLayoutInflater;
    public LoadMoreScrollView(Context context) {
        super(context);
        mLayoutInflater = LayoutInflater.from(context);

    }

    public LoadMoreScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public LoadMoreScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void initView(LinearLayout headerView,LinearLayout footView){
        mHeaderView = mLayoutInflater.inflate(R.layout.refresh_header, this, false);//获取header布局
        mHeaderImageView = (ImageView) mHeaderView
                .findViewById(R.id.pull_to_refresh_image);//获取header布局的ImageView
        mHeaderTextView = (TextView) mHeaderView
                .findViewById(R.id.pull_to_refresh_text);//获取下拉刷新/以及松开后刷新TextView
        mHeaderUpdateTextView = (TextView) mHeaderView
                .findViewById(R.id.pull_to_refresh_updated_at);//获取刷新时间TextView
        mHeaderProgressBar = (ProgressBar) mHeaderView
                .findViewById(R.id.pull_to_refresh_progress);//获取ProgressBar
        // 设置刷新时间为当前时间
        mHeaderUpdateTextView.setText("更新于:" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();//量测header布局的高度
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);//设置头布局隐藏
        if (headerView!=null){
            headerView.addView(mHeaderView);
        }

        mFooterView= mLayoutInflater.inflate(R.layout.refresh_footer_all, null);
        mFooterView.measure(0, 0);
        mFooterViewHeight=mFooterView.getMeasuredHeight();
        mProgress= (ProgressBar) mFooterView.findViewById(R.id.load_more_progress);
        mLoadTxt= (TextView) mFooterView.findViewById(R.id.load_more_text);
        mFooterView.setVisibility(View.GONE);
        if (footView!=null){
            footView.addView(mFooterView);
        }
        creatAnimation();
    }
    /**
     * 创建箭头动画
     */
    public void creatAnimation(){
        // 设置箭头的动画并且让动画完成后停留在此位置
        mFlipAnimation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mFlipAnimation.setDuration(250);
        mFlipAnimation.setFillAfter(true);

        mReverseFlipAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setDuration(250);
        mReverseFlipAnimation.setFillAfter(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mStarty= (int) ev.getRawY();//记录开始坐标
                break;
            case MotionEvent.ACTION_MOVE:
                if (mCurrentState==PULL_REFRESHING_STATE){
                    return true;//如果正在刷新  不可移动
                }
                if (mStarty==-1){
                    mStarty=(int) ev.getRawY();//开始坐标为-1 重新记录
                }
                int end=(int) ev.getRawY();//记录结束坐标
                int des=(end-mStarty)/4; //缩减移动的距离  防止下拉头过于灵敏
                //往下滑动
                if (des>0&&getScrollY()==0){
                    int padding=des-mHeaderViewHeight;
                    if (des>=mHeaderViewHeight*2){
                        return true; //移动距离最大为mHeaderViewHeigt高度的2倍
                    }
                    mHeaderView.setPadding(0, padding,0,0);//设置头布局状态
                    if (padding>=0&&mCurrentState!=PULL_RELEASE_STATE){
                        mCurrentState=PULL_RELEASE_STATE;
                        refreshState();
                    }else if (padding<0&&mCurrentState!=PULL_REFRESH_STATE){
                        mCurrentState=PULL_REFRESH_STATE;
                        refreshState();
                    }
                    return  true;//事件拦截 自行处理
                }
                break;
            case MotionEvent.ACTION_UP:
                mStarty=-1;
                if (mCurrentState==PULL_RELEASE_STATE){
                    mCurrentState=PULL_REFRESHING_STATE;
                    mHeaderView.setPadding(0,0,0,0);
                    refreshState();
                }else if(mCurrentState==PULL_REFRESH_STATE){
                    mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 各种状态处理事件
     */

    public void refreshState(){
        switch (mCurrentState){
            case PULL_REFRESH_STATE:
                mHeaderImageView.clearAnimation();
                mHeaderImageView.startAnimation(mReverseFlipAnimation);
                mHeaderTextView.setText(R.string.pull_to_refresh_pull_label);
                break;
            case PULL_RELEASE_STATE:
                mHeaderTextView.setText(R.string.pull_to_refresh_release_label);
                mHeaderUpdateTextView.setVisibility(View.VISIBLE);
                mHeaderImageView.clearAnimation();
                mHeaderImageView.startAnimation(mFlipAnimation);
                break;
            case PULL_REFRESHING_STATE:
                mHeaderImageView.setVisibility(View.GONE);
                mHeaderImageView.clearAnimation();
                mHeaderImageView.setImageDrawable(null);
                mHeaderProgressBar.setVisibility(View.VISIBLE);
                mHeaderTextView.setText(R.string.pull_to_refresh_refreshing_label);
                if (onScorllPullAndLoadListener!=null){
                    onScorllPullAndLoadListener.setOnScorllPullListener();
                }
                break;
        }
    }
    public void onPullFinish() {
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
        mHeaderImageView.setVisibility(View.VISIBLE);
        mHeaderImageView.setImageResource(R.mipmap.pull_icon_big);
        mHeaderTextView.setText(R.string.pull_to_refresh_pull_label);
        mHeaderProgressBar.setVisibility(View.GONE);
        mHeaderUpdateTextView.setText("更新于:" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        mCurrentState = PULL_REFRESH_STATE;


        //上拉加载后底部加载可以执行
        mIsScrolling=false;
        mLoadTxt.setText("正在加载更多...");
        mProgress.setVisibility(View.VISIBLE);
        mFooterView.setVisibility(View.GONE);

    }


    public void LoadNew(){
        //上拉加载后底部加载可以执行
        mIsScrolling=false;
        //如果线下拉刷新清除集合
        if (mData!=null){
            mData.clear();
        }
        mLoadTxt.setText("正在加载更多...");
        mProgress.setVisibility(View.VISIBLE);
        mFooterView.setVisibility(View.GONE);
    }
    /**
     * 首次加载自定刷新
     */
    public void autoRefresh(){
        mCurrentState = PULL_REFRESHING_STATE;
        mHeaderView.setPadding(0, 0, 0, 0);
        mHeaderImageView.setVisibility(View.GONE);
        mHeaderImageView.clearAnimation();
        mHeaderImageView.setImageDrawable(null);
        mHeaderProgressBar.setVisibility(View.VISIBLE);
        mHeaderTextView.setText(R.string.pull_to_refresh_refreshing_label);
        mHeaderUpdateTextView.setText("更新于:" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    //------------------------------------上拉加载---------------------------------
    /**
     * 判断滑动底部
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    private int dec;
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        dec=getChildAt(0).getMeasuredHeight();
        if (dec>=getHeight()&&getHeight()+getScrollY()>=dec-mFooterViewHeight-10){
            mFooterView.setVisibility(View.VISIBLE);
        }
        //监听滑到底部  为避免多次调用  使用List记录
        if (mData!=null&&dec<=getHeight()+getScrollY()&&(!mData.contains(dec))&&
                !mIsScrolling &&dec>=getHeight()){
            mData.add(dec);
            mIsScrolling=true;
            handler.sendEmptyMessage(1);  //项目正常运行用
            LogUtils.e(dec+"&&&&&&&&&&"+getHeight()+"&&&&&&&&&&&"+getScrollY());
        }
        //如果线下拉刷新清除集合
        if (getScrollY()<=0&&mData!=null){
            mData.clear();
        }

    }
    public void displayAll(){
        mFooterView.setVisibility(View.VISIBLE);
        mLoadTxt.setText("已显示全部内容~");
        mProgress.setVisibility(View.GONE);
        mIsScrolling=true;
    }
    /**
     * handler接收滑到底部信息
     */
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (mData!=null&&onScorllPullAndLoadListener!=null&&msg.what==1){
                onScorllPullAndLoadListener.setOnScorllLoadListener();
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 上拉加载完成
     */
    public void downLoadFinish(){
        mIsScrolling=false;
        mFooterView.setVisibility(View.GONE);
    }
    /**
     * 获取回调接口对象
     */
    private OnScorllPullAndLoadListener onScorllPullAndLoadListener;
    public void setOnScorllPullAndLoadListener(OnScorllPullAndLoadListener onScorllPullAndLoadListener){
        this.onScorllPullAndLoadListener=onScorllPullAndLoadListener;
    }

    /**
     * 回调数据
     */
    public interface OnScorllPullAndLoadListener{
        public void setOnScorllPullListener();
        public void setOnScorllLoadListener();
    }

}
