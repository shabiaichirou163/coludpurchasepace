package com.cloudpurchase.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudpurchase.adpater.ForSmallRoomAdpater;
import com.cloudpurchase.adpater.ForSmallRoomNewstAdpater_item;
import com.cloudpurchase.adpater.GoodsDetailsAdpater;
import com.cloudpurchase.adpater.SellerShopGoodsAdpater;
import com.cloudpurchase.cloudpurchase.MyApplication;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.utils.JsonReslove;
import com.cloudpurchase.veiw.LoadMoreScrollView;
import com.cloudpurchase.veiw.MyListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by oscar on 2016/7/12.
 *小房间推荐
 */
public class SmallRoomItemRecommedFragment extends Fragment {
    private  View mRecommedFragmentView;
    private MyListView mRecommedLst;
    private JsonReslove mJsonReslove;
    private List<GoodsDetails> mGoodsData;//获取数据
    private LoadMoreScrollView mScrollView;//底部加载更多
    private ForSmallRoomAdpater mSmallRoomAdpater;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRecommedFragmentView=inflater.inflate(R.layout.fragment_small_room_item_recommend,null);
        mRecommedLst= (MyListView) mRecommedFragmentView.findViewById(R.id.smallRoom_goods_recommend);
        mJsonReslove=new JsonReslove(getParentFragment().getActivity());
        getRecommedestGoodsInfo("", "new");
        return mRecommedFragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mScrollView = (LoadMoreScrollView) ((OneYuanAreaFragment)getParentFragment()).getView().findViewById(R.id.one_frag_scrollView);
        super.onActivityCreated(savedInstanceState);
    }
    /*
     *获取商品详情请求
     */
    public void getRecommedestGoodsInfo(String url,final String mode) {
        jasonResolver("{}",mode);
        //mTag="HttpPost";
//        HttpRequest.getHttpRequest().requestGET(url, null, new RequestResultIn() {
//            @Override
//            public void requstSuccful(String result) {
//
//                jasonResolver(result, mode);
//
//            }
//
//            @Override
//            public void requstError(VolleyError error) {
//                ((OneYuanAreaFragment)getParentFragment()).cancelDialog();
//                LogUtils.e(error.getClass()+"####################"+error);
//                if (error != null)
//                    Toast.makeText(getParentFragment().getActivity(), error.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
//                mScrollView.displayAll();
//            }
//        });
    }


    public void jasonResolver(String result,String mode){
        try {
            JSONObject object=new JSONObject(result);
           // mGoodsData= mJsonReslove.resloveGoods(object);//正常用
            mGoodsData= mJsonReslove.resloveGoods1(object);//正常用
            switch (mode){
                        case "new":
                            if(null!=mGoodsData&&mGoodsData.size()>0) {
                                ((OneYuanAreaFragment)getParentFragment()).cancelDialog();
                                mSmallRoomAdpater=new ForSmallRoomAdpater(getParentFragment().getActivity(),mGoodsData);
                                mRecommedLst.setAdapter(mSmallRoomAdpater);
                            }
                            break;
                        case "footer":
                            if (null!=mGoodsData&&mGoodsData.size()>0){
                                mSmallRoomAdpater.footerRefreshAddGoods(mGoodsData);
                            }
                            break;
                        case "header":
                            if (null!=mGoodsData&&mGoodsData.size()>0) {
                                mSmallRoomAdpater.headRefreshGoods(mGoodsData);
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
