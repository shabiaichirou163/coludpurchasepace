package com.cloudpurchase.cloudpurchase;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cloudpurchase.adpater.OrderInfoAdpater;
import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.entity.ShoppingCartInfo;
import com.cloudpurchase.net.HttpRequest;
import com.cloudpurchase.utils.Constants;
import com.cloudpurchase.utils.JsonReslove;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.utils.RequestResultIn;
import com.cloudpurchase.veiw.MyListView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 订单确认界面
 */

public class OrderCheckActivity extends BaseActivity implements View.OnClickListener {
    private HashMap<String, String> mOrderMap;
    private ImageButton mBackBtn;//返回按钮
    private ImageView mDisplayOreder;//点击显示订单 隐藏订单按钮
    private Button mPostOrderBtn;
    private LinearLayout mDisplayOrederLyaout, mOrderInfoLyaout;//点击显示订单 隐藏订单布局
    private static final int OPEN_STATE = 0;
    private static final int CLOSE_STATE = 1;
    private int mCurrentState = CLOSE_STATE;
    private int mFlag;
    private JsonReslove mReslove;
    private List<GoodsDetails> mCartGoodsData;
    private ShoppingCartInfo mInfo;//加载后购物车信息
    private TextView mGoodsNum;
    private TextView mTotalPrice, mTotalPrice1;
    private MyListView mGoodsInfoLst;
    private OrderInfoAdpater mAdpater;
    @Override
    public void initView() {
        setContentView(R.layout.activity_order_check);
        mReslove = new JsonReslove(this);
        mBackBtn = (ImageButton) findViewById(R.id.activity_oreder_check_back_btn);
        mDisplayOreder = (ImageView) findViewById(R.id.order_display_order_img);
        mOrderInfoLyaout = (LinearLayout) findViewById(R.id.order_info_layout);
        mDisplayOrederLyaout = (LinearLayout) findViewById(R.id.order_display_order);
        mPostOrderBtn = (Button) findViewById(R.id.order_check_post_btn);
        mGoodsNum = (TextView) findViewById(R.id.order_total_goods_num);
        mTotalPrice = (TextView) findViewById(R.id.order_check_total_price_top);
        mTotalPrice1 = (TextView) findViewById(R.id.order_check_total_price);
        mGoodsInfoLst = (MyListView) findViewById(R.id.order_goods_info_lst);
        downLoadShoppingCartData();
    }


    /**
     * 网络下载购物车信息
     *
     * @param
     */

    public void downLoadShoppingCartData() {
        String url = Constants.GOOD_SHOPPING_CART_LIST + MyApplication.USER_ID + "?" + "token" + "=" + MyApplication.USER_TOKEN;
        HttpRequest.getHttpRequest().requestGET(url, null, new RequestResultIn() {
            @Override
            public void requstSuccful(String result) {
                LogUtils.e(result + "购物车列表");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    mCartGoodsData = mReslove.resloverShoppingCart(jsonObject);
                    mInfo = mReslove.resloveShoppingCartInfo(jsonObject);
                    setData();
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
     * 获取的参数进行设置
     */
    public void setData() {
        mGoodsNum.setText("共" + mCartGoodsData.size() + "件商品");
        mTotalPrice.setText(mInfo.getTotalPrice() + ".00元");
        mTotalPrice1.setText("¥" + mInfo.getTotalPrice() + ".00元");
        mAdpater=new OrderInfoAdpater(this,mCartGoodsData);
        mGoodsInfoLst.setAdapter(mAdpater);
    }

    @Override
    public void setOnclick() {
        mBackBtn.setOnClickListener(this);
        mDisplayOrederLyaout.setOnClickListener(this);
        mPostOrderBtn.setOnClickListener(this);
    }

    @Override
    public void initList() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_oreder_check_back_btn:
                this.finish();
                break;
            case R.id.order_check_post_btn:
                createNewOrder();
                break;
            case R.id.order_display_order:
                if (mFlag % 2 == 0) {
                    mCurrentState = OPEN_STATE;
                } else {
                    mCurrentState = CLOSE_STATE;
                }
                ++mFlag;
                orederInfo();
                break;
        }
    }

    /**
     * 显示关闭订单信息
     */
    public void orederInfo() {
        switch (mCurrentState) {
            case OPEN_STATE:
                mDisplayOreder.setImageResource(R.mipmap.invisble);
                mOrderInfoLyaout.setVisibility(View.VISIBLE);
                break;
            case CLOSE_STATE:
                mDisplayOreder.setImageResource(R.mipmap.gone);
                mOrderInfoLyaout.setVisibility(View.GONE);
                break;
        }
    }


    /**
     * 购物城生成订单
     */
    String mOrderurl;

    public void createNewOrder() {
        mOrderurl = Constants.CREAT_NEW_ORDER;
        for (int i = 0; i < mCartGoodsData.size(); i++) {
            mOrderMap = new HashMap<String, String>();
            GoodsDetails good = mCartGoodsData.get(i);
            mOrderMap.put("userId", MyApplication.USER_ID);
            mOrderMap.put("activityId", good.getActivityId());
            mOrderMap.put("payFee", (good.getPersonNum() * good.getJonitCost()) + "");
            mOrderMap.put("number", good.getPersonNum() + "");
            mOrderMap.put("token", MyApplication.USER_TOKEN);
            netRequestData(mOrderMap);
        }
    }

    /**
     * 解析数据
     *
     * @param map
     */
    public void netRequestData(HashMap<String, String> map) {
        HttpRequest.getHttpRequest().requestPOST(mOrderurl, null, map, new RequestResultIn() {
            @Override
            public void requstSuccful(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    mReslove.resultOrederInfo(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void requstError(VolleyError error) {

            }
        });

    }
}
