package com.ms.portfoliomanager.publisher;

import com.ms.portfoliomanager.model.Portfolio;
import com.ms.portfoliomanager.model.Position;
import com.ms.portfoliomanager.util.PortfolioPopulator;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Topic;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class PortfolioPublisher {

    //private Topic topic;
    Map<String, Topic> userTopicMap;

    @Autowired
    PortfolioPopulator portfolioPopulator;

    private JmsTemplate jmsTemplate;
    @Autowired
    PortfolioPublisher(JmsTemplate jmsTemplate){
        this.jmsTemplate = jmsTemplate;
        userTopicMap = new HashMap<>();
    }
    //@PostConstruct
    public void publish() throws JMSException {//List<String> tickers,String user
        Topic userTopic;
        String user = "Anil";
        if(userTopicMap.containsKey(user)){
            userTopic = userTopicMap.get(user);
        }else{
            userTopic = new ActiveMQTopic(user + "_Portfolio.topic");
            userTopicMap.put("Anil",userTopic);

        }

        System.out.println("Sending Portfolio.  " + userTopic.getTopicName());
        Position position = Position.builder().build();
        Position position1 = Position.builder().build();
        List<Position> positions = Arrays.asList(position,position1);
        Portfolio portfolio=Portfolio.builder().positions(positions).build();
        jmsTemplate.convertAndSend( userTopic, portfolioPopulator.populate(portfolio));
    }

}
