package com.yzxt.yh.module.chk.dao;

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
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.ach.bean.Dossier;
import com.yzxt.yh.module.chk.bean.PressurePulse;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

public class PressurePulseDao extends HibernateSupport<PressurePulse> implements BaseDao<PressurePulse>
{
    /**
     * 插入血压脉搏信息
     */
    public String insert(PressurePulse bean) throws Exception
    {
        super.insert(bean);
        return bean.getId();
    }
    /**
     * 查询血压脉率列表
     * @param filter 过滤条件
     * @param sysTime 同步时间点
     * @param dir 同步方式，大于0：取时间点后的数据，小于0取时间点前的数据
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageTran<PressurePulse> queryTra(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from chk_pressure_pulse t");
		sql.append(" inner join sys_user tu where tu.id = t.cust_id");
		//String sql = "select t.id,t.cust_id,t.d_b_p,t.s_b_p,t.pulse,tu.name_ from chk_pressure_pulse t inner join sys_user tu on tu.id = t.cust_id and t.cust_id in(select member_id from sys_cust_family_audit s where s.apply_cust_id =?) order by t.create_time desc"
        // 查询条件
        String userId = (String) filter.get("userId");
        if (StringUtil.isNotEmpty(userId))
        {
        	sql.append(" and t.cust_id in(select member_id from sys_cust_family_audit s where s.apply_cust_id =?)");
            params.add(userId, Hibernate.STRING);
        }
        else
        {
            return new PageTran<PressurePulse>();
        }
        sql.append(" order by t.create_time desc");
        //String sql ="select t.id,t.cust_id,t.d_b_p,t.s_b_p,t.pulse,tu.name_ from chk_pressure_pulse t inner join sys_user tu on tu.id = t.cust_id where 1=1 and t.cust_id in(select member_id from sys_cust_family_audit s where s.apply_cust_id =?) order by t.create_time desc";
     /*   String sql ="select t.id,t.cust_id,t.d_b_p,t.s_b_p,t.pulse,tu.name_ from chk_pressure_pulse t inner join sys_user tu where tu.id = t.cust_id";
        String userId = (String) filter.get("userId");
        params.add(userId, Hibernate.STRING);*/
        /* // 同步方式
        if (sysTime == null)
        {
            sql.append(" order by t.createTime desc");
        }
        else if (dir < 0)
        {
            if (dir < 0)
            {
                sql.append(" and t.createTime < ? order by t.createTime desc");
            }
            elsew
            {
                sql.append(" and t.createTime > ? order by t.createTime asc");
            }
            params.add(sysTime, Hibernate.TIMESTAMP);
        }*/
        List<PressurePulse> list = super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).setMaxResults(count).list();
        return new PageTran<PressurePulse>(list);
    }
   
    @SuppressWarnings("unchecked")
    public PageTran<PressurePulse> queryTran(Map<String, Object> filter,Timestamp sysTime, int dir, int count)
    {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append("select t.id,rp.id as reportId,t.cust_id,t.d_b_p,t.s_b_p,t.pulse,tu.name_ ,t.check_time,t.descript,t.type,s.memo from chk_pressure_pulse t");
		sql.append(" inner join sys_user tu on tu.id = t.cust_id");
		sql.append(" inner join sys_customer s on tu.id = s.user_id");
		sql.append(" inner join rpt_analysis_report rp on rp.cust_id = tu.id");
		sql.append( " where 1=1");
		String userId = (String) filter.get("userId");
		// 查询条件
		if (StringUtil.isNotEmpty(userId))
		{
			sql.append(" and t.cust_id in(select s.member_id from sys_cust_family_audit s where s.apply_cust_id =?)");
		    params.add(userId, Hibernate.STRING);
		    sql.append(" GROUP BY t.id");
		}
		/*if(StringUtil.isNotEmpty(custId)){
			sql.append(" and sys_cust_family_audit.member_id=?");
			 params.add(custId, Hibernate.STRING);
		}*/
		/*else
		{
		    return new PageTran<PressurePulse>();
		}*/
		
		// 同步方式
		if (sysTime == null)
		{
		    sql.append(" order by t.create_time desc");
		}
		else if (dir < 0)
		{
		    if (dir < 0)
		    {
		        sql.append(" and t.create_time < ? order by t.create_time desc");
		    }
		    else
		    {
		        sql.append(" and t.create_time > ? order by t.create_time asc");
		    }
		    params.add(sysTime, Hibernate.TIMESTAMP);
		}
		List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
				.addScalar("reportId", Hibernate.STRING)
                .addScalar("d_b_p", Hibernate.INTEGER).addScalar("s_b_p", Hibernate.INTEGER)
                .addScalar("pulse", Hibernate.INTEGER).addScalar("name_", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("check_time", Hibernate.TIMESTAMP)
                .addScalar("descript", Hibernate.STRING).addScalar("type", Hibernate.INTEGER)
                .addScalar("memo", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<PressurePulse> list = new ArrayList<PressurePulse>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            PressurePulse pp = new PressurePulse();
            idx = 0;
            pp.setId((String) objs[idx]);
            idx++;
            pp.setReportId((String) objs[idx]);
            idx++;
            pp.setDBP((Integer) objs[idx]);
            idx++;
            pp.setSBP((Integer) objs[idx]);
            idx++;
            pp.setPulse((Integer) objs[idx]);
            idx++;
            pp.setUserName((String) objs[idx]);
            idx++;
            pp.setCustId((String) objs[idx]);
            idx++;
            pp.setCheckTime((Timestamp) objs[idx]);
            idx++;
            pp.setDescript((String) objs[idx]);
            idx++;
            pp.setType((Integer) objs[idx]);
            idx++;
            pp.setMemo((String) objs[idx]);
            idx++;
            list.add(pp);
        }
        return new PageTran<PressurePulse>(list);
    }
    
    /**
     * 查询个人血压记录
     * @param filter
     * @param sysTime
     * @param dir
     * @param count
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageTran<PressurePulse> queryMember(Map<String, Object> filter,Timestamp sysTime, int dir, int count)
    {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append("select t.id,rp.id as reportId,t.cust_id,t.d_b_p,t.s_b_p,t.pulse,tu.name_ ,t.check_time,t.descript,t.type,s.memo from chk_pressure_pulse t");
		sql.append(" inner join sys_user tu on tu.id = t.cust_id");
		sql.append(" inner join sys_customer s on tu.id = s.user_id");
		sql.append(" inner join rpt_analysis_report rp on rp.cust_id = tu.id");
		sql.append( " where 1=1");
		String custId = (String) filter.get("custId");
		// 查询条件
		if(StringUtil.isNotEmpty(custId)){
			sql.append(" and t.cust_id=?");
			 params.add(custId, Hibernate.STRING);
			 sql.append(" GROUP BY t.id");
		}
		
		else
		{
		    return new PageTran<PressurePulse>();
		}
		
		// 同步方式
		if (sysTime == null)
		{
		    sql.append(" order by t.create_time desc");
		}
		else if (dir < 0)
		{
		    if (dir < 0)
		    {
		        sql.append(" and t.create_time < ? order by t.create_time desc");
		    }
		    else
		    {
		        sql.append(" and t.create_time > ? order by t.create_time asc");
		    }
		    params.add(sysTime, Hibernate.TIMESTAMP);
		}
		List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
				.addScalar("reportId", Hibernate.STRING)
                .addScalar("d_b_p", Hibernate.INTEGER).addScalar("s_b_p", Hibernate.INTEGER)
                .addScalar("pulse", Hibernate.INTEGER).addScalar("name_", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("check_time", Hibernate.TIMESTAMP)
                .addScalar("descript", Hibernate.STRING).addScalar("type", Hibernate.INTEGER)
                .addScalar("memo", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<PressurePulse> list = new ArrayList<PressurePulse>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            PressurePulse pp = new PressurePulse();
            idx = 0;
            pp.setId((String) objs[idx]);
            idx++;
            pp.setReportId((String) objs[idx]);
            idx++;
            pp.setDBP((Integer) objs[idx]);
            idx++;
            pp.setSBP((Integer) objs[idx]);
            idx++;
            pp.setPulse((Integer) objs[idx]);
            idx++;
            pp.setUserName((String) objs[idx]);
            idx++;
            pp.setCustId((String) objs[idx]);
            idx++;
            pp.setCheckTime((Timestamp) objs[idx]);
            idx++;
            pp.setDescript((String) objs[idx]);
            idx++;
            pp.setType((Integer) objs[idx]);
            idx++;
            pp.setMemo((String) objs[idx]);
            idx++;
            list.add(pp);
        }
        return new PageTran<PressurePulse>(list);
    }
    /**
     * 查询血压脉率列表
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageModel<PressurePulse> query(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<PressurePulse> pageModel = new PageModel<PressurePulse>();
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from chk_pressure_pulse t");
        // mSql.append(" left join chk_check_warn tcw on tcw.id = t.id");
        mSql.append(" where 1=1");
        // 查询条件
        Date startDate = (Date) filter.get("startDate");
        if (startDate != null)
        {
            mSql.append(" and t.check_time >= ?");
            params.add(startDate, Hibernate.DATE);
        }
        Date endDate = (Date) filter.get("endDate");
        if (endDate != null)
        {
            mSql.append(" and t.check_time < ?");
            params.add(DateUtil.addDay(endDate, 1), Hibernate.DATE);
        }
        String custId = (String) filter.get("custId");
        if (StringUtil.isNotEmpty(custId))
        {
            mSql.append(" and t.cust_id = ? and t.type = 2");
            params.add(custId, Hibernate.STRING);
        }
        else
        {
            // 操作人
            User user = (User) filter.get("operUser");
            // 权限
            if (Constant.USER_TYPE_CUSTOMER.equals(user.getType()))
            {
                mSql.append(" and t.cust_id = ? ");
                params.add(user.getId(), Hibernate.STRING);
            }
            else if (Constant.USER_TYPE_DOCTOR.equals(user.getType()))
            // 医生查询自己的会员记录
            {
                mSql.append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = t.cust_id and smi.doctor_id = ? and smi.state = 1)");
                params.add(user.getId(), Hibernate.STRING);
            }
            else
            // 其它非管理员用户不能查询到记录
            {
                return pageModel;
            }
        }
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(t.id) as c ").append(mSql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.cust_id,t.d_b_p, t.s_b_p, t.pulse, t.check_time");
        pSql.append(", t.level, t.descript");
        pSql.append(mSql);
        pSql.append(" order by t.check_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
        		.addScalar("cust_id", Hibernate.STRING)
                .addScalar("d_b_p", Hibernate.INTEGER).addScalar("s_b_p", Hibernate.INTEGER)
                .addScalar("pulse", Hibernate.INTEGER).addScalar("check_time", Hibernate.TIMESTAMP)
                .addScalar("level", Hibernate.INTEGER).addScalar("descript", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<PressurePulse> list = new ArrayList<PressurePulse>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            PressurePulse pp = new PressurePulse();
            idx = 0;
            pp.setId((String) objs[idx]);
            idx++;
            pp.setCustId((String) objs[idx]);
            idx++;
            pp.setDBP((Integer) objs[idx]);
            idx++;
            pp.setSBP((Integer) objs[idx]);
            idx++;
            pp.setPulse((Integer) objs[idx]);
            idx++;
            pp.setCheckTime((Timestamp) objs[idx]);
            idx++;
            pp.setLevel((Integer) objs[idx]);
            idx++;
            pp.setDescript((String) objs[idx]);
            idx++;
            list.add(pp);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 血压检测记录数
     * 
     * @param filter
     * @return
     * @throws Exception
     */
    public int queryDataCount(Map<String, Object> filter) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(t.id) as c from PressurePulse t where 1=1");
        String custId = (String) filter.get("custId");
        if (StringUtil.isNotEmpty(custId))
        {
            sql.append(" and t.custId = ?");
            params.add(custId, Hibernate.STRING);
        }
        Date startDate = (Date) filter.get("startDate");
        if (startDate != null)
        {
            sql.append(" and t.checkTime >= ?");
            params.add(startDate, Hibernate.DATE);
        }
        Date endDate = (Date) filter.get("endDate");
        if (endDate != null)
        {
            sql.append(" and t.checkTime < ?");
            // 由于没有时间部分补上时间
            params.add(DateUtil.addDay(endDate, 1), Hibernate.DATE);
        }
        Long c = (Long) super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).uniqueResult();
        return c.intValue();
    }

    /**
     * 获取健康分析报告的数据
     * @param custId
     * @param startDate
     * @param endDate
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object[] getAnaRptData(String custId, Date startDate, Date endDate)
    {
        endDate = DateUtil.addDay(endDate, 1);
        // 曲线图和散点图
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.d_b_p, t.s_b_p, t.check_time,t.pulse,t.id from chk_pressure_pulse t where 1=1");
        sql.append(" and t.cust_id = ? and t.check_time >= ? ");
        sql.append(" order by t.check_time desc");
        params.add(custId, Hibernate.STRING);
        params.add(startDate, Hibernate.DATE);
        //params.add(endDate, Hibernate.DATE);
        List<Object[]> list = super.getSession().createSQLQuery(sql.toString()).addScalar("d_b_p", Hibernate.INTEGER)
                .addScalar("s_b_p", Hibernate.INTEGER).addScalar("check_time", Hibernate.TIMESTAMP)
                .addScalar("pulse", Hibernate.INTEGER).addScalar("id", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        // 每天时间段统计
        Integer[][] tStat = new Integer[4][];
        tStat[0] = getAnaRptTimeStat(custId, startDate, startDate, "06:00:00", "10:00:00", false);
        tStat[1] = getAnaRptTimeStat(custId, startDate, startDate, "10:00:00", "19:00:00", false);
        tStat[2] = getAnaRptTimeStat(custId, startDate, startDate, "19:00:00", "06:00:00", true);
        tStat[3] = getAnaRptTimeStat(custId, startDate, startDate, null, null, false);
        return new Object[]
        {list, tStat};
    }

    /**
     * 分区间统计
     * @param custId
     * @param startDate
     * @param endDate
     * @return
     */
    @SuppressWarnings("unchecked")
    private Integer[] getAnaRptTimeStat(String custId, Date startDate, Date endDate, String timeStart, String timeEnd,
            boolean crossDay)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(t.id) as c, min(t.d_b_p) as mind, max(t.d_b_p) as maxd, avg(t.d_b_p) as avgd");
        sql.append(", min(t.s_b_p) as mins, max(t.s_b_p) as maxs, avg(t.s_b_p) as avgs");
        sql.append(" from chk_pressure_pulse t where 1=1");
        if (crossDay && StringUtil.isNotEmpty(timeStart) && StringUtil.isNotEmpty(timeEnd))
        {
            sql.append(" and (date_format(t.check_time,'%T') >= ? or date_format(t.check_time,'%T') < ?)");
            params.add(timeStart, Hibernate.STRING);
            params.add(timeEnd, Hibernate.STRING);
        }
        else
        {
            if (StringUtil.isNotEmpty(timeStart))
            {
                sql.append(" and date_format(t.check_time,'%T') >= ?");
                params.add(timeStart, Hibernate.STRING);
            }
            if (StringUtil.isNotEmpty(timeEnd))
            {
                sql.append(" and date_format(t.check_time,'%T') < ?");
                params.add(timeEnd, Hibernate.STRING);
            }
        }
        sql.append(" and t.cust_id = ? and t.check_time >= ?");
        params.add(custId, Hibernate.STRING);
        params.add(startDate, Hibernate.DATE);
       // params.add(endDate, Hibernate.DATE);
        List<Object[]> list = super.getSession().createSQLQuery(sql.toString()).addScalar("c", Hibernate.INTEGER)
                .addScalar("mind", Hibernate.INTEGER).addScalar("maxd", Hibernate.INTEGER)
                .addScalar("avgd", Hibernate.INTEGER).addScalar("mins", Hibernate.INTEGER)
                .addScalar("maxs", Hibernate.INTEGER).addScalar("avgs", Hibernate.INTEGER)
                .setParameters(params.getVals(), params.getTypes()).list();
        Object[] objs = list.get(0);
        return new Integer[]
        {(Integer) objs[0], (Integer) objs[1], (Integer) objs[2], (Integer) objs[3], (Integer) objs[4],
                (Integer) objs[5], (Integer) objs[6]};
    }
    /**
     * 获取个人的血压记录
     * @param custId
     * @return
     */
	public List<PressurePulse> queryByCustId(String custId,String id) {
		HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select * from chk_pressure_pulse where cust_id=? and id=?");
        params.add(custId, Hibernate.STRING);
        params.add(id, Hibernate.STRING);
    	List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("type", Hibernate.INTEGER).addScalar("state", Hibernate.INTEGER)
                .addScalar("cust_id", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
    	  List<PressurePulse> list = new ArrayList<PressurePulse>();
          int idx = 0;
          for (Object[] objs : objsList)
          {
              PressurePulse pp = new PressurePulse();
              idx = 0;
              pp.setId((String) objs[idx]);
              idx++;
              pp.setType((Integer) objs[idx]);
              idx++;
              pp.setState((Integer) objs[idx]);
              idx++;
              pp.setCustId((String) objs[idx]);
              idx++;
              list.add(pp);
          }
          return list;
	}
	
	 /**
     * 根据Id获取个人的血压记录
     * @param custId
     * @return
     */
	public List<PressurePulse> queryById(String id) {
		HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select * from chk_pressure_pulse where id=?");
        params.add(id, Hibernate.STRING);
    	List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("type", Hibernate.INTEGER).addScalar("cust_id", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
    	  List<PressurePulse> list = new ArrayList<PressurePulse>();
          int idx = 0;
          for (Object[] objs : objsList)
          {
              PressurePulse pp = new PressurePulse();
              idx = 0;
              pp.setId((String) objs[idx]);
              idx++;
              pp.setType((Integer) objs[idx]);
              idx++;
              pp.setCustId((String) objs[idx]);
              idx++;
              list.add(pp);
          }
          return list;
	}
	public int updatePre(PressurePulse pre) {
		HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" update PressurePulse t set");
        sql.append(" t.type = ?");
        sql.append(" where t.custId = ? and t.id = ?");
        params.add(pre.getType(), Hibernate.INTEGER);
        params.add(pre.getCustId(), Hibernate.STRING);
        params.add(pre.getId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
		
	}
	public int updatePres(PressurePulse pre) {
		HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" update PressurePulse t set");
        sql.append(" t.type = ?");
        sql.append(" where t.custId = ? and id = ?");
        params.add(pre.getType(), Hibernate.INTEGER);
        params.add(pre.getCustId(), Hibernate.STRING);
        params.add(pre.getId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
		
	}

}
