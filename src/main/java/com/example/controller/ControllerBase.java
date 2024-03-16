package com.example.controller;

import com.example.db.GenerateKey;
import com.example.service.RecordIF;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class ControllerBase {
	
	private static final Logger log = LoggerFactory.getLogger(ControllerBase.class);
	
	private static final int GET_FILE_TIME_LIMIT = 1000 * 5; // 5秒
	
	public static final String WORKING_DIRECTORY = Paths.get("").toAbsolutePath().normalize().toString();
	
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
	
	/**
	 * 當檔案存在時，取得檔案的絕對路徑
	 * */
	public String getFilePathWhenFileExists(File file) {
		boolean exists = false;
        long start = System.currentTimeMillis(), end = start;
        while (System.currentTimeMillis() <= start + GET_FILE_TIME_LIMIT) {
            if (file.exists()) {
                exists = true;
                end = System.currentTimeMillis();
                break;
            }
        }
        if (exists) {
            log.info("Get file starting at: " + start);
            log.info("Get file ending at: " + end);
            log.info("Get file spending time: " + (end - start));
            return file.getAbsolutePath();
        } else {
        	log.error("Unable to get file");
        	return "";
        }
	}
	
}