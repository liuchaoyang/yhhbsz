package com.yzxt.yh.module.sys.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstRole;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.PressurePulse;
import com.yzxt.yh.module.sys.bean.CustFamily;
import com.yzxt.yh.module.sys.bean.Depts;
import com.yzxt.yh.module.sys.bean.Doctor;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

public class DoctorDao extends HibernateSupport<Doctor>implements BaseDao<Doctor> {

	public String insert(Doctor doctor) throws Exception {
		super.save(doctor);
		return doctor.getUserId();
	}

	public int update(Doctor doctor) throws Exception {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append("update Doctor set sex = ?, birthday = ?, address = ?, degree = ?");
		sql.append(",  professionTitle = ?, deptName = ?, skillInfo = ?, describe = ?,price =?,yprice =?");
		sql.append(",  updateBy = ?, updateTime = ?");
		sql.append(" where userId = ?");
		params.add(doctor.getSex(), Hibernate.INTEGER).add(doctor.getBirthday(), Hibernate.DATE)
				.add(doctor.getAddress(), Hibernate.STRING).add(doctor.getDegree(), Hibernate.INTEGER)
				.add(doctor.getProfessionTitle(), Hibernate.STRING).add(doctor.getDeptName(), Hibernate.STRING)
				.add(doctor.getSkillInfo(), Hibernate.STRING).add(doctor.getDescribe(), Hibernate.STRING)
				.add(doctor.getPrice(), Hibernate.INTEGER).add(doctor.getYprice(), Hibernate.INTEGER)
				.add(doctor.getUpdateBy(), Hibernate.STRING).add(doctor.getUpdateTime(), Hibernate.TIMESTAMP)
				.add(doctor.getUserId(), Hibernate.STRING);
		int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
				.executeUpdate();
		return c;
	}

