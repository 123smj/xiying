/*
 * Decompiled with CFR 0_124.
 */
package com.common.dwr;

import com.common.dao.ICommQueryDAO;
import com.gy.system.SysParamUtil;
import com.gy.util.ContextUtil;
import com.gy.util.StringUtil;
import com.manage.bean.OprInfo;

import java.util.LinkedHashMap;
import java.util.List;

public class SelectMethod {
    static ICommQueryDAO commQueryDAO = (ICommQueryDAO) ContextUtil.getBean("commQueryDAO");

    public static LinkedHashMap<String, String> getBrhLvlByOprInfo(Object[] params) {
        LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();
        OprInfo operator = (OprInfo) params[0];
        if ("supers".endsWith(operator.getCompany_id())) {
            dataMap.put("1", "\u5206\u884c");
            dataMap.put("2", "\u652f\u884c");
            dataMap.put("3", "\u7f51\u70b9");
        } else {
            dataMap.put("2", "\u652f\u884c");
            dataMap.put("3", "\u7f51\u70b9");
        }
        return dataMap;
    }

    public static LinkedHashMap<String, String> getDaifuPan(Object[] params) {
        LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();
        StringBuffer sql = new StringBuffer("select bank_card_no,bank_name ||'-'|| cust_name from tbl_daifu_group where 1=1");
        if (params != null && params.length >= 2 && params[1] != null) {
            String group_id = params[1].toString();
            sql.append(" and group_id = '").append(group_id).append("'");
        }
        List<Object[]> dataList = commQueryDAO.findBySQLQuery(sql.toString());
        for (Object[] obj : dataList) {
            dataMap.put(obj[0].toString().trim(), String.valueOf(obj[0].toString().trim()) + "-" + StringUtil.trans2Str(obj[1]));
        }
        return dataMap;
    }

    public static LinkedHashMap<String, String> getMchtInfo(Object[] params) {
        OprInfo operator = (OprInfo) params[0];
        LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();
        StringBuffer sql = new StringBuffer("select mcht_no,mcht_name from trade_qrcode_mcht_info where 1=1");
        sql.append(" and mcht_no = '" + operator.getOpr_id() + "'");
        sql.append(" order by crt_time desc");
        List<Object[]> dataList = commQueryDAO.findBySQLQuery(sql.toString());
        for (Object[] obj : dataList) {
            dataMap.put(obj[0].toString().trim(), String.valueOf(obj[0].toString().trim()) + "-" + obj[1]);
        }
        return dataMap;
    }

    public static LinkedHashMap<String, String> getMchtInfoTmp(Object[] params) {
        OprInfo operator = (OprInfo) params[0];
        LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();
        StringBuffer sql = new StringBuffer("select mcht_no,mcht_name from trade_qrcode_mcht_info_tmp where 1=1");
        sql.append(" and mcht_no = '" + operator.getOpr_id() + "'");
        sql.append(" order by crt_time desc");
        List<Object[]> dataList = commQueryDAO.findBySQLQuery(sql.toString());
        for (Object[] obj : dataList) {
            dataMap.put(obj[0].toString().trim(), String.valueOf(obj[0].toString().trim()) + "-" + StringUtil.trans2Str(obj[1]));
        }
        return dataMap;
    }

    public static LinkedHashMap<String, String> getChannelMchtNo(Object[] params) {
        OprInfo operator = (OprInfo) params[0];
        LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();
        StringBuffer sql = new StringBuffer("select channel_mcht_no,channel_name from trade_qrcode_channel_inf where 1=1");
        if (params != null && params.length >= 2 && params[1] != null) {
            String channel_id = params[1].toString();
            sql.append(" and channel_id = '").append(channel_id).append("'");
        }
        sql.append(" order by crt_time desc");
        List<Object[]> dataList = commQueryDAO.findBySQLQuery(sql.toString());
        for (Object[] obj : dataList) {
            dataMap.put(obj[0].toString().trim(), String.valueOf(obj[0].toString().trim()) + "-" + StringUtil.trans2Str(obj[1]));
        }
        return dataMap;
    }

