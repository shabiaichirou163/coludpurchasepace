package com.cloudpurchase.cloudpurchase;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * MyApplicaiton类，处理整个应用程序的公共部分
 *  @author WangLiang
 */
public class MyApplication extends Application {
    public static String WE_CHAT_SECRET = "998b2da2a579655b093c699c1f500d52";
    public static String WE_CHAT_APPID = "wx733bdfe159d933ea";//微信官网申请的APPID
    public static String SHORT_MESSAGE_INTERFACE = "18883414399569";//短信接口账号
    public static String SHORT_MESSAGE_INTERFACE_KEY = "7RZ8UrjOM3Aj";//短信接口密码
    public Context context ;
    private static RequestQueue mQueue;//Volley请求队列
    public static boolean USER_IS_LOGIN_FLAG = false;//用于判断用户是否登录
    public static String USER_ID ;//用户ID
    public static String USER_PHONE;//用户的手机号
    public static String USER_NICKNAME = "云购玩家";//用户的昵称
    public static String USER_TOKEN;//用户token
    public static String USER_INTEGTAL;//用户积分
    public static String USER_BIG_GOLDEN;//大金币
    public static String USER_SMALL_GOLDEN;//小金币
    public static String USER_HEADIMG_URL;//用户头像url
    public static String USER_SEX;//用户性别
    public static String USER_NAME;//用户姓名
    public static String USER_USERRANK;//会员等级
    public static String USER_RANK_NAME;//会员等级名称
    public static String USER_QQ;//QQ三方登陆
    public static String USER_WE_CHAT;//微信第三方登陆
    public static String USER_WEIBO;//微博第三方登陆
    private String mSeclectFrag;//选择的fragment
    public static final String SerialNumber = android.os.Build.SERIAL;//获取设备唯一ID
    @Override
    public void onCreate() {
        super.onCreate();
        mQueue = Volley.newRequestQueue(getApplicationContext());
        initImageLoad();
    }
     /*
      获取请求队列
     */
    public static RequestQueue getHttpRequestQueue(){
        return mQueue;
    }

    /*
    ImageLoad初始化方法
     */
    private void initImageLoad(){
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(getApplicationContext());
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024);
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs();
        ImageLoader.getInstance().init(config.build());
    }

    /*
    获取全局Context
     */
    public Context getMyApplicationContext(){
        context = getApplicationContext();
        return context;
    }

    public String getmSeclectFrag() {
        return mSeclectFrag;
    }

    public void setmSeclectFrag(String mSeclectFrag) {
        this.mSeclectFrag = mSeclectFrag;
    }


}
