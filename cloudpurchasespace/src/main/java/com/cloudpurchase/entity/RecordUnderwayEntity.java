package com.cloudpurchase.entity;

/**
 * 云购记录 正在进行中实体类
 */
public class RecordUnderwayEntity {
    private String mImg;//商品图片
    private String mGoodsName;//商品描述
    private String mNum;//总需量
    private String mPrgNum;//进度
    private String mPrompt;//提示信息

    public String getmGoodsName() {
        return mGoodsName;
    }

    public String getmImg() {
        return mImg;
    }

    public String getmNum() {
        return mNum;
    }

    public String getmPrgNum() {
        return mPrgNum;
    }

    public String getmPrompt() {
        return mPrompt;
    }

    public void setmImg(String mImg) {
        this.mImg = mImg;
    }

    public void setmGoodsName(String mGoodsName) {
        this.mGoodsName = mGoodsName;
    }

    public void setmNum(String mNum) {
        this.mNum = mNum;
    }

    public void setmPrgNum(String mPrgNum) {
        this.mPrgNum = mPrgNum;
    }

    public void setmPrompt(String mPrompt) {
        this.mPrompt = mPrompt;
    }
}
