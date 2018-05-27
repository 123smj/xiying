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
import com.trade.enums.CardTypeEnum;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeSource;
import com.trade.enums.TradeStateEnum;
import com.trade.service.BankCardPayService;
import com.trade.service.ThirdPartyPayService;
import com.trade.util.JsonUtil;
import com.trade.util.MD5Util;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@PayChannelImplement(channelId = "CS")
//三方支付接口  网银支付接口
public class CSpayServiceImpl implements ThirdPartyPayService, BankCardPayService {
    @Autowired
    private ThirdPartyPayDetailDao thirdPartyPayDetailDao;
    @Autowired
    private MerchantInfDao merchantInfDaoImpl;
    @Autowired
    private BankCardPayDao bankCardPayDao;
    private static Logger log = Logger.getLogger(CSpayServiceImpl.class);

    private enum PayTypeEnum {
        wechat("2", "\u5fae\u4fe1\u626b\u7801\u652f\u4ed8"),//微信扫码支付
        balance("NO","余额代付"),
        limit("DZ","余额代付");

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
    /***
     * 发起支付实现此方法
     */
    @Override
    //創建訂單號
    public ThirdPartyPayDetail doOrderCreate(PayRequest payRequest, MerchantInf qrcodeMcht, PayChannelInf qrChannelInf) {
        String tradeNo = UUIDGenerator.getOrderIdByUUId((int) 20);
        String payType = PayTypeEnum.wechat.toString();

        if (TradeSource.ALIPAY.equals(payRequest.getTradeSource())) {
            payType = PayTypeEnum.balance.toString();

        } else if (TradeSource.WEPAY.equals(payRequest.getTradeSource())) {
            payType = PayTypeEnum.limit.toString();

        }
        HashMap<String,String> params=new HashMap<String ,String >();
        params.put("mch_id",qrChannelInf.getChannel_mcht_no());
        params.put("trans_money",String.valueOf(qrcodeMcht.getAliwap_fee_value()));//交易金额
        params.put("service", payType);
        params.put("out_trade_no",qrcodeMcht.getChannelMchtNo());//商户交易单号
        params.put("account_name",qrcodeMcht.getMchtName());
        params.put("bank_card",qrcodeMcht.getCard_name());
        params.put("bank_name",qrcodeMcht.getBank_name());
        params.put("bank_linked",payRequest.getBankcardnum());//联行号
        params.put("phoneNo",qrcodeMcht.getPhone());
        params.put("idcard",payRequest.getGoodsName());
        params.put("sign",MD5Util.getMd5SignNoKeyByMap(params, qrChannelInf.getSecret_key(), (String[]) new String[]{"utf-8"}));
        String keyValue = JsonUtil.buildJson4Map(params);
        String jsonResult = HttpUtility.postData(Environment.CS_PAY_URL, keyValue);
        Map returnMap = (Map) JsonUtil.parseJson(jsonResult);
        String resultCode = StringUtil.trans2Str(returnMap.get("ret_code"));//返回码
        String resultMsg = StringUtil.trans2Str(returnMap.get("ret_message"));//返回码说明

        String platformId = StringUtil.trans2Str(returnMap.get("orderNo"));//交易单号
        String bankId = StringUtil.trans2Str(returnMap.get("tradeMessage"));//代付说明
        String codeUrl = StringUtil.trans2Str(returnMap.get("tradeStatus"));//代付状态
        Boolean success = (Boolean) returnMap.get("success") && returnMap.get("ret_code").equals(0);
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
                .withNotifyUrl(SysParamUtil.getParam("CS_PAY_URL"))
                .withCodeUrl(codeUrl)
                .build();
        detail.setOut_transaction_id(platformId);
        detail.setBank_billno(bankId);
        detail.setResp_code(resultCode);
        detail.setErr_msg(resultMsg);
        return detail;
    }

