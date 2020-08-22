package com.ms.portfoliomanager.data;

import com.ms.portfoliomanager.model.Ticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketRepository  extends JpaRepository<Ticker,String>{
    Ticker findByTickerCode(String tickerCode);
}
