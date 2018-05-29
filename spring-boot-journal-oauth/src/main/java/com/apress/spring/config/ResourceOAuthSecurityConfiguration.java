package com.apress.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
/*
 * @EnableAuthorizationServer annotation enables the authorization /oauth/authorize 
 * and the token /oauth/token endpoints. 
 * The user is responsible for securing the authorization endpoint. 
 * The token endpoint will be automatically secured using HTTP basic authentication on the client’s credentials, 
 * in this case by using the username and password from the database.
 */
@EnableAuthorizationServer
/*
 * @EnableResourceServer annotation enables the Spring security filter that
 * authenticates requests via an incoming OAuth2 token.
 */
@EnableResourceServer
public class ResourceOAuthSecurityConfiguration extends ResourceServerConfigurerAdapter{

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		   .antMatchers("/").permitAll()
		   .antMatchers("/api/**").authenticated();
	}
	
}
