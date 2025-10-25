package com.logistic.reeasy.demo.users.dao;

import com.logistic.reeasy.demo.common.abs.AbstractDAO;
import com.logistic.reeasy.demo.users.iface.iUserDAO;
import com.logistic.reeasy.demo.users.models.UserModel;
import org.sql2o.Sql2o;

public class UserDAOImpl extends AbstractDAO<UserModel> implements iUserDAO {

    public UserDAOImpl(Sql2o sql2o) {
        super(UserModel.class, "Users", sql2o);
    }
}
