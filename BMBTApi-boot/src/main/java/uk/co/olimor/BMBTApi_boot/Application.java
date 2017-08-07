package uk.co.olimor.BMBTApi_boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"uk.co.olimor.BMBTApi_boot.controller", 
		"uk.co.olimor.BMBTApi_boot.dao", "uk.co.olimor.BMBTApi_boot.config"})
@SpringBootApplication
public class Application {
	
	public static void main (String[] args) {
		System.out.println("Hello");
		try {
		SpringApplication.run(Application.class, args);
		} catch (Exception e) {
			System.out.println("Error");
			e.printStackTrace();
		}
		System.out.println("Goodbye");
	}

}
