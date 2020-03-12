package com.example.demo.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;




@Configuration
//@EnableWebSecurity
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	  @Override
	    protected void configure(HttpSecurity http) throws Exception {
		  http.cors().disable();
		  http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll();
	        http.authorizeRequests()
	                .antMatchers("/","/auth/login", "/login", "/auth/**","/user/auth/auth/login/**","/auth/auth/login/**","/user/auth/auth/login/","/auth/auth/login/")
	                .permitAll();	

	    }

	  @Override
	  public void configure(WebSecurity web) throws Exception {
	      web.ignoring().antMatchers(HttpMethod.OPTIONS, "/oauth/token");
	  }
}

