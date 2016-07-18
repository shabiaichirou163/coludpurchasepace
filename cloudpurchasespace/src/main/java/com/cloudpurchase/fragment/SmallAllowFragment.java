package com.cloudpurchase.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cloudpurchase.adpater.SmallAllowAdapter;
import com.cloudpurchase.base.BaseFragment;
import com.cloudpurchase.cloudpurchase.GoodsDetailsActivity;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.SmallAllowEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户中心——>小房间——》正在进行的Fragment
 *
 */
public class SmallAllowFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private SmallAllowAdapter adapter;
    private List<SmallAllowEntity> list = new ArrayList<SmallAllowEntity>();

    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.list_view,null);
        mListView = (ListView) f(view,R.id.listView);
        return view;
    }

    @Override
    public void initList() {
        initBean();
        adapter = new SmallAllowAdapter(getActivity(),list);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);//隐藏下划线
    }

    private void initBean(){
        for(int i = 0 ; i < 3 ; i ++){
            SmallAllowEntity bean = new SmallAllowEntity();
            bean.img = "http://img12.360buyimg.com/n2/jfs/t23" +
                  "[=plk7yft6wq   7/305" +
                    "/2900649018/39411/b92be95d/571ee3a0N0e76900e.jpg";
            bean.id = 0;
            bean.title = "OPPO R9plus 4GB+64GB内存版 玫瑰金 全网通4G手机 双卡双待";
            bean.allNum = "514";
            bean.money = "3299.00";
            bean.userName = "就是我";
            bean.progress = 25+i+5;
            bean.surplusNum = "134";
            bean.olderNum ="323";
            list.add(bean);
        }
    }


    @Override
    public void setOnclick() {
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent mIntent = new Intent(getActivity(),GoodsDetailsActivity.class);
        startActivity(mIntent);
    }
}
