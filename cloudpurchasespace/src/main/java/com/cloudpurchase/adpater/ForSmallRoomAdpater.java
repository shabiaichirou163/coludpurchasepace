package com.cloudpurchase.adpater;

import android.app.Activity;
import android.content.Intent;
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

import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.cloudpurchase.GoodsDetailsActivity;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.cloudpurchase.SmallRoomGoodsDetailsActivity;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.veiw.LootAlertDialog;

import java.util.List;

/**
 * Created by oscar on 2016/6/6.
 * 推荐房间适配器
 */
public class ForSmallRoomAdpater extends BaseAdapter {
    private Activity mContext;
    private List<GoodsDetails> mData;
    LootAlertDialog dialog;
    public ForSmallRoomAdpater(Activity context, List<GoodsDetails> data){
        mContext=context;
        mData=data;
        dialog=new LootAlertDialog(mContext);
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.adpater_small_room_goods,null);
            voldHoder.smallRoomGoodsImg= (ImageView) convertView.findViewById(R.id.small_room_img);
            voldHoder.smallRoomPriceTxt= (TextView) convertView.findViewById(R.id.small_room_text_price);
            voldHoder.smallRoomProgressBar= (ProgressBar) convertView.findViewById(R.id.small_room_prgress);
            voldHoder.smallRoomParticipateNum= (TextView) convertView.findViewById(R.id.small_room_text_participate);
            voldHoder.smallRoomTotalNum= (TextView) convertView.findViewById(R.id.small_room_text_total);
            voldHoder.smallRoomRemainingNum= (TextView) convertView.findViewById(R.id.small_room_text_remaining);
            voldHoder.smallRoomCartBtn= (ImageButton) convertView.findViewById(R.id.small_room_goods_cart_img);
            voldHoder.smallRoomDescription= (TextView) convertView.findViewById(R.id.small_room_text_description);
            voldHoder.whoRoom= (TextView) convertView.findViewById(R.id.small_who_room_txt);
            convertView.setTag(voldHoder);
        }else{
            voldHoder= (ViewHolder) convertView.getTag();
        }
        GoodsDetails goodsDetails=mData.get(position);
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
                Toast.makeText(mContext, "开始夺宝" + position, Toast.LENGTH_SHORT).show();
                dialog.displayDialog();
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,SmallRoomGoodsDetailsActivity.class);
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
}
