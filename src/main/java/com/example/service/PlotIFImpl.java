package com.example.service;

import com.example.Constants;
import java.util.List;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PlotIFImpl implements PlotIF {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PlotIFImpl.class);
	
	private static String generateCoordinatesString(List<Integer> coordinates) {
        StringBuilder builder = new StringBuilder();
        for (int number : coordinates) {
            builder.append(Constants.COMMA).append(number);
        }
        return builder.substring(Constants.COMMA.length());
    }
	
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
        String content =
        		foodString + Constants.NEXT_LINE +
                clothesString + Constants.NEXT_LINE +
                entertainmentString + Constants.NEXT_LINE +
                accommodationString + Constants.NEXT_LINE +
                transportationString + Constants.NEXT_LINE +
                ratioString;
        try {
        	Files.write(Paths.get(filePath), content.getBytes(StandardCharsets.UTF_8));
        	return filePath;
        } catch (IOException ioe) {
        	LOGGER.error("Unable to generate coordinates file", ioe);
        	return Constants.EMPTY;
        }
    }
	
	@Override
	public String drawPlot(String scriptPath, String coordinatePath, String imagePath,
			List<Integer> food, List<Integer> clothes, List<Integer> entertainment,
			List<Integer> accommodation, List<Integer> transportation, List<Integer> ratio) {
		String command = "python3" + Constants.SPACE + scriptPath + Constants.SPACE + coordinatePath + Constants.SPACE + imagePath;
		try {
			Runtime.getRuntime().exec(command);
			return imagePath;
		} catch (IOException ioe) {
			LOGGER.error("Unable to draw plot", ioe);
			return Constants.EMPTY;
		}
	}
	
}