/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.ResponseBody
 */
package com.gy2cupe.controller;

import com.alibaba.fastjson.JSONObject;
import com.gy.util.Constants4Return;
import com.gy.util.StringUtil;
import com.gy.util.UUIDGenerator;
import com.gy2cupe.bean.CupeResult;
import com.gy2cupe.service.CupeResultService;
import com.gy2cupe.util.CupeConnect;
import com.gy2cupe.util.CupeUtil;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = {"/cupePay"})
public class CupePayController {
    private static Logger log = Logger.getLogger(CupePayController.class);
    @Autowired
    private CupeResultService cupeResultService;

    @ResponseBody
    @RequestMapping(value = {"/getPayurl.do"}, produces = {"text/plain;charset=utf-8"})
    public String getPayurl(HttpServletRequest request, Model model) {
        String equipCode = request.getParameter("equipCode");
        String orderAmount = request.getParameter("orderAmount");
        String tradeSn = request.getParameter("tradeSn");
        String notifyUrl = request.getParameter("notifyUrl");
        HashMap<String, String> receptParam = new HashMap<String, String>();
        receptParam.put("equipCode", equipCode);
        receptParam.put("orderAmount", orderAmount);
        receptParam.put("tradeSn", tradeSn);
        if (notifyUrl != null) {
            receptParam.put("notifyUrl", notifyUrl);
        }
        if (StringUtil.isEmpty(equipCode) || StringUtil.isEmpty(orderAmount) || StringUtil.isEmpty(tradeSn)) {
            log.info((Object) "\u53c2\u6570\u6709\u8bef");
            return "\u53c2\u6570\u6709\u8bef";
        }
        if (!this.cupeResultService.checkSign(receptParam, request.getParameter("sign"))) {
            log.info((Object) "\u7b7e\u540d\u9519\u8bef");
            return "\u7b7e\u540d\u9519\u8bef";
        }
        log.info((Object) ("\u8bf7\u6c42\u4e8c\u7ef4\u7801begin------tradeSn:" + tradeSn));
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String tradeNo = UUIDGenerator.getUUID();
        String cupeRandom = CupeUtil.getRandom(16);
        HashMap<String, String> reqParam = new HashMap<String, String>();
        reqParam.put("p", "160069");
        reqParam.put("t", timeMillis);
        reqParam.put("r", cupeRandom);
        reqParam.put("n", "http://113.106.95.37:7777/gyprovider/cupeResult/notifyResult.do");
        reqParam.put("p0", "");
        reqParam.put("p1", equipCode);
        reqParam.put("p2", tradeNo);
        reqParam.put("p3", orderAmount);
        CupeResult cupeResult = null;
        try {
            Map<String, Object> payResult = this.cupeResultService.queryPay(tradeSn);
            if (payResult != null) {
                log.info((Object) ("\u7cfb\u7edf\u6d41\u6c34\u91cd\u590d\uff1a" + tradeSn));
                return CupeUtil.buildCupeReturn("50010", Constants4Return.STATUS_TRANSELATE.get("50010"));
            }
            cupeResult = CupeConnect.getPay(reqParam);
            if (cupeResult == null) {
                return CupeUtil.buildCupeReturn("50050", Constants4Return.STATUS_TRANSELATE.get("50050"));
            }
            if ("0".equals(cupeResult.getResult())) {
                reqParam.put("tradeSn", tradeSn);
                reqParam.put("notifyUrl", notifyUrl);
                int result = this.cupeResultService.savePay(reqParam);
                if (result != 1) {
                    return CupeUtil.buildCupeReturn("80000", Constants4Return.STATUS_TRANSELATE.get("80000"));
                }
                return CupeUtil.buildCupeReturn("00000", cupeResult.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CupeUtil.buildCupeReturn("50000", Constants4Return.STATUS_TRANSELATE.get("50000"));
        }
        log.info((Object) ("\u8bf7\u6c42\u4e8c\u7ef4\u7801end------\u8fd4\u56de:" + cupeResult.getBody()));
        return CupeUtil.buildCupeReturn(cupeResult.getResult(), cupeResult.getBody());
    }

    @ResponseBody
    @RequestMapping(value = {"/queryPay.do"}, produces = {"text/plain;charset=utf-8"})
    public String queryPay(HttpServletRequest request, Model model) {
        String equipCode;
        String p2;
        String p4;
        String p3;
        String p5;
        String s;
        String orderAmount;
        String tradeSnOri;
        String p0;
        String p1;
        block9:
        {
            tradeSnOri = request.getParameter("tradeSnOri");
            if (StringUtil.isEmpty(tradeSnOri)) {
                log.info((Object) "\u53c2\u6570\u6709\u8bef");
                return "\u53c2\u6570\u6709\u8bef";
            }
            HashMap<String, String> receptParam = new HashMap<String, String>();
            receptParam.put("tradeSnOri", tradeSnOri);
            if (!this.cupeResultService.checkSign(receptParam, request.getParameter("sign"))) {
                log.info((Object) "\u7b7e\u540d\u9519\u8bef");
                return "\u7b7e\u540d\u9519\u8bef";
            }
            log.info((Object) ("\u652f\u4ed8\u67e5\u8be2begin------\u8bf7\u6c42\u6d41\u6c34\u53f7\uff1a" + tradeSnOri));
            CupeResult cupeResult = null;
            Map<String, Object> payResult = this.cupeResultService.queryPay(tradeSnOri);
            log.info((Object) ("\u67e5\u8be2\u7ed3\u679c:" + payResult));
            if (payResult == null) {
                log.info((Object) ("\u4ea4\u6613\u4e0d\u5b58\u5728\uff1a" + tradeSnOri));
                return CupeUtil.buildCupeReturn("50005", Constants4Return.STATUS_TRANSELATE.get("50005"));
            }
            equipCode = CupeUtil.toString(payResult.get("EQUIP_CODE"));
            String tradeNo = CupeUtil.toString(payResult.get("TRADE_NO"));
            orderAmount = CupeUtil.toString(payResult.get("ORDER_AMOUNT"));
            String payStatus = CupeUtil.toString(payResult.get("PAY_STATUS"));
            p0 = null;
            p1 = null;
            p2 = null;
            p3 = null;
            p4 = null;
            p5 = null;
            String p6 = null;
            s = null;
            if ("0".equals(payStatus)) {
                String cupeRandom = CupeUtil.getRandom(16);
                String timeMillis = String.valueOf(System.currentTimeMillis());
                HashMap<String, String> reqParam = new HashMap<String, String>();
                reqParam.put("p", "160069");
                reqParam.put("t", timeMillis);
                reqParam.put("r", cupeRandom);
                reqParam.put("p0", "");
                reqParam.put("p1", equipCode);
                reqParam.put("p2", tradeNo);
                try {
                    cupeResult = CupeConnect.queryPay(reqParam);
                    if (cupeResult == null) {
                        return CupeUtil.buildCupeReturn("50050", Constants4Return.STATUS_TRANSELATE.get("50050"));
                    }
                    if ("0".equals(cupeResult.getResult())) {
                        String rsBody = cupeResult.getBody().toString();
                        JSONObject jsonObject = JSONObject.parseObject((String) rsBody);
                        p0 = jsonObject.getString("p0");
                        p1 = jsonObject.getString("p1");
                        p2 = jsonObject.getString("p2");
                        p3 = jsonObject.getString("p3");
                        p4 = jsonObject.getString("p4");
                        p5 = jsonObject.getString("p5");
                        p6 = jsonObject.getString("p6");
                        s = jsonObject.getString("s");
                        HashMap<String, String> paramMap = new HashMap<String, String>();
                        paramMap.put("p0", p0);
                        paramMap.put("p1", p1);
                        paramMap.put("p2", p2);
                        paramMap.put("p3", p3);
                        paramMap.put("p4", p4);
                        paramMap.put("p5", p5);
                        paramMap.put("p6", p6);
                        paramMap.put("s", s);
                        paramMap.put("tradeSnOri", tradeSnOri);
                        int result = this.cupeResultService.saveQueryResult(paramMap);
                        if (result != 1) {
                            return CupeUtil.buildCupeReturn("80000", Constants4Return.STATUS_TRANSELATE.get("80000"));
                        }
                        break block9;
                    }
                    return CupeUtil.buildCupeReturn(cupeResult.getResult(), cupeResult.getBody());
                } catch (Exception e) {
                    e.printStackTrace();
                    return CupeUtil.buildCupeReturn("50000", Constants4Return.STATUS_TRANSELATE.get("50000"));
                }
            }
            p0 = CupeUtil.toString(payResult.get("CHANNEL_TRADE_SN"));
            p1 = CupeUtil.toString(payResult.get("PAY_APP"));
            p2 = CupeUtil.toString(payResult.get("PAY_TIME"));
            p3 = CupeUtil.toString(payResult.get("PAY_AMOUNT"));
            p4 = CupeUtil.toString(payResult.get("DISCOUNT_AMOUNT"));
            p5 = CupeUtil.toString(payResult.get("APP_ORDER_NO"));
            p6 = CupeUtil.toString(payResult.get("REFUND_AMOUNT"));
            s = CupeUtil.toString(payResult.get("PAY_STATUS"));
        }
        HashMap<String, String> rtmap = new HashMap<String, String>();
        rtmap.put("tradeSnOri", tradeSnOri);
        rtmap.put("equipCode", equipCode);
        rtmap.put("orderAmount", orderAmount);
        rtmap.put("payAmount", p3);
        rtmap.put("discountAmount", p4);
        rtmap.put("payStatus", s);
        rtmap.put("channelTradeSn", p0);
        rtmap.put("payError", String.valueOf(s) + "-" + CupeUtil.ORDER_STATUS.get(s));
        rtmap.put("payApp", p1);
        rtmap.put("appOrderNo", p5);
        rtmap.put("payTime", p2);
        String queryResult = CupeUtil.buildCupeReturn("00000", rtmap);
        log.info((Object) ("\u8fd4\u56de\u7ed3\u679c:" + queryResult));
        log.info((Object) "----\u652f\u4ed8\u67e5\u8be2end------");
        return queryResult;
    }
}
