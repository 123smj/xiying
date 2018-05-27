/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 */
package com.trade.service.quickimpl;

import com.account.service.TradeMchtAccountService;
import com.gy.util.CommonFunction;
import com.gy.util.ContextUtil;
import com.gy.util.StringUtil;
import com.manage.bean.PageModle;
import com.trade.bean.BankCardPay;
import com.trade.bean.QuickpayQueryBean;
import com.trade.bean.own.BankCardPayRequest;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.own.PayChannelInf;
import com.trade.bean.response.BankPayResponse;
import com.trade.dao.MerchantInfDao;
import com.trade.dao.BankCardPayDao;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeSource;
import com.trade.enums.TradeStateEnum;
import com.trade.service.QuickpayService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/***
 * 快捷支付虚类， 快捷支付通道实现类继承此类
 */
public abstract class QuickpayServiceImpl
implements QuickpayService {
    @Autowired
    protected BankCardPayDao bankCardPayDao;
    @Autowired
    protected TradeMchtAccountService tradeMchtAccountService;
    @Autowired
    protected MerchantInfDao merchantInfDaoImpl;

    @Override
    public BankPayResponse prePay(BankCardPayRequest reqParam, MerchantInf qrcodeMcht, PayChannelInf qrChannelInf) {
        BankPayResponse response = new BankPayResponse();
        if (this.bankCardPayDao.getByTradesn(reqParam.getTradeSn(), reqParam.getMerchantId()) != null) {
            response.setResultCode(ResponseEnum.FAIL_ORDER_NO_REPEAT.getCode());
            response.setMessage(ResponseEnum.FAIL_ORDER_NO_REPEAT.getMemo());
            return response;
        }
        BankCardPay tradeInfo = this.doPrepay(reqParam, qrChannelInf);
        tradeInfo.setMch_id(qrChannelInf.getChannel_mcht_no());
        tradeInfo.setMch_create_ip(reqParam.getRemoteAddr());
        tradeInfo.setChannel_id(qrChannelInf.getChannel_id());
        if (!TradeSource.DAIKOU.getCode().equals(tradeInfo.getTrade_source())) {
            tradeInfo.setTrade_source(TradeSource.QUICKPAY.getCode());
        } else if (!CommonFunction.isTradeSourceOpen(qrcodeMcht.getTrade_source_list(), TradeSource.DAIKOU)) {
            response.setResultCode(ResponseEnum.PAY_TYPE_UNSUPPORT.getCode());
            response.setMessage(ResponseEnum.PAY_TYPE_UNSUPPORT.getMemo());
            return response;
        }
        tradeInfo.setTradeSn(reqParam.getTradeSn());
        tradeInfo.setMch_create_ip(reqParam.getRemoteAddr());
        tradeInfo.setTotal_fee(reqParam.getOrderAmount());
        tradeInfo.setT0Flag(reqParam.getT0Flag());
        tradeInfo.setMerchantId(reqParam.getMerchantId());
        QuickpayServiceImpl.countMchtFee(tradeInfo, qrcodeMcht);
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
        this.bankCardPayDao.save(tradeInfo);
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
    public BankPayResponse checkPay(BankCardPayRequest reqParam, PayChannelInf qrChannelInf) {
        BankPayResponse response = new BankPayResponse();
        BankCardPay BankCardPay = this.bankCardPayDao.getById(reqParam.getTransaction_id());
        if (BankCardPay == null || !reqParam.getMerchantId().equals(BankCardPay.getMerchantId())) {
            response.setResultCode(ResponseEnum.FAIL_ORDER_NOT_EXIST.getCode());
            response.setMessage(ResponseEnum.FAIL_ORDER_NOT_EXIST.getMemo());
            return response;
        }
        response.setTransaction_id(BankCardPay.getOut_trade_no());
        response.setMerchantId(BankCardPay.getMerchantId());
        response.setNonce(StringUtil.getRandom(32));
        if (TradeStateEnum.SUCCESS.getCode().equals(BankCardPay.getTrade_state())) {
            response.setResultCode(ResponseEnum.FAIL_PAY_REPEAT.getCode());
            response.setMessage(ResponseEnum.FAIL_PAY_REPEAT.getMemo());
            return response;
        }
        QuickpayService realQucikpayService = QuickpayServiceImpl.switchService(BankCardPay.getChannel_id());
        qrChannelInf = this.merchantInfDaoImpl.getChannelInf(BankCardPay.getChannel_id(), BankCardPay.getMch_id());
        BankCardPay = realQucikpayService.doCheckpay(BankCardPay, qrChannelInf, reqParam.getYzm());
        if (TradeStateEnum.SUCCESS.getCode().equals(BankCardPay.getTrade_state())) {
            response.setResultCode(ResponseEnum.SUCCESS.getCode());
            response.setMessage(ResponseEnum.SUCCESS.getMemo());
            this.tradeMchtAccountService.notifyQucikpaySuccess(BankCardPay);
            this.buildCheckData(response, BankCardPay);
        } else {
            response.setResultCode(BankCardPay.getResult_code());
            response.setMessage(BankCardPay.getMessage());
        }
        this.bankCardPayDao.update(BankCardPay);
        return response;
    }

    @Override
    public BankPayResponse queryPay(BankCardPayRequest reqParam, PayChannelInf qrChannelInf) {
        BankPayResponse response = new BankPayResponse();
        BankCardPay BankCardPay = this.bankCardPayDao.getByTradesn(reqParam.getTradeSn(), reqParam.getMerchantId());
        if (BankCardPay == null) {
            response.setResultCode(ResponseEnum.FAIL_ORDER_NOT_EXIST.getCode());
            response.setMessage(ResponseEnum.FAIL_ORDER_NOT_EXIST.getMemo());
            return response;
        }
        if (!TradeStateEnum.SUCCESS.getCode().equals(BankCardPay.getTrade_state())) {
            QuickpayService realQucikpayService = QuickpayServiceImpl.switchService(BankCardPay.getChannel_id());
            BankCardPay = realQucikpayService.doQuerypay(BankCardPay, qrChannelInf);
            this.bankCardPayDao.update(BankCardPay);
        }
        if (TradeStateEnum.SUCCESS.getCode().equals(BankCardPay.getTrade_state())) {
            response.setResultCode(ResponseEnum.SUCCESS.getCode());
            response.setMessage(ResponseEnum.SUCCESS.getMemo());
            response.setOut_transaction_id(BankCardPay.getTransaction_id());
        } else {
            response.setResultCode(BankCardPay.getResp_code());
            response.setMessage(BankCardPay.getMessage());
        }
        this.buildQueryData(response, BankCardPay);
        return response;
    }

    public static void countMchtFee(BankCardPay tradeInf, MerchantInf qrcodeMcht) {
        Double mchtRate = null;
        int mchtRateFee = 0;
        int mchtSettleFee = 0;
        if (TradeSource.QUICKPAY.getCode().equals(tradeInf.getTrade_source())) {
            mchtRate = qrcodeMcht.getQuickpay_fee_value();
        } else if (TradeSource.NETPAY.getCode().equals(tradeInf.getTrade_source())) {
            mchtRate = qrcodeMcht.getNetpay_fee_value();
        } else if (TradeSource.DAIKOU.getCode().equals(tradeInf.getTrade_source())) {
            mchtRate = qrcodeMcht.getDaikou_fee_value();
        }
        if (mchtRate == null) {
            return;
        }
        mchtRateFee = new BigDecimal(tradeInf.getTotal_fee()).multiply(new BigDecimal(mchtRate / 100.0)).setScale(0, RoundingMode.HALF_UP).intValue();
        if (TradeSource.DAIKOU.getCode().equals(tradeInf.getTrade_source())) {
            mchtRateFee += qrcodeMcht.getDaikou_fee_limit_value().intValue();
        }
        mchtSettleFee = tradeInf.getTotal_fee() - mchtRateFee;
        tradeInf.setMcht_rate(mchtRate);
        tradeInf.setRate_fee(mchtRateFee);
        tradeInf.setSettle_fee(mchtSettleFee);
    }

    /***
     * 选择快捷支付的实现类
     * @param channelId 通道ID(一个ID 与 一个通道对应)
     * @return 实现类
     */
    public static QuickpayService switchService(String channelId) {
        if (channelId == null) {
            return null;
        }
        QuickpayService service = null;
        if ("mobao".equals(channelId)) {
            service = (QuickpayService)ContextUtil.getBean("mobaoPayServiceImpl");
        } else if ("helibao".equals(channelId)) {
            service = (QuickpayService)ContextUtil.getBean("helibaoQuickServiceImpl");
        } else if ("yinshengbao".equals(channelId)) {
            service = (QuickpayService)ContextUtil.getBean("yinshengbaoPayServiceImpl");
        }
        return service;
    }

    @Override
    public PageModle<QuickpayQueryBean> listByPage(Map<String, String> param, int pageNum, int perPageNum) {
        return this.bankCardPayDao.listByPage(param, pageNum, perPageNum);
    }

    @Override
    public List<QuickpayQueryBean> listAll(Map<String, String> param) {
        return this.bankCardPayDao.listAll(param);
    }

    private void buildQueryData(BankPayResponse response, BankCardPay BankCardPay) {
        response.setMerchantId(BankCardPay.getMerchantId());
        response.setNonce(StringUtil.getRandom(32));
        response.setTransaction_id(BankCardPay.getOut_trade_no());
        this.buildCheckData(response, BankCardPay);
    }

    private void buildCheckData(BankPayResponse response, BankCardPay BankCardPay) {
        response.setTradeSn(BankCardPay.getTradeSn());
        response.setOut_transaction_id(BankCardPay.getTransaction_id());
        response.setTradeState(BankCardPay.getTrade_state());
        response.setOrderAmount(String.valueOf(BankCardPay.getTotal_fee()));
        response.setCardHolderName(BankCardPay.getCardHolderName());
        response.setCardNo(BankCardPay.getCardNo());
        response.setCardType(BankCardPay.getCardType());
        response.setBankName(BankCardPay.getBankName());
        response.setTimeEnd(BankCardPay.getTime_end());
    }
}
