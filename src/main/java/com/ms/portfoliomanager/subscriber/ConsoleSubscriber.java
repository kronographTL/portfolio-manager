package com.ms.portfoliomanager.subscriber;

import com.ms.portfoliomanager.model.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;


@Component
public class ConsoleSubscriber {

    @Autowired
    JmsTemplate jmsTemplate;
    //@PostConstruct
    public void receiveTopicMessage(Portfolio portfolio) throws JMSException {
        Message body = jmsTemplate.receive("Anil"+"_Portfolio.topic");

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + body.getBody(Portfolio.class).getNetAssetValue());
    }
}
