package com.cloudpurchase.fragment;

import android.renderscript.AllocationAdapter;
import android.view.View;
import android.widget.ListView;

import com.cloudpurchase.adpater.AllRecordAdapter;
import com.cloudpurchase.base.BaseFragment;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.AllRecordEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 全部云购记录界面
 *
 */
public class AllRecordListViewFragment extends BaseFragment{

    private List<AllRecordEntity> list = new ArrayList<AllRecordEntity>();
    private ListView mListView;
    private AllRecordAdapter adapter;

    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_record_listview,null);
        mListView = (ListView) f(view,R.id.record_listView);
        return view;
    }

    @Override
    public void initList() {
        initBean();
        adapter = new AllRecordAdapter(getActivity(),list);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);//隐藏下划线
    }

    private void initBean(){
        for(int i = 0 ; i < 10 ; i++){
            AllRecordEntity bean = new AllRecordEntity();
            bean.mImg = "http://img11.360buyimg.com/n1/s180x180_j" +
                    "fs/t2647/73/1904154386/258908/b9160d58/574e65c5Ndb334524.jpg";
            bean.mTitle = "vivo X6 移动联通双4G手机 4GB+32GB 双卡双待 金色";
            bean.mTimeNum = "2016060602821";
            bean.mNum ="2222";
            bean.mUserName="没错，就是我";
            list.add(bean);
        }
    }

    @Override
    public void setOnclick() {

    }
}
