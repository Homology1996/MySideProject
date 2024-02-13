package com.example.dao;

import java.util.List;
import com.example.model.Record;
import com.example.dao.mapper.RecordRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

@Repository
public class RecordDao extends DaoBase implements RecordDaoIF {
	
	@Override
	public Record loadRecordByRecordKey(int recordKey) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM daily_record WHERE record_key = :recordKey");
		SqlParameterSource parameters = new MapSqlParameterSource().addValue("recordKey", recordKey);
		return super.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), parameters, new RecordRowMapper());
	}
	
	@Override
	public List<Record> loadRecordsByUserKey(int userKey) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM daily_record WHERE user_key = :userKey");
		SqlParameterSource parameters = new MapSqlParameterSource().addValue("userKey", userKey);
		return super.getNamedParameterJdbcTemplate().query(sql.toString(), parameters, new RecordRowMapper());
	}
	
	@Override
	public void createRecord(int recordKey, String recordDate, int userKey,
			int food, int clothes, int entertainment, int accommodation, int transportation) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO daily_record(record_key, record_date, user_key,"
				+ " food, clothes, entertainment, accommodation, transportation)"
				+ " VALUES (:recordKey, :recordDate, :userKey,"
				+ " :food, :clothes, :entertainment, :accommodation, :transportation)");
		SqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("recordKey", recordKey).addValue("recordDate", recordDate).addValue("userKey", userKey)
				.addValue("food", food).addValue("clothes", clothes).addValue("entertainment", entertainment)
				.addValue("accommodation", accommodation).addValue("transportation", transportation);
		super.getNamedParameterJdbcTemplate().update(sql.toString(), parameters);
	}
	
	@Override
	public void updateRecord(int recordKey, String recordDate, int userKey,
			int food, int clothes, int entertainment, int accommodation, int transportation) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE daily_record SET record_date = :recordDate,"
				+ " food = :food, clothes = :clothes, entertainment = :entertainment,"
				+ " accommodation = :accommodation, transportation = :transportation"
				+ " WHERE record_key = :recordKey AND user_key = :userKey");
		SqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("recordKey", recordKey).addValue("recordDate", recordDate).addValue("userKey", userKey)
				.addValue("food", food).addValue("clothes", clothes).addValue("entertainment", entertainment)
				.addValue("accommodation", accommodation).addValue("transportation", transportation);
		super.getNamedParameterJdbcTemplate().update(sql.toString(), parameters);
	}
	
}