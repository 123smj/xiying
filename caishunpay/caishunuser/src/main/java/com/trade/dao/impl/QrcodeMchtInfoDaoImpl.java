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
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.dao.QrcodeMchtInfoDao;

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
public class QrcodeMchtInfoDaoImpl
        extends HibernateDaoSupport
        implements QrcodeMchtInfoDao {
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;

    @Override
    public QrcodeMchtInfo getMchtInfo(String mchtNo) {
        return (QrcodeMchtInfo) this.getHibernateTemplate().get(QrcodeMchtInfo.class, (Serializable) ((Object) mchtNo));
    }

    @Override
    public QrcodeChannelInf getChannelInf(String channelId, String channelMchtNo) {
        QrcodeChannelInf param = new QrcodeChannelInf(channelId, channelMchtNo);
        return (QrcodeChannelInf) this.getHibernateTemplate().get(QrcodeChannelInf.class, (Serializable) param);
    }

    @Override
    public PageModle<QrcodeMchtInfo> listMchtInfoByPage(final Map<String, String> param, final int pageNum, final int perPageNum, final OprInfo oprInfo) {
        return (PageModle) this.getHibernateTemplate().execute((HibernateCallback) new HibernateCallback<PageModle<QrcodeMchtInfo>>() {

            public PageModle<QrcodeMchtInfo> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" QrcodeMchtInfo t where 1=1  ");
                if (!StringUtil.isEmpty((String) param.get("mchtNo"))) {
                    where.append("and t.mchtNo=:mchtNo ");
                    queryParam.put("mchtNo", (String) param.get("mchtNo"));
                }
                if (oprInfo == null || !"1".equals(oprInfo.getOpr_type())) {
                    where.append(" and t.company_id='" + oprInfo.getCompany_id() + "' ");
                }
                where.append("order by t.crtTime desc");
                String sql = "select t from " + where.toString();
                String countSql = "SELECT count(*) from " + where;
                Query query = session.createQuery(sql);
                Query queryCount = session.createQuery(countSql);
                for (Map.Entry entry : queryParam.entrySet()) {
                    if (entry.getValue() == null || "".equals(entry.getValue())) continue;
                    query.setParameter((String) entry.getKey(), entry.getValue());
                    queryCount.setParameter((String) entry.getKey(), entry.getValue());
                }
                if (pageNum > 0 || perPageNum > 0) {
                    query.setFirstResult((pageNum - 1) * perPageNum);
                    query.setMaxResults(perPageNum);
                }
                List list = query.list();
                int total = Integer.valueOf(queryCount.uniqueResult().toString());
                PageModle<QrcodeMchtInfo> pageModle = new PageModle<QrcodeMchtInfo>();
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
    public List<QrcodeMchtInfo> listAllMcht(final Map<String, String> param, final OprInfo oprInfo) {
        return (List) this.getHibernateTemplate().execute((HibernateCallback) new HibernateCallback<List<QrcodeMchtInfo>>() {

            public List<QrcodeMchtInfo> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" QrcodeMchtInfo t where 1=1  ");
                if (!StringUtil.isEmpty((String) param.get("mchtNo"))) {
                    where.append("and t.mchtNo=:mchtNo ");
                    queryParam.put("mchtNo", (String) param.get("mchtNo"));
                }
                if (oprInfo == null || !"1".equals(oprInfo.getOpr_type())) {
                    where.append(" and t.company_id='" + oprInfo.getCompany_id() + "' ");
                }
                where.append("order by t.crtTime desc");
                String sql = "select t from " + where.toString();
                Query query = session.createQuery(sql);
                for (Map.Entry entry : queryParam.entrySet()) {
                    if (entry.getValue() == null || "".equals(entry.getValue())) continue;
                    query.setParameter((String) entry.getKey(), entry.getValue());
                }
                List list = query.list();
                return list;
            }
        });
    }

    @Override
    public PageModle<QrcodeChannelInf> listChannelMchtInfoByPage(final Map<String, String> param, final int pageNum, final int perPageNum) {
        return (PageModle) this.getHibernateTemplate().execute((HibernateCallback) new HibernateCallback<PageModle<QrcodeChannelInf>>() {

            public PageModle<QrcodeChannelInf> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" QrcodeChannelInf t where 1=1  ");
                if (!StringUtil.isEmpty((String) param.get("channel_mcht_no"))) {
                    where.append("and t.channel_mcht_no=:channel_mcht_no ");
                    queryParam.put("channel_mcht_no", (String) param.get("channel_mcht_no"));
                }
                if (!StringUtil.isEmpty((String) param.get("channel_id"))) {
                    where.append("and t.channel_id=:channel_id ");
                    queryParam.put("channel_id", (String) param.get("channel_id"));
                }
                where.append("order by t.crt_time desc");
                String sql = "select t from " + where.toString();
                String countSql = "SELECT count(*) from " + where;
                Query query = session.createQuery(sql);
                Query queryCount = session.createQuery(countSql);
                for (Map.Entry entry : queryParam.entrySet()) {
                    if (entry.getValue() == null || "".equals(entry.getValue())) continue;
                    query.setParameter((String) entry.getKey(), entry.getValue());
                    queryCount.setParameter((String) entry.getKey(), entry.getValue());
                }
                if (pageNum > 0 || perPageNum > 0) {
                    query.setFirstResult((pageNum - 1) * perPageNum);
                    query.setMaxResults(perPageNum);
                }
                List list = query.list();
                int total = Integer.valueOf(queryCount.uniqueResult().toString());
                PageModle<QrcodeChannelInf> pageModle = new PageModle<QrcodeChannelInf>();
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
    public void saveMchtInfo(QrcodeMchtInfo mchtInfo) {
        this.getHibernateTemplate().save((Object) mchtInfo);
    }

    @Override
    public void updateMchtInfo(QrcodeMchtInfo mchtInfo) {
        this.getHibernateTemplate().update((Object) mchtInfo);
    }

    @Resource(name = "sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public void saveChannelInf(QrcodeChannelInf channelInf) {
        this.getHibernateTemplate().save((Object) channelInf);
    }

    @Override
    public void updateChannelInf(QrcodeChannelInf channelInf) {
        this.getHibernateTemplate().update((Object) channelInf);
    }
}
