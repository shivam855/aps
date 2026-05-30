package com.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ExcelUtil - Read test data from Excel (.xlsx) files using Apache POI.
 *
 * USAGE EXAMPLES:
 *
 * 1. Read a single cell:
 *    String value = ExcelUtil.getCellData("TestData.xlsx", "LoginData", 1, 0);
 *
 * 2. Read entire sheet as List of Maps:
 *    List<Map<String,String>> data = ExcelUtil.getSheetData("TestData.xlsx", "LoginData");
 *
 * 3. Use with @DataProvider in TestNG:
 *    @DataProvider
 *    public Object[][] loginData() {
 *        return ExcelUtil.getDataAsArray("TestData.xlsx", "LoginData");
 *    }
 */
public class ExcelUtil {

    private static final Logger log = LogManager.getLogger(ExcelUtil.class);

    // -------------------------------------------------------
    // Read a single cell value (row and col are 0-indexed)
    // -------------------------------------------------------
    public static String getCellData(String filePath, String sheetName, int row, int col) {
        String value = "";
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) throw new RuntimeException("Sheet '" + sheetName + "' not found.");

            Row dataRow = sheet.getRow(row);
            if (dataRow == null) return "";

            Cell cell = dataRow.getCell(col);
            if (cell == null) return "";

            value = getCellValueAsString(cell);
            log.debug("Read cell[" + row + "][" + col + "] = " + value);

        } catch (IOException e) {
            log.error("Error reading Excel file: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return value;
    }

    // -------------------------------------------------------
    // Read full sheet - returns List of Maps (header -> value)
    // First row is treated as HEADER row.
    // -------------------------------------------------------
    public static List<Map<String, String>> getSheetData(String filePath, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) throw new RuntimeException("Sheet '" + sheetName + "' not found.");

            Row headerRow = sheet.getRow(0);
            int colCount = headerRow.getLastCellNum();

            // Get column headers
            List<String> headers = new ArrayList<>();
            for (int col = 0; col < colCount; col++) {
                headers.add(getCellValueAsString(headerRow.getCell(col)));
            }

            // Read data rows
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) continue;

                Map<String, String> rowData = new HashMap<>();
                for (int col = 0; col < colCount; col++) {
                    String header = headers.get(col);
                    String value = getCellValueAsString(row.getCell(col));
                    rowData.put(header, value);
                }
                dataList.add(rowData);
            }
            log.info("Read " + dataList.size() + " rows from sheet: " + sheetName);

        } catch (IOException e) {
            log.error("Error reading Excel file: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return dataList;
    }

    // -------------------------------------------------------
    // Get data as Object[][] for TestNG @DataProvider
    // -------------------------------------------------------
    public static Object[][] getDataAsArray(String filePath, String sheetName) {
        List<Map<String, String>> dataList = getSheetData(filePath, sheetName);
        Object[][] data = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i);
        }
        return data;
    }

    // -------------------------------------------------------
    // Get row count (excluding header)
    // -------------------------------------------------------
    public static int getRowCount(String filePath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            return sheet == null ? 0 : sheet.getLastRowNum();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // -------------------------------------------------------
    // Convert any cell type to String
    // -------------------------------------------------------
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }
}
