/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package com.trade.util.ninepi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

public class MerchantConfig {
    private String requestUrl;
    private String offlineNotifyUrl;
    private String pageReturnUrl;
    private String merchantId;
    private String merchantName;
    private String signType;
    private String version;
    private String merchantCertPath;
    private String merchantCertPass;
    private String merchantCertPass2;
    private String rootCertPath;
    private String checkFileDir;
    private String charset;
    private String payRequestUrl;
    private String b2bRequestUrl;
    private String notifyUrl;
    private static MerchantConfig config;
    private Properties properties;

    public static MerchantConfig getConfig() {
        if (config == null) {
            config = new MerchantConfig();
        }
        return config;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void loadPropertiesFromPath(String rootPath) {
        File file = new File(String.valueOf(rootPath) + File.separator + "p2pmerchant.properties");
        FileInputStream in = null;
        if (file.exists()) {
            try {
                try {
                    in = new FileInputStream(file);
                    this.properties = new Properties();
                    this.properties.load(in);
                    this.loadProperties(this.properties);
                    if (in == null) return;
                    try {
                        in.close();
                        return;
                    }
                    catch (IOException var21) {
                        var21.printStackTrace();
                    }
                    return;
                }
                catch (FileNotFoundException var22) {
                    var22.printStackTrace();
                    if (in != null) {
                        try {
                            in.close();
                        }
                        catch (IOException var20) {
                            var20.printStackTrace();
                        }
                    }
                    if (in == null) return;
                    try {
                        in.close();
                        return;
                    }
                    catch (IOException var18) {
                        var18.printStackTrace();
                    }
                    return;
                }
                catch (IOException var23) {
                    var23.printStackTrace();
                    if (in != null) {
                        try {
                            in.close();
                        }
                        catch (IOException var19) {
                            var19.printStackTrace();
                        }
                    }
                    if (in == null) return;
                    try {
                        in.close();
                        return;
                    }
                    catch (IOException var18) {
                        var18.printStackTrace();
                    }
                    return;
                }
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    }
                    catch (IOException var18) {
                        var18.printStackTrace();
                    }
                }
            }
        }
        System.out.println(String.valueOf(rootPath) + "p2pmerchant.properties" + "File not found");
    }

    public void loadPropertiesFromSrc() {
        block20 : {
            InputStream in = null;
            try {
                try {
                    in = MerchantConfig.class.getClassLoader().getResourceAsStream("p2pmerchant.properties");
                    if (in != null) {
                        this.properties = new Properties();
                        this.properties.load(in);
                    }
                    this.loadProperties(this.properties);
                    if (in == null) break block20;
                    try {
                        in.close();
                    }
                    catch (IOException var16) {
                        var16.printStackTrace();
                    }
                }
                catch (IOException var18) {
                    var18.printStackTrace();
                    if (in != null) {
                        try {
                            in.close();
                        }
                        catch (IOException var15) {
                            var15.printStackTrace();
                        }
                    }
                    if (in == null) break block20;
                    try {
                        in.close();
                    }
                    catch (IOException var14) {
                        var14.printStackTrace();
                    }
                }
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    }
                    catch (IOException var14) {
                        var14.printStackTrace();
                    }
                }
            }
        }
    }

    public void loadProperties(Properties pro) {
        String value = pro.getProperty("pay.requestUrl");
        if (!StringUtils.isBlank((CharSequence)value)) {
            this.requestUrl = value.trim();
        }
        if (!StringUtils.isBlank((CharSequence)(value = pro.getProperty("sdk.offlineNotifyUrl")))) {
            this.offlineNotifyUrl = value.trim();
        }
        if (!StringUtils.isBlank((CharSequence)(value = pro.getProperty("sdk.pageReturnUrl")))) {
            this.pageReturnUrl = value.trim();
        }
        if (!StringUtils.isBlank((CharSequence)(value = pro.getProperty("sdk.merchantId")))) {
            this.merchantId = value.trim();
        }
        if (!StringUtils.isBlank((CharSequence)(value = pro.getProperty("sdk.merchantName")))) {
            this.merchantName = value.trim();
        }
        if (!StringUtils.isBlank((CharSequence)(value = pro.getProperty("sdk.signType")))) {
            this.signType = value.trim();
        }
        if (!StringUtils.isBlank((CharSequence)(value = pro.getProperty("sdk.version")))) {
            this.version = value.trim();
        }
        if (!StringUtils.isBlank((CharSequence)(value = pro.getProperty("sdk.merchantCertPath")))) {
            this.merchantCertPath = value.trim();
        }
        if (!StringUtils.isBlank((CharSequence)(value = pro.getProperty("sdk.merchantCertPass")))) {
            this.merchantCertPass = value.trim();
        }
        if (!StringUtils.isBlank((CharSequence)(value = pro.getProperty("sdk.charset")))) {
            this.charset = value.trim();
        }
        if (!StringUtils.isBlank((CharSequence)(value = pro.getProperty("sdk.rootCertPath")))) {
            this.rootCertPath = value.trim();
        }
        if (!StringUtils.isBlank((CharSequence)(value = pro.getProperty("sdk.checkFileDir")))) {
            this.checkFileDir = value.trim();
        }
        if (!StringUtils.isBlank((CharSequence)(value = pro.getProperty("pay.requestUrl")))) {
            this.payRequestUrl = value.trim();
        }
        if (!StringUtils.isBlank((CharSequence)(value = pro.getProperty("b2b.requestUrl")))) {
            this.b2bRequestUrl = value.trim();
        }
        if (!StringUtils.isBlank((CharSequence)(value = pro.getProperty("sdk.notifyUrl")))) {
            this.notifyUrl = value.trim();
        }
    }

    public String getB2bRequestUrl() {
        return this.b2bRequestUrl;
    }

    public String getRequestUrl() {
        return this.requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getOfflineNotifyUrl() {
        return this.offlineNotifyUrl;
    }

    public void setOfflineNotifyUrl(String offlineNotifyUrl) {
        this.offlineNotifyUrl = offlineNotifyUrl;
    }

    public String getPageReturnUrl() {
        return this.pageReturnUrl;
    }

    public void setPageReturnUrl(String pageReturnUrl) {
        this.pageReturnUrl = pageReturnUrl;
    }

    public String getMerchantId() {
        return this.merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return this.merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getSignType() {
        return this.signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMerchantCertPath() {
        return this.merchantCertPath;
    }

    public void setMerchantCertPath(String merchantCertPath) {
        this.merchantCertPath = merchantCertPath;
    }

    public String getMerchantCertPass() {
        return this.merchantCertPass;
    }

    public void setMerchantCertPass(String merchantCertPass) {
        this.merchantCertPass = merchantCertPass;
    }

    public String getRootCertPath() {
        return this.rootCertPath;
    }

    public void setRootCertPath(String rootCertPath) {
        this.rootCertPath = rootCertPath;
    }

    public String getCharset() {
        return this.charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getCheckFileDir() {
        return this.checkFileDir;
    }

    public void setCheckFileDir(String checkFileDir) {
        this.checkFileDir = checkFileDir;
    }

    public Properties getProperties() {
        return this.properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getPayRequestUrl() {
        return this.payRequestUrl;
    }

    public void setPayRequestUrl(String payRequestUrl) {
        this.payRequestUrl = payRequestUrl;
    }

    public String getNotifyUrl() {
        return this.notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public static void main(String[] args) {
    }
}
