package com.cloudpurchase.net;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.cloudpurchase.cloudpurchase.MyApplication;
import com.cloudpurchase.utils.RequestResultIn;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 对Volley框架的二次封装
 *
 *实现方法：GET请求  POST请求
 *
 *
 */

public class HttpRequest {
    private final static String TYPE_UTF8_CHARSET = "UTF-8"; //服务器编码格式
    /*
    设置请求单例模式,确保使用相同Url在不同情况请求为同一个
     */
    private static HttpRequest mHttpRequest;
    private HttpRequest(){}
    /*
     获取HttpRequest对象
     */
    public static HttpRequest getHttpRequest(){
        if (null==mHttpRequest){
            synchronized (HttpRequest.class){
                if (null==mHttpRequest){
                    mHttpRequest=new HttpRequest();
                }
            }
        }
        return mHttpRequest;
    }

    /*
    发起GET请求

    url ：请求服务器的地址
     */
    public void requestGET(String url,String tag,final RequestResultIn requestResultIn){
        StringRequest srReq = new StringRequest(Request.Method.GET, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String arg0) {
                requestResultIn.requstSuccful(arg0);
            };
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                requestResultIn.requstError(arg0);
            }
        } ) {
            /*
             * 重写parseNetworkResponse()方法，以解决乱码
             * @param response
             * @return 请求结果
             */
            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                String str = null;
                try {
                    str = new String(response.data, TYPE_UTF8_CHARSET);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        srReq.setShouldCache(true); // 是否使用缓存
        srReq.setTag(tag);
        srReq.setRetryPolicy(new DefaultRetryPolicy(50000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getHttpRequestQueue().add(srReq);
    }

    /*
     * Post请求
     *
     * @param url post请求地址
     */
    public void requestPOST(String url,String tag,final HashMap<String,String> params,final RequestResultIn requestResultIn) {
        StringRequest srReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                requestResultIn.requstSuccful(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                requestResultIn.requstError(volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        srReq.setShouldCache(true); // 是否使用缓存
        srReq.setTag(tag);
        srReq.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getHttpRequestQueue().add(srReq);
    }
}
