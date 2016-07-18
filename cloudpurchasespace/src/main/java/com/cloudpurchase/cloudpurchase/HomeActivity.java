package com.cloudpurchase.cloudpurchase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.cloudpurchase.base.BaseFragmentActivity;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.fragment.NewShownFragment;
import com.cloudpurchase.fragment.OneYuanAreaFragment;
import com.cloudpurchase.fragment.ShoppingCartFragment;
import com.cloudpurchase.fragment.ShowOrderFragment;
import com.cloudpurchase.fragment.UserFragment;
import com.cloudpurchase.net.HttpRequest;
import com.cloudpurchase.utils.Constants;
import com.cloudpurchase.utils.EncryptionAndDecryptUtils;
import com.cloudpurchase.utils.FileUtils;
import com.cloudpurchase.utils.InsertSuccessful;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.utils.RequestResultIn;
import com.cloudpurchase.utils.ScreenUtils;
import com.cloudpurchase.utils.SharedFileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Home页的Activity
 *
 *根据首页(SelectActivity)Button判断用户是点击的button加载不同的布局(一元区，十元区。。小房间，卖家区)
 *
 * @author oscar
 */
public class HomeActivity extends BaseFragmentActivity implements RadioGroup.OnCheckedChangeListener,View.OnTouchListener
,View.OnClickListener,InsertSuccessful {
    private android.support.v4.app.FragmentManager fragmentManager;//获取Fragment管理器
    private FragmentTransaction transaction;
    private OneYuanAreaFragment oneYuanAreaFragment;//一元专区
    private RadioGroup mBottomGuid;
    private NewShownFragment mNewShownFragment;//最新揭晓
    private ImageView mDragImg ;//悬浮按钮
    private UserFragment mUserFragment;//用户中心
    private ShowOrderFragment mShowOrderFragment;//晒单
    private ShoppingCartFragment mShoppingFragment;//购物车
    private int mRbId[]={R.id.buttom_homePage,R.id.buttom_newest,R.id.buttom_shoppingCart,R.id.buttom_showOrder,R.id.buttom_user};
    private RadioButton mRadioButton[];
    private TextView mShoppingCunt;//购物车文字提醒
    private MyApplication mApplication;//全局application
    private String mFlag;//跳转标示
    private static FileUtils mFilUtils;//本地存储工具类
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg!=null&&msg.what==0&&mAnimationLayout!=null){
                mAnimationLayout.removeAllViews();//动画结束后清除view
            }
            super.handleMessage(msg);
        }
    } ;

    @Override
    public void initView() {
        setContentView(R.layout.activity_buttom);
        mApplication = (MyApplication) this.getApplicationContext();
        mDragImg= (ImageView) findViewById(R.id.dragImg);//签到按钮

        mBottomGuid= (RadioGroup) findViewById(R.id.buttom_guid_gp);
        mShoppingCunt= (TextView)findViewById(R.id.shopping_count);
        mRadioButton=new RadioButton[mRbId.length];

        for (int i=0;i<mRbId.length;i++){
            mRadioButton[i]= (RadioButton) this.findViewById(mRbId[i]);
        }
        getDataFromOtherActivity();
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        addFragment(transaction);
        goneFragment();
        if (mFlag!=null&&mFlag.equals("loot")){
            mShoppingFragment=new ShoppingCartFragment();
            transaction.add(R.id.fragmentLayout, mShoppingFragment);
            mRadioButton[0].setChecked(false);
            mRadioButton[0].setTextColor(getResources().getColor(R.color.text_color_block));
            mRadioButton[2].setChecked(true);
            mRadioButton[2].setTextColor(getResources().getColor(R.color.text_color_red));
        }else if(mFlag!=null&&mFlag.equals("login")){
            mUserFragment = new UserFragment();
            transaction.add(R.id.fragmentLayout, mUserFragment);
            mRadioButton[0].setChecked(false);
            mRadioButton[0].setTextColor(getResources().getColor(R.color.text_color_block));
            mRadioButton[4].setChecked(true);
            mRadioButton[4].setTextColor(getResources().getColor(R.color.text_color_red));
        }else{
            switch (mApplication.getmSeclectFrag()) {
                case "oneYuanArea":
                case "tenYuanArea":
                case "oneHundredYuanArea":
                case "smallRoom":
                case "sellArea":
                case "sellerShop":
                    transaction.show(oneYuanAreaFragment);
                    break;
            }
        }
        transaction.commit();
        mFilUtils = new FileUtils(this);
        initUserID();
    }

    /**
     * 获取跳转携带数据
     */
    public  void getDataFromOtherActivity(){
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (null!=bundle){
            mFlag = bundle.getString("flag");
        }
        LogUtils.e(bundle+" ############onCreate#############"+mApplication.getmSeclectFrag());
    }
    @Override
    public void setOnclick() {
        mBottomGuid.setOnCheckedChangeListener(this);
        if (mDragImg!=null){
            mDragImg.setOnClickListener(this);
            mDragImg.setOnTouchListener(this);
        }
    }
    @Override
    public void initList() {

    }
    /*
     *RadioGroup监听事件
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i=0;i<mRbId.length;i++){
            if (checkedId==mRbId[i]){
                mRadioButton[i].setTextColor(getResources().getColor(R.color.text_color_red));
            }else {
                mRadioButton[i].setTextColor(getResources().getColor(R.color.text_color_block));
            }
        }
        transaction = fragmentManager.beginTransaction();
        goneFragment();
        switch (checkedId){
            case R.id.buttom_homePage:
                mDragImg.setVisibility(View.VISIBLE);
                transaction.show(oneYuanAreaFragment);
                break;
            case R.id.buttom_newest:
                mDragImg.setVisibility(View.GONE);
                transaction.show(mNewShownFragment);
                break;
            case R.id.buttom_shoppingCart:
                mDragImg.setVisibility(View.GONE);
                mShoppingFragment=new ShoppingCartFragment();
                transaction.add(R.id.fragmentLayout, mShoppingFragment);
                break;
            case R.id.buttom_showOrder:
                mDragImg.setVisibility(View.GONE);
                transaction.show(mShowOrderFragment);
                break;
            case R.id.buttom_user:
                mDragImg.setVisibility(View.GONE);
                initUserID();
                //判读用户是否为登录状态，如果没有登录跳转至登录界面
                if(!MyApplication.USER_IS_LOGIN_FLAG){
                    mUserFragment = new UserFragment();
                    transaction.add(R.id.fragmentLayout, mUserFragment);
                    transaction.hide(mUserFragment);
                    Intent mIntent = new Intent(this,LoginActivity.class);
                    startActivity(mIntent);
                }else if(MyApplication.USER_IS_LOGIN_FLAG){
                    mUserFragment = new UserFragment();
                    transaction.add(R.id.fragmentLayout, mUserFragment);
                    transaction.show(mUserFragment);
                }else{
                    transaction.show(mUserFragment);
                }
                break;
        }
        transaction.commit();
    }
    /*
  *判断是否已经等陆过，如果已经登录过就从本地获取用户信息
  */
    private void initUserID(){
        SharedFileUtils util = new SharedFileUtils(this,"USER_INFO", Activity.MODE_PRIVATE);
        String id = util.readStringFile("user_id","");
        if(id.equals("")){
            MyApplication.USER_IS_LOGIN_FLAG = false;
            return;
        }else{
            MyApplication.USER_ID = util.readStringFile("user_id","");
            MyApplication.USER_PHONE = util.readStringFile("phone","");
            MyApplication.USER_NICKNAME = util.readStringFile("nickName","");
            MyApplication.USER_TOKEN = util.readStringFile("token","");
            MyApplication.USER_NAME = util.readStringFile("username","");
            MyApplication.USER_INTEGTAL = util.readStringFile("integtal","0");
            MyApplication.USER_BIG_GOLDEN = util.readStringFile("bigGolden","0");
            MyApplication.USER_SMALL_GOLDEN = util.readStringFile("smallGolden","0");
            MyApplication.USER_HEADIMG_URL = util.readStringFile("headImgUrl","");
            MyApplication.USER_SEX = util.readStringFile("sex","");
            MyApplication.USER_USERRANK = util.readStringFile("userrank","1");
            MyApplication.USER_RANK_NAME = util.readStringFile("rankName","出入江湖");
            MyApplication.USER_IS_LOGIN_FLAG = true;
        }

    }
    /**
     * 设置签到按钮点击效果
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        PointF startPostion=new PointF();
        Matrix matrix=new Matrix();
        Matrix currentPostion=new Matrix();
        switch (event.getAction()&MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                currentPostion.set(mDragImg.getImageMatrix());
                startPostion.set(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                float x=event.getX()-startPostion.x;
                float y=event.getY()-startPostion.y;
                matrix.set(currentPostion);
                matrix.postTranslate(x, y);
                break;
        }
        mDragImg.setImageMatrix(matrix);
        return false;
    }

    /**
     * 悬浮按钮随着手指的移动
     */
