package com.example.dao;

import javax.sql.DataSource;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

/**
 * 有關Dao的類別需要繼承此類別，這樣才能讀取到application.properties的設定
 * */
public class DaoBase extends NamedParameterJdbcDaoSupport {
	
	@Autowired
	private DataSource dataSource;
	
	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}
	
}