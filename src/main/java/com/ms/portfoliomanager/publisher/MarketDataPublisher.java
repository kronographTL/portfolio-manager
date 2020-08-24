package com.ms.portfoliomanager.publisher;

import com.ms.portfoliomanager.model.TickerDTO;
import com.ms.portfoliomanager.service.market.MarketService;
import com.ms.portfoliomanager.util.CommonUtil;
import lombok.Data;
import lombok.extern.java.Log;
import org.apache.activemq.command.ActiveMQTopic;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Topic;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Log
@Data
@Component
public class MarketDataPublisher {

    @Autowired
    private MarketService marketService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JmsTemplate jmsTemplate;

    private Map<String, Topic> topicMap;
    private List<TickerDTO> tickers;


    static Timer timer = new Timer();

    class Task extends TimerTask {
        TickerDTO ticker;
        Task(TickerDTO ticker){
            this.ticker = ticker;
        }

        @Override
        public void run() {
            int delay = (5 + new Random().nextInt(20)) * 100;// Time is in 1/100 seconds
            timer.schedule(new Task(ticker), delay);
            jmsTemplate.convertAndSend(topicMap.get(ticker.getTickerCode()), CommonUtil.generateSharePrice(ticker,delay));
        }

    }

    public void publish(){
        CompletableFuture.runAsync(()-> tickers.forEach(ticker -> new Task(ticker).run()));
        try{
            Thread.sleep(5000);
        }catch (Exception ex){
            log.info("The thread got interrupted while running the tickers  ");
        }

    }

    public Map<String, Topic> initTopicsMap() {
        tickers = marketService.getAllTickers().stream().map(l-> modelMapper.map(l, TickerDTO.class)).collect(Collectors.toList());
        topicMap = tickers.stream().map(TickerDTO::getTickerCode).collect(Collectors.toMap(ticker -> ticker, ticker-> new ActiveMQTopic(ticker+".topic")));
        return topicMap;
    }


}
