package com.cloudpurchase.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.UserTaskEntity;

import java.util.List;

/**
 * 任务中心适配器
 */
public class UserTaskAdapter extends BaseAdapter{

    private List<UserTaskEntity> list;
    private Context context;

    public UserTaskAdapter(Context context , List<UserTaskEntity> list){
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
            convertView = View.inflate(context, R.layout.item_user_task,null);
            holder.mItemImg = (ImageView) convertView.findViewById(R.id.userTask_item_taskImg);
            holder.mItemTitle = (TextView) convertView.findViewById(R.id.userTask_title);
            holder.mItemNum = (TextView) convertView.findViewById(R.id.userTask_item_num);
            holder.mItemNum2 = (TextView) convertView.findViewById(R.id.userTask_item_num2);
            holder.mItemFlagImg = (ImageView) convertView.findViewById(R.id.userTask_item_flagImg);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mItemImg.setImageResource(list.get(position).taskImg);
        holder.mItemTitle.setText(list.get(position).taskTitle);
        holder.mItemNum.setText(list.get(position).num);
        holder.mItemNum2.setText(list.get(position).num);
        if(list.get(position).flag){
            holder.mItemFlagImg.setImageResource(R.mipmap.a_rw_no_2x);
        }else{
            holder.mItemFlagImg.setImageResource(R.mipmap.a_rw_f_2x);
        }
        return convertView;
    }

    private class ViewHolder{
        ImageView mItemImg;//任务图标
        TextView mItemTitle;//任务名称
        TextView mItemNum;//任务奖励积分
        TextView mItemNum2;
        ImageView mItemFlagImg;//任务是否已完成标志图
    }
}
