package com.yzxt.yh.module.rgm.dao;

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
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.rgm.bean.HealthyPlan;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;


public class HealthyPlanDao extends HibernateSupport<HealthyPlan> implements BaseDao<HealthyPlan>
{

    /**
     * 删除健康计划
     * @param id
     * @return
     * @throws Exception 
     */
    public String insert(HealthyPlan healthyPlan) throws Exception
    {
        super.save(healthyPlan);
        return healthyPlan.getId();
    }

    /**
     * 删除健康计划
     * @param id
     * @return
     */
    public int update(HealthyPlan healthyPlan) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update HealthyPlan t set t.name = ?, t.type = ?");
        sql.append(", t.startDate = ?, t.endDate = ?, t.targetValue = ?, t.targetValue2 = ?");
        sql.append(", t.targetValue3 = ?, t.updateBy = ?, t.updateTime = ?");
        sql.append(" where t.id = ?");
        params.add(healthyPlan.getName(), Hibernate.STRING);
        params.add(healthyPlan.getType(), Hibernate.INTEGER);
        params.add(healthyPlan.getStartDate(), Hibernate.DATE);
        params.add(healthyPlan.getEndDate(), Hibernate.DATE);
        params.add(healthyPlan.getTargetValue(), Hibernate.STRING);
        params.add(healthyPlan.getTargetValue2(), Hibernate.STRING);
        params.add(healthyPlan.getTargetValue3(), Hibernate.STRING);
        params.add(healthyPlan.getUpdateBy(), Hibernate.STRING);
        params.add(healthyPlan.getUpdateTime(), Hibernate.TIMESTAMP);
        params.add(healthyPlan.getId(), Hibernate.STRING);
        int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

    /**
     * 删除健康计划
     * @param id
     * @return
     */
    public int delete(String id)
    {
        String sql = "delete from HealthyPlan t where t.id = ?";
        int c = super.getSession().createQuery(sql).setString(0, id).executeUpdate();
        return c;
    }

