package com.example.service;

import java.util.List;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PlotIFImpl implements PlotIF {
	
	private static final Logger log = LoggerFactory.getLogger(PlotIFImpl.class);
	
	private static final String SPACE = " ";
	
	private static final String COMMA = ",";
	
	private static String generateCoordinatesString(List<Integer> coordinates) {
        StringBuilder builder = new StringBuilder();
        for (int number : coordinates) {
            builder.append(COMMA).append(number);
        }
        return builder.substring(COMMA.length());
    }
	
	/**
	 * 製作python腳本所需要的數據文件
	 * */
	@Override
	public String generateCoordinatesFile(String filePath,
			List<Integer> food, List<Integer> clothes, List<Integer> entertainment,
            List<Integer> accommodation, List<Integer> transportation, List<Integer> ratio) {
        String foodString = generateCoordinatesString(food);
        String clothesString = generateCoordinatesString(clothes);
        String entertainmentString = generateCoordinatesString(entertainment);
        String accommodationString = generateCoordinatesString(accommodation);
        String transportationString = generateCoordinatesString(transportation);
        String ratioString = generateCoordinatesString(ratio);
        String nextLine = "\n";
        String content =
        		foodString + nextLine +
                clothesString + nextLine +
                entertainmentString + nextLine +
                accommodationString + nextLine +
                transportationString + nextLine +
                ratioString;
        Path path = Paths.get(filePath);
        try {
            Files.write(path, content.getBytes(StandardCharsets.UTF_8));
            return filePath;
        } catch (IOException ioe) {
            log.error("Unable to generate coordinates file", ioe);
            return "";
        }
    }
	
	/**
	 * 利用shell command執行python腳本<br/>
	 * 執行完python腳本後將產生圖片，回傳該圖片的路徑
	 * */
	@Override
	public String drawPlot(String scriptPath, String coordinatePath, String imagePath,
			List<Integer> food, List<Integer> clothes, List<Integer> entertainment,
			List<Integer> accommodation, List<Integer> transportation, List<Integer> ratio) {
		String command = "python3" + SPACE + scriptPath + SPACE + coordinatePath + SPACE + imagePath;
		try {
			Runtime.getRuntime().exec(command);
			return imagePath;
		} catch (IOException ioe) {
			log.error("Unable to draw plot", ioe);
			return "";
		}
	}
	
}
