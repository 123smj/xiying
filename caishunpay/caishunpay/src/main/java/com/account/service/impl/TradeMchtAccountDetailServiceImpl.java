/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 */
package com.account.service.impl;

import com.account.bean.TradeMchtAccountDetail;
import com.account.dao.TradeMchtAccountDetailDao;
import com.account.service.TradeMchtAccountDetailService;
import com.manage.bean.PageModle;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TradeMchtAccountDetailServiceImpl
implements TradeMchtAccountDetailService {
    @Autowired
    private TradeMchtAccountDetailDao tradeMchtAccountDetailDaoImpl;

    @Override
    public TradeMchtAccountDetail get(String accountOrderNo) {
        return this.tradeMchtAccountDetailDaoImpl.get(accountOrderNo);
    }

    @Override
    public TradeMchtAccountDetail getByDfsn(String mchtNo, String dfSn) {
        return this.tradeMchtAccountDetailDaoImpl.getByDfsn(mchtNo, dfSn);
    }

    @Override
    public List<TradeMchtAccountDetail> listAllAccountDetail(Map<String, String> param) {
        return this.tradeMchtAccountDetailDaoImpl.listAllAccountDetail(param);
    }

    @Override
    public PageModle<TradeMchtAccountDetail> listMchtAccountDetailByPage(Map<String, String> param, String accType, int pageNum, int perPageNum) {
        return this.tradeMchtAccountDetailDaoImpl.listMchtAccountDetailByPage(param, accType, pageNum, perPageNum);
    }

    @Override
    public List<TradeMchtAccountDetail> listUnSuccessDf() {
        return this.tradeMchtAccountDetailDaoImpl.listUnSuccessDf();
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    @Override
    public void save(TradeMchtAccountDetail mchtAccountDetail) {
        this.tradeMchtAccountDetailDaoImpl.save(mchtAccountDetail);
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    @Override
    public void update(TradeMchtAccountDetail mchtAccountDetail) {
        this.tradeMchtAccountDetailDaoImpl.update(mchtAccountDetail);
    }
}
