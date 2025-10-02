package com.logistic.reeasy.demo.scan.iface;

import com.logistic.reeasy.demo.scan.models.ScanModel;

public interface ScanDAO {
    public void findById(Long id);
    public void add(ScanModel scanModel);
}
