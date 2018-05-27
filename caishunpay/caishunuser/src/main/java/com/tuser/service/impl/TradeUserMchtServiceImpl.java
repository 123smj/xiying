/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.tuser.service.impl;

import com.tuser.bean.TradeUserMcht;
import com.tuser.dao.TradeUserMchtDao;
import com.tuser.service.TradeUserMchtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeUserMchtServiceImpl
        implements TradeUserMchtService {
    @Autowired
    private TradeUserMchtDao tradeUserMchtDaoImpl;

    @Override
    public void delete(TradeUserMcht tradeUserMcht) {
        this.tradeUserMchtDaoImpl.delete(tradeUserMcht);
    }

    @Override
    public TradeUserMcht get(String mchtNo) {
        return this.tradeUserMchtDaoImpl.get(mchtNo);
    }

    @Override
    public void save(TradeUserMcht tradeUserMcht) {
        this.tradeUserMchtDaoImpl.save(tradeUserMcht);
    }

    @Override
    public void update(TradeUserMcht tradeUserMcht) {
        this.tradeUserMchtDaoImpl.update(tradeUserMcht);
    }
}
