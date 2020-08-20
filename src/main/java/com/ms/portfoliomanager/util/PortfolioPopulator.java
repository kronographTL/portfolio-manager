package com.ms.portfoliomanager.util;

import com.ms.portfoliomanager.model.Portfolio;
import com.ms.portfoliomanager.model.Position;
import com.ms.portfoliomanager.publisher.MarketDataPublisher;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Log
public class PortfolioPopulator {


    @Autowired
    MarketDataPublisher marketDataPublisher;

    @JmsListener(destination = "A.topic" , containerFactory = "topicListenerFactory")
    public String listener(String message) {
        System.out.println("Received Message: " + message);
        return message;
    }

    @Autowired
    private JmsTemplate jmsTemplate;
    public Portfolio populate(Portfolio portfolio) throws JMSException {
        CompletableFuture.runAsync(()->  marketDataPublisher.publish());
        log.info("Started Market Publishing");
        Message abc = jmsTemplate.receive("A.topic");
        int body = 2;//abc.getBody(Integer.class);
        List<Position> positions = portfolio.getPositions();
        positions.stream().forEach(position -> position.getShareCode());
        portfolio.setNetAssetValue(Double.valueOf(body+""));
        portfolio.setUserId("Anil");
        log.info(" Position A value "+body);
        return portfolio;
    }
}
