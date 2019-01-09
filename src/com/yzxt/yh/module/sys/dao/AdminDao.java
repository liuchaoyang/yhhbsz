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
import com.yzxt.yh.constant.ConstRole;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.User;

public class AdminDao extends HibernateSupport<Customer> implements BaseDao<Customer>
{

    /**
     * 分页组织管理员数据
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageModel<User> query(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<User> pageModel = new PageModel<User>();
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from sys_user t");
        mSql.append(" where t.type_ = ").append(Constant.USER_TYPE_ADMIN);
        // 查询条件
        // 角色限定
        mSql.append(" and exists(select st.id from sys_user_role st where st.role_id = '").append(ConstRole.ORG_ADMIN)
                .append("'").append(" and st.user_id = t.id)");
        // 组织限定
        String orgId = (String) filter.get("orgId");
        mSql.append(" and t.org_id = ?");
        params.add(orgId, Hibernate.STRING);
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(t.id) as c ").append(mSql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.name_, t.account, t.phone, t.email, t.create_time");
        pSql.append(", (select sor.name_ from sys_org sor where sor.id = t.org_id) as org_name");
        pSql.append(mSql);
        pSql.append(" order by t.create_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("account", Hibernate.STRING)
                .addScalar("phone", Hibernate.STRING).addScalar("email", Hibernate.STRING)
                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("org_name", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<User> list = new ArrayList<User>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            User user = new User();
            idx = 0;
            user.setId((String) objs[idx]);
            idx++;
            user.setName((String) objs[idx]);
            idx++;
            user.setAccount((String) objs[idx]);
            idx++;
            user.setPhone((String) objs[idx]);
            idx++;
            user.setEmail((String) objs[idx]);
            idx++;
            user.setCreateTime((Timestamp) objs[idx]);
            idx++;
            user.setOrgName((String) objs[idx]);
            idx++;
            list.add(user);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

}
