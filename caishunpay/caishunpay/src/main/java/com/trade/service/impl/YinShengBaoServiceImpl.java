package com.trade.service.impl;

import com.gy.system.Environment;
import com.gy.system.SysParamUtil;
import com.gy.util.*;
import com.trade.annotations.PayChannelImplement;
import com.trade.bean.ThirdPartyPayDetail;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.own.PayChannelInf;
import com.trade.bean.own.PayRequest;
import com.trade.controller.RedirectController;
import com.trade.dao.BankCardPayDao;
import com.trade.dao.MerchantInfDao;
import com.trade.dao.ThirdPartyPayDetailDao;
import com.trade.enums.TradeSource;
import com.trade.enums.TradeStateEnum;
import com.trade.service.ThirdPartyPayService;
import com.trade.util.JsonUtil;
import com.trade.util.MD5Util;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.gy.util.CommonFunction.getCurrentDateTime;

@PayChannelImplement(channelId = "yinshengbao")
public class YinShengBaoServiceImpl implements ThirdPartyPayService {
    @Autowired
    private ThirdPartyPayDetailDao thirdPartyPayDetailDao;
    @Autowired
    private MerchantInfDao merchantInfDaoImpl;
    @Autowired
    private BankCardPayDao bankCardPayDao;

    private static final String YINSHENGBAO_NETPAY_URL = "https://www.unspay.com/unspay/page/linkbank/payRequest.do";
    private static final String YINSHENGBAO_NETPAY_QUERY_URL = "https://www.unspay.com/unspay/page/linkbank/netQueryTransStatus.action";

    private static Logger log = Logger.getLogger(YinShengBaoServiceImpl.class);
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    private static String getCallbackUrl(String raw) {
        if (!StringUtil.isEmpty(raw) && raw.startsWith("http")) {
            return raw;
        } else {
            return "http://gateway.ykbpay.com:8080/caishunpay-web/trade/payok.jsp";
        }
    }

    private String toAmountString(Double amount) {
        return String.format("%.2f", amount);
    }

    @Override
    public ThirdPartyPayDetail doOrderCreate(PayRequest payRequest, MerchantInf qrcodeMcht, PayChannelInf qrChannelInf) {
        if (payRequest.getTradeSource().equals(TradeSource.QUICKPAY))
            return doQuickPayOrderCreate(payRequest, qrcodeMcht, qrChannelInf);
        else
            return doNetPayOrderCreate(payRequest, qrcodeMcht, qrChannelInf);
    }

    private ThirdPartyPayDetail doNetPayOrderCreate(PayRequest payRequest, MerchantInf qrcodeMcht, PayChannelInf qrChannelInf) {
        String tradeNo = UUIDGenerator.getOrderIdByUUId((int) 20);
        Map<String, String> params = new LinkedHashMap<>();
        params.put("merchantId", qrChannelInf.getChannel_mcht_no());
        params.put("merchantUrl", SysParamUtil.getParam("YINSHENGBAO_NOTIFY_NP_URL"));
        params.put("responseMode", "2");
        params.put("orderId", tradeNo);
        params.put("currencyType", "CNY");
        params.put("amount", toAmountString(payRequest.getOrderAmount() / 100.0));
        params.put("assuredPay", "false");
        params.put("time", getCurrentDateTime());
        params.put("remark", "none");
        params.put("merchantKey", qrChannelInf.getSecret_key());
        String signature = MD5Util.md5Sign(params, "utf-8");
        params.put("mac", signature);
        params.put("version", "3.0.0");
        String redirectUrl = RedirectController.makeJump(params, "post", YINSHENGBAO_NETPAY_URL, Environment.DEFAULT_JUMP_URL.getBaseUrl());
        ThirdPartyPayDetail detail = ThirdPartyPayDetail.ThirdPartyPayDetailBuilder.getBuilder()
                .isSuccess(true)
                .withLocalTradeNumber(tradeNo)
                .withPayRequest(payRequest)
                .withPayChannelInf(qrChannelInf)
                .withTradeSource(payRequest.getTradeSource())
                .withTradeState(TradeStateEnum.NOTPAY)
                .withNotifyUrl(SysParamUtil.getParam("YINSHENGBAO_NOTIFY_URL"))
                .withCodeUrl(redirectUrl)
                .build();
        return detail;
    }

