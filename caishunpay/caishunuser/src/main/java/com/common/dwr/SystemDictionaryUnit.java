/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  javax.servlet.ServletContext
 *  org.dom4j.DocumentException
 */
package com.common.dwr;

import com.common.dwr.XmlDBParser;

import java.io.InputStream;
import java.util.LinkedHashMap;
import javax.servlet.ServletContext;

import org.dom4j.DocumentException;

public class SystemDictionaryUnit {
    private static LinkedHashMap<String, Object> sysDicMap = new LinkedHashMap();

    public static void initSysDic(ServletContext context) throws DocumentException {
        InputStream inputStream = context.getResourceAsStream("/WEB-INF/xml/SysDic.xml");
        XmlDBParser.parseSysDic(inputStream);
    }

    public static void addRecord(String tbl_nm, String fld_nm, String fld_val, String fld_desc) {
        if (sysDicMap.containsKey(tbl_nm)) {
            LinkedHashMap subMap = (LinkedHashMap) sysDicMap.get(tbl_nm);
            if (subMap.containsKey(fld_nm)) {
                LinkedHashMap subSubMap = (LinkedHashMap) subMap.get(fld_nm);
                subSubMap.put(fld_val, fld_desc);
            } else {
                LinkedHashMap<String, String> subSubMap = new LinkedHashMap<String, String>();
                subSubMap.put(fld_val, fld_desc);
                subMap.put(fld_nm, subSubMap);
            }
        } else {
            LinkedHashMap<String, String> subSubMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, LinkedHashMap<String, String>> subMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
            subSubMap.put(fld_val, fld_desc);
            subMap.put(fld_nm, subSubMap);
            sysDicMap.put(tbl_nm, subMap);
        }
    }

    public static String getFieldDesc(String tbl_nm, String fld_nm, String fld_val) {
        if (sysDicMap.containsKey(tbl_nm)) {
            LinkedHashMap subMap = (LinkedHashMap) sysDicMap.get(tbl_nm);
            if (subMap.containsKey(fld_nm)) {
                LinkedHashMap subsubMap = (LinkedHashMap) subMap.get(fld_nm);
                if (subsubMap.containsKey(fld_val)) {
                    return (String) subsubMap.get(fld_val);
                }
                return fld_val;
            }
            return fld_val;
        }
        return fld_val;
    }

    public static String chkFieldDesc(String tbl_nm, String fld_nm, String fld_val) {
        if (sysDicMap.containsKey(tbl_nm)) {
            LinkedHashMap subMap = (LinkedHashMap) sysDicMap.get(tbl_nm);
            if (subMap.containsKey(fld_nm)) {
                LinkedHashMap subsubMap = (LinkedHashMap) subMap.get(fld_nm);
                if (subsubMap.containsKey(fld_val)) {
                    return (String) subsubMap.get(fld_val);
                }
                return null;
            }
            return null;
        }
        return null;
    }

    public static LinkedHashMap getAllFieldDesc(String tbl_nm, String fld_nm) {
        if (sysDicMap.containsKey(tbl_nm)) {
            LinkedHashMap subMap = (LinkedHashMap) sysDicMap.get(tbl_nm);
            if (subMap.containsKey(fld_nm)) {
                LinkedHashMap subsubMap = (LinkedHashMap) subMap.get(fld_nm);
                return subsubMap;
            }
            return null;
        }
        return null;
    }
}
