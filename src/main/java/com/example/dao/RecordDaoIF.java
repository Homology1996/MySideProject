package com.example.dao;

import java.util.List;
import com.example.model.Record;

public interface RecordDaoIF {

	Record loadRecordByRecordKey(int recordKey);
	
	List<Record> loadRecordsByUserKey(int userKey);
	
	void createRecord(int recordKey, String recordDate, int userKey,
			int food, int clothes, int entertainment, int accommodation, int transportation);
	
	void updateRecord(int recordKey, String recordDate, int userKey,
			int food, int clothes, int entertainment, int accommodation, int transportation);
	
}