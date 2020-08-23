package com.ms.portfoliomanager.util;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ExportToExcelUtil {

    public static void exportExcel(HttpServletResponse response, String title, String data, String fileName, String splitStr)  {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "utf-8"));
            exportExcel(title, data, splitStr, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void exportExcel(String title, String data, String splitStr, OutputStream out) throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();
        try {
            String sheetName = "Sheet1";
            XSSFSheet sheet = wb.createSheet(sheetName);
            writeExcel(wb, sheet, title, data, splitStr);

            wb.write(out);
        } finally {
            wb.close();
        }
    }

    private static void writeExcel(XSSFWorkbook wb, Sheet sheet, String title, String data, String splitStr) {
        int rowIndex = 0;
        List<String> titleList = new ArrayList<>();
        titleList.add(title);

        rowIndex = writeTitlesToExcel(wb, sheet, titleList);
        writeRowsToExcel(wb, sheet, data, rowIndex, splitStr);
        autoSizeColumns(sheet, title.split(UtilityConstants.COMMA).length);
    }

    private static int writeTitlesToExcel(XSSFWorkbook wb, Sheet sheet, List<String> titles) {
        int rowIndex = 0;
        int colIndex;

        XSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        for (String title : titles) {
            colIndex = 0;
            Row titleRow = sheet.createRow(rowIndex);
            String[] titleList = title.split(UtilityConstants.COMMA);
            for (String field : titleList) {
                Cell cell = titleRow.createCell(colIndex);
                cell.setCellValue(field);
                cell.setCellStyle(titleStyle);
                colIndex++;
            }
            rowIndex++;
        }
        return rowIndex;
    }

    private static int writeRowsToExcel(XSSFWorkbook wb, Sheet sheet, String data, int rowIndex, String splitStr) {
        int colIndex = 0;
        XSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        String[] rows = data.split("\n");

        for (String rowData : rows) {
            Row dataRow = sheet.createRow(rowIndex);
            colIndex = 0;

            String[] rowItems = rowData.split(splitStr) ;
            for(int i = 0; i < rowItems.length; i++){
                String cellData = rowItems[i];
                Cell cell = dataRow.createCell(colIndex);
                if (cellData != null) {
                    cell.setCellValue(cellData);
                } else {
                    cell.setCellValue(UtilityConstants.EMPTY_STRING);
                }
                cell.setCellStyle(titleStyle);
                colIndex++;
            }
            rowIndex++;
        }
        return rowIndex;
    }

    private static void autoSizeColumns(Sheet sheet, int columnNumber) {

        for (int i = 0; i < columnNumber; i++) {
            int orgWidth = sheet.getColumnWidth(i);
            sheet.autoSizeColumn(i, true);
            int newWidth = (sheet.getColumnWidth(i) + 100);
            if (newWidth > orgWidth) {
                sheet.setColumnWidth(i, newWidth);
            } else {
                sheet.setColumnWidth(i, orgWidth);
            }
        }
    }
}