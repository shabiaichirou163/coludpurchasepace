package com.cloudpurchase.adpater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudpurchase.cloudpurchase.AllAddressActivity;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.db.AddressBean;
import com.cloudpurchase.db.AddressDB_Use;
import com.cloudpurchase.utils.FileUtils;
import com.cloudpurchase.utils.LogUtils;

import java.util.List;

/**
 * 用于展示收货地址信息
 */
public class AddressAdapter extends BaseAdapter{

    private  List<AddressBean> list;
    private Context context;
    private SetItemFlagCall call;
    private AddressDB_Use sql;
    private FileUtils mFile;

    public AddressAdapter(Context context,List<AddressBean> list){
        this.context = context;
        this.list = list;
        sql = new AddressDB_Use(context);
        mFile = new FileUtils(context);
        for(int j = 0 ; j < list.size() ; j++){
            int id= list.get(j).getId();
            sql.upData(id,0);
        }
        //每次启动这个页面时，在本地获取被设置为默认的额地址
        SharedPreferences sharedPreferences = context.getSharedPreferences("DEFAULT_ADDRESS", Activity.MODE_PRIVATE);
        String index = sharedPreferences.getString("key","1");
        if(index == null){

        }else{
            sql.upData(Integer.valueOf(index),1);
        }
      /*  if(mFile.ReadTxt("DEFAULT_ADDRESS") == null){
            LogUtils.e("mFile.ReadTxt(\"DEFAULT_ADDRESS\") == null");
            return;
        }else {
            sql.upData(Integer.valueOf(mFile.ReadTxt("DEFAULT_ADDRESS")),1);
            LogUtils.e("取出的ID是："+mFile.ReadTxt("DEFAULT_ADDRESS"));
        }*/
    }

    public void setCall(SetItemFlagCall call){
        this.call =call;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_address,null);
            holder = new ViewHolder();
            holder.mItemName = (TextView) convertView.findViewById(R.id.address_item_name);
            holder.mImtePhone = (TextView) convertView.findViewById(R.id.address_item_phone);
            holder.mItemInfo = (TextView) convertView.findViewById(R.id.address_item_info);
            holder.mItemFlag = (ImageView) convertView.findViewById(R.id.address_item_flagImg);
            holder.mItemSetFlag = (LinearLayout) convertView.findViewById(R.id.address_item_setFlag);
            holder.mItemDel = (LinearLayout) convertView.findViewById(R.id.address_item_del);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mItemName.setText(list.get(position).getName());
        holder.mImtePhone.setText(list.get(position).getPhone());
        holder.mItemInfo.setText(list.get(position).getInfo());
        if(list.get(position).getFlag() == 1){
            holder.mItemFlag.setImageResource(R.mipmap.a_ico_mark_2x);
            call.setFlagIsTrue();
        }else {
            holder.mItemFlag.setImageResource(R.mipmap.a_admin_duihao2_2x);
            call.setFlagIsFalse();
        }
        holder.mItemSetFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当被设置为默认地址时  1 为 true   0 为false
                if(list.get(position).getFlag() == 1){
                    holder.mItemFlag.setImageResource(R.mipmap.a_ico_mark_2x);
                    //当取消设置默认时，在本地删除文件
//                    mFile.deleteFile("DEFAULT_ADDRESS");
                    SharedPreferences sharedPreferences = context.getSharedPreferences("DEFAULT_ADDRESS", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();//清除方法
                    editor.commit();
                    call.setFlagIsFalse();
                    Toast.makeText(context,"oo",Toast.LENGTH_SHORT).show();
                    for(int j = 0 ; j < list.size() ; j++){
                        int id= list.get(j).getId();
                        sql.upData(id,0);
                    }
                    AllAddressActivity.ACTIVITY_THIS.finish();
                    Intent i = new Intent(AllAddressActivity.ACTIVITY_THIS,AllAddressActivity.class);
                    AllAddressActivity.ACTIVITY_THIS.startActivity(i);

                }else {
                    //当非 设为默认时
                    //将被设为默认的地址存入本地，下次重新启动页面时，再读取出来
                    LogUtils.e("第"+list.get(position).getId()+"设为默认");
                    String saveValue = String.valueOf(list.get(position).getId());
//                    mFile.saveTxt("DEFAULT_ADDRESS",saveValue);
//                    LogUtils.e("存入的ID是："+saveValue);
                    SharedPreferences sharedPreferences = context.getSharedPreferences("DEFAULT_ADDRESS", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("key",saveValue);//将ID保存
                    editor.commit();
                    holder.mItemFlag.setImageResource(R.mipmap.a_admin_duihao2_2x);
                    call.setFlagIsTrue();
                    Toast.makeText(context,"oo",Toast.LENGTH_SHORT).show();
                    LogUtils.e("第"+position+"取消设为默认");
                    for(int j = 0 ; j < list.size() ; j++){
                        int id= list.get(j).getId();
                        sql.upData(id,0);
                    }
                    sql.upData(list.get(position).getId(),1);
                    AllAddressActivity.ACTIVITY_THIS.finish();
                    Intent i = new Intent(AllAddressActivity.ACTIVITY_THIS,AllAddressActivity.class);
                    AllAddressActivity.ACTIVITY_THIS.startActivity(i);
                }
            }
        });
        holder.mItemDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                call.delData();
                Toast.makeText(context,"删除",Toast.LENGTH_SHORT).show();
                LogUtils.e("第"+position+"删除");
                sql.deleteData(list.get(position).getId());
            }
        });
        return convertView;
    }

    private class ViewHolder{
        TextView mItemName;
        TextView mImtePhone;
        TextView mItemInfo;//收货地址
        ImageView mItemFlag;//是否设为默认img
        LinearLayout mItemSetFlag;//是否设为默认 按钮
        LinearLayout mItemDel;//删除按钮
    }

    /**
     * 当点击设置为默认地址时调用此回调方法
     */
    public interface SetItemFlagCall{
        public void setFlagIsTrue();
        public void setFlagIsFalse();
        public void delData();
    }
}
