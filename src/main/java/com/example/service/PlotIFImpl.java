package com.example.service;

import java.util.UUID;
import java.io.IOException;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PlotIFImpl implements PlotIF {
	
	private static final Logger log = LoggerFactory.getLogger(PlotIFImpl.class);
	
	private static final String SPACE = " ";
	
	private static String generateCoordinatesString(int... coordinates) {
        StringBuilder builder = new StringBuilder();
        for (int number : coordinates) {
            builder.append(SPACE).append(number);
        }
        return builder.toString().trim();
    }
	
	/**
	 * 利用shell command執行python腳本<br/>
	 * 執行完python腳本後將產生圖片，回傳該圖片的路徑
	 * */
	public String drawPlot(int... coordinates) {
		String uuid = UUID.randomUUID().toString();
		String currentWorkingDirectory = Paths.get("").toAbsolutePath().normalize().toString();
		String scriptPath = currentWorkingDirectory + "/src/main/resources/static/scripts/drawPlot.py";
		String imagePath = currentWorkingDirectory + "/images/" + uuid + ".png";
		String command = "python3" + SPACE + 
				scriptPath + SPACE + imagePath + SPACE + generateCoordinatesString(coordinates);
		try {
			Runtime.getRuntime().exec(command);
			return imagePath;
		} catch (IOException ioe) {
			log.error("Unable to drow plot", ioe);
			return "";
		}
	}
	
}
