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
package com.gy.system.dao.impl;

import com.gy.system.bean.IpWhiteList;
import com.gy.system.dao.IpWhiteListDao;
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
public class IpWhiteListDaoImpl
extends HibernateDaoSupport
implements IpWhiteListDao {
    @Override
    public List<IpWhiteList> listIpAll() {
        return (List)this.getHibernateTemplate().execute((HibernateCallback)new HibernateCallback<List<IpWhiteList>>(){

            public List<IpWhiteList> doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery("select new IpWhiteList(ip, flag, addDate) from IpWhiteList where flag='00'");
                List list = query.list();
                return list;
            }
        });
    }

    @Resource
    public void setMySessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

}
