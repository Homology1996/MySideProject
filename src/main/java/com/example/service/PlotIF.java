package com.example.service;

import java.util.List;

public interface PlotIF {

	String generateCoordinatesFile(String filePath,
			List<Integer> food, List<Integer> clothes, List<Integer> entertainment,
            List<Integer> accommodation, List<Integer> transportation, List<Integer> ratio);
	
	String drawPlot(String scriptPath, String coordinatePath, String imagePath,
			List<Integer> food, List<Integer> clothes, List<Integer> entertainment,
			List<Integer> accommodation, List<Integer> transportation, List<Integer> ratio);
}
