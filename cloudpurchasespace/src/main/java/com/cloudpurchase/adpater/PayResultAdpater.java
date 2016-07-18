package com.cloudpurchase.adpater;

import android.app.Activity;
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

import com.cloudpurchase.cloudpurchase.GoodsDetailsActivity;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.db.DBWrapper;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.utils.InsertSuccessful;
import com.cloudpurchase.utils.LogUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/6/4.
 * 支付成功适配
 */
public class PayResultAdpater extends BaseAdapter {
    private Activity mContext;
    private List<GoodsDetails> mData;
    private LayoutInflater  mLayoutInflater;
    public PayResultAdpater(Activity context, List<GoodsDetails> data){
        mContext=context;
        mData=data;
        mLayoutInflater=LayoutInflater.from(mContext);
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
        if (convertView==null){
            voldHoder=new ViewHolder();
            convertView= mLayoutInflater.inflate(R.layout.adpater_pay_result,null);
            voldHoder.goodsImg= (ImageView) convertView.findViewById(R.id.adpaer_pay_result_img);
            voldHoder.typeTxt= (TextView) convertView.findViewById(R.id.adpaer_pay_result_type);
            voldHoder.personNumTxt= (TextView) convertView.findViewById(R.id.adpaer_pay_result_personnum);
            voldHoder.dataTxt= (TextView) convertView.findViewById(R.id.adpaer_pay_result_data);
            convertView.setTag(voldHoder);
        }else {
            voldHoder= (ViewHolder) convertView.getTag();
        }
        GoodsDetails goodsDetails=mData.get(position);
        voldHoder.goodsImg.setImageResource(R.mipmap.iphone);
        voldHoder.typeTxt.setText(goodsDetails.getGoodsName());
        voldHoder.personNumTxt.setText("参与人次:" + goodsDetails.getPersonNum() + "人次");
        voldHoder.dataTxt.setText("参与期数:1期");
        return convertView;
    }
    class ViewHolder{
          ImageView goodsImg;//购买商品图片
          TextView typeTxt;//购买商品类型描述
          TextView personNumTxt;//购买商品人次
          TextView dataTxt;//购买商品的期数
    }
}
