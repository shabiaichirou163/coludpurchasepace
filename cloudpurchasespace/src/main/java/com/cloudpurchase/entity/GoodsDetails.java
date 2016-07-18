package com.cloudpurchase.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/6/4.
 */
public class GoodsDetails implements Serializable{
    private String icon;//图片url
    private int price;//单价
    private int participate;//参与人数
    private int total;//总需人数
    private int remaining;//剩余
    private String description;//产品描述
    private String goodsName;//产品名称
    private int personNum;//自己参与次数
    private String goodsId;//商品唯一标示id
    private String activityId;//活动Id
    private int jonitCost;//参与花费的金币
    private String number;//期号
    private String createTime;//创建时间
    private List<String> imgUrlArray;//商品详情页图片
    private int shoppingCartId;//购物车id

    public int getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(int shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public List<String> getImgUrlArray() {
        return imgUrlArray;
    }

    public void setImgUrlArray(List<String> imgUrlArray) {
        this.imgUrlArray = imgUrlArray;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getJonitCost() {
        return jonitCost;
    }

    public void setJonitCost(int jonitCost) {
        this.jonitCost = jonitCost;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public int getPersonNum() {
        return personNum;
    }

    public void setPersonNum(int personNum) {
        this.personNum = personNum;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getParticipate() {
        return participate;
    }

    public void setParticipate(int participate) {
        this.participate = participate;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }
}
