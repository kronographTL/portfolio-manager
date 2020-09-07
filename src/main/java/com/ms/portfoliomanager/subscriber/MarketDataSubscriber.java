package com.ms.portfoliomanager.subscriber;

import com.ms.portfoliomanager.model.TickerDTO;
import com.ms.portfoliomanager.processor.PortfolioManager;
import com.ms.portfoliomanager.publisher.MarketDataPublisher;
import lombok.extern.java.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;

@Log
@Component
public class MarketDataSubscriber implements JmsListenerConfigurer {

    @Autowired
    MarketDataPublisher marketDataPublisher;
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
    }

}
