/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  com.account.service.TradeMchtAccountService
 *  com.gy.system.Environment
 *  com.gy.system.SysParamUtil
 *  com.gy.util.DateUtil
 *  com.gy.util.HttpUtility
 *  com.gy.util.StringUtil
 *  com.gy.util.UUIDGenerator
 *  com.trade.bean.QuickpayBean
 *  com.trade.bean.WxpayScanCode
 *  com.trade.bean.own.QrcodeChannelInf
 *  com.trade.bean.own.QrcodeMchtInfo
 *  com.trade.bean.own.QuickpayParam
 *  com.trade.bean.own.TradeParam
 *  com.trade.bean.response.QuickpayResponse
 *  com.trade.bean.response.ResponseCode
 *  com.trade.dao.QrcodeMchtInfoDao
 *  com.trade.dao.QuickpayDao
 *  com.trade.dao.WxNativeDao
 *  com.trade.enums.CardTypeEnum
 *  com.trade.enums.ResponseEnum
 *  com.trade.enums.TradeSourceEnum
 *  com.trade.enums.TradeStateEnum
 *  com.trade.service.NetpayService
 *  com.trade.service.TradeService
 *  com.trade.service.impl.CommonService
 *  com.trade.service.netpayimpl.NetpayHandlerService
 *  com.trade.util.GuangdaUtil
 *  com.trade.util.JsonUtil
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.trade.service.impl;

import com.gy.system.Environment;
import com.gy.system.SysParamUtil;
import com.gy.util.*;
import com.trade.annotations.PayChannelImplement;
import com.trade.bean.BankCardPay;
import com.trade.bean.ThirdPartyPayDetail;
import com.trade.bean.own.BankCardPayRequest;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.own.PayChannelInf;
import com.trade.bean.own.PayRequest;
import com.trade.dao.BankCardPayDao;
import com.trade.dao.MerchantInfDao;
import com.trade.dao.ThirdPartyPayDetailDao;
import com.trade.enums.*;
import com.trade.service.BankCardPayService;
import com.trade.service.ThirdPartyPayService;
import com.trade.util.MD5Util;
import com.trade.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@PayChannelImplement(channelId = "haibei")
// 实现三方支付接口和网银支付接口
public class HaibeipayServiceImpl implements ThirdPartyPayService, BankCardPayService {
    // 三方支付详细
    @Autowired
    private ThirdPartyPayDetailDao thirdPartyPayDetailDao;
    //银行卡支付账目数据库操作Interface
    @Autowired
    private BankCardPayDao bankCardPayDao;
    //商人信息实现接口
    @Autowired
    private MerchantInfDao merchantInfDaoImpl;
    private static Logger log = Logger.getLogger(HaibeipayServiceImpl.class);

    private enum PayTypeEnum {
        wechat("2", "\u5fae\u4fe1\u626b\u7801\u652f\u4ed8"),//微信扫码支付
        ali("4", "\u652f\u4ed8\u5b9d\u626b\u7801\u652f\u4ed8"),//支付宝扫码支付
        qqpay("8", "QQ\u94b1\u5305\u624b\u673a\u626b\u7801"),//QQ钱包手机扫码
        netpay("11", "\u7f51\u94f6B2C\u652f\u4ed8");//网银B2C支付

        private String value;
        private String desc;

        PayTypeEnum(String value, String desc) {
            this.value = value;
            this.desc = (String) desc;
        }

