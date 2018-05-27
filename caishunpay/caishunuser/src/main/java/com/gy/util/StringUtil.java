/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.RandomStringUtils
 */
package com.gy.util;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;

public final class StringUtil {
    public static Map<String, String> bankSegmentMap = new HashMap<String, String>() {
        {
            this.put("1004", "\u5efa\u8bbe\u94f6\u884c");
            this.put("1002", "\u519c\u4e1a\u94f6\u884c");
            this.put("1001", "\u5de5\u5546\u94f6\u884c");
            this.put("1003", "\u4e2d\u56fd\u94f6\u884c");
            this.put("1014", "\u6d66\u53d1\u94f6\u884c");
            this.put("1008", "\u5149\u5927\u94f6\u884c");
            this.put("1011", "\u5e73\u5b89\u94f6\u884c");
            this.put("1013", "\u5174\u4e1a\u94f6\u884c");
            this.put("1006", "\u90ae\u653f\u50a8\u84c4\u94f6\u884c");
            this.put("1007", "\u4e2d\u4fe1\u94f6\u884c");
            this.put("1009", "\u534e\u590f\u94f6\u884c");
            this.put("1012", "\u62db\u5546\u94f6\u884c");
            this.put("1017", "\u5e7f\u53d1\u94f6\u884c");
            this.put("1016", "\u5317\u4eac\u94f6\u884c");
            this.put("1025", "\u4e0a\u6d77\u94f6\u884c");
            this.put("1010", "\u6c11\u751f\u94f6\u884c");
            this.put("1005", "\u4ea4\u901a\u94f6\u884c");
            this.put("1103", "\u5317\u4eac\u519c\u6751\u5546\u4e1a\u94f6\u884c");
        }
    };
    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";
    public static final String[] LETTERS = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    public static final String[] NUMS = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    public static final String[] LETTERNUMS = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    public static final String[] NUMSLETTER_A_F = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
    public static final byte[] b8 = new byte[8];

    private StringUtil() {
    }

    public static String byte2hex(byte[] b) {
        char[] digit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] out = new char[b.length * 2];
        int i = 0;
        while (i < b.length) {
            byte c = b[i];
            out[i * 2] = digit[c >>> 4 & 15];
            out[i * 2 + 1] = digit[c & 15];
            ++i;
        }
        return new String(out);
    }

    public static byte[] hex2byte(byte[] b) {
        if (b.length % 2 != 0) {
            throw new IllegalArgumentException("\u957f\u5ea6\u4e0d\u662f\u5076\u6570");
        }
        byte[] b2 = new byte[b.length / 2];
        int n = 0;
        while (n < b.length) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
            n += 2;
        }
        b = null;
        return b2;
    }

    public static String getRandomNumAndLetterAF(int len) {
        String s = "";
        s.toCharArray();
        return StringUtil.getRandom(len, NUMSLETTER_A_F);
    }

    public static String getRandomLetter(int len) {
        return StringUtil.getRandom(len, LETTERS);
    }

    public static String getRandomNum(int len) {
        return StringUtil.getRandom(len, NUMS);
    }

    public static String getRandomLetterAndNum(int len) {
        return StringUtil.getRandom(len, LETTERNUMS);
    }

    public static String getRandom(int len, String[] arr) {
        String s = "";
        if (len <= 0 || arr == null || arr.length < 0) {
            return s;
        }
        Random ra = new Random();
        int arrLen = arr.length;
        int i = 0;
        while (i < len) {
            s = String.valueOf(s) + arr[ra.nextInt(arrLen)];
            ++i;
        }
        return s;
    }

    public static boolean isEmpty(String str) {
        if (str != null && !str.isEmpty()) {
            return false;
        }
        return true;
    }

    public static String null2String(String str) {
        return str == null ? "" : str.trim();
    }

    public static /* varargs */ boolean isNotEmpty(String... field) {
        if (field == null || field.length < 1) {
            return false;
        }
        String[] arrstring = field;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String f = arrstring[n2];
            if (StringUtil.isEmpty(f)) {
                return false;
            }
            ++n2;
        }
        return true;
    }

    public static boolean isEquals(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 != null && str2 != null && str1.compareTo(str2) == 0) {
            return true;
        }
        return false;
    }

    public static String getRandom(int length) {
        return RandomStringUtils.randomAlphanumeric((int) length);
    }

    public static int parseInt(String intStringValue) {
        int amount = 0;
        if (intStringValue != null && intStringValue.trim().length() > 0) {
            try {
                amount = Integer.parseInt(intStringValue.trim());
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        return amount;
    }

    public static String fillValue(String value, int len, char fillValue) {
        String str = value == null ? "" : value.trim();
        StringBuffer result = new StringBuffer();
        result.append(str);
        int paramLen = str.length();
        if (paramLen < len) {
            int i = 0;
            while (i < len - paramLen) {
                result.append(fillValue);
                ++i;
            }
        }
        return result.toString();
    }

    public static String fillLeftValue(String value, int len, char fillValue) {
        String str = value == null ? "" : value.trim();
        String fills = "";
        int paramLen = str.length();
        if (paramLen < len) {
            int i = 0;
            while (i < len - paramLen) {
                fills = String.valueOf(fills) + fillValue;
                ++i;
            }
        }
        return String.valueOf(fills) + str;
    }

    public static String mosaic(String target, char replaceWord) {
        String result = "";
        if (StringUtil.isEmpty(target) && target.length() <= 8) {
            return target;
        }
        result = String.valueOf(result) + target.substring(0, 4);
        result = StringUtil.fillValue(result, target.length() - 4, replaceWord);
        result = String.valueOf(result) + target.substring(target.length() - 4);
        return result;
    }

    public static String changeF2Y(int amount) {
        return BigDecimal.valueOf(amount).divide(new BigDecimal(100)).toString();
    }

    public static String map2HttpParam(Map<String, String> param) {
        StringBuffer sb = new StringBuffer();
        boolean isFirst = true;
        for (Map.Entry<String, String> entry : param.entrySet()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append("&");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        long t1 = System.currentTimeMillis();
        long t2 = System.currentTimeMillis();
        System.out.println(StringUtil.encode(null, "utf-8"));
        System.out.println(StringUtil.changeF2Y(2));
        System.out.println(StringUtil.getBankNameBySeq(null));
    }

    public static String changeY2F(Long amount) {
        return BigDecimal.valueOf(amount).multiply(new BigDecimal(100)).toString();
    }

    public static int changeY2F(Double amount) {
        return BigDecimal.valueOf(amount).multiply(new BigDecimal(100)).intValue();
    }

    public static String trans2Str(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public static String trim(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString().trim();
    }

    public static String decode(String destStr, String charset) {
        String result = "";
        try {
            result = URLDecoder.decode(destStr, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String encode(String destStr, String charset) {
        if (destStr == null) {
            return "";
        }
        String result = "";
        try {
            result = URLEncoder.encode(destStr, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getBankNameBySeq(String bankSeq) {
        String bankName = bankSegmentMap.get(bankSeq);
        return bankName == null ? bankSeq : bankName;
    }

    public static String getRandNum(int length) {
        int max = (int) Math.pow(10.0, length);
        int randNum = (int) (Math.random() * (double) max);
        return String.valueOf(randNum);
    }
}
