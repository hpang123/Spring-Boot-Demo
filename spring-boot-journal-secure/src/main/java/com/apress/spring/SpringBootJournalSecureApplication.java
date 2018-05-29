package com.apress.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.apress.spring.service.JournalService;

@SpringBootApplication
public class SpringBootJournalSecureApplication {
	private static final Logger log = LoggerFactory.getLogger(SpringBootJournalSecureApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJournalSecureApplication.class, args);
	}
	
	@Bean
	InitializingBean saveData(JournalService service){
		return () -> {
			log.info("@@ Inserting Data....");
			/*
			 * spring-data enables you to use the schema.sql and data.sql files (in the root of the classpath) 
			 * to create the database and insert data.
			 * If we have data.sql with insert statement, we don't need manually insert data here
			 */
			service.insertData();
		};
	}
}
