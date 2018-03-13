package org.test.bookpub;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.test.bookpub.entity.Author;
import org.test.bookpub.entity.Book;
import org.test.bookpub.entity.Publisher;
import org.test.bookpub.entity.Reviewer;
import org.test.bookpub.repository.AuthorRepository;
import org.test.bookpub.repository.BookRepository;
import org.test.bookpub.repository.PublisherRepository;
import org.test.bookpub.repository.ReviewerRepository;
/*
 * make startupRunner run later
 */
@Order(Ordered.LOWEST_PRECEDENCE - 15)
public class StartupRunner implements CommandLineRunner {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private DataSource ds;
	
	@Autowired private BookRepository bookRepository;
    @Autowired private AuthorRepository authorRepository;
    @Autowired private PublisherRepository publisherRepository;
    
    @Autowired private ReviewerRepository reviewerRepository;

	
	@Override
	public void run(String... args) throws Exception {
		logger.info("Welcome to the Book Catalog System!");
		logger.info("DataSource: "+ds.toString());
		
		/* It works. but we want to add data go through sql file manually.
		 * Use the Spring JDBC support for schema.sql and data.sql.
		 * as long as  add compile("org.springframework.boot:spring-boot-starter-jdbc")
		 */
		/*
		Author author = new Author("Alex", "Antonov");
        author = authorRepository.save(author);
        Publisher publisher = new Publisher("Packt");
        publisher = publisherRepository.save(publisher);
        
        Book book = new Book("978-1-78528-415-1", "Spring Boot Recipes", author, publisher);
        
        Reviewer reviewer = new Reviewer("Ray", "Pang");
        reviewerRepository.save(reviewer);
        
        List<Reviewer> reviewsers = book.getReviewers();
        if(reviewsers == null){
        	reviewsers = new ArrayList<Reviewer>();
        }
        reviewsers.add(reviewer);
        book.setReviewers(reviewsers);
        
        bookRepository.save(book);
        */

	}
	
	/* It works. Disable now*/
	@Scheduled(initialDelay = 1000, fixedRate = 10000)
    public void run() {
        logger.info("Number of books: " + bookRepository.count());
    }

}
