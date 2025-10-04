package com.logistic.reeasy.demo.scan.dao;

import org.sql2o.Sql2o;

import com.logistic.reeasy.demo.scan.abs.AbstractDAO;
import com.logistic.reeasy.demo.scan.iface.ScanDAO;
import com.logistic.reeasy.demo.scan.models.ScanModel;

public class ScanDAOImpl extends AbstractDAO implements ScanDAO {

    private final Sql2o sql2o;

    public ScanDAOImpl(Sql2o sql2o) {
        super(sql2o);
        this.sql2o = sql2o;
    }

    @Override
    public <T> T insert(T entity, String tableName) throws Exception {
        return super.insert(entity, tableName);
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
        }

        return scanModel;
    }

    @Override
    public ScanModel findById(Long id) {
        // TODO: Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

}
