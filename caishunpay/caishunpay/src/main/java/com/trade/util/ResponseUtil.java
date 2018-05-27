package com.trade.util;

import com.trade.bean.BankCardPay;
import com.trade.bean.ThirdPartyPayDetail;
import com.trade.bean.response.BankPayResponse;
import com.trade.bean.response.ThirdPartyResponse;

public class ResponseUtil {
    public static ThirdPartyResponse buildResponseQuery(ThirdPartyPayDetail thirdPartyPayDetail) {
        ThirdPartyResponse response = new ThirdPartyResponse();
        response.setMerchantId(thirdPartyPayDetail.getMerchantId());
        response.setTradeSn(thirdPartyPayDetail.getTradeSn());
        response.setOrderAmount(thirdPartyPayDetail.getTotal_fee());
//        response.setCoupon_fee(thirdPartyPayDetail.getCoupon_fee());
//        response.setBankType(thirdPartyPayDetail.getBank_type());
        response.setTimeEnd(thirdPartyPayDetail.getTime_end());
        response.setTradeState(thirdPartyPayDetail.getTrade_state());
        return response;
    }

    public static BankPayResponse buildBankPayResponse(BankCardPay BankCardPay) {
        BankPayResponse response = new BankPayResponse();
        response.setMerchantId(BankCardPay.getMerchantId());
        response.setTradeSn(BankCardPay.getTradeSn());
        response.setTradeState(BankCardPay.getTrade_state());
        response.setOrderAmount(String.valueOf(BankCardPay.getTotal_fee()));
//        response.setChannelType(BankCardPay.getDevice_info());
//        response.setCardType(BankCardPay.getCardType());
//        response.setBankName(BankCardPay.getBankName());
        response.setTimeEnd(BankCardPay.getTime_end());
        return response;
    }
}
