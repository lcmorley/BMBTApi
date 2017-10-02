package uk.co.olimor.BMBTApi_boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@EnableCaching
@ComponentScan(basePackages = {"uk.co.olimor.BMBTApi_boot.controller", 
		"uk.co.olimor.BMBTApi_boot.dao", "uk.co.olimor.BMBTApi_boot.config", "uk.co.olimor.BMBTApi_boot.builder", 
		"uk.co.olimor.BMBTApi_boot.security", " uk.co.olimor.BMBTApi_Common.security"})
@SpringBootApplication
public class Application {
	
	public static void main (String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
