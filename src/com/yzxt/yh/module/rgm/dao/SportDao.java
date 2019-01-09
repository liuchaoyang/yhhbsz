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
import com.yzxt.yh.module.rgm.bean.Sport;



public class SportDao extends HibernateSupport<Sport> implements BaseDao<Sport>
{

 
    /**
     * 插入运动
     * @param 
     * @return 主键ID
     * @throws Exception 
     */
    public String insert(Sport sport) throws Exception
    {
        super.save(sport);
        return sport.getId();
    }
    
    /**
     * 查询是否存在该运动名
     * @param 
     * @return 主键ID
     * @throws Exception 
     */
    public boolean isSportExist(String name)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) as c from Sport s where 1=1");
        sql.append("and lower(s.name) = ?");
        params.add(name.toLowerCase(), Hibernate.STRING);
        Long c = (Long) super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        return c.longValue()>0;
    }

   
    @SuppressWarnings("unchecked")
    public PageModel<Sport> findSportByPage(Map<String,Object> filter,int page,int pageSize) throws Exception
    {
        PageModel<Sport> pageModel = new PageModel<Sport>();
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append("from rgm_sport t where 1=1 and t.state=1 ");
        String keyword = (String) filter.get("keyword");
        keyword = keyword != null ? keyword.trim() : null;
        if (keyword != null && keyword.length() > 0)
        {
            mSql.append(" and (t.name_ like ").append(params.addLikePart(keyword)).append(" or t.type like ")
                    .append(params.addLikePart(keyword)).append(")");
        }
        StringBuilder totalCountsql = new StringBuilder();
        totalCountsql.append("select count(*) as c ").append(mSql);
        Integer totalCount = (Integer) super.getSession().createSQLQuery(totalCountsql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.name_,t.sport_type, t.sport_heat, t.create_time, t.update_time, t.state ").append(mSql).append(" order by t.name_ asc");
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .addScalar("id", Hibernate.STRING).addScalar("name_", Hibernate.STRING)
                .addScalar("sport_type", Hibernate.INTEGER).addScalar("sport_heat", Hibernate.DOUBLE)
                .addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("update_time", Hibernate.TIMESTAMP).addScalar("state", Hibernate.INTEGER)
                .setParameters(params.getVals(), params.getTypes()).setFirstResult(pageSize * (page - 1))
                .setMaxResults(pageSize).list();
        List<Sport> list = new ArrayList<Sport>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Sport  sport= new Sport();
            idx = 0;
            sport.setId((String) objs[idx]);
            idx++;
            sport.setName((String) objs[idx]);
            idx++;
            sport.setSportType((Integer) objs[idx]);
            idx++;
            sport.setSportHeat((Double) objs[idx]);
            idx++;
            sport.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            /*sport.setUpdateBy((String) objs[idx]);
            idx++;*/
            sport.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            sport.setState((Integer) objs[idx]);
            idx++;
            list.add(sport);
        }
        pageModel.setTotal(totalCount);
        pageModel.setData(list);
        return pageModel;
    }
    
    public int update(Sport sport) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update Sport t set t.name_ = ?, t.Type = ?, t.Heat = ?, t.state = ?");
        sql.append(", t.createTime = ?, t.updateTime = ?");
        sql.append(" where t.id = ?");
        params.add(sport.getName(), Hibernate.STRING);
        params.add(sport.getSportType(), Hibernate.INTEGER);
        params.add(sport.getSportHeat(), Hibernate.DOUBLE);
        params.add(sport.getState(), Hibernate.INTEGER);
        /*params.add(sport.getUpdateBy(), Hibernate.STRING);*/
        params.add(sport.getCreateTime(), Hibernate.TIMESTAMP);
        params.add(sport.getUpdateTime(), Hibernate.TIMESTAMP);
        params.add(sport.getId(), Hibernate.STRING);
        int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

    public void updateState(Sport sport)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update Sport t set  t.state = ?, t.updateTime = ?");
        sql.append(" where t.id = ?");
        params.add(sport.getState(), Hibernate.INTEGER);
        /*params.add(sport.getUpdateBy(), Hibernate.STRING);*/
        params.add(sport.getUpdateTime(), Hibernate.TIMESTAMP);
        params.add(sport.getId(), Hibernate.STRING);
        super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<Sport> getSportByName(String Name)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append("from rgm_sport t where 1=1 and t.state=1 ");
        mSql.append(" and t.name_ = ?");
        params.add(Name,  Hibernate.STRING);
       
        List<Object[]> objsList = super.getSession().createSQLQuery(mSql.toString())
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Sport> list = new ArrayList<Sport>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Sport  sport= new Sport();
            idx = 0;
            sport.setId((String) objs[idx]);
            idx++;
            sport.setName((String) objs[idx]);
            idx++;
            sport.setSportType((Integer) objs[idx]);
            idx++;
            sport.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            /*sport.setUpdateBy((String) objs[idx]);
            idx++;*/
            sport.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            sport.setState((Integer) objs[idx]);
            idx++;
            list.add(sport);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<Sport> getSports(String sportId,String sportLevel)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append("select t.id, t.name_,t.sport_type, t.sport_heat, t.create_time, t.update_time, t.state from rgm_sport t where 1=1 and t.state=1 ");
