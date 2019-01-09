package com.yzxt.yh.module.msg.dao;

import java.util.List;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.msg.bean.InfoCatalog;
import com.yzxt.yh.module.msg.bean.InfoCatalogRelate;

public class InfoCatalogRelateDao extends HibernateSupport<InfoCatalogRelate> implements BaseDao<InfoCatalogRelate>
{
    /**
     * 插入健康资讯目录
     * @param infoCatalogRelate
     * @return 主键ID
     * @throws Exception 
     */
    public String insert(InfoCatalogRelate infoCatalogRelate) throws Exception
    {
        super.save(infoCatalogRelate);
        return infoCatalogRelate.getId();
    }

    /**
     * 添加资讯栏目关系
     * @param infoId
     * @param catalogId
     * @throws Exception 
     */
    public void addInfoCatalogRelate(String infoId, String catalogId) throws Exception
    {
        InfoCatalogRelate relate = new InfoCatalogRelate();
        relate.setInfoId(infoId);
        relate.setCatalogId(catalogId);
        super.save(relate);
    }

    /**
     * 添加资讯栏目关系
     * @param infoId
     * @param catalogId
     * @throws Exception 
     */
    public void addInfoCatalogRelates(String infoId, String[] catalogIds) throws Exception
    {
        for (String catalogId : catalogIds)
        {
            InfoCatalogRelate relate = new InfoCatalogRelate();
            relate.setInfoId(infoId);
            relate.setCatalogId(catalogId);
            super.save(relate);
        }
    }

    /**
     * 删除资讯栏目关系
     * @param infoId
     */
    public int deleteInfoCatalogRelateByInfo(String infoId)
    {
        HibernateParams params = new HibernateParams();
        String sql = "delete from InfoCatalogRelate t where t.infoId = ?";
        params.add(infoId, Hibernate.STRING);
        return super.getSession().createQuery(sql).setParameters(params.getVals(), params.getTypes()).executeUpdate();
    }

    /**
     * 查询资讯栏目信息
     * @param 主键ID
     * @throws Exception 
     */
    @SuppressWarnings(
    {"unchecked"})
    public String[] getInfoColumns(String id) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct t.catalog_id");
        sql.append(" from msg_info_catalog_relate t");
        sql.append(" where 1=1");
        sql.append(" and exists (select sic.id from msg_info_catalog sic where sic.state = ")
                .append(Constant.INFO_CATALOG_STATE_IN_USING).append(" and sic.type_ = ?")
                .append(" and sic.id = t.catalog_id)");
        sql.append(" and t.info_id = ?");
        params.add(Constant.INFO_CATALOG_TYPE_COLUMN, Hibernate.INTEGER);
        params.add(id, Hibernate.STRING);
        List<String> list = super.getSession().createSQLQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).list();
        return list.toArray(new String[list.size()]);
    }

    /**
     * 查询资讯专题信息
     * @param 主键ID
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public InfoCatalog getInfoTopic(String id) throws Exception
    {
        InfoCatalog cata = null;
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct tic.id, tic.name_");
        sql.append(" from msg_info_catalog_relate t");
        sql.append(" inner join msg_info_catalog tic on tic.state = ").append(Constant.INFO_CATALOG_STATE_IN_USING)
                .append(" and tic.type_ = ?").append(" and tic.id = t.catalog_id");
        sql.append(" where 1=1");
        sql.append(" and t.info_id = ?");
        params.add(Constant.INFO_CATALOG_TYPE_TOPIC, Hibernate.INTEGER);
        params.add(id, Hibernate.STRING);
        List<Object[]> listObjs = super.getSession().createSQLQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).list();
        if (listObjs != null && listObjs.size() > 0)
        {
            Object[] objs = listObjs.get(0);
            cata = new InfoCatalog();
            cata.setId((String) objs[0]);
            cata.setName((String) objs[1]);
        }
        return cata;
    }
}