//    private int startX,startY;
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//
//        switch (event.getAction()&MotionEvent.ACTION_MASK){
//            case MotionEvent.ACTION_DOWN:
//                //记录初始位置
//                startX= (int) event.getRawX();
//                startY= (int) event.getRawY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                //移动位置
//                int newX=(int) event.getRawX();
//                int newY=(int) event.getRawY();
//                //移动距离
//                int dx=newX-startX;
//                int dy=newY-startY;
//                //拿到原始坐标
//                int oldL=v.getLeft();
//                int oldR=v.getRight();
//                int oldT=v.getTop();
//                int oldB=v.getBottom();
//
//                //移动后新坐标
//                int newL=dx+oldL;
//                int newR=dx+oldR;
//                int newT=oldT+dy;
//                int newB=oldB+dy;
//                if (newL<=0){
//                    newL=0;
//                    newR=v.getWidth();
//                }
//                if (newR>=ScreenUtils.getWidth(this)){
//                    newR=ScreenUtils.getWidth(this);
//                    newL=ScreenUtils.getWidth(this)-v.getWidth();
//                }
//                if (newT<=0){
//                    newT=0;
//                    newB=v.getHeight();
//                }
//                mBottomGuid.measure(0,0);
//                if (newB>=ScreenUtils.getHeight(this)-mBottomGuid.getMeasuredHeight()){
//                    newB=ScreenUtils.getHeight(this)-mBottomGuid.getMeasuredHeight();
//                    newT=ScreenUtils.getHeight(this)-mBottomGuid.getMeasuredHeight()-v.getHeight();
//                }
//                //重新绘制
//                v.layout(newL, newT, newR, newB);
//                //记录下一次开始坐标
//                startX= (int) event.getRawX();
//                startY= (int) event.getRawY();
//                break;
//            case MotionEvent.ACTION_UP:
//                //防止父布局重新绘制导致回到初始位置
//                int lastX=v.getLeft();
//                int lastY=v.getTop();
//                FrameLayout.LayoutParams params= new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
//                params.leftMargin=lastX;
//                params.topMargin=lastY;
//                v.setLayoutParams(params);
//                break;
//        }
//
//        return false;
//    }
    /**
     * 添加Fragment
     */
    public void addFragment(FragmentTransaction transaction){
        mNewShownFragment=new NewShownFragment();
        transaction.add(R.id.fragmentLayout, mNewShownFragment);

        mShowOrderFragment=new ShowOrderFragment();
        transaction.add(R.id.fragmentLayout, mShowOrderFragment);

        oneYuanAreaFragment = new OneYuanAreaFragment();
        transaction.add(R.id.fragmentLayout, oneYuanAreaFragment);//将一元专区Framgment添加入Activity中

    }

    /**
     * 隐藏Fragment方法
     */
    private void goneFragment(){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (oneYuanAreaFragment != null) {
            ft.hide(oneYuanAreaFragment);
        }
        if(mUserFragment != null){
            ft.hide(mUserFragment);
        }
        if (null!=mNewShownFragment){
            ft.hide(mNewShownFragment);
        }
        if (null!=mShowOrderFragment){
            ft.hide(mShowOrderFragment);
        }
        if (null!=mShoppingFragment){
            ft.hide(mShoppingFragment);
        }
        ft.commit();
    }

    /**
     * 签到按钮点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (MyApplication.USER_IS_LOGIN_FLAG){
            mDragImg.setVisibility(View.GONE);
            Toast.makeText(this,"签到完成",Toast.LENGTH_SHORT).show();
            //待留后续签到逻辑实现
        }else {
            Toast.makeText(this,"请先登录后,点击签到",Toast.LENGTH_SHORT).show();
            toOtherActivity(LoginActivity.class);
        }


    }

    private List<GoodsDetails> mAddShopList;//查询数据库资料

    /**
     * 回调数据
     * @param drawable
     * @param startLoaction
     */
    @Override
    public void InsertSuccessful(Drawable drawable, int[] startLoaction,String type) {
        if (drawable!=null&&startLoaction!=null){
            creatAnimation(drawable, startLoaction,type);
        }else{
            downLoadShoppingCartInfo();
        }
    }

    /**
     * 获取购物车商品种类
     */
    public void downLoadShoppingCartInfo(){
        String url= Constants.GOOD_SHOPPING_CART_LIST+MyApplication.USER_ID+"?"+"token"+"="+ MyApplication.USER_TOKEN;
        HttpRequest.getHttpRequest().requestGET(url, null, new RequestResultIn() {
            @Override
            public void requstSuccful(String result) {
                try {
                    JSONObject object=new JSONObject(result);
                    JSONObject ob=object.getJSONObject("data");
                    JSONArray array=ob.getJSONArray("shopCart");
                    addShoppingCartCount(array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void requstError(VolleyError error) {

            }
        });
    }
    /**
     * 添加商品到购物车后购物车右上角文字提醒
     */
    public void addShoppingCartCount(JSONArray array){
        if (array!=null&&array.length()>0){
            mShoppingCunt.setVisibility(View.VISIBLE);
            mShoppingCunt.setText(array.length()+"");
        }else {
            mShoppingCunt.setVisibility(View.GONE);
        }
    }

    /**
     * 创建购物车动画
     * @param drawable
     * @param startLoaction
     */
    private ViewGroup mAnimationLayout;
    public void creatAnimation(Drawable drawable,int startLoaction[],String type){
        if (mAnimationLayout==null){
            mAnimationLayout= CreatAnimationLayout();
        }
        ImageView img=new ImageView(this);
        img.setImageDrawable(drawable);

        final View view = addViewToAnimLayout(mAnimationLayout,img,startLoaction,type);
        //获取购物车信息
        int endLoaction[]=new int[2];
        mRadioButton[2].getLocationInWindow(endLoaction);
        int startx=startLoaction[0];
        int starty=startLoaction[1];
        int endx=endLoaction[0];
        int endy=endLoaction[1];
        TranslateAnimation translatex=null;
        ScaleAnimation scale=null;
        switch (type){
            case "other":
                translatex=new TranslateAnimation(0,endx-startx-ScreenUtils.getWidth(this)/30,0,0);
                scale=new ScaleAnimation(1f,0.3f,1f,0.3f, Animation.RELATIVE_TO_SELF,0.5f,
                        Animation.RELATIVE_TO_SELF,0.5f);
                break;
            case "seller":
                translatex=new TranslateAnimation(0,endx-startx,0,0);
                scale=new ScaleAnimation(1f,0.5f,1f,0.5f, Animation.RELATIVE_TO_SELF,0.5f,
                        Animation.RELATIVE_TO_SELF,0.5f);
                break;
        }

        translatex.setInterpolator(new LinearInterpolator());
        TranslateAnimation translatey=new TranslateAnimation(0,0,0,endy-starty-ScreenUtils.getHeight(this)/30);
        translatey.setInterpolator(new AccelerateInterpolator());

        AnimationSet set=new AnimationSet(false);
        set.setDuration(800);
        set.addAnimation(scale);
        set.addAnimation(translatey);
        set.addAnimation(translatex);
        set.setFillAfter(true);


        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //发送至handler处理动画残余
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(set);

    }

    /**
     * 创建动画层
     * @return
     */
    public ViewGroup CreatAnimationLayout(){
        ViewGroup vg= (ViewGroup) this.getWindow().getDecorView();//获取顶层试图
        FrameLayout animationLayout=new FrameLayout(this);
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                ,FrameLayout.LayoutParams.MATCH_PARENT);//设置动画层匹配父屏幕
        animationLayout.setLayoutParams(lp);
        animationLayout.setBackgroundResource(android.R.color.transparent);//设置动画层为透明色
        vg.addView(animationLayout);
        return animationLayout;
    }

    /**
     * 添加ImageView到动画层,并设置ImageView的初始位置
     * @param vg
     * @param view
     * @param startLoaction
     * @return
     */
    public View addViewToAnimLayout(ViewGroup vg,ImageView view,int startLoaction[],String type){
        vg.addView(view);//把动画添加到动画层
        int x = startLoaction[0];
        int y = startLoaction[1];
        FrameLayout.LayoutParams lp=null;
        switch (type){
            case "other":
                 lp = new FrameLayout.LayoutParams(
                         dip2px(this,115),
                        dip2px(this,115));
                break;
            case "seller":
                lp = new FrameLayout.LayoutParams(
                        dip2px(this,75),
                        dip2px(this,75));
                break;
        }

        lp.leftMargin =x;
        lp.topMargin =y;
        view.setLayoutParams(lp);
        return view;
    }
    /**
     * dip，dp转化成px 用来处理不同分辨路的屏幕
     * @param context
     * @param dpValue
     * @return
     */
    private int dip2px(Context context,float dpValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale +0.5f);
    }
    /**
     *
     * 内存过低时及时处理动画产生的未处理冗余
     *
     */
    @Override
    public void onLowMemory() {
        if(mAnimationLayout!=null)
        mAnimationLayout.removeAllViews();
        super.onLowMemory();
    }
    /*
    * Activity可见时实现购物车提醒
   */
    @Override
    public void onResume() {
        downLoadShoppingCartInfo();
        super.onResume();
    }
    /**
     *返回逻辑
     */
    @Override
    public void onBackPressed() {
        if (mApplication.getmSeclectFrag().equals("sellerShop")) {
            mApplication.setmSeclectFrag("smallRoom");
            this.finish();
        } else if (!mRadioButton[0].isChecked()){
            mRadioButton[0].setChecked(true);
        }else if (mRadioButton[0].isChecked()){
            toOtherActivity(SelectActivity.class);
        }
    }
  //当activity被回收时 fragment一起被释放
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }
}
