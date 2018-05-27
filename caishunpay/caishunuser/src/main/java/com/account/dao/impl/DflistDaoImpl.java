/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.hibernate.SessionFactory
 *  org.springframework.orm.hibernate4.HibernateTemplate
 *  org.springframework.orm.hibernate4.support.HibernateDaoSupport
 *  org.springframework.stereotype.Component
 */
package com.account.dao.impl;

import com.account.bean.DfListBean;
import com.account.dao.DfListDao;

import java.io.Serializable;
import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class DflistDaoImpl
        extends HibernateDaoSupport
        implements DfListDao {
    @Override
    public void saveDf(DfListBean dfBean) {
        this.getHibernateTemplate().save((Object) dfBean);
    }

    @Override
    public DfListBean getDfBean(Integer id) {
        return (DfListBean) this.getHibernateTemplate().get(DfListBean.class, (Serializable) id);
    }

    @Resource(name = "sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
}