    private ThirdPartyPayDetail doQuickPayOrderCreate(PayRequest payRequest, MerchantInf qrcodeMcht, PayChannelInf qrChannelInf) {
        String tradeNo = UUIDGenerator.getOrderIdByUUId((int) 20);
        Map<String, String> params = new LinkedHashMap<>();
        params.put("accountId", qrChannelInf.getChannel_mcht_no());
        params.put("customerId", qrChannelInf.getAgtId());
        params.put("orderNo", tradeNo);
        params.put("commodityName", "test");
        params.put("amount", toAmountString(payRequest.getOrderAmount() / 100.0));
        params.put("responseUrl", SysParamUtil.getParam("YINSHENGBAO_NOTIFY_QP_URL"));
        params.put("pageResponseUrl", getCallbackUrl(payRequest.getCallback_url()));
        String signature = MD5Util.md5Sign(params, qrChannelInf.getSecret_key(), "utf-8");
        params.put("mac", signature);
        String redirectUrl = RedirectController.makeJump(params, "post", Environment.YINSHENGBAO_QUICK_FRONTEND_URL.getBaseUrl(), Environment.DEFAULT_JUMP_URL.getBaseUrl());
        ThirdPartyPayDetail detail = ThirdPartyPayDetail.ThirdPartyPayDetailBuilder.getBuilder()
                .isSuccess(true)
                .withLocalTradeNumber(tradeNo)
                .withPayRequest(payRequest)
                .withPayChannelInf(qrChannelInf)
                .withTradeSource(payRequest.getTradeSource())
                .withTradeState(TradeStateEnum.NOTPAY)
                .withNotifyUrl(SysParamUtil.getParam("YINSHENGBAO_NOTIFY_URL"))
                .withCodeUrl(redirectUrl)
                .build();
        return detail;
    }

    private void processYSBQuickPayResponse(ThirdPartyPayDetail thirdPartyPayDetail, Map<String, String> resultMap, PayChannelInf qrChannelInf) {
        if (resultMap != null) {
            Map<String, String> signCheckmap = new LinkedHashMap<>();
            String respSign = resultMap.remove("mac");
            signCheckmap.put("accountId", qrChannelInf.getChannel_mcht_no());
            signCheckmap.put("orderNo", resultMap.get("orderNo"));
            signCheckmap.put("userId", resultMap.get("userId"));
            signCheckmap.put("bankName", resultMap.get("bankName"));
            signCheckmap.put("tailNo", resultMap.get("tailNo"));
            signCheckmap.put("amount", resultMap.get("amount"));
            signCheckmap.put("result_code", resultMap.get("result_code"));
            signCheckmap.put("result_msg", resultMap.get("result_msg"));
            signCheckmap.put("key", qrChannelInf.getSecret_key());
            String respSignCheck = MD5Util.md5Sign(signCheckmap, "UTF-8");
            if (!respSignCheck.equals(respSign)) {
                log.error("yinshengbao quickpay sign check error : " + respSignCheck + " , " + respSign);
                return;
            }
            if ("0000".equals(resultMap.get("result_code"))) {
                thirdPartyPayDetail.setTrade_state(TradeStateEnum.SUCCESS.getCode());
            } else {
                thirdPartyPayDetail.setTrade_state(TradeStateEnum.NOTPAY.getCode());
                thirdPartyPayDetail.setErr_msg(resultMap.get("result_msg"));
            }
        }
    }

    private void processYSBNetPayResponse(ThirdPartyPayDetail thirdPartyPayDetail, Map<String, String> resultMap, PayChannelInf qrChannelInf) {
        if (resultMap != null) {
            Map<String, String> signCheckmap = new LinkedHashMap<>();
            String respSign = resultMap.remove("mac");
            signCheckmap.put("merchantId", resultMap.get("merchantId"));
            signCheckmap.put("responseMode", resultMap.get("responseMode"));
            signCheckmap.put("orderId", resultMap.get("orderId"));
            signCheckmap.put("currencyType", resultMap.get("currencyType"));
            signCheckmap.put("amount", resultMap.get("amount"));
            signCheckmap.put("returnCode", resultMap.get("returnCode"));
            signCheckmap.put("returnMessage", resultMap.get("returnMessage"));
            signCheckmap.put("merchantKey", qrChannelInf.getSecret_key());
            String respSignCheck = MD5Util.md5Sign(signCheckmap, "UTF-8");
            if (!respSignCheck.equals(respSign)) {
                log.error("yinshengbao netpay sign check error : " + respSignCheck + " , " + respSign);
                return;
            }
            if ("0000".equals(resultMap.get("returnCode"))) {
                thirdPartyPayDetail.setTrade_state(TradeStateEnum.SUCCESS.getCode());
            } else if ("0009".equals(resultMap.get("returnCode"))) {
                thirdPartyPayDetail.setTrade_state(TradeStateEnum.REFUND.getCode());
            } else {
                thirdPartyPayDetail.setTrade_state(TradeStateEnum.NOTPAY.getCode());
                thirdPartyPayDetail.setErr_msg(resultMap.get("result_msg"));
            }
        }
    }

