package com.cloudpurchase.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oscar Hu
 * 存储加入购物数据
 */
public class DBWrapper {
    private DataSqlLiteOpen mOpen;
    private SQLiteDatabase mDatabase;
    public DBWrapper(Context context){
        mOpen=new DataSqlLiteOpen(context);
    }
    /**
     *插入数据
     */
    public void insertData(GoodsDetails goods){
        mDatabase=mOpen.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Constants.GOODS_URL_NAME,goods.getIcon());
        values.put(Constants.GOODS_NAME_NAME,goods.getGoodsName());
        values.put(Constants.GOODS_TOTAL_NAME,goods.getTotal());
        values.put(Constants.GOODS_REMAIN_NAME,goods.getRemaining());
        values.put(Constants.GOODS_PERSON_NUM_NAME,1);
        values.put(Constants.GOODS_GOODSID_NAME,goods.getGoodsId());
        values.put(Constants.GOODS_ACTIVITYID_NAME,goods.getActivityId());
        values.put(Constants.GOODS_JOIN_COST_NAME,goods.getJonitCost());
        mDatabase.insert(Constants.GOODS_TABLE_NAME, null, values);
        mDatabase.close();
    }
    /**
     * 查询所有
     */
    public List<GoodsDetails> selectData(){
        mDatabase=mOpen.getReadableDatabase();
        List<GoodsDetails> list=new ArrayList<GoodsDetails>();
        Cursor cursor=mDatabase.query(Constants.GOODS_TABLE_NAME, null,null, null, null, null, null);
        while(cursor.moveToNext()){
            GoodsDetails goods=new GoodsDetails();
            String url=cursor.getString(cursor.getColumnIndex(Constants.GOODS_URL_NAME));
            String goodsName=cursor.getString(cursor.getColumnIndex(Constants.GOODS_NAME_NAME));
            int total=cursor.getInt(cursor.getColumnIndex(Constants.GOODS_TOTAL_NAME));
            int remain=cursor.getInt(cursor.getColumnIndex(Constants.GOODS_REMAIN_NAME));
            int personNum=cursor.getInt(cursor.getColumnIndex(Constants.GOODS_PERSON_NUM_NAME));
            String goodsId=cursor.getString(cursor.getColumnIndex(Constants.GOODS_GOODSID_NAME));
            String activityId=cursor.getString(cursor.getColumnIndex(Constants.GOODS_ACTIVITYID_NAME));
            int joinCost=cursor.getInt(cursor.getColumnIndex(Constants.GOODS_JOIN_COST_NAME));
            goods.setIcon(url);
            goods.setGoodsName(goodsName);
            goods.setTotal(total);
            goods.setRemaining(remain);
            goods.setPersonNum(personNum);
            goods.setGoodsId(goodsId);
            goods.setActivityId(activityId);
            goods.setJonitCost(joinCost);
            list.add(goods);
        }
        mDatabase.close();
        cursor.close();
        return list;
    }

    /**
     * 删除所有
     */
    public void deleteData(){
        mDatabase=mOpen.getWritableDatabase();
        mDatabase.delete(Constants.GOODS_TABLE_NAME, null, null);
        mDatabase.close();
    }
    /**
     * 删除指定
     */
    public void deleteData(GoodsDetails good){
        mDatabase=mOpen.getWritableDatabase();
        mDatabase.delete(Constants.GOODS_TABLE_NAME,Constants.GOODS_ACTIVITYID_NAME+"=?",new String[]{good.getActivityId()+""});
        mDatabase.close();
    }
    /**
     * 跟新数据库信息
     */
    public void update(GoodsDetails good){
        mDatabase=mOpen.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Constants.GOODS_PERSON_NUM_NAME,good.getPersonNum());
        mDatabase.update(Constants.GOODS_TABLE_NAME,values, Constants.GOODS_ACTIVITYID_NAME + "=?", new String[]{good.getActivityId() + ""});
        mDatabase.close();
    }
}
