package com.cloudpurchase.entity;

/**
 * 收货人信息实体类 adapter 从这里取出数据
 *
 */
public class AddressInfoEntity {
    public int id;
    public String mName;//收货人姓名
    public String mPhone;//收货人手机
    public String mAddress;//收货人地址
    public boolean mFlag = false;//是否设为默认标志
}
