package com.ms.portfoliomanager.util;

import com.ms.portfoliomanager.model.TickerDTO;
import com.ms.portfoliomanager.processor.GBMotionCalculator;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class CommonUtil {

    private static Random random = new Random();
    public static String getDateString(LocalDateTime dateTime, String datePatten) {
        if (dateTime == null) {
            return UtilityConstants.EMPTY_STRING;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(datePatten, Locale.US);
            Date date = localDateTimeToDate(dateTime);
            return sdf.format(date);
        }
    }

    private static Date localDateTimeToDate(LocalDateTime dateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = dateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }


    public static TickerDTO generateSharePrice(TickerDTO ticker, int delay) {
        double timeInSeconds = delay/10;
        Double value = GBMotionCalculator.geometricMotionForStocks(ticker.getExpectedReturn(),ticker.getAnnualizedStandardDeviation(),timeInSeconds,ticker.getMarketValue(),random.nextGaussian());
        if(value<=0){
          return generateSharePrice(ticker,delay);
        }
        ticker.setMarketValue(value);
        return ticker;
    }

}
