package com.yzxt.yh.module.sys.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstRole;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.sys.bean.Resource;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.bean.UserRole;
import com.yzxt.yh.util.StringUtil;

public class UserRoleDao extends HibernateSupport<UserRole> implements BaseDao<UserRole>
{
    /**
     * 根据用户ID获得该用户的所有角色ID
     * @param userId
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Collection<String> getRoleIdsByUser(String userId) throws Exception
    {
        HibernateParams params = new HibernateParams();
        String sql = "select t.roleId from UserRole t where t.userId = ?";
        params.add(userId, Hibernate.STRING);
        List<String> roles = super.getSession().createQuery(sql).setParameters(params.getVals(), params.getTypes())
                .list();
        return roles;
    }

    /**
     * 根据用户ID获得该用户的所有角色ID
     * @param userId
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Resource>[] getResourcesByUser(String userId) throws Exception
    {
        List<Resource>[] ress = new List[3];
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select trr.resource_id");
        sql.append(" from sys_user_role tur inner join sys_role_resource trr on trr.role_id = tur.role_id");
        sql.append(" where tur.user_id = ?");
        params.add(userId, Hibernate.STRING);
        List<String> ids = super.getSession().createSQLQuery(sql.toString()).addScalar("resource_id", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        if (ids == null || ids.isEmpty())
        {
            return null;
        }
        // 添加三级菜单
        List<Resource> lv2Ress = getResources(ids);
        if (lv2Ress == null || lv2Ress.isEmpty())
        {
            return null;
        }
        ress[2] = lv2Ress;
        // 添加二级菜单
        ids.clear();
        for (Resource res : lv2Ress)
        {
            ids.add(res.getParentId());
        }
        List<Resource> lv1Ress = getResources(ids);
        if (lv1Ress == null || lv1Ress.isEmpty())
        {
            return null;
        }
        ress[1] = lv1Ress;
        // 添加一级菜单
        ids.clear();
        for (Resource res : lv1Ress)
        {
            ids.add(res.getParentId());
        }
        List<Resource> lv0Ress = getResources(ids);
        if (lv0Ress == null || lv0Ress.isEmpty())
        {
            return null;
        }
        ress[0] = lv0Ress;
        return ress;
    }

    /**
     * 根据资源ID查找资源
     */
    @SuppressWarnings("unchecked")
    private List<Resource> getResources(Collection<String> ids)
    {
        if (ids == null || ids.isEmpty())
        {
            return null;
        }
        int idC = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.parent_id, t.name_, t.path, t.icon, t.seq");
        sql.append(" from sys_resource t");
        sql.append(" where t.id in (");
        for (String id : ids)
        {
            if (StringUtil.isNotEmpty(id))
            {
                sql.append(idC++ > 0 ? ", " : "").append("'").append(id).append("'");
            }
        }
        if (idC == 0)
        {
            return null;
        }
        sql.append(") order by t.seq asc");
        List<Object[]> objss = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("parent_id", Hibernate.STRING).addScalar("name_", Hibernate.STRING)
                .addScalar("path", Hibernate.STRING).addScalar("icon", Hibernate.STRING)
                .addScalar("seq", Hibernate.INTEGER).list();
        List<Resource> ress = new ArrayList<Resource>();
        for (Object[] objs : objss)
        {
            Resource res = new Resource();
            res.setId((String) objs[0]);
            res.setParentId((String) objs[1]);
            res.setName((String) objs[2]);
            res.setPath((String) objs[3]);
            res.setIcon((String) objs[4]);
            res.setSeq((Integer) objs[5]);
            ress.add(res);
        }
        return ress;
    }

    /**
     * 分页查询用户角色数据
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageModel<UserRole> query(Map<String, Object> filter, int page, int pageSize)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from sys_user tu");
        mSql.append(" left join sys_org tor on tor.id = tu.org_id");
        mSql.append(" where tu.type_ = 2");
        // 查询条件
        String userName = (String) filter.get("userName");
        if (StringUtil.isNotEmpty(userName))
        {
            mSql.append(" and tu.name_ like ").append(params.addLikePart(userName));
        }
        // 查询人
        User operUser = (User) filter.get("operUser");
        // 不是管理员
        Collection<String> roles = operUser.getRoles();
        if (!Constant.USER_TYPE_ADMIN.equals(operUser.getType()) || roles == null)
        {
            return new PageModel<UserRole>();
        }
        // 不是系统管理员，添加组织限制
        if (!roles.contains(ConstRole.ADMIN))
        {
            mSql.append(" and tor.full_id like ").append(params.addLikePart("/" + operUser.getOrgId() + "/"));
        }
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(tu.id) as c ").append(mSql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select tu.id as user_id, tu.name_ as user_name");
        pSql.append(", tor.orgName as org_name");
        pSql.append(", (select sr.name_ from sys_user_role sur inner join sys_role sr on sr.id = sur.role_id");
        pSql.append(" where sur.user_id = tu.id limit 0, 1) as role_name");
        pSql.append(mSql);
        pSql.append(" order by tu.update_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .addScalar("user_id", Hibernate.STRING).addScalar("user_name", Hibernate.STRING)
                .addScalar("org_name", Hibernate.STRING).addScalar("role_name", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<UserRole> list = new ArrayList<UserRole>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            UserRole ur = new UserRole();
            idx = 0;
            ur.setUserId((String) objs[idx]);
            idx++;
            ur.setUserName((String) objs[idx]);
            idx++;
            ur.setOrgName((String) objs[idx]);
            idx++;
            ur.setRoleName((String) objs[idx]);
            idx++;
            list.add(ur);
        }
        PageModel<UserRole> pageModel = new PageModel<UserRole>();
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 加载角色信息
     */
    public UserRole load(String id) throws Exception
    {
        return super.get(id);
    }

    /**
     * 删除用户角色信息
     */
    public int deleteRolesByUser(String userId) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("delete from UserRole where userId = ?");
        params.add(userId, Hibernate.STRING);
        int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

}
