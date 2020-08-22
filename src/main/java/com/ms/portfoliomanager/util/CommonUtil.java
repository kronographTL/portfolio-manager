package com.ms.portfoliomanager.util;

import com.ms.portfoliomanager.model.Ticker;
import com.ms.portfoliomanager.processor.GBMotionCalculator;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class CommonUtil {



    public static String getDateString(LocalDateTime dateTime, String datePatten) {
        if (dateTime == null) {
            return UtilityConstants.EMPTY_STRING;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(datePatten, Locale.US);
            Date date = localDateTimeToDate(dateTime);
            //return String.valueOf(sdf.format(date));
            return sdf.format(date);
        }
    }

    private static Date localDateTimeToDate(LocalDateTime dateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = dateTime.atZone(zone).toInstant();
        //java.util.Date date = Date.from(instant);
        return Date.from(instant);
    }


    public static Ticker generateSharePrice(Ticker ticker, int delay) {
        // TODO Brownian Motion
        //converting time in seconds
        double timeInSeconds = delay/10;
        Double value = GBMotionCalculator.geometricMotion(ticker.getExpectedReturn(),ticker.getAnnualizedStandardDeviation(),timeInSeconds,ticker.getMarketValue(),0.33);//new Random().nextDouble();
        ticker.setMarketValue(value);
        return ticker;
    }


}
