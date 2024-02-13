package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

// 加上Scan之後才會去讀取指定的package
@ComponentScan({"com.example.controller", "com.example.dao", "com.example.dao.mapper", "com.example.dao.mapper",
	"com.example.db", "com.example.demo", "com.example.model", "com.example.service"})
@EntityScan({"com.example.controller", "com.example.dao", "com.example.dao.mapper", "com.example.dao.mapper",
	"com.example.db", "com.example.demo", "com.example.model", "com.example.service"})
@SpringBootApplication
public class MySideProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySideProjectApplication.class, args);
	}

}