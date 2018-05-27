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

import com.account.bean.TradeAccountFileDetail;
import com.account.dao.TradeAccountFileDetailDao;
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
public class TradeAccountFileDetailDaoImpl
extends HibernateDaoSupport
implements TradeAccountFileDetailDao {
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;

    @Override
    public void save(TradeAccountFileDetail tradeAccountFileInfo) {
        this.getHibernateTemplate().save((Object)tradeAccountFileInfo);
    }

    @Override
    public void update(TradeAccountFileDetail tradeAccountFileInfo) {
        this.getHibernateTemplate().update((Object)tradeAccountFileInfo);
    }

    @Override
    public PageModle<TradeAccountFileDetail> listDetailByPage(final Map<String, String> param, final int pageNum, final int perPageNum) {
        OprInfo oprInfo = (OprInfo)this.session.getAttribute("oprInfo");
        return (PageModle)this.getHibernateTemplate().execute((HibernateCallback)new HibernateCallback<PageModle<TradeAccountFileDetail>>(){

            public PageModle<TradeAccountFileDetail> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" TradeAccountFileDetail t where 1=1  ");
                if (!StringUtil.isEmpty((String)param.get("time_start_begin"))) {
                    where.append("and t.opr_time>=:time_start_begin ");
                    queryParam.put("time_start_begin", (String)param.get("time_start_begin"));
                } else {
                    where.append("and t.opr_time>='" + DateUtil.getCurrentFormat("yyyyMMdd") + "000000' ");
                }
                if (!StringUtil.isEmpty((String)param.get("time_start_end"))) {
                    where.append("and substr(t.opr_time,0, 8)<=:time_start_end ");
                    queryParam.put("time_start_end", (String)param.get("time_start_end"));
                }
                if (!StringUtil.isNull(param.get("offerSeq"))) {
                    where.append("and offerSeq=" + (String)param.get("offerSeq"));
                }
                where.append(" order by t.offerSeq desc,dfsn asc");
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
                PageModle<TradeAccountFileDetail> pageModle = new PageModle<TradeAccountFileDetail>();
                pageModle.setData(list);
                pageModle.setPerPageNum(perPageNum);
                pageModle.setCurrentPage(pageNum);
                pageModle.setTotalNum(total);
                pageModle.setTotalPage(total / perPageNum + (total % perPageNum == 0 ? 0 : 1));
                return pageModle;
            }
        });
    }

    @Override
    public List<TradeAccountFileDetail> getAccoutFileDetailList(final Integer offerSeq) {
        return (List)this.getHibernateTemplate().execute((HibernateCallback)new HibernateCallback<List<TradeAccountFileDetail>>(){

            public List<TradeAccountFileDetail> doInHibernate(Session session) throws HibernateException {
                StringBuffer sql = new StringBuffer("select d from TradeAccountFileDetail d where 1=1 ");
                sql.append("and d.offerSeq=" + offerSeq);
                Query query = session.createQuery(sql.toString());
                List list = query.list();
                return list;
            }
        });
    }

    @Override
    public TradeAccountFileDetail get(String mchtNo, String dfSn) {
        TradeAccountFileDetail param = new TradeAccountFileDetail();
        param.setMcht_no(mchtNo);
        param.setDfsn(dfSn);
        return (TradeAccountFileDetail)this.getHibernateTemplate().get(TradeAccountFileDetail.class, (Serializable)param);
    }

    @Resource(name="sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

}
