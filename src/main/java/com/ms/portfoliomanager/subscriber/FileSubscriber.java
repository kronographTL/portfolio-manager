package com.ms.portfoliomanager.subscriber;

import com.ms.portfoliomanager.publisher.PortfolioPublisher;
import com.ms.portfoliomanager.service.file.FileService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;
import javax.servlet.http.HttpServletResponse;

@Log
@RestController
@RequestMapping("/portfolio")
public class FileSubscriber {

    @Autowired
    private FileService fileService;

    @Autowired
    PortfolioPublisher portfolioPublisher;

    @GetMapping("/download.excel")
    public void getPortfolioFileOnDemand(HttpServletResponse response) throws JMSException {
        portfolioPublisher.publish();
        log.info("Started Publishing Portfoilo Publisher");
        fileService.getPortfolioFileOnDemand(response);
    }
}
