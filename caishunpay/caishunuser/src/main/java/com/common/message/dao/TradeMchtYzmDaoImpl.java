/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.hibernate.SessionFactory
 *  org.springframework.orm.hibernate4.HibernateTemplate
 *  org.springframework.orm.hibernate4.support.HibernateDaoSupport
 *  org.springframework.stereotype.Component
 */
package com.common.message.dao;

import com.common.message.TradeMchtYzm;
import com.common.message.dao.TradeMchtYzmDao;

import java.io.Serializable;
import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class TradeMchtYzmDaoImpl
        extends HibernateDaoSupport
        implements TradeMchtYzmDao {
    @Override
    public TradeMchtYzm getYzm(String id) {
        return (TradeMchtYzm) this.getHibernateTemplate().get(TradeMchtYzm.class, (Serializable) ((Object) id));
    }

    @Override
    public void save(TradeMchtYzm mchtYzm) {
        this.getHibernateTemplate().save((Object) mchtYzm);
    }

    @Override
    public void update(TradeMchtYzm mchtYzm) {
        this.getHibernateTemplate().update((Object) mchtYzm);
    }

    @Resource(name = "sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
}
