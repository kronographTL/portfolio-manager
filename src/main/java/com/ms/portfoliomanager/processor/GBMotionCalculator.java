package com.ms.portfoliomanager.processor;

import com.ms.portfoliomanager.model.TickerDTO;
import com.ms.portfoliomanager.util.UtilityConstants;
import org.apache.commons.math3.distribution.NormalDistribution;

public class GBMotionCalculator {
    public static final int TIME = 1;
    public static final int FREE_INTEREST_RATE = 2;

    public static double geometricMotionForStocks(Double mu, Double sigma, Double time, Double price, Double normalDistFactor){
        double part = time/UtilityConstants.GBMConstant;
        double muPart = mu*part;
        double ndPart = (sigma*normalDistFactor)*Math.sqrt(part);
        double delS = price*(muPart+ndPart);
        return price + delS;
    }

    public static Double calculateTheoreticalValueForCallOption(Double lockedValue, TickerDTO ticker) {
        Double callValue ; //lockedValue = K in the formula
        double marketValue = ticker.getMarketValue(); // S in the formula
        double d1 = getD1(marketValue,lockedValue,ticker.getAnnualizedStandardDeviation());
        double d2 = getD2(d1,ticker.getAnnualizedStandardDeviation());
        NormalDistribution normalDistribution = new NormalDistribution();
        double cumulativeProbabilityOfD1 = normalDistribution.cumulativeProbability(d1);
        double cumulativeProbabilityOfD2 = normalDistribution.cumulativeProbability(d2);

        // 𝑐 = 𝑆𝑁(𝑑1) − 𝐾𝑒−𝑟t𝑁(𝑑2) --> formula to calculate CallOption Price
        callValue = (marketValue*cumulativeProbabilityOfD1) - getEquationWithStrikePart(lockedValue, cumulativeProbabilityOfD2);
        return callValue;
    }

    private static double getEquationWithStrikePart(Double lockedValue, double cumulativeProbabilityOfD2) {
        return lockedValue* Math.exp(-1*FREE_INTEREST_RATE*TIME)*cumulativeProbabilityOfD2;
    }

    public static Double calculateTheoreticalValueForPutOption(Double lockedValue, TickerDTO ticker) {
        Double putValue;
        // lockedValue = K in the formula
        double marketValue = ticker.getMarketValue(); // S in the formula
        double d1 = getD1(marketValue,lockedValue,ticker.getAnnualizedStandardDeviation());
        double d2 = getD2(d1,ticker.getAnnualizedStandardDeviation());
        NormalDistribution normalDistribution = new NormalDistribution();
        double cumulativeProbabilityOfD1 = normalDistribution.cumulativeProbability(-d1);
        double cumulativeProbabilityOfD2 = normalDistribution.cumulativeProbability(-d2);
        //𝑝 = 𝐾𝑒−𝑟t𝑁(−𝑑2) − 𝑆𝑁(−𝑑1) --> formula to calculate Put Options value
        putValue = getEquationWithStrikePart(lockedValue,cumulativeProbabilityOfD2) - (marketValue*cumulativeProbabilityOfD1);
        return putValue;
    }

    private static double getD2(double d1, Double sigma) {
        double denominator = sigma*Math.sqrt(1);
        return  d1 - denominator;
    }

    private static double getD1(double marketValue, Double lockedValue,Double sigma) {
        double logSK = Math.log(marketValue/lockedValue);  // ln(S/K)
        double rateAnnualDeviation = TIME*(FREE_INTEREST_RATE + ((sigma*sigma)/2));// (𝑟 + 𝜎2/2 )t
        double numerator = logSK + rateAnnualDeviation;
        double denominator = sigma*Math.sqrt(TIME);
        return  numerator/denominator;
    }

}
