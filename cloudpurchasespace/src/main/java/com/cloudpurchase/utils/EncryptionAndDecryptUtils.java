package com.cloudpurchase.utils;

import java.nio.charset.Charset;

/**
 * 加密 解密工具类
 *
 * 使用在将用户私人信息存入本地
 */
public class EncryptionAndDecryptUtils {
    private static final String key0 = "FECOI()*&<MNCXZPKL";
    private static final Charset charset = Charset.forName("UTF-8");
    private static byte[] keyBytes = key0.getBytes(charset);


    /*
    使用异或运算对字符串进行简单加密、
    enc：要加密的内容
     */
    public static String encode(String enc){
        byte[] b = enc.getBytes(charset);
        for(int i=0,size=b.length;i<size;i++){
            for(byte keyBytes0:keyBytes){
                b[i] = (byte) (b[i]^keyBytes0);
            }
        }
        return new String(b);
    }

    /*
    对加密后的内容进行解密
     */
    public static String decode(String dec){
        byte[] e = dec.getBytes(charset);
        byte[] dee = e;
        for(int i=0,size=e.length;i<size;i++){
            for(byte keyBytes0:keyBytes){
                e[i] = (byte) (dee[i]^keyBytes0);
            }
        }
        return new String(e);
    }

}
