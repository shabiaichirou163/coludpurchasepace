package com.cloudpurchase.veiw;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * 用户中心自定义View
 *
 */
public class UserView extends ScrollView{

    private LinearLayout mLinear;//父容器
    private LinearLayout mTopLinear;//顶部
    private LinearLayout mCenterLinear;//中部
    private LinearLayout mButtomLinear;//底部
    private int height;//屏幕高度
    private int width;//屏幕宽度

    public UserView(Context context){
        super(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        height = wm.getDefaultDisplay().getHeight();
        width = wm.getDefaultDisplay().getWidth();
        initView();
    }
    public UserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        height = wm.getDefaultDisplay().getHeight();
        width = wm.getDefaultDisplay().getWidth();
        initView();
    }

    /*
    初始化控件
     */
    private void initView(){
        setVerticalScrollBarEnabled(false);
        mLinear = new LinearLayout(getContext());
        mLinear.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        mLinear.setOrientation(LinearLayout.VERTICAL);


        mTopLinear = new LinearLayout(getContext());
        mTopLinear.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        mTopLinear.setBackgroundColor(Color.BLUE);


        mCenterLinear = new LinearLayout(getContext());
        mCenterLinear.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        mCenterLinear.setBackgroundColor(Color.YELLOW);


        mButtomLinear = new LinearLayout(getContext());
        mButtomLinear.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        mButtomLinear.setBackgroundColor(Color.BLACK);
        mLinear.addView(mTopLinear);
        mLinear.addView(mCenterLinear);
        mLinear.addView(mButtomLinear);
        addView(mLinear);
    }
    /*
    设置顶部View
     */
    public void setTopLinear(View view){
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mTopLinear.addView(view);
    }
    /*
      设置中部View
       */
    public void setCenterLinear(View view){
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mCenterLinear.addView(view);
    }
    /*
      设置底部View
       */
    public void setButtomLinear(View view){
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mButtomLinear.addView(view);
    }
}
