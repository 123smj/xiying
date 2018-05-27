package com.trade.service.impl;

import com.gy.system.Environment;
import com.gy.system.SysParamUtil;
import com.gy.util.*;
import com.trade.annotations.PayChannelImplement;
import com.trade.bean.ThirdPartyPayDetail;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.own.PayChannelInf;
import com.trade.bean.own.PayRequest;
import com.trade.bean.response.ThirdPartyPayResponse;
import com.trade.controller.H5PayController;
import com.trade.dao.MerchantInfDao;
import com.trade.dao.ThirdPartyPayDetailDao;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeSource;
import com.trade.enums.TradeStateEnum;
import com.trade.service.ThirdPartyPayService;
import com.trade.service.WappayService;
import com.trade.util.MD5Util;
import com.trade.util.NetworkUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@PayChannelImplement(channelId = "hengfutong")
//      三方支付接口
public class HengFuTongServiceImpl implements WappayService , ThirdPartyPayService{
    @Autowired
    private ThirdPartyPayDetailDao thirdPartyPayDetailDao;
    @Autowired
    private MerchantInfDao merchantInfDaoImpl;
    //log追踪本类 类信息
    private static Logger log = Logger.getLogger(HengFuTongServiceImpl.class);
    //NOTIFY_URL 值为 HENGFUTONG_NOTIFY_URL对应的值 HENGFUTONG_NOTIFY_URL值应为支付通道地址
    private static String NOTIFY_URL = SysParamUtil.getParam((String) "HENGFUTONG_NOTIFY_URL");
    /**
     * 类似获取日期
     * @param amount
     * @return
     */
    private String toAmountString(Double amount) {
        return String.format("%.2f", amount);
    }

    /**
     * 此处拉起客户端支付
     */
    @Override
    public ThirdPartyPayResponse doWapTrade(HttpServletRequest request, ThirdPartyPayDetail thirdPartyPayDetail, MerchantInf qrcodeMcht, PayChannelInf qrChannelInf) {
        if (thirdPartyPayDetail.getTrade_state().equals(TradeStateEnum.PRESUCCESS.getCode())) {
            return this.doH5Trade(request, thirdPartyPayDetail, qrcodeMcht, qrChannelInf);
        } else {
            ThirdPartyPayResponse code = new ThirdPartyPayResponse();
            code.setPay_url(thirdPartyPayDetail.getCode_url());
            code.setMerchantId(thirdPartyPayDetail.getMerchantId());
            code.setResultCode(thirdPartyPayDetail.getResult_code().equals("00") ? ResponseEnum.SUCCESS.getCode() : ResponseEnum.FAIL_SYSTEM.getCode());
            code.setMessage(thirdPartyPayDetail.getErr_msg());
            return code;
        }
    }
    public ThirdPartyPayResponse doH5Trade(HttpServletRequest request, ThirdPartyPayDetail payDetail, MerchantInf qrcodeMcht, PayChannelInf qrChannelInf) {
        ThirdPartyPayResponse response = new ThirdPartyPayResponse();
        String currTime = DateUtil.getCurrTime();
        response.setMerchantId(payDetail.getMerchantId());
        //params用户支付相关信息
        HashMap<String, String> params = new HashMap<String, String>();
        String orderAmount = toAmountString(payDetail.getTotal_fee() / 100.0);
        params.put("merch_id", qrChannelInf.getChannel_mcht_no());
        params.put("channel_code", "wxH5");
        params.put("mer_order_id", payDetail.getOut_trade_no());
        params.put("mer_order_date", currTime);
        params.put("order_amount", orderAmount);
        params.put("product_desc", payDetail.getBody());
        params.put("url_sync", NOTIFY_URL);
        params.put("order_remark", payDetail.getBody());
        params.put("wx_open_id", qrChannelInf.getAgtId());
        params.put("ip_payer", NetworkUtil.getRealIp(request));
        params.put("signature", MD5Util.generateMd5("UTF-8", qrChannelInf.getSecret_key(), qrChannelInf.getChannel_mcht_no(), null, "wxH5", payDetail.getOut_trade_no(), orderAmount));
        return this.connectChannel(payDetail, params);
    }

