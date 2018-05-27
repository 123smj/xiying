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

import com.account.bean.TradeBranchAccount;
import com.account.dao.TradeBranchAccountDao;

import java.io.Serializable;
import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class TradeBranchAccountDaoImpl
        extends HibernateDaoSupport
        implements TradeBranchAccountDao {
    @Override
    public TradeBranchAccount get(String branchNo) {
        return (TradeBranchAccount) this.getHibernateTemplate().get(TradeBranchAccount.class, (Serializable) ((Object) branchNo));
    }

    @Override
    public void save(TradeBranchAccount tradeBranchAccount) {
        this.getHibernateTemplate().save((Object) tradeBranchAccount);
    }

    @Override
    public void update(TradeBranchAccount tradeBranchAccount) {
        this.getHibernateTemplate().update((Object) tradeBranchAccount);
    }

    @Resource(name = "sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
}
