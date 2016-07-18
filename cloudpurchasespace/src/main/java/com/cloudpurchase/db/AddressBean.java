package com.cloudpurchase.db;

/**
 * 用户中心—》添加地址，
 * 写入数据库的实体类
 */
public class AddressBean {

    private int id;
    private String name;
    private String phone;
    private String info;
    private int flag;

    public AddressBean(){
        super();
    }
    public AddressBean(int id , String name, String phone , String info , int flag){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.info = info;
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
