package com.cloudpurchase.adpater;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.AgoShowEntity;
import com.cloudpurchase.net.HttpImgLoader;
import com.cloudpurchase.utils.BitmapUtils;
import com.cloudpurchase.utils.LogUtils;
import com.sina.weibo.sdk.utils.ImageUtils;

import java.util.List;

/**
 * 往期揭晓适配器
 *
 */
public class AgoShowAdapter extends BaseAdapter  {

    private List<AgoShowEntity> list;
    private Context context;

    public AgoShowAdapter(Context context,List<AgoShowEntity> list){
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_ago_show,null);
            holder.mItemUserImg = (ImageView) convertView.findViewById(R.id.agoShow_item_userImg);
            holder.mItemNumber = (TextView) convertView.findViewById(R.id.agoShow_item_number);
            holder.mItemTime = (TextView) convertView.findViewById(R.id.agoShow_item_time);
            holder.mItemName = (TextView) convertView.findViewById(R.id.agoShow_item_name);
            holder.mItemIP = (TextView) convertView.findViewById(R.id.agoShow_item_ip);
            holder.mItemID = (TextView) convertView.findViewById(R.id.agoShow_item_id);
            holder.mItemLuckyNum = (TextView) convertView.findViewById(R.id.agoShow_item_luckyNum);
            holder.mItemJoinNum = (TextView) convertView.findViewById(R.id.agoShow_item_joinNum);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        HttpImgLoader.getHttpImgLoader().initImg(list.get(position).mUserImgUrl,holder.mItemUserImg);
        holder.mItemNumber.setText(list.get(position).mNumber);
        holder.mItemTime.setText(list.get(position).mTime);
        holder.mItemName.setText(list.get(position).mName);
        holder.mItemIP.setText(list.get(position).mIP);
        holder.mItemID.setText(list.get(position).mID);
        holder.mItemLuckyNum.setText(list.get(position).mLuckyNum);
        holder.mItemJoinNum.setText(list.get(position).mJoinNum);

        return convertView;
    }


    private class ViewHolder{
        ImageView mItemUserImg;//用户头像
        TextView mItemNumber;//揭晓期号
        TextView mItemTime;//揭晓时间
        TextView mItemName;//用户名称
        TextView mItemIP;//用户IP
        TextView  mItemID;//用户ID
        TextView mItemLuckyNum;//中奖号码，幸运号码
        TextView mItemJoinNum;//参加次数
    }

}
