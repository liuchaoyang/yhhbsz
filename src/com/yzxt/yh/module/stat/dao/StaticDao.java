package com.yzxt.yh.module.stat.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstRole;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.sys.bean.LoginRecord;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

public class StaticDao extends HibernateSupport<Object> implements BaseDao<Object>
{

    /**
     * 查询用户活跃数统计
     * @author h
     * 2015.7.21
     * @param pageSize 
     * @param page 
     * */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryUserNum(Map<String, Object> filter)
    {
        /*PageModel<Object> pageModel = new PageModel<Object>();*/
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from sys_login_record t");
        sql.append(" inner join sys_user su on su.id = t.user_id ");
        sql.append(" inner join sys_org so on so.id = su.org_id ");
        sql.append(" where 1 = 1 ");
        //查询条件
        // 权限过滤
        User operUser = (User) filter.get("operUser");
        if (Constant.USER_TYPE_ADMIN.equals(operUser.getType()))
        {
            Collection<String> roles = operUser.getRoles();
            // 不是系统管理员，添加组织限制
            if (roles != null && !roles.contains(ConstRole.ADMIN))
            {
                sql.append(" and so.full_id like ").append(params.addLikePart("/" + operUser.getOrgId() + "/"));
            }
        }
        else if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
        {
            String orgId = operUser.getOrgId();
            // 正常情况下医生所属组织不会为空
            if (StringUtil.isNotEmpty(orgId))
            {
                sql.append(" and so.org_id = ?");
                params.add(orgId, Hibernate.STRING);
            }
            else
            {
                return list;
            }
        }
        else if (!Constant.USER_TYPE_ADMIN.equals(operUser.getType()))
        {
            return list;
        }
        String typeStr = (String) filter.get("typeStr");
        if (typeStr != null)
        {
            sql.append(" and t.type_ = ? ");
            params.add(Integer.parseInt(typeStr), Hibernate.INTEGER);
        }
        Date startDate = (Date) filter.get("startDate");
        if (startDate != null)
        {
            sql.append(" and t.create_time >= ?");
            params.add(startDate, Hibernate.DATE);
        }
        Date endDate = (Date) filter.get("endDate");
        if (endDate != null)
        {
            sql.append(" and t.create_time < ?");
            params.add(endDate, Hibernate.DATE);
        }
        /*sql.append(" GROUP BY left(t.create_time,10),t.type_");*/
        sql.append(" GROUP BY su.org_id,t.type_");
        StringBuilder mSql = new StringBuilder();
        mSql.append("select count(t.id) as c, t.type_, so.name_ as orgName ").append(sql);
        mSql.append(" order by so.create_time desc ");
        /*mSql.append("select count(t.id) as c, t.type_, left(t.create_time,10) as createTime ").append(sql);
        mSql.append(" order by t.create_time desc ");*/
        /*params.add(pageSize*(page-1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);*/
        List<Object[]> objsList = super.getSession().createSQLQuery(mSql.toString()).addScalar("c", Hibernate.STRING)
                .addScalar("type_", Hibernate.INTEGER).addScalar("orgName", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        if (objsList != null)
        {
            int idx = 0;
            for (Object[] objs : objsList)
            {
                idx = 0;
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("c", objs[idx] != null ? objs[idx] : Integer.valueOf(0));
                idx++;
                map.put("type", objs[idx] != null ? objs[idx] : Integer.valueOf(0));
                idx++;
                map.put("orgName", objs[idx] != null ? objs[idx] : Integer.valueOf(0));
                idx++;
                list.add(map);
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public PageModel<LoginRecord> queryUser(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<LoginRecord> pageModel = new PageModel<LoginRecord>();
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from sys_login_record t ");
        sql.append(" inner join sys_user su on su.id = t.user_id");
        sql.append(" left join sys_org sor on sor.id = su.org_id");
        sql.append(" where 1 = 1");
        //查询条件
        // 权限过滤
        User operUser = (User) filter.get("operUser");
        if (Constant.USER_TYPE_ADMIN.equals(operUser.getType()))
        {
            Collection<String> roles = operUser.getRoles();
            // 不是系统管理员，添加组织限制
            if (roles != null && !roles.contains(ConstRole.ADMIN))
            {
                sql.append(" and sor.full_id like ").append(params.addLikePart("/" + operUser.getOrgId() + "/"));
            }
        }
        else if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
        {
            String orgId = operUser.getOrgId();
            // 正常情况下医生所属组织不会为空
            if (StringUtil.isNotEmpty(orgId))
            {
                sql.append(" and sor.org_id = ?");
                params.add(orgId, Hibernate.STRING);
            }
            else
            {
                return pageModel;
            }
        }
        else if (!Constant.USER_TYPE_ADMIN.equals(operUser.getType()))
        {
            return pageModel;
        }
        Date startDate = (Date) filter.get("startDate");
        if (startDate != null)
        {
            sql.append(" and t.create_time >= ?");
            params.add(startDate, Hibernate.DATE);
        }
        Date endDate = (Date) filter.get("endDate");
        if (endDate != null)
        {
            sql.append(" and t.create_time < ?");
            params.add(endDate, Hibernate.DATE);
        }
        //总条数
        StringBuilder tatolSql = new StringBuilder();
        tatolSql.append(" select count(t.user_id) as c ").append(sql);
        Integer total = (Integer) super.getSession().createSQLQuery(tatolSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        //分页查询
        StringBuilder pSql = new StringBuilder();
        pSql.append(" select t.user_id, t.id, su.name_, t.create_time, t.type_").append(sql)
                .append(" group by t.create_time");
        pSql.append(" order by t.create_time desc limit ?,?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .addScalar("user_id", Hibernate.STRING).addScalar("id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("type_", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).list();
        List<LoginRecord> list = new ArrayList<LoginRecord>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            LoginRecord loginRecord = new LoginRecord();
            idx = 0;
            loginRecord.setUserId((String) objs[idx]);
            idx++;
            loginRecord.setId((String) objs[idx]);
            idx++;
            loginRecord.setUserName((String) objs[idx]);
            idx++;
            loginRecord.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            loginRecord.setType((Integer) objs[idx]);
            idx++;
            list.add(loginRecord);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 查询个人的登录次数统计
     * @author h
     * 2015.7.23
     * */
    @SuppressWarnings("unchecked")
    public PageModel<LoginRecord> getDetail(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<LoginRecord> pageModel = new PageModel<LoginRecord>();
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from sys_login_record t ");
        sql.append(" where 1 = 1 ");
        //查询条件
        sql.append(" and t.user_id = ? ");
        String userId = (String) filter.get("userId");
        params.add(userId, Hibernate.STRING);
        Date startDate = (Date) filter.get("startDate");
        if (startDate != null)
        {
            sql.append(" and t.create_time >= ?");
            params.add(startDate, Hibernate.DATE);
        }
        Date endDate = (Date) filter.get("endDate");
        if (endDate != null)
        {
            sql.append(" and t.create_time < ?");
            params.add(endDate, Hibernate.DATE);
        }
        //查询总数
        StringBuilder tatolCount = new StringBuilder();
        tatolCount.append(" select count(t.id) as c ").append(sql);
        Integer tatol = (Integer) super.getSession().createSQLQuery(tatolCount.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        //分页显示
        StringBuilder pSql = new StringBuilder();
        pSql.append(" select t.id, t.user_id, t.type_, t.create_time ").append(sql);
        pSql.append(" order by t.create_time desc limit ?,?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("user_id", Hibernate.STRING).addScalar("type_", Hibernate.INTEGER)
                .addScalar("create_time", Hibernate.TIMESTAMP).setParameters(params.getVals(), params.getTypes())
                .list();
        List<LoginRecord> list = new ArrayList<LoginRecord>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            LoginRecord loginRecord = new LoginRecord();
            idx = 0;
            loginRecord.setId((String) objs[idx]);
            idx++;
            loginRecord.setUserId((String) objs[idx]);
            idx++;
            loginRecord.setType((Integer) objs[idx]);
            idx++;
            loginRecord.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            list.add(loginRecord);
        }
        pageModel.setTotal(tatol);
        pageModel.setData(list);
        return pageModel;
    }

}
