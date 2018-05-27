/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
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
import com.trade.bean.TradeDetailBean;
import com.trade.bean.WxJspayTradeInfo;
import com.trade.bean.WxpayScanCode;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.dao.WxNativeDao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
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
public class WxNativeDaoImpl
        extends HibernateDaoSupport
        implements WxNativeDao {
    @Autowired
    private HttpSession session;

    @Override
    public WxpayScanCode getByTradesn(final String tradeSn, final String gy_mcht_id) {
        return (WxpayScanCode) this.getHibernateTemplate().execute((HibernateCallback) new HibernateCallback<WxpayScanCode>() {

            public WxpayScanCode doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(" from WxpayScanCode as s where tradeSn=?0 and gy_mcht_id=?1");
                query.setString("0", tradeSn);
                query.setString("1", gy_mcht_id);
                List list = query.list();
                if (list != null && list.size() != 0) {
                    return (WxpayScanCode) list.get(0);
                }
                return null;
            }
        });
    }

    @Override
    public WxpayScanCode getById(String outTradeNo) {
        return (WxpayScanCode) this.getHibernateTemplate().get(WxpayScanCode.class, (Serializable) ((Object) outTradeNo));
    }

    @Override
    public void save(WxpayScanCode wxpay) {
        this.getHibernateTemplate().save((Object) wxpay);
    }

    @Override
    public void update(WxpayScanCode wxpay) {
        this.getHibernateTemplate().update((Object) wxpay);
    }

    @Override
    public void saveOrUpdate(WxpayScanCode wxpay) {
        this.getHibernateTemplate().saveOrUpdate((Object) wxpay);
    }

    @Override
    public List<TradeDetailBean> listAll(final Map<String, String> param, OprInfo oprInfo) {
        final QrcodeMchtInfo mchtInfo = (QrcodeMchtInfo) this.session.getAttribute("mchtInfo");
        return (List) this.getHibernateTemplate().execute((HibernateCallback) new HibernateCallback<List<TradeDetailBean>>() {

            public List<TradeDetailBean> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" from TRADE_WXPAY_INF wx left join TRADE_Df_info df on wx.out_trade_no=df.out_trade_no left join trade_qrcode_mcht_info m on wx.gy_mcht_Id=m.mcht_No where 1=1 ");
                where.append("and wx.gy_mcht_Id='" + mchtInfo.getMchtNo() + "'");
                if (!StringUtil.isEmpty((String) param.get("time_start_begin"))) {
                    where.append("and wx.time_start>=:time_start_begin ");
                    queryParam.put("time_start_begin", (String) param.get("time_start_begin"));
                } else {
                    where.append("and wx.time_start>='" + DateUtil.getCurrentFormat("yyyyMMdd") + "000000' ");
                }
                if (!StringUtil.isEmpty((String) param.get("time_start_end"))) {
                    where.append("and wx.time_start<=:time_start_end ");
                    queryParam.put("time_start_end", StringUtil.fillValue((String) param.get("time_start_end"), 14, '9'));
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
                if (!StringUtil.isEmpty((String) param.get("trade_source"))) {
                    where.append("and wx.trade_source=:trade_source ");
                    queryParam.put("trade_source", (String) param.get("trade_source"));
                }
                if (!StringUtil.isEmpty((String) param.get("channel_id"))) {
                    where.append("and wx.channel_id=:channel_id ");
                    queryParam.put("channel_id", (String) param.get("channel_id"));
                }
                if (!StringUtil.isEmpty((String) param.get("mch_id"))) {
                    where.append("and wx.mch_id=:mch_id ");
                    queryParam.put("mch_id", (String) param.get("mch_id"));
                }
                where.append(" order by wx.time_start desc");
                String sql = "select wx.time_start time_start,wx.gy_mcht_id gymchtId,m.mcht_Name mchtName,wx.mch_id mcht_id,wx.tradeSn tradeSn,wx.out_trade_no out_trade_no,wx.transaction_id transaction_id,TO_CHAR(TO_NUMBER(wx.total_fee)/100, 'fm9999999990.00') total_fee,wx.channel_id channel_id,wx.service service,wx.result_code result_code,wx.message message,wx.trade_state trade_state,wx.time_end time_end,wx.t0Flag t0Flag,wx.trade_source trade_source,df.t0RespCode t0RespCode,df.t0RespDesc t0RespDesc,df.bankname bankname,df.bankcardnum bankcardnum,df.realname realname,df.free free " + where.toString();
                String countSql = "SELECT count(*) from (" + sql + ")";
                Query query = session.createSQLQuery(sql).addScalar("time_start", (Type) StandardBasicTypes.STRING).addScalar("gymchtId", (Type) StandardBasicTypes.STRING).addScalar("mchtName", (Type) StandardBasicTypes.STRING).addScalar("mcht_id", (Type) StandardBasicTypes.STRING).addScalar("tradeSn", (Type) StandardBasicTypes.STRING).addScalar("out_trade_no", (Type) StandardBasicTypes.STRING).addScalar("transaction_id", (Type) StandardBasicTypes.STRING).addScalar("total_fee", (Type) StandardBasicTypes.STRING).addScalar("channel_id", (Type) StandardBasicTypes.STRING).addScalar("service", (Type) StandardBasicTypes.STRING).addScalar("result_code", (Type) StandardBasicTypes.STRING).addScalar("message", (Type) StandardBasicTypes.STRING).addScalar("trade_state", (Type) StandardBasicTypes.STRING).addScalar("time_end", (Type) StandardBasicTypes.STRING).addScalar("t0Flag", (Type) StandardBasicTypes.STRING).addScalar("trade_source", (Type) StandardBasicTypes.STRING).addScalar("t0RespCode", (Type) StandardBasicTypes.STRING).addScalar("t0RespDesc", (Type) StandardBasicTypes.STRING).addScalar("bankname", (Type) StandardBasicTypes.STRING).addScalar("bankcardnum", (Type) StandardBasicTypes.STRING).addScalar("realname", (Type) StandardBasicTypes.STRING).addScalar("free", (Type) StandardBasicTypes.STRING).setResultTransformer(Transformers.aliasToBean(TradeDetailBean.class));
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
    public PageModle<TradeDetailBean> listByPage(final Map<String, String> param, final int pageNum, final int perPageNum, OprInfo oprInfo) {
        final QrcodeMchtInfo mchtInfo = (QrcodeMchtInfo) this.session.getAttribute("mchtInfo");
        return (PageModle) this.getHibernateTemplate().execute((HibernateCallback) new HibernateCallback<PageModle<TradeDetailBean>>() {

            public PageModle<TradeDetailBean> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" from TRADE_WXPAY_INF wx left join TRADE_Df_info df on wx.out_trade_no=df.out_trade_no left join trade_qrcode_mcht_info m on wx.gy_mcht_Id=m.mcht_No where 1=1 ");
                where.append("and wx.gy_mcht_Id='" + mchtInfo.getMchtNo() + "'");
                if (!StringUtil.isEmpty((String) param.get("time_start_begin"))) {
                    where.append("and wx.time_start>=:time_start_begin ");
                    queryParam.put("time_start_begin", (String) param.get("time_start_begin"));
                } else {
                    where.append("and wx.time_start>='" + DateUtil.getCurrentFormat("yyyyMMdd") + "000000' ");
                }
                if (!StringUtil.isEmpty((String) param.get("time_start_end"))) {
                    where.append("and wx.time_start<=:time_start_end ");
                    queryParam.put("time_start_end", StringUtil.fillValue((String) param.get("time_start_end"), 14, '9'));
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
                if (!StringUtil.isEmpty((String) param.get("trade_source"))) {
                    where.append("and wx.trade_source=:trade_source ");
                    queryParam.put("trade_source", (String) param.get("trade_source"));
                }
                if (!StringUtil.isEmpty((String) param.get("channel_id"))) {
                    where.append("and wx.channel_id=:channel_id ");
                    queryParam.put("channel_id", (String) param.get("channel_id"));
                }
                if (!StringUtil.isEmpty((String) param.get("mch_id"))) {
                    where.append("and wx.mch_id=:mch_id ");
                    queryParam.put("mch_id", (String) param.get("mch_id"));
                }
                where.append("order by wx.time_start desc");
                String sql = "select wx.time_start time_start,wx.gy_mcht_id gymchtId,m.mcht_Name mchtName,wx.mch_id mcht_id,wx.tradeSn tradeSn,wx.out_trade_no out_trade_no,wx.transaction_id transaction_id,TO_CHAR(TO_NUMBER(wx.total_fee)/100, 'fm9999999990.00') total_fee,wx.channel_id channel_id,wx.service service,wx.result_code result_code,wx.message message,wx.trade_state trade_state,wx.time_end time_end,wx.t0Flag t0Flag,wx.trade_source trade_source,df.t0RespCode t0RespCode,df.t0RespDesc t0RespDesc,df.bankname bankname,df.bankcardnum bankcardnum,df.realname realname,df.free free " + where.toString();
                String countSql = "SELECT count(*) countNum, sum(total_fee) totalAmount from (" + sql + ")";
                Query query = session.createSQLQuery(sql).addScalar("time_start", (Type) StandardBasicTypes.STRING).addScalar("gymchtId", (Type) StandardBasicTypes.STRING).addScalar("mchtName", (Type) StandardBasicTypes.STRING).addScalar("mcht_id", (Type) StandardBasicTypes.STRING).addScalar("tradeSn", (Type) StandardBasicTypes.STRING).addScalar("out_trade_no", (Type) StandardBasicTypes.STRING).addScalar("transaction_id", (Type) StandardBasicTypes.STRING).addScalar("total_fee", (Type) StandardBasicTypes.STRING).addScalar("channel_id", (Type) StandardBasicTypes.STRING).addScalar("service", (Type) StandardBasicTypes.STRING).addScalar("result_code", (Type) StandardBasicTypes.STRING).addScalar("message", (Type) StandardBasicTypes.STRING).addScalar("trade_state", (Type) StandardBasicTypes.STRING).addScalar("time_end", (Type) StandardBasicTypes.STRING).addScalar("t0Flag", (Type) StandardBasicTypes.STRING).addScalar("trade_source", (Type) StandardBasicTypes.STRING).addScalar("t0RespCode", (Type) StandardBasicTypes.STRING).addScalar("t0RespDesc", (Type) StandardBasicTypes.STRING).addScalar("bankname", (Type) StandardBasicTypes.STRING).addScalar("bankcardnum", (Type) StandardBasicTypes.STRING).addScalar("realname", (Type) StandardBasicTypes.STRING).addScalar("free", (Type) StandardBasicTypes.STRING).setResultTransformer(Transformers.aliasToBean(TradeDetailBean.class));
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
                PageModle<TradeDetailBean> pageModle = new PageModle<TradeDetailBean>();
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
    public void saveJspay(WxJspayTradeInfo wxJspay) {
        this.getHibernateTemplate().save((Object) wxJspay);
    }

    @Override
    public List<WxpayScanCode> listNotifyFailTrade() {
        return null;
    }

    @Override
    public int acount(final Map<String, String> param, OprInfo oprInfo) {
        final QrcodeMchtInfo mchtInfo = (QrcodeMchtInfo) this.session.getAttribute("mchtInfo");
        return (Integer) this.getHibernateTemplate().execute((HibernateCallback) new HibernateCallback<Integer>() {

            public Integer doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" from TRADE_WXPAY_INF wx left join trade_qrcode_mcht_info m on wx.gy_mcht_Id=m.mcht_No  where 1=1 ");
                where.append("and wx.gy_mcht_Id='" + mchtInfo.getMchtNo() + "'");
                if (!StringUtil.isEmpty((String) param.get("time_start_begin"))) {
                    where.append("and wx.time_start>=:time_start_begin ");
                    queryParam.put("time_start_begin", (String) param.get("time_start_begin"));
                } else {
                    where.append("and wx.time_start>='" + DateUtil.getCurrentFormat("yyyyMMdd") + "000000' ");
                }
                if (!StringUtil.isEmpty((String) param.get("time_start_end"))) {
                    where.append("and wx.time_start<=:time_start_end ");
                    queryParam.put("time_start_end", StringUtil.fillValue((String) param.get("time_start_end"), 14, '9'));
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
                if (!StringUtil.isEmpty((String) param.get("trade_source"))) {
                    where.append("and wx.trade_source=:trade_source ");
                    queryParam.put("trade_source", (String) param.get("trade_source"));
                }
                if (!StringUtil.isEmpty((String) param.get("channel_id"))) {
                    where.append("and wx.channel_id=:channel_id ");
                    queryParam.put("channel_id", (String) param.get("channel_id"));
                }
                if (!StringUtil.isEmpty((String) param.get("mch_id"))) {
                    where.append("and wx.mch_id=:mch_id ");
                    queryParam.put("mch_id", (String) param.get("mch_id"));
                }
                where.append(" order by wx.time_start desc");
                String countSql = "SELECT count(*) " + where.toString();
                SQLQuery queryCount = session.createSQLQuery(countSql);
                for (Map.Entry entry : queryParam.entrySet()) {
                    if (entry.getValue() == null || "".equals(entry.getValue())) continue;
                    queryCount.setParameter((String) entry.getKey(), entry.getValue());
                }
                Object count = queryCount.uniqueResult();
                return Integer.valueOf(count == null ? "0" : count.toString());
            }
        });
    }

    @Resource(name = "sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
}
