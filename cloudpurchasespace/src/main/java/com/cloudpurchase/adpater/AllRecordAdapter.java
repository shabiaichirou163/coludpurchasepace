package com.cloudpurchase.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.AllRecordEntity;
import com.cloudpurchase.net.HttpImgLoader;

import java.util.List;

/**
 * 云购记录——全部记录适配器
 *
 */
public class AllRecordAdapter extends BaseAdapter{

    private List<AllRecordEntity> list;
    private Context context;

    public AllRecordAdapter(Context context,List<AllRecordEntity> list){
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
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_all_record,null);
            holder.mItemImg = (ImageView) convertView.findViewById(R.id.record_item_all_img);
            holder.mItemTitle = (TextView) convertView.findViewById(R.id.record_item_all_title);
            holder.mItemTimeNum = (TextView) convertView.findViewById(R.id.record_item_all_timeNum);
            holder.mItemNum = (TextView) convertView.findViewById(R.id.record_item_all_num);
            holder.mItemShow = (TextView) convertView.findViewById(R.id.record_item_all_show);
            holder.mItemUserName = (TextView) convertView.findViewById(R.id.record_item_all_userName);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        HttpImgLoader.getHttpImgLoader().initImg(list.get(position).mImg,holder.mItemImg);
        holder.mItemTitle.setText(list.get(position).mTitle);
        holder.mItemTimeNum.setText(list.get(position).mTimeNum);
        holder.mItemNum.setText(list.get(position).mNum);
        holder.mItemUserName.setText(list.get(position).mUserName);
        holder.mItemShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"查看详情",Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
    private class ViewHolder{
        ImageView mItemImg;//商品图片
        TextView mItemTitle;//商品描述
        TextView mItemTimeNum;//商品期号
        TextView mItemNum;//我参与的次数
        TextView mItemShow;//查看详情按钮
        TextView mItemUserName;//中奖者昵称
    }

}
