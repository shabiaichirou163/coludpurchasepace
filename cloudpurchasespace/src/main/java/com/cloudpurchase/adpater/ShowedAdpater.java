package com.cloudpurchase.adpater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudpurchase.cloudpurchase.GoodsDetailsActivity;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.GoodsDetails;

import java.util.List;

/**
 * Created by oscar on 2016/6/7.
 * 已经揭晓界面
 */
public class ShowedAdpater extends BaseAdapter {
    private Activity mContext;
    private List<GoodsDetails> mData;
    public ShowedAdpater(Activity context, List<GoodsDetails> data){
        mContext=context;
        mData=data;

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
    ViewHolder viewHolder;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null==convertView){
            viewHolder=new ViewHolder();
            convertView=LayoutInflater.from(mContext).inflate(R.layout.adpater_showned_item,null);
            viewHolder.showedImg= (ImageView) convertView.findViewById(R.id.showned_item_img);
            viewHolder.showedDescription= (TextView) convertView.findViewById(R.id.showned_item_txt_goodsinfo);
            viewHolder.showedDataTime= (TextView) convertView.findViewById(R.id.showned_data_num);
            viewHolder.showedUser= (TextView) convertView.findViewById(R.id.showned_user);
            viewHolder.showedTotalNum= (TextView) convertView.findViewById(R.id.showned_use_num);
            viewHolder.showedLuckNum= (TextView) convertView.findViewById(R.id.showned_luck_num);
            viewHolder.showedTime= (TextView) convertView.findViewById(R.id.showned_time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.showedImg.setImageResource(R.mipmap.ip);
        viewHolder.showedDescription.setText(mData.get(position).getDescription());
        viewHolder.showedDataTime.setText("30601665");
        viewHolder.showedUser.setText("小小老虎");
        viewHolder.showedTotalNum.setText("4399");
        viewHolder.showedLuckNum.setText("10003167");
        viewHolder.showedTime.setText("今天13:08");
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GoodsDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("flag", "showned");
                bundle.putSerializable("goodsInfo", mData.get(position));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                Toast.makeText(mContext, "position" + position, Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
    class ViewHolder{
        /*
        已经揭晓数据
         */
        ImageView showedImg;
        TextView showedDescription,showedDataTime,showedUser,showedTotalNum,showedLuckNum,showedTime;
    }
    public void footerRefreshAddGoods(List<GoodsDetails> data){
        mData.addAll(data);
        notifyDataSetChanged();
    }
    public void headRefreshGoods(List<GoodsDetails> data){
        mData=data;
        notifyDataSetChanged();
    }
}
