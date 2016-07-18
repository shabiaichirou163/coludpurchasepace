package com.cloudpurchase.cloudpurchase;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudpurchase.adpater.AddressAdapter;
import com.cloudpurchase.db.AddressBean;
import com.cloudpurchase.db.AddressDB_Use;
import com.cloudpurchase.mrwujay.cascade.activity.BaseActivity;
import com.cloudpurchase.utils.FileUtils;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.wheel.widget.OnWheelChangedListener;
import com.cloudpurchase.wheel.widget.WheelView;
import com.cloudpurchase.wheel.widget.adapters.ArrayWheelAdapter;


/**
 * 添加地址界面
 *
 *代表存入本地的地址数量
 *
 */
public class AddAddressActivity extends BaseActivity implements View.OnClickListener, OnWheelChangedListener {

    private ImageView mBack;
    private Button mAdd;
    private EditText mName;//收货人姓名
    private EditText mMobile;//收货人手机号
    private TextView mSelectAddress;//选择地址
    private EditText mAddressInfo;//地址信息
    private boolean flag = true;//是否设为默认地址标志
    private WheelView mViewProvince;//创建省市级三级联动
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private Button mBtnConfirm;//dialog确定按钮
    private Dialog mDialog;
    private Button mCancel;//关闭dialog
    private AddressDB_Use sql;
    public static AddAddressActivity ADD_ADDRESS_ACTIVITY_THIS;
    private Intent mIntent;
    private boolean onBackFlag = false;//判断用户是否点击了修改
    private int ID = -1; //从AllAddressActivity 传递过来的ID
    private FileUtils mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ID =  AllAddressActivity.ACTIVITY_THIS.ID;
        ADD_ADDRESS_ACTIVITY_THIS = this;
        mFile = new FileUtils(this);
        sql = new AddressDB_Use(this);
        mDialog = new AlertDialog.Builder(this).create();//初始化dialog
        mIntent = this.getIntent();
        initView();
        setOnclick();
    }

    public void initView() {
        mBack = (ImageView) findViewById(R.id.address_back);
        mAdd = (Button) findViewById(R.id.address_addInfo);
        mName = (EditText) findViewById(R.id.address_name);
        mMobile = (EditText) findViewById(R.id.address_mobile);
        mSelectAddress = (TextView) findViewById(R.id.address_selectAddress);
        mAddressInfo = (EditText) findViewById(R.id.address_info);
        if(mIntent.getStringExtra("name")!=null){
            onBackFlag = true;
            String name = mIntent.getStringExtra("name");
            String phone = mIntent.getStringExtra("phone");
            String info = mIntent.getStringExtra("info");
            int flag = mIntent.getIntExtra("flag",0);
            mName.setText(name);
            mMobile.setText(phone);
            mAddressInfo.setText(info);
            if(flag == 1){
                this.flag = true;
            }else{
                this.flag = false;
            }
        }
    }


    public void setOnclick() {
        mBack.setOnClickListener(this);
        mAdd.setOnClickListener(this);
        mSelectAddress.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent mIntent = null;
        switch (id){
            case R.id.address_back:
                if(onBackFlag){
                    // 当 onBackFlag 为true时，表示当前显示的数据是从 AllAddressActivity 传递过来的
                    String name = mName.getText().toString();
                    String phone = mMobile.getText().toString();
                    String info = mAddressInfo.getText().toString();
                    if(name.length() == 0 &&  phone.length() == 0  && info.length() == 0 ){
                        Toast.makeText(this,"请将信息填写完整",Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        AddressBean bean = new AddressBean(sql.getDBSize(),name,phone,info,0);
                        sql.insert(bean);
                        finish();
                        mIntent = new Intent(this,AllAddressActivity.class);
                        startActivity(mIntent);
                    }
                }else{
                    finish();
                    mIntent = new Intent(this,AllAddressActivity.class);
                    startActivity(mIntent);
                }
                break;
            case R.id.address_addInfo:
                if(onBackFlag){
                    // 当 onBackFlag 为true时，表示当前显示的数据是从 AllAddressActivity 传递过来的
                    String name = mName.getText().toString();
                    String phone = mMobile.getText().toString();
                    String info = mAddressInfo.getText().toString();
                    if(name.length() == 0 &&  phone.length() == 0  && info.length() == 0 ){
                        Toast.makeText(this,"请将信息填写完整",Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        AddressBean bean = new AddressBean(sql.getDBSize(),name,phone,info,0);
                        sql.insert(bean);
                        finish();
                        mIntent = new Intent(this,AllAddressActivity.class);
                        startActivity(mIntent);
                    }
                }else{
                    String name = mName.getText().toString();
                    String phone = mMobile.getText().toString();
                    String info = mAddressInfo.getText().toString();
                    if(name.length() == 0 ||  phone.length() == 0  || info.length() == 0 ){
                        Toast.makeText(this,"请将信息填写完整",Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        AddressBean bean = new AddressBean(sql.getDBSize(),name,phone,info,0);
                        sql.insert(bean);
                        Toast.makeText(this,"添加成功", Toast.LENGTH_SHORT).show();
                        finish();
                        mIntent = new Intent(this,AllAddressActivity.class);
                        startActivity(mIntent);
                    }
                }
                break;
            case R.id.address_selectAddress:
                // 选择地址
                initDiaLog();
                break;
            case R.id.btn_confirm:
                showSelectedResult();//调用方法，输入选择的信息
                mDialog.dismiss();//关闭dialog
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(onBackFlag){
            // 当 onBackFlag 为true时，表示当前显示的数据是从 AllAddressActivity 传递过来的
            String name = mName.getText().toString();
            String phone = mMobile.getText().toString();
            String info = mAddressInfo.getText().toString();
            if(name.length() == 0 &&  phone.length() == 0  && info.length() == 0 ){
                Toast.makeText(this,"请将信息填写完整",Toast.LENGTH_SHORT).show();
                return;
            }else {
                AddressBean bean = new AddressBean(sql.getDBSize(),name,phone,info,0);
                sql.insert(bean);
                Toast.makeText(this,"修改", Toast.LENGTH_SHORT).show();
                finish();
                mIntent = new Intent(this,AllAddressActivity.class);
                startActivity(mIntent);
            }
        }else{
            finish();
            mIntent = new Intent(this,AllAddressActivity.class);
            startActivity(mIntent);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initDiaLog(){
        View view = View.inflate(this,R.layout.view_select_address,null);
        mDialog.show();
        mDialog.getWindow().setContentView(view);
        setUpViews(view);
        setUpListener();
        setUpData();
    }
    private void setUpViews(View view) {
        mViewProvince = (WheelView) view.findViewById(R.id.id_province);
        mViewCity = (WheelView) view.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) view.findViewById(R.id.id_district);
        mBtnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        mCancel = (Button) view.findViewById(R.id.btn_cancel);
    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mBtnConfirm.setOnClickListener(this);
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(this, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        mCurrentDistrictName= mDistrictDatasMap.get(mCurrentCityName)[0];
        if (wheel == mViewProvince) {
            updateCities();
            mCurrentDistrictName=mDistrictDatasMap.get(mCurrentCityName)[0];
        } else if (wheel == mViewCity) {
            updateAreas();
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[] { "" };
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[] { "" };
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showSelectedResult() {
       /* Toast.makeText(this, "当前选中:"+mCurrentProviceName+","+mCurrentCityName+","
                +mCurrentDistrictName+","+mCurrentZipCode, Toast.LENGTH_SHORT).show();*/
        if(mAddressInfo.getText() != null){
            mAddressInfo.setText(mCurrentProviceName+" "+mCurrentCityName+" "+
                    mCurrentDistrictName+" ");
        }else{
            mAddressInfo.setText("");
            mAddressInfo.setText(mCurrentProviceName+" "+mCurrentCityName+" "+
                    mCurrentDistrictName+" ");
        }
    }
}
