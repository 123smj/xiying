/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 */
package com.trade.service.impl;

import com.trade.bean.TradeDfDayamount;
import com.trade.dao.TradeDfDayamountDao;
import com.trade.service.TradeDfDayamountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TradeDfDayamountServiceImpl implements TradeDfDayamountService {
    @Autowired
    private TradeDfDayamountDao tradeDfDayamountDaoImpl;

    @Override
    public TradeDfDayamount get(String bankCardNum, String dfDay) {
        return this.tradeDfDayamountDaoImpl.get(bankCardNum, dfDay);
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    @Override
    public void save(TradeDfDayamount dfDayamount) {
        this.tradeDfDayamountDaoImpl.save(dfDayamount);
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    @Override
    public void update(TradeDfDayamount dfDayamount) {
        this.tradeDfDayamountDaoImpl.update(dfDayamount);
    }
}
