/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Controller
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.ResponseBody
 */
package com.trade.controller;

import com.gy.util.StringUtil;
import com.trade.bean.ThirdPartyPayDetail;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.own.PayRequest;
import com.trade.bean.response.Response;
import com.trade.bean.response.ThirdPartyResponse;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeSource;
import com.trade.exception.RequestLimitedException;
import com.trade.service.MerchantInfService;
import com.trade.service.impl.ThirdPartyPayDispatcherService;
import com.trade.util.MD5Util;

import javax.servlet.http.HttpServletRequest;

import com.trade.util.NetworkUtil;
import com.trade.util.ResponseUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 三方支付接口
 */
@Controller
@RequestMapping("/thirdParty")
public class ThirdPartyPayController {
    private static Logger log = Logger.getLogger(ThirdPartyPayController.class);
    @Autowired
    private MerchantInfService merchantInfServiceImpl;
    @Autowired
    private ThirdPartyPayDispatcherService thirdPartyPayDispatcherService;

    @ResponseBody
    @RequestMapping(value={"/createOrder"})
    public Response getPayurl(HttpServletRequest request) {
        try {
            PayRequest reqParam = this.buildTradeParam(request);
            String checkValue = reqParam.checkPram();
            if (!"00".equals(checkValue)) {
                return Response.with(ResponseEnum.FAIL_PARAM, checkValue);
            }
            MerchantInf qrcodeMcht = this.merchantInfServiceImpl.getMchtInfo(reqParam.getMerchantId());
            if(qrcodeMcht == null){
                return Response.with(ResponseEnum.FAIL_MCHT_NOT_EXIST);
            }

            String checkSign = MD5Util.getMd5SignByMap(MD5Util.mappingParameterMap(request.getParameterMap()), qrcodeMcht.getSecretKey(),"UTF-8");

            if (!checkSign.equals(request.getParameter("sign"))) {
                return Response.with(ResponseEnum.FAIL_SIGN);
            }
            if (reqParam.getTradeSource() == null) {
                reqParam.setTradeSource(TradeSource.WEPAY);
            }
            if (StringUtil.isEmpty(qrcodeMcht.getChannel_id()) || StringUtil.isEmpty(qrcodeMcht.getChannelMchtNo())) {
                return Response.with(ResponseEnum.UNAUTHOR_ERROR);
            }
            if (reqParam.getT0Flag() == null || !"1".equals(reqParam.getT0Flag())) {
                reqParam.setT0Flag("0");
            }
            reqParam.setClientIp(NetworkUtil.getRealIp(request));
            Response response;
            response = this.thirdPartyPayDispatcherService.doOrderCreate(reqParam);
            response.setNonce(StringUtil.getRandom(32));
            response.setSign(MD5Util.getMd5Sign(response, qrcodeMcht.getSecretKey()));
            return response;
        }
        catch (IllegalArgumentException e) {
            return Response.with(ResponseEnum.FAIL_PARAM, "参数有误");
        } catch (RequestLimitedException e) {
            return Response.with(ResponseEnum.FAIL_SYSTEM, e.getMessage());
        }
        catch (Exception e) {
            log.error("when request third party order create :" ,e);
            return Response.with(ResponseEnum.FAIL_SYSTEM);
        }
    }

    @ResponseBody
    @RequestMapping(value={"/queryOrder"})
    public ThirdPartyResponse queryNativePay(HttpServletRequest request) {
        ThirdPartyResponse response = new ThirdPartyResponse();
        try {
            PayRequest reqParam = this.buildTradeParam(request);
            log.info("\u626b\u7801\u652f\u4ed8\u67e5\u8be2begin------sign:" + request.getParameter("sign") + "----" + reqParam);
            String checkRet = reqParam.checkQueryParam() ;
            if (checkRet != null) {
                response.setResultCode(ResponseEnum.FAIL_PARAM.getCode());
                response.setMessage(checkRet);
                return response;
            }
            MerchantInf qrcodeMcht = this.merchantInfServiceImpl.getMchtInfo(reqParam.getMerchantId());
            if (qrcodeMcht == null) {
                response.setResultCode(ResponseEnum.FAIL_MCHT_NOT_EXIST.getCode());
                response.setMessage(ResponseEnum.FAIL_MCHT_NOT_EXIST.getMemo());
                return response;
            }
            String checkSign = MD5Util.getMd5Sign(reqParam, qrcodeMcht.getSecretKey());
            if (!checkSign.equals(request.getParameter("sign"))) {
                response.setResultCode(ResponseEnum.FAIL_SIGN.getCode());
                response.setMessage(ResponseEnum.FAIL_SIGN.getMemo());
                return response;
            }
            if (StringUtil.isEmpty(qrcodeMcht.getChannel_id()) || StringUtil.isEmpty(qrcodeMcht.getChannelMchtNo())) {
                response.setResultCode(ResponseEnum.UNAUTHOR_ERROR.getCode());
                response.setMessage(ResponseEnum.UNAUTHOR_ERROR.getMemo());
                return response;
            }

            ThirdPartyPayDetail thirdPartyPayDetail = this.thirdPartyPayDispatcherService.doOrderQuery(reqParam.getTradeSn(), reqParam.getMerchantId());

            if (thirdPartyPayDetail == null) {
                response.setResultCode(ResponseEnum.FAIL_ORDER_NOT_EXIST.getCode());
                response.setMessage(ResponseEnum.FAIL_ORDER_NOT_EXIST.getMemo());
                return response;
            }
            if (thirdPartyPayDetail.getTrade_state() == null && thirdPartyPayDetail.getCode_url() == null) {
                response.setResultCode(ResponseEnum.FAIL_INVALID_TRADE.getCode());
                response.setMessage(ResponseEnum.FAIL_INVALID_TRADE.getMemo());
                return response;
            }
            response = ResponseUtil.buildResponseQuery(thirdPartyPayDetail);
            response.setNonce(StringUtil.getRandom(32));
            response.setSign(MD5Util.getMd5Sign(response, qrcodeMcht.getSecretKey()));
        }
        catch (IllegalArgumentException e) {
            response.setResultCode(ResponseEnum.FAIL_PARAM.getCode());
            response.setMessage(ResponseEnum.FAIL_PARAM.getMemo());
        }
        catch (Exception e) {
            response.setResultCode(ResponseEnum.FAIL_SYSTEM.getCode());
            response.setMessage(ResponseEnum.FAIL_SYSTEM.getMemo());
            e.printStackTrace();
            log.error("error occurred", e);
        }
        return response;
    }


    private PayRequest buildTradeParam(HttpServletRequest request) {
        PayRequest payRequest = new PayRequest();
        payRequest.setMerchantId(request.getParameter("merchantId"));
        payRequest.setChanelId(request.getParameter("chanelId"));
        payRequest.setNotifyUrl(request.getParameter("notifyUrl"));
        payRequest.setTradeSn(request.getParameter("tradeSn"));
        payRequest.setOrderAmount(StringUtil.parseIntOrGet(request.getParameter("orderAmount"),null));
        payRequest.setGoodsName(request.getParameter("goodsName"));
        payRequest.setExpirySecond(request.getParameter("expirySecond"));
        payRequest.setT0Flag(request.getParameter("t0Flag"));
        payRequest.setIdcard(request.getParameter("idcard"));
        payRequest.setRealname(request.getParameter("realname"));
        payRequest.setBankcardnum(request.getParameter("bankcardnum"));
        payRequest.setBankname(request.getParameter("bankname"));
        payRequest.setSubbranch(request.getParameter("subbranch"));
        payRequest.setPcnaps(request.getParameter("pcnaps"));
        if (StringUtil.isNotEmpty(request.getParameter("tradeSource")))
            payRequest.setTradeSource(TradeSource.get(request.getParameter("tradeSource")));
        payRequest.setClientIp(request.getParameter("clientIp"));
        payRequest.setNonce(request.getParameter("nonce"));
        return payRequest;
    }
}
