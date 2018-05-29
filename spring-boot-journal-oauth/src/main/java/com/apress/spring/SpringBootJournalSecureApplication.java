package com.apress.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.apress.spring.service.JournalService;

/*
 * To test:
 * http://localhost:8080/ should be ok to access
 * 
 * http://localhost:8080/api will require full authentication
 * 
 * Try the following command with username, password from db and clientId and client-secret defined in property file:
 * curl -i localhost:8080/oauth/token -d "grant_type=password&scope=read&username=springboot&password=isawesome" -u trustedclient:trustedclient123
 * 
 * It will return access token and refresh token
 * 
 * Next Try the following command with the new access token (replace 0eae474b-32ef-4c29-b39e-81c63114ee65)
 * curl -i -H "Authorization: bearer 0eae474b-32ef-4c29-b39e-81c63114ee65" localhost:8080/api
 */
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
