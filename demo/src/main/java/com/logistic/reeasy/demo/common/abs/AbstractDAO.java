package com.logistic.reeasy.demo.common.abs;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.sql2o.Sql2o;

public abstract class AbstractDAO {

    private final Sql2o sql2o;

    public AbstractDAO(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    /* usando reflexion para la bd */
    public <T> T insert(T entity, String tableName) throws Exception{
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
        }

        return entity;
    }

}
