package com.cloudpurchase.cloudpurchase;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.cloudpurchase.adpater.AgoShowAdapter;
import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.entity.AgoShowEntity;
import com.cloudpurchase.veiw.Pull_To_LoadListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 往期揭晓界面
 *
 */
public class AgoShowActivity extends BaseActivity implements
        View.OnClickListener, AdapterView.OnItemClickListener, Pull_To_LoadListView.PullToLoad, AbsListView.OnScrollListener {

    private ImageView mBack;//返回按钮
    private ImageView mSearch;//搜索按钮
    private Pull_To_LoadListView mListView;
    private AgoShowAdapter adapter;//适配器
    private List<AgoShowEntity> list = new ArrayList<AgoShowEntity>();//数据源
    private int size = 5;//每次加载数量

    @Override
    public void initView() {
        setContentView(R.layout.activity_ago_show);
        mBack = (ImageView) f(R.id.agoShow_back);
        mSearch = (ImageView) f(R.id.agoShow_search);
        mListView = (Pull_To_LoadListView) f(R.id.agoShow_listView);
        mListView.setAdapter(adapter);
        mListView.setPullToLoad(this);
        mListView.setDivider(null);//隐藏下划线
    }

    @Override
    public void setOnclick() {
        mBack.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
    }

    @Override
    public void initList() {
        initBean();
        adapter = new AgoShowAdapter(this,list);
    }

    private void initBean(){
        for (int i = list.size() ; i <size ; i++){
            AgoShowEntity bean = new AgoShowEntity();
            bean.mUserImgUrl = "https://ss0.baidu.com/9rkZbzqaKgQUo" +
                    "hGko9WTAnF6hhy/mms-res/fFhO6IiiBRAMBUXW9U-QZUC" +
                    "3ZIhMuQRShIkw9UAYhIliBWlChI8LhpRenFvkrUCIuWCaZU8enG" +
                    "v3XURbBWsaBIswuUjR.jpg";
            bean.mNumber = "874278923";
            bean.mTime = "2016年6月8日15:14:47";
            bean.mName = "话多多";
            bean.mIP = "192.168.1.255";
            bean.mID = "284263"+i;
            bean.mJoinNum ="49273";
            bean.mLuckyNum ="100029";
            list.add(bean);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.agoShow_back:
                finish();
                break;
            case R.id.agoShow_search:

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this,list.get(position).mID,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Pull() {
        size +=10;
        initBean();
        adapter.notifyDataSetChanged();
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    handler.sendEmptyMessage(0);
                    }
                }
        ).start();
    }

    @Override
    public void Load() {
        mListView.complate();//关闭刷新状态
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mListView.complate();//关闭刷新状态
        }
    };

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState){
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                if(mListView.getLastVisiblePosition() == mListView.getCount() -1){
                    //下拉到最后一个item自动加载
                    size +=10;
                    initBean();
                    adapter.notifyDataSetChanged();
                }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
