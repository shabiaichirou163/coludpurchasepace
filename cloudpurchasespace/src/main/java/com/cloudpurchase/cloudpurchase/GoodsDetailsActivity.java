package com.cloudpurchase.cloudpurchase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.cloudpurchase.adpater.GoodsDetailsUsrInfoAdpater;
import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.entity.RequestInfo;
import com.cloudpurchase.entity.UserInfo;
import com.cloudpurchase.net.HttpRequest;
import com.cloudpurchase.utils.Constants;
import com.cloudpurchase.utils.JsonReslove;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.utils.RequestResultIn;
import com.cloudpurchase.utils.ViewPagerUtils;
import com.cloudpurchase.veiw.LoadMoreScrollView;
import com.cloudpurchase.veiw.MyListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 普通商品详情界面
 */
public class GoodsDetailsActivity extends BaseActivity implements View.OnClickListener,LoadMoreScrollView.OnScorllPullAndLoadListener{
    private LinearLayout mGoodsInfo,mAgoShow,mShowOreder;//进入图文详情  往期揭晓   订单分享
    private ImageButton mBackBtn,mShoppingCartBtn;//返回  购物车按钮
    private Button mLootGoods,mAddOrder,mSoonGo;//立即夺宝  加入订单按钮，马上前往
    private TextView mHottingTxt;
    private String mActivityId;
    private TextView mGoodCount,mCurrentTimeTxt;//购买商品的数量   当天揭晓时间
    private ViewPager mViewPager;  //viewPager
    private ViewPagerUtils mUtils;//viewpager工具类 实现图片滑动
    private LinearLayout mPointLayout;
    private LoadMoreScrollView mScrollView;//下拉刷新 上拉加载ScrollView
    private LinearLayout mHeaderLayout,mFootLayout;//下拉刷新头布局
    private MyListView mUserInfoLst;
    private String mUrl="";//请求地址
    private JsonReslove mReslove;
    private GoodsDetailsUsrInfoAdpater mUserInfoAdpater;//适配器
    private String mMode="other";//other 其它  footer 为上拉加载  header 为下拉刷新
    private TextView mNumber,mTotalNum,mRemainNum;//期号,总需人次 剩余人次
    private ProgressBar mProgress;
    private LinearLayout mDiffLayout;//在不同状态下加载不同的布局
    private TextView mLoginTxt;
    private JsonReslove mResloveData;//解析数据
    private TextView mGoodsDescription;//商品描述
    private TextView mGoodsStartTime;
    HashMap<String,String> mDataMap;//加入购物车参数
    @Override
    public void initView() {
        setContentView(R.layout.activity_goods_details);
        mResloveData=new JsonReslove(this);
        mDataMap=new HashMap<String,String>();
        //初始化下拉刷新 上拉自动加载等
        mScrollView= (LoadMoreScrollView) findViewById(R.id.goods_details_pullandload_layout);
        mHeaderLayout= (LinearLayout) findViewById(R.id.goods_details_header_layout);
        mFootLayout= (LinearLayout) findViewById(R.id.goods_details_foot_layout);
        mScrollView.initView(mHeaderLayout, mFootLayout);//添加头布局到LinearLayout
        mScrollView.setOnScorllPullAndLoadListener(this);

        mReslove=new JsonReslove(this);

        //返回按钮 购物车按钮 以及购物车数字提醒绑定
        mBackBtn= (ImageButton) findViewById(R.id.goodsDetails_back);
        mShoppingCartBtn= (ImageButton) findViewById(R.id.goodsDetails_shopping_cart);
        mGoodCount= (TextView) findViewById(R.id.activity_goods_details_shopping_count);

        //为防止跳转后直接显示到listView处   迫使焦点在最顶部
        mShoppingCartBtn.setFocusable(true);
        mShoppingCartBtn.setFocusableInTouchMode(true);
        mShoppingCartBtn.requestFocus();

        //商品详情图片实现ViewPager
        mGoodsDescription= (TextView) findViewById(R.id.goods_details_goods_description);
        mViewPager= (ViewPager) findViewById(R.id.goods_details_imageDisplay);
        mPointLayout= (LinearLayout) this.findViewById(R.id.goodsDetails_ponit_layout);

        mDiffLayout= (LinearLayout) findViewById(R.id.goods_details_load_diffLayout);

        //图文详情  往期揭晓  晒单分享  用户信息listview
        mGoodsInfo= (LinearLayout) findViewById(R.id.goodsDetails_goods_info);
        mAgoShow= (LinearLayout) findViewById(R.id.goodsDetails_ago_show);
        mShowOreder= (LinearLayout) findViewById(R.id.goodsDetails_showorder);
        mUserInfoLst= (MyListView) findViewById(R.id.goods_details_narmal_show_lst);

        //揭晓当天时间
        mGoodsStartTime= (TextView) findViewById(R.id.goods_details_narmal_time_start_txt);
        mCurrentTimeTxt= (TextView) findViewById(R.id.goods_details_narmal_time_txt);
        mCurrentTimeTxt.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        //立即夺宝  加入清单
        mLootGoods= (Button) findViewById(R.id.goodsDetails_loot_goods);
        mAddOrder= (Button) findViewById(R.id.goodsDetails_add_order);
        mHottingTxt= (TextView) findViewById(R.id.goodsDetails_hotting_txt);
        mSoonGo= (Button) findViewById(R.id.goodsDetails_soon_go);


        //获取携带的数据
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String flag="";
        if (bundle!=null) {
            mActivityId=bundle.getString("activityId");
            flag=bundle.getString("flag");
        }
        switch (flag){
            case "onefragment":
                addNormalLayout();
                break;
            case "newShown":
                addNewShownLayout();
                break;
            case "showned":
                addShownedLayout();
                break;
        }
        //请求数据
        downLoadData();

    }

