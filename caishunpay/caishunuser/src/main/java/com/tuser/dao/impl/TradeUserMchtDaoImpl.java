/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.springframework.orm.hibernate4.HibernateTemplate
 *  org.springframework.stereotype.Component
 */
package com.tuser.dao.impl;

import com.tuser.bean.TradeUserMcht;
import com.tuser.dao.TradeUserMchtDao;
import com.tuser.dao.impl.BaseDao;

import java.io.Serializable;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Component;

@Component
public class TradeUserMchtDaoImpl
        extends BaseDao
        implements TradeUserMchtDao {
    @Override
    public void delete(TradeUserMcht tradeUserMcht) {
        this.getHibernateTemplate().delete((Object) tradeUserMcht);
    }

    @Override
    public TradeUserMcht get(String mchtNo) {
        return (TradeUserMcht) this.getHibernateTemplate().get(TradeUserMcht.class, (Serializable) ((Object) mchtNo));
    }

    @Override
    public void save(TradeUserMcht tradeUserMcht) {
        this.getHibernateTemplate().save((Object) tradeUserMcht);
    }

    @Override
    public void update(TradeUserMcht tradeUserMcht) {
        this.getHibernateTemplate().update((Object) tradeUserMcht);
    }
}
