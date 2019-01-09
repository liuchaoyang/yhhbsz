package com.yzxt.yh.module.msg.dao;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.msg.bean.AskCatalog;

public class AskCatalogDao extends HibernateSupport<AskCatalog> implements BaseDao<AskCatalog>
{
    /**
     * 获取一级目录
     */
    @SuppressWarnings("unchecked")
    public List<AskCatalog> getTopLevelAskCatalogs()
    {
        String sql = "select t from AskCatalog t where t.state = ? and t.level = ? order by t.seq asc";
        List<AskCatalog> list = super.getSession().createQuery(sql).setInteger(0, Constant.ASK_CATALOG_STATE_IN_USING)
                .setInteger(1, Constant.ASK_CATALOG_LEVEL_ONE).list();
        return list;
    }

    /**
     * 获取子目录
     */
    @SuppressWarnings("unchecked")
    public List<AskCatalog> getChildrenAskCatalogs(String parentCatalogId)
    {
        String sql = "select t from AskCatalog t where t.state = ? and t.parentId = ? order by t.seq asc";
        List<AskCatalog> list = super.getSession().createQuery(sql).setInteger(0, Constant.ASK_CATALOG_STATE_IN_USING)
                .setString(1, parentCatalogId).list();
        return list;
    }

    /**
     * 获取最新需要同步的时间
     * @return
     */
    public Timestamp getLatestCatalogSynTime()
    {
        HibernateParams params = new HibernateParams();
        String sql = "select max(t.update_time) as ts from msg_ask_catalog t where t.level = ?";
        params.add(Constant.ASK_CATALOG_LEVEL_TWO, Hibernate.INTEGER);
        return (Timestamp) super.getSession().createSQLQuery(sql.toString()).addScalar("ts", Hibernate.TIMESTAMP)
                .setParameters(params.getVals(), params.getTypes()).uniqueResult();
    }

    /**
     * 获取所有二级目录
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AskCatalog> getCatalogsBySyn()
    {
        HibernateParams params = new HibernateParams();
        String sql = "select t from AskCatalog t where t.state = ? and t.level = ? order by t.seq asc";
        params.add(Constant.ASK_CATALOG_STATE_IN_USING, Hibernate.INTEGER);
        params.add(Constant.ASK_CATALOG_LEVEL_TWO, Hibernate.INTEGER);
        List<AskCatalog> list = super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).list();
        return list;
    }

}
