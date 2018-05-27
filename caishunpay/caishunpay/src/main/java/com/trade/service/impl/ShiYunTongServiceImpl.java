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
import com.trade.dao.MerchantInfDao;
import com.trade.dao.ThirdPartyPayDetailDao;
import com.trade.enums.TradeSource;
import com.trade.enums.TradeStateEnum;
import com.trade.exception.RequestLimitedException;
import com.trade.service.ThirdPartyPayService;
import com.trade.util.Base64;
import com.trade.util.JsonUtil;
import com.trade.util.MD5Util;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@PayChannelImplement(channelId = "shiyuntong")
public class ShiYunTongServiceImpl implements ThirdPartyPayService {
    private static Logger log = Logger.getLogger(ShiYunTongServiceImpl.class);
    @Autowired
    private ThirdPartyPayDetailDao thirdPartyPayDetailDao;
    @Autowired
    private MerchantInfDao merchantInfDaoImpl;
    private static Map<TradeSource, String> serviceNameMap = new HashMap<>();

    static {
        serviceNameMap.put(TradeSource.WEPAY, "11");
        serviceNameMap.put(TradeSource.ALI_WAP_PAY, "22");
        serviceNameMap.put(TradeSource.ALIPAY, "24");
        serviceNameMap.put(TradeSource.QQPAY, "33");
    }

    private String toAmountString(Double amount) {
        return String.format("%.2f", amount);
    }

    @Override
    public ThirdPartyPayDetail doOrderCreate(PayRequest payRequest, MerchantInf qrcodeMcht, PayChannelInf qrChannelInf) {
        String tradeNo = UUIDGenerator.getOrderIdByUUId((int) 20);
        if (payRequest.getOrderAmount() < 10) {
            throw new RequestLimitedException("此通道订单金额不得少于1元");
        }
        if (payRequest.getOrderAmount() > 500000) {
            throw new RequestLimitedException("此通道订单金额不得高于5000元");
        }
        String orderAmount = toAmountString(payRequest.getOrderAmount() / 100.0);
        Map<String, String> params = new LinkedHashMap<>();
        params.put("orderAmount", orderAmount);
        params.put("orderId", tradeNo);
        params.put("partner", qrChannelInf.getChannel_mcht_no());
        params.put("payMethod", serviceNameMap.get(payRequest.getTradeSource()));
        params.put("payType", "syt");
        params.put("signType", "MD5");
        params.put("version", "1.0");
        params.put("sign", MD5Util.md5SignNokey(params, qrChannelInf.getSecret_key(), "UTF-8").toUpperCase());
        params.put("productName", payRequest.getGoodsName());
        params.put("notifyUrl", SysParamUtil.getParam("SHIYUNTONG_NOTIFY_URL"));
        String redirectUrl = RedirectController.makeJump(params, "post", Environment.SHIFUTONG_FRONTEND_URL.getBaseUrl(), Environment.DEFAULT_JUMP_URL.getBaseUrl());
        return ThirdPartyPayDetail.ThirdPartyPayDetailBuilder.getBuilder()
                .isSuccess(true)
                .withLocalTradeNumber(tradeNo)
                .withPayRequest(payRequest)
                .withPayChannelInf(qrChannelInf)
                .withTradeSource(payRequest.getTradeSource())
                .withTradeState(TradeStateEnum.NOTPAY)
                .withNotifyUrl(SysParamUtil.getParam("SHIYUNTONG_NOTIFY_URL"))
                .withCodeUrl(redirectUrl)
                .build();
    }

    public ThirdPartyPayDetail doOrderQuery(ThirdPartyPayDetail thirdPartyPayDetail, PayChannelInf qrChannelInf) {
        return thirdPartyPayDetail;
    }

    private void processResultMap(ThirdPartyPayDetail payDetail, Map returnMap) {
        if (returnMap != null) {
            String resultCode = StringUtil.trans2Str(returnMap.get("code"));
            String resultMsg = StringUtil.trans2Str(returnMap.get("message"));
            String orderNo = StringUtil.trans2Str(returnMap.get("orderId"));
            if (!payDetail.getOut_trade_no().equals(orderNo)) {
                log.error("paydetail not match, will not update.");
                return;
            }
            String orderStatus = returnMap.get("code").toString();
            TradeStateEnum tradeStateEnum = getTradeStateByResultId(orderStatus);
            if (orderStatus != null) {
                payDetail.setResp_code(resultCode);
                payDetail.setErr_msg(resultMsg);
                payDetail.setTrade_state(tradeStateEnum.getCode());
            } else {
                log.error("parameter is illegal, skip");
            }
        }
    }

    @Override
    public Set<TradeSource> supportedThirdPartyTradeSource() {
        return SetUtil.toSet(
                TradeSource.ALI_WAP_PAY, TradeSource.ALIPAY
        );
    }

    public ThirdPartyPayDetail acceptThirdPartyPayNotify(Map<String, String> resultMap) {
        ThirdPartyPayDetail payDetail = this.thirdPartyPayDetailDao.getById(resultMap.get("orderId"));
        try {
            PayChannelInf qrChannelInf = this.merchantInfDaoImpl.getChannelInf(payDetail.getChannel_id(), payDetail.getMch_id());
            String json = resultMap.get("rawJson");
            String sign = resultMap.get("sign");
            String b64 = Base64.encode(json.getBytes());
            String b64md5 = MD5Encrypt.getMessageDigest(b64).toLowerCase();
            String kb64md5 = qrChannelInf.getSecret_key() + b64md5;
            String verifySign = MD5Encrypt.getMessageDigest(kb64md5).toUpperCase();
            if (!sign.equals(verifySign)) {
                log.error("sign verify error, " + json + ", " + verifySign + ", " + sign);
                return payDetail;
            }
            if (!TradeStateEnum.SUCCESS.getCode().equals(payDetail.getTrade_state())) {
                processResultMap(payDetail, resultMap);
            }
            return payDetail;
        } catch (Exception e) {
            log.error("when processing notify", e);
            e.printStackTrace();
            return payDetail;
        }
    }

    private TradeStateEnum getTradeStateByResultId(String orderStatus) {
        switch (orderStatus) {
            case "000000":
                return TradeStateEnum.SUCCESS;
            default:
                return TradeStateEnum.PAYERROR;
        }
    }
}
