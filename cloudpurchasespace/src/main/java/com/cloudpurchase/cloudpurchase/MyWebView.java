package com.cloudpurchase.cloudpurchase;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.utils.LogUtils;

/**
 * 所有的webView
 *
 * url 在使用Intent 跳转时添加
 */
public class MyWebView extends BaseActivity{

    private ProgressBar mPrg;
    private WebView mView;
    public String URL;
    public static MyWebView WEB_VIEW_THIS;

    @Override
    public void initView() {
        setContentView(R.layout.view_web);
        WEB_VIEW_THIS = this;
        mPrg = (ProgressBar) f(R.id.web_prg);
        mView = (WebView) f(R.id.web_view);
        Intent mIntent = this.getIntent();
        URL = mIntent.getStringExtra("url");
        mView.getSettings().setJavaScriptEnabled(true);//允许网页中加载js
        //监听每次打开的地址
        mView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mView.loadUrl(url);
                return false;
            }
        });
        //监听打开进度
        mView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mPrg.setVisibility(View.VISIBLE);
                if(newProgress == 100){
                    mPrg.setVisibility(View.GONE);
                }
                mPrg.setProgress(newProgress);//设置打开进度
            }
        });
        mView.loadUrl(URL);
    }

    @Override
    public void setOnclick() {

    }

    @Override
    public void initList() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&mView.canGoBack()){
            mView.goBack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
