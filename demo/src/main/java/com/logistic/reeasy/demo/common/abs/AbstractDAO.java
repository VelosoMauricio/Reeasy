package com.logistic.reeasy.demo.common.abs;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.sql2o.Sql2o;

public abstract class AbstractDAO<T> implements InterfaceDAO<T>{

    protected final Class<T> type ;
    private final String tableName;
    private final Sql2o sql2o;

    public AbstractDAO(Class<T> type, String tableName, Sql2o sql2o) {
        this.type = type;
        this.tableName = tableName;
        this.sql2o = sql2o;
    }

    @Override
    public T insert(T entity) throws Exception {
        Field[] fields = type.getDeclaredFields();

        String columns = Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.joining(", "));

        String values = Arrays.stream(fields)
                .map(f -> ":" + f.getName())
                .collect(Collectors.joining(", "));

        String sql = "INSERT INTO " + this.tableName + " (" + columns + ") VALUES (" + values + ")";

        try (var con = sql2o.open()) {
            var query = con.createQuery(sql);
            for (Field field : fields) {
                field.setAccessible(true);
                query.addParameter(field.getName(), field.get(entity));
            }
            query.executeUpdate();
        }

        return entity;
    }

    @Override
    public T findById(Long id, String idColumnName) throws Exception{

        String sql = "SELECT * FROM " + tableName + " WHERE " + idColumnName  + " = :id";

        try (var con = sql2o.open()) {

            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(type);
        }
    }

}
