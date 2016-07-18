package com.cloudpurchase.adpater;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.utils.BitmapUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by oscar on 2016/6/7.
 */
public class ShowListAdpater extends BaseAdapter {
    private Activity mContext;
    private List<GoodsDetails> mData;
    public ShowListAdpater(Activity context, List<GoodsDetails> data){
        mContext=context;
        mData=data;
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
    ViewHolder viewHolder;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null==convertView){
            viewHolder=new ViewHolder();
            convertView=LayoutInflater.from(mContext).inflate(R.layout.adpater_show_list_item,null);
            viewHolder.userImg= (ImageView) convertView.findViewById(R.id.show_list_adpater_img);
            viewHolder.userName= (TextView) convertView.findViewById(R.id.show_list_adpater_user_txt);
            viewHolder.time= (TextView) convertView.findViewById(R.id.show_list_adpater_time_txt);
            viewHolder.goods= (TextView) convertView.findViewById(R.id.show_list_adpater_goods);
            viewHolder.goodsType= (TextView) convertView.findViewById(R.id.show_list_adpater_goods_type);
            viewHolder.dataNum= (TextView) convertView.findViewById(R.id.show_list_adpater_data_num);
            viewHolder.review= (TextView) convertView.findViewById(R.id.show_list_adpater_show_speak);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.userImg.setImageBitmap(BitmapUtils.steOnDrawImg(BitmapFactory.decodeResource(mContext.getResources(),
                R.mipmap.ipad1)));
        viewHolder.userName.setText("话多多");
        viewHolder.time.setText(new SimpleDateFormat("MM-dd HH:mm").format(new Date()));
        viewHolder.goods.setText("中奖喽,OPPOR9");
        viewHolder.goodsType.setText("OPPOR9 R9 4G手机");
        viewHolder.dataNum.setText("期号:30586667161");
        viewHolder.review.setText("好开心,不知道说些什么,感谢云购空间,希望你们越办越好,能在给我中个奖就更好！");

        return convertView;
    }
    class ViewHolder{
        ImageView userImg;
        TextView userName,time,goods,goodsType,dataNum,review;

    }
    public void footerRefreshAddGoods(List<GoodsDetails> data){
        mData.addAll(data);
        notifyDataSetChanged();
    }
    public void headRefreshGoods(List<GoodsDetails> data){
        mData=data;
        notifyDataSetChanged();
    }
}
