package com.cloudpurchase.cloudpurchase;

import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudpurchase.base.BaseActivity;

/**
 * 我的客服界面
 */
public class MyServiceActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBack;
    private ImageView mNoviceHelp;//新手帮助
    private ImageView mWinningRule;//中奖规则
    private ImageView mAddress;//收货地址
    private Button mBtn;//微信客服按钮
    private TextView mQuestion1,mQuestion2,mQuestion3,mQuestion4;//问题

    @Override
    public void initView() {
        setContentView(R.layout.activity_myservice);
        mBack = (ImageView) f(R.id.myService_back);
        mNoviceHelp = (ImageView) f(R.id.myService_noviceHelp);
        mWinningRule = (ImageView) f(R.id.myService_winningRule);
        mAddress = (ImageView) f(R.id.myService_address);
        mBtn = (Button) f(R.id.myService_btn);
        mQuestion1 = (TextView) f(R.id.myService_question1);
        mQuestion2 = (TextView) f(R.id.myService_question2);
        mQuestion3 = (TextView) f(R.id.myService_question3);
        mQuestion4 = (TextView) f(R.id.myService_question4);
    }

    @Override
    public void setOnclick() {
        mBack.setOnClickListener(this);
        mNoviceHelp.setOnClickListener(this);
        mWinningRule.setOnClickListener(this);
        mAddress.setOnClickListener(this);
        mBtn.setOnClickListener(this);
        mQuestion1.setOnClickListener(this);
        mQuestion2.setOnClickListener(this);
        mQuestion3.setOnClickListener(this);
        mQuestion4.setOnClickListener(this);
    }

    @Override
    public void initList() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent mIntent = null;
        switch(id){
            case R.id.myService_back:
                finish();
                break;
            case R.id.myService_noviceHelp:
                mIntent = new Intent(this,MyWebView.class);
                mIntent.putExtra("url","https://www.baidu.com/");
                startActivity(mIntent);
                break;
            case R.id.myService_winningRule:
                mIntent = new Intent(this,MyWebView.class);
                mIntent.putExtra("url","https://www.baidu.com/");
                startActivity(mIntent);
                break;
            case R.id.myService_address:
                mIntent = new Intent(this,MyWebView.class);
                mIntent.putExtra("url","https://www.baidu.com/");
                startActivity(mIntent);
                break;
            case R.id.myService_btn:
                initStart();
                break;
            case R.id.myService_question1:
                mIntent = new Intent(this,MyWebView.class);
                mIntent.putExtra("url","https://www.baidu.com/");
                startActivity(mIntent);
                break;
            case R.id.myService_question2:
                mIntent = new Intent(this,MyWebView.class);
                mIntent.putExtra("url","https://www.baidu.com/");
                startActivity(mIntent);
                break;
            case R.id.myService_question3:
                mIntent = new Intent(this,MyWebView.class);
                mIntent.putExtra("url","https://www.baidu.com/");
                startActivity(mIntent);
                break;
            case R.id.myService_question4:
                mIntent = new Intent(this,MyWebView.class);
                mIntent.putExtra("url","https://www.baidu.com/");
                startActivity(mIntent);
                break;
        }
    }

    /*
    跳转到微信聊天界面
     */
    private void initStart(){
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        startActivityForResult(intent, 0);
    }

}
