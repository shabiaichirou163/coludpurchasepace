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
public class GoodsDetailsAdpater extends BaseAdapter {
    private Activity mContext;
    private List<GoodsDetails> mData;
    private LayoutInflater  mLayoutInflater;
    HashMap<ImageButton,ImageView> mHashMap;//绑定Button和ImageView
    HashMap<String,String> mDataMap;//加入购物车参数
    private JsonReslove mReslove;
    public GoodsDetailsAdpater(Activity context, List<GoodsDetails> data){
        mContext=context;
        mData=data;
        mLayoutInflater=LayoutInflater.from(context);
        mHashMap=new HashMap<ImageButton,ImageView>();
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
        if (convertView==null){
            voldHoder=new ViewHolder();
            convertView= mLayoutInflater.inflate(R.layout.adpater_goods,null);
            voldHoder.goodsImg= (ImageView) convertView.findViewById(R.id.goods_img);
            voldHoder.priceTxt= (TextView) convertView.findViewById(R.id.total_price_txt);
            voldHoder.progressBar= (ProgressBar) convertView.findViewById(R.id.prgress);
            voldHoder.participateNum= (TextView) convertView.findViewById(R.id.participate_num);
            voldHoder.totalNum= (TextView) convertView.findViewById(R.id.total_num);
            voldHoder.remainingNum= (TextView) convertView.findViewById(R.id.remaining_num);
            voldHoder.cartBtn= (ImageButton) convertView.findViewById(R.id.goods_cart_img);
            convertView.setTag(voldHoder);
        }else {
            voldHoder= (ViewHolder) convertView.getTag();
        }
        final GoodsDetails goodsDetails=mData.get(position);
        mHashMap.put(voldHoder.cartBtn,voldHoder.goodsImg);
        voldHoder.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.USER_IS_LOGIN_FLAG) {
                    //添加到购物车
                    addToShoppingCart(goodsDetails);
                    //获取商品坐标以及复制imageView
                    if (insertSuccessful != null && mHashMap != null) {
                        ImageView img = mHashMap.get(v);
                        int startLoaction[] = new int[2];
                        img.getLocationInWindow(startLoaction);
                        Drawable drawable = img.getDrawable();
                        insertSuccessful.InsertSuccessful(drawable, startLoaction, "other");
                    }
                }else{
                    Toast.makeText(mContext,"请先您登录后在加入购物车,谢谢",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });

        HttpImgLoader.getHttpImgLoader().initImgNoBitmap(goodsDetails.getIcon(),voldHoder.goodsImg);
       // voldHoder.goodsImg.setScaleType(ImageView.ScaleType.FIT_XY);
        voldHoder.priceTxt.setText("价格：¥" + goodsDetails.getPrice());
        voldHoder.participateNum.setText(goodsDetails.getParticipate()+"");
        voldHoder.totalNum.setText(goodsDetails.getTotal()+"");
        voldHoder.remainingNum.setText(goodsDetails.getRemaining() + "");
        voldHoder.progressBar.setMax(goodsDetails.getTotal());
        voldHoder.progressBar.setProgress(goodsDetails.getParticipate());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,GoodsDetailsActivity.class);
                Bundle bundle = new Bundle();
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
          ImageView goodsImg;
          TextView priceTxt;
          ProgressBar progressBar;
          TextView participateNum;
          TextView totalNum;
          TextView remainingNum;
          ImageButton cartBtn;
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
        mDataMap.put("token",MyApplication.USER_TOKEN);
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
