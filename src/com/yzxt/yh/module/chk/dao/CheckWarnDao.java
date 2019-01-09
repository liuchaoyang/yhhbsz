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
import com.yzxt.yh.constant.ConstChr;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.CheckWarn;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

public class CheckWarnDao extends HibernateSupport<CheckWarn> implements BaseDao<CheckWarn>
{
    /**
     * 插入检测告警信息
     */
    public String insert(CheckWarn checkWarn) throws Exception
    {
        super.save(checkWarn);
        return checkWarn.getId();
    }

    /**
     * 获取最新的告警信息
     */
    @SuppressWarnings("unchecked")
    public CheckWarn getLatest(Map<String, Object> filter) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t from CheckWarn t where 1=1");
        String type = (String) filter.get("type");
        if (StringUtil.isNotEmpty(type))
        {
            sql.append(" and t.type = ?");
            params.add(type, Hibernate.STRING);
        }
        String custId = (String) filter.get("custId");
        if (StringUtil.isNotEmpty(custId))
        {
            sql.append(" and t.custId = ?");
            params.add(custId, Hibernate.STRING);
        }
        // 起始告警时间，加入此字段用于提高查询速度
        Date startDate = (Date) filter.get("startDate");
        if (startDate != null)
        {
            sql.append(" and t.warnTime >= ?");
            params.add(startDate, Hibernate.DATE);
        }
        sql.append(" order by t.warnTime desc");
        List<CheckWarn> list = super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).setFirstResult(0).setFetchSize(1).list();
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    @SuppressWarnings("unchecked")
    public PageModel<CheckWarn> getWarnList(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<CheckWarn> pageModel = new PageModel<CheckWarn>();
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from chk_check_warn t ");
        sql.append(" inner join sys_customer sc on sc.user_id = t.cust_id");
        sql.append(" left join sys_user su on su.id = sc.user_id");
        sql.append(" where 1=1 ");
        sql.append(" and t.warn_time in (select MAX(t.warn_time) from chk_check_warn t where 1=1 GROUP BY t.cust_id) ");
        //查詢條件
        String userName = (String) filter.get("userName");
        if (StringUtil.isNotEmpty(userName))
        {
            sql.append(" and su.name_ like ").append(params.addLikePart(userName));
        }
        String healthyStatus = (String) filter.get("healthyStatus");
        if (StringUtil.isNotEmpty(healthyStatus))
        {
            sql.append(" and sc.healthy_status = ?");
            Integer healthyState = Integer.parseInt(healthyStatus);
            params.add(healthyState, Hibernate.INTEGER);
            /*sql.append(" and tu.name_ like ").append(params.addLikePart(healthyStatus));*/
        }
        //是否所有的预警已处理 是代表2 否代表1 
        String tatolState = (String) filter.get("tatolState");
        if (StringUtil.isNotEmpty(tatolState))
        {
            sql.append(" and sc.healthy_status = ?");
            Integer healthyState = Integer.parseInt(healthyStatus);
            params.add(healthyState, Hibernate.INTEGER);
            sql.append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = t.cust_id and smi.doctor_id = ? and smi.state = 1)");
            /* params.add(user.getId(), Hibernate.STRING);*/
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
        User user = (User) filter.get("operUser");
        // 权限
        if (Constant.USER_TYPE_DOCTOR.equals(user.getType()))
        // 医生查询自己的会员记录
        {
            sql.append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = t.cust_id and smi.doctor_id = ? and smi.state = 1)");
            params.add(user.getId(), Hibernate.STRING);
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
        sql.append(" GROUP BY t.cust_id ");
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        StringBuilder lSql = new StringBuilder();
        lSql.append("select max(t.warn_time) as warn_time ").append(sql);
        totalCountSql.append(" select count(m.warn_time) as c from ( ").append(lSql).append(" ) m");
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.cust_id, t.type_, t.level, t.descript, t.state, t.warn_time");
        pSql.append(", sc.sex, sc.birthday, sc.healthy_status, su.name_, su.id_card");
        pSql.append(sql);
        pSql.append(" order by warn_time asc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("type_", Hibernate.STRING)
                .addScalar("level", Hibernate.INTEGER).addScalar("descript", Hibernate.STRING)
                .addScalar("state", Hibernate.INTEGER).addScalar("warn_time", Hibernate.TIMESTAMP)
                .addScalar("sex", Hibernate.INTEGER).addScalar("birthday", Hibernate.DATE)
                .addScalar("healthy_status", Hibernate.INTEGER).addScalar("name_", Hibernate.STRING)
                .addScalar("id_card", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
        List<CheckWarn> list = new ArrayList<CheckWarn>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            CheckWarn checkWarn = new CheckWarn();
            idx = 0;
            checkWarn.setId((String) objs[idx]);
            idx++;
            checkWarn.setCustId((String) objs[idx]);
            idx++;
            checkWarn.setType((String) objs[idx]);
            idx++;
            checkWarn.setLevel((Integer) objs[idx]);
            idx++;
            checkWarn.setDescript((String) objs[idx]);
            idx++;
            checkWarn.setState((Integer) objs[idx]);
            idx++;
            checkWarn.setWarnTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            checkWarn.setSex((Integer) objs[idx]);
            idx++;
            checkWarn.setBirthday((Date) objs[idx]);
            idx++;
            checkWarn.setHealthyStatus((Integer) objs[idx]);
            idx++;
            checkWarn.setName((String) objs[idx]);
            idx++;
            checkWarn.setIdCard((String) objs[idx]);
            idx++;
            list.add(checkWarn);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    @SuppressWarnings("unchecked")
    public CheckWarn getPersonal(String id) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from chk_check_warn t");
        sql.append(" inner join sys_user su on su.id = t.cust_id");
        sql.append(" where 1 = 1 and t.cust_id = ?");
        params.add(id, Hibernate.STRING);
        StringBuilder pSql = new StringBuilder();
        pSql.append(" select t.cust_id, su.name_, su.id_card ").append(sql);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .addScalar("cust_id", Hibernate.STRING).addScalar("name_", Hibernate.STRING)
                .addScalar("id_card", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
        CheckWarn checkWarn = null;
        if (objsList != null && objsList.size() > 0)
        {
            checkWarn = new CheckWarn();
            Object[] objs = objsList.get(0);
            int idx = 0;
            checkWarn.setCustId((String) objs[idx]);
            idx++;
            checkWarn.setName((String) objs[idx]);
            idx++;
            checkWarn.setIdCard((String) objs[idx]);
            idx++;
        }
        return checkWarn;
    }

    public PageModel<CheckWarn> query(Map<String, Object> filter, int page, int pageSize)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        String custId = (String) filter.get("custId");
        sql.append(" from chk_check_warn t ");
        sql.append(" where 1 = 1 and t.cust_id = ?");
        params.add(custId, Hibernate.STRING);
        // 查询条件
        String stutas = (String) filter.get("dealState");
        if (StringUtil.isNotEmpty(stutas))
        {
            sql.append(" and t.state = ?");
            Integer state = Integer.parseInt(stutas);
            params.add(state, Hibernate.INTEGER);
        }
        String warnLevel = (String) filter.get("warnLevel");
        if (StringUtil.isNotEmpty(warnLevel))
        {
            sql.append(" and t.level = ?");
            Integer level = Integer.parseInt(warnLevel);
            params.add(level, Hibernate.INTEGER);
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
        pSql.append("select t.id, t.cust_id, t.type_, t.level, t.descript, t.state, t.warn_time");
        pSql.append(sql);
        pSql.append(" order by t.warn_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        @SuppressWarnings("unchecked")
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("type_", Hibernate.STRING)
                .addScalar("level", Hibernate.INTEGER).addScalar("descript", Hibernate.STRING)
                .addScalar("state", Hibernate.INTEGER).addScalar("warn_time", Hibernate.TIMESTAMP)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<CheckWarn> list = new ArrayList<CheckWarn>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            CheckWarn checkWarn = new CheckWarn();
            idx = 0;
            checkWarn.setId((String) objs[idx]);
            idx++;
            checkWarn.setCustId((String) objs[idx]);
            idx++;
            checkWarn.setType((String) objs[idx]);
            idx++;
            checkWarn.setLevel((Integer) objs[idx]);
            idx++;
            checkWarn.setDescript((String) objs[idx]);
            idx++;
            checkWarn.setState((Integer) objs[idx]);
            idx++;
            checkWarn.setWarnTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            list.add(checkWarn);
        }
        PageModel<CheckWarn> pageModel = new PageModel<CheckWarn>();
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    public int updateState(CheckWarn checkWarn)
    {
        HibernateParams params = new HibernateParams();
        String sql = "update chk_check_warn t set  t.state= ? where t.id = ?";
        params.add(checkWarn.getState(), Hibernate.INTEGER);
        params.add(checkWarn.getId(), Hibernate.STRING);
        return super.getSession().createSQLQuery(sql).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();

    }

    public PageModel<CheckWarn> queryMyWarns(Map<String, Object> filter, int page, int pageSize)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        String custId = (String) filter.get("custId");
        sql.append(" from chk_check_warn t ");
        sql.append(" where 1 = 1 and t.cust_id = ?");
        params.add(custId, Hibernate.STRING);
        // 查询条件
        String stutas = (String) filter.get("dealState");
        if (StringUtil.isNotEmpty(stutas))
        {
            sql.append(" and t.state = ?");
            Integer state = Integer.parseInt(stutas);
            params.add(state, Hibernate.INTEGER);
        }
        String warnLevel = (String) filter.get("warnLevel");
        if (StringUtil.isNotEmpty(warnLevel))
        {
            sql.append(" and t.level = ?");
            Integer level = Integer.parseInt(warnLevel);
            params.add(level, Hibernate.INTEGER);
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
        pSql.append("select t.id, t.cust_id, t.type_, t.level, t.descript, t.state, t.warn_time");
        pSql.append(sql);
        pSql.append(" order by t.warn_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        @SuppressWarnings("unchecked")
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("type_", Hibernate.STRING)
                .addScalar("level", Hibernate.INTEGER).addScalar("descript", Hibernate.STRING)
                .addScalar("state", Hibernate.INTEGER).addScalar("warn_time", Hibernate.TIMESTAMP)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<CheckWarn> list = new ArrayList<CheckWarn>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            CheckWarn checkWarn = new CheckWarn();
            idx = 0;
            checkWarn.setId((String) objs[idx]);
            idx++;
            checkWarn.setCustId((String) objs[idx]);
            idx++;
            checkWarn.setType((String) objs[idx]);
            idx++;
            checkWarn.setLevel((Integer) objs[idx]);
            idx++;
            checkWarn.setDescript((String) objs[idx]);
            idx++;
            checkWarn.setState((Integer) objs[idx]);
            idx++;
            checkWarn.setWarnTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            list.add(checkWarn);
        }
        PageModel<CheckWarn> pageModel = new PageModel<CheckWarn>();
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 医生首页查询有未处理告警的用户。
     * */
    public PageModel<CheckWarn> queryWarnCust(Map<String, Object> filter, int page, int pageSize) throws Exception
    {
        PageModel<CheckWarn> pageModel = new PageModel<CheckWarn>();
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from (");
        mSql.append(" select t.id as cust_id, t.name_ as cust_name, t.id_card");
        mSql.append(", (select scw.id from chk_check_warn scw where scw.cust_id = t.id and scw.state = 1 order by scw.warn_time desc limit 0, 1) as last_warn_id");
        mSql.append(", (select count(scw.id) from chk_check_warn scw where scw.cust_id = t.id and scw.state = ?) as no_deal_num");
        params.add(Constant.WARNING_STATE_NOT_DEAL, Hibernate.INTEGER);
        mSql.append(" from sys_user t");
        mSql.append(" where 1=1");
        // 客户名称
        String custName = (String) filter.get("custName");
        if (StringUtil.isNotEmpty(custName))
        {
            mSql.append(" and t.name_ like ").append(params.addLikePart(custName));
        }
        // 权限
        User operUser = (User) filter.get("operUser");
        if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
        {
            mSql.append(" and exists(select smi.id from svb_member_info smi where smi.state = ? and smi.cust_id = t.id and smi.doctor_id = ?)");
            params.add(Constant.CUSTOMER_MEMBER_STATUS_YES, Hibernate.INTEGER);
            params.add(operUser.getId(), Hibernate.STRING);
        }
        else if (!Constant.USER_TYPE_ADMIN.equals(operUser.getType()))
        {
            return pageModel;
        }
        mSql.append(") tt");
        mSql.append(" left join chk_check_warn tl on tl.id = tt.last_warn_id");
        mSql.append(" where 1=1");
        Date beginDate = (Date) filter.get("beginDate");
        if (beginDate != null)
        {
            mSql.append(" and tl.warn_time >= ?");
            params.add(beginDate, Hibernate.DATE);
        }
        Date endDate = (Date) filter.get("endDate");
        if (endDate != null)
        {
            mSql.append(" and tl.warn_time < ?");
            params.add(DateUtil.addDay(endDate, 1), Hibernate.DATE);
        }
        String haveNotDealWarn = (String) filter.get("haveNotDealWarn");
        if ("Y".equals(haveNotDealWarn))
        {
            mSql.append(" and tt.no_deal_num > 0");
        }
        else if ("N".equals(haveNotDealWarn))
        {
            mSql.append(" and tt.no_deal_num = 0");
        }
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append(" select count(*) as c ").append(mSql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select tt.cust_id, tt.cust_name, tt.id_card, tt.no_deal_num");
        pSql.append(", tl.id as warn_id, tl.type_ as warn_type, tl.level, tl.descript, tl.warn_time ");
        pSql.append(mSql);
        pSql.append(" order by tl.warn_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        @SuppressWarnings("unchecked")
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .addScalar("cust_id", Hibernate.STRING).addScalar("cust_name", Hibernate.STRING)
                .addScalar("id_card", Hibernate.STRING).addScalar("no_deal_num", Hibernate.INTEGER)
                .addScalar("warn_id", Hibernate.STRING).addScalar("warn_type", Hibernate.STRING)
                .addScalar("level", Hibernate.INTEGER).addScalar("descript", Hibernate.STRING)
                .addScalar("warn_time", Hibernate.TIMESTAMP).setParameters(params.getVals(), params.getTypes()).list();
        List<CheckWarn> list = new ArrayList<CheckWarn>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            CheckWarn checkWarn = new CheckWarn();
            idx = 0;
            checkWarn.setCustId((String) objs[idx]);
            idx++;
            checkWarn.setName((String) objs[idx]);
            idx++;
            checkWarn.setIdCard((String) objs[idx]);
            idx++;
            checkWarn.setNoDealNum((Integer) objs[idx]);
            idx++;
            checkWarn.setId((String) objs[idx]);
            idx++;
            checkWarn.setType((String) objs[idx]);
            idx++;
            checkWarn.setLevel((Integer) objs[idx]);
            idx++;
            checkWarn.setDescript((String) objs[idx]);
            idx++;
            checkWarn.setWarnTime((Timestamp) objs[idx]);
            idx++;
            list.add(checkWarn);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 客服端获取个人预警 未处理的。
     * @author huangGang
     * 2015.4.29
     * */
    @SuppressWarnings("unchecked")
    public List<CheckWarn> getWarnListForClient(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from chk_check_warn t ");
        mSql.append(" inner join sys_user su on su.id = t.cust_id ");
        mSql.append(" where 1 = 1 ");
        String custId = (String) filter.get("custId");
        mSql.append(" and t.cust_id = ?");
        params.add(custId, Hibernate.STRING);
        mSql.append(" and t.state = ?");
        params.add(ConstChr.CHR_ISHANDLED_NOT, Hibernate.INTEGER);
        // 指定拉取时间 上拉 是之前的数据，下拉是之后的数据 下拉的时间比实际的时间少1
        //1表示同步时间点之后的数据，2表示同步时间点之前的数据
        Timestamp sysTime = (Timestamp) filter.get("sysTime");
        if (sysTime != null)
        {
            mSql.append(" and t.warn_time < ?");
            params.add(sysTime, Hibernate.TIMESTAMP);
        }
        mSql.append(" order by warn_time desc");
        // 取数据条数
        mSql.append(" limit 0, ?");
        params.add(filter.get("maxSize"), Hibernate.INTEGER);
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.cust_id, t.type_, t.level, t.descript, t.state, t.warn_time");
        pSql.append(", su.id_card, su.name_ as custName ");
        pSql.append(mSql);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("type_", Hibernate.STRING)
                .addScalar("level", Hibernate.INTEGER).addScalar("descript", Hibernate.STRING)
                .addScalar("state", Hibernate.INTEGER).addScalar("warn_time", Hibernate.TIMESTAMP)
                .addScalar("id_card", Hibernate.STRING).addScalar("custName", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<CheckWarn> list = new ArrayList<CheckWarn>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            CheckWarn checkWarn = new CheckWarn();
            idx = 0;
            checkWarn.setId((String) objs[idx]);
            idx++;
            checkWarn.setCustId((String) objs[idx]);
            idx++;
            checkWarn.setType((String) objs[idx]);
            idx++;
            checkWarn.setLevel((Integer) objs[idx]);
            idx++;
            checkWarn.setDescript((String) objs[idx]);
            idx++;
            checkWarn.setState((Integer) objs[idx]);
            idx++;
            checkWarn.setWarnTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            checkWarn.setIdCard((String) objs[idx]);
            idx++;
            checkWarn.setName((String) objs[idx]);
            idx++;
            list.add(checkWarn);
        }
        return list;
    }

    /**
     * 客户端查询告警会员列表
     * @param filter
     * @param sysTime
     * @param dir
     * @param count
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageTran<CheckWarn> queryWarnCustTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select tu.id, tu.name_, tu.id_card, t.type_, t.level, t.descript, t.warn_time");
        sql.append(" from chk_check_warn t");
        sql.append(" inner join sys_user tu on tu.id = t.cust_id");
        // 查询条件
        // 权限
        sql.append(" where t.warn_time = (select max(st.warn_time) from chk_check_warn st where st.state = ? and st.cust_id = t.cust_id)");
        params.add(Constant.WARNING_STATE_NOT_DEAL, Hibernate.INTEGER);
        String userId = (String) filter.get("userId");
        sql.append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = t.cust_id and smi.doctor_id = ? and smi.state = ?)");
        params.add(userId, Hibernate.STRING);
        params.add(Constant.CUSTOMER_MEMBER_STATUS_YES, Hibernate.INTEGER);
        // 同步方式
        if (sysTime == null)
        {
            sql.append(" order by t.warn_time desc");
        }
        else if (dir < 0)
        {
            if (dir < 0)
            {
                sql.append(" and t.warn_time < ? order by t.warn_time desc");
            }
            else
            {
                sql.append(" and t.warn_time > ? order by t.warn_time asc");
            }
            params.add(sysTime, Hibernate.TIMESTAMP);
        }
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
                .addScalar("type_", Hibernate.STRING).addScalar("level", Hibernate.INTEGER)
                .addScalar("descript", Hibernate.STRING).addScalar("warn_time", Hibernate.TIMESTAMP)
                .setParameters(params.getVals(), params.getTypes()).setMaxResults(count).list();
        List<CheckWarn> list = new ArrayList<CheckWarn>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            CheckWarn cw = new CheckWarn();
            idx = 0;
            cw.setCustId((String) objs[idx]);
            idx++;
            cw.setName((String) objs[idx]);
            idx++;
            cw.setIdCard((String) objs[idx]);
            idx++;
            cw.setType((String) objs[idx]);
            idx++;
            cw.setLevel((Integer) objs[idx]);
            idx++;
            cw.setDescript((String) objs[idx]);
            idx++;
            cw.setWarnTime((Timestamp) objs[idx]);
            idx++;
            list.add(cw);
        }
        return new PageTran<CheckWarn>(list);
    }

    /**
     * 获取用户告警相关人信息。
     * 用户id，用户姓名，手机号，是否会员
     * 会员用户健康管理师ID，手机号
     * 自己的推送ID信息List<Object[]>，Object[]设备类型，设备CHANNELID
     * 其他家庭成员推送ID信息List<Object[]>，Object[]设备类型，设备CHANNELID
     */
    @SuppressWarnings("unchecked")
    public Object[] getWarnRelatedPersonnels(String custId)
    {
        // 客户信息
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.name_, t.phone, tc.member_id");
        sql.append(" from sys_user t inner join sys_customer tc");
        sql.append(" on tc.user_id = t.id");
        sql.append(" where t.id = ?");
        List<Object[]> custList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("phone", Hibernate.STRING)
                .addScalar("member_id", Hibernate.STRING).setParameter(0, custId).list();
        Object[] custObjs = custList.get(0);
        String memberId = (String) custObjs[3];
        String[] cust = new String[]
        {(String) custObjs[0], (String) custObjs[1], (String) custObjs[2], StringUtil.isNotEmpty(memberId) ? "Y" : "N"};
        // 健康管理师信息
        String[] doct = new String[2];
        if (StringUtil.isNotEmpty(memberId))
        {
            StringBuilder sql1 = new StringBuilder();
            sql1.append("select tu.id, tu.phone");
            sql1.append(" from svb_member_info t inner join sys_user tu on tu.id = t.doctor_id");
            sql1.append(" where t.id = ?");
            List<Object[]> doctList = super.getSession().createSQLQuery(sql1.toString())
                    .addScalar("id", Hibernate.STRING).addScalar("phone", Hibernate.STRING).setParameter(0, memberId)
                    .list();
            if (doctList != null && doctList.size() > 0)
            {
                Object[] doctObjs = doctList.get(0);
                doct[0] = (String) doctObjs[0];
                doct[1] = (String) doctObjs[1];
            }
        }
        // 自己的推送ID信息
        StringBuilder sql2 = new StringBuilder();
        sql2.append("select t.device_type, t.push_channel_id");
        sql2.append(" from sys_push_user t where t.user_id = ?");
        List<Object[]> pushSelfList = super.getSession().createSQLQuery(sql2.toString())
                .addScalar("device_type", Hibernate.INTEGER).addScalar("push_channel_id", Hibernate.STRING)
                .setParameter(0, custId).list();
        // 家庭成员推送ID信息
        StringBuilder sql3 = new StringBuilder();
        sql3.append("select t.device_type, t.push_channel_id");
        sql3.append(" from sys_push_user t where t.user_id in (");
        sql3.append("select st.member_id from sys_cust_family st where st.cust_id = ?)");
        List<Object[]> pushFmList = super.getSession().createSQLQuery(sql3.toString())
                .addScalar("device_type", Hibernate.INTEGER).addScalar("push_channel_id", Hibernate.STRING)
                .setParameter(0, custId).list();
        return new Object[]
        {cust, doct, pushSelfList, pushFmList};
    }

}
