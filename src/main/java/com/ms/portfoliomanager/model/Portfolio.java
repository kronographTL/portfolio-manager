package com.ms.portfoliomanager.model;

import lombok.*;

import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Portfolio {

//    Portfolio(List<String> tickers, String userId){
//
//    }
    private String userId;
    private String userName;
    private List<Position> positions;
    private Double netAssetValue;

}