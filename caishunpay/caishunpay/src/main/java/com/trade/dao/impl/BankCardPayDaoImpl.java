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
import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.trade.bean.BankCardPay;
import com.trade.bean.QuickpayQueryBean;
import com.trade.dao.BankCardPayDao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class BankCardPayDaoImpl
extends HibernateDaoSupport
implements BankCardPayDao {
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;

    @Override
    public BankCardPay getByTradesn(final String tradeSn, final String gy_mcht_id) {
        return (BankCardPay)this.getHibernateTemplate().execute((HibernateCallback)new HibernateCallback<BankCardPay>(){

            public BankCardPay doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(" from BankCardPay as s where tradeSn=? and merchantId=?");
                query.setString(0, tradeSn);
                query.setString(1, gy_mcht_id);
                List list = query.list();
                if (list != null && list.size() != 0) {
                    return (BankCardPay)list.get(0);
                }
                return null;
            }
        });
    }

    @Override
    public PageModle<QuickpayQueryBean> listByPage(final Map<String, String> param, final int pageNum, final int perPageNum) {
        final OprInfo oprInfo = (OprInfo)this.session.getAttribute("oprInfo");
        return (PageModle)this.getHibernateTemplate().execute((HibernateCallback)new HibernateCallback<PageModle<QuickpayQueryBean>>(){

            public PageModle<QuickpayQueryBean> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" from TRADE_QUICKPAY_INF wx left join trade_qrcode_mcht_info m on wx.gy_mcht_Id=m.mcht_No where 1=1 ");
                if (!StringUtil.isEmpty((String)param.get("gymchtId"))) {
                    where.append("and wx.gy_mcht_Id=:gymchtId ");
                    queryParam.put("gymchtId", (String)param.get("gymchtId"));
                }
                if (!StringUtil.isEmpty((String)param.get("time_start_begin"))) {
                    where.append("and wx.time_start>=:time_start_begin ");
                    queryParam.put("time_start_begin", (String)param.get("time_start_begin"));
                } else {
                    where.append("and wx.time_start>='" + DateUtil.getCurrentFormat("yyyyMMdd") + "000000' ");
                }
                if (!StringUtil.isEmpty((String)param.get("time_start_end"))) {
                    where.append("and substr(wx.time_start,0, 8)<=:time_start_end ");
                    queryParam.put("time_start_end", (String)param.get("time_start_end"));
                }
                if (!StringUtil.isEmpty((String)param.get("tradeSn"))) {
                    where.append("and wx.tradeSn=:tradeSn ");
                    queryParam.put("tradeSn", (String)param.get("tradeSn"));
                }
                if (!StringUtil.isEmpty((String)param.get("transaction_id"))) {
                    where.append("and wx.transaction_id=:transaction_id ");
                    queryParam.put("transaction_id", (String)param.get("transaction_id"));
                }
                if (!StringUtil.isEmpty((String)param.get("out_trade_no"))) {
                    where.append("and wx.out_trade_no=:out_trade_no ");
                    queryParam.put("out_trade_no", (String)param.get("out_trade_no"));
                }
                if (!StringUtil.isEmpty((String)param.get("trade_state"))) {
                    where.append("and wx.trade_state=:trade_state ");
                    queryParam.put("trade_state", (String)param.get("trade_state"));
                }
                if (!StringUtil.isEmpty((String)param.get("card_type"))) {
                    where.append("and wx.card_type=:card_type ");
                    queryParam.put("card_type", (String)param.get("card_type"));
                }
                if (!StringUtil.isEmpty((String)param.get("trade_source"))) {
                    where.append("and wx.trade_source=:trade_source ");
                    queryParam.put("trade_source", (String)param.get("trade_source"));
                }
                if (oprInfo == null || !"1".equals(oprInfo.getOpr_type())) {
                    where.append(" and m.company_id='" + oprInfo.getCompany_id() + "' ");
                }
                where.append("order by wx.time_start desc");
                String sql = "select wx.time_start time_start,wx.gy_mcht_id gymchtId,m.mcht_Name mchtName,wx.mch_id mcht_id,wx.tradeSn tradeSn,wx.out_trade_no out_trade_no,wx.transaction_id transaction_id,TO_CHAR(TO_NUMBER(wx.total_fee)/100, 'fm9999999990.00') total_fee,wx.channel_id channel_id,wx.service service,wx.result_code result_code,wx.message message,wx.trade_state trade_state,wx.time_end time_end,wx.card_Holder_Name cardHolderName, wx.card_No cardNo, wx.cer_Number cerNumber, wx.mobileNum mobileNum, decode(wx.card_Type,'00','\u8d37\u8bb0\u5361','01','\u501f\u8bb0\u5361','02','\u51c6\u8d37\u8bb0\u5361', wx.card_Type) cardType, wx.bank_Name bankName, wx.trade_source tradeSource,TO_CHAR(wx.mcht_rate, 'fm9999999990.000') mcht_rate, TO_CHAR(wx.rate_fee/100, 'fm9999999990.00') rate_fee, TO_CHAR(wx.settle_fee/100, 'fm9999999990.00') settle_fee " + where.toString();
                String countSql = "SELECT count(*) countNum, sum(total_fee) totalAmount from (" + sql + ")";
                Query query = session.createSQLQuery(sql).addScalar("time_start", (Type)StandardBasicTypes.STRING).addScalar("gymchtId", (Type)StandardBasicTypes.STRING).addScalar("mchtName", (Type)StandardBasicTypes.STRING).addScalar("mcht_id", (Type)StandardBasicTypes.STRING).addScalar("tradeSn", (Type)StandardBasicTypes.STRING).addScalar("out_trade_no", (Type)StandardBasicTypes.STRING).addScalar("transaction_id", (Type)StandardBasicTypes.STRING).addScalar("total_fee", (Type)StandardBasicTypes.STRING).addScalar("channel_id", (Type)StandardBasicTypes.STRING).addScalar("service", (Type)StandardBasicTypes.STRING).addScalar("result_code", (Type)StandardBasicTypes.STRING).addScalar("message", (Type)StandardBasicTypes.STRING).addScalar("trade_state", (Type)StandardBasicTypes.STRING).addScalar("time_end", (Type)StandardBasicTypes.STRING).addScalar("cardHolderName", (Type)StandardBasicTypes.STRING).addScalar("cerNumber", (Type)StandardBasicTypes.STRING).addScalar("mobileNum", (Type)StandardBasicTypes.STRING).addScalar("cardNo", (Type)StandardBasicTypes.STRING).addScalar("cardType", (Type)StandardBasicTypes.STRING).addScalar("bankName", (Type)StandardBasicTypes.STRING).addScalar("tradeSource", (Type)StandardBasicTypes.STRING).addScalar("mcht_rate", (Type)StandardBasicTypes.STRING).addScalar("rate_fee", (Type)StandardBasicTypes.STRING).addScalar("settle_fee", (Type)StandardBasicTypes.STRING).setResultTransformer(Transformers.aliasToBean(QuickpayQueryBean.class));
                SQLQuery queryCount = session.createSQLQuery(countSql);
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
                Object[] countResult = (Object[])queryCount.uniqueResult();
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
        final OprInfo oprInfo = (OprInfo)this.session.getAttribute("oprInfo");
        return (List)this.getHibernateTemplate().execute((HibernateCallback)new HibernateCallback<List<QuickpayQueryBean>>(){

            public List<QuickpayQueryBean> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" from TRADE_QUICKPAY_INF wx left join trade_qrcode_mcht_info m on wx.gy_mcht_Id=m.mcht_No where 1=1 ");
                if (!StringUtil.isEmpty((String)param.get("gymchtId"))) {
                    where.append("and wx.gy_mcht_Id=:gymchtId ");
                    queryParam.put("gymchtId", (String)param.get("gymchtId"));
                }
                if (!StringUtil.isEmpty((String)param.get("time_start_begin"))) {
                    where.append("and wx.time_start>=:time_start_begin ");
                    queryParam.put("time_start_begin", (String)param.get("time_start_begin"));
                } else {
                    where.append("and wx.time_start>='" + DateUtil.getCurrentFormat("yyyyMMdd") + "000000' ");
                }
                if (!StringUtil.isEmpty((String)param.get("time_start_end"))) {
                    where.append("and substr(wx.time_start,0, 8)<=:time_start_end ");
                    queryParam.put("time_start_end", (String)param.get("time_start_end"));
                }
                if (!StringUtil.isEmpty((String)param.get("tradeSn"))) {
                    where.append("and wx.tradeSn=:tradeSn ");
                    queryParam.put("tradeSn", (String)param.get("tradeSn"));
                }
                if (!StringUtil.isEmpty((String)param.get("transaction_id"))) {
                    where.append("and wx.transaction_id=:transaction_id ");
                    queryParam.put("transaction_id", (String)param.get("transaction_id"));
                }
                if (!StringUtil.isEmpty((String)param.get("out_trade_no"))) {
                    where.append("and wx.out_trade_no=:out_trade_no ");
                    queryParam.put("out_trade_no", (String)param.get("out_trade_no"));
                }
                if (!StringUtil.isEmpty((String)param.get("trade_state"))) {
                    where.append("and wx.trade_state=:trade_state ");
                    queryParam.put("trade_state", (String)param.get("trade_state"));
                }
                if (!StringUtil.isEmpty((String)param.get("card_type"))) {
                    where.append("and wx.card_type=:card_type ");
                    queryParam.put("card_type", (String)param.get("card_type"));
                }
                if (oprInfo == null || !"1".equals(oprInfo.getOpr_type())) {
                    where.append(" and m.company_id='" + oprInfo.getCompany_id() + "' ");
                }
                where.append("order by wx.time_start desc");
                String sql = "select wx.time_start time_start,wx.gy_mcht_id gymchtId,m.mcht_Name mchtName,wx.mch_id mcht_id,wx.tradeSn tradeSn,wx.out_trade_no out_trade_no,wx.transaction_id transaction_id,TO_CHAR(TO_NUMBER(wx.total_fee)/100, 'fm9999999990.00') total_fee,wx.channel_id channel_id,wx.service service,wx.result_code result_code,wx.message message,wx.trade_state trade_state,wx.time_end time_end,wx.card_Holder_Name cardHolderName, wx.card_No cardNo, decode(wx.card_Type,'00','\u8d37\u8bb0\u5361','01','\u501f\u8bb0\u5361','02','\u51c6\u8d37\u8bb0\u5361', wx.card_Type) cardType, wx.bank_Name bankName, wx.trade_source tradeSource,TO_CHAR(wx.mcht_rate, 'fm9999999990.000') mcht_rate, TO_CHAR(wx.rate_fee/100, 'fm9999999990.00') rate_fee, TO_CHAR(wx.settle_fee/100, 'fm9999999990.00') settle_fee " + where.toString();
                String countSql = "SELECT count(*) from (" + sql + ")";
                Query query = session.createSQLQuery(sql).addScalar("time_start", (Type)StandardBasicTypes.STRING).addScalar("gymchtId", (Type)StandardBasicTypes.STRING).addScalar("mchtName", (Type)StandardBasicTypes.STRING).addScalar("mcht_id", (Type)StandardBasicTypes.STRING).addScalar("tradeSn", (Type)StandardBasicTypes.STRING).addScalar("out_trade_no", (Type)StandardBasicTypes.STRING).addScalar("transaction_id", (Type)StandardBasicTypes.STRING).addScalar("total_fee", (Type)StandardBasicTypes.STRING).addScalar("channel_id", (Type)StandardBasicTypes.STRING).addScalar("service", (Type)StandardBasicTypes.STRING).addScalar("result_code", (Type)StandardBasicTypes.STRING).addScalar("message", (Type)StandardBasicTypes.STRING).addScalar("trade_state", (Type)StandardBasicTypes.STRING).addScalar("time_end", (Type)StandardBasicTypes.STRING).addScalar("cardHolderName", (Type)StandardBasicTypes.STRING).addScalar("cardNo", (Type)StandardBasicTypes.STRING).addScalar("cardType", (Type)StandardBasicTypes.STRING).addScalar("bankName", (Type)StandardBasicTypes.STRING).addScalar("tradeSource", (Type)StandardBasicTypes.STRING).addScalar("mcht_rate", (Type)StandardBasicTypes.STRING).addScalar("rate_fee", (Type)StandardBasicTypes.STRING).addScalar("settle_fee", (Type)StandardBasicTypes.STRING).setResultTransformer(Transformers.aliasToBean(QuickpayQueryBean.class));
                SQLQuery queryCount = session.createSQLQuery(countSql);
                for (Map.Entry entry : queryParam.entrySet()) {
                    if (entry.getValue() == null || "".equals(entry.getValue())) continue;
                    query.setParameter((String)entry.getKey(), entry.getValue());
                    queryCount.setParameter((String)entry.getKey(), entry.getValue());
                }
                List list = query.list();
                return list;
            }
        });
    }

    @Override
    public List<BankCardPay> listNotifyFailTrade() {
        return (List)this.getHibernateTemplate().execute((HibernateCallback)new HibernateCallback<List<BankCardPay>>(){

            public List<BankCardPay> doInHibernate(Session session) throws HibernateException {
                StringBuffer sql = new StringBuffer("select wx from BankCardPay wx where 1=1 ");
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
    public BankCardPay getById(String outTradeNo) {
        return (BankCardPay)this.getHibernateTemplate().get(BankCardPay.class, (Serializable)((Object)outTradeNo));
    }

    @Override
    public void save(BankCardPay BankCardPay) {
        this.getHibernateTemplate().save((Object) BankCardPay);
    }

    @Override
    public void saveOrUpdate(BankCardPay BankCardPay) {
        this.getHibernateTemplate().saveOrUpdate((Object) BankCardPay);
    }

    @Override
    public void update(BankCardPay BankCardPay) {
        this.getHibernateTemplate().update((Object) BankCardPay);
    }

    @Resource(name="sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

}
