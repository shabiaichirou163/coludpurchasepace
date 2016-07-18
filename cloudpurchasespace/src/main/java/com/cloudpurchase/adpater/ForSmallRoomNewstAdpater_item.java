package com.cloudpurchase.adpater;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudpurchase.cloudpurchase.GoodsDetailsActivity;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.cloudpurchase.SmallRoomGoodsDetailsActivity;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.veiw.OpenRoomAlertDialog;

import java.util.List;

/**
 * Created by oscar on 2016/6/6.
 * 小房间最新商品列适配
 */
public class ForSmallRoomNewstAdpater_item extends BaseAdapter {
    private Activity mContext;
    private List<GoodsDetails> mData;
    private OpenRoomAlertDialog dialog;//开房对话框
    public ForSmallRoomNewstAdpater_item(Activity context, List<GoodsDetails> data){
        mContext=context;
        mData=data;
        dialog=new OpenRoomAlertDialog(mContext);
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.adpater_small_newest_goods_item,null);
            voldHoder.smallRoomGoodsImg= (ImageView) convertView.findViewById(R.id.small_room_item_newst_img);
            voldHoder.smallRoomtxt_goods=(TextView)convertView.findViewById(R.id.small_room_item_description);
            voldHoder.mallRoomtxt_price=(TextView)convertView.findViewById(R.id.small_room_item_price);
            voldHoder.mallRoomtxt_num=(TextView)convertView.findViewById(R.id.small_room_item_num);
            voldHoder.getSmallRoombtn=(Button)convertView.findViewById(R.id.small_room_item_btn);
            convertView.setTag(voldHoder);
        }else{
            voldHoder= (ViewHolder) convertView.getTag();
        }
        GoodsDetails goodsDetails=mData.get(position);
        voldHoder.smallRoomGoodsImg.setImageResource(R.mipmap.ipad1);
        voldHoder.smallRoomtxt_goods.setText(goodsDetails.getDescription());
        voldHoder.mallRoomtxt_price.setText("2888");
        voldHoder.mallRoomtxt_num.setText("1件");
        voldHoder.getSmallRoombtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "我要开小房间" + position, Toast.LENGTH_SHORT).show();
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
        TextView smallRoomtxt_goods,mallRoomtxt_price,mallRoomtxt_num;
        TextView getSmallRoombtn;
    }
}
