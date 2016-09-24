package com.saatya.ws.service;

import javax.servlet.ServletConfig;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

import com.saatya.ws.resource.AppConfigManager;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultiPartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan (basePackages = {"com.saatya.ws"})
@EnableAutoConfiguration
@Configuration

public class Application {

	
	 @Bean
	    public MultipartConfigElement multipartConfigElement() {
			MultiPartConfigFactory factory = new MultiPartConfigFactory();
	        factory.setMaxFileSize("1280KB");
	        factory.setMaxRequestSize("1280KB");
	        return factory.createMultipartConfig();
	    }
	
    public static void main(String[] args) {
    	AppConfigManager.init(null);
    	
        SpringApplication.run(Application.class, args);
    }
}