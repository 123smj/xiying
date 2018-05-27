/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.hibernate.SessionFactory
 *  org.springframework.orm.hibernate4.HibernateTemplate
 *  org.springframework.orm.hibernate4.support.HibernateDaoSupport
 *  org.springframework.stereotype.Component
 */
package com.tuser.dao.impl;

import com.tuser.bean.TradeMchtRoleFuncMap;
import com.tuser.bean.TradeRoleFuncMapPK;
import com.tuser.dao.TradeRoleFuncMapDao;

import java.io.Serializable;
import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class TradeRoleFuncMapDaoImpl
        extends HibernateDaoSupport
        implements TradeRoleFuncMapDao {
    @Override
    public void delete(TradeMchtRoleFuncMap tradeRoleFuncMap) {
        this.getHibernateTemplate().delete((Object) tradeRoleFuncMap);
    }

    @Override
    public TradeMchtRoleFuncMap get(TradeRoleFuncMapPK id) {
        return (TradeMchtRoleFuncMap) this.getHibernateTemplate().get(TradeMchtRoleFuncMap.class, (Serializable) id);
    }

    @Override
    public void save(TradeMchtRoleFuncMap tradeRoleFuncMap) {
        this.getHibernateTemplate().save((Object) tradeRoleFuncMap);
    }

    @Override
    public void update(TradeMchtRoleFuncMap tradeRoleFuncMap) {
        this.getHibernateTemplate().update((Object) tradeRoleFuncMap);
    }

    @Resource(name = "sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
}
