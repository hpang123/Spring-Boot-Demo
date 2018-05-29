package com.apress.spring.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.apress.spring.domain.Journal;

/*
 * http://localhost:8080/journals return HATEOAS
 * This is the result of using spring-boot-starter-data-rest and spring-bootstarter-data-jpa , 
 * where you defined your interface that extends from the JpaRepository interface.
 * 
 * By default the Spring Data REST will create the plural of the entity,
 * so that Journal become journals
 */
public interface JournalRepository extends JpaRepository<Journal, Long> {
	public Journal findById(long id);
	
	/*
	 * Same SQL query: select * from JOURNAL where title like %?1% .
	 */
	public List<Journal> findByTitleContaining(String word);
	
	/*
	 * Same SQL query: select * from JOURNAL where created > ?1 .
	 */
	List<Journal> findByCreatedAfter(Date date);
	
	@Query("select j from Journal j where j.summary like %?1%")
	List<Journal> findByCustomQuery(String word);
}
