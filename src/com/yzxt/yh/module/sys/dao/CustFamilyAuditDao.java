package com.yzxt.yh.module.sys.dao;

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
import com.yzxt.yh.module.sys.bean.CustFamily;
import com.yzxt.yh.module.sys.bean.CustFamilyAudit;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.util.StringUtil;

public class CustFamilyAuditDao extends HibernateSupport<CustFamilyAudit> implements BaseDao<CustFamilyAudit>
{

    /**
     * 增加申请信息
     * @author h
     * @return id
     * @param custFamilyAudit
     * */
    public String insert(CustFamilyAudit custFamilyAudit) throws Exception
    {
        super.insert(custFamilyAudit);
        return custFamilyAudit.getId();
    }

    /**
     * 加载客户信息
     */
    public CustFamilyAudit load(String id) throws Exception
    {
        return super.get(id);
    }

    /**
     * 客户端获取家庭成员申请请求数据接口
     * @author h
     * @param filter 
     * @return 
     * 2015.6.24
     * */
    @SuppressWarnings("unchecked")
    public List<CustFamilyAudit> getAuditList(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from sys_cust_family_audit scfa ");
        sql.append(" inner join sys_user tu on tu.id = scfa.member_id");
        sql.append(" left join sys_file_desc sfd on sfd.id = tu.img_file_id");
        sql.append(" where 1=1 and scfa.state = ?");
        params.add(Constant.FAMILY_STATE_APPLY, Hibernate.INTEGER);
        String userId = (String) filter.get("userId");
        sql.append(" and scfa.apply_cust_id = ? GROUP BY scfa.member_id");
        params.add(userId, Hibernate.STRING);
        StringBuilder mSql = new StringBuilder();
        mSql.append(" select scfa.id, scfa.apply_cust_id, scfa.member_id, tu.phone,");
        mSql.append(" scfa.memo, scfa.apply_context, scfa.state, scfa.apply_time, scfa.audit_time,");
        mSql.append(" tu.name_, tu.id_card, sfd.path,tu.sex, tu.address,tu.type_").append(sql);
        List<Object[]> objsList = super.getSession().createSQLQuery(mSql.toString())
                .addScalar("id", Hibernate.STRING).addScalar("apply_cust_id", Hibernate.STRING)
                .addScalar("member_id", Hibernate.STRING).addScalar("phone", Hibernate.STRING)
                .addScalar("memo", Hibernate.STRING).addScalar("apply_context", Hibernate.STRING)
                .addScalar("state", Hibernate.INTEGER)
                .addScalar("apply_time", Hibernate.TIMESTAMP).addScalar("audit_time", Hibernate.TIMESTAMP)
                .addScalar("name_", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
                .addScalar("path", Hibernate.STRING).addScalar("sex", Hibernate.INTEGER).addScalar("address", Hibernate.STRING)
                .addScalar("type_", Hibernate.INTEGER)
                .setParameters(params.getVals(), params.getTypes())
                .list();
        
        List<CustFamilyAudit> list = new ArrayList<CustFamilyAudit>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            CustFamilyAudit cust = new CustFamilyAudit();
            idx = 0;
            cust.setId((String) objs[idx]);
            idx++;
            cust.setCustId((String) objs[idx]);
            idx++;
            cust.setMemberId((String) objs[idx]);
            idx++;
            cust.setMemberPhone((String) objs[idx]);
            idx++;
            cust.setMemo((String) objs[idx]);
            idx++;
            cust.setApplyContext((String) objs[idx]);
            idx++;
            cust.setState(objs[idx] != null ? Integer.parseInt(objs[idx].toString()) : null);
            idx++;
            cust.setApplyTime((Timestamp) objs[idx]);
            idx++;
            cust.setAuditTime((Timestamp) objs[idx]);
            idx++;
            cust.setApplyName((String) objs[idx]);
            idx++;
            cust.setIdCard((String) objs[idx]);
            idx++;
            cust.setPath((String) objs[idx]);
            idx++;
            cust.setSex(objs[idx] != null ? Integer.parseInt(objs[idx].toString()) : null);
            idx++;
            cust.setAddress((String) objs[idx]);
            idx++;
            cust.setType(objs[idx] != null ? Integer.parseInt(objs[idx].toString()) : null);
            idx++;
            list.add(cust);
        }
        return list;
    }

