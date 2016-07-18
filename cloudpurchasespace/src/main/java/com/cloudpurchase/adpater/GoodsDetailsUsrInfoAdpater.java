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
import com.cloudpurchase.entity.UserInfo;
import com.cloudpurchase.utils.InsertSuccessful;
import com.cloudpurchase.veiw.RoundImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/6/4.
 * 商品详情用户信息适配器
 */
public class GoodsDetailsUsrInfoAdpater extends BaseAdapter {
    private Activity mContext;
    private List<UserInfo> mData;
    private LayoutInflater mLayoutInflater;

    public GoodsDetailsUsrInfoAdpater(Activity context, List<UserInfo> data) {
        mContext = context;
        mData = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return null != mData ? mData.size() : 0;
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
        if (convertView == null) {
            voldHoder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.adpater_goods_details_userinfo, null);
            voldHoder.userImg= (RoundImageView) convertView.findViewById(R.id.adpater_goods_details_user_img);
            voldHoder.userNameTxt= (TextView) convertView.findViewById(R.id.adpater_goods_details_user_name_txt);
            voldHoder.addrTxt= (TextView) convertView.findViewById(R.id.adpater_goods_details_user_addr);
            voldHoder.countTxt= (TextView) convertView.findViewById(R.id.adpater_goods_details_user_count);
            voldHoder.dataAndTimeTxt= (TextView) convertView.findViewById(R.id.adpater_goods_details_data_time);
            convertView.setTag(voldHoder);
        } else {
            voldHoder = (ViewHolder) convertView.getTag();
        }
        UserInfo userInfo=mData.get(position);

        voldHoder.userNameTxt.setText(userInfo.getUserName());
        voldHoder.addrTxt.setText("("+userInfo.getCity()+" "+userInfo.getIpAddr()+")");
        voldHoder.countTxt.setText(userInfo.getCount()+"");
        voldHoder.dataAndTimeTxt.setText(userInfo.getDataTime());
        return convertView;
    }

    public void footerRefreshAddGoods(List<UserInfo> data){
        mData.addAll(data);
        notifyDataSetChanged();
    }
    public void headRefreshGoods(List<UserInfo> data){
        mData=data;
        notifyDataSetChanged();
    }

    class ViewHolder{
          RoundImageView userImg;
          TextView userNameTxt;
          TextView addrTxt;
          TextView countTxt;
          TextView dataAndTimeTxt;
    }
}
