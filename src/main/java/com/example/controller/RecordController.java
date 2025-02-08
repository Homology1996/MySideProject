package com.example.controller;

import com.example.Constants;
import com.example.model.Record;
import com.example.service.PlotIF;
import com.example.service.RecordIF;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RecordController extends ControllerBase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RecordController.class);
	
	@Autowired
	private RecordIF recordIF;
	
	@Autowired
	private PlotIF plotIF;
	
	/**
	 * 進入會員中心，並顯示相關資訊
	 * */
	@GetMapping("/member")
	public String toMember(Model model, RedirectAttributes redirectAttributes,
			@RequestParam(value = "userKey", required = true) int userKey) {
		if (!super.isLogin(userKey)) {
			return super.setupNotLoginMessage(redirectAttributes);
		}
		model.addAttribute("userKey", userKey);
		return "member";
	}
	
	/**
	 * 取得表格所需資料
	 * */
	@GetMapping("/record/show/{userKey}")
	@ResponseBody
	public String getRecordsForPage(@PathVariable(value = "userKey", required = true) int userKey) {
		/*
		 * 此方法示範了幾個概念
		 * 1: 在thymeleaf使用ajax
		 * 2: 在thymeleaf傳送的url裡面使用path variable
		 * 3: 在springboot接收的參數使用path variable
		 * 4: 在springboot回傳結果不是以往的頁面url，而是json字串
		 * */
		return recordIF.transformRecordsIntoJSONArray(recordIF.loadRecordsByUserKey(userKey)).toString();
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
		if (recordDate.isBlank()) {
			redirectAttributes.addFlashAttribute("noEmptyDate", true);
		} else {
			recordIF.createRecord(super.generateKey.generate(Constants.RECORD_KEY_SEQUENCE), recordDate, userKey,
					food, clothes, entertainment, accommodation, transportation);
			redirectAttributes.addFlashAttribute("createRecordSuccessful", true);
		}
		return super.redirectToMember(redirectAttributes, userKey);
	}
	
	/**
	 * 更新單筆資料紀錄
	 * */
	@PostMapping("/record/update")
	@ResponseBody
	public String updateRecord(
			@RequestParam(value = "recordKey", required = true) int recordKey,
			@RequestParam(value = "recordDate", required = true) String recordDate,
			@RequestParam(value = "userKey", required = true) int userKey,
			@RequestParam(value = "food", required = false, defaultValue = "0") int food,
			@RequestParam(value = "clothes", required = false, defaultValue = "0") int clothes,
			@RequestParam(value = "entertainment", required = false, defaultValue = "0") int entertainment,
			@RequestParam(value = "accommodation", required = false, defaultValue = "0") int accommodation,
			@RequestParam(value = "transportation", required = false, defaultValue = "0") int transportation) {
		recordIF.updateRecord(recordKey, recordDate, userKey,
				food, clothes, entertainment, accommodation, transportation);
		return "updateSuccessful";
	}
	
	private int getRatioInt(Map<String, String> statistics, String name) {
		return Integer.parseInt(Long.toString(Math.round(Double.parseDouble(statistics.get(name)))));
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
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		Map<String, String> statistics = recordIF.getStatistics(startDate, endDate, userKey);
		if (statistics == null || statistics.size() == 0) {
			redirectAttributes.addFlashAttribute("noStatisticsAvailable", true);
			return super.redirectToMember(redirectAttributes, userKey);
		} else {
			model.addAttribute("statistics", statistics);
			List<Record> records = recordIF.loadRecordsByUserKey(userKey, startDate, endDate);
			List<Integer> food = new LinkedList<>();
			List<Integer> clothes = new LinkedList<>();
			List<Integer> entertainment = new LinkedList<>();
			List<Integer> accommodation = new LinkedList<>();
			List<Integer> transportation = new LinkedList<>();
			List<Integer> ratio = new LinkedList<>();
			for (Record record : records) {
				food.add(record.getFood());
				clothes.add(record.getClothes());
				entertainment.add(record.getEntertainment());
				accommodation.add(record.getAccommodation());
				transportation.add(record.getTransportation());
			}
			ratio.add(this.getRatioInt(statistics, "foodRatio"));
			ratio.add(this.getRatioInt(statistics, "clothesRatio"));
			ratio.add(this.getRatioInt(statistics, "entertainmentRatio"));
			ratio.add(this.getRatioInt(statistics, "accommodationRatio"));
			ratio.add(this.getRatioInt(statistics, "transportationRatio"));
			String uuid = UUID.randomUUID().toString(), seperator = File.separator;
			String scriptPath = Constants.WORKING_DIRECTORY + seperator + "src" + seperator + "main" + seperator +
					"resources" + seperator + "static" + seperator + "scripts" + seperator + Constants.DRAW_PLOT_SCRIPT;
			String uuidPrefix = Constants.WORKING_DIRECTORY + seperator + "images" + seperator + uuid;
			String coordinate = plotIF.generateCoordinatesFile(uuidPrefix + Constants.TEXT_EXTENSION, food, clothes,
					entertainment, accommodation, transportation, ratio);
			String coordinatePath = super.getFilePathWhenFileExists(new File(coordinate));
			String image = plotIF.drawPlot(scriptPath, coordinatePath, uuidPrefix + Constants.PNG_EXTENSION, food, clothes,
					entertainment, accommodation, transportation, ratio);
			String imagePath = super.getFilePathWhenFileExists(new File(image));
			model.addAttribute("coordinatePath", coordinatePath);
			model.addAttribute("imagePath", imagePath);
			return "statistics";
		}
	}
	
	/**
	 * 取得數據圖
	 * */
	@GetMapping("/statistics/plot")
	@ResponseBody
	public String plot(@RequestParam(value = "imagePath", required = true) String imagePath,
			@RequestParam(value = "coordinatePath", required = true) String coordinatePath) {
		JSONObject result = new JSONObject();
		Path path = Paths.get(imagePath);
		String base64 = null;
		try {
			byte[] fileContent = Files.readAllBytes(path);
			byte[] encodedContent = Base64.getEncoder().encode(fileContent);
	        base64 =  new String(encodedContent);
		} catch (IOException ioe) {
			LOGGER.error("Unable to get file content", ioe);
			result.put("IOE", ioe.toString());
		}
		if (base64 == null || base64.isBlank()) {
			result.put("imgSrc", Constants.EMPTY);
		} else {
			result.put("imgSrc", "data:image/png;base64, " + base64);
			File image = path.toFile();
			if (image.exists()) {
				if (image.delete()) {
					LOGGER.debug("image deleted");
				}
			}
			File coordinate = new File(coordinatePath);
			if (coordinate.exists()) {
				if (coordinate.delete()) {
					LOGGER.debug("coordinate deleted");
				}
			}
		}
		return result.toString();
	}

}