package com.cloudpurchase.fragment;

import android.view.View;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.cloudpurchase.adpater.RecordOverAdapter;
import com.cloudpurchase.base.BaseFragment;
import com.cloudpurchase.cloudpurchase.MyApplication;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.RecordOverEntity;
import com.cloudpurchase.net.HttpRequest;
import com.cloudpurchase.utils.Constants;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.utils.RequestResultIn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 全部云购记录——》已经揭晓的Listview
 *
 */
public class RecordOverListViewFragment extends BaseFragment {

    private List<RecordOverEntity> mOverList =  new ArrayList<RecordOverEntity>();
    private int mOverSize = 10;
    private RecordOverAdapter adaper;
    private ListView mListView;
    private int mPagenum = 1;//每次请求的页数，每次请求后+1

    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_record_listview,null);
        mListView = (ListView) f(view,R.id.record_listView);

        return view;
    }

    @Override
    public void initList() {
        initOverBean();
        //TODO
        String url = Constants.RECORD+MyApplication.USER_ID+"/"+mPagenum+"?token="+MyApplication.USER_TOKEN;
        HttpRequest.getHttpRequest().requestGET(url, null, new RequestResultIn() {
            @Override
            public void requstSuccful(String result) {
                LogUtils.e(result);
            }

            @Override
            public void requstError(VolleyError error) {
                LogUtils.e(String.valueOf(error));
            }
        });
        adaper = new RecordOverAdapter(getActivity(),mOverList);
        mListView.setAdapter(adaper);
        mListView.setDivider(null);//隐藏下划线
    }

    private void initOverBean(){
        for (int i = mOverList.size() ; i < mOverSize ;i++){
            RecordOverEntity bean = new RecordOverEntity();
            bean.mImg = "http://img12.360buyimg.com/n7/jfs/t2" +
                    "779/143/488687134/300651/fbb4e608/5715e107N3f4413f8.jpg";
            bean.mGoodsName = "三星 Galaxy S7 edge（G9350）32G版 铂光金移动联通电信4G手机 双卡双待 骁龙820手机";
            bean.mNum = "312312";
            bean.mUserName = "苦逼小码农";
            bean.mUserNum = "424人次";
            bean.mLuckyNum = "41234213";
            bean.mTime = "2016年6月9日15:55:49";
            mOverList.add(bean);
        }
    }

    @Override
    public void setOnclick() {

    }
}
