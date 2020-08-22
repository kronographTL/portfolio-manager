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
        System.out.println("--:    " + portfolio.getUserName()+"'s  Portfolio at "+ ZonedDateTime.now() +"    :--       "
                +System.lineSeparator() + System.lineSeparator()
                + "Positions : "+portfolio.getPositions() +System.lineSeparator()
                + "NAV    :   "+ portfolio.getNetAssetValue()+ System.lineSeparator());

    }
}