    /**
     * 添加1 10 100元跳转过来布局
     */
    public void addNormalLayout(){
        View view=LayoutInflater.from(this).inflate(R.layout.activity_goods_details_normal_item1,null);
        mDiffLayout.addView(view);
        if(MyApplication.USER_IS_LOGIN_FLAG){
            //登录没有参加
            View  loginNoPart=LayoutInflater.from(this).inflate(R.layout.activity_goods_details_login_no_part,null);
            mDiffLayout.addView(loginNoPart);


        }else{
            //没有登录
            View noLoginView=LayoutInflater.from(this).inflate(R.layout.activity_goods_details_no_login,null);
            mDiffLayout.addView(noLoginView);
            getLoginTxt();
        }

        displayNormalLayout();
        //期号,总需人次 剩余人次
        mNumber= (TextView) findViewById(R.id.goodsDetails_data_number);
        mTotalNum= (TextView) findViewById(R.id.goodsDetails_total_num);
        mRemainNum= (TextView) findViewById(R.id.goodsDetails_remain);
        mProgress= (ProgressBar) findViewById(R.id.goodsDetails_prgress);

    }

    /**
     *显示1 10 100元按钮
     */
    public void displayNormalLayout(){
        mLootGoods.setVisibility(View.VISIBLE);
        mAddOrder.setVisibility(View.VISIBLE);
        mHottingTxt.setVisibility(View.GONE);
        mSoonGo.setVisibility(View.GONE);
    }

    /**
     * add即将揭晓跳转过来布局
     */
    public void addNewShownLayout(){
        View view=LayoutInflater.from(this).inflate(R.layout.activity_goods_details_soon_shown,null);
        mDiffLayout.addView(view);
        if(MyApplication.USER_IS_LOGIN_FLAG){
            //登录没有参加
            View  loginNoPart=LayoutInflater.from(this).inflate(R.layout.activity_goods_details_login_no_part,null);
            mDiffLayout.addView(loginNoPart);


        }else{
            //没有登录
            View noLoginView=LayoutInflater.from(this).inflate(R.layout.activity_goods_details_no_login,null);
            mDiffLayout.addView(noLoginView);
            getLoginTxt();
        }
        displayNewShownLayout();
    }