    /**
     * 结果查询实现此方法
     */
    public ThirdPartyPayDetail doOrderQuery(ThirdPartyPayDetail thirdPartyPayDetail, PayChannelInf qrChannelInf) {
        if (thirdPartyPayDetail != null && StringUtil.isNotEmpty((String[]) new String[]{thirdPartyPayDetail.getCode_url()}) && !TradeStateEnum.SUCCESS.getCode().equals(thirdPartyPayDetail.getTrade_state())) {
            Environment env = Environment.CS_DFTRADE_URL;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("mch_id", qrChannelInf.getChannel_mcht_no());
            params.put("out_trade_no", thirdPartyPayDetail.getOut_trade_no());
            params.put("sign", MD5Util.getMd5SignNoKeyByMap(params, (String) qrChannelInf.getSecret_key(), (String[]) new String[]{"utf-8"}));
            String keyValue = MD5Util.map2HttpParam(params);
            String jsonResult = HttpUtility.postData((Environment) env, (String) keyValue);
            Map returnMap = (Map) JsonUtil.parseJson((String) jsonResult);
            if (returnMap != null) {
                String resultCode = StringUtil.trans2Str(returnMap.get("ret_code"));
                String resultMsg = StringUtil.trans2Str(returnMap.get("ret_message"));
                if ("T".equals(resultCode)) {
                    String payStatus = (String) returnMap.get("payStatus");
                    String backSign = (String) returnMap.remove("sign");
                    String checkSign = MD5Util.getMd5SignNoKeyByMap((Map) returnMap, (String) qrChannelInf.getSecret_key(), (String[]) new String[]{"utf-8"});
                    if (!checkSign.equals(backSign)) {
                        return thirdPartyPayDetail;
                    }
                    if ("SUCCESS".equals(payStatus)) {
                        thirdPartyPayDetail.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                    } else if ("INIT".equals(payStatus)) {
                        thirdPartyPayDetail.setTrade_state(TradeStateEnum.NOTPAY.getCode());
                    } else if ("REFUND".equals(payStatus)) {
                        thirdPartyPayDetail.setTrade_state(TradeStateEnum.REFUND.getCode());
                    } else {
                        thirdPartyPayDetail.setTrade_state(TradeStateEnum.PAYERROR.getCode());
                    }
                    thirdPartyPayDetail.setPay_result("0");
                    thirdPartyPayDetail.setOut_transaction_id((String) returnMap.get("csret_code"));
                    thirdPartyPayDetail.setTime_end(DateUtil.getCurrTime());
                }
            }
        }
        return thirdPartyPayDetail;
    }
    /***
     * 接收支付结果通知实现此方法
     */
    public ThirdPartyPayDetail acceptThirdPartyPayNotify(Map<String, String> resultMap) {
        ThirdPartyPayDetail payDetail = this.thirdPartyPayDetailDao.getById(resultMap.get("out_trade_no"));
        PayChannelInf qrChannelInf = this.merchantInfDaoImpl.getChannelInf(payDetail.getChannel_id(), payDetail.getMch_id());
        String backSign = resultMap.remove("sign");
        String checkSign = MD5Util.getMd5SignNoKeyByMap(resultMap, (String) qrChannelInf.getSecret_key(), (String[]) new String[]{"utf-8"});
        if (!checkSign.equals(backSign)) {
            log.error("cs check sign error:" + resultMap);
            return payDetail;
        }
        if (!TradeStateEnum.SUCCESS.getCode().equals(payDetail.getTrade_state()) && "SUCCESS".equals(resultMap.get("ret_code"))) {
            payDetail.setTrade_state(TradeStateEnum.SUCCESS.getCode());
            payDetail.setTime_end(DateUtil.getCurrTime());
            return payDetail;
        }
        payDetail.setOut_transaction_id(resultMap.get("csret_code"));
        return payDetail;
    }


    /**
     * 通过此接口，返回支持的接口类型
     */
    public Set<TradeSource> supportedThirdPartyTradeSource() {
        return SetUtil.toSet(TradeSource.XFZF_DF_DZ);
    }









    /***
     * 网银支付接口
     *
     */

    @Override
    public BankCardPay doOrderQuery(BankCardPay BankCardPay, PayChannelInf qrChannelInf) {


        return BankCardPay;
    }

    public BankCardPay doOrderCreate(BankCardPayRequest reqParam, PayChannelInf qrChannelInf) {

        return null;
    }
    public BankCardPay acceptBankCardPayNotify(Map<String, String> resultMap) {

        return null;
    }


}
