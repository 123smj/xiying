/*
 * Decompiled with CFR 0_124.
 */
package com.gy.util;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }

    public static Long getCurrentTimeStamp() {
        return new Date().getTime();
    }

    public static String getCurrentDay() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMdd");
        String s = outFormat.format(now);
        return s;
    }

    public static String getCurrentFormat(String format) {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat(format);
        String s = outFormat.format(now);
        return s;
    }

    public static String transDateFormat(String date, String fromFormat, String toFormat) {
        SimpleDateFormat from = new SimpleDateFormat(fromFormat);
        SimpleDateFormat to = new SimpleDateFormat(toFormat);
        Date fromDate = null;
        String s = null;
        try {
            fromDate = from.parse(date);
            s = to.format(fromDate);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String getCurrentTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("HHmmss");
        String s = outFormat.format(now);
        return s;
    }

    public static String getDateBeforeDays(int day, String format) {
        Date dat = null;
        Calendar cd = Calendar.getInstance();
        cd.add(5, - day);
        dat = cd.getTime();
        SimpleDateFormat dformat = new SimpleDateFormat(format);
        String date = dformat.format(dat);
        return date;
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.getCurrentFormat("yy"));
        System.out.println("20170302163600".compareTo("20170302163640"));
    }

    public static String getDateAfterSeconds(int seconds, String format) {
        Date dat = null;
        Calendar cd = Calendar.getInstance();
        cd.add(13, seconds);
        dat = cd.getTime();
        SimpleDateFormat dformat = new SimpleDateFormat(format);
        String date = dformat.format(dat);
        return date;
    }

    public static String getDateAfterSeconds(Date beginDate, int seconds, String format) {
        Date dat = null;
        Calendar cd = Calendar.getInstance();
        cd.setTime(beginDate);
        cd.add(13, seconds);
        dat = cd.getTime();
        SimpleDateFormat dformat = new SimpleDateFormat(format);
        String date = dformat.format(dat);
        return date;
    }

    public static String getFormatDate(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    public static Date getDate(String dateStr, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
