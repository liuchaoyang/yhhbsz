package com.yzxt.yh.module.rpt.dao;

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
import com.yzxt.yh.module.rpt.bean.CheckReport;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

public class CheckReportDao extends HibernateSupport<CheckReport> implements BaseDao<CheckReport>
{
    /**
     * 插入客户体检信息
     */
    public String insert(CheckReport checkReport) throws Exception
    {
        super.save(checkReport);
        return checkReport.getId();
    }

    /**
     * 查询客户体检信息
     * */
    @SuppressWarnings("unchecked")
    public PageTran<CheckReport> queryTran(Map<String, Object> filter, Timestamp sysTime, int count)
    {
        PageTran<CheckReport> pageTran = new PageTran<CheckReport>();
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.cust_id, t.report_file_id, t.create_by, t.create_time");
        sql.append(", tu.name_ as cust_name, tfd.path as report_file_path");
        sql.append(" from rpt_check_report t");
        sql.append(" inner join sys_user tu on tu.id = t.cust_id");
        sql.append(" inner join sys_file_desc tfd on tfd.id = t.report_file_id");
        sql.append(" where 1=1");
        User operUser = (User) filter.get("operUser");
        // 权限
        if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
        // 医生查询自己的组织用户记录
        {
            String custId = (String) filter.get("custId");
            if (StringUtil.isNotEmpty(custId))
            {
                sql.append(" and t.cust_id = ?");
                params.add(custId, Hibernate.STRING);
            }
            else
            {
                sql.append(" and tu.org_id = ?");
                params.add(operUser.getOrgId(), Hibernate.STRING);
            }
        }
        else if (Constant.USER_TYPE_CUSTOMER.equals(operUser.getType()))
        // 普通客户查询自己的记录
        {
            sql.append(" and t.cust_id = ?");
            params.add(operUser.getId(), Hibernate.STRING);
        }
        else
        // 其它非管理员用户不能查询到记录
        {
            return pageTran;
        }
        // 同步方式
        if (sysTime == null)
        {
            sql.append(" order by t.create_time desc");
        }
        else
        {
            sql.append(" and t.create_time < ? order by t.create_time desc");
            params.add(sysTime, Hibernate.TIMESTAMP);
        }
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("report_file_id", Hibernate.STRING)
                .addScalar("create_by", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("cust_name", Hibernate.STRING).addScalar("report_file_path", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<CheckReport> list = new ArrayList<CheckReport>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            CheckReport cr = new CheckReport();
            idx = 0;
            cr.setId((String) objs[idx]);
            idx++;
            cr.setCustId((String) objs[idx]);
            idx++;
            cr.setReportFileId((String) objs[idx]);
            idx++;
            cr.setCreateBy((String) objs[idx]);
            idx++;
            cr.setCreateTime((Timestamp) objs[idx]);
            idx++;
            cr.setCustName((String) objs[idx]);
            idx++;
            cr.setReportFilePath((String) objs[idx]);
            idx++;
            list.add(cr);
        }
        pageTran.setData(list);
        return pageTran;
    }

    /**
     * 平台查询体检报告
     * @author h
     * @param filter
     * 2015.8.19
     */
    @SuppressWarnings("unchecked")
    public PageModel<CheckReport> query(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<CheckReport> pageModel = new PageModel<CheckReport>();
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from rpt_check_report t ");
        sql.append(" inner join sys_user su on su.id = t.cust_id ");
        sql.append(" inner join sys_customer tc on tc.user_id = t.cust_id");
        sql.append(" where 1 = 1");
        //查询条件
        String custName = (String) filter.get("custName");
        if (StringUtil.isNotEmpty(custName))
        {
            sql.append(" and su.name_ = ? ");
            params.add(custName, Hibernate.STRING);
        }
        String createTime = (String) filter.get("createTime");
        if (StringUtil.isNotEmpty(createTime))
        {
            sql.append(" and t.create_time = ? ");
            params.add(createTime, Hibernate.STRING);
        }
        //查询权限 医生可以查看该组织下的所有会员
        User user = (User) filter.get("operUser");
        if (Constant.USER_TYPE_DOCTOR.equals(user.getType()))
        // 医生查询自己的会员记录
        {
            //在医生相同组织下的，没有组织的全部成员建议放在云中兴腾下面
            if (user.getOrgId().equals("1"))
            {
                sql.append(" and (su.org_id = ? or su.org_id is null or su.org_id = '' ) ");
                params.add(user.getOrgId(), Hibernate.STRING);
            }
            else
            {
                sql.append(" and (su.org_id = ? ) ");
                params.add(user.getOrgId(), Hibernate.STRING);
            }
        }
        else if (Constant.USER_TYPE_CUSTOMER.equals(user.getType()))
        // 普通客户查询自己的记录
        {
            sql.append(" and t.cust_id = ?");
            params.add(user.getId(), Hibernate.STRING);
        }
        else
        // 其它非管理员用户不能查询到记录
        {
            return pageModel;
        }
        //总条数
        StringBuilder totalCountsql = new StringBuilder();
        totalCountsql.append("select count(*) as c ").append(sql);
        Integer totalCount = (Integer) super.getSession().createSQLQuery(totalCountsql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, su.name_, su.id_card, su.phone, tc.birthday ");
        pSql.append(", t.create_by, t.create_time, t.update_by, t.update_time");
        pSql.append(sql).append(" order by t.create_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
                .addScalar("phone", Hibernate.STRING).addScalar("birthday", Hibernate.DATE)
                .addScalar("create_by", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("update_by", Hibernate.STRING).addScalar("update_time", Hibernate.TIMESTAMP)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<CheckReport> list = new ArrayList<CheckReport>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            CheckReport checkReport = new CheckReport();
            idx = 0;
            checkReport.setId((String) objs[idx]);
            idx++;
            checkReport.setCustName((String) objs[idx]);
            idx++;
            checkReport.setIdCard((String) objs[idx]);
            idx++;
            checkReport.setPhone((String) objs[idx]);
            idx++;
            checkReport.setBirthday((Date) objs[idx]);
            idx++;
            checkReport.setCreateBy((String) objs[idx]);
            idx++;
            checkReport.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            checkReport.setUpdateBy((String) objs[idx]);
            idx++;
            checkReport.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            list.add(checkReport);
        }
        pageModel.setData(list);
        pageModel.setTotal(totalCount);
        return pageModel;
    }

    /**
     * 平台查看某人的体检报告的图片
     * @author h
     * @param filter
     * 2015.8.19
     */
    @SuppressWarnings("unchecked")
    public CheckReport getReport(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from rpt_check_report t ");
        sql.append(" inner join sys_file_desc sfd on sfd.id = t.report_file_id ");
        sql.append(" where 1 = 1 ");
        sql.append(" and t.id = ? ");
        String reportId = (String) filter.get("reportId");
        params.add(reportId, Hibernate.STRING);
        StringBuilder pSql = new StringBuilder();
        pSql.append(" select t.cust_id, sfd.path ").append(sql);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .addScalar("cust_id", Hibernate.STRING).addScalar("path", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        CheckReport checkReport = null;
        if (objsList != null && objsList.size() > 0)
        {
            checkReport = new CheckReport();
            Object[] objs = objsList.get(0);
            int idx = 0;
            checkReport.setCustId((String) objs[idx]);
            idx++;
            checkReport.setReportFilePath((String) objs[idx]);
            idx++;
        }
        return checkReport;
    }

}
