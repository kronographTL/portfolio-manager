package com.ms.portfoliomanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommonStock {

    private String shareCode;
    private String shareName;
    private Integer noOfShares;
    private Double totalValue;
    private Double currentValue;

}
