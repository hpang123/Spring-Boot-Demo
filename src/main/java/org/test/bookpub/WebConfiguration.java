package org.test.bookpub;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.test.bookpub.formatters.BookFormatter;
//import org.test.bookpub.formatters.BookFormatter;
import org.test.bookpub.repository.BookRepository;

@Configuration
@PropertySource("classpath:/tomcat.https.properties")
@EnableConfigurationProperties(WebConfiguration.TomcatSslConnectorProperties.class)

/*
 * adding an interceptor need to do it via WebMvcConfigurer or by overriding
 * WebMvcConfigurationSupport. WebMvcConfigurerAdapter implementation of
 * WebMvcConfigurer
 */
/*
 * During the MVC autoconfiguration phase, Spring Boot detects instances of
 * WebMvcConfigurer and sequentially calls the callback methods on all of them.
 * It means that we can have more than one implementation of the
 * WebMvcConfigurer class if we want to have some logical separation.
 */
public class WebConfiguration extends WebMvcConfigurerAdapter {
	/*
	 * Configuring custom servlet filters When Spring Boot detects all the beans
	 * of javax.servlet.Filter, it will add them to the filter chain
	 * automatically. So, all we have to do, if we want to add more filters, is
	 * to just declare them as @Bean configurations.
	 */
	@Bean
	public RemoteIpFilter remoteIpFilter() {
		/*
		 * tomcat 8 get remote ip address
		 */
		return new RemoteIpFilter();
	}

	/*
	 * Configuring custom interceptors
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		return new LocaleChangeInterceptor();
	}

	/*
	 * To test: go to http://localhost:8080/books?locale=foo. if you look at the
	 * console logs, you will see a bunch of stack trace errors basically saying
	 * the following: Caused by: java.lang.UnsupportedOperationException: Cannot
	 * change HTTP accept header - use a different locale resolution strategy
	 * because the default locale resolution strategy does not allow the
	 * resetting of the locale that is requested by the browser.
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	@Bean
	public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
		return new ByteArrayHttpMessageConverter();
	}

	/*
	 * Mostly because of its statefulness and lack of thread safety, since
	 * version 3, Spring has added a Formatter interface as a replacement for
	 * PropertyEditor. The Formatters are intended to provide a similar
	 * functionality but in a completely thread-safe manner and focusing on a
	 * very specific task of parsing a String in an object type and converting
	 * an object to its String representation.
	 * 
	 * As Formatters are stateless, we don’t need to do the registration in our
	 * controller for every call; we have to do it only once and this will
	 * ensure S pring to use it for every web request.
	 */
	@Autowired
	private BookRepository bookRepository;

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatter(new BookFormatter(bookRepository));
	}

	// This code is commented out because it was used only as an example, but
	// actually breaks the data rendering.
	// @Override
	// public void extendMessageConverters(List<HttpMessageConverter<?>>
	// converters) {
	// converters.clear();
	// converters.add(new ByteArrayHttpMessageConverter());
	// }
	/*
	 * configure our application to not use the suffix pattern match of .* and
	 * not to strip the values after the dot when parsing the parameters
	 * 
	 * The configurer.setUseSuffixPatternMatch(false) method indicates that we
	 * don’t want to use the .* suffix so as to strip the trailing characters
	 * after the last dot. This translates into Spring parsing out the entire
	 * 978-1-78528-415-1.1 as an {isbn} parameter for BookController. So
	 * http://localhost:8080/books/978-1-78528-415-1.1 and
	 * http://localhost:8080/books/978-1-78528-415-1 will become different URLs.
	 * 
	 * The configurer.setUseTrailingSlashMatch(true) method indicates that we
	 * want to use the trailing / in the URL as a match as if it were not there.
	 * This effectively makes http://localhost:8080/books/978-1-78528-415-1 the
	 * same as http://localhost:8080/books/978-1-78528-415-1/
	 * 
	 * To test http://localhost:8080/books/978-1-78528-415-1.1/reviewers
	 */
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseSuffixPatternMatch(false).setUseTrailingSlashMatch(
				true);
	}

	/*
	 * Define custom mappings for static resource URLs and connect them with the
	 * resources on the file system or application classpath
	 * 
	 * To test: http://localhost:8080/internal/application.properties
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/internal/**").addResourceLocations(
				"classpath:/");
	}

	/*
	 * EmbeddedServletContainerCustomizer interface to programmatically define
	 * our configuration Even though the session timeout can be easily
	 * configured by setting the server.sessiontimeout property in
	 * application.properties to our desired value. We will demo how to do it
	 * with EmbeddedServletContainerCustomizer
	 */
	@Bean
	public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
		/*
		 * non lambda way 
		 * return new EmbeddedServletContainerCustomizer() {
		 * 
		 * @Override public void customize(ConfigurableEmbeddedServletContainer
		 * container) { container.setSessionTimeout(1, TimeUnit.MINUTES); } };
		 */
		return (ConfigurableEmbeddedServletContainer container) ->
			{
				container.setSessionTimeout(1, TimeUnit.MINUTES);
			};
	}
	
	/*
	 * To create .keystore file: keytool" -genkey -alias tomcat -keyalg RSA
	 * To test: https://localhost:8443/internal/tomcat.https.properties
	 */
	@Bean
    public EmbeddedServletContainerFactory servletContainer(TomcatSslConnectorProperties properties) {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.addAdditionalTomcatConnectors(createSslConnector(properties));
        return tomcat;
    }

    private Connector createSslConnector(TomcatSslConnectorProperties properties) {
        Connector connector = new Connector();
        properties.configureConnector(connector);
        return connector;
    }

    /*
     * Inside tomcat.https.properties file, custom.tomcat.https.xxx
     */
	@ConfigurationProperties(prefix = "custom.tomcat.https")
	//locations = "classpath:/tomcat.https.properties")
    public static class TomcatSslConnectorProperties {
        private Integer port;
        private Boolean ssl = true;
        private Boolean secure = true;
        private String scheme = "https";
        private File keystore;
        private String keystorePassword;

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public File getKeystore() {
            return keystore;
        }

        public void setKeystore(File keystore) {
            this.keystore = keystore;
        }

        public String getKeystorePassword() {
            return keystorePassword;
        }

        public void setKeystorePassword(String keystorePassword) {
            this.keystorePassword = keystorePassword;
        }

        public String getScheme() {
            return scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }

        public Boolean isSecure() {
            return secure;
        }

        public void setSecure(Boolean secure) {
            this.secure = secure;
        }

        public Boolean isSsl() {
            return ssl;
        }

        public void setSsl(Boolean ssl) {
            this.ssl = ssl;
        }

        public void configureConnector(Connector connector) {
            if (port != null)
                connector.setPort(port);
            if (secure != null)
                connector.setSecure(secure);
            if (scheme != null)
                connector.setScheme(scheme);
            if (ssl != null)
                connector.setProperty("SSLEnabled", ssl.toString());
            if (keystore != null && keystore.exists()) {
                connector.setProperty("keystoreFile", keystore.getAbsolutePath());
                connector.setProperty("keystorePassword", keystorePassword);
            }
        }
	}

}
