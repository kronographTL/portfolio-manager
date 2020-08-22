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


    public static Ticker generateSharePrice(Ticker ticker) {
        // TODO Brownian Motion
        Double value = GBMotionCalculator.geometricMotion(0.25,0.134,1,ticker.getInitialMarketValue(),0.33);//new Random().nextDouble();
        ticker.setInitialMarketValue(value);
        return ticker;
    }


}
