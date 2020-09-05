package com.ms.portfoliomanager;

import com.ms.portfoliomanager.publisher.MarketDataPublisher;
import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;

@Log
@SpringBootApplication
public class PortfolioManagerApplication {

	public static void main(String[] args) {

		// Launch the application
		ConfigurableApplicationContext context = SpringApplication.run(PortfolioManagerApplication.class, args);
		MarketDataPublisher marketDataPublisher = context.getBean(MarketDataPublisher.class);
		CompletableFuture.runAsync(marketDataPublisher::publish);// Starting the Market Publisher
		log.info("Market Publisher Started, The Market is Up Now");
	}

	@PostConstruct
	public void name(){
		System.out.println("Complete Post Construct Main");
	}


}