//        mSql.append(" and t.sport_type = ? ");
//        if(sportLevel.equals("1")){
//            params.add(sportType,  Hibernate.INTEGER); 
//        }else{
//            Integer sportType=2
//            params.add(sportType,  Hibernate.INTEGER);
//        }
        System.out.println(sportLevel);
        if(sportLevel!=null){
/*          if(sportLevel.equals("1")||sportLevel.equals("2")){*/
              mSql.append(" and t.sport_type = ? ");
              /*Integer sportType=1;*/
              Integer sportType=Integer.parseInt(sportLevel);
              params.add(sportType,  Hibernate.INTEGER); 
         /* }*/
        }
        List<Object[]> objsList = super.getSession().createSQLQuery(mSql.toString())
                .addScalar("id", Hibernate.STRING).addScalar("name_", Hibernate.STRING)
                .addScalar("sport_type", Hibernate.INTEGER).addScalar("sport_heat", Hibernate.DOUBLE)
                .addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("update_time", Hibernate.TIMESTAMP).addScalar("state", Hibernate.INTEGER)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Sport> list = new ArrayList<Sport>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Sport  sport= new Sport();
            idx = 0;
            sport.setId((String) objs[idx]);
            idx++;
            sport.setName((String) objs[idx]);
            idx++;
            sport.setSportType((Integer) objs[idx]);
            idx++;
            sport.setSportHeat((Double) objs[idx]);
            idx++;
            sport.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            /*sport.setUpdateBy((String) objs[idx]);
            idx++;*/
            sport.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            sport.setState((Integer) objs[idx]);
            idx++;
            list.add(sport);
        }
        return list;
    }
    
    /**
     * 获取子目录
     */
    @SuppressWarnings("unchecked")
    public List<Sport> getChildrenSports(String sportType)
    {
        String sql = "select t from Sport t where t.state = ? and t.sportType = ? order by t.seq asc";
        List<Sport> list = super.getSession().createQuery(sql).setInteger(0, 1)
                .setString(1, sportType).list();
        return list;
    }

    /*
     * 获取所有运动类型
     * @author huangGang
     * 2014.10.15
     * */
    @SuppressWarnings("unchecked")
    public List<Sport> getTypeList(Map<String, Object> filter)
    {
        String sql = "select DISTINCT t.sportType,t.id from Sport t ";
        List<Object[]> objsList = super.getSession().createQuery(sql).list();
        List<Sport> list = new ArrayList<Sport>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Sport  sport= new Sport();
            idx = 0;
            sport.setSportType((Integer) objs[idx]);
            idx++;
            sport.setId((String) objs[idx]);
            idx++;
            list.add(sport);
        }
        return list;
        
    }

    @SuppressWarnings("unchecked")
    public Double getEnergyById(String sportId)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.sport_heat ,t.id");
        sql.append(" from rgm_sport t ");
        sql.append(" where t.id = ?");
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("t.sport_heat", Hibernate.DOUBLE).addScalar("t.id", Hibernate.STRING).setString(0, sportId)
                .list();
        Double sportHeat;
        Sport sport = null;
        int idx = 0;
        if (objsList.size() > 0)
        {
            Object[] objs = objsList.get(0);
            sport = new Sport();
            idx = 0;
            sport.setSportHeat((Double) objs[idx]);
        }
        sportHeat= sport.getSportHeat();
        return sportHeat;
    }

    @SuppressWarnings("unchecked")
    public List<Sport> getSportForClient()
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.name_,t.sport_type,  t.is_aerobic_exercise, t.sport_heat, t.state, t.create_time, t.update_time ");
        sql.append("from rgm_sport t where 1=1 and t.state=1 ");
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("id", Hibernate.STRING).addScalar("name_", Hibernate.STRING)
                .addScalar("sport_type", Hibernate.INTEGER).addScalar("is_aerobic_exercise", Hibernate.INTEGER)
                .addScalar("sport_heat", Hibernate.DOUBLE)
                .addScalar("state", Hibernate.INTEGER)
                .addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("update_time", Hibernate.TIMESTAMP)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Sport> list = new ArrayList<Sport>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Sport  sport= new Sport();
            idx = 0;
            sport.setId((String) objs[idx]);
            idx++;
            sport.setName((String) objs[idx]);
            idx++;
            sport.setSportType((Integer) objs[idx]);
            idx++;
            sport.setIsAerobicExercise((Integer) objs[idx]);
            idx++;
            sport.setSportHeat((Double) objs[idx]);
            idx++;
            sport.setState((Integer) objs[idx]);
            idx++;
            sport.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            sport.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            list.add(sport);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<Sport> getSportInfo(Map<String, Object> filter)
    {
        Integer sportType= (Integer)filter.get("sportType");
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.name_, t.sport_type");
        sql.append(" from rgm_sport t ");
        sql.append(" where t.sport_type = ?");
        params.add(sportType, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("sport_type", Hibernate.INTEGER)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Sport> list = new ArrayList<Sport>();
        System.out.println(sql);
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Sport sport = new Sport();
            idx = 0;
            sport.setId((String) objs[idx]);
            idx++;
            sport.setName((String) objs[idx]);
            idx++;
            sport.setSportType((Integer) objs[idx]);
            idx++;
            list.add(sport);
        }
        return list;
    }
    
}
