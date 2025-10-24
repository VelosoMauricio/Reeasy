package com.logistic.reeasy.demo.common.abs;

public interface InterfaceDAO <T> {
    T insert(T entity) throws Exception;
    T findById(Long id, String idColumnName) throws Exception;
}
