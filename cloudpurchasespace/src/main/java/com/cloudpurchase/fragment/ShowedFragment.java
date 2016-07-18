package com.cloudpurchase.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.cloudpurchase.adpater.NewShownAdpater;
import com.cloudpurchase.adpater.ShowedAdpater;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.utils.JsonReslove;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.veiw.LoadMoreScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by oscar on 2016/7/12.
 * 已经揭晓
 */
public class ShowedFragment extends Fragment {
    private  View mShowedFragment;
    private GridView mShowedGrd;
    private JsonReslove mJsonReslove;
    private List<GoodsDetails> mGoodsData;//获取数据
    private ShowedAdpater mShowedAdpater;//已经揭晓时瞥
    private LoadMoreScrollView mLazyScrollView;//底部加载更多
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mShowedFragment=inflater.inflate(R.layout.fragment_new_shown_item1,null);
        mShowedGrd= (GridView) mShowedFragment.findViewById(R.id.showned_grd);
        mJsonReslove=new JsonReslove(getParentFragment().getActivity());
        mLazyScrollView = (LoadMoreScrollView) getParentFragment().getView().findViewById(R.id.new_shown_lazyScrollview);
        downLoadShowedData("","new");
        return mShowedFragment;
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
                    mShowedAdpater=new ShowedAdpater(getActivity(),mGoodsData);
                    mShowedGrd.setAdapter(mShowedAdpater);
                    break;
                case "footer":
                    mShowedAdpater.footerRefreshAddGoods(mGoodsData);
                    mLazyScrollView.downLoadFinish();
                    break;
                case "header":
                    mShowedAdpater.headRefreshGoods(mGoodsData);
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
}
