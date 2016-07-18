package com.cloudpurchase.cloudpurchase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cloudpurchase.adpater.UserTaskAdapter;
import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.entity.UserTaskEntity;
import com.cloudpurchase.net.HttpImgLoader;
import com.cloudpurchase.utils.FileUtils;
import com.cloudpurchase.veiw.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 每日任务界面
 *
 */
public class UserTaskActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBack;
    private TextView mHelp;//玩法介绍
    private ScrollView mScrollView;
    private RoundImageView mUserImg;//用户头像
    private TextView mVIP_Lv;//VIP等级
    private ProgressBar mPrg;//经验值进度条
    private TextView mPrgNum;//当前进度
    private ListView mListView;
    private List<UserTaskEntity> list = new ArrayList<UserTaskEntity>();
    private UserTaskAdapter adapter;
    private FileUtils mFile;

    @Override
    public void initView() {
        setContentView(R.layout.activity_user_task);
        mFile = new FileUtils(this);
        mBack = (ImageView) f(R.id.userTask_back);
        mHelp = (TextView) f(R.id.userTask_help);
        mScrollView = (ScrollView) f(R.id.userTask_scrollView);
        mUserImg = (RoundImageView) f(R.id.userTask_usrImg);
        mVIP_Lv = (TextView) f(R.id.userTask_VIP_Lv);
        mPrg = (ProgressBar) f(R.id.userTask_prg);
        mPrgNum = (TextView) f(R.id.userTask_prgNum);
        mListView = (ListView) f(R.id.userTask_listView);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);//隐藏下划线
        refrashLv(mListView);
        HttpImgLoader.getHttpImgLoader().initImg(MyApplication.USER_HEADIMG_URL,mUserImg);
        mVIP_Lv.setText("lv."+MyApplication.USER_USERRANK+MyApplication.USER_RANK_NAME);
        mPrg.setProgress(55);
        mPrgNum.setText(mPrg.getProgress()+"");

    }

    @Override
    public void setOnclick() {
        mBack.setOnClickListener(this);
        mHelp.setOnClickListener(this);
    }

    @Override
    public void initList() {
        initBean();
        adapter = new UserTaskAdapter(this,list);
    }

    private void initBean(){
        for(int i = 0; i < 4 ; i++){
            UserTaskEntity bean = new UserTaskEntity();
            bean.taskImg = R.mipmap.a_rw_1_2x;
            bean.num = "50";
            bean.taskTitle = "每日签到"+i;
            bean.flag = true;
            list.add(bean);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent mIntent = null;
        switch (id){
            case R.id.userTask_back:
                finish();
                break;
            case R.id.userTask_help:
                mIntent = new Intent(this,MyWebView.class);
                mIntent.putExtra("url","https://www.baidu.com/");
                startActivity(mIntent);
                break;
        }
    }

    //刷新Listview高度方法
    public void refrashLv(ListView lv){
        int height = 0;
        ListAdapter adapter = lv.getAdapter();
        for(int i = 0;i < adapter.getCount();i ++){
            View itemView = adapter.getView(i, null, null);
            itemView.measure(0,0);
            height+=itemView.getMeasuredHeight();
        }
        height = height + lv.getDividerHeight()*(adapter.getCount()-1)
                +lv.getPaddingBottom()+lv.getPaddingTop();
        lv.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, height));
        mScrollView.invalidate();
    }
}
