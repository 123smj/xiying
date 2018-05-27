/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpSession
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Controller
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.ResponseBody
 */
package com.manage.controller;

import com.common.model.Response;
import com.gy.system.SysParamUtil;
import com.gy.util.CommonFunction;
import com.gy.util.DateUtil;
import com.gy.util.StringUtil;
import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.trade.bean.QuickpayQueryBean;
import com.trade.bean.TradeDetailBean;
import com.trade.enums.OprTypeEnum;
import com.trade.service.QuickpayService;
import com.trade.service.WxpayService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = {"/manage"})
public class TradeQueryController {
    private static Logger log = Logger.getLogger(TradeQueryController.class);
    @Autowired
    private WxpayService wxpayServiceImpl;
    @Autowired
    private QuickpayService mobaoPayServiceImpl;

    @RequestMapping(value = {"/tradeQuery"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public PageModle<TradeDetailBean> tradeQuery(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
        PageModle<TradeDetailBean> pageModle = null;
        try {
            int pageNum = 1;
            Map param = this.buildRequestParam(request);
            if (!StringUtil.isEmpty((String) param.get("pageNum"))) {
                pageNum = StringUtil.parseInt((String) param.get("pageNum"));
            }
            System.out.println(param);
            pageModle = this.wxpayServiceImpl.listByPage(param, pageNum, 10, oprInfo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("\u5546\u6237\u4ea4\u6613\u67e5\u8be2\u5f02\u5e38:" + oprInfo.getOpr_id() + e));
        }
        return pageModle;
    }

    @RequestMapping(value = {"/quickPayQuery"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public PageModle<QuickpayQueryBean> quickPayQuery(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
        PageModle<QuickpayQueryBean> pageModle = null;
        try {
            int pageNum = 1;
            Map param = this.buildRequestParam(request);
            if (!StringUtil.isEmpty((String) param.get("pageNum"))) {
                pageNum = StringUtil.parseInt((String) param.get("pageNum"));
            }
            System.out.println(param);
            pageModle = this.mobaoPayServiceImpl.listByPage(param, pageNum, 10);
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return pageModle;
    }

    @RequestMapping(value = {"/quickPayExport"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public String quickPayExport(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
        List<QuickpayQueryBean> tradeList = null;
        String rootPath = String.valueOf(SysParamUtil.getParam("TRADE_EXPORT_PATH")) + DateUtil.getCurrentDay();
        File pathFile = new File(rootPath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        String fileName = "\u5feb\u6377\u652f\u4ed8\u62a5\u8868_" + oprInfo.getOpr_id() + "_" + DateUtil.getCurrTime() + StringUtil.getRandom(3) + ".csv";
        String result = "";
        try {
            Map param = this.buildRequestParam(request);
            tradeList = this.mobaoPayServiceImpl.listAll(param);
            result = this.generateQuickpayFile(tradeList, String.valueOf(rootPath) + "/" + fileName, false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return result;
    }

    @RequestMapping(value = {"/tradeExport"}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public Response<String> tradeExport(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
        List<TradeDetailBean> tradeList = null;
        String rootPath = String.valueOf(SysParamUtil.getParam("TRADE_EXPORT_PATH")) + DateUtil.getCurrentDay();
        File pathFile = new File(rootPath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        String fileName = "\u626b\u7801\u4ea4\u6613\u62a5\u8868_" + oprInfo.getOpr_id() + "_" + DateUtil.getCurrTime() + StringUtil.getRandom(3) + ".csv";
        Response<String> response = new Response<String>();
        try {
            Map param = this.buildRequestParam(request);
            int acountTrade = this.wxpayServiceImpl.acount(param, oprInfo);
            if (acountTrade > 500) {
                response.setCode("08");
                response.setMessage("\u6570\u636e\u8d85\u8fc7500\u6761\uff0c\u8bf7\u5206\u6279\u4e0b\u8f7d");
                return response;
            }
            tradeList = this.wxpayServiceImpl.listAll(param, oprInfo);
            String path = this.generateTradeFile(tradeList, String.valueOf(rootPath) + "/" + fileName, this.isManager(oprInfo));
            response.setMessage("\u6210\u529f");
            response.setCode("00");
            response.setData(path);
        } catch (Exception e) {
            e.printStackTrace();
            log.error((Object) ("exception-----opr_id:" + oprInfo.getOpr_id() + e));
            response.setCode("09");
            response.setMessage("\u64cd\u4f5c\u5931\u8d25");
            return response;
        }
        return response;
    }

    private String generateTradeFile(List<TradeDetailBean> tradeList, String filePath, boolean isManager) {
        File tradeFile = new File(filePath);
        try {
            FileChannel fileChannel = new FileOutputStream(tradeFile).getChannel();
            StringBuffer sBuffer = new StringBuffer();
            if (isManager) {
                sBuffer.append("\u4ea4\u6613\u65e5\u671f,\u4ea4\u6613\u65f6\u95f4,\u5546\u6237\u7f16\u53f7,\u5546\u6237\u540d\u79f0,\u6e20\u9053\u5546\u6237\u53f7,\u6e20\u9053\u540d\u79f0,\u63a5\u5165\u65b9\u8ba2\u5355\u53f7,\u5e73\u53f0\u8ba2\u5355\u53f7,\u4e0a\u6e38\u8ba2\u5355\u53f7,\u4ea4\u6613\u91d1\u989d(\u5143),\u63a5\u53e3\u7c7b\u578b,\u5e94\u7b54\u7801,\u5e94\u7b54\u7801\u63cf\u8ff0,\u4ea4\u6613\u72b6\u6001,\u652f\u4ed8\u65f6\u95f4,\u662f\u5426T+0,\u4ea4\u6613\u6765\u6e90");
            } else {
                sBuffer.append("\u4ea4\u6613\u65e5\u671f,\u4ea4\u6613\u65f6\u95f4,\u5546\u6237\u7f16\u53f7,\u5546\u6237\u540d\u79f0,\u6e20\u9053\u5546\u6237\u53f7,\u63a5\u5165\u65b9\u8ba2\u5355\u53f7,\u5e73\u53f0\u8ba2\u5355\u53f7,\u4ea4\u6613\u91d1\u989d(\u5143),\u5e94\u7b54\u7801,\u5e94\u7b54\u7801\u63cf\u8ff0,\u4ea4\u6613\u72b6\u6001,\u652f\u4ed8\u65f6\u95f4,\u662f\u5426T+0,\u4ea4\u6613\u6765\u6e90");
            }
            sBuffer.append("\n");
            for (TradeDetailBean tradeBean : tradeList) {
                sBuffer.append("'").append(tradeBean.getTime_start().substring(0, 8));
                sBuffer.append(",'").append(tradeBean.getTime_start().substring(8, 14));
                sBuffer.append(",'").append(tradeBean.getGymchtId());
                sBuffer.append(",'").append(tradeBean.getMchtName());
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
                sBuffer.append(",").append(tradeBean.getTotal_fee());
                sBuffer.append(",'").append(StringUtil.trans2Str(tradeBean.getResult_code()));
                sBuffer.append(",'").append(StringUtil.trans2Str(tradeBean.getMessage()));
                sBuffer.append(",'").append(CommonFunction.transTradeState(tradeBean.getTrade_state()));
                sBuffer.append(",'").append(StringUtil.trans2Str(tradeBean.getTime_end()));
                sBuffer.append(",'").append(CommonFunction.transT0Flag(tradeBean.getT0Flag()));
                sBuffer.append(",'").append(CommonFunction.transTradeSource(tradeBean.getTrade_source()));
                sBuffer.append("\n");
            }
            ByteBuffer buffer = ByteBuffer.wrap(sBuffer.toString().getBytes());
            fileChannel.write(buffer);
            buffer.clear();
            fileChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    private String generateQuickpayFile(List<QuickpayQueryBean> tradeList, String filePath, boolean isManager) {
        File tradeFile = new File(filePath);
        try {
            FileChannel fileChannel = new FileOutputStream(tradeFile).getChannel();
            StringBuffer sBuffer = new StringBuffer();
            if (isManager) {
                sBuffer.append("\u4ea4\u6613\u65e5\u671f,\u4ea4\u6613\u65f6\u95f4,\u5546\u6237\u7f16\u53f7,\u5546\u6237\u540d\u79f0,\u6e20\u9053\u5546\u6237\u53f7,\u6e20\u9053\u540d\u79f0,\u63a5\u5165\u65b9\u8ba2\u5355\u53f7,\u5e73\u53f0\u8ba2\u5355\u53f7,\u4e0a\u6e38\u8ba2\u5355\u53f7,\u4ea4\u6613\u91d1\u989d(\u5143),\u652f\u4ed8\u7c7b\u578b,\u5361\u7c7b\u578b,\u94f6\u884c\u540d\u79f0,\u5361\u53f7,\u6301\u5361\u4eba,\u63a5\u53e3\u7c7b\u578b,\u5e94\u7b54\u7801,\u5e94\u7b54\u7801\u63cf\u8ff0,\u4ea4\u6613\u72b6\u6001,\u652f\u4ed8\u65f6\u95f4");
            } else {
                sBuffer.append("\u4ea4\u6613\u65e5\u671f,\u4ea4\u6613\u65f6\u95f4,\u5546\u6237\u7f16\u53f7,\u5546\u6237\u540d\u79f0,\u6e20\u9053\u5546\u6237\u53f7,\u63a5\u5165\u65b9\u8ba2\u5355\u53f7,\u5e73\u53f0\u8ba2\u5355\u53f7,\u4ea4\u6613\u91d1\u989d(\u5143),\u652f\u4ed8\u7c7b\u578b,\u5361\u7c7b\u578b,\u94f6\u884c\u540d\u79f0,\u5361\u53f7,\u6301\u5361\u4eba,\u5e94\u7b54\u7801,\u5e94\u7b54\u7801\u63cf\u8ff0,\u4ea4\u6613\u72b6\u6001,\u652f\u4ed8\u65f6\u95f4");
            }
            sBuffer.append("\n");
            for (QuickpayQueryBean tradeBean : tradeList) {
                sBuffer.append("'").append(tradeBean.getTime_start().substring(0, 8));
                sBuffer.append(",'").append(tradeBean.getTime_start().substring(8, 14));
                sBuffer.append(",'").append(tradeBean.getGymchtId());
                sBuffer.append(",'").append(tradeBean.getMchtName());
                sBuffer.append(",'").append(tradeBean.getMcht_id());
                if (isManager) {
                    sBuffer.append(",'").append(CommonFunction.transChannel(tradeBean.getChannel_id()));
                }
                sBuffer.append(",'").append(tradeBean.getTradeSn());
                sBuffer.append(",'").append(tradeBean.getOut_trade_no());
                sBuffer.append(",'").append(tradeBean.getTotal_fee());
                sBuffer.append(",'").append(CommonFunction.transTradeSource(tradeBean.getTradeSource()));
                sBuffer.append(",'").append(StringUtil.trans2Str(tradeBean.getCardType()));
                sBuffer.append(",'").append(StringUtil.trans2Str(tradeBean.getBankName()));
                sBuffer.append(",'").append(StringUtil.trans2Str(tradeBean.getCardNo()));
                sBuffer.append(",'").append(StringUtil.trans2Str(tradeBean.getCardHolderName()));
                sBuffer.append(",'").append(StringUtil.trans2Str(tradeBean.getResult_code()));
                sBuffer.append(",\"").append(StringUtil.trans2Str(tradeBean.getMessage())).append("\"");
                sBuffer.append(",'").append(CommonFunction.transTradeState(tradeBean.getTrade_state()));
                sBuffer.append(",'").append(StringUtil.trans2Str(tradeBean.getTime_end()));
                sBuffer.append("\n");
            }
            ByteBuffer buffer = ByteBuffer.wrap(sBuffer.toString().getBytes());
            fileChannel.write(buffer);
            buffer.clear();
            fileChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    private Map buildRequestParam(HttpServletRequest request) {
        Map properties = request.getParameterMap();
        HashMap<String, String> returnMap = new HashMap<String, String>();
        Iterator entries = properties.entrySet().iterator();
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            value = null;
            if (valueObj == null) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                int i = 0;
                while (i < values.length) {
                    value = value == null ? (values[i] == null ? "" : values[i]) : String.valueOf(value) + "," + (values[i] == null ? "" : values[i]);
                    ++i;
                }
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }

    private boolean isManager(OprInfo oprInfo) {
        if (oprInfo != null && OprTypeEnum.PARENT.getCode().equals(oprInfo.getOpr_type())) {
            return true;
        }
        return false;
    }
}
