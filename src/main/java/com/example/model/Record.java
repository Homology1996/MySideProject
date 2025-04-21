package com.example.model;

import org.springframework.stereotype.Component;

@Component
public class Record {

	private int recordKey;
	
	private String recordDate;
	
	private int userKey;
	
	private int food;
	
	private int clothes;
	
	private int entertainment;
	
	private int accommodation;
	
	private int transportation;
	
	public Record() {}
	
	public Record(int recordKey, String recordDate, int userKey,
			int food, int clothes, int entertainment, int accommodation, int transportation) {
		this();
		this.setRecordKey(recordKey);
		this.setRecordDate(recordDate);
		this.setUserKey(userKey);
		this.setFood(food);
		this.setClothes(clothes);
		this.setEntertainment(entertainment);
		this.setAccommodation(accommodation);
		this.setTransportation(transportation);
	}
	
	public int getRecordKey() {
		return this.recordKey;
	}
	
	public void setRecordKey(int recordKey) {
		this.recordKey = recordKey;
	}
	
	public String getRecordDate() {
		return this.recordDate;
	}
	
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	
	public int getUserKey() {
		return this.userKey;
	}
	
	public void setUserKey(int userKey) {
		this.userKey = userKey;
	}
	
	public int getFood() {
		return this.food;
	}
	
	public void setFood(int food) {
		this.food = food;
	}
	
	public int getClothes() {
		return this.clothes;
	}
	
	public void setClothes(int clothes) {
		this.clothes = clothes;
	}
	
	public int getEntertainment() {
		return this.entertainment;
	}
	
	public void setEntertainment(int entertainment) {
		this.entertainment = entertainment;
	}
	
	public int getAccommodation() {
		return this.accommodation;
	}
	
	public void setAccommodation(int accommodation) {
		this.accommodation = accommodation;
	}
	
	public int getTransportation() {
		return this.transportation;
	}
	
	public void setTransportation(int transportation) {
		this.transportation = transportation;
	}
	
}