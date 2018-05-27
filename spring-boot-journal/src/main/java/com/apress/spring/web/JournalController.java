package com.apress.spring.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apress.spring.domain.Journal;
import com.apress.spring.repository.JournalRepository;

@Controller
public class JournalController {

	@Autowired
	JournalRepository repo;
	
	@RequestMapping(value="/journal", produces = {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody List<Journal> getJournal(){
		return repo.findAll();
	}
	
	@RequestMapping(value="/journal/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE})
	public @ResponseBody Journal getJournalById(@PathVariable long id){
		return repo.findById(id);
	}
	
	@RequestMapping("/")
	public String index(Model model){
		model.addAttribute("journal", repo.findAll());
		return "index";
	}
}
