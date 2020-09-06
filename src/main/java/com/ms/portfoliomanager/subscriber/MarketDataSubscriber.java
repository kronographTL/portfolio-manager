package com.ms.portfoliomanager.subscriber;

import com.ms.portfoliomanager.model.TickerDTO;
import com.ms.portfoliomanager.publisher.MarketDataPublisher;
import com.ms.portfoliomanager.processor.PortfolioManager;
import lombok.extern.java.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;

@Log
@Component
public class MarketDataSubscriber implements JmsListenerConfigurer {

    @Autowired
    MarketDataPublisher marketDataPublisher;
    @Autowired
    JmsTemplate jmsTemplate;
    @Autowired
    PortfolioManager portfolioManager;

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
        marketDataPublisher.initTopicsMap().keySet().forEach(s -> {
                    SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
                    endpoint.setId(s);
                    endpoint.setDestination(s+".topic");
                    endpoint.setMessageListener(message -> {
                        try {
                            TickerDTO tic = getTickerDTO((TextMessage) message);
                            receive(tic);
                        } catch (JMSException | JSONException e) {
                            log.info("Error while Converting the Values ");
                        }
                    });
                    registrar.registerEndpoint(endpoint);
                }
        );
    }

    private TickerDTO getTickerDTO(TextMessage message) throws JMSException, JSONException {
        String payload = message.getText();
        JSONObject obj =  new JSONObject(payload);
        return TickerDTO.builder()
                .shareName(obj.get("shareName").toString())
                .marketValue(Double.valueOf(obj.get("marketValue").toString()))
                .tickerCode(obj.get("tickerCode").toString())
                .annualizedStandardDeviation(Double.valueOf(obj.get("annualizedStandardDeviation").toString()))
                .expectedReturn(Double.valueOf(obj.get("expectedReturn").toString()))
                .build();
    }

    public void receive(TickerDTO ticker) {
        portfolioManager.receiveTickerFromMarket(ticker);
//        if(portfolioManager.userPublishMap!=null) {
//            portfolioManager.userPublishMap.forEach((userId, portfolio) -> {
//                publishChangeInStocks(ticker, portfolio);
//                publishChangeInCallOptions(ticker, portfolio);
//                publishChangeInPutOptions(ticker, portfolio);
//                jmsTemplate.convertAndSend(portfolioManager.userTopicMap.get(portfolio.getUserId()), portfolio);
//                jmsTemplate.convertAndSend(portfolioManager.userTopicMap.get(portfolio.getUserId()), portfolio);
//            });
//        }
    }

//    private void publishChangeInStocks(TickerDTO ticker, Portfolio portfolio) {
//        if (portfolio.getStockPositions().stream().map(StockPosition::getShareCode).anyMatch(s -> s.equalsIgnoreCase(ticker.getTickerCode()))) {
//            PositionCalculator.calculateStockPosition(ticker, portfolio);
//            PositionCalculator.calculateAndSetNetAssetValue(portfolio);
//
//        }
//    }

//    private void publishChangeInCallOptions(TickerDTO ticker, Portfolio portfolio) {
//        if (portfolio.getCallPositions().stream().map(CallPosition::getShareCode).anyMatch(s -> s.equalsIgnoreCase(ticker.getTickerCode()))) {
//            PositionCalculator.calculateCallOptions(ticker, portfolio);
//            PositionCalculator.calculateAndSetNetAssetValue(portfolio);
//           // jmsTemplate.convertAndSend(portfolioManager.userTopicMap.get(portfolio.getUserId()), portfolio);
//        }
//    }
//
//    private void publishChangeInPutOptions(TickerDTO ticker, Portfolio portfolio) {
//        if (portfolio.getPutPositions().stream().map(PutPosition::getShareCode).anyMatch(s -> s.equalsIgnoreCase(ticker.getTickerCode()))) {
//            PositionCalculator.calculatePutOptions(ticker, portfolio);
//            PositionCalculator.calculateAndSetNetAssetValue(portfolio);
//           // jmsTemplate.convertAndSend(portfolioManager.userTopicMap.get(portfolio.getUserId()), portfolio);
//        }
//    }

}
