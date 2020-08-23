package com.ms.portfoliomanager.processor;

import com.ms.portfoliomanager.model.Portfolio;
import com.ms.portfoliomanager.model.TickerDTO;

public class PositionCalculator {

    public static void calculateAndSetNetAssetValue(Portfolio portfolio) {
        Double value = portfolio.getStockPositions().stream().mapToDouble(position -> (position.getTotalValue()!=null ? position.getTotalValue() : 0.0)).sum();
        value = value + portfolio.getCallPositions().stream().mapToDouble(position -> (position.getTotalValue()!=null ? position.getTotalValue() : 0.0)).sum();
        value = value + portfolio.getPutPositions().stream().mapToDouble(position -> (position.getTotalValue()!=null ? position.getTotalValue() : 0.0)).sum();
        portfolio.setNetAssetValue(value);
    }
    public static void calculateStockPosition(TickerDTO ticker, Portfolio portfolio) {
        portfolio.getStockPositions().forEach(position -> {
            if (position.getShareCode().equalsIgnoreCase(ticker.getTickerCode())) {
                position.setCurrentValue(ticker.getMarketValue());
                position.setTotalValue(position.getCurrentValue() * position.getNoOfShares());
            }
        });
    }

    public static void calculateCallOptions(TickerDTO ticker, Portfolio portfolio) {
        portfolio.getCallPositions().forEach(position -> {
            if (position.getShareCode().equalsIgnoreCase(ticker.getTickerCode())) {
                Double theoreticalValue = GBMotionCalculator.calculateTheoreticalValueForCallOption(position.getStrikePrice(),ticker);
                position.setTheoreticalValue(theoreticalValue);
                if(position.getTickerType().equalsIgnoreCase("SHORT")){
                    position.setTotalValue(theoreticalValue * position.getNoOfShares() * -1);
                }else {
                    position.setTotalValue(theoreticalValue * position.getNoOfShares());
                }
            }
        });
    }
    public static void calculatePutOptions(TickerDTO ticker, Portfolio portfolio) {
        portfolio.getPutPositions().forEach(position -> {
            if (position.getShareCode().equalsIgnoreCase(ticker.getTickerCode())) {
                Double theoreticalValue = GBMotionCalculator.calculateTheoreticalValueForPutOption(position.getStrikePrice(),ticker);
                position.setTheoreticalValue(theoreticalValue);
                if(position.getTickerType().equalsIgnoreCase("SHORT")){
                    position.setTotalValue(theoreticalValue * position.getNoOfShares() * -1);
                }else {
                    position.setTotalValue(theoreticalValue* position.getNoOfShares());
                }
            }
        });
    }
}
