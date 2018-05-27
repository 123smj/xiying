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
package com.manage.dao.impl;

import com.manage.bean.TradeFuncInf;
import com.manage.dao.TradeFuncInfDao;
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
public class TradeFuncInfDaoImpl
extends HibernateDaoSupport
implements TradeFuncInfDao {
    @Override
    public void delete(TradeFuncInf tradeFuncInf) {
        this.getHibernateTemplate().delete((Object)tradeFuncInf);
    }

    @Override
    public TradeFuncInf get(Integer funcId) {
        return (TradeFuncInf)this.getHibernateTemplate().get(TradeFuncInf.class, (Serializable)funcId);
    }

    @Override
    public void save(TradeFuncInf tradeFuncInf) {
        this.getHibernateTemplate().save((Object)tradeFuncInf);
    }

    @Override
    public void update(TradeFuncInf tradeFuncInf) {
        this.getHibernateTemplate().update((Object)tradeFuncInf);
    }

    @Override
    public List<TradeFuncInf> getFuncByOprDegree(final Integer oprDegree) {
        return (List)this.getHibernateTemplate().execute((HibernateCallback)new HibernateCallback<List<TradeFuncInf>>(){

            public List<TradeFuncInf> doInHibernate(Session session) throws HibernateException {
                String sql = "select f from com.manage.bean.TradeFuncInf as f, com.manage.bean.TradeRoleFuncMap as m where 1=1 and f.funcId=m.id.valueId and m.id.keyId=:keyId ";
                Query query = session.createQuery(sql);
                query.setParameter("keyId", (Object)oprDegree);
                return query.list();
            }
        });
    }

    @Resource(name="sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

}
