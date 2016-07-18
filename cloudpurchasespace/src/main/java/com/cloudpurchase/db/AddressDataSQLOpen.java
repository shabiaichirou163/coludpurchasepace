package com.cloudpurchase.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



/**
 * 创建一个SQL数据库 ，用来存储用户保存的地址信息、
 */
public class AddressDataSQLOpen extends SQLiteOpenHelper {

    public AddressDataSQLOpen(Context context){
        super(context, AddressKey.DB_NAME, null, AddressKey.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE =
                "create table " + AddressKey.DB_TABLE + " ( "
                        + AddressKey._ID + " Integer primary key autoincrement,"
                        + AddressKey.NAME + " text,"
                        + AddressKey.PHONE +" text,"
                        + AddressKey.INFO +" text,"
                        + AddressKey.FLAG+" text)";//0 = false ; 1 = true
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
