package com.yzxt.yh.module.chk.dao;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.yzxt.yh.module.chk.bean.Exam;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

public class ExamDao extends HibernateSupport<Exam> implements BaseDao<Exam>
{
    /**
     * 每日体检编号日期部分格式
     */
    private static SimpleDateFormat dfmt = new SimpleDateFormat("yyyyMMdd");
    private static DecimalFormat ifmt = new DecimalFormat("0000");

    /**
     * 插入体检信息
     */
    public String insert(Exam exam) throws Exception
    {
        super.save(exam);
        return exam.getId();
    }

    /**
     * 生成当天下一个体检编号
     * @throws ParseException 
     */
    public String getNextExamNo(Timestamp now) throws ParseException
    {
        HibernateParams params = new HibernateParams();
        String datePrefix = dfmt.format(now);
        Date today = dfmt.parse(datePrefix);
        StringBuilder sql = new StringBuilder();
        sql.append("select max(right(t.exam_no, 4)) as last_no");
        sql.append(" from chk_exam t");
        sql.append(" where t.create_time >= ?");
        params.add(today, Hibernate.DATE);
        String lastNo = (String) super.getSession().createSQLQuery(sql.toString())
                .addScalar("last_no", Hibernate.STRING).setParameters(params.getVals(), params.getTypes())
                .uniqueResult();
        if (StringUtil.isNotEmpty(lastNo))
        {
            return datePrefix + ifmt.format(Integer.parseInt(lastNo) + 1);
        }
        else
        {
            return datePrefix + "0001";
        }
    }

    /**
     * 查询体检记录
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public PageModel<Exam> queryCustExam(Map<String, Object> filter, int page, int pageSize) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from chk_exam t");
        mSql.append(" where 1=1");
        // 查询条件
        // 体检编号
        String examNo = (String) filter.get("examNo");
        if (StringUtil.isNotEmpty(examNo))
        {
            mSql.append(" and t.exam_no like ").append(params.addLikePart(examNo));
        }
        // 体检日期
        Date startExamDate = (Date) filter.get("startExamDate");
        if (startExamDate != null)
        {
            mSql.append(" and t.exam_date >= ?");
            params.add(startExamDate, Hibernate.DATE);
        }
        Date endExamDate = (Date) filter.get("endExamDate");
        if (endExamDate != null)
        {
            mSql.append(" and t.exam_date < ?");
            params.add(DateUtil.addDay(endExamDate, 1), Hibernate.DATE);
        }
        // 客户ID
        String custId = (String) filter.get("custId");
        mSql.append(" and t.cust_id = ?");
        params.add(custId, Hibernate.STRING);
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(t.id) as c ").append(mSql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.exam_no, t.exam_date, t.industry_name");
        pSql.append(mSql);
        pSql.append(" order by t.update_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("exam_no", Hibernate.STRING).addScalar("exam_date", Hibernate.DATE)
                .addScalar("industry_name", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
        List<Exam> list = new ArrayList<Exam>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Exam exam = new Exam();
            idx = 0;
            exam.setId((String) objs[idx]);
            idx++;
            exam.setExamNo((String) objs[idx]);
            idx++;
            exam.setExamDate((Date) objs[idx]);
            idx++;
            exam.setIndustryName((String) objs[idx]);
            idx++;
            list.add(exam);
        }
        PageModel<Exam> pageModel = new PageModel<Exam>();
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 查询体检记录
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public PageTran<Exam> queryCustExamTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.cust_id, t.exam_no, t.industry_name, t.exam_date, t.create_time");
        sql.append(", tu.name_ as cust_name, tu.id_card");
        sql.append(" from chk_exam t");
        sql.append(" inner join sys_user tu on tu.id = t.cust_id");
        sql.append(" where 1=1");
        // 查询条件
        String custId = (String) filter.get("custId");
        sql.append(" and t.cust_id = ?");
        params.add(custId, Hibernate.STRING);
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
                .addScalar("cust_id", Hibernate.STRING).addScalar("exam_no", Hibernate.STRING)
                .addScalar("industry_name", Hibernate.STRING).addScalar("exam_date", Hibernate.DATE)
                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("cust_name", Hibernate.STRING)
                .addScalar("id_card", Hibernate.STRING).setParameters(params.getVals(), params.getTypes())
                .setMaxResults(count).list();
        List<Exam> list = new ArrayList<Exam>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Exam e = new Exam();
            idx = 0;
            e.setId((String) objs[idx]);
            idx++;
            e.setCustId((String) objs[idx]);
            idx++;
            e.setExamNo((String) objs[idx]);
            idx++;
            e.setIndustryName((String) objs[idx]);
            idx++;
            e.setExamDate((Date) objs[idx]);
            idx++;
            e.setCreateTime((Timestamp) objs[idx]);
            idx++;
            e.setName((String) objs[idx]);
            idx++;
            e.setIdCard((String) objs[idx]);
            idx++;
            list.add(e);
        }
        return new PageTran<Exam>(list);
    }

}
