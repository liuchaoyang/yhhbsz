package com.yzxt.yh.module.cli.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.cli.bean.Step;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

public class StepDao extends HibernateSupport<Step> implements BaseDao<Step>
{
    /**
     * 插入计步信息记录
     * @author h
     * 2015.11.4
     */
    public String insert(Step bean) throws Exception
    {
        super.insert(bean);
        return bean.getId();
    }
    
    /**
     * 客户端查询计步列表
     * @param filter 过滤条件
     * @param sysTime 同步时间点
     * @param dir 同步方式，大于0：取时间点后的数据，小于0取时间点前的数据
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageTran<Step> queryTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t from Step t where 1=1");
        // 查询条件
        String custId = (String) filter.get("custId");
        if (StringUtil.isNotEmpty(custId))
        {
            sql.append(" and t.custId = ?");
            params.add(custId, Hibernate.STRING);
        }
        else
        {
            return new PageTran<Step>();
        }
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
        List<Step> list = super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).setMaxResults(count).list();
        return new PageTran<Step>(list);
    }

    /**
     * 平台查询计步列表
     * @return
     * @author h
     * 2015.11.5
     */
    @SuppressWarnings("unchecked")
    public PageModel<Step> query(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<Step> pageModel = new PageModel<Step>();
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from cli_step t");
        mSql.append(" where 1=1");
        // 查询条件
        Date startDate = (Date) filter.get("startDate");
        if (startDate != null)
        {
            mSql.append(" and t.check_time >= ?");
            params.add(startDate, Hibernate.DATE);
        }
        Date endDate = (Date) filter.get("endDate");
        if (endDate != null)
        {
            mSql.append(" and t.check_time < ?");
            params.add(DateUtil.addDay(endDate, 1), Hibernate.DATE);
        }
        String custId = (String) filter.get("custId");
        if (StringUtil.isNotEmpty(custId))
        {
            mSql.append(" and t.cust_id = ?");
            params.add(custId, Hibernate.STRING);
        }
        else
        {
            // 操作人
            User user = (User) filter.get("operUser");
            // 权限
            if (Constant.USER_TYPE_CUSTOMER.equals(user.getType()))
            {
                mSql.append(" and t.cust_id = ?");
                params.add(user.getId(), Hibernate.STRING);
            }
            else if (Constant.USER_TYPE_DOCTOR.equals(user.getType()))
            // 医生查询自己的会员记录
            {
                mSql.append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = t.cust_id and smi.doctor_id = ? and smi.state = 1)");
                params.add(user.getId(), Hibernate.STRING);
            }
            else
            // 其它非管理员用户不能查询到记录
            {
                return pageModel;
            }
        }
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(t.id) as c ").append(mSql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.total_steps, t.total_dis, t.calories, t.check_time");
        pSql.append(mSql);
        pSql.append(" order by t.check_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("total_steps", Hibernate.INTEGER).addScalar("total_dis", Hibernate.INTEGER)
                .addScalar("calories", Hibernate.DOUBLE).addScalar("check_time", Hibernate.TIMESTAMP)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Step> list = new ArrayList<Step>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Step bean = new Step();
            idx = 0;
            bean.setId((String) objs[idx]);
            idx++;
            bean.setTotalSteps((Integer) objs[idx]);
            idx++;
            bean.setTotalDis((Integer) objs[idx]);
            idx++;
            bean.setCalories((Double) objs[idx]);
            idx++;
            bean.setCheckTime((Timestamp) objs[idx]);
            idx++;
            list.add(bean);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }
    
}
