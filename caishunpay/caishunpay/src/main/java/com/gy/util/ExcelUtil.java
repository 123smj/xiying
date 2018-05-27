/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.poi.hssf.usermodel.HSSFCell
 *  org.apache.poi.hssf.usermodel.HSSFCellStyle
 *  org.apache.poi.hssf.usermodel.HSSFFont
 *  org.apache.poi.hssf.usermodel.HSSFRow
 *  org.apache.poi.hssf.usermodel.HSSFSheet
 *  org.apache.poi.hssf.usermodel.HSSFWorkbook
 *  org.apache.poi.ss.usermodel.Cell
 *  org.apache.poi.ss.usermodel.IndexedColors
 *  org.apache.poi.ss.usermodel.Row
 *  org.apache.poi.ss.util.CellRangeAddress
 */
package com.gy.util;

import com.gy.system.SysParamUtil;
import com.gy.util.StringUtil;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelUtil {
    public static HSSFWorkbook makeExcel(String[] titles, List<Map<String, String>> dataList, List<String> keyList, int defaultSize) throws Exception {
        int rowIndex;
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet s = wb.createSheet();
        s.setDefaultColumnWidth(defaultSize);
        int n = rowIndex = 0;
        rowIndex = (short)(n + 1);
        HSSFRow rowTitle = s.createRow(n);
        int i = 0;
        while (i < titles.length) {
            HSSFCell title = rowTitle.createCell(i);
            title.setCellType(1);
            title.setCellValue(titles[i]);
            ++i;
        }
        i = 0;
        while (i < dataList.size()) {
            Map<String, String> dataMap = dataList.get(i);
            int n2 = rowIndex;
            rowIndex = (short)(n2 + 1);
            HSSFRow row = s.createRow(n2);
            int j = 0;
            while (j < keyList.size()) {
                String key = keyList.get(j);
                String value = dataMap.get(key);
                HSSFCell cell = row.createCell(j);
                cell.setCellType(1);
                if (!StringUtil.isEmpty(value)) {
                    cell.setCellValue(dataMap.get(keyList.get(j)).trim());
                }
                ++j;
            }
            ++i;
        }
        return wb;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static List<Map<String, String>> getExcel(InputStream is, List<String> keyList) throws Exception {
//        dataList = new ArrayList<Map<String, String>>();
//        wb = new HSSFWorkbook(is);
//        sheet = wb.getSheetAt(0);
//        for (Row row : sheet) {
//            if (row.getRowNum() == 0) continue;
//            tmpMap = new HashMap<String, String>();
//            i = 0;
//            while (i < keyList.size()) {
//                block13 : {
//                    block16 : {
//                        block14 : {
//                            block15 : {
//                                block12 : {
//                                    cell = row.getCell(i);
//                                    data = "";
//                                    if (cell != null) break block12;
//                                    tmpMap.put(keyList.get(i), "");
//                                    break block13;
//                                }
//                                switch (cell.getCellType()) {
//                                    case 3: {
//                                        data = "";
//                                        break;
//                                    }
//                                    case 4: {
//                                        data = "ERROR";
//                                        break;
//                                    }
//                                    case 5: {
//                                        data = "ERROR";
//                                        break;
//                                    }
//                                    case 2: {
//                                        data = cell.getCellFormula();
//                                        break;
//                                    }
//                                    case 0: {
//                                        data = String.valueOf(cell.getNumericCellValue());
//                                        break;
//                                    }
//                                    case 1: {
//                                        data = cell.getStringCellValue();
//                                    }
//                                }
//                                if (StringUtil.isNull(data) || data.equals("ERROR")) break block14;
//                                if (data.indexOf("E") == -1) break block15;
//                                df = new DecimalFormat("0");
//                                data = df.format(Double.parseDouble(data));
//                                break block16;
//                            }
//                            if (data.indexOf(".") == -1) break block16;
//                            if (Double.valueOf(data) != 0.0) ** GOTO lbl47
//                            data = "0";
//                            break block16;
//                        }
//                        tmpMap.put(keyList.get(i), "");
//                        break block13;
//lbl-1000: // 1 sources:
//                        {
//                            data = data.substring(0, data.length() - 1);
//lbl47: // 2 sources:
//                            ** while (data.lastIndexOf((String)"0") == data.length() - 1)
//                        }
//lbl48: // 1 sources:
//                        if (data.lastIndexOf(".") == data.length() - 1) {
//                            data = data.substring(0, data.length() - 1);
//                        }
//                    }
//                    tmpMap.put(keyList.get(i), data.trim());
//                }
//                ++i;
//            }
//            dataList.add(tmpMap);
//        }
//        is.close();
//        return dataList;
        return null;
    }

    public static HSSFWorkbook makeSpecialExcel2(String title, String[] detail, List<String> keyList, List<Map<String, String>> dataList, int defaultWidth, int titleLength) {
        int i;
        int rowIndex;
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        HSSFRow row = null;
        HSSFCell cell = null;
        HSSFFont font1 = workbook.createFont();
        font1.setFontHeight((short)400);
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight((short)700);
        sheet.setDefaultColumnWidth(defaultWidth);
        HSSFCellStyle style1 = workbook.createCellStyle();
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, keyList.size() - 1));
        style1.setAlignment((short)2);
        style1.setFont(font1);
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setAlignment((short)2);
        HSSFCellStyle style3 = workbook.createCellStyle();
        style3.setAlignment((short)2);
        style3.setVerticalAlignment((short)1);
        style3.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
        style3.setBorderLeft((short)1);
        style3.setBorderRight((short)1);
        style3.setBorderTop((short)1);
        style3.setBorderBottom((short)1);
        style3.setFont(font2);
        HSSFCellStyle style4 = workbook.createCellStyle();
        style4.setAlignment((short)3);
        style4.setBorderLeft((short)1);
        style4.setBorderRight((short)1);
        style4.setBorderTop((short)1);
        style4.setBorderBottom((short)1);
        int n = rowIndex = 0;
        rowIndex = (short)(n + 1);
        row = sheet.createRow(n);
        row.setHeight((short)800);
        cell = row.createCell(0);
        cell.setCellStyle(style1);
        cell.setCellType(1);
        cell.setCellValue(title);
        rowIndex = (short)(rowIndex + 1);
        if (detail.length != 0) {
            int n2 = rowIndex;
            rowIndex = (short)(n2 + 1);
            row = sheet.createRow(n2);
            i = 0;
            while (i < detail.length) {
                cell = row.createCell(i);
                cell.setCellType(1);
                cell.setCellValue(detail[i]);
                cell.setCellStyle(style2);
                ++i;
            }
        }
        i = 0;
        while (i < dataList.size()) {
            Map<String, String> dataMap = dataList.get(i);
            int n3 = rowIndex;
            rowIndex = (short)(n3 + 1);
            row = sheet.createRow(n3);
            int j = 0;
            while (j < keyList.size()) {
                cell = row.createCell(j);
                cell.setCellType(1);
                if (i < titleLength) {
                    cell.setCellStyle(style3);
                } else {
                    cell.setCellStyle(style4);
                }
                if (StringUtil.isNull(dataMap.get(keyList.get(j)))) {
                    cell.setCellValue("");
                } else {
                    cell.setCellValue(dataMap.get(keyList.get(j)).trim());
                }
                ++j;
            }
            ++i;
        }
        return workbook;
    }

    public static HSSFWorkbook makeSpecialExcel(String title, String[] detail, String[] titles, List<String> keyList, List<Map<String, String>> dataList, String[] totalDetial, String[] subDetial, int defaultWidth) {
        int i;
        int rowIndex;
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        HSSFRow row = null;
        HSSFCell cell = null;
        HSSFFont font1 = workbook.createFont();
        font1.setFontHeight((short)600);
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight((short)700);
        sheet.setDefaultColumnWidth(defaultWidth);
        HSSFCellStyle style1 = workbook.createCellStyle();
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, keyList.size() - 1));
        style1.setAlignment((short)2);
        style1.setFont(font1);
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setAlignment((short)2);
        HSSFCellStyle style3 = workbook.createCellStyle();
        style3.setAlignment((short)2);
        style3.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
        style3.setBorderLeft((short)1);
        style3.setBorderRight((short)1);
        style3.setBorderTop((short)1);
        style3.setBorderBottom((short)1);
        style3.setFont(font2);
        HSSFCellStyle style4 = workbook.createCellStyle();
        style4.setAlignment((short)3);
        style4.setBorderLeft((short)1);
        style4.setBorderRight((short)1);
        style4.setBorderTop((short)1);
        style4.setBorderBottom((short)1);
        int n = rowIndex = 0;
        rowIndex = (short)(n + 1);
        row = sheet.createRow(n);
        row.setHeight((short)800);
        cell = row.createCell(0);
        cell.setCellStyle(style1);
        cell.setCellType(1);
        cell.setCellValue(title);
        rowIndex = (short)(rowIndex + 1);
        if (detail.length != 0) {
            int n2 = rowIndex;
            rowIndex = (short)(n2 + 1);
            row = sheet.createRow(n2);
            i = 0;
            while (i < detail.length) {
                cell = row.createCell(i);
                cell.setCellType(1);
                cell.setCellValue(detail[i]);
                cell.setCellStyle(style2);
                ++i;
            }
        }
        int n3 = rowIndex;
        rowIndex = (short)(n3 + 1);
        row = sheet.createRow(n3);
        i = 0;
        while (i < titles.length) {
            cell = row.createCell(i);
            cell.setCellStyle(style3);
            cell.setCellType(1);
            cell.setCellValue(titles[i]);
            ++i;
        }
        i = 0;
        while (i < dataList.size()) {
            Map<String, String> dataMap = dataList.get(i);
            int n4 = rowIndex;
            rowIndex = (short)(n4 + 1);
            row = sheet.createRow(n4);
            int j = 0;
            while (j < keyList.size()) {
                String key = keyList.get(j);
                String value = dataMap.get(key);
                cell = row.createCell(j);
                cell.setCellType(1);
                cell.setCellStyle(style4);
                if (StringUtil.isNull(dataMap.get(keyList.get(j)))) {
                    cell.setCellValue("");
                } else {
                    cell.setCellValue(dataMap.get(keyList.get(j)).trim());
                }
                ++j;
            }
            ++i;
        }
        rowIndex = (short)(rowIndex + 2);
        if (totalDetial.length != 0) {
            int n5 = rowIndex;
            rowIndex = (short)(n5 + 1);
            row = sheet.createRow(n5);
            i = 0;
            while (i < totalDetial.length) {
                cell = row.createCell(i);
                cell.setCellType(1);
                cell.setCellValue(totalDetial[i]);
                cell.setCellStyle(style2);
                ++i;
            }
        }
        int n6 = rowIndex;
        rowIndex = (short)(n6 + 1);
        row = sheet.createRow(n6);
        i = 0;
        while (i < subDetial.length) {
            cell = row.createCell(i);
            cell.setCellType(1);
            cell.setCellValue(subDetial[i]);
            cell.setCellStyle(style2);
            ++i;
        }
        return workbook;
    }

    public static String changeMoneyDivide(String[] Moneys, String key, String value) {
        try {
            String[] arrstring = Moneys;
            int n = arrstring.length;
            int n2 = 0;
            while (n2 < n) {
                String moneyKey = arrstring[n2];
                if (moneyKey.equals(key)) {
                    BigDecimal bd = new BigDecimal(value).divide(new BigDecimal(100)).setScale(2);
                    return String.valueOf(bd);
                }
                ++n2;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static boolean isMoney(String[] Moneys, String key) {
        if (Moneys.length == 0) {
            return false;
        }
        String[] arrstring = Moneys;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String moneyKey = arrstring[n2];
            if (moneyKey.equals(key)) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public static HSSFFont createFontBig(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setFontHeight((short)400);
        return font;
    }

    public static HSSFFont createFontBold(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setBoldweight((short)700);
        return font;
    }

    public static HSSFCellStyle createStyleTitle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment((short)1);
        style.setAlignment((short)2);
        style.setFont(ExcelUtil.createFontBig(workbook));
        return style;
    }

    public static HSSFCellStyle createStyleBold(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment((short)1);
        style.setAlignment((short)2);
        style.setBorderLeft((short)1);
        style.setBorderRight((short)1);
        style.setBorderTop((short)1);
        style.setBorderBottom((short)1);
        style.setFont(ExcelUtil.createFontBold(workbook));
        return style;
    }

    public static HSSFCellStyle createStyleLeft(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment((short)1);
        style.setAlignment((short)1);
        style.setBorderLeft((short)1);
        style.setBorderRight((short)1);
        style.setBorderTop((short)1);
        style.setBorderBottom((short)1);
        return style;
    }

    public static HSSFCellStyle createStyleCenter(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment((short)1);
        style.setAlignment((short)2);
        style.setBorderLeft((short)1);
        style.setBorderRight((short)1);
        style.setBorderTop((short)1);
        style.setBorderBottom((short)1);
        return style;
    }

    public static HSSFCellStyle createStyleRight(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment((short)1);
        style.setAlignment((short)3);
        style.setBorderLeft((short)1);
        style.setBorderRight((short)1);
        style.setBorderTop((short)1);
        style.setBorderBottom((short)1);
        return style;
    }

    public static HSSFCellStyle createStyleThinCenter(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment((short)1);
        style.setAlignment((short)2);
        return style;
    }

    public static String writeFiles(HSSFWorkbook workbook, String fileName) {
        FilterOutputStream out = null;
        try {
            String basePath = SysParamUtil.getParam("TRADE_EXPORT_PATH");
            basePath = basePath.replace("\\", "/");
            File writeFile = new File(String.valueOf(basePath) + fileName);
            if (!writeFile.getParentFile().exists()) {
                writeFile.getParentFile().mkdirs();
            }
            if (writeFile.exists()) {
                writeFile.delete();
            }
            out = new DataOutputStream(new FileOutputStream(writeFile));
            workbook.write((OutputStream)out);
            out.flush();
            String string = String.valueOf(basePath) + fileName;
            return string;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "F";
    }

    public static void main(String[] args) {
        ArrayList<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("aa", "aaValue");
        data.put("bb", "bbValue");
        ArrayList<String> keyList = new ArrayList<String>();
        keyList.add("aa");
        keyList.add("bb");
        String[] titles = new String[]{"\u6807\u98981", "\u6807\u98982"};
        try {
            HSSFWorkbook hssf = ExcelUtil.makeExcel(titles, dataList, keyList, keyList.size());
            ExcelUtil.writeFiles(hssf, "\u6d4b\u8bd5excel.xls");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
