/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 */
package com.gy.util;

import com.common.dao.ICommQueryDAO;
import com.gy.util.CommonFunction;
import com.gy.util.ContextUtil;
import com.gy.util.DateUtil;
import com.gy.util.StringUtil;
import java.math.BigDecimal;
import java.util.List;
import org.apache.log4j.Logger;

public class GenerateNextId {
    private static ICommQueryDAO commQueryDAO = (ICommQueryDAO)ContextUtil.getBean("commQueryDAO");
    private static Logger log = Logger.getLogger(GenerateNextId.class);

    public static synchronized String getMchntId(String str) {
        String sql = "select count(1) from TBL_MCHT_BASE_INF_TMP where trim(mcht_no) = '" + str + "0001" + "'";
        BigDecimal c = (BigDecimal)commQueryDAO.findBySQLQuery(sql).get(0);
        if (c.intValue() == 0) {
            return String.valueOf(str) + "0001";
        }
        sql = "select min(substr(mcht_no,12,4) + 1) from TBL_MCHT_BASE_INF_TMP where (substr(mcht_no,12,4) + 1) not in (select substr(mcht_no,12,4) + 0 from TBL_MCHT_BASE_INF_TMP where substr(mcht_no,1,11) = '" + str + "') " + "and substr(mcht_no,1,11) = '" + str + "'";
        List resultSet = commQueryDAO.findBySQLQuery(sql);
        if (resultSet.get(0) == null) {
            return String.valueOf(str) + "0001";
        }
        int id = ((BigDecimal)resultSet.get(0)).intValue();
        if (id == 10000) {
            return "";
        }
        return String.valueOf(str) + CommonFunction.fillString(String.valueOf(id), '0', 4, false);
    }

    public static synchronized String getNextMchntId(String companyNo) throws Exception {
        if (companyNo == null) {
            throw new Exception("\u516c\u53f8\u7f16\u53f7\u4e0d\u6b63\u786e!");
        }
        String sql = "select count(1) from trade_qrcode_mcht_info where company_id = '" + companyNo + "'";
        BigDecimal c = (BigDecimal)commQueryDAO.findBySQLQuery(sql).get(0);
        int num = 1;
        String mchtPre = companyNo.length() > 6 ? companyNo.substring(0, 6) : String.valueOf(StringUtil.fillValue(companyNo, 6, '0')) + DateUtil.getCurrentFormat("yy");
        String seq = "";
        String mchtNo = "";
        String mchtSql = "";
        String mchtTempSql = "";
        BigDecimal count = null;
        BigDecimal tempCount = null;
        do {
            seq = CommonFunction.fillString(String.valueOf(c.intValue() + num), '0', 4, false);
            mchtNo = String.valueOf(mchtPre) + seq;
            mchtSql = "select count(1) from trade_qrcode_mcht_info where mcht_no = '" + mchtNo + "'";
            mchtTempSql = "select count(1) from trade_qrcode_mcht_info_tmp where mcht_no = '" + mchtNo + "'";
            count = (BigDecimal)commQueryDAO.findBySQLQuery(mchtSql).get(0);
            tempCount = (BigDecimal)commQueryDAO.findBySQLQuery(mchtTempSql).get(0);
        } while ((count.intValue() > 0 || tempCount.intValue() > 0) && ++num <= 1000);
        return String.valueOf(mchtPre) + seq;
    }
}
