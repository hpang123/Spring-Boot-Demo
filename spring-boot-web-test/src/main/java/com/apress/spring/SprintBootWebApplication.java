package com.apress.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * http://localhost:8080/journal/all
 * http://localhost:8080/journal/findBy/title/cloud
 * 
 * $ curl -X POST -d '{"title":"Test Spring Boot","created":"06/18/2016","summary":"Create UnitTest for Spring Boot"}' -H "Content-Type: application/json" http://localhost:8080/journal
 */
@SpringBootApplication
public class SprintBootWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SprintBootWebApplication.class, args);
	}
}
