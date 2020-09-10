package com.ms.portfoliomanager.controller;

import com.ms.portfoliomanager.model.Portfolio;
import com.ms.portfoliomanager.processor.PortfolioManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(TradeController.class)
@AutoConfigureMockMvc(addFilters = false)
class TradeControllerTest {


    @Autowired
    MockMvc mockMvc;

    @MockBean
    private PortfolioManager portfolioManager;
    @Test
    void startTrade() throws Exception {
        Portfolio portfolio = Portfolio.builder().userName("DOW JOE").userId("USER_01").build();
        when(portfolioManager.createPortfolio(any())).thenReturn(portfolio);
        mockMvc.perform(MockMvcRequestBuilders.get("/trade")
                .contentType(MediaType.TEXT_PLAIN));
    }
}