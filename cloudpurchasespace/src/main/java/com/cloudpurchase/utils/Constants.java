package com.cloudpurchase.utils;

/**
 * 微博第三方登录配置APP_KEY
 *
 *
 * 需要实现好友邀请接口，需要在SCOPE参数中添加"invitation_write"值。
 */
public interface Constants {
    public static final String APP_KEY      = "2045436852";		   // 应用的APP_KEY
    public static final String REDIRECT_URL = "http://www.sina.com";// 应用的回调页
    public static final String SCOPE = 							   // 应用申请的高级权限
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";



   //商品数据库量表(SQLite) 先暂时未用
    public static final String GOODS_DB_NAME="ShoppingInfo.db";
    public static final String GOODS_TABLE_NAME="goods";
    public static final String GOODS_ID_NAME="_id";
    public static final String GOODS_URL_NAME="icon";
    public static final String GOODS_NAME_NAME="goodsName";
    public static final String GOODS_TOTAL_NAME="total";
    public static final String GOODS_REMAIN_NAME="remaining";
    public static final String GOODS_PERSON_NUM_NAME="num";
    public static final String GOODS_GOODSID_NAME="goodsId";//商品唯一标示id 但可以参加多项活动
    public static final String GOODS_ACTIVITYID_NAME="acitvityId";//商品唯一标示id
    public static final String GOODS_JOIN_COST_NAME="joinCost";//分别代表1元区   10元区  百元区商品


    public static  final String BASE="http://192.168.0.109:8080/CloudPhrase";



    /**
     * 1元区最新  最热   进度   总需人次（升序  降序） URL
     */
    public static final String ONE_AREA_NEW=BASE+"/api/goods_sortlist/1/newData/1";
    public static final String ONE_AREA_HOT=BASE+"/api/goods_sortlist/1/hotData/1";
    public static final String ONE_AREA_PROGRESS=BASE+"/api/goods_sortlist/1/progress/1";
    public static final String ONE_AREA_ASC_SOFT=BASE+"/api/goods_sortlist/1/maxPrice/1";
    public static final String ONE_AREA_DES_SOFT=BASE+"/api/goods_sortlist/1/minPrice/1";



    public static final String ONE=BASE+"/api/launchAd";


    /**
     * 10元区最新  最热   进度   总需人次（升序  降序） URL
     */
    public static final String TEN_AREA_NEW=BASE+"/api/goods_sortlist/10/newData/1";
    public static final String TEN_AREA_HOT=BASE+"/api/goods_sortlist/10/hotData/1";
    public static final String TEN_AREA_PROGRESS=BASE+"/api/goods_sortlist/10/progress/1";
    public static final String TEN_AREA_ASC_SOFT=BASE+"/api/goods_sortlist/10/maxPrice/1";
    public static final String TEN_AREA_DES_SOFT=BASE+"/api/goods_sortlist/10/minPrice/1";



    /**
     * 百元区最新  最热   进度   总需人次（升序  降序） URL
     */
    public static final String ONEHUND_AREA_NEW=BASE+"/api/goods_sortlist/100/newData/1";
    public static final String ONEHUND_AREA_HOT=BASE+"/api/goods_sortlist/100/hotData/1";
    public static final String ONEHUND_AREA_PROGRESS=BASE+"/api/goods_sortlist/100/progress/1";
    public static final String ONEHUND_AREA_ASC_SOFT=BASE+"/api/goods_sortlist/100/maxPrice/1";
    public static final String ONEHUND_AREA_DES_SOFT=BASE+"/api/goods_sortlist/100/minPrice/1";


    /**
     * 商品详情页url
     */
    public static final String GOODS_DETAILS_NORMAL=BASE+"/api/goods/";

    /**
     * 商品详情页活动
     */
    public static final String GOODS_DETAILS_ACTIVITY=BASE+"/api/goods_playlist/";

    /**
     * 加入购物车
     */
    public static final String GOODS_ADD_SHOPPING_CART=BASE+"/api/add_cart";
    /**
     * 购物车列表  此处暂用会员id暂用1
     */
     public static final String GOOD_SHOPPING_CART_LIST=BASE+"/api/shopcart_list/";

    /**
     * 删除购物车
     */

    public static final String DELETE_SHOPPING_CART=BASE+"/api/del_cart";

    /**
     *购物车数量加
     */
    public static final String ADD_SHOPPING_CART=BASE+"/api/add_num";
    /**
     * 购物车数量减
     */
    public static final String REDUCE_SHOPPING_CART=BASE+"/api/del_num";

    /**
     * 生成订单
     */
    public static final String CREAT_NEW_ORDER=BASE+"/api/submit_shopcart";
    /**
     * 获取订单支付方式
     */

    public static final String GET_ORDER_PAY_TYPE=BASE+"/api/pay_list";

    /**
     * 订单支付成功
     */
    public static final String ORDER_PAY_SUCCEED=BASE+"/api/submit_order";


    /**
     * 登陆
     */
    public static final String LOGIN = BASE+"/api/login";
    public static final String OUT_LOGIN = BASE+"/api/logout";//退出登陆
    /**
     * 注册
     */
    public static final String RIGISTER = BASE+ "/api/register";
    public static final String REGISTER_CHAECK = BASE+ "/api/check_phone_number";//判断手机号是否注册
    /**
     * 修改密码
     */
    public static final String FIND_PWD = BASE+"/api/update_password";
    /**
     * 参与记录
     */
    public static final String RECORD = BASE +"/api/order_list/";
    /**
     * 中奖记录
     */
    public static final String LUCKY = BASE + "/api/order_wining_list/";


}
