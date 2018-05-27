/*
 * Decompiled with CFR 0_124.
 */
package com.account.service;

import com.account.bean.TradeMchtAccount;
import com.account.bean.TradeMchtAccountDetail;
import com.manage.bean.PageModle;
import com.trade.bean.BankCardPay;
import com.trade.bean.ThirdPartyPayDetail;
import com.trade.bean.own.DfParam;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.response.DfResponse;
import com.trade.enums.ResponseEnum;
import java.util.List;
import java.util.Map;

public interface TradeMchtAccountService {
    public boolean addAccount(String var1, int var2);

    public int decreaseAccount(String var1, int var2);

    public void addAccountDetail(MerchantInf var1, int var2, int var3, int var4);

    public void notifySuccess(ThirdPartyPayDetail var1);

    public void notifyQucikpaySuccess(BankCardPay var1);

    public TradeMchtAccount queryMchtAccount(String var1);

    public ResponseEnum decreaseDetail(TradeMchtAccountDetail var1, MerchantInf var2);

    public ResponseEnum decreaseBranchDetail(TradeMchtAccountDetail var1);

    public DfResponse singlePay(DfParam var1, MerchantInf var2);

    public TradeMchtAccountDetail querySinglePay(DfParam var1, MerchantInf var2);

    public PageModle<TradeMchtAccount> listMchtAccountByPage(Map<String, String> var1, int var2, int var3);

    public PageModle<TradeMchtAccountDetail> listMchtAccountDetailByPage(Map<String, String> var1, String var2, int var3, int var4);

    public List<TradeMchtAccountDetail> listAllAccountDetail(Map<String, String> var1);

    public void queryUnSuccessDaifuResult();
}
