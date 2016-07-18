package com.cloudpurchase.cloudpurchase;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.db.DBWrapper;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.fragment.SelcetClassification;
import com.cloudpurchase.utils.ViewPagerUtils;
import com.cloudpurchase.veiw.LoadMoreScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 小房间商品详情
 */
public class SmallRoomGoodsDetailsActivity extends BaseActivity implements LoadMoreScrollView.OnScorllPullAndLoadListener,View.OnClickListener{
    private LoadMoreScrollView mScrollView;//下拉刷新 上拉加载ScrollView
    private LinearLayout mHeaderLayout;//下拉刷新头布局
    private ImageButton mBackBtn,mShoppingCartBtn;//返回  购物车按钮
    private TextView mGoodCount;//购买商品的数量
    private int mIcon[]={R.mipmap.a,R.mipmap.viewpager,R.mipmap.viewpager1,R.mipmap.viewpager2,R.mipmap.viewpager3};
    private ViewPager mViewPager;  //viewPager
    private ViewPagerUtils mUtils;//viewpager工具类 实现图片滑动
    private LinearLayout mPointLayout;//指示点layout
    private LinearLayout mIntoShopLayout;//进入小房间layout
    private TextView mIntoDetailsTxt,mAgoShowTxt,mShareTxt;
    private Button mLootBtn;//参与夺宝
    private List<GoodsDetails> mAddShopList;//查询数据库商品信息
    private DBWrapper mDBWrapper;
    private GoodsDetails goodsDetails;
    private MyApplication mApplication;//全局application
    private LinearLayout mDiffLayout;//在不同状态下加载不同的布局
    @Override
    public void initView() {
        setContentView(R.layout.activity_small_room_goods_details);
        mApplication = (MyApplication) this.getApplicationContext();
        //初始化下拉刷新 上拉自动加载等
        mScrollView= (LoadMoreScrollView) findViewById(R.id.small_room_goods_details_pullandload_layout);
        mHeaderLayout= (LinearLayout) findViewById(R.id.small_room_goods_details_header_layout);
        mScrollView.initView(mHeaderLayout,null);//添加头布局到LinearLayout
        mScrollView.setOnScorllPullAndLoadListener(this);

        //初始化数据库类  数据库信息封装
        mDBWrapper=new DBWrapper(this);
        mAddShopList=new ArrayList<GoodsDetails>();

        //返回按钮 购物车按钮 以及购物车数字提醒绑定
        mBackBtn= (ImageButton) findViewById(R.id.small_room_goodsDetails_back);
        mShoppingCartBtn= (ImageButton) findViewById(R.id.small_room_goodsDetails_shopping_cart);
        mGoodCount= (TextView) findViewById(R.id.small_room_activity_goods_details_shopping_count);

        //商品详情图片实现ViewPager
        mViewPager= (ViewPager) findViewById(R.id.goods_details_imageDisplay);
        mPointLayout= (LinearLayout) this.findViewById(R.id.goodsDetails_ponit_layout);
        mUtils=new ViewPagerUtils(this,mViewPager,mIcon,mPointLayout,mIcon.length,null,null);
        mUtils.event();
        mDiffLayout= (LinearLayout) findViewById(R.id.goods_details_load_diffLayout);
        //进入商铺
        mIntoShopLayout= (LinearLayout) findViewById(R.id.small_detail_into_room);
        //进入图文详情  往期揭晓  一键分享  参与夺宝
        mIntoDetailsTxt= (TextView) findViewById(R.id.activity_goods_info_forseller_goodsdetails);
        mAgoShowTxt= (TextView) findViewById(R.id.activity_goods_info_forseller_agoshow);
        mShareTxt= (TextView) findViewById(R.id.activity_goods_info_forseller_share);
        mLootBtn= (Button) findViewById(R.id.small_activity_goods_info_loot_btn);
        addNormalLayout();
        //获取携带的数据
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null)
            goodsDetails= (GoodsDetails) bundle.getSerializable("goodsInfo");
    }


    /**
     * 添加1 10 100元跳转过来布局
     */
    public void addNormalLayout(){
        View view= LayoutInflater.from(this).inflate(R.layout.activity_goods_details_normal_item1,null);
        View noLoginView=LayoutInflater.from(this).inflate(R.layout.activity_goods_details_no_login,null);
        mDiffLayout.addView(view);
        mDiffLayout.addView(noLoginView);

//        //期号,总需人次 剩余人次
//        mNumber= (TextView) findViewById(R.id.goodsDetails_data_number);
//        mTotalNum= (TextView) findViewById(R.id.goodsDetails_total_num);
//        mRemainNum= (TextView) findViewById(R.id.goodsDetails_remain);
//        mProgress= (ProgressBar) findViewById(R.id.goodsDetails_prgress);
//
//        mNumber.setText("期号: "+goodsDetails.getNumber());
//        mTotalNum.setText("总需"+goodsDetails.getTotal()+"人次");
//        mRemainNum.setText(goodsDetails.getRemaining()+"");
//        mProgress.setMax(goodsDetails.getTotal());
//        mProgress.setProgress(goodsDetails.getTotal()-goodsDetails.getRemaining());

    }

    @Override
    public void setOnclick() {
        mBackBtn.setOnClickListener(this);
        mShoppingCartBtn.setOnClickListener(this);
        mIntoShopLayout.setOnClickListener(this);
        mIntoDetailsTxt.setOnClickListener(this);
        mAgoShowTxt.setOnClickListener(this);
        mShareTxt.setOnClickListener(this);
        mLootBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.small_room_goodsDetails_back:
                this.finish();
                break;
            case R.id.small_room_goodsDetails_shopping_cart:
                Bundle bundle1=new Bundle();
                bundle1.putString("flag","loot");
                toOtherActivity(HomeActivity.class, bundle1);
                this.finish();
                break;
            case R.id.small_detail_into_room:
                mApplication.setmSeclectFrag("sellerShop");
                toOtherActivity(HomeActivity.class);
                break;
            case R.id.activity_goods_info_forseller_goodsdetails:
                Bundle bu=new Bundle();
                bu.putString("url", "https://www.baidu.com/");
                toOtherActivity(MyWebView.class, bu);
                Toast.makeText(this,"WebView图文详情界面,加载URL,此处暂用https://www.baidu.com/",Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_goods_info_forseller_agoshow:
                toOtherActivity(AgoShowActivity.class);
                break;
            case R.id.activity_goods_info_forseller_share:
                Toast.makeText(this,"一键完成分享", Toast.LENGTH_SHORT).show();
                break;
            case R.id.small_activity_goods_info_loot_btn:
                addOrder();
                Bundle bundle=new Bundle();
                bundle.putString("flag", "loot");
                toOtherActivity(HomeActivity.class, bundle);
                this.finish();
                break;
        }

    }
    /**
     * 加入清单
     */
    public void addOrder(){
        boolean isHas=false;//标示数据库商品Id 数据库存在此id 返回true
        mAddShopList=mDBWrapper.selectData();
        if (mAddShopList.size()>0){
            for (GoodsDetails good:mAddShopList){
                if (good.getGoodsId()==goodsDetails.getGoodsId()){
                    isHas=true;
                }
            }
        }
        if (!isHas||mAddShopList.size()==0){
            mDBWrapper.insertData(goodsDetails);
        }
    }

    @Override
    protected void onResume() {
        if (mAddShopList!=null){
            addShoppingCartCount();
        }
        super.onResume();
    }

    /**
     * 添加商品到购物车后购物车右上角文字提醒
     */

    public void addShoppingCartCount(){
        mAddShopList=mDBWrapper.selectData();
        if (mAddShopList.size()>0&&mAddShopList!=null){
            mGoodCount.setVisibility(View.VISIBLE);
            mGoodCount.setText(mAddShopList.size()+"");
        }else {
            mGoodCount.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void initList() {

    }

    @Override
    public void setOnScorllPullListener() {
      mScrollView.onPullFinish();
    }

    @Override
    public void setOnScorllLoadListener() {

    }

}
