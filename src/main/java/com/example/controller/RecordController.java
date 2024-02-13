package com.example.controller;

import com.example.service.RecordIF;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RecordController extends ControllerBase {
	
	private static final Logger log = LoggerFactory.getLogger(RecordController.class);
	
	@Autowired
	private RecordIF recordIF;
	
	/**
	 * 進入會員中心，並顯示相關資訊
	 * */
	@GetMapping("/member")
	public String toMember(Model model, RedirectAttributes redirectAttributes,
			@RequestParam(value = "userKey", required = true) int userKey) {
		if (!super.isLogin(userKey)) {
			return super.setupNotLoginMessage(redirectAttributes);
		}
		super.setupUserKeyAndRecords(model, userKey);
		return "member";
	}
	
	/**
	 * 建立單筆資料紀錄
	 * */
	@PostMapping("/record/create")
	public String createRecord(Model model, RedirectAttributes redirectAttributes,
			@RequestParam(value = "recordDate", required = true) String recordDate,
			@RequestParam(value = "userKey", required = true) int userKey,
			@RequestParam(value = "food", required = false, defaultValue = "0") int food,
			@RequestParam(value = "clothes", required = false, defaultValue = "0") int clothes,
			@RequestParam(value = "entertainment", required = false, defaultValue = "0") int entertainment,
			@RequestParam(value = "accommodation", required = false, defaultValue = "0") int accommodation,
			@RequestParam(value = "transportation", required = false, defaultValue = "0") int transportation) {
		if (!super.isLogin(userKey)) {
			return super.setupNotLoginMessage(redirectAttributes);
		}
		if (recordDate.isBlank()) {
			redirectAttributes.addFlashAttribute("noEmptyDate", true);
		} else {
			recordIF.createRecord(super.generateKey.generate("daily_record"), recordDate, userKey,
					food, clothes, entertainment, accommodation, transportation);
			redirectAttributes.addFlashAttribute("createRecordSuccessful", true);
		}
		return super.redirectToMember(redirectAttributes, userKey);
	}
	
	/**
	 * 更新單筆資料紀錄
	 * */
	@GetMapping("/record/update")
	public String updateRecord(Model model, RedirectAttributes redirectAttributes,
			@RequestParam(value = "recordKey", required = true) int recordKey,
			@RequestParam(value = "recordDate", required = true) String recordDate,
			@RequestParam(value = "userKey", required = true) int userKey,
			@RequestParam(value = "food", required = false, defaultValue = "0") int food,
			@RequestParam(value = "clothes", required = false, defaultValue = "0") int clothes,
			@RequestParam(value = "entertainment", required = false, defaultValue = "0") int entertainment,
			@RequestParam(value = "accommodation", required = false, defaultValue = "0") int accommodation,
			@RequestParam(value = "transportation", required = false, defaultValue = "0") int transportation) {
		if (!super.isLogin(userKey)) {
			return super.setupNotLoginMessage(redirectAttributes);
		}
		recordIF.updateRecord(recordKey, recordDate, userKey,
				food, clothes, entertainment, accommodation, transportation);
		redirectAttributes.addFlashAttribute("updateRecordSuccessful", true);
		return super.redirectToMember(redirectAttributes, userKey);
	}
	
	/**
	 * 顯示統計資料
	 * */
	@GetMapping("/statistics")
	public String toStatistics(Model model, RedirectAttributes redirectAttributes,
			@RequestParam(value = "userKey", required = true) int userKey, 
			@RequestParam(value = "startDate", required = true) String startDate,
			@RequestParam(value = "endDate", required = true) String endDate) {
		if (!super.isLogin(userKey)) {
			return super.setupNotLoginMessage(redirectAttributes);
		}
		model.addAttribute("userKey", userKey);
		Map<String, String> statistics = recordIF.getStatistics(startDate, endDate, userKey);
		if (statistics == null || statistics.size() == 0) {
			redirectAttributes.addFlashAttribute("noStatisticsAvailable", true);
			return super.redirectToMember(redirectAttributes, userKey);
		} else {
			model.addAttribute("statistics", statistics);
			return "statistics";
		}
	}
	
}