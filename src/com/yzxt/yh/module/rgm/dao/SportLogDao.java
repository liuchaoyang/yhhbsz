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
import com.yzxt.yh.module.rgm.bean.SportLog;



public class SportLogDao extends HibernateSupport<SportLog> implements BaseDao<SportLog>
{
    /*
     * 添加健康日志
     * */
    public String addSportLog(SportLog sportLog) throws Exception
    {
        super.save(sportLog);
        return sportLog.getId();
    }

    /*
     * 得到健康日志总列表
     * */
    @SuppressWarnings("unchecked")
    public List<SportLog> getList(Map<String, Object> filter) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select su.name_, sum(t.time_span), sum((t.time_span*rs.sport_heat))/60 as dayConsumeEnergy, left(t.sport_time,10) as createTime");
        sql.append(" from rgm_sport_log t ");
        sql.append(" inner join rgm_sport rs on t.sport_id = rs.id");
        sql.append(" inner join sys_user su on su.id = t.user_id");
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
            sql.append(" and t.sport_time < ?");
            params.add(sysTime, Hibernate.TIMESTAMP);
        }
        /*if (sysTime != null)
        {
            sql.append(" and t.update_time < ?");
            params.add(sysTime, Hibernate.TIMESTAMP);
        }*/
        // 取最新发布倒序
        //根据时间天数分组 得到一天的总能量和总时长
        sql.append(" group by left(t.sport_time,10) ");
        sql.append(" order by createTime desc");
        // 取数据条数
        sql.append(" limit 0, ?");
        params.add(filter.get("maxSize"), Hibernate.INTEGER);
        // 查询数据

        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("name_", Hibernate.STRING)
                .addScalar("sum(t.time_span)", Hibernate.INTEGER).addScalar("dayConsumeEnergy", Hibernate.DOUBLE)
                .addScalar("createTime", Hibernate.TIMESTAMP).setParameters(params.getVals(), params.getTypes()).list();
        List<SportLog> list = new ArrayList<SportLog>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            SportLog sportLog = new SportLog();
            idx = 0;
            sportLog.setCustName((String) objs[idx]);
            idx++;
            sportLog.setTimeSpan((Integer) objs[idx]);
            idx++;
            sportLog.setDayConsumeEnergy((Double) objs[idx]);
            idx++;
            sportLog.setSportTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            list.add(sportLog);
        }
        return list;
    }

    /**
     * 根据时间段来查询运动日志
     * @author h
     * 客户端查询
     */
    @SuppressWarnings("unchecked")
    public List<SportLog> querySportLog(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(t.time_span), sum(t.sport_energy), left(t.create_time,10) as createTime");
        sql.append(" from rgm_sport_log t ");
        sql.append(" where 1=1");
        String creator = (String) filter.get("createBy");
        sql.append(" and t.create_by = ?");
        params.add(creator, Hibernate.STRING);
        Timestamp beginDate = (Timestamp) filter.get("beginDate");
        if (beginDate != null)
        {
            sql.append(" and t.sport_time >= ?");
            params.add(beginDate, Hibernate.TIMESTAMP);
        }
        Timestamp endDate = (Timestamp) filter.get("endDate");
        if (endDate != null)
        {
            sql.append(" and t.sport_time < adddate(?, interval 1 day)");
            params.add(endDate, Hibernate.TIMESTAMP);
        }
        sql.append(" group by left(t.sport_time,10) ");
        sql.append(" order by t.sport_span desc");
        // 取数据条数
        sql.append(" limit 0, ?");
        int maxsize = (Integer) filter.get("maxSize");
        params.add(maxsize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("sum(t.time_span)", Hibernate.INTEGER).addScalar("sum(t.sport_energy)", Hibernate.DOUBLE)
                .addScalar("createTime", Hibernate.TIMESTAMP).setParameters(params.getVals(), params.getTypes()).list();
        List<SportLog> list = new ArrayList<SportLog>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            SportLog sportLog = new SportLog();
            idx = 0;
            sportLog.setId((String) objs[idx]);
            idx++;
            /*sportLog.setSportType((Integer) objs[idx]);
            idx++;*/
            sportLog.setSportId((String) objs[idx]);
            idx++;
            sportLog.setSportTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            sportLog.setTimeSpan((Integer) objs[idx]);/*(((BigDecimal) objs[idx]).doubleValue());*/
            idx++;
            sportLog.setCreateBy((String) objs[idx]);/*(objs[idx] != null ? (Date) objs[idx] : null);*/
            idx++;
            sportLog.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            sportLog.setUpdateBy((String) objs[idx]);/*(objs[idx] != null ? (Date) objs[idx] : null);*/
            idx++;
            sportLog.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            list.add(sportLog);
        }
        return list;
    }

    /*
     * 得到健康日志详细列表
     * 客户端查询
     * @author h
     * 2016.3.25
     * */
    @SuppressWarnings("unchecked")
    public List<SportLog> getDetailLog(Map<String, Object> filter) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, rs.sport_type, t.sport_id, t.sport_time, t.time_span, (t.time_span*rs.sport_heat)/60 as dayConsumeEnergy, t.create_time ");
        sql.append(" from rgm_sport_log t ");
        sql.append(" inner join rgm_sport rs on t.sport_id = rs.id");
        sql.append(" where 1=1");
        String creator = (String) filter.get("creator");
        sql.append(" and t.user_id = ?");
        params.add(creator, Hibernate.STRING);
        Date sportTime = (Date) filter.get("sportTime");
        if (sportTime != null)
        {
            sql.append(" and t.sport_time = ?");
            params.add(sportTime, Hibernate.DATE);
        }
        sql.append(" order by t.time_span desc");
        // 取数据条数
        // 查询数据
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("sport_type", Hibernate.INTEGER).addScalar("sport_id", Hibernate.STRING)
                .addScalar("sport_time", Hibernate.TIMESTAMP).addScalar("time_span", Hibernate.INTEGER)
                .addScalar("dayConsumeEnergy", Hibernate.DOUBLE).addScalar("create_time", Hibernate.TIMESTAMP)
                .setParameters(params.getVals(), params.getTypes()).list();
        System.out.println(sql);
        List<SportLog> list = new ArrayList<SportLog>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            SportLog sportLog = new SportLog();
            idx = 0;
            sportLog.setId((String) objs[idx]);
            idx++;
            sportLog.setSportType((Integer) objs[idx]);
            idx++;
            sportLog.setSportId((String) objs[idx]);
            idx++;
            sportLog.setSportTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            sportLog.setTimeSpan((Integer) objs[idx]);
            idx++;
            sportLog.setDayConsumeEnergy((Double) objs[idx]);
            idx++;
            sportLog.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            list.add(sportLog);
        }
        return list;
    }

    /*
     * 用于服务端查询
     * @author huanggang
     * 2014.9.29
     * */
    @SuppressWarnings("unchecked")
    public PageModel<SportLog> getSportLogList(Map<String, Object> filter, int page, int pagesize) throws Exception
    {
        PageModel<SportLog> pageModel = new PageModel<SportLog>();
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(t.time_span), sum((t.time_span*rs.sport_heat))/60 as dayConsumeEnergy, left(t.sport_time,10) as sportTime");
        sql.append(" from rgm_sport_log t ");
        sql.append(" inner join rgm_sport rs on t.sport_id = rs.id");
        sql.append(" where 1=1");
        String creator = (String) filter.get("createBy");
        sql.append(" and t.user_id = ?");
        params.add(creator, Hibernate.STRING);
        String beginTime = (String) filter.get("beginTime");
        if (beginTime != null && beginTime.length() > 0) {
            sql.append(" and t.sport_time >= '").append(beginTime)
                    .append("'");
        }
        String endTime = (String) filter.get("endTime");
        if (endTime != null && endTime.length() > 0) {
            sql.append(" and t.sport_time < adddate('").append(endTime)
                    .append("', interval 1 day)");
        }
        sql.append(" group by left(t.sport_time,10) ");
        sql.append(" order by sportTime desc");
        // 取数据条数
        StringBuilder totalCountsql = new StringBuilder();
        totalCountsql.append("select count(*) as c from rgm_sport_log t where t.create_by = ? ");
        if (beginTime != null && beginTime.length() > 0)
        {
            totalCountsql.append(" and t.sport_time >= '").append(beginTime).append("'");
        }
        if (endTime != null && endTime.length() > 0)
        {
            totalCountsql.append(" and t.sport_time < adddate('").append(endTime).append("', interval 1 day)");
        }
        totalCountsql.append(" group by left(t.sport_time,10) ");
        List<Object[]> total = super.getSession().createSQLQuery(totalCountsql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).list();
        Integer totalCount = total.size();
        // 查询数据
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("sum(t.time_span)", Hibernate.INTEGER).addScalar("dayConsumeEnergy", Hibernate.DOUBLE)
                .addScalar("sportTime", Hibernate.TIMESTAMP).setParameters(params.getVals(), params.getTypes())
                .setFirstResult(pagesize * (page - 1)).setMaxResults(pagesize).list();
        List<SportLog> list = new ArrayList<SportLog>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            SportLog sportLog = new SportLog();
            idx = 0;
            sportLog.setTimeSpan((Integer) objs[idx]);
            idx++;
            sportLog.setDayConsumeEnergy((Double) objs[idx]);
            idx++;
            sportLog.setSportTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            list.add(sportLog);
        }
        pageModel.setTotal(totalCount);
        pageModel.setData(list);
        return pageModel;
    }

    @SuppressWarnings("unchecked")
    public List<SportLog> getDetailsLog(SportLog sportLog) throws Exception 
    {
        HibernateParams params= new HibernateParams();
        String sql = " select t.sport_time, rs.sport_type, t.sport_id, rs.name_, t.time_span, (t.time_span*rs.sport_heat)/60 as dayConsumeEnergy from rgm_sport_log t inner join rgm_sport rs on rs.id = t.sport_id" +
        		" where t.user_id = ? and t.sport_time = ? order by t.time_span desc";
        params.add(sportLog.getUserId(), Hibernate.STRING);
        params.add(sportLog.getSportTime(), Hibernate.TIMESTAMP);
     /*   List<SportLog> list = super.getSession().createSQLQuery(sql)
                .setParameters(params.getVals(), params.getTypes()).list();*/
       /* StringBuilder sql= new StringBuilder();
        sql.append("select t.id, t.sport_type, t.sport_id, t.sport_time, t.time_span, t.sport_energy, t.create_time ");
        sql.append(" from rgm_sport_log t ");
        sql.append(" where 1=1");
        String createBy = sportLog.getCreateBy();
        sql.append(" and t.create_by = ?");
        params.add(createBy, Hibernate.STRING);
        Timestamp sportTime = sportLog.getSportTime();
        sql.append(" and t.sport_time = ?");
        params.add(sportTime, Hibernate.STRING);
        sql.append(" order by t.time_span desc");
        // 查询数据*/
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("sport_time", Hibernate.TIMESTAMP)
                .addScalar("sport_type", Hibernate.INTEGER).addScalar("sport_id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING)
                .addScalar("time_span", Hibernate.INTEGER)
                .addScalar("dayConsumeEnergy", Hibernate.DOUBLE)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<SportLog> list = new ArrayList<SportLog>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            SportLog Log = new SportLog();
            idx = 0;
            Log.setSportTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            Log.setSportType((Integer) objs[idx]);
            idx++;
            Log.setSportId((String) objs[idx]);
            idx++;
            Log.setSportName((String) objs[idx]);
            idx++;
            Log.setTimeSpan((Integer) objs[idx]);
            idx++;
            Log.setDayConsumeEnergy((Double) objs[idx]);
            idx++;
            list.add(Log);
        }
        return list;
    }

    //添加运动日志后的总能量和时长
    @SuppressWarnings("unchecked")
    public SportLog getLogByDay(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(t.time_span), sum((t.time_span*rs.sport_heat))/60 as dayConsumeEnergy ");
        sql.append(" from rgm_sport_log t ");
        sql.append(" inner join rgm_sport rs on t.sport_id = rs.id");
        sql.append(" where 1=1");
        String creator = (String) filter.get("createBy");
        sql.append(" and t.user_id = ?");
        params.add(creator, Hibernate.STRING);
        Timestamp sysTime = (Timestamp) filter.get("sysTime");
        if (sysTime != null)
        {
            sql.append(" and t.sport_time = ?");
            params.add(sysTime, Hibernate.TIMESTAMP);
        }
        sql.append(" group by left(t.sport_time,10) ");
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("sum(t.time_span)", Hibernate.INTEGER).addScalar("dayConsumeEnergy", Hibernate.DOUBLE)
                .setParameters(params.getVals(), params.getTypes()).list();
        SportLog sportLog = null;
        int idx = 0;
        if (objsList.size() > 0)
        {
            Object[] objs = objsList.get(0);
            sportLog = new SportLog();
            idx = 0;
            sportLog.setTimeSpan((Integer) objs[idx]);
            idx++;
            sportLog.setDayConsumeEnergy((Double) objs[idx]);
            idx++;
        }
        return sportLog;
    }

    /**
     * 删除一天运动日志
     * @author h
     * 客户端删除
     */
    public void deleteSportLogs(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        /*StringBuilder sql = new StringBuilder();*/
        String createBy = (String) filter.get("createBy");
        Date sportTime = (Date) filter.get("sportTime");
        StringBuilder sql = new StringBuilder();
        sql.append("delete from SportLog t where  t.sportTime = ? and t.userId = ?");
        /*String sql = "delete from sportLog t where  t.sport_time = ?";*/
        /*sql.append("delete from rgm_sport_log t where  t.create_by = ? and t.sport_time = ?") ;*/
        params.add(sportTime, Hibernate.DATE);
        params.add(createBy, Hibernate.STRING);
        super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
        .executeUpdate();
    }

    public void deleteSportLog(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        String createBy = (String) filter.get("createBy");
        String id = (String) filter.get("sportLogId");
        StringBuilder sql = new StringBuilder();
        sql.append("delete from SportLog t where  t.id = ? and t.userId = ?");
        params.add(id, Hibernate.STRING);
        params.add(createBy, Hibernate.STRING);
        super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
        .executeUpdate();
    }

}
