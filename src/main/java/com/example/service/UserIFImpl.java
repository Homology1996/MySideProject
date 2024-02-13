package com.example.service;

import com.example.model.User;
import com.example.dao.UserDaoIF;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserIFImpl implements UserIF {
	
	private static final Logger log = LoggerFactory.getLogger(UserIFImpl.class);
	
	@Autowired
	private UserDaoIF userDao;
	
	@Override
	public User loadUserByUserKey(int userKey) {
		User user = null;
		try {
			user = userDao.loadUserByUserKey(userKey);
		} catch (Exception e) {
			log.error("Cannot load user", e);
		}
		return user;
	}
	
	@Override
	public List<User> loadAllUsers() {
		List<User> allUsers = null;
		try {
			allUsers = userDao.loadAllUsers();
		} catch (Exception e) {
			log.error("Cannot load all users", e);
		}
		return allUsers;
	} 
	
	@Override
	@Transactional
	public void createUser(int userKey, String account, String password) {
		userDao.createUser(userKey, account, password);
		log.info(String.format("(userKey, account) = (%s, %s) created.", userKey, account));
	}
	
	@Override
	@Transactional
	public void updateUser(int userKey, String password) {
		userDao.updateUser(userKey, password);
		log.info(String.format("userKey = %s updated.", userKey));
	}
	
}