/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.hibernate.HibernateException
 *  org.hibernate.Query
 *  org.hibernate.SQLQuery
 *  org.hibernate.Session
 *  org.hibernate.SessionFactory
 *  org.hibernate.transform.AliasToEntityMapResultTransformer
 *  org.hibernate.transform.ResultTransformer
 *  org.hibernate.transform.Transformers
 *  org.springframework.orm.hibernate4.HibernateCallback
 *  org.springframework.orm.hibernate4.HibernateTemplate
 *  org.springframework.orm.hibernate4.support.HibernateDaoSupport
 *  org.springframework.stereotype.Component
 */
package com.gy2cupe.service;

import com.gy.util.DateUtil;
import com.gy.util.StringUtil;
import com.gy2cupe.util.CupeUtil;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class CupeResultService
        extends HibernateDaoSupport {
    public List getTradeStatus(final String tradeNo) {
        String querySql = "select PAY_STATUS,notify_url from TRADE_CUPE_TRADE_MAP where TRADE_NO=:TRADE_NO";
        return (List) this.getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                SQLQuery queryStatus = session.createSQLQuery("select PAY_STATUS,notify_url from TRADE_CUPE_TRADE_MAP where TRADE_NO=:TRADE_NO");
                queryStatus.setString("TRADE_NO", tradeNo);
                List list = queryStatus.list();
                return list;
            }
        });
    }

    public int saveBackResult(final Map<String, String> param) {
        String updateSql = "update TRADE_CUPE_TRADE_MAP set CHANNEL_TRADE_SN=:CHANNEL_TRADE_SN,PAY_APP=:PAY_APP,PAY_TIME=:PAY_TIME,PAY_AMOUNT=:PAY_AMOUNT,DISCOUNT_AMOUNT=:DISCOUNT_AMOUNT,APP_ORDER_NO=:APP_ORDER_NO,PAY_STATUS=:PAY_STATUS where TRADE_NO=:TRADE_NO";
        return (Integer) this.getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                SQLQuery query = session.createSQLQuery("update TRADE_CUPE_TRADE_MAP set CHANNEL_TRADE_SN=:CHANNEL_TRADE_SN,PAY_APP=:PAY_APP,PAY_TIME=:PAY_TIME,PAY_AMOUNT=:PAY_AMOUNT,DISCOUNT_AMOUNT=:DISCOUNT_AMOUNT,APP_ORDER_NO=:APP_ORDER_NO,PAY_STATUS=:PAY_STATUS where TRADE_NO=:TRADE_NO");
                query.setString("CHANNEL_TRADE_SN", (String) param.get("p0"));
                String payType = "2";
                if (StringUtil.isNotEmpty((String) param.get("p1")) && ((String) param.get("p1")).length() == 1) {
                    payType = (String) param.get("p1");
                }
                query.setString("PAY_APP", payType);
                query.setString("PAY_TIME", (String) param.get("p2"));
                query.setString("PAY_AMOUNT", (String) param.get("p3"));
                query.setString("DISCOUNT_AMOUNT", (String) param.get("p4"));
                query.setString("APP_ORDER_NO", (String) param.get("p5"));
                String payStatus = "1";
                if (!"0".equals(param.get("c"))) {
                    payStatus = "2";
                }
                query.setString("PAY_STATUS", payStatus);
                query.setString("TRADE_NO", (String) param.get("p7"));
                return query.executeUpdate();
            }
        });
    }

    public int saveQueryResult(final Map<String, String> param) {
        String updateSql = "update TRADE_CUPE_TRADE_MAP set CHANNEL_TRADE_SN=:CHANNEL_TRADE_SN,PAY_APP=:PAY_APP,PAY_TIME=:PAY_TIME,PAY_AMOUNT=:PAY_AMOUNT,DISCOUNT_AMOUNT=:DISCOUNT_AMOUNT,APP_ORDER_NO=:APP_ORDER_NO,REFUND_AMOUNT=:REFUND_AMOUNT,PAY_STATUS=:PAY_STATUS where TRADE_SN=:TRADE_SN";
        return (Integer) this.getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                SQLQuery query = session.createSQLQuery("update TRADE_CUPE_TRADE_MAP set CHANNEL_TRADE_SN=:CHANNEL_TRADE_SN,PAY_APP=:PAY_APP,PAY_TIME=:PAY_TIME,PAY_AMOUNT=:PAY_AMOUNT,DISCOUNT_AMOUNT=:DISCOUNT_AMOUNT,APP_ORDER_NO=:APP_ORDER_NO,REFUND_AMOUNT=:REFUND_AMOUNT,PAY_STATUS=:PAY_STATUS where TRADE_SN=:TRADE_SN");
                query.setString("CHANNEL_TRADE_SN", (String) param.get("p0"));
                query.setString("PAY_APP", (String) param.get("p1"));
                query.setString("PAY_TIME", (String) param.get("p2"));
                query.setString("PAY_AMOUNT", (String) param.get("p3"));
                query.setString("DISCOUNT_AMOUNT", (String) param.get("p4"));
                query.setString("APP_ORDER_NO", (String) param.get("p5"));
                query.setString("REFUND_AMOUNT", (String) param.get("p6"));
                query.setString("PAY_STATUS", (String) param.get("s"));
                query.setString("TRADE_SN", (String) param.get("tradeSnOri"));
                return query.executeUpdate();
            }
        });
    }

    public int savePay(final Map<String, String> param) {
        String updateSql = "insert into TRADE_CUPE_TRADE_MAP(PK_TRADE_CUPE_TRADE_MAP, TRADE_SN, EQUIP_CODE, ORDER_AMOUNT, PAY_STATUS, TRADE_NO, RANDOM_STR, CRT_TIME, notify_url) values(SEQ_CUPE_TRADE_MAP.nextval, ?, ?, ?, ?, ?, ?, ?, ?)";
        return (Integer) this.getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                SQLQuery query = session.createSQLQuery("insert into TRADE_CUPE_TRADE_MAP(PK_TRADE_CUPE_TRADE_MAP, TRADE_SN, EQUIP_CODE, ORDER_AMOUNT, PAY_STATUS, TRADE_NO, RANDOM_STR, CRT_TIME, notify_url) values(SEQ_CUPE_TRADE_MAP.nextval, ?, ?, ?, ?, ?, ?, ?, ?)");
                query.setString(0, (String) param.get("tradeSn"));
                query.setString(1, (String) param.get("p1"));
                query.setString(2, (String) param.get("p3"));
                query.setInteger(3, 0);
                query.setString(4, (String) param.get("p2"));
                query.setString(5, (String) param.get("r"));
                query.setString(6, DateUtil.getCurrTime());
                query.setString(7, (String) param.get("notifyUrl"));
                return query.executeUpdate();
            }
        });
    }

    public Map<String, Object> queryPay(final String tradeSnOri) {
        String querySql = "select EQUIP_CODE, TRADE_NO, ORDER_AMOUNT,RANDOM_STR,ORDER_NO,PAY_STATUS,CHANNEL_TRADE_SN,PAY_APP,PAY_TIME,PAY_AMOUNT,DISCOUNT_AMOUNT,APP_ORDER_NO,REFUND_AMOUNT from TRADE_CUPE_TRADE_MAP where TRADE_SN=:TRADE_SN ";
        return (Map) this.getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                Query queryStatus = session.createSQLQuery("select EQUIP_CODE, TRADE_NO, ORDER_AMOUNT,RANDOM_STR,ORDER_NO,PAY_STATUS,CHANNEL_TRADE_SN,PAY_APP,PAY_TIME,PAY_AMOUNT,DISCOUNT_AMOUNT,APP_ORDER_NO,REFUND_AMOUNT from TRADE_CUPE_TRADE_MAP where TRADE_SN=:TRADE_SN ").setResultTransformer((ResultTransformer) Transformers.ALIAS_TO_ENTITY_MAP);
                queryStatus.setString("TRADE_SN", tradeSnOri);
                List list = queryStatus.list();
                Map result = null;
                if (list != null && list.size() != 0) {
                    result = (Map) list.get(0);
                }
                return result;
            }
        });
    }

    public boolean checkSign(Map<String, String> param, String sign) {
        Map<String, String> sortedMap = CupeUtil.sortMapByKey(param);
        String signValue = CupeUtil.md5Sign(sortedMap);
        return signValue.equals(sign);
    }

    @Resource
    public void setMySessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
}
