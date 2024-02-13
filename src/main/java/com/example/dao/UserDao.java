package com.example.dao;

import java.util.List;
import com.example.model.User;
import com.example.dao.mapper.UserRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

@Repository
public class UserDao extends DaoBase implements UserDaoIF {
	
	@Override
	public User loadUserByUserKey(int userKey) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM user_info WHERE user_key = :userKey");
		SqlParameterSource parameters = new MapSqlParameterSource().addValue("userKey", userKey);
		return super.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), parameters, new UserRowMapper());
	}
	
	@Override
	public List<User> loadAllUsers() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM user_info ORDER BY user_key");
		SqlParameterSource parameters = new MapSqlParameterSource();
		return super.getNamedParameterJdbcTemplate().query(sql.toString(), parameters, new UserRowMapper());
	}
	
	@Override
	public void createUser(int userKey, String account, String password) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO user_info(user_key, account, password) VALUES (:userKey, :account, :password)");
		SqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("userKey", userKey).addValue("account", account).addValue("password", password);
		super.getNamedParameterJdbcTemplate().update(sql.toString(), parameters);
	}
	
	@Override
	public void updateUser(int userKey, String password) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE user_info SET password = :password WHERE user_key = :userKey");
		SqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("userKey", userKey).addValue("password", password);
		super.getNamedParameterJdbcTemplate().update(sql.toString(), parameters);
	}
	
}