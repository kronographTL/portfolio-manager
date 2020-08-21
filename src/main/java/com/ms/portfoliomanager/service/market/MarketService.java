package com.ms.portfoliomanager.service.market;

import com.ms.portfoliomanager.model.Ticker;

import java.util.List;

public interface MarketService {
    List<Ticker> getAllTickers();
}

