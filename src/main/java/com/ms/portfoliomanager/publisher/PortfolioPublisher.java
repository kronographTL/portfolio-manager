package com.ms.portfoliomanager.publisher;

import com.ms.portfoliomanager.model.Portfolio;
import com.ms.portfoliomanager.model.Position;
import com.ms.portfoliomanager.model.Ticker;
import com.ms.portfoliomanager.service.market.MarketService;
import com.ms.portfoliomanager.util.PortfolioPopulator;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Topic;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Component
public class PortfolioPublisher {

    //private Topic topic;
    public Map<String, Topic> userTopicMap;
    public Map<String, Portfolio> userPublishMap;

    @Autowired
    PortfolioPopulator portfolioPopulator;

    @Autowired
    MarketService marketService;
    private JmsTemplate jmsTemplate;
    @Autowired
    PortfolioPublisher(JmsTemplate jmsTemplate){
        this.jmsTemplate = jmsTemplate;
        userTopicMap = new HashMap<>();
        userPublishMap = new HashMap<>();
    }

    public void createPortfolio(Portfolio portfolio) {
//        portfolio.getPositions().stream().map(l-> {
//           Ticker tic = marketService.getTickerByTickerCode(l.getShareCode());
//           l.setCurrentValue(tic.getInitialMarketValue());
//           l.setShareName(tic.getShareName());
//           return l;
//        });

        List<Position> positions = portfolio.getPositions();
        for (Position pos : positions){
            Ticker tic = marketService.getTickerByTickerCode(pos.getShareCode());
            pos.setShareName(tic.getShareName());
            pos.setCurrentValue(tic.getInitialMarketValue());
        }
        portfolio.setPositions(positions);
        Topic userTopic;
        if(userTopicMap.containsKey(portfolio.getUserId())){
            userTopic = userTopicMap.get(portfolio.getUserId());
        }else{
            userTopic = new ActiveMQTopic(portfolio.getUserId() + ".topic");
            userTopicMap.put(portfolio.getUserId(),userTopic);

        }
        if(!userPublishMap.containsKey(portfolio.getUserId())){
            userPublishMap.put(portfolio.getUserId(),portfolio);
        }
    }

}
