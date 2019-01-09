package com.yzxt.yh.module.msg.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.msg.bean.Knowledge;
import com.yzxt.yh.util.StringUtil;

public class KnowledgeDao extends HibernateSupport<Knowledge> implements BaseDao<Knowledge>
{

    /**
     * 插入健康知识
     * @param knowledge
     * @return 主键ID
     * @throws Exception 
     */
    public String insert(Knowledge knowledge) throws Exception
    {
        super.save(knowledge);
        return knowledge.getId();
    }

    /**
     * 更新健康知识
     * @param knowledge
     * @return 主键ID
     * @throws Exception 
     */
    public int update(Knowledge knowledge) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update Knowledge t set t.title = ?, t.richtextId = ?, t.catalogId = ?");
        sql.append(", t.updateBy = ?, t.updateTime = ?");
        sql.append(" where t.id = ?");
        params.add(knowledge.getTitle(), Hibernate.STRING);
        params.add(knowledge.getRichtextId(), Hibernate.STRING);
        params.add(knowledge.getCatalogId(), Hibernate.STRING);
        params.add(knowledge.getUpdateBy(), Hibernate.STRING);
        params.add(knowledge.getUpdateTime(), Hibernate.TIMESTAMP);
        params.add(knowledge.getId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 删除健康知识
     * @param id
     * @throws Exception 
     */
    public void delete(String id) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("delete from Knowledge t where t.id = ?");
        params.add(id, Hibernate.STRING);
        super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 插入健康知识
     * @param id
     * @throws Exception 
     */
    public Knowledge load(String id) throws Exception
    {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.title, t.richtext_id, t.catalog_id");
        sql.append(", t.create_by, t.create_time, t.update_by, t.update_time");
        sql.append(", trt.summary, trt.content");
        sql.append(", tic.name_ as catalog_name");
        sql.append(", tu.name_ as update_by_name");
        sql.append(" from msg_knowledge t");
        sql.append(" inner join msg_richtext trt on trt.id = t.richtext_id");
        sql.append(" inner join msg_knowledge_catalog tic on tic.id = t.catalog_id");
        sql.append(" left join sys_user tu on tu.id = t.update_by");
        sql.append(" where t.id = ?");
        Object[] objs = (Object[]) super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("title", Hibernate.STRING).addScalar("richtext_id", Hibernate.STRING)
                .addScalar("catalog_id", Hibernate.STRING).addScalar("create_by", Hibernate.STRING)
                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("update_by", Hibernate.STRING)
                .addScalar("update_time", Hibernate.TIMESTAMP).addScalar("summary", Hibernate.STRING)
                .addScalar("content", Hibernate.STRING).addScalar("catalog_name", Hibernate.STRING)
                .addScalar("update_by_name", Hibernate.STRING).setString(0, id).uniqueResult();
        Knowledge knowledge = null;
        if (objs != null)
        {
            knowledge = new Knowledge();
            int i = 0;
            knowledge.setId((String) objs[i]);
            i++;
            knowledge.setTitle((String) objs[i]);
            i++;
            knowledge.setRichtextId((String) objs[i]);
            i++;
            knowledge.setCatalogId((String) objs[i]);
            i++;
            knowledge.setCreateBy((String) objs[i]);
            i++;
            knowledge.setCreateTime(objs[i] != null ? (Timestamp) objs[i] : null);
            i++;
            knowledge.setUpdateBy((String) objs[i]);
            i++;
            knowledge.setUpdateTime(objs[i] != null ? (Timestamp) objs[i] : null);
            i++;
            knowledge.setSummary((String) objs[i]);
            i++;
            knowledge.setContent((String) objs[i]);
            i++;
            knowledge.setCatalogName((String) objs[i]);
            i++;
            knowledge.setUpdateByName((String) objs[i]);
            i++;
        }
        return knowledge;
    }

    /**
     * 分页查询健康知识信息
     */
    @SuppressWarnings("unchecked")
    public PageModel<Knowledge> queryKnowledgeByPage(Map<String, Object> filter, int page, int pageSize)
            throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append("from msg_knowledge t");
        mSql.append(" inner join msg_richtext trt on trt.id = t.richtext_id");
        mSql.append(" inner join msg_knowledge_catalog tkc on tkc.id = t.catalog_id");
        mSql.append(" left join sys_user tu on tu.id = t.update_by");
        mSql.append(" where 1=1");
        // 知识标题名称
        String keyword = (String) filter.get("keyword");
        if (StringUtil.isNotEmpty(keyword))
        {
            mSql.append(" and t.title like ").append(params.addLikePart(keyword));
        }
        // 所属目录
        String catalogFullId = (String) filter.get("catalogFullId");
        if (StringUtil.isNotEmpty(catalogFullId))
        {
            mSql.append(" and (tkc.full_id = ? or tkc.full_id like ?)");
            params.add(catalogFullId, Hibernate.STRING);
            params.add(catalogFullId + "/%", Hibernate.STRING);
        }
        // 总启启记录数
        StringBuilder totalCountsql = new StringBuilder();
        totalCountsql.append("select count(*) as c ").append(mSql);
        Integer totalCount = (Integer) super.getSession().createSQLQuery(totalCountsql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.title, trt.summary, tu.name_, t.update_time ").append(mSql)
                .append(" order by t.create_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Knowledge> list = new ArrayList<Knowledge>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Knowledge nlg = new Knowledge();
            idx = 0;
            nlg.setId((String) objs[idx]);
            idx++;
            nlg.setTitle((String) objs[idx]);
            idx++;
            nlg.setSummary((String) objs[idx]);
            idx++;
            nlg.setUpdateByName((String) objs[idx]);
            idx++;
            nlg.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            list.add(nlg);
        }
        PageModel<Knowledge> pageModel = new PageModel<Knowledge>();
        pageModel.setTotal(totalCount);
        pageModel.setPageNo(page);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 查询健康知识信息，指定的最大条数，主要用于客户端查询
     * @param filter
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Knowledge> queryKnowledgesBySyn(Map<String, Object> filter) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.title,t.catalog_id, trt.summary, tu.name_, t.update_time");
        sql.append(" from msg_knowledge t");
        sql.append(" inner join msg_richtext trt on trt.id = t.richtext_id");
        sql.append(" left join sys_user tu on tu.id = t.update_by");
        sql.append(" where 1=1");
        // 所属目录
        String catalogId = (String) filter.get("nlgCatalogId");
        sql.append(" and t.catalog_id = ?");
        params.add(catalogId, Hibernate.STRING);
        // 指定拉取时间
        Timestamp sysTime = (Timestamp) filter.get("sysTime");
        if (sysTime != null)
        {
            sql.append(" and t.update_time < ?");
            params.add(sysTime, Hibernate.TIMESTAMP);
        }
        // 取最新发布倒序
        sql.append(" order by t.update_time desc");
        // 取数据条数
        sql.append(" limit 0, ?");
        params.add(filter.get("maxSize"), Hibernate.INTEGER);
        // 查询数据
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Knowledge> list = new ArrayList<Knowledge>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Knowledge nlg = new Knowledge();
            idx = 0;
            nlg.setId((String) objs[idx]);
            idx++;
            nlg.setTitle((String) objs[idx]);
            idx++;
            nlg.setCatalogId((String) objs[idx]);
            idx++;
            nlg.setSummary((String) objs[idx]);
            idx++;
            nlg.setUpdateByName((String) objs[idx]);
            idx++;
            nlg.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            list.add(nlg);
        }
        return list;
    }

}
