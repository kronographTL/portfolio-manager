package com.ms.portfoliomanager.processor;

import com.ms.portfoliomanager.util.UtilityConstants;
import org.apache.commons.math3.distribution.NormalDistribution;

public class GBMotionCalculator {

    public static double geometricMotionForStocks(Double mu, Double sigma, Double time, Double price, Double normalDistFactor){
//        NormalDistribution normalDistribution = new NormalDistribution(mu,sigma);
//        normalDistFactor = normalDistribution.inverseCumulativeProbability(normalDistFactor);
        double part = time/UtilityConstants.GBMConstant;
        double muPart = mu*part;
        double ndPart = (sigma*normalDistFactor)*Math.sqrt(part);
        double delS = price*(muPart+ndPart);
        return price + delS;
    }
    public static double geometricMotionForPutOptions(Double mu, Double sigma, Double time, Double price, Double normalDistFactor){
        NormalDistribution normalDistribution = new NormalDistribution(mu,sigma);
        normalDistFactor = normalDistribution.inverseCumulativeProbability(normalDistFactor);
        double part = time/UtilityConstants.GBMConstant;
        double muPart = mu*part;
        double ndPart = (sigma*normalDistFactor)*Math.sqrt(part);
        double delS = price*(muPart+ndPart);
        return price + delS;
    }
    public static double geometricMotionForCallOptions(Double mu, Double sigma, Double time, Double price, Double normalDistFactor){
        NormalDistribution normalDistribution = new NormalDistribution(mu,sigma);
        normalDistFactor = normalDistribution.inverseCumulativeProbability(normalDistFactor);
        double part = time/UtilityConstants.GBMConstant;
        double muPart = mu*part;
        double ndPart = (sigma*normalDistFactor)*Math.sqrt(part);
        double delS = price*(muPart+ndPart);
        return price + delS;
    }
}