    private ThirdPartyPayResponse connectChannel(ThirdPartyPayDetail thirdPartyPayDetail, Map<String, String> params) {
        ThirdPartyPayResponse response = new ThirdPartyPayResponse();
        String keyValue = MD5Util.map2HttpParam(params);
        String resp = HttpUtility.postData(Environment.HENGFUTONG_PAY_URL, keyValue);
        if (StringUtil.isEmpty((String) resp)) {
            response.setResultCode(ResponseEnum.BACK_EXCEPTION.getCode());
            response.setMessage(ResponseEnum.BACK_EXCEPTION.getMemo());
            return response;
        }

        Map<String, String> resultMap = Dom4jUtil.parseXml2Map(resp);
        String resultCode = StringUtil.trans2Str(resultMap.get("status"));
        if (!"N".equals(resultCode)) {
            response.setResultCode(ResponseEnum.SUCCESS.getCode());
            response.setPay_url(resultMap.get("pay_url"));
            thirdPartyPayDetail.setCode_url(response.getPay_url());
            thirdPartyPayDetail.setTrade_state(TradeStateEnum.NOTPAY.getCode());
            thirdPartyPayDetail.setResp_code("00");
            thirdPartyPayDetail.setResult_code("00");
            thirdPartyPayDetail.setTransaction_id(resultMap.get("order_id"));
        } else {
            response.setResultCode(String.valueOf(resultCode) + "-" + resultMap.get("err_code"));
            response.setMessage(resultMap.get("err_info"));
            thirdPartyPayDetail.setResult_code(resultMap.get("err_code"));
            thirdPartyPayDetail.setTrade_state(TradeStateEnum.PAYERROR.getCode());
        }
        thirdPartyPayDetail.setErr_msg(resultMap.get("err_info"));
        this.thirdPartyPayDetailDao.saveOrUpdate(thirdPartyPayDetail);
        return response;
    }



   //发起支付实现此方法
    @Override
    public ThirdPartyPayDetail doOrderCreate(PayRequest payRequest, MerchantInf qrcodeMcht, PayChannelInf qrChannelInf) {
        String tradeNo = UUIDGenerator.getOrderIdByUUId(20);
        ThirdPartyPayDetail detail =  ThirdPartyPayDetail.ThirdPartyPayDetailBuilder.getBuilder()
                .withLocalTradeNumber(tradeNo)
                .withPayRequest(payRequest)
                .withPayChannelInf(qrChannelInf)
                .withTradeSource(TradeSource.WE_WAP_PAY)
                .withTradeState(TradeStateEnum.PRESUCCESS)
                .withNotifyUrl(NOTIFY_URL)
                .withCodeUrl(H5PayController.makeJumpView(qrChannelInf.getJump_url(), tradeNo))
                .isSuccess(true)
                .build() ;
        detail.setMch_create_ip(payRequest.getRemoteAddr());
        return detail;
    }
    //结果查询实现此方法
    public ThirdPartyPayDetail doOrderQuery(ThirdPartyPayDetail thirdPartyPayDetail, PayChannelInf qrChannelInf) {
        if (thirdPartyPayDetail != null && !TradeStateEnum.SUCCESS.getCode().equals(thirdPartyPayDetail.getTrade_state())) {
            if(thirdPartyPayDetail.getTrade_state().equals(TradeStateEnum.PRESUCCESS.getCode())){
                return thirdPartyPayDetail;
            }
            Environment env = Environment.HENGFUTONG_QUERY_URL;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("merch_id", qrChannelInf.getChannel_mcht_no());
            params.put("mer_order_id", thirdPartyPayDetail.getOut_trade_no());
            params.put("signature", MD5Util.generateMd5(
                    "UTF-8",
                    qrChannelInf.getSecret_key(),
                    qrChannelInf.getChannel_mcht_no(),
                    thirdPartyPayDetail.getOut_trade_no()
            ));
            String keyValue = MD5Util.map2HttpParam(params);
            String jsonResult = HttpUtility.postData(env, keyValue);
            Map<String, String> returnMap = Dom4jUtil.parseXml2Map(jsonResult);
            String backSign = returnMap.get("signature").toUpperCase();
            String checkSign = MD5Util.generateMd5(
                    "UTF-8",
                    qrChannelInf.getSecret_key(),
                    qrChannelInf.getChannel_mcht_no(),
                    thirdPartyPayDetail.getOut_trade_no(),
                    returnMap.get("status")
            );
            if (checkSign != null && checkSign.toUpperCase().equals(backSign.toUpperCase())) {
                saveWxpayScanStatus(returnMap, qrChannelInf, thirdPartyPayDetail);
            } else {
                log.warn("checkSign != backSign, ignore !");
            }
        }
        return thirdPartyPayDetail;
    }

