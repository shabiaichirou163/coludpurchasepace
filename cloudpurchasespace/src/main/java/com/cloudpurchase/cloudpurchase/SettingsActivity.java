package com.cloudpurchase.cloudpurchase;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.net.HttpRequest;
import com.cloudpurchase.utils.Constants;
import com.cloudpurchase.utils.FileUtils;
import com.cloudpurchase.utils.RequestResultIn;
import com.cloudpurchase.utils.SharedFileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 设置界面
 */
public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBack;
    private Button mOutLogin;//退出登陆
    private FileUtils mFile;
    private TextView mHelp;//新手帮助
    private TextView mPrivate;//隐私政策
    private TextView mAboutUs;//关于我们
    private TextView mClear;//清除缓存
    private TextView mNotice;//打开推送
    private TextView mProblem;//常见问题


    @Override
    public void initView() {
        setContentView(R.layout.activity_settings);
        mFile = new FileUtils(this);
        mBack = (ImageView) f(R.id.settings_back);
        mOutLogin = (Button) f(R.id.settings_outLogin);
        mHelp = (TextView) f(R.id.settings_help);
        mPrivate = (TextView) f(R.id.settings_private);
        mAboutUs = (TextView) f(R.id.settings_aboutUs);
        mClear = (TextView) f(R.id.settings_clear);
        mNotice = (TextView) f(R.id.settings_notice);
        mProblem = (TextView) f(R.id.settings_problem);
    }

    @Override
    public void setOnclick() {
        mBack.setOnClickListener(this);
        mOutLogin.setOnClickListener(this);
        mHelp.setOnClickListener(this);
        mPrivate.setOnClickListener(this);
        mAboutUs.setOnClickListener(this);
        mClear.setOnClickListener(this);
        mNotice.setOnClickListener(this);
        mProblem.setOnClickListener(this);
    }

    @Override
    public void initList() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent mIntent = null;
        switch (id){
            case R.id.settings_back:
                finish();
                break;
            case R.id.settings_outLogin:
                creatProgressDialog();
                String url = Constants.OUT_LOGIN;
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("userId",MyApplication.USER_ID);
                map.put("userIdsn",MyApplication.SerialNumber);
                HttpRequest.getHttpRequest().requestPOST(url, null,map, new RequestResultIn() {
                    @Override
                    public void requstSuccful(String result) {
                        try {
                            JSONObject obj = new JSONObject(result);
                            String code = obj.getString("code");
                            if(code.equals("0")){
                                Toast.makeText(SettingsActivity.this,"退出成功",Toast.LENGTH_SHORT).show();
                                MyApplication.USER_IS_LOGIN_FLAG = false;
                                finish();
                                toOtherActivity(HomeActivity.class);
                                SharedFileUtils util = new SharedFileUtils(SettingsActivity.this,"USER_INFO", Activity.MODE_PRIVATE);
                                util.delFile();//删除本地存储的用户信息
                                cancelDialog();
                            }else{
                                Toast.makeText(SettingsActivity.this,result,Toast.LENGTH_SHORT).show();
                                cancelDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void requstError(VolleyError error) {
                        cancelDialog();
                    }
                });
                break;
            case R.id.settings_help:
                mIntent = new Intent(this,MyWebView.class);
                mIntent.putExtra("url","https://www.baidu.com/");
                startActivity(mIntent);
                break;
            case R.id.settings_private:
                mIntent = new Intent(this,MyWebView.class);
                mIntent.putExtra("url","https://www.baidu.com/");
                startActivity(mIntent);
                break;
            case R.id.settings_aboutUs:
                mIntent = new Intent(this,MyWebView.class);
                mIntent.putExtra("url","https://www.baidu.com/");
                startActivity(mIntent);
                break;
            case R.id.settings_clear:

                break;
            case R.id.settings_problem:
                mIntent = new Intent(this,MyWebView.class);
                mIntent.putExtra("url","https://www.baidu.com/");
                startActivity(mIntent);
                break;
            case R.id.settings_notice:
                mIntent = new Intent(this,MyWebView.class);
                mIntent.putExtra("url","https://www.baidu.com/");
                startActivity(mIntent);
                break;

        }
    }
}
