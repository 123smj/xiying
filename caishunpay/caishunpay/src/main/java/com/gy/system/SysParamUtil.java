/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 */
package com.gy.system;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.apache.log4j.Logger;

public class SysParamUtil {
    private static Logger log = Logger.getLogger(SysParamUtil.class);
    private static String SYSPARAM_FILE = "SysParam";
    private static Properties props = null;

    static {
        SysParamUtil.loadProps();
    }

    private static void loadProps() {
        String classPath = SysParamUtil.class.getClassLoader().getResource("").getPath();
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(String.valueOf(classPath) + SYSPARAM_FILE + ".properties"));
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

    public static void reloadProps() {
        SysParamUtil.loadProps();
    }

    public static String getParam(String key) {
        return (String)props.get(key);
    }
}
