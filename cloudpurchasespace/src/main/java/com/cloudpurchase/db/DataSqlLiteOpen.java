package com.cloudpurchase.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cloudpurchase.utils.Constants;

/**
 * Created by Oscar Hu
 * SQLite帮助类
 * 创建数据库和数据库表
 */
public class DataSqlLiteOpen extends SQLiteOpenHelper {
    public DataSqlLiteOpen(Context context) {

        super(context, Constants.GOODS_DB_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table "+Constants.GOODS_TABLE_NAME+"("+
                Constants.GOODS_ID_NAME+" integer primary key autoincrement,"
                +Constants.GOODS_URL_NAME+" text,"
                +Constants.GOODS_NAME_NAME+" text,"+Constants.GOODS_TOTAL_NAME+
                " integer,"+Constants.GOODS_REMAIN_NAME+" integer,"+
                Constants.GOODS_PERSON_NUM_NAME+" integer,"+
        Constants.GOODS_GOODSID_NAME+" text,"+Constants.GOODS_ACTIVITYID_NAME+" text,"+Constants.GOODS_JOIN_COST_NAME+" integer)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
