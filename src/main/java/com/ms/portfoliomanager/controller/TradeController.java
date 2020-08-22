package com.ms.portfoliomanager.controller;

import com.ms.portfoliomanager.model.Portfolio;
import com.ms.portfoliomanager.model.Position;
import com.ms.portfoliomanager.publisher.PortfolioPublisher;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log
@RestController
@RequestMapping("/startTrade")
public class TradeController {
    private String userName;
    @Autowired
    PortfolioPublisher portfolioPublisher;
    @GetMapping
    public String startTrade(){
        String filePath ="src/main/resources/trade/trade.csv";
        try(Stream<String> lines = Files.lines(Paths.get((filePath))))
        {
            List<Position> positions = lines.skip(1).map(l-> {
                String[] strings = l.split(",");
                return Position.builder().shareCode(strings[0]).noOfShares(Integer.parseInt(strings[1])).build();
            }).collect(Collectors.toList());
            //TODO

            Portfolio portfolio=Portfolio.builder().userId("user_01").userName(" Dow Joe").positions(positions).build();
            userName = portfolio.getUserName();

        portfolioPublisher.createPortfolio(portfolio);
        }
        catch (Exception e) {
            log.info("The fie does not found on " + filePath+ " path");
        }
        return  "<h1>  ('-')    "+ userName.toUpperCase() +"  Your Trade Started Successfully   ('-')   </h1>";
    }
}
