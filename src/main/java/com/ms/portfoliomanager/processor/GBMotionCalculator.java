package com.ms.portfoliomanager.processor;

import org.apache.commons.math3.distribution.NormalDistribution;

public class GBMotionCalculator {

    public static double geometricMotion(double mu, double sigma,int time,double price,double nd){
        NormalDistribution normalDistribution = new NormalDistribution(mu ,
                sigma);
        nd = normalDistribution.inverseCumulativeProbability(nd);
        double part = (double) time/7257600;
        double muPart = mu*part;
        double ndPart = (sigma*nd)*Math.sqrt(part);
        double delS = price*(muPart+ndPart);

        return price + delS;
    }
}
