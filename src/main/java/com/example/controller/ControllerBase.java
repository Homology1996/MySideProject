package com.example.controller;

import com.example.Constants;
import com.example.db.GenerateKey;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class ControllerBase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerBase.class);
	
	@Autowired
	protected GenerateKey generateKey;
	
	/**
	 * 在伺服器端檢查使用者的登入狀態
	 * */
	@Autowired
	protected HttpSession httpSession;
	
	public String getSessionAttributeForUser(int userKey) {
		return Constants.USER_SESSION_HEADER + userKey;
	}
	
	/**
	 * 檢查使用者是否登入
	 * */
	public boolean isLogin(int userKey) {
		Object session = httpSession.getAttribute(this.getSessionAttributeForUser(userKey));
		return session != null;
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
		AtomicBoolean exists = new AtomicBoolean(false);
        AtomicLong start = new AtomicLong(System.currentTimeMillis()), end = new AtomicLong(start.get());
        
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(() -> {
        	boolean canTerminate = false;
        	if (file.exists() && file.length() > 0) {
        		canTerminate = true;
        		exists.set(true);
        		end.set(System.currentTimeMillis());
        	} else {
        		canTerminate = System.currentTimeMillis() > start.get() + Constants.GET_FILE_TIME_LIMIT;
        	}
        	if (canTerminate) {
        		countDownLatch.countDown();
        	}
        }, 0, 500, TimeUnit.MILLISECONDS);
        
        try {
            countDownLatch.await(); // 主執行緒呼叫await進入等待狀態，等到CountDownLatch歸零時才會開始動作
        } catch (InterruptedException e) {
            LOGGER.error("Unable to get file", e);
        } finally {
            service.shutdown();
        }
        
        if (exists.get()) {
        	String filePath = file.getAbsolutePath();
        	long startTime = start.get(), endTime = end.get();
        	LOGGER.info("Get " + filePath + " starting at: " + startTime);
        	LOGGER.info("Get " + filePath + " ending at: " + endTime);
        	LOGGER.info("Get " + filePath + " spending time: " + (endTime - startTime));
            return filePath;
        } else {
        	LOGGER.error("Unable to get file");
        	return Constants.EMPTY;
        }
	}
	
}