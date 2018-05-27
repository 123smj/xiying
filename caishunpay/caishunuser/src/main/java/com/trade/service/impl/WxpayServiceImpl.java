/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.trade.service.impl;

import com.account.service.TradeMchtAccountService;
import com.gy.system.Environment;
import com.gy.system.SysParamUtil;
import com.gy.util.DateUtil;
import com.gy.util.Dom4jUtil;
import com.gy.util.HttpUtility;
import com.gy.util.StringUtil;
import com.gy.util.UUIDGenerator;
import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.trade.bean.NativeNotifyResultBean;
import com.trade.bean.TradeDetailBean;
import com.trade.bean.WxJspayParam;
import com.trade.bean.WxNativeParam;
import com.trade.bean.WxpayScanCode;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.bean.own.TradeParam;
import com.trade.bean.response.ResponseCode;
import com.trade.bean.response.ResponseQuery;
import com.trade.dao.QrcodeMchtInfoDao;
import com.trade.dao.WxNativeDao;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeSourceEnum;
import com.trade.enums.TradeStateEnum;
import com.trade.service.WxpayService;
import com.trade.service.impl.CommonService;
import com.trade.util.BeanUtil;
import com.trade.util.GuangdaUtil;
import com.trade.util.JsonUtil;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WxpayServiceImpl
        extends CommonService
        implements WxpayService {
    @Autowired
    private WxNativeDao wxNativeDao;
    @Autowired
    private QrcodeMchtInfoDao qrcodeMchtInfoDaoImpl;
    private static Logger log = Logger.getLogger(WxpayServiceImpl.class);

    @Override
    public ResponseCode doTrade(TradeParam reqParam, QrcodeChannelInf qrChannelInf) {
        ResponseCode response = new ResponseCode();
        response.setGymchtId(reqParam.getGymchtId());
        if (StringUtil.isNotEmpty(SysParamUtil.getParam("AMOUNT_LIMIT")) && !TradeSourceEnum.ALIPAY.getCode().equals(reqParam.getTradeSource()) && reqParam.getOrderAmount() >= Integer.parseInt(SysParamUtil.getParam("AMOUNT_LIMIT"))) {
            response.setResultCode(ResponseEnum.OUT_AMOUNT_LIMIT.getCode());
            response.setMessage(ResponseEnum.OUT_AMOUNT_LIMIT.getMemo());
            return response;
        }
        if (this.wxNativeDao.getByTradesn(reqParam.getTradeSn(), reqParam.getGymchtId()) != null) {
            response.setResultCode(ResponseEnum.FAIL_ORDER_NO_REPEAT.getCode());
            response.setMessage(ResponseEnum.FAIL_ORDER_NO_REPEAT.getMemo());
            return response;
        }
        WxNativeParam wxNativeParam = new WxNativeParam();
        WxpayScanCode wxpay = new WxpayScanCode();
        String tradeNo = UUIDGenerator.getOrderIdByUUId();
        if (TradeSourceEnum.ALIPAY.getCode().equals(reqParam.getTradeSource())) {
            wxNativeParam.setService("pay.alipay.native");
            wxpay.setTrade_source(TradeSourceEnum.ALIPAY.getCode());
            wxNativeParam.setOp_user_id(qrChannelInf.getChannel_mcht_no());
            wxNativeParam.setProduct_id("\u5546\u54c1ID000001");
        } else if (TradeSourceEnum.QQPAY.getCode().equals(reqParam.getTradeSource())) {
            wxNativeParam.setService("pay.tenpay.native");
            wxpay.setTrade_source(TradeSourceEnum.QQPAY.getCode());
        } else {
            wxNativeParam.setService("pay.weixin.native");
            wxpay.setTrade_source(TradeSourceEnum.WEPAY.getCode());
            wxNativeParam.setOp_user_id(qrChannelInf.getChannel_mcht_no());
            wxNativeParam.setProduct_id("\u5546\u54c1ID000001");
        }
        wxNativeParam.setMch_id(qrChannelInf.getChannel_mcht_no());
        wxNativeParam.setOut_trade_no(tradeNo);
        wxNativeParam.setBody(reqParam.getGoodsName());
        wxNativeParam.setAttach("\u5546\u54c1\u9644\u52a0\u4fe1\u606f");
        wxNativeParam.setTotal_fee(reqParam.getOrderAmount());
        wxNativeParam.setMch_create_ip(reqParam.getRemoteAddr());
        wxNativeParam.setNotify_url(SysParamUtil.getParam("NATIVE_NOTIFY_URL"));
        wxNativeParam.setTime_start(DateUtil.getCurrTime());
        int expir = StringUtil.parseInt(reqParam.getExpirySecond());
        wxNativeParam.setTime_expire(DateUtil.getDateAfterSeconds(expir == 0 ? 3600 : expir, "yyyyMMddHHmmss"));
        wxNativeParam.setNonce_str(StringUtil.getRandom(16));
        wxNativeParam.setSign(GuangdaUtil.getMd5Sign(wxNativeParam, qrChannelInf.getSecret_key()));
        String xml = Dom4jUtil.createXmlStr(wxNativeParam, "xml");
        String resultXml = HttpUtility.postData(Environment.GUANGDA_URL, xml);
        BeanUtil.copyPropertiesNotNull(wxNativeParam, wxpay, null, new String[0]);
        wxpay = Dom4jUtil.parseXml2Object(resultXml, wxpay);
        wxpay.setT0Flag(reqParam.getT0Flag());
        wxpay.setGymchtId(reqParam.getGymchtId());
        wxpay.setGy_notifyUrl(reqParam.getNotifyUrl());
        wxpay.setChannel_id(qrChannelInf.getChannel_id());
        wxpay.setTradeSn(reqParam.getTradeSn());
        if ("0".equals(wxpay.getStatus())) {
            if ("0".equals(wxpay.getResult_code())) {
                response.setResultCode(ResponseEnum.SUCCESS.getCode());
                response.setCode_url(wxpay.getCode_url());
                wxpay.setTrade_state(TradeStateEnum.NOTPAY.getCode());
                wxpay.setResp_code("00");
            } else {
                response.setResultCode(wxpay.getErr_code());
                response.setMessage(wxpay.getErr_msg());
                wxpay.setResp_code(wxpay.getErr_code());
            }
        } else {
            response.setResultCode(wxpay.getStatus());
            response.setMessage(wxpay.getMessage());
            wxpay.setResp_code(wxpay.getStatus());
        }
        this.wxNativeDao.save(wxpay);
        return response;
    }

    @Override
    public ResponseCode doTrade(WxpayScanCode wxpayScanCode) {
        ResponseCode response = new ResponseCode();
        QrcodeChannelInf qrChannelInf = this.qrcodeMchtInfoDaoImpl.getChannelInf(wxpayScanCode.getChannel_id(), wxpayScanCode.getMch_id());
        if (qrChannelInf == null) {
            response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
            response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
            return response;
        }
        WxNativeParam wxNativeParam = new WxNativeParam();
        if (TradeSourceEnum.ALIPAY.getCode().equals(wxpayScanCode.getTrade_source())) {
            wxNativeParam.setService("pay.alipay.native");
            wxNativeParam.setOp_user_id(wxpayScanCode.getMch_id());
            wxNativeParam.setProduct_id("\u5546\u54c1ID000001");
        } else if (TradeSourceEnum.QQPAY.getCode().equals(wxpayScanCode.getTrade_source())) {
            wxNativeParam.setService("pay.tenpay.native");
        } else {
            wxNativeParam.setService("pay.weixin.native");
            wxNativeParam.setOp_user_id(wxpayScanCode.getMch_id());
            wxNativeParam.setProduct_id("\u5546\u54c1ID000001");
        }
        wxNativeParam.setCharset("UTF-8");
        wxNativeParam.setVersion("2.0");
        wxNativeParam.setSign_type("MD5");
        wxNativeParam.setMch_id(wxpayScanCode.getMch_id());
        wxNativeParam.setOut_trade_no(wxpayScanCode.getOut_trade_no());
        wxNativeParam.setBody(wxpayScanCode.getBody());
        wxNativeParam.setAttach("\u5546\u54c1\u9644\u52a0\u4fe1\u606f");
        wxNativeParam.setTotal_fee(wxpayScanCode.getTotal_fee());
        wxNativeParam.setMch_create_ip(wxpayScanCode.getMch_create_ip());
        wxNativeParam.setNotify_url(SysParamUtil.getParam("NATIVE_NOTIFY_URL"));
        wxNativeParam.setTime_start(DateUtil.getCurrTime());
        int expir = 0;
        wxNativeParam.setTime_expire(DateUtil.getDateAfterSeconds(expir == 0 ? 3600 : expir, "yyyyMMddHHmmss"));
        wxNativeParam.setNonce_str(StringUtil.getRandom(16));
        wxNativeParam.setSign(GuangdaUtil.getMd5Sign(wxNativeParam, qrChannelInf.getSecret_key()));
        String xml = Dom4jUtil.createXmlStr(wxNativeParam, "xml");
        String resultXml = HttpUtility.postData(Environment.GUANGDA_URL, xml);
        wxpayScanCode = Dom4jUtil.parseXml2Object(resultXml, wxpayScanCode);
        if ("0".equals(wxpayScanCode.getStatus())) {
            if ("0".equals(wxpayScanCode.getResult_code())) {
                response.setResultCode(ResponseEnum.SUCCESS.getCode());
                response.setCode_url(wxpayScanCode.getCode_url());
                wxpayScanCode.setTrade_state(TradeStateEnum.NOTPAY.getCode());
                wxpayScanCode.setResp_code("00");
            } else {
                response.setResultCode(wxpayScanCode.getErr_code());
                response.setMessage(wxpayScanCode.getErr_msg());
                wxpayScanCode.setResp_code(wxpayScanCode.getErr_code());
            }
        } else {
            response.setResultCode(wxpayScanCode.getStatus());
            response.setMessage(wxpayScanCode.getMessage());
            wxpayScanCode.setResp_code(wxpayScanCode.getStatus());
        }
        wxpayScanCode.setService(wxNativeParam.getService());
        this.wxNativeDao.update(wxpayScanCode);
        return response;
    }

    @Override
    public String saveResultNotify(Map<String, String> resultMap) {
        return null;
    }

    @Override
    public WxpayScanCode queryByTradesn(String tradeSn, String gyMchtId, QrcodeChannelInf qrChannelInf) {
        WxpayScanCode wxpayScanCode = this.wxNativeDao.getByTradesn(tradeSn, gyMchtId);
        return this.queryFromChannel(wxpayScanCode, qrChannelInf);
    }

    @Override
    public WxpayScanCode queryFromChannel(WxpayScanCode wxpayScanCode, QrcodeChannelInf qrChannelInf) {
        if (wxpayScanCode != null && !TradeStateEnum.SUCCESS.getCode().equals(wxpayScanCode.getTrade_state())) {
            WxNativeParam wxQueryParam = new WxNativeParam("unified.trade.query", wxpayScanCode.getMch_id(), wxpayScanCode.getOut_trade_no());
            wxQueryParam.setNonce_str(StringUtil.getRandom(16));
            wxQueryParam.setSign(GuangdaUtil.getMd5Sign(wxQueryParam, qrChannelInf.getSecret_key()));
            String xml = Dom4jUtil.createXmlStr(wxQueryParam, "xml");
            String resultXml = HttpUtility.postData(Environment.GUANGDA_URL, xml);
            wxpayScanCode = Dom4jUtil.parseXml2Object(resultXml, wxpayScanCode);
            this.wxNativeDao.update(wxpayScanCode);
        }
        return wxpayScanCode;
    }

    @Override
    public WxpayScanCode queryTrade(String outTradeNo) {
        return this.wxNativeDao.getById(outTradeNo);
    }

    @Override
    public String saveResultNotify(NativeNotifyResultBean nativeNotifyBean) {
        WxpayScanCode notifyBeanTemp = this.wxNativeDao.getById(nativeNotifyBean.getOut_trade_no());
        if (notifyBeanTemp == null || !String.valueOf(notifyBeanTemp.getTotal_fee()).equals(nativeNotifyBean.getTotal_fee())) {
            return "notexist";
        }
        String notifyBack = "";
        if (!TradeStateEnum.SUCCESS.getCode().equals(notifyBeanTemp.getTrade_state())) {
            BeanUtil.copyPropertiesNotNull(nativeNotifyBean, notifyBeanTemp, null, new String[0]);
            if ("0".equals(notifyBeanTemp.getStatus()) && "0".equals(notifyBeanTemp.getResult_code()) && "0".equals(notifyBeanTemp.getPay_result())) {
                notifyBeanTemp.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                this.tradeMchtAccountService.notifySuccess(notifyBeanTemp);
            }
        }
        if (notifyBeanTemp.getGy_notifyUrl() != null && !"success".equals(notifyBeanTemp.getGynotify_back())) {
            Environment evn = Environment.createEnvironment(notifyBeanTemp.getGy_notifyUrl(), "utf-8", "application/json");
            QrcodeMchtInfo qrcodeMcht = this.qrcodeMchtInfoDaoImpl.getMchtInfo(notifyBeanTemp.getGymchtId());
            ResponseQuery backNotify = this.buildResponseQuery(notifyBeanTemp);
            if ("0".equals(notifyBeanTemp.getStatus()) && "0".equals(notifyBeanTemp.getResult_code()) && "0".equals(notifyBeanTemp.getPay_result())) {
                backNotify.setPay_result("0");
                backNotify.setTimeEnd(nativeNotifyBean.getTime_end());
            } else {
                backNotify.setPay_result(StringUtil.trans2Str(nativeNotifyBean.getPay_info()));
                backNotify.setPay_info(StringUtil.trans2Str(nativeNotifyBean.getPay_info()));
            }
            backNotify.setSign(GuangdaUtil.getMd5Sign(backNotify, qrcodeMcht.getSecretKey()));
            String jsonStr = JsonUtil.buildJson(backNotify);
            log.info((Object) ("\u56de\u8c03\u63a5\u5165\u65b9:" + notifyBeanTemp.getGy_notifyUrl() + "----" + jsonStr));
            notifyBack = HttpUtility.postData(evn, jsonStr);
            log.info((Object) ("\u56de\u8c03\u63a5\u5165\u65b9\u8fd4\u56de:" + notifyBack));
            notifyBeanTemp.setNotify_data(jsonStr);
            this.buildNotifyBack(notifyBeanTemp, notifyBack);
        }
        this.wxNativeDao.update(notifyBeanTemp);
        return "success";
    }

    @Override
    public ResponseCode getWxjspay(TradeParam reqParam, QrcodeChannelInf qrChannelInf) {
        ResponseCode response = new ResponseCode();
        String checkValue = reqParam.checkPram();
        if (!"00".equals(checkValue)) {
            response.setResultCode(ResponseEnum.FAIL_PARAM.getCode());
            response.setMessage(checkValue);
            return response;
        }
        if (this.wxNativeDao.getByTradesn(reqParam.getTradeSn(), reqParam.getGymchtId()) != null) {
            response.setResultCode(ResponseEnum.FAIL_ORDER_NO_REPEAT.getCode());
            response.setMessage(ResponseEnum.FAIL_ORDER_NO_REPEAT.getMemo());
            return response;
        }
        WxJspayParam wxJspayParam = new WxJspayParam();
        WxpayScanCode wxJspayInfo = new WxpayScanCode();
        String tradeNo = UUIDGenerator.getOrderIdByUUId();
        wxJspayParam.setService("pay.weixin.jspay");
        wxJspayParam.setCharset("UTF-8");
        wxJspayParam.setVersion("2.0");
        wxJspayParam.setSign_type("MD5");
        wxJspayParam.setMch_id(qrChannelInf.getChannel_mcht_no());
        wxJspayParam.setIs_raw(reqParam.getIs_raw());
        wxJspayParam.setOut_trade_no(tradeNo);
        wxJspayParam.setBody(reqParam.getGoodsName());
        if (StringUtil.isNotEmpty(reqParam.getSub_appid())) {
            wxJspayParam.setSub_appid(reqParam.getSub_appid());
        }
        wxJspayParam.setSub_openid(reqParam.getSub_openid());
        wxJspayParam.setAttach("\u5546\u54c1\u9644\u52a0\u4fe1\u606f");
        wxJspayParam.setTotal_fee(reqParam.getOrderAmount());
        wxJspayParam.setMch_create_ip(reqParam.getRemoteAddr());
        wxJspayParam.setNotify_url(SysParamUtil.getParam("JSPAY_NOTIFY_URL"));
        wxJspayParam.setCallback_url(reqParam.getCallback_url());
        wxJspayParam.setTime_start(DateUtil.getCurrTime());
        int expir = StringUtil.parseInt(reqParam.getExpirySecond());
        wxJspayParam.setTime_expire(DateUtil.getDateAfterSeconds(expir == 0 ? 3600 : expir, "yyyyMMddHHmmss"));
        wxJspayParam.setNonce_str(StringUtil.getRandom(16));
        wxJspayParam.setLimit_credit_pay(SysParamUtil.getParam("limit_credit_pay"));
        wxJspayParam.setSign(GuangdaUtil.getMd5Sign(wxJspayParam, qrChannelInf.getSecret_key()));
        String xml = Dom4jUtil.createXmlStr(wxJspayParam, "xml");
        String resultXml = HttpUtility.postData(Environment.GUANGDA_URL, xml);
        BeanUtil.copyPropertiesNotNull(wxJspayParam, wxJspayInfo, null, new String[0]);
        wxJspayInfo = Dom4jUtil.parseXml2Object(resultXml, wxJspayInfo);
        wxJspayInfo.setT0Flag(reqParam.getT0Flag());
        wxJspayInfo.setGymchtId(reqParam.getGymchtId());
        wxJspayInfo.setGy_notifyUrl(reqParam.getNotifyUrl());
        wxJspayInfo.setCallback_url(reqParam.getCallback_url());
        wxJspayInfo.setChannel_id(qrChannelInf.getChannel_id());
        wxJspayInfo.setTradeSn(reqParam.getTradeSn());
        wxJspayInfo.setTrade_source(TradeSourceEnum.WEJSPAY.getCode());
        if ("0".equals(wxJspayInfo.getStatus())) {
            if ("0".equals(wxJspayInfo.getResult_code())) {
                response.setResultCode(ResponseEnum.SUCCESS.getCode());
                response.setMessage(ResponseEnum.SUCCESS.getMemo());
                String codeUrl = "https://pay.swiftpass.cn/pay/jspay?token_id=" + wxJspayInfo.getToken_id() + "&showwxpaytitle=1";
                response.setPay_info(codeUrl);
                wxJspayInfo.setTrade_state(TradeStateEnum.NOTPAY.getCode());
                wxJspayInfo.setCode_url(codeUrl);
                wxJspayInfo.setResp_code("00");
            } else {
                response.setResultCode(wxJspayInfo.getErr_code());
                response.setMessage(wxJspayInfo.getErr_msg());
                wxJspayInfo.setResp_code(wxJspayInfo.getErr_code());
            }
        } else {
            response.setResultCode(wxJspayInfo.getStatus());
            response.setMessage(wxJspayInfo.getMessage());
            wxJspayInfo.setResp_code(wxJspayInfo.getStatus());
        }
        this.wxNativeDao.save(wxJspayInfo);
        return response;
    }

    @Override
    public List<TradeDetailBean> listAll(Map<String, String> param, OprInfo oprInfo) {
        return this.wxNativeDao.listAll(param, oprInfo);
    }

    @Override
    public PageModle<TradeDetailBean> listByPage(Map<String, String> param, int pageNum, int perPageNum, OprInfo oprInfo) {
        return this.wxNativeDao.listByPage(param, pageNum, perPageNum, oprInfo);
    }

    @Override
    public int acount(Map<String, String> param, OprInfo oprInfo) {
        return this.wxNativeDao.acount(param, oprInfo);
    }
}
