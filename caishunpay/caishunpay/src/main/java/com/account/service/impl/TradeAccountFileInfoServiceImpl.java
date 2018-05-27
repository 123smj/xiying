/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpSession
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.account.service.impl;

import com.account.bean.TradeAccountFileDetail;
import com.account.bean.TradeAccountFileInfo;
import com.account.dao.TradeAccountFileDetailDao;
import com.account.dao.TradeAccountFileInfoDao;
import com.account.service.TradeAccountFileInfoService;
import com.account.service.TradeMchtAccountService;
import com.gy.util.DateUtil;
import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.trade.bean.own.MerchantInf;
import com.trade.dao.MerchantInfDao;
import com.trade.enums.AccountEnum;
import com.trade.enums.MchtStatusEnum;
import com.trade.enums.ResponseEnum;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeAccountFileInfoServiceImpl
implements TradeAccountFileInfoService {
    @Autowired
    private HttpSession session;
    @Autowired
    private TradeAccountFileInfoDao tradeAccountFileInfoDaoImpl;
    @Autowired
    private TradeAccountFileDetailDao tradeAccountFileDetailDaoImpl;
    @Autowired
    private MerchantInfDao merchantInfDaoImpl;
    @Autowired
    private TradeMchtAccountService tradeMchtAccountServiceImpl;

    @Override
    public TradeAccountFileInfo getAccountFileInfo(Integer offerSeq) {
        return this.tradeAccountFileInfoDaoImpl.get(offerSeq);
    }

    @Override
    public PageModle<TradeAccountFileInfo> listAccountFileByPage(Map<String, String> param, int pageNum, int perPageNum) {
        return this.tradeAccountFileInfoDaoImpl.listAccountFileByPage(param, pageNum, perPageNum);
    }

    @Override
    public void save(TradeAccountFileInfo tradeAccountFileInfo) {
        this.tradeAccountFileInfoDaoImpl.save(tradeAccountFileInfo);
    }

    @Override
    public void update(TradeAccountFileInfo tradeAccountFileInfo) {
        this.tradeAccountFileInfoDaoImpl.update(tradeAccountFileInfo);
    }

    @Override
    public PageModle<TradeAccountFileDetail> listDetailByPage(Map<String, String> param, int pageNum, int perPageNum) {
        return this.tradeAccountFileDetailDaoImpl.listDetailByPage(param, pageNum, perPageNum);
    }

    @Override
    public void save(TradeAccountFileDetail tradeAccountFileDetail) {
        this.tradeAccountFileDetailDaoImpl.save(tradeAccountFileDetail);
    }

    @Override
    public void update(TradeAccountFileDetail tradeAccountFileDetail) {
        this.tradeAccountFileDetailDaoImpl.update(tradeAccountFileDetail);
    }

    @Override
    public void saveDetails(List<TradeAccountFileDetail> details) {
        TradeAccountFileDetail accountDetail = null;
        for (TradeAccountFileDetail tradeAccountFileDetail : details) {
            accountDetail = this.tradeAccountFileDetailDaoImpl.get(tradeAccountFileDetail.getMcht_no(), tradeAccountFileDetail.getDfsn());
            if (accountDetail != null) {
                throw new IllegalArgumentException("\u8d26\u76ee\u5e8f\u5217\u53f7\u91cd\u590d");
            }
            this.tradeAccountFileDetailDaoImpl.save(tradeAccountFileDetail);
        }
    }

    @Override
    public synchronized String checkAccept(Integer offerSeq) {
        TradeAccountFileInfo tradeAccountFileInfo = this.tradeAccountFileInfoDaoImpl.get(offerSeq);
        if (tradeAccountFileInfo == null) {
            return "\u6587\u4ef6\u6279\u6b21\u4e0d\u5b58\u5728";
        }
        if (!MchtStatusEnum.ADD.getCode().equals(tradeAccountFileInfo.getStatus())) {
            return "\u8be5\u6279\u6b21\u5df2\u88ab\u5ba1\u6838\u5904\u7406\uff0c\u8bf7\u5237\u65b0\u6570\u636e";
        }
        List<TradeAccountFileDetail> list = this.tradeAccountFileDetailDaoImpl.getAccoutFileDetailList(offerSeq);
        if (list == null || list.size() == 0) {
            return "\u672a\u627e\u5230\u8be5\u6279\u6b21\u5bf9\u5e94\u7684\u6570\u636e\uff0c\u8bf7\u6838\u5b9e";
        }
        OprInfo oprInfo = (OprInfo)this.session.getAttribute("oprInfo");
        tradeAccountFileInfo.setCheckOpr(oprInfo.getOpr_id());
        tradeAccountFileInfo.setCheckTime(DateUtil.getCurrTime());
        tradeAccountFileInfo.setStatus(MchtStatusEnum.NORMAL.getCode());
        MerchantInf mchtBaseInf = null;
        for (TradeAccountFileDetail tradeAccountFileDetail : list) {
            mchtBaseInf = this.merchantInfDaoImpl.getMchtInfo(tradeAccountFileDetail.getMcht_no());
            if (mchtBaseInf == null) {
                throw new IllegalArgumentException("\u5546\u6237\u4e0d\u5b58\u5728:" + tradeAccountFileDetail.getMcht_no());
            }
            int mchtFeeValueFen = tradeAccountFileDetail.getTrade_amount() - tradeAccountFileDetail.getMcht_income();
            this.tradeMchtAccountServiceImpl.addAccountDetail(mchtBaseInf, tradeAccountFileDetail.getTrade_amount(), mchtFeeValueFen, tradeAccountFileDetail.getMcht_income());
            tradeAccountFileDetail.setStatus(AccountEnum.ACCOUNT_COMPLETE.getCode());
            this.tradeAccountFileDetailDaoImpl.update(tradeAccountFileDetail);
        }
        return ResponseEnum.SUCCESS.getCode();
    }

    @Override
    public synchronized String checkRefuse(Integer offerSeq, String refuseReason) {
        TradeAccountFileInfo tradeAccountFileInfo = this.tradeAccountFileInfoDaoImpl.get(offerSeq);
        if (tradeAccountFileInfo == null) {
            return "\u6587\u4ef6\u6279\u6b21\u4e0d\u5b58\u5728";
        }
        if (!MchtStatusEnum.ADD.getCode().equals(tradeAccountFileInfo.getStatus())) {
            return "\u8be5\u6279\u6b21\u5df2\u88ab\u5ba1\u6838\u5904\u7406\uff0c\u8bf7\u5237\u65b0\u6570\u636e";
        }
        OprInfo oprInfo = (OprInfo)this.session.getAttribute("oprInfo");
        tradeAccountFileInfo.setCheckOpr(oprInfo.getOpr_id());
        tradeAccountFileInfo.setCheckTime(DateUtil.getCurrTime());
        tradeAccountFileInfo.setDescription(refuseReason);
        tradeAccountFileInfo.setStatus(MchtStatusEnum.ADD_REFUSE.getCode());
        this.tradeAccountFileInfoDaoImpl.update(tradeAccountFileInfo);
        return ResponseEnum.SUCCESS.getCode();
    }
}
