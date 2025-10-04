package com.logistic.reeasy.demo.scan.iface;

import com.logistic.reeasy.demo.scan.models.ScanModel;

public interface ScanDAO {
    public <T> T insert(T entity, String tableName) throws Exception;
}
