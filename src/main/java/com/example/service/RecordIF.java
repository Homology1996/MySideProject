package com.example.service;

import java.util.List;
import java.util.Map;
import com.example.model.Record;

public interface RecordIF {
	
	Record loadRecordByRecordKey(int recordKey);
	
	List<Record> loadRecordsByUserKey(int userKey);
	
	void createRecord(int recordKey, String recordDate, int userKey,
			int food, int clothes, int entertainment, int accommodation, int transportation);
	
	void updateRecord(int recordKey, String recordDate, int userKey,
			int food, int clothes, int entertainment, int accommodation, int transportation);
	
	Map<String, String> getStatistics(String startDate, String endDate, int userKey);
	
}