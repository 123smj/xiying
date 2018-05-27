package com.trade.service.impl;

import com.gy.system.Environment;
import com.gy.system.SysParamUtil;
import com.gy.util.HttpUtility;
import com.gy.util.SetUtil;
import com.gy.util.StringUtil;
import com.gy.util.UUIDGenerator;
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
import com.trade.exception.RequestLimitedException;
import com.trade.service.ThirdPartyPayService;
import com.trade.service.ws.ips.ScanService;
import com.trade.util.JsonUtil;
import com.trade.util.MD5Util;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.gy.util.CommonFunction.getCurrentDateTime;

@PayChannelImplement(channelId = "huanxun")
//实现三方支付接口
public class IPSPayServiceImpl implements ThirdPartyPayService {
    @Autowired
    private ThirdPartyPayDetailDao thirdPartyPayDetailDao;
    @Autowired
    private MerchantInfDao merchantInfDaoImpl;
    @Autowired
    private BankCardPayDao bankCardPayDao;
    @Autowired
    private ScanService huanxunService;

    private static Logger log = Logger.getLogger(IPSPayServiceImpl.class);
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    private static String IPS_NETPAY_REQUEST_URL = "https://newpay.ips.com.cn/psfp-entry/gateway/payment.do";
    private static String IPS_QUICKPAY_REQUEST_URL = "https://mobilegw.ips.com.cn/psfp-mgw/paymenth5.do";

    private static String getCallbackUrl(String raw) {
        if (!StringUtil.isEmpty(raw) && raw.startsWith("http")) {
            return raw;
        } else {
            return "http://gateway.ykbpay.com:8080/caishunpay-web/trade/payok.jsp";
        }
    }

    private String getNetPayNotifyUrl() {
        return SysParamUtil.getParam("HUANXUN_NOTIFY_NP_URL");
    }

    private String toAmountString(Double amount) {
        return String.format("%.2f", amount);
    }

    @Override
    public ThirdPartyPayDetail doOrderCreate(PayRequest payRequest, MerchantInf qrcodeMcht, PayChannelInf qrChannelInf) {
        TradeSource tradeSource = payRequest.getTradeSource();
        if(TradeSource.NETPAY.equals(tradeSource) || TradeSource.QUICKPAY.equals(tradeSource)) {
            return doNetPayOrderCreate(payRequest, qrcodeMcht, qrChannelInf);
        }  else {
            return doQuickPayOrderCreate(payRequest, qrcodeMcht, qrChannelInf);
        }
    }

    @Override
    public ThirdPartyPayDetail doOrderQuery(ThirdPartyPayDetail var1, PayChannelInf var2) {
        return null;
    }

    private ThirdPartyPayDetail doQuickPayOrderCreate(PayRequest payRequest, MerchantInf qrcodeMcht, PayChannelInf qrChannelInf) {
        String tradeNo = UUIDGenerator.getOrderIdByUUId((int) 20);
        String xml = getScanPayXml(tradeNo,payRequest,qrChannelInf);
        String xmlResp = huanxunService.scanPay(xml);
        System.out.println(xmlResp);
        String qrCode = getXmlField(xmlResp,"QrCode");
        String respCode = getXmlField(xmlResp, "RspCode");
        String respMsg = getXmlField(xmlResp, "RespMsg");
        Boolean success ="000000" .equals(respCode);
        ThirdPartyPayDetail detail = ThirdPartyPayDetail.ThirdPartyPayDetailBuilder.getBuilder()
                .isSuccess(success)
                .withLocalTradeNumber(tradeNo)
                .withPayRequest(payRequest)
                .withPayChannelInf(qrChannelInf)
                .withTradeSource(payRequest.getTradeSource())
                .withTradeState(success ? TradeStateEnum.NOTPAY : TradeStateEnum.PAYERROR)
                .withNotifyUrl(getNetPayNotifyUrl())
                .withCodeUrl(qrCode)
                .build();

        detail.setResp_code(respCode);
        detail.setErr_msg(respMsg);
        return detail;
    }

