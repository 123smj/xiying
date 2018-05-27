/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.directwebremoting.util.Logger
 */
package com.common.dwr;

import com.common.dao.ICommQueryDAO;
import com.common.dwr.SelectDynamicMode;
import com.common.dwr.SelectMethod;
import com.common.dwr.SelectOptionExtractMethod;
import com.common.dwr.SelectOptionUnit;
import com.common.dwr.SelectSqlMode;
import com.common.dwr.SelectStaticMode;
import com.common.dwr.SystemDictionaryUnit;
import com.gy.util.ContextUtil;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;

import org.directwebremoting.util.Logger;

public class SelectOption {
    private static Logger log = Logger.getLogger(SelectOption.class);

    public static LinkedHashMap<String, String> getSelectView(String txnId, Object[] args) throws Exception {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        SelectOptionExtractMethod extractMethod = SelectOptionUnit.getSelectMethod(txnId.trim());
        return SelectOption.parseExtractMethod(extractMethod, args, map);
    }

    public static LinkedHashMap<String, String> parseExtractMethod(SelectOptionExtractMethod extractMethod, Object[] args, LinkedHashMap<String, String> map) throws Exception {
        String extractMode = extractMethod.getExtractMode().trim();
        if ("STATIC".equalsIgnoreCase(extractMode)) {
            SelectStaticMode selectStaticMode = extractMethod.getSelectStaticMode();
            String tblNm = selectStaticMode.getTblNm().trim();
            String fldNm = selectStaticMode.getFldNm().trim();
            map = SystemDictionaryUnit.getAllFieldDesc(tblNm, fldNm);
        } else if ("SQL".equalsIgnoreCase(extractMode)) {
            SelectSqlMode selectSqlMode = extractMethod.getSelectSqlMode();
            String queryDao = selectSqlMode.getQueryDao();
            String sql = selectSqlMode.getSql().trim();
            ICommQueryDAO commQueryDAO = (ICommQueryDAO) ContextUtil.getBean(queryDao);
            List<Object[]> dataList = (List<Object[]>) commQueryDAO.findBySQLQuery(sql);
            for (Object[] obj : dataList) {
                String value = obj[0].toString().trim();
                String text = obj[1].toString().trim();
                map.put(value, text);
            }
        } else if ("DYNAMIC".equalsIgnoreCase(extractMode)) {
            if (args == null) {
                return null;
            }
            SelectDynamicMode dynamicMode = extractMethod.getSelectDynamicMode();
            String method = dynamicMode.getMethod().trim();
            if (!method.equals("")) {
                map = (LinkedHashMap) SelectMethod.class.getMethod(method, Object[].class).invoke(SelectMethod.class, (Object) args);
            }
        } else {
            log.info("SelectOption\u65b9\u6cd5\u6ca1\u6709\u627e\u5230");
            throw new Exception("SelectOption\u65b9\u6cd5\u6ca1\u6709\u627e\u5230");
        }
        return map;
    }
}
