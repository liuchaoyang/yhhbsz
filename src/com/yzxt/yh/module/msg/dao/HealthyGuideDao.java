package com.yzxt.yh.module.msg.dao;

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
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.CheckWarn;
import com.yzxt.yh.module.msg.bean.HealthyGuide;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

public class HealthyGuideDao extends HibernateSupport<HealthyGuide> implements BaseDao<HealthyGuide>
{
    /**
     * 插入客户信息
     */
    public String insert(HealthyGuide healthyGuide) throws Exception
    {
        super.save(healthyGuide);
        return healthyGuide.getId();
    }

    @SuppressWarnings("unchecked")
    public PageModel<HealthyGuide> getList(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<HealthyGuide> pageModel = new PageModel<HealthyGuide>();
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from msg_healthy_guide t ");
        sql.append(" inner join sys_customer sc on sc.user_id = t.cust_id");
        sql.append(" inner join sys_user su on su.id = sc.user_id");
        sql.append(" inner join sys_user su2 on su2.id = t.create_by");
        sql.append(" inner join svb_member_info sm on sm.cust_id = su.id");
        sql.append(" where 1= 1 and sm.state = 1");
        //查询条件
        String memberName = (String) filter.get("memberName");
        if (StringUtil.isNotEmpty(memberName))
        {
            sql.append(" and su.name_ like ").append(params.addLikePart(memberName));
        }
        String custId = (String) filter.get("custId");
        if (StringUtil.isNotEmpty(custId) && !custId.equals("null"))
        {
            sql.append(" and t.cust_id = ?");
            params.add(custId, Hibernate.STRING);
        }
        String directReason = (String) filter.get("directReason");
        if (StringUtil.isNotEmpty(directReason))
        {
            sql.append(" and t.direct_reason like ").append(params.addLikePart(directReason));
        }
        String beginDate = (String) filter.get("beginDate");
        if (beginDate != null && beginDate.length() > 0)
        {
            sql.append(" and t.create_time >= '").append(beginDate).append("'");
        }
        String endDate = (String) filter.get("endDate");
        if (endDate != null && endDate.length() > 0)
        {
            sql.append(" and t.create_time < adddate('").append(endDate).append("', interval 1 day)");
        }
        // 权限
        User user = (User) filter.get("operUser");
        if (Constant.USER_TYPE_DOCTOR.equals(user.getType()))
        // 医生查询自己的会员记录
        {
            sql.append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = t.cust_id and smi.doctor_id = ? and smi.state = 1)");
            params.add(user.getId(), Hibernate.STRING);
        }
        else if (Constant.USER_TYPE_CUSTOMER.equals(user.getType()) && !StringUtil.isNotEmpty(custId))
        // 普通客户查询自己的记录
        {
            sql.append(" and t.cust_id = ?");
            params.add(user.getId(), Hibernate.STRING);
        }
        else if (Constant.USER_TYPE_CUSTOMER.equals(user.getType()) && StringUtil.isNotEmpty(custId))
        // 普通客户查询自己家庭成员的记录
        {

        }
        else
        // 其它非管理员用户不能查询到记录
        {
            return pageModel;
        }
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append(" select count(t.id) as c ").append(sql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.cust_id, t.direct_reason, t.sport_direct, t.food_direct, t.drug_suggest, t.memo ");
        pSql.append(", t.create_by, t.create_time, su.name_, su2.name_ as doctorName  ");
        /*pSql.append(", t.create_by, t.create_time, su.name_, su.name_ as doctorName ");*/
        pSql.append(sql);
        pSql.append(" order by t.create_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("direct_reason", Hibernate.STRING)
                .addScalar("sport_direct", Hibernate.STRING).addScalar("food_direct", Hibernate.STRING)
                .addScalar("drug_suggest", Hibernate.STRING).addScalar("memo", Hibernate.STRING)
                .addScalar("create_by", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("name_", Hibernate.STRING).addScalar("doctorName", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<HealthyGuide> list = new ArrayList<HealthyGuide>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            HealthyGuide healthyGuide = new HealthyGuide();
            idx = 0;
            healthyGuide.setId((String) objs[idx]);
            idx++;
            healthyGuide.setCustId((String) objs[idx]);
            idx++;
            healthyGuide.setDirectReason((String) objs[idx]);
            idx++;
            healthyGuide.setSportDirect((String) objs[idx]);
            idx++;
            healthyGuide.setFoodDirect((String) objs[idx]);
            idx++;
            healthyGuide.setDrugSuggest((String) objs[idx]);
            idx++;
            healthyGuide.setMemo((String) objs[idx]);
            idx++;
            healthyGuide.setCreateBy((String) objs[idx]);
            idx++;
            healthyGuide.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            healthyGuide.setMemberName((String) objs[idx]);
            idx++;
            healthyGuide.setDoctorName((String) objs[idx]);
            idx++;
            list.add(healthyGuide);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    @SuppressWarnings("unchecked")
    public HealthyGuide getDetail(String id) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from msg_healthy_guide t ");
        sql.append(" inner join sys_customer sc on sc.user_id = t.cust_id");
        sql.append(" inner join sys_user su on su.id = sc.user_id");
        sql.append(" inner join sys_user su2 on su2.id = t.create_by");
        sql.append(" inner join svb_member_info sm on sm.cust_id = su.id");
        sql.append(" where 1 = 1 and t.id = ?");
        params.add(id, Hibernate.STRING);
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.cust_id, t.direct_reason, t.sport_direct, t.food_direct, t.drug_suggest, t.memo ");
        pSql.append(", t.create_by, t.create_time, su.name_, su2.name_ as doctorName  ").append(sql);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("direct_reason", Hibernate.STRING)
                .addScalar("sport_direct", Hibernate.STRING).addScalar("food_direct", Hibernate.STRING)
                .addScalar("drug_suggest", Hibernate.STRING).addScalar("memo", Hibernate.STRING)
                .addScalar("create_by", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("name_", Hibernate.STRING).addScalar("doctorName", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        HealthyGuide healthyGuide = null;
        if (objsList != null && objsList.size() > 0)
        {
            healthyGuide = new HealthyGuide();
            Object[] objs = objsList.get(0);
            int idx = 0;
            healthyGuide.setId((String) objs[idx]);
            idx++;
            healthyGuide.setCustId((String) objs[idx]);
            idx++;
            healthyGuide.setDirectReason((String) objs[idx]);
            idx++;
            healthyGuide.setSportDirect((String) objs[idx]);
            idx++;
            healthyGuide.setFoodDirect((String) objs[idx]);
            idx++;
            healthyGuide.setDrugSuggest((String) objs[idx]);
            idx++;
            healthyGuide.setMemo((String) objs[idx]);
            idx++;
            healthyGuide.setCreateBy((String) objs[idx]);
            idx++;
            healthyGuide.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            healthyGuide.setMemberName((String) objs[idx]);
            idx++;
            healthyGuide.setDoctorName((String) objs[idx]);
            idx++;
        }
        return healthyGuide;
    }
    
    @SuppressWarnings("unchecked")
    public HealthyGuide lookSuggest(String custId,String id) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from msg_healthy_guide t ");
        sql.append(" inner join sys_customer sc on sc.user_id = t.cust_id");
        sql.append(" inner join sys_user su on su.id = sc.user_id");
        sql.append(" inner join chk_pressure_pulse ch on su.id = ch.cust_id");
        sql.append(" inner join sys_user su2 on su2.id = t.create_by");
        sql.append(" inner join svb_member_info sm on sm.cust_id = su.id");
        sql.append(" where 1 = 1 and t.cust_id = ? and t.preId = ?");
        params.add(custId, Hibernate.STRING);
        params.add(id, Hibernate.STRING);
        sql.append(" GROUP BY t.id");
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.cust_id, t.direct_reason, t.sport_direct, t.food_direct, t.drug_suggest, t.memo ");
        pSql.append(", t.create_by, t.create_time, su.name_, su2.name_ as doctorName ").append(sql);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("direct_reason", Hibernate.STRING)
                .addScalar("sport_direct", Hibernate.STRING).addScalar("food_direct", Hibernate.STRING)
                .addScalar("drug_suggest", Hibernate.STRING).addScalar("memo", Hibernate.STRING)
                .addScalar("create_by", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("name_", Hibernate.STRING).addScalar("doctorName", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        HealthyGuide healthyGuide = null;
        if (objsList != null && objsList.size() > 0)
        {
            healthyGuide = new HealthyGuide();
            Object[] objs = objsList.get(0);
            int idx = 0;
            healthyGuide.setId((String) objs[idx]);
            idx++;
            healthyGuide.setCustId((String) objs[idx]);
            idx++;
            healthyGuide.setDirectReason((String) objs[idx]);
            idx++;
            healthyGuide.setSportDirect((String) objs[idx]);
            idx++;
            healthyGuide.setFoodDirect((String) objs[idx]);
            idx++;
            healthyGuide.setDrugSuggest((String) objs[idx]);
            idx++;
            healthyGuide.setMemo((String) objs[idx]);
            idx++;
            healthyGuide.setCreateBy((String) objs[idx]);
            idx++;
            healthyGuide.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            healthyGuide.setMemberName((String) objs[idx]);
            idx++;
            healthyGuide.setDoctorName((String) objs[idx]);
            idx++;
        }
        return healthyGuide;
    }
    @SuppressWarnings("unchecked")
    public PageModel<HealthyGuide> getPersonalList(Map<String, Object> filter, int page, int pageSize)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from msg_healthy_guide t ");
        sql.append(" inner join sys_customer sc on sc.user_id = t.cust_id");
        sql.append(" inner join sys_user su on su.id = sc.user_id");
        sql.append(" inner join sys_user su2 on su2.id = t.create_by");
        sql.append(" inner join svb_member_info sm on sm.cust_id = su.id");
        sql.append(" where 1= 1 and sm.state = 1");
        //查询条件
        String userId = (String) filter.get("userId");
        if (StringUtil.isNotEmpty(userId))
        {
            sql.append(" and t.cust_id = ?");
            params.add(userId, Hibernate.STRING);
            /*sql.append(" and su.name_ like ").append(params.addLikePart(memberName));*/
        }
        String directReason = (String) filter.get("directReason");
        if (StringUtil.isNotEmpty(directReason))
        {
            sql.append(" and t.direct_reason like ").append(params.addLikePart(directReason));
        }
        String beginDate = (String) filter.get("beginDate");
        if (beginDate != null && beginDate.length() > 0)
        {
            sql.append(" and t.create_time >= '").append(beginDate).append("'");
        }
        String endDate = (String) filter.get("endDate");
        if (endDate != null && endDate.length() > 0)
        {
            sql.append(" and t.create_time < adddate('").append(endDate).append("', interval 1 day)");
        }
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append(" select count(t.id) as c ").append(sql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.cust_id, t.direct_reason, t.sport_direct, t.food_direct, t.drug_suggest, t.memo ");
        pSql.append(", t.create_by, t.create_time, su.name_, su2.name_ as doctorName  ");
        /*pSql.append(", t.create_by, t.create_time, su.name_, su.name_ as doctorName ");*/
        pSql.append(sql);
        pSql.append(" order by t.create_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("direct_reason", Hibernate.STRING)
                .addScalar("sport_direct", Hibernate.STRING).addScalar("food_direct", Hibernate.STRING)
                .addScalar("drug_suggest", Hibernate.STRING).addScalar("memo", Hibernate.STRING)
                .addScalar("create_by", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("name_", Hibernate.STRING).addScalar("doctorName", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<HealthyGuide> list = new ArrayList<HealthyGuide>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            HealthyGuide healthyGuide = new HealthyGuide();
            idx = 0;
            healthyGuide.setId((String) objs[idx]);
            idx++;
            healthyGuide.setCustId((String) objs[idx]);
            idx++;
            healthyGuide.setDirectReason((String) objs[idx]);
            idx++;
            healthyGuide.setSportDirect((String) objs[idx]);
            idx++;
            healthyGuide.setFoodDirect((String) objs[idx]);
            idx++;
            healthyGuide.setDrugSuggest((String) objs[idx]);
            idx++;
            healthyGuide.setMemo((String) objs[idx]);
            idx++;
            healthyGuide.setCreateBy((String) objs[idx]);
            idx++;
            healthyGuide.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            healthyGuide.setMemberName((String) objs[idx]);
            idx++;
            healthyGuide.setDoctorName((String) objs[idx]);
            idx++;
            list.add(healthyGuide);
        }
        PageModel<HealthyGuide> pageModel = new PageModel<HealthyGuide>();
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 医生客户端查询自己的健康指导情况
     * @author h
     * 2015.9.23
     * */
    @SuppressWarnings("unchecked")
    public PageTran<HealthyGuide> queryGuideTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.cust_id, tu.name_, t.direct_reason, t.sport_direct, t.food_direct, t.drug_suggest, t.memo, t.create_time");
        sql.append(" from msg_healthy_guide t ");
        sql.append(" inner join sys_user su su.id = t.cust_id ");
        sql.append(" where 1 = 1 and t.create_by = ? ");
        String userId = (String) filter.get("userId");
        params.add(userId, Hibernate.STRING);
        //权限控制
        sql.append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = t.cust_id and smi.doctor_id = ? and smi.state = ?)");
        params.add(userId, Hibernate.STRING);
        params.add(Constant.CUSTOMER_MEMBER_STATUS_YES, Hibernate.INTEGER);
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
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("id", Hibernate.STRING).addScalar("cust_id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("direct_reason", Hibernate.STRING)
                .addScalar("sport_direct", Hibernate.STRING).addScalar("food_direct", Hibernate.STRING)
                .addScalar("drug_suggest", Hibernate.STRING).addScalar("memo", Hibernate.STRING)
                .addScalar("create_time", Hibernate.TIMESTAMP)
                .setParameters(params.getVals(), params.getTypes()).setMaxResults(count).list();
        List<HealthyGuide> list = new ArrayList<HealthyGuide>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            HealthyGuide healthyGuide = new HealthyGuide();
            idx = 0;
            healthyGuide.setId((String) objs[idx]);
            idx++;
            healthyGuide.setCustId((String) objs[idx]);
            idx++;
            healthyGuide.setMemberName((String) objs[idx]);
            idx++;
            healthyGuide.setDirectReason((String) objs[idx]);
            idx++;
            healthyGuide.setSportDirect((String) objs[idx]);
            idx++;
            healthyGuide.setFoodDirect((String) objs[idx]);
            idx++;
            healthyGuide.setDrugSuggest((String) objs[idx]);
            idx++;
            healthyGuide.setMemo((String) objs[idx]);
            idx++;
            healthyGuide.setCreateTime((Timestamp) objs[idx]);
            idx++;
            list.add(healthyGuide);
        }
        return new PageTran<HealthyGuide>(list);
    }

}
