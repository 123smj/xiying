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

import com.account.bean.TradeBranchAccount;
import com.account.bean.TradeMchtAccount;
import com.account.bean.TradeMchtAccountDetail;
import com.account.dao.DfListDao;
import com.account.dao.TradeBranchAccountDao;
import com.account.dao.TradeMchtAccountDao;
import com.account.dao.TradeMchtAccountDetailDao;
import com.account.service.TradeMchtAccountService;
import com.guoyin.rmi.DAIFU;
import com.gy.system.SysParamUtil;
import com.gy.util.DateUtil;
import com.gy.util.StringUtil;
import com.gy.util.UUIDGenerator;
import com.manage.bean.PageModle;
import com.trade.bean.QuickpayBean;
import com.trade.bean.TradeDfDayamount;
import com.trade.bean.WxpayScanCode;
import com.trade.bean.own.DfParam;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.bean.response.DfResponse;
import com.trade.dao.QrcodeMchtInfoDao;
import com.trade.dao.TradeDfDayamountDao;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeSourceEnum;
import com.trade.util.BeanUtil;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeMchtAccountServiceImpl
        implements TradeMchtAccountService {
    private static Logger log = Logger.getLogger(TradeMchtAccountServiceImpl.class);
    @Autowired
    private TradeMchtAccountDao tradeMchtAccountDaoImpl;
    @Autowired
    private TradeMchtAccountDetailDao tradeMchtAccountDetailDaoImpl;
    @Autowired
    private TradeBranchAccountDao tradeBranchAccountDaoImpl;
    @Autowired
    private QrcodeMchtInfoDao qrcodeMchtInfoDaoImpl;
    @Autowired
    private TradeDfDayamountDao tradeDfDayamountDaoImpl;
    private static Map<String, TradeMchtAccountDetail> dfMap = new ConcurrentHashMap<String, TradeMchtAccountDetail>();
    private static ConcurrentHashMap<String, String> accountMap = new ConcurrentHashMap();
    @Autowired
    private DfListDao dfListDao;
    public static final String DAIFU_URL = "112.74.126.228:9911";
    private static final String DAIFU_USER = "zhongzhuan";
    private static final String DAIFU_PASSWORD = "6fe08b3418d62c6d3efaf9fc07766f98";
    public static final String ACC_TYPE_INCOME = "1";
    public static final String ACC_TYPE_OUTCOME = "2";
    public static final String STATUS_SUCCESS = "00";
    public static final String STATUS_WAITING_DAIFU = "01";
    public static final String STATUS_ERROR_DAIFU = "02";
    public static final String STATUS_ERROR_PREDAIFU = "09";

    @Override
    public synchronized boolean addAccount(String mchtNo, int totalFee) {
        TradeMchtAccount mchtAccount;
        block3:
        {
            try {
                mchtAccount = this.tradeMchtAccountDaoImpl.get(mchtNo);
                if (mchtAccount != null) break block3;
                return false;
            } catch (Exception ex) {
                return false;
            }
        }
        int balance = mchtAccount.getBalance();
        mchtAccount.setBalance(balance += totalFee);
        mchtAccount.setUpdateTime(DateUtil.getCurrTime());
        this.tradeMchtAccountDaoImpl.update(mchtAccount);
        return true;
    }

    public synchronized boolean decreaseAccount(String mchtNo, int totalFee) {
        TradeMchtAccount mchtAccount;
        int balance;
        block5:
        {
            block4:
            {
                try {
                    mchtAccount = this.tradeMchtAccountDaoImpl.get(mchtNo);
                    if (mchtAccount != null) break block4;
                    return false;
                } catch (Exception ex) {
                    return false;
                }
            }
            balance = mchtAccount.getBalance();
            if ((balance -= totalFee) >= 0) break block5;
            return false;
        }
        mchtAccount.setBalance(balance);
        mchtAccount.setUpdateTime(DateUtil.getCurrTime());
        this.tradeMchtAccountDaoImpl.update(mchtAccount);
        return true;
    }

    public synchronized boolean addBranchAccount(String branchNo, int totalFee) {
        TradeBranchAccount branchAccount;
        block3:
        {
            try {
                branchAccount = this.tradeBranchAccountDaoImpl.get(branchNo);
                if (branchAccount != null) break block3;
                return false;
            } catch (Exception ex) {
                return false;
            }
        }
        int balance = branchAccount.getBalance();
        branchAccount.setBalance(balance += totalFee);
        branchAccount.setUpdateTime(DateUtil.getCurrTime());
        this.tradeBranchAccountDaoImpl.update(branchAccount);
        return true;
    }

    public synchronized boolean decreaseBranchAccount(String branchNo, int totalFee) {
        TradeBranchAccount branchAccount;
        int balance;
        block5:
        {
            block4:
            {
                try {
                    branchAccount = this.tradeBranchAccountDaoImpl.get(branchNo);
                    if (branchAccount != null) break block4;
                    return false;
                } catch (Exception ex) {
                    return false;
                }
            }
            balance = branchAccount.getBalance();
            if ((balance -= totalFee) >= 0) break block5;
            return false;
        }
        branchAccount.setBalance(balance);
        branchAccount.setUpdateTime(DateUtil.getCurrTime());
        this.tradeBranchAccountDaoImpl.update(branchAccount);
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public ResponseEnum decreaseDetail(TradeMchtAccountDetail mchtAccountDetail, QrcodeMchtInfo qrcodeMcht) {
        String key = String.valueOf(mchtAccountDetail.getMchtNo()) + mchtAccountDetail.getDfSn();
        Map<String, TradeMchtAccountDetail> map = dfMap;
        synchronized (map) {
            if (dfMap.size() > 500) {
                dfMap.clear();
            }
            if (dfMap.get(key) != null) {
                log.error((Object) "daifu---ORDER_REPEAT");
                return ResponseEnum.FAIL_ORDER_NO_REPEAT;
            }
            dfMap.put(key, mchtAccountDetail);
        }
        if (this.tradeMchtAccountDetailDaoImpl.getByDfsn(mchtAccountDetail.getMchtNo(), mchtAccountDetail.getDfSn()) != null) {
            dfMap.remove(key);
            return ResponseEnum.FAIL_ORDER_NO_REPEAT;
        }
        mchtAccountDetail.setMemo("\u672a\u6263\u6b3e");
        mchtAccountDetail.setReceiptTime(DateUtil.getCurrTime());
        mchtAccountDetail.setAccType("2");
        mchtAccountDetail.setTimeEnd(DateUtil.getCurrTime());
        mchtAccountDetail.setStatus("02");
        this.tradeMchtAccountDetailDaoImpl.save(mchtAccountDetail);
        TradeDfDayamount dfDayamount = this.tradeDfDayamountDaoImpl.get(mchtAccountDetail.getReceiptPan(), DateUtil.getCurrentDay());
        System.out.println("\u54c8\u54c8\u54c8aaa----------" + (mchtAccountDetail.getReceiptAmount() > qrcodeMcht.getDfcard_day_limit()));
        if (mchtAccountDetail.getReceiptAmount() > qrcodeMcht.getDfcard_day_limit() || dfDayamount != null && dfDayamount.getMoney_amount() + mchtAccountDetail.getReceiptAmount() > qrcodeMcht.getDfcard_day_limit()) {
            mchtAccountDetail.setDfDesc(ResponseEnum.OUT_DFDAYAMOUNT_LIMIT.getMemo());
            this.tradeMchtAccountDetailDaoImpl.update(mchtAccountDetail);
            return ResponseEnum.OUT_DFDAYAMOUNT_LIMIT;
        }
        boolean result = this.decreaseAccount(mchtAccountDetail.getMchtNo(), mchtAccountDetail.getMchtIncome());
        ResponseEnum response = ResponseEnum.FAIL_SYSTEM;
        if (!result) {
            mchtAccountDetail.setStatus("02");
            response = ResponseEnum.BALANCE_EMPTY;
            dfMap.remove(key);
            return response;
        }
        mchtAccountDetail.setMemo("\u5df2\u6263\u6b3e");
        Map<String, Object> dfResult = null;
        try {
            dfResult = this.daifu(mchtAccountDetail);
            if (dfResult == null) {
                response = ResponseEnum.BACK_EXCEPTION;
                mchtAccountDetail.setStatus("02");
            } else {
                mchtAccountDetail.setDfStatus(StringUtil.trans2Str(dfResult.get("resCode")));
                mchtAccountDetail.setDfDesc(StringUtil.trans2Str(dfResult.get("resDesc")));
                if ("0000".equals(dfResult.get("resCode")) || "1111".equals(dfResult.get("resCode"))) {
                    mchtAccountDetail.setStatus("01");
                    response = ResponseEnum.SUCCESS;
                    this.dealDfAmount(mchtAccountDetail);
                } else {
                    response = ResponseEnum.DAIFU_ERROR;
                    mchtAccountDetail.setStatus("02");
                    this.addAccount(mchtAccountDetail.getMchtNo(), mchtAccountDetail.getMchtIncome());
                    mchtAccountDetail.setMemo("\u672a\u6263\u6b3e");
                }
            }
        } catch (RemoteException re) {
            log.info((Object) ("daifu---\u5f02\u5e38" + re.getMessage()));
            mchtAccountDetail.setStatus("02");
            this.addAccount(mchtAccountDetail.getMchtNo(), mchtAccountDetail.getMchtIncome());
            mchtAccountDetail.setMemo("\u672a\u6263\u6b3e");
        } catch (Exception e) {
            log.info((Object) ("daifu---\u5f02\u5e38" + e.getMessage()));
            mchtAccountDetail.setStatus("02");
            e.printStackTrace();
        }
        dfMap.remove(key);
        this.tradeMchtAccountDetailDaoImpl.update(mchtAccountDetail);
        return response;
    }

    private synchronized void dealDfAmount(TradeMchtAccountDetail mchtAccountDetail) {
        TradeDfDayamount tradeDfDayamount = this.tradeDfDayamountDaoImpl.get(mchtAccountDetail.getReceiptPan(), DateUtil.getCurrentDay());
        if (tradeDfDayamount == null) {
            tradeDfDayamount = new TradeDfDayamount();
            tradeDfDayamount.setMcht_no(mchtAccountDetail.getMchtNo());
            tradeDfDayamount.setUpdate_time(DateUtil.getCurrTime());
            tradeDfDayamount.setBank_card_num(mchtAccountDetail.getReceiptPan());
            tradeDfDayamount.setBank_name(mchtAccountDetail.getReceiptBankNm());
            tradeDfDayamount.setReal_name(mchtAccountDetail.getReceiptName());
            tradeDfDayamount.setMoney_amount(mchtAccountDetail.getReceiptAmount());
            tradeDfDayamount.setDf_day(DateUtil.getCurrentDay());
            this.tradeDfDayamountDaoImpl.save(tradeDfDayamount);
        } else {
            tradeDfDayamount.setUpdate_time(DateUtil.getCurrTime());
            tradeDfDayamount.setMoney_amount(tradeDfDayamount.getMoney_amount() + mchtAccountDetail.getReceiptAmount());
            this.tradeDfDayamountDaoImpl.update(tradeDfDayamount);
        }
    }

    private Map<String, Object> daifu(TradeMchtAccountDetail mchtAccountDetail) throws Exception {
        log.info((Object) ("to---daifu:" + mchtAccountDetail.getAccountOrderNo() + "---" + mchtAccountDetail.getReceiptName() + "---" + mchtAccountDetail.getReceiptPan() + "---" + mchtAccountDetail.getReceiptBankNm() + "---" + mchtAccountDetail.getReceiptAmount()));
        if ("true".equals(SysParamUtil.getParam("is_df_moni"))) {
            HashMap<String, Object> result = new HashMap<String, Object>();
            result.put("resCode", "0000");
            result.put("resDesc", "success");
            return result;
        }
        Map result = DAIFU.pay((String) "zhongzhuan", (String) "6fe08b3418d62c6d3efaf9fc07766f98", (String) "112.74.126.228:9911", (Object[]) new Object[]{mchtAccountDetail.getAccountOrderNo(), mchtAccountDetail.getReceiptName(), mchtAccountDetail.getReceiptPan(), mchtAccountDetail.getReceiptBankNm(), String.valueOf(mchtAccountDetail.getReceiptAmount())});
        log.info((Object) ("daifu---\u8fd4\u56de" + result));
        return result;
    }

    @Override
    public ResponseEnum decreaseBranchDetail(TradeMchtAccountDetail mchtAccountDetail) {
        boolean result = this.decreaseBranchAccount(mchtAccountDetail.getBranchNo(), mchtAccountDetail.getReceiptAmount());
        if (!result) {
            return ResponseEnum.BALANCE_EMPTY;
        }
        mchtAccountDetail.setStatus("00");
        mchtAccountDetail.setReceiptTime(DateUtil.getCurrTime());
        mchtAccountDetail.setAccType("2");
        this.tradeMchtAccountDetailDaoImpl.save(mchtAccountDetail);
        return ResponseEnum.SUCCESS;
    }

    @Override
    public void addAccountDetail(QrcodeMchtInfo mchtBaseInf, int amount, int mchtFeeValue, int mchtIncome) {
        TradeMchtAccountDetail mchtAccountDetail = new TradeMchtAccountDetail();
        mchtAccountDetail.setAccountOrderNo(UUIDGenerator.getOrderIdByUUId("JZ"));
        mchtAccountDetail.setDfSn(UUIDGenerator.getOrderIdByUUId("JZ"));
        mchtAccountDetail.setMchtNo(mchtBaseInf.getMchtNo());
        mchtAccountDetail.setBranchNo(mchtBaseInf.getCompany_id());
        mchtAccountDetail.setStatus("00");
        mchtAccountDetail.setTradeSource("");
        mchtAccountDetail.setReceiptTime(DateUtil.getCurrTime());
        mchtAccountDetail.setAccType("1");
        mchtAccountDetail.setTotalAmount(amount);
        mchtAccountDetail.setMchtFeeValue(mchtFeeValue);
        mchtAccountDetail.setBranchFeeValue(0);
        mchtAccountDetail.setCostFeeValue(0);
        mchtAccountDetail.setMchtIncome(mchtIncome);
        mchtAccountDetail.setBranchIncome(0);
        mchtAccountDetail.setCostIncome(0);
        mchtAccountDetail.setReceiptAmount(amount);
        boolean result = this.addAccount(mchtBaseInf.getMchtNo(), mchtAccountDetail.getMchtIncome());
        if (result) {
            mchtAccountDetail.setStatus("00");
            mchtAccountDetail.setReceiptTime(DateUtil.getCurrTime());
        }
        this.tradeMchtAccountDetailDaoImpl.save(mchtAccountDetail);
    }

    @Override
    public synchronized void notifySuccess(WxpayScanCode tradeInf) {
    }

    @Override
    public synchronized void notifyQucikpaySuccess(QuickpayBean quickpayBean) {
    }

    private TradeMchtAccountDetail countProfit(QrcodeMchtInfo mchtBaseInf, WxpayScanCode tradeInf) {
        TradeMchtAccountDetail mchtAccountDetail = new TradeMchtAccountDetail();
        mchtAccountDetail.setAccountOrderNo(tradeInf.getOut_trade_no());
        mchtAccountDetail.setMchtNo(tradeInf.getGymchtId());
        mchtAccountDetail.setBranchNo(mchtBaseInf.getCompany_id());
        mchtAccountDetail.setStatus("01");
        mchtAccountDetail.setTradeSource(tradeInf.getTrade_source());
        mchtAccountDetail.setReceiptTime(DateUtil.getCurrTime());
        mchtAccountDetail.setAccType("1");
        mchtAccountDetail.setTotalAmount(tradeInf.getTotal_fee());
        double mchtRate = 0.0;
        double branchRate = 0.0;
        double costRate = 0.0;
        int mchtIncome = 0;
        int branchIncome = 0;
        int costIncome = 0;
        int mchtFeeValue = 0;
        int branchFeeValue = 0;
        int costFeeValue = 0;
        boolean mchtFeeMax = false;
        boolean branchFeeMax = false;
        boolean costFeeMax = false;
        boolean hasMaxFee = false;
        if (TradeSourceEnum.ALIPAY.getCode().equals(tradeInf.getTrade_source())) {
            mchtRate = mchtBaseInf.getAlipay_fee_value();
        } else if (TradeSourceEnum.WEPAY.getCode().equals(tradeInf.getTrade_source())) {
            mchtRate = mchtBaseInf.getWechat_fee_value();
        }
        mchtFeeValue = new BigDecimal(tradeInf.getTotal_fee()).multiply(new BigDecimal(mchtRate / 100.0)).setScale(0, RoundingMode.HALF_UP).intValue();
        branchFeeValue = new BigDecimal(tradeInf.getTotal_fee()).multiply(new BigDecimal(branchRate / 100.0)).setScale(0, RoundingMode.HALF_UP).intValue();
        costFeeValue = new BigDecimal(tradeInf.getTotal_fee()).multiply(new BigDecimal(costRate / 100.0)).setScale(0, RoundingMode.HALF_UP).intValue();
        mchtIncome = tradeInf.getTotal_fee() - mchtFeeValue;
        branchIncome = mchtFeeValue - branchFeeValue;
        costIncome = branchFeeValue - costFeeValue;
        mchtAccountDetail.setMchtRate(mchtRate);
        mchtAccountDetail.setBranchRate(branchRate);
        mchtAccountDetail.setCostRate(costRate);
        mchtAccountDetail.setMchtFeeValue(mchtFeeValue);
        mchtAccountDetail.setBranchFeeValue(branchFeeValue);
        mchtAccountDetail.setCostFeeValue(costFeeValue);
        mchtAccountDetail.setMchtIncome(mchtIncome);
        mchtAccountDetail.setBranchIncome(branchIncome);
        mchtAccountDetail.setCostIncome(costIncome);
        mchtAccountDetail.setReceiptAmount(mchtIncome);
        return mchtAccountDetail;
    }

    private TradeMchtAccountDetail countQuickpayProfit(QrcodeMchtInfo mchtBaseInf, QuickpayBean quickpayBean) {
        TradeMchtAccountDetail mchtAccountDetail = new TradeMchtAccountDetail();
        mchtAccountDetail.setAccountOrderNo(quickpayBean.getOut_trade_no());
        mchtAccountDetail.setMchtNo(quickpayBean.getGymchtId());
        mchtAccountDetail.setBranchNo(mchtBaseInf.getCompany_id());
        mchtAccountDetail.setStatus("01");
        mchtAccountDetail.setTradeSource(quickpayBean.getTrade_source());
        mchtAccountDetail.setReceiptTime(DateUtil.getCurrTime());
        mchtAccountDetail.setAccType("1");
        mchtAccountDetail.setTotalAmount(quickpayBean.getTotal_fee());
        double mchtRate = 0.0;
        double branchRate = 0.0;
        double costRate = 0.0;
        int mchtIncome = 0;
        int branchIncome = 0;
        int costIncome = 0;
        int mchtFeeValue = 0;
        int branchFeeValue = 0;
        int costFeeValue = 0;
        mchtRate = mchtBaseInf.getQuickpay_fee_value();
        mchtFeeValue = new BigDecimal(quickpayBean.getTotal_fee()).multiply(new BigDecimal(mchtRate / 100.0)).setScale(0, RoundingMode.HALF_UP).intValue();
        mchtIncome = quickpayBean.getTotal_fee() - mchtFeeValue;
        mchtAccountDetail.setMchtRate(mchtRate);
        mchtAccountDetail.setBranchRate(branchRate);
        mchtAccountDetail.setCostRate(costRate);
        mchtAccountDetail.setMchtFeeValue(mchtFeeValue);
        mchtAccountDetail.setBranchFeeValue(branchFeeValue);
        mchtAccountDetail.setCostFeeValue(costFeeValue);
        mchtAccountDetail.setMchtIncome(mchtIncome);
        mchtAccountDetail.setBranchIncome(branchIncome);
        mchtAccountDetail.setCostIncome(costIncome);
        mchtAccountDetail.setReceiptAmount(mchtIncome);
        return mchtAccountDetail;
    }

    @Override
    public TradeMchtAccount queryMchtAccount(String mchtNo) {
        return this.tradeMchtAccountDaoImpl.get(mchtNo);
    }

    @Override
    public DfResponse singlePay(DfParam dfParam, QrcodeMchtInfo qrcodeMcht) {
        DfResponse response = new DfResponse();
        response.setGymchtId(dfParam.getGymchtId());
        response.setDfSn(dfParam.getDfSn());
        TradeMchtAccountDetail mchtAccountDetail = new TradeMchtAccountDetail();
        BeanUtil.copyProperties((Object) dfParam, (Object) mchtAccountDetail);
        mchtAccountDetail.setAccountOrderNo(UUIDGenerator.getOrderIdByUUId("CZ"));
        mchtAccountDetail.setMchtNo(dfParam.getGymchtId());
        mchtAccountDetail.setMchtFeeValue(0);
        mchtAccountDetail.setSingle_extra_fee(qrcodeMcht.getSingle_extra_fee());
        mchtAccountDetail.setTotalAmount(qrcodeMcht.getSingle_extra_fee() + dfParam.getReceiptAmount());
        mchtAccountDetail.setMchtIncome(qrcodeMcht.getSingle_extra_fee() + dfParam.getReceiptAmount());
        ResponseEnum responseEnum = this.decreaseDetail(mchtAccountDetail, qrcodeMcht);
        response.setResultCode(responseEnum.getCode());
        response.setMessage(mchtAccountDetail.getDfStatus() != null ? String.valueOf(mchtAccountDetail.getDfStatus()) + "-" + mchtAccountDetail.getDfDesc() : responseEnum.getMemo());
        if (responseEnum == null) {
            response.setResultCode(ResponseEnum.FAIL_SYSTEM.getCode());
            response.setMessage(ResponseEnum.FAIL_SYSTEM.getMemo());
        } else if (ResponseEnum.SUCCESS.equals((Object) responseEnum)) {
            response = this.buildSinglepayResp(mchtAccountDetail, response);
        }
        return response;
    }

    @Override
    public TradeMchtAccountDetail querySinglePay(DfParam dfParam) {
        TradeMchtAccountDetail mchtAccountDetail = null;
        mchtAccountDetail = !StringUtil.isEmpty(dfParam.getDfTransactionId()) ? this.tradeMchtAccountDetailDaoImpl.get(dfParam.getDfTransactionId()) : this.tradeMchtAccountDetailDaoImpl.getByDfsn(dfParam.getGymchtId(), dfParam.getDfSn());
        if (mchtAccountDetail == null || !dfParam.getGymchtId().equals(mchtAccountDetail.getMchtNo())) {
            return null;
        }
        mchtAccountDetail = this.querySinglePayFromChannel(mchtAccountDetail);
        return mchtAccountDetail;
    }

    private TradeMchtAccountDetail querySinglePayFromChannel(TradeMchtAccountDetail mchtAccountDetail) {
//        if ("01".equals(mchtAccountDetail.getStatus())) {
//            try {
//                Map res = DAIFU.query((String) "zhongzhuan", (String) "6fe08b3418d62c6d3efaf9fc07766f98", (String) "112.74.126.228:9911", (Object[]) new Object[]{mchtAccountDetail.getAccountOrderNo()});
//                if (res != null) {
//                    mchtAccountDetail.setDfStatus(StringUtil.trans2Str(res.get("resCode")));
//                    mchtAccountDetail.setDfDesc(StringUtil.trans2Str(res.get("resDesc")).length() > 100 ? StringUtil.trans2Str(res.get("resDesc")).substring(0, 100) : StringUtil.trans2Str(res.get("resDesc")));
//                    mchtAccountDetail.setTimeEnd(DateUtil.getCurrTime());
//                    if ("0000".equals(res.get("resCode"))) {
//                        mchtAccountDetail.setStatus("00");
//                    } else if ("1111".equals(res.get("resCode"))) {
//                        mchtAccountDetail.setStatus("01");
//                    } else {
//                        mchtAccountDetail.setStatus("02");
//                    }
//                    this.tradeMchtAccountDetailDaoImpl.update(mchtAccountDetail);
//                }
//            } catch (Exception e) {
//                log.info((Object) ("daifu\u67e5\u8be2---\u5f02\u5e38" + e.getMessage()));
//                e.printStackTrace();
//            }
//        }
        return mchtAccountDetail;
    }

    @Override
    public void queryUnSuccessDaifuResult() {
        List<TradeMchtAccountDetail> list = this.tradeMchtAccountDetailDaoImpl.listUnSuccessDf();
        log.info((Object) ("\u67e5\u8be2\u4ee3\u4ed8\u7ed3\u679c\u5b9a\u65f6\u4efb\u52a1\uff0c\u672c\u6b21\u5f85\u67e5\u8be2\uff1a" + list.size()));
        for (TradeMchtAccountDetail mchtAccountDetail : list) {
            this.querySinglePayFromChannel(mchtAccountDetail);
        }
    }

    @Override
    public PageModle<TradeMchtAccount> listMchtAccountByPage(Map<String, String> param, int pageNum, int perPageNum) {
        return this.tradeMchtAccountDaoImpl.listMchtAccountByPage(param, pageNum, perPageNum);
    }

    @Override
    public PageModle<TradeMchtAccountDetail> listMchtAccountDetailByPage(Map<String, String> param, String accType, int pageNum, int perPageNum) {
        return this.tradeMchtAccountDetailDaoImpl.listMchtAccountDetailByPage(param, accType, pageNum, perPageNum);
    }

    @Override
    public List<TradeMchtAccountDetail> listAllAccountDetail(Map<String, String> param) {
        return this.tradeMchtAccountDetailDaoImpl.listAllAccountDetail(param);
    }

    private DfResponse buildSinglepayResp(TradeMchtAccountDetail mchtAccountDetail, DfResponse response) {
        response.setResultCode(ResponseEnum.SUCCESS.getCode());
        response.setMessage(ResponseEnum.SUCCESS.getMemo());
        response.setDfSn(mchtAccountDetail.getDfSn());
        response.setDfTransactionId(mchtAccountDetail.getAccountOrderNo());
        response.setDfState(mchtAccountDetail.getStatus());
        response.setDfDesc(mchtAccountDetail.getDfDesc());
        response.setTimeEnd(mchtAccountDetail.getTimeEnd());
        response.setNonce(StringUtil.getRandom(32));
        return response;
    }


    public static void payByOne() throws MalformedURLException, RemoteException, NotBoundException {
        String card = "6222003602112408541";
        String bankCode = "310581000236";
        String amount = "0.01";
        String name = "\u90d1\u4e49\u9713";
        String bankName = "\u5de5\u5546\u94f6\u884c";
        Map res = DAIFU.pay((String) "zhongzhuan", (String) "6fe08b3418d62c6d3efaf9fc07766f98", (String) "112.74.126.228:9911", (Object[]) new Object[]{"20170427001", name, card, bankName, amount});
        System.out.println(res.toString());
    }

    public static void check() throws MalformedURLException, RemoteException, NotBoundException {
        Map res = DAIFU.query((String) "zhongzhuan", (String) "6fe08b3418d62c6d3efaf9fc07766f98", (String) "112.74.126.228:9911", (Object[]) new Object[]{"20170427001"});
        System.out.println(res.toString());
    }
}
