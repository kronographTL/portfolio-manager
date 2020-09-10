package com.ms.portfoliomanager.processor;

import com.ms.portfoliomanager.model.*;
import com.ms.portfoliomanager.publisher.PortfolioPublisher;
import com.ms.portfoliomanager.service.market.MarketService;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Topic;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class PortfolioManager {

    public Map<String, Topic> userTopicMap;
    public Map<String, Portfolio> userPublishMap;

    @Autowired
    MarketService marketService;
    private JmsTemplate jmsTemplate;
    @Autowired
    PortfolioPublisher portfolioPublisher;
    @Autowired
    PortfolioManager(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
        userTopicMap = new HashMap<>();
        userPublishMap = new HashMap<>();
    }

    public Portfolio createPortfolio(Portfolio portfolio) {

        Set<String> stockCodes = getShareCodes(portfolio);
        List<Ticker> tickers = marketService.getAllTickersById(stockCodes);
        Map<String, Ticker> tickerMap = tickers.stream().collect(Collectors.toMap(Ticker::getTickerCode, ticker -> ticker));
        List<CommonStock> commonStocks = portfolio.getCommonStocks();
        getUpdatedPortfolio(portfolio, tickerMap);
        portfolio.setCommonStocks(commonStocks);
        Topic userTopic;
        if (userTopicMap.containsKey(portfolio.getUserId())) {
            userTopic = userTopicMap.get(portfolio.getUserId());
        } else {
            userTopic = new ActiveMQTopic(portfolio.getUserId() + ".topic");
            userTopicMap.put(portfolio.getUserId(), userTopic);

        }
        if (!userPublishMap.containsKey(portfolio.getUserId())) {
            userPublishMap.put(portfolio.getUserId(), portfolio);
        }
        return portfolio;
    }

    private void getUpdatedPortfolio(Portfolio portfolio, Map<String, Ticker> tickerMap) {
        portfolio.getCommonStocks().forEach(position -> {
            Ticker tick = tickerMap.get(position.getShareCode());
            position.setCurrentValue(tick.getMarketValue());
            position.setShareName(tick.getShareName());
        });
        portfolio.getOptions().forEach(position -> {
            Ticker tick = tickerMap.get(position.getShareCode());
            position.setShareName(tick.getShareName());
        });

    }

    private Set<String> getShareCodes(Portfolio portfolio) {
        Set<String> stockCodes = portfolio.getCommonStocks().stream().map(CommonStock::getShareCode
        ).collect(Collectors.toSet());
        stockCodes.addAll(portfolio.getOptions().stream().map(Option::getShareCode
        ).collect(Collectors.toSet()));
        return stockCodes;
    }

    public void receiveTickerFromMarket(TickerDTO ticker) {
        if (userPublishMap != null) {
            userPublishMap.forEach((userId, portfolio) -> {
                publishChangeInStocks(ticker, portfolio);
                publishChangeInCallOptions(ticker, portfolio);
                portfolioPublisher.publishPortfolio(portfolio,userTopicMap);
            });
        }
    }

    private void publishChangeInStocks(TickerDTO ticker, Portfolio portfolio) {
        if (portfolio.getCommonStocks().stream().map(CommonStock::getShareCode).anyMatch(s -> s.equalsIgnoreCase(ticker.getTickerCode()))) {
            PositionCalculator.calculateCommonStockPositionValue(ticker, portfolio);
            PositionCalculator.calculateAndSetNetAssetValue(portfolio);
        }
    }

    private void publishChangeInCallOptions(TickerDTO ticker, Portfolio portfolio) {
        if (portfolio.getOptions().stream().map(Option::getShareCode).anyMatch(s -> s.equalsIgnoreCase(ticker.getTickerCode()))) {
            PositionCalculator.calculateOptionsValue(ticker, portfolio);
            PositionCalculator.calculateAndSetNetAssetValue(portfolio);
        }
    }

}