    public static LinkedHashMap<String, String> getChannelMchtNoTemp(Object[] params) {
        LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();
        StringBuffer sql = new StringBuffer("select channel_mcht_no,channel_name from trade_qrcode_channel_inf where status='01' and channel_id='hfbank' ");
        if (params != null && params.length >= 2 && params[1] != null) {
            String channel_id = params[1].toString();
            sql.append(" and channel_id = '").append(channel_id).append("'");
        }
        sql.append(" order by crt_time desc");
        List<Object[]> dataList = commQueryDAO.findBySQLQuery(sql.toString());
        for (Object[] obj : dataList) {
            dataMap.put(obj[0].toString().trim(), String.valueOf(obj[0].toString().trim()) + "-" + StringUtil.trans2Str(obj[1]));
        }
        return dataMap;
    }

    public static LinkedHashMap<String, String> getChannelMchtNoTrue(Object[] params) {
        OprInfo operator = (OprInfo) params[0];
        LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();
        StringBuffer sql = new StringBuffer("select channel_mcht_no,channel_name from trade_qrcode_channel_inf where status='00' ");
        if (params != null && params.length >= 2 && params[1] != null) {
            String channel_id = params[1].toString();
            sql.append(" and channel_id = '").append(channel_id).append("'");
        }
        sql.append(" order by crt_time desc");
        List<Object[]> dataList = commQueryDAO.findBySQLQuery(sql.toString());
        for (Object[] obj : dataList) {
            dataMap.put(obj[0].toString().trim(), String.valueOf(obj[0].toString().trim()) + "-" + StringUtil.trans2Str(obj[1]));
        }
        return dataMap;
    }

    public static LinkedHashMap<String, String> getChannelId(Object[] params) {
        OprInfo operator = (OprInfo) params[0];
        LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();
        StringBuffer sql = new StringBuffer("select KEY,VALUE from CST_SYS_PARAM where OWNER='CHANNEL_ID' ");
        List<Object[]> dataList = commQueryDAO.findBySQLQuery(sql.toString());
        for (Object[] obj : dataList) {
            dataMap.put(obj[0].toString().trim(), String.valueOf(obj[0].toString().trim()) + "-" + StringUtil.trans2Str(obj[1]));
        }
        return dataMap;
    }

    public static LinkedHashMap<String, String> getCompanyId(Object[] params) {
        OprInfo operator = (OprInfo) params[0];
        LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();
        StringBuffer sql = new StringBuffer("select distinct company_id,company_name from trade_opr_info where 1=1");
        List<Object[]> dataList = commQueryDAO.findBySQLQuery(sql.toString());
        for (Object[] obj : dataList) {
            dataMap.put(obj[0].toString().trim(), String.valueOf(obj[0].toString().trim()) + "-" + StringUtil.trans2Str(obj[1]));
        }
        return dataMap;
    }

    public static LinkedHashMap<String, String> getJumpGroup(Object[] params) {
        OprInfo operator = (OprInfo) params[0];
        LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();
        StringBuffer sql = new StringBuffer("select distinct jump_group,group_name from trade_jump_list ");
        List<Object[]> dataList = commQueryDAO.findBySQLQuery(sql.toString());
        for (Object[] obj : dataList) {
            dataMap.put(obj[0].toString().trim(), String.valueOf(obj[0].toString().trim()) + "-" + StringUtil.trans2Str(obj[1]));
        }
        return dataMap;
    }

    public static LinkedHashMap<String, String> getApiList(Object[] params) {
        LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();
        StringBuffer sql = new StringBuffer("select KEY,VALUE from CST_SYS_PARAM where OWNER='API_LIST' ");
        List<Object[]> dataList = commQueryDAO.findBySQLQuery(sql.toString());
        for (Object[] obj : dataList) {
            dataMap.put(StringUtil.trans2Str(obj[1]), String.valueOf(SysParamUtil.getParam("API_UPLOAD_PATH")) + StringUtil.trans2Str(obj[1]));
        }
        return dataMap;
    }

    public static LinkedHashMap<String, String> getReportList(Object[] params) {
        OprInfo operator = (OprInfo) params[0];
        LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();
        StringBuffer sql = new StringBuffer("select KEY,VALUE,DESCR from CST_SYS_PARAM where OWNER='REPORT_LIST' and KEY != 'qrc_all_report' ");
        List<Object[]> dataList = commQueryDAO.findBySQLQuery(sql.toString());
        for (Object[] obj : dataList) {
            dataMap.put(StringUtil.trans2Str(obj[1]), StringUtil.trans2Str(obj[2]));
        }
        return dataMap;
    }
}
