package com.logistic.reeasy.demo.Scan.iface;
import com.logistic.reeasy.demo.Scan.models.ScanModel;

public interface ScanDAO {
    public void findById(Long id);
    public void add(ScanModel scanModel);
}
