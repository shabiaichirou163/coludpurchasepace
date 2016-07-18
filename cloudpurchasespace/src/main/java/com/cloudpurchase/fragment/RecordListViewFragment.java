package com.cloudpurchase.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudpurchase.adpater.RecordAdaper;
import com.cloudpurchase.base.BaseFragment;
import com.cloudpurchase.cloudpurchase.GoodsDetailsActivity;
import com.cloudpurchase.cloudpurchase.HomeActivity;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.RecordUnderwayEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 全部云购记录—— 进行中的listView
 */
public class RecordListViewFragment extends BaseFragment implements RecordAdaper.AddBtn, AdapterView.OnItemClickListener {

    private List<RecordUnderwayEntity> list = new ArrayList<RecordUnderwayEntity>();
    private RecordAdaper adaper;
    private int mRecordSize = 10;
    private ListView mListView;
    private TextView mDefault;
    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_record_listview,null);
        mDefault = (TextView) f(view,R.id.record_default);
        mListView = (ListView) f(view,R.id.record_listView);
        mListView.setDivider(null);//隐藏下划线
        mListView.setVerticalScrollBarEnabled(true);
        return view;
    }

    @Override
    public void initList() {
        initRecordBean();
        if(list.size() == 0){
            //表示没有数据
            mDefault.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }else{
            //表示有数据
            mDefault.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            adaper = new RecordAdaper(getActivity(),list);
            adaper.setCall(this);
            mListView.setAdapter(adaper);
            adaper.setCall(this);
        }
    }
    private int mNum = 100;
    private int mPrgNum = 20;
    private void initRecordBean(){
        for (int i = list.size();i < mRecordSize; i++){
            RecordUnderwayEntity bean = new RecordUnderwayEntity();
            bean.setmImg("https://gss0.baidu.com/80M_cCml_Ao" +
                    "JksuboYuT_XF5eBk7hKzk-cq/bos_1463161267.6_603261_24685.jpg@w_225");
            bean.setmGoodsName("vivo X6S Plus 4G智能手机vivoX6Splus X6S4+64G 全网通 指纹识别 智能闪充");
            bean.setmNum(mNum+"");
            bean.setmPrompt("追加一人次，提升1分中奖几率");
            bean.setmPrgNum(mPrgNum+"");
            list.add(bean);
        }
    }

    @Override
    public void setOnclick() {
        mListView.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent mIntent = new Intent(getActivity(),GoodsDetailsActivity.class);
        startActivity(mIntent);
    }

    @Override
    public void Add() {
        Intent mIntent = new Intent(getActivity(), HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("flag","loot");
        mIntent.putExtras(bundle);
        getActivity().startActivity(mIntent);
    }
}
