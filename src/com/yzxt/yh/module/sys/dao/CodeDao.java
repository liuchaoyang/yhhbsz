package com.yzxt.yh.module.sys.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.util.PwdUtil;
import com.yzxt.yh.module.sys.bean.Codes;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.bean.UserPassword;
import com.yzxt.yh.util.StringUtil;

public class CodeDao extends HibernateSupport<Codes>implements BaseDao<Codes> {
	/**
	 * 插入用户信息
	 */
	public String insert(Codes code) throws Exception {
		super.save(code);
		return code.getId();
	}

	/**
	 * 通过用户手机号查询验证码
	 * 
	 * @param phone
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Codes getCodeByPhone(String phone) {
		HibernateParams params = new HibernateParams();
		String sql = "select t from Codes t where t.phone = ?";
		params.add(phone, Hibernate.STRING);
		List<Codes> users = super.getSession().createQuery(sql).setParameters(params.getVals(), params.getTypes())
				.list();
		return users != null && !users.isEmpty() ? users.get(0) : null;
	}
	
	/**
	 * 更新验证码
	 * 
	 * @param userId
	 * @param newPassword
	 * @return
	 * @throws Exception
	 */
	public int updateCode(Codes code) throws Exception {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append("update Codes set code= ?,createTime = ? where id = ?");
		params.add(code.getCode(), Hibernate.STRING)
		.add(code.getCreateTime(), Hibernate.TIMESTAMP)
		.add(code.getId(), Hibernate.STRING);
		int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
				.executeUpdate();
		return c;
	}


}
