/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.hibernate.HibernateException
 *  org.hibernate.Query
 *  org.hibernate.Session
 *  org.springframework.orm.hibernate4.HibernateCallback
 *  org.springframework.orm.hibernate4.HibernateTemplate
 *  org.springframework.stereotype.Component
 */
package com.manage.dao.impl;

import com.manage.bean.TradeMchtFile;
import com.manage.dao.TradeMchtFileDao;
import com.manage.dao.impl.BaseDao;
import java.io.Serializable;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Component;

@Component
public final class TradeMchtFileDaoImpl
extends BaseDao
implements TradeMchtFileDao {
    @Override
    public void delete(TradeMchtFile mchtFile) {
        this.getHibernateTemplate().delete((Object)mchtFile);
    }

    @Override
    public TradeMchtFile get(String fileId) {
        return (TradeMchtFile)this.getHibernateTemplate().get(TradeMchtFile.class, (Serializable)((Object)fileId));
    }

    @Override
    public void save(TradeMchtFile mchtFile) {
        this.getHibernateTemplate().save((Object)mchtFile);
    }

    @Override
    public void update(TradeMchtFile mchtFile) {
        this.getHibernateTemplate().update((Object)mchtFile);
    }

    @Override
    public List<TradeMchtFile> getMchtFiles(final String mchtNo) {
        return (List)this.getHibernateTemplate().execute((HibernateCallback)new HibernateCallback<List<TradeMchtFile>>(){

            public List<TradeMchtFile> doInHibernate(Session session) throws HibernateException {
                String sql = "select f from com.manage.bean.TradeMchtFile as f where 1=1 and f.mchtNo=:mchtNo order by f.fileId asc";
                Query query = session.createQuery(sql);
                query.setParameter("mchtNo", (Object)mchtNo);
                return query.list();
            }
        });
    }

}
