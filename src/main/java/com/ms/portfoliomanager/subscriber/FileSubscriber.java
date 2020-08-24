package com.ms.portfoliomanager.subscriber;

import com.ms.portfoliomanager.service.file.FileService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Log
@RestController
@RequestMapping("/portfolio")
public class FileSubscriber {

    @Autowired
    private FileService fileService;

    @GetMapping("/download.excel")
    public void getPortfolioFileOnDemand(HttpServletResponse response) {
        log.info("Started Publishing Portfolio Publisher");
        fileService.getPortfolioFileOnDemand(response);
    }
}
