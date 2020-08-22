package com.ms.portfoliomanager.subscriber;

import com.ms.portfoliomanager.model.Portfolio;
import com.ms.portfoliomanager.model.Position;
import lombok.extern.java.Log;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;


@Log
@Component
public class ConsoleSubscriber {

    @JmsListener(destination = "user_01.topic", containerFactory = "topicListenerFactory")
    public void receiveTopicMessage(Portfolio portfolio) {
//        System.out.println("--:    " + portfolio.getUserName()+"'s  Portfolio at "+ ZonedDateTime.now() +"    :--       "
//                +System.lineSeparator() + System.lineSeparator()
//                + "Positions : "+portfolio.getPositions() +System.lineSeparator()
//                + "NAV    :   "+ portfolio.getNetAssetValue()+ System.lineSeparator());
        printDataInStandardFormat(portfolio);
    }

    private void printDataInStandardFormat(Portfolio portfolio) {
        System.out.println("_____________________________________________________________________________________________________________________________________________________"+System.lineSeparator());
        System.out.println("    USER                "+ portfolio.getUserName()+"'s");
        System.out.println("    TIME                "+ ZonedDateTime.now());
        System.out.println("    Positions");
        for (Position p : portfolio.getPositions()){
            System.out.println("                    "+p.toString());
        }
        System.out.println(System.lineSeparator()+"    Net Asset Value     "+portfolio.getNetAssetValue());
        System.out.println("_____________________________________________________________________________________________________________________________________________________"+System.lineSeparator());
    }
}
