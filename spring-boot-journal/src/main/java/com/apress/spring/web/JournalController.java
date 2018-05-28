package com.apress.spring.web;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apress.spring.domain.Journal;
import com.apress.spring.service.JournalService;

/*
 * http://localhost:8080/journal?date=02/02/2016
 * http://localhost:8080/journal/title/know
 * http://localhost:8080/journal
 * http://localhost:8080/journal/1
 * http://localhost:8080/journal/summary/Today
 * 
 */
@Controller
public class JournalController {

	@Autowired
	JournalService service;
	
	@RequestMapping(value="/journal", produces = {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody List<Journal> getJournal(@RequestParam(value="date", required=false) Date date){
		if(date !=null){
			return service.findByCreatedAfter(date);
		}
		return service.findAll();
	}
	
	@RequestMapping(value="/journal/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE})
	public @ResponseBody Journal getJournalById(@PathVariable long id){
		return service.findById(id);
	}
	
	@RequestMapping(value="/journal/title/{word}", method = RequestMethod.GET)
	public @ResponseBody List<Journal> getByTitleContaining(@PathVariable String word){
		return service.findByTitleContaining(word);
	}
	
	@RequestMapping(value="/journal/summary/{word}", method = RequestMethod.GET)
	public @ResponseBody List<Journal> getBySummaryContaining(@PathVariable String word){
		return service.findByCustomQuery(word);
	}
	
	@RequestMapping("/")
	public String index(Model model){
		model.addAttribute("journal", service.findAll());
		return "index";
	}
}
