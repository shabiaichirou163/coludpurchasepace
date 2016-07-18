package com.cloudpurchase.veiw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.cloudpurchase.entity.ADEntity;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by oscar on 2016/6/7.
 * 设置垂直滚动TextView
 */
public class VerticalScrollTextView extends TextView {
    private int mDuration; //文字从出现到显示消失的时间
    private int mInterval; //文字停留在中间的时长切换的间隔
    private List<ADEntity> mTexts; //显示文字的数据源
    private int mY = 0; //文字的Y坐标
    private int mIndex = 0; //当前的数据下标
    private Paint mPaintBack; //绘制内容的画笔
    private Paint mPaintFront; //绘制前缀的画笔
    private boolean isMove = true; //文字是否移动
    private String TAG = "ADTextView";
    private boolean hasInit = false;
    private Paint mMore;
    public VerticalScrollTextView(Context context) {
        super(context);
        init();
    }

    public VerticalScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalScrollTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    /**
     * 回传Url以便于点击事件连接
     */
    public interface onClickLitener {
        public void onClick(String mUrl);
    }
    private onClickLitener onClickLitener;
    public void setOnClickLitener(VerticalScrollTextView.onClickLitener onClickLitener) {
        this.onClickLitener = onClickLitener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (onClickLitener != null) {
                    onClickLitener.onClick(mTexts.get(mIndex).getmUrl());
                }
                break;
        }
        return true;
    }
    //设置数据源
    public void setmTexts(List<ADEntity> mTexts) {
        this.mTexts = mTexts;
    }

    //设置广告文字的停顿时间
    public void setmInterval(int mInterval) {
        this.mInterval = mInterval;
    }
    //设置文字从出现到消失的时长
    public void setmDuration(int mDuration) {
        this.mDuration = mDuration;
    }
    //设置前缀的文字颜色
    public void setFrontColor(int mFrontColor) {
        mPaintFront.setColor(mFrontColor);
    }
    //设置正文内容的颜色
    public void setBackColor(int mBackColor) {
        mPaintBack.setColor(mBackColor);
    }

    /**
     * 初始化数据
     */
    private void init() {
        mDuration = 10;
        mInterval = 3000;
        mIndex = 0;
        mPaintFront = new Paint();
        mPaintFront.setAntiAlias(true);
        mPaintFront.setDither(true);
        mPaintFront.setTextSize(dip2px(12));
        mPaintBack = new Paint();
        mPaintBack.setAntiAlias(true);
        mPaintBack.setDither(true);
        mPaintBack.setTextSize(dip2px(12));

    }

    /**
     * 方便进行屏幕适配
     * @param dip
     * @return
     */
    public int dip2px(int dip){
        float density = this.getContext().getResources().getDisplayMetrics().density;
        return (int)(dip*density+0.5f);
    }
    /**
     * 重新onDraw方法进行文字回执
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (mTexts != null) {
            ADEntity model = mTexts.get(mIndex);
            String font = model.getmFront();
            String back = model.getmBack();

            //绘制前缀
            Rect indexBound = new Rect();
            mPaintFront.getTextBounds(font, 0, font.length(), indexBound);

            //绘制内容文字
            Rect contentBound = new Rect();
            mPaintBack.getTextBounds(back, 0, back.length(), contentBound);

            if (mY == 0 && hasInit == false) {
                mY = getMeasuredHeight()-indexBound.top/2;
                hasInit = true;
            }
            //移动到最上面
            if (mY == 0 - indexBound.bottom) {
                mY = getMeasuredHeight()-indexBound.top/2;
                mIndex++;
            }
            //布局宽度
            int layoutWidth=getMeasuredWidth()-getPaddingRight()-getPaddingLeft();
            //字体的总宽度
            int textViewWidth=indexBound.width()+contentBound.width();
            //单个字体占宽度
            int singleTxtViewWidth=indexBound.width()/font.length();
            //可以下剩余的长度
            int reminLength=(layoutWidth-indexBound.width())/singleTxtViewWidth;
            if (textViewWidth>layoutWidth){
                //只绘制剩余的部分 不绘制所有
                canvas.drawText(back, 0, reminLength, (indexBound.right - indexBound.left) + 20, mY, mPaintBack);
            }else{
                canvas.drawText(back, 0, back.length(), (indexBound.right - indexBound.left) + 20, mY, mPaintBack);
            }
            //分别绘制不同颜色的字体
            //canvas.drawText(more, 0, more.length(), (indexBound.right - indexBound.left) + contentBound.right + 20, mY, mMore);
            canvas.drawText(font, 0, font.length(), 10, mY, mPaintFront);
            //移动到中间
            if (mY == getMeasuredHeight() / 2 - (indexBound.top + indexBound.bottom) / 2) {
                isMove = false;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        postInvalidate();
                        isMove = true;
                    }
                }, mInterval);
            }
            mY -= 1;
            //循环使用数据
            if (mIndex == mTexts.size()) {
                mIndex = 0;
            }
             //如果是处于移动状态时的,则延迟绘制
             //计算公式为一个比例,一个时间间隔移动组件高度,则多少毫秒来移动1像素
            if (isMove) {
                postInvalidateDelayed(mDuration / getMeasuredHeight());
            }
        }
    }
}
