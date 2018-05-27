/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  javax.mail.MessagingException
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Component
 */
package com.trade.timer;

import com.account.service.TradeMchtAccountService;
import com.common.dao.CommQueryDAO;
import com.gy.util.ContextUtil;
import com.gy.util.DateUtil;
import com.gy.util.EmailUtil;
import java.security.GeneralSecurityException;
import java.util.List;
import javax.mail.MessagingException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DaifuResultTask {
    @Autowired
    private TradeMchtAccountService tradeMchtAccountServiceImpl;
    private static Logger log = Logger.getLogger(DaifuResultTask.class);

    public void queryDfResult() {
        try {
            this.tradeMchtAccountServiceImpl.queryUnSuccessDaifuResult();
        }
        catch (Exception e) {
            log.error((Object)("\u5b9a\u65f6\u4efb\u52a1\u5f02\u5e38:queryDfResult" + e.getMessage()));
        }
    }

    public void queryDfException() {
        try {
            CommQueryDAO commQueryDAO = (CommQueryDAO)ContextUtil.getBean("commQueryDAO");
            String date = DateUtil.getDateBeforeDays(1, "yyyyMMdd");
            String sql = "select mcht_no,t.df_sn,t.account_order_no,t.receipt_amount,t.receipt_pan,t.receipt_bank_nm,t.receipt_name,t.df_status,t.df_desc,t.receipt_time from trade_mcht_account_detail t where acc_type='2' and t.df_status='9999' and t.receipt_time like'" + date + "%' order by t.receipt_time desc";
            List result = commQueryDAO.findBySQLQuery(sql);
            if (result != null && result.size() != 0) {
                this.sendDfExcToManager(date, result);
            }
        }
        catch (Exception e) {
            log.error((Object)("\u5b9a\u65f6\u4efb\u52a1\u5f02\u5e38:queryDfException" + e.getMessage()));
        }
    }

    private void sendDfExcToManager(String date, List result) {
        String title = String.valueOf(date) + "\u4ee3\u4ed8\u5f02\u5e38\u8ba2\u5355";
        StringBuilder content = new StringBuilder();
        content.append("\u5f02\u5e38\u4ee3\u4ed8\u8ba2\u5355\u5982\u4e0b: \n");
        Object[] objs = null;
        int i = 0;
        while (i < result.size()) {
            objs = (Object[])result.get(i);
            content.append("    \u5e8f\u53f7\uff1a" + (i + 1) + "\n    \u5546\u6237\u53f7\uff1a" + objs[0] + "\n    \u8ba2\u5355\u65f6\u95f4\uff1a" + objs[9] + "\n    \u5546\u6237\u4ee3\u4ed8\u5355\u53f7\uff1a" + objs[1] + "\n    \u5e73\u53f0\u4ee3\u4ed8\u5355\u53f7\uff1a" + objs[2] + "\n    \u4ee3\u4ed8\u91d1\u989d(\u5206)\uff1a" + objs[3] + "\n    \u94f6\u884c\u5361\u53f7\uff1a" + objs[4] + "\n    \u94f6\u884c\u540d\u79f0\uff1a" + objs[5] + "\n    \u6536\u6b3e\u4eba\uff1a" + objs[6] + "\n    \u901a\u9053\u8fd4\u56de\u7801\uff1a" + objs[7] + "\n    \u901a\u9053\u8fd4\u56de\u7801\u63cf\u8ff0\uff1a" + objs[8] + "\n    ------------------------------------------------------   \n");
            ++i;
        }
        content.append("    \u5f02\u5e38\u4ee3\u4ed8\u8ba2\u5355\u5171\u4e0a\u8ff0" + result.size() + "\u7b14\uff0c\u8bf7\u68c0\u67e5");
//        try {
//            EmailUtil.sendEmal("admin@gjdincs.com", title, content.toString());
//        }
//        catch (MessagingException e) {
//            e.printStackTrace();
//        }
//        catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        }
    }
}
