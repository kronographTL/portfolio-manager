package com.ms.portfoliomanager.service.file;

import com.ms.portfoliomanager.model.Portfolio;
import com.ms.portfoliomanager.util.CommonUtil;
import com.ms.portfoliomanager.util.ExportToExcelUtil;
import com.ms.portfoliomanager.util.PositionUtil;
import com.ms.portfoliomanager.util.UtilityConstants;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Service
public class FileServiceImpl implements FileService{

    private Boolean isPublish = false;
    private Portfolio portfolio = null;

    @Override
    public void getPortfolioFileOnDemand(HttpServletResponse response) {
        isPublish = true;
        while(isPublish) {
            if(portfolio!=null) {
                isPublish = false;
                writeDataToExcelUsingString(response, portfolio);
                portfolio = null;
            }
        }
    }
   @JmsListener(destination = "user_01.topic", containerFactory = "topicListenerFactory")
    private void receiveMessage(Portfolio portfolio) {
        if(isPublish){
            this.portfolio = portfolio;
            isPublish = false;
        }

    }

    private void writeDataToExcelUsingString(HttpServletResponse response, Portfolio portfolio) {
        String dataHeader = UtilityConstants.DATA_HEADER;
        StringBuilder dataBody = new StringBuilder();
        if(portfolio!=null){
            if (portfolio.getCommonStocks() !=null){
                portfolio.getCommonStocks().forEach(position -> PositionUtil.getCommonStockElementsWithComaSeparation(dataBody,position));
            }
            if (portfolio.getOptions() !=null){
                portfolio.getOptions().forEach(position -> PositionUtil.getOptionElementsWithComaSeparation(dataBody,position));

            }
            dataBody.append(System.lineSeparator())
                    .append(UtilityConstants.COMMA).append(UtilityConstants.COMMA).append(UtilityConstants.COMMA).append(UtilityConstants.COMMA)
                    .append(UtilityConstants.NET_ASSET_VALUE).append(UtilityConstants.COMMA).append(portfolio.getNetAssetValue());
        }
        String fileNamePrefix = "'s_PORTFOLIO_REPORT_";
        String fileName = portfolio.getUserName().toUpperCase()+fileNamePrefix + CommonUtil.getDateString(LocalDateTime.now(),"ddMMMyy")+".xlsx";
        ExportToExcelUtil.exportExcel(response,dataHeader,dataBody.toString(),fileName, UtilityConstants.COMMA);
    }

}