    public Integer updateStateForAudit(CustFamilyAudit cfa)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" update CustFamilyAudit t set");
        sql.append(" t.state = ?, t.auditTime = ?");
        sql.append(" where t.id = ?");
        params.add(cfa.getState(), Hibernate.INTEGER);
        params.add(cfa.getAuditTime(), Hibernate.TIMESTAMP);
        params.add(cfa.getId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 平台获取家庭申请情况
     * @param filter
     * @author h
     * @return
     * 2015.7.8
     * */
    @SuppressWarnings("unchecked")
    public PageModel<CustFamilyAudit> queryApply(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<CustFamilyAudit> pageModel = new PageModel<CustFamilyAudit>();
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from sys_cust_family_audit t ");
        sql.append(" inner join sys_user su on su.id = t.member_id ");
        sql.append(" inner join sys_customer sc on sc.user_id = t.member_id");
        sql.append(" where 1 = 1");
        sql.append(" and t.state = ? ");
        params.add(Constant.FAMILY_STATE_APPLY, Hibernate.INTEGER);
        //查询条件
        String userId = (String)filter.get("userId");
        if (StringUtil.isNotEmpty(userId))
        {
            sql.append(" and t.apply_cust_id = ? ");
            params.add(userId, Hibernate.STRING);
        }
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(t.id) as c ").append(sql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.member_id, su.name_, su.id_card, t.member_phone,t.state,t.audit_time,t.apply_time,t.apply_context,sc.sex,sc.birthday,t.memo");
        pSql.append(sql);
        pSql.append(" order by t.apply_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .addScalar("id", Hibernate.STRING).addScalar("member_id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
                .addScalar("member_phone", Hibernate.STRING).addScalar("state", Hibernate.INTEGER)
                .addScalar("audit_time", Hibernate.TIMESTAMP).addScalar("apply_time", Hibernate.TIMESTAMP)
                .addScalar("apply_context", Hibernate.STRING)
                .addScalar("sex", Hibernate.INTEGER).addScalar("birthday", Hibernate.TIMESTAMP)
                .addScalar("memo", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<CustFamilyAudit> list = new ArrayList<CustFamilyAudit>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            CustFamilyAudit custFA = new CustFamilyAudit();
            idx = 0;
            custFA.setId((String) objs[idx]);
            idx++;
            custFA.setMemberId((String) objs[idx]);
            idx++;
            custFA.setApplyName((String) objs[idx]);
            idx++;
            custFA.setIdCard((String) objs[idx]);
            idx++;
            custFA.setMemberPhone((String) objs[idx]);
            idx++;
            custFA.setState((Integer) objs[idx]);
            idx++;
            custFA.setAuditTime((Timestamp) objs[idx]);
            idx++;
            custFA.setApplyTime((Timestamp) objs[idx]);
            idx++;
            custFA.setApplyContext((String) objs[idx]);
            idx++;
            custFA.setSex((Integer) objs[idx]);
            idx++;
            custFA.setBirthday((Timestamp) objs[idx]);
            idx++;
            custFA.setMemo((String) objs[idx]);
            idx++;
            list.add(custFA);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 平台获取家庭申请情况
     * @param filter
     * @author h
     * @return
     * 2015.7.8
     * */
    @SuppressWarnings("unchecked")
    public PageModel<CustFamilyAudit> queryAudit(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<CustFamilyAudit> pageModel = new PageModel<CustFamilyAudit>();
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from sys_cust_family_audit t ");
        sql.append(" inner join sys_user su on su.id = t.apply_cust_id ");
        sql.append(" inner join sys_customer sc on sc.user_id = t.apply_cust_id");
        sql.append(" where 1 = 1");
        sql.append(" and t.state = ? ");
        params.add(Constant.FAMILY_STATE_APPLY, Hibernate.INTEGER);
        //查询条件
        String userId = (String)filter.get("userId");
        if (StringUtil.isNotEmpty(userId))
        {
            sql.append(" and t.member_id = ? ");
            params.add(userId, Hibernate.STRING);
        }
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(t.id) as c ").append(sql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.apply_cust_id, t.member_id, su.name_, su.id_card, su.phone,t.state,t.audit_time,t.apply_time,t.apply_context,sc.sex,sc.birthday,t.memo");
        pSql.append(sql);
        pSql.append(" order by t.apply_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .addScalar("id", Hibernate.STRING)
                .addScalar("apply_cust_id", Hibernate.STRING).addScalar("member_id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
                .addScalar("phone", Hibernate.STRING).addScalar("state", Hibernate.INTEGER)
                .addScalar("audit_time", Hibernate.TIMESTAMP).addScalar("apply_time", Hibernate.TIMESTAMP)
                .addScalar("apply_context", Hibernate.STRING)
                .addScalar("sex", Hibernate.INTEGER).addScalar("birthday", Hibernate.TIMESTAMP)
                .addScalar("memo", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<CustFamilyAudit> list = new ArrayList<CustFamilyAudit>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            CustFamilyAudit custFA = new CustFamilyAudit();
            idx = 0;
            custFA.setId((String) objs[idx]);
            idx++;
            custFA.setCustId((String) objs[idx]);
            idx++;
            custFA.setMemberId((String) objs[idx]);
            idx++;
            custFA.setApplyName((String) objs[idx]);
            idx++;
            custFA.setIdCard((String) objs[idx]);
            idx++;
            custFA.setMemberPhone((String) objs[idx]);
            idx++;
            custFA.setState((Integer) objs[idx]);
            idx++;
            custFA.setAuditTime((Timestamp) objs[idx]);
            idx++;
            custFA.setApplyTime((Timestamp) objs[idx]);
            idx++;
            custFA.setApplyContext((String) objs[idx]);
            idx++;
            custFA.setSex((Integer) objs[idx]);
            idx++;
            custFA.setBirthday((Timestamp) objs[idx]);
            idx++;
            custFA.setMemo((String) objs[idx]);
            idx++;
            list.add(custFA);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }
    /**
     * 修改家庭成员申请数据
     * @author h
     * @return 
     * 2015.6.18
     * */
    public int updateMemInfo(CustFamilyAudit custF)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" update CustFamilyAudit t set");
        sql.append(" t.memo = ?");
        sql.append(" where t.custId = ? and t.memberId = ?");
        params.add(custF.getMemo(), Hibernate.STRING);
        params.add(custF.getCustId(), Hibernate.STRING);
        params.add(custF.getMemberId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }
}
