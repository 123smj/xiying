/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.manage.service.impl;

import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.manage.bean.RateInfo;
import com.manage.dao.OprInfoDao;
import com.manage.dao.RateInfoDao;
import com.manage.service.OprInfoService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OprInfoServiceImpl
implements OprInfoService {
    @Autowired
    private OprInfoDao oprInfoDaoImpl;
    @Autowired
    private RateInfoDao rateInfoDaoImpl;

    @Override
    public void delete(OprInfo oprInfo) {
        this.oprInfoDaoImpl.delete(oprInfo);
    }

    @Override
    public OprInfo get(String oprId) {
        return this.oprInfoDaoImpl.get(oprId);
    }

    @Override
    public void save(OprInfo oprInfo) {
        this.oprInfoDaoImpl.save(oprInfo);
    }

    @Override
    public void update(OprInfo oprInfo) {
        this.oprInfoDaoImpl.update(oprInfo);
    }

    @Override
    public void save(OprInfo oprInfo, RateInfo rateInfo) {
        this.save(oprInfo);
        this.rateInfoDaoImpl.save(rateInfo);
    }

    @Override
    public void update(OprInfo oprInfo, RateInfo rateInfo) {
        this.update(oprInfo);
        this.rateInfoDaoImpl.update(rateInfo);
    }

    @Override
    public RateInfo getRate(String oprId) {
        return this.rateInfoDaoImpl.get(oprId);
    }

    @Override
    public PageModle<Map<String, String>> listCompanyInfoByPage(Map<String, String> param, int pageNum, int perPageNum) {
        return this.rateInfoDaoImpl.listCompanyInfoByPage(param, pageNum, perPageNum);
    }
}
