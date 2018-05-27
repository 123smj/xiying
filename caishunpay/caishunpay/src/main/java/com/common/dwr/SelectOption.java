package com.common.dwr;

import com.common.dao.ICommQueryDAO;
import com.gy.util.ContextUtil;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import org.directwebremoting.util.Logger;

public class SelectOption {
    private static Logger log = Logger.getLogger(SelectOption.class);

    public static LinkedHashMap getSelectView(String txnId, Object[] args) throws Exception {
        LinkedHashMap map = new LinkedHashMap();
        SelectOptionExtractMethod extractMethod = SelectOptionUnit.getSelectMethod(txnId.trim());
        return parseExtractMethod(extractMethod, args, map);
    }

    public static LinkedHashMap parseExtractMethod(SelectOptionExtractMethod extractMethod, Object[] args, LinkedHashMap map) throws Exception {
        String extractMode = extractMethod.getExtractMode().trim();
        String method;
        String sql;
        if ("STATIC".equalsIgnoreCase(extractMode)) {
            SelectStaticMode selectStaticMode = extractMethod.getSelectStaticMode();
            method = selectStaticMode.getTblNm().trim();
            sql = selectStaticMode.getFldNm().trim();
            map = SystemDictionaryUnit.getAllFieldDesc(method, sql);
        } else if ("SQL".equalsIgnoreCase(extractMode)) {
            SelectSqlMode selectSqlMode = extractMethod.getSelectSqlMode();
            method = selectSqlMode.getQueryDao();
            sql = selectSqlMode.getSql().trim();
            ICommQueryDAO commQueryDAO = (ICommQueryDAO)ContextUtil.getBean(method);
            List dataList = commQueryDAO.findBySQLQuery(sql);
            Iterator iterator = dataList.iterator();

            while(iterator.hasNext()) {
                Object[] obj = (Object[])iterator.next();
                String value = obj[0].toString().trim();
                String text = obj[1].toString().trim();
                map.put(value, text);
            }
        } else {
            if (!"DYNAMIC".equalsIgnoreCase(extractMode)) {
                log.info("SelectOption方法没有找到");
                throw new Exception("SelectOption方法没有找到");
            }

            if (args == null) {
                return null;
            }

            SelectDynamicMode dynamicMode = extractMethod.getSelectDynamicMode();
            method = dynamicMode.getMethod().trim();
            if (!method.equals("")) {
                map = (LinkedHashMap)SelectMethod.class.getMethod(method, Object[].class).invoke(SelectMethod.class, (Object)args);
            }
        }

        return map;
    }
}
