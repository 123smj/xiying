/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 */
package com.gy2cupe.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

public class CupeHttpUtil {
    private static Logger log = Logger.getLogger(CupeHttpUtil.class);

    public static String postHttpRequest(String url, String data) throws Exception {
        log.info((Object) ("-\u8bf7\u6c42:" + data));
        HttpURLConnection hconn = null;
        OutputStream os = null;
        InputStream is = null;
        BufferedReader reader = null;
        try {
            hconn = (HttpURLConnection) new URL(url).openConnection();
            hconn.setRequestMethod("POST");
            hconn.setDoInput(true);
            hconn.setDoOutput(true);
            hconn.setUseCaches(false);
            hconn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            hconn.setConnectTimeout(30000);
            hconn.setReadTimeout(30000);
            log.info((Object) ("\u8bf7\u6c42\u5730\u5740:" + url));
            os = hconn.getOutputStream();
            byte[] f = data.getBytes("utf-8");
            os.write(f, 0, f.length);
            os.flush();
            int code = hconn.getResponseCode();
            System.out.println("url_code" + code);
            String sCurrentLine = "";
            String returneddata = "";
            if (code == 200) {
                is = hconn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                while ((sCurrentLine = reader.readLine()) != null) {
                    if (sCurrentLine.length() <= 0) continue;
                    returneddata = String.valueOf(returneddata) + sCurrentLine.trim();
                }
            }
            log.info((Object) ("-\u54cd\u5e94:" + returneddata));
            String string = returneddata;
            return string;
        } catch (Exception e) {
            log.error((Object) "-\u8bf7\u6c42\u5931\u8d25", (Throwable) e);
            throw e;
        } finally {
            if (hconn != null) {
                hconn.disconnect();
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
