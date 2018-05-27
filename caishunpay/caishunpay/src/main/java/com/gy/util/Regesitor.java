/*
 * Decompiled with CFR 0_124.
 */
package com.gy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Regesitor {
    public static String sendGet(String url, String param) {
        String result;
        result = "";
        BufferedReader in = null;
        try {
            try {
                String line;
                String urlNameString = String.valueOf(url) + "?" + param;
                URL realUrl = new URL(urlNameString);
                URLConnection connection = realUrl.openConnection();
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                connection.connect();
                Map<String, List<String>> map = connection.getHeaderFields();
                for (String key : map.keySet()) {
                    System.out.println(String.valueOf(key) + "--->" + map.get(key));
                }
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = in.readLine()) != null) {
                    result = String.valueOf(result) + line;
                }
            }
            catch (Exception e) {
                System.out.println("\u53d1\u9001GET\u8bf7\u6c42\u51fa\u73b0\u5f02\u5e38\uff01" + e);
                e.printStackTrace();
                try {
                    if (in != null) {
                        in.close();
                    }
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static String sendPost(String url, String param) {
        String result;
        PrintWriter out = null;
        BufferedReader in = null;
        result = "";
        try {
            try {
                String line;
                URL realUrl = new URL(url);
                URLConnection conn = realUrl.openConnection();
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("connection", "Keep-Alive");
                conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                out = new PrintWriter(conn.getOutputStream());
                out.print(param);
                out.flush();
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = in.readLine()) != null) {
                    result = String.valueOf(result) + line;
                }
            }
            catch (Exception e) {
                System.out.println("\u53d1\u9001 POST \u8bf7\u6c42\u51fa\u73b0\u5f02\u5e38\uff01" + e);
                e.printStackTrace();
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
