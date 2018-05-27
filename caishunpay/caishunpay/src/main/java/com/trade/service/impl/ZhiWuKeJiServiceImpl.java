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
import java.util.TreeMap;

@PayChannelImplement(channelId = "zhiwukeji")
public class ZhiWuKeJiServiceImpl implements ThirdPartyPayService {
    private static Logger log = Logger.getLogger(ZhiWuKeJiServiceImpl.class);
    @Autowired
    private ThirdPartyPayDetailDao thirdPartyPayDetailDao;
    @Autowired
    private MerchantInfDao merchantInfDaoImpl;
    private static Map<TradeSource, String> serviceNameMap = new HashMap<>();

    static {
        serviceNameMap.put(TradeSource.WEPAY, "1003");
        serviceNameMap.put(TradeSource.ALIPAY, "2003");
        serviceNameMap.put(TradeSource.QQPAY, "5003");
        serviceNameMap.put(TradeSource.JDPAY, "6003");
        serviceNameMap.put(TradeSource.UNIPAY_QRCODE, "3007");
    }

    private String toAmountString(Double amount) {
        return String.format("%.2f", amount);
    }

    @Override
    public ThirdPartyPayDetail doOrderCreate(PayRequest payRequest, MerchantInf qrcodeMcht, PayChannelInf qrChannelInf) {
        String tradeNo = UUIDGenerator.getOrderIdByUUId((int) 20);
        String orderAmount = toAmountString(payRequest.getOrderAmount() / 100.0);
        TreeMap<String, String> requestMap = new TreeMap<String, String>();
        requestMap.put("TRDE_CODE", "20001");
        requestMap.put("PRT_CODE", qrChannelInf.getAgtId());
        requestMap.put("VER_NO", "1.0");
        requestMap.put("MERC_ID", qrChannelInf.getChannel_mcht_no());
        requestMap.put("BIZ_CODE", serviceNameMap.get(payRequest.getTradeSource()));
        requestMap.put("ORDER_NO", tradeNo);
        requestMap.put("TXN_AMT", orderAmount);
        requestMap.put("PRO_DESC", "");
//        requestMap.put("CARD_NO", "");
//        requestMap.put("BNK_CD", "307");
//        requestMap.put("ACC_TYP", "");
        requestMap.put("NOTIFY_URL", SysParamUtil.getParam("ZHIWUKEJI_NOTIFY_URL"));
        String nonce_str = StringUtil.getRandom(16);
        requestMap.put("NON_STR", nonce_str);
        requestMap.put("TM_SMP", System.currentTimeMillis() + "");
        requestMap.put("SIGN_TYP", "MD5");
        requestMap.put("SIGN_DAT", MD5Util.md5SignZhiWu(requestMap, qrChannelInf.getSecret_key()));
        String keyValue = JsonUtil.buildJson4Map(requestMap);
        String jsonResult = HttpUtility.postData(Environment.ZHIWUKEJI_PAY_URL, keyValue);
        Map returnMap = (Map) JsonUtil.parseJson(jsonResult);
        String resultCode = StringUtil.trans2Str(returnMap.get("RETURNCODE"));
        String resultMsg = StringUtil.trans2Str(returnMap.get("RETURNCON"));
        String codeUrl = StringUtil.trans2Str(returnMap.get("QR_CODE"));
        Boolean success = "0000".equals(resultCode);
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
        detail.setResp_code(resultCode);
        detail.setErr_msg(resultMsg);
        return detail;
    }

    public ThirdPartyPayDetail doOrderQuery(ThirdPartyPayDetail thirdPartyPayDetail, PayChannelInf qrChannelInf) {
        if (thirdPartyPayDetail != null && StringUtil.isNotEmpty((String[]) new String[]{thirdPartyPayDetail.getCode_url()}) && !TradeStateEnum.SUCCESS.getCode().equals(thirdPartyPayDetail.getTrade_state())) {
            Environment env = Environment.ZHIWUKEJI_QUERY_URL;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("TRDE_CODE", "20002");
            params.put("PRT_CODE", qrChannelInf.getAgtId());
            params.put("VER_NO", "1.0");
            params.put("MERC_ID", qrChannelInf.getChannel_mcht_no());
            params.put("ORDER_NO", thirdPartyPayDetail.getOut_trade_no());
            String nonce_str = StringUtil.getRandom(16);
            params.put("NON_STR", nonce_str);
            params.put("TM_SMP", System.currentTimeMillis() + "");
            params.put("SIGN_TYP", "MD5");
            params.put("SIGN_DAT", MD5Util.md5SignZhiWu(params, qrChannelInf.getSecret_key()));
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
            String resultCode = StringUtil.trans2Str(returnMap.get("RETURNCODE"));
            String resultMsg = StringUtil.trans2Str(returnMap.get("RETURNCON"));
            String orderNo = StringUtil.trans2Str(returnMap.get("ORDER_NO"));
            if (!payDetail.getOut_trade_no().equals(orderNo)) {
                log.error("paydetail not match, will not update.");
                return;
            }
            String backSign = (String) returnMap.remove("SIGN_DAT");
            String checkSign = MD5Util.md5SignZhiWu(returnMap, qrChannelInf.getSecret_key(), "utf-8");
            if (!checkSign.equals(backSign)) {
                log.error("sigh check error");
            }
            String orderStatus = returnMap.get("ORD_STS").toString();
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
                TradeSource.UNIPAY_QRCODE, TradeSource.QQPAY
        );
    }

    public ThirdPartyPayDetail acceptThirdPartyPayNotify(Map<String, String> resultMap) {
        ThirdPartyPayDetail payDetail = this.thirdPartyPayDetailDao.getById(resultMap.get("ORDER_NO"));
        PayChannelInf qrChannelInf = this.merchantInfDaoImpl.getChannelInf(payDetail.getChannel_id(), payDetail.getMch_id());
        String backSign = resultMap.remove("SIGN_DAT");
        String checkSign = MD5Util.md5SignZhiWu(resultMap, (String) qrChannelInf.getSecret_key(), (String[]) new String[]{"utf-8"});
        if (!checkSign.equals(backSign)) {
            log.error("zhiwukeji check sign error:" + resultMap);
            return payDetail;
        }
        if (!TradeStateEnum.SUCCESS.getCode().equals(payDetail.getTrade_state())) {
            processResultMap(payDetail, resultMap);
        }
        return payDetail;
    }

    private TradeStateEnum getTradeStateByResultId(String orderStatus) {
        switch (orderStatus) {
            default:
                return TradeStateEnum.PAYERROR;
            case "0":
                return TradeStateEnum.NOTPAY;
            case "1":
                return TradeStateEnum.SUCCESS;
            case "2":
                return TradeStateEnum.PAYERROR;
            case "T":
                return TradeStateEnum.PAYERROR;
            case "C":
                return TradeStateEnum.NOTPAY;
        }
    }
}
