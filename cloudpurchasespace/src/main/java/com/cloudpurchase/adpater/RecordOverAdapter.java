package com.cloudpurchase.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.RecordOverEntity;
import com.cloudpurchase.net.HttpImgLoader;

import java.util.List;

/**
 * 全部记录-》已揭晓 适配器
 */
public class RecordOverAdapter extends BaseAdapter {

    private List<RecordOverEntity> list;
    private Context context;

    public RecordOverAdapter(Context context , List<RecordOverEntity> list){
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
            convertView = View.inflate(context, R.layout.item_record_over,null);
            holder.mItemImg = (ImageView) convertView.findViewById(R.id.record_item_over_img);
            holder.mItemGoodsName = (TextView) convertView.findViewById(R.id.record_item_over_goodsName);
            holder.mItemNum = (TextView) convertView.findViewById(R.id.record_item_over_num);
            holder.mItemName = (TextView) convertView.findViewById(R.id.record_item_over_name);
            holder.mItemLuckyNum = (TextView) convertView.findViewById(R.id.record_item_over_luckyNum);
            holder.mItemUserNum = (TextView) convertView.findViewById(R.id.record_item_over_userNum);
            holder.mItemTime = (TextView) convertView.findViewById(R.id.record_item_over_time);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        HttpImgLoader.getHttpImgLoader().initImg(list.get(position).mImg,holder.mItemImg);
        holder.mItemGoodsName.setText(list.get(position).mGoodsName);
        holder.mItemNum.setText(list.get(position).mNum);
        holder.mItemName.setText(list.get(position).mUserName);
        holder.mItemUserNum.setText(list.get(position).mUserNum);
        holder.mItemLuckyNum.setText(list.get(position).mLuckyNum);
        holder.mItemTime.setText(list.get(position).mTime);
        return convertView;
    }

    private class ViewHolder{
        ImageView mItemImg;//商品图
        TextView mItemGoodsName;//商品名称
        TextView mItemNum;//总需次数
        TextView mItemName;//中奖人昵称
        TextView mItemLuckyNum;//中奖号码
        TextView mItemUserNum;//参与次数
        TextView mItemTime;//时间
    }
}
