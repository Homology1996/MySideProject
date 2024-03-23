package com.example.service;

import com.example.model.Record;
import com.example.dao.RecordDaoIF;
import java.util.*;
import java.util.stream.Collectors;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecordIFImpl implements RecordIF {
	
	private static final Logger log = LoggerFactory.getLogger(RecordIFImpl.class);

	@Autowired
	private RecordDaoIF recordDao;
	
	@Override
	public Record loadRecordByRecordKey(int recordKey) {
		Record record = null;
		try {
			record = recordDao.loadRecordByRecordKey(recordKey);
		} catch (Exception e) {
			log.error("Cannot load record", e);
		}
		return record;
	}
	
	private final Comparator<Record> recordComparator = (r1, r2) -> {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		try {
			int compareDate = Long.compare(formatter.parse(r1.getRecordDate()).getTime(),
					formatter.parse(r2.getRecordDate()).getTime());
			if (compareDate != 0) {
				return compareDate;
			} else {
				return Integer.compare(r1.getRecordKey(), r2.getRecordKey());
			}
		} catch (Exception e) {
			log.error("Cannot compare date", e);
			return 0;
		}
	};
	
	@Override
	public List<Record> loadRecordsByUserKey(int userKey) {
		List<Record> records = null;
		try {
			records = recordDao.loadRecordsByUserKey(userKey).stream()
					.sorted(this.recordComparator.reversed()).collect(Collectors.toList());
		} catch (Exception e) {
			log.error("Cannot load records", e);
		}
		return records;
	}
	
	@Override
	public List<Record> loadRecordsByUserKey(int userKey, String startDate, String endDate) {
		List<Record> filteredRecords = new ArrayList<>();
		List<Record> records = this.loadRecordsByUserKey(userKey);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		long startTime = 0, endTime = 0;
		try {
			Date parsedStartDate = formatter.parse(startDate);
			Date parsedEndDate = formatter.parse(endDate);
			startTime = parsedStartDate.getTime();
			endTime = parsedEndDate.getTime();
		} catch (Exception e) {
			log.error("The input date is illegal", e);
			return null;
		}
		for (Record record : records) {
			try {
				long time = formatter.parse(record.getRecordDate()).getTime();
				if (time >= startTime && time < endTime) {
					filteredRecords.add(record);
				}
			} catch (Exception ignore) {}
		}
		return filteredRecords;
	}
	
	@Override
	@Transactional
	public void createRecord(int recordKey, String recordDate, int userKey,
			int food, int clothes, int entertainment, int accommodation, int transportation) {
		recordDao.createRecord(recordKey, recordDate, userKey,
				food, clothes, entertainment, accommodation, transportation);
		log.info(String.format("(recordKey, recordDate, userKey,"
				+ " food, clothes, entertainment, accommodation, transportation) ="
				+ " (%s, %s, %s, %s, %s, %s, %s, %s) created.", recordKey, recordDate, userKey,
				food, clothes, entertainment, accommodation, transportation));
	}
	
	@Override
	@Transactional
	public void updateRecord(int recordKey, String recordDate, int userKey,
			int food, int clothes, int entertainment, int accommodation, int transportation) {
		recordDao.updateRecord(recordKey, recordDate, userKey,
				food, clothes, entertainment, accommodation, transportation);
		log.info(String.format("(recordKey, userKey) = (%s, %s) updated.", recordKey, userKey));
	}
	
	private static double calculateStandardDeviation(List<Double> array) {
		double sum = 0.0;
		for (Double num : array) {
			sum += num;
		}
		int length = array.size();
		double mean = sum / length;
		double standardDeviation = 0.0;
		for (Double num : array) {
			standardDeviation += Math.pow(num - mean, 2);
		}
		return Math.sqrt(standardDeviation/ length);
	}
	
	@Override
	public Map<String, String> getStatistics(String startDate, String endDate, int userKey) {
		int day = -1;
		Date parsedStartDate = null, parsedEndDate = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		long startTime = 0, endTime = 0;
		try {
			parsedStartDate = formatter.parse(startDate);
			parsedEndDate = formatter.parse(endDate);
			startTime = parsedStartDate.getTime();
			endTime = parsedEndDate.getTime();
			long millisecondToDay = 1000 * 60 * 60 * 24;
			long startDay = startTime / millisecondToDay;
			long endDay = endTime / millisecondToDay;
			long passDays = endDay - startDay;
			day = Integer.parseInt(Long.toString(passDays));
		} catch (Exception e) {
			log.error("The input date is illegal", e);
			return null;
		}
		if (day < 1 || startTime == 0 || endTime == 0 || parsedStartDate == null || parsedEndDate == null) {
			return null;
		}
		
		List<Record> filteredRecords = new ArrayList<>();
		List<Record> records = this.loadRecordsByUserKey(userKey);
		for (Record record : records) {
			try {
				long time = formatter.parse(record.getRecordDate()).getTime();
				if (time >= startTime && time < endTime) {
					filteredRecords.add(record);
				}
			} catch (Exception ignore) {}
		}
		if (filteredRecords.size() < 1) {
			return null;
		}
		
		int foodSum = 0;
		int foodMin = Integer.MAX_VALUE;
		int foodMax = Integer.MIN_VALUE;
		double foodAverage = 0.0;
		double foodStandardDeviation = 0.0;
		double foodRatio = 0.0;
		List<Double> allFood = new ArrayList<>();
		int clothesSum = 0;
		int clothesMin = Integer.MAX_VALUE;
		int clothesMax = Integer.MIN_VALUE;
		double clothesAverage = 0.0;
		double clothesStandardDeviation = 0.0;
		double clothesRatio = 0.0;
		List<Double> allClothes = new ArrayList<>();
		int entertainmentSum = 0;
		int entertainmentMin = Integer.MAX_VALUE;
		int entertainmentMax = Integer.MIN_VALUE;
		double entertainmentAverage = 0.0;
		double entertainmentStandardDeviation = 0.0;
		double entertainmentRatio = 0.0;
		List<Double> allEntertainment = new ArrayList<>();
		int accommodationSum = 0;
		int accommodationMin = Integer.MAX_VALUE;
		int accommodationMax = Integer.MIN_VALUE;
		double accommodationAverage = 0.0;
		double accommodationStandardDeviation = 0.0;
		double accommodationRatio = 0.0;
		List<Double> allAccommodation = new ArrayList<>();
		int transportationSum = 0;
		int transportationMin = Integer.MAX_VALUE;
		int transportationMax = Integer.MIN_VALUE;
		double transportationAverage = 0.0;
		double transportationStandardDeviation = 0.0;
		double transportationRatio = 0.0;
		List<Double> allTransportation = new ArrayList<>();
		
		for (Record record : filteredRecords) {
			int food = record.getFood();
			int clothes = record.getClothes();
			int entertainment = record.getEntertainment();
			int accommodation = record.getAccommodation();
			int transportation = record.getTransportation();
			allFood.add((double) food);
			allClothes.add((double) clothes);
			allEntertainment.add((double) entertainment);
			allAccommodation.add((double) accommodation);
			allTransportation.add((double) transportation);
			foodSum += food;
			foodMin = Math.min(foodMin, food);
			foodMax = Math.max(foodMax, food);
			clothesSum += clothes;
			clothesMin = Math.min(clothesMin, clothes);
			clothesMax = Math.max(clothesMax, clothes);
			entertainmentSum += entertainment;
			entertainmentMin = Math.min(entertainmentMin, entertainment);
			entertainmentMax = Math.max(entertainmentMax, entertainment);
			accommodationSum += accommodation;
			accommodationMin = Math.min(accommodationMin, accommodation);
			accommodationMax = Math.max(accommodationMax, accommodation);
			transportationSum += transportation;
			transportationMin = Math.min(transportationMin, transportation);
			transportationMax = Math.max(transportationMax, transportation);
		}
		foodAverage = foodSum / day;
		foodStandardDeviation = calculateStandardDeviation(allFood);
		clothesAverage = clothesSum / day;
		clothesStandardDeviation = calculateStandardDeviation(allClothes);
		entertainmentAverage = entertainmentSum / day;
		entertainmentStandardDeviation = calculateStandardDeviation(allEntertainment);
		accommodationAverage = accommodationSum / day;
		accommodationStandardDeviation = calculateStandardDeviation(allAccommodation);
		transportationAverage = transportationSum / day;
		transportationStandardDeviation = calculateStandardDeviation(allTransportation);
		int totalSum = foodSum + clothesSum + entertainmentSum + accommodationSum + transportationSum;
		foodRatio = 100 * foodSum / totalSum;
		clothesRatio = 100 * clothesSum / totalSum;
		entertainmentRatio = 100 * entertainmentSum / totalSum;
		accommodationRatio = 100 * accommodationSum / totalSum;
		transportationRatio = 100 * transportationSum / totalSum;
		
		Map<String, String> result = new HashMap<>();
		result.put("foodSum", Integer.toString(foodSum));
		result.put("foodMin", Integer.toString(foodMin));
		result.put("foodMax", Integer.toString(foodMax));
		result.put("foodAverage", Double.toString(foodAverage));
		result.put("foodStandardDeviation", Double.toString(foodStandardDeviation));
		result.put("foodRatio", Double.toString(foodRatio));
		result.put("clothesSum", Integer.toString(clothesSum));
		result.put("clothesMin", Integer.toString(clothesMin));
		result.put("clothesMax", Integer.toString(clothesMax));
		result.put("clothesAverage", Double.toString(clothesAverage));
		result.put("clothesStandardDeviation", Double.toString(clothesStandardDeviation));
		result.put("clothesRatio", Double.toString(clothesRatio));
		result.put("entertainmentSum", Integer.toString(entertainmentSum));
		result.put("entertainmentMin", Integer.toString(entertainmentMin));
		result.put("entertainmentMax", Integer.toString(entertainmentMax));
		result.put("entertainmentAverage", Double.toString(entertainmentAverage));
		result.put("entertainmentStandardDeviation", Double.toString(entertainmentStandardDeviation));
		result.put("entertainmentRatio", Double.toString(entertainmentRatio));
		result.put("accommodationSum", Integer.toString(accommodationSum));
		result.put("accommodationMin", Integer.toString(accommodationMin));
		result.put("accommodationMax", Integer.toString(accommodationMax));
		result.put("accommodationAverage", Double.toString(accommodationAverage));
		result.put("accommodationStandardDeviation", Double.toString(accommodationStandardDeviation));
		result.put("accommodationRatio", Double.toString(accommodationRatio));
		result.put("transportationSum", Integer.toString(transportationSum));
		result.put("transportationMin", Integer.toString(transportationMin));
		result.put("transportationMax", Integer.toString(transportationMax));
		result.put("transportationAverage", Double.toString(transportationAverage));
		result.put("transportationStandardDeviation", Double.toString(transportationStandardDeviation));
		result.put("transportationRatio", Double.toString(transportationRatio));
		
		return result;
	}
	
	@Override
	public JSONObject transformRecordIntoJSONObject(Record record) {
		JSONObject result = new JSONObject();
		/*
		Class<?> recordClass = Record.class;
		Field[] fields = recordClass.getDeclaredFields();
		Method[] methods = recordClass.getDeclaredMethods();
		for (Field field : fields) {
			String fieldName = field.getName();
			for (Method method : methods) {
				if (method.getName().equals("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1))) {
					try {
						Object getRecord = method.invoke(record);
						result.put(fieldName, getRecord);
					} catch (InvocationTargetException | IllegalAccessException ignore) {}
					break;
				}
			}
		}
		*/
		result.put("recordKey", record.getRecordKey());
		result.put("recordDate", record.getRecordDate());
		result.put("food", record.getFood());
		result.put("clothes", record.getClothes());
		result.put("entertainment", record.getEntertainment());
		result.put("accommodation", record.getAccommodation());
		result.put("transportation", record.getTransportation());
		return result;
	}
	
	@Override
	public JSONArray transformRecordsIntoJSONArray(List<Record> records) {
		JSONArray result = new JSONArray();
		records.forEach(record -> result.put(this.transformRecordIntoJSONObject(record)));
		return result;
	}

}