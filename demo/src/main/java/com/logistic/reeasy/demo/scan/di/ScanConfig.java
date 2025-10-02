package com.logistic.reeasy.demo.scan.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sql2o.Sql2o;

import com.logistic.reeasy.demo.scan.dao.ScanDAOImpl;
import com.logistic.reeasy.demo.scan.iface.ScanDAO;

@Configuration
public class ScanConfig {

  @Bean
  public ScanDAO scanDAO(Sql2o sql2o) {
      return new ScanDAOImpl(sql2o);
  }
}
