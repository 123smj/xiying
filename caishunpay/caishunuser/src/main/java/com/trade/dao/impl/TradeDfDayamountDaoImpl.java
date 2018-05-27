/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.hibernate.SessionFactory
 *  org.springframework.orm.hibernate4.HibernateTemplate
 *  org.springframework.orm.hibernate4.support.HibernateDaoSupport
 *  org.springframework.stereotype.Component
 */
package com.trade.dao.impl;

import com.trade.bean.TradeDfDayamount;
import com.trade.dao.TradeDfDayamountDao;

import java.io.Serializable;
import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class TradeDfDayamountDaoImpl
        extends HibernateDaoSupport
        implements TradeDfDayamountDao {
    @Override
    public TradeDfDayamount get(String bankCardNum, String dfDay) {
        TradeDfDayamount param = new TradeDfDayamount();
        param.setBank_card_num(bankCardNum);
        param.setDf_day(dfDay);
        return (TradeDfDayamount) this.getHibernateTemplate().get(TradeDfDayamount.class, (Serializable) param);
    }

    @Override
    public void save(TradeDfDayamount dfDayamount) {
        this.getHibernateTemplate().save((Object) dfDayamount);
    }

    @Override
    public void update(TradeDfDayamount dfDayamount) {
        this.getHibernateTemplate().update((Object) dfDayamount);
    }

    @Resource(name = "sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
}
