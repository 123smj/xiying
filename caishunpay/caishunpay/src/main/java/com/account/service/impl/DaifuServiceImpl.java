/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  com.guoyin.rmi.DAIFU
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.account.service.impl;

import com.account.bean.TradeMchtAccountDetail;
import com.account.service.DaifuService;
import com.account.service.TradeMchtAccountDetailService;
import com.account.service.TradeMchtAccountService;
import com.guoyin.rmi.DAIFU;
import com.gy.system.Environment;
import com.gy.system.SysParamUtil;
import com.gy.util.DateUtil;
import com.gy.util.HttpUtility;
import com.gy.util.StringUtil;
import com.trade.bean.TradeDfDayamount;
import com.trade.bean.own.DfParam;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.own.PayChannelInf;
import com.trade.enums.ResponseEnum;
import com.trade.service.MerchantInfService;
import com.trade.service.TradeDfDayamountService;
import com.trade.util.MD5Util;
import com.trade.util.JsonUtil;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DaifuServiceImpl
implements DaifuService {
    private static Logger log = Logger.getLogger(DaifuServiceImpl.class);
    @Autowired
    private TradeMchtAccountService tradeMchtAccountServiceImpl;
    @Autowired
    private TradeMchtAccountDetailService tradeMchtAccountDetailServiceImpl;
    @Autowired
    private MerchantInfService merchantInfServiceImpl;
    @Autowired
    private TradeDfDayamountService tradeDfDayamountServiceImpl;
    private static Map<String, TradeMchtAccountDetail> dfMap = new ConcurrentHashMap<String, TradeMchtAccountDetail>();
    private static ConcurrentHashMap<String, String> accountMap = new ConcurrentHashMap();
    public static final String DAIFU_URL = "112.74.126.228:9911";
    private static final String DAIFU_USER = "zhongzhuan";
    private static final String DAIFU_PASSWORD = "6fe08b3418d62c6d3efaf9fc07766f98";
    public static final String ACC_TYPE_INCOME = "1";
    public static final String ACC_TYPE_OUTCOME = "2";
    public static final String STATUS_SUCCESS = "00";
    public static final String STATUS_WAITING_DAIFU = "01";
    public static final String STATUS_ERROR_DAIFU = "02";
    public static final String STATUS_PREPARED = "08";
    public static final String STATUS_ERROR_PREDAIFU = "09";

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public ResponseEnum singlePay(TradeMchtAccountDetail mchtAccountDetail, MerchantInf qrcodeMcht) {
        int result;
        String key = String.valueOf(mchtAccountDetail.getMchtNo()) + mchtAccountDetail.getDfSn();
        Map<String, TradeMchtAccountDetail> map = dfMap;
        synchronized (map) {
            if (dfMap.size() > 10) {
                dfMap.clear();
            }
            if (dfMap.get(key) != null) {
                log.error((Object)"daifu---ORDER_REPEAT");
                return ResponseEnum.FAIL_ORDER_NO_REPEAT;
            }
            dfMap.put(key, mchtAccountDetail);
        }
        if (this.tradeMchtAccountDetailServiceImpl.getByDfsn(mchtAccountDetail.getMchtNo(), mchtAccountDetail.getDfSn()) != null) {
            dfMap.remove(key);
            return ResponseEnum.FAIL_ORDER_NO_REPEAT;
        }
        mchtAccountDetail.setMemo("\u672a\u6263\u6b3e");
        mchtAccountDetail.setReceiptTime(DateUtil.getCurrTime());
        mchtAccountDetail.setAccType("2");
        mchtAccountDetail.setTimeEnd(DateUtil.getCurrTime());
        mchtAccountDetail.setStatus("02");
        this.tradeMchtAccountDetailServiceImpl.save(mchtAccountDetail);
        TradeDfDayamount dfDayamount = this.tradeDfDayamountServiceImpl.get(mchtAccountDetail.getReceiptPan(), DateUtil.getCurrentDay());
        if (mchtAccountDetail.getReceiptAmount() > qrcodeMcht.getDfcard_day_limit() || dfDayamount != null && dfDayamount.getMoney_amount() + mchtAccountDetail.getReceiptAmount() > qrcodeMcht.getDfcard_day_limit()) {
            mchtAccountDetail.setDfDesc(ResponseEnum.OUT_DFDAYAMOUNT_LIMIT.getMemo());
            this.tradeMchtAccountDetailServiceImpl.update(mchtAccountDetail);
            return ResponseEnum.OUT_DFDAYAMOUNT_LIMIT;
        }
        DaifuServiceImpl daifuServiceImpl = this;
        synchronized (daifuServiceImpl) {
            result = this.tradeMchtAccountServiceImpl.decreaseAccount(mchtAccountDetail.getMchtNo(), mchtAccountDetail.getMchtIncome());
        }
        ResponseEnum response = ResponseEnum.SUCCESS;
        if (result != 1) {
            response = result == 2 ? ResponseEnum.BALANCE_EMPTY : ResponseEnum.BALANCE_EXCEPTION;
            mchtAccountDetail.setStatus("02");
            dfMap.remove(key);
            return response;
        }
        mchtAccountDetail.setMemo("\u5df2\u6263\u6b3e");
        mchtAccountDetail.setStatus("08");
        dfMap.remove(key);
        return response;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public ResponseEnum doSinglePay(TradeMchtAccountDetail mchtAccountDetail, MerchantInf qrcodeMcht) {
        ResponseEnum response;
        block16 : {
            response = null;
            Map<String, Object> dfResult = null;
            try {
                dfResult = this.daifu(mchtAccountDetail, qrcodeMcht);
                if (dfResult == null) {
                    response = ResponseEnum.BACK_EXCEPTION;
                    mchtAccountDetail.setStatus("02");
                    break block16;
                }
                mchtAccountDetail.setDfStatus(StringUtil.trans2Str(dfResult.get("resCode")));
                mchtAccountDetail.setDfDesc(StringUtil.trans2Str(dfResult.get("resDesc")));
                if ("0000".equals(dfResult.get("resCode")) || "1111".equals(dfResult.get("resCode")) || "9999".equals(dfResult.get("resCode"))) {
                    mchtAccountDetail.setStatus("01");
                    response = ResponseEnum.SUCCESS;
                    DaifuServiceImpl daifuServiceImpl = this;
                    synchronized (daifuServiceImpl) {
                        TradeDfDayamount tradeDfDayamount = this.tradeDfDayamountServiceImpl.get(mchtAccountDetail.getReceiptPan(), DateUtil.getCurrentDay());
                        if (tradeDfDayamount == null) {
                            tradeDfDayamount = new TradeDfDayamount();
                            tradeDfDayamount.setMcht_no(mchtAccountDetail.getMchtNo());
                            tradeDfDayamount.setUpdate_time(DateUtil.getCurrTime());
                            tradeDfDayamount.setBank_card_num(mchtAccountDetail.getReceiptPan());
                            tradeDfDayamount.setBank_name(mchtAccountDetail.getReceiptBankNm());
                            tradeDfDayamount.setReal_name(mchtAccountDetail.getReceiptName());
                            tradeDfDayamount.setMoney_amount(mchtAccountDetail.getReceiptAmount());
                            tradeDfDayamount.setDf_day(DateUtil.getCurrentDay());
                            this.tradeDfDayamountServiceImpl.save(tradeDfDayamount);
                        } else {
                            tradeDfDayamount.setUpdate_time(DateUtil.getCurrTime());
                            tradeDfDayamount.setMoney_amount(tradeDfDayamount.getMoney_amount() + mchtAccountDetail.getReceiptAmount());
                            this.tradeDfDayamountServiceImpl.update(tradeDfDayamount);
                        }
                        break block16;
                    }
                }
                response = ResponseEnum.DAIFU_ERROR;
                mchtAccountDetail.setStatus("02");
                DaifuServiceImpl daifuServiceImpl = this;
                synchronized (daifuServiceImpl) {
                    this.tradeMchtAccountServiceImpl.addAccount(mchtAccountDetail.getMchtNo(), mchtAccountDetail.getMchtIncome());
                }
                mchtAccountDetail.setMemo("\u672a\u6263\u6b3e");
            }
            catch (RemoteException re) {
                log.error((Object)("daifu---\u5f02\u5e38" + re.getMessage()));
                mchtAccountDetail.setStatus("02");
                DaifuServiceImpl tradeDfDayamount = this;
                synchronized (tradeDfDayamount) {
                    this.tradeMchtAccountServiceImpl.addAccount(mchtAccountDetail.getMchtNo(), mchtAccountDetail.getMchtIncome());
                }
                mchtAccountDetail.setMemo("\u672a\u6263\u6b3e");
            }
            catch (Exception e) {
                log.info((Object)("daifu---\u5f02\u5e38" + e.getMessage()));
                mchtAccountDetail.setStatus("02");
                e.printStackTrace();
            }
        }
        this.tradeMchtAccountDetailServiceImpl.update(mchtAccountDetail);
        return response;
    }

    private synchronized void dealDfAmount(TradeMchtAccountDetail mchtAccountDetail) {
        TradeDfDayamount tradeDfDayamount = this.tradeDfDayamountServiceImpl.get(mchtAccountDetail.getReceiptPan(), DateUtil.getCurrentDay());
        if (tradeDfDayamount == null) {
            tradeDfDayamount = new TradeDfDayamount();
            tradeDfDayamount.setMcht_no(mchtAccountDetail.getMchtNo());
            tradeDfDayamount.setUpdate_time(DateUtil.getCurrTime());
            tradeDfDayamount.setBank_card_num(mchtAccountDetail.getReceiptPan());
            tradeDfDayamount.setBank_name(mchtAccountDetail.getReceiptBankNm());
            tradeDfDayamount.setReal_name(mchtAccountDetail.getReceiptName());
            tradeDfDayamount.setMoney_amount(mchtAccountDetail.getReceiptAmount());
            tradeDfDayamount.setDf_day(DateUtil.getCurrentDay());
            this.tradeDfDayamountServiceImpl.save(tradeDfDayamount);
        } else {
            tradeDfDayamount.setUpdate_time(DateUtil.getCurrTime());
            tradeDfDayamount.setMoney_amount(tradeDfDayamount.getMoney_amount() + mchtAccountDetail.getReceiptAmount());
            this.tradeDfDayamountServiceImpl.update(tradeDfDayamount);
        }
    }

    private Map<String, Object> daifu(TradeMchtAccountDetail mchtAccountDetail) throws Exception {
        log.info((Object)("to---daifu:" + mchtAccountDetail.getAccountOrderNo() + "---" + mchtAccountDetail.getReceiptName() + "---" + mchtAccountDetail.getReceiptPan() + "---" + mchtAccountDetail.getReceiptBankNm() + "---" + mchtAccountDetail.getReceiptAmount() + "---" + mchtAccountDetail.getMchtNo() + "---" + mchtAccountDetail.getDfSn()));
        if ("true".equals(SysParamUtil.getParam("is_df_moni"))) {
            HashMap<String, Object> result = new HashMap<String, Object>();
            result.put("resCode", "0000");
            result.put("resDesc", "success");
            return result;
        }
        Map result = DAIFU.pay((String)"zhongzhuan", (String)"6fe08b3418d62c6d3efaf9fc07766f98", (String)"112.74.126.228:9911", (Object[])new Object[]{mchtAccountDetail.getAccountOrderNo(), mchtAccountDetail.getReceiptName(), mchtAccountDetail.getReceiptPan(), mchtAccountDetail.getReceiptBankNm(), String.valueOf(mchtAccountDetail.getReceiptAmount()), mchtAccountDetail.getMchtNo(), mchtAccountDetail.getDfSn()});
        log.info((Object)("daifu---\u8fd4\u56de" + result));
        return result;
    }

    private Map<String, Object> daifu(TradeMchtAccountDetail mchtAccountDetail, MerchantInf qrcodeMcht) throws Exception {
        log.info((Object)("to---daifu:" + mchtAccountDetail.getAccountOrderNo() + "---" + mchtAccountDetail.getReceiptName() + "---" + mchtAccountDetail.getReceiptPan() + "---" + mchtAccountDetail.getReceiptBankNm() + "---" + mchtAccountDetail.getReceiptAmount() + "---" + mchtAccountDetail.getMchtNo() + "---" + mchtAccountDetail.getDfSn()));
        if ("true".equals(SysParamUtil.getParam("is_df_moni"))) {
            HashMap<String, Object> result = new HashMap<String, Object>();
            result.put("resCode", "0000");
            result.put("resDesc", "success");
            return result;
        }
        HashMap<String, Object> result = null;
        PayChannelInf qrChannelInf = this.merchantInfServiceImpl.getChannelInf(qrcodeMcht.getChannel_id(), qrcodeMcht.getChannelMchtNo());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("gymchtId", qrcodeMcht.getChannelMchtNo());
        params.put("dfSn", mchtAccountDetail.getAccountOrderNo());
        params.put("receiptAmount", String.valueOf(mchtAccountDetail.getReceiptAmount()));
        params.put("curType", "1");
        params.put("payType", "1");
        params.put("receiptName", mchtAccountDetail.getReceiptName());
        params.put("receiptPan", mchtAccountDetail.getReceiptPan());
        params.put("receiptBankNm", mchtAccountDetail.getReceiptBankNm());
        params.put("acctType", "0");
        params.put("nonce", StringUtil.getRandom(16));
        params.put("sign", MD5Util.getMd5SignByMap(params, qrChannelInf.getSecret_key(), "utf-8"));
        String keyValue = MD5Util.map2HttpParam(params);
        String jsonResult = HttpUtility.postData(Environment.GUOYIN_DAIFU_URL, keyValue);
        Map returnMap = (Map)JsonUtil.parseJson(jsonResult);
        String resultCode = StringUtil.trans2Str(returnMap.get("resultCode"));
        String message = StringUtil.trans2Str(returnMap.get("message"));
        result = new HashMap<String, Object>();
        if ("00000".equals(resultCode)) {
            if ("00".equals(returnMap.get("dfState"))) {
                result.put("resCode", "0000");
                result.put("resDesc", returnMap.get("dfDesc"));
            } else if ("01".equals(returnMap.get("dfState"))) {
                result.put("resCode", "1111");
                result.put("resDesc", returnMap.get("dfDesc"));
            } else if ("02".equals(returnMap.get("dfState"))) {
                result.put("resCode", "1009");
                result.put("resDesc", returnMap.get("dfDesc"));
            }
        } else {
            result.put("resCode", "1008");
            result.put("resDesc", message);
        }
        log.info((Object)("daifu---\u8fd4\u56de" + result));
        return result;
    }

    @Override
    public TradeMchtAccountDetail querySinglePay(DfParam dfParam) {
        return null;
    }
}
