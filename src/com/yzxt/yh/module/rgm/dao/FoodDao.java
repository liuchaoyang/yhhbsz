package com.yzxt.yh.module.rgm.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;


import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.rgm.bean.Food;
import com.yzxt.yh.util.StringUtil;

public class FoodDao extends HibernateSupport<Food> implements BaseDao<Food>
{
    /**
     * 分页查询食物信息
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public PageModel<Food> queryFoodByPage(Map<String, Object> filter, int page, int pageSize) throws Exception
    {
        PageModel<Food> pageModel = new PageModel<Food>();
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append("from rgm_food t");
        mSql.append(" inner join rgm_food_catalog tfc on tfc.id = t.catalog_id");
        mSql.append(" where t.state = ?");
        params.add(Constant.FOOD_STATE_ENABLE, Hibernate.INTEGER);
        // 关键字
        String keyword = (String) filter.get("keyword");
        if (StringUtil.isNotEmpty(keyword))
        {
            mSql.append(" and t.name_ like ").append(params.addLikePart(keyword));
        }
        // 所属目录
        String topLevelCatalogId = (String) filter.get("topLevelCatalogId");
        String secondLevelCatalogId = (String) filter.get("secondLevelCatalogId");
        if (StringUtil.isNotEmpty(secondLevelCatalogId))
        {
            mSql.append(" and t.catalog_id = ?");
            params.add(secondLevelCatalogId, Hibernate.STRING);
        }
        else if (StringUtil.isNotEmpty(topLevelCatalogId))
        {
            mSql.append(" and tfc.parent_id = ?");
            params.add(topLevelCatalogId, Hibernate.STRING);
        }
        // 总启启记录数
        StringBuilder totalCountsql = new StringBuilder();
        totalCountsql.append("select count(*) as c ").append(mSql);
        Integer totalCount = (Integer) super.getSession().createSQLQuery(totalCountsql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.name_, t.catalog_id, t.food_heat, t.state, t.update_by, t.update_time, tfc.parent_id as parent_catalog_id");
        pSql.append(", concat((select sfc.name from rgm_food_catalog sfc where sfc.id = tfc.parent_id), '/', tfc.name) as catalog_full_name ");
        pSql.append(mSql).append(" order by t.create_time asc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("catalog_id", Hibernate.STRING)
                .addScalar("food_heat", Hibernate.DOUBLE).addScalar("state", Hibernate.INTEGER)
                .addScalar("update_by", Hibernate.STRING).addScalar("update_time", Hibernate.TIMESTAMP)
                .addScalar("parent_catalog_id", Hibernate.STRING).addScalar("catalog_full_name", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Food> list = new ArrayList<Food>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Food food = new Food();
            idx = 0;
            food.setId((String) objs[idx]);
            idx++;
            food.setName((String) objs[idx]);
            idx++;
            food.setCatalogId((String) objs[idx]);
            idx++;
            food.setFoodHeat(objs[idx] != null ? (Double) objs[idx] : 0d);
            idx++;
            food.setState(objs[idx] != null ? (Integer) objs[idx] : 0);
            idx++;
            food.setUpdateBy((String) objs[idx]);
            idx++;
            food.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            food.setParentCatalogId((String) objs[idx]);
            idx++;
            food.setCatalogFullName((String) objs[idx]);
            idx++;
            list.add(food);
        }
        pageModel.setTotal(totalCount);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 添加食物信息
     * @throws Exception 
     */
    public String insert(Food food) throws Exception
    {
        super.save(food);
        return food.getId();
    }

    /**
     * 更新食物信息
     */
    public int update(Food food)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update Food t set t.name = ?, t.catalogId = ?, t.foodHeat = ?");
        sql.append(", t.updateBy = ?, t.updateTime = ?");
        sql.append(" where t.id = ?");
        params.add(food.getName(), Hibernate.STRING);
        params.add(food.getCatalogId(), Hibernate.STRING);
        params.add(food.getFoodHeat(), Hibernate.DOUBLE);
        params.add(food.getUpdateBy(), Hibernate.STRING);
        params.add(food.getUpdateTime(), Hibernate.TIMESTAMP);
        params.add(food.getId(), Hibernate.STRING);
        int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

    /**
     * 加载食物信息
     */
    public Food load(String id)
    {
        return super.get(id);
    }

    /**
     * 删除食物信息
     */
    public void delete(String id)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update Food t set t.state = ? where t.id = ?");
        params.add(Constant.FOOD_STATE_DISABLE, Hibernate.INTEGER);
        params.add(id, Hibernate.STRING);
        super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 判断食物名称是否已经存在
     */
    public boolean isFoodNameExist(String foodName, String exceptId)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) as c from Food t where t.state = ?");
        params.add(Constant.FOOD_STATE_ENABLE, Hibernate.INTEGER);
        if (StringUtil.isNotEmpty(exceptId))
        {
            sql.append(" and lower(t.code) <> ?");
            params.add(exceptId.toLowerCase(), Hibernate.STRING);
        }
        sql.append(" and lower(t.name) = ?");
        params.add(foodName.toLowerCase(), Hibernate.STRING);
        // 查询结果
        Long c = (Long) super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).uniqueResult();
        return c.longValue() > 0;
    }

    @SuppressWarnings("unchecked")
    public List<Food> getFoodForClient()
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.name_, t.catalog_id, t.food_heat, t.state, t.create_by, t.create_time, t.update_by, t.update_time  ");
        sql.append(" from rgm_food t ");
        /*sql.append(" inner join rgm_food rf on t.food_id = rf.id");*/
        sql.append(" where 1=1");
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("catalog_id", Hibernate.STRING)
                .addScalar("food_heat", Hibernate.DOUBLE).addScalar("state", Hibernate.INTEGER)
                .addScalar("create_by", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("update_by", Hibernate.STRING).addScalar("update_time", Hibernate.TIMESTAMP)  
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Food> list = new ArrayList<Food>();
        System.out.println(sql);
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Food food = new Food();
            idx = 0;
            food.setId((String) objs[idx]);
            idx++;
            food.setName((String) objs[idx]);
            idx++;
            food.setCatalogId((String) objs[idx]);
            idx++;
            food.setFoodHeat(objs[idx] != null ? (Double) objs[idx] : 0d);
            idx++;
            food.setState(objs[idx] != null ? (Integer) objs[idx] : 0);
            idx++;
            food.setCreateBy((String) objs[idx]);
            idx++;
            food.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            food.setUpdateBy((String) objs[idx]);
            idx++;
            food.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            list.add(food);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<Food> getFood(Map<String, Object> filter)
    {
        String catalogId= (String)filter.get("catalogId");
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.name_, t.catalog_id");
        sql.append(" from rgm_food t ");
        sql.append(" where t.catalog_id = ?");
        params.add(catalogId, Hibernate.STRING);
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("catalog_id", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Food> list = new ArrayList<Food>();
        System.out.println(sql);
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Food food = new Food();
            idx = 0;
            food.setId((String) objs[idx]);
            idx++;
            food.setName((String) objs[idx]);
            idx++;
            food.setCatalogId((String) objs[idx]);
            idx++;
            list.add(food);
        }
        return list;
    }

}
