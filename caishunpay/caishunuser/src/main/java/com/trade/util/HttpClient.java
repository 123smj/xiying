/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpClient {
    public static String post(String content, String url) {
        StringBuffer data;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        data = new StringBuffer();
        try {
            try {
                String line;
                URL connect = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) connect.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                writer.write(content);
                writer.flush();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    if (writer != null) {
                        writer.close();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return data.toString();
    }
}
