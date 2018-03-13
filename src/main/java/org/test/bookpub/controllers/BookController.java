package org.test.bookpub.controllers;

import java.beans.PropertyEditorSupport;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.test.bookpub.entity.Book;
import org.test.bookpub.entity.Reviewer;
import org.test.bookpub.repository.BookRepository;

//http://localhost:8080/books

/* RestController = @ResponseBody + @Controller*/
@RestController
@RequestMapping("/books")
public class BookController {
	@Autowired
	private BookRepository bookRepository;

	/*
	 * @InitBinder annotation is scanned and all the detected methods should
	 * have a signature of accepting WebDataBinder as an argument. Among other
	 * things, WebDataBinder provides us with an ability to register as many
	 * custom editors as we require for the controller methods to be bound
	 * properly.
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class,
				new StringTrimmerEditor(false));

		binder.registerCustomEditor(Isbn.class, new IsbnEditor());
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	@RequestMapping(value = "/{isbn}", method = RequestMethod.GET)
	// public Book getBook(@PathVariable String isbn) {
	public Book getBook(@PathVariable Isbn isbn) {
		return bookRepository.findBookByIsbn(isbn.getIsbn());
	}

	/*
	 * This is to test formatter to convert isbn string to Book
	 */
	@RequestMapping(value = "/{isbn}/reviewers", method = RequestMethod.GET)
	public List<Reviewer> getReviewers(@PathVariable("isbn") Book book) {
		return book.getReviewers();
	}
	
	/*
	 * Demo session timeout setting in EmbeddedServletContainerCustomizer
	 */
	@RequestMapping(value = "/session", method = RequestMethod.GET)
    public String getSessionId(HttpServletRequest request) {
        return request.getSession().getId();
    }

}

class Isbn {
	private String isbn;

	public Isbn(String isbn) {
		this.isbn = isbn;
	}

	public String getIsbn() {
		return isbn;
	}
}

/*
 * PropertyEditor is not thread safe. we have to create a new instance of our
 * custom editors for every web request and register them with WebDataBinder. it
 * is best to create one by extending PropertyEditorSupport and overriding the
 * desired methods with custom implementation.
 */
class IsbnEditor extends PropertyEditorSupport {
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (StringUtils.hasText(text)) {
			setValue(new Isbn(text.trim()));
		} else {
			setValue(null);
		}
	}

	@Override
	public String getAsText() {
		Isbn isbn = (Isbn) getValue();
		if (isbn != null) {
			return isbn.getIsbn();
		} else {
			return "";
		}
	}
}
