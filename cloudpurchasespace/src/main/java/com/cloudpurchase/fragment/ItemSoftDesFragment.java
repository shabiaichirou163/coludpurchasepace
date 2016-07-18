package com.cloudpurchase.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.cloudpurchase.adpater.GoodsDetailsAdpater;
import com.cloudpurchase.adpater.SellerShopGoodsAdpater;
import com.cloudpurchase.cloudpurchase.HomeActivity;
import com.cloudpurchase.cloudpurchase.MyApplication;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.net.HttpRequest;
import com.cloudpurchase.utils.Constants;
import com.cloudpurchase.utils.JsonReslove;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.utils.RequestResultIn;
import com.cloudpurchase.veiw.LoadMoreScrollView;
import com.cloudpurchase.veiw.MyGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by oscar on 2016/7/12.
 * 降序
 */
public class ItemSoftDesFragment extends Fragment {
    private  View mDesFragmentView;
    private MyGridView mDesGrd;
    private JsonReslove mJsonReslove;
    private List<GoodsDetails> mGoodsData;//获取数据
    private GoodsDetailsAdpater mAdpater;//已经揭晓时瞥
    private LoadMoreScrollView mScrollView;//底部加载更多
    private MyApplication mApplication;
    private SellerShopGoodsAdpater mSellerShopAdpater;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDesFragmentView=inflater.inflate(R.layout.fragment_one_yuan_item_soft_des,null);
        mDesGrd= (MyGridView) mDesFragmentView.findViewById(R.id.one_frag_item_soft_des);
        mJsonReslove=new JsonReslove(getParentFragment().getActivity());
        mScrollView = (LoadMoreScrollView) ((OneYuanAreaFragment)getParentFragment()).getView().findViewById(R.id.one_frag_scrollView);
        mApplication = (MyApplication) getParentFragment().getActivity().getApplicationContext();
        switch (mApplication.getmSeclectFrag()){
            case "oneYuanArea":
                getSoftDesDataInfo(Constants.ONEHUND_AREA_DES_SOFT,"new");
                break;
            case "tenYuanArea":
                getSoftDesDataInfo(Constants.TEN_AREA_DES_SOFT, "new");
                break;
            case "oneHundredYuanArea":
                getSoftDesDataInfo(Constants.ONEHUND_AREA_DES_SOFT, "new");
                break;
            case "sellerShop":
                break;

        }
        return mDesFragmentView;
    }

    /*
     *获取商品详情请求
     */
    public void getSoftDesDataInfo(String url,final String mode) {
//        jasonResolver("{}",mode);
        //mTag="HttpPost";
        HttpRequest.getHttpRequest().requestGET(url, null, new RequestResultIn() {
            @Override
            public void requstSuccful(String result) {
                jasonResolver(result, mode);

            }

            @Override
            public void requstError(VolleyError error) {
                ((OneYuanAreaFragment)getParentFragment()).cancelDialog();
                if (error != null)
                    Toast.makeText(getParentFragment().getActivity(), error.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
                mScrollView.displayAll();
            }
        });
    }


    public void jasonResolver(String result,String mode){
        try {
            JSONObject object=new JSONObject(result);
            mGoodsData=mJsonReslove.resloveGoods(object);//正常用
//            mGoodsData= mJsonReslove.resloveGoods1(object);//正常用
            switch (mApplication.getmSeclectFrag()){
                case "sellerShop":
                    switch (mode){
                        case "new":
                            mSellerShopAdpater=new SellerShopGoodsAdpater(getParentFragment().getActivity(),mGoodsData);
                            if (null!=mGoodsData&&mGoodsData.size()>0) {
                                ((OneYuanAreaFragment)getParentFragment()).cancelDialog();
                                mDesGrd.setAdapter(mSellerShopAdpater);
                            }
                            break;
                        case "footer":
                            if (null!=mGoodsData&&mGoodsData.size()>0) {
                                mSellerShopAdpater.footerRefreshAddGoods(mGoodsData);
                                mScrollView.downLoadFinish();
                            }
                            break;
                        case "header":
                            if (null!=mGoodsData&&mGoodsData.size()>0) {
                                mSellerShopAdpater.headRefreshGoods(mGoodsData);
                                mScrollView.onPullFinish();
                            }
                            break;
                    }
                    break;
                default:
                    switch (mode){
                        case "new":
                            mAdpater=new GoodsDetailsAdpater(getParentFragment().getActivity(),mGoodsData);
                            mAdpater.getInsertSuccessful((HomeActivity) getParentFragment().getActivity());
                            if (null!=mGoodsData&&mGoodsData.size()>0) {
                                ((OneYuanAreaFragment)getParentFragment()).cancelDialog();
                                mDesGrd.setAdapter(mAdpater);
                            }
                            break;
                        case "footer":
                            if (null!=mGoodsData&&mGoodsData.size()>0) {
                                mAdpater.footerRefreshAddGoods(mGoodsData);
                                mScrollView.downLoadFinish();
                            }
                            break;
                        case "header":
                            if (null!=mGoodsData&&mGoodsData.size()>0) {
                                mAdpater.headRefreshGoods(mGoodsData);
                                mScrollView.onPullFinish();
                            }
                            break;
                    }
                    break;
            }
            if (mGoodsData!=null&&mGoodsData.size()<10){
                mScrollView.displayAll();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    }
