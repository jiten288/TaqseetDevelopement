package com.extra.pos.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@Configuration
@ComponentScan
@EnableTransactionManagement
@PropertySources({ @PropertySource(value = { "classpath:database.properties" }) })
public class DataSourceConfig {

	@Autowired
	private Environment env;

	@Bean(name = "namedParameterJdbcTemplate")
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Qualifier("Database") DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}

	@Bean(name = "Database")
	public DataSource dataSource() {
		String dbHost = env.getProperty("dbhost");
		String dbPort = env.getProperty("dbport");
		String dbServiceName = env.getProperty("dbservicename");
		String dbUserName = env.getProperty("dbUsername");
		String dbPassword = env.getProperty("dbPassword");
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName(oracle.jdbc.driver.OracleDriver.class.getName());
		ds.setUrl("jdbc:oracle:thin:@" + dbHost + ":" + dbPort + ":" + dbServiceName + "");
		ds.setUsername(dbUserName);
		ds.setPassword(dbPassword);
		return ds;
	}

}
