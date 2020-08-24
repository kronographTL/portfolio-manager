package com.ms.portfoliomanager.subscriber;

import com.ms.portfoliomanager.model.CallPosition;
import com.ms.portfoliomanager.model.Portfolio;
import com.ms.portfoliomanager.model.PutPosition;
import com.ms.portfoliomanager.model.StockPosition;
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
        System.out.println("_________________________________________________________________________________________________________________________________________________________________________________________________"+System.lineSeparator());
        System.out.println("    USER               "+ portfolio.getUserName()+"'s");
        System.out.println("    TIME                "+ ZonedDateTime.now());
        System.out.println("    Positions");
        for (StockPosition p : portfolio.getStockPositions()){
            System.out.println("                        "+p.toString());
        }
        for (CallPosition p : portfolio.getCallPositions()){
            System.out.println("                        "+p.toString());
        }
        for (PutPosition p : portfolio.getPutPositions()){
            System.out.println("                        "+p.toString());
        }
        System.out.println(System.lineSeparator()+"    Net Asset Value     "+portfolio.getNetAssetValue());
        System.out.println("_________________________________________________________________________________________________________________________________________________________________________________________________"+System.lineSeparator());
    }
}
