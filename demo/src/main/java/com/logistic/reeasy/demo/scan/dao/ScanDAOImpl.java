package com.logistic.reeasy.demo.scan.dao;

import java.sql.Connection;

import com.logistic.reeasy.demo.scan.iface.ScanDAO;
import com.logistic.reeasy.demo.scan.models.ScanModel;

import org.sql2o.Sql2o;

public class ScanDAOImpl implements ScanDAO {
    
    private final Sql2o sql2o;

    public ScanDAOImpl(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    //Aqui creamos la base de datos ☆*: .｡. o(≧▽≦)o .｡.:*☆
    @Override
    public void add(ScanModel scanModel) {

        String sql = "INSERT INTO scan (user_id, bottle_id, amount, image, timestamp) VALUES (:userId, :bottleId, :amount, :image, :timestamp)";

        scanModel.getData().forEach(bottleTypeData -> {
        
            try(var con = sql2o.open()) {
                con.createQuery(sql)
                   .addParameter("userId", scanModel.getUserId())
                   .addParameter("bottleId", bottleTypeData.getType())
                   .addParameter("amount", bottleTypeData.getAmount())
                   .addParameter("image", scanModel.getImage())
                   .addParameter("timestamp", new java.sql.Timestamp(System.currentTimeMillis()))
                   .executeUpdate(); 
            } catch (Exception e) {
                e.printStackTrace();
            }
        
        });

    }

    @Override
    public void findById(Long id) {
        // TODO: Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

}
