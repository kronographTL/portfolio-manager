package com.ms.portfoliomanager.util;

import com.ms.portfoliomanager.domainValue.ProductType;
import com.ms.portfoliomanager.model.CommonStock;
import com.ms.portfoliomanager.model.Option;

public class PositionUtil {

    public static void getCommonStockElementsWithComaSeparation(StringBuilder dataBody, CommonStock position) {
        dataBody.append(ProductType.STOCK).append(UtilityConstants.COMMA)
                .append(position.getShareCode()).append(UtilityConstants.COMMA)
                .append(position.getShareName()).append(UtilityConstants.COMMA)
                .append(position.getNoOfShares()).append(UtilityConstants.COMMA)
                .append(position.getCurrentValue()).append(UtilityConstants.COMMA)
                .append(position.getTotalValue()).append(UtilityConstants.COMMA)
                .append(System.lineSeparator());
    }
    public static void getOptionElementsWithComaSeparation(StringBuilder dataBody, Option position) {
        dataBody.append(position.getOptionType()).append(UtilityConstants.COMMA)
                .append(position.getShareCode()).append(UtilityConstants.COMMA)
                .append(position.getShareName()).append(UtilityConstants.COMMA)
                .append(position.getNoOfShares()).append(UtilityConstants.COMMA)
                .append(position.getTheoreticalValue()).append(UtilityConstants.COMMA)
                .append(position.getTotalValue()).append(UtilityConstants.COMMA)
                .append(System.lineSeparator());
    }
}
