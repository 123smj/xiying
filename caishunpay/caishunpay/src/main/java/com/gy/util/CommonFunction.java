/*
 * Decompiled with CFR 0_124.
 */
package com.gy.util;

import com.common.dao.ICommQueryDAO;
import com.manage.bean.OprInfo;
import com.trade.enums.*;
import com.trade.enums.TradeSource;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonFunction {
    private static SimpleDateFormat showDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sysDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static SimpleDateFormat sysTimeFormat = new SimpleDateFormat("HHmmss");
    private static SimpleDateFormat sysDateFormat8 = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat showDateFormatZHCN = new SimpleDateFormat("yyyy\u5e74MM\u6708dd\u65e5 HH\u65f6mm\u5206ss\u79d2");
    private static ICommQueryDAO commQueryDAO = (ICommQueryDAO)ContextUtil.getBean("commQueryDAO");
    public static String ADD_TO_CHECK = "0";
    public static String DELETE = "1";
    public static String NORMAL = "2";
    public static String MODIFY_TO_CHECK = "3";
    public static String DELETE_TO_CHECK = "4";
    public static String HANG_FLAG_CHECK = "0";
    public static String HANG_FLAG = "1";
    public static String HANG_RECEIVE = "2";
    public static String HANG_RECEIVE_CHECK = "3";
    public static String HANG_FLAG_REFUSE = "4";

    public static String getCurrentDateTime() {
        return sysDateFormat.format(new Date());
    }

    public static String getCurrentTime() {
        return sysTimeFormat.format(new Date());
    }

    public static String getCurrentDate() {
        return sysDateFormat8.format(new Date());
    }

    public static String getBeforeDate(String thisDate, int days) {
        return CommonFunction.getOffSizeDate(thisDate, String.valueOf(days));
    }

    public static String getAddress() {
        String address = " ";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            address = addr.getHostName();
        }
        catch (UnknownHostException addr) {
            // empty catch block
        }
        return address;
    }

    public static String getIp() {
        String ip = " ";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
        }
        catch (UnknownHostException addr) {
            // empty catch block
        }
        return ip;
    }

    public static String getCurrentDateTimeForShow() {
        return showDateFormat.format(new Date());
    }

    public static String getCurrentDateTimeZHCN() {
        return showDateFormatZHCN.format(new Date());
    }

    public static String Md5(String plainText) {
        StringBuffer buf = new StringBuffer("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte[] b = md.digest();
            int offset = 0;
            while (offset < b.length) {
                int i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
                ++offset;
            }
            System.out.println("result: " + buf.toString());
            System.out.println("result: " + buf.toString().substring(8, 24));
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    public static String fillString(String str, char fill, int len, boolean isEnd) {
        if (str == null) {
            str = "";
        }
        int fillLen = len - str.getBytes().length;
        if (len <= 0) {
            return str;
        }
        int i = 0;
        while (i < fillLen) {
            str = isEnd ? String.valueOf(str) + fill : String.valueOf(fill) + str;
            ++i;
        }
        return str;
    }

    public static String changeCharset(String str, String newCharset) throws UnsupportedEncodingException {
        if (str != null) {
            byte[] bs = str.getBytes();
            return new String(bs, newCharset);
        }
        return null;
    }

    public static String changeCharsetOld(String str, String oldCharset, String newCharset) throws UnsupportedEncodingException {
        if (str != null) {
            byte[] bs = str.getBytes(oldCharset);
            return new String(bs, newCharset);
        }
        return null;
    }

    public static String fillStringForChinese(String str, char fill, int len, boolean isEnd) {
        int num = 0;
        Pattern p = Pattern.compile("^[\u4e00-\u9fa5]");
        int i = 0;
        while (i < str.length()) {
            Matcher m = p.matcher(str.substring(i, i + 1));
            if (m.find()) {
                ++num;
            }
            ++i;
        }
        int fillLen = len - (str.length() + num);
        if (len <= 0) {
            return str;
        }
        int i2 = 0;
        while (i2 < fillLen) {
            str = isEnd ? String.valueOf(str) + fill : String.valueOf(fill) + str;
            ++i2;
        }
        return str;
    }

    public static String getBelowBrhInfo(Map<String, String> brhMap) {
        String belowBrhInfo = "(";
        Iterator<String> iter = brhMap.keySet().iterator();
        while (iter.hasNext()) {
            String brhId = iter.next();
            belowBrhInfo = String.valueOf(belowBrhInfo) + "'" + brhId + "'";
            if (!iter.hasNext()) continue;
            belowBrhInfo = String.valueOf(belowBrhInfo) + ",";
        }
        belowBrhInfo = String.valueOf(belowBrhInfo) + ")";
        return belowBrhInfo;
    }

    public static String getBelowBrhSql(String branchNo) {
        String branchSql = " (select branch_no from tbl_branch_manager_true start with branch_no ='" + branchNo + "' connect by prior branch_no=parent_branch_id ) ";
        return branchSql;
    }

    public static String getBelowBrhSqlForExists(String branchNo, String connectField) {
        return " (select branch_no from tbl_branch_manager_true where " + connectField + "=branch_no start with branch_no = '" + branchNo + "' connect by prior branch_no = parent_branch_id) ";
    }

    public static String getOffSizeDate(String refDate, String offSize) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Integer.parseInt(refDate.substring(0, 4)), Integer.parseInt(refDate.substring(4, 6)) - 1, Integer.parseInt(refDate.substring(6, 8)));
        calendar.add(5, Integer.parseInt(offSize));
        String year = String.valueOf(calendar.get(1));
        String month = String.valueOf(calendar.get(2) + 1);
        String retDate = String.valueOf(calendar.get(5));
        if (Integer.parseInt(month) < 10) {
            month = "0" + month;
        }
        if (Integer.parseInt(retDate) < 10) {
            retDate = "0" + retDate;
        }
        return String.valueOf(year) + month + retDate;
    }

    public static String transYuanToFen(String str) {
        if (str == null || "".equals(str.trim())) {
            return "";
        }
        BigDecimal bigDecimal = new BigDecimal(str.trim());
        return bigDecimal.movePointRight(2).toString();
    }

    public static String transFenToYuan(String str) {
        if (str == null || "".equals(str.trim())) {
            return "";
        }
        String result = str.trim();
        try {
            BigDecimal bigDecimal = new BigDecimal(str.trim());
            result = bigDecimal.movePointLeft(2).toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getRandomNum(int len) {
        String ran = "";
        Random random = new Random();
        int i = 0;
        while (i < len) {
            ran = String.valueOf(ran) + String.valueOf(random.nextInt(10));
            ++i;
        }
        return ran;
    }

    public static boolean isMoney(String str) {
        int i = 0;
        while (i < str.length()) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
            ++i;
        }
        return true;
    }

    public static boolean isAllDigit(String str) {
        int i = 0;
        while (i < str.length()) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
            ++i;
        }
        return true;
    }

    public static Date getCurrentTs() {
        Date now = new Date();
        return new Timestamp(now.getTime());
    }

    public static Double getDValue(String value, Double _default) {
        if (StringUtil.isNotEmpty(value)) {
            return Double.valueOf(value);
        }
        return _default;
    }

    public static BigDecimal getBValue(String value, BigDecimal _default) {
        if (StringUtil.isNotEmpty(value)) {
            try {
                return new BigDecimal(value.trim());
            }
            catch (Exception ex) {
                return _default;
            }
        }
        return _default;
    }

    public static Integer getInt(String value, int _default) {
        if (StringUtil.isNotEmpty(value)) {
            try {
                return Integer.parseInt(value.trim());
            }
            catch (Exception ex) {
                return _default;
            }
        }
        return _default;
    }

    public static String formate8Date(String str) {
        if (str.length() == 8) {
            return String.valueOf(str.substring(0, 4)) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8);
        }
        return str;
    }

    public static String getCurrDate(String format) {
        SimpleDateFormat formater = new SimpleDateFormat(format);
        return formater.format(new Date());
    }

    private static byte toByte(char c) {
        byte b = (byte)"0123456789abcdef".indexOf(c);
        return b;
    }

    public static byte[] hexStringToByte(String hex) {
        int len = hex.length() / 2;
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        int i = 0;
        while (i < len) {
            int pos = i * 2;
            result[i] = (byte)(CommonFunction.toByte(achar[pos]) << 4 | CommonFunction.toByte(achar[pos + 1]));
            ++i;
        }
        return result;
    }

    public static String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        int i = 0;
        while (i < bArray.length) {
            String sTemp = Integer.toHexString(255 & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
            ++i;
        }
        return sb.toString();
    }

    public static String urlToRoleId(String url) {
        try {
            String[] array = url.split("/");
            String[] result = array[array.length - 1].split("\\.");
            String res = result[0];
            return res;
        }
        catch (Exception e) {
            return url;
        }
    }

    public static String urlToPage(String url) {
        try {
            String[] array = url.split("/");
            String res = array[array.length - 1];
            return res;
        }
        catch (Exception e) {
            return url;
        }
    }

    public static String transMoney(double n) {
        try {
            String[] fraction = new String[]{"\u89d2", "\u5206"};
            String[] digit = new String[]{"\u96f6", "\u58f9", "\u8d30", "\u53c1", "\u8086", "\u4f0d", "\u9646", "\u67d2", "\u634c", "\u7396"};
            String[][] unit = new String[][]{{"\u5143", "\u4e07", "\u4ebf"}, {"", "\u62fe", "\u4f70", "\u4edf"}};
            String head = n < 0.0 ? "\u8d1f" : "";
            n = Math.abs(n);
            String s = "";
            int i = 0;
            while (i < fraction.length) {
                s = String.valueOf(s) + new StringBuilder(String.valueOf(digit[(int)(Math.floor(n * 10.0 * Math.pow(10.0, i)) % 10.0)])).append(fraction[i]).toString().replaceAll("(\u96f6.)+", "");
                ++i;
            }
            if (s.length() < 1) {
                s = "\u6574";
            }
            int integerPart = (int)Math.floor(n);
            int i2 = 0;
            while (i2 < unit[0].length && integerPart > 0) {
                String p = "";
                int j = 0;
                while (j < unit[1].length && n > 0.0) {
                    p = String.valueOf(digit[integerPart % 10]) + unit[1][j] + p;
                    integerPart /= 10;
                    ++j;
                }
                s = String.valueOf(p.replaceAll("(\u96f6.)*\u96f6$", "").replaceAll("^$", "\u96f6")) + unit[0][i2] + s;
                ++i2;
            }
            return String.valueOf(head) + s.replaceAll("(\u96f6.)*\u96f6\u5143", "\u5143").replaceAll("(\u96f6.)+", "").replaceAll("(\u96f6.)+", "\u96f6").replaceAll("^\u6574$", "\u96f6\u5143\u6574");
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String insertString(String src, String fill) {
        String tmp = "";
        int i = 0;
        while (i < src.length()) {
            tmp = String.valueOf(tmp) + fill;
            tmp = String.valueOf(tmp) + src.substring(i, i + 1);
            ++i;
        }
        return tmp;
    }

    public static String cardReplace(Object card) {
        if (card == null) {
            return "";
        }
        String result = String.valueOf(card).trim();
        if (result.length() > 10) {
            String start = result.substring(0, 6);
            String end = result.substring(result.length() - 4);
            result = String.valueOf(start) + CommonFunction.fillString("", '*', result.length() - 10, true) + end;
        }
        return result;
    }

    public static String charFormatForSql(Object src, boolean needPreSeperator, boolean needPostFixSeperator) {
        String srcStr = "'" + src.toString() + "'";
        if (needPreSeperator) {
            srcStr = "," + srcStr;
        }
        if (needPostFixSeperator) {
            srcStr = String.valueOf(srcStr) + ",";
        }
        return srcStr;
    }

    public static String transDateFormate(String dateStr, String srcFormate, String desFormate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(srcFormate);
        SimpleDateFormat ddf = new SimpleDateFormat(desFormate);
        Date srcDate = sdf.parse(dateStr);
        return ddf.format(srcDate);
    }

    public static String translateTxnNum(String srcStr) {
        HashMap<String, String> values = new HashMap<String, String>();
        values.put("\u6d88\u8d39", "1101");
        values.put("\u9000\u8d27", "5151");
        values.put("\u9884\u6388\u6743\u5b8c\u6210", "1091");
        return (String)values.get(srcStr);
    }

    public static String listToInSql(List<String> list) {
        StringBuffer inSql = new StringBuffer("('##')");
        for (String obj : list) {
            inSql.insert(1, "'" + obj + "', ");
        }
        return inSql.toString();
    }

    public static Map<String, String> parseList2Map(String data, String regex) {
        String[] arrays = data.split("\\" + regex);
        HashMap<String, String> map = new HashMap<String, String>();
        int i = 0;
        while (i < arrays.length) {
            if (arrays[i] != null && !"".equals(arrays[i]) && arrays[i].contains("=")) {
                String[] keyValues = arrays[i].split("=");
                map.put(keyValues[0], keyValues[1]);
            }
            ++i;
        }
        return map;
    }

    public static String transTradeState(String status) {
        TradeStateEnum[] arrtradeStateEnum = TradeStateEnum.values();
        int n = arrtradeStateEnum.length;
        int n2 = 0;
        while (n2 < n) {
            TradeStateEnum em = arrtradeStateEnum[n2];
            if (em.getCode().equals(status)) {
                return em.getMemo();
            }
            ++n2;
        }
        return status == null ? "" : status;
    }

    public static String transDfState(String status) {
        DfStateEnum[] arrdfStateEnum = DfStateEnum.values();
        int n = arrdfStateEnum.length;
        int n2 = 0;
        while (n2 < n) {
            DfStateEnum em = arrdfStateEnum[n2];
            if (em.getCode().equals(status)) {
                return em.getMemo();
            }
            ++n2;
        }
        return status == null ? "" : status;
    }

    public static String transTradeSource(String tradeSource) {
        TradeSource[] arrtradeSource = TradeSource.values();
        int n = arrtradeSource.length;
        int n2 = 0;
        while (n2 < n) {
            TradeSource em = arrtradeSource[n2];
            if (em.getCode().equals(tradeSource)) {
                return em.getMemo();
            }
            ++n2;
        }
        return tradeSource == null ? "" : tradeSource;
    }

    public static String getTradeSourceList() {
        StringBuffer sb = new StringBuffer();
        TradeSource[] arrtradeSource = TradeSource.values();
        int n = arrtradeSource.length;
        int n2 = 0;
        while (n2 < n) {
            TradeSource em = arrtradeSource[n2];
            sb.append(em.getCode()).append(':');
            ++n2;
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static boolean isTradeSourceOpen(String tradeSourceList, TradeSource em) {
        String[] tradeSources = tradeSourceList.split(":");
        boolean result = false;
        String[] arrstring = tradeSources;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String tradeSource = arrstring[n2];
            if (em.getCode().equals(tradeSource)) {
                result = true;
                break;
            }
            ++n2;
        }
        return result;
    }

    public static String transferTradeSourceList(String tradeSourceList, String seg) {
        if (StringUtil.isEmpty(tradeSourceList)) {
            return tradeSourceList;
        }
        String[] tradeSources = tradeSourceList.split(":");
        StringBuilder sb = new StringBuilder();
        String[] arrstring = tradeSources;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String tradeSource = arrstring[n2];
            sb.append(TradeSource.get(tradeSource).getMemo()).append(seg);
            ++n2;
        }
        return sb.toString();
    }

    public static String transChannel(String channelId) {
        ChannelInfoEnum[] arrchannelInfoEnum = ChannelInfoEnum.values();
        int n = arrchannelInfoEnum.length;
        int n2 = 0;
        while (n2 < n) {
            ChannelInfoEnum em = arrchannelInfoEnum[n2];
            if (em.getCode().equals(channelId)) {
                return em.getMemo();
            }
            ++n2;
        }
        return channelId == null ? "" : channelId;
    }

    public static boolean isTradeSourceOpen(String tradeSourceList, String tSource) {
        if (StringUtil.isEmpty(tradeSourceList) || StringUtil.isEmpty(tSource)) {
            return false;
        }
        String[] tradeSources = tradeSourceList.split(":");
        boolean result = false;
        String[] arrstring = tradeSources;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String tradeSource = arrstring[n2];
            if (tSource.equals(tradeSource)) {
                result = true;
                break;
            }
            ++n2;
        }
        return result;
    }

    public static String transT0Flag(String t0Flag) {
        if (t0Flag == null) {
            return "";
        }
        if ("1".equals(t0Flag)) {
            return "\u662f";
        }
        return "\u5426";
    }

    public static String getBelowMcht(String companyId) {
        String sql = "select mcht_no from trade_qrcode_mcht_info where company_id='" + companyId + "'";
        StringBuffer sb = new StringBuffer();
        List<String> dataList = commQueryDAO.findBySQLQuery(sql);
        if (dataList != null && dataList.size() != 0) {
            sb.append("(");
            for (String mchtNo : dataList) {
                sb.append("'").append(mchtNo).append("',");
            }
            sb.setCharAt(sb.length() - 1, ')');
        }
        return sb.toString();
    }

    public static String buildCommanySql(String collomn, OprInfo oprInfo) {
        String company_id = oprInfo.getCompany_id();
        String sql = "";
        if (!OprTypeEnum.PARENT.getCode().equals(oprInfo.getOpr_type())) {
            sql = String.valueOf(sql) + " and " + collomn + "='" + company_id + "'";
        }
        return sql;
    }

    public static void main(String[] args) {
        System.out.println(CommonFunction.getTradeSourceList());
    }
}
