package com.example.service;

import com.example.model.User;
import com.example.dao.UserDaoIF;
import java.util.LinkedList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserIFImpl implements UserIF {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserIFImpl.class);
	
	@Autowired
	private UserDaoIF userDao;
	
	@Override
	public User loadUserByUserKey(int userKey) {
		User user = null;
		try {
			user = userDao.loadUserByUserKey(userKey);
		} catch (Exception e) {
			LOGGER.error("Cannot load user", e);
		}
		return user;
	}
	
	@Override
	public List<User> loadAllUsers() {
		List<User> allUsers = new LinkedList<>();
		try {
			allUsers = userDao.loadAllUsers();
		} catch (Exception e) {
			LOGGER.error("Cannot load all users", e);
		}
		return allUsers;
	}
	
	@Override
	@Transactional
	public void createUser(int userKey, String account, String password) {
		userDao.createUser(userKey, account, BCrypt.hashpw(password, BCrypt.gensalt()));
		LOGGER.info(String.format("(userKey, account) = (%s, %s) created.", userKey, account));
	}
	
	@Override
	@Transactional
	public void updateUser(int userKey, String password) {
		userDao.updateUser(userKey, BCrypt.hashpw(password, BCrypt.gensalt()));
		LOGGER.info(String.format("userKey = %s updated.", userKey));
	}
	
	@Override
	public boolean checkPassword(String password, String encodedPassword) {
		return BCrypt.checkpw(password, encodedPassword);
	}
	
}