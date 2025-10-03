package com.logistic.reeasy.demo.scan.dao;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.sql2o.Sql2o;

import com.logistic.reeasy.demo.scan.iface.ScanDAO;
import com.logistic.reeasy.demo.scan.models.ScanModel;
/* los metodos de la api de reflexion */
import java.lang.reflect.Field;

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
        }

        return scanModel;
    }

    /* usando reflexion para la bd */
    public <T> void insert(T entity, String tableName) {
        Class<?> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();

        // Generar columnas y placeholders dinámicos
        String columns = Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.joining(", "));
        String values = Arrays.stream(fields)
                .map(f -> ":" + f.getName())
                .collect(Collectors.joining(", "));

        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";

        try (var con = sql2o.open()) {
            var query = con.createQuery(sql);
            for (Field field : fields) {
                field.setAccessible(true); // para acceder a campos privados
                query.addParameter(field.getName(), field.get(entity));
            }
            query.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ScanModel findById(Long id) {
        // TODO: Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

}