    private String getGatewayType(TradeSource tradeSource){
        Map<TradeSource,String> map = new HashMap<>();
        map.put(TradeSource.QUICKPAY,"01");
        map.put(TradeSource.NETPAY,"01");
        map.put(TradeSource.WEPAY,"10");
        map.put(TradeSource.ALIPAY,"11");
        return map.get(tradeSource);
    }
     private String getScanPayXml(String tradeNo,PayRequest payRequest, PayChannelInf qrChannelInf){
       String bodyXml = "<body>" +
                "<MerBillNo>" + tradeNo + "</MerBillNo>" +
                "<GatewayType>" + getGatewayType(payRequest.getTradeSource()) + "</GatewayType>" +
                "<Date>" + DATE_FORMAT.format(new Date()) + "</Date>" +
                "<CurrencyType>" + "156" + "</CurrencyType>" +
                "<Amount>" + toAmountString(payRequest.getOrderAmount() / 100.0) + "</Amount>" +
                "<Lang>" + "GB" + "</Lang>" +
                "<Attach>" + "" + "</Attach>" +
                "<RetEncodeType>" + "17" + "</RetEncodeType>" +
                "<ServerUrl>" + getNetPayNotifyUrl() + "</ServerUrl>" +
                "<BillEXP>" + "2" + "</BillEXP>" +
                "<GoodsName>" + payRequest.getGoodsName() + "</GoodsName>" +
                "</body>";
        String sign = MD5Util.md5Sign(bodyXml + qrChannelInf.getChannel_mcht_no() + qrChannelInf.getSecret_key()).toLowerCase();
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String xml = "<Ips>" +
                "<GateWayReq>" +
                "<head>" +
                "<Version>" + "" + "</Version>" +
                "<MerCode>" + qrChannelInf.getChannel_mcht_no() + "</MerCode>" +
                "<MerName>" + "" + "</MerName>" +
                "<Account>" + qrChannelInf.getAgtId() + "</Account>" +
                "<MsgId>" + "msg" + date + "</MsgId >" +
                "<ReqDate>" + date + "</ReqDate >" +
                "<Signature>" + sign + "</Signature>" +
                "</head>" +
                bodyXml +
                "</GateWayReq>" +
                "</Ips>";
        return xml;
    }

    private String getPayXml(String tradeNo,PayRequest payRequest, PayChannelInf qrChannelInf){
       String bodyXml = "<body>" +
                "<MerBillNo>" + tradeNo + "</MerBillNo>" +
                "<Lang>" + "GB" + "</Lang>" +
                "<Amount>" + toAmountString(payRequest.getOrderAmount() / 100.0) + "</Amount>" +
                "<Date>" + DATE_FORMAT.format(new Date()) + "</Date>" +
                "<CurrencyType>" + "156" + "</CurrencyType>" +
                "<GatewayType>" + getGatewayType(payRequest.getTradeSource()) + "</GatewayType>" +
                "<Merchanturl>" + getCallbackUrl(payRequest.getCallback_url()) + "</Merchanturl>" +
                "<FailUrl><![CDATA[" + getCallbackUrl(payRequest.getCallback_url()) + "]]></FailUrl>" +
                "<Attach><![CDATA[" + "" + "]]></Attach>" +
                "<OrderEncodeType>" + "5" + "</OrderEncodeType>" +
                "<RetEncodeType>" + "17" + "</RetEncodeType>" +
                "<RetType>" + "1" + "</RetType>" +
                "<ServerUrl><![CDATA[" + getNetPayNotifyUrl() + "]]></ServerUrl>" +
                "<BillEXP>" + "2" + "</BillEXP>" +
                "<GoodsName>" + payRequest.getGoodsName() + "</GoodsName>" +
                "<IsCredit>" + "" + "</IsCredit>" +
                "<BankCode>" + "</BankCode>" +
                "<ProductType>" + "</ProductType>" +
                "</body>";
        String sign = MD5Util.md5Sign(bodyXml + qrChannelInf.getChannel_mcht_no() + qrChannelInf.getSecret_key()).toLowerCase();
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String xml = "<Ips>" +
                "<GateWayReq>" +
                "<head>" +
                "<Version>" + "" + "</Version>" +
                "<MerCode>" + qrChannelInf.getChannel_mcht_no() + "</MerCode>" +
                "<MerName>" + "" + "</MerName>" +
                "<Account>" + qrChannelInf.getAgtId() + "</Account>" +
                "<MsgId>" + "msg" + date + "</MsgId >" +
                "<ReqDate>" + date + "</ReqDate >" +
                "<Signature>" + sign + "</Signature>" +
                "</head>" +
                bodyXml +
                "</GateWayReq>" +
                "</Ips>";
        return xml;
    }

    private ThirdPartyPayDetail doNetPayOrderCreate(PayRequest payRequest, MerchantInf qrcodeMcht, PayChannelInf qrChannelInf) {
        String tradeNo = UUIDGenerator.getOrderIdByUUId((int) 20);
        String xml = getPayXml(tradeNo,payRequest,qrChannelInf);
        Map<String, String> params = new HashMap<>();
        params.put("pGateWayReq", xml);
        String payUrl = null;
        if (TradeSource.NETPAY.equals(payRequest.getTradeSource())) {
            payUrl = IPS_NETPAY_REQUEST_URL;
        }
        if (TradeSource.QUICKPAY.equals(payRequest.getTradeSource())) {
            payUrl = IPS_QUICKPAY_REQUEST_URL;
        }
        if (payUrl == null) {
            throw new RequestLimitedException("不支持的交易类别");
        }
        String redirectUrl = RedirectController.makeJump(params, "post", payUrl, qrChannelInf.getJump_url());
        ThirdPartyPayDetail detail = ThirdPartyPayDetail.ThirdPartyPayDetailBuilder.getBuilder()
                .isSuccess(true)
                .withLocalTradeNumber(tradeNo)
                .withPayRequest(payRequest)
                .withPayChannelInf(qrChannelInf)
                .withTradeSource(payRequest.getTradeSource())
                .withTradeState(TradeStateEnum.NOTPAY)
                .withNotifyUrl(getNetPayNotifyUrl())
                .withCodeUrl(redirectUrl)
                .build();
        return detail;
    }

    @Override
    public Set<TradeSource> supportedThirdPartyTradeSource() {
        return SetUtil.toSet(TradeSource.QUICKPAY, TradeSource.NETPAY, TradeSource.ALIPAY, TradeSource.WEPAY);
    }

    public ThirdPartyPayDetail acceptThirdPartyPayNotify(Map<String, String> resultMap) {
        String resp = resultMap.get("paymentResult");
        if (!"000000".equals(getXmlField(resp, "RspCode"))) {
            return null;
        }
        String sign = getXmlField(resp, "Signature");
        String bodyXml = "<body>" + getXmlField(resp, "body") + "</body>";
        ThirdPartyPayDetail payDetail = this.thirdPartyPayDetailDao.getById(getXmlField(bodyXml, "MerBillNo"));
        PayChannelInf qrChannelInf = this.merchantInfDaoImpl.getChannelInf(payDetail.getChannel_id(), payDetail.getMch_id());
        String signCheck = MD5Util.md5Sign(bodyXml + qrChannelInf.getChannel_mcht_no() + qrChannelInf.getSecret_key()).toLowerCase();
        if (signCheck.equals(sign)) {
            switch (getXmlField(resp, "Status")) {
                case "Y":
                    payDetail.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                    break;
                default:
                    payDetail.setTrade_state(TradeStateEnum.NOTPAY.getCode());
                    break;
            }
        } else {
            log.error("sign check failed! , " + sign + " , " + signCheck);
        }
        return payDetail;
    }

    private static String getXmlField(String xml, String fieldName) {
        String s_field = "<" + fieldName + ">";
        String e_field = "</" + fieldName + ">";
        int s_index = xml.indexOf(s_field);
        int e_index = xml.indexOf(e_field);
        if(s_index<0 || e_index <0){
            return null;
        }
        return xml.substring(s_index + s_field.length(), e_index);
    }
}
