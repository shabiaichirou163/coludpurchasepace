package com.cloudpurchase.utils;

import android.content.Context;
import android.provider.ContactsContract;

import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.entity.Order;
import com.cloudpurchase.entity.RequestInfo;
import com.cloudpurchase.entity.ShoppingCartInfo;
import com.cloudpurchase.entity.UserFragmentEntity;
import com.cloudpurchase.entity.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.InflaterOutputStream;


/**
 * Created by Oscar Hu on 16-６-５.
 * 用于解析网络请求数据
 */
public class JsonReslove {
    private Context mContext;
    public JsonReslove(Context context){
        this.mContext=context;
    }

    /**
     * 解析一元 十元 百元区 商品信息
     * @param result
     * @return
     */
    public List<GoodsDetails> resloveGoods(JSONObject result){

        List<GoodsDetails> goods=new ArrayList<GoodsDetails>();
        try {
            JSONArray array=result.getJSONArray("data");
            for (int i=0;i<array.length();i++){
                GoodsDetails goodsDetails=new GoodsDetails();
                JSONObject object=array.getJSONObject(i);
                String goodName=object.getString("goodsName");
                String iconUrl=object.getString("goodsImg");
                int  price=object.getInt("price");
                int participatePeron=object.getInt("joinCount");
                int totalPreson=object.getInt("requireCount");
                int remainPreson=object.getInt("surplusCount");
                String number=object.getString("number");
                String goodId=object.getString("goodsId");
                String activityId=object.getString("activityId");
                String description=object.getString("goodsBrief");
                int joinCost=object.getInt("joinCost");
                goodsDetails.setGoodsName(goodName);
                goodsDetails.setIcon(iconUrl);
                goodsDetails.setPrice(price);
                goodsDetails.setParticipate(participatePeron);
                goodsDetails.setTotal(totalPreson);
                goodsDetails.setRemaining(remainPreson);
                goodsDetails.setNumber(number);
                goodsDetails.setGoodsId(goodId);
                goodsDetails.setActivityId(activityId);
                goodsDetails.setDescription(description);
                goodsDetails.setJonitCost(joinCost);
                goods.add(goodsDetails);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goods;
    }

    /**
     * 解析商品详情页面信息
     */
    public GoodsDetails resloveGoodsDetails(JSONObject result){
        GoodsDetails goods=new GoodsDetails();
        try {
            JSONObject object=result.getJSONObject("data");
            String activityId=object.getString("activityId");
            String  goodsId=object.getString("goodsId");
            String goodsName=object.getString("goodsName");
            int total=object.getInt("requireCount");
            int remain=object.getInt("surplusCount");
            int participate=object.getInt("joinCount");
            String description=object.getString("goodsBrief");
            String number=object.getString("number");
            int joinCost=object.getInt("joinCost");
            Long time=object.getLong("createTime");
            String creatTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
            JSONArray array=object.getJSONArray("images");
            List<String> list=new ArrayList<String>();
            for (int i=0;i<array.length();i++){
                list.add(array.getString(i));
            }
            String imgUrl=object.getString("goodsImg");
            String goodName=object.getString("goodsName");
            goods.setTotal(total);
            goods.setRemaining(remain);
            goods.setParticipate(participate);
            goods.setDescription(description);
            goods.setNumber(number);
            goods.setJonitCost(joinCost);
            goods.setCreateTime(creatTime);
            goods.setImgUrlArray(list);
            goods.setGoodsName(goodName);
            goods.setIcon(imgUrl);
            goods.setGoodsName(goodsName);
            goods.setActivityId(activityId);
            goods.setGoodsId(goodsId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return goods;
    }
    /**
     * 解析商品信息  目前为假数据
     * @param result
     * @return
     */
    public List<GoodsDetails> resloveGoods1(JSONObject result){
        List<GoodsDetails> data=new ArrayList<GoodsDetails>();
        for (int i=0;i<9;i++){
            GoodsDetails goodsDetails=new GoodsDetails();
            goodsDetails.setParticipate(2000);
            goodsDetails.setPrice(3000);
            goodsDetails.setRemaining(1050);
            goodsDetails.setTotal(3050);
            goodsDetails.setGoodsId((6666+i)+"");
            goodsDetails.setActivityId((140+i)+"");
            goodsDetails.setDescription("国行【送壳+钢化膜】Apple/苹果iphone 6S Plus 5.5英寸全网通");
            data.add(goodsDetails);
        }
        return data;
    }
    /**
     * 解析商品详情页用户参与信息
     * @param result
     * @return
     */

    public List<UserInfo> resloveUserInfo(JSONObject result){
        List<UserInfo> data=new ArrayList<UserInfo>();
        try {
            JSONObject ob=result.getJSONObject("data");
            JSONArray array=ob.getJSONArray("arrUserMsg");
            for (int i=0;i<array.length();i++){
                UserInfo userInfo=new UserInfo();
                JSONObject object=array.getJSONObject(i);
                String ip=object.getString("userIp");
                String city=object.getString("city");
                String user=object.getString("userName");
                int count=object.getInt("number");
                Long time=object.getLong("time");
                String partTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
                userInfo.setIpAddr(ip);
                userInfo.setCity(city);
                userInfo.setUserName(user);
                userInfo.setCount(count);
                userInfo.setDataTime(partTime);
                data.add(userInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }


    /**
     * 解析返回是否成功  作为返回成功参考  code 0 标示成功
     */
    public RequestInfo resloverIsSuff(String  result){
        RequestInfo info=new RequestInfo();
        try {
            JSONObject object=new JSONObject(result);
            int code=object.getInt("code");
            //int data=object.getInt("data");
            String mes=object.getString("message");
            info.setCode(code);
            //info.setData(data);
            info.setMessage(mes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
         return info;
    }
    public List<GoodsDetails> resloverShoppingCart(JSONObject result){
        List<GoodsDetails> data=new ArrayList<GoodsDetails>();
        try {
            JSONObject ob=result.getJSONObject("data");
            JSONArray array=ob.getJSONArray("shopCart");
            for (int i=0;i<array.length();i++){
                GoodsDetails goods=new GoodsDetails();
                JSONObject object=array.getJSONObject(i);
                int shoppingCartId=object.getInt("id");
                String activityId=object.getString("activityId");
                String goodName=object.getString("goodsName");
                String iconUrl=object.getString("image");
                int personNum=object.getInt("goodsnumber");
                int total=object.getInt("requireCount");
                int remain=object.getInt("surplusCount");
                int joinCount=object.getInt("joinCount");
                int joinCost=object.getInt("joinCost");
                goods.setShoppingCartId(shoppingCartId);
                goods.setActivityId(activityId);
                goods.setGoodsName(goodName);
                goods.setIcon(iconUrl);
                goods.setPersonNum(personNum);
                goods.setTotal(total);
                goods.setRemaining(remain);
                goods.setJonitCost(joinCount);
                goods.setJonitCost(joinCost);
                data.add(goods);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public ShoppingCartInfo resloveShoppingCartInfo(JSONObject result){
        ShoppingCartInfo info=new ShoppingCartInfo();
        try {
            JSONObject ob=result.getJSONObject("data");
            int totalNum=ob.getInt("totalNumber");
            int totalPrice=ob.getInt("totalPrice");
            info.setTotalNum(totalNum);
            info.setTotalPrice(totalPrice);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return info;
    }

    /**
     * 登陆
     */
    public UserFragmentEntity resultUserFragment(String json){
        if(json == ""){
            return null;
        }else {
            UserFragmentEntity bean = new UserFragmentEntity();
            try {
                JSONObject obj = new JSONObject(json);
                bean.msg = obj.getString("message");
                bean.code = obj.getInt("code");
                JSONObject obj2 = obj.getJSONObject("data");
                bean.userId = obj2.getInt("userId");
                bean.headImgUrl = obj2.getString("headImgUrl");
                bean.userMsg = obj2.getString("userMsg");
                bean.integral = obj2.getInt("integral");
                bean.bigGolden = obj2.getInt("bigGolden");
                bean.smallGolden = obj2.getInt("smallGolden");
                bean.username = obj2.getString("username");
                bean.nickName = obj2.getString("nickName");
                bean.password = obj2.getString("password");
                bean.email = obj2.getString("email");
                bean.sex = obj2.getBoolean("sex");
                bean.birthday = obj2.getString("birthday");
                bean.usermoney = obj2.getInt("usermoney");
                bean.regtime = obj2.getLong("regtime");
                bean.lasttime = obj2.getLong("lasttime");
                bean.lastip = obj2.getString("lastip");
                bean.qq = obj2.getString("qq");
                bean.microblog = obj2.getString("microblog");
                bean.wechat = obj2.getString("wechat");
                bean.officephone = obj2.getString("officephone");
                bean.homephone = obj2.getString("homephone");
                bean.mobilephone = obj2.getString("mobilephone");
                bean.userrank = obj2.getInt("userrank");
                bean.rankName= obj2.getString("rankName");
                bean.token = obj2.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bean;
        }
    }

    /**
     * 解析生成订单号
     */

    public Order resultOrederInfo(JSONObject result){
        Order order=new Order();
        try {
            JSONObject object=result.getJSONObject("data");
            String activityId=object.getString("activityId");
            int userId=object.getInt("userId");
            int payPrice=object.getInt("payFee");
            String orderNum=object.getString("orderNumber");
            order.setUserId(userId);
            order.setPayPrice(payPrice);
            order.setOrderNumber(orderNum);
            order.setActivityId(activityId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return order;
    }
}

