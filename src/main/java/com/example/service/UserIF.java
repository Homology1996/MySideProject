package com.example.service;

import java.util.List;
import com.example.model.User;

public interface UserIF {

	User loadUserByUserKey(int userKey);
	
	List<User> loadAllUsers();
	
	void createUser(int userKey, String account, String password);
	
	void updateUser(int userKey, String password);
	
}