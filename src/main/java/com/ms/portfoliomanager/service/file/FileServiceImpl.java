package com.ms.portfoliomanager.service.file;

import com.ms.portfoliomanager.domainValue.PositionType;
import com.ms.portfoliomanager.model.CallPosition;
import com.ms.portfoliomanager.model.Portfolio;
import com.ms.portfoliomanager.model.PutPosition;
import com.ms.portfoliomanager.model.StockPosition;
import com.ms.portfoliomanager.util.CommonUtil;
import com.ms.portfoliomanager.util.ExportToExcelUtil;
import com.ms.portfoliomanager.util.UtilityConstants;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

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
            List<StockPosition> stockPositions = portfolio.getStockPositions();
            if (stockPositions !=null){
                for (StockPosition pos : stockPositions){
                    dataBody.append(PositionType.STOCK).append(UtilityConstants.COMMA)
                            .append(pos.getShareCode()).append(UtilityConstants.COMMA)
                            .append(pos.getShareName()).append(UtilityConstants.COMMA)
                            .append(pos.getNoOfShares()).append(UtilityConstants.COMMA)
                            .append(pos.getCurrentValue()).append(UtilityConstants.COMMA)
                            .append(pos.getTotalValue()).append(UtilityConstants.COMMA)
                            .append(System.lineSeparator());
                }

            }

            List<CallPosition> callPositions = portfolio.getCallPositions();
            if (stockPositions !=null){
                for (CallPosition pos : callPositions){
                    dataBody.append(PositionType.CALL).append(UtilityConstants.COMMA)
                            .append(pos.getShareCode()).append(UtilityConstants.COMMA)
                            .append(pos.getShareName()).append(UtilityConstants.COMMA)
                            .append(pos.getNoOfShares()).append(UtilityConstants.COMMA)
                            .append(pos.getTheoreticalValue()).append(UtilityConstants.COMMA)
                            .append(pos.getTotalValue()).append(UtilityConstants.COMMA)
                            .append(System.lineSeparator());
                }

            }

            List<PutPosition> putPositions = portfolio.getPutPositions();
            if (putPositions !=null){
                for (PutPosition pos : putPositions){
                    dataBody.append(PositionType.PUT).append(UtilityConstants.COMMA)
                            .append(pos.getShareCode()).append(UtilityConstants.COMMA)
                            .append(pos.getShareName()).append(UtilityConstants.COMMA)
                            .append(pos.getNoOfShares()).append(UtilityConstants.COMMA)
                            .append(pos.getTheoreticalValue()).append(UtilityConstants.COMMA)
                            .append(pos.getTotalValue()).append(UtilityConstants.COMMA)
                            .append(System.lineSeparator());
                }

            }
            dataBody.append(System.lineSeparator())
                    .append(UtilityConstants.COMMA).append(UtilityConstants.COMMA).append(UtilityConstants.COMMA).append(UtilityConstants.COMMA)
                    .append(UtilityConstants.NET_ASSET_VALUE).append(UtilityConstants.COMMA).append(portfolio.getNetAssetValue());
        }
        String fileNamePrefix = "'_PORTFOLIO_REPORT_";
        String fileName = portfolio.getUserName().toUpperCase()+fileNamePrefix + CommonUtil.getDateString(LocalDateTime.now(),"ddMMMyy")+".xlsx";
        ExportToExcelUtil.exportExcel(response,dataHeader,dataBody.toString(),fileName, UtilityConstants.COMMA);
    }
}
