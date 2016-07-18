package com.cloudpurchase.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.cloudpurchase.adpater.NewShownAdpater;
import com.cloudpurchase.adpater.ShowedAdpater;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.utils.JsonReslove;
import com.cloudpurchase.veiw.LoadMoreScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by oscar on 2016/7/12.
 * 已经揭晓
 */
public class SoonShowFragment extends Fragment {
    private  View mSoonShowFragment;
    private GridView mSoonShowGrd;
    private JsonReslove mJsonReslove;
    private List<GoodsDetails> mGoodsData;//获取数据
    private NewShownAdpater mSoonShowAdpater;//已经揭晓时瞥
    private LoadMoreScrollView mLazyScrollView;//底部加载更多
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSoonShowFragment=inflater.inflate(R.layout.fragment_new_shown_item2,null);
        mSoonShowGrd= (GridView) mSoonShowFragment.findViewById(R.id.soon_shown_grd);
        mJsonReslove=new JsonReslove(getParentFragment().getActivity());
        downLoadShowedData("","new");
        return mSoonShowFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mLazyScrollView = (LoadMoreScrollView) getParentFragment().getView().findViewById(R.id.new_shown_lazyScrollview);
        super.onActivityCreated(savedInstanceState);
    }


    /**
     * 请求数据
     * @param Url
     * @param mMode
     *
     */
    public void downLoadShowedData(String Url,String mMode){
        String str="{}";
        JSONObject b= null;
        try {
            b = new JSONObject(str);
            mGoodsData=mJsonReslove.resloveGoods1(b);
            switch (mMode){
                case "new":
                    mSoonShowAdpater=new NewShownAdpater(getActivity(),mGoodsData);
                    mSoonShowGrd.setAdapter(mSoonShowAdpater);
                    break;
                case "footer":
                    mSoonShowAdpater.footerRefreshAddGoods(mGoodsData);
                    mLazyScrollView.downLoadFinish();
                    break;
                case "header":
                    mSoonShowAdpater.headRefreshGoods(mGoodsData);
                    mLazyScrollView.onPullFinish();
                    break;

            }
            if (mGoodsData!=null&&mGoodsData.size()<10){
                mLazyScrollView.displayAll();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * fragment销毁时停止倒计时
     */
    @Override
    public void onDestroy() {
        if (mSoonShowAdpater!=null){
            mSoonShowAdpater.cancelAllTimers();
        }
        super.onDestroy();
    }
}
