package com.yzxt.yh.module.sys.dao;

import java.sql.Timestamp;
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

public class UserDao extends HibernateSupport<User>implements BaseDao<User> {
	/**
	 * 插入用户信息
	 */
	public String insert(User user) throws Exception {
		super.save(user);
		return user.getId();
	}

	/**
	 * 根据客户信息修改用户信息
	 * 
	 * @param user
	 * @return
	 */
	public int updateByCustomer(User user) {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append("update User t set");
		sql.append(
				" t.idCard = ?, t.phone = ?, t.email = ?, t.name = ?, t.orgId = ?, t.updateBy = ?, t.updateTime = ?");
		sql.append(" where t.id = ?");
		params.add(user.getIdCard(), Hibernate.STRING);
		params.add(user.getPhone(), Hibernate.STRING);
		params.add(user.getEmail(), Hibernate.STRING);
		params.add(user.getName(), Hibernate.STRING);
		params.add(user.getOrgId(), Hibernate.STRING);
		params.add(user.getUpdateBy(), Hibernate.STRING);
		params.add(user.getUpdateTime(), Hibernate.TIMESTAMP);
		params.add(user.getId(), Hibernate.STRING);
		return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
				.executeUpdate();
	}

	/**
	 * 根据管理员信息修改用户信息
	 * 
	 * @param user
	 * @return
	 */
	public int updateByAdmin(User user) {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append("update User t set");
		sql.append(" t.phone = ?, t.email = ?, t.name = ?, t.updateBy = ?, t.updateTime = ?");
		sql.append(" where t.id = ?");
		params.add(user.getPhone(), Hibernate.STRING);
		params.add(user.getEmail(), Hibernate.STRING);
		params.add(user.getName(), Hibernate.STRING);
		params.add(user.getUpdateBy(), Hibernate.STRING);
		params.add(user.getUpdateTime(), Hibernate.TIMESTAMP);
		params.add(user.getId(), Hibernate.STRING);
		return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
				.executeUpdate();
	}