    private void saveWxpayScanStatus(Map<String, String> returnMap, PayChannelInf qrChannelInf, ThirdPartyPayDetail thirdPartyPayDetail) {
        if (returnMap != null) {
            String errCode = StringUtil.trans2Str(returnMap.get("err_code"));
            String errInfo = StringUtil.trans2Str(returnMap.get("err_info"));
            String orderStatus = returnMap.get("status");
            if (errCode != null && errCode.length() > 0) {
                thirdPartyPayDetail.setTrade_state(TradeStateEnum.PAYERROR.getCode());
                thirdPartyPayDetail.setErr_msg(errInfo);
                this.thirdPartyPayDetailDao.update(thirdPartyPayDetail);
            } else {
                if ("Y".equals(orderStatus)) {
                    thirdPartyPayDetail.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                } else if ("N".equals(orderStatus))
                    thirdPartyPayDetail.setTrade_state(TradeStateEnum.NOTPAY.getCode());
                thirdPartyPayDetail.setTime_end(DateUtil.getCurrTime());
                this.thirdPartyPayDetailDao.update(thirdPartyPayDetail);
            }
        }
    }
    //通过此接口，返回支持的接口类型
    @Override
    public Set<TradeSource> supportedThirdPartyTradeSource() {
        return SetUtil.toSet(TradeSource.WE_WAP_PAY);
    }
    //接收支付结果通知实现此方法
    public ThirdPartyPayDetail acceptThirdPartyPayNotify(Map<String, String> resultMap) {
        ThirdPartyPayDetail notifyBeanTemp = this.thirdPartyPayDetailDao.getById(resultMap.get("mer_order_id"));
        PayChannelInf qrChannelInf = this.merchantInfDaoImpl.getChannelInf(notifyBeanTemp.getChannel_id(), notifyBeanTemp.getMch_id());
        String backSign = resultMap.get("signature").toUpperCase();
        String checkSign = MD5Util.generateMd5(
                "UTF-8",
                qrChannelInf.getSecret_key(),
                qrChannelInf.getChannel_mcht_no(),
                notifyBeanTemp.getOut_trade_no(),
                resultMap.get("order_amount"),
                notifyBeanTemp.getTransaction_id(),
                resultMap.get("status")
        );
        if (!checkSign.equals(backSign)) {
            log.error("hengfutong check_sign_error: " + String.format("%s/%s : ", backSign, checkSign) + resultMap);
            return notifyBeanTemp;
        }
        if (!TradeStateEnum.SUCCESS.getCode().equals(notifyBeanTemp.getTrade_state()) && "Y".equals(resultMap.get("status"))) {
            notifyBeanTemp.setTrade_state(TradeStateEnum.SUCCESS.getCode());
            notifyBeanTemp.setTime_end(DateUtil.getCurrTime());
        }
        return notifyBeanTemp;
    }

}
