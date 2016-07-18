package com.cloudpurchase.adpater;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudpurchase.cloudpurchase.GoodsDetailsActivity;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.db.DBWrapper;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.utils.InsertSuccessful;
import com.cloudpurchase.utils.LogUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by oscar on 2016/6/6.
 */
public class ForSellAreaItemAdpater extends BaseAdapter {
    private Activity mContext;
    private List<GoodsDetails> mData;
    private DBWrapper mDBWrapper;
    public ForSellAreaItemAdpater(Activity context, List<GoodsDetails> data){
        mContext=context;
        mData=data;
        mDBWrapper=new DBWrapper(context);
    }
    @Override
    public int getCount() {
        return null!=mData?mData.size():0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    ViewHolder voldHoder;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null==convertView){
            voldHoder=new ViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.adpater_sell_area_goods_info_item,null);
            voldHoder.smallRoomGoodsImg= (ImageView) convertView.findViewById(R.id.seller_area_item_img);
            voldHoder.smallRoomPriceTxt= (TextView) convertView.findViewById(R.id.seller_area_item_text_price);
            voldHoder.smallRoomProgressBar= (ProgressBar) convertView.findViewById(R.id.seller_area_item_prgress);
            voldHoder.smallRoomParticipateNum= (TextView) convertView.findViewById(R.id.seller_area_item_text_participate);
            voldHoder.smallRoomTotalNum= (TextView) convertView.findViewById(R.id.seller_area_item_text_total);
            voldHoder.smallRoomRemainingNum= (TextView) convertView.findViewById(R.id.seller_area_item_text_remaining);
            voldHoder.smallRoomCartBtn= (ImageButton) convertView.findViewById(R.id.seller_area_item_goods_cart_img);
            voldHoder.smallRoomDescription= (TextView) convertView.findViewById(R.id.seller_area_item_text_description);
            voldHoder.whoRoom= (TextView) convertView.findViewById(R.id.seller_area_item_who_room_txt);
            convertView.setTag(voldHoder);
        }else{
            voldHoder= (ViewHolder) convertView.getTag();
        }
        final GoodsDetails goodsDetails=mData.get(position);
        voldHoder.smallRoomGoodsImg.setImageResource(R.mipmap.iphone);
        voldHoder.smallRoomPriceTxt.setText(goodsDetails.getPrice() + "");
        voldHoder.smallRoomParticipateNum.setText(goodsDetails.getParticipate()+"");
        voldHoder.smallRoomTotalNum.setText(goodsDetails.getTotal() + "");
        voldHoder.smallRoomRemainingNum.setText(goodsDetails.getRemaining() + "");
        voldHoder.smallRoomDescription.setText(goodsDetails.getDescription());
        voldHoder.smallRoomProgressBar.setMax(goodsDetails.getTotal());
        voldHoder.smallRoomProgressBar.setProgress(goodsDetails.getParticipate());
        voldHoder.whoRoom.setText("小小的老虎");
        voldHoder.smallRoomCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isHas = false;//标示数据库商品Id 数据库存在此id 返回true
                List<GoodsDetails> data = mDBWrapper.selectData();
                if (data.size() > 0) {
                    for (GoodsDetails good : data) {
                        if (good.getGoodsId() == goodsDetails.getGoodsId()) {
                            isHas = true;
                        }
                    }
                }
                if (!isHas || data.size() == 0) {
                    mDBWrapper.insertData(goodsDetails);
                }
                if (insertSuccessful != null) {
                    int startLoaction[] = new int[2];
                    v.getLocationInWindow(startLoaction);
                    startLoaction[0]=startLoaction[0]-voldHoder.smallRoomProgressBar.getMeasuredWidth()-
                            voldHoder.smallRoomGoodsImg.getMeasuredHeight()-voldHoder.smallRoomPriceTxt.getMeasuredHeight();
                    startLoaction[1]=startLoaction[1]-voldHoder.smallRoomDescription.getMeasuredHeight()-voldHoder.smallRoomPriceTxt.getMeasuredHeight();
                    Drawable drawable = voldHoder.smallRoomGoodsImg.getDrawable();
                    insertSuccessful.InsertSuccessful(drawable, startLoaction,"seller");
                }
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,GoodsDetailsActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("goodsInfo", mData.get(position));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                Toast.makeText(mContext, "position" + position, Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
    public void footerRefreshAddGoods(List<GoodsDetails> data){
        mData.addAll(data);
        notifyDataSetChanged();
    }
    public void headRefreshGoods(List<GoodsDetails> data){
        mData=data;
        notifyDataSetChanged();
    }
    class ViewHolder{
        ImageView smallRoomGoodsImg;
        TextView smallRoomPriceTxt;
        ProgressBar smallRoomProgressBar;
        TextView smallRoomParticipateNum;
        TextView smallRoomTotalNum;
        TextView smallRoomRemainingNum;
        ImageButton smallRoomCartBtn;
        TextView smallRoomDescription;
        TextView whoRoom;
    }
    /**
     * 获取insertSuccessful对象
     */
    private InsertSuccessful insertSuccessful;
    public void getInsertSuccessful(InsertSuccessful insertSuccessful){
        this.insertSuccessful=insertSuccessful;
    }
}
