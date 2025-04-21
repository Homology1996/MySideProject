package com.example.controller;

import com.example.Constants;
import com.example.model.User;
import com.example.service.UserIF;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 									// 傳送後端的變數到前端使用
import org.springframework.web.bind.annotation.GetMapping;				// 由前端傳送資料到後端使用的mapping
import org.springframework.web.bind.annotation.PostMapping;				// 由前端傳送資料到後端使用的mapping
import org.springframework.web.bind.annotation.PathVariable;			// url的變數，放在?前面
import org.springframework.web.bind.annotation.RequestParam;			// url的變數，放在?後面
import org.springframework.beans.factory.annotation.Autowired;			// springframework注入變數
import org.springframework.web.servlet.mvc.support.RedirectAttributes;	// 重新導向頁面時，可以傳送變數

@Controller
public class UserController extends ControllerBase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserIF userIF;
	
	/**
	 * 前往首頁畫面，這裡只能登入或是註冊會員
	 * */
	@GetMapping("/")
	public String toIndex() {
		return "index";
	}
	
	/**
	 * 按下登入按鍵後，判斷成功與否來決定接下來的走向
	 * */
	@PostMapping("/login")
	public String toLogin(Model model, RedirectAttributes redirectAttributes,
			@RequestParam(required = true) String account,
			@RequestParam(required = false) String password) {
		List<User> allUsers = userIF.loadAllUsers();
		Optional<User> currentUser = allUsers.stream().filter(user ->
			user.getAccount().equals(account) && userIF.checkPassword(password, user.getPassword())).findFirst();
		if (currentUser.isPresent()) {
			int userKey = currentUser.get().getUserKey();
			String sessionAttributeForUser = super.getSessionAttributeForUser(userKey);
			model.addAttribute("userKey", userKey); // 要把變數從後端傳送到前端時，就需要使用model
			super.httpSession.setAttribute(sessionAttributeForUser, account);
			LOGGER.info(sessionAttributeForUser + " login");
			return "member";
		} else {
			/*
			 * 如果是要重新導向，那麼model的變數會無法傳送到前端(model使用的是forward，不是redirect)
			 * 因此這裡就要改成使用redirectAttributes
			 * 接著就根據使用情境來使用addAttribute或是addFlashAttribute
			 * addAttribute: 建立一個request parameter，作為重新導向時的變數使用
			 * 可以使用的型態只有String與primitives，通常是用在後端變數使用
			 * addFlashAttribute: 在使用者的session建立一個flashmap，作為當前session的變數使用
			 * 在接受到下一個redirect request時，就會移除這個變數
			 * 基本上任何型態都可以使用，通常是作為前端變數使用
			 * */
			redirectAttributes.addFlashAttribute("wrongUserInfo", true);
			return "redirect:/";
		}
	}
	
	/**
	 * 清除session並且登出，然後回到首頁
	 * */
	@PostMapping("/logout")
	public String toLogout(Model model,
			@RequestParam(required = true) int userKey) {
		String sessionAttributeForUser = super.getSessionAttributeForUser(userKey);
		Object session = super.httpSession.getAttribute(sessionAttributeForUser);
		if (session != null) {
			super.httpSession.removeAttribute(sessionAttributeForUser);
			LOGGER.info(sessionAttributeForUser + " logout");
		}
		return "redirect:/";
	}
	
	/**
	 * 前往註冊畫面
	 * */
	@GetMapping("/register")
	public String toRegister() {
		return "register";
	}
	
	/**
	 * 按下註冊按鈕後，檢查帳號是否重複
	 * */
	@PostMapping("/register/check")
	public String registerCheck(Model model, RedirectAttributes redirectAttributes,
			@RequestParam(required = true) String account,
			@RequestParam(required = false) String password) {
		List<User> allUsers = userIF.loadAllUsers();
		boolean isAccountUnique = allUsers.stream().noneMatch(user -> user.getAccount().equals(account));
		if (isAccountUnique && !account.isBlank()) {
			int newUserKey = super.generateKey.generate(Constants.USER_KEY_SEQUENCE);
			String sessionAttributeForUser = super.getSessionAttributeForUser(newUserKey);
			userIF.createUser(newUserKey, account, password);
			super.httpSession.setAttribute(sessionAttributeForUser, account);
			LOGGER.info(sessionAttributeForUser + " login");
			return super.redirectToMember(redirectAttributes, newUserKey);
			// addAttribute是作為後端的request parameter使用
		} else {
			redirectAttributes.addFlashAttribute("error", true);
			return "redirect:/register";
			// addFlashAttribute是作為前端的變數使用
		}
	}
	
	/**
	 * 更改會員資料
	 * */
	@PostMapping("/user/update")
	public String updateUser(Model model, RedirectAttributes redirectAttributes,
			@RequestParam(required = true) int userKey,
			@RequestParam(required = false) String password) {
		userIF.updateUser(userKey, password);
		redirectAttributes.addFlashAttribute("updateUserSuccessful", true);
		return super.redirectToMember(redirectAttributes, userKey);
	}
	
}