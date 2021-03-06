package com.ms.portfoliomanager.service.market;

import com.ms.portfoliomanager.data.MarketRepository;
import com.ms.portfoliomanager.model.Ticker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class MarketServiceImpl implements  MarketService {

    @Autowired
    MarketRepository marketRepository;

    @Override
    public List<Ticker> getAllTickers() {
        return marketRepository.findAll();
    }

    @Override
    public List<Ticker> getAllTickersById(Set<String> tickerCodes) {
        return marketRepository.findAllByTickerCode(tickerCodes);
    }


}
