package com.yzxt.yh.module.msg.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.msg.bean.KnowledgeCatalog;
import com.yzxt.yh.util.StringUtil;

public class KnowledgeCatalogDao extends HibernateSupport<KnowledgeCatalog> implements BaseDao<KnowledgeCatalog>
{
    /**
     * 查询健康知识目录信息，指定的最大条数，主要用于客户端查询
     * @param filter
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<KnowledgeCatalog> queryKnowledgeCatalogsBySyn(Map<String, Object> filter) throws Exception
    {

        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.parent_id, t.name_, t.detail, t.level, t.is_leaf, t.state, t.seq");
        sql.append(" from msg_knowledge_catalog t where t.state = ?");
        params.add(Constant.KNOWLEDGE_STATE_IN_USING, Hibernate.INTEGER);
        String parentId = (String) filter.get("parentId");
        if (StringUtil.isNotEmpty(parentId))
        {
            // 指定了父
            sql.append(" and t.parent_id = ?");
            params.add(filter.get("parentId"), Hibernate.STRING);
        }
        else
        {
            // 没有指定父ID，取顶级目录
            sql.append(" and (t.parent_id is null or t.parent_id = '')");
        }
        sql.append(" order by t.seq asc");
        List<Object[]> objList = super.getSession().createSQLQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).list();
        List<KnowledgeCatalog> list = new ArrayList<KnowledgeCatalog>();
        int idx = 0;
        for (Object[] objs : objList)
        {
            KnowledgeCatalog cata = new KnowledgeCatalog();
            idx = 0;
            cata.setId((String) objs[idx]);
            idx++;
            cata.setParentId((String) objs[idx]);
            idx++;
            cata.setName((String) objs[idx]);
            idx++;
            cata.setDetail((String) objs[idx]);
            idx++;
            cata.setLevel((Integer) objs[idx]);
            idx++;
            cata.setIsLeaf((Integer) objs[idx]);
            idx++;
            cata.setState((Integer) objs[idx]);
            idx++;
            cata.setSeq((Integer) objs[idx]);
            idx++;
            list.add(cata);
        }
        return list;
    }

    /**
     * 目录树获取子节点
     */
    @SuppressWarnings("unchecked")
    public List<KnowledgeCatalog> getTreeNodeChildren(String parentId)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t from KnowledgeCatalog t where t.state = ?");
        params.add(Constant.KNOWLEDGE_STATE_IN_USING, Hibernate.INTEGER);
        if (StringUtil.isNotEmpty(parentId))
        {
            sql.append(" and t.parentId = ?");
            params.add(parentId, Hibernate.STRING);
        }
        else
        {
            sql.append(" and(t.parentId is null or t.parentId = '')");
        }
        sql.append(" order by t.seq asc");
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes()).list();
    }

    @SuppressWarnings("unchecked")
    public List<KnowledgeCatalog> getKnowledgeCatalogByName(String parentname)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.* from msg_knowledge_catalog t where t.name_ = ?");
        params.add(parentname, Hibernate.STRING);
        List<Object[]> objList = super.getSession().createSQLQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).list();
        System.out.println(objList);
        List<KnowledgeCatalog> list = new ArrayList<KnowledgeCatalog>();
        int idx = 0;
        for (Object[] objs : objList)
        {
            KnowledgeCatalog cata = new KnowledgeCatalog();
            idx = 0;
            cata.setId((String) objs[idx]);
            idx++;
            cata.setParentId((String) objs[idx]);
            idx++;
            idx++;
            cata.setName((String) objs[idx]);
            idx++;
            cata.setDetail((String) objs[idx]);
            idx++;
            cata.setLevel((Integer) objs[idx]);
            idx++;
            cata.setIsLeaf((Integer) objs[idx]);
            idx++;
            cata.setState((Integer) objs[idx]);
            idx++;
            cata.setSeq((Integer) objs[idx]);
            idx++;
            list.add(cata);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<KnowledgeCatalog> getKnowledgeCatalogById(String parentId)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.* from msg_knowledge_catalog t where t.parent_id = ?");
        params.add(parentId, Hibernate.STRING);
        List<Object[]> objList = super.getSession().createSQLQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).list();
        System.out.println(objList);
        List<KnowledgeCatalog> list = new ArrayList<KnowledgeCatalog>();
        int idx = 0;
        for (Object[] objs : objList)
        {
            KnowledgeCatalog cata = new KnowledgeCatalog();
            idx = 0;
            cata.setId((String) objs[idx]);
            idx++;
            cata.setParentId((String) objs[idx]);
            idx++;
            idx++;
            cata.setName((String) objs[idx]);
            idx++;
            cata.setDetail((String) objs[idx]);
            idx++;
            cata.setLevel((Integer) objs[idx]);
            idx++;
            cata.setIsLeaf((Integer) objs[idx]);
            idx++;
            cata.setState((Integer) objs[idx]);
            idx++;
            cata.setSeq((Integer) objs[idx]);
            idx++;
            list.add(cata);
        }
        return list;
    }

}
