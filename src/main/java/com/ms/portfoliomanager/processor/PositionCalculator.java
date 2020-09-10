package com.ms.portfoliomanager.processor;

import com.ms.portfoliomanager.domainValue.ProductType;
import com.ms.portfoliomanager.model.Portfolio;
import com.ms.portfoliomanager.model.TickerDTO;

public class PositionCalculator {

    public static void calculateAndSetNetAssetValue(Portfolio portfolio) {
        Double value = portfolio.getCommonStocks().stream().mapToDouble(position -> (position.getTotalValue()!=null ? position.getTotalValue() : 0.0)).sum();
        value = value + portfolio.getOptions().stream().mapToDouble(position -> (position.getTotalValue()!=null ? position.getTotalValue() : 0.0)).sum();
        portfolio.setNetAssetValue(value);
    }
    public static void calculateCommonStockPositionValue(TickerDTO ticker, Portfolio portfolio) {
        portfolio.getCommonStocks().forEach(commonStock -> {
            if (commonStock.getShareCode().equalsIgnoreCase(ticker.getTickerCode())) {
                commonStock.setCurrentValue(ticker.getMarketValue());
                commonStock.setTotalValue(commonStock.getCurrentValue() * commonStock.getNoOfShares());
            }
        });
    }

    public static void calculateOptionsValue(TickerDTO ticker, Portfolio portfolio) {
        portfolio.getOptions().forEach(option -> {
            if (option.getShareCode().equalsIgnoreCase(ticker.getTickerCode())) {
                Double theoreticalValue;
                if(ProductType.CALL.name().equals(option.getOptionType())) {
                    theoreticalValue = GBMotionCalculator.calculateTheoreticalValueForCallOption(option.getStrikePrice(), ticker);
                }else{
                    theoreticalValue = GBMotionCalculator.calculateTheoreticalValueForPutOption(option.getStrikePrice(), ticker);
                }
                option.setTheoreticalValue(theoreticalValue);
                if(option.getTickerType().equalsIgnoreCase("SHORT")){
                    option.setTotalValue(theoreticalValue * option.getNoOfShares() * -1);
                }else {
                    option.setTotalValue(theoreticalValue * option.getNoOfShares());
                }
            }
        });
    }
}