	public boolean queryIsExistAccount(String account) {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append(" form sys_doctor t ");
		sql.append(" where 1= 1");
		if (account != null) {

		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public List<Doctor> deptDocList(Map<String, Object> filter) {
		HibernateParams params = new HibernateParams();
		StringBuilder mSql = new StringBuilder();
		mSql.append(" from sys_doctor t");
		mSql.append(" inner join sys_user tu on tu.id = t.user_id");
		// 查询条件
		String deptId = (String) filter.get("deptId");
		if (StringUtil.isNotEmpty(deptId)) {
			mSql.append(" where 1=1");
			mSql.append(" and t.dept_id = ?");
			params.add(deptId, Hibernate.STRING);
		}
		/*
		 * // 查询人 User operUser = (User) filter.get("operUser"); // 不是管理员
		 * Collection<String> roles = operUser.getRoles(); if
		 * (!Constant.USER_TYPE_ADMIN.equals(operUser.getType()) || roles ==
		 * null) { return null; }
		 */
		// 总记录数
		StringBuilder totalCountSql = new StringBuilder();
		totalCountSql.append("select count(t.user_id) as c ").append(mSql);
		Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
				.addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
		// 分页记录
		StringBuilder pSql = new StringBuilder();
		pSql.append(
				"select t.user_id, t.sex, t.birthday, t.address, t.degree, t.profession_title, t.dept_name, t.skill_info, t.describe_, t.create_time, t.update_time");
		pSql.append(", tu.account, tu.name_, tu.email, tu.phone ");
		pSql.append(mSql);
		// pSql.append(" order by t.update_time desc limit ?, ?");
		/*
		 * params.add(pageSize * (page - 1), Hibernate.INTEGER);
		 * params.add(pageSize, Hibernate.INTEGER);
		 */
		List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
				.addScalar("user_id", Hibernate.STRING).addScalar("sex", Hibernate.INTEGER)
				.addScalar("birthday", Hibernate.DATE).addScalar("address", Hibernate.STRING)
				.addScalar("degree", Hibernate.INTEGER).addScalar("profession_title", Hibernate.STRING)
				.addScalar("dept_name", Hibernate.STRING).addScalar("skill_info", Hibernate.STRING)
				.addScalar("describe_", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
				.addScalar("update_time", Hibernate.TIMESTAMP).addScalar("account", Hibernate.STRING)
				.addScalar("name_", Hibernate.STRING).addScalar("email", Hibernate.STRING)
				.addScalar("phone", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
		List<Doctor> list = new ArrayList<Doctor>();
		int idx = 0;
		for (Object[] objs : objsList) {
			Doctor doctor = new Doctor();
			idx = 0;
			doctor.setUserId((String) objs[idx]);
			idx++;
			doctor.setSex((Integer) objs[idx]);
			idx++;
			doctor.setBirthday((Date) objs[idx]);
			idx++;
			doctor.setAddress((String) objs[idx]);
			idx++;
			doctor.setDegree((Integer) objs[idx]);
			idx++;
			doctor.setProfessionTitle((String) objs[idx]);
			idx++;
			doctor.setDeptName((String) objs[idx]);
			idx++;
			doctor.setSkillInfo((String) objs[idx]);
			idx++;
			doctor.setDescribe((String) objs[idx]);
			idx++;
			doctor.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
			idx++;
			doctor.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
			idx++;
			doctor.setAccount((String) objs[idx]);
			idx++;
			doctor.setDoctorName((String) objs[idx]);
			idx++;
			doctor.setEmail((String) objs[idx]);
			idx++;
			doctor.setPhone((String) objs[idx]);
			idx++;
			list.add(doctor);
		}
		/*
		 * PageModel<Doctor> pageModel = new PageModel<Doctor>();
		 * pageModel.setTotal(total); pageModel.setData(list);
		 */
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageModel<Doctor> getList(Map<String, Object> filter, int page, int pageSize) {
		HibernateParams params = new HibernateParams();
		StringBuilder mSql = new StringBuilder();
		mSql.append(" from sys_doctor t");
		mSql.append(" inner join sys_user tu on tu.id = t.user_id");
		mSql.append(" inner join sys_org tor on tor.id = tu.org_id");
		mSql.append(" where 1=1");
		// 查询条件
		String name = (String) filter.get("name");
		if (StringUtil.isNotEmpty(name)) {
			mSql.append(" and tu.name_ like ").append(params.addLikePart(name));
		}
		// 查询人
		User operUser = (User) filter.get("operUser");
		// 不是管理员
		Collection<String> roles = operUser.getRoles();
		if (!Constant.USER_TYPE_ADMIN.equals(operUser.getType()) || roles == null) {
			return new PageModel<Doctor>();
		}
		// 不是系统管理员，添加组织限制
		if (!roles.contains(ConstRole.ADMIN)) {
			mSql.append(" and tor.full_id like ").append(params.addLikePart("/" + operUser.getOrgId() + "/"));
		}
		// 总记录数
		StringBuilder totalCountSql = new StringBuilder();
		totalCountSql.append("select count(t.user_id) as c ").append(mSql);
		Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
				.addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
		// 分页记录
		StringBuilder pSql = new StringBuilder();
		pSql.append(
				"select t.user_id, t.sex, t.birthday, t.address, t.degree, t.profession_title, t.dept_name, t.skill_info, t.describe_, t.create_time,t.price,t.yprice,t.type,t.update_time");
		pSql.append(", tu.account, tu.name_, tu.email, tu.phone, tor.id as orgId, tor.orgName as org_name");
		pSql.append(mSql);
		pSql.append(" order by t.update_time desc limit ?, ?");
		params.add(pageSize * (page - 1), Hibernate.INTEGER);
		params.add(pageSize, Hibernate.INTEGER);
		List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
				.addScalar("user_id", Hibernate.STRING).addScalar("sex", Hibernate.INTEGER)
				.addScalar("birthday", Hibernate.DATE).addScalar("address", Hibernate.STRING)
				.addScalar("degree", Hibernate.INTEGER).addScalar("profession_title", Hibernate.STRING)
				.addScalar("dept_name", Hibernate.STRING).addScalar("skill_info", Hibernate.STRING)
				.addScalar("describe_", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
				.addScalar("price", Hibernate.INTEGER).addScalar("yprice", Hibernate.INTEGER).addScalar("type", Hibernate.INTEGER)
				.addScalar("update_time", Hibernate.TIMESTAMP).addScalar("account", Hibernate.STRING)
				.addScalar("name_", Hibernate.STRING).addScalar("email", Hibernate.STRING)
				.addScalar("phone", Hibernate.STRING).addScalar("orgId", Hibernate.STRING)
				.addScalar("org_name", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
		
		List<Doctor> list = new ArrayList<Doctor>();
		int idx = 0;
		for (Object[] objs : objsList) {
			Doctor doctor = new Doctor();
			idx = 0;
			doctor.setUserId((String) objs[idx]);
			idx++;
			doctor.setSex((Integer) objs[idx]);
			idx++;
			doctor.setBirthday((Date) objs[idx]);
			idx++;
			doctor.setAddress((String) objs[idx]);
			idx++;
			doctor.setDegree((Integer) objs[idx]);
			idx++;
			doctor.setProfessionTitle((String) objs[idx]);
			idx++;
			doctor.setDeptName((String) objs[idx]);
			idx++;
			doctor.setSkillInfo((String) objs[idx]);
			idx++;
			doctor.setDescribe((String) objs[idx]);
			idx++;
			doctor.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
			idx++;
			doctor.setPrice(objs[idx] != null ? Integer.parseInt(objs[idx].toString()) : null);
			idx++;
			doctor.setYprice(objs[idx] != null ? Integer.parseInt(objs[idx].toString()) : null);
			idx++;
			doctor.setType(objs[idx] != null ? Integer.parseInt(objs[idx].toString()) : null);
			idx++;
			doctor.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
			idx++;
			doctor.setAccount((String) objs[idx]);
			idx++;
			doctor.setDoctorName((String) objs[idx]);
			idx++;
			doctor.setEmail((String) objs[idx]);
			idx++;
			doctor.setPhone((String) objs[idx]);
			idx++;
			doctor.setOrgId((String) objs[idx]);
			idx++;
			doctor.setOrgName((String) objs[idx]);
			idx++;
			list.add(doctor);
		}
		PageModel<Doctor> pageModel = new PageModel<Doctor>();
		pageModel.setTotal(total);
		pageModel.setData(list);
		return pageModel;
	}

	/**
	 * 查询医生列表
	 * 
	 * @param filter
	 *            过滤条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Doctor> queryDoctorTran(Map<String, Object> filter) {
		HibernateParams params = new HibernateParams();
		StringBuilder mSql = new StringBuilder();
		mSql.append(" from sys_doctor t");
		mSql.append(" inner join sys_user tu on tu.id = t.user_id");
		mSql.append(" inner join sys_org tor on tor.id = tu.org_id");
		mSql.append(" where 1=1 ");
		mSql.append(" and  t.user_id not in (SELECT doctor_id from  svb_member_info where cust_id = ?)");
		// 查询人
		User operUser = (User) filter.get("operUser");
		params.add(operUser.getId(), Hibernate.STRING);
		// 查询条件
		String name = (String) filter.get("doctorName");
		if (StringUtil.isNotEmpty(name)) {
			mSql.append(" and tu.name_ like ").append(params.addLikePart(name));
		}
		String address = (String) filter.get("address");
		if (StringUtil.isNotEmpty(address)) {
			mSql.append(" and t.address like ").append(params.addLikePart(address));
		}
		String deptName = (String) filter.get("deptName");
		if (StringUtil.isNotEmpty(deptName)) {
			mSql.append(" and  t.dept_name like ").append(params.addLikePart(deptName));
		}
		
	/*	if (operUser != null) {
			// 不是管理员
			Collection<String> roles = operUser.getRoles();
			if (!Constant.USER_TYPE_ADMIN.equals(operUser.getType()) || roles == null) {
				return new ArrayList<Doctor>();
			}
			// 不是系统管理员，添加组织限制
			if (!roles.contains(ConstRole.ADMIN)) {
				mSql.append(" and tor.full_id like ").append(params.addLikePart("/" + operUser.getOrgId() + "/"));
			}
		}*/
		// 总记录数
		StringBuilder totalCountSql = new StringBuilder();
		totalCountSql.append("select count(t.user_id) as c ").append(mSql);
		Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
				.addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
		// 分页记录
		StringBuilder pSql = new StringBuilder();
		pSql.append(
				"select t.user_id, t.sex, t.birthday, t.address, t.degree, t.profession_title, t.dept_name, t.skill_info, t.describe_, t.price,t.yprice,t.create_time, t.update_time");
		pSql.append(", tu.account, tu.name_, tu.email, tu.phone, tor.id as orgId, tor.orgName as org_name");
		pSql.append(mSql);
		List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
				.addScalar("user_id", Hibernate.STRING).addScalar("sex", Hibernate.INTEGER)
				.addScalar("birthday", Hibernate.DATE).addScalar("address", Hibernate.STRING)
				.addScalar("degree", Hibernate.INTEGER).addScalar("profession_title", Hibernate.STRING)
				.addScalar("dept_name", Hibernate.STRING).addScalar("skill_info", Hibernate.STRING)
				.addScalar("describe_", Hibernate.STRING).addScalar("price", Hibernate.INTEGER)
				.addScalar("yprice", Hibernate.INTEGER).addScalar("create_time", Hibernate.TIMESTAMP)
				.addScalar("update_time", Hibernate.TIMESTAMP).addScalar("account", Hibernate.STRING)
				.addScalar("name_", Hibernate.STRING).addScalar("email", Hibernate.STRING)
				.addScalar("phone", Hibernate.STRING).addScalar("orgId", Hibernate.STRING)
				.addScalar("org_name", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
		List<Doctor> list = new ArrayList<Doctor>();
		int idx = 0;
		for (Object[] objs : objsList) {
			Doctor doctor = new Doctor();
			idx = 0;
			doctor.setUserId((String) objs[idx]);
			idx++;
			doctor.setSex((Integer) objs[idx]);
			idx++;
			doctor.setBirthday((Date) objs[idx]);
			idx++;
			doctor.setAddress((String) objs[idx]);
			idx++;
			doctor.setDegree((Integer) objs[idx]);
			idx++;
			doctor.setProfessionTitle((String) objs[idx]);
			idx++;
			doctor.setDeptName((String) objs[idx]);
			idx++;
			doctor.setSkillInfo((String) objs[idx]);
			idx++;
			doctor.setDescribe((String) objs[idx]);
			idx++;
			doctor.setPrice(objs[idx] != null ? Integer.parseInt(objs[idx].toString())/100 : null);
			idx++;
			doctor.setYprice(objs[idx] != null ? Integer.parseInt(objs[idx].toString())/100 : null);
			idx++;
			doctor.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
			idx++;
			doctor.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
			idx++;
			doctor.setAccount((String) objs[idx]);
			idx++;
			doctor.setDoctorName((String) objs[idx]);
			idx++;
			doctor.setEmail((String) objs[idx]);
			idx++;
			doctor.setPhone((String) objs[idx]);
			idx++;
			doctor.setOrgId((String) objs[idx]);
			idx++;
			doctor.setOrgName((String) objs[idx]);
			idx++;
			list.add(doctor);
		}

		return list;
	}

	public List<Depts> querydept(Integer parentId) {
		 HibernateParams params = new HibernateParams();
	        StringBuilder sql = new StringBuilder();
	        sql.append(" select t.codeid, t.parentid, t.deptName from depts t " );
	        //String parentId = (String) filter.get("parentId");
	        
            sql.append(" where 1=1 and t.parentid = ?");
            // 查询条件
            params.add(parentId, Hibernate.INTEGER);
	       
	        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
	                .addScalar("codeid", Hibernate.INTEGER).addScalar("parentid", Hibernate.INTEGER)
	                .addScalar("deptName", Hibernate.STRING)
	                .setParameters(params.getVals(), params.getTypes())
	                .list();
	       
	        List<Depts> list = new ArrayList<Depts>();
	        int idx = 0;
	        for (Object[] objs : objsList)
	        {
	        	Depts bean = new Depts();
	            idx = 0;
	            bean.setCodeId(Integer.parseInt(objs[idx].toString()));
	            idx++;
	            bean.setParentId(Integer.parseInt(objs[idx].toString()));
	            idx++;
	            bean.setDeptName((String) objs[idx]);
	            idx++;
	            list.add(bean);
	        }
	        return list;
	}
	/**
	 * 根据id获取医生信息
	 */
	
	public Object[] getDoctorById(String doctorId){
		HibernateParams params = new HibernateParams();
		StringBuilder mSql = new StringBuilder();
		mSql.append(" from sys_doctor t");
		mSql.append(" inner join sys_user tu on tu.id = t.user_id");
		mSql.append(" inner join sys_org tor on tor.id = tu.org_id");
		mSql.append(" where tu.id= '" + doctorId + "'");
		/*mSql.append(" where tu.id= ?");
		// 查询条件
		if (StringUtil.isNotEmpty(doctorId)) {
			mSql.append(" and tu.id=? ");
		}
		params.add(doctorId, Hibernate.STRING);*/
		StringBuilder pSql = new StringBuilder();
		pSql.append(
				"select t.user_id, t.sex,t.address, t.profession_title, t.dept_name, t.skill_info, t.describe_, t.price,t.yprice");
		pSql.append(", tu.name_, tu.phone, tor.orgName,t.state");
		pSql.append(mSql);
		Object obj = this.getSession().createSQLQuery(pSql.toString()).uniqueResult();
		Object[] objs = (Object[]) obj;
		return objs;
	
	}
	private static Doctor objToDoctor(Object[] objs) {
		if (objs == null || objs.length < 13) {
			return null;
		}
		Doctor bean = new Doctor();
		bean.setUserId((String) objs[0]);
		if (objs[1] != null) {
			String sexx = String.valueOf(objs[1]);
			if(StringUtil.isNotEmpty(sexx))
				bean.setSex(Integer.parseInt(sexx));
		}
		bean.setAddress((String) objs[2]);
		bean.setProfessionTitle((String) objs[3]);
		bean.setDeptName((String) objs[4]);
		bean.setSkillInfo((String) objs[5]);
		bean.setDescribe((String) objs[6]);
		if (objs[7] != null) {
			String priceStr = String.valueOf(objs[7]);
			if(StringUtil.isNotEmpty(priceStr))
				bean.setPrice(Integer.parseInt(priceStr));
		}
		if (objs[8] != null) {
			String ypriceStr = String.valueOf(objs[8]);
			if(StringUtil.isNotEmpty(ypriceStr))
				bean.setYprice(Integer.parseInt(ypriceStr));
		}
		bean.setDoctorName((String) objs[9]);
		bean.setPhone((String) objs[10]);
		bean.setOrgName((String) objs[11]);
		if (objs[12] != null) {
			String states= String.valueOf(objs[12]);
			if(StringUtil.isNotEmpty(states))
				bean.setState(Integer.parseInt(states));
		}
		return bean;
	}
	/**
	 * 获取订单详情
	 * @param orderId
	 * @return
	 */
	public Object[] getOrderByOrderId(String orderId) {
		HibernateParams params = new HibernateParams();
		StringBuilder mSql = new StringBuilder();
		mSql.append(" from sys_customer t");
		mSql.append(" inner join sys_user tu on tu.id = t.user_id");
		mSql.append(" left join svb_member_info tmi on tmi.state = 1 and tmi.id = t.member_id");
		mSql.append(" left join sys_org tor on tor.id = tu.org_id");
		mSql.append(" inner join sys_doctor sd on tmi.doctor_id = sd.user_id");
		mSql.append(" inner join orders o on tu.id = o.uid");
		mSql.append(" where o.oid= '" + orderId + "'");
		/*mSql.append(" where tu.id= ?");
		// 查询条件
		if (StringUtil.isNotEmpty(doctorId)) {
			mSql.append(" and tu.id=? ");
		}
		params.add(doctorId, Hibernate.STRING);*/
		StringBuilder pSql = new StringBuilder();
		pSql.append(
				"select t.user_id,t.sex,tu.name_,tmi.start_day,tmi.end_day,o.ordertime,o.total,sd.type,sd.dept_name,t.memo,tor.orgName,(select su.name_ from svb_member_info sm inner join sys_user su on su.id = sm.doctor_id where sm.id = t.member_id)");
		pSql.append(mSql);
		Object obj = this.getSession().createSQLQuery(pSql.toString()).uniqueResult();
		Object[] objs = (Object[]) obj;
		return objs;
	}
	public Doctor getDoctor(String doctorId) {

        HibernateParams params = new HibernateParams();
        String hql = "select t from Doctor t where t. userId= ? ";
        params.add(doctorId, Hibernate.STRING);
        List<Doctor> cust = super.getSession().createQuery(hql).setParameters(params.getVals(), params.getTypes())
                .list();
        return cust != null && !cust.isEmpty() ? cust.get(0) : null;

	}
	
	public int updatePre(Doctor doctor) {
		HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" update Doctor t set");
        sql.append(" t.state = ?");
        sql.append(" where t. userId = ?");
        params.add(doctor.getState(), Hibernate.INTEGER);
        params.add(doctor.getUserId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
		
	}
	
}
