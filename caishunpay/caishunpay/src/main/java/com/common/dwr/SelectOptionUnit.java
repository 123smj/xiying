/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.dom4j.Document
 *  org.dom4j.Element
 *  org.dom4j.io.SAXReader
 *  org.springframework.context.ApplicationContext
 *  org.springframework.core.io.Resource
 */
package com.common.dwr;

import com.common.dwr.SelectDynamicMode;
import com.common.dwr.SelectElement;
import com.common.dwr.SelectOptionExtractMethod;
import com.common.dwr.SelectSqlMode;
import com.common.dwr.SelectStaticMode;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

public class SelectOptionUnit {
    private static HashMap<String, SelectOptionExtractMethod> selectMap = new HashMap(10);

    public static void initSelectMethod(String txnId, SelectOptionExtractMethod extractMethod) {
        selectMap.put(txnId.trim(), extractMethod);
    }

    public static SelectOptionExtractMethod getSelectMethod(String txnId) throws Exception {
        if (!selectMap.containsKey(txnId.trim())) {
            throw new Exception("\u6ca1\u6709\u627e\u5230\u6307\u5b9a\u7684\u7f16\u53f7->[ " + txnId + " ]");
        }
        return selectMap.get(txnId.trim());
    }

    public static void initSelectOptions(ApplicationContext context) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = null;
        document = reader.read(context.getResource("/WEB-INF/xml/SelectOptions.xml").getInputStream());
        Element root = document.getRootElement();
        List txnElements = root.elements("TXN_ID");
        int i = 0;
        int n = txnElements.size();
        while (i < n) {
            Element txnElement = (Element)txnElements.get(i);
            String id = txnElement.attributeValue("id").trim();
            Element extractType = txnElement.element("EXTRACT_TYPE");
            String value = extractType.attributeValue("value");
            Element staticMode = extractType.element("STATIC_MODE");
            Element tblNmElement = staticMode.element("TBL_NM");
            Element fldNmElement = staticMode.element("FLD_NM");
            String tblNm = tblNmElement.getText();
            String fldNm = fldNmElement.getText();
            Element sqlModeElement = extractType.element("SQL_MODE");
            Element sqlElement = sqlModeElement.element("SQL");
            Element queryDaoElement = sqlModeElement.element("QUERY_DAO");
            String sql = sqlElement.getText();
            String queryDao = queryDaoElement.getText();
            Element dynamicModeElement = extractType.element("DYNAMIC_MODE");
            Element methodModeElement = dynamicModeElement.element("METHOD");
            String methodNm = methodModeElement.attributeValue("name");
            SelectElement selectElement = new SelectElement();
            SelectOptionExtractMethod extractMethod = new SelectOptionExtractMethod();
            SelectStaticMode selectStaticMode = new SelectStaticMode();
            SelectSqlMode selectSqlMode = new SelectSqlMode();
            SelectDynamicMode selectDynamicMode = new SelectDynamicMode();
            selectStaticMode.setFldNm(fldNm);
            selectStaticMode.setTblNm(tblNm);
            selectSqlMode.setQueryDao(queryDao);
            selectSqlMode.setSql(sql);
            selectDynamicMode.setMethod(methodNm);
            extractMethod.setExtractMode(value);
            extractMethod.setSelectStaticMode(selectStaticMode);
            extractMethod.setSelectSqlMode(selectSqlMode);
            extractMethod.setSelectDynamicMode(selectDynamicMode);
            selectElement.setTxnId(id);
            selectElement.setExtractMethod(extractMethod);
            SelectOptionUnit.initSelectMethod(id, extractMethod);
            ++i;
        }
    }
}
