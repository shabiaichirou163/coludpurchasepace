package com.cloudpurchase.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.utils.LogUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 *
 * 图片异步加载工具类，（对ImageLoad框架的二次封装）
 *
 *使用有参构造器时可以设置一些默认图片（图片下载期间显示的图片，解码过程中失败显示的图片等）
 *
 * 也可以使用无参构造器（无法设置默认图片）
 *
 * @author WangLiang
 */
public class HttpImgLoader {

    private DisplayImageOptions options;//ImageLoad 对象
    private LogUtils log;
    private static HttpImgLoader mHttpImgLoader=null;

    /*
        作用:为ImageLoad初始化

     * @param course 设置图片下载期间现实的图片
	 * @param vacancy Uri为空或是错误时显示的图片
	 * @param error 图片加载/解码过程中错误时显示的图片
     */
    private HttpImgLoader(int course,int vacancy,int error ){
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(course) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(vacancy)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(error)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)
                //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .build();//构建完成
        log = new LogUtils();//创建LogUtils对象
    }
    /*
        作用:为ImageLoad初始化

         无参构造器
     */
    private HttpImgLoader(){
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)
                //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .build();//构建完成
        log = new LogUtils();//创建LogUtils对象
    }


      /*
        因为Bitmp对象比较大,单利设置
     */

    public static HttpImgLoader getHttpImgLoader(){
        if (null==mHttpImgLoader){
            synchronized (HttpImgLoader.class){
                if (null==mHttpImgLoader){
                    mHttpImgLoader=new HttpImgLoader();
                }
            }
        }
        return mHttpImgLoader;
    }


    /*

图片异步加载方法

url:请求图片的网址

img：要在哪个ImageView控件上显示
 */
    public void initImg(String url,ImageView img){
        if (url == null) {
            log.e("HttpImgLoader—url为空");
            return;
        }else if(url.length() == 0){
            log.e( "HttpImgLoader—url为空");
            return;
        }else {
            ImageLoader.getInstance().displayImage(url, img, options, new SimpleImageLoadingListener(){
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {


                    super.onLoadingComplete(imageUri, view, loadedImage);
                }
            });

            log.e("请求网址：" + url);
        }
    }
    /*

    图片异步加载方法

    url:请求图片的网址

    img：要在哪个ImageView控件上显示
     */
    public void initImgNoBitmap(String url,ImageView img){
        if (url == null) {
            LogUtils.e("HttpImgLoader—url为空");
            return;
        }else if(url.length() == 0){
            LogUtils.e( "HttpImgLoader—url为空");
            return;
        }else {
            ImageLoader.getInstance().displayImage(url, img, options);
            LogUtils.e("请求网址：" + url);
        }
    }


}
