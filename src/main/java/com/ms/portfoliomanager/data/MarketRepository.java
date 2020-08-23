package com.ms.portfoliomanager.data;

import com.ms.portfoliomanager.model.Ticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MarketRepository  extends JpaRepository<Ticker,String>{
    @Query("Select tick FROM ticker tick WHERE tick.tickerCode IN (:tickerCodes)")
    List<Ticker> findAllByTickerCode(@Param("tickerCodes") Set<String> tickerCodes);
}
