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
import com.gy.util.HttpUtility;
import com.gy.util.StringUtil;
import com.gy.util.UUIDGenerator;
import com.trade.bean.WxpayScanCode;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.bean.own.TradeParam;
import com.trade.bean.response.ResponseCode;
import com.trade.bean.response.ResponseQuery;
import com.trade.dao.QrcodeMchtInfoDao;
import com.trade.dao.WxNativeDao;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeStateEnum;
import com.trade.util.AesUtil;
import com.trade.util.GuangdaUtil;
import com.trade.util.JsonUtil;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService {
    @Autowired
    private QrcodeMchtInfoDao qrcodeMchtInfoDaoImpl;
    @Autowired
    private WxNativeDao wxNativeDao;
    @Autowired
    protected TradeMchtAccountService tradeMchtAccountService;
    private static Logger log = Logger.getLogger(CommonService.class);

    public ResponseCode prepareTrade(TradeParam reqParam, QrcodeChannelInf qrChannelInf) throws Exception {
        WxpayScanCode wxpay = new WxpayScanCode();
        String tradeNo = UUIDGenerator.getOrderIdByUUId();
        ResponseCode response = new ResponseCode();
        response.setGymchtId(reqParam.getGymchtId());
        if (this.wxNativeDao.getByTradesn(reqParam.getTradeSn(), reqParam.getGymchtId()) != null) {
            response.setResultCode(ResponseEnum.FAIL_ORDER_NO_REPEAT.getCode());
            response.setMessage(ResponseEnum.FAIL_ORDER_NO_REPEAT.getMemo());
            return response;
        }
        String currTime = DateUtil.getCurrTime();
        wxpay.setMch_id(qrChannelInf.getChannel_mcht_no());
        wxpay.setMch_create_ip(reqParam.getRemoteAddr());
        wxpay.setChannel_id(qrChannelInf.getChannel_id());
        wxpay.setTime_start(currTime);
        wxpay.setOut_trade_no(tradeNo);
        wxpay.setTradeSn(reqParam.getTradeSn());
        wxpay.setTotal_fee(reqParam.getOrderAmount());
        wxpay.setBody(reqParam.getGoodsName());
        wxpay.setT0Flag(reqParam.getT0Flag());
        wxpay.setGymchtId(reqParam.getGymchtId());
        wxpay.setGy_notifyUrl(reqParam.getNotifyUrl());
        byte[] byteSecret = AesUtil.encodeAES("17A060CF293B4D54".getBytes(), tradeNo.getBytes());
        String secretData = StringUtil.byte2hex(byteSecret);
        String codeUrl = String.valueOf(reqParam.getRootUrl()) + "/unionPay/" + reqParam.getGymchtId() + "/" + secretData;
        response.setCode_url(codeUrl);
        response.setResultCode(ResponseEnum.SUCCESS.getCode());
        response.setMessage(ResponseEnum.SUCCESS.getMemo());
        this.wxNativeDao.save(wxpay);
        return response;
    }

    protected ResponseQuery buildResponseQuery(WxpayScanCode wxpayScanCode) {
        ResponseQuery response = new ResponseQuery();
        response.setGymchtId(wxpayScanCode.getGymchtId());
        response.setTradeSn(wxpayScanCode.getTradeSn());
        response.setTransaction_id(wxpayScanCode.getOut_trade_no());
        if ("1".equals(SysParamUtil.getParam("out_transaction_id_return"))) {
            response.setOut_transaction_id(wxpayScanCode.getTransaction_id());
        }
        response.setOrderAmount(wxpayScanCode.getTotal_fee());
        response.setCoupon_fee(wxpayScanCode.getCoupon_fee());
        response.setBankType(wxpayScanCode.getBank_type());
        response.setT0Flag(wxpayScanCode.getT0Flag());
        response.setTimeEnd(wxpayScanCode.getTime_end());
        if (TradeStateEnum.SUCCESS.getCode().equals(wxpayScanCode.getTrade_state())) {
            response.setPay_result("0");
        } else if (!TradeStateEnum.NOTPAY.getCode().equals(wxpayScanCode.getTrade_state())) {
            response.setPay_result("1");
            response.setPay_info(wxpayScanCode.getTrade_state());
        }
        return response;
    }

    protected String tradeNotify(WxpayScanCode wxpayScanCode) {
        String jsonStr = wxpayScanCode.getNotify_data();
        if (StringUtil.isEmpty(wxpayScanCode.getNotify_data())) {
            QrcodeMchtInfo qrcodeMcht = this.qrcodeMchtInfoDaoImpl.getMchtInfo(wxpayScanCode.getGymchtId());
            ResponseQuery backNotify = this.buildResponseQuery(wxpayScanCode);
            backNotify.setSign(GuangdaUtil.getMd5Sign(backNotify, qrcodeMcht.getSecretKey()));
            jsonStr = JsonUtil.buildJson(backNotify);
            wxpayScanCode.setNotify_data(jsonStr);
        }
        String notifyBack = "";
        if (StringUtil.isNotEmpty(wxpayScanCode.getGy_notifyUrl()) && (wxpayScanCode.getGy_notifyUrl().contains("http") || wxpayScanCode.getGy_notifyUrl().contains("www"))) {
            Environment evn = Environment.createEnvironment(wxpayScanCode.getGy_notifyUrl(), "utf-8", "application/json");
            log.info((Object) String.format("\u7b56\u7565\u56de\u8c03\u901a\u77e5\u7b2c%s\u6b21%s----%s", wxpayScanCode.getNotify_num() + 1, wxpayScanCode.getGy_notifyUrl(), jsonStr));
            notifyBack = HttpUtility.postData(evn, jsonStr);
            log.info((Object) ("\u7b56\u7565\u56de\u8c03\u901a\u77e5\u8fd4\u56de:" + notifyBack));
        }
        return notifyBack;
    }

    public void notifyUnsuccessTrade() {
        List<WxpayScanCode> list = this.wxNativeDao.listNotifyFailTrade();
        log.info((Object) ("\u652f\u4ed8\u7ed3\u679c\u5f02\u6b65\u901a\u77e5, \u672c\u6b21\u5f85\u901a\u77e5\u4ea4\u6613:" + list.size()));
        for (WxpayScanCode tradeBean : list) {
            if ("success".equals(tradeBean.getGynotify_back()) || !this.assertNotify(tradeBean.getNofity_time(), tradeBean.getNotify_num()))
                continue;
            this.buildNotifyBack(tradeBean, this.tradeNotify(tradeBean));
            this.wxNativeDao.update(tradeBean);
        }
    }

    protected void buildNotifyBack(WxpayScanCode wxpayScanCode, String notifyBack) {
        wxpayScanCode.setGynotify_back(notifyBack != null && notifyBack.length() > 20 ? notifyBack.substring(0, 20) : notifyBack);
        wxpayScanCode.setNotify_num((wxpayScanCode.getNotify_num() == null ? 0 : wxpayScanCode.getNotify_num()) + 1);
        wxpayScanCode.setNofity_time(DateUtil.getCurrTime());
    }

    private boolean assertNotify(String notifyTime, int notifyNum) {
        if (notifyNum >= 5) {
            return false;
        }
        if (StringUtil.isEmpty(notifyTime)) {
            return true;
        }
        int intervalSecond = notifyNum * 60;
        String nextNotifyTime = DateUtil.getDateAfterSeconds(DateUtil.getDate(notifyTime, "yyyyMMddHHmmss"), intervalSecond, "yyyyMMddHHmmss");
        String current = DateUtil.getCurrTime();
        if (current.compareTo(nextNotifyTime) < 0) {
            return false;
        }
        return true;
    }
}
