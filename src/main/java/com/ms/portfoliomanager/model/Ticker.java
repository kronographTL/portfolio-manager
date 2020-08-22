package com.ms.portfoliomanager.model;

import lombok.*;

import javax.persistence.*;

@Entity(name = "ticker")
@Table(name = "ticker")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class Ticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String tickerId;
    private String tickerCode;
    private String shareName;
    private Double marketValue;
    private Double expectedReturn;
    private Double annualizedStandardDeviation;


}
