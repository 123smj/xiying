/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.trade.service.impl;

import com.account.bean.TradeMchtAccount;
import com.account.dao.TradeMchtAccountDao;
import com.gy.cache.QrcodeMchtCache;
import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.trade.bean.BankCardPay;
import com.trade.bean.own.JumpListBean;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.own.PayChannelInf;
import com.trade.bean.own.QrcodeMchtInfoTmp;
import com.trade.dao.BankCardPayDao;
import com.trade.dao.JumpListDao;
import com.trade.dao.MerchantInfDao;
import com.trade.dao.QrcodeMchtInfoTmpDao;
import com.trade.enums.TradeSource;
import com.trade.service.MerchantInfService;
import com.trade.util.BeanUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.trade.util.RandomUtil;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.alibaba.fastjson.JSON.toJSONString;

@Service
public class MerchantInfServiceImpl implements MerchantInfService {
    @Autowired
    private MerchantInfDao merchantInfDaoImpl;
    @Autowired
    private QrcodeMchtInfoTmpDao qrcodeMchtInfoTmpDaoImpl;
    @Autowired
    private TradeMchtAccountDao tradeMchtAccountDaoImpl;
    @Autowired
    private JumpListDao jumpListDaoImpl;
    @Autowired
    private QrcodeMchtCache qrcodeMchtCache;
    @Autowired
    protected BankCardPayDao bankCardPayDao;
    private static Map<String, Integer> jumpPosMap = new ConcurrentHashMap<String, Integer>();

    @Override
    public MerchantInf getMchtInfo(String mchtNo) {
        return this.merchantInfDaoImpl.getMchtInfo(mchtNo);
    }

    @Override
    public PayChannelInf getChannelInf(String channelId, String channelMchtNo) {
        return this.merchantInfDaoImpl.getChannelInf(channelId, channelMchtNo);
    }

    /**
     * 跳码选取支付渠道
     */
    @Override
    public PayChannelInf getChannelInfBalance(String jumpGroup, String tradeSource) {
        List<JumpListBean> list = this.qrcodeMchtCache.getJumpList(jumpGroup, tradeSource);
        Map<JumpListBean , Integer> beanMap = new HashMap<>();
        for(JumpListBean bean : list)
            beanMap.put(bean,bean.getWeight());
        if (beanMap.size() == 0) {
            return null;
        }
        JumpListBean jumpListBean = RandomUtil.weightRandom(beanMap).get();
        return this.merchantInfDaoImpl.getChannelInf(jumpListBean.getChannel_id(), jumpListBean.getChannel_mcht_no());
    }

    @Override
    public PayChannelInf getChannelInf(MerchantInf merchantInf, TradeSource tradeSource) {
        if("1".equals(merchantInf.getJump_flag())){
            return getChannelInfBalance(merchantInf.getJump_group(),tradeSource.getCode());
        } else {
            return getChannelInf(merchantInf.getChannel_id(),merchantInf.getChannelMchtNo());
        }
    }

    @Override
    public PageModle<MerchantInf> listMchtInfoByPage(Map<String, String> param, int pageNum, int perPageNum, OprInfo oprInfo) {
        return this.merchantInfDaoImpl.listMchtInfoByPage(param, pageNum, perPageNum, oprInfo);
    }

    @Override
    public List<MerchantInf> listAllMcht(Map<String, String> param, OprInfo oprInfo) {
        return this.merchantInfDaoImpl.listAllMcht(param, oprInfo);
    }

    @Override
    public PageModle<PayChannelInf> listChannelMchtInfoByPage(Map<String, String> param, int pageNum, int perPageNum) {
        return this.merchantInfDaoImpl.listChannelMchtInfoByPage(param, pageNum, perPageNum);
    }

    @Override
    public void saveMchtInfo(MerchantInf mchtInfo, TradeMchtAccount tradeMchtAccount) {
        QrcodeMchtInfoTmp mchtInfoTemp = new QrcodeMchtInfoTmp();
        BeanUtil.copyProperties((Object)mchtInfo, (Object)mchtInfoTemp);
        this.merchantInfDaoImpl.saveMchtInfo(mchtInfo);
        this.qrcodeMchtInfoTmpDaoImpl.saveMchtInfo(mchtInfoTemp);
        this.tradeMchtAccountDaoImpl.save(tradeMchtAccount);
    }

    @Override
    public void saveMchtInfo(MerchantInf mchtInfo) {
        this.merchantInfDaoImpl.saveMchtInfo(mchtInfo);
    }

    @Override
    public void updateMchtInfo(MerchantInf mchtInfo) {
        this.merchantInfDaoImpl.updateMchtInfo(mchtInfo);
    }

    @Override
    public void saveChannelInf(PayChannelInf channelInf) {
        this.merchantInfDaoImpl.saveChannelInf(channelInf);
    }

    @Override
    public void updateChannelInf(PayChannelInf channelInf) {
        this.merchantInfDaoImpl.updateChannelInf(channelInf);
    }

    @Override
    public void updateQuickpayBean(BankCardPay BankCardPay) {
        this.bankCardPayDao.update(BankCardPay);
    }
}
