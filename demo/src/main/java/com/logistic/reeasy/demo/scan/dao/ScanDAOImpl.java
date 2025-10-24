package com.logistic.reeasy.demo.scan.dao;

import com.logistic.reeasy.demo.scan.models.ScanTableModel;
import org.sql2o.Sql2o;

import com.logistic.reeasy.demo.common.abs.AbstractDAO;
import com.logistic.reeasy.demo.scan.iface.ScanDAO;

public class ScanDAOImpl extends AbstractDAO<ScanTableModel> implements ScanDAO {
    public ScanDAOImpl(Sql2o sql2o) {
        super(ScanTableModel.class,"Scans",sql2o);
    }
}
