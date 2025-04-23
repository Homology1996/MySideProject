package com.example.demo;

import com.example.Constants;
import java.io.File;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

// 加上Scan之後才會去讀取指定的package
@ComponentScan({"com.example.controller", "com.example.dao", "com.example.dao.mapper",
	"com.example.db", "com.example.demo", "com.example.model", "com.example.service"})
@EntityScan({"com.example.controller", "com.example.dao", "com.example.dao.mapper",
	"com.example.db", "com.example.demo", "com.example.model", "com.example.service"})
@SpringBootApplication
public class MySideProjectApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		// 設定log位置
		System.setProperty("logFilename", Constants.WORKING_DIRECTORY +
				File.separator + Constants.LOG_FOLDER + File.separator + Constants.LOG_FILE_NAME);
		SpringApplication.run(MySideProjectApplication.class, args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MySideProjectApplication.class);
    }

}