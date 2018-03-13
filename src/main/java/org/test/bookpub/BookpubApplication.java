package org.test.bookpub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.test.bookpubstarter.dbcount.EnableDbCounting;

/*
 * Run H2 (C:\H2\bin\h2.bat)
 * JDBC URL:jdbc:h2:~/test;;AUTO_SERVER=TRUE
 */
/* annotated with @SpringBootApplication, which in turn is a convenience 
 * meta-annotation that declares @ComponentScan among others.
 */

/* @SpringBootApplication works, but we replace it with 
 * @Configuration, @EnableAutoConfiguration, and @ComponentScan 
 * for excluding test class. To enable @SpringBootApplication, we need to
 * remove the following @Configuration, @EnableAutoConfiguration, and @ComponentScan
 */

@SpringBootApplication

/* for excluding test class that use @UsedForTesting annotation (like mock) Not work!
@Configuration
@EnableAutoConfiguration
@interface UsedForTesting {}
@ComponentScan(excludeFilters=@ComponentScan.Filter(UsedForTesting.class))
*/

@EnableScheduling
/*
 * @EnableDbCounting, we transitively tell Spring that 
 * it should include DbCountAutoConfiguration as a part of 
 * the application context as well
 */
@EnableDbCounting
public class BookpubApplication {

	@Bean
	public StartupRunner schedulerRunner() {
		return new StartupRunner();
	}

	public static void main(String[] args) {
		SpringApplication.run(BookpubApplication.class, args);
	}
	
	/* Test auto configuration. It will not run dbCountAutoConfiguration due to
	 * @ConditionalOnMissingBean
  protected final Log logger = LogFactory.getLog(getClass());
  @Bean
  public DbCountRunner dbCountRunner(Collection<CrudRepository> repositories) {
      return new DbCountRunner(repositories) {
          @Override
          public void run(String... args) throws Exception {
              logger.info("Manually Declared DbCountRunner");
          }
      };
  }
  */

}
