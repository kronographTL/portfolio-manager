package com.ms.portfoliomanager;

import com.ms.portfoliomanager.publisher.MarketDataPublisher;
import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;

@Log
@SpringBootApplication
public class PortfolioManagerApplication {

	public static void main(String[] args) {

		// Launch the application
		ConfigurableApplicationContext context = SpringApplication.run(PortfolioManagerApplication.class, args);
		//log.info("");
		MarketDataPublisher marketDataPublisher = context.getBean(MarketDataPublisher.class);
		CompletableFuture.runAsync(marketDataPublisher::publish);
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

		// Send a message with a POJO - the template reuse the message converter

	}

	@PostConstruct
	public void name(){
		System.out.println("Complete Post Construct Main");
	}


}
