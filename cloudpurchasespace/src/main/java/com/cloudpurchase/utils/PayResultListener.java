package com.cloudpurchase.utils;

/**
 * Created by oscar on 2016/7/18.
 * 支付回调借口
 */
public interface PayResultListener {
    public void paySucceed();//支付成功
    public void payAffirming();//支付确认中
    public void payFail();//支付失败

}
