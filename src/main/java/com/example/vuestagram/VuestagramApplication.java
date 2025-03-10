package com.example.vuestagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class VuestagramApplication {

	public static void main(String[] args) {
		SpringApplication.run(VuestagramApplication.class, args);
	}

}
