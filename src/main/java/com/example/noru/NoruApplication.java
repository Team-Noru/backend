package com.example.noru;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NoruApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoruApplication.class, args);
	}

}
