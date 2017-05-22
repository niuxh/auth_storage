package com.nxh.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * web启动入口
 * @author xn051950
 */
@SpringBootApplication
public class App extends SpringBootServletInitializer{
	static Logger LOGGER = LoggerFactory.getLogger(App.class);
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(App.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
