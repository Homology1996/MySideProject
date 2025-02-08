package com.example.db;

import com.example.dao.DaoBase;
import org.springframework.stereotype.Component;

@Component
public class GenerateKey extends DaoBase {
	
	public int generate(String keyName) {
		String sql = "SELECT nextval('" + keyName + "_seq')";
		return super.getJdbcTemplate().queryForObject(sql, Integer.class);
	}
	
}