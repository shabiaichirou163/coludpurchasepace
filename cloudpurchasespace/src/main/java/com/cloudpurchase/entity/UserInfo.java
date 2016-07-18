package com.cloudpurchase.entity;

/**
 * Created by oscar on 2016/7/1.
 * 用户参与信息
 */
public class UserInfo {
    private String userImgUrl;//用户图片地址
    private String userName;//用户姓名
    private String city;//用户所在城市
    private String ipAddr;//用户ip地址
    private int count;//用户参与次数
    private String dataTime;//用户参与时间

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }
}
