package com.ms.portfoliomanager.publisher;

import com.ms.portfoliomanager.service.market.MarketService;
import lombok.Data;
import lombok.extern.java.Log;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Topic;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Log
@Component
@Data
public class MarketDataPublisher {

    @Autowired
    private MarketService marketService;

    @Autowired
    private JmsTemplate jmsTemplate;

    Map<String, Topic> topicMap;

    static Timer timer = new Timer();

    class Task extends TimerTask {
        String topic;
        Task(String topic){
            this.topic = topic;
        }

        //jmsTemplate.convertAndSend( new ActiveMQTopic(MAILBOX_TOPIC), new Email("info@example.com", "Hello"));
        @Override
        public void run() {
            int delay = (5 + new Random().nextInt(20)) * 100;
            timer.schedule(new Task(topic), delay);
            log.info("Publishing on " + topic + " after " + delay);
            jmsTemplate.convertAndSend(topicMap.get(topic),new Random().nextInt(40));
        }

    }


   // @PostConstruct
    public void publish(){
        CompletableFuture.runAsync(()-> {
            List<String> tickers = marketService.getAllTickers();
            topicMap = getTopicsMap(tickers);
            topicMap.keySet().stream().forEach(ticker -> new Task(ticker).run());
        });
        try{
            Thread.sleep(2000);
        }catch (Exception ex){

        }

//        List<String> tickers = marketService.getAllTickers();
//        topicMap = getTopicsMap(tickers);
//        topicMap.keySet().stream().forEach(ticker -> new Task(ticker).run());
    }

    public Map<String, Topic> getTopicsMap(List<String> tickers) {
        tickers = Arrays.asList("A","B");
       return tickers.stream().collect(Collectors.toMap(ticker -> ticker,ticker-> new ActiveMQTopic(ticker+".topic")));
    }


}
