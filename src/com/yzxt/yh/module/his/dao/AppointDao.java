package com.yzxt.yh.module.his.dao;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.his.bean.Appoint;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

public class AppointDao extends HibernateSupport<Appoint> implements BaseDao<Appoint> {

	public String insert(Appoint bean) throws Exception {
		super.save(bean);
		return bean.getId();
	}

	/**
	 * 平台查询
	 */
	@SuppressWarnings("unchecked")
	public PageModel<Appoint> query(Map<String, Object> filter, int page, int pageSize) {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append(" from his_appoint t ");
		sql.append(" inner join sys_user tu on tu.id = t.cust_id");
		sql.append(" inner join his_dept hd on hd.id = t.dept_id");
		sql.append(" inner join his_dept hdt on hdt.id = t.depart_id");
		sql.append(" where 1=1 ");
		// 查询条件
		String status = (String) filter.get("status");
		if (StringUtil.isNotEmpty(status)) {
			sql.append(" and t.status = ?");
			Integer state = Integer.parseInt(status);
			params.add(state, Hibernate.INTEGER);
		}
		String beginDate = (String) filter.get("beginDate");
		if (beginDate != null && beginDate.length() > 0) {
			sql.append(" and t.appoint_time >= '").append(beginDate).append("'");
		}
		String endDate = (String) filter.get("endDate");
		if (endDate != null && endDate.length() > 0) {
			sql.append(" and t.appoint_time < adddate('").append(endDate).append("', interval 1 day)");
		}
		String custName = (String) filter.get("custName");
		if (StringUtil.isNotEmpty(custName)) {
			sql.append(" and tu.name_ like ").append(params.addLikePart(custName));
		}
		String deptName = (String) filter.get("deptName");
		if (StringUtil.isNotEmpty(deptName)) {
			sql.append(" and hd.name_ like ").append(params.addLikePart(deptName));
		}
		// 权限
		User user = (User) filter.get("operUser");
		if (Constant.USER_TYPE_DOCTOR.equals(user.getType()) || Constant.USER_TYPE_ADMIN.equals(user.getType())) {
			// 暂查所有记录
			sql.append(" and t.create_by = '").append(user.getId()).append("'");
		} else {
			sql.append(" and t.cust_id = '").append(user.getId()).append("'");
		}
		// 总记录数
		StringBuilder totalCountsql = new StringBuilder();
		totalCountsql.append("select count(*) as c ").append(sql);
		Integer totalCount = (Integer) super.getSession().createSQLQuery(totalCountsql.toString())
				.addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
		// 分页记录
		StringBuilder pSql = new StringBuilder();
		pSql.append("select t.id, t.self_symptom, t.first_visit, t.memo, t.appoint_time ");
		pSql.append(", t.detail_time, t.status, t.result_explain ");
		pSql.append(", tu.name_ as custName, hd.name_ as deptName, hdt.name_ as departname, t.create_time ");
		pSql.append(sql).append(" order by t.create_time desc limit ?, ?");
		params.add(pageSize * (page - 1), Hibernate.INTEGER);
		params.add(pageSize, Hibernate.INTEGER);
		List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
				.addScalar("self_symptom", Hibernate.STRING).addScalar("first_visit", Hibernate.STRING)
				.addScalar("memo", Hibernate.STRING).addScalar("appoint_time", Hibernate.TIMESTAMP)
				.addScalar("detail_time", Hibernate.INTEGER).addScalar("status", Hibernate.INTEGER)
				.addScalar("result_explain", Hibernate.STRING).addScalar("custName", Hibernate.STRING)
				.addScalar("deptName", Hibernate.STRING).addScalar("departname", Hibernate.STRING)
				.addScalar("create_time", Hibernate.TIMESTAMP).setParameters(params.getVals(), params.getTypes())
				.list();
		List<Appoint> list = new ArrayList<Appoint>();
		int idx = 0;
		for (Object[] objs : objsList) {
			Appoint bean = new Appoint();
			idx = 0;
			bean.setId((String) objs[idx]);
			idx++;
			bean.setSelfSymptom((String) objs[idx]);
			idx++;
			bean.setFirstVisit((String) objs[idx]);
			idx++;
			bean.setMemo((String) objs[idx]);
			idx++;
			bean.setAppointTime((Timestamp) objs[idx]);
			idx++;
			bean.setDetailTime((Integer) objs[idx]);
			idx++;
			bean.setStatus((Integer) objs[idx]);
			idx++;
			bean.setResultExplain((String) objs[idx]);
			idx++;
			bean.setCustName((String) objs[idx]);
			idx++;
			bean.setDeptName((String) objs[idx]);
			idx++;
			bean.setDepartName((String) objs[idx]);
			idx++;
			bean.setCreateTime((Timestamp) objs[idx]);
			idx++;
			list.add(bean);
		}
		PageModel<Appoint> pageModel = new PageModel<Appoint>();
		pageModel.setTotal(totalCount);
		pageModel.setData(list);
		return pageModel;
	}

