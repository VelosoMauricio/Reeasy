package com.logistic.reeasy.demo.users.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sql2o.Sql2o;
import com.logistic.reeasy.demo.users.iface.iUserDAO;
import com.logistic.reeasy.demo.users.dao.UserDAOImpl;

@Configuration
public class UserConfig {

    @Bean
    public iUserDAO userDAO(Sql2o sql2o) {
        return new UserDAOImpl(sql2o);
    }

}
