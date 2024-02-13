package com.example.controller;

import com.example.db.GenerateKey;
import com.example.service.RecordIF;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class ControllerBase {
	@Autowired
	protected GenerateKey generateKey;
	
	@Autowired
	protected HttpSession httpSession;	// 在伺服器端檢查使用者的登入狀態
	
	@Autowired
	private RecordIF recordIF;
	
	/**
	 * 檢查使用者是否登入
	 * */
	public boolean isLogin(int userKey) {
		Object session = httpSession.getAttribute("user_" + userKey);
		return session != null;
	}
	
	/**
	 * 輸入使用者基本資訊與相關資料。在前往會員中心時使用
	 * */
	public void setupUserKeyAndRecords(Model model, int userKey) {
		// 要把變數從後端傳送到前端時，就需要使用model
		model.addAttribute("userKey", userKey);
		model.addAttribute("records", recordIF.loadRecordsByUserKey(userKey));
	}
	
	/**
	 * 設置未登入的錯誤訊息
	 * */
	public String setupNotLoginMessage(RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("notLogin", true);
		return "redirect:/";
	}
	
	/**
	 * 重新導向至會員中心
	 * */
	public String redirectToMember(RedirectAttributes redirectAttributes, int userKey) {
		redirectAttributes.addAttribute("userKey", userKey);
		return "redirect:/member";
	}
	
}