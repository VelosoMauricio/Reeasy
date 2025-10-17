package com.logistic.reeasy.demo.common.abs;

public interface InterfaceDAO {
    <T> T insert(T entity, String tableName) throws Exception;
}
