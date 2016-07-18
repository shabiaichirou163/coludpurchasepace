package com.cloudpurchase.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cloudpurchase.adpater.ForShoppingCartAdpater;
import com.cloudpurchase.base.BaseFragment;
import com.cloudpurchase.cloudpurchase.GoodsDetailsActivity;
import com.cloudpurchase.cloudpurchase.HomeActivity;
import com.cloudpurchase.cloudpurchase.LoginActivity;
import com.cloudpurchase.cloudpurchase.MyApplication;
import com.cloudpurchase.cloudpurchase.OrderCheckActivity;
import com.cloudpurchase.cloudpurchase.PayActivity;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.db.DBWrapper;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.entity.ShoppingCartInfo;
import com.cloudpurchase.net.HttpRequest;
import com.cloudpurchase.utils.Constants;
import com.cloudpurchase.utils.JsonReslove;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.utils.PersonNumChangeListener;
import com.cloudpurchase.utils.RequestResultIn;
import com.cloudpurchase.utils.ShoppingCartDeleteListener;
import com.cloudpurchase.utils.ShoppingClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by oscar on 2016/6/14.
 * 购物车列表界面
 */
public class ShoppingCartFragment extends BaseFragment implements View.OnClickListener,ShoppingCartDeleteListener,ShoppingClickListener,
        PersonNumChangeListener{
    private View mShoppingCart;
    private Button mBtn,mGoToLogin;
    private FragmentManager mFm;
    private List<GoodsDetails> mCartGoodsData;
    private LinearLayout mLayout;
    private ListView mShoppingInfoLst;
    private Button mGoPayBtn;//支付按钮
    private TextView mShoppingNum,mGoodsPirce;//购物车产品的数量 以及价格
    private ForShoppingCartAdpater mAdpater;
    private JsonReslove mReslove;//解析购物页面信息
    private ShoppingCartInfo mInfo;//加载后购物车信息
    private boolean mIsFisrtData;//时候首次加载
    private String mFlag;
    private TextView mTxtMark;//购物车回调文字提醒
    @Override
    public View initView() {
        mShoppingCart= LayoutInflater.from(getActivity()).inflate(R.layout.fragment_shopping_cart, null);
        mReslove=new JsonReslove(getActivity());
        mFm=getActivity().getSupportFragmentManager();
        mLayout= (LinearLayout) mShoppingCart.findViewById(R.id.shopping_cart_layout);
        if (MyApplication.USER_IS_LOGIN_FLAG) {
            this.creatProgressDialog();
            downLoadShoppingCartData();
        }else {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_shopping_cart_nologin, null);
            mLayout.addView(view);
            mGoToLogin= (Button) mShoppingCart.findViewById(R.id.frag_shopping_cart_nologin_btn);
            mGoToLogin.setOnClickListener(this);
        }
        return mShoppingCart;
    }

    @Override
    public void initList() {
    }

    @Override
    public void setOnclick() {

    }

    /**
     * 网络下载购物车信息
     * @param
     */

    public void downLoadShoppingCartData(){
        String url= Constants.GOOD_SHOPPING_CART_LIST+MyApplication.USER_ID+"?"+"token"+"="+ MyApplication.USER_TOKEN;
        HttpRequest.getHttpRequest().requestGET(url, null, new RequestResultIn() {
            @Override
            public void requstSuccful(String result) {
                LogUtils.e(result + "购物车列表");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    mCartGoodsData = mReslove.resloverShoppingCart(jsonObject);
                    mInfo = mReslove.resloveShoppingCartInfo(jsonObject);
                    shoppingCartSetdata();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requstError(VolleyError error) {

            }
        });
    }
    public void shoppingCartSetdata(){
        //根据购物车数量加载不同的布局
        if (mCartGoodsData.size()>0&&mCartGoodsData!=null){
            if (!mIsFisrtData) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_shopping_cart_shopping, null);
                mLayout.addView(view);
                mShoppingNum = (TextView) mShoppingCart.findViewById(R.id.shopping_cart_goods_num);
                mGoodsPirce = (TextView) mShoppingCart.findViewById(R.id.shopping_cart_goods_price);
                mGoPayBtn = (Button) mShoppingCart.findViewById(R.id.shopping_cart_goods_pay_btn);
                mGoPayBtn.setOnClickListener(this);
                mShoppingInfoLst = (ListView) mShoppingCart.findViewById(R.id.shopping_cart_goods_lst);
                mShoppingNum.setText(mCartGoodsData.size() + "");
                mGoodsPirce.setText(mInfo.getTotalPrice() + "");
                mAdpater = new ForShoppingCartAdpater(getActivity(), mCartGoodsData);
                mAdpater.getDeleteFinish(this);
                mAdpater.getPersonNumChangeListener(this);
                mAdpater.getShoppingClickListener(this);
                mShoppingInfoLst.setAdapter(mAdpater);
                this.cancelDialog();
                mIsFisrtData=true;
            }else {
                switch (mFlag){
                    case "delete":
                        mShoppingNum.setText(mCartGoodsData.size() + "");
                        mGoodsPirce.setText(mInfo.getTotalPrice() + "");
                        mAdpater.refreshData(mCartGoodsData);
                        addShoppingCartCount();
                        break;
                    case "change":
                        mGoodsPirce.setText(mInfo.getTotalPrice() + "");
                        break;
                }

            }
        }else {
            if (!mIsFisrtData) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_shopping_cart_noshopping, null);
                mLayout.addView(view);
                this.cancelDialog();
                mBtn = (Button) mShoppingCart.findViewById(R.id.frag_shopping_cart_btn);
                mBtn.setOnClickListener(this);
                mIsFisrtData=true;
            }else {
                RadioButton btn0= (RadioButton) ((HomeActivity) getActivity()).findViewById(R.id.buttom_homePage);
                RadioButton btn2= (RadioButton) ((HomeActivity) getActivity()).findViewById(R.id.buttom_shoppingCart);
                btn0.setChecked(true);
                btn2.setChecked(true);
                addShoppingCartCount();
            }

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mTxtMark= (TextView) getActivity().findViewById(R.id.shopping_count);
        super.onActivityCreated(savedInstanceState);
    }



    /**
     * 添加商品到购物车后购物车右上角文字提醒
     */
    public void addShoppingCartCount(){
        if (mCartGoodsData!=null&&mCartGoodsData.size()>0){
            mTxtMark.setVisibility(View.VISIBLE);
            mTxtMark.setText(mCartGoodsData.size()+"");
        }else {
            mTxtMark.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.frag_shopping_cart_btn:
                displayHomePage();
                break;
            case R.id.shopping_cart_goods_pay_btn:
                toOtherActivityResult(OrderCheckActivity.class);
                break;
            case R.id.frag_shopping_cart_nologin_btn:
                toOtherActivityResult(LoginActivity.class);
                break;
        }
    }

    /**
     * 删除后回调
     */
    @Override
    public void deleteFinish() {
        mFlag="delete";
        downLoadShoppingCartData();
    }
    /**
     * 购物车跳转
     * @param goodsDetails
     */
    @Override
    public void ShoppingClickListener(GoodsDetails goodsDetails) {
        Bundle bundle=new Bundle();
        bundle.putString("flag", "onefragment");
        bundle.putString("activityId",goodsDetails.getActivityId());
        toOtherActivity(GoodsDetailsActivity.class, bundle);
    }

    /**
     * 更新金币价格
     */
    @Override
    public void setPersonNumChangeListener() {
        mFlag="change";
       downLoadShoppingCartData();
    }



    /**
     * 支付成功后重新加载购物车页
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1&&resultCode==1000){
            displayHomePage();
            RadioButton button= (RadioButton) getActivity().findViewById(R.id.buttom_shoppingCart);
            button.setChecked(true);
        }
    }



    /**
     * 隐藏当前fragment 显示首页
     */
    public void displayHomePage(){
        //隐藏当前frgment
        FragmentTransaction transaction=mFm.beginTransaction();
        transaction.hide(this);
        transaction.commit();
        //显示OneYuanAreaFragment 并设置导航home button为checked
        RadioButton button= (RadioButton) getActivity().findViewById(R.id.buttom_homePage);
        button.setChecked(true);
        button.setTextColor(getResources().getColor(R.color.text_color_red));
    }
}
