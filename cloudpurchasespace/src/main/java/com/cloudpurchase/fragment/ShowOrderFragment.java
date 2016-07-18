package com.cloudpurchase.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.cloudpurchase.adpater.ShowListAdpater;
import com.cloudpurchase.base.BaseFragment;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.cloudpurchase.SearchActivity;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.utils.JsonReslove;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.veiw.LoadMoreListView;
import com.cloudpurchase.veiw.LoadMoreScrollView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
/**
 * Created by oscar on 2016/6/14.
 * 晒单Fragment
 */
public class ShowOrderFragment extends BaseFragment implements LoadMoreListView.OnScorllPullAndLoadListener,View.OnClickListener{
    private View mShowFrag;
    private List<GoodsDetails> mGoodsData;
    private JsonReslove mJsonReslove;
    private String mUrl="";
    private String mMode="new";//new 为点击切换  footer 为上拉加载  header 为下拉刷新
    private ImageButton mSarchBtn;
    private LoadMoreListView mShowOrder;
    private ShowListAdpater mAdpater;
    @Override
    public View initView() {
        mShowFrag=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_show_order,null);
        mJsonReslove=new JsonReslove(getActivity());
        //mBackBtn= (ImageButton)mShowFrag.findViewById(R.id.frag_show_order_back);
        mSarchBtn= (ImageButton)mShowFrag.findViewById(R.id.frag_show_order_search);
        mShowOrder= (LoadMoreListView) mShowFrag.findViewById(R.id.frag_show_order_lst);
        mShowOrder.setOnScorllPullAndLoadListener(this);
        downLoadData(mUrl, mMode);
        return mShowFrag;
    }

    @Override
    public void initList() {

    }

    @Override
    public void setOnclick() {
        //mBackBtn.setOnClickListener(this);
        if (mSarchBtn!=null)
        mSarchBtn.setOnClickListener(this);

    }
    public void downLoadData(String Url,String mode){
        String str="{}";
        JSONObject b= null;
        try {
            b = new JSONObject(str);
            mGoodsData=mJsonReslove.resloveGoods1(b);
            switch (mode){
                case "new":
                    mAdpater=new ShowListAdpater(getActivity(),mGoodsData);
                    mShowOrder.setAdapter(mAdpater);
                    break;
                case "footer":
                    mAdpater.footerRefreshAddGoods(mGoodsData);
                    break;
                case "header":
                    mAdpater.headRefreshGoods(mGoodsData);
                    break;
            }
            mShowOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getActivity(), "position" + (position-1), Toast.LENGTH_SHORT).show();

                    //parent.getAdapter().getItem(position) 点击的item
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.frag_show_order_back:
//                getActivity().finish();
//                break;
            case R.id.frag_show_order_search:
                toOtherActivity(SearchActivity.class);
                break;
        }
    }

    /**
     * 上拉刷新
     */
    @Override
    public void setOnScorllPullListener() {
        mMode = "header";
        downLoadData(mUrl, mMode);
        mShowOrder.onPullFinish();
    }
    /**
     * 下拉加载
     */

    @Override
    public void setOnScorllLoadListener() {
        mMode="footer";
        downLoadData(mUrl, mMode);
        mShowOrder.downLoadFinish();

    }

}
