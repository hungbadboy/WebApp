package com.siblinks.ws.service;

import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.jolbox.bonecp.BoneCPDataSource;
import com.siblinks.ws.security.SibSecurityConfig;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.siblinks.ws" })
@PropertySources({ @PropertySource("classpath:application.properties"),
	@PropertySource("classpath:appconfig_${sib.env}.properties"), @PropertySource("classpath:configuration_${sib.env}.properties"),
    @PropertySource("classpath:DataServiceSQLMap.properties"), @PropertySource("classpath:DataServiceSQLMapExt.properties"), @PropertySource("classpath:SQL_DAM.properties"), @PropertySource("classpath:SQL_TA.properties") })
@SpringBootApplication
@EnableCaching
public class Application {

    public static String rule;

    @Value("${init-db:false}")
    private String initDatabase;

    @Autowired
    private Environment environment;

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        org.springframework.boot.web.servlet.MultipartConfigFactory factory = new org.springframework.boot.web.servlet.MultipartConfigFactory();
        factory.setMaxFileSize("5120KB");
        factory.setMaxRequestSize("5120KB");
        return factory.createMultipartConfig();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        BoneCPDataSource dataSource = new BoneCPDataSource();
        dataSource.setDriverClass(environment.getRequiredProperty("disp.db.driver"));
        dataSource.setJdbcUrl(environment.getRequiredProperty("disp.db.url"));
        dataSource.setUsername(environment.getRequiredProperty("disp.db.user"));
        dataSource.setPassword(environment.getRequiredProperty("disp.db.password"));
        dataSource.setIdleConnectionTestPeriod(Integer.valueOf(environment.getProperty("disp.db.idleMaxAgeInMinutes")));
        dataSource.setIdleMaxAge(Long.valueOf(environment
            .getRequiredProperty("disp.db.idleConnectionTestPeriodInMinutes")));
        dataSource.setMaxConnectionsPerPartition(Integer.valueOf(environment
            .getRequiredProperty("disp.db.maxConnectionsPerPartition")));
        dataSource.setMinConnectionsPerPartition(Integer.valueOf(environment
            .getRequiredProperty("disp.db.minConnectionsPerPartition")));
        dataSource.setPartitionCount(Integer.valueOf(environment.getRequiredProperty("disp.db.partitionCount")));
        dataSource.setAcquireIncrement(Integer.valueOf(environment.getRequiredProperty("disp.db.acquireIncrement")));
        dataSource
            .setStatementCacheSize(Integer.valueOf(environment.getRequiredProperty("disp.db.statementsCacheSize")));
        dataSource.setReleaseHelperThreads(Integer.valueOf(environment
            .getRequiredProperty("disp.db.releaseHelperThreads")));
        return dataSource;
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("data.sql"));
        dataSourceInitializer.setDatabasePopulator(databasePopulator);
        dataSourceInitializer.setEnabled(Boolean.parseBoolean(initDatabase));
        return dataSourceInitializer;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter() {
        return new SibSecurityConfig();
    }

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}