package com.cloudpurchase.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.RedPackageEntity;

import java.util.List;

/**
 * 红包界面 适配器
 *
 * 构造器 传入 0 表示 可以使用的红包(默认)
 *       传入 1 表示 已经过期的红包
 *
 */
public class RedPackageAdapter extends BaseAdapter{

    private List<RedPackageEntity> list;
    private Context context;
    private int flag = 0;

    public RedPackageAdapter(Context context,List<RedPackageEntity> list, int  flag){
        this.context = context;
        this.list = list;
        this.flag = flag;
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
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_red_package,null);
            holder.mItemMoney = (TextView) convertView.findViewById(R.id.redPackage_item_money);
            holder.mItemTremMoney = (TextView) convertView.findViewById(R.id.redPackage_item_term);
            holder.mItemTitle = (TextView) convertView.findViewById(R.id.redPackage_item_title);
            holder.mItemBornTime = (TextView) convertView.findViewById(R.id.redPackage_item_bornTime);
            holder.mItemValidityPeriod = (TextView) convertView.findViewById(R.id.redPackage_item_validityPeriod);
            holder.mItemNotes = (TextView) convertView.findViewById(R.id.redPackage_item_notes);
            holder.mItemImg = (ImageView) convertView.findViewById(R.id.redPackage_item_img);
            holder.mItemUse = (TextView) convertView.findViewById(R.id.redPackage_item_use);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mItemMoney.setText(list.get(position).mMoney);
        holder.mItemTremMoney.setText(list.get(position).mTremMoney);
        holder.mItemTitle.setText(list.get(position).mTile);
        holder.mItemBornTime.setText(list.get(position).mBornTime);
        holder.mItemValidityPeriod.setText(list.get(position).mValidityPeriod);
        holder.mItemNotes.setText(list.get(position).mNotes);
        if(flag == 0){
            // 表示可以使用的红包
//            holder.mItemImg.setImageResource();
        }else if(flag == 1){
            //表示已经过期的红包
            holder.mItemImg.setImageResource(R.mipmap.hb_ygq_2x);
            holder.mItemUse.setVisibility(View.GONE);
        }

        holder.mItemUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"使用",Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
    private class ViewHolder{
        TextView mItemMoney;//红包金额
        TextView mItemTremMoney;//满足条件
        TextView mItemTitle;//红包标题
        TextView mItemBornTime;//生效期
        TextView mItemValidityPeriod;//有效期
        TextView mItemNotes;//提示消息
        ImageView mItemImg;//是否过期标志
        TextView mItemUse;//使用 按钮
    }

}
