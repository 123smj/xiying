///*
// * Decompiled with CFR 0_124.
// *
// * Could not load the following classes:
// *  javax.servlet.ServletInputStream
// *  javax.servlet.http.HttpServletRequest
// *  org.apache.log4j.Logger
// *  org.springframework.beans.factory.annotation.Autowired
// *  org.springframework.stereotype.Controller
// *  org.springframework.web.bind.annotation.RequestMapping
// *  org.springframework.web.bind.annotation.ResponseBody
// *  org.springframework.web.servlet.ModelAndView
// */
//package com.trade.controller;
//
//import com.gy.system.SysParamUtil;
//import com.gy.util.ContextUtil;
//import com.gy.util.Dom4jUtil;
//import com.gy.util.StringUtil;
//import com.trade.bean.NativeNotifyResultBean;
//import com.trade.bean.own.QrcodeChannelInf;
//import com.trade.dao.QrcodeMchtInfoDao;
//import com.trade.enums.ChannelInfoEnum;
//import com.trade.enums.ChannelStatusEnum;
//import com.trade.service.CupeService;
//import com.trade.service.DaydayPayService;
//import com.trade.service.HuikaService;
//import com.trade.service.MinshengService;
//import com.trade.service.NetpayService;
//import com.trade.service.QrcodeMchtInfoService;
//import com.trade.service.QuickpayService;
//import com.trade.service.TradeService;
//import com.trade.service.WxpayService;
//import com.trade.util.GuangdaUtil;
//import com.trade.util.JsonUtil;
//import com.trade.util.RSAUtils;
//import com.trade.util.SM2Utils;
//import com.trade.util.StreamUtil;
//import java.io.InputStream;
//import java.io.PrintStream;
//import java.net.URLDecoder;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import javax.servlet.ServletInputStream;
//import javax.servlet.http.HttpServletRequest;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//
//@Controller
//@RequestMapping(value={"/notify"})
//public class WxNativeNotifyController {
//    private static Logger log = Logger.getLogger(WxNativeNotifyController.class);
//    @Autowired
//    private WxpayService wxpayServiceImpl;
//    @Autowired
//    private DaydayPayService daydayPayServiceImpl;
//    @Autowired
//    private HuikaService huikaServiceImpl;
//    @Autowired
//    private TradeService tfbServiceImpl;
//    @Autowired
//    private MinshengService minshengServiceImpl;
//    @Autowired
//    private CupeService cupeServiceImpl;
//    @Autowired
//    private TradeService ebusiServiceImpl;
//    @Autowired
//    private TradeService wljrServiceImpl;
//    @Autowired
//    private TradeService helibaoServiceImpl;
//    @Autowired
//    private TradeService hfbankServiceImpl;
//    @Autowired
//    private TradeService qiyepayServiceImpl;
//    @Autowired
//    private QrcodeMchtInfoDao qrcodeMchtInfoDaoImpl;
//    @Autowired
//    private QuickpayService helibaoQuickServiceImpl;
//    @Autowired
//    private QrcodeMchtInfoService qrcodeMchtInfoServiceImpl;
//    private NetpayService tfbNetpayServiceImpl = (NetpayService)ContextUtil.getBean("tfbServiceImpl");
//
//    @ResponseBody
//    @RequestMapping(value={"/notifyNativePay.do"})
//    public String notifyNativePay(HttpServletRequest request) {
//        String response;
//        NativeNotifyResultBean nativeNotifyBean;
//        block3 : {
//            response = null;
//            String requestXml = StreamUtil.getString((InputStream)request.getInputStream(), "UTF-8");
//            nativeNotifyBean = new NativeNotifyResultBean();
//            System.out.println("requestXml:" + requestXml);
//            nativeNotifyBean = Dom4jUtil.parseXml2Object(requestXml, nativeNotifyBean);
//            log.info((Object)("\u4ea4\u6613\u7ed3\u679c\u901a\u77e5\uff1a" + nativeNotifyBean.getOut_trade_no()));
//            if (nativeNotifyBean.getOut_trade_no() != null) break block3;
//            return "\u8ba2\u5355\u53f7\u4e3a\u7a7a";
//        }
//        try {
//            response = this.wxpayServiceImpl.saveResultNotify(nativeNotifyBean);
//        }
//        catch (Exception e) {
//            response = "fail";
//            e.printStackTrace();
//        }
//        log.info((Object)("\u5e94\u7b54\u56de\u8c03\uff1a" + response));
//        return response;
//    }
//
//    @ResponseBody
//    @RequestMapping(value={"/notifyDaydayPay.do"})
//    public String notifyDaydayPay(HttpServletRequest request) {
//        String response = null;
//        HashMap<String, String> replyNotify = new HashMap<String, String>();
//        try {
//            String requestJson = StreamUtil.getString((InputStream)request.getInputStream(), "UTF-8");
//            System.out.println("requestJson:" + requestJson);
//            Map resultMap = (Map)JsonUtil.parseJson(requestJson);
//            log.info((Object)("\u4ea4\u6613\u7ed3\u679c\u901a\u77e5\uff1a" + requestJson));
//            if (resultMap == null) {
//                replyNotify.put("state", "\u901a\u77e5\u4fe1\u606f\u4e3a\u7a7a");
//                return JsonUtil.buildJson4Map(replyNotify);
//            }
//            response = this.daydayPayServiceImpl.saveResultNotify(resultMap);
//        }
//        catch (Exception e) {
//            response = replyNotify.put("state", "fail");
//            e.printStackTrace();
//        }
//        log.info((Object)("\u5e94\u7b54\u56de\u8c03\uff1a" + response));
//        return response;
//    }
//
//    @ResponseBody
//    @RequestMapping(value={"/notifyHuika.do"})
//    public String notifyHuika(HttpServletRequest request) {
//        String response = null;
//        HashMap<String, String> replyNotify = new HashMap<String, String>();
//        try {
//            String requestJson = StreamUtil.getString((InputStream)request.getInputStream(), "UTF-8");
//            System.out.println("requestJson:" + requestJson);
//            Map resultMap = (Map)JsonUtil.parseJson(requestJson);
//            log.info((Object)("\u4ea4\u6613\u7ed3\u679c\u901a\u77e5\uff1a" + resultMap));
//            if (resultMap == null) {
//                replyNotify.put("state", "\u901a\u77e5\u4fe1\u606f\u4e3a\u7a7a");
//                return JsonUtil.buildJson4Map(replyNotify);
//            }
//            response = this.huikaServiceImpl.saveResultNotify(resultMap);
//        }
//        catch (Exception e) {
//            response = replyNotify.put("state", "fail");
//            e.printStackTrace();
//        }
//        log.info((Object)("\u5e94\u7b54\u56de\u8c03\uff1a" + response));
//        return response;
//    }
//
//    @ResponseBody
//    @RequestMapping(value={"/notifyTfb.do"})
//    public String notifyTfb(HttpServletRequest request) {
//        String response;
//        Map<String, String> resultMap;
//        block3 : {
//            response = null;
//            String requestValue = StreamUtil.getString((InputStream)request.getInputStream(), "GBK");
//            System.out.println("requestValue:" + requestValue);
//            resultMap = GuangdaUtil.httpParam2Map(requestValue);
//            log.info((Object)("\u4ea4\u6613\u7ed3\u679c\u901a\u77e5\uff1a" + resultMap));
//            if (resultMap != null) break block3;
//            return "fail";
//        }
//        try {
//            response = this.tfbServiceImpl.saveResultNotify(resultMap);
//        }
//        catch (Exception e) {
//            response = "fail";
//            e.printStackTrace();
//        }
//        log.info((Object)("\u5e94\u7b54\u56de\u8c03\uff1a" + response));
//        return response;
//    }
//
//    @ResponseBody
//    @RequestMapping(value={"/notifyMinsheng.do"})
//    public String notifyMinsheng(HttpServletRequest request) {
//        String response;
//        Map<String, Object> returnMap;
//        block5 : {
//            response = null;
//            String requestValue = StreamUtil.getString((InputStream)request.getInputStream(), "UTF-8");
//            System.out.println("requestValue:" + requestValue);
//            returnMap = JsonUtil.gsonParseJson(requestValue);
//            if (returnMap != null) break block5;
//            return "fail";
//        }
//        try {
//            String resultJson = SM2Utils.dncrypt(StringUtil.trans2Str(returnMap.get("context")), SysParamUtil.getParam("cust0001_sm2"), new String[0]);
//            log.info((Object)("\u4ea4\u6613\u7ed3\u679c\u901a\u77e5\uff1a" + resultJson));
//            Map resultMap = (Map)JsonUtil.parseJson(resultJson);
//            String body = StringUtil.trans2Str(resultMap.get("body"));
//            String backSign = StringUtil.trans2Str(resultMap.get("sign"));
//            boolean backSignCheck = SM2Utils.signCheck(body, backSign, SysParamUtil.getParam("cust0001_cer"), new String[0]);
//            if (backSignCheck) {
//                Map resultData = (Map)JsonUtil.parseJson(body);
//                response = this.minshengServiceImpl.saveResultNotify(resultData);
//            } else {
//                response = "sign_error";
//            }
//        }
//        catch (Exception e) {
//            response = "fail";
//            e.printStackTrace();
//        }
//        log.info((Object)("\u5e94\u7b54\u56de\u8c03\uff1a" + response));
//        return response;
//    }
//
//    @ResponseBody
//    @RequestMapping(value={"/notifyCupe.do"})
//    public String notifyCupe(HttpServletRequest request) {
//        String response = null;
//        try {
//            HashMap<String, String> resultMap = new HashMap<String, String>();
//            resultMap.put("c", request.getParameter("c"));
//            resultMap.put("t", request.getParameter("t"));
//            resultMap.put("r", request.getParameter("r"));
//            resultMap.put("p0", request.getParameter("p0"));
//            resultMap.put("p1", request.getParameter("p1"));
//            resultMap.put("p2", request.getParameter("p2"));
//            resultMap.put("p3", request.getParameter("p3"));
//            resultMap.put("p4", request.getParameter("p4"));
//            resultMap.put("p5", request.getParameter("p5"));
//            resultMap.put("p6", request.getParameter("p6"));
//            resultMap.put("p7", request.getParameter("p7"));
//            log.info((Object)("\u4ea4\u6613\u7ed3\u679c\u901a\u77e5\uff1a" + resultMap));
//            response = this.cupeServiceImpl.saveResultNotify(resultMap);
//        }
//        catch (Exception e) {
//            response = "fail";
//            e.printStackTrace();
//        }
//        log.info((Object)("\u5e94\u7b54\u56de\u8c03\uff1a" + response));
//        return response;
//    }
//
//    @ResponseBody
//    @RequestMapping(value={"/notifyEbusi.do"})
//    public String notifyEbusi(HttpServletRequest request) {
//        String response = null;
//        try {
//            HashMap<String, String> resultMap = new HashMap<String, String>();
//            resultMap.put("orderNum", request.getParameter("orderNum"));
//            resultMap.put("pl_orderNum", request.getParameter("pl_orderNum"));
//            resultMap.put("pl_payState", request.getParameter("pl_payState"));
//            resultMap.put("pl_payMessage", request.getParameter("pl_payMessage"));
//            resultMap.put("groupId", request.getParameter("groupId"));
//            resultMap.put("service", request.getParameter("service"));
//            resultMap.put("sign", request.getParameter("sign"));
//            resultMap.put("signType", request.getParameter("signType"));
//            resultMap.put("datetime", request.getParameter("datetime"));
//            log.info((Object)("\u4ea4\u6613\u7ed3\u679c\u901a\u77e5\uff1a" + resultMap));
//            response = this.ebusiServiceImpl.saveResultNotify(resultMap);
//        }
//        catch (Exception e) {
//            response = "fail";
//            e.printStackTrace();
//        }
//        log.info((Object)("\u5e94\u7b54\u56de\u8c03\uff1a" + response));
//        return response;
//    }
//
//    @ResponseBody
//    @RequestMapping(value={"/notifyWljr.do"})
//    public String notifyWljr(HttpServletRequest request) {
//        String response;
//        Map returnMap;
//        String requestValue;
//        block3 : {
//            response = null;
//            requestValue = StreamUtil.getString((InputStream)request.getInputStream(), "UTF-8");
//            returnMap = (Map)JsonUtil.parseJson(requestValue);
//            if (returnMap != null) break block3;
//            return "fail";
//        }
//        try {
//            log.info((Object)("\u4ea4\u6613\u7ed3\u679c\u901a\u77e5\uff1a" + requestValue));
//            response = this.wljrServiceImpl.saveResultNotify(returnMap);
//        }
//        catch (Exception e) {
//            response = "fail";
//            e.printStackTrace();
//        }
//        log.info((Object)("\u5e94\u7b54\u56de\u8c03\uff1a" + response));
//        return response;
//    }
//
//    @ResponseBody
//    @RequestMapping(value={"/wzNotify.do"})
//    public String wzNotify(HttpServletRequest request) {
//        String response;
//        Map returnMap;
//        String requestValue;
//        block3 : {
//            response = null;
//            requestValue = StreamUtil.getString((InputStream)request.getInputStream(), "UTF-8");
//            returnMap = (Map)JsonUtil.parseJson(requestValue);
//            if (returnMap != null) break block3;
//            return "fail";
//        }
//        try {
//            log.info((Object)("\u4ea4\u6613\u7ed3\u679c\u901a\u77e5\uff1a" + requestValue));
//            response = this.wljrServiceImpl.saveResultNotify(returnMap);
//        }
//        catch (Exception e) {
//            response = "fail";
//            e.printStackTrace();
//        }
//        log.info((Object)("\u5e94\u7b54\u56de\u8c03\uff1a" + response));
//        return response;
//    }
//
//    @ResponseBody
//    @RequestMapping(value={"/notifyHelibao.do"})
//    public String notifyHelibao(HttpServletRequest request) {
//        String response;
//        String requestValue;
//        block3 : {
//            response = null;
//            requestValue = StreamUtil.getString((InputStream)request.getInputStream(), "UTF-8");
//            if (requestValue != null) break block3;
//            return "fail";
//        }
//        try {
//            log.info((Object)("\u4ea4\u6613\u7ed3\u679c\u901a\u77e5\uff1a" + requestValue));
//            Map<String, String> resultMap = GuangdaUtil.httpParam2Map(requestValue);
//            response = this.helibaoServiceImpl.saveResultNotify(resultMap);
//        }
//        catch (Exception e) {
//            response = "fail";
//            e.printStackTrace();
//        }
//        log.info((Object)("\u5e94\u7b54\u56de\u8c03\uff1a" + response));
//        return response;
//    }
//
//    @ResponseBody
//    @RequestMapping(value={"/notifyHfbank.do"})
//    public String notifyHfbank(HttpServletRequest request) {
//        String response;
//        String requestValue;
//        block3 : {
//            response = null;
//            requestValue = StreamUtil.getString((InputStream)request.getInputStream(), "UTF-8");
//            if (requestValue != null) break block3;
//            return "fail";
//        }
//        try {
//            log.info((Object)("\u6052\u4e30\u4ea4\u6613\u7ed3\u679c\u901a\u77e5\uff1a" + requestValue));
//            Map<String, String> resultMap = GuangdaUtil.httpParam2Map(requestValue);
//            response = this.hfbankServiceImpl.saveResultNotify(resultMap);
//        }
//        catch (Exception e) {
//            response = "fail";
//            e.printStackTrace();
//        }
//        log.info((Object)("\u5e94\u7b54\u56de\u8c03\uff1a" + response));
//        return response;
//    }
//
//    @ResponseBody
//    @RequestMapping(value={"/notifyHfbankVerify.do"})
//    public String notifyHfbankVerify(HttpServletRequest request) {
//        String response;
//        String requestValue;
//        block5 : {
//            response = null;
//            requestValue = StreamUtil.getString((InputStream)request.getInputStream(), "UTF-8");
//            if (!StringUtil.isEmpty(requestValue)) break block5;
//            return "fail";
//        }
//        try {
//            log.info((Object)("\u6052\u4e30\u9a8c\u5361\u7ed3\u679c\u901a\u77e5\uff1a" + requestValue));
//            Map<String, String> resultMap = GuangdaUtil.httpParam2Map(requestValue);
//            if ("000000".equals(resultMap.get("respCode"))) {
//                QrcodeChannelInf channelInf = this.qrcodeMchtInfoDaoImpl.getChannelInf(ChannelInfoEnum.hfbank.getCode(), resultMap.get("account"));
//                channelInf.setStatus(ChannelStatusEnum.NORMAL.getCode());
//                this.qrcodeMchtInfoDaoImpl.updateChannelInf(channelInf);
//            } else {
//                log.error((Object)("\u6052\u4e30\u9a8c\u5361\u5931\u8d25\u901a\u77e5\uff1a" + requestValue));
//            }
//        }
//        catch (Exception e) {
//            response = "fail";
//            e.printStackTrace();
//        }
//        log.info((Object)("\u5e94\u7b54\u56de\u8c03\uff1a" + response));
//        return response;
//    }
//
//    @ResponseBody
//    @RequestMapping(value={"/notifyQiyepay.do"})
//    public String notifyQiyepay(HttpServletRequest request) {
//        String response;
//        String requestValue;
//        block3 : {
//            response = null;
//            requestValue = StreamUtil.getString((InputStream)request.getInputStream(), "UTF-8");
//            if (requestValue != null) break block3;
//            return "fail";
//        }
//        try {
//            log.info((Object)("\u797a\u4e1a\u652f\u4ed8\u4ea4\u6613\u7ed3\u679c\u901a\u77e5\uff1a" + requestValue));
//            Map<String, String> resultMap = GuangdaUtil.httpParam2Map(requestValue);
//            response = this.qiyepayServiceImpl.saveResultNotify(resultMap);
//        }
//        catch (Exception e) {
//            response = "fail";
//            e.printStackTrace();
//        }
//        log.info((Object)("\u5e94\u7b54\u56de\u8c03\uff1a" + response));
//        return response;
//    }
//
//    @ResponseBody
//    @RequestMapping(value={"/notifyHelibaoQuick.do"})
//    public String notifyHelibaoQuick(HttpServletRequest request) {
//        String response;
//        LinkedHashMap<String, String> resultMap;
//        block6 : {
//            String[] sortedKeys;
//            response = null;
//            resultMap = new LinkedHashMap<String, String>();
//            String[] arrstring = sortedKeys = new String[]{"rt1_bizType", "rt2_retCode", "rt3_retMsg", "rt4_customerNumber", "rt5_orderId", "rt6_serialNumber", "rt7_completeDate", "rt8_orderAmount", "rt9_orderStatus", "rt10_bindId", "rt11_bankId", "rt12_onlineCardType", "rt13_cardAfterFour", "rt14_userId", "sign"};
//            int n = arrstring.length;
//            int n2 = 0;
//            while (n2 < n) {
//                String key = arrstring[n2];
//                if ("rt3_retMsg".equals(key) || "rt7_completeDate".equals(key)) {
//                    resultMap.put(key, URLDecoder.decode(request.getParameter(key), "UTF-8"));
//                } else {
//                    resultMap.put(key, request.getParameter(key));
//                }
//                ++n2;
//            }
//            if (resultMap.size() != 0) break block6;
//            return "fail";
//        }
//        try {
//            log.info((Object)("\u4ea4\u6613\u7ed3\u679c\u901a\u77e5\uff1a" + resultMap));
//            response = this.helibaoQuickServiceImpl.saveResultNotify(resultMap);
//        }
//        catch (Exception e) {
//            response = "fail";
//            e.printStackTrace();
//        }
//        log.info((Object)("\u5e94\u7b54\u56de\u8c03\uff1a" + response));
//        return response;
//    }
//
//    @ResponseBody
//    @RequestMapping(value={"/notifyTfbNetpay.do"})
//    public String notifyTfbNetpay(HttpServletRequest request) {
//        String response = null;
//        try {
//            String retmsg = new String(request.getParameter("retmsg").getBytes("iso8859-1"), "utf-8");
//            String cipher_data = "";
//            if (request.getParameter("cipher_data") != null) {
//                cipher_data = RSAUtils.decryptResponseData(request.getParameter("cipher_data"), new String[0]);
//                Map<String, String> result = GuangdaUtil.httpParam2Map(cipher_data);
//                response = this.tfbNetpayServiceImpl.saveNetpayNotify(result);
//            } else {
//                response = "empty_notify";
//            }
//            log.info((Object)("\u53c2\u6570\u83b7\u53d6:" + request.getParameter("retcode") + "---" + retmsg + "---" + cipher_data));
//        }
//        catch (Exception e) {
//            response = "fail";
//            e.printStackTrace();
//        }
//        log.info((Object)("\u5e94\u7b54\u56de\u8c03\uff1a" + response));
//        return response;
//    }
//
//    @ResponseBody
//    @RequestMapping(value={"/callBackTfbNetpay.do"})
//    public ModelAndView callBackTfbNetpay(HttpServletRequest request) {
//        ModelAndView view = null;
//        try {
//            log.info((Object)("\u9875\u9762\u8df3\u8f6c\u53c2\u6570\u83b7\u53d6:" + request.getParameter("cipher_data")));
//            log.info((Object)("callBack\u4e1a\u52a1\u53c2\u6570:spid:" + request.getParameter("spid") + ",listid:" + request.getParameter("listid") + ",result:" + request.getParameter("result") + ",pay_type:" + request.getParameter("pay_type")));
//            String cipher_data = "";
//            String callBackUrl = "";
//            if (request.getParameter("cipher_data") != null) {
//                cipher_data = RSAUtils.decryptResponseData(request.getParameter("cipher_data"), new String[0]);
//                Map<String, String> result = GuangdaUtil.httpParam2Map(cipher_data);
//                callBackUrl = this.tfbNetpayServiceImpl.getNetpayCallBack(result);
//                view = new ModelAndView("redirect:" + callBackUrl);
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return view;
//    }
//}
