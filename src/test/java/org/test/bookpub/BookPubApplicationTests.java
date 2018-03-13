package org.test.bookpub;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.test.bookpub.entity.Book;
import org.test.bookpub.repository.BookRepository;

@RunWith(SpringJUnit4ClassRunner.class)
/*
 * @SpringApplicationConfiguration(classes = BookPubApplication.class):
 * This is a Spring Boot annotation that is used to determine how to load and 
 * configure the Spring Application Context for the integration tests. 
 * It is a meta-annotation that contains the ContextConfiguration annotation, 
 * which instructs the testing framework to use Spring Boot’s 
 * SpringApplicationContextLoader for application context creation.
 */
@SpringApplicationConfiguration(classes = BookpubApplication.class)

/*
 * This is an annotation that indicates to Spring Boot that the current test is 
 * an integration test and will require a complete context initialization 
 * and application startup, as if it were a real deal. 
 * This annotation is usually included along with @SpringApplicationConfiguration 
 * for the integration tests. 
 * The server.port:0 value is used to tell Spring Boot to start the
 * Tomcat server on a randomly chosen http port, which we will later obtain by 
 * declaring the @Value("${local.server.port}") private int port; value field.
 * This ability to select a random http port is very handy when running tests 
 * on a Jenkins or any other CI server where, if multiple jobs are running 
 * in parallel, you could get a port collision.
 */
@WebIntegrationTest("server.port:0")
public class BookPubApplicationTests {
	@Autowired
	private WebApplicationContext context;
	@Autowired
	private BookRepository repository;
	@Autowired
	private DataSource ds;
	@Value("${local.server.port}")
	private int port;

	private MockMvc mockMvc;
	private RestTemplate restTemplate = new TestRestTemplate();
	private static boolean loadDataFixtures = true;

	@Before
	public void setupMockMvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	/* we can run test-data.sql here,
	@Before
	public void loadDataFixtures() {
		if (loadDataFixtures) {
			ResourceDatabasePopulator populator = new ResourceDatabasePopulator(context.getResource("classpath:/test-data.sql"));
			DatabasePopulatorUtils.execute(populator, ds);
			loadDataFixtures = false;
		}
	}
*/
	@Test
	public void contextLoads() {
		assertEquals(1, repository.count());
	}

	@Test
	public void webappBookIsbnApi() {
		Book book = restTemplate.getForObject("http://localhost:" + port + "/books/978-1-78528-415-1", Book.class);
		assertNotNull(book);
		assertEquals("Packt", book.getPublisher().getName());
	}

	@Test
	public void webappPublisherApi() throws Exception {
		mockMvc.perform(get("/publishers/1")).
				andExpect(status().isOk()).
				andExpect(content().contentType(MediaType.parseMediaType("application/hal+json;charset=UTF-8"))).
				andExpect(content().string(containsString("Packt"))).
				andExpect(jsonPath("$.name").value("Packt"));
	}
}
