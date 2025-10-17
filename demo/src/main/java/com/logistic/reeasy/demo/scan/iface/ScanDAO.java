package com.logistic.reeasy.demo.scan.iface;

public interface ScanDAO {
    <T> T insert(T entity, String tableName) throws Exception;
}