	@SuppressWarnings("unchecked")
	public PageTran<Appoint> queryTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count) {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append("select t.id, t.self_symptom, t.first_visit, t.memo, t.appoint_time ");
		sql.append(", t.detail_time, t.status, t.result_explain ");
		sql.append(", tu.name_ as custName, hd.name_ as deptName, hdt.name_ as departname ");
		sql.append(" from his_appoint t ");
		sql.append(" inner join sys_user tu on tu.id = t.cust_id");
		sql.append(" inner join his_dept hd on hd.id = t.dept_id");
		sql.append(" inner join his_dept hdt on hdt.id = t.depart_id");
		sql.append(" where 1=1 ");
		// 查询条件
		/*
		 * String custId = (String) filter.get("custId"); if
		 * (StringUtil.isNotEmpty(custId)) { sql.append(" and t.cust_id = ?");
		 * params.add(custId, Hibernate.STRING); } else { return new
		 * PageTran<Appoint>(); }
		 */
		// 同步方式
		if (sysTime == null) {
			sql.append(" order by t.create_time desc");
		} else if (dir < 0) {
			if (dir < 0) {
				sql.append(" and t.create_time < ? order by t.create_time desc");
			} else {
				sql.append(" and t.create_time > ? order by t.create_time asc");
			}
			params.add(sysTime, Hibernate.TIMESTAMP);
		}
		List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
				.addScalar("self_symptom", Hibernate.STRING).addScalar("first_visit", Hibernate.STRING)
				.addScalar("memo", Hibernate.STRING).addScalar("appoint_time", Hibernate.TIMESTAMP)
				.addScalar("detail_time", Hibernate.INTEGER).addScalar("status", Hibernate.INTEGER)
				.addScalar("result_explain", Hibernate.STRING).addScalar("custName", Hibernate.STRING)
				.addScalar("deptName", Hibernate.STRING).addScalar("departname", Hibernate.STRING)
				.setParameters(params.getVals(), params.getTypes()).setMaxResults(count).list();
		List<Appoint> list = new ArrayList<Appoint>();
		int idx = 0;
		for (Object[] objs : objsList) {
			Appoint bean = new Appoint();
			idx = 0;
			bean.setId((String) objs[idx]);
			idx++;
			bean.setSelfSymptom((String) objs[idx]);
			idx++;
			bean.setFirstVisit((String) objs[idx]);
			idx++;
			bean.setMemo((String) objs[idx]);
			idx++;
			bean.setAppointTime((Timestamp) objs[idx]);
			idx++;
			bean.setDetailTime((Integer) objs[idx]);
			idx++;
			bean.setStatus((Integer) objs[idx]);
			idx++;
			bean.setResultExplain((String) objs[idx]);
			idx++;
			bean.setCustName((String) objs[idx]);
			idx++;
			bean.setDeptName((String) objs[idx]);
			idx++;
			bean.setDepartName((String) objs[idx]);
			idx++;
			list.add(bean);
		}
		/*
		 * List<Appoint> list = super.getSession().createQuery(sql.toString())
		 * .setParameters(params.getVals(),
		 * params.getTypes()).setMaxResults(count).list();
		 */
		return new PageTran<Appoint>(list);
	}

	/**
	 * 处理预约挂号申请
	 * 
	 * @author h 2016.1.22
	 */
	public int updateAppoint(Appoint bean) {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append("update Appoint set status = ?, resultExplain = ?");
		params.add(bean.getStatus(), Hibernate.INTEGER);
		params.add(bean.getResultExplain(), Hibernate.STRING);
		sql.append(", updateBy = ?, updateTime = ?");
		sql.append(" where id = ?");
		params.add(bean.getUpdateBy(), Hibernate.STRING);
		params.add(bean.getUpdateTime(), Hibernate.TIMESTAMP);
		params.add(bean.getId(), Hibernate.STRING);
		return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
				.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public Appoint getAppointById(String id) {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append(" from his_appoint t ");
		sql.append(" inner join sys_user su on su.id = t.cust_id");
		sql.append(" inner join his_dept hd on hd.id = t.dept_id");
		sql.append(" left join his_dept ht on ht.id = t.depart_id");
		sql.append(" where 1 = 1 and t.id = ?");
		params.add(id, Hibernate.STRING);
		StringBuilder pSql = new StringBuilder();
		pSql.append("select t.id, t.cust_id, hd.name_ as deptName, ht.name_ as departName ");
		pSql.append(", t.self_symptom, t.first_visit, t.memo, t.appoint_time ");
		pSql.append(", t.detail_time, su.name_ as custName, t.status, t.result_explain  ");
		pSql.append(sql);
		List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
				.addScalar("cust_id", Hibernate.STRING).addScalar("deptName", Hibernate.STRING)
				.addScalar("departName", Hibernate.STRING).addScalar("self_symptom", Hibernate.STRING)
				.addScalar("first_visit", Hibernate.STRING).addScalar("memo", Hibernate.STRING)
				.addScalar("appoint_time", Hibernate.DATE).addScalar("detail_time", Hibernate.INTEGER)
				.addScalar("custName", Hibernate.STRING).addScalar("status", Hibernate.INTEGER)
				.addScalar("result_explain", Hibernate.STRING).setParameters(params.getVals(), params.getTypes())
				.list();
		Appoint bean = null;
		if (objsList != null && objsList.size() > 0) {
			bean = new Appoint();
			Object[] objs = objsList.get(0);
			int idx = 0;
			bean.setId((String) objs[idx]);
			idx++;
			bean.setCustId((String) objs[idx]);
			idx++;
			bean.setDeptName((String) objs[idx]);
			idx++;
			bean.setDepartName((String) objs[idx]);
			idx++;
			bean.setSelfSymptom((String) objs[idx]);
			idx++;
			bean.setFirstVisit((String) objs[idx]);
			idx++;
			bean.setMemo((String) objs[idx]);
			idx++;
			bean.setAppointTime((Date) objs[idx]);
			idx++;
			bean.setDetailTime((Integer) objs[idx]);
			idx++;
			bean.setCustName((String) objs[idx]);
			idx++;
			bean.setStatus((Integer) objs[idx]);
			idx++;
			bean.setResultExplain((String) objs[idx]);
			idx++;
		}
		return bean;
	}

	public List<Appoint> queryCustmerName(Map<String, Object> filter) {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append("select t.id, t.self_symptom, t.first_visit, t.memo, t.appoint_time ,t.detail_time,");
		sql.append(" t.status, t.result_explain,tu.name_ as custName, t.cust_id, t.doctor_id");
		sql.append(" from his_appoint t");
		sql.append(" inner join sys_user tu on tu.id = t.cust_id");
		sql.append(" inner join sys_doctor sd on t.doctor_id = sd.user_id");
		sql.append(" where 1=1 ");
		// 查询条件
		String status = (String) filter.get("status");
		if (StringUtil.isNotEmpty(status)) {
			sql.append(" and t.status = ?");
			Integer state = Integer.parseInt(status);
			params.add(state, Hibernate.INTEGER);
		}
		User user = (User) filter.get("operUser");
		sql.append(" and t.doctor_id = '").append(user.getId()).append("'");
		@SuppressWarnings("unchecked")
		List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
		.addScalar("self_symptom", Hibernate.STRING).addScalar("first_visit", Hibernate.STRING)
		.addScalar("memo", Hibernate.STRING).addScalar("appoint_time", Hibernate.TIMESTAMP)
		.addScalar("detail_time", Hibernate.INTEGER).addScalar("status", Hibernate.INTEGER)
		.addScalar("result_explain", Hibernate.STRING).addScalar("custName", Hibernate.STRING)
		.addScalar("cust_id", Hibernate.STRING).addScalar("doctor_id", Hibernate.STRING)
		.setParameters(params.getVals(), params.getTypes())
		.list();
		List<Appoint> list = new ArrayList<Appoint>();
		int idx = 0;
		for (Object[] objs : objsList) {
			Appoint bean = new Appoint();
			idx = 0;
			bean.setId((String) objs[idx]);
			idx++;
			bean.setSelfSymptom((String) objs[idx]);
			idx++;
			bean.setFirstVisit((String) objs[idx]);
			idx++;
			bean.setMemo((String) objs[idx]);
			idx++;
			bean.setAppointTime((Timestamp) objs[idx]);
			idx++;
			bean.setDetailTime((Integer) objs[idx]);
			idx++;
			bean.setStatus((Integer) objs[idx]);
			idx++;
			bean.setResultExplain((String) objs[idx]);
			idx++;
			bean.setCustName((String) objs[idx]);
			idx++;
			bean.setCustId(String.valueOf(objs[idx]));
			idx++;
			bean.setDoctorId(String.valueOf(objs[idx]));
			idx++;
			list.add(bean);
		}
		return list;
		
	}

}
