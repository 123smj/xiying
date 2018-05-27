/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.trade.service.netpayimpl;

import com.gy.util.StringUtil;
import com.trade.bean.BankCardPay;
import com.trade.bean.own.BankCardPayRequest;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.own.PayChannelInf;
import com.trade.bean.response.BankPayResponse;
import com.trade.dao.BankCardPayDao;
import com.trade.dao.MerchantInfDao;
import com.trade.enums.ChannelStatusEnum;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeSource;
import com.trade.enums.TradeStateEnum;
import com.trade.service.BankCardPayService;
import com.trade.service.quickimpl.QuickpayServiceImpl;
import com.trade.util.ReflectionUtils;
import com.trade.util.ResponseUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BankCardPayDispatcherService {
    private static Logger log = Logger.getLogger(BankCardPayDispatcherService.class);

    @Autowired
    private MerchantInfDao merchantInfDaoImpl;
    @Autowired
    private BankCardPayDao bankCardPayDao;

    @Autowired
    private List<BankCardPayService> bankCardPayServices;

    private static Map<String, BankCardPayService> bankCardServiceMap = new HashMap<>();

    @PostConstruct
    public void postConstruct() {
        for (BankCardPayService service : bankCardPayServices) {
            ReflectionUtils.putServiceTo(service, bankCardServiceMap);
        }
    }

    public BankPayResponse doOrderCreate(BankCardPayRequest reqParam, MerchantInf qrcodeMcht, PayChannelInf qrChannelInf) {
        BankPayResponse response = new BankPayResponse();
        if (this.bankCardPayDao.getByTradesn(reqParam.getTradeSn(), reqParam.getMerchantId()) != null) {
            response.setResultCode(ResponseEnum.FAIL_ORDER_NO_REPEAT.getCode());
            response.setMessage(ResponseEnum.FAIL_ORDER_NO_REPEAT.getMemo());
            return response;
        }
        BankCardPayService bankCardPayService = switchService(qrChannelInf.getChannel_id());
        if (bankCardPayService == null) {
            response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
            response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
            return response;
        }
        if (ChannelStatusEnum.FREEZE.getCode().equals(qrChannelInf.getStatus())) {
            response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
            response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
            return response;
        }
        BankCardPay tradeInfo = bankCardPayService.doOrderCreate(reqParam, qrChannelInf);
        tradeInfo.setService(qrChannelInf.getChannel_id());
        tradeInfo.setMch_id(qrChannelInf.getChannel_mcht_no());
        tradeInfo.setMch_create_ip(reqParam.getRemoteAddr());
        tradeInfo.setChannel_id(qrChannelInf.getChannel_id());
        tradeInfo.setTrade_source(TradeSource.NETPAY.getCode());
        tradeInfo.setTradeSn(reqParam.getTradeSn());
        tradeInfo.setTotal_fee(reqParam.getOrderAmount());
        tradeInfo.setT0Flag(reqParam.getT0Flag());
        tradeInfo.setMerchantId(reqParam.getMerchantId());
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
        QuickpayServiceImpl.countMchtFee(tradeInfo, qrcodeMcht);
        this.bankCardPayDao.save(tradeInfo);
        response.setTransaction_id(tradeInfo.getOut_trade_no());
        if ("00".equals(tradeInfo.getResp_code())) {
            response.setResultCode(ResponseEnum.SUCCESS.getCode());
            response.setMessage(ResponseEnum.SUCCESS.getMemo());
            response.setPayUrl(tradeInfo.getPayUrl());
        } else {
            response.setResultCode(ResponseEnum.BACK_EXCEPTION.getCode());
            response.setMessage(tradeInfo.getMessage());
        }
        return response;
    }

    public BankPayResponse doOrderQuery(BankCardPayRequest reqParam) {
        BankCardPayService bankCardPayService;
        BankPayResponse response = new BankPayResponse();
        BankCardPay tradeInfo = this.bankCardPayDao.getByTradesn(reqParam.getTradeSn(), reqParam.getMerchantId());
        if (tradeInfo == null) {
            response.setResultCode(ResponseEnum.FAIL_ORDER_NOT_EXIST.getCode());
            response.setMessage(ResponseEnum.FAIL_ORDER_NOT_EXIST.getMemo());
            return response;
        }
        PayChannelInf qrChannelInf = this.merchantInfDaoImpl.getChannelInf(tradeInfo.getChannel_id(), tradeInfo.getMch_id());
        if (qrChannelInf != null && !TradeStateEnum.SUCCESS.getCode().equals(tradeInfo.getTrade_state())) {
            bankCardPayService = switchService(qrChannelInf.getChannel_id());
            tradeInfo = bankCardPayService.doOrderQuery(tradeInfo, qrChannelInf);
        }
        response = ResponseUtil.buildBankPayResponse(tradeInfo);
        return response;
    }

    public BankCardPayService switchService(String channelId) {
        return bankCardServiceMap.get(channelId);
    }
}
