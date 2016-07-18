package com.cloudpurchase.db;

/**
 * 收货人地址信息所有的key
 */
public interface AddressKey {
    public static final String DB_NAME = "MY_ADDRESS_INFO.db"; //数据库名称
    public static final String DB_TABLE ="INFO";
    public static final int VERSION = 1; //版本
    public final static String _ID = "id";
    public final static String NAME = "name";
    public final static String PHONE = "phone";
    public final static String INFO = "info";
    public final static String FLAG = "flag";


}
