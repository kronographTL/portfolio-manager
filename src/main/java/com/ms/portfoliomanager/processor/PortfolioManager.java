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

    public void createPortfolio(Portfolio portfolio) {

        Set<String> stockCodes = getShareCodes(portfolio);
        List<Ticker> tickers = marketService.getAllTickersById(stockCodes);
        Map<String, Ticker> tickerMap = tickers.stream().collect(Collectors.toMap(Ticker::getTickerCode, ticker -> ticker));
        List<StockPosition> stockPositions = portfolio.getStockPositions();
        getUpdatedPortfolio(portfolio, tickerMap);
        portfolio.setStockPositions(stockPositions);
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
    }

    private void getUpdatedPortfolio(Portfolio portfolio, Map<String, Ticker> tickerMap) {
        portfolio.getStockPositions().forEach(position -> {
            Ticker tick = tickerMap.get(position.getShareCode());
            position.setCurrentValue(tick.getMarketValue());
            position.setShareName(tick.getShareName());
        });
        portfolio.getCallPositions().forEach(position -> {
            Ticker tick = tickerMap.get(position.getShareCode());
            position.setShareName(tick.getShareName());
        });
        portfolio.getPutPositions().forEach(position -> {
            Ticker tick = tickerMap.get(position.getShareCode());
            position.setShareName(tick.getShareName());
        });
    }

    private Set<String> getShareCodes(Portfolio portfolio) {
        Set<String> stockCodes = portfolio.getStockPositions().stream().map(StockPosition::getShareCode
        ).collect(Collectors.toSet());
        stockCodes.addAll(portfolio.getCallPositions().stream().map(CallPosition::getShareCode
        ).collect(Collectors.toSet()));
        stockCodes.addAll(portfolio.getPutPositions().stream().map(PutPosition::getShareCode
        ).collect(Collectors.toSet()));
        return stockCodes;
    }

    public void receiveTickerFromMarket(TickerDTO ticker) {
        if (userPublishMap != null) {
            userPublishMap.forEach((userId, portfolio) -> {
                publishChangeInStocks(ticker, portfolio);
                publishChangeInCallOptions(ticker, portfolio);
                publishChangeInPutOptions(ticker, portfolio);
                portfolioPublisher.publishPortfolio(portfolio,userTopicMap);
            });
        }
    }

    private void publishChangeInStocks(TickerDTO ticker, Portfolio portfolio) {
        if (portfolio.getStockPositions().stream().map(StockPosition::getShareCode).anyMatch(s -> s.equalsIgnoreCase(ticker.getTickerCode()))) {
            PositionCalculator.calculateStockPosition(ticker, portfolio);
            PositionCalculator.calculateAndSetNetAssetValue(portfolio);

        }
    }

    private void publishChangeInCallOptions(TickerDTO ticker, Portfolio portfolio) {
        if (portfolio.getCallPositions().stream().map(CallPosition::getShareCode).anyMatch(s -> s.equalsIgnoreCase(ticker.getTickerCode()))) {
            PositionCalculator.calculateCallOptions(ticker, portfolio);
            PositionCalculator.calculateAndSetNetAssetValue(portfolio);
        }
    }

    private void publishChangeInPutOptions(TickerDTO ticker, Portfolio portfolio) {
        if (portfolio.getPutPositions().stream().map(PutPosition::getShareCode).anyMatch(s -> s.equalsIgnoreCase(ticker.getTickerCode()))) {
            PositionCalculator.calculatePutOptions(ticker, portfolio);
            PositionCalculator.calculateAndSetNetAssetValue(portfolio);
        }
    }
}
