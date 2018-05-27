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
package com.trade.dao.impl;

import com.trade.bean.own.JumpListBean;
import com.trade.dao.JumpListDao;

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
public class JumpListDaoImpl
        extends HibernateDaoSupport
        implements JumpListDao {
    @Override
    public void delet(JumpListBean jumpListBean) {
        this.getHibernateTemplate().delete((Object) jumpListBean);
    }

    @Override
    public List<JumpListBean> getJumpList(final String jumpGroup, final String tradeSource) {
        return (List) this.getHibernateTemplate().execute((HibernateCallback) new HibernateCallback<List<JumpListBean>>() {

            public List<JumpListBean> doInHibernate(Session session) throws HibernateException {
                StringBuffer where = new StringBuffer(" JumpListBean t where 1=1 and jump_group=:jump_group and trade_source=:trade_source ");
                String sql = "select t from " + where.toString();
                Query query = session.createQuery(sql);
                query.setParameter("jump_group", (Object) jumpGroup);
                query.setParameter("trade_source", (Object) tradeSource);
                return query.list();
            }
        });
    }

    @Override
    public void save(JumpListBean jumpListBean) {
        this.getHibernateTemplate().save((Object) jumpListBean);
    }

    @Override
    public void update(JumpListBean jumpListBean) {
        this.getHibernateTemplate().update((Object) jumpListBean);
    }

    @Resource(name = "sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
}
