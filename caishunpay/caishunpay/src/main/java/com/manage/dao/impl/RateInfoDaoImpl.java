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
 *  org.hibernate.type.StandardBasicTypes
 *  org.hibernate.type.StringType
 *  org.hibernate.type.Type
 *  org.springframework.orm.hibernate4.HibernateCallback
 *  org.springframework.orm.hibernate4.HibernateTemplate
 *  org.springframework.orm.hibernate4.support.HibernateDaoSupport
 *  org.springframework.stereotype.Component
 */
package com.manage.dao.impl;

import com.gy.util.StringUtil;
import com.manage.bean.PageModle;
import com.manage.bean.RateInfo;
import com.manage.dao.RateInfoDao;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class RateInfoDaoImpl
extends HibernateDaoSupport
implements RateInfoDao {
    @Override
    public void delete(RateInfo rateInfo) {
        this.getHibernateTemplate().delete((Object)rateInfo);
    }

    @Override
    public RateInfo get(String ownerNo) {
        return (RateInfo)this.getHibernateTemplate().get(RateInfo.class, (Serializable)((Object)ownerNo));
    }

    @Override
    public void save(RateInfo rateInfo) {
        this.getHibernateTemplate().save((Object)rateInfo);
    }

    @Override
    public void update(RateInfo rateInfo) {
        this.getHibernateTemplate().update((Object)rateInfo);
    }

    @Override
    public PageModle<Map<String, String>> listCompanyInfoByPage(final Map<String, String> param, final int pageNum, final int perPageNum) {
        return (PageModle)this.getHibernateTemplate().execute((HibernateCallback)new HibernateCallback<PageModle<Map<String, String>>>(){

            public PageModle<Map<String, String>> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" from trade_opr_info p left join trade_rate_info r on p.opr_id=r.owner_no where 1=1 ");
                if (!StringUtil.isEmpty((String)param.get("company_id"))) {
                    where.append("and p.company_id=:company_id ");
                    queryParam.put("company_id", (String)param.get("company_id"));
                }
                if (!StringUtil.isEmpty((String)param.get("identify_no"))) {
                    where.append("and p.identify_no=:identify_no ");
                    queryParam.put("identify_no", (String)param.get("identify_no"));
                }
                if (!StringUtil.isEmpty((String)param.get("opr_mobile"))) {
                    where.append("and p.opr_mobile=:opr_mobile ");
                    queryParam.put("opr_mobile", (String)param.get("opr_mobile"));
                }
                where.append(" and p.opr_type!='1' ");
                where.append("order by p.register_dt desc, p.last_upd_ts desc");
                String sql = "select company_id,company_name,connact_name,identify_no,opr_mobile,address,debit_card_fee_value,debit_card_max_fee,credit_card_fee_value,wechat_fee_value,alipay_fee_value,qq_fee_value,quickpay_fee_value,netpay_fee_value " + where.toString();
                String countSql = "SELECT count(*) countNum from (" + sql + ")";
                Query query = session.createSQLQuery(sql).addScalar("company_id", (Type)StandardBasicTypes.STRING).addScalar("company_name", (Type)StandardBasicTypes.STRING).addScalar("connact_name", (Type)StandardBasicTypes.STRING).addScalar("identify_no", (Type)StandardBasicTypes.STRING).addScalar("opr_mobile", (Type)StandardBasicTypes.STRING).addScalar("address", (Type)StandardBasicTypes.STRING).addScalar("debit_card_fee_value", (Type)StandardBasicTypes.STRING).addScalar("debit_card_max_fee", (Type)StandardBasicTypes.STRING).addScalar("credit_card_fee_value", (Type)StandardBasicTypes.STRING).addScalar("wechat_fee_value", (Type)StandardBasicTypes.STRING).addScalar("alipay_fee_value", (Type)StandardBasicTypes.STRING).addScalar("qq_fee_value", (Type)StandardBasicTypes.STRING).addScalar("quickpay_fee_value", (Type)StandardBasicTypes.STRING).addScalar("netpay_fee_value", (Type)StandardBasicTypes.STRING).setResultTransformer((ResultTransformer)Transformers.ALIAS_TO_ENTITY_MAP);
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
                int total = Integer.valueOf(queryCount.uniqueResult().toString());
                PageModle<Map<String, String>> pageModle = new PageModle<Map<String, String>>();
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
