package com.ms.portfoliomanager.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class Position {

    private String shareCode;
    private String shareName;
    private Integer noOfShares;
    private Double currentValue;
    private Double totalValue;
}
