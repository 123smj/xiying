/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.poi.hssf.usermodel.HSSFCell
 *  org.apache.poi.hssf.usermodel.HSSFRichTextString
 *  org.apache.poi.hssf.usermodel.HSSFRow
 *  org.apache.poi.hssf.usermodel.HSSFSheet
 *  org.apache.poi.hssf.usermodel.HSSFWorkbook
 *  org.apache.poi.xssf.usermodel.XSSFCell
 *  org.apache.poi.xssf.usermodel.XSSFRow
 *  org.apache.poi.xssf.usermodel.XSSFSheet
 *  org.apache.poi.xssf.usermodel.XSSFWorkbook
 */
package com.gy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelParse {
    public static void writeToFile(String sourceFile, String targetFile) throws IOException {
        try {
            File file = new File(sourceFile);
            if (file.isDirectory()) {
                File[] fl = file.listFiles();
                int i = 0;
                while (i < fl.length) {
                    ExcelParse.writeToFile(fl[i].getPath(), String.valueOf(targetFile) + "/" + fl[i].getName());
                    ++i;
                }
            } else {
                int slen;
                File toFile = new File(targetFile);
                if (toFile.isDirectory()) {
                    toFile = new File(String.valueOf(targetFile) + "/" + file.getName());
                }
                toFile.createNewFile();
                FileInputStream is = new FileInputStream(file);
                FileOutputStream fos = new FileOutputStream(toFile);
                byte[] c = new byte[1024];
                while ((slen = is.read(c, 0, c.length)) != -1) {
                    fos.write(c, 0, slen);
                }
                fos.flush();
                fos.close();
                is.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }

    public static List<List<String>> readXls(String path) throws Exception {
        FileInputStream is = new FileInputStream(new File(path));
        HSSFWorkbook hhsfWorkbook = new HSSFWorkbook((InputStream)is);
        ArrayList<List<String>> result = new ArrayList<List<String>>();
        int numSheet = 0;
        while (numSheet < hhsfWorkbook.getNumberOfSheets()) {
            HSSFSheet hssfSheet = hhsfWorkbook.getSheetAt(numSheet);
            if (hssfSheet != null) {
                int rowNum = 0;
                while (rowNum <= hssfSheet.getLastRowNum()) {
                    HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                    int minColIx = hssfRow.getFirstCellNum();
                    short maxColIx = hssfRow.getLastCellNum();
                    ArrayList<String> rowList = new ArrayList<String>();
                    int colIx = minColIx;
                    while (colIx < maxColIx) {
                        HSSFCell cell = hssfRow.getCell(colIx);
                        if (cell != null) {
                            rowList.add(ExcelParse.getStringValue(cell));
                        }
                        ++colIx;
                    }
                    result.add(rowList);
                    ++rowNum;
                }
            }
            ++numSheet;
        }
        return result;
    }

    public static List<List<String>> readXls(String path, int sheetNum) throws Exception {
        FileInputStream is = new FileInputStream(new File(path));
        HSSFWorkbook hhsfWorkbook = new HSSFWorkbook((InputStream)is);
        HSSFSheet hssfSheet = hhsfWorkbook.getSheetAt(sheetNum);
        if (hssfSheet == null) {
            return null;
        }
        ArrayList<List<String>> result = new ArrayList<List<String>>();
        System.out.println("\u884c\u6570\uff1a" + hssfSheet.getLastRowNum());
        int rowNum = 0;
        while (rowNum <= hssfSheet.getLastRowNum()) {
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            if (hssfRow == null) break;
            int minColIx = hssfRow.getFirstCellNum();
            short maxColIx = hssfRow.getLastCellNum();
            ArrayList<String> rowList = new ArrayList<String>();
            int colIx = minColIx;
            while (colIx < maxColIx) {
                HSSFCell cell = hssfRow.getCell(colIx);
                if (cell != null) {
                    rowList.add(ExcelParse.getStringValue(cell));
                }
                ++colIx;
            }
            result.add(rowList);
            ++rowNum;
        }
        return result;
    }

    public static List<List<String>> readXlsx(String path) throws Exception {
        FileInputStream is = new FileInputStream(new File(path));
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook((InputStream)is);
        ArrayList<List<String>> result = new ArrayList<List<String>>();
        for (XSSFSheet xssfSheet : xssfWorkbook) {
            if (xssfSheet == null) continue;
            int rowNum = 1;
            while (rowNum <= xssfSheet.getLastRowNum()) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                int minColIx = xssfRow.getFirstCellNum();
                short maxColIx = xssfRow.getLastCellNum();
                ArrayList<String> rowList = new ArrayList<String>();
                int colIx = minColIx;
                while (colIx < maxColIx) {
                    XSSFCell cell = xssfRow.getCell(colIx);
                    if (cell != null) {
                        rowList.add(cell.toString());
                    }
                    ++colIx;
                }
                result.add(rowList);
                ++rowNum;
            }
        }
        return result;
    }

    public static void reWriteXlsx(String path, String oldValue, String newValue) throws Exception {
        FileInputStream is = new FileInputStream(new File(path));
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook((InputStream)is);
        for (XSSFSheet xssfSheet : xssfWorkbook) {
            if (xssfSheet == null) continue;
            int rowNum = 1;
            while (rowNum <= xssfSheet.getLastRowNum()) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                int minColIx = xssfRow.getFirstCellNum();
                short maxColIx = xssfRow.getLastCellNum();
                int colIx = minColIx;
                while (colIx < maxColIx) {
                    XSSFCell cell = xssfRow.getCell(colIx);
                    if (cell != null) {
                        String cellValue = cell.toString();
                        String newCellValue = "";
                        if (cellValue.indexOf(oldValue) != -1) {
                            newCellValue = cellValue.replace(oldValue, newValue);
                            cell.setCellValue(newCellValue);
                        }
                    }
                    ++colIx;
                }
                ++rowNum;
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        xssfWorkbook.write((OutputStream)fileOutputStream);
    }

    public static void reWriteXlsxWithNextFloorData(String path, TreeMap<String, String> floorData, String keyPrefix, String valuePrefix, String valueSuffix, String floorNameLike, boolean isDown) throws Exception {
        FileInputStream is = new FileInputStream(new File(path));
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook((InputStream)is);
        for (XSSFSheet xssfSheet : xssfWorkbook) {
            if (xssfSheet == null) continue;
            int rowNum = 1;
            while (rowNum <= xssfSheet.getLastRowNum()) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                int minColIx = xssfRow.getFirstCellNum();
                short maxColIx = xssfRow.getLastCellNum();
                int colIx = minColIx;
                while (colIx < maxColIx) {
                    XSSFCell cell = xssfRow.getCell(colIx);
                    if (cell != null) {
                        String cellValue = cell.toString();
                        String newCellValue = "";
                        cellValue = cell.toString();
                        int keyIndex = cellValue.indexOf(keyPrefix);
                        int valueIndex = cellValue.indexOf(valuePrefix);
                        if (keyIndex != -1 && valueIndex != -1) {
                            String newValue;
                            String key = cellValue.substring(keyPrefix.length() + keyIndex, valueIndex);
                            String value = cellValue.substring(valuePrefix.length() + valueIndex, cellValue.length() - valueSuffix.length());
                            String newKey = "";
                            String oldIndex = key.replace(floorNameLike, "");
                            int newIndex = 0;
                            if (isDown) {
                                newIndex = Integer.valueOf(oldIndex) - 1;
                                if (newIndex == 0) {
                                    newIndex = -1;
                                }
                            } else {
                                newIndex = Integer.valueOf(oldIndex) + 1;
                                if (newIndex == 0) {
                                    newIndex = 1;
                                }
                            }
                            if ((newValue = floorData.get(newKey = key.replace(oldIndex, String.valueOf(newIndex)))) != null && !newValue.equals(value)) {
                                newCellValue = String.valueOf(keyPrefix) + newKey + valuePrefix + newValue + valueSuffix;
                                cell.setCellValue(newCellValue);
                            }
                        }
                    }
                    ++colIx;
                }
                ++rowNum;
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        xssfWorkbook.write((OutputStream)fileOutputStream);
    }

    public static void addXlsxColumn(String path) throws Exception {
        FileInputStream is = new FileInputStream(new File(path));
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook((InputStream)is);
        for (XSSFSheet xssfSheet : xssfWorkbook) {
            if (xssfSheet == null) continue;
            int rowNum = 1;
            while (rowNum <= xssfSheet.getLastRowNum()) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                int minColIx = xssfRow.getFirstCellNum();
                short maxColIx = xssfRow.getLastCellNum();
                int colIx = minColIx;
                while (colIx < maxColIx) {
                    int temp;
                    String cellValue;
                    XSSFCell cell = xssfRow.getCell(colIx);
                    if (cell != null && (temp = (cellValue = cell.toString()).indexOf("Elevation=")) != -1) {
                        String newColumn = cellValue.substring(temp + "Elevation=".length(), cellValue.length() - 1);
                        XSSFCell newXssfCell = xssfRow.getCell(colIx + 1) == null ? xssfRow.createCell(colIx + 1) : xssfRow.getCell(colIx + 1);
                        newXssfCell.setCellValue(newColumn);
                    }
                    ++colIx;
                }
                ++rowNum;
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        xssfWorkbook.write((OutputStream)fileOutputStream);
    }

    public static String getStringValue(HSSFCell cell) {
        switch (cell.getCellType()) {
            case 4: {
                return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
            }
            case 2: {
                try {
                    return String.valueOf(cell.getNumericCellValue());
                }
                catch (IllegalStateException e) {
                    return String.valueOf((Object)cell.getRichStringCellValue());
                }
            }
            case 0: {
                cell.setCellType(1);
                return cell.getStringCellValue();
            }
            case 1: {
                return cell.getStringCellValue();
            }
        }
        return "";
    }

    public static boolean isEmpty(String str) {
        if (str != null && !str.isEmpty()) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        String path = "E:/java/excel_parse/001.xlsx";
    }
}
