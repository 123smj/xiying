/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpSession
 *  org.hibernate.HibernateException
 *  org.hibernate.Query
 *  org.hibernate.SQLQuery
 *  org.hibernate.Session
 *  org.hibernate.SessionFactory
 *  org.hibernate.transform.ResultTransformer
 *  org.hibernate.transform.Transformers
 *  org.hibernate.type.StandardBasicTypes
 *  org.hibernate.type.StringType
 *  org.hibernate.type.Type
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.orm.hibernate4.HibernateCallback
 *  org.springframework.orm.hibernate4.HibernateTemplate
 *  org.springframework.orm.hibernate4.support.HibernateDaoSupport
 *  org.springframework.stereotype.Component
 */
package com.trade.dao.impl;

import com.gy.util.DateUtil;
import com.gy.util.StringUtil;
import com.manage.bean.PageModle;
import com.trade.bean.QuickpayBean;
import com.trade.bean.QuickpayQueryBean;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.dao.QuickpayDao;

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
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class QuickpayDaoImpl
        extends HibernateDaoSupport
        implements QuickpayDao {
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;

    @Override
    public QuickpayBean getByTradesn(final String tradeSn, final String gy_mcht_id) {
        return (QuickpayBean) this.getHibernateTemplate().execute((HibernateCallback) new HibernateCallback<QuickpayBean>() {

            public QuickpayBean doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(" from QuickpayBean as s where tradeSn=? and gy_mcht_id=?");
                query.setString(0, tradeSn);
                query.setString(1, gy_mcht_id);
                List list = query.list();
                if (list != null && list.size() != 0) {
                    return (QuickpayBean) list.get(0);
                }
                return null;
            }
        });
    }

    @Override
    public PageModle<QuickpayQueryBean> listByPage(final Map<String, String> param, final int pageNum, final int perPageNum) {
        final QrcodeMchtInfo mchtInfo = (QrcodeMchtInfo) this.session.getAttribute("mchtInfo");
        return (PageModle) this.getHibernateTemplate().execute((HibernateCallback) new HibernateCallback<PageModle<QuickpayQueryBean>>() {

            public PageModle<QuickpayQueryBean> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" from TRADE_QUICKPAY_INF wx left join trade_qrcode_mcht_info m on wx.gy_mcht_Id=m.mcht_No where 1=1 ");
                where.append("and wx.gy_mcht_Id='" + mchtInfo.getMchtNo() + "'");
                if (!StringUtil.isEmpty((String) param.get("time_start_begin"))) {
                    where.append("and wx.time_start>=:time_start_begin ");
                    queryParam.put("time_start_begin", (String) param.get("time_start_begin"));
                } else {
                    where.append("and wx.time_start>='" + DateUtil.getCurrentFormat("yyyyMMdd") + "000000' ");
                }
                if (!StringUtil.isEmpty((String) param.get("time_start_end"))) {
                    where.append("and substr(wx.time_start,0, 8)<=:time_start_end ");
                    queryParam.put("time_start_end", (String) param.get("time_start_end"));
                }
                if (!StringUtil.isEmpty((String) param.get("tradeSn"))) {
                    where.append("and wx.tradeSn=:tradeSn ");
                    queryParam.put("tradeSn", (String) param.get("tradeSn"));
                }
                if (!StringUtil.isEmpty((String) param.get("transaction_id"))) {
                    where.append("and wx.transaction_id=:transaction_id ");
                    queryParam.put("transaction_id", (String) param.get("transaction_id"));
                }
                if (!StringUtil.isEmpty((String) param.get("out_trade_no"))) {
                    where.append("and wx.out_trade_no=:out_trade_no ");
                    queryParam.put("out_trade_no", (String) param.get("out_trade_no"));
                }
                if (!StringUtil.isEmpty((String) param.get("trade_state"))) {
                    where.append("and wx.trade_state=:trade_state ");
                    queryParam.put("trade_state", (String) param.get("trade_state"));
                }
                if (!StringUtil.isEmpty((String) param.get("card_type"))) {
                    where.append("and wx.card_type=:card_type ");
                    queryParam.put("card_type", (String) param.get("card_type"));
                }
                if (!StringUtil.isEmpty((String) param.get("trade_source"))) {
                    where.append("and wx.trade_source=:trade_source ");
                    queryParam.put("trade_source", (String) param.get("trade_source"));
                }
                where.append("order by wx.time_start desc");
                String sql = "select wx.time_start time_start,wx.gy_mcht_id gymchtId,m.mcht_Name mchtName,wx.mch_id mcht_id,wx.tradeSn tradeSn,wx.out_trade_no out_trade_no,wx.transaction_id transaction_id,TO_CHAR(TO_NUMBER(wx.total_fee)/100, 'fm9999999990.00') total_fee,wx.channel_id channel_id,wx.service service,wx.result_code result_code,wx.message message,wx.trade_state trade_state,wx.time_end time_end,wx.card_Holder_Name cardHolderName, wx.card_No cardNo, decode(wx.card_Type,'00','\u8d37\u8bb0\u5361','01','\u501f\u8bb0\u5361','02','\u51c6\u8d37\u8bb0\u5361', wx.card_Type) cardType, wx.bank_Name bankName, wx.trade_source tradeSource" + where.toString();
                String countSql = "SELECT count(*) countNum, sum(total_fee) totalAmount from (" + sql + ")";
                Query query = session.createSQLQuery(sql).addScalar("time_start", (Type) StandardBasicTypes.STRING).addScalar("gymchtId", (Type) StandardBasicTypes.STRING).addScalar("mchtName", (Type) StandardBasicTypes.STRING).addScalar("mcht_id", (Type) StandardBasicTypes.STRING).addScalar("tradeSn", (Type) StandardBasicTypes.STRING).addScalar("out_trade_no", (Type) StandardBasicTypes.STRING).addScalar("transaction_id", (Type) StandardBasicTypes.STRING).addScalar("total_fee", (Type) StandardBasicTypes.STRING).addScalar("channel_id", (Type) StandardBasicTypes.STRING).addScalar("service", (Type) StandardBasicTypes.STRING).addScalar("result_code", (Type) StandardBasicTypes.STRING).addScalar("message", (Type) StandardBasicTypes.STRING).addScalar("trade_state", (Type) StandardBasicTypes.STRING).addScalar("time_end", (Type) StandardBasicTypes.STRING).addScalar("cardHolderName", (Type) StandardBasicTypes.STRING).addScalar("cardNo", (Type) StandardBasicTypes.STRING).addScalar("cardType", (Type) StandardBasicTypes.STRING).addScalar("bankName", (Type) StandardBasicTypes.STRING).addScalar("tradeSource", (Type) StandardBasicTypes.STRING).setResultTransformer(Transformers.aliasToBean(QuickpayQueryBean.class));
                SQLQuery queryCount = session.createSQLQuery(countSql);
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
                double totalAmount = 0.0;
                if (countResult != null) {
                    total = Integer.valueOf(countResult[0] == null ? "0" : countResult[0].toString());
                    totalAmount = Double.valueOf(countResult[1] == null ? "0.00" : countResult[1].toString());
                }
                PageModle<QuickpayQueryBean> pageModle = new PageModle<QuickpayQueryBean>();
                pageModle.setData(list);
                pageModle.setPerPageNum(perPageNum);
                pageModle.setCurrentPage(pageNum);
                pageModle.setTotalNum(total);
                pageModle.setTotalAmount(totalAmount);
                pageModle.setTotalPage(total / perPageNum + (total % perPageNum == 0 ? 0 : 1));
                return pageModle;
            }
        });
    }

    @Override
    public List<QuickpayQueryBean> listAll(final Map<String, String> param) {
        final QrcodeMchtInfo mchtInfo = (QrcodeMchtInfo) this.session.getAttribute("mchtInfo");
        return (List) this.getHibernateTemplate().execute((HibernateCallback) new HibernateCallback<List<QuickpayQueryBean>>() {

            public List<QuickpayQueryBean> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" from TRADE_QUICKPAY_INF wx left join trade_qrcode_mcht_info m on wx.gy_mcht_Id=m.mcht_No where 1=1 ");
                where.append("and wx.gy_mcht_Id='" + mchtInfo.getMchtNo() + "'");
                if (!StringUtil.isEmpty((String) param.get("time_start_begin"))) {
                    where.append("and wx.time_start>=:time_start_begin ");
                    queryParam.put("time_start_begin", (String) param.get("time_start_begin"));
                } else {
                    where.append("and wx.time_start>='" + DateUtil.getCurrentFormat("yyyyMMdd") + "000000' ");
                }
                if (!StringUtil.isEmpty((String) param.get("time_start_end"))) {
                    where.append("and substr(wx.time_start,0, 8)<=:time_start_end ");
                    queryParam.put("time_start_end", (String) param.get("time_start_end"));
                }
                if (!StringUtil.isEmpty((String) param.get("tradeSn"))) {
                    where.append("and wx.tradeSn=:tradeSn ");
                    queryParam.put("tradeSn", (String) param.get("tradeSn"));
                }
                if (!StringUtil.isEmpty((String) param.get("transaction_id"))) {
                    where.append("and wx.transaction_id=:transaction_id ");
                    queryParam.put("transaction_id", (String) param.get("transaction_id"));
                }
                if (!StringUtil.isEmpty((String) param.get("out_trade_no"))) {
                    where.append("and wx.out_trade_no=:out_trade_no ");
                    queryParam.put("out_trade_no", (String) param.get("out_trade_no"));
                }
                if (!StringUtil.isEmpty((String) param.get("trade_state"))) {
                    where.append("and wx.trade_state=:trade_state ");
                    queryParam.put("trade_state", (String) param.get("trade_state"));
                }
                if (!StringUtil.isEmpty((String) param.get("card_type"))) {
                    where.append("and wx.card_type=:card_type ");
                    queryParam.put("card_type", (String) param.get("card_type"));
                }
                where.append("order by wx.time_start desc");
                String sql = "select wx.time_start time_start,wx.gy_mcht_id gymchtId,m.mcht_Name mchtName,wx.mch_id mcht_id,wx.tradeSn tradeSn,wx.out_trade_no out_trade_no,wx.transaction_id transaction_id,TO_CHAR(TO_NUMBER(wx.total_fee)/100, 'fm9999999990.00') total_fee,wx.channel_id channel_id,wx.service service,wx.result_code result_code,wx.message message,wx.trade_state trade_state,wx.time_end time_end,wx.card_Holder_Name cardHolderName, wx.card_No cardNo, decode(wx.card_Type,'00','\u8d37\u8bb0\u5361','01','\u501f\u8bb0\u5361','02','\u51c6\u8d37\u8bb0\u5361', wx.card_Type) cardType, wx.bank_Name bankName" + where.toString();
                String countSql = "SELECT count(*) from (" + sql + ")";
                Query query = session.createSQLQuery(sql).addScalar("time_start", (Type) StandardBasicTypes.STRING).addScalar("gymchtId", (Type) StandardBasicTypes.STRING).addScalar("mchtName", (Type) StandardBasicTypes.STRING).addScalar("mcht_id", (Type) StandardBasicTypes.STRING).addScalar("tradeSn", (Type) StandardBasicTypes.STRING).addScalar("out_trade_no", (Type) StandardBasicTypes.STRING).addScalar("transaction_id", (Type) StandardBasicTypes.STRING).addScalar("total_fee", (Type) StandardBasicTypes.STRING).addScalar("channel_id", (Type) StandardBasicTypes.STRING).addScalar("service", (Type) StandardBasicTypes.STRING).addScalar("result_code", (Type) StandardBasicTypes.STRING).addScalar("message", (Type) StandardBasicTypes.STRING).addScalar("trade_state", (Type) StandardBasicTypes.STRING).addScalar("time_end", (Type) StandardBasicTypes.STRING).addScalar("cardHolderName", (Type) StandardBasicTypes.STRING).addScalar("cardNo", (Type) StandardBasicTypes.STRING).addScalar("cardType", (Type) StandardBasicTypes.STRING).addScalar("bankName", (Type) StandardBasicTypes.STRING).setResultTransformer(Transformers.aliasToBean(QuickpayQueryBean.class));
                SQLQuery queryCount = session.createSQLQuery(countSql);
                for (Map.Entry entry : queryParam.entrySet()) {
                    if (entry.getValue() == null || "".equals(entry.getValue())) continue;
                    query.setParameter((String) entry.getKey(), entry.getValue());
                    queryCount.setParameter((String) entry.getKey(), entry.getValue());
                }
                List list = query.list();
                return list;
            }
        });
    }

    @Override
    public List<QuickpayBean> listNotifyFailTrade() {
        return (List) this.getHibernateTemplate().execute((HibernateCallback) new HibernateCallback<List<QuickpayBean>>() {

            public List<QuickpayBean> doInHibernate(Session session) throws HibernateException {
                StringBuffer sql = new StringBuffer("select wx from QuickpayBean wx where 1=1 ");
                sql.append("and wx.time_start>='" + DateUtil.getDateBeforeDays(1, "yyyyMMdd") + "000000' ");
                sql.append("and wx.trade_state='SUCCESS' and (wx.gynotify_back is null or wx.gynotify_back !='success') and notify_num <5 and gy_notifyUrl is not null ");
                sql.append("order by wx.notify_num asc, wx.time_end asc");
                Query query = session.createQuery(sql.toString());
                query.setFirstResult(0);
                query.setMaxResults(30);
                List list = query.list();
                return list;
            }
        });
    }

    @Override
    public QuickpayBean getById(String outTradeNo) {
        return (QuickpayBean) this.getHibernateTemplate().get(QuickpayBean.class, (Serializable) ((Object) outTradeNo));
    }

    @Override
    public void save(QuickpayBean quickpayBean) {
        this.getHibernateTemplate().save((Object) quickpayBean);
    }

    @Override
    public void saveOrUpdate(QuickpayBean quickpayBean) {
        this.getHibernateTemplate().saveOrUpdate((Object) quickpayBean);
    }

    @Override
    public void update(QuickpayBean quickpayBean) {
        this.getHibernateTemplate().update((Object) quickpayBean);
    }

    @Resource(name = "sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
}
