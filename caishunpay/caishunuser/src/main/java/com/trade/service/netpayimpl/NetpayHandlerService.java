/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.trade.service.netpayimpl;

import com.gy.system.Environment;
import com.gy.util.ContextUtil;
import com.gy.util.DateUtil;
import com.gy.util.HttpUtility;
import com.gy.util.StringUtil;
import com.trade.bean.QuickpayBean;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.bean.own.QuickpayParam;
import com.trade.bean.response.QuickpayResponse;
import com.trade.dao.QrcodeMchtInfoDao;
import com.trade.dao.QuickpayDao;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeSourceEnum;
import com.trade.enums.TradeStateEnum;
import com.trade.service.NetpayService;
import com.trade.util.GuangdaUtil;
import com.trade.util.JsonUtil;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NetpayHandlerService {
    @Autowired
    private QrcodeMchtInfoDao qrcodeMchtInfoDaoImpl;
    @Autowired
    private QuickpayDao quickpayDao;
    private NetpayService netpayService;
    private static Logger log = Logger.getLogger(NetpayHandlerService.class);

    public QuickpayResponse netpayApply(QuickpayParam reqParam, QrcodeChannelInf qrChannelInf) {
        QuickpayResponse response = new QuickpayResponse();
        if (this.quickpayDao.getByTradesn(reqParam.getTradeSn(), reqParam.getGymchtId()) != null) {
            response.setResultCode(ResponseEnum.FAIL_ORDER_NO_REPEAT.getCode());
            response.setMessage(ResponseEnum.FAIL_ORDER_NO_REPEAT.getMemo());
            return response;
        }
        this.netpayService = this.switchService(qrChannelInf.getChannel_id());
        if (this.netpayService == null) {
            response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
            response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
            return response;
        }
        QuickpayBean tradeInfo = this.netpayService.netpayApply(reqParam, qrChannelInf);
        tradeInfo.setService(qrChannelInf.getChannel_id());
        tradeInfo.setMch_id(qrChannelInf.getChannel_mcht_no());
        tradeInfo.setMch_create_ip(reqParam.getRemoteAddr());
        tradeInfo.setChannel_id(qrChannelInf.getChannel_id());
        tradeInfo.setTrade_source(TradeSourceEnum.NETPAY.getCode());
        tradeInfo.setTradeSn(reqParam.getTradeSn());
        tradeInfo.setMch_create_ip(reqParam.getRemoteAddr());
        tradeInfo.setTotal_fee(reqParam.getOrderAmount());
        tradeInfo.setT0Flag(reqParam.getT0Flag());
        tradeInfo.setGymchtId(reqParam.getGymchtId());
        tradeInfo.setGy_notifyUrl(reqParam.getNotifyUrl());
        tradeInfo.setCallback_url(reqParam.getCallbackUrl());
        tradeInfo.setCardHolderName(reqParam.getCardHolderName());
        tradeInfo.setCardNo(reqParam.getCardNo());
        tradeInfo.setCardType(reqParam.getCardType());
        tradeInfo.setExpireDate(reqParam.getExpireDate());
        tradeInfo.setCvv(reqParam.getCvv());
        tradeInfo.setBankCode(reqParam.getBankCode());
        tradeInfo.setBankName(StringUtil.getBankNameBySeq(reqParam.getBankSegment()));
        tradeInfo.setCerType(reqParam.getCerType());
        tradeInfo.setCerNumber(reqParam.getCerNumber());
        tradeInfo.setMobileNum(reqParam.getMobileNum());
        tradeInfo.setDevice_info(reqParam.getChannelType());
        this.quickpayDao.save(tradeInfo);
        response.setTransaction_id(tradeInfo.getOut_trade_no());
        if ("00".equals(tradeInfo.getResp_code())) {
            response.setResultCode(ResponseEnum.SUCCESS.getCode());
            response.setMessage(ResponseEnum.SUCCESS.getMemo());
            response.setPayUrl(tradeInfo.getPayUrl());
        } else {
            response.setResultCode(ResponseEnum.BACK_EXCEPTION.getCode());
            response.setMessage(tradeInfo.getMessage());
        }
        response.setNonce(StringUtil.getRandom(32));
        return response;
    }

    public QuickpayResponse netpayQuery(QuickpayParam reqParam, QrcodeChannelInf qrChannelInf) {
        QuickpayResponse response = new QuickpayResponse();
        QuickpayBean tradeInfo = this.quickpayDao.getByTradesn(reqParam.getTradeSn(), reqParam.getGymchtId());
        if (tradeInfo == null) {
            response.setResultCode(ResponseEnum.FAIL_ORDER_NOT_EXIST.getCode());
            response.setMessage(ResponseEnum.FAIL_ORDER_NOT_EXIST.getMemo());
            return response;
        }
        qrChannelInf = this.qrcodeMchtInfoDaoImpl.getChannelInf(tradeInfo.getChannel_id(), tradeInfo.getMch_id());
        if (qrChannelInf != null && !TradeStateEnum.SUCCESS.getCode().equals(tradeInfo.getTrade_state())) {
            this.netpayService = this.switchService(qrChannelInf.getChannel_id());
            if (this.netpayService != null) {
                tradeInfo = this.netpayService.queryNetpayFromChannel(tradeInfo, qrChannelInf);
            }
        }
        response = NetpayHandlerService.buildResponseQuery(tradeInfo);
        return response;
    }

    public void notifyUnsuccessTrade() {
        List<QuickpayBean> list = this.quickpayDao.listNotifyFailTrade();
        log.info((Object) ("\u7f51\u94f6\u652f\u4ed8\u7ed3\u679c\u5f02\u6b65\u901a\u77e5, \u672c\u6b21\u5f85\u901a\u77e5\u4ea4\u6613:" + list.size()));
        for (QuickpayBean tradeBean : list) {
            if ("success".equals(tradeBean.getGynotify_back()) || !this.assertNotify(tradeBean.getNofity_time(), tradeBean.getNotify_num()))
                continue;
            NetpayHandlerService.buildNotifyBack(tradeBean, this.tradeNotify(tradeBean));
            this.quickpayDao.update(tradeBean);
        }
    }

    protected String tradeNotify(QuickpayBean tradeBean) {
        String jsonStr = tradeBean.getNotify_data();
        if (StringUtil.isEmpty(tradeBean.getNotify_data())) {
            QrcodeMchtInfo qrcodeMcht = this.qrcodeMchtInfoDaoImpl.getMchtInfo(tradeBean.getGymchtId());
            QuickpayResponse backNotify = NetpayHandlerService.buildResponseQuery(tradeBean);
            backNotify.setSign(GuangdaUtil.getMd5Sign(backNotify, qrcodeMcht.getSecretKey()));
            jsonStr = JsonUtil.buildJson(backNotify);
            tradeBean.setNotify_data(jsonStr);
        }
        String notifyBack = "";
        if (StringUtil.isNotEmpty(tradeBean.getGy_notifyUrl()) && (tradeBean.getGy_notifyUrl().contains("http") || tradeBean.getGy_notifyUrl().contains("www"))) {
            Environment evn = Environment.createEnvironment(tradeBean.getGy_notifyUrl(), "utf-8", "application/json");
            log.info((Object) String.format("\u7b56\u7565\u56de\u8c03\u7f51\u94f6\u652f\u4ed8\u901a\u77e5\u7b2c%s\u6b21%s----%s", tradeBean.getNotify_num() + 1, tradeBean.getGy_notifyUrl(), jsonStr));
            notifyBack = HttpUtility.postData(evn, jsonStr);
            log.info((Object) ("\u7b56\u7565\u56de\u8c03\u7f51\u94f6\u652f\u4ed8\u901a\u77e5\u8fd4\u56de:" + notifyBack));
        }
        return notifyBack;
    }

    private NetpayService switchService(String channelId) {
        if (channelId == null) {
            return null;
        }
        NetpayService service = null;
        if ("tfb".equals(channelId)) {
            service = (NetpayService) ContextUtil.getBean("tfbServiceImpl");
        }
        return service;
    }

    public static QuickpayResponse buildResponseQuery(QuickpayBean quickpayBean) {
        QuickpayResponse response = new QuickpayResponse();
        response.setGymchtId(quickpayBean.getGymchtId());
        response.setNonce(StringUtil.getRandom(32));
        response.setTransaction_id(quickpayBean.getOut_trade_no());
        response.setTradeSn(quickpayBean.getTradeSn());
        response.setOut_transaction_id(quickpayBean.getTransaction_id());
        response.setTradeState(quickpayBean.getTrade_state());
        response.setOrderAmount(String.valueOf(quickpayBean.getTotal_fee()));
        response.setChannelType(quickpayBean.getDevice_info());
        response.setCardType(quickpayBean.getCardType());
        response.setBankName(quickpayBean.getBankName());
        response.setTimeEnd(quickpayBean.getTime_end());
        return response;
    }

    public static void buildNotifyBack(QuickpayBean quickpayBean, String notifyBack) {
        quickpayBean.setGynotify_back(notifyBack != null && notifyBack.length() > 20 ? notifyBack.substring(0, 20) : notifyBack);
        quickpayBean.setNotify_num((quickpayBean.getNotify_num() == null ? 0 : quickpayBean.getNotify_num()) + 1);
        quickpayBean.setNofity_time(DateUtil.getCurrTime());
    }

    public static String buildCallbackParam(QuickpayBean quickpayBean, QrcodeMchtInfo qrcodeMcht) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("gymchtId", quickpayBean.getGymchtId());
        paramMap.put("tradeSn", quickpayBean.getTradeSn());
        paramMap.put("transaction_id", quickpayBean.getOut_trade_no());
        paramMap.put("tradeState", quickpayBean.getTrade_state());
        paramMap.put("orderAmount", String.valueOf(quickpayBean.getTotal_fee()));
        paramMap.put("nonce", StringUtil.getRandom(32));
        paramMap.put("sign", GuangdaUtil.getMd5Sign(paramMap, qrcodeMcht.getSecretKey()));
        return GuangdaUtil.map2HttpParam(paramMap);
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
