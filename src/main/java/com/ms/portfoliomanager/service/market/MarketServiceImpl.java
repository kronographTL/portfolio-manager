package com.ms.portfoliomanager.service.market;

import com.ms.portfoliomanager.data.MarketRepository;
import com.ms.portfoliomanager.model.Ticker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketServiceImpl implements  MarketService {

    @Autowired
    MarketRepository marketRepository;
    @Override
    public List<Ticker> getAllTickers() {
        //List<Ticker> tickers = marketRepository.findAll();
        return marketRepository.findAll();//tickers;//Arrays.asList("A","B","C");
    }
}
