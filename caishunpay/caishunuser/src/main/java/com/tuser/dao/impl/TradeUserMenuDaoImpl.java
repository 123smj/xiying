/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.hibernate.HibernateException
 *  org.hibernate.Query
 *  org.hibernate.Session
 *  org.hibernate.SessionFactory
 *  org.springframework.orm.hibernate4.HibernateCallback
 *  org.springframework.orm.hibernate4.HibernateTemplate
 *  org.springframework.orm.hibernate4.support.HibernateDaoSupport
 *  org.springframework.stereotype.Component
 */
package com.tuser.dao.impl;

import com.tuser.bean.TradeUserMenu;
import com.tuser.dao.TradeUserMenuDao;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class TradeUserMenuDaoImpl
        extends HibernateDaoSupport
        implements TradeUserMenuDao {
    @Override
    public void delete(TradeUserMenu TradeUserMenu2) {
        this.getHibernateTemplate().delete((Object) TradeUserMenu2);
    }

    @Override
    public TradeUserMenu get(Integer funcId) {
        return (TradeUserMenu) this.getHibernateTemplate().get(TradeUserMenu.class, (Serializable) funcId);
    }

    @Override
    public void save(TradeUserMenu TradeUserMenu2) {
        this.getHibernateTemplate().save((Object) TradeUserMenu2);
    }

    @Override
    public void update(TradeUserMenu TradeUserMenu2) {
        this.getHibernateTemplate().update((Object) TradeUserMenu2);
    }

    @Override
    public List<TradeUserMenu> getFuncByOprDegree(final Integer oprDegree) {
        return (List) this.getHibernateTemplate().execute((HibernateCallback) new HibernateCallback<List<TradeUserMenu>>() {

            public List<TradeUserMenu> doInHibernate(Session session) throws HibernateException {
                String sql = "select f from com.tuser.bean.TradeUserMenu as f, com.tuser.bean.TradeMchtRoleFuncMap as m where 1=1 and f.funcId=m.id.valueId and m.id.keyId=:keyId ";
                Query query = session.createQuery(sql);
                query.setParameter("keyId", (Object) oprDegree);
                return query.list();
            }
        });
    }

    @Resource(name = "sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
}
