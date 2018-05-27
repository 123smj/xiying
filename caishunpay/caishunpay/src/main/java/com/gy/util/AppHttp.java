/*
 * Decompiled with CFR 0_124.
 */
package com.gy.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class AppHttp {
    public static String appadd(String ADD_URL, String param) {
        StringBuffer sb = new StringBuffer("");
        try {
            String lines;
            URL url = new URL(ADD_URL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(param.getBytes("UTF-8"));
            out.flush();
            out.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            reader.close();
            connection.disconnect();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
