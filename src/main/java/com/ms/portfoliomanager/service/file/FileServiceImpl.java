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


    @Override
    public void getPortfolioFileOnDemand(HttpServletResponse response) {
        Portfolio portfolio = receiveMessage(Portfolio.builder().build());
        writeDataToExcelUsingString(response,portfolio);
    }
  @JmsListener(destination = "Anil_Portfolio.topic", containerFactory = "topicListenerFactory")
    private Portfolio receiveMessage(Portfolio portfolio) {
        return portfolio;
    }

    private void writeDataToExcelUsingString(HttpServletResponse response, Portfolio portfolio) {
        String dataHeader = " shareCode,shareName,noOfShares,currentValue,totalValue";
        StringBuilder dataBody = new StringBuilder();
        if(portfolio!=null){
            List<Position> positions = portfolio.getPositions();
            if (positions!=null){
                for (Position pos : positions){
                    dataBody.append(pos.getShareCode()).append(",")
                            .append(pos.getShareName()).append(",")
                            .append(pos.getNoOfShares()).append(",")
                            .append(pos.getCurrentValue()).append(",")
                            .append(pos.getTotalValue()).append(",");
                }

            }
        }
        String fileNamePrefix = "PORTFOLIO_REPORT_";
        String fileName = fileNamePrefix + CommonUtil.getDateString(LocalDateTime.now(),"ddMMMyy")+".xlsx";
        ExportToExcelUtil.exportExcel(response,dataHeader,dataBody.toString(),fileName, UtilityConstants.COMMA);
    }
}