    /**
     *显示即将揭晓相关控件
     */
    public void displayNewShownLayout(){
        mLootGoods.setVisibility(View.GONE);
        mAddOrder.setVisibility(View.GONE);
        mHottingTxt.setVisibility(View.VISIBLE);
        mSoonGo.setVisibility(View.VISIBLE);
    }

    /**
     * 添加已经些页面
     */
    public void addShownedLayout(){
        View view=LayoutInflater.from(this).inflate(R.layout.activity_goods_details_showned,null);
        mDiffLayout.addView(view);
        if(MyApplication.USER_IS_LOGIN_FLAG){
            //登录没有参加
            View  loginNoPart=LayoutInflater.from(this).inflate(R.layout.activity_goods_details_login_no_part,null);
            mDiffLayout.addView(loginNoPart);


        }else{
            //没有登录
            View noLoginView=LayoutInflater.from(this).inflate(R.layout.activity_goods_details_no_login,null);
            mDiffLayout.addView(noLoginView);
            getLoginTxt();
        }
        displayNewShownLayout();
    }

    public  void getLoginTxt(){
        mLoginTxt= (TextView) findViewById(R.id.goods_details_login_txt);
    }
    @Override
    public void setOnclick() {
        mBackBtn.setOnClickListener(this);
        mShoppingCartBtn.setOnClickListener(this);
        mGoodsInfo.setOnClickListener(this);
        mAgoShow.setOnClickListener(this);
        mShowOreder.setOnClickListener(this);
        mLootGoods.setOnClickListener(this);
        mAddOrder.setOnClickListener(this);
        if (mLoginTxt!=null){
            mLoginTxt.setOnClickListener(this);
        }
    }

