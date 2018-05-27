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
package com.trade.dao.impl;

import com.gy.util.StringUtil;
import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.own.PayChannelInf;
import com.trade.dao.MerchantInfDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class MerchantInfDaoImpl
extends HibernateDaoSupport
implements MerchantInfDao {
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;

    @Override
    public MerchantInf getMchtInfo(String mchtNo) {
        return (MerchantInf)this.getHibernateTemplate().get(MerchantInf.class, (Serializable)((Object)mchtNo));
    }

    @Override
    public PayChannelInf getChannelInf(String channelId, String channelMchtNo) {
        PayChannelInf param = new PayChannelInf(channelId, channelMchtNo);
        return (PayChannelInf)this.getHibernateTemplate().get(PayChannelInf.class, (Serializable)param);
    }

    @Override
    public PageModle<MerchantInf> listMchtInfoByPage(final Map<String, String> param, final int pageNum, final int perPageNum, final OprInfo oprInfo) {
        return (PageModle)this.getHibernateTemplate().execute((HibernateCallback)new HibernateCallback<PageModle<MerchantInf>>(){

            public PageModle<MerchantInf> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" MerchantInf t, OprInfo c where t.company_id=c.company_id ");
                if (!StringUtil.isEmpty((String)param.get("company_id"))) {
                    where.append("and t.company_id=:company_id ");
                    queryParam.put("company_id", (String)param.get("company_id"));
                }
                if (!StringUtil.isEmpty((String)param.get("mchtNo"))) {
                    where.append("and t.mchtNo=:mchtNo ");
                    queryParam.put("mchtNo", (String)param.get("mchtNo"));
                }
                if (!StringUtil.isEmpty((String)param.get("identity_no"))) {
                    where.append("and t.identity_no=:identity_no ");
                    queryParam.put("identity_no", (String)param.get("identity_no"));
                }
                if (oprInfo == null || !"1".equals(oprInfo.getOpr_type())) {
                    where.append(" and t.company_id='" + oprInfo.getCompany_id() + "' ");
                }
                where.append("order by t.crtTime desc");
                String sql = "select distinct t,c.company_name from" + where.toString();
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
                List<Object[]> result = query.list();
                ArrayList<MerchantInf> list = new ArrayList<MerchantInf>();
                for (Object[] objs : result) {
                    MerchantInf mcht = (MerchantInf)objs[0];
                    String companyName = (String)objs[1];
                    mcht.setCompanyName(companyName);
                    list.add(mcht);
                }
                result = null;
                int total = Integer.valueOf(queryCount.uniqueResult().toString());
                PageModle<MerchantInf> pageModle = new PageModle<MerchantInf>();
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
    public List<MerchantInf> listAllMcht(final Map<String, String> param, final OprInfo oprInfo) {
        return (List)this.getHibernateTemplate().execute((HibernateCallback)new HibernateCallback<List<MerchantInf>>(){

            public List<MerchantInf> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" MerchantInf t, OprInfo c where t.company_id=c.company_id ");
                if (!StringUtil.isEmpty((String)param.get("company_id"))) {
                    where.append("and t.company_id=:company_id ");
                    queryParam.put("company_id", (String)param.get("company_id"));
                }
                if (!StringUtil.isEmpty((String)param.get("mchtNo"))) {
                    where.append("and t.mchtNo=:mchtNo ");
                    queryParam.put("mchtNo", (String)param.get("mchtNo"));
                }
                if (!StringUtil.isEmpty((String)param.get("identity_no"))) {
                    where.append("and t.identity_no=:identity_no ");
                    queryParam.put("identity_no", (String)param.get("identity_no"));
                }
                if (oprInfo == null || !"1".equals(oprInfo.getOpr_type())) {
                    where.append(" and t.company_id='" + oprInfo.getCompany_id() + "' ");
                }
                where.append("order by t.crtTime desc");
                String sql = "select distinct t,c.company_name from " + where.toString();
                Query query = session.createQuery(sql);
                for (Map.Entry entry : queryParam.entrySet()) {
                    if (entry.getValue() == null || "".equals(entry.getValue())) continue;
                    query.setParameter((String)entry.getKey(), entry.getValue());
                }
                List<Object[]> result = query.list();
                ArrayList<MerchantInf> list = new ArrayList<MerchantInf>();
                for (Object[] objs : result) {
                    MerchantInf mcht = (MerchantInf)objs[0];
                    String companyName = (String)objs[1];
                    mcht.setCompanyName(companyName);
                    list.add(mcht);
                }
                result = null;
                return list;
            }
        });
    }

    @Override
    public List<Map<String, String>> listAllMchtMap(Map<String, String> param, OprInfo oprInfo) {
        return (List)this.getHibernateTemplate().execute((HibernateCallback)new HibernateCallback<List<Map<String, String>>>(){

            public List<Map<String, String>> doInHibernate(Session session) throws HibernateException {
                return null;
            }
        });
    }

    @Override
    public PageModle<PayChannelInf> listChannelMchtInfoByPage(final Map<String, String> param, final int pageNum, final int perPageNum) {
        OprInfo oprInfo = (OprInfo)this.session.getAttribute("oprInfo");
        return (PageModle)this.getHibernateTemplate().execute((HibernateCallback)new HibernateCallback<PageModle<PayChannelInf>>(){

            public PageModle<PayChannelInf> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" PayChannelInf t where 1=1  ");
                if (!StringUtil.isEmpty((String)param.get("channel_mcht_no"))) {
                    where.append("and t.channel_mcht_no=:channel_mcht_no ");
                    queryParam.put("channel_mcht_no", (String)param.get("channel_mcht_no"));
                }
                if (!StringUtil.isEmpty((String)param.get("channel_id"))) {
                    where.append("and t.channel_id=:channel_id ");
                    queryParam.put("channel_id", (String)param.get("channel_id"));
                }
                where.append("order by t.crt_time desc");
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
                PageModle<PayChannelInf> pageModle = new PageModle<PayChannelInf>();
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
    public void saveMchtInfo(MerchantInf mchtInfo) {
        this.getHibernateTemplate().save((Object)mchtInfo);
    }

    @Override
    public void updateMchtInfo(MerchantInf mchtInfo) {
        this.getHibernateTemplate().update((Object)mchtInfo);
    }

    @Resource(name="sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public void saveChannelInf(PayChannelInf channelInf) {
        this.getHibernateTemplate().save((Object)channelInf);
    }

    @Override
    public void updateChannelInf(PayChannelInf channelInf) {
        this.getHibernateTemplate().update((Object)channelInf);
    }

}
