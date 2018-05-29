package com.apress.spring.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apress.spring.domain.JournalEntry;
import com.apress.spring.repository.JournalRepository;

@Service
public class JournalService {
	private static final Logger log = LoggerFactory.getLogger(JournalService.class);
	
	@Autowired
	JournalRepository repo;
	
	public List<JournalEntry> findAll(){
		return repo.findAll();
	}
	
	/*
	public JournalEntry findById(long id){
		return repo.findById(id);
	}*/
	
	public List<JournalEntry> findByTitleContaining(String word){
		return repo.findByTitleContaining(word);
	}
	
	public List<JournalEntry> findBySummaryContaining(String word){
		return repo.findBySummaryContaining(word);
	}
	
	public List<JournalEntry> findByCreatedAfter(Date date){
		return repo.findByCreatedAfter(date);
	}
	
	public void insertData() throws ParseException{
		log.info("> Inserting data...");
		repo.save(new JournalEntry("Get to know Spring Boot","Today I will learn Spring Boot","2016-01-02 00:00:00.00"));
		repo.save(new JournalEntry("Simple Spring Boot Project","I will do my first Spring Boot Project","2016-03-03 00:00:00.00"));
		repo.save(new JournalEntry("Spring Boot Reading","Read more about Spring Boot","2016-02-01 00:00:00.00"));
		repo.save(new JournalEntry("Spring Boot in the Cloud","Spring Boot using Cloud Foundry","2016-04-04 00:00:00.00"));
		log.info("> Done.");
	}
}