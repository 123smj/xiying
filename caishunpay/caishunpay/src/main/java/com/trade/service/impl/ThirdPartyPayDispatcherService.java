/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.trade.service.impl;

import com.account.service.TradeMchtAccountService;
import com.gy.util.ContextUtil;
import com.trade.bean.ThirdPartyPayDetail;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.own.PayChannelInf;
import com.trade.bean.own.PayRequest;
import com.trade.bean.response.Response;
import com.trade.bean.response.ThirdPartyPayResponse;
import com.trade.controller.H5PayController;
import com.trade.dao.MerchantInfDao;
import com.trade.dao.ThirdPartyPayDetailDao;
import com.trade.enums.*;
import com.trade.service.BankCardPayService;
import com.trade.service.MerchantInfService;
import com.trade.service.ThirdPartyPayService;
import com.trade.service.WappayService;
import com.trade.util.FeeCountUtil;
import com.trade.util.ReflectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 此类为核心类，它负责发起支付
 * @author Administrator
 *
 */
@Service
public class ThirdPartyPayDispatcherService {
    //log用来追踪ThirdPartyPayDispatcherService类信息
    private static Logger log = Logger.getLogger(ThirdPartyPayDispatcherService.class);
    @Autowired
    private ThirdPartyPayDetailDao thirdPartyPayDetailDao;
    @Autowired
    private MerchantInfDao merchantInfDaoImpl;
    @Autowired
    protected TradeMchtAccountService tradeMchtAccountService;
    @Autowired
    private MerchantInfService merchantInfService;
    @Autowired
    private List<ThirdPartyPayService> thirdPartyPayServices;
    @Autowired
    private List<WappayService> wappayServices;

    private static Map<String, ThirdPartyPayService> thirdPartyServiceMap = new HashMap<>();
    private static Map<String, WappayService> wapPayServiceMap = new HashMap<>();

    @PostConstruct
    //支付初始化
    public void onInitialize() {
        for (ThirdPartyPayService service : thirdPartyPayServices) {
            ReflectionUtils.putServiceTo(service, thirdPartyServiceMap);
        }
        for (WappayService service : wappayServices) {
            ReflectionUtils.putServiceTo(service, wapPayServiceMap);
        }
    }
    /**
     * 创建商户订单信息
     * @param payRequest 传入的第三方支付信息
     * @return
     */
    public Response doOrderCreate(PayRequest payRequest) {
        //根据商户id获取商户信息
        MerchantInf qrcodeMcht = this.merchantInfService.getMchtInfo(payRequest.getMerchantId());
        //FAIL_MCHT_NOT_EXIST 商户id不存在时返回的错误信息
        if (qrcodeMcht == null)
            return Response.with(ResponseEnum.FAIL_MCHT_NOT_EXIST);
        //根据获取到的支付状态代码与商户支付状态是否相等
        if (MchtStatusEnum.FREEZE.getCode().equals(qrcodeMcht.getStatus()))
            return Response.with(ResponseEnum.FAIL_MCHT_FREEZE);
        //tradeSourced第三方支付类型
        TradeSource tradeSource = payRequest.getTradeSource();
        //根据商户信息及支付类型获取通道信息
        PayChannelInf payChannelInf = merchantInfService.getChannelInf(qrcodeMcht, tradeSource);
        //此处判断通道信息是否存在 如不存在则返回一个状态码
        if (payChannelInf == null) {
            return Response.with(ResponseEnum.UNAUTHOR_ERROR);
        }
        //根据获取到的通道信息代码与商户支付通道信息是否相等如相等则返回一个状态码
        if (ChannelStatusEnum.FREEZE.getCode().equals(payChannelInf.getStatus())) {
            return Response.with(ResponseEnum.ERROR_CHANNEL);
        }
        //根据channelId拿到的map数据存到service里面
        ThirdPartyPayService service =switchService(payChannelInf.getChannel_id());
        //此次方法判断第三方请求是否支持（或开通）如不支持则返回一个状态码
        if (!service.supportedThirdPartyTradeSource().contains(payRequest.getTradeSource())) {
            return Response.with(ResponseEnum.ERROR_UNSUPPORT);
        }
        if (this.thirdPartyPayDetailDao.getByTradesn(payRequest.getTradeSn(), payRequest.getMerchantId()) != null) {
            return Response.with(ResponseEnum.FAIL_ORDER_NO_REPEAT);
        }
        //创建一个账单信息
        ThirdPartyPayDetail thirdPartyPayDetail = service.doOrderCreate(payRequest, qrcodeMcht, payChannelInf);
        //根据传入的账单信息以及商户信息计算费用
        FeeCountUtil.countMchtFee(thirdPartyPayDetail, qrcodeMcht);
        //添加账单信息
        thirdPartyPayDetailDao.save(thirdPartyPayDetail);
        //
        if (thirdPartyPayDetail.isSuccess()) {
            ThirdPartyPayResponse response = ThirdPartyPayResponse
                    .success(thirdPartyPayDetail.getCode_url(), thirdPartyPayDetail.getMerchantId());
            response.setImg_url(H5PayController.makeQrCodeUrl(thirdPartyPayDetail.getCode_url()));
            return response;
        } else {
            return ThirdPartyPayResponse.fail(ResponseEnum.CHANNEL_ERROR);
        }
    }
    /**
     * 根据订单id扫码支付订单信息
     * @param outTransactionId 要扫描的订单id
     * @return
     */
    public ThirdPartyPayDetail doOrderQuery(String outTransactionId) {
        ThirdPartyPayDetail detail = this.thirdPartyPayDetailDao.getById(outTransactionId);
        return this.doOrderQuery(detail.getTradeSn(), detail.getMerchantId());
    }
    /**
     *
     * @param tradeSn 交易号
     * @param gyMchtId
     * @return 返回thirdPartyPayDetail对象
     */
    public ThirdPartyPayDetail doOrderQuery(String tradeSn, String gyMchtId) {
        ThirdPartyPayDetail thirdPartyPayDetail = this.thirdPartyPayDetailDao.getByTradesn(tradeSn, gyMchtId);
        if (thirdPartyPayDetail == null) {
            return null;
        }
        if (!TradeStateEnum.SUCCESS.getCode().equals(thirdPartyPayDetail.getTrade_state())) {
            ThirdPartyPayService thirdPartyPayService = switchService(thirdPartyPayDetail.getChannel_id());
            PayChannelInf qrChannelInf = this.merchantInfDaoImpl.getChannelInf(thirdPartyPayDetail.getChannel_id(), thirdPartyPayDetail.getMch_id());
            thirdPartyPayDetail = thirdPartyPayService.doOrderQuery(thirdPartyPayDetail, qrChannelInf);
            this.thirdPartyPayDetailDao.update(thirdPartyPayDetail);
        }
        return thirdPartyPayDetail;
    }
    /**
     *根据channelId来拿thirdPartyServiceMap里的数据
     * @param channelId 要传入的通道id
     * @return
     */
    public ThirdPartyPayService switchService(String channelId) {
        return thirdPartyServiceMap.get(channelId);
    }
    /**
     *根据channelId来拿wapPayServiceMap里的数据
     * @param channelId 要传入的通道id
     * @return
     */
    public WappayService switchWapService(String channelId) {
        return wapPayServiceMap.get(channelId);
    }

}
