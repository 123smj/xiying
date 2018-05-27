/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.hibernate.SessionFactory
 *  org.springframework.orm.hibernate4.HibernateTemplate
 *  org.springframework.orm.hibernate4.support.HibernateDaoSupport
 *  org.springframework.stereotype.Component
 */
package com.manage.dao.impl;

import com.manage.bean.OprInfo;
import com.manage.dao.OprInfoDao;
import java.io.Serializable;
import javax.annotation.Resource;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class OprInfoDaoImpl
extends HibernateDaoSupport
implements OprInfoDao {
    @Override
    public void delete(OprInfo oprInfo) {
        this.getHibernateTemplate().delete((Object)oprInfo);
    }

    @Override
    public OprInfo get(String oprId) {
        return (OprInfo)this.getHibernateTemplate().get(OprInfo.class, (Serializable)((Object)oprId));
    }

    @Override
    public void save(OprInfo oprInfo) {
        this.getHibernateTemplate().save((Object)oprInfo);
    }

    @Override
    public void update(OprInfo oprInfo) {
        this.getHibernateTemplate().update((Object)oprInfo);
    }

    @Resource(name="sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
}