        public String toString() {
            return this.value;
        }
    }
    /**
     * 三方支付接口
     */
    @Override
    //創建訂單號
    public ThirdPartyPayDetail doOrderCreate(PayRequest payRequest, MerchantInf qrcodeMcht, PayChannelInf qrChannelInf) {
        String tradeNo = UUIDGenerator.getOrderIdByUUId((int) 20);
        String payType = PayTypeEnum.wechat.toString();

        if (TradeSource.ALIPAY.equals(payRequest.getTradeSource())) {
            payType = PayTypeEnum.ali.toString();

        } else if (TradeSource.WEPAY.equals(payRequest.getTradeSource())) {
            payType = PayTypeEnum.wechat.toString();

        } else if (TradeSource.QQPAY.equals(payRequest.getTradeSource())) {
            payType = PayTypeEnum.qqpay.toString();
        }
        //将对接的请求参数放入map集合中
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("merchantNo", qrChannelInf.getChannel_mcht_no());
        params.put("orderAmount", String.valueOf(payRequest.getOrderAmount()));
        params.put("orderNo", tradeNo);
        params.put("notifyUrl", SysParamUtil.getParam((String) "HAIBEI_NOTIFY_URL"));
        params.put("callbackUrl", payRequest.getCallback_url());
        params.put("payType", payType);
        params.put("productName", payRequest.getGoodsName());
        params.put("productDesc", payRequest.getGoodsName());
        params.put("remark", payRequest.getGoodsName());
        params.put("sign", MD5Util.getMd5SignNoKeyByMap(params, qrChannelInf.getSecret_key(), (String[]) new String[]{"utf-8"}));
        String keyValue = MD5Util.map2HttpParam(params);
        String jsonResult = HttpUtility.postData(Environment.HAIBEI_PAY_URL, keyValue);
        Map returnMap = (Map) JsonUtil.parseJson(jsonResult);
        //解析响应参数
        String resultCode = StringUtil.trans2Str(returnMap.get("status"));
        String resultMsg = StringUtil.trans2Str(returnMap.get("errMsg"));
        String codeUrl = StringUtil.trans2Str(returnMap.get("payUrl"));
         //使用枚舉的方式
        Boolean success = "T".equals(resultCode);
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
                .withNotifyUrl(SysParamUtil.getParam("HAIBEI_NOTIFY_URL"))
                .withCodeUrl(codeUrl)
                .build();
        detail.setResp_code(resultCode);
        detail.setResult_code(StringUtil.trans2Str(returnMap.get("errCode")));
        detail.setErr_msg(resultMsg);
        return detail;
    }
    //   结果查询实现此方法
    public ThirdPartyPayDetail doOrderQuery(ThirdPartyPayDetail thirdPartyPayDetail, PayChannelInf qrChannelInf) {
        if (thirdPartyPayDetail != null && StringUtil.isNotEmpty((String[]) new String[]{thirdPartyPayDetail.getCode_url()}) && !TradeStateEnum.SUCCESS.getCode().equals(thirdPartyPayDetail.getTrade_state())) {
            Environment env = Environment.HAIBEI_QUERY_URL;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("merchantNo", qrChannelInf.getChannel_mcht_no());
            params.put("orderNo", thirdPartyPayDetail.getOut_trade_no());
            params.put("sign", MD5Util.getMd5SignNoKeyByMap(params, (String) qrChannelInf.getSecret_key(), (String[]) new String[]{"utf-8"}));
            String keyValue = MD5Util.map2HttpParam(params);
            String jsonResult = HttpUtility.postData((Environment) env, (String) keyValue);
            Map returnMap = (Map) JsonUtil.parseJson((String) jsonResult);
            if (returnMap != null) {
                String resultCode = StringUtil.trans2Str(returnMap.get("status"));
                String resultMsg = StringUtil.trans2Str(returnMap.get("errMsg"));
                if ("T".equals(resultCode)) {
                    String orderStatus = (String) returnMap.get("orderStatus");
                    String backSign = (String) returnMap.remove("sign");
                    String checkSign = MD5Util.getMd5SignNoKeyByMap((Map) returnMap, (String) qrChannelInf.getSecret_key(), (String[]) new String[]{"utf-8"});
                    if (!checkSign.equals(backSign)) {
                        return thirdPartyPayDetail;
                    }
                    if ("SUCCESS".equals(orderStatus)) {
                        thirdPartyPayDetail.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                    } else if ("INIT".equals(orderStatus)) {
                        thirdPartyPayDetail.setTrade_state(TradeStateEnum.NOTPAY.getCode());
                    } else if ("REFUND".equals(orderStatus)) {
                        thirdPartyPayDetail.setTrade_state(TradeStateEnum.REFUND.getCode());
                    } else {
                        thirdPartyPayDetail.setTrade_state(TradeStateEnum.PAYERROR.getCode());
                      }
                    thirdPartyPayDetail.setPay_result("0");
                    thirdPartyPayDetail.setOut_transaction_id((String) returnMap.get("wtfOrderNo"));
                    thirdPartyPayDetail.setTime_end(DateUtil.getCurrTime());
                }
            }
        }
        return thirdPartyPayDetail;
    }
    //接收支付结果通知实现此方法
    public ThirdPartyPayDetail acceptThirdPartyPayNotify(Map<String, String> resultMap) {
        ThirdPartyPayDetail payDetail = this.thirdPartyPayDetailDao.getById(resultMap.get("orderNo"));
        PayChannelInf qrChannelInf = this.merchantInfDaoImpl.getChannelInf(payDetail.getChannel_id(), payDetail.getMch_id());
        String backSign = resultMap.remove("sign");
        String checkSign = MD5Util.getMd5SignNoKeyByMap(resultMap, (String) qrChannelInf.getSecret_key(), (String[]) new String[]{"utf-8"});
        if (!checkSign.equals(backSign)) {
            log.error("haibei check sign error:" + resultMap);
            return payDetail;
        }
        if (!TradeStateEnum.SUCCESS.getCode().equals(payDetail.getTrade_state()) && "SUCCESS".equals(resultMap.get("orderStatus"))) {
            payDetail.setTrade_state(TradeStateEnum.SUCCESS.getCode());
            payDetail.setTime_end(DateUtil.getCurrTime());
            return payDetail;
        }
        payDetail.setOut_transaction_id(resultMap.get("wtfOrderNo"));
        return payDetail;
    }
    //通过此接口，返回支持的接口类型
    @Override
    public Set<TradeSource> supportedThirdPartyTradeSource() {
        return SetUtil.toSet(TradeSource.QQPAY);
    }



