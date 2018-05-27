/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean;

import com.gy.util.DateUtil;
import com.trade.bean.own.PayChannelInf;
import com.trade.bean.own.PayRequest;
import com.trade.enums.TradeSource;
import com.trade.enums.TradeStateEnum;

/**
 * 扫码支付账单类
 */
public class ThirdPartyPayDetail extends PayDetail {
    private String status;
    private String message;
    private String result_code;
    private String err_code;
    private String err_msg;
    private String code_url;
    private String code_img_url;
    private String openid;
    private String trade_type;
    private String is_subscribe;
    private String pay_result;
    private String pay_info;
    private String transaction_id;
    private String out_transaction_id;
    private String sub_is_subscribe;
    private String sub_appid;
    private String sub_openid;
    private Integer coupon_fee;
    private String fee_type;
    private String bank_type;
    private String bank_billno;
    private String time_end;
    private String t0Flag;
    private String trade_source;
    private String appid;
    private String token_id;
    private String is_raw;
    private String callback_url;
    private String limit_credit_pay;
    private String resp_code;
    private String is_new_trade;
    private Integer settle_fee;
    private Integer rate_fee;
    private Double mcht_rate;

    public Boolean isSuccess(){
        return "SUCCESS".equals(this.status);
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult_code() {
        return this.result_code;
    }

    public void setResult_code(String resultCode) {
        this.result_code = resultCode;
    }

    public String getErr_code() {
        return this.err_code;
    }

    public void setErr_code(String errCode) {
        this.err_code = errCode;
    }

    public String getErr_msg() {
        return this.err_msg;
    }

    public void setErr_msg(String errMsg) {
        this.err_msg = errMsg;
    }

    public String getCode_url() {
        return this.code_url;
    }

    public void setCode_url(String codeUrl) {
        this.code_url = codeUrl;
    }

    public String getCode_img_url() {
        return this.code_img_url;
    }

    public void setCode_img_url(String codeImgUrl) {
        this.code_img_url = codeImgUrl;
    }

    public static void main(String[] args) {
        System.out.println(new ThirdPartyPayDetail().toString());
    }

    public String getOpenid() {
        return this.openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getTrade_type() {
        return this.trade_type;
    }

    public void setTrade_type(String tradeType) {
        this.trade_type = tradeType;
    }

    public String getIs_subscribe() {
        return this.is_subscribe;
    }

    public void setIs_subscribe(String isSubscribe) {
        this.is_subscribe = isSubscribe;
    }

    public String getPay_result() {
        return this.pay_result;
    }

    public void setPay_result(String payResult) {
        this.pay_result = payResult;
    }

    public String getPay_info() {
        return this.pay_info;
    }

    public void setPay_info(String payInfo) {
        this.pay_info = payInfo;
    }

    public String getTransaction_id() {
        return this.transaction_id;
    }

    public void setTransaction_id(String transactionId) {
        this.transaction_id = transactionId;
    }

    public String getOut_transaction_id() {
        return this.out_transaction_id;
    }

    public void setOut_transaction_id(String outTransactionId) {
        this.out_transaction_id = outTransactionId;
    }

    public String getSub_is_subscribe() {
        return this.sub_is_subscribe;
    }

    public void setSub_is_subscribe(String subIsSubscribe) {
        this.sub_is_subscribe = subIsSubscribe;
    }

    public String getSub_appid() {
        return this.sub_appid;
    }

    public void setSub_appid(String subAppid) {
        this.sub_appid = subAppid;
    }

    public String getSub_openid() {
        return this.sub_openid;
    }

    public void setSub_openid(String subOpenid) {
        this.sub_openid = subOpenid;
    }

    public Integer getCoupon_fee() {
        return this.coupon_fee;
    }

    public void setCoupon_fee(Integer couponFee) {
        this.coupon_fee = couponFee;
    }

    public String getFee_type() {
        return this.fee_type;
    }

    public void setFee_type(String feeType) {
        this.fee_type = feeType;
    }

    public String getBank_type() {
        return this.bank_type;
    }

    public void setBank_type(String bankType) {
        this.bank_type = bankType;
    }

    public String getBank_billno() {
        return this.bank_billno;
    }

    public void setBank_billno(String bankBillno) {
        this.bank_billno = bankBillno;
    }

    public String getTime_end() {
        return this.time_end;
    }

    public void setTime_end(String timeEnd) {
        this.time_end = timeEnd;
    }

    public String getT0Flag() {
        return this.t0Flag;
    }

    public void setT0Flag(String t0Flag) {
        this.t0Flag = t0Flag;
    }

    public String getAppid() {
        return this.appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getToken_id() {
        return this.token_id;
    }

    public void setToken_id(String tokenId) {
        this.token_id = tokenId;
    }

    public String getIs_raw() {
        return this.is_raw;
    }

    public void setIs_raw(String isRaw) {
        this.is_raw = isRaw;
    }

    public String getCallback_url() {
        return this.callback_url;
    }

    public void setCallback_url(String callbackUrl) {
        this.callback_url = callbackUrl;
    }

    public String getLimit_credit_pay() {
        return this.limit_credit_pay;
    }

    public void setLimit_credit_pay(String limitCreditPay) {
        this.limit_credit_pay = limitCreditPay;
    }

    public String getResp_code() {
        return this.resp_code;
    }

    public void setResp_code(String respCode) {
        this.resp_code = respCode;
    }

    public String getTrade_source() {
        return this.trade_source;
    }

    public void setTrade_source(String tradeSource) {
        this.trade_source = tradeSource;
    }

    public String getIs_new_trade() {
        return this.is_new_trade;
    }

    public void setIs_new_trade(String isNewTrade) {
        this.is_new_trade = isNewTrade;
    }

    public Integer getSettle_fee() {
        return this.settle_fee;
    }

    public void setSettle_fee(Integer settleFee) {
        this.settle_fee = settleFee;
    }

    public Integer getRate_fee() {
        return this.rate_fee;
    }

    public void setRate_fee(Integer rateFee) {
        this.rate_fee = rateFee;
    }

    public Double getMcht_rate() {
        return this.mcht_rate;
    }

    public void setMcht_rate(Double mchtRate) {
        this.mcht_rate = mchtRate;
    }

    public static final class ThirdPartyPayDetailBuilder {
        //主键, 对上游通道的交易号
        private String out_trade_no;
        private String service;
        private String version;
        private String status;
        private String charset;
        private String message;
        private String sign_type;
        private String result_code;
        private String mch_id;
        private String err_code;
        private String sign_agentno;
        private String err_msg;
        private String device_info;
        private String code_url;
        private String code_img_url;
        private String body;
        private String attach;
        private String merchantId;
        private int total_fee;
        private String channel_id;
        private String mch_create_ip;
        private String gy_notifyUrl;
        private String notify_url;
        //对下游通道的交易号
        private String tradeSn;
        private String time_start;
        private String time_expire;
        //交易状态
        private String trade_state;
        private String op_user_id;
        private String openid;
        private String goods_tag;
        private String trade_type;
        private String product_id;
        private String is_subscribe;
        private String nonce_str;
        private String pay_result;
        private String sign;
        private String pay_info;
        private String transaction_id;
        private String out_transaction_id;
        private String sub_is_subscribe;
        private String sub_appid;
        private String sub_openid;
        private Integer coupon_fee;
        private String fee_type;
        private String bank_type;
        private String bank_billno;
        private String time_end;
        private String t0Flag;
        private String trade_source;
        private String appid;
        private String token_id;
        private String is_raw;
        private String callback_url;
        private String limit_credit_pay;
        private String gynotify_back;
        private String resp_code;
        private String is_new_trade;
        private Integer notify_num;
        private String notify_data;
        private String nofity_time;
        private Integer settle_fee;
        private Integer rate_fee;
        private Double mcht_rate;

        private ThirdPartyPayDetailBuilder() {
        }

        public static ThirdPartyPayDetailBuilder getBuilder() {
            return new ThirdPartyPayDetailBuilder();
        }

        public ThirdPartyPayDetailBuilder withTradeState(TradeStateEnum tradeStateEnum){
            this.trade_state = tradeStateEnum.getCode();
            return this;
        }
        public ThirdPartyPayDetailBuilder withTradeSource(TradeSource tradeSource){
            this.trade_source = tradeSource.getCode();
            return this;
        }

        public ThirdPartyPayDetailBuilder withLocalTradeNumber(String tradeNumber){
            this.out_trade_no = tradeNumber;
            return this;
        }

        public ThirdPartyPayDetailBuilder withPayRequest(PayRequest payRequest) {
            this.mch_create_ip = payRequest.getRemoteAddr();
            this.tradeSn = payRequest.getTradeSn();
            this.total_fee = payRequest.getOrderAmount();
            this.body = payRequest.getGoodsName();
            this.t0Flag = payRequest.getT0Flag();
            this.merchantId = payRequest.getMerchantId();
            this.gy_notifyUrl = payRequest.getNotifyUrl();
            return this;
        }

        public ThirdPartyPayDetailBuilder withPayChannelInf(PayChannelInf payChannelInf){
            this.service = payChannelInf.getChannel_id();
            this.mch_id=payChannelInf.getChannel_mcht_no();
            this.channel_id=payChannelInf.getChannel_id();
            this.trade_type=payChannelInf.getChannel_id();
            return this;
        }

        public ThirdPartyPayDetailBuilder isSuccess(Boolean success){
            if(success)
                this.status = "SUCCESS";
            else
                this.status = "ERROR";
            return this;
        }

        public ThirdPartyPayDetailBuilder withNotifyUrl(String notify_url){
            this.notify_url = notify_url;
            return this;
        }

        public ThirdPartyPayDetailBuilder withCodeUrl(String code_url){
            this.code_url = code_url;
            return this;
        }


        public ThirdPartyPayDetail build() {
            assert(this.out_trade_no!=null);
            assert(this.status!=null);
            assert(this.merchantId!=null);
            assert(this.channel_id!=null);
            assert(this.tradeSn!=null);
            assert(this.total_fee!=0);
            assert(this.notify_url!=null);
            this.time_start = DateUtil.getCurrTime();
            if(this.status.equals("SUCCESS")){
                assert(this.code_url!=null || this.code_img_url!=null);
            }
            ThirdPartyPayDetail thirdPartyPayDetail = new ThirdPartyPayDetail();
            thirdPartyPayDetail.setOut_trade_no(out_trade_no);
            thirdPartyPayDetail.setService(service);
            thirdPartyPayDetail.setVersion(version);
            thirdPartyPayDetail.setStatus(status);
            thirdPartyPayDetail.setCharset(charset);
            thirdPartyPayDetail.setMessage(message);
            thirdPartyPayDetail.setSign_type(sign_type);
            thirdPartyPayDetail.setResult_code(result_code);
            thirdPartyPayDetail.setMch_id(mch_id);
            thirdPartyPayDetail.setErr_code(err_code);
            thirdPartyPayDetail.setSign_agentno(sign_agentno);
            thirdPartyPayDetail.setErr_msg(err_msg);
            thirdPartyPayDetail.setDevice_info(device_info);
            thirdPartyPayDetail.setCode_url(code_url);
            thirdPartyPayDetail.setCode_img_url(code_img_url);
            thirdPartyPayDetail.setBody(body);
            thirdPartyPayDetail.setAttach(attach);
            thirdPartyPayDetail.setMerchantId(merchantId);
            thirdPartyPayDetail.setTotal_fee(total_fee);
            thirdPartyPayDetail.setChannel_id(channel_id);
            thirdPartyPayDetail.setMch_create_ip(mch_create_ip);
            thirdPartyPayDetail.setGy_notifyUrl(gy_notifyUrl);
            thirdPartyPayDetail.setNotify_url(notify_url);
            thirdPartyPayDetail.setTradeSn(tradeSn);
            thirdPartyPayDetail.setTime_start(time_start);
            thirdPartyPayDetail.setTime_expire(time_expire);
            thirdPartyPayDetail.setTrade_state(trade_state);
            thirdPartyPayDetail.setOp_user_id(op_user_id);
            thirdPartyPayDetail.setOpenid(openid);
            thirdPartyPayDetail.setGoods_tag(goods_tag);
            thirdPartyPayDetail.setTrade_type(trade_type);
            thirdPartyPayDetail.setProduct_id(product_id);
            thirdPartyPayDetail.setIs_subscribe(is_subscribe);
            thirdPartyPayDetail.setNonce_str(nonce_str);
            thirdPartyPayDetail.setPay_result(pay_result);
            thirdPartyPayDetail.setSign(sign);
            thirdPartyPayDetail.setPay_info(pay_info);
            thirdPartyPayDetail.setTransaction_id(transaction_id);
            thirdPartyPayDetail.setOut_transaction_id(out_transaction_id);
            thirdPartyPayDetail.setSub_is_subscribe(sub_is_subscribe);
            thirdPartyPayDetail.setSub_appid(sub_appid);
            thirdPartyPayDetail.setSub_openid(sub_openid);
            thirdPartyPayDetail.setCoupon_fee(coupon_fee);
            thirdPartyPayDetail.setFee_type(fee_type);
            thirdPartyPayDetail.setBank_type(bank_type);
            thirdPartyPayDetail.setBank_billno(bank_billno);
            thirdPartyPayDetail.setTime_end(time_end);
            thirdPartyPayDetail.setT0Flag(t0Flag);
            thirdPartyPayDetail.setTrade_source(trade_source);
            thirdPartyPayDetail.setAppid(appid);
            thirdPartyPayDetail.setToken_id(token_id);
            thirdPartyPayDetail.setIs_raw(is_raw);
            thirdPartyPayDetail.setCallback_url(callback_url);
            thirdPartyPayDetail.setLimit_credit_pay(limit_credit_pay);
            thirdPartyPayDetail.setGynotify_back(gynotify_back);
            thirdPartyPayDetail.setResp_code(resp_code);
            thirdPartyPayDetail.setIs_new_trade(is_new_trade);
            thirdPartyPayDetail.setNotify_num(notify_num);
            thirdPartyPayDetail.setNotify_data(notify_data);
            thirdPartyPayDetail.setNofity_time(nofity_time);
            thirdPartyPayDetail.setSettle_fee(settle_fee);
            thirdPartyPayDetail.setRate_fee(rate_fee);
            thirdPartyPayDetail.setMcht_rate(mcht_rate);
            return thirdPartyPayDetail;
        }
    }
}
