package com.logistic.reeasy.demo.scan.dao;

import org.sql2o.Sql2o;

import com.logistic.reeasy.demo.scan.abs.AbstractDAO;
import com.logistic.reeasy.demo.scan.iface.ScanDAO;

public class ScanDAOImpl extends AbstractDAO implements ScanDAO {

    public ScanDAOImpl(Sql2o sql2o) {
        super(sql2o);
    }
}
