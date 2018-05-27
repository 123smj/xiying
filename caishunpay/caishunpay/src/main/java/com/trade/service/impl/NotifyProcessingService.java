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
import com.gy.util.DateUtil;
import com.gy.util.HttpUtility;
import com.gy.util.StringUtil;
import com.trade.bean.BankCardPay;
import com.trade.bean.PayDetail;
import com.trade.bean.ThirdPartyPayDetail;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.response.BankPayResponse;
import com.trade.bean.response.ThirdPartyResponse;
import com.trade.dao.BankCardPayDao;
import com.trade.dao.MerchantInfDao;
import com.trade.dao.ThirdPartyPayDetailDao;
import com.trade.enums.TradeStateEnum;
import com.trade.service.BankCardPayService;
import com.trade.service.ThirdPartyPayService;
import com.trade.service.netpayimpl.BankCardPayDispatcherService;
import com.trade.util.MD5Util;
import com.trade.util.JsonUtil;

import java.util.List;
import java.util.Map;

import com.trade.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 此类为核心类 它负责支付回调
 * @author Administrator
 *
 */
@Service
public class NotifyProcessingService {
    @Autowired
    private MerchantInfDao merchantInfDaoImpl;
    @Autowired
    private ThirdPartyPayDetailDao thirdPartyPayDetailDao;
    @Autowired
    private  BankCardPayDao bankCardPayDao;
    @Autowired
    protected TradeMchtAccountService tradeMchtAccountService;
    @Autowired
    private BankCardPayDispatcherService bankCardPayDispatcherService;
    @Autowired
    private ThirdPartyPayDispatcherService thirdPartyPayDispatcherService;
    private static Logger log = Logger.getLogger(NotifyProcessingService.class);

    public void acceptThirdPartyNotify(String channelId, Map<String, String> resultMap) {
        ThirdPartyPayService service = thirdPartyPayDispatcherService.switchService(channelId);
        ThirdPartyPayDetail detail = service.acceptThirdPartyPayNotify(resultMap);
        if (detail == null) {
            return;
        }
        try {
            if (detail.getTrade_state().equals(TradeStateEnum.SUCCESS.getCode())) {
                detail.setTime_end(DateUtil.getCurrTime());
                this.notifyThirdPartyPay(detail);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("when notify third party:", e);
        }
        thirdPartyPayDetailDao.update(detail);
    }

    public void acceptBankCardNotify(String channelId, Map<String, String> resultMap) {
        BankCardPayService service = bankCardPayDispatcherService.switchService(channelId);
        BankCardPay detail = service.acceptBankCardPayNotify(resultMap);
        try {
            if (detail.getTrade_state().equals(TradeStateEnum.SUCCESS.getCode()))
                detail.setTime_end(DateUtil.getCurrTime());
            this.notifyBankCardPay(detail);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("when notify bank card:", e);
        }
        bankCardPayDao.update(detail);
    }

    /**
     * 回调银行卡支付结果
     */
    public void notifyBankCardPay(BankCardPay detail) {
        if (shouldBeNotified(detail)) {
            MerchantInf qrcodeMcht = this.merchantInfDaoImpl.getMchtInfo(detail.getMerchantId());
            BankPayResponse backNotify = ResponseUtil.buildBankPayResponse(detail);
            backNotify.setNonce(StringUtil.getRandom(32));
            backNotify.setSign(MD5Util.getMd5Sign(backNotify, qrcodeMcht.getSecretKey()));
            String data = JsonUtil.buildJson(backNotify);
            detail.setNotify_data(data);
            log.info("start notify bank card pay : " + data);
            Environment evn = Environment.createEnvironment(detail.getGy_notifyUrl(), "utf-8", "application/json");
            String notifyBack = HttpUtility.postData(evn, data);
            if (notifyBack != null) {
                detail.setGynotify_back(StringUtils.substring(notifyBack, 0, 20));
                log.info("get notify bank card pay result : " + StringUtils.substring(notifyBack, 0, 20));
            }
        }
        detail.setNotify_num(detail.getNotify_num() == null ? 0 : detail.getNotify_num() + 1);
        bankCardPayDao.update(detail);
    }

    /**
     * 回调三方支付结果
     */
    public void notifyThirdPartyPay(ThirdPartyPayDetail detail) {
        if (shouldBeNotified(detail)) {
            MerchantInf qrcodeMcht = this.merchantInfDaoImpl.getMchtInfo(detail.getMerchantId());
            ThirdPartyResponse response = ResponseUtil.buildResponseQuery(detail);
            response.setNonce(StringUtil.getRandom(32));
            response.setSign(MD5Util.getMd5Sign(response, qrcodeMcht.getSecretKey()));
            String data = JsonUtil.buildJson(response);
            log.info("start notify third pay result: " + data);
            detail.setNotify_data(data);
            Environment evn = Environment.createEnvironment(detail.getGy_notifyUrl(), "utf-8", "application/json");
            String notifyBack = HttpUtility.postData(evn, data);
            if (notifyBack != null) {
                detail.setGynotify_back(StringUtils.substring(notifyBack, 0, 20));
                log.info("get notify third pay result : " + StringUtils.substring(notifyBack, 0, 20));
            }
        }
        detail.setNotify_num(detail.getNotify_num() == null ? 0 : detail.getNotify_num() + 1);
        thirdPartyPayDetailDao.update(detail);
    }

    public void thirdPartyDetailNotifyTask() {
        List<ThirdPartyPayDetail> list = this.thirdPartyPayDetailDao.listNotifyFailTrade();
        for (ThirdPartyPayDetail tradeBean : list) {
            notifyThirdPartyPay(tradeBean);
        }
    }

    public void bankCardPayDetailNotifyTask() {
        List<BankCardPay> list = this.bankCardPayDao.listNotifyFailTrade();
        for (BankCardPay tradeBean : list) {
            notifyBankCardPay(tradeBean);
        }
    }

    private static boolean shouldBeNotified(PayDetail payDetail) {
        if (!payDetail.getTrade_state().equals(TradeStateEnum.SUCCESS.getCode())) {
            return false;
        }
        if (payDetail.getGynotify_back() != null && payDetail.getGynotify_back().toLowerCase().startsWith("success")) {
            return false;
        }
        if (StringUtil.isEmpty(payDetail.getGy_notifyUrl())) {
            return false;
        }
        if (!payDetail.getGy_notifyUrl().contains("http")) {
            return false;
        }
        if (StringUtil.isEmpty(payDetail.getNofity_time())) {
            return true;
        }
        int intervalSecond = payDetail.getNotify_num() * 60;
        String nextNotifyTime = DateUtil.getDateAfterSeconds(DateUtil.getDate(payDetail.getNofity_time(), "yyyyMMddHHmmss"), intervalSecond, "yyyyMMddHHmmss");
        String current = DateUtil.getCurrTime();
        if (current.compareTo(nextNotifyTime) < 0) {
            return false;
        }
        return true;
    }
}
