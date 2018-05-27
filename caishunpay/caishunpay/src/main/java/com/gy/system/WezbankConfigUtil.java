/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 */
package com.gy.system;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import org.apache.log4j.Logger;

public class WezbankConfigUtil {
    private static Logger log = Logger.getLogger(WezbankConfigUtil.class);
    private static String SYSPARAM_FILE = "wezbank_config";
    private static Properties props = null;
    private static String classPath;

    static {
        WezbankConfigUtil.loadProps();
    }

    private static void loadProps() {
        classPath = String.valueOf(WezbankConfigUtil.class.getClassLoader().getResource("").getPath()) + SYSPARAM_FILE + ".properties";
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(classPath));
        }
        catch (FileNotFoundException e) {
            log.error((Object)("loadProps FileNotFoundException: " + e.getMessage()));
        }
        props = new Properties();
        try {
            props.load(bufferedInputStream);
        }
        catch (IOException e) {
            log.error((Object)("loadProps IOException: " + e.getMessage()));
        }
    }

    public static void saveProps() {
        try {
            FileOutputStream os = new FileOutputStream(new File(classPath));
            props.store(os, null);
            os.flush();
            os.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadProps() {
        WezbankConfigUtil.loadProps();
    }

    public static String getParam(String key) {
        return (String)props.get(key);
    }

    public static void setParam(String key, String value) {
        props.setProperty(key, value);
    }
}
