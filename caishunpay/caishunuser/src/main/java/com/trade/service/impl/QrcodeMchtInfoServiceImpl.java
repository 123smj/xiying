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
import com.trade.bean.own.JumpListBean;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.dao.JumpListDao;
import com.trade.dao.QrcodeMchtInfoDao;
import com.trade.service.QrcodeMchtInfoService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QrcodeMchtInfoServiceImpl
        implements QrcodeMchtInfoService {
    @Autowired
    private QrcodeMchtInfoDao qrcodeMchtInfoDaoImpl;
    @Autowired
    private TradeMchtAccountDao tradeMchtAccountDaoImpl;
    @Autowired
    private JumpListDao jumpListDaoImpl;
    @Autowired
    private QrcodeMchtCache qrcodeMchtCache;
    private static Map<String, Integer> jumpPosMap = new ConcurrentHashMap<String, Integer>();

    @Override
    public QrcodeMchtInfo getMchtInfo(String mchtNo) {
        return this.qrcodeMchtInfoDaoImpl.getMchtInfo(mchtNo);
    }

    @Override
    public QrcodeChannelInf getChannelInf(String channelId, String channelMchtNo) {
        return this.qrcodeMchtInfoDaoImpl.getChannelInf(channelId, channelMchtNo);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public QrcodeChannelInf getChannelInfBalance(String jumpGroup, String tradeSource) {
        List<JumpListBean> list = this.qrcodeMchtCache.getJumpList(jumpGroup, tradeSource);
        if (list == null || list.size() == 0) {
            return null;
        }
        String key = String.valueOf(jumpGroup) + tradeSource;
        JumpListBean jumpListBean = null;
        Map<String, Integer> map = jumpPosMap;
        synchronized (map) {
            Integer pos = jumpPosMap.get(key);
            if (pos == null || pos >= list.size()) {
                pos = 0;
            }
            jumpListBean = list.get(pos);
            pos = pos + 1;
            jumpPosMap.put(key, pos);
        }
        return this.qrcodeMchtInfoDaoImpl.getChannelInf(jumpListBean.getChannel_id(), jumpListBean.getChannel_mcht_no());
    }

    @Override
    public PageModle<QrcodeMchtInfo> listMchtInfoByPage(Map<String, String> param, int pageNum, int perPageNum, OprInfo oprInfo) {
        return this.qrcodeMchtInfoDaoImpl.listMchtInfoByPage(param, pageNum, perPageNum, oprInfo);
    }

    @Override
    public List<QrcodeMchtInfo> listAllMcht(Map<String, String> param, OprInfo oprInfo) {
        return this.qrcodeMchtInfoDaoImpl.listAllMcht(param, oprInfo);
    }

    @Override
    public PageModle<QrcodeChannelInf> listChannelMchtInfoByPage(Map<String, String> param, int pageNum, int perPageNum) {
        return this.qrcodeMchtInfoDaoImpl.listChannelMchtInfoByPage(param, pageNum, perPageNum);
    }

    @Override
    public void saveMchtInfo(QrcodeMchtInfo mchtInfo, TradeMchtAccount tradeMchtAccount) {
        this.qrcodeMchtInfoDaoImpl.saveMchtInfo(mchtInfo);
        this.tradeMchtAccountDaoImpl.save(tradeMchtAccount);
    }

    @Override
    public void saveMchtInfo(QrcodeMchtInfo mchtInfo) {
        this.qrcodeMchtInfoDaoImpl.saveMchtInfo(mchtInfo);
    }

    @Override
    public void updateMchtInfo(QrcodeMchtInfo mchtInfo) {
        this.qrcodeMchtInfoDaoImpl.updateMchtInfo(mchtInfo);
    }

    @Override
    public void saveChannelInf(QrcodeChannelInf channelInf) {
        this.qrcodeMchtInfoDaoImpl.saveChannelInf(channelInf);
    }

    @Override
    public void updateChannelInf(QrcodeChannelInf channelInf) {
        this.qrcodeMchtInfoDaoImpl.updateChannelInf(channelInf);
    }
}
