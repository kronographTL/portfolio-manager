package com.ms.portfoliomanager.service.market;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MarketServiceImpl implements  MarketService {
    @Override
    public List<String> getAllTickers() {
        return Arrays.asList("A","B","C");
    }
}
