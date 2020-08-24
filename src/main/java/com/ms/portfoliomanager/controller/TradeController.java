package com.ms.portfoliomanager.controller;

import com.ms.portfoliomanager.domainValue.PositionType;
import com.ms.portfoliomanager.exception.TradeFileNotFoundException;
import com.ms.portfoliomanager.model.CallPosition;
import com.ms.portfoliomanager.model.Portfolio;
import com.ms.portfoliomanager.model.PutPosition;
import com.ms.portfoliomanager.model.StockPosition;
import com.ms.portfoliomanager.publisher.PortfolioManager;
import com.ms.portfoliomanager.util.UtilityConstants;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Log
@RestController
@RequestMapping("/trade")
public class TradeController {

    public static final String USER_ID = "user_01";
    public static final String USER_NAME = " Dow Joe";

    @Autowired
    PortfolioManager portfolioManager;
    @GetMapping
    public String startTrade() throws TradeFileNotFoundException {
        String filePath ="src/main/resources/trade/trade.csv";
        try(Stream<String> lines = Files.lines(Paths.get((filePath))))
        {
            List<StockPosition> stockPositions = new ArrayList<>();
            List<CallPosition> callPositions = new ArrayList<>();
            List<PutPosition> putPositions = new ArrayList<>();

            lines.skip(1).forEach(line-> {
                String[] string = line.split(UtilityConstants.COMMA);
                if(PositionType.CALL.name().equals(string[0])){
                    CallPosition position = CallPosition.builder().shareCode(string[1])
                            .noOfShares(Integer.parseInt(string[2])).strikePrice(Double.parseDouble(string[3]))
                            .tickerType(string[4]).build();
                    callPositions.add(position);
                }else if(PositionType.PUT.name().equals(string[0])) {
                    PutPosition position = PutPosition.builder().shareCode(string[1])
                            .noOfShares(Integer.parseInt(string[2])).strikePrice(Double.parseDouble(string[3]))
                            .tickerType(string[4]).build();
                    putPositions.add(position);
                }else{
                    StockPosition position = StockPosition.builder().shareCode(string[1]).noOfShares(Integer.parseInt(string[2])).build();
                    stockPositions.add(position);
                }
            });

            Portfolio portfolio=Portfolio.builder().userId(USER_ID).userName(USER_NAME).stockPositions(stockPositions).callPositions(callPositions).putPositions(putPositions).build();

        portfolioManager.createPortfolio(portfolio);
        }
        catch (Exception e) {
            throw  new TradeFileNotFoundException("The Trade file for "+USER_NAME + " Not found ");
        }
        return  "<h1>  ('-')    "+ USER_NAME.toUpperCase() +"  Your Trade Started Successfully   ('-')   </h1>";
    }
}
