package com.logistic.reeasy.demo.coupons.iface;

public interface AdminDAO {
    public <T> T insert(T entity, String tableName) throws Exception;
}
