package com.cloudpurchase.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cloudpurchase.cloudpurchase.AllAddressActivity;
import java.util.ArrayList;
import java.util.List;

/**
 *  实现用户保存到本地的地址管理
 *  ：增删改查
 */
public class AddressDB_Use {

    private AddressDataSQLOpen SQLOpen;
    private SQLiteDatabase mDataBase;

    public AddressDB_Use(Context context){
        SQLOpen = new AddressDataSQLOpen(context);
    }

    /*
    插入数据
     */
    public void insert(AddressBean bean){
        mDataBase = SQLOpen.getWritableDatabase();
        ContentValues values=new ContentValues();
//        values.put(AddressKey._ID,bean.getId());
        values.put(AddressKey.NAME,bean.getName());
        values.put(AddressKey.PHONE,bean.getPhone());
        values.put(AddressKey.INFO,bean.getInfo());
        values.put(AddressKey.FLAG,bean.getFlag());
        mDataBase.insert(AddressKey.DB_TABLE,null,values);
        mDataBase.close();
    }

    /*
    删除数据
     */
    public void deleteData(int id){
        mDataBase = SQLOpen.getWritableDatabase();
        mDataBase.delete
                (AddressKey.DB_TABLE,AddressKey._ID+"=?",new String[]{String.valueOf(id)});
        AllAddressActivity.ACTIVITY_THIS.finish();
        Intent i = new Intent(AllAddressActivity.ACTIVITY_THIS,AllAddressActivity.class);
        AllAddressActivity.ACTIVITY_THIS.startActivity(i);
    }

    /*
    修改指定id的数据
     */
    public void upData(int id , int flag){
        mDataBase = SQLOpen.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AddressKey._ID,id);
        values.put(AddressKey.FLAG,flag);
        mDataBase.update
                (AddressKey.DB_TABLE,values,AddressKey._ID+"=?",new String[]{String.valueOf(id)});
        mDataBase.close();
    }

    /*
    修改指定id的所有数据
     */
    public void upInIdAllData(int id ,AddressBean bean){
        mDataBase = SQLOpen.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AddressKey._ID,id);
        values.put(AddressKey.FLAG,bean.getName());
        values.put(AddressKey.FLAG,bean.getPhone());
        values.put(AddressKey.FLAG,bean.getFlag());
        values.put(AddressKey.FLAG,bean.getInfo());
        mDataBase.update
                (AddressKey.DB_TABLE,values,AddressKey._ID+"=?",new String[]{String.valueOf(id)});
        mDataBase.close();
    }


    /*
    查询所有数据
     */
    public List<AddressBean> findAll(){
        List<AddressBean> list = null;
        mDataBase = SQLOpen.getWritableDatabase();
        Cursor cursor = mDataBase.query(AddressKey.DB_TABLE, null, null, null, null, null,null);
        if(cursor != null){
            list = new ArrayList<AddressBean>();
            while (cursor.moveToNext()){
                AddressBean bean = new AddressBean();
                bean.setId(cursor.getInt(cursor.getColumnIndex(AddressKey._ID)));
                bean.setName(cursor.getString(cursor.getColumnIndex(AddressKey.NAME)));
                bean.setPhone(cursor.getString(cursor.getColumnIndex(AddressKey.PHONE)));
                bean.setInfo(cursor.getString(cursor.getColumnIndex(AddressKey.INFO)));
                bean.setFlag(cursor.getInt(cursor.getColumnIndex(AddressKey.FLAG)));
                list.add(bean);
            }
            return list;
        }else if (cursor == null){
            return null;
        }
        mDataBase.close();
        return null;
    }

    /*
    查询指定ID数据
     */
    public AddressBean fingById(int id){
        mDataBase = SQLOpen.getWritableDatabase();
        Cursor cursor = mDataBase.query
                (AddressKey.DB_TABLE,
                        null, AddressKey._ID + "=?",
                        new String[]{String.valueOf(id)}, null, null, null);
        AddressBean bean = null;
        if(cursor != null && cursor.moveToFirst()){
            bean = new AddressBean();
            bean.setId(cursor.getInt(cursor.getColumnIndex(AddressKey._ID)));
            bean.setName(cursor.getString(cursor.getColumnIndex(AddressKey.NAME)));
            bean.setPhone(cursor.getString(cursor.getColumnIndex(AddressKey.PHONE)));
            bean.setInfo(cursor.getString(cursor.getColumnIndex(AddressKey.INFO)));
            bean.setFlag(cursor.getInt(cursor.getColumnIndex(AddressKey.FLAG)));
            return bean;
        }if(cursor == null){
            return null;
        }
        mDataBase.close();
        return null;
    }

    /*
    返回数据库的数量
     */
    public int getDBSize(){
        if(findAll() == null){
            return 0;
        }else{
            return findAll().size();
        }
    }
}
