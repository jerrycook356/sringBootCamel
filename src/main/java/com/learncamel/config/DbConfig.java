package com.learncamel.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Bean(name = "dataSource") //name of the bean to use it later
    @ConfigurationProperties(prefix = "spring.datasource") //location of configuration used
    public DataSource dataSource(){
        //builds datasource connection using the configuration specified in ConfigurationProperties
        return DataSourceBuilder.create().build();

    }
}
