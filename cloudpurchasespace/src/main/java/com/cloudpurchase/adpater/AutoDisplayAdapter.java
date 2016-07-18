package com.cloudpurchase.adpater;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cloudpurchase.net.HttpImgLoader;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.utils.ScreenUtils;

import java.net.URI;
import java.util.List;

/**
 * Created by Oscar Hu on 16-6-1.
 * Viewpager适配器
 */
public class AutoDisplayAdapter extends PagerAdapter{
    private int mIcon[];
    private Context mContext;
    private int mCount;
    private List<String> mUrl;
    public AutoDisplayAdapter(Context context,int icon [],int count,List<String> url){
        if (icon!=null) {
            mIcon = new int[icon.length];
            mIcon = icon;
        }
        mContext=context;
        mCount=count;
        mUrl=url;
    }

    @Override
    public int getCount() {
        if (mUrl!=null){
            return mUrl.size();
        }else {
            return null!=mIcon?mCount:0;
        }


    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        ImageView img=new ImageView(mContext);
        if (mUrl!=null){
            LogUtils.e(mUrl.get(position));
            HttpImgLoader.getHttpImgLoader().initImgNoBitmap(mUrl.get(position),img);
        }else{
            img.setImageResource(mIcon[position % mIcon.length]);
        }

        img.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击跳转至你想连接的Url" + (position % mIcon.length), Toast.LENGTH_SHORT).show();
            }
        });
        return img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}
