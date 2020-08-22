package com.ms.portfoliomanager.config;

import com.ms.portfoliomanager.model.TickerDTO;
import com.ms.portfoliomanager.publisher.MarketDataPublisher;
import com.ms.portfoliomanager.subscriber.MarketDataSubscriber;
import lombok.extern.java.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.TextMessage;

@EnableJms
@Configuration
@Log
public class Config implements JmsListenerConfigurer {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    DefaultJmsListenerContainerFactory getFactory(){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(true);
        return factory;
    }
    @Bean
    public JmsListenerContainerFactory<?> topicListenerFactory(ConnectionFactory connectionFactory,
                                                               DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setPubSubDomain(true);
        return factory;
    }
    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Autowired
    MarketDataPublisher marketDataPublisher;
    @Autowired
    MarketDataSubscriber marketDataSubscriber;

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
        marketDataPublisher.initTopicsMap().keySet().stream().forEach(s -> {
            SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
            endpoint.setId(s);
            log.info(" listening to  " + s+".topic");
            endpoint.setDestination(s+".topic");
            endpoint.setMessageListener(message -> {
                //System.out.println("Received Message By Market Consumer from  Market Publisher  : " + message);
                try {
                    TextMessage textMessage = (TextMessage) message;
                    String payload = textMessage.getText();
                   //  Ticker ticker = message.getBody(Ticker.class);
                   JSONObject obj =  new JSONObject(payload);//message.getBody(JSONObject.class);
                    TickerDTO tic = TickerDTO.builder()
                            .shareName(obj.get("shareName").toString())
                            .marketValue(Double.valueOf(obj.get("marketValue").toString()))
                            .tickerCode(obj.get("tickerCode").toString())
                            .annualizedStandardDeviation(Double.valueOf(obj.get("annualizedStandardDeviation").toString()))
                            .expectedReturn(Double.valueOf(obj.get("expectedReturn").toString()))
                            .build();
                     marketDataSubscriber.receive(tic);
                    //log.info("Market Consumer : " + tic); TODO Proper Logging for Market Consumer
                } catch (JMSException | JSONException e) {
                   log.info("Error while Converting the Values ");
                }
            });
            registrar.registerEndpoint(endpoint);
        }
        );
    }
}
