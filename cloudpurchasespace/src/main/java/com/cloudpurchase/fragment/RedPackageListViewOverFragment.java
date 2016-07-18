package com.cloudpurchase.fragment;

import android.view.View;
import android.widget.ListView;

import com.cloudpurchase.adpater.RedPackageAdapter;
import com.cloudpurchase.base.BaseFragment;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.RedPackageEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 已经过期的红包 Fragment
 */
public class RedPackageListViewOverFragment extends BaseFragment{

    private ListView mListView;
    private List<RedPackageEntity> list = new ArrayList<RedPackageEntity>();
    private RedPackageAdapter adapter;

    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.activity_red_package_listview,null);
        mListView = (ListView) f(view,R.id.redPackage_listView);
        mListView.setDivider(null);//隐藏下划线
        return view;
    }

    @Override
    public void initList() {
        initBean();
        adapter = new RedPackageAdapter(getActivity(),list,1);
        mListView.setAdapter(adapter);
    }

    private void initBean(){
        for(int i = 0 ; i < 3 ; i ++){
            RedPackageEntity bean = new RedPackageEntity();
            bean.mMoney = "200";
            bean.mTremMoney = "1000";
            bean.mTile = "新用户红包";
            bean.mBornTime = "2016年6月15日12:03:54";
            bean.mValidityPeriod = "2016年6月15日12:03:56";
            bean.mNotes = "单次支付满十金币可用（适用于夺宝所有商品）";
            list.add(bean);
        }
    }

    @Override
    public void setOnclick() {

    }
}
