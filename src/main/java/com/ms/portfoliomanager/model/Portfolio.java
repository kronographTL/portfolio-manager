package com.ms.portfoliomanager.model;

import lombok.*;

import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Portfolio {

    private String userId;
    private String userName;
    private List<CommonStock> commonStocks;
    //private List<PutPosition> putPositions;
    private List<Option> options;
    private Double netAssetValue;

}
