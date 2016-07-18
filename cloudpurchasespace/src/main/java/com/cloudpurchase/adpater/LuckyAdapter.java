package com.cloudpurchase.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.LuckyEntity;
import com.cloudpurchase.net.HttpImgLoader;

import java.util.List;

/**
 *
 * 户中心——》幸运记录界面适配器
 *
 */
public class LuckyAdapter extends BaseAdapter{

    private Context context;
    private List<LuckyEntity> list;
    private OnclickCall call;
    public void setCall(OnclickCall call){
        this.call=call;
    }


    public LuckyAdapter(Context context,List<LuckyEntity> list){
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
            view = View.inflate(context, R.layout.item_lucky,null);
            holder.mItemImg = (ImageView) view.findViewById(R.id.lucky_item_img);
            holder.mItemTitle = (TextView) view.findViewById(R.id.lucky_item_title);
            holder.mItemTimeNum = (TextView) view.findViewById(R.id.lucky_item_timeNum);
            holder.mItemUserNum = (TextView) view.findViewById(R.id.lucky_item_userNum);
            holder.mItemLuckyNum = (TextView) view.findViewById(R.id.lucky_item_luckyNum);
            holder.mItemJoinNum = (TextView) view.findViewById(R.id.lucky_item_joinNum);
            holder.mItemTime = (TextView) view.findViewById(R.id.lucky_item_time);
            holder.mItemState = (TextView) view.findViewById(R.id.lucky_item_state);
            holder.mItemShow= (LinearLayout) view.findViewById(R.id.lucky_item_show);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        HttpImgLoader.getHttpImgLoader().initImg(list.get(i).img,holder.mItemImg);
        holder.mItemTitle.setText(list.get(i).title);
        holder.mItemTimeNum.setText(list.get(i).timeNum);
        holder.mItemUserNum.setText(list.get(i).userNum);
        holder.mItemLuckyNum.setText(list.get(i).luckyNum);
        holder.mItemJoinNum.setText(list.get(i).joinNum);
        holder.mItemTime.setText(list.get(i).time);
        holder.mItemState.setText(list.get(i).state);
        holder.mItemShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call.call();
            }
        });
        return view;
    }

    public interface OnclickCall{
        public void call();
    }
    private class ViewHolder{
        ImageView mItemImg;//商品图片
        TextView mItemTitle;//商品描述
        TextView mItemTimeNum;//期号
        TextView mItemUserNum;//总需人数
        TextView mItemLuckyNum;//幸运号码
        TextView mItemJoinNum;//参与人数
        TextView mItemTime;//时间
        TextView mItemState;//状态
        LinearLayout mItemShow;//查看收获详情
    }
}
