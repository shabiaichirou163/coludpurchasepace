package com.cloudpurchase.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.RechargeRecordEntity;

import java.util.List;

/**
 * 充值记录适配器
 */
public class RechargeRecordAdapter extends BaseAdapter{

    private List<RechargeRecordEntity> list;
    private Context context;

    public RechargeRecordAdapter(Context context, List<RechargeRecordEntity> list){
        this.context = context;
        this.list = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_recharge,null);
            holder.mItem_mode = (TextView) convertView.findViewById(R.id.recharge_item_mode);
            holder.mItem_time = (TextView) convertView.findViewById(R.id.recharge_item_time);
            holder.mItem_money = (TextView) convertView.findViewById(R.id.recharge_item_money);
            holder.mItem_flag = (TextView) convertView.findViewById(R.id.recharge_item_flag);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mItem_mode.setText(list.get(position).mMode);
        holder.mItem_time.setText(list.get(position).mTime);
        holder.mItem_money.setText(list.get(position).mMoney);
        holder.mItem_flag.setText(list.get(position).mFlag);
        return convertView;
    }

    private class ViewHolder{
        TextView mItem_mode;//支付方式
        TextView mItem_time;//充值时间
        TextView mItem_money;//充值金额
        TextView mItem_flag;//是否付款标志
    }
}
