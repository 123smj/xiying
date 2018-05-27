/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Component
 */
package com.trade.timer;

import com.common.dao.CommQueryDAO;
import com.gy.system.SysParamUtil;
import com.gy.util.CommonFunction;
import com.gy.util.ContextUtil;
import com.gy.util.DateUtil;
import com.gy.util.StringUtil;
import com.manage.bean.OprInfo;
import com.trade.bean.TradeDetailBean;
import com.trade.dao.ThirdPartyPayDetailDao;
import com.trade.enums.OprTypeEnum;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TradeReportTask {
    @Autowired
    private ThirdPartyPayDetailDao thirdPartyPayDetailDao;
    private static Logger log = Logger.getLogger(TradeReportTask.class);

    public void createMchtReports() {
        try {
            CommQueryDAO commQueryDAO = (CommQueryDAO)ContextUtil.getBean("commQueryDAO");
            String date = DateUtil.getDateBeforeDays(1, "yyyyMMdd");
            String sql = "select distinct gy_mcht_Id from TRADE_WXPAY_INF t where t.time_start like'" + date + "%' ";
            List result = commQueryDAO.findBySQLQuery(sql);
            if (result == null || result.size() == 0) {
                return;
            }
            for (Object obj : result) {
                this.createMchtReport(date, (String)obj);
            }
        }
        catch (Exception e) {
            log.error((Object)("\u5b9a\u65f6\u4efb\u52a1\u5f02\u5e38:createMchtReports" + e.getMessage()));
        }
    }

    public void createManagerReports() {
        try {
            String date = DateUtil.getDateBeforeDays(1, "yyyyMMdd");
            this.createManagerReport(date);
        }
        catch (Exception e) {
            log.error((Object)("\u5b9a\u65f6\u4efb\u52a1\u5f02\u5e38:createManagerReports" + e.getMessage()));
        }
    }

    private void createMchtReport(String date, String mchtNo) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("gymchtId", mchtNo);
        param.put("time_start_begin", date);
        param.put("time_start_end", date);
        OprInfo oprInfo = new OprInfo();
        oprInfo.setOpr_type(OprTypeEnum.PARENT.getCode());
        List<TradeDetailBean> tradeList = this.thirdPartyPayDetailDao.listAll(param, oprInfo);
        String rootPath = String.valueOf(SysParamUtil.getParam("TRADE_REPORT_AUTOCREATE_PATH")) + date;
        File pathFile = new File(rootPath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        String fileName = "\u626b\u7801\u4ea4\u6613\u62a5\u8868_" + date + "_" + mchtNo + ".csv";
        try {
            this.generateTradeFile(tradeList, String.valueOf(rootPath) + "/" + fileName, false);
        }
        catch (Exception e) {
            log.error((Object)("\u81ea\u52a8\u751f\u6210\u626b\u7801\u4ea4\u6613\u62a5\u8868\u5f02\u5e38:" + e.toString()));
            e.printStackTrace();
        }
    }

    private void createManagerReport(String date) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("time_start_begin", date);
        param.put("time_start_end", date);
        OprInfo oprInfo = new OprInfo();
        oprInfo.setOpr_type(OprTypeEnum.PARENT.getCode());
        List<TradeDetailBean> tradeList = this.thirdPartyPayDetailDao.listAll(param, oprInfo);
        String rootPath = String.valueOf(SysParamUtil.getParam("TRADE_REPORT_AUTOCREATE_PATH")) + date;
        File pathFile = new File(rootPath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        String fileName = "\u626b\u7801\u4ea4\u6613\u62a5\u8868\u6c47\u603b_" + date + ".csv";
        try {
            this.generateTradeFile(tradeList, String.valueOf(rootPath) + "/" + fileName, true);
        }
        catch (Exception e) {
            log.error((Object)("\u81ea\u52a8\u751f\u6210\u626b\u7801\u4ea4\u6613\u62a5\u8868\u5f02\u5e38:" + e.toString()));
            e.printStackTrace();
        }
    }

    private String generateTradeFile(List<TradeDetailBean> tradeList, String filePath, boolean isManager) throws Exception {
        File tradeFile = new File(filePath);
        FileChannel fileChannel = new FileOutputStream(tradeFile).getChannel();
        StringBuffer sBuffer = new StringBuffer();
        if (isManager) {
            sBuffer.append("\u4ea4\u6613\u65e5\u671f,\u4ea4\u6613\u65f6\u95f4,\u5546\u6237\u7f16\u53f7,\u5546\u6237\u540d\u79f0,\u6e20\u9053\u5546\u6237\u53f7,\u6e20\u9053\u540d\u79f0,\u63a5\u5165\u65b9\u8ba2\u5355\u53f7,\u5e73\u53f0\u8ba2\u5355\u53f7,\u4e0a\u6e38\u8ba2\u5355\u53f7,\u4ea4\u6613\u91d1\u989d(\u5143),\u8d39\u7387(%),\u624b\u7eed\u8d39(\u5143),\u6e05\u7b97\u91d1\u989d(\u5143),\u63a5\u53e3\u7c7b\u578b,\u5e94\u7b54\u7801,\u5e94\u7b54\u7801\u63cf\u8ff0,\u4ea4\u6613\u72b6\u6001,\u652f\u4ed8\u65f6\u95f4,\u662f\u5426T+0,\u4ea4\u6613\u6765\u6e90");
        } else {
            sBuffer.append("\u4ea4\u6613\u65e5\u671f,\u4ea4\u6613\u65f6\u95f4,\u5546\u6237\u7f16\u53f7,\u5546\u6237\u540d\u79f0,\u6e20\u9053\u5546\u6237\u53f7,\u63a5\u5165\u65b9\u8ba2\u5355\u53f7,\u5e73\u53f0\u8ba2\u5355\u53f7,\u4e0a\u6e38\u8ba2\u5355\u53f7,\u4ea4\u6613\u91d1\u989d(\u5143),\u63a5\u53e3\u7c7b\u578b,\u5e94\u7b54\u7801,\u5e94\u7b54\u7801\u63cf\u8ff0,\u4ea4\u6613\u72b6\u6001,\u652f\u4ed8\u65f6\u95f4,\u662f\u5426T+0,\u4ea4\u6613\u6765\u6e90");
        }
        sBuffer.append("\n");
        for (TradeDetailBean tradeBean : tradeList) {
            sBuffer.append("'").append(tradeBean.getTime_start().substring(0, 8));
            sBuffer.append(",'").append(tradeBean.getTime_start().substring(8, 14));
            sBuffer.append(",'").append(tradeBean.getGymchtId());
            sBuffer.append(",").append(tradeBean.getMchtName());
            sBuffer.append(",'").append(tradeBean.getMcht_id());
            if (isManager) {
                if ("guangda".equals(tradeBean.getChannel_id())) {
                    if (tradeBean.getMcht_id().startsWith("1015")) {
                        sBuffer.append(",'").append("\u5174\u4e1a\u94f6\u884c");
                    } else if (tradeBean.getMcht_id().startsWith("1025")) {
                        sBuffer.append(",'").append("\u4e2d\u4fe1\u94f6\u884c");
                    } else if (tradeBean.getMcht_id().startsWith("1035")) {
                        sBuffer.append(",'").append("\u6d66\u53d1\u94f6\u884c");
                    } else if (tradeBean.getMcht_id().startsWith("1055")) {
                        sBuffer.append(",'").append("\u5149\u5927\u94f6\u884c");
                    } else {
                        sBuffer.append(",'").append(CommonFunction.transChannel(tradeBean.getChannel_id()));
                    }
                } else {
                    sBuffer.append(",'").append(CommonFunction.transChannel(tradeBean.getChannel_id()));
                }
            }
            sBuffer.append(",'").append(tradeBean.getTradeSn());
            sBuffer.append(",'").append(tradeBean.getOut_trade_no());
            sBuffer.append(",'").append(StringUtil.trans2Str(tradeBean.getTransaction_id()));
            sBuffer.append(",").append(tradeBean.getTotal_fee());
            if (isManager) {
                sBuffer.append(",'").append(StringUtil.trans2Str(tradeBean.getMcht_rate()));
                sBuffer.append(",'").append(StringUtil.trans2Str(tradeBean.getRate_fee()));
                sBuffer.append(",'").append(StringUtil.trans2Str(tradeBean.getSettle_fee()));
            }
            sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getService()));
            sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getResult_code()));
            sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getMessage()));
            sBuffer.append(",").append(CommonFunction.transTradeState(tradeBean.getTrade_state()));
            sBuffer.append(",'").append(StringUtil.trans2Str(tradeBean.getTime_end()));
            sBuffer.append(",").append(CommonFunction.transT0Flag(tradeBean.getT0Flag()));
            sBuffer.append(",").append(CommonFunction.transTradeSource(tradeBean.getTrade_source()));
            sBuffer.append("\n");
        }
        ByteBuffer buffer = ByteBuffer.wrap(sBuffer.toString().getBytes());
        fileChannel.write(buffer);
        buffer.clear();
        fileChannel.close();
        return filePath;
    }
}
