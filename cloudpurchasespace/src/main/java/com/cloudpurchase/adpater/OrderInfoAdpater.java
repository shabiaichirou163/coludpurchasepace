package com.cloudpurchase.adpater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.cloudpurchase.cloudpurchase.GoodsDetailsActivity;
import com.cloudpurchase.cloudpurchase.LoginActivity;
import com.cloudpurchase.cloudpurchase.MyApplication;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.entity.RequestInfo;
import com.cloudpurchase.net.HttpImgLoader;
import com.cloudpurchase.net.HttpRequest;
import com.cloudpurchase.utils.Constants;
import com.cloudpurchase.utils.InsertSuccessful;
import com.cloudpurchase.utils.JsonReslove;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.utils.RequestResultIn;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/6/4.
 * 商品详情适配器
 */
public class OrderInfoAdpater extends BaseAdapter {
    private Context mContext;
    private List<GoodsDetails> mData;
    private LayoutInflater  mLayoutInflater;
    public OrderInfoAdpater(Context context, List<GoodsDetails> data){
        mContext=context;
        mData=data;
        mLayoutInflater=LayoutInflater.from(context);
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
    ViewHolder viewHoder;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            viewHoder=new ViewHolder();
            convertView= mLayoutInflater.inflate(R.layout.adpater_order_info,null);
            viewHoder.goodsDescription= (TextView) convertView.findViewById(R.id.adpater_order_info_goods_info);
            viewHoder.personNum= (TextView) convertView.findViewById(R.id.adpater_order_info_goods_num);
            convertView.setTag(viewHoder);
        }else {
            viewHoder= (ViewHolder) convertView.getTag();
        }
        final GoodsDetails goodsDetails=mData.get(position);
        viewHoder.goodsDescription.setText(goodsDetails.getDescription());
        viewHoder.personNum.setText(goodsDetails.getPersonNum());
        return convertView;
    }

    class ViewHolder{
        TextView goodsDescription;
          TextView  personNum;
    }
}
