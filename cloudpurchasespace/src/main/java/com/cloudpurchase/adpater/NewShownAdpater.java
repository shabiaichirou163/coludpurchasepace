package com.cloudpurchase.adpater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.cloudpurchase.utils.CountDownUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by oscar on 2016/6/7.
 * 最新揭晓适配器
 */
public class NewShownAdpater extends BaseAdapter {
    private Activity mContext;
    private List<GoodsDetails> mData;
    HashMap<TextView,CountDownUtil> leftTimeMap;
    public NewShownAdpater(Activity context, List<GoodsDetails> data){
        mContext=context;
        mData=data;
        leftTimeMap=new HashMap<TextView,CountDownUtil>();
    }
    @Override
    public int getCount() {
        return null!=mData?mData.size():0;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    ViewHolder viewHolder;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null==convertView){
            viewHolder=new ViewHolder();
            convertView=LayoutInflater.from(mContext).inflate(R.layout.adpater_new_shown_item,null);
            viewHolder.img= (ImageView) convertView.findViewById(R.id.new_shown_item_img);
            viewHolder.periodInfo=(TextView)convertView.findViewById(R.id.new_shown_item_txt_goodsinfo);
            viewHolder.mShowTime=(TextView)convertView.findViewById(R.id.new_shown_item_time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.img.setImageResource(R.mipmap.ip);
        viewHolder.periodInfo.setText(mData.get(position).getDescription());

        //获取控件对应的倒计时控件是否存在,存在就取消,解决时间重叠问题
        CountDownUtil tc = leftTimeMap.get(viewHolder.mShowTime);
        if (tc != null) {
            tc.cancel();
            tc = null;
        }

        //实例化倒计时类
        CountDownUtil cdu = new CountDownUtil(1000*60*6, 1,
                viewHolder.mShowTime);
        //开启倒计时
        cdu.start();

        //[醒目]此处需要map集合将控件和倒计时类关联起来,就是这里
        leftTimeMap.put(viewHolder.mShowTime, cdu);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GoodsDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("flag","newShown");
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
         *最新揭晓数据
         */
        ImageView img;
        TextView periodInfo,mShowTime;

    }

    /**
     * 下拉刷新添加数据
     * @param data
     */
    public void footerRefreshAddGoods(List<GoodsDetails> data){
        mData.addAll(data);
        notifyDataSetChanged();
    }
    public void headRefreshGoods(List<GoodsDetails> data){
        mData=data;
        notifyDataSetChanged();
    }



    //作为严谨的码工,当然要善始善终
    public void cancelAllTimers() {
        Set<Map.Entry<TextView, CountDownUtil>> s = leftTimeMap.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            try {
                Map.Entry pairs = (Map.Entry) it.next();
                CountDownUtil cdt = (CountDownUtil) pairs.getValue();
                cdt.cancel();
                cdt = null;
            } catch (Exception e) {
            }
        }
        it = null;
        s = null;
        leftTimeMap.clear();
    }
}
