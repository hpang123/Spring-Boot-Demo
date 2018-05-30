package com.apress.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/*
 * 
 * https://localhost:8443/
 * Then click Advance and continue process to localhost(unsafe) in browser
 * In login page, type spring/boot
 * 
 * To create executable Jar:
 * mvn package
 * 
 * java -jar target/spring-boot-journal-0.0.1-SNAPSHOT.jar
 * 
 * To create executable war:
 * Change package jar to war:
 * <packaging>war</packaging>
 * 
 * java -jar target/spring-boot-journal-0.0.1-SNAPSHOT.war
 */
@SpringBootApplication
public class SpringBootJournalApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringBootJournalApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJournalApplication.class, args);
	}
}
