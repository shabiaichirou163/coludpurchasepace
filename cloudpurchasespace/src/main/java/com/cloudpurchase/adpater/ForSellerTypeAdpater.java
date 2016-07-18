package com.cloudpurchase.adpater;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.utils.ScreenUtils;

import org.w3c.dom.Text;

/**
 * Created by oscar on 2016/6/9.
 * 卖家区适配区
 */
public class ForSellerTypeAdpater extends BaseAdapter {
    private boolean isSelect[];

    public boolean[] getIsSelect() {
        return isSelect;
    }

    private Activity mContext;
    private String mType [];
    public ForSellerTypeAdpater(Activity context,String type[]){
        mContext=context;
        mType=new String[type.length];
        mType=type;
        isSelect=new boolean[type.length];
        isSelect[0]=true;
    }
    @Override
    public int getCount() {
        return null!=mType?mType.length:0;
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.adpater_seller_goods_type_item,null);
            viewHolder.goodsType= (TextView) convertView.findViewById(R.id.Seller_goods_type_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.goodsType.setText(mType[position]);
        if (getIsSelect()[position]){
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.backgroud_color_white));
            viewHolder.goodsType.setTextColor(mContext.getResources().getColor(R.color.text_color_red));
        }else {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.unfocused));
            viewHolder.goodsType.setTextColor(mContext.getResources().getColor(R.color.text_color_block));
        }
        return convertView;
    }
    class ViewHolder{
        TextView goodsType;
    }
}
