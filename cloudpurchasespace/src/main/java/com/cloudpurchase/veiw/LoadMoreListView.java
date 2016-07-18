package com.cloudpurchase.veiw;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.utils.LogUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by oscar on 2016/6/28.
 * 自定义ListView 下拉刷新 滑到底部加载更多
 */
public class LoadMoreListView extends ListView {
    private LayoutInflater mLayoutInflater;
    private List<Integer> mData=new ArrayList<Integer>();//记录LisView的总数  防止多次发送
    private View mHeaderView,mFooterView;//头布局和底部布局
    private ImageView mHeaderImageView;//头部箭头布局
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

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLayoutInflater =LayoutInflater.from(context);
        initView();
    }
    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLayoutInflater =LayoutInflater.from(context);
        initView();
    }
    public LoadMoreListView(Context context) {
        super(context);
        mLayoutInflater =LayoutInflater.from(context);
        initView();
    }

    /**
     * 添加下拉头布局和底部布局
     */
    public void initView(){
        // header view   初始化header布局以及添加头布局
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
        addHeaderView(mHeaderView);

        //添加底部布局
        mFooterView= mLayoutInflater.inflate(R.layout.refresh_footer_all,null);
        mFooterView.measure(0,0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();//量测footer布局的高度
        mFooterView.setVisibility(View.GONE);
        addFooterView(mFooterView);
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
                if (des>0&&getFirstVisiblePosition()==0){
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
                if (onScorllListener!=null){
                    onScorllListener.setOnScorllPullListener();
                }
                break;
        }
    }
    public void onPullFinish(){
        mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);
        mHeaderImageView.setVisibility(View.VISIBLE);
        mHeaderImageView.setImageResource(R.mipmap.pull_icon_big);
        mHeaderTextView.setText(R.string.pull_to_refresh_pull_label);
        mHeaderProgressBar.setVisibility(View.GONE);
        mHeaderUpdateTextView.setText("更新于:" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        mCurrentState=PULL_REFRESH_STATE;
    }



    //------------------------------------上拉加载---------------------------------
    /**
     * 在windows获取焦点是做listView setOnScrollListener确保回调赋值
     * @param hasWindowFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount) {
                    mFooterView.setVisibility(View.VISIBLE);
                    View lastItemView=getChildAt(getChildCount()-1);
                    if ((getBottom())==lastItemView.getBottom()&&(!mData.contains(totalItemCount))&&!mIsScrolling) {
                        mData.add(totalItemCount);
                        mIsScrolling=true;
                        // handler.sendEmptyMessage(1);  //项目正常运行用
                        handler.sendEmptyMessageDelayed(1,2000);//测试用
                    }
                }
                if (firstVisibleItem==0){
                    mData.clear();
                }
            }
        });
        super.onWindowFocusChanged(hasWindowFocus);
    }
    /**
     * handler接收滑到底部信息
     */
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (onScorllListener!=null&&msg.what==1){
                onScorllListener.setOnScorllLoadListener();
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
    private OnScorllPullAndLoadListener onScorllListener;
    public void setOnScorllPullAndLoadListener(OnScorllPullAndLoadListener onScorllListener){
        this.onScorllListener=onScorllListener;
    }

    /**
     * 回调数据
     */
    public interface OnScorllPullAndLoadListener{
        public void setOnScorllPullListener();
        public void setOnScorllLoadListener();
    }

}
