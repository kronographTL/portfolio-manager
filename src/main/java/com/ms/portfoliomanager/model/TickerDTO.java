package com.ms.portfoliomanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TickerDTO {

    private String tickerCode;
    private String shareName;
    private Double marketValue;

}