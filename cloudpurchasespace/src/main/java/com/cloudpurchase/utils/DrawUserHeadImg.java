package com.cloudpurchase.utils;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * 绘制圆形图片
 *
 */
public class DrawUserHeadImg{

    public static Bitmap steOnDrawImg(Bitmap bitmap){
        int w=bitmap.getWidth();
        int h=bitmap.getHeight();
        int min=Math.min(w, h);

        Bitmap b=Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);

        Canvas canvas=new Canvas(b);
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        canvas.drawCircle(min/2,min/2,min/2,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,0,0,paint);
        return b;
    }

}
