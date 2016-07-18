package com.cloudpurchase.cloudpurchase;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.cloudpurchase.adpater.LuckyAdapter;
import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.entity.LuckyEntity;
import com.cloudpurchase.net.HttpRequest;
import com.cloudpurchase.utils.Constants;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.utils.RequestResultIn;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户中心——》幸运记录界面
 *
 */
public class LuckyActivity extends BaseActivity implements View.OnClickListener, LuckyAdapter.OnclickCall, AdapterView.OnItemClickListener {

    private ImageView mBack;
    private ListView mListView;
    private List<LuckyEntity> list = new ArrayList<LuckyEntity>();
    private LuckyAdapter adapter;

    @Override
    public void initView() {
        setContentView(R.layout.activity_lucky);
        mBack = (ImageView) f(R.id.lucky_back);
        mListView = (ListView) f(R.id.lucky_listView);
        initBean();
        adapter = new LuckyAdapter(this,list);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);//隐藏下划线
        adapter.setCall(this);
    }

    @Override
    public void setOnclick() {
        mBack.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void initList() {

    }

    private void initBean(){
        for(int i = 0 ; i < 1 ; i++){
            LuckyEntity bean = new LuckyEntity();
            bean.img ="http://i4.mercrt.fd.zol-img.com.cn/g5/M00/02/0B" +
                    "/ChMkJ1d2BwWId8PdAAHFiPqkJi8AATIHQMPvt4AAcWg294.jpg";
            bean.title = "Acer E5-572G全高清游戏本 i5标压 2G独显";
            bean.timeNum = "2016-7-1 18:31:02";
            bean.userNum = "5112";
            bean.luckyNum = "12536532423";
            bean.joinNum = "2135";
            bean.time = "2016年7月1日18:31:19";
            bean.state = "已签收";
            list.add(bean);
        }
        String url = Constants.LUCKY +MyApplication.USER_ID+"/1"+"?token="+MyApplication.USER_TOKEN;
        HttpRequest.getHttpRequest().requestGET(url, null, new RequestResultIn() {
            @Override
            public void requstSuccful(String result) {
                LogUtils.e(result);
            }

            @Override
            public void requstError(VolleyError error) {
                LogUtils.e(String.valueOf(error));
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.lucky_back:
                finish();
                break;
        }
    }

    @Override
    public void call() {
        toOtherActivity(TakeGoodsActivity.class);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent mIntent = new Intent(this,GoodsDetailsActivity.class);
        startActivity(mIntent);
    }
}
