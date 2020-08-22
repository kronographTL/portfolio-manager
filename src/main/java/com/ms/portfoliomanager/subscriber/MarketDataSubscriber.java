package com.ms.portfoliomanager.subscriber;

import com.ms.portfoliomanager.model.Position;
import com.ms.portfoliomanager.model.Ticker;
import com.ms.portfoliomanager.publisher.PortfolioPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MarketDataSubscriber {

    @Autowired
    JmsTemplate jmsTemplate;
    @Autowired
    PortfolioPublisher portfolioPublisher;

    public void receive(Ticker ticker) {
        if(portfolioPublisher.userPublishMap!=null) {
            portfolioPublisher.userPublishMap.forEach((userId, portfolio) -> {
                if (portfolio.getPositions().stream().map(Position::getShareCode).anyMatch(s -> s.equalsIgnoreCase(ticker.getTickerCode()))) {
                    Double nav = portfolio.getPositions().stream().map(l -> {
                        if (l.getShareCode().equalsIgnoreCase(ticker.getTickerCode())) {
                            l.setCurrentValue(ticker.getInitialMarketValue());
                            l.setTotalValue(l.getCurrentValue() * l.getNoOfShares());
                        }
                        return l.getTotalValue();
                    }).mapToDouble(x -> (x!=null) ? x:0.0 ).sum();
                    portfolio.setNetAssetValue(nav);
                    jmsTemplate.convertAndSend(portfolioPublisher.userTopicMap.get(userId), portfolio);
                }
            });
        }
    }
}
