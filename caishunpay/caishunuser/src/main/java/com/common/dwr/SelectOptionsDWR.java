/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpServletResponse
 *  javax.servlet.http.HttpSession
 *  org.apache.log4j.Logger
 */
package com.common.dwr;

import com.common.dwr.JSONBean;
import com.common.dwr.SelectOption;
import com.manage.bean.OprInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class SelectOptionsDWR {
    private static Logger log = Logger.getLogger(SelectOptionsDWR.class);

    public String getComboData(String txnId, HttpServletRequest request, HttpServletResponse response) {
        String jsonData = "{\"data\":[{\"valueField\":\"\",\"displayField\":\"\u6ca1\u6709\u627e\u5230\u53ef\u9009\u5185\u5bb9\"}]}";
        try {
            OprInfo operator = (OprInfo) request.getSession().getAttribute("oprInfo");
            LinkedHashMap<String, String> dataMap = SelectOption.getSelectView(txnId, new Object[]{operator});
            Iterator<String> iter = dataMap.keySet().iterator();
            if (iter.hasNext()) {
                HashMap<String, Object> jsonDataMap = new HashMap<String, Object>();
                LinkedList jsonDataList = new LinkedList();
                LinkedHashMap<String, String> tmpMap = null;
                String key = null;
                while (iter.hasNext()) {
                    tmpMap = new LinkedHashMap<String, String>();
                    key = iter.next();
                    tmpMap.put("valueField", key);
                    tmpMap.put("displayField", dataMap.get(key));
                    jsonDataList.add(tmpMap);
                }
                jsonDataMap.put("data", jsonDataList);
                jsonData = JSONBean.genMapToJSON(jsonDataMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) e.getMessage());
        }
        return jsonData;
    }

    public String getComboDataWithParameter(String txnId, String parameter, HttpServletRequest request, HttpServletResponse response) {
        String jsonData = "{\"data\":[{\"valueField\":\"\",\"displayField\":\"\u6ca1\u6709\u627e\u5230\u53ef\u9009\u5185\u5bb9\"}]}";
        try {
            OprInfo operator = (OprInfo) request.getSession().getAttribute("oprInfo");
            LinkedHashMap<String, String> dataMap = SelectOption.getSelectView(txnId, new Object[]{operator, parameter});
            Iterator<String> iter = dataMap.keySet().iterator();
            if (iter.hasNext()) {
                HashMap<String, Object> jsonDataMap = new HashMap<String, Object>();
                LinkedList jsonDataList = new LinkedList();
                LinkedHashMap<String, String> tmpMap = null;
                String key = null;
                while (iter.hasNext()) {
                    tmpMap = new LinkedHashMap<String, String>();
                    key = iter.next();
                    tmpMap.put("valueField", key);
                    tmpMap.put("displayField", dataMap.get(key));
                    jsonDataList.add(tmpMap);
                }
                jsonDataMap.put("data", jsonDataList);
                jsonData = JSONBean.genMapToJSON(jsonDataMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) e.getMessage());
        }
        return jsonData;
    }

    public LinkedHashMap<String, String> getDataMap(String txnId, HttpServletRequest request, HttpServletResponse response) {
        try {
            OprInfo operator = (OprInfo) request.getSession().getAttribute("oprInfo");
            LinkedHashMap<String, String> dataMap = SelectOption.getSelectView(txnId, new Object[]{operator});
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) e.getMessage());
            return null;
        }
    }

    public String getFuncAllData(String txnId, HttpServletRequest request, HttpServletResponse response) {
        String jsonData = "{\"data\":[{\"valueField\":\"\",\"displayField\":\"\u6ca1\u6709\u627e\u5230\u53ef\u9009\u5185\u5bb9\"}]}";
        try {
            OprInfo operator = (OprInfo) request.getSession().getAttribute("oprInfo");
            LinkedHashMap<String, String> dataMap = SelectOption.getSelectView(txnId, new Object[]{operator});
            Iterator<String> iter = dataMap.keySet().iterator();
            if (iter.hasNext()) {
                HashMap<String, Object> jsonDataMap = new HashMap<String, Object>();
                LinkedList jsonDataList = new LinkedList();
                LinkedHashMap<String, String> tmpMap = null;
                String key = null;
                while (iter.hasNext()) {
                    tmpMap = new LinkedHashMap<String, String>();
                    key = iter.next();
                    tmpMap.put("valueField", key);
                    tmpMap.put("displayField", dataMap.get(key));
                    jsonDataList.add(tmpMap);
                }
                jsonDataMap.put("data", jsonDataList);
                jsonData = JSONBean.genMapToJSON(jsonDataMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) e.getMessage());
        }
        return jsonData;
    }
}