    /**
     * 加载健康计划
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public HealthyPlan load(String id) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.user_id, t.name, t.type, t.start_date, t.end_date, t.state");
        sql.append(", t.target_value, t.target_value2, t.target_value3");
        sql.append(", t.create_by, t.create_time, t.update_by, t.update_time");
        sql.append(", tu.id_card");
        sql.append(" from rgm_healthy_plan t");
        sql.append(" inner join sys_user tu on tu.id = t.user_id");
        sql.append(" where t.id = ?");
        params.add(id, Hibernate.STRING);
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("user_id", Hibernate.STRING).addScalar("name", Hibernate.STRING)
                .addScalar("type", Hibernate.INTEGER).addScalar("start_date", Hibernate.DATE)
                .addScalar("end_date", Hibernate.DATE).addScalar("state", Hibernate.INTEGER)
                .addScalar("target_value", Hibernate.STRING).addScalar("target_value2", Hibernate.STRING)
                .addScalar("target_value3", Hibernate.STRING).addScalar("create_by", Hibernate.STRING)
                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("update_by", Hibernate.STRING)
                .addScalar("update_time", Hibernate.TIMESTAMP).addScalar("id_card", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        HealthyPlan healthyPlan = null;
        if (objsList != null && objsList.size() > 0)
        {
            healthyPlan = new HealthyPlan();
            Object[] objs = objsList.get(0);
            int idx = 0;
            healthyPlan.setId((String) objs[idx]);
            idx++;
            healthyPlan.setUserId((String) objs[idx]);
            idx++;
            healthyPlan.setName((String) objs[idx]);
            idx++;
            healthyPlan.setType((Integer) objs[idx]);
            idx++;
            healthyPlan.setStartDate((Date) objs[idx]);
            idx++;
            healthyPlan.setEndDate((Date) objs[idx]);
            idx++;
            healthyPlan.setState((Integer) objs[idx]);
            idx++;
            healthyPlan.setTargetValue((String) objs[idx]);
            idx++;
            healthyPlan.setTargetValue2((String) objs[idx]);
            idx++;
            healthyPlan.setTargetValue3((String) objs[idx]);
            idx++;
            healthyPlan.setCreateBy((String) objs[idx]);
            idx++;
            healthyPlan.setCreateTime((Timestamp) objs[idx]);
            idx++;
            healthyPlan.setUpdateBy((String) objs[idx]);
            idx++;
            healthyPlan.setUpdateTime((Timestamp) objs[idx]);
            idx++;
            healthyPlan.setIdCard((String) objs[idx]);
            idx++;
        }
        return healthyPlan;
    }

    /**
     * 查询健康计划
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public PageModel<HealthyPlan> queryHealthyPlanByPage(Map<String, Object> filter, int page, int pageSize) throws Exception
    {
        PageModel<HealthyPlan> pageModel = new PageModel<HealthyPlan>();
        Date today = (Date) filter.get("today");
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append("from rgm_healthy_plan t");
        mSql.append(" inner join sys_customer tu on tu.user_id = t.update_by");
        mSql.append(" where t.state = 1");
        // 权限判断
        User user = (User) filter.get("user");
        if (user != null && Constant.USER_TYPE_CUSTOMER.equals(user.getType()))
        {
            mSql.append(" and t.user_id = '").append(user.getId()).append("'");
        }
        else
        {
            mSql.append(" and 1<>1");
        }
        // 关键字
        String keyword = (String) filter.get("keyword");
        if (StringUtil.isNotEmpty(keyword))
        {
            mSql.append(" and t.name like '%").append(keyword).append("%'");
        }
        // 总启启记录数
        StringBuilder totalCountsql = new StringBuilder();
        totalCountsql.append("select count(*) as c ").append(mSql);
        Integer totalCount = (Integer) super.getSession().createSQLQuery(totalCountsql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select tt.id, tt.user_id, tt.name, tt.type, tt.start_date, tt.end_date, tt.target_value, tt.target_value2, tt.target_value3");
        pSql.append(", tt.state, tt.create_by, tt.create_time, tt.update_by, tt.update_time");
        // ---开始时的血压或血糖等的值---
        pSql.append(", (case when tt.start_date <= ? then (case when tt.type = 1 then (");
        // 血压查询
        pSql.append("select concat(spp.s_b_p, '/', spp.d_b_p) from chk_pressure_pulse spp");
        pSql.append(" where spp.check_time < tt.start_date and spp.cust_id = tt.cust_id order by spp.check_time desc limit 0, 1");
        pSql.append(") when tt.type = 2 then (");
        // 血糖查询
        pSql.append("concat(");
        // 空腹血糖
        pSql.append("ifnull((select sbs.blood_sugar from chk_blood_sugar sbs");
        pSql.append(" where (sbs.blood_sugar_type = 1 or sbs.blood_sugar_type = 2) and sbs.check_time < tt.start_date and sbs.cust_id = tt.cust_id order by sbs.check_time desc limit 0, 1), '')");
        pSql.append(", '/' ,");
        // 餐后血糖
        pSql.append("ifnull((select sbs.blood_sugar from chk_blood_sugar sbs");
        pSql.append(" where sbs.blood_sugar_type = 3 and sbs.check_time < tt.start_date and sbs.cust_id = tt.cust_id order by sbs.check_time desc limit 0, 1), '')");
        pSql.append(", '/' ,");
        // 服糖后血糖
        pSql.append("ifnull((select sbs.blood_sugar from chk_blood_sugar sbs");
        pSql.append(" where sbs.blood_sugar_type = 4 and sbs.check_time < tt.start_date and sbs.cust_id = tt.cust_id order by sbs.check_time desc limit 0, 1), '')");
        pSql.append(")");
        // 其它类型
        pSql.append(") else null end) else null end) as start_val_desc");
        // ---结束时的血压或血糖等的值---
        pSql.append(", (case when tt.end_date < ? then (case when tt.type = 1 then (");
        // 血压查询
        pSql.append("select concat(spp.s_b_p, '/', spp.d_b_p) from chk_pressure_pulse spp");
        pSql.append(" where spp.check_time >= date_add(tt.end_date, interval 1 day) and spp.cust_id = tt.cust_id order by spp.check_time asc limit 0, 1");
        pSql.append(") when tt.type = 2 then (");
        // 血糖查询
        pSql.append("concat(");
        // 空腹血糖
        pSql.append("ifnull((select sbs.blood_sugar from chk_blood_sugar sbs");
        pSql.append(" where (sbs.blood_sugar_type = 1 or sbs.blood_sugar_type = 2) and sbs.check_time >= date_add(tt.end_date, interval 1 day) and sbs.cust_id = tt.cust_id order by sbs.check_time asc limit 0, 1), '')");
        pSql.append(", '/' ,");
        // 餐后血糖
        pSql.append("ifnull((select sbs.blood_sugar from chk_blood_sugar sbs");
        pSql.append(" where sbs.blood_sugar_type = 3 and sbs.check_time >= date_add(tt.end_date, interval 1 day) and sbs.cust_id = tt.cust_id order by sbs.check_time asc limit 0, 1), '')");
        pSql.append(", '/' ,");
        // 服糖后血糖
        pSql.append("ifnull((select sbs.blood_sugar from chk_blood_sugar sbs");
        pSql.append(" where sbs.blood_sugar_type = 4 and sbs.check_time >= date_add(tt.end_date, interval 1 day) and sbs.cust_id = tt.cust_id order by sbs.check_time asc limit 0, 1), '')");
        pSql.append(")");
        // 其它类型
        pSql.append(") else null end) else null end) as end_val_desc");
        pSql.append(" from (select t.id, t.user_id, t.name, t.type, t.start_date, t.end_date, t.target_value, t.target_value2, t.target_value3, t.state");
        pSql.append(", t.create_by, t.create_time, t.update_by, t.update_time, tu.user_id as cust_id  ");
        pSql.append(mSql).append(" order by t.start_date desc limit ?, ?) tt");
        params.add(today, Hibernate.DATE);
        params.add(today, Hibernate.DATE);
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .addScalar("id", Hibernate.STRING)
                .addScalar("user_id", Hibernate.STRING).addScalar("name", Hibernate.STRING)
                .addScalar("type", Hibernate.INTEGER).addScalar("start_date", Hibernate.DATE)
                .addScalar("end_date", Hibernate.DATE).addScalar("target_value", Hibernate.STRING)
                .addScalar("target_value2", Hibernate.STRING).addScalar("target_value3", Hibernate.STRING)
                .addScalar("state", Hibernate.INTEGER).addScalar("create_by", Hibernate.STRING)
                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("update_by", Hibernate.STRING)
                .addScalar("update_time", Hibernate.TIMESTAMP).addScalar("start_val_desc", Hibernate.STRING)
                .addScalar("end_val_desc", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
        List<HealthyPlan> list = new ArrayList<HealthyPlan>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            HealthyPlan plan = new HealthyPlan();
            idx = 0;
            plan.setId((String) objs[idx]);
            idx++;
            plan.setUserId((String) objs[idx]);
            idx++;
            plan.setName((String) objs[idx]);
            idx++;
            plan.setType((Integer) objs[idx]);
            idx++;
            plan.setStartDate((Date) objs[idx]);
            idx++;
            plan.setEndDate((Date) objs[idx]);
            idx++;
            plan.setTargetValue((String) objs[idx]);
            idx++;
            plan.setTargetValue2((String) objs[idx]);
            idx++;
            plan.setTargetValue3((String) objs[idx]);
            idx++;
            plan.setState((Integer) objs[idx]);
            idx++;
            plan.setCreateBy((String) objs[idx]);
            idx++;
            plan.setCreateTime((Timestamp) objs[idx]);
            idx++;
            plan.setUpdateBy((String) objs[idx]);
            idx++;
            plan.setUpdateTime((Timestamp) objs[idx]);
            idx++;
            plan.setStartValDesc((String) objs[idx]);
            idx++;
            plan.setEndValDesc((String) objs[idx]);
            idx++;
            list.add(plan);
        }
        pageModel.setTotal(totalCount);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 查询健康计划，主要用于客户端查询
     * @param filter
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<HealthyPlan> queryHealthyPlansBySyn(Map<String, Object> filter) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.user_id, t.name, t.type, t.start_date, t.end_date, t.state");
        sql.append(", t.target_value, t.target_value2, t.target_value3");
        sql.append(", t.create_by, t.create_time, t.update_by, t.update_time");
        sql.append(" from rgm_healthy_plan t");
        sql.append(" where 1=1");
        // 健康计划执行人
        User user = (User) filter.get("user");
        sql.append(" and t.user_id = ?");
        params.add(user.getId(), Hibernate.STRING);
        // 拉取时间
        /*Timestamp synTime = (Timestamp) filter.get("sysTime");
        Integer maxSize = (Integer) filter.get("maxSize");
        if (synTime != null)
        {
            sql.append(" and t.update_time < ?");
            params.add(synTime, Hibernate.TIMESTAMP);
        }*/
        // 取最新时间倒序
        sql.append(" order by t.update_time desc");
        // 取数据条数
       /* sql.append(" limit 0, ?");
        params.add(maxSize, Hibernate.INTEGER);*/
        // 查询数据
        List<Object[]> listObjs = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("user_id", Hibernate.STRING).addScalar("name", Hibernate.STRING)
                .addScalar("type", Hibernate.INTEGER).addScalar("start_date", Hibernate.DATE)
                .addScalar("end_date", Hibernate.DATE).addScalar("state", Hibernate.INTEGER)
                .addScalar("target_value", Hibernate.STRING).addScalar("target_value2", Hibernate.STRING)
                .addScalar("target_value3", Hibernate.STRING).addScalar("create_by", Hibernate.STRING)
                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("update_by", Hibernate.STRING)
                .addScalar("update_time", Hibernate.TIMESTAMP).setParameters(params.getVals(), params.getTypes())
                .list();
        List<HealthyPlan> list = new ArrayList<HealthyPlan>();
        if (listObjs != null && listObjs.size() > 0)
        {
            int idx = 0;
            for (Object[] objs : listObjs)
            {
                HealthyPlan healthyPlan = new HealthyPlan();
                idx = 0;
                healthyPlan.setId((String) objs[idx]);
                idx++;
                healthyPlan.setUserId((String) objs[idx]);
                idx++;
                healthyPlan.setName((String) objs[idx]);
                idx++;
                healthyPlan.setType((Integer) objs[idx]);
                idx++;
                healthyPlan.setStartDate((Date) objs[idx]);
                idx++;
                healthyPlan.setEndDate((Date) objs[idx]);
                idx++;
                healthyPlan.setState((Integer) objs[idx]);
                idx++;
                healthyPlan.setTargetValue((String) objs[idx]);
                idx++;
                healthyPlan.setTargetValue2((String) objs[idx]);
                idx++;
                healthyPlan.setTargetValue3((String) objs[idx]);
                idx++;
                healthyPlan.setCreateBy((String) objs[idx]);
                idx++;
                healthyPlan.setCreateTime((Timestamp) objs[idx]);
                idx++;
                healthyPlan.setUpdateBy((String) objs[idx]);
                idx++;
                healthyPlan.setUpdateTime((Timestamp) objs[idx]);
                idx++;
                list.add(healthyPlan);
            }
        }
        return list;
    }

}
