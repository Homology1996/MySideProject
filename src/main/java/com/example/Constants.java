package com.example;

import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class Constants {
	
	private Constants() {}
	
	public static final int GET_FILE_TIME_LIMIT = 1000 * 10;
	
	public static final String DOT = ".";
	
	public static final String EMPTY = "";
	
	public static final String SPACE = " ";
	
	public static final String COMMA = ",";
	
	public static final String NEXT_LINE = "\n";
	
	public static final String UNDERSCORE = "_";
	
	public static final String WORKING_DIRECTORY = Paths.get(EMPTY).toAbsolutePath().normalize().toString();
	
	public static final String LOG_FOLDER = "logs";
	
	public static final String LOG_FILE_NAME = "myLog4j2" + DOT + "log";
	
	public static final String USER_SESSION_HEADER = "user" + UNDERSCORE;
	
	public static final String USER_KEY_SEQUENCE = "user" + UNDERSCORE + "key";
	
	public static final String RECORD_KEY_SEQUENCE = "daily" + UNDERSCORE + "record";
	
	public static final String DRAW_PLOT_SCRIPT = "drawPlot" + DOT + "py";
	
	public static final String TEXT_EXTENSION = DOT + "txt";
	
	public static final String PNG_EXTENSION = DOT + "png";
	
	public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy/MM/dd");
	
	public static final NumberFormat NUMBER_FORMATTER = new DecimalFormat("###,000.###");
	
}