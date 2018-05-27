package com.trade.service.impl;

import com.gy.system.Environment;
import com.gy.system.SysParamUtil;
import com.gy.util.*;
import com.trade.annotations.PayChannelImplement;
import com.trade.bean.ThirdPartyPayDetail;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.own.PayChannelInf;
import com.trade.bean.own.PayRequest;
import com.trade.dao.MerchantInfDao;
import com.trade.dao.ThirdPartyPayDetailDao;
import com.trade.enums.TradeSource;
import com.trade.enums.TradeStateEnum;
import com.trade.service.ThirdPartyPayService;
import com.trade.util.JsonUtil;
import com.trade.util.MD5Util;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@PayChannelImplement(channelId = "wanzhongyunfu")
public class WanZhongYunFuServiceImpl implements ThirdPartyPayService {
    private static Logger log = Logger.getLogger(WanZhongYunFuServiceImpl.class);
    @Autowired
    private ThirdPartyPayDetailDao thirdPartyPayDetailDao;
    @Autowired
    private MerchantInfDao merchantInfDaoImpl;
    private static Map<TradeSource, String> serviceNameMap = new HashMap<>();

    static {
        serviceNameMap.put(TradeSource.WEPAY, "weixin.native");
        serviceNameMap.put(TradeSource.ALIPAY, "alipay.native");
        serviceNameMap.put(TradeSource.QQPAY, "qqpay.native");
    }

    @Override
    public ThirdPartyPayDetail doOrderCreate(PayRequest payRequest, MerchantInf qrcodeMcht, PayChannelInf qrChannelInf) {
        String tradeNo = UUIDGenerator.getOrderIdByUUId((int) 20);
        HashMap<String, Object> params = new HashMap<>();
        params.put("appId", qrChannelInf.getChannel_mcht_no());
        params.put("timestamp", ((Long) (DateUtil.getCurrentTimeStamp() / 1000)).toString());
        params.put("nonce", StringUtil.getRandom(32));
        params.put("service", serviceNameMap.get(payRequest.getTradeSource()));
        params.put("orderNo", tradeNo);
        params.put("totalAmount", payRequest.getOrderAmount().toString());
        params.put("clientIp", payRequest.getClientIp());
        params.put("notifyUrl", SysParamUtil.getParam("WANZHONGYUNFU_NOTIFY_URL"));
        params.put("body", payRequest.getGoodsName());
        params.put("sign", MD5Util.getMd5SignByMap(params, qrChannelInf.getSecret_key(), "utf-8"));
        String keyValue = JsonUtil.buildJson4Map(params);
        String jsonResult = HttpUtility.postData(Environment.WANZHONGYUNFU_PAY_URL, keyValue);
        Map returnMap = (Map) JsonUtil.parseJson(jsonResult);
        String resultCode = StringUtil.trans2Str(returnMap.get("errorCode"));
        String resultMsg = StringUtil.trans2Str(returnMap.get("errorMsg"));
        String platformId = StringUtil.trans2Str(returnMap.get("platformOrderNo"));
        String bankId = StringUtil.trans2Str(returnMap.get("outBankTradeOrderNo"));
        String codeUrl = StringUtil.trans2Str(returnMap.get("payInfo"));
        Boolean success = (Boolean) returnMap.get("success") && returnMap.get("status").equals(0);
        TradeStateEnum tradeStateEnum;
        if (success) {
            tradeStateEnum = TradeStateEnum.NOTPAY;
        } else {
            tradeStateEnum = TradeStateEnum.PAYERROR;
        }
        ThirdPartyPayDetail detail = ThirdPartyPayDetail.ThirdPartyPayDetailBuilder.getBuilder()
                .isSuccess(success)
                .withLocalTradeNumber(tradeNo)
                .withPayRequest(payRequest)
                .withPayChannelInf(qrChannelInf)
                .withTradeSource(payRequest.getTradeSource())
                .withTradeState(tradeStateEnum)
                .withNotifyUrl(SysParamUtil.getParam("WANZHONGYUNFU_NOTIFY_URL"))
                .withCodeUrl(codeUrl)
                .build();
        detail.setOut_transaction_id(platformId);
        detail.setBank_billno(bankId);
        detail.setResp_code(resultCode);
        detail.setErr_msg(resultMsg);
        return detail;
    }

