package com.cloudpurchase.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.SmallOlderEntity;
import com.cloudpurchase.net.HttpImgLoader;

import java.util.List;

/**
 *  用户中心——>小房间——》正已经揭晓的适配器
 *
 */
public class SmallOlderAdapter extends BaseAdapter{

    private List<SmallOlderEntity> list;
    private Context context;

    public SmallOlderAdapter(Context context,List<SmallOlderEntity> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.item_record_over,null);
            holder.mItemImg = (ImageView) view.findViewById(R.id.record_item_over_img);
            holder.mItemGoodsName = (TextView) view.findViewById(R.id.record_item_over_goodsName);
            holder.mItemNum = (TextView) view.findViewById(R.id.record_item_over_num);
            holder.mItemName = (TextView) view.findViewById(R.id.record_item_over_name);
            holder.mItemLuckyNum = (TextView) view.findViewById(R.id.record_item_over_luckyNum);
            holder.mItemUserNum = (TextView) view.findViewById(R.id.record_item_over_userNum);
            holder.mItemTime = (TextView) view.findViewById(R.id.record_item_over_time);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        HttpImgLoader.getHttpImgLoader().initImg(list.get(i).mImg,holder.mItemImg);
        holder.mItemGoodsName.setText(list.get(i).mGoodsName);
        holder.mItemNum.setText(list.get(i).mNum);
        holder.mItemName.setText(list.get(i).mUserName);
        holder.mItemUserNum.setText(list.get(i).mUserNum);
        holder.mItemLuckyNum.setText(list.get(i).mLuckyNum);
        holder.mItemTime.setText(list.get(i).mTime);
        return view;
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