	/**
	 * 加载用户信息
	 */
	@SuppressWarnings("unchecked")
	public User load(String id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select t.id, t.state, t.type_, t.account, t.phone, t.email, t.id_card, t.name_");
		sql.append(", t.img_file_id, t.org_id, t.create_by, t.create_time, t.update_by, t.update_time");
		sql.append(", (select sfd.path from sys_file_desc sfd where sfd.id = t.img_file_id) as img_file_path");
		sql.append(" from sys_user t");
		sql.append(" where t.id = ?");
		List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
				.addScalar("state", Hibernate.INTEGER).addScalar("type_", Hibernate.INTEGER)
				.addScalar("account", Hibernate.STRING).addScalar("phone", Hibernate.STRING)
				.addScalar("email", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
				.addScalar("name_", Hibernate.STRING).addScalar("img_file_id", Hibernate.STRING)
				.addScalar("org_id", Hibernate.STRING).addScalar("create_by", Hibernate.STRING)
				.addScalar("create_time", Hibernate.TIMESTAMP).addScalar("update_by", Hibernate.STRING)
				.addScalar("update_time", Hibernate.TIMESTAMP).addScalar("img_file_path", Hibernate.STRING)
				.setParameter(0, id).list();
		User user = null;
		if (objsList != null && objsList.size() > 0) {
			Object[] objs = objsList.get(0);
			user = new User();
			int i = 0;
			user.setId((String) objs[i++]);
			user.setState((Integer) objs[i++]);
			user.setType((Integer) objs[i++]);
			user.setAccount((String) objs[i++]);
			user.setPhone((String) objs[i++]);
			user.setEmail((String) objs[i++]);
			user.setIdCard((String) objs[i++]);
			user.setName((String) objs[i++]);
			user.setImgFileId((String) objs[i++]);
			user.setOrgId((String) objs[i++]);
			user.setCreateBy((String) objs[i++]);
			user.setCreateTime((Timestamp) objs[i++]);
			user.setUpdateBy((String) objs[i++]);
			user.setUpdateTime((Timestamp) objs[i++]);
			user.setImgFilePath((String) objs[i++]);
		}
		return user;
	}

	/**
	 * 判断帐号是否存在
	 * 
	 * @param account
	 * @param exceptUserId
	 * @return
	 * @throws Exception
	 */
	public boolean getAccountExist(String account, String exceptUserId) throws Exception {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append("select count(t.id) as c from User t where 1=1");
		if (StringUtil.isNotEmpty(exceptUserId)) {
			sql.append(" and t.id <> ?");
			params.add(exceptUserId, Hibernate.STRING);
		}
		sql.append(" and lower(t.account) = ?");
		params.add(account.toLowerCase(), Hibernate.STRING);
		Long c = (Long) super.getSession().createQuery(sql.toString())
				.setParameters(params.getVals(), params.getTypes()).uniqueResult();
		return c.longValue() > 0;
	}

	/**
	 * 判断电子邮箱是否存在
	 * 
	 * @param email
	 * @param exceptUserId
	 * @return
	 * @throws Exception
	 */
	public boolean getEmailExist(String email, String exceptUserId) throws Exception {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append("select count(t.id) as c from User t where 1=1");
		if (StringUtil.isNotEmpty(exceptUserId)) {
			sql.append(" and t.id <> ?");
			params.add(exceptUserId, Hibernate.STRING);
		}
		sql.append(" and lower(t.email) = ?");
		params.add(email.toLowerCase(), Hibernate.STRING);
		Long c = (Long) super.getSession().createQuery(sql.toString())
				.setParameters(params.getVals(), params.getTypes()).uniqueResult();
		return c.longValue() > 0;
	}

	/**
	 * 判断电话号码是否存在
	 * 
	 * @param phone
	 * @param exceptUserId
	 * @return
	 * @throws Exception
	 */
	public boolean getPhoneExist(String phone, String exceptUserId) throws Exception {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append("select count(t.id) as c from User t where 1=1");
		if (StringUtil.isNotEmpty(exceptUserId)) {
			sql.append(" and t.id <> ?");
			params.add(exceptUserId, Hibernate.STRING);
		}
		sql.append(" and lower(t.phone) = ?");
		params.add(phone.toLowerCase(), Hibernate.STRING);
		Long c = (Long) super.getSession().createQuery(sql.toString())
				.setParameters(params.getVals(), params.getTypes()).uniqueResult();
		return c.longValue() > 0;
	}

	/**
	 * 判断身份证号码是否存在
	 * 
	 * @param idCard
	 * @param exceptUserId
	 * @return
	 * @throws Exception
	 */
	public boolean getIdCardExist(String idCard, String exceptUserId) throws Exception {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append("select count(t.id) as c from User t where 1=1");
		if (StringUtil.isNotEmpty(exceptUserId)) {
			sql.append(" and t.id <> ?");
			params.add(exceptUserId, Hibernate.STRING);
		}
		sql.append(" and lower(t.idCard) = ?");
		params.add(idCard.toLowerCase(), Hibernate.STRING);
		Long c = (Long) super.getSession().createQuery(sql.toString())
				.setParameters(params.getVals(), params.getTypes()).uniqueResult();
		return c.longValue() > 0;
	}

	@SuppressWarnings("unchecked")
	public User getUserByAcount(String account) throws Exception {
		HibernateParams params = new HibernateParams();
		String sql = "select t from User t where t.account = ?";
		params.add(account, Hibernate.STRING);
		List<User> users = super.getSession().createQuery(sql).setParameters(params.getVals(), params.getTypes())
				.list();
		return users != null && !users.isEmpty() ? users.get(0) : null;
	}

	/**
	 * 通过用户身份证号码查询用户
	 * 
	 * @param idCard
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public User getUserByIdCard(String idCard) {
		HibernateParams params = new HibernateParams();
		String sql = "select t from User t where t.idCard = ?";
		params.add(idCard, Hibernate.STRING);
		List<User> users = super.getSession().createQuery(sql).setParameters(params.getVals(), params.getTypes())
				.list();
		return users != null && !users.isEmpty() ? users.get(0) : null;
	}

	/**
	 * 通过用户手机号查询用户
	 * 
	 * @param phone
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public User getUserByPhone(String phone) {
		HibernateParams params = new HibernateParams();
		String sql = "select t from User t where t.phone = ?";
		params.add(phone, Hibernate.STRING);
		List<User> users = super.getSession().createQuery(sql).setParameters(params.getVals(), params.getTypes())
				.list();
		return users != null && !users.isEmpty() ? users.get(0) : null;
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
	 * 通过用户电子邮箱查询用户
	 * 
	 * @param email
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public User getUserByEmail(String email) {
		HibernateParams params = new HibernateParams();
		String sql = "select t from User t where t.email = ?";
		params.add(email, Hibernate.STRING);
		List<User> users = super.getSession().createQuery(sql).setParameters(params.getVals(), params.getTypes())
				.list();
		return users != null && !users.isEmpty() ? users.get(0) : null;
	}

	/**
	 * 插入用户密码信息
	 */
	public int addPassword(String userId, String password) throws Exception {
		HibernateParams params = new HibernateParams();
		String sql = "insert into sys_user_password (user_id, password) values (?, ?)";
		params.add(userId, Hibernate.STRING);
		params.add(PwdUtil.encrypt(password, userId), Hibernate.STRING);
		int c = super.getSession().createSQLQuery(sql).setParameters(params.getVals(), params.getTypes())
				.executeUpdate();
		return c;
	}

	/**
	 * 更新用户密码
	 * 
	 * @param userId
	 * @param newPassword
	 * @return
	 * @throws Exception
	 */
	public int updatePassword(String userId, String newPassword) throws Exception {
		HibernateParams params = new HibernateParams();
		String sql = "update sys_user_password set password = ? where user_id = ?";
		params.add(PwdUtil.encrypt(newPassword, userId), Hibernate.STRING);
		params.add(userId, Hibernate.STRING);
		int c = super.getSession().createSQLQuery(sql).setParameters(params.getVals(), params.getTypes())
				.executeUpdate();
		return c;
	}
	
	/**
	 * 更新验证码
	 * 
	 * @param userId
	 * @param newPassword
	 * @return
	 * @throws Exception
	 */
	public int updateCode(User user) throws Exception {
		HibernateParams params = new HibernateParams();
		String sql = "update sys_user set code = ? ,update_time = ? where id = ?";
		params.add(user.getCode(), Hibernate.STRING);
		params.add(user.getUpdateTime(), Hibernate.TIMESTAMP);
		params.add(user.getId(), Hibernate.STRING);
		int c = super.getSession().createSQLQuery(sql).setParameters(params.getVals(), params.getTypes())
				.executeUpdate();
		return c;
	}

	/**
	 * 验证用户密码是否有效
	 * 
	 * @param userId
	 * @param newPassword
	 * @return
	 * @throws Exception
	 */
	public boolean getPasswordValid(String userId, String password) throws Exception {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append("select count(t.user_id) as c from sys_user_password t where 1=1");
		sql.append(" and t.password = ? and t.user_id = ?");
		params.add(PwdUtil.encrypt(password, userId), Hibernate.STRING);
		params.add(userId, Hibernate.STRING);
		Long c = (Long) super.getSession().createSQLQuery(sql.toString()).addScalar("c", Hibernate.LONG)
				.setParameters(params.getVals(), params.getTypes()).uniqueResult();
		return c.longValue() > 0;
	}

	/**
	 * 更新客户信息user
	 */
	public int updateByAchive(User user) throws Exception {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append("update User set name = ?, updateBy = ?, updateTime = ?");
		params.add(user.getName(), Hibernate.STRING);
		params.add(user.getUpdateBy(), Hibernate.STRING);
		params.add(user.getUpdateTime(), Hibernate.TIMESTAMP);
		if (StringUtil.isNotEmpty(user.getImgFileId())) {
			sql.append(", imgFileId = ?");
			params.add(user.getImgFileId(), Hibernate.STRING);
		}
		sql.append(" where id = ?");
		params.add(user.getId(), Hibernate.STRING);
		int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
				.executeUpdate();
		return c;
	}

	/**
	 * 加入会员更新用户信息，主要是用户组织
	 */
	public int updateByMember(User user) throws Exception {
		if (StringUtil.isEmpty(user.getOrgId())) {
			return 0;
		}
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append("update User set orgId = ? where id = ?");
		params.add(user.getOrgId(), Hibernate.STRING);
		params.add(user.getId(), Hibernate.STRING);
		int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
				.executeUpdate();
		return c;
	}

	/**
	 * 修改账号是修改客户信息
	 * 
	 * @param user
	 * @return
	 */
	public int updateByAccount(User user) {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append("update User t set");
		sql.append(" t.name = ?, t.phone = ?, t.email = ?");
		params.add(user.getName(), Hibernate.STRING);
		params.add(user.getPhone(), Hibernate.STRING);
		params.add(user.getEmail(), Hibernate.STRING);
		// 修改了账号
		if (StringUtil.isNotEmpty(user.getAccount())) {
			sql.append(", t.account = ?");
			params.add(user.getAccount(), Hibernate.STRING);
		}
		// 修改了身份证号
		if (StringUtil.isNotEmpty(user.getIdCard())) {
			sql.append(", t.idCard = ?");
			params.add(user.getIdCard(), Hibernate.STRING);
		}
		sql.append(" where t.id = ?");
		params.add(user.getId(), Hibernate.STRING);
		int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
				.executeUpdate();
		return c;
	}

	/**
	 * 根据id获取用户密码
	 * @param userId
	 * @return
	 */
	public UserPassword getPassword(String userId) {
		HibernateParams params = new HibernateParams();
		String sql = "select t from UserPassword t where t.userId = ?";
		params.add(userId, Hibernate.STRING);
		@SuppressWarnings("unchecked")
		List<UserPassword> users = super.getSession().createQuery(sql)
				.setParameters(params.getVals(), params.getTypes()).list();
		return users != null && !users.isEmpty() ? users.get(0) : null;
	}

}
