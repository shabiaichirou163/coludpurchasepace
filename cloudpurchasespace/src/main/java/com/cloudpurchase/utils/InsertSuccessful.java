package com.cloudpurchase.utils;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageView;

/**
 * Created by oscar on 2016/6/14.
 * 数据库插入成功后显示viewbadger
 */
public interface InsertSuccessful {
    public void InsertSuccessful(Drawable drawable, int startLoaction[], String flag);
}
