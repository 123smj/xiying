/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpSession
 *  org.hibernate.HibernateException
 *  org.hibernate.Query
 *  org.hibernate.Session
 *  org.hibernate.SessionFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.orm.hibernate4.HibernateCallback
 *  org.springframework.orm.hibernate4.HibernateTemplate
 *  org.springframework.orm.hibernate4.support.HibernateDaoSupport
 *  org.springframework.stereotype.Component
 */
package com.account.dao.impl;

import com.account.bean.TradeAccountFileInfo;
import com.account.dao.TradeAccountFileInfoDao;
import com.gy.util.DateUtil;
import com.gy.util.StringUtil;
import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class TradeAccountFileInfoDaoImpl
extends HibernateDaoSupport
implements TradeAccountFileInfoDao {
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;

    @Override
    public TradeAccountFileInfo get(Integer offerSeq) {
        return (TradeAccountFileInfo)this.getHibernateTemplate().get(TradeAccountFileInfo.class, (Serializable)offerSeq);
    }

    @Override
    public void save(TradeAccountFileInfo tradeAccountFileInfo) {
        this.getHibernateTemplate().save((Object)tradeAccountFileInfo);
    }

    @Override
    public void update(TradeAccountFileInfo tradeAccountFileInfo) {
        this.getHibernateTemplate().update((Object)tradeAccountFileInfo);
    }

    @Override
    public PageModle<TradeAccountFileInfo> listAccountFileByPage(final Map<String, String> param, final int pageNum, final int perPageNum) {
        OprInfo oprInfo = (OprInfo)this.session.getAttribute("oprInfo");
        return (PageModle)this.getHibernateTemplate().execute((HibernateCallback)new HibernateCallback<PageModle<TradeAccountFileInfo>>(){

            public PageModle<TradeAccountFileInfo> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" TradeAccountFileInfo t where 1=1  ");
                if (!StringUtil.isEmpty((String)param.get("time_start_begin"))) {
                    where.append("and t.oprTime>=:time_start_begin ");
                    queryParam.put("time_start_begin", (String)param.get("time_start_begin"));
                } else {
                    where.append("and t.oprTime>='" + DateUtil.getCurrentFormat("yyyyMMdd") + "000000' ");
                }
                if (!StringUtil.isEmpty((String)param.get("time_start_end"))) {
                    where.append("and substr(t.oprTime,0, 8)<=:time_start_end ");
                    queryParam.put("time_start_end", (String)param.get("time_start_end"));
                }
                if (!StringUtil.isNull(param.get("offerSeq"))) {
                    where.append("and offerSeq=" + (String)param.get("offerSeq"));
                }
                if (!StringUtil.isNull(param.get("status"))) {
                    where.append("and status='" + (String)param.get("status") + "'");
                }
                where.append(" order by t.oprTime desc");
                String sql = "select t from " + where.toString();
                String countSql = "SELECT count(*) from " + where;
                Query query = session.createQuery(sql);
                Query queryCount = session.createQuery(countSql);
                for (Map.Entry entry : queryParam.entrySet()) {
                    if (entry.getValue() == null || "".equals(entry.getValue())) continue;
                    query.setParameter((String)entry.getKey(), entry.getValue());
                    queryCount.setParameter((String)entry.getKey(), entry.getValue());
                }
                if (pageNum > 0 || perPageNum > 0) {
                    query.setFirstResult((pageNum - 1) * perPageNum);
                    query.setMaxResults(perPageNum);
                }
                List list = query.list();
                int total = Integer.valueOf(queryCount.uniqueResult().toString());
                PageModle<TradeAccountFileInfo> pageModle = new PageModle<TradeAccountFileInfo>();
                pageModle.setData(list);
                pageModle.setPerPageNum(perPageNum);
                pageModle.setCurrentPage(pageNum);
                pageModle.setTotalNum(total);
                pageModle.setTotalPage(total / perPageNum + (total % perPageNum == 0 ? 0 : 1));
                return pageModle;
            }
        });
    }

    @Resource(name="sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

}
