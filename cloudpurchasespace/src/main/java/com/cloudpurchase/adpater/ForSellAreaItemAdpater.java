package com.cloudpurchase.adpater;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.cloudpurchase.db.DBWrapper;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.entity.RequestInfo;
import com.cloudpurchase.net.HttpRequest;
import com.cloudpurchase.utils.Constants;
import com.cloudpurchase.utils.InsertSuccessful;
import com.cloudpurchase.utils.JsonReslove;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.utils.RequestResultIn;

import java.util.HashMap;
import java.util.List;

/**
 * Created by oscar on 2016/6/6.
 */
public class ForSellAreaItemAdpater extends BaseAdapter {
    private Activity mContext;
    private List<GoodsDetails> mData;
    private HashMap<ImageButton,ImageView> mMap;
    private HashMap<String,String> mDataMap;
    private JsonReslove mReslove;
    public ForSellAreaItemAdpater(Activity context, List<GoodsDetails> data){
        mContext=context;
        mData=data;
        mMap=new HashMap<ImageButton,ImageView>();
        mDataMap=new HashMap<String,String>();
        mReslove=new JsonReslove(mContext);
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.adpater_sell_area_goods_info_item,null);
            voldHoder.smallRoomGoodsImg= (ImageView) convertView.findViewById(R.id.seller_area_item_img);
            voldHoder.smallRoomPriceTxt= (TextView) convertView.findViewById(R.id.seller_area_item_text_price);
            voldHoder.smallRoomProgressBar= (ProgressBar) convertView.findViewById(R.id.seller_area_item_prgress);
            voldHoder.smallRoomParticipateNum= (TextView) convertView.findViewById(R.id.seller_area_item_text_participate);
            voldHoder.smallRoomTotalNum= (TextView) convertView.findViewById(R.id.seller_area_item_text_total);
            voldHoder.smallRoomRemainingNum= (TextView) convertView.findViewById(R.id.seller_area_item_text_remaining);
            voldHoder.smallRoomCartBtn= (ImageButton) convertView.findViewById(R.id.seller_area_item_goods_cart_img);
            voldHoder.smallRoomDescription= (TextView) convertView.findViewById(R.id.seller_area_item_text_description);
            voldHoder.whoRoom= (TextView) convertView.findViewById(R.id.seller_area_item_who_room_txt);
            convertView.setTag(voldHoder);
        }else{
            voldHoder= (ViewHolder) convertView.getTag();
        }
        final GoodsDetails goodsDetails=mData.get(position);
        mMap.put(voldHoder.smallRoomCartBtn,voldHoder.smallRoomGoodsImg);
        voldHoder.smallRoomGoodsImg.setImageResource(R.mipmap.iphone);
        voldHoder.smallRoomPriceTxt.setText(goodsDetails.getPrice() + "");
        voldHoder.smallRoomParticipateNum.setText(goodsDetails.getParticipate()+"");
        voldHoder.smallRoomTotalNum.setText(goodsDetails.getTotal() + "");
        voldHoder.smallRoomRemainingNum.setText(goodsDetails.getRemaining() + "");
        voldHoder.smallRoomDescription.setText(goodsDetails.getDescription());
        voldHoder.smallRoomProgressBar.setMax(goodsDetails.getTotal());
        voldHoder.smallRoomProgressBar.setProgress(goodsDetails.getParticipate());
        voldHoder.whoRoom.setText("小小的老虎");
        voldHoder.smallRoomCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (MyApplication.USER_IS_LOGIN_FLAG) {
                   addToShoppingCart(goodsDetails);
                   if (insertSuccessful != null&&mMap!=null) {
                       ImageView img = mMap.get(v);
                       int startLoaction[] = new int[2];
                       img.getLocationInWindow(startLoaction);
                       Drawable drawable = img.getDrawable();
                       insertSuccessful.InsertSuccessful(drawable, startLoaction, "seller");
                   }
               }else{
                   Toast.makeText(mContext,"请先您登录后在加入购物车,谢谢",Toast.LENGTH_SHORT).show();
                   Intent intent=new Intent(mContext, LoginActivity.class);
                   mContext.startActivity(intent);
               }
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,GoodsDetailsActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("flag","onefragment");
                bundle.putString("activityId",goodsDetails.getActivityId());
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
        mData=data;
        notifyDataSetChanged();
    }
    class ViewHolder{
        ImageView smallRoomGoodsImg;
        TextView smallRoomPriceTxt;
        ProgressBar smallRoomProgressBar;
        TextView smallRoomParticipateNum;
        TextView smallRoomTotalNum;
        TextView smallRoomRemainingNum;
        ImageButton smallRoomCartBtn;
        TextView smallRoomDescription;
        TextView whoRoom;
    }
    /**
     * 获取insertSuccessful对象
     */
    private InsertSuccessful insertSuccessful;
    public void getInsertSuccessful(InsertSuccessful insertSuccessful){
        this.insertSuccessful=insertSuccessful;
    }


    /**
     * 加入购物车网络请求
     *
     */
    public void addToShoppingCart(GoodsDetails goods){
        if (mDataMap!=null){
            mDataMap.clear();
        }
        mDataMap.put("memberId",7+"");
        mDataMap.put("activityId",goods.getActivityId());
        mDataMap.put("goodsnumber", 1 + "");
        mDataMap.put("token", MyApplication.USER_TOKEN);
        HttpRequest.getHttpRequest().requestPOST(Constants.GOODS_ADD_SHOPPING_CART,
                null, mDataMap, new RequestResultIn() {
                    @Override
                    public void requstSuccful(String result) {
                        LogUtils.e(result);//测试看结果
                        RequestInfo info=mReslove.resloverIsSuff(result);
                        if (info!=null&&insertSuccessful!=null&&info.getCode()==0){
                            //添加购物车ok，回调
                            insertSuccessful.InsertSuccessful(null,null,null);
                        }
                    }
                    @Override
                    public void requstError(VolleyError error) {
                        LogUtils.e(error.getClass().getSimpleName());//测试看结果
                    }
                });
    }
}
