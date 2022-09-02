package com.udacity.jwdnd.course1.cloudstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CloudStorageApplication {

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/login");
		SpringApplication.run(CloudStorageApplication.class, args);
	}

}
