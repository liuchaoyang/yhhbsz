package com.yzxt.yh.module.his.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstHis;
import com.yzxt.yh.module.his.bean.Appoint;
import com.yzxt.yh.module.his.bean.BaseArea;
import com.yzxt.yh.module.his.bean.Dept;
import com.yzxt.yh.util.StringUtil;

public class DeptDao extends HibernateSupport<Dept> implements BaseDao<Dept>
{
    public String insert(Dept bean) throws Exception
    {
        super.save(bean);
        return bean.getId();
    }

    @SuppressWarnings("unchecked")
    public PageTran<Dept> queryDeptTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t from Dept t where 1=1");
        sql.append(" and t.level = ?");
        params.add(ConstHis.HIS_LEVEL_YY, Hibernate.INTEGER);
        /*String parentId = (String) filter.get("parentId");
        if (StringUtil.isNotEmpty(parentId))
        {
            sql.append(" and t.parentId = ?");
            params.add(parentId, Hibernate.STRING);
        }*/
        /*Integer level = (Integer) filter.get("level");
        if (StringUtil.isNotEmpty(level.toString()))
        {
            sql.append(" and t.level = ?");
            params.add(level, Hibernate.INTEGER);
        }*/
        // 查询条件
        /*String custId = (String) filter.get("custId");
        if (StringUtil.isNotEmpty(custId))
        {
            sql.append(" and t.custId = ?");
            params.add(custId, Hibernate.STRING);
        }
        else
        {
            return new PageTran<Dept>();
        }*/
        // 同步方式
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
            else
            {
                sql.append(" and t.createTime > ? order by t.createTime asc");
            }
            params.add(sysTime, Hibernate.TIMESTAMP);
        }
        List<Dept> list = super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).setMaxResults(count).list();
        return new PageTran<Dept>(list);
    }
    
    /**
     * 客户端获取医院科室列表
     * @author h
     * 2016.1.22
     * */
    @SuppressWarnings("unchecked")
    public PageTran<Dept> queryDepartTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.parent_id, t.name_, t.level, t.memo from his_dept t " );
        sql.append(" where 1=1 and (t.parent_id = ? or t.parent_id in ");
        sql.append("(select DISTINCT t.id from his_dept t where 1=1 and t.parent_id = ? )) ");
        // 查询条件
        String parentId = (String) filter.get("parentId");
        params.add(parentId, Hibernate.STRING);
        params.add(parentId, Hibernate.STRING);
        // 同步方式
        if (sysTime == null)
        {
            sql.append(" order by t.create_Time desc");
        }
        else if (dir < 0)
        {
            if (dir < 0)
            {
                sql.append(" and t.create_Time < ? order by t.create_Time desc");
            }
            else
            {
                sql.append(" and t.create_Time > ? order by t.create_Time asc");
            }
            params.add(sysTime, Hibernate.TIMESTAMP);
        }
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("id", Hibernate.STRING).addScalar("parent_id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("level", Hibernate.INTEGER)
                .addScalar("memo", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes())
                .list();
        List<Dept> list = new ArrayList<Dept>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Dept bean = new Dept();
            idx = 0;
            bean.setId((String) objs[idx]);
            idx++;
            bean.setParentId((String) objs[idx]);
            idx++;
            bean.setName((String) objs[idx]);
            idx++;
            bean.setLevel((Integer) objs[idx]);
            idx++;
            bean.setMemo((String) objs[idx]);
            idx++;
            list.add(bean);
        }
        return new PageTran<Dept>(list);
       /* List<Dept> list = super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).setMaxResults(count).list();
        return new PageTran<Dept>(list);*/
    }

    /**
     * 平台查询医院列表
     * @author h
     * 2016.1.22
     * */
    @SuppressWarnings("unchecked")
    public List<Dept> queryDept(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.parent_id, t.name_, t.level, t.memo,t.areaid from his_dept t " );
        String parentId = (String) filter.get("parentId");
        if(StringUtil.isNotEmpty(parentId))
        {
            sql.append(" where 1=1 and t.parent_id = ?");
            //sql.append("(select DISTINCT t.id from his_dept t where 1=1 and t.parent_id = ? )) ");
            // 查询条件
            params.add(parentId, Hibernate.STRING);
            //params.add(parentId, Hibernate.STRING);
        }
        else
        {
            sql.append(" where 1=1 and t.level = ?");
            params.add(ConstHis.HIS_LEVEL_YY, Hibernate.INTEGER);
        }
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("id", Hibernate.STRING).addScalar("parent_id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("level", Hibernate.INTEGER)
                .addScalar("memo", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes())
                .list();
        List<Dept> list = new ArrayList<Dept>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Dept bean = new Dept();
            idx = 0;
            bean.setId((String) objs[idx]);
            idx++;
            bean.setParentId((String) objs[idx]);
            idx++;
            bean.setName((String) objs[idx]);
            idx++;
            bean.setLevel((Integer) objs[idx]);
            idx++;
            bean.setMemo((String) objs[idx]);
            idx++;
            list.add(bean);
        }
        return list;
    }
    /**
     * 获取地区列表
     * @author h
     * 2016.1.22
     * */
    @SuppressWarnings("unchecked")
    public List<BaseArea> queryArea(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.codeid, t.parentid, t.cityName from base_area t " );
        String parentId = (String) filter.get("parentId");
        if(StringUtil.isNotEmpty(parentId))
        {
            sql.append(" where 1=1 and t.parentid = ?");
            // 查询条件
            params.add(parentId, Hibernate.STRING);
        }
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("codeid", Hibernate.STRING).addScalar("parentid", Hibernate.STRING)
                .addScalar("cityName", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes())
                .list();
        List<BaseArea> list = new ArrayList<BaseArea>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
        	BaseArea bean = new BaseArea();
            idx = 0;
            bean.setCodeId(Integer.parseInt(objs[idx].toString()));
            idx++;
            bean.setParentId(Integer.parseInt(objs[idx].toString()));
            idx++;
            bean.setCityName((String) objs[idx]);
            idx++;
            list.add(bean);
        }
        return list;
    }
}
