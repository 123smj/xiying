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
package com.trade.dao.impl;

import com.gy.util.StringUtil;
import com.manage.bean.PageModle;
import com.trade.bean.own.JumpListBean;
import com.trade.dao.JumpListDao;
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
public class JumpListDaoImpl
extends HibernateDaoSupport
implements JumpListDao {
    @Override
    public void delet(JumpListBean jumpListBean) {
        this.getHibernateTemplate().delete((Object)jumpListBean);
    }

    @Override
    public JumpListBean getJumpList(String jumpGroup, String channelId, String channelMchtNo, String tradeSource) {
        JumpListBean param = new JumpListBean();
        param.setJump_group(jumpGroup);
        param.setChannel_id(channelId);
        param.setChannel_mcht_no(channelMchtNo);
        param.setTrade_source(tradeSource);
        return (JumpListBean)this.getHibernateTemplate().get(JumpListBean.class, (Serializable)param);
    }

    @Override
    public List<JumpListBean> getJumpList(final String jumpGroup, final String tradeSource) {
        return (List)this.getHibernateTemplate().execute((HibernateCallback)new HibernateCallback<List<JumpListBean>>(){

            public List<JumpListBean> doInHibernate(Session session) throws HibernateException {
                StringBuffer where = new StringBuffer(" JumpListBean t where 1=1 and jump_group=:jump_group and trade_source=:trade_source ");
                String sql = " from " + where.toString();
                Query query = session.createQuery(sql);
                query.setParameter("jump_group", (Object)jumpGroup);
                query.setParameter("trade_source", (Object)tradeSource);
                return query.list();
            }
        });
    }

    @Override
    public JumpListBean getFirstJumpList(final String jumpGroup) {
        return (JumpListBean)this.getHibernateTemplate().execute((HibernateCallback)new HibernateCallback<JumpListBean>(){

            public JumpListBean doInHibernate(Session session) throws HibernateException {
                StringBuffer where = new StringBuffer(" JumpListBean t where 1=1 and jump_group=:jump_group ");
                String sql = " from " + where.toString();
                Query query = session.createQuery(sql);
                query.setParameter("jump_group", (Object)jumpGroup);
                List list = query.list();
                JumpListBean jumpList = null;
                if (list != null && list.size() != 0) {
                    jumpList = (JumpListBean)list.get(0);
                }
                return jumpList;
            }
        });
    }

    @Override
    public PageModle<Map<String, String>> listGroupByPage(final Map<String, String> param, final int pageNum, final int perPageNum) {
        return (PageModle)this.getHibernateTemplate().execute((HibernateCallback)new HibernateCallback<PageModle<Map<String, String>>>(){

            public PageModle<Map<String, String>> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" from trade_jump_list p left join (select KEY,VALUE from CST_SYS_PARAM where OWNER='CHANNEL_ID' ) r on p.channel_id=r.KEY left join trade_qrcode_channel_inf c on p.channel_id=c.channel_id and p.channel_mcht_no=c.channel_mcht_no where 1=1 ");
                if (!StringUtil.isEmpty((String)param.get("jump_group"))) {
                    where.append("and p.jump_group=:jump_group ");
                    queryParam.put("jump_group", (String)param.get("jump_group"));
                }
                if (!StringUtil.isEmpty((String)param.get("channel_id"))) {
                    where.append("and p.channel_id=:channel_id ");
                    queryParam.put("channel_id", (String)param.get("channel_id"));
                }
                if (!StringUtil.isEmpty((String)param.get("channel_mcht_no"))) {
                    where.append("and p.channel_mcht_no=:channel_mcht_no ");
                    queryParam.put("channel_mcht_no", (String)param.get("channel_mcht_no"));
                }
                if (!StringUtil.isEmpty((String)param.get("trade_source"))) {
                    where.append("and p.trade_source=:trade_source ");
                    queryParam.put("trade_source", (String)param.get("trade_source"));
                }
                where.append("order by p.jump_group asc, p.trade_source asc, p.channel_id asc, p.channel_mcht_no asc");
                String sql = "select p.jump_group jump_group,p.group_name group_name,p.trade_source trade_source,p.channel_id channel_id,r.value channel_name,p.channel_mcht_no channel_mcht_no,c.channel_name channel_mcht_name,p.weight weight" + where.toString();
                String countSql = "SELECT count(*) countNum from (" + sql + ")";
                Query query = session.createSQLQuery(sql).addScalar("jump_group", (Type)StandardBasicTypes.STRING).addScalar("group_name", (Type)StandardBasicTypes.STRING).addScalar("trade_source", (Type)StandardBasicTypes.STRING).addScalar("channel_id", (Type)StandardBasicTypes.STRING).addScalar("channel_name", (Type)StandardBasicTypes.STRING).addScalar("channel_mcht_no", (Type)StandardBasicTypes.STRING).addScalar("channel_mcht_name", (Type)StandardBasicTypes.STRING).addScalar("weight", (Type)StandardBasicTypes.STRING).setResultTransformer((ResultTransformer)Transformers.ALIAS_TO_ENTITY_MAP);
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

    @Override
    public List<Map<String, String>> listAllJumpgroup(final Map<String, String> param) {
        return (List)this.getHibernateTemplate().execute((HibernateCallback)new HibernateCallback<List<Map<String, String>>>(){

            public List<Map<String, String>> doInHibernate(Session session) throws HibernateException {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                StringBuffer where = new StringBuffer(" from trade_jump_list p left join (select KEY,VALUE from CST_SYS_PARAM where OWNER='CHANNEL_ID' ) r on p.channel_id=r.KEY left join trade_qrcode_channel_inf c on p.channel_id=c.channel_id and p.channel_mcht_no=c.channel_mcht_no where 1=1 ");
                if (!StringUtil.isEmpty((String)param.get("jump_group"))) {
                    where.append("and p.jump_group=:jump_group ");
                    queryParam.put("jump_group", (String)param.get("jump_group"));
                }
                if (!StringUtil.isEmpty((String)param.get("channel_id"))) {
                    where.append("and p.channel_id=:channel_id ");
                    queryParam.put("channel_id", (String)param.get("channel_id"));
                }
                if (!StringUtil.isEmpty((String)param.get("channel_mcht_no"))) {
                    where.append("and p.channel_mcht_no=:channel_mcht_no ");
                    queryParam.put("channel_mcht_no", (String)param.get("channel_mcht_no"));
                }
                if (!StringUtil.isEmpty((String)param.get("trade_source"))) {
                    where.append("and p.trade_source=:trade_source ");
                    queryParam.put("trade_source", (String)param.get("trade_source"));
                }
                where.append("order by p.jump_group asc, p.trade_source asc, p.channel_id asc, p.channel_mcht_no asc");
                String sql = "select p.jump_group,p.group_name,p.trade_source,p.channel_id,r.value channel_name,p.channel_mcht_no,c.channel_name channel_mcht_name,p.weight" + where.toString();
                Query query = session.createSQLQuery(sql).addScalar("jump_group", (Type)StandardBasicTypes.STRING).addScalar("group_name", (Type)StandardBasicTypes.STRING).addScalar("trade_source", (Type)StandardBasicTypes.STRING).addScalar("channel_id", (Type)StandardBasicTypes.STRING).addScalar("channel_name", (Type)StandardBasicTypes.STRING).addScalar("channel_mcht_no", (Type)StandardBasicTypes.STRING).addScalar("channel_mcht_name", (Type)StandardBasicTypes.STRING).addScalar("weight", (Type)StandardBasicTypes.STRING).setResultTransformer((ResultTransformer)Transformers.ALIAS_TO_ENTITY_MAP);
                for (Map.Entry entry : queryParam.entrySet()) {
                    if (entry.getValue() == null || "".equals(entry.getValue())) continue;
                    query.setParameter((String)entry.getKey(), entry.getValue());
                }
                List list = query.list();
                return list;
            }
        });
    }

    @Override
    public void save(JumpListBean jumpListBean) {
        this.getHibernateTemplate().saveOrUpdate((Object)jumpListBean);
    }

    @Override
    public void update(JumpListBean jumpListBean) {
        this.getHibernateTemplate().update((Object)jumpListBean);
    }

    @Resource(name="sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

}
