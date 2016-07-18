package com.cloudpurchase.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.NoticeEntity;

import java.util.List;

/**
 * 通知界面适配器
 */

public class NoticeAdapter extends BaseAdapter{

    private List<NoticeEntity> list;
    private Context context;

    public NoticeAdapter(Context context,List<NoticeEntity> list){
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
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_notice,null);
            holder.mItem_title = (TextView) convertView.findViewById(R.id.notice_item_title);
            holder.mItem_time = (TextView) convertView.findViewById(R.id.notice_item_time);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mItem_title.setText(list.get(position).mTitle);
        holder.mItem_time.setText(list.get(position).mTime);
        return convertView;
    }
    private class ViewHolder{
        TextView mItem_title;//标题
        TextView mItem_time;//时间
    }

}
