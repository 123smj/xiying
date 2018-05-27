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

import com.account.bean.TradeMchtAccountDetail;
import com.account.dao.TradeMchtAccountDetailDao;
import com.gy.util.DateUtil;
import com.gy.util.StringUtil;
import com.manage.bean.PageModle;
import com.trade.bean.own.QrcodeMchtInfo;

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
public class TradeMchtAccountDetailDaoImpl
        extends HibernateDaoSupport
        implements TradeMchtAccountDetailDao {
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;

    @Override
    public TradeMchtAccountDetail get(String accountOrderNo) {
        return (TradeMchtAccountDetail) this.getHibernateTemplate().get(TradeMchtAccountDetail.class, (Serializable) ((Object) accountOrderNo));
    }

    @Override
    public void save(TradeMchtAccountDetail mchtAccountDetail) {
        this.getHibernateTemplate().save((Object) mchtAccountDetail);
    }

    @Override
    public void update(TradeMchtAccountDetail mchtAccountDetail) {
        this.getHibernateTemplate().update((Object) mchtAccountDetail);
    }

    @Override
    public TradeMchtAccountDetail getByDfsn(final String mchtNo, final String dfSn) {
        return (TradeMchtAccountDetail) this.getHibernateTemplate().execute((HibernateCallback) new HibernateCallback<TradeMchtAccountDetail>() {

            public TradeMchtAccountDetail doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(" from TradeMchtAccountDetail as s where dfSn=?0 and mchtNo=?1");
                query.setString("0", dfSn);
                query.setString("1", mchtNo);
                List list = query.list();
                if (list != null && list.size() != 0) {
                    return (TradeMchtAccountDetail) list.get(0);
                }
                return null;
            }
        });
    }

    @Override
    public List<TradeMchtAccountDetail> listUnSuccessDf() {
        return (List) this.getHibernateTemplate().execute((HibernateCallback) new HibernateCallback<List<TradeMchtAccountDetail>>() {

            public List<TradeMchtAccountDetail> doInHibernate(Session session) throws HibernateException {
                StringBuffer sql = new StringBuffer("select wx from TradeMchtAccountDetail wx where 1=1 ");
                sql.append("and wx.receiptTime>='" + DateUtil.getDateBeforeDays(1, "yyyyMMdd") + "000000' ");
                sql.append("and wx.accType='2' and wx.status='01' ");
                sql.append("order by wx.receiptTime desc");
                Query query = session.createQuery(sql.toString());
                query.setFirstResult(0);
                query.setMaxResults(30);
                List list = query.list();
                return list;
            }
        });
    }

    @Override
    public PageModle<TradeMchtAccountDetail> listMchtAccountDetailByPage(final Map<String, String> param, final String accType, final int pageNum, final int perPageNum) {
        final QrcodeMchtInfo mchtInfo = (QrcodeMchtInfo) this.session.getAttribute("mchtInfo");
        return (PageModle) this.getHibernateTemplate().execute((HibernateCallback) new HibernateCallback<PageModle<TradeMchtAccountDetail>>() {

            public PageModle<TradeMchtAccountDetail> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" TradeMchtAccountDetail t where 1=1 and accType='" + accType + "' ");
                if (!StringUtil.isEmpty((String) param.get("time_start_begin"))) {
                    where.append("and t.receiptTime>=:time_start_begin ");
                    queryParam.put("time_start_begin", (String) param.get("time_start_begin"));
                } else {
                    where.append("and t.receiptTime>='" + DateUtil.getCurrentFormat("yyyyMMdd") + "000000' ");
                }
                if (!StringUtil.isEmpty((String) param.get("time_start_end"))) {
                    where.append("and substr(t.receiptTime,0, 8)<=:time_start_end ");
                    queryParam.put("time_start_end", (String) param.get("time_start_end"));
                }
                if (!StringUtil.isEmpty((String) param.get("dfSn"))) {
                    where.append("and t.dfSn=:dfSn ");
                    queryParam.put("dfSn", (String) param.get("dfSn"));
                }
                if (!StringUtil.isEmpty((String) param.get("dfTransactionId"))) {
                    where.append("and t.accountOrderNo=:dfTransactionId ");
                    queryParam.put("dfTransactionId", (String) param.get("dfTransactionId"));
                }
                if (!StringUtil.isEmpty((String) param.get("status"))) {
                    where.append("and t.status=:status ");
                    queryParam.put("status", (String) param.get("status"));
                }
                where.append(" and t.mchtNo ='" + mchtInfo.getMchtNo() + "'");
                where.append("order by t.receiptTime desc");
                String sql = "select t from " + where.toString();
                String countSql = "SELECT count(*) , sum(receiptAmount), sum(single_extra_fee),sum(mchtFeeValue), sum(mchtIncome)  from " + where;
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
                Object[] countResult = (Object[]) queryCount.uniqueResult();
                int total = 0;
                Double totalAmount = 0.0;
                Double totalSingleFee = 0.0;
                Double totalFee = 0.0;
                Double accountNum = 0.0;
                if (countResult != null) {
                    total = Integer.valueOf(countResult[0] == null ? "0" : countResult[0].toString());
                    totalAmount = Double.valueOf(countResult[1] == null ? "0.00" : StringUtil.changeF2Y(Integer.valueOf(countResult[1].toString())));
                    totalSingleFee = Double.valueOf(countResult[2] == null ? "0.00" : StringUtil.changeF2Y(Integer.valueOf(countResult[2].toString())));
                    totalFee = Double.valueOf(countResult[3] == null ? "0.00" : StringUtil.changeF2Y(Integer.valueOf(countResult[3].toString())));
                    accountNum = Double.valueOf(countResult[4] == null ? "0.00" : StringUtil.changeF2Y(Integer.valueOf(countResult[4].toString())));
                }
                PageModle<TradeMchtAccountDetail> pageModle = new PageModle<TradeMchtAccountDetail>();
                pageModle.setData(list);
                pageModle.setPerPageNum(perPageNum);
                pageModle.setCurrentPage(pageNum);
                pageModle.setTotalNum(total);
                pageModle.setTotalAmount(totalAmount);
                pageModle.setTotalSingleFee(totalSingleFee);
                pageModle.setTotalFee(totalFee);
                pageModle.setAccountNum(accountNum);
                pageModle.setTotalPage(total / perPageNum + (total % perPageNum == 0 ? 0 : 1));
                return pageModle;
            }
        });
    }

    @Override
    public List<TradeMchtAccountDetail> listAllAccountDetail(final Map<String, String> param) {
        final QrcodeMchtInfo mchtInfo = (QrcodeMchtInfo) this.session.getAttribute("mchtInfo");
        return (List) this.getHibernateTemplate().execute((HibernateCallback) new HibernateCallback<List<TradeMchtAccountDetail>>() {

            public List<TradeMchtAccountDetail> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" TradeMchtAccountDetail t where 1=1 and accType='2' ");
                if (!StringUtil.isEmpty((String) param.get("time_start_begin"))) {
                    where.append("and t.receiptTime>=:time_start_begin ");
                    queryParam.put("time_start_begin", (String) param.get("time_start_begin"));
                } else {
                    where.append("and t.receiptTime>='" + DateUtil.getCurrentFormat("yyyyMMdd") + "000000' ");
                }
                if (!StringUtil.isEmpty((String) param.get("time_start_end"))) {
                    where.append("and substr(t.receiptTime,0, 8)<=:time_start_end ");
                    queryParam.put("time_start_end", (String) param.get("time_start_end"));
                }
                if (!StringUtil.isEmpty((String) param.get("mchtNo"))) {
                    where.append("and t.mchtNo=:mchtNo ");
                    queryParam.put("mchtNo", (String) param.get("mchtNo"));
                }
                if (!StringUtil.isEmpty((String) param.get("dfSn"))) {
                    where.append("and t.dfSn=:dfSn ");
                    queryParam.put("dfSn", (String) param.get("dfSn"));
                }
                if (!StringUtil.isEmpty((String) param.get("dfTransactionId"))) {
                    where.append("and t.accountOrderNo=:dfTransactionId ");
                    queryParam.put("dfTransactionId", (String) param.get("dfTransactionId"));
                }
                if (!StringUtil.isEmpty((String) param.get("status"))) {
                    where.append("and t.status=:status ");
                    queryParam.put("status", (String) param.get("status"));
                }
                where.append(" and t.mchtNo ='" + mchtInfo.getMchtNo() + "'");
                where.append("order by t.receiptTime desc");
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

    @Resource(name = "sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
}
