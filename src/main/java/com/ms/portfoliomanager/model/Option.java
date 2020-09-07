package com.ms.portfoliomanager.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class Option {

    private String shareCode;
    private String shareName;
    private Integer noOfShares;
    private Double totalValue;
    private String tickerType;
    private Double strikePrice;
    private Double theoreticalValue;
    private String optionType;

}