    /***
     * 网银支付接口
     * doOrderQuery
     */


    public BankCardPay doOrderCreate(BankCardPayRequest reqParam,
                                     PayChannelInf qrChannelInf) {
        BankCardPay tradeInfo = new BankCardPay();
        String tradeNo = UUIDGenerator.getOrderIdByUUId((String) "qk");
        tradeInfo.setOut_trade_no(tradeNo);
        tradeInfo.setVersion("1.0.1");
        tradeInfo.setTime_start(DateUtil.getCurrTime());
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            if (CardTypeEnum.CREDIT.getCode().equals(reqParam.getCardType())) {
                params.put("cardType", "02");
            } else {
                params.put("cardType", "1");
            }
            params.put("merchantNo", qrChannelInf.getChannel_mcht_no());
            params.put("orderAmount", reqParam.getOrderAmount().toString());
            params.put("orderNo", tradeNo);
            params.put("notifyUrl", SysParamUtil.getParam((String) "HAIBEI_NETPAY_NOTIFY_URL"));
            params.put("callbackUrl", reqParam.getCallbackUrl());
            params.put("payType", PayTypeEnum.netpay.toString());
            params.put("productName", reqParam.getGoodsName());
            params.put("productDesc", reqParam.getGoodsName());
            params.put("remark", reqParam.getGoodsName());
            params.put("ip", reqParam.getRemoteAddr());
            params.put("currencyType", "CNY");
            params.put("bankName", StringUtil.haibeiBankMap.get(reqParam.getBankSegment()));
            params.put("sign", MD5Util.getMd5SignNoKeyByMap(params, (String) qrChannelInf.getSecret_key(), (String[]) new String[]{"utf-8"}));
            String keyValue = MD5Util.map2HttpParam(params);
            String jsonResult = HttpUtility.postData((Environment) Environment.HAIBEI_PAY_URL, (String) keyValue);
            if (StringUtil.isEmpty((String) jsonResult)) {
                tradeInfo.setTrade_state(TradeStateEnum.PREERROR.getCode());
                tradeInfo.setResp_code(ResponseEnum.BACK_EXCEPTION.getCode());
                tradeInfo.setMessage(ResponseEnum.BACK_EXCEPTION.getMemo());
                return tradeInfo;
            }
            Map returnMap = (Map) JsonUtil.parseJson((String) jsonResult);
            String resultCode = StringUtil.trans2Str(returnMap.get("status"));
            String resultMsg = StringUtil.trans2Str(returnMap.get("errMsg"));
            //请求成功
            if ("T".equals(resultCode)) {
                tradeInfo.setPayUrl((String) returnMap.get("payUrl"));
                tradeInfo.setTrade_state(TradeStateEnum.NOTPAY.getCode());
                tradeInfo.setResp_code("00");
                tradeInfo.setResult_code("00");
                tradeInfo.setMessage("\u8bf7\u6c42\u6210\u529f");
            } else {
                tradeInfo.setTrade_state(TradeStateEnum.PAYERROR.getCode());
                tradeInfo.setResult_code(String.valueOf(resultCode) + "-" + (String) returnMap.get("errCode"));
                tradeInfo.setResp_code(String.valueOf(resultCode) + "-" + (String) returnMap.get("errCode"));
                tradeInfo.setMessage(resultMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tradeInfo;
    }
    public BankCardPay doOrderQuery(BankCardPay BankCardPay, PayChannelInf qrChannelInf) {
        if (BankCardPay != null && !TradeStateEnum.SUCCESS.getCode().equals(BankCardPay.getTrade_state())) {
            Environment env = Environment.HAIBEI_QUERY_URL;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("merchantNo", qrChannelInf.getChannel_mcht_no());
            params.put("orderNo", BankCardPay.getOut_trade_no());
            params.put("sign", MD5Util.getMd5SignNoKeyByMap(params, (String) qrChannelInf.getSecret_key(), (String[]) new String[]{"utf-8"}));
            String keyValue = MD5Util.map2HttpParam(params);

            String jsonResult = HttpUtility.postData((Environment) env, (String) keyValue);
            Map returnMap = (Map) JsonUtil.parseJson((String) jsonResult);
            if (returnMap != null) {
                String resultCode = StringUtil.trans2Str(returnMap.get("status"));
                String resultMsg = StringUtil.trans2Str(returnMap.get("errMsg"));
                if ("T".equals(resultCode)) {
                    String orderStatus = (String) returnMap.get("orderStatus");
                    String backSign = (String) returnMap.remove("sign");
                    String checkSign = MD5Util.getMd5SignNoKeyByMap((Map) returnMap, (String) qrChannelInf.getSecret_key(), (String[]) new String[]{"utf-8"});
                    if (!checkSign.equals(backSign)) {
                        return BankCardPay;
                    }
                    if ("SUCCESS".equals(orderStatus)) {
                        BankCardPay.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                    } else if ("INIT".equals(orderStatus)) {
                        BankCardPay.setTrade_state(TradeStateEnum.NOTPAY.getCode());
                    } else if ("REFUND".equals(orderStatus)) {
                        BankCardPay.setTrade_state(TradeStateEnum.REFUND.getCode());
                    } else {
                        BankCardPay.setTrade_state(TradeStateEnum.PAYERROR.getCode());
                    }
                    BankCardPay.setPay_result("0");
                    BankCardPay.setOut_transaction_id((String) returnMap.get("wtfOrderNo"));
                    BankCardPay.setTime_end(DateUtil.getCurrTime());
                }
            }
        }
        return BankCardPay;
    }

    public BankCardPay acceptBankCardPayNotify(Map<String, String> resultMap) {
        BankCardPay BankCardPay = this.bankCardPayDao.getById(resultMap.get("orderNo"));
        String tradeStatus = resultMap.get("orderStatus");
        PayChannelInf qrChannelInf = this.merchantInfDaoImpl.getChannelInf(BankCardPay.getChannel_id(), BankCardPay.getMch_id());
        String backSign = resultMap.remove("sign");
        String checkSign = MD5Util.getMd5SignNoKeyByMap(resultMap, (String) qrChannelInf.getSecret_key(), (String[]) new String[]{"utf-8"});
        if (!checkSign.equals(backSign)) {
            log.error("haibei check_sign_error: " + resultMap);
            return BankCardPay;
        }
        if (!TradeStateEnum.SUCCESS.getCode().equals(BankCardPay.getTrade_state())) {
            if ("SUCCESS".equals(tradeStatus)) {
                BankCardPay.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                BankCardPay.setTime_end(DateUtil.getCurrTime());
            } else {
                BankCardPay.setTrade_state(TradeStateEnum.PAYERROR.getCode());
            }
            BankCardPay.setTransaction_id(resultMap.get("wtfOrderNo"));
        }
        return BankCardPay;
    }



}
