package com.cloudpurchase.fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.cloudpurchase.adpater.ForSellAreaItemAdpater;
import com.cloudpurchase.adpater.ForSellerTypeAdpater;
import com.cloudpurchase.base.BaseFragment;
import com.cloudpurchase.cloudpurchase.HomeActivity;
import com.cloudpurchase.cloudpurchase.MyApplication;
import com.cloudpurchase.cloudpurchase.MyWebView;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.cloudpurchase.SearchActivity;
import com.cloudpurchase.entity.ADEntity;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.utils.Constants;
import com.cloudpurchase.utils.JsonReslove;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.utils.ViewPagerUtils;
import com.cloudpurchase.veiw.LoadMoreScrollView;
import com.cloudpurchase.veiw.VerticalScrollTextView;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * OneYuanAreaFragment 一元专区
 *
 * 显示首页上的Fragment
 *
 */
public class OneYuanAreaFragment extends BaseFragment implements View.OnClickListener,
       LoadMoreScrollView.OnScorllPullAndLoadListener{
    private ViewPager mImageDisplay;//显示图片
    private int mIcon[]={R.mipmap.a,R.mipmap.viewpager,R.mipmap.viewpager1,R.mipmap.viewpager2,R.mipmap.viewpager3};
    private ViewPagerUtils mUtils;
    private RadioGroup mSallRoomGroup;
    private LinearLayout mButtonLayout;
    private int mButtonId []={R.id.new_rbn,R.id.hot_rbn,R.id.progress_rbn,R.id.total_rbn};//Button 的id
    private Button mBtn [];//最新 最热 进度  总需

    private View view;//Fragment加载布局
    private ImageButton mBack,mSearch;//返回按钮
    private TextView mOneTxt;
    private String mTag;//网络请求表示,可以在Activity销毁时取消请求
    private JsonReslove mJsonReslove;//解析网络请求数据
    private List<GoodsDetails> mGoodsData;//数据封装
    private VerticalScrollTextView mVerticalScrollText;
    private RadioButton  mNewest,mRecommend;//小房间tab
    private String mUrl="";//网络请求地址
    private String mMode="new";//new 为点击切换  footer 为上拉加载  header 为下拉刷新
    private ImageView mSellerImg;//卖家区顶部显示图片
    private String goodsType[]={"平板电脑","家用电器","电脑设备","女性时尚","珠宝首饰","虚拟专区","租房售房"
    ,"美食天地","影音数码","健身器材","汽车用品","其它商品"};
    private ListView mGoodsTypeLst,mGoodsInfoLst;//卖家区的商品类型和商品详细信息ListView
    private ForSellAreaItemAdpater mSellAreaGoosInfoAdapater;
    private LinearLayout mOneYuanFragLayout,mInnerLayout,mPointLayout,mLineLayout,mVerticalSTxtLayout;//总线性布局  scrollView内layout 轮播点layout  红line的layout
    private RelativeLayout mTitilLayout,mSellerShopLayout;//顶部试图
    private FrameLayout mViewPagerLayout;
    private LoadMoreScrollView mScrollView;//底部加载scrollview
    private TextView mOneKeyShare,mCareNum;//卖家商铺分享按钮  关注数
    private ImageButton mAddImgBtn;//添加关注按钮
    private MyApplication mApplication;//全局application

    private String mSort="des";//des 代表降序  asc代表升序
    private String mType="newFrag";//"newFrag"代表最新  "hotFrag"代表最热 "progressFrag"代表进度 "ascFrag" 代表升序 "desFrag"  代表降序
    private ItemNewFragment mNewFrag;//最新Fragment
    private ItemHotFragment mHotFrag;//最热Fragment
    private ItemProgressFragment mProgress;//进度Fragment
    private ItemSoftAscFragment mSoftAscFrag;//升序Fragment
    private ItemSoftDesFragment mSoftDesFrag;//降序Fragment
    private FragmentManager mFm;
    private FragmentTransaction mFt;
    private SmallRoomItemNewFragment mItemNewFragment;//小房间最新商品
    private SmallRoomItemRecommedFragment mItemRecommedFragment;//最新推荐
    @Override
    public View initView() {
        mApplication = (MyApplication) getActivity().getApplicationContext();
        mJsonReslove=new JsonReslove(getActivity());
        view = View.inflate(getActivity(), R.layout.fragment_oneyuan_area,null);
        //GridView会超出整个父屏幕,为了防止直接显示到底部,加入如下三行克服此问题在上面空间加入焦点
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        mFm=getChildFragmentManager();
        //自定义下拉刷新上拉加载初始化
        mScrollView= (LoadMoreScrollView) view.findViewById(R.id.one_frag_scrollView);
        LinearLayout headerLayout= (LinearLayout) view.findViewById(R.id.one_frag_head_view_layout);
        LinearLayout footLayout= (LinearLayout) view.findViewById(R.id.one_frag_foot_view_layout);
        //传递头布局和底部布局 以便添加头部和底部
        mScrollView.initView(headerLayout, footLayout);
        mScrollView.setOnScorllPullAndLoadListener(this);

        mOneYuanFragLayout= (LinearLayout) view.findViewById(R.id.one_frag_Layout);
        mInnerLayout= (LinearLayout) view.findViewById(R.id.one_frag_scroll_inner_layout);

        //顶部试图信息
        mTitilLayout= (RelativeLayout) view.findViewById(R.id.oneArear_titil);
        mViewPagerLayout= (FrameLayout) view.findViewById(R.id.one_frag_frame_layout);
        mVerticalSTxtLayout= (LinearLayout) view.findViewById(R.id.verticalScrollTextView_layout);
        mOneTxt= (TextView) view.findViewById(R.id.one_yuan_text);
        mBack= (ImageButton) view.findViewById(R.id.one_yuan_back);
        mSearch= (ImageButton) view.findViewById(R.id.one_yuan_search);


        //一元十元百元区信息初始化
        mButtonLayout= (LinearLayout) view.findViewById(R.id.one_yuan_rg);
        mLineLayout= (LinearLayout) view.findViewById(R.id.one_frag_line);
        //ViewPager相关
        mPointLayout= (LinearLayout) view.findViewById(R.id.one_fragment_ponit_layout);
        mImageDisplay= (ViewPager) view.findViewById(R.id.imageDisplay);
        mUtils=new ViewPagerUtils(getActivity(),mImageDisplay,mIcon,mPointLayout,Integer.MAX_VALUE, null,null);
        mUtils.event();

        //显示对话框
        this.creatProgressDialog();
        switch (mApplication.getmSeclectFrag()){
            case "smallRoom":
                loadSmallRoomFrag();
                break;
            case "sellArea":
                addSellerLayout();
                GoneALl();
                mTitilLayout.setVisibility(View.VISIBLE);
                mOneTxt.setText(getActivity().getResources().getString(R.string.seller_area));
                getSellerAreaGoodsInfo("","");
                break;
            case "sellerShop":
                mSellerShopLayout= (RelativeLayout) view.findViewById(R.id.serller_shop_layout);
                addSellerShopLayout();
                loadOtherFrag();//初始加载卖家区信息
                break;
            default:
                loadOtherFrag();//初始进入分别加载不同价钱最新的信息
                break;
        }

        //垂直轮播textView
        initVerticalScrollText();
        return view;
    }



    /**
     *加载不同fragment
     */
    public void loadOtherFrag(){
        GoneALl();
        mFt=mFm.beginTransaction();
        switch (mApplication.getmSeclectFrag()) {
            case "oneYuanArea":
                visibleAllGrd();
                mOneTxt.setText(getActivity().getResources().getString(R.string.one_yuan_area));
                mUrl= Constants.ONE_AREA_NEW;
                break;
            case "tenYuanArea":
                visibleAllGrd();
                mOneTxt.setText(getActivity().getResources().getString(R.string.ten_yuan_area));
                mUrl=Constants.TEN_AREA_NEW;
                break;
            case "oneHundredYuanArea":
                visibleAllGrd();
                mOneTxt.setText(getActivity().getResources().getString(R.string.onehundred_yuan_area));
                mUrl=Constants.ONEHUND_AREA_NEW;
                break;
            case "sellerShop":
                visibleSellerShopInfo();
                break;
        }

        mNewFrag=new ItemNewFragment();
        mFt.add(R.id.one_fragmentLayout, mNewFrag);
        mFt.commit();
    }

    /**
     * 加载小房间不同fragment
     */
    public void loadSmallRoomFrag(){
        addSmallLayout();
        GoneALl();
        visibleAllLst();
        mOneTxt.setText(getActivity().getResources().getString(R.string.small_room_area));
        mFt=mFm.beginTransaction();
        mItemNewFragment=new SmallRoomItemNewFragment();
        mFt.add(R.id.small_room_fraglayout,mItemNewFragment);
        mFt.commit();
    }


    @Override
    public void initList() {
        mBack.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        mBtn=new Button[mButtonId.length];
        for (int i=0;i<mButtonId.length;i++){
            mBtn[i]= (Button) view.findViewById(mButtonId[i]);
            mBtn[i].setOnClickListener(new BtnOnclickOnClickListener());
        }
    }
    @Override
    public void setOnclick() {
        if (mShopBack!=null){
            mShopBack.setOnClickListener(this);
        }
        if (mSallRoomGroup!=null) {
            mSallRoomGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    mPage=1;
                    OneYuanAreaFragment.this.creatProgressDialog();//显示对话框
                    mFt=mFm.beginTransaction();
                    switch (checkedId) {
                        case R.id.small_room_newst_goods:
                            mType = "newst";
                            mRecommend.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            mRecommend.setTextColor(getResources().getColor(R.color.text_color_block));
                            mNewest.setBackgroundColor(getResources().getColor(R.color.backgroud_color_red));
                            mNewest.setTextColor(getResources().getColor(R.color.text_color_white));
                            mItemNewFragment=new SmallRoomItemNewFragment();
                            mFt.replace(R.id.small_room_fraglayout, mItemNewFragment);
                            break;
                        case R.id.small_room_recommend_room:
                            mType = "recommend";
                            mNewest.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            mNewest.setTextColor(getResources().getColor(R.color.text_color_block));
                            mRecommend.setBackgroundColor(getResources().getColor(R.color.backgroud_color_red));
                            mRecommend.setTextColor(getResources().getColor(R.color.text_color_white));
                            mItemRecommedFragment=new SmallRoomItemRecommedFragment();
                            mFt.replace(R.id.small_room_fraglayout, mItemRecommedFragment);
                            break;
                    }
                    mFt.commit();
                }
            });
        }
        if (mOneKeyShare!=null){
            mOneKeyShare.setOnClickListener(this);
        }
        if (mAddImgBtn!=null){
            mAddImgBtn.setOnClickListener(this);
        }
    }

    /**
     * 设置垂直轮播的textview
     */
    public void initVerticalScrollText(){
        List<ADEntity> list=new ArrayList<ADEntity>();
        for (int i=0;i<100;i++){
            ADEntity adEntity=new ADEntity();
            adEntity.setmFront("最新公告:");
            adEntity.setmBack("小米手机 4S 高S全网通版64GB 颜色随时全网通版64GB,颜色随 时全网 通版64GB,颜色随时");
            adEntity.setmUrl("提供Url进行跳转比如:https://www.baidu.com/");
            list.add(adEntity);
        }
        switch (mApplication.getmSeclectFrag()){
            case "sellArea":
                mVerticalScrollText= (VerticalScrollTextView) view.findViewById(R.id.verticalScrollTextView1);
                break;
            default:
                mVerticalScrollText= (VerticalScrollTextView) view.findViewById(R.id.verticalScrollTextView);
                break;
        }
        mVerticalScrollText.setmTexts(list);
        mVerticalScrollText.setBackColor(Color.parseColor("#6a6a6a"));
        mVerticalScrollText.setFrontColor(Color.RED);
        mVerticalScrollText.setmDuration(5);
        mVerticalScrollText.setmInterval(3000);
        mVerticalScrollText.setOnClickLitener(new VerticalScrollTextView.onClickLitener() {
            @Override
            public void onClick(String mUrl) {
                Bundle bu=new Bundle();
                bu.putString("url", "https://www.baidu.com/");
                toOtherActivity(MyWebView.class, bu);
                Toast.makeText(getActivity(),"WebView图文详情界面,加载URL,此处暂用https://www.baidu.com/",Toast.LENGTH_SHORT).show();
            }
        });

    }
    /*
     *设置Button点击事件 ------最新 最热  进度  总需
     */
    class  BtnOnclickOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            for (int i=0;i<mButtonId.length;i++){
                if (mButtonId[i]==v.getId()){
                    mBtn[i].setBackgroundColor(getActivity().getResources().getColor(R.color.backgroud_color_red));
                    mBtn[i].setTextColor(getActivity().getResources().getColor(R.color.backgroud_color_white));
                }else {
                    mBtn[i].setBackgroundColor(getActivity().getResources().getColor(R.color.backgroud_color_white));
                    mBtn[i].setTextColor(getActivity().getResources().getColor(R.color.text_color_block));
                }
            }
            OneYuanAreaFragment.this.creatProgressDialog();//显示对话框
            mPage=1;
            mScrollView.LoadNew();
            mFt=mFm.beginTransaction();
            switch (v.getId()){
                case R.id.new_rbn:
                    mSort="des";
                    mType="newFrag";
                    loadNewData();
                    mNewFrag=new ItemNewFragment();
                    mFt.replace(R.id.one_fragmentLayout, mNewFrag);
                    break;
                case R.id.hot_rbn:
                    mSort="des";
                    mType="hotFrag";
                    loadHotData();
                    mHotFrag=new ItemHotFragment();
                    mFt.replace(R.id.one_fragmentLayout, mHotFrag);
                    break;
                case R.id.progress_rbn:
                    mSort="des";
                    mType="progressFrag";
                    loadProgressData();
                    mProgress=new ItemProgressFragment();
                    mFt.replace(R.id.one_fragmentLayout, mProgress);
                    break;
                case R.id.total_rbn:
                    switch (mSort){
                        case "des"://降序排列 默认
                            mType="desFrag";
                            mSort="asc";
                            loadSofDesData();
                            mSoftDesFrag=new ItemSoftDesFragment();
                            mFt.replace(R.id.one_fragmentLayout, mSoftDesFrag);
                            break;
                        case "asc"://升序排列
                            mType="ascFrag";
                            mSort="des";
                            loadSofAscData();
                            mSoftAscFrag=new ItemSoftAscFragment();
                            mFt.replace(R.id.one_fragmentLayout, mSoftAscFrag);
                            break;
                    }
                    break;
            }
            mFt.commit();

        }
    }

    /*
    *返回和查询按钮点击事件
    */
    private int careNum=180;//暂定
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.one_yuan_back://返回
                getActivity().finish();
                break;
            case R.id.one_yuan_search://查询
                toOtherActivity(SearchActivity.class);
                break;
            case R.id.seller_shop_back://查询
                mApplication.setmSeclectFrag("smallRoom");
                getActivity().finish();
                break;
            case R.id.seller_one_key_share://一键分享
                Toast.makeText(getActivity(),"分享按钮",Toast.LENGTH_SHORT).show();
                break;
            case R.id.seller_shop_add_care://添加关注
                mCareNum.setText((careNum++) + "");
                break;
        }
    }

    /*
     *改变不同布局的url
    */
    public void loadNewData(){
        switch (mApplication.getmSeclectFrag()){
            case "oneYuanArea":
                mUrl=Constants.ONE_AREA_NEW;
                break;
            case "tenYuanArea":
                mUrl=Constants.TEN_AREA_NEW;
                break;
            case "oneHundredYuanArea":
                mUrl=Constants.ONEHUND_AREA_NEW;
                break;
            case "sellerShop":
                break;
        }
    }
    /*
      *改变不同布局的url
     */
    public void loadHotData(){
        switch (mApplication.getmSeclectFrag()){
            case "oneYuanArea":
                mUrl=Constants.ONE_AREA_HOT;
                break;
            case "tenYuanArea":
                mUrl=Constants.TEN_AREA_HOT;
                break;
            case "oneHundredYuanArea":
                mUrl=Constants.ONEHUND_AREA_HOT;
                break;
            case "sellerShop":
                break;
        }
    }
         /*
         *改变不同布局的url
        */
    public void loadProgressData(){
        switch (mApplication.getmSeclectFrag()){
            case "oneYuanArea":
                mUrl=Constants.ONE_AREA_PROGRESS;
                break;
            case "tenYuanArea":
                mUrl=Constants.TEN_AREA_PROGRESS;
                break;
            case "oneHundredYuanArea":
                mUrl=Constants.ONEHUND_AREA_PROGRESS;
                break;
            case "sellerShop":

                break;
        }
    }

    /*
    *总需升序排列 改变不同布局的url
    */
    public void loadSofAscData(){
        switch (mApplication.getmSeclectFrag()){
            case "oneYuanArea":
                mUrl=Constants.ONE_AREA_ASC_SOFT;
                break;
            case "tenYuanArea":
                mUrl=Constants.TEN_AREA_ASC_SOFT;
                break;
            case "oneHundredYuanArea":
                mUrl=Constants.ONEHUND_AREA_ASC_SOFT;
                break;
            case "sellerShop":

                break;
        }
    }

    /*
    *总需降序序排列 改变不同布局的url
    */
    public void loadSofDesData(){
        switch (mApplication.getmSeclectFrag()){
            case "oneYuanArea":
                mUrl=Constants.ONE_AREA_DES_SOFT;
                break;
            case "tenYuanArea":
                mUrl=Constants.TEN_AREA_DES_SOFT;
                break;
            case "oneHundredYuanArea":
                mUrl=Constants.ONEHUND_AREA_DES_SOFT;
                break;
            case "sellerShop":
                break;
        }
    }
    /*
     * 添加卖家布局
     */
    public void addSellerLayout(){
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_seller_item,null);
        mOneYuanFragLayout.addView(view);
        initSellerListViewInfo();
    }
    /*
     *显示GridView相关控件
     */
   public void visibleAllGrd(){
       mScrollView.setVisibility(View.VISIBLE);
       mLineLayout.setVisibility(View.VISIBLE);
       mTitilLayout.setVisibility(View.VISIBLE);
       mViewPagerLayout.setVisibility(View.VISIBLE);
       mVerticalSTxtLayout.setVisibility(View.VISIBLE);
       mButtonLayout.setVisibility(View.VISIBLE);

   }
    /*
     *进入小房间显示相关信息
     */
    public void visibleAllLst(){
        mTitilLayout.setVisibility(View.VISIBLE);
        mScrollView.setVisibility(View.VISIBLE);
        mViewPagerLayout.setVisibility(View.VISIBLE);
        mVerticalSTxtLayout.setVisibility(View.VISIBLE);
    }
    /**
     * 添加小房间信息
     */
    public void  addSmallLayout(){
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_smallroom_item,null);
        mInnerLayout.addView(view);

        //小房间信息初始化
        mSallRoomGroup= (RadioGroup) view.findViewById(R.id.small_room_rg);
        mNewest= (RadioButton) view.findViewById(R.id.small_room_newst_goods);
        mRecommend= (RadioButton) view.findViewById(R.id.small_room_recommend_room);
    }
    /**
     * 添加卖家商铺
     */

    public void addSellerShopLayout(){
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_sellershop,null);
        mSellerShopLayout.addView(view);
        mUserImg= (ImageView) view.findViewById(R.id.seller_shop_user_img);
        mShopBack= (ImageButton) view.findViewById(R.id.seller_shop_back);
        mOneKeyShare= (TextView) view.findViewById(R.id.seller_one_key_share);
        mCareNum= (TextView) view.findViewById(R.id.seller_shop_care_num);
        mAddImgBtn= (ImageButton) view.findViewById(R.id.seller_shop_add_care);
    }

    /**
     * 显示卖家商铺信息
     */
    private ImageView mUserImg;
    private ImageButton mShopBack;
    public void visibleSellerShopInfo(){
        mSellerShopLayout.setVisibility(View.VISIBLE);
        mButtonLayout.setVisibility(View.VISIBLE);
        mScrollView.setVisibility(View.VISIBLE);
        mLineLayout.setVisibility(View.VISIBLE);

    }

    /*
     获取商品详情请求
    */
    public void getSellerAreaGoodsInfo(String url,final String mode){
        jasonResolver("{}", mode);//测试用
        //mTag="HttpPost";
//        HttpRequest.getHttpRequest().requestGET(url, null, new RequestResultIn() {
//            @Override
//            public void requstSuccful(String result) {
//                jasonResolver(result, flag, mode);
//            }
//
//            @Override
//            public void requstError(VolleyError error) {
//                OneYuanAreaFragment.this.cancelDialog();
//                if (error!=null)
//                Toast.makeText(getActivity(),error.getClass().getSimpleName(),Toast.LENGTH_SHORT).show();
//                mScrollView.displayAll();
//            }
//        });


    }

    public void jasonResolver(String result,String mode){

        JSONObject object= null;
        try {
            object = new JSONObject(result);
            // mGoodsData=mJsonReslove.resloveGoods(object);//正常用
            mGoodsData=mJsonReslove.resloveGoods1(object);//测试用
            mSellAreaGoosInfoAdapater=new ForSellAreaItemAdpater(getActivity(),mGoodsData);
            mSellAreaGoosInfoAdapater.getInsertSuccessful((HomeActivity) getActivity());
            if (null!=mGoodsData&&mGoodsData.size()>0) {
                this.cancelDialog();
                mGoodsInfoLst.setAdapter(mSellAreaGoosInfoAdapater);
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }


    }
    /*
       初始化卖家区相关信息
    */
    public void initSellerListViewInfo(){
        mSellerImg= (ImageView) view.findViewById(R.id.serller_info_img);
        mSellerImg.setScaleType(ImageView.ScaleType.FIT_XY);
        mGoodsTypeLst= (ListView) view.findViewById(R.id.seller_goods_type_lst);
        displaySellerGoodsType();
        mGoodsInfoLst= (ListView) view.findViewById(R.id.seller_goods_infos_lst);
    }
    /*
       设置显示商品类型信息
    */
    private ForSellerTypeAdpater sellerGoodsTypeadpater;
    public void displaySellerGoodsType(){
        sellerGoodsTypeadpater=new ForSellerTypeAdpater(getActivity(),goodsType);
        mGoodsTypeLst.setAdapter(sellerGoodsTypeadpater);
        mGoodsTypeLst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txt = (TextView) view.findViewById(R.id.Seller_goods_type_item);
                boolean isSelect[] = sellerGoodsTypeadpater.getIsSelect();
                for (int i = 0; i < isSelect.length; i++) {
                    if (i == position) {
                        isSelect[i] = true;
                    } else {
                        isSelect[i] = false;
                    }
                    sellerGoodsTypeadpater.notifyDataSetChanged();
                }

            }


        });
    }

    /**
     * 下拉刷新
     */
    @Override
    public void setOnScorllPullListener() {
        headerRefreshGoods();

    }
    /**
     * 底部自动加载
     *
     */
    @Override
    public void setOnScorllLoadListener() {
        footerRefreshGoods();
    }

    /*
     *下拉增加更多数据
     */
    private int mPage=1;
    public void footerRefreshGoods(){
        ++mPage;
        String NewUrl=mUrl.substring(0,mUrl.length()-1);
        NewUrl=NewUrl+mPage;
        switch (mType){
            case "newFrag":
                mNewFrag.getNewestGoodsInfo(NewUrl, "footer");
                break;
            case "hotFrag":
                mHotFrag.getHotGoodsInfo(NewUrl, "footer");
                break;
            case "progressFrag":
                mProgress.getProgressGoodsInfo(NewUrl, "footer");
                break;
            case "desFrag":
                mSoftDesFrag.getSoftDesDataInfo(NewUrl, "footer");
                break;
            case "ascFrag":
                mSoftAscFrag.getSoftAscGoodsInfo(NewUrl, "footer");
                break;
            case "newst":
                mItemNewFragment.getSmallNewestGoodsInfo("", "footer");
                break;
            case "recommend":
                mItemRecommedFragment.getRecommedestGoodsInfo("", "footer");
                break;
        }
    }

    /*
     上拉刷新数据
    */
    public void headerRefreshGoods(){
        mPage = 1;
        switch (mType){
            case "newFrag":
                mNewFrag.getNewestGoodsInfo(mUrl,"header");
                break;
            case "hotFrag":
                mHotFrag.getHotGoodsInfo(mUrl,"header");
                break;
            case "progressFrag":
                mProgress.getProgressGoodsInfo(mUrl, "header");
                break;
            case "desFrag":
                mSoftDesFrag.getSoftDesDataInfo(mUrl, "header");
                break;
            case "ascFrag":
                mSoftAscFrag.getSoftAscGoodsInfo(mUrl, "header");
                break;
            case "newst":
                mItemNewFragment.getSmallNewestGoodsInfo("", "footer");
                break;
            case "recommend":
                mItemRecommedFragment.getRecommedestGoodsInfo("", "footer");
                break;
        }

    }

    /*
      隐藏需要隐藏控件
     */
    public void GoneALl(){
        if (null!=mScrollView){
            mScrollView.setVisibility(View.GONE);
        }
        if (null!=mLineLayout){
            mLineLayout.setVisibility(View.GONE);
        }
        if (null!=mTitilLayout){
            mTitilLayout.setVisibility(View.GONE);
        }
        if (null!=mViewPagerLayout){
            mViewPagerLayout.setVisibility(View.GONE);
        }
        if (null!=mVerticalSTxtLayout){
            mVerticalSTxtLayout.setVisibility(View.GONE);
        }
        if (null!=mButtonLayout){
            mButtonLayout.setVisibility(View.GONE);
        }
        if (null!=mSellerShopLayout){
            mSellerShopLayout.setVisibility(View.GONE);
        }
    }

    /*
     *Fragment可见时实现图片自动轮播
     * Fragment可见时实现购物车提醒
    */
    @Override
    public void onResume() {
        if(mUtils!=null){
            mUtils.viewPagerAutoScroll(true);
        }
        super.onResume();
    }
    /*
    *fragment停止时停止轮播
    */
    @Override
    public void onStop() {
        if(mUtils!=null){
            mUtils.viewPagerAutoScroll(false);
        }
        super.onStop();
    }



}
