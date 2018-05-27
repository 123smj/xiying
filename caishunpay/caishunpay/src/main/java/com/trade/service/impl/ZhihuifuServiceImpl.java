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
import com.trade.util.MD5Util;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

@PayChannelImplement(channelId = "zhihuifu")
public class ZhihuifuServiceImpl implements ThirdPartyPayService {
    @Autowired
    private ThirdPartyPayDetailDao thirdPartyPayDetailDao;
    @Autowired
    private MerchantInfDao merchantInfDaoImpl;
    @Autowired
    private BankCardPayDao bankCardPayDao;

    private static Logger log = Logger.getLogger(ZhihuifuServiceImpl.class);
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    private static Map<TradeSource, String> serviceNameMap = new HashMap<>();

    static {
        serviceNameMap.put(TradeSource.QUICKPAY, "0203");
        serviceNameMap.put(TradeSource.NETPAY, "0201");
    }

    private static String getCallbackUrl(String raw) {
        if (!StringUtil.isEmpty(raw) && raw.startsWith("http")) {
            return raw;
        } else {
            return "http://gateway.ykbpay.com:8080/caishunpay-web/trade/payok.jsp";
        }
    }

    @Override
    public ThirdPartyPayDetail doOrderCreate(PayRequest payRequest, MerchantInf qrcodeMcht, PayChannelInf qrChannelInf) {
        String tradeNo = UUIDGenerator.getOrderIdByUUId((int) 20);
        HashMap<String, String> params = new HashMap<>();
        params.put("version", "1.0.0");
        params.put("txnType", "01");
        params.put("txnSubType", "00");
        params.put("bizType", "000000");
        params.put("accessType", "0");
        params.put("accessMode", "01");
        params.put("merId", qrChannelInf.getChannel_mcht_no());
        params.put("merOrderId", tradeNo);
        params.put("txnTime", DATE_FORMAT.format(new Date()));
        params.put("txnAmt", payRequest.getOrderAmount().toString());
        params.put("currency", "CNY");
        params.put("channelId", "");
        params.put("frontUrl", getCallbackUrl(payRequest.getCallback_url()));
        params.put("backUrl", SysParamUtil.getParam("ZHIHUIFU_NOTIFY_URL"));
        params.put("payType", serviceNameMap.get(payRequest.getTradeSource()));
        params.put("subject", payRequest.getGoodsName());
        params.put("body", payRequest.getGoodsName());
        params.put("merResv1", "");
        String signature = MD5Util.md5SignZhihuifu(params, qrChannelInf.getSecret_key(), "utf-8");
        params.put("signature", signature);
        params.put("signMethod", "MD5");
        params.put("subject", Base64.encodeBase64String(payRequest.getGoodsName().getBytes()));
        params.put("body", Base64.encodeBase64String(payRequest.getGoodsName().getBytes()));
        String redirectUrl = RedirectController.makeJump(params, "post", Environment.ZHIHUIFU_FRONTEND_URL.getBaseUrl(), qrChannelInf.getJump_url());
        ThirdPartyPayDetail detail = ThirdPartyPayDetail.ThirdPartyPayDetailBuilder.getBuilder()
                .isSuccess(true)
                .withLocalTradeNumber(tradeNo)
                .withPayRequest(payRequest)
                .withPayChannelInf(qrChannelInf)
                .withTradeSource(payRequest.getTradeSource())
                .withTradeState(TradeStateEnum.NOTPAY)
                .withNotifyUrl(SysParamUtil.getParam("ZHIHUIFU_NOTIFY_URL"))
                .withCodeUrl(redirectUrl)
                .build();
        return detail;
    }

    private void processRemoteReturn(ThirdPartyPayDetail thirdPartyPayDetail, Map<String, String> resultMap, PayChannelInf qrChannelInf) {
        if (resultMap != null) {
            String respSign = resultMap.remove("signature");
            resultMap.remove("signMethod");
            resultMap.put("respMsg", new String(Base64.decodeBase64(resultMap.get("respMsg").toString())));
            resultMap.put("resv", new String(Base64.decodeBase64(resultMap.get("resv").toString())));
            String respSignCheck = MD5Util.md5SignZhihuifu(resultMap, qrChannelInf.getSecret_key(), "utf-8");
            byte[] b = Base64.decodeBase64(respSign);
            byte[] bc = Base64.decodeBase64(respSignCheck);
            if (Arrays.equals(b, bc)) {
                log.error("zhihuifu sign check OK : " + respSign + ":" + respSignCheck);
            } else {
                log.error("zhihuifu sign check error : " + respSign + ":" + respSignCheck);
                return;
            }
            thirdPartyPayDetail.setResp_code(resultMap.get("respCode"));
            thirdPartyPayDetail.setErr_msg(resultMap.get("respMsg"));
            if ("1001".equals(resultMap.get("respCode"))) {
                thirdPartyPayDetail.setTime_end(DateUtil.getCurrTime());
                thirdPartyPayDetail.setTrade_state(TradeStateEnum.SUCCESS.getCode());
            }
            if ("1111".equals(resultMap.get("respCode"))) {
                thirdPartyPayDetail.setTrade_state(TradeStateEnum.NOTPAY.getCode());
            }
            if ("1003".equals(resultMap.get("respCode"))) {
                thirdPartyPayDetail.setTrade_state(TradeStateEnum.REFUND.getCode());
            }
            if ("1002".equals(resultMap.get("respCode"))) {
                thirdPartyPayDetail.setTrade_state(TradeStateEnum.PAYERROR.getCode());
            }
        }
    }

    public ThirdPartyPayDetail doOrderQuery(ThirdPartyPayDetail thirdPartyPayDetail, PayChannelInf qrChannelInf) {
        if (thirdPartyPayDetail != null) {
            HashMap<String, String> params = new HashMap<>();
            params.put("version", "1.0.0");
            params.put("txnType", "00");
            params.put("txnSubType", "01");
            params.put("merId", qrChannelInf.getChannel_mcht_no());
            params.put("merOrderId", thirdPartyPayDetail.getOut_trade_no());
            String signature = MD5Util.md5SignZhihuifu(params, qrChannelInf.getSecret_key(), "utf-8");
            params.put("signature", signature);
            params.put("signMethod", "MD5");
            String respResult = HttpUtility.getData(Environment.ZHIHUIFU_QUERY_URL.getBaseUrl(), params);
            Map<String, String> resultMap = HttpUtility.httpParam2map(respResult);
            processRemoteReturn(thirdPartyPayDetail, resultMap, qrChannelInf);
        }
        return thirdPartyPayDetail;
    }

    @Override
    public Set<TradeSource> supportedThirdPartyTradeSource() {
        return SetUtil.toSet(TradeSource.QUICKPAY, TradeSource.NETPAY);
    }

    public ThirdPartyPayDetail acceptThirdPartyPayNotify(Map<String, String> resultMap) {
        ThirdPartyPayDetail payDetail = this.thirdPartyPayDetailDao.getById(resultMap.get("merOrderId"));
        PayChannelInf qrChannelInf = this.merchantInfDaoImpl.getChannelInf(payDetail.getChannel_id(), payDetail.getMch_id());
        this.processRemoteReturn(payDetail, resultMap, qrChannelInf);
        return payDetail;
    }
}
