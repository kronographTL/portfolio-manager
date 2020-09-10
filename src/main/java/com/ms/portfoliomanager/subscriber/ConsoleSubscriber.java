package com.ms.portfoliomanager.subscriber;

import com.ms.portfoliomanager.model.Portfolio;
import lombok.extern.java.Log;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;


@Log
@Component
public class ConsoleSubscriber {

    @JmsListener(destination = "user_01.topic", containerFactory = "topicListenerFactory")
    public void receiveTopicMessage(Portfolio portfolio) {
        printDataInStandardFormat(portfolio);
    }

    private void printDataInStandardFormat(Portfolio portfolio) {
        System.out.println("_____________________________________________________________________________________"+System.lineSeparator());
        System.out.println("    USER               "+ portfolio.getUserName()+"'s");
        System.out.println("    TIME                "+ ZonedDateTime.now());
        System.out.println(System.lineSeparator()+"    Net Asset Value     "+portfolio.getNetAssetValue());
        System.out.println("_____________________________________________________________________________________"+System.lineSeparator());
    }
}
