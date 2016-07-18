package com.cloudpurchase.db;

import java.util.List;

/**
 * 通过此接口实现
 * 增删改查
 *
 */
public interface AddressSQLOperate {
    public void add(AddressBean bean);
    public void delete(int id);
    public void upData(AddressBean bean);
    public List<AddressBean> findAll();
    public AddressBean findById(int id);
}
