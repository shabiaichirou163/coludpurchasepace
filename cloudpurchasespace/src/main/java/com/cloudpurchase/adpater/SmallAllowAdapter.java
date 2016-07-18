package com.cloudpurchase.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.SmallAllowEntity;
import com.cloudpurchase.net.HttpImgLoader;

import java.util.List;

/**
 *  用户中心——>小房间——》正在进行的适配器
 *
 *
 */
public class SmallAllowAdapter extends BaseAdapter{

    private List<SmallAllowEntity> list;
    private Context context;

    public SmallAllowAdapter(Context context, List<SmallAllowEntity> list){
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
        if(view == null){
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.item_small_allow,null);
            holder.mItemImg = (ImageView) view.findViewById(R.id.small_item_img);
            holder.mItemTitle = (TextView) view.findViewById(R.id.small_item_title);
            holder.mItemPrg = (ProgressBar) view.findViewById(R.id.small_item_prg);
            holder.mItemUserName = (TextView) view.findViewById(R.id.small_item_userName);
            holder.mItemMoney = (TextView) view.findViewById(R.id.small_item_money);
            holder.mItemAllNum = (TextView) view.findViewById(R.id.small_item_allNum);
            holder.mItemSurplusNum = (TextView) view.findViewById(R.id.small_item_surplusNum);
            holder.mItemOlderNum = (TextView) view.findViewById(R.id.small_item_olderNum);
            holder.mItemBtn = (ImageButton) view.findViewById(R.id.small_item_start);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        HttpImgLoader.getHttpImgLoader().initImg(list.get(i).img,holder.mItemImg);
        holder.mItemTitle.setText(list.get(i).title);
        holder.mItemUserName.setText(list.get(i).userName);
        holder.mItemOlderNum.setText(list.get(i).olderNum);
        holder.mItemSurplusNum.setText(list.get(i).surplusNum);
        holder.mItemAllNum.setText(list.get(i).allNum);
        holder.mItemPrg.setProgress(list.get(i).progress);
        holder.mItemMoney.setText(list.get(i).money);
        holder.mItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"夺宝",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private class ViewHolder{
        ImageView mItemImg;//商品图片
        TextView mItemTitle;//商品描述
        TextView mItemMoney;//商品价格
        TextView mItemUserName;//房主
        ProgressBar mItemPrg;//进度条
        TextView mItemOlderNum;//已经参与的人数
        TextView mItemAllNum;//总需要的人数
        TextView mItemSurplusNum;//剩余人数
        ImageButton mItemBtn;//夺宝 按钮
        /*
    public String img;//商品图片
    public int id;//商品ID
    public String title;//商品描述
    public String money;//商品价格
    public String userName;//房主
    public int progress;//进度
    public String olderNum;//以参与的人数
    public String allNum;//总需人数
    public String surplusNum;//剩余人数
         */
    }
}
