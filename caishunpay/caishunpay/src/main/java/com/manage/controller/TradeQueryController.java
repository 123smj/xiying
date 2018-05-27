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
 *  org.springframework.web.servlet.ModelAndView
 */
package com.manage.controller;

import com.common.model.Response;
import com.gy.system.Environment;
import com.gy.system.SysParamUtil;
import com.gy.util.AppHttp;
import com.gy.util.CommonFunction;
import com.gy.util.DateUtil;
import com.gy.util.StringUtil;
import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.manage.bean.TradeMchtFile;
import com.manage.service.TradeMchtFileService;
import com.trade.bean.BankCardPay;
import com.trade.bean.QuickpayQueryBean;
import com.trade.bean.ThirdPartyPayDetail;
import com.trade.bean.TradeDetailBean;
import com.trade.dao.BankCardPayDao;
import com.trade.dao.ThirdPartyPayDetailDao;
import com.trade.enums.OprTypeEnum;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeStateEnum;
import com.trade.service.MerchantInfService;
import com.trade.service.QuickpayService;
import com.trade.service.impl.NotifyProcessingService;
import com.trade.service.impl.ThirdPartyPayDispatcherService;
import com.trade.util.JsonUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value={"/manage"})
public class TradeQueryController {
    private static Logger log = Logger.getLogger(TradeQueryController.class);
    @Autowired
    private ThirdPartyPayDetailDao thirdPartyPayDetailDao;
    @Autowired
    private QuickpayService mobaoPayServiceImpl;
    @Autowired
    private NotifyProcessingService notifyProcessingService;
    @Autowired
    private ThirdPartyPayDispatcherService thirdPartyPayDispatcherService;
    @Autowired
    protected BankCardPayDao bankCardPayDao;
    @Autowired
    protected MerchantInfService merchantInfServiceImpl;
    @Autowired
    private TradeMchtFileService tradeMchtFileServiceImpl;

