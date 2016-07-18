package com.cloudpurchase.utils;

import com.android.volley.VolleyError;

/**
 * Created by Oscar Hu on 16-6-5.
 * 回调网络请求返回结果
 */
public interface RequestResultIn {
    public void requstSuccful(String result);
    public void requstError(VolleyError error);
}
