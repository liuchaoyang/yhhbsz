package com.yzxt.yh.module.chk.dao;

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
import com.yzxt.yh.module.chk.bean.BloodSugar;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

public class BloodSugarDao extends HibernateSupport<BloodSugar> implements BaseDao<BloodSugar>
{
    /**
     * 插入血糖信息
     */
    public String insert(BloodSugar bean) throws Exception
    {
        super.save(bean);
        return bean.getId();
    }

    /**
     * 查询血糖列表
     * @param filter 过滤条件
     * @param sysTime 同步时间点
     * @param dir 同步方式，大于0：取时间点后的数据，小于0取时间点前的数据
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageTran<BloodSugar> queryTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t from BloodSugar t where 1=1");
        // 查询条件
        String custId = (String) filter.get("custId");
        if (StringUtil.isNotEmpty(custId))
        {
            sql.append(" and t.custId = ?");
            params.add(custId, Hibernate.STRING);
        }
        else
        {
            return new PageTran<BloodSugar>();
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
        List<BloodSugar> list = super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).setMaxResults(count).list();
        return new PageTran<BloodSugar>(list);
    }

    /**
     * 查询血糖列表
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageModel<BloodSugar> query(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<BloodSugar> pageModel = new PageModel<BloodSugar>();
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from chk_blood_sugar t");
        // mSql.append(" left join chk_check_warn tcw on tcw.id = t.id");
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
        pSql.append("select t.id, t.blood_sugar, t.blood_sugar_type, t.meal_type, t.time_to_meal, t.check_time");
        pSql.append(", t.level, t.descript");
        pSql.append(mSql);
        pSql.append(" order by t.check_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("blood_sugar", Hibernate.DOUBLE).addScalar("blood_sugar_type", Hibernate.INTEGER)
                .addScalar("meal_type", Hibernate.INTEGER).addScalar("time_to_meal", Hibernate.DOUBLE)
                .addScalar("check_time", Hibernate.TIMESTAMP).addScalar("level", Hibernate.INTEGER)
                .addScalar("descript", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
        List<BloodSugar> list = new ArrayList<BloodSugar>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            BloodSugar b = new BloodSugar();
            idx = 0;
            b.setId((String) objs[idx]);
            idx++;
            b.setBloodSugar((Double) objs[idx]);
            idx++;
            b.setBloodSugarType((Integer) objs[idx]);
            idx++;
            b.setMealType((Integer) objs[idx]);
            idx++;
            b.setTimeToMeal((Double) objs[idx]);
            idx++;
            b.setCheckTime((Timestamp) objs[idx]);
            idx++;
            b.setLevel((Integer) objs[idx]);
            idx++;
            b.setDescript((String) objs[idx]);
            idx++;
            list.add(b);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 血糖检测记录数
     * 
     * @param filter
     * @return
     * @throws Exception
     */
    public int queryDataCount(Map<String, Object> filter) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(t.id) as c from BloodSugar t where 1=1");
        String custId = (String) filter.get("custId");
        if (StringUtil.isNotEmpty(custId))
        {
            sql.append(" and t.custId = ?");
            params.add(custId, Hibernate.STRING);
        }
        Date startDate = (Date) filter.get("startDate");
        if (startDate != null)
        {
            sql.append(" and t.checkTime >= ?");
            params.add(startDate, Hibernate.DATE);
        }
        Date endDate = (Date) filter.get("endDate");
        if (endDate != null)
        {
            sql.append(" and t.checkTime < ?");
            // 由于没有时间部分补上时间
            params.add(DateUtil.addDay(endDate, 1), Hibernate.DATE);
        }
        Long c = (Long) super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).uniqueResult();
        return c.intValue();
    }

    /**
     * 获取健康分析报告的数据
     * @param custId
     * @param startDate
     * @param endDate
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object[] getAnaRptData(String custId, Date startDate, Date endDate)
    {
        endDate = DateUtil.addDay(endDate, 1);
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.blood_sugar_type, t.blood_sugar, t.check_time, t.level");
        sql.append(" from chk_blood_sugar t where 1=1");
        sql.append(" and t.cust_id = ? and t.check_time >= ? and t.check_time < ?");
        sql.append(" order by t.check_time asc");
        params.add(custId, Hibernate.STRING);
        params.add(startDate, Hibernate.DATE);
        params.add(endDate, Hibernate.DATE);
        List<Object[]> list = super.getSession().createSQLQuery(sql.toString())
                .addScalar("blood_sugar_type", Hibernate.INTEGER).addScalar("blood_sugar", Hibernate.DOUBLE)
                .addScalar("check_time", Hibernate.TIMESTAMP).addScalar("level", Hibernate.INTEGER)
                .setParameters(params.getVals(), params.getTypes()).list();
        return new Object[]
        {list};
    }

}
