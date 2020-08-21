package com.ms.portfoliomanager.publisher;

import com.ms.portfoliomanager.model.Ticker;
import com.ms.portfoliomanager.service.market.MarketService;
import com.ms.portfoliomanager.util.CommonUtil;
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

    List<Ticker> tickers;
    static Timer timer = new Timer();

    class Task extends TimerTask {
        Ticker ticker;
        Task(Ticker ticker){
            this.ticker = ticker;
        }

        @Override
        public void run() {
            int delay = (5 + new Random().nextInt(20)) * 100;
            timer.schedule(new Task(ticker), delay);
            log.info("Market Publishing " + ticker);
            jmsTemplate.convertAndSend(topicMap.get(ticker.getTickerCode()), CommonUtil.generateSharePrice(ticker));
        }

    }

    public void publish(){
        CompletableFuture.runAsync(()-> {
            tickers.stream().forEach(ticker -> new Task(ticker).run());
        });
        try{
            Thread.sleep(5000);
        }catch (Exception ex){
            log.info("Problem ");// TODO define proper message here
        }

    }

    public Map<String, Topic> initTopicsMap() {
        tickers = marketService.getAllTickers();
        topicMap = tickers.stream().map(l-> l.getTickerCode()).collect(Collectors.toMap(ticker -> ticker,ticker-> new ActiveMQTopic(ticker+".topic")));
        return topicMap;
    }


}
