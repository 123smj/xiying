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

import com.manage.bean.TradeRoleFuncMap;
import com.manage.bean.TradeRoleFuncMapPK;
import com.manage.dao.TradeRoleFuncMapDao;
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
    public void delete(TradeRoleFuncMap tradeRoleFuncMap) {
        this.getHibernateTemplate().delete((Object)tradeRoleFuncMap);
    }

    @Override
    public TradeRoleFuncMap get(TradeRoleFuncMapPK id) {
        return (TradeRoleFuncMap)this.getHibernateTemplate().get(TradeRoleFuncMap.class, (Serializable)id);
    }

    @Override
    public void save(TradeRoleFuncMap tradeRoleFuncMap) {
        this.getHibernateTemplate().save((Object)tradeRoleFuncMap);
    }

    @Override
    public void update(TradeRoleFuncMap tradeRoleFuncMap) {
        this.getHibernateTemplate().update((Object)tradeRoleFuncMap);
    }

    @Resource(name="sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
}