    @Override
    public void initList() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goodsDetails_goods_info:
                Bundle bu=new Bundle();
                bu.putString("url", "https://www.baidu.com/");
                toOtherActivity(MyWebView.class,bu);
                Toast.makeText(this,"WebView图文详情界面,加载URL,此处暂用https://www.baidu.com/",Toast.LENGTH_SHORT).show();
                break;
            case R.id.goodsDetails_ago_show:
                toOtherActivity(AgoShowActivity.class);
                break;
            case R.id.goodsDetails_showorder:
                toOtherActivity(ShowOrderShareActivity.class);
                break;
            case R.id.goodsDetails_back:
                this.finish();
                break;
            case R.id.goodsDetails_shopping_cart:
                Bundle bundle1=new Bundle();
                bundle1.putString("flag", "loot");
                toOtherActivity(HomeActivity.class, bundle1);
                this.finish();
                break;
            case R.id.goodsDetails_loot_goods:
                addToShoppingCart();
                Bundle bundle=new Bundle();
                bundle.putString("flag", "loot");
                toOtherActivity(HomeActivity.class, bundle);
                this.finish();
                break;
            case R.id.goodsDetails_add_order:
                addToShoppingCart();
                break;
            case R.id.goods_details_login_txt:
                toOtherActivity(LoginActivity.class);
                break;
        }
    }
    @Override
    protected void onResume() {
        downLoadShoppingCartInfo();
        super.onResume();
    }
    /**
     * 向服务器请求数据商品详情数据
     */

    public void downLoadData(){
        mUrl= Constants.GOODS_DETAILS_NORMAL+mActivityId;
        HttpRequest.getHttpRequest().requestGET(mUrl, null, new RequestResultIn() {
            @Override
            public void requstSuccful(String result) {
                jsonResloverData(result);
            }

            @Override
            public void requstError(VolleyError error) {

            }
        });

    }

    /**
     * 解析商品信息和用户参与信息
     * @param result
     */

    private GoodsDetails mGoodData;
    private List<UserInfo>  mUserData;
    public void jsonResloverData(String result){
        LogUtils.e(result);
        try {
            JSONObject object=new JSONObject(result);
            mGoodData=mResloveData.resloveGoodsDetails(object);
            setData();
            //mUserData=mResloveData.resloveUserInfo(object);
           // resloverData(mUserData);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置商品信息
     */
    public void setData(){
        mGoodsDescription.setText(mGoodData.getDescription());
        mNumber.setText("期号: "+mGoodData.getNumber());
        mTotalNum.setText("总需" + mGoodData.getTotal() + "人次");
        mRemainNum.setText(mGoodData.getRemaining() + "");
        mProgress.setMax(mGoodData.getTotal());
        mProgress.setProgress(mGoodData.getParticipate());
        mGoodsStartTime.setText(mGoodData.getCreateTime());
        List<String> imgUrl=mGoodData.getImgUrlArray();//获取图
        mUtils=new ViewPagerUtils(this,mViewPager,null,mPointLayout,imgUrl.size(),null,imgUrl);
        mUtils.event();

    }

//    /**
//     * 请求回来数据
//     * @param
//     */
//    public void resloverData(List<UserInfo>  userData){
//        if (userData!=null&&mUserData.size()>0) {
//            switch (mMode) {
//                case "other":
//                    mUserInfoAdpater = new GoodsDetailsUsrInfoAdpater(this, userData);
//                    mUserInfoLst.setAdapter(mUserInfoAdpater);
//                    break;
//                case "header":
//                    mUserInfoAdpater.headRefreshGoods(userData);
//                    mScrollView.onPullFinish();
//                    break;
//                case "footer":
//                    mUserInfoAdpater.footerRefreshAddGoods(userData);
//                    mScrollView.downLoadFinish();
//                    break;
//            }
//        }else if (userData!=null&&mUserData.size()<20){
//            mScrollView.displayAll();
//        }
//
//    }

    /**
     * 加入购物车网络请求
     *
     */
    public void addToShoppingCart(){
        if (mDataMap!=null){
            mDataMap.clear();
        }
        mDataMap.put("memberId",7+"");
        mDataMap.put("activityId",141+"");
        mDataMap.put("goodsnumber", 1 + "");
        mDataMap.put("token",MyApplication.USER_TOKEN);
        HttpRequest.getHttpRequest().requestPOST(Constants.GOODS_ADD_SHOPPING_CART,
                null, mDataMap, new RequestResultIn() {
                    @Override
                    public void requstSuccful(String result) {
                        LogUtils.e(result);//测试看结果
                        RequestInfo info=mReslove.resloverIsSuff(result);
                        if (info!=null&&info.getCode()==0){
                            downLoadShoppingCartInfo();
                            Toast.makeText(GoodsDetailsActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void requstError(VolleyError error) {
                        LogUtils.e(error.getClass().getSimpleName());//测试看结果
                    }
                });
    }


    /**
     * 添加商品到购物车后购物车右上角文字提醒
     */

    public void addShoppingCartCount(JSONArray array){
        if (array.length()>0){
            mGoodCount.setVisibility(View.VISIBLE);
            mGoodCount.setText(array.length()+"");
        }else {
            mGoodCount.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 获取购物车商品种类
     */
    public void downLoadShoppingCartInfo(){
        String url=Constants.GOOD_SHOPPING_CART_LIST+"?"+"token"+"="+MyApplication.USER_TOKEN;
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
     * 下拉刷新
     */
    @Override
    public void setOnScorllPullListener() {
        mMode="header";
        //downLoadData();

    }
    /**
     * 上拉加载
     */
    @Override
    public void setOnScorllLoadListener() {
        mMode= "footer";
        //downLoadData();
    }
  }
