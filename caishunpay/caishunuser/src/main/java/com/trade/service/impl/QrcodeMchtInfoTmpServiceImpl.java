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
import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.trade.bean.own.QrcodeMchtInfoTmp;
import com.trade.dao.QrcodeMchtInfoTmpDao;
import com.trade.service.QrcodeMchtInfoTmpService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QrcodeMchtInfoTmpServiceImpl
        implements QrcodeMchtInfoTmpService {
    @Autowired
    private QrcodeMchtInfoTmpDao qrcodeMchtInfoTmpDaoImpl;
    @Autowired
    private TradeMchtAccountDao tradeMchtAccountDaoImpl;

    @Override
    public QrcodeMchtInfoTmp getMchtInfo(String mchtNo) {
        return this.qrcodeMchtInfoTmpDaoImpl.getMchtInfo(mchtNo);
    }

    @Override
    public PageModle<QrcodeMchtInfoTmp> listMchtInfoTmpByPage(Map<String, String> param, int pageNum, int perPageNum, OprInfo oprInfo) {
        return this.qrcodeMchtInfoTmpDaoImpl.listMchtInfoTmpByPage(param, pageNum, perPageNum, oprInfo);
    }

    @Override
    public PageModle<QrcodeMchtInfoTmp> listMchtInfoTmp4CheckByPage(Map<String, String> param, int pageNum, int perPageNum, OprInfo oprInfo) {
        return this.qrcodeMchtInfoTmpDaoImpl.listMchtInfoTmp4CheckByPage(param, pageNum, perPageNum, oprInfo);
    }

    @Override
    public List<QrcodeMchtInfoTmp> listAllMcht(Map<String, String> param, OprInfo oprInfo) {
        return this.qrcodeMchtInfoTmpDaoImpl.listAllMcht(param, oprInfo);
    }

    @Override
    public void saveMchtInfo(QrcodeMchtInfoTmp mchtInfo, TradeMchtAccount tradeMchtAccount) {
        this.qrcodeMchtInfoTmpDaoImpl.saveMchtInfo(mchtInfo);
        this.tradeMchtAccountDaoImpl.save(tradeMchtAccount);
    }

    @Override
    public void updateMchtInfo(QrcodeMchtInfoTmp mchtInfo) {
        this.qrcodeMchtInfoTmpDaoImpl.updateMchtInfo(mchtInfo);
    }
}