    @RequestMapping(value={"/tradeQuery"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public PageModle<TradeDetailBean> tradeQuery(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
        PageModle<TradeDetailBean> pageModle = null;
        try {
            int pageNum = 1;
            Map param = this.buildRequestParam(request);
            if (!StringUtil.isEmpty((String)param.get("pageNum"))) {
                pageNum = StringUtil.parseInt((String)param.get("pageNum"));
            }
            System.out.println(param);
            pageModle = this.thirdPartyPayDetailDao.listByPage(param, pageNum, 10, oprInfo);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return pageModle;
    }

    @RequestMapping(value={"/reQueryTrade"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public Response<String> reQueryTrade(HttpServletRequest request, String tradeSn) {
        Response<String> response = new Response<String>();
        try {
            this.thirdPartyPayDispatcherService.doOrderQuery(tradeSn);
            response.setCode("00");
            response.setMessage("\u72b6\u6001\u540c\u6b65\u6210\u529f");
        }
        catch (Exception e) {
            log.error("when query", e);
            response.setCode("09");
            response.setMessage("\u72b6\u6001\u540c\u6b65\u5931\u8d25");
        }
        return response;
    }

    @RequestMapping(value={"/reNotifyTrade"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public Response<String> reNotifyTrade(HttpServletRequest request, String outTradeNo) {
        Response<String> response = new Response<String>();
        try {
            ThirdPartyPayDetail thirdPartyPayDetail = this.thirdPartyPayDetailDao.getById(outTradeNo);
            if (thirdPartyPayDetail == null) {
                response.setCode("08");
                response.setMessage("\u672a\u627e\u5230\u5bf9\u5e94\u4ea4\u6613");
                return response;
            }
            this.notifyProcessingService.notifyThirdPartyPay(thirdPartyPayDetail);
            response.setCode("00");
            response.setMessage("\u901a\u77e5\u8865\u53d1\u6210\u529f");
        }
        catch (Exception e) {
            log.error("when reNotifyTrade : ", e);
            response.setCode("09");
            response.setMessage("\u901a\u77e5\u8865\u53d1\u5931\u8d25");
        }
        return response;
    }

    @RequestMapping(value={"/quickPayQuery"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public PageModle<QuickpayQueryBean> quickPayQuery(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
        PageModle<QuickpayQueryBean> pageModle = null;
        try {
            int pageNum = 1;
            Map param = this.buildRequestParam(request);
            if (!StringUtil.isEmpty((String)param.get("pageNum"))) {
                pageNum = StringUtil.parseInt((String)param.get("pageNum"));
            }
            System.out.println(param);
            pageModle = this.mobaoPayServiceImpl.listByPage(param, pageNum, 10);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return pageModle;
    }

    @RequestMapping(value={"/quickPayExport"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public String quickPayExport(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
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
            result = this.generateQuickpayFile(tradeList, String.valueOf(rootPath) + "/" + fileName, this.isManager(oprInfo));
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return result;
    }

    @RequestMapping(value={"/daikouCheckDetail"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public ModelAndView daikouCheckDetail(HttpServletRequest request, String outOrderNo) {
        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
        ModelAndView view = new ModelAndView("/manage/daikou_check_detail");
        try {
            BankCardPay BankCardPay = this.bankCardPayDao.getById(outOrderNo);
            List<TradeMchtFile> orderFiles = this.tradeMchtFileServiceImpl.getMchtFiles(BankCardPay.getTradeSn());
            request.setAttribute("quickpaybean", (Object) BankCardPay);
            request.setAttribute("orderFiles", orderFiles);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
        }
        return view;
    }

    @RequestMapping(value={"/checkDaikou"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public String checkDaikou(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
        String orderNo = request.getParameter("orderNo");
        String mchtNo = request.getParameter("mchtNo");
        BankCardPay BankCardPay = this.bankCardPayDao.getByTradesn(orderNo, mchtNo);
        String sendPackge = BankCardPay.getSend_packge();
        String result = AppHttp.appadd("http://" + Environment.YINSHENGBAO_PAY_URL.getBaseUrl() + "/unspay-external/delegateCollect/collect", sendPackge);
        System.out.println("result:" + result);
        Map jsonResult = new HashMap();
        try {
            jsonResult = (Map)JsonUtil.parseJson(result);
        }
        catch (Exception e) {
            String tmp = result.split(",")[0];
            tmp = tmp.substring(tmp.indexOf(":") + 1);
            tmp = tmp.replaceAll("\"", "");
            jsonResult.put("result_code", tmp);
            jsonResult.put("result_msg", result);
        }
        String res = "";
        if (jsonResult != null) {
            if ("0000".equals(jsonResult.get("result_code"))) {
                BankCardPay.setTrade_state(TradeStateEnum.PRESUCCESS.getCode());
                BankCardPay.setTime_end(DateUtil.getCurrTime());
                log.info((Object)("\u94f6\u751f\u5b9d\u59d4\u6258\u4ee3\u6263\u4ea4\u6613\u6210\u529f" + BankCardPay.getTradeSn()));
                res = ResponseEnum.SUCCESS.getCode();
            } else {
                BankCardPay.setTrade_state(TradeStateEnum.PAYERROR.getCode());
            }
            BankCardPay.setResult_code((String)jsonResult.get("result_code"));
            BankCardPay.setMessage((String)jsonResult.get("result_msg"));
            res = String.valueOf((String)jsonResult.get("result_code")) + "--" + (String)jsonResult.get("result_msg");
        } else {
            log.error((Object)("\u94f6\u751f\u5b9d\u59d4\u6258\u4ee3\u6263\u5931\u8d25:" + result));
            BankCardPay.setMessage(ResponseEnum.BACK_EXCEPTION.getCode());
            BankCardPay.setMessage(ResponseEnum.BACK_EXCEPTION.getMemo());
            res = String.valueOf(ResponseEnum.BACK_EXCEPTION.getCode()) + "--" + ResponseEnum.BACK_EXCEPTION.getMemo();
        }
        this.merchantInfServiceImpl.updateQuickpayBean(BankCardPay);
        return res;
    }

    @RequestMapping(value={"/refuseDaikou"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public String refuseDaikou(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
        String orderNo = request.getParameter("orderNo");
        String mchtNo = request.getParameter("mchtNo");
        String remark = request.getParameter("remark");
        BankCardPay BankCardPay = this.bankCardPayDao.getByTradesn(orderNo, mchtNo);
        BankCardPay.setTrade_state(TradeStateEnum.REFUSE.getCode());
        BankCardPay.setOp_user_id(oprInfo.getOpr_id());
        BankCardPay.setMessage(remark);
        this.merchantInfServiceImpl.updateQuickpayBean(BankCardPay);
        return ResponseEnum.SUCCESS.getCode();
    }

    @RequestMapping(value={"/tradeExport"}, produces={"application/json; charset=utf-8"})
    @ResponseBody
    public Response<String> tradeExport(HttpServletRequest request) {
        OprInfo oprInfo = (OprInfo)request.getSession().getAttribute("oprInfo");
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
            int acountTrade = this.thirdPartyPayDetailDao.acount(param, oprInfo);
            if (acountTrade > 500) {
                response.setCode("08");
                response.setMessage("\u6570\u636e\u8d85\u8fc7500\u6761\uff0c\u8bf7\u5206\u6279\u4e0b\u8f7d");
                return response;
            }
            tradeList = this.thirdPartyPayDetailDao.listAll(param, oprInfo);
            String path = this.generateTradeFile(tradeList, String.valueOf(rootPath) + "/" + fileName, this.isManager(oprInfo));
            response.setMessage("\u6210\u529f");
            response.setCode("00");
            response.setData(path);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error((Object)("exception-----opr_id:" + oprInfo.getOpr_id() + e));
            response.setCode("09");
            response.setMessage("\u64cd\u4f5c\u5931\u8d25");
            return response;
        }
        return response;
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
            sBuffer.append("").append(tradeBean.getTime_start().substring(0, 8));
            sBuffer.append(",").append(tradeBean.getTime_start().substring(8, 14));
            sBuffer.append(",").append(tradeBean.getGymchtId());
            sBuffer.append(",").append(tradeBean.getMchtName());
            sBuffer.append(",").append(tradeBean.getMcht_id());
            if (isManager) {
                if ("guangda".equals(tradeBean.getChannel_id())) {
                    if (tradeBean.getMcht_id().startsWith("1015")) {
                        sBuffer.append(",").append("\u5174\u4e1a\u94f6\u884c");
                    } else if (tradeBean.getMcht_id().startsWith("1025")) {
                        sBuffer.append(",").append("\u4e2d\u4fe1\u94f6\u884c");
                    } else if (tradeBean.getMcht_id().startsWith("1035")) {
                        sBuffer.append(",").append("\u6d66\u53d1\u94f6\u884c");
                    } else if (tradeBean.getMcht_id().startsWith("1055")) {
                        sBuffer.append(",").append("\u5149\u5927\u94f6\u884c");
                    } else {
                        sBuffer.append(",").append(CommonFunction.transChannel(tradeBean.getChannel_id()));
                    }
                } else {
                    sBuffer.append(",").append(CommonFunction.transChannel(tradeBean.getChannel_id()));
                }
            }
            sBuffer.append(",").append(tradeBean.getTradeSn());
            sBuffer.append(",").append(tradeBean.getOut_trade_no());
            sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getTransaction_id()));
            sBuffer.append(",").append(tradeBean.getTotal_fee());
            if (isManager) {
                sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getMcht_rate()));
                sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getRate_fee()));
                sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getSettle_fee()));
            }
            sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getService()));
            sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getResult_code()));
            sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getMessage()));
            sBuffer.append(",").append(CommonFunction.transTradeState(tradeBean.getTrade_state()));
            sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getTime_end()));
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

    private String generateQuickpayFile(List<QuickpayQueryBean> tradeList, String filePath, boolean isManager) {
        File tradeFile = new File(filePath);
        try {
            FileChannel fileChannel = new FileOutputStream(tradeFile).getChannel();
            StringBuffer sBuffer = new StringBuffer();
            if (isManager) {
                sBuffer.append("\u4ea4\u6613\u65e5\u671f,\u4ea4\u6613\u65f6\u95f4,\u5546\u6237\u7f16\u53f7,\u5546\u6237\u540d\u79f0,\u6e20\u9053\u5546\u6237\u53f7,\u6e20\u9053\u540d\u79f0,\u63a5\u5165\u65b9\u8ba2\u5355\u53f7,\u5e73\u53f0\u8ba2\u5355\u53f7,\u4e0a\u6e38\u8ba2\u5355\u53f7,\u4ea4\u6613\u91d1\u989d(\u5143),\u8d39\u7387(%),\u624b\u7eed\u8d39(\u5143),\u6e05\u7b97\u91d1\u989d(\u5143),\u652f\u4ed8\u7c7b\u578b,\u5361\u7c7b\u578b,\u94f6\u884c\u540d\u79f0,\u5361\u53f7,\u6301\u5361\u4eba,\u63a5\u53e3\u7c7b\u578b,\u5e94\u7b54\u7801,\u5e94\u7b54\u7801\u63cf\u8ff0,\u4ea4\u6613\u72b6\u6001,\u652f\u4ed8\u65f6\u95f4");
            } else {
                sBuffer.append("\u4ea4\u6613\u65e5\u671f,\u4ea4\u6613\u65f6\u95f4,\u5546\u6237\u7f16\u53f7,\u5546\u6237\u540d\u79f0,\u6e20\u9053\u5546\u6237\u53f7,\u63a5\u5165\u65b9\u8ba2\u5355\u53f7,\u5e73\u53f0\u8ba2\u5355\u53f7,\u4e0a\u6e38\u8ba2\u5355\u53f7,\u4ea4\u6613\u91d1\u989d(\u5143),\u652f\u4ed8\u7c7b\u578b,\u5361\u7c7b\u578b,\u94f6\u884c\u540d\u79f0,\u5361\u53f7,\u6301\u5361\u4eba,\u63a5\u53e3\u7c7b\u578b,\u5e94\u7b54\u7801,\u5e94\u7b54\u7801\u63cf\u8ff0,\u4ea4\u6613\u72b6\u6001,\u652f\u4ed8\u65f6\u95f4");
            }
            sBuffer.append("\n");
            for (QuickpayQueryBean tradeBean : tradeList) {
                sBuffer.append("").append(tradeBean.getTime_start().substring(0, 8));
                sBuffer.append(",").append(tradeBean.getTime_start().substring(8, 14));
                sBuffer.append(",").append(tradeBean.getGymchtId());
                sBuffer.append(",").append(tradeBean.getMchtName());
                sBuffer.append(",").append(tradeBean.getMcht_id());
                if (isManager) {
                    sBuffer.append(",").append(CommonFunction.transChannel(tradeBean.getChannel_id()));
                }
                sBuffer.append(",").append(tradeBean.getTradeSn());
                sBuffer.append(",").append(tradeBean.getOut_trade_no());
                sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getTransaction_id()));
                sBuffer.append(",").append(tradeBean.getTotal_fee());
                if (isManager) {
                    sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getMcht_rate()));
                    sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getRate_fee()));
                    sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getSettle_fee()));
                }
                sBuffer.append(",").append(CommonFunction.transTradeSource(tradeBean.getTradeSource()));
                sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getCardType()));
                sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getBankName()));
                sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getCardNo()));
                sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getCardHolderName()));
                sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getService()));
                sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getResult_code()));
                sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getMessage())).append("\"");
                sBuffer.append(",").append(CommonFunction.transTradeState(tradeBean.getTrade_state()));
                sBuffer.append(",").append(StringUtil.trans2Str(tradeBean.getTime_end()));
                sBuffer.append("\n");
            }
            ByteBuffer buffer = ByteBuffer.wrap(sBuffer.toString().getBytes());
            fileChannel.write(buffer);
            buffer.clear();
            fileChannel.close();
        }
        catch (Exception e) {
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
            Map.Entry entry = (Map.Entry)entries.next();
            name = (String)entry.getKey();
            Object valueObj = entry.getValue();
            value = null;
            if (valueObj == null) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[])valueObj;
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
