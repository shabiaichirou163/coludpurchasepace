package com.cloudpurchase.cloudpurchase;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.cloudpurchase.adpater.NoticeAdapter;
import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.entity.NoticeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知界面
 *
 * 该界面为ListView，点击 item后跳转到webView
 *
 */
public class NoticeActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView mBack;
    private ListView mListView;
    private List<NoticeEntity> list = new ArrayList<NoticeEntity>();
    private NoticeAdapter adapter;


    @Override
    public void initView() {
        setContentView(R.layout.activity_notice);
        mBack = (ImageView) f(R.id.notice_back);
        mListView = (ListView) f(R.id.notice_listView);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);//隐藏下划线
    }

    @Override
    public void setOnclick() {
        mBack.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void initList() {
        NoticeEntity bean = new NoticeEntity();
        bean.mTitle = "云购空间升级上线,一元夺宝，更多惊喜等你";
        bean.mTime = "2016年6月14日14:51:50";
        bean.url = "https://www.baidu.com/";
        list.add(bean);
        adapter = new NoticeAdapter(this,list);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.notice_back){
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent mIntent = new Intent(this,MyWebView.class);
        mIntent.putExtra("url",list.get(position).url);
        startActivity(mIntent);
    }
}
