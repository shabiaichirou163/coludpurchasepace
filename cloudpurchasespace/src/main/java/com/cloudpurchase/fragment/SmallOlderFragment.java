package com.cloudpurchase.fragment;

import android.view.View;
import android.widget.ListView;

import com.cloudpurchase.adpater.SmallAllowAdapter;
import com.cloudpurchase.adpater.SmallOlderAdapter;
import com.cloudpurchase.base.BaseFragment;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.RecordOverEntity;
import com.cloudpurchase.entity.SmallOlderEntity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 用户中心——>小房间——》已经揭晓的Fragment
 */
public class SmallOlderFragment extends BaseFragment{

    private ListView mListView;
    private SmallOlderAdapter adapter;
    private List<SmallOlderEntity> list = new ArrayList<SmallOlderEntity>();

    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.list_view,null);
        mListView = (ListView) f(view,R.id.listView);
        return view;
    }

    @Override
    public void initList() {
        initBean();
        adapter = new SmallOlderAdapter(getActivity(),list);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);//隐藏下划线
    }

    private void initBean(){
        for (int i = 0 ; i < 5 ;i++){
            SmallOlderEntity bean = new SmallOlderEntity();
            bean.mImg = "http://img12.360buyimg.com/n2/jfs/t2" +
                    "038/233/2889865271/93808/d742d970/56fa4c04N6fb66f12.jpg";
            bean.mGoodsName = "欧奇（OUKI） OKP6 移动4G 联通 智能手机 双卡双待 黑色";
            bean.mNum = "312312";
            bean.mUserName = "苦逼小码农";
            bean.mUserNum = "424人次";
            bean.mLuckyNum = "41234213";
            bean.mTime = "2016年6月23日14:14:44";
            list.add(bean);
        }
    }

    @Override
    public void setOnclick() {

    }
}
