package com.ms.portfoliomanager.service.file;

import javax.servlet.http.HttpServletResponse;

public interface FileService {

    void getPortfolioFileOnDemand(HttpServletResponse response);
}