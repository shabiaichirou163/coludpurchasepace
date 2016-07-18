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
import com.cloudpurchase.adpater.ForSmallRoomAdpater;
import com.cloudpurchase.adpater.ForSmallRoomNewstAdpater_item;
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
import com.cloudpurchase.veiw.MyListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by oscar on 2016/7/12.
 *小房间最新商品
 */
public class SmallRoomItemNewFragment extends Fragment {
    private  View mNewFragmentView;
    private MyListView mNewLst;
    private JsonReslove mJsonReslove;
    private List<GoodsDetails> mGoodsData;//获取数据
    private LoadMoreScrollView mScrollView;//底部加载更多
    private ForSmallRoomNewstAdpater_item smallRoomAdpaterItem;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNewFragmentView=inflater.inflate(R.layout.fragment_small_room_item_new,null);
        mNewLst= (MyListView) mNewFragmentView.findViewById(R.id.smallRoom_goods_new);
        mJsonReslove=new JsonReslove(getParentFragment().getActivity());
        mScrollView = (LoadMoreScrollView) ((OneYuanAreaFragment)getParentFragment()).getView().findViewById(R.id.one_frag_scrollView);
        getSmallNewestGoodsInfo("", "new");
        return mNewFragmentView;
    }

    /*
     *获取商品详情请求
     */
    public void getSmallNewestGoodsInfo(String url,final String mode) {
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
                                smallRoomAdpaterItem=new ForSmallRoomNewstAdpater_item(getParentFragment().getActivity(),mGoodsData);
                                 mNewLst.setAdapter(smallRoomAdpaterItem);
                            }
                            break;
                        case "footer":
                            if (null!=mGoodsData&&mGoodsData.size()>0){
                                smallRoomAdpaterItem.footerRefreshAddGoods(mGoodsData);
                                mScrollView.downLoadFinish();
                            }
                            break;
                        case "header":
                            if (null!=mGoodsData&&mGoodsData.size()>0) {
                                smallRoomAdpaterItem.headRefreshGoods(mGoodsData);
                                mScrollView.onPullFinish();
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
