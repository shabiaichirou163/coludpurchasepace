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

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/6/4.
 * 商品详情适配器
 */
public class SellerShopGoodsAdpater extends BaseAdapter {
    private Activity mContext;
    private List<GoodsDetails> mData;
    private DBWrapper mDBWrapper;
    public SellerShopGoodsAdpater(Activity context, List<GoodsDetails> data){
        mContext=context;
        mData=data;
        mDBWrapper=new DBWrapper(context);
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.adpater_seller_shop_goods_info,null);
            voldHoder.goodsImg= (ImageView) convertView.findViewById(R.id.adpater_seller_shop_goods_img);
            voldHoder.priceTxt= (TextView) convertView.findViewById(R.id.adpater_seller_shop_total_price_txt);
            voldHoder.progressTxt= (TextView) convertView.findViewById(R.id.adpater_seller_shop_progress_txt);
            voldHoder.progressBar= (ProgressBar) convertView.findViewById(R.id.adpater_seller_shop_prgress);
            voldHoder.participateNum= (TextView) convertView.findViewById(R.id.adpater_seller_shop_participate_num);
            voldHoder.totalNum= (TextView) convertView.findViewById(R.id.adpater_seller_shop_total_num);
            voldHoder.remainingNum= (TextView) convertView.findViewById(R.id.adpater_seller_shop_remaining_num);
            convertView.setTag(voldHoder);
        }else {
            voldHoder= (ViewHolder) convertView.getTag();
        }
        final GoodsDetails goodsDetails=mData.get(position);
        voldHoder.goodsImg.setImageResource(R.mipmap.ip1);
        voldHoder.goodsImg.setScaleType(ImageView.ScaleType.FIT_XY);
        voldHoder.priceTxt.setText(goodsDetails.getPrice() + "");
        voldHoder.participateNum.setText(goodsDetails.getParticipate()+"");
        voldHoder.totalNum.setText(goodsDetails.getTotal()+"");
        voldHoder.remainingNum.setText(goodsDetails.getRemaining() + "");
        voldHoder.progressBar.setMax(goodsDetails.getTotal());
        voldHoder.progressBar.setProgress(goodsDetails.getParticipate());
        voldHoder.progressTxt.setText((int) ((double) goodsDetails.getParticipate() / goodsDetails.getTotal() * 100) + "%");
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,GoodsDetailsActivity.class);
                Bundle bundle = new Bundle();
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
        notifyDataSetChanged();
        mData=data;
    }

    class ViewHolder{
        ImageView goodsImg;
        TextView progressTxt;
        TextView priceTxt;
        ProgressBar progressBar;
        TextView participateNum;
        TextView totalNum;
        TextView remainingNum;
    }

}
