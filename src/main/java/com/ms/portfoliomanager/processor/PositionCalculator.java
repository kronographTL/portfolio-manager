package com.ms.portfoliomanager.processor;

import com.ms.portfoliomanager.domainValue.ProductType;
import com.ms.portfoliomanager.model.Portfolio;
import com.ms.portfoliomanager.model.TickerDTO;

public class PositionCalculator {

    public static void calculateAndSetNetAssetValue(Portfolio portfolio) {
        Double value = portfolio.getCommonStocks().stream().mapToDouble(position -> (position.getTotalValue()!=null ? position.getTotalValue() : 0.0)).sum();
        value = value + portfolio.getOptions().stream().mapToDouble(position -> (position.getTotalValue()!=null ? position.getTotalValue() : 0.0)).sum();
        //value = value + portfolio.getPutPositions().stream().mapToDouble(position -> (position.getTotalValue()!=null ? position.getTotalValue() : 0.0)).sum();
        portfolio.setNetAssetValue(value);
    }
    public static void calculateCommonStockPositionValue(TickerDTO ticker, Portfolio portfolio) {
        portfolio.getCommonStocks().forEach(position -> {
            if (position.getShareCode().equalsIgnoreCase(ticker.getTickerCode())) {
                position.setCurrentValue(ticker.getMarketValue());
                position.setTotalValue(position.getCurrentValue() * position.getNoOfShares());
            }
        });
    }

    public static void calculateOptionsValue(TickerDTO ticker, Portfolio portfolio) {
        portfolio.getOptions().forEach(position -> {
            if (position.getShareCode().equalsIgnoreCase(ticker.getTickerCode())) {
                Double theoreticalValue;
                if(ProductType.CALL.name().equals(position.getOptionType())) {
                    theoreticalValue = GBMotionCalculator.calculateTheoreticalValueForCallOption(position.getStrikePrice(), ticker);
                }else{
                    theoreticalValue = GBMotionCalculator.calculateTheoreticalValueForPutOption(position.getStrikePrice(), ticker);
                }
                position.setTheoreticalValue(theoreticalValue);
                if(position.getTickerType().equalsIgnoreCase("SHORT")){
                    position.setTotalValue(theoreticalValue * position.getNoOfShares() * -1);
                }else {
                    position.setTotalValue(theoreticalValue * position.getNoOfShares());
                }
            }
        });
    }
}
