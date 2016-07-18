package com.cloudpurchase.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudpurchase.adpater.NewShownAdpater;
import com.cloudpurchase.adpater.ShowedAdpater;
import com.cloudpurchase.base.BaseFragment;
import com.cloudpurchase.cloudpurchase.GoodsDetailsActivity;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.cloudpurchase.SearchActivity;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.utils.JsonReslove;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.veiw.LoadMoreScrollView;
import com.cloudpurchase.veiw.MyGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by oscar on 2016/6/6.
 * 最新揭晓界面
 */
public class NewShownFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener,LoadMoreScrollView.OnScorllPullAndLoadListener{
    private View mNewShownView;
    private RadioGroup mRadio;
    private RadioButton mNewShown,mShowned;
    private String mUrl="";
    private String mMode="new";//new 为点击切换  footer 为上拉加载  header 为下拉刷新
    private ImageButton mSarchBtn;
    private String mFlag="showed";//newShow 代表即将揭晓    showed 代表已经揭晓
    private LoadMoreScrollView mLazyScrollView;//底部加载更多
    private LinearLayout mHeaderLayout,mFootViewLayout;
    private FragmentManager mManager;
    private FragmentTransaction mFT;
    private ShowedFragment mShowedFrag;
    private SoonShowFragment  mSoonShowFrag;
    @Override
    public View initView() {
        mManager = getChildFragmentManager();
        mNewShownView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_new_shown_area, null);
        mNewShownView.setFocusable(true);
        mNewShownView.setFocusableInTouchMode(true);
        mNewShownView.requestFocus();

        mLazyScrollView= (LoadMoreScrollView) mNewShownView.findViewById(R.id.new_shown_lazyScrollview);
        mHeaderLayout= (LinearLayout) mNewShownView.findViewById(R.id.head_view_layout);
        mFootViewLayout= (LinearLayout) mNewShownView.findViewById(R.id.foot_view_layout);
        mLazyScrollView.initView(mHeaderLayout, mFootViewLayout);
        mLazyScrollView.setOnScorllPullAndLoadListener(this);


        mRadio= (RadioGroup) mNewShownView.findViewById(R.id.new_shown_rg);
        mNewShown= (RadioButton) mNewShownView.findViewById(R.id.new_shown_rb);
        mShowned= (RadioButton) mNewShownView.findViewById(R.id.showned_rb);

        mSarchBtn= (ImageButton) mNewShownView.findViewById(R.id.new_shown_search);
        mFT=mManager.beginTransaction();
        mShowedFrag=new ShowedFragment();
        mFT.add(R.id.newShow_fragmentLayout, mShowedFrag);
        mFT.commit();
        return mNewShownView;
    }

    @Override
    public void initList() {

    }

    @Override
    public void setOnclick() {
        mRadio.setOnCheckedChangeListener(this);
        mSarchBtn.setOnClickListener(this);

    }

    /**
     * 设置radioButton相关
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mMode="new";
        mFT=mManager.beginTransaction();
        switch (checkedId){
            case R.id.new_shown_rb:
                mNewShown.setTextColor(getResources().getColor(R.color.text_color_white));
                mShowned.setTextColor(getResources().getColor(R.color.text_color_block));
                mNewShown.setBackgroundColor(getResources().getColor(R.color.backgroud_color_red));
                mShowned.setBackgroundColor(getResources().getColor(R.color.backgroud_color_white));
                mFlag="newShow";
                mSoonShowFrag=new SoonShowFragment();
                mFT.replace(R.id.newShow_fragmentLayout, mSoonShowFrag);
                break;
            case R.id.showned_rb:
                mNewShown.setTextColor(getResources().getColor(R.color.text_color_block));
                mShowned.setTextColor(getResources().getColor(R.color.text_color_white));
                mNewShown.setBackgroundColor(getResources().getColor(R.color.backgroud_color_white));
                mShowned.setBackgroundColor(getResources().getColor(R.color.backgroud_color_red));
                mFlag="showed";
                mShowedFrag=new ShowedFragment();
                mFT.replace(R.id.newShow_fragmentLayout, mShowedFrag);
                break;
        }
        mFT.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_shown_search:
                toOtherActivity(SearchActivity.class);
                break;
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void setOnScorllPullListener() {
        mMode="header";
        switch (mFlag){
            case "newShow":
                mSoonShowFrag.downLoadShowedData("",mMode);
                break;
            case "showed":
                mShowedFrag.downLoadShowedData("", mMode);
                break;
        }

    }
    /**
     * 上滑加载更多
     */
    @Override
    public void setOnScorllLoadListener() {
        mMode="footer";
        switch (mFlag){
            case "newShow":
                mSoonShowFrag.downLoadShowedData("",mMode);
                break;
            case "showed":
                mShowedFrag.downLoadShowedData("",mMode);
                break;
        }
    }

}
