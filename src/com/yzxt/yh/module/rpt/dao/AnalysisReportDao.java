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
import com.yzxt.yh.module.rpt.bean.AnalysisReport;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

public class AnalysisReportDao extends HibernateSupport<AnalysisReport> implements BaseDao<AnalysisReport>
{
    /**
     * 插入分析报告
     */
    public String insert(AnalysisReport analysisReport) throws Exception
    {
        super.save(analysisReport);
        return analysisReport.getId();
    }

    /**
     * 查询分析报告数
     * 
     * @param filter
     * @return
     * @throws Exception
     */
    public int queryReportCount(Map<String, Object> filter) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(t.id) as c from AnalysisReport t where 1=1");
        // 报告类型
        Integer reportType = (Integer) filter.get("reportType");
        if (reportType != null)
        {
            sql.append(" and t.reportType = ?");
            params.add(reportType, Hibernate.INTEGER);
        }
        String custId = (String) filter.get("custId");
        if (StringUtil.isNotEmpty(custId))
        {
            sql.append(" and t.custId = ?");
            params.add(custId, Hibernate.STRING);
        }
        Date startDate = (Date) filter.get("startDate");
        if (startDate != null)
        {
            sql.append(" and t.examBeginTime = ?");
            params.add(startDate, Hibernate.DATE);
        }
        Date endDate = (Date) filter.get("endDate");
        if (endDate != null)
        {
            sql.append(" and t.examEndTime = ?");
            // 由于没有时间部分补上时间
            params.add(endDate, Hibernate.DATE);
        }
        Long c = (Long) super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).uniqueResult();
        return c.intValue();
    }

    /**
     * 分页查询分析报告数据
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageModel<AnalysisReport> query(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<AnalysisReport> pageModel = new PageModel<AnalysisReport>();
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from rpt_analysis_report t");
        mSql.append(" inner join sys_user tu on tu.id = t.cust_id");
        mSql.append(" inner join sys_customer tc on tc.user_id = t.cust_id");
        mSql.append(" where 1=1");
        // 查询条件
        String custName = (String) filter.get("custName");
        if (StringUtil.isNotEmpty(custName))
        {
            mSql.append(" and tu.name_ like ").append(params.addLikePart(custName));
        }
        String idCard = (String) filter.get("idCard");
        if (StringUtil.isNotEmpty(idCard))
        {
            mSql.append(" and tu.id_card like ").append(params.addLikePart(idCard));
        }
        Integer reportType = (Integer) filter.get("reportType");
        if (reportType != null)
        {
            mSql.append(" and t.report_type = ?");
            params.add(reportType, Hibernate.INTEGER);
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
            if (Constant.USER_TYPE_DOCTOR.equals(user.getType()))
            // 医生查询自己的会员记录
            {
                mSql.append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = t.cust_id and smi.doctor_id = ? and smi.state = ?)");
                params.add(user.getId(), Hibernate.STRING);
                params.add(Constant.CUSTOMER_MEMBER_STATUS_YES, Hibernate.INTEGER);
            }
            else if (Constant.USER_TYPE_CUSTOMER.equals(user.getType()))
            // 普通客户查询自己的记录
            {
                mSql.append(" and t.cust_id = ?");
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
        pSql.append("select t.id, t.cust_id, t.exam_begin_time, t.exam_end_time, t.report_type, t.suggest");
        pSql.append(", t.create_by, t.create_time, t.update_by, t.update_time");
        pSql.append(", tu.name_ as cust_name, tu.id_card, tu.phone, tc.birthday");
        pSql.append(mSql);
        pSql.append(" order by t.create_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("exam_begin_time", Hibernate.DATE)
                .addScalar("exam_end_time", Hibernate.DATE).addScalar("report_type", Hibernate.INTEGER)
                .addScalar("suggest", Hibernate.STRING).addScalar("create_by", Hibernate.STRING)
                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("update_by", Hibernate.STRING)
                .addScalar("update_time", Hibernate.TIMESTAMP).addScalar("cust_name", Hibernate.STRING)
                .addScalar("id_card", Hibernate.STRING).addScalar("phone", Hibernate.STRING)
                .addScalar("birthday", Hibernate.DATE).setParameters(params.getVals(), params.getTypes()).list();
        List<AnalysisReport> list = new ArrayList<AnalysisReport>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            AnalysisReport anaRpt = new AnalysisReport();
            idx = 0;
            anaRpt.setId((String) objs[idx]);
            idx++;
            anaRpt.setCustId((String) objs[idx]);
            idx++;
            anaRpt.setExamBeginTime((Date) objs[idx]);
            idx++;
            anaRpt.setExamEndTime((Date) objs[idx]);
            idx++;
            anaRpt.setReportType((Integer) objs[idx]);
            idx++;
            anaRpt.setSuggest((String) objs[idx]);
            idx++;
            anaRpt.setCreateBy((String) objs[idx]);
            idx++;
            anaRpt.setCreateTime((Timestamp) objs[idx]);
            idx++;
            anaRpt.setUpdateBy((String) objs[idx]);
            idx++;
            anaRpt.setUpdateTime((Timestamp) objs[idx]);
            idx++;
            anaRpt.setCustomerName((String) objs[idx]);
            idx++;
            anaRpt.setIdCard((String) objs[idx]);
            idx++;
            anaRpt.setPhone((String) objs[idx]);
            idx++;
            anaRpt.setBirthday((Date) objs[idx]);
            idx++;
            list.add(anaRpt);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 客户端分页查询自己分析报告数据
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageTran<AnalysisReport> queryMyTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t from AnalysisReport t where 1=1");
        // 查询条件
        // 报告类型
        Integer reportType = (Integer) filter.get("reportType");
        if (reportType != null && reportType.intValue() > 0)
        {
            sql.append(" and t.reportType = ?");
            params.add(reportType, Hibernate.INTEGER);
        }
        String custId = (String) filter.get("custId");
        if (StringUtil.isNotEmpty(custId))
        {
            sql.append(" and t.custId = ?");
            params.add(custId, Hibernate.STRING);
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
        List<AnalysisReport> list = super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).setMaxResults(count).list();
        return new PageTran<AnalysisReport>(list);
    }

    /**
     * 保存评估建议
     * @return
     */
    public int saveSuggest(AnalysisReport report)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update AnalysisReport set suggest = ?, updateBy = ?, updateTime = ?");
        sql.append(" where id = ?");
        params.add(report.getSuggest(), Hibernate.STRING);
        params.add(report.getUpdateBy(), Hibernate.STRING);
        params.add(report.getUpdateTime(), Hibernate.TIMESTAMP);
        params.add(report.getId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

}
