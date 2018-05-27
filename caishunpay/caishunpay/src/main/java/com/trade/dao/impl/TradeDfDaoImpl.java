/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.hibernate.SessionFactory
 *  org.springframework.orm.hibernate4.HibernateTemplate
 *  org.springframework.orm.hibernate4.support.HibernateDaoSupport
 *  org.springframework.stereotype.Component
 */
package com.trade.dao.impl;

import com.trade.bean.TradeDfbean;
import com.trade.dao.TradeDfDao;
import java.io.Serializable;
import javax.annotation.Resource;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class TradeDfDaoImpl
extends HibernateDaoSupport
implements TradeDfDao {
    @Override
    public TradeDfbean get(String outTradeNo) {
        return (TradeDfbean)this.getHibernateTemplate().get(TradeDfbean.class, (Serializable)((Object)outTradeNo));
    }

    @Override
    public void save(TradeDfbean dfBean) {
        this.getHibernateTemplate().save((Object)dfBean);
    }

    @Override
    public void update(TradeDfbean dfBean) {
        this.getHibernateTemplate().update((Object)dfBean);
    }

    @Resource(name="sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
}
