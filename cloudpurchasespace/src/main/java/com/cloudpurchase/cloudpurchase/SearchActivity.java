package com.cloudpurchase.cloudpurchase;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.cloudpurchase.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener{
    private ImageButton mBackBtn,mDeleteBtn;
    private EditText mEdit;
    private String mInputGoods;
    @Override
    public void initView() {
        setContentView(R.layout.activity_search);
        mBackBtn= (ImageButton) findViewById(R.id.serach_back_btn);
        mDeleteBtn= (ImageButton) findViewById(R.id.search_delete);
        mEdit= (EditText) findViewById(R.id.serach_edit);


    }

    @Override
    public void setOnclick() {
        mEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mInputGoods=s.toString();
                if(s.length()>0&&null!=s){
                    mDeleteBtn.setVisibility(View.VISIBLE);
                }else {
                    mDeleteBtn.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDeleteBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
    }

    @Override
    public void initList() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.serach_back_btn:
                this.finish();
                break;
            case R.id.search_delete:
                if (mInputGoods.length()>0){
                    mEdit.setText("");
                    mDeleteBtn.setVisibility(View.GONE);
                }
                break;
        }
    }
}
