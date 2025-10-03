package com.logistic.reeasy.demo.scan.dao;

import org.sql2o.Sql2o;

import com.logistic.reeasy.demo.scan.iface.ScanDAO;
import com.logistic.reeasy.demo.scan.models.ScanModel;

public class ScanDAOImpl implements ScanDAO {

    private final Sql2o sql2o;

    public ScanDAOImpl(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public ScanModel save(ScanModel scanModel) {

        String sql = "INSERT INTO Scans (user_id, bottle_id, amount, image, timestamp) VALUES (:userId, :bottleId, :amount, :image, :timestamp)";

        try (var con = sql2o.open()) {
            var query = con.createQuery(sql);

            scanModel.getData().forEach(bottleTypeData -> {
                query.addParameter("userId", scanModel.getUserId())
                        .addParameter("bottleId", bottleTypeData.getType().getId())
                        .addParameter("amount", bottleTypeData.getAmount())
                        .addParameter("image", scanModel.getImage())
                        .addParameter("timestamp", new java.sql.Timestamp(System.currentTimeMillis()))
                        .addToBatch();
            });

            query.executeBatch();
<<<<<<< HEAD
        } catch (Exception e) {
            throw new RuntimeException("Error al insertar", e);
        }
=======
        } 
>>>>>>> 6b5ed568d543fd5e91cb2f7f09da8a17b59d6c6c

        return scanModel;
    }

    @Override
    public ScanModel findById(Long id) {
        // TODO: Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

}
