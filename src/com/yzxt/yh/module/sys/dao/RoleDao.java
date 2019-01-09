package com.yzxt.yh.module.sys.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.sys.bean.Resource;
import com.yzxt.yh.module.sys.bean.Role;
import com.yzxt.yh.module.sys.bean.RoleResource;
import com.yzxt.yh.util.StringUtil;

public class RoleDao extends HibernateSupport<Role> implements BaseDao<Role>
{

    private RoleResourceDao roleResourceDao;

    public RoleResourceDao getRoleResourceDao()
    {
        return roleResourceDao;
    }

    public void setRoleResourceDao(RoleResourceDao roleResourceDao)
    {
        this.roleResourceDao = roleResourceDao;
    }

    /**
     * 插入角色信息
     */
    public String insert(Role role) throws Exception
    {
        super.save(role);
        return role.getId();
    }

    /**
     * 更新角色信息
     */
    public int update(Role role) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update Role set roleName = ?, memo = ?, updateTime = ?");
        sql.append(" where id = ?");
        params.add(role.getRoleName(), Hibernate.STRING).add(role.getMemo(), Hibernate.STRING)
                .add(role.getUpdateTime(), Hibernate.TIMESTAMP).add(role.getId(), Hibernate.STRING);
        int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

    /**
     * 更新角色信息
     */
    public int delete(Role role) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("delete from Role where id = ?");
        params.add(role.getId(), Hibernate.STRING);
        int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

    /**
     * 加载角色信息
     */
    public Role load(String id) throws Exception
    {
        return super.get(id);
    }

    /**
     * 分页查询角色数据
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageModel<Role> query(Map<String, Object> filter, int page, int pageSize)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from sys_role t ");
        mSql.append(" where 1=1 and t.type_ = 2");
        // 查询条件
        String roleName = (String) filter.get("roleName");
        if (StringUtil.isNotEmpty(roleName))
        {
            mSql.append(" and t.name_ like").append(params.addLikePart(roleName));
        }
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(t.id) as c ").append(mSql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.type_, t.name_, t.memo,t.create_time,t.update_time");
        pSql.append(mSql);
        pSql.append(" order by t.create_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("type_", Hibernate.INTEGER).addScalar("name_", Hibernate.STRING)
                .addScalar("memo", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("update_time", Hibernate.TIMESTAMP).setParameters(params.getVals(), params.getTypes())
                .list();
        List<Role> list = new ArrayList<Role>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Role role = new Role();
            idx = 0;
            role.setId((String) objs[idx]);
            idx++;
            role.setType((Integer) objs[idx]);
            idx++;
            role.setRoleName((String) objs[idx]);
            idx++;
            role.setMemo((String) objs[idx]);
            idx++;
            role.setCreateTime((Timestamp) objs[idx]);
            idx++;
            role.setUpdateTime((Timestamp) objs[idx]);
            idx++;
            list.add(role);
        }
        PageModel<Role> pageModel = new PageModel<Role>();
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 判断角色是否存在
     * @param account
     * @param exceptRoleId
     * @return
     * @throws Exception
     */
    public boolean getRoleExist(String roleName, String exceptRoleId) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) as c from sys_role t where 1=1 ");
        sql.append(" and lower(t.name_) = ?");
        params.add(roleName.toLowerCase(), Hibernate.STRING);
        if (StringUtil.isNotEmpty(exceptRoleId))
        {
            sql.append(" and t.id <> ?");
            params.add(exceptRoleId, Hibernate.STRING);
        }
        // 查询结果
        Long c = (Long) super.getSession().createSQLQuery(sql.toString()).addScalar("c", Hibernate.LONG)
                .setParameters(params.getVals(), params.getTypes()).uniqueResult();
        return c.longValue() > 0;
    }

    /**
     * 根据用户ID获得该用户的所有角色ID
     * @param userId
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Resource> getAllResources() throws Exception
    {
        List<Resource> ress = new ArrayList<Resource>();
        String sql = "select id as id, name_ as name, parent_id as parent_id from sys_resource where type_= 2";
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("name", Hibernate.STRING).addScalar("parent_id", Hibernate.STRING).list();
        for (Object[] objs : objsList)
        {
            Resource s = new Resource();
            s.setId((String) objs[0]);
            s.setName((String) objs[1]);
            s.setParentId(StringUtil.isNotEmpty((String) objs[2]) ? (String) objs[2] : "0");
            ress.add(s);
        }
        return ress;
    }

    /**
     * 根据用户ID获得该用户的所有角色ID
     * @param userId
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Resource> getResourcesByRole(String roleId) throws Exception
    {
        List<Resource> ress = new ArrayList<Resource>();
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.resource_id, t.role_id from sys_role_resource t");
        sql.append(" where 1=1 and role_id = ? ");
        params.add(roleId, Hibernate.STRING);
        List<Object[]> objs = new ArrayList<Object[]>();
        objs = super.getSession().createSQLQuery(sql.toString()).addScalar("resource_id", Hibernate.STRING)
                .addScalar("role_id", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
        if (objs.size() > 0)
        {
            for (Object[] os : objs)
            {
                Resource s = new Resource();
                s.setId((String) os[0]);
                ress.add(s);
            }
        }
        return ress;
    }

    /**
     * 更新角色资源信息
     */
    public int updateRoleRess(String userID, String resourceIds) throws Exception
    {
        //先将用户原有的菜单删除
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("delete from RoleResource where 1=1");
        sql.append(" and roleId = ?");
        params.add(userID, Hibernate.STRING);
        int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        //插入角色菜单表
        String[] split = resourceIds.split(",");
        for (int i = 0; i < split.length; i++)
        {
            RoleResource roleResource = new RoleResource();
            roleResource.setRoleId(userID);
            roleResource.setResourceId(split[i]);
            roleResourceDao.save(roleResource);
        }
        return c;
    }

}
