package com.trade.util;

import com.trade.bean.ThirdPartyPayDetail;
import com.trade.bean.own.MerchantInf;
import com.trade.enums.TradeSource;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FeeCountUtil {
    public static void countMchtFee(ThirdPartyPayDetail tradeInf, MerchantInf qrcodeMcht) {
        Double mchtRate = null;
        int mchtRateFee = 0;
        int mchtSettleFee = 0;
        if (TradeSource.ALIPAY.getCode().equals(tradeInf.getTrade_source())) {
            mchtRate = qrcodeMcht.getAlipay_fee_value();
        } else if (TradeSource.WEPAY.getCode().equals(tradeInf.getTrade_source()) || TradeSource.WEJSPAY.getCode().equals(tradeInf.getTrade_source())) {
            mchtRate = qrcodeMcht.getWechat_fee_value();
        } else if (TradeSource.QQPAY.getCode().equals(tradeInf.getTrade_source())) {
            mchtRate = qrcodeMcht.getQq_fee_value();
        } else if (TradeSource.WE_WAP_PAY.getCode().equals(tradeInf.getTrade_source())) {
            mchtRate = qrcodeMcht.getWechatwap_fee_value();
        } else if (TradeSource.ALI_WAP_PAY.getCode().equals(tradeInf.getTrade_source())) {
            mchtRate = qrcodeMcht.getAliwap_fee_value();
        } else if (TradeSource.QUICKPAY.getCode().equals(tradeInf.getTrade_source())) {
            mchtRate = qrcodeMcht.getQuickpay_fee_value();
        } else if (TradeSource.NETPAY.getCode().equals(tradeInf.getTrade_source())) {
            mchtRate = qrcodeMcht.getNetpay_fee_value();
        } else if (TradeSource.UNIPAY_QRCODE.getCode().equals(tradeInf.getTrade_source())) {
            mchtRate = qrcodeMcht.getUnipay_qrcode_fee_value();
        } else if (TradeSource.DAIKOU.getCode().equals(tradeInf.getTrade_source())) {
            mchtRate = qrcodeMcht.getDaikou_fee_value();
        }
        if (mchtRate == null) {
            return;
        }
        mchtRateFee = new BigDecimal(tradeInf.getTotal_fee()).multiply(new BigDecimal(mchtRate / 100.0)).setScale(0, RoundingMode.HALF_UP).intValue();
        mchtSettleFee = tradeInf.getTotal_fee() - mchtRateFee;
        tradeInf.setMcht_rate(mchtRate);
        tradeInf.setRate_fee(mchtRateFee);
        tradeInf.setSettle_fee(mchtSettleFee);
    }
}
