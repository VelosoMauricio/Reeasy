package com.logistic.reeasy.demo.scan.iface;

import com.logistic.reeasy.demo.scan.models.ScanModel;

public interface ScanDAO {
    public ScanModel findById(Long id);
    public ScanModel save(ScanModel scanModel);
    public <T> T insert(T entity, String tableName) throws Exception;
}