    public ThirdPartyPayDetail doOrderQuery(ThirdPartyPayDetail thirdPartyPayDetail, PayChannelInf qrChannelInf) {
        if (thirdPartyPayDetail != null && StringUtil.isNotEmpty((String[]) new String[]{thirdPartyPayDetail.getCode_url()}) && !TradeStateEnum.SUCCESS.getCode().equals(thirdPartyPayDetail.getTrade_state())) {
            Environment env = Environment.WANZHONGYUNFU_QUERY_URL;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("appId", qrChannelInf.getChannel_mcht_no());
            params.put("nonce", StringUtil.getRandom(32));
            params.put("timestamp", ((Long) (DateUtil.getCurrentTimeStamp() / 1000)).toString());
            params.put("orderNo", thirdPartyPayDetail.getOut_trade_no());
            params.put("sign", MD5Util.getMd5SignByMap(params, qrChannelInf.getSecret_key(), (String[]) new String[]{"utf-8"}));
            String keyValue = JsonUtil.buildJson4Map(params);
            String jsonResult = HttpUtility.postData((Environment) env, (String) keyValue);
            Map returnMap = (Map) JsonUtil.parseJson(jsonResult);
            processResultMap(thirdPartyPayDetail, returnMap);
        }
        return thirdPartyPayDetail;
    }

    private void processResultMap(ThirdPartyPayDetail payDetail, Map returnMap) {
        PayChannelInf qrChannelInf = this.merchantInfDaoImpl.getChannelInf(payDetail.getChannel_id(), payDetail.getMch_id());
        if (returnMap != null) {
            String resultCode = StringUtil.trans2Str(returnMap.get("errorCode"));
            String resultMsg = StringUtil.trans2Str(returnMap.get("errorMsg"));
            String platformId = StringUtil.trans2Str(returnMap.get("platformTradeNo"));
            if (StringUtil.isEmpty(platformId)) {
                platformId = StringUtil.trans2Str(returnMap.get("platformOrderNo"));
            }
            if (!payDetail.getOut_transaction_id().equals(platformId)) {
                log.error("paydetail not match, will not update.");
                return;
            }
            String backSign = (String) returnMap.remove("sign");
            String checkSign = MD5Util.getMd5SignByMap(returnMap, qrChannelInf.getSecret_key(), "utf-8");
            if (!checkSign.equals(backSign)) {
                    log.error("sigh check error");
                }
            Integer orderStatus = (Integer) returnMap.get("tradeState");
            if (orderStatus == null) {
                orderStatus = Integer.valueOf(returnMap.get("status").toString());
            }
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
                TradeSource.WEPAY
//                TradeSource.ALIPAY,
//                TradeSource.QQPAY
        );
    }

    public ThirdPartyPayDetail acceptThirdPartyPayNotify(Map<String, String> resultMap) {
        ThirdPartyPayDetail payDetail = this.thirdPartyPayDetailDao.getById(resultMap.get("orderNo"));
        PayChannelInf qrChannelInf = this.merchantInfDaoImpl.getChannelInf(payDetail.getChannel_id(), payDetail.getMch_id());
        String backSign = resultMap.remove("sign");
        String checkSign = MD5Util.getMd5SignByMap(resultMap, (String) qrChannelInf.getSecret_key(), (String[]) new String[]{"utf-8"});
        if (!checkSign.equals(backSign)) {
            log.error("wanzhongyunfu check sign error:" + resultMap);
            return payDetail;
        }
        if (!TradeStateEnum.SUCCESS.getCode().equals(payDetail.getTrade_state())) {
            processResultMap(payDetail, resultMap);
        }
        return payDetail;
    }

    private TradeStateEnum getTradeStateByResultId(Integer orderStatus) {
        if (orderStatus == 3) {
            return TradeStateEnum.SUCCESS;
        } else if (orderStatus == 1) {
            return TradeStateEnum.NOTPAY;
        } else if (orderStatus == 2) {
            return TradeStateEnum.CHECKING;
        } else if (orderStatus == 4) {
            return TradeStateEnum.PAYERROR;
        } else if (orderStatus == 5 || orderStatus == 6) {
            return TradeStateEnum.REFUND;
        } else if (orderStatus == 7) {
            return TradeStateEnum.CLOSED;
        } else {
            return TradeStateEnum.PAYERROR;
        }
    }
}
