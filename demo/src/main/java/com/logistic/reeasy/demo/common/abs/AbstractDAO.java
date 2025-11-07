package com.logistic.reeasy.demo.common.abs;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import com.logistic.reeasy.demo.common.annotations.Autoincrement;
import org.sql2o.Sql2o;

public abstract class AbstractDAO<T> implements InterfaceDAO<T>{

    protected final Class<T> type ;
    protected final String tableName;
    protected final Sql2o sql2o;

    public AbstractDAO(Class<T> type, String tableName, Sql2o sql2o) {
        this.type = type;
        this.tableName = tableName;
        this.sql2o = sql2o;
    }

    @Override
    public T insert(T entity) throws Exception {
        Field[] fields = type.getDeclaredFields();

        Optional<Field> maybeAnAutoincrementField = Arrays.stream(fields)
                .filter(f -> f.isAnnotationPresent(Autoincrement.class))
                .findFirst();

        List<Field> nonAutoincrementFields = Arrays.stream(fields)
                .filter(f -> maybeAnAutoincrementField.map(id -> !f.equals(id)).orElse(true))
                .collect(Collectors.toList());

        String columns = nonAutoincrementFields.stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));

        String values = nonAutoincrementFields.stream()
                .map(f -> ":" + f.getName())
                .collect(Collectors.joining(", "));

        String sql = "INSERT INTO " + this.tableName + " (" + columns + ") VALUES (" + values + ")";

        try (var con = sql2o.open()) {
            var query = con.createQuery(sql, true);
            for (Field field : nonAutoincrementFields) {
                field.setAccessible(true);
                query.addParameter(field.getName(), field.get(entity));
            }

            Object generatedKey = maybeAnAutoincrementField.isPresent() ? query.executeUpdate().getKey() : null;

            if (maybeAnAutoincrementField.isPresent() && generatedKey != null) {
                Field idField = maybeAnAutoincrementField.get();
                String selectSql = "SELECT * FROM " + this.tableName + " WHERE " + idField.getName() + " = :id";
                return con.createQuery(selectSql)
                        .addParameter("id", generatedKey)
                        .executeAndFetchFirst(type);
            }

            return entity;
        }
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
