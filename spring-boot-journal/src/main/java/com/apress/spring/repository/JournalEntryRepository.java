package com.apress.spring.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.transaction.annotation.Transactional;

import com.apress.spring.domain.JournalEntry;

/*
 * 
 * http://localhost:8080/journalEntries return HATEOAS 
 * because of spring-boot-starter-data-rest
 * 
 * You also have the GET, POST, PUT, PATCH, and DELETE HTTP methods,
 * 
 * http://localhost:8080/journalEntries/search will show apis defined:
 * http://localhost:8080/journalEntries/search/findByCreatedAfter?after=2016-02-01 
 * http://localhost:8080/journalEntries/search/findByCreatedBetween?after=2016-02-01&before=2016-03-01
 * http://localhost:8080/journalEntries/search/findBySummaryContaining?word=Today
 * http://localhost:8080/journalEntries/search/findByTitleContaining?word=Cloud
 * 
 */

@Transactional
/*
 * If you want to change path name or rel collection name, use @RepositoryRestResource
 */
//@RepositoryRestResource(collectionResourceRel = "entry", path = "journal1")
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
	/*
	 * @Param has a value that will define the parameter name to use for the URL

	 * @DateTimeFormat is a helper for that parameter when the type is the date value, 
	 * meaning that you will need to pass a date in the form of yyyy-mm-dd, 
	 * which is the ISO date format.
	 */
	
	List<JournalEntry> findByCreatedAfter(@Param("after") @DateTimeFormat(iso = ISO.DATE) Date date);

	List<JournalEntry> findByCreatedBetween(@Param("after") @DateTimeFormat(iso = ISO.DATE) Date after,
			@Param("before") @DateTimeFormat(iso = ISO.DATE) Date before);

	List<JournalEntry> findByTitleContaining(@Param("word") String word);

	List<JournalEntry> findBySummaryContaining(@Param("word") String word);
}
