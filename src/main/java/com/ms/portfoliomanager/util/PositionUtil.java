package com.ms.portfoliomanager.util;

import com.ms.portfoliomanager.domainValue.PositionType;
import com.ms.portfoliomanager.model.CallPosition;
import com.ms.portfoliomanager.model.PutPosition;
import com.ms.portfoliomanager.model.StockPosition;

public class PositionUtil {

    public static void getStockPositionElementsWithComaSeparation(StringBuilder dataBody, StockPosition position) {
        dataBody.append(PositionType.STOCK).append(UtilityConstants.COMMA)
                .append(position.getShareCode()).append(UtilityConstants.COMMA)
                .append(position.getShareName()).append(UtilityConstants.COMMA)
                .append(position.getNoOfShares()).append(UtilityConstants.COMMA)
                .append(position.getCurrentValue()).append(UtilityConstants.COMMA)
                .append(position.getTotalValue()).append(UtilityConstants.COMMA)
                .append(System.lineSeparator());
    }
    public static void getCallPositionElementsWithComaSeparation(StringBuilder dataBody, CallPosition position) {
        dataBody.append(PositionType.CALL).append(UtilityConstants.COMMA)
                .append(position.getShareCode()).append(UtilityConstants.COMMA)
                .append(position.getShareName()).append(UtilityConstants.COMMA)
                .append(position.getNoOfShares()).append(UtilityConstants.COMMA)
                .append(position.getTheoreticalValue()).append(UtilityConstants.COMMA)
                .append(position.getTotalValue()).append(UtilityConstants.COMMA)
                .append(System.lineSeparator());
    }
    public static void getPutPositionElementsWithComaSeparation(StringBuilder dataBody, PutPosition position) {
        dataBody.append(PositionType.PUT).append(UtilityConstants.COMMA)
                .append(position.getShareCode()).append(UtilityConstants.COMMA)
                .append(position.getShareName()).append(UtilityConstants.COMMA)
                .append(position.getNoOfShares()).append(UtilityConstants.COMMA)
                .append(position.getTheoreticalValue()).append(UtilityConstants.COMMA)
                .append(position.getTotalValue()).append(UtilityConstants.COMMA)
                .append(System.lineSeparator());
    }
}
