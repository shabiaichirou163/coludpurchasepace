package com.cloudpurchase.cloudpurchase;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cloudpurchase.adpater.AddressAdapter;
import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.db.AddressBean;
import com.cloudpurchase.db.AddressDB_Use;
import com.cloudpurchase.utils.FileUtils;
import com.cloudpurchase.utils.LogUtils;

import java.util.List;

/**
 * 打开收货地址后的 地址列表（所有地址）
 *
 */
public class AllAddressActivity extends BaseActivity implements
        View.OnClickListener, AddressAdapter.SetItemFlagCall, AdapterView.OnItemClickListener {

    private ImageView mBack;
    private ImageView mAddImg;//右上方添加按钮
    private Button mAddBtn;
    private LinearLayout mLinearLayout;//用于管理背景
    private ListView mListView;
    private AddressAdapter adapter;
    private AddressDB_Use sql;
    private List<AddressBean> list;
    public static AllAddressActivity ACTIVITY_THIS;
    public static int ID;

    @Override
    public void initView() {
        ACTIVITY_THIS = this;
        setContentView(R.layout.actitivy_all_address);
        mListView = (ListView) f(R.id.add_address_listView);
        mBack = (ImageView) f(R.id.add_address_back);
        mAddImg = (ImageView) f(R.id.add_address_addImg);
        mAddBtn = (Button) f(R.id.add_address_addBtn);
        mLinearLayout = (LinearLayout) f(R.id.add_address_background);
        //当储存的地址数量等于0 时，隐藏ListView,反之显示
        if(getImteSize() == 0){
            mListView.setVisibility(View.GONE);
            mAddBtn.setVisibility(View.VISIBLE);
            mLinearLayout.setVisibility(View.VISIBLE);
        }else{
            adapter = new AddressAdapter(this,list);
            mListView.setAdapter(adapter);
            adapter.setCall(this);
            mListView.setDivider(null);//隐藏下划线
            mListView.setVisibility(View.VISIBLE);
            mAddBtn.setVisibility(View.GONE);
            mLinearLayout.setBackgroundColor(Color.WHITE);
            LogUtils.e("默认地址的信息是："+getDefaultInfo());
        }
    }

    @Override
    public void setOnclick() {
        mBack.setOnClickListener(this);
        mAddImg.setOnClickListener(this);
        mAddBtn.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void initList() {
        sql = new AddressDB_Use(this);
        if(sql == null){
            return;
        }else{
            list = sql.findAll();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent mIntent = null;
        switch(id){
            case R.id.add_address_back:
                finish();
                break;
            case R.id.add_address_addImg:
                finish();
                mIntent = new Intent(this,AddAddressActivity.class);
                startActivity(mIntent);
                break;
            case R.id.add_address_addBtn:
                finish();
                mIntent = new Intent(this,AddAddressActivity.class);
                startActivity(mIntent);
                break;
        }
    }

    /*
    设置为默认地址后的回调
     */
    @Override
    public void setFlagIsTrue() {

    }
    /*
      取消 设置为默认地址后的回调
     */
    @Override
    public void setFlagIsFalse() {

    }

    @Override
    public void delData() {

    }

    private int getImteSize(){
        return  sql.findAll().size();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.finish();
        String name = list.get(position).getName().toString();
        String phone = list.get(position).getPhone().toLowerCase();
        String info = list.get(position).getInfo().toString();
        int flag = list.get(position).getFlag();
        Intent mIntet = new Intent(this,AddAddressActivity.class);
        mIntet.putExtra("name",name);
        mIntet.putExtra("phone",phone);
        mIntet.putExtra("info",info);
        mIntet.putExtra("flag",flag);
        this.ID = list.get(position).getId();
        sql.deleteData(ID);
        startActivity(mIntet);
    }


    /*
    获取被设为默认地址的信息
     */
    private String getDefaultInfo(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("DEFAULT_ADDRESS", Activity.MODE_PRIVATE);
        String index = sharedPreferences.getString("key","1");
        int id = Integer.valueOf(index);
        if(index == null){
            return null;
        }else{
            AddressBean bean = sql.fingById(id);
            int beanID = bean.getId();
            String name = bean.getName();
            String phone = bean.getPhone();
            String info = bean.getInfo();
            return " id :"+beanID+",name: "+name+",phone: "+phone+",info: "+ info;
        }
    }

}
