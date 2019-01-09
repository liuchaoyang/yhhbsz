package com.yzxt.yh.module.pwd.dao;

import java.util.List;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.pwd.bean.FindPwd;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

public class RegisterDao extends HibernateSupport<User> implements BaseDao<User>
{
    /**
     * 插入用户信息
     */
    public String insert(User user) throws Exception
    {
        super.save(user);
        return user.getId();
    }

    /**
     * 判断帐号是否存在
     * @param account
     * @param exceptUserId
     * @return
     * @throws Exception
     */
    public boolean getAccountExist(String account, String exceptUserId) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(t.id) as c from User t where 1=1");
        if (StringUtil.isNotEmpty(exceptUserId))
        {
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
     * @param email
     * @param exceptUserId
     * @return
     * @throws Exception
     */
    public boolean getEmailExist(String email, String exceptUserId) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(t.id) as c from User t where 1=1");
        if (StringUtil.isNotEmpty(exceptUserId))
        {
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
     * @param phone
     * @param exceptUserId
     * @return
     * @throws Exception
     */
    public boolean getPhoneExist(String phone, String exceptUserId) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(t.id) as c from User t where 1=1");
        if (StringUtil.isNotEmpty(exceptUserId))
        {
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
     * @param idCard
     * @param exceptUserId
     * @return
     * @throws Exception
     */
    public boolean getIdCardExist(String idCard, String exceptUserId) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(t.id) as c from User t where 1=1");
        if (StringUtil.isNotEmpty(exceptUserId))
        {
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
    public User getUserByAcount(String account) throws Exception
    {
        HibernateParams params = new HibernateParams();
        String sql = "select t from User t where t.account = ?";
        params.add(account, Hibernate.STRING);
        List<User> users = super.getSession().createQuery(sql).setParameters(params.getVals(), params.getTypes())
                .list();
        return users != null && !users.isEmpty() ? users.get(0) : null;
    }

    /**
     * 插入用户角色信息
     */
    public int addRole(String id, String userId) throws Exception
    {
        HibernateParams params = new HibernateParams();
        String sql = "insert into sys_user_role (id,user_id, role_id) values (?,?, ?)";
        params.add(id, Hibernate.STRING);
        params.add(userId, Hibernate.STRING);
        params.add("31", Hibernate.STRING);
        int c = super.getSession().createSQLQuery(sql).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

    /**
     * 根据用户名和邮箱得到用户信息
     * @param name
     * @param email
     * @return
     */
    public boolean getUserDetail(String name, String email)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) as c from sys_user t where 1=1 and t.account=? and t.email=?");
        params.add(name, Hibernate.STRING);
        params.add(email, Hibernate.STRING);
        // 查询结果nate
        Long c = (Long) super.getSession().createSQLQuery(sql.toString()).addScalar("c", Hibernate.LONG)
                .setParameters(params.getVals(), params.getTypes()).uniqueResult();
        System.out.print(c.longValue());
        return c.longValue() > 0;
    }

    /**
     * 通过用户名和邮箱得到用户信息
     * @param name
     * @param email
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public User getUserByNameAndEmail(String name, String email) throws Exception
    {
        HibernateParams params = new HibernateParams();
        String hql = "select t from User t where t.account = ? and t.email=?";
        params.add(name, Hibernate.STRING);
        params.add(email, Hibernate.STRING);
        List<User> users = super.getSession().createQuery(hql).setParameters(params.getVals(), params.getTypes())
                .list();
        return users != null && !users.isEmpty() ? users.get(0) : null;
    }

    /**
     * 根据key用户名查找user
     * @param name
     * @param key
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public FindPwd getCheck(String name, String key) throws Exception
    {
        HibernateParams params = new HibernateParams();
        String hql = "select t from FindPwd t where  t.validateCode=?";
        params.add(key, Hibernate.STRING);
        List<FindPwd> findPwds = super.getSession().createQuery(hql).setParameters(params.getVals(), params.getTypes())
                .list();
        return findPwds != null && !findPwds.isEmpty() ? findPwds.get(0) : null;
    }

}
