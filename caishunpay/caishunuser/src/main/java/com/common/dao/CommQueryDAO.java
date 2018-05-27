/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.hibernate.HibernateException
 *  org.hibernate.Query
 *  org.hibernate.SQLQuery
 *  org.hibernate.Session
 *  org.hibernate.SessionFactory
 *  org.hibernate.Transaction
 *  org.springframework.orm.hibernate4.HibernateCallback
 *  org.springframework.orm.hibernate4.HibernateTemplate
 *  org.springframework.orm.hibernate4.support.HibernateDaoSupport
 *  org.springframework.stereotype.Component
 */
package com.common.dao;

import org.hibernate.*;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class CommQueryDAO extends HibernateDaoSupport implements ICommQueryDAO {
    @Override
    public void excute(final String sql) {
        this.getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                session.getTransaction().begin();
                SQLQuery query = session.createSQLQuery(sql);
                int executeColum = query.executeUpdate();
                session.getTransaction().commit();
                return executeColum;
            }
        });
    }

    @Override
    public List find(String query) {
        return this.getHibernateTemplate().find(query, new Object[0]);
    }

    @Override
    public List findByHQLQuery(final String hql, final int begin, final int count) {
        return (List) this.getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                if (begin >= 0) {
                    query.setFirstResult(begin);
                    query.setMaxResults(count);
                }
                return query.list();
            }
        });
    }

    @Override
    public List findByHQLQuery(final String hql) {
        List data = (List) this.getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                return query.list();
            }
        });
        return data;
    }

    @Override
    public List findByNamedQuery(String name) {
        return this.getHibernateTemplate().findByNamedQuery(name, new Object[0]);
    }

    @Override
    public List findByNamedQuery(final String name, final int begin, final int count) {
        return (List) this.getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.getNamedQuery(name);
                if (begin >= 0) {
                    query.setFirstResult(begin);
                    query.setMaxResults(count);
                }
                return query.list();
            }
        });
    }

    @Override
    public List findByNamedQuery(String name, Map params) {
        return this.findByNamedQuery(name, params, -1, -1);
    }

    @Override
    public List findByNamedQuery(final String name, final Map params, final int begin, final int count) {
        return (List) this.getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.getNamedQuery(name);
                if (params != null) {
                    for (Object entry : params.entrySet()) {
                        Map.Entry entry1 = (Map.Entry) entry;
                        query.setParameter((String) entry1.getKey(), entry1.getValue());
                    }
                }
                if (begin >= 0) {
                    query.setFirstResult(begin);
                    query.setMaxResults(count);
                }
                return query.list();
            }
        });
    }

    @Override
    public List findByNamedQuery(String name, Serializable[] params) {
        return this.findByNamedQuery(name, params, -1, -1);
    }

    @Override
    public List findByNamedQuery(final String name, final Serializable[] params, final int begin, final int count) {
        return (List) this.getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.getNamedQuery(name);
                if (params != null) {
                    int i = 0;
                    while (i < params.length) {
                        query.setParameter(i, (Object) params[i]);
                        ++i;
                    }
                }
                if (begin >= 0) {
                    query.setFirstResult(begin);
                    query.setMaxResults(count);
                }
                return query.list();
            }
        });
    }

    @Override
    public List findBySQLQuery(final String sql, final int begin, final int count) {
        return (List) this.getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                SQLQuery query = session.createSQLQuery(sql);
                if (begin >= 0) {
                    query.setFirstResult(begin);
                    query.setMaxResults(count);
                }
                return query.list();
            }
        });
    }

    @Override
    public List findBySQLQuery(final String sql, final Map map) {
        List data = (List) this.getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                SQLQuery query = session.createSQLQuery(sql);
                Iterator iter = map.keySet().iterator();
                while (iter.hasNext()) {
                    String key = iter.next().toString();
                    Object obj = map.get(key);
                    String[] keys = query.getNamedParameters();
                    int i = 0;
                    while (i < keys.length) {
                        if (key != null && key.equals(keys[i])) {
                            if (obj instanceof String) {
                                query.setString(key, obj.toString());
                            }
                            if (obj instanceof Number) {
                                query.setInteger(key, Integer.parseInt(obj.toString()));
                            }
                            if (obj instanceof BigDecimal) {
                                query.setBigDecimal(key, (BigDecimal) obj);
                            }
                            if (obj instanceof List) {
                                query.setParameterList(key, (Collection) ((List) obj));
                            }
                        }
                        ++i;
                    }
                }
                return query.list();
            }
        });
        return data;
    }

    @Override
    public List findBySQLQuery(final String sql, final int begin, final int count, final Map map) {
        return (List) this.getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                SQLQuery query = session.createSQLQuery(sql);
                Iterator iter = map.keySet().iterator();
                while (iter.hasNext()) {
                    String key = iter.next().toString();
                    Object obj = map.get(key);
                    String[] keys = query.getNamedParameters();
                    int i = 0;
                    while (i < keys.length) {
                        if (key != null && key.equals(keys[i])) {
                            if (obj instanceof String) {
                                query.setString(key, obj.toString());
                            }
                            if (obj instanceof Number) {
                                query.setInteger(key, Integer.parseInt(obj.toString()));
                            }
                            if (obj instanceof BigDecimal) {
                                query.setBigDecimal(key, (BigDecimal) obj);
                            }
                            if (obj instanceof List) {
                                query.setParameterList(key, (Collection) ((List) obj));
                            }
                        }
                        ++i;
                    }
                }
                if (begin >= 0) {
                    query.setFirstResult(begin);
                    query.setMaxResults(count);
                }
                return query.list();
            }
        });
    }

    @Override
    public String findCountBySQLQuery(final String countSql) {
        List data = (List) this.getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                SQLQuery query = session.createSQLQuery(countSql);
                return query.list();
            }
        });
        if (data == null || data.size() == 0 || data.get(0) == null) {
            return "";
        }
        return data.get(0).toString();
    }

    @Override
    public String findCountBySQLQuery(final String countSql, final Map map) {
        List data = (List) this.getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                SQLQuery query = session.createSQLQuery(countSql);
                Iterator iter = map.keySet().iterator();
                while (iter.hasNext()) {
                    String key = iter.next().toString();
                    Object obj = map.get(key);
                    String[] keys = query.getNamedParameters();
                    int i = 0;
                    while (i < keys.length) {
                        if (key != null && key.equals(keys[i])) {
                            if (obj instanceof String) {
                                query.setString(key, obj.toString());
                            }
                            if (obj instanceof Number) {
                                query.setInteger(key, Integer.parseInt(obj.toString()));
                            }
                            if (obj instanceof BigDecimal) {
                                query.setBigDecimal(key, (BigDecimal) obj);
                            }
                            if (obj instanceof List) {
                                query.setParameterList(key, (Collection) ((List) obj));
                            }
                        }
                        ++i;
                    }
                }
                return query.list();
            }
        });
        return data.get(0).toString();
    }

    @Override
    public void flush() {
        this.getHibernateTemplate().flush();
    }

    @Override
    public List findBySQLQuery(final String sql) {
        List data = (List) this.getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                SQLQuery query = session.createSQLQuery(sql);
                return query.list();
            }
        });
        return data;
    }

    @Resource(name = "sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
}
