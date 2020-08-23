package com.ms.portfoliomanager.service.market;

import com.ms.portfoliomanager.model.Ticker;

import java.util.List;
import java.util.Set;

public interface MarketService {

    List<Ticker> getAllTickers();
    List<Ticker> getAllTickersById(Set<String> ids);
}

