package com.yzxt.yh.module.chk.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.msg.bean.CheckRemind;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

public class CheckRemindDao extends HibernateSupport<CheckRemind> implements BaseDao<CheckRemind>
{

    /**
     * 查询提醒会员列
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public PageModel<CheckRemind> queryRemindSet(Map<String, Object> filter, int page, int pageSize) throws Exception
    {
        PageModel<CheckRemind> pageModel = new PageModel<CheckRemind>();
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from chk_physiolog_index t");
        mSql.append(" inner join sys_user tu on t.cust_id = tu.id");
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
        Integer minNoCheckDay = (Integer) filter.get("minNoCheckDay");
        if (minNoCheckDay != null && minNoCheckDay.intValue() > 0)
        {
            mSql.append(" and datediff(now(), t.last_check_time)-1 > ?");
            params.add(minNoCheckDay, Hibernate.INTEGER);
        }
        Integer remindIntervalDay = (Integer) filter.get("remindIntervalDay");
        if (remindIntervalDay != null)
        {
            if (remindIntervalDay.intValue() > 0)
            {
                mSql.append(" and t.remind_interval_day = ?");
                params.add(remindIntervalDay, Hibernate.INTEGER);
            }
            else if (remindIntervalDay.intValue() < 0)
            {
                mSql.append(" and t.remind_interval_day < 0");
            }
        }
        // 操作人
        User operUser = (User) filter.get("operUser");
        // 权限
        if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
        {
            mSql.append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = t.cust_id and smi.doctor_id = ? and smi.state = ?)");
            params.add(operUser.getId(), Hibernate.STRING);
            params.add(Constant.CUSTOMER_MEMBER_STATUS_YES, Hibernate.INTEGER);
        }
        else if (!Constant.USER_TYPE_ADMIN.equals(operUser.getType()))
        // 其它情况管理员不做控制
        {
            return pageModel;
        }
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(t.cust_id) as c ").append(mSql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.cust_id, t.last_check_item, t.last_check_time, t.remind_interval_day, datediff(now(), t.last_check_time)-1 as no_check_days");
        pSql.append(", tu.name_ as cust_name, tu.id_card, tu.create_time as cust_user_time");
        pSql.append(mSql);
        pSql.append(" order by t.last_check_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .addScalar("cust_id", Hibernate.STRING).addScalar("last_check_item", Hibernate.STRING)
                .addScalar("last_check_time", Hibernate.TIMESTAMP).addScalar("remind_interval_day", Hibernate.INTEGER)
                .addScalar("no_check_days", Hibernate.INTEGER).addScalar("cust_name", Hibernate.STRING)
                .addScalar("id_card", Hibernate.STRING).addScalar("cust_user_time", Hibernate.TIMESTAMP)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<CheckRemind> list = new ArrayList<CheckRemind>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            CheckRemind r = new CheckRemind();
            idx = 0;
            r.setCustId((String) objs[idx]);
            idx++;
            r.setLastCheckItem((String) objs[idx]);
            idx++;
            r.setLastCheckTime((Timestamp) objs[idx]);
            idx++;
            r.setRemindIntervalDay((Integer) objs[idx]);
            idx++;
            Integer noCheckDay = (Integer) objs[idx];
            r.setNoCheckDays(noCheckDay != null && noCheckDay.intValue() >= 0 ? noCheckDay : 0);
            idx++;
            r.setCustName((String) objs[idx]);
            idx++;
            r.setIdCard((String) objs[idx]);
            idx++;
            r.setCreateUserTime((Timestamp) objs[idx]);
            idx++;
            list.add(r);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 保存检测提醒设置
     * @return
     */
    public int saveSet(CheckRemind checkRemind)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("update chk_physiolog_index t");
        sql.append(" set t.remind_interval_day = :p0");
        sql.append(" where t.cust_id in (:p1)");
        int c = super.getSession().createSQLQuery(sql.toString())
                .setParameter("p0", checkRemind.getRemindIntervalDay(), Hibernate.INTEGER)
                .setParameterList("p1", checkRemind.getCustIds(), Hibernate.STRING).executeUpdate();
        return c;
    }

    /**
     * 查询需呀发送提醒的用户
     * @param filter
     * @param maxSize
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CheckRemind> getNeedRemindCust(Map<String, Object> filter, int maxSize)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.cust_id, datediff(now(), t.last_check_time)-1 as no_check_days");
        sql.append(", tu.name_ as cust_name, tu.phone as cust_phone");
        sql.append(" from chk_physiolog_index t");
        sql.append(" inner join sys_user tu on tu.id = t.cust_id");
        sql.append(" where 1=1");
        sql.append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = t.cust_id and smi.state = ?)");
        sql.append(" and t.remind_interval_day <= datediff(now(), (case when t.last_remind_time is null then t.last_check_time else t.last_remind_time end))-1");
        sql.append(" and t.remind_interval_day > 0");
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("cust_id", Hibernate.STRING).addScalar("no_check_days", Hibernate.INTEGER)
                .addScalar("cust_name", Hibernate.STRING).addScalar("cust_phone", Hibernate.STRING)
                .setParameter(0, Constant.CUSTOMER_MEMBER_STATUS_YES, Hibernate.INTEGER).list();
        List<CheckRemind> list = new ArrayList<CheckRemind>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            CheckRemind r = new CheckRemind();
            idx = 0;
            r.setCustId((String) objs[idx]);
            idx++;
            r.setNoCheckDays((Integer) objs[idx]);
            idx++;
            r.setCustName((String) objs[idx]);
            idx++;
            r.setCustPhone((String) objs[idx]);
            idx++;
            list.add(r);
        }
        return list;
    }

    /**
     * 保存提醒结果
     * @param checkRemind
     * @return
     */
    public int saveRemind(CheckRemind checkRemind)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("update chk_physiolog_index t");
        sql.append(" set t.last_remind_time = ?");
        sql.append(" where t.cust_id = ?");
        int c = super.getSession().createSQLQuery(sql.toString())
                .setParameter(0, checkRemind.getLastRemindTime(), Hibernate.TIMESTAMP)
                .setParameter(1, checkRemind.getCustId(), Hibernate.STRING).executeUpdate();
        return c;
    }

}