    private void queryNetPay(ThirdPartyPayDetail thirdPartyPayDetail, PayChannelInf qrChannelInf) {
        HashMap<String, String> params = new HashMap<>();
        params.put("merchantId", qrChannelInf.getChannel_mcht_no());
        params.put("orderId", thirdPartyPayDetail.getOut_trade_no());
        params.put("merchantKey", qrChannelInf.getSecret_key());
        params.put("mac", MD5Util.md5Sign(params, "utf-8"));
        params.remove("merchantKey");
        String stringResult = HttpUtility.getData(YINSHENGBAO_NETPAY_QUERY_URL, params);
        String[] results = stringResult.split("\\|");
        if ("0000".equals(results[3])) {
            if ("0000".equals(results[5])) {
                thirdPartyPayDetail.setTrade_state(TradeStateEnum.SUCCESS.getCode());
            }
        }
    }

    private void queryQuickPay(ThirdPartyPayDetail thirdPartyPayDetail, PayChannelInf qrChannelInf) {
        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", qrChannelInf.getChannel_mcht_no());
        params.put("orderNo", thirdPartyPayDetail.getOut_trade_no());
        params.put("mac", MD5Util.md5Sign(params, qrChannelInf.getSecret_key(), "utf-8"));
        String jsonResult = HttpUtility.getData(Environment.YINSHENGBAO_QUICK_QUERY_URL.getBaseUrl(), params);
        Map resultMap = JsonUtil.gsonParseJson(jsonResult);
        if ("0000".equals(resultMap.get("result_code"))) {
            if (resultMap.get("status").equals("00")) {
                thirdPartyPayDetail.setTrade_state(TradeStateEnum.SUCCESS.getCode());
            }
            if (resultMap.get("status").equals("20")) {
                thirdPartyPayDetail.setTrade_state(TradeStateEnum.PAYERROR.getCode());
            }
        }
    }


    public ThirdPartyPayDetail doOrderQuery(ThirdPartyPayDetail thirdPartyPayDetail, PayChannelInf qrChannelInf) {
        if (thirdPartyPayDetail != null) {
            if (thirdPartyPayDetail.getTrade_source().equals(TradeSource.QUICKPAY.getCode())) {
                queryQuickPay(thirdPartyPayDetail, qrChannelInf);
            } else {
                queryNetPay(thirdPartyPayDetail, qrChannelInf);
            }
        }
        return thirdPartyPayDetail;
    }

    @Override
    public Set<TradeSource> supportedThirdPartyTradeSource() {
        return SetUtil.toSet(TradeSource.QUICKPAY, TradeSource.NETPAY);
    }

    public ThirdPartyPayDetail acceptThirdPartyPayNotify(Map<String, String> resultMap) {
        if (resultMap.get("__TYPE").equals("quickpay")) {
            ThirdPartyPayDetail payDetail = this.thirdPartyPayDetailDao.getById(resultMap.get("orderNo"));
            PayChannelInf qrChannelInf = this.merchantInfDaoImpl.getChannelInf(payDetail.getChannel_id(), payDetail.getMch_id());
            this.processYSBQuickPayResponse(payDetail, resultMap, qrChannelInf);
            return payDetail;
        }
        if (resultMap.get("__TYPE").equals("netpay")) {
            ThirdPartyPayDetail payDetail = this.thirdPartyPayDetailDao.getById(resultMap.get("orderId"));
            PayChannelInf qrChannelInf = this.merchantInfDaoImpl.getChannelInf(payDetail.getChannel_id(), payDetail.getMch_id());
            this.processYSBNetPayResponse(payDetail, resultMap, qrChannelInf);
            return payDetail;
        }
        throw new RuntimeException("__TYPE not set");
    }
}
