package com.ms.portfoliomanager.service.file;

import com.ms.portfoliomanager.model.Portfolio;
import com.ms.portfoliomanager.model.Position;
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



    Boolean isPublish = false;
    Portfolio portfolio = null;
    @Override
    public void getPortfolioFileOnDemand(HttpServletResponse response) {
        isPublish = true;
        //Portfolio portfolio = receiveMessage(Portfolio.builder().build());
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
            //writeDataToExcelUsingString(httpServletResponse,portfolio);
            isPublish = false;
        }

    }

    private void writeDataToExcelUsingString(HttpServletResponse response, Portfolio portfolio) {
        String dataHeader = " shareCode,shareName,noOfShares,currentValue,totalValue,Net Asset Value";//TODO these can be Picked from the Properties files
        StringBuilder dataBody = new StringBuilder();
        if(portfolio!=null){
            List<Position> positions = portfolio.getPositions();
            if (positions!=null){
                for (Position pos : positions){
                    dataBody.append(pos.getShareCode()).append(",")
                            .append(pos.getShareName()).append(",")
                            .append(pos.getNoOfShares()).append(",")
                            .append(pos.getCurrentValue()).append(",")
                            .append(pos.getTotalValue()).append(",")
                            .append(portfolio.getNetAssetValue()).append(",")
                            .append(System.lineSeparator());
                }

            }
        }
        String fileNamePrefix = "PORTFOLIO_REPORT_";
        String fileName = fileNamePrefix + CommonUtil.getDateString(LocalDateTime.now(),"ddMMMyy")+".xlsx";
        ExportToExcelUtil.exportExcel(response,dataHeader,dataBody.toString(),fileName, UtilityConstants.COMMA);
    }
}
