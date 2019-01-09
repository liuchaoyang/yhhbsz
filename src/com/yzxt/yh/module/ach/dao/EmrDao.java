package com.yzxt.yh.module.ach.dao;

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
import com.yzxt.yh.module.ach.bean.Emr;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;

public class EmrDao extends HibernateSupport<Emr> implements BaseDao<Emr>
{

    /**
     * 添加电子病历
     * @throws Exception 
     */
    public String insert(Emr emr) throws Exception
    {
        super.insert(emr);
        return emr.getId();
    }

    /**
     * 修改电子病历
     * @throws Exception 
     */
    public int update(Emr emr) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update ach_emr set treat_date = ?, diagnosis = ?, test = ?, other = ?");
        sql.append(", update_by = ?, update_time = ?");
        sql.append(" where id = ?");
        params.add(emr.getTreatDate(), Hibernate.DATE).add(emr.getDiagnosis(), Hibernate.STRING)
                .add(emr.getTest(), Hibernate.STRING).add(emr.getOther(), Hibernate.STRING)
                .add(emr.getUpdateBy(), Hibernate.STRING).add(emr.getUpdateTime(), Hibernate.TIMESTAMP)
                .add(emr.getId(), Hibernate.STRING);
        int c = super.getSession().createSQLQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

    /**
     * 加载电子病历信息
     */
    @SuppressWarnings("unchecked")
    public Emr load(String id) throws Exception
    {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.cust_id, t.doctor_id, t.treat_date, t.diagnosis");
        sql.append(", t.test, t.other");
        sql.append(", t.create_by, t.create_time, t.update_by, t.update_time");
        sql.append(", (select sc.name_ from sys_user sc where sc.id = t.cust_id) as cust_name");
        sql.append(", (select su.name_ from sys_user su where su.id = t.doctor_id) as doctor_name");
        sql.append(" from ach_emr t");
        sql.append(" where t.id = ?");
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("doctor_id", Hibernate.STRING)
                .addScalar("treat_date", Hibernate.DATE).addScalar("diagnosis", Hibernate.STRING)
                .addScalar("test", Hibernate.STRING).addScalar("other", Hibernate.STRING)
                .addScalar("create_by", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("update_by", Hibernate.STRING).addScalar("update_time", Hibernate.TIMESTAMP)
                .addScalar("cust_name", Hibernate.STRING).addScalar("doctor_name", Hibernate.STRING)
                .setParameter(0, id).list();
        Emr emr = null;
        if (objsList != null && !objsList.isEmpty())
        {
            emr = new Emr();
            Object[] objs = objsList.get(0);
            int idx = 0;
            emr.setId((String) objs[idx]);
            idx++;
            emr.setCustId((String) objs[idx]);
            idx++;
            emr.setDoctorId((String) objs[idx]);
            idx++;
            emr.setTreatDate((Date) objs[idx]);
            idx++;
            emr.setDiagnosis((String) objs[idx]);
            idx++;
            emr.setTest((String) objs[idx]);
            idx++;
            emr.setOther((String) objs[idx]);
            idx++;
            emr.setCreateBy((String) objs[idx]);
            idx++;
            emr.setCreateTime((Timestamp) objs[idx]);
            idx++;
            emr.setUpdateBy((String) objs[idx]);
            idx++;
            emr.setUpdateTime((Timestamp) objs[idx]);
            idx++;
            emr.setCustName((String) objs[idx]);
            idx++;
            emr.setDoctorName((String) objs[idx]);
            idx++;
        }
        return emr;
    }

    /**
     * 分页查询电子病历数据
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageModel<Emr> queryCustEmr(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<Emr> pageModel = new PageModel<Emr>();
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from ach_emr t");
        mSql.append(" where 1=1");
        // 查询条件
        Date startTreatDate = (Date) filter.get("startTreatDate");
        if (startTreatDate != null)
        {
            mSql.append(" and t.treat_date >= ?");
            params.add(startTreatDate, Hibernate.DATE);
        }
        Date endTreatDate = (Date) filter.get("endTreatDate");
        if (endTreatDate != null)
        {
            mSql.append(" and t.treat_date < ?");
            params.add(DateUtil.addDay(endTreatDate, 1), Hibernate.DATE);
        }
        String custId = (String) filter.get("custId");
        mSql.append(" and t.cust_id = ?");
        params.add(custId, Hibernate.STRING);
        User operUser = (User) filter.get("operUser");
        if (operUser == null)
        {
            return pageModel;
        }
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(t.id) as c ").append(mSql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.cust_id, t.doctor_id, t.treat_date, t.diagnosis");
        pSql.append(", t.test, t.other, t.create_time, t.update_time");
        pSql.append(", (select su.name_ from sys_user su where su.id = t.doctor_id) as doctor_name");
        pSql.append(mSql);
        pSql.append(" order by t.create_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("doctor_id", Hibernate.STRING)
                .addScalar("treat_date", Hibernate.DATE).addScalar("diagnosis", Hibernate.STRING)
                .addScalar("test", Hibernate.STRING).addScalar("other", Hibernate.STRING)
                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("update_time", Hibernate.TIMESTAMP)
                .addScalar("doctor_name", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
        List<Emr> list = new ArrayList<Emr>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Emr emr = new Emr();
            idx = 0;
            emr.setId((String) objs[idx]);
            idx++;
            emr.setCustId((String) objs[idx]);
            idx++;
            emr.setDoctorId((String) objs[idx]);
            idx++;
            emr.setTreatDate((Date) objs[idx]);
            idx++;
            emr.setDiagnosis((String) objs[idx]);
            idx++;
            emr.setTest((String) objs[idx]);
            idx++;
            emr.setOther((String) objs[idx]);
            idx++;
            emr.setCreateTime((Timestamp) objs[idx]);
            idx++;
            emr.setUpdateTime((Timestamp) objs[idx]);
            idx++;
            emr.setDoctorName((String) objs[idx]);
            idx++;
            list.add(emr);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 客户端查询电子病历
     * @param filter
     * @param sysTime
     * @param count
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageTran<Emr> queryTran(Map<String, Object> filter, Timestamp sysTime, int count)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.cust_id, t.doctor_id, t.treat_date, t.diagnosis");
        sql.append(", t.test, t.other, t.create_time, t.update_time");
        sql.append(", (select su.name_ from sys_user su where su.id = t.doctor_id) as doctor_name");
        sql.append(" from ach_emr t");
        sql.append(" where 1=1");
        User operUser = (User) filter.get("operUser");
        if (operUser == null)
        {
            return new PageTran<Emr>();
        }
        // 查询条件
        // 用户ID
        String custId = (String) filter.get("custId");
        sql.append(" and t.cust_id = ?");
        params.add(custId, Hibernate.STRING);
        // 同步方式
        if (sysTime == null)
        {
            sql.append(" order by t.update_time desc");
        }
        else
        {
            sql.append(" and t.update_time < ? order by t.update_time desc");
            params.add(sysTime, Hibernate.TIMESTAMP);
        }
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("doctor_id", Hibernate.STRING)
                .addScalar("treat_date", Hibernate.DATE).addScalar("diagnosis", Hibernate.STRING)
                .addScalar("test", Hibernate.STRING).addScalar("other", Hibernate.STRING)
                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("update_time", Hibernate.TIMESTAMP)
                .addScalar("doctor_name", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
        List<Emr> list = new ArrayList<Emr>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Emr emr = new Emr();
            idx = 0;
            emr.setId((String) objs[idx]);
            idx++;
            emr.setCustId((String) objs[idx]);
            idx++;
            emr.setDoctorId((String) objs[idx]);
            idx++;
            emr.setTreatDate((Date) objs[idx]);
            idx++;
            emr.setDiagnosis((String) objs[idx]);
            idx++;
            emr.setTest((String) objs[idx]);
            idx++;
            emr.setOther((String) objs[idx]);
            idx++;
            emr.setCreateTime((Timestamp) objs[idx]);
            idx++;
            emr.setUpdateTime((Timestamp) objs[idx]);
            idx++;
            emr.setDoctorName((String) objs[idx]);
            idx++;
            list.add(emr);
        }
        return new PageTran<Emr>(list);
    }

}
