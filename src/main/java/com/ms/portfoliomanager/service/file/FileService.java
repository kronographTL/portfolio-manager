package com.ms.portfoliomanager.service.file;

import javax.servlet.http.HttpServletResponse;

public interface FileService {

    public void getPortfolioFileOnDemand(HttpServletResponse response);
}