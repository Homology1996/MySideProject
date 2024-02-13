package com.example.dao.mapper;

import com.example.model.Record;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class RecordRowMapper implements RowMapper<Record> {
	
	@Override
	public Record mapRow(ResultSet rs, int line) throws SQLException {
		return new Record(rs.getInt(1), rs.getString(2), rs.getInt(3),
				rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8));
	}
	
}