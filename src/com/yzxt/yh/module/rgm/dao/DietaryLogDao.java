package com.yzxt.yh.module.rgm.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.rgm.bean.DietaryLog;



public  class DietaryLogDao extends HibernateSupport<DietaryLog> implements BaseDao<DietaryLog>
{
    /*
     * 添加健康日志
     * */
    public String addDietaryLog(DietaryLog dietaryLog) throws Exception
    {
        super.save(dietaryLog);
        return dietaryLog.getId();
    }

    /**
     * 膳食日志列表
     * @author huangGang
     * update 2014.11.17
     * 客户端查询膳食日志总列表
     */
    @SuppressWarnings("unchecked")
    public List<DietaryLog> getList(Map<String, Object> filter) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(t.food_weight*rf.food_heat)/1000 as intakeEnergy, left(t.dietary_time,10) as dietaryTime");
        sql.append(" from rgm_dietary_log t ");
        sql.append(" inner join rgm_food rf on t.food_id = rf.id");
        sql.append(" where 1=1");
        String creator = (String) filter.get("createBy");
        sql.append(" and t.user_id = ?");
        params.add(creator, Hibernate.STRING);
        // 指定拉取时间 上拉 是之前的数据，下拉是之后的数据 下拉的时间比实际的时间少1
        //1表示同步时间点之后的数据，2表示同步时间点之前的数据
        Timestamp sysTime = (Timestamp) filter.get("sysTime");
        /* int synType = (Integer)filter.get("synType");
         if (synType == 2)
         {
             Timestamp endTime = sysTime;
             if (sysTime != null)
             {
                 sql.append(" and t.sport_time < ?");
                 params.add(endTime, Hibernate.TIMESTAMP);
             }
         }
         else if(synType ==1)
         {
             Timestamp beginTime = sysTime;
             if (sysTime != null)
             {
                 sql.append(" and t.sport_time > ?");
                 params.add(beginTime, Hibernate.TIMESTAMP);
             }
         }*/
        if (sysTime != null)
        {
            sql.append(" and t.dietary_time < ?");
            params.add(sysTime, Hibernate.TIMESTAMP);
        }
        /*if (sysTime != null)
        {
            sql.append(" and t.update_time < ?");
            params.add(sysTime, Hibernate.TIMESTAMP);
        }*/
        // 取最新发布倒序
        //根据时间天数分组 得到一天的总能量和总时长
        sql.append(" group by left(t.dietary_time,10) ");
        sql.append(" order by dietaryTime desc");
        // 取数据条数
        sql.append(" limit 0, ?");
        params.add(filter.get("maxSize"), Hibernate.INTEGER);
        // 查询数据

        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("intakeEnergy", Hibernate.DOUBLE)
                .addScalar("dietaryTime", Hibernate.TIMESTAMP).setParameters(params.getVals(), params.getTypes()).list();
        List<DietaryLog> list = new ArrayList<DietaryLog>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            DietaryLog dietaryLog = new DietaryLog();
            idx = 0;
            dietaryLog.setIntakeEnergy((Double) objs[idx]);
            idx++;
            dietaryLog.setDietaryTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            list.add(dietaryLog);
        }
        return list;
    }

    /**
     * 膳食日志详细列表
     * @author huangGang
     * update time 2014.11.17
     * 客户端查询
     */
    @SuppressWarnings("unchecked")
    public List<DietaryLog> getDetailLog(Map<String, Object> filter) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.user_id, t.id, t.dietary_type, t.food_id, t.dietary_time, t.food_weight, t.create_time ");
        sql.append(" from rgm_dietary_log t ");
        sql.append(" inner join rgm_food rf on t.food_id = rf.id");
        sql.append(" where 1=1");
        String creator = (String) filter.get("creator");
        sql.append(" and t.user_id = ?");
        params.add(creator, Hibernate.STRING);
        Date intakeTime = (Date) filter.get("intakeTime");
        if (intakeTime != null)
        {
            sql.append(" and t.dietary_time = ?");
            params.add(intakeTime, Hibernate.DATE);
        }
        sql.append(" order by t.food_weight desc");
        // 取数据条数
        // 查询数据
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("user_id", Hibernate.STRING).addScalar("id", Hibernate.STRING)
                .addScalar("dietary_type", Hibernate.INTEGER).addScalar("food_id", Hibernate.STRING)
                .addScalar("dietary_time", Hibernate.TIMESTAMP)
                .addScalar("food_weight", Hibernate.DOUBLE).addScalar("create_time", Hibernate.TIMESTAMP)
                .setParameters(params.getVals(), params.getTypes()).list();
        System.out.println(sql);
        List<DietaryLog> list = new ArrayList<DietaryLog>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            DietaryLog dietaryLog = new DietaryLog();
            idx = 0;
            dietaryLog.setUserId((String) objs[idx]);
            idx++;
            dietaryLog.setId((String) objs[idx]);
            idx++;
            dietaryLog.setDietaryType((Integer) objs[idx]);
            idx++;
            dietaryLog.setFoodId((String) objs[idx]);
            idx++;
            dietaryLog.setDietaryTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            dietaryLog.setFoodWeight((Double) objs[idx]);
            idx++;
            dietaryLog.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            list.add(dietaryLog);
        }
        return list;
    }

    /*
     * 用于服务端查询膳食总列表
     * @author huangGang
     * 2014.10.11
     * */
    @SuppressWarnings("unchecked")
    public PageModel<DietaryLog> getDietaryLogList(Map<String, Object> filter, int page, int pagesize) throws Exception
    {
        PageModel<DietaryLog> pageModel = new PageModel<DietaryLog>();
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        /*StringBuilder mSql = new StringBuilder();*/
        sql.append("select sum(t.food_weight*rf.food_heat)/1000 as intakeEnergy, left(t.dietary_time,10) as dietaryTime");
        sql.append(" from rgm_dietary_log t ");
        sql.append(" inner join rgm_food rf on t.food_id = rf.id");
        sql.append(" where 1=1");
        String creator = (String) filter.get("createBy");
        sql.append(" and t.user_id = ?");
        params.add(creator, Hibernate.STRING);
        String beginTime = (String) filter.get("beginTime");
        if (beginTime != null && beginTime.length() > 0) {
            sql.append(" and t.dietary_time >= '").append(beginTime)
                    .append("'");
        }
        String endTime = (String) filter.get("endTime");
        if (endTime != null && endTime.length() > 0) {
            sql.append(" and t.dietary_time < adddate('").append(endTime)
                    .append("', interval 1 day)");
        }
        sql.append(" group by left(t.dietary_time,10) ");
        sql.append(" order by dietaryTime desc");
        // 取数据条数
        StringBuilder totalCountsql = new StringBuilder();
        totalCountsql.append("select count(*) as c from rgm_dietary_log t where t.create_by = ? ");
        if (beginTime != null && beginTime.length() > 0)
        {
            totalCountsql.append(" and t.dietary_time >= '").append(beginTime).append("'");
        }
        if (endTime != null && endTime.length() > 0)
        {
            totalCountsql.append(" and t.dietary_time < adddate('").append(endTime).append("', interval 1 day)");
        }
        totalCountsql.append(" group by left(t.dietary_time,10) ");
        List<Object[]> total = super.getSession().createSQLQuery(totalCountsql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).list();
        Integer totalCount = total.size();
        // 查询数据
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("intakeEnergy", Hibernate.DOUBLE)
                .addScalar("dietaryTime", Hibernate.TIMESTAMP).setParameters(params.getVals(), params.getTypes())
                .setFirstResult(pagesize * (page - 1)).setMaxResults(pagesize).list();
        List<DietaryLog> list = new ArrayList<DietaryLog>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            DietaryLog dietaryLog = new DietaryLog();
            idx = 0;
            dietaryLog.setIntakeEnergy((Double) objs[idx]);
            idx++;
            dietaryLog.setDietaryTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            list.add(dietaryLog);
        }
        pageModel.setTotal(totalCount);
        pageModel.setData(list);
        return pageModel;
    }

    @SuppressWarnings("unchecked")
    public List<DietaryLog> getDetailsLog(DietaryLog dietaryLog) throws Exception 
    {
        HibernateParams params= new HibernateParams();
        String sql = " select t.dietary_time, t.dietary_type, t.food_id, t.food_weight, rs.catalog_id, rs.name_, (t.food_weight*rs.food_heat)/1000 as intakeEnergy from rgm_dietary_log t inner join rgm_food rs on rs.id = t.food_id" +
        		" where t.user_id = ? and t.dietary_time = ? order by rs.name_ desc";
        params.add(dietaryLog.getUserId(), Hibernate.STRING);
        params.add(dietaryLog.getDietaryTime(), Hibernate.TIMESTAMP);
        // 查询数据*/
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("dietary_time", Hibernate.TIMESTAMP)
                .addScalar("dietary_type", Hibernate.INTEGER).addScalar("food_id", Hibernate.STRING)
                .addScalar("food_weight", Hibernate.DOUBLE).addScalar("catalog_id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING)
                .addScalar("intakeEnergy", Hibernate.DOUBLE)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<DietaryLog> list = new ArrayList<DietaryLog>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            DietaryLog Log = new DietaryLog();
            idx = 0;
            Log.setDietaryTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            Log.setDietaryType((Integer) objs[idx]);
            idx++;
            Log.setFoodId((String) objs[idx]);
            idx++;
            Log.setFoodWeight((Double) objs[idx]);
            idx++;
            Log.setFoodCatalogId((String) objs[idx]);
            idx++;
            Log.setName((String) objs[idx]);
            idx++;
            Log.setIntakeEnergy((Double) objs[idx]);
            idx++;
            list.add(Log);
        }
        return list;
    }

    
    /*添加运动日志后的总能量
     * @author huangGang
     * 2014.10.11
     * */
    @SuppressWarnings("unchecked")
    public DietaryLog getLogByDay(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select  sum((t.food_weight*rf.food_heat)) as intakeEnergy ");
        sql.append(" from rgm_dietary_log t ");
        sql.append(" inner join rgm_food rf on t.food_id = rf.id");
        sql.append(" where 1=1");
        String creator = (String) filter.get("createBy");
        sql.append(" and t.user_id = ?");
        params.add(creator, Hibernate.STRING);
        Timestamp sysTime = (Timestamp) filter.get("sysTime");
        if (sysTime != null)
        {
            sql.append(" and t.dietary_time = ?");
            params.add(sysTime, Hibernate.TIMESTAMP);
        }
        sql.append(" group by left(t.dietary_time,10) ");
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("intakeEnergy", Hibernate.DOUBLE)
                .setParameters(params.getVals(), params.getTypes()).list();
        //说明，此处是重新将Object赋值 应该还有问题
        List<Object> queNums = new ArrayList<Object>(objsList);
        DietaryLog dietaryLog = null;
        int idx = 0;
        if (objsList.size() > 0)
        {
            Object objs = queNums.get(0);
            dietaryLog = new DietaryLog();
            idx = 0;
            dietaryLog.setIntakeEnergy((Double) objs);
        }
        return dietaryLog;
    }

    public void deleteDietaryLogs(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        /*StringBuilder sql = new StringBuilder();*/
        String createBy = (String) filter.get("createBy");
        Date dietaryTime = (Date) filter.get("intakeTime");
        StringBuilder sql = new StringBuilder();
        sql.append("delete from DietaryLog t where  t.dietaryTime = ? and t.userId = ?");
        /*String sql = "delete from sportLog t where  t.sport_time = ?";*/
        /*sql.append("delete from rgm_sport_log t where  t.create_by = ? and t.sport_time = ?") ;*/
        params.add(dietaryTime, Hibernate.DATE);
        params.add(createBy, Hibernate.STRING);
        super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
        .executeUpdate();
    }

    public void deleteDietaryLog(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        String createBy = (String) filter.get("createBy");
        String id = (String) filter.get("foodLogId");
        StringBuilder sql = new StringBuilder();
        sql.append("delete from DietaryLog t where  t.id = ? and t.userId = ?");
        params.add(id, Hibernate.STRING);
        params.add(createBy, Hibernate.STRING);
        super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes()).executeUpdate();
    }

}
