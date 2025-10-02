package com.logistic.reeasy.demo.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sql2o.Sql2o;

@Configuration
public class DatabaseConfig {

    @Bean
    public Sql2o sql2o(
            @Value("${db.url}") String url,
            @Value("${db.user}") String user,
            @Value("${db.password}") String password) {
        return new Sql2o(url, user, password);
    }
}