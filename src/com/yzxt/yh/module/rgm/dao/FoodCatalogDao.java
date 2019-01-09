package com.yzxt.yh.module.rgm.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.rgm.bean.FoodCatalog;

public class FoodCatalogDao extends HibernateSupport<FoodCatalog> implements BaseDao<FoodCatalog>
{
    /**
     * 获取一级目录
     */
    @SuppressWarnings("unchecked")
    public List<FoodCatalog> getTopLevelFoodCatalogs()
    {
        String sql = "select t from FoodCatalog t where t.level = ? order by t.createTime asc";
        List<FoodCatalog> list = super.getSession().createQuery(sql).setInteger(0, Constant.FOOD_CATALOG_LEVEL_ONE)
                .list();
        return list;
    }

    /**
     * 获取子目录
     */
    @SuppressWarnings("unchecked")
    public List<FoodCatalog> getChildrenFoodCatalogs(String parentCatalogId)
    {
        String sql = "select t from FoodCatalog t where t.parentId = ? order by t.createTime asc";
        List<FoodCatalog> list = super.getSession().createQuery(sql).setString(0, parentCatalogId).list();
        return list;
    }

    /**
     * 客户端更新数据
     * @author huangGang
     * 2014.11.18
     */
    @SuppressWarnings("unchecked")
    public List<FoodCatalog> getFoodCatalogForClient()
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.parent_id, t.level, t.name, t.create_by, t.create_time, t.update_by, t.update_time  ");
        sql.append(" from rgm_food_catalog t ");
        sql.append(" where 1=1");
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("id", Hibernate.STRING).addScalar("parent_id", Hibernate.STRING)
                .addScalar("level", Hibernate.INTEGER).addScalar("name", Hibernate.STRING) 
                .addScalar("create_by", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("update_by", Hibernate.STRING).addScalar("update_time", Hibernate.TIMESTAMP)  
                .setParameters(params.getVals(), params.getTypes()).list();
        List<FoodCatalog> list = new ArrayList<FoodCatalog>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            FoodCatalog foodCatalog = new FoodCatalog();
            idx = 0;
            foodCatalog.setId((String) objs[idx]);
            idx++;
            foodCatalog.setParentId((String) objs[idx]);
            idx++;
            foodCatalog.setLevel(objs[idx] != null ? (Integer) objs[idx] : 0);
            idx++;
            foodCatalog.setName((String) objs[idx]);
            idx++;
            foodCatalog.setCreateBy((String) objs[idx]);
            idx++;
            foodCatalog.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            foodCatalog.setUpdateBy((String) objs[idx]);
            idx++;
            foodCatalog.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            list.add(foodCatalog);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<FoodCatalog> getfoodCatalog()
    {
        String sql = "select t from FoodCatalog t where 1=1 order by t.createTime asc";
        List<FoodCatalog> list = super.getSession().createQuery(sql).list();
        return list;
    }
}
