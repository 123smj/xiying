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
import com.trade.bean.own.QrcodeMchtInfoTmp;
import com.trade.dao.QrcodeMchtInfoTmpDao;
import com.trade.enums.MchtStatusEnum;

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
public class QrcodeMchtInfoTmpDaoImpl
        extends HibernateDaoSupport
        implements QrcodeMchtInfoTmpDao {
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;

    @Override
    public QrcodeMchtInfoTmp getMchtInfo(String mchtNo) {
        return (QrcodeMchtInfoTmp) this.getHibernateTemplate().get(QrcodeMchtInfoTmp.class, (Serializable) ((Object) mchtNo));
    }

    @Override
    public PageModle<QrcodeMchtInfoTmp> listMchtInfoTmpByPage(final Map<String, String> param, final int pageNum, final int perPageNum, final OprInfo oprInfo) {
        return (PageModle) this.getHibernateTemplate().execute((HibernateCallback) new HibernateCallback<PageModle<QrcodeMchtInfoTmp>>() {

            public PageModle<QrcodeMchtInfoTmp> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" QrcodeMchtInfoTmp t where 1=1  ");
                if (!StringUtil.isEmpty((String) param.get("mchtNo"))) {
                    where.append("and t.mchtNo=:mchtNo ");
                    queryParam.put("mchtNo", (String) param.get("mchtNo"));
                }
                if (!StringUtil.isEmpty((String) param.get("identity_no"))) {
                    where.append("and t.identity_no=:identity_no ");
                    queryParam.put("identity_no", (String) param.get("identity_no"));
                }
                if (!StringUtil.isEmpty((String) param.get("status"))) {
                    where.append("and t.status=:status ");
                    queryParam.put("status", (String) param.get("status"));
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
                PageModle<QrcodeMchtInfoTmp> pageModle = new PageModle<QrcodeMchtInfoTmp>();
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
    public PageModle<QrcodeMchtInfoTmp> listMchtInfoTmp4CheckByPage(final Map<String, String> param, final int pageNum, final int perPageNum, final OprInfo oprInfo) {
        return (PageModle) this.getHibernateTemplate().execute((HibernateCallback) new HibernateCallback<PageModle<QrcodeMchtInfoTmp>>() {

            public PageModle<QrcodeMchtInfoTmp> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" QrcodeMchtInfoTmp t where 1=1  ");
                if (!StringUtil.isEmpty((String) param.get("mchtNo"))) {
                    where.append("and t.mchtNo=:mchtNo ");
                    queryParam.put("mchtNo", (String) param.get("mchtNo"));
                }
                if (!StringUtil.isEmpty((String) param.get("identity_no"))) {
                    where.append("and t.identity_no=:identity_no ");
                    queryParam.put("identity_no", (String) param.get("identity_no"));
                }
                if (!StringUtil.isEmpty((String) param.get("status"))) {
                    where.append("and t.status=:status ");
                    queryParam.put("status", (String) param.get("status"));
                }
                where.append("and t.status in('" + MchtStatusEnum.ADD.getCode() + "', '" + MchtStatusEnum.UPDATE.getCode() + "', '" + MchtStatusEnum.FREEZE_TO_CHECK.getCode() + "')");
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
                PageModle<QrcodeMchtInfoTmp> pageModle = new PageModle<QrcodeMchtInfoTmp>();
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
    public List<QrcodeMchtInfoTmp> listAllMcht(final Map<String, String> param, final OprInfo oprInfo) {
        return (List) this.getHibernateTemplate().execute((HibernateCallback) new HibernateCallback<List<QrcodeMchtInfoTmp>>() {

            public List<QrcodeMchtInfoTmp> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" QrcodeMchtInfoTmp t where 1=1  ");
                if (!StringUtil.isEmpty((String) param.get("mchtNo"))) {
                    where.append("and t.mchtNo=:mchtNo ");
                    queryParam.put("mchtNo", (String) param.get("mchtNo"));
                }
                if (!StringUtil.isEmpty((String) param.get("identity_no"))) {
                    where.append("and t.identity_no=:identity_no ");
                    queryParam.put("identity_no", (String) param.get("identity_no"));
                }
                if (!StringUtil.isEmpty((String) param.get("status"))) {
                    where.append("and t.status=:status ");
                    queryParam.put("status", (String) param.get("status"));
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
    public void saveMchtInfo(QrcodeMchtInfoTmp mchtInfo) {
        this.getHibernateTemplate().save((Object) mchtInfo);
    }

    @Override
    public void updateMchtInfo(QrcodeMchtInfoTmp mchtInfo) {
        this.getHibernateTemplate().update((Object) mchtInfo);
    }

    @Resource(name = "sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
}
