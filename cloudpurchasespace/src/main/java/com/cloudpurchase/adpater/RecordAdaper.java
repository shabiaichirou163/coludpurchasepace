package com.cloudpurchase.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.RecordUnderwayEntity;
import com.cloudpurchase.net.HttpImgLoader;
import com.cloudpurchase.utils.LogUtils;

import java.util.List;

/**
 * 云购记录适配器
 *
 */
public class RecordAdaper extends BaseAdapter{

    private List<RecordUnderwayEntity> list;
    private Context context;
    private AddBtn call;

    public RecordAdaper(Context context , List<RecordUnderwayEntity> list){
        this.context = context;
        this.list = list;
    }

    public void setCall(AddBtn call){
        this.call = call;
    }

    public interface AddBtn{
        public void Add();
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

    int prgNum;
    int num;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_record_underway,null);
            holder.mItemGoodsName = (TextView) convertView.findViewById(R.id.record_item_goodsName);
            holder.mItemImg = (ImageView) convertView.findViewById(R.id.record_item_img);
            holder.mItemNum = (TextView) convertView.findViewById(R.id.record_item_num);
            holder.mItemPrgNum = (TextView) convertView.findViewById(R.id.record_item_prgNum);
            holder.mItemProgress = (ProgressBar) convertView.findViewById(R.id.record_item_prgress);
            holder.mItemmPrompt = (TextView) convertView.findViewById(R.id.record_item_prompt);
            holder.mItemAdd = (ImageView) convertView.findViewById(R.id.record_item_add);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        HttpImgLoader.getHttpImgLoader().initImg(list.get(position).getmImg(),holder.mItemImg);
        holder.mItemGoodsName.setText(list.get(position).getmGoodsName());
        holder.mItemmPrompt.setText(list.get(position).getmPrompt());
        holder.mItemPrgNum.setText(list.get(position).getmPrgNum());
        holder.mItemNum.setText(list.get(position).getmNum());
        prgNum = Integer.parseInt((String) holder.mItemPrgNum.getText());//获取进度条内容，转为Int值
        num = Integer.parseInt((String) holder.mItemNum.getText());//获取总需量，转为Int值
        holder.mItemProgress.setMax(num);
        holder.mItemProgress.setProgress(prgNum);
        holder.mItemPrgNum.setText(prgNum+"");
        holder.mItemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //老板要求点击追加按钮后跳转到购物车

                call.Add();
              /*  int newPrgNum = Integer.parseInt(list.get(position).getmPrgNum());
                int newNum = Integer.parseInt(list.get(position).getmNum());
                newPrgNum++;
                newNum--;
                list.get(position).setmPrgNum(newPrgNum+"");
                list.get(position).setmNum(newNum+"");
                holder.mItemNum.setText(newNum+"");
                holder.mItemPrgNum.setText(newPrgNum+"");
                holder.mItemProgress.setProgress(newPrgNum);
                LogUtils.e("newPrgNum=   "+newPrgNum+""+"newNum=  "+newNum+"");
                notifyDataSetChanged();*/
            }
        });
        return convertView;
    }
    private class ViewHolder{
        ImageView mItemImg;//商品图片
        TextView mItemGoodsName;//商品描述
        TextView mItemNum;//总需量
        TextView mItemPrgNum;//进度
        ProgressBar mItemProgress;//进度条
        TextView mItemmPrompt;//提示信息
        ImageView mItemAdd;//追加按钮
    }
}