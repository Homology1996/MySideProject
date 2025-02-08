package com.example.service;

import java.util.List;

public interface PlotIF {
	
	/**
	 * 製作python腳本所需要的數據文件
	 * */
	String generateCoordinatesFile(String filePath,
			List<Integer> food, List<Integer> clothes, List<Integer> entertainment,
            List<Integer> accommodation, List<Integer> transportation, List<Integer> ratio);
	
	/**
	 * 利用shell command執行python腳本<br/>
	 * 執行完python腳本後將產生圖片，回傳該圖片的路徑
	 * */
	String drawPlot(String scriptPath, String coordinatePath, String imagePath,
			List<Integer> food, List<Integer> clothes, List<Integer> entertainment,
			List<Integer> accommodation, List<Integer> transportation, List<Integer> ratio);
	
}