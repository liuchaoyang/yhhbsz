package com.yzxt.yh.module.msg.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.msg.bean.AskReply;
import com.yzxt.yh.module.sys.bean.CustFamily;
import com.yzxt.yh.util.StringUtil;

public class AskReplyDao extends HibernateSupport<AskReply> implements BaseDao<AskReply>
{
    public String insert(AskReply askReply) throws Exception
    {
        super.save(askReply);
        return askReply.getId();
    }

    public int update(AskReply askReply)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update AskReply t set t.richtextId = ?");
        sql.append(", t.updateBy = ?, t.updateTime = ?");
        sql.append(" where t.id = ?");
        params.add(askReply.getRichtextId(), Hibernate.STRING);
        params.add(askReply.getUpdateBy(), Hibernate.STRING);
        params.add(askReply.getUpdateTime(), Hibernate.TIMESTAMP);
        params.add(askReply.getId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    public void delete(String id)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("delete from AskReply t where t.id = ?");
        params.add(id, Hibernate.STRING);
        super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    public AskReply load(String id)
    {
        return super.get(id);
    }

    /**
     * 删除问答回复信息
     * @param askId
     * @return
     */
    public int deleteReplysByAsk(String askId)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("delete from AskReply t where t.askId = ?");
        params.add(askId, Hibernate.STRING);
        int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

    /**
     * 查询问答回复列表
     * @param askId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AskReply> getReplysByAsk(String askId)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.ask_id, t.richtext_id, t.update_by, t.update_time");
        sql.append(", trt.content, tu.name_ as update_by_name");
        sql.append(" from msg_ask_reply t");
        sql.append(" inner join msg_richtext trt on trt.id = t.richtext_id");
        sql.append(" left join sys_user tu on tu.id = t.update_by");
        sql.append(" where t.ask_id = ?");
        sql.append(" order by t.update_time asc");
        params.add(askId, Hibernate.STRING);
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("ask_id", Hibernate.STRING).addScalar("richtext_id", Hibernate.STRING)
                .addScalar("update_by", Hibernate.STRING).addScalar("update_time", Hibernate.TIMESTAMP)
                .addScalar("content", Hibernate.STRING).addScalar("update_by_name", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<AskReply> list = new ArrayList<AskReply>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            AskReply reply = new AskReply();
            idx = 0;
            reply.setId((String) objs[idx]);
            idx++;
            reply.setAskId((String) objs[idx]);
            idx++;
            reply.setRichtextId((String) objs[idx]);
            idx++;
            reply.setUpdateBy((String) objs[idx]);
            idx++;
            reply.setUpdateTime((Timestamp) objs[idx]);
            idx++;
            reply.setContent((String) objs[idx]);
            idx++;
            reply.setUpdateByName((String) objs[idx]);
            idx++;
            list.add(reply);
        }
        return list;
    }

    /**
     * 查询问题回复信息列表，主要用于客户端
     * @param filter
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AskReply> queryReplysBySyn(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        String custId = (String) filter.get("custId");
        String doctorId = (String) filter.get("doctorId");
        Timestamp sysTime = (Timestamp) filter.get("sysTime");
        Integer maxSize = (Integer) filter.get("maxSize");
        sql.append("select t.id, trt.content, t.update_by, t.update_time,ms.type");
        sql.append(", tu.type_ as update_by_user_type, tu.name_ as update_by_name,ms.create_by,ms.title,ms.create_time");
        sql.append(" from msg_ask_reply t");
        sql.append(" inner join msg_richtext trt on trt.id = t.richtext_id");
        sql.append(" left join sys_user tu on tu.id = t.update_by");
        sql.append(" inner join msg_ask ms on t.ask_id= ms.id");
        if(StringUtil.isNotEmpty(custId)){
    	   sql.append(" where ms.update_by = ?");
           params.add(custId, Hibernate.STRING);
        }
        if(StringUtil.isNotEmpty(doctorId)){
     	   sql.append(" and t.update_by = ?");
           params.add(doctorId, Hibernate.STRING);
         }
        if (sysTime != null)
        {
            sql.append(" and t.update_time < ?");
            params.add(sysTime, Hibernate.TIMESTAMP);
        }
        // 取最新发布倒序
        sql.append(" order by t.update_time asc");
        // 取数据条数
        sql.append(" limit 0, ?");
        params.add(maxSize, Hibernate.INTEGER);
        // 查询数据
        List<Object[]> listObjs = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("content", Hibernate.STRING).addScalar("update_by", Hibernate.STRING).addScalar("type", Hibernate.INTEGER)
                .addScalar("update_time", Hibernate.TIMESTAMP).addScalar("update_by_user_type", Hibernate.INTEGER)
                .addScalar("update_by_name", Hibernate.STRING) .addScalar("create_by", Hibernate.STRING)
                .addScalar("title", Hibernate.STRING) .addScalar("create_time", Hibernate.TIMESTAMP)
                .setParameters(params.getVals(), params.getTypes())
                .list();
        List<AskReply> list = new ArrayList<AskReply>();
        if (listObjs != null && listObjs.size() > 0)
        {
            int idx = 0;
            for (Object[] objs : listObjs)
            {
                AskReply reply = new AskReply();
                idx = 0;
                reply.setId((String) objs[idx]);
                idx++;
                reply.setContent((String) objs[idx]);
                idx++;
                reply.setUpdateBy((String) objs[idx]);
                idx++;
                reply.setType((Integer) objs[idx]);
                idx++;
                reply.setUpdateTime((Timestamp) objs[idx]);
                idx++;
                reply.setUpdateByUserType((Integer) objs[idx]);
                idx++;
                reply.setUpdateByName((String) objs[idx]);
                idx++;
                reply.setCreateBys((String) objs[idx]);
                idx++;
                reply.setTitle((String) objs[idx]);
                idx++;
                reply.setCreateTimes((Timestamp) objs[idx]);
                idx++;
                list.add(reply);
            }
        }
        return list;
    }

    /**
     * 获取问题的一个回答
     * @return
     */
    @SuppressWarnings("unchecked")
    public AskReply getAskReplyBySyn(String askId)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, trt.content, t.update_by, t.update_time");
        sql.append(", tu.type_ as update_by_user_type, tu.name_ as update_by_name");
        sql.append(" from msg_ask_reply t");
        sql.append(" inner join msg_richtext trt on trt.id = t.richtext_id");
        sql.append(" left join sys_user tu on tu.id = t.update_by");
        sql.append(" where t.ask_id = ?");
        params.add(askId, Hibernate.STRING);
        sql.append(" order by t.update_time asc");
        // 取数据条数
        sql.append(" limit 0, ?");
        params.add(1, Hibernate.INTEGER);
        // 查询数据
        List<Object[]> listObjs = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("content", Hibernate.STRING).addScalar("update_by", Hibernate.STRING)
                .addScalar("update_time", Hibernate.TIMESTAMP).addScalar("update_by_user_type", Hibernate.INTEGER)
                .addScalar("update_by_name", Hibernate.STRING).setParameters(params.getVals(), params.getTypes())
                .list();
        AskReply reply = null;
        if (listObjs != null && listObjs.size() > 0)
        {
            int idx = 0;
            for (Object[] objs : listObjs)
            {
                reply = new AskReply();
                idx = 0;
                reply.setId((String) objs[idx]);
                idx++;
                reply.setContent((String) objs[idx]);
                idx++;
                reply.setUpdateBy((String) objs[idx]);
                idx++;
                reply.setUpdateTime((Timestamp) objs[idx]);
                idx++;
                reply.setUpdateByUserType((Integer) objs[idx]);
                idx++;
                reply.setUpdateByName((String) objs[idx]);
                idx++;
            }
        }
        return reply;
    }
   
}
