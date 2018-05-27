/*
 * Decompiled with CFR 0_124.
 *
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 */
package com.trade.service.quickimpl;

import com.account.service.TradeMchtAccountService;
import com.gy.util.ContextUtil;
import com.gy.util.StringUtil;
import com.manage.bean.PageModle;
import com.trade.bean.QuickpayBean;
import com.trade.bean.QuickpayQueryBean;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.QuickpayParam;
import com.trade.bean.response.QuickpayResponse;
import com.trade.dao.QrcodeMchtInfoDao;
import com.trade.dao.QuickpayDao;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeSourceEnum;
import com.trade.enums.TradeStateEnum;
import com.trade.service.QuickpayService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class QuickpayServiceImpl
        implements QuickpayService {
    @Autowired
    protected QuickpayDao quickpayDao;
    @Autowired
    protected TradeMchtAccountService tradeMchtAccountService;
    @Autowired
    protected QrcodeMchtInfoDao qrcodeMchtInfoDaoImpl;

    @Override
    public QuickpayResponse prePay(QuickpayParam reqParam, QrcodeChannelInf qrChannelInf) {
        QuickpayResponse response = new QuickpayResponse();
        if (this.quickpayDao.getByTradesn(reqParam.getTradeSn(), reqParam.getGymchtId()) != null) {
            response.setResultCode(ResponseEnum.FAIL_ORDER_NO_REPEAT.getCode());
            response.setMessage(ResponseEnum.FAIL_ORDER_NO_REPEAT.getMemo());
            return response;
        }
        QuickpayBean tradeInfo = this.doPrepay(reqParam, qrChannelInf);
        tradeInfo.setMch_id(qrChannelInf.getChannel_mcht_no());
        tradeInfo.setMch_create_ip(reqParam.getRemoteAddr());
        tradeInfo.setChannel_id(qrChannelInf.getChannel_id());
        tradeInfo.setTrade_source(TradeSourceEnum.QUICKPAY.getCode());
        tradeInfo.setTradeSn(reqParam.getTradeSn());
        tradeInfo.setMch_create_ip(reqParam.getRemoteAddr());
        tradeInfo.setTotal_fee(reqParam.getOrderAmount());
        tradeInfo.setT0Flag(reqParam.getT0Flag());
        tradeInfo.setGymchtId(reqParam.getGymchtId());
        tradeInfo.setCardHolderName(reqParam.getCardHolderName());
        tradeInfo.setCardNo(reqParam.getCardNo());
        tradeInfo.setCardType(reqParam.getCardType());
        tradeInfo.setExpireDate(reqParam.getExpireDate());
        tradeInfo.setCvv(reqParam.getCvv());
        tradeInfo.setBankCode(reqParam.getBankCode());
        tradeInfo.setBankName(reqParam.getBankName());
        tradeInfo.setCerType(reqParam.getCerType());
        tradeInfo.setCerNumber(reqParam.getCerNumber());
        tradeInfo.setMobileNum(reqParam.getMobileNum());
        this.quickpayDao.save(tradeInfo);
        if ("00".equals(tradeInfo.getResp_code())) {
            response.setResultCode(ResponseEnum.SUCCESS.getCode());
            response.setMessage(ResponseEnum.SUCCESS.getMemo());
            response.setTransaction_id(tradeInfo.getOut_trade_no());
            response.setYzm(tradeInfo.getYzm());
        } else {
            response.setResultCode(ResponseEnum.BACK_EXCEPTION.getCode());
            response.setMessage(tradeInfo.getMessage());
        }
        response.setNonce(StringUtil.getRandom(32));
        return response;
    }

    @Override
    public QuickpayResponse checkPay(QuickpayParam reqParam, QrcodeChannelInf qrChannelInf) {
        QuickpayResponse response = new QuickpayResponse();
        QuickpayBean quickpayBean = this.quickpayDao.getById(reqParam.getTransaction_id());
        if (quickpayBean == null || !reqParam.getGymchtId().equals(quickpayBean.getGymchtId())) {
            response.setResultCode(ResponseEnum.FAIL_ORDER_NOT_EXIST.getCode());
            response.setMessage(ResponseEnum.FAIL_ORDER_NOT_EXIST.getMemo());
            return response;
        }
        response.setTransaction_id(quickpayBean.getOut_trade_no());
        response.setGymchtId(quickpayBean.getGymchtId());
        response.setNonce(StringUtil.getRandom(32));
        if (TradeStateEnum.SUCCESS.getCode().equals(quickpayBean.getTrade_state())) {
            response.setResultCode(ResponseEnum.FAIL_PAY_REPEAT.getCode());
            response.setMessage(ResponseEnum.FAIL_PAY_REPEAT.getMemo());
            return response;
        }
        QuickpayService realQucikpayService = QuickpayServiceImpl.switchService(quickpayBean.getChannel_id());
        qrChannelInf = this.qrcodeMchtInfoDaoImpl.getChannelInf(quickpayBean.getChannel_id(), quickpayBean.getMch_id());
        quickpayBean = realQucikpayService.doCheckpay(quickpayBean, qrChannelInf, reqParam.getYzm());
        if (TradeStateEnum.SUCCESS.getCode().equals(quickpayBean.getTrade_state())) {
            response.setResultCode(ResponseEnum.SUCCESS.getCode());
            response.setMessage(ResponseEnum.SUCCESS.getMemo());
            this.tradeMchtAccountService.notifyQucikpaySuccess(quickpayBean);
            this.buildCheckData(response, quickpayBean);
        } else {
            response.setResultCode(quickpayBean.getResult_code());
            response.setMessage(quickpayBean.getMessage());
        }
        this.quickpayDao.update(quickpayBean);
        return response;
    }

    @Override
    public QuickpayResponse queryPay(QuickpayParam reqParam, QrcodeChannelInf qrChannelInf) {
        QuickpayResponse response = new QuickpayResponse();
        QuickpayBean quickpayBean = this.quickpayDao.getByTradesn(reqParam.getTradeSn(), reqParam.getGymchtId());
        if (quickpayBean == null) {
            response.setResultCode(ResponseEnum.FAIL_ORDER_NOT_EXIST.getCode());
            response.setMessage(ResponseEnum.FAIL_ORDER_NOT_EXIST.getMemo());
            return response;
        }
        if (!TradeStateEnum.SUCCESS.getCode().equals(quickpayBean.getTrade_state())) {
            QuickpayService realQucikpayService = QuickpayServiceImpl.switchService(quickpayBean.getChannel_id());
            quickpayBean = realQucikpayService.doQuerypay(quickpayBean, qrChannelInf);
            this.quickpayDao.update(quickpayBean);
        }
        if (TradeStateEnum.SUCCESS.getCode().equals(quickpayBean.getTrade_state())) {
            response.setResultCode(ResponseEnum.SUCCESS.getCode());
            response.setMessage(ResponseEnum.SUCCESS.getMemo());
            response.setOut_transaction_id(quickpayBean.getTransaction_id());
        } else {
            response.setResultCode(quickpayBean.getResp_code());
            response.setMessage(quickpayBean.getMessage());
        }
        this.buildQueryData(response, quickpayBean);
        return response;
    }

    public static QuickpayService switchService(String channelId) {
        if (channelId == null) {
            return null;
        }
        QuickpayService service = null;
        if ("mobao".equals(channelId)) {
            service = (QuickpayService) ContextUtil.getBean("mobaoPayServiceImpl");
        } else if ("helibao".equals(channelId)) {
            service = (QuickpayService) ContextUtil.getBean("helibaoQuickServiceImpl");
        }
        return service;
    }

    @Override
    public PageModle<QuickpayQueryBean> listByPage(Map<String, String> param, int pageNum, int perPageNum) {
        return this.quickpayDao.listByPage(param, pageNum, perPageNum);
    }

    @Override
    public List<QuickpayQueryBean> listAll(Map<String, String> param) {
        return this.quickpayDao.listAll(param);
    }

    private void buildQueryData(QuickpayResponse response, QuickpayBean quickpayBean) {
        response.setGymchtId(quickpayBean.getGymchtId());
        response.setNonce(StringUtil.getRandom(32));
        response.setTransaction_id(quickpayBean.getOut_trade_no());
        this.buildCheckData(response, quickpayBean);
    }

    private void buildCheckData(QuickpayResponse response, QuickpayBean quickpayBean) {
        response.setTradeSn(quickpayBean.getTradeSn());
        response.setOut_transaction_id(quickpayBean.getTransaction_id());
        response.setTradeState(quickpayBean.getTrade_state());
        response.setOrderAmount(String.valueOf(quickpayBean.getTotal_fee()));
        response.setCardHolderName(quickpayBean.getCardHolderName());
        response.setCardNo(quickpayBean.getCardNo());
        response.setCardType(quickpayBean.getCardType());
        response.setBankName(quickpayBean.getBankName());
        response.setTimeEnd(quickpayBean.getTime_end());
    }
}
