package com.cloudpurchase.cloudpurchase;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cloudpurchase.adpater.NewShownAdpater;
import com.cloudpurchase.adpater.ShowListAdpater;
import com.cloudpurchase.adpater.ShowedAdpater;
import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.utils.JsonReslove;
import com.cloudpurchase.veiw.LoadMoreListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 的还大呢分享
 */
public class ShowOrderShareActivity extends BaseActivity implements  LoadMoreListView.OnScorllPullAndLoadListener,View.OnClickListener, AdapterView.OnItemClickListener {
    private List<GoodsDetails> mGoodsData;
    private JsonReslove mJsonReslove;
    private String mUrl="";
    private String mMode="new";//new 为点击切换  footer 为上拉加载  header 为下拉刷新
    private ImageButton mBackBtn,mSarchBtn;
    private LoadMoreListView mShowOrder;
    private ShowListAdpater mAdpater;
    @Override
    public void initView() {
        setContentView(R.layout.activity_show_order_share);
        mJsonReslove=new JsonReslove(this);
        mBackBtn= (ImageButton) findViewById(R.id.show_order_back);
        mSarchBtn= (ImageButton) findViewById(R.id.show_order_search);
        mShowOrder= (LoadMoreListView) this.findViewById(R.id.show_order_lst);
        mShowOrder.setOnScorllPullAndLoadListener(this);
        downLoadData(mUrl, mMode);


    }

    @Override
    public void setOnclick() {
        if(mBackBtn!=null)
        mBackBtn.setOnClickListener(this);
        if (mSarchBtn!=null)
        mSarchBtn.setOnClickListener(this);
        mShowOrder.setOnItemClickListener(this);
    }

    @Override
    public void initList() {

    }


    public void downLoadData(String Url,String mode){
        String str="{}";
        JSONObject b= null;
        try {
            b = new JSONObject(str);
            mGoodsData=mJsonReslove.resloveGoods1(b);
            switch (mode){
                case "new":
                    mAdpater=new ShowListAdpater(this,mGoodsData);
                    mShowOrder.setAdapter(mAdpater);
                    break;
                case "footer":
                    mAdpater.footerRefreshAddGoods(mGoodsData);
                    break;
                case "header":
                    mAdpater.headRefreshGoods(mGoodsData);
                    break;

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.show_order_back:
                this.finish();
                break;
            case R.id.show_order_search:
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
        downLoadData(mUrl, mMode);
        mShowOrder.onPullFinish();
    }
    /**
     * 上拉加载
     */
    @Override
    public void setOnScorllLoadListener() {
        mMode="footer";
        downLoadData(mUrl, mMode);
        mShowOrder.downLoadFinish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent mIntent = new Intent(this,GoodsDetailsActivity.class);
        startActivity(mIntent);
    }
}
