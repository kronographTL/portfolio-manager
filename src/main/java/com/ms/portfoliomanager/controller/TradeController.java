package com.ms.portfoliomanager.controller;

import com.ms.portfoliomanager.domainValue.ProductType;
import com.ms.portfoliomanager.exception.TradeFileNotFoundException;
import com.ms.portfoliomanager.model.CommonStock;
import com.ms.portfoliomanager.model.Option;
import com.ms.portfoliomanager.model.Portfolio;
import com.ms.portfoliomanager.processor.PortfolioManager;
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
            List<CommonStock> commonStocks = new ArrayList<>();
            List<Option> options = new ArrayList<>();

            lines.skip(1).forEach(line-> {
                String[] string = line.split(UtilityConstants.COMMA);
                if(ProductType.CALL.name().equals(string[0]) || ProductType.PUT.name().equals(string[0])){
                    Option option = Option.builder().optionType(string[0]).shareCode(string[1])
                            .noOfShares(Integer.parseInt(string[2])).strikePrice(Double.parseDouble(string[3]))
                            .tickerType(string[4]).build();
                    options.add(option);
                }else{
                    CommonStock position = CommonStock.builder().shareCode(string[1]).noOfShares(Integer.parseInt(string[2])).build();
                    commonStocks.add(position);
                }
            });

            Portfolio portfolio=Portfolio.builder().userId(USER_ID).userName(USER_NAME).commonStocks(commonStocks).options(options).build();

        portfolio = portfolioManager.createPortfolio(portfolio);
        }
        catch (Exception e) {
            throw  new TradeFileNotFoundException("The Trade file for "+USER_NAME + " Not found ");
        }
        return  "<h1>  ('-')    "+ USER_NAME.toUpperCase() +"  Your Trade Started Successfully   ('-')   </h1>";
    }
}
