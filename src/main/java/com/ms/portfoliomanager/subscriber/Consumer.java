package com.ms.portfoliomanager.subscriber;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @JmsListener(destination = "A.topic" , containerFactory = "topicListenerFactory")
    public void listener(String message) {
        System.out.println("Received Message: " + message);
    }
}
