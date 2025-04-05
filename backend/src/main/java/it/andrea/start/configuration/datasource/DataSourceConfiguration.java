package it.andrea.start.configuration.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfiguration {

    @Value("${spring.datasource.url}")
    private String globalDbUrl;

    @Value("${spring.datasource.username}")
    private String globalDbUsername;

    @Value("${spring.datasource.password}")
    private String globalDbPassword;

    @Bean
    @ConfigurationProperties(prefix = "spring.tenants")
    Map<String, Map<String, String>> tenantDataSources() {
        return new HashMap<>();
    }

    @Bean
    DataSource dataSource() {
        TenantRoutingDataSource routingDataSource = new TenantRoutingDataSource();

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("global", createDataSource(globalDbUrl, globalDbUsername, globalDbPassword));

        tenantDataSources().forEach((tenant, properties) -> targetDataSources.put(tenant, createDataSource(properties.get("url"), properties.get("username"), properties.get("password"))));

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(targetDataSources.get("global"));

        return routingDataSource;
    }

    private DataSource createDataSource(String url, String username, String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

}
