package com.ms.portfoliomanager.subscriber;

import com.ms.portfoliomanager.model.Portfolio;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class ConsoleSubscriber {

    @JmsListener(destination = "user_01.topic", containerFactory = "topicListenerFactory")
    public void receiveTopicMessage(Portfolio portfolio) {
        System.out.println("The User current Portfolio is   :       " + portfolio);
    }
}
