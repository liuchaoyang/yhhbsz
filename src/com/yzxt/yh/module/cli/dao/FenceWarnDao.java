package com.yzxt.yh.module.cli.dao;

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
import com.yzxt.yh.module.chk.bean.CheckWarn;
import com.yzxt.yh.module.cli.bean.FenceWarn;
import com.yzxt.yh.util.StringUtil;

public class FenceWarnDao extends HibernateSupport<FenceWarn> implements BaseDao<FenceWarn>
{
    /**
     * 插入围栏告警信息
     */
    public String insert(FenceWarn bean) throws Exception
    {
        super.save(bean);
        return bean.getId();
    }

    /**
     * 分页查询电子围栏告警信息
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageModel<FenceWarn> query(Map<String, Object> filter, int page, int pageSize)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from cli_fence_warn t");
        sql.append(" where 1 = 1");
        //查询条件
        // 设备编号
        String deviceNo = (String) filter.get("deviceNo");
        if (StringUtil.isNotEmpty(deviceNo))
        {
            sql.append(" and t.device_no = ?");
            params.add(deviceNo, Hibernate.STRING);
        }
        // 客户ID
        String custId = (String) filter.get("custId");
        if (StringUtil.isNotEmpty(custId))
        {
            sql.append(" and t.cust_id = ?");
            params.add(custId, Hibernate.STRING);
        }
        String beginDate = (String) filter.get("beginDate");
        if (beginDate != null && beginDate.length() > 0)
        {
            sql.append(" and t.warn_time >= '").append(beginDate).append("'");
        }
        String endDate = (String) filter.get("endDate");
        if (endDate != null && endDate.length() > 0)
        {
            sql.append(" and t.warn_time < adddate('").append(endDate).append("', interval 1 day)");
        }
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append(" select count(*) as c  ").append(sql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.cust_id, t.device_no, t.longitude, t.latitude");
        pSql.append(", t.warn_time, t.descript, t.address, t.create_time");
        pSql.append(sql);
        pSql.append(" order by t.warn_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("device_no", Hibernate.STRING)
                .addScalar("longitude", Hibernate.DOUBLE).addScalar("latitude", Hibernate.DOUBLE)
                .addScalar("warn_time", Hibernate.TIMESTAMP).addScalar("descript", Hibernate.STRING)
                .addScalar("address", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<FenceWarn> list = new ArrayList<FenceWarn>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            FenceWarn fw = new FenceWarn();
            idx = 0;
            fw.setId((String) objs[idx]);
            idx++;
            fw.setCustId((String) objs[idx]);
            idx++;
            fw.setDeviceNo((String) objs[idx]);
            idx++;
            fw.setLongitude((Double) objs[idx]);
            idx++;
            fw.setLatitude((Double) objs[idx]);
            idx++;
            fw.setWarnTime((Timestamp) objs[idx]);
            idx++;
            fw.setDescript((String) objs[idx]);
            idx++;
            fw.setAddress((String) objs[idx]);
            idx++;
            fw.setCreateTime((Timestamp) objs[idx]);
            idx++;
            list.add(fw);
        }
        PageModel<FenceWarn> pageModel = new PageModel<FenceWarn>();
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 客户端查询电子围栏告警信息
     * @param filter
     * @param sysTime
     * @param dir
     * @param count
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageTran<FenceWarn> queryTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.cust_id, t.device_no, t.longitude, t.latitude");
        sql.append(", t.warn_time, t.descript, t.address, t.create_time");
        sql.append(" from cli_fence_warn t");
        sql.append(" where 1=1");
        // 设备编号
        String deviceNo = (String) filter.get("deviceNo");
        if (StringUtil.isNotEmpty(deviceNo))
        {
            sql.append(" and t.device_no = ?");
            params.add(deviceNo, Hibernate.STRING);
        }
        // 客户ID
        String custId = (String) filter.get("custId");
        if (StringUtil.isNotEmpty(custId))
        {
            sql.append(" and t.cust_id = ?");
            params.add(custId, Hibernate.STRING);
        }
        // 同步方式
        if (sysTime == null)
        {
            sql.append(" order by t.create_time desc");
        }
        else if (dir < 0)
        {
            if (dir < 0)
            {
                sql.append(" and t.create_time < ? order by t.create_time desc");
            }
            else
            {
                sql.append(" and t.create_time > ? order by t.create_time asc");
            }
            params.add(sysTime, Hibernate.TIMESTAMP);
        }
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("device_no", Hibernate.STRING)
                .addScalar("longitude", Hibernate.DOUBLE).addScalar("latitude", Hibernate.DOUBLE)
                .addScalar("warn_time", Hibernate.TIMESTAMP).addScalar("descript", Hibernate.STRING)
                .addScalar("address", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .setParameters(params.getVals(), params.getTypes()).setMaxResults(count).list();
        List<FenceWarn> list = new ArrayList<FenceWarn>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            FenceWarn fw = new FenceWarn();
            idx = 0;
            fw.setId((String) objs[idx]);
            idx++;
            fw.setCustId((String) objs[idx]);
            idx++;
            fw.setDeviceNo((String) objs[idx]);
            idx++;
            fw.setLongitude((Double) objs[idx]);
            idx++;
            fw.setLatitude((Double) objs[idx]);
            idx++;
            fw.setWarnTime((Timestamp) objs[idx]);
            idx++;
            fw.setDescript((String) objs[idx]);
            idx++;
            fw.setAddress((String) objs[idx]);
            idx++;
            fw.setCreateTime((Timestamp) objs[idx]);
            idx++;
            list.add(fw);
        }
        return new PageTran<FenceWarn>(list);
    }

}
