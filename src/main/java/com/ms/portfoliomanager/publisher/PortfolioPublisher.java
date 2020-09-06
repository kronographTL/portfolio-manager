package com.ms.portfoliomanager.publisher;

import com.ms.portfoliomanager.model.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Topic;
import java.util.Map;

@Component
public class PortfolioPublisher {

    @Autowired
    JmsTemplate jmsTemplate;

    public void publishPortfolio(Portfolio portfolio, Map<String, Topic> userTopicMap) {
        jmsTemplate.convertAndSend(userTopicMap.get(portfolio.getUserId()), portfolio);
    }
}
