package com.yzxt.yh.module.sys.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstRole;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.svb.bean.MemberInfo;
import com.yzxt.yh.module.sys.bean.CustFamilyAudit;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.vo.CustFamilyAuditVO;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

public class CustomerDao extends HibernateSupport<Customer> implements BaseDao<Customer>
{
    /**
     * 插入客户信息
     */
    public String insert(Customer cust) throws Exception
    {
        super.save(cust);
        return cust.getUserId();
    }

    /**
     * 更新客户信息
     */
    public int update(Customer cust) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update Customer set sex = ?, birthday = ?, national = ?, contactPhone = ?");
        sql.append(", degree = ?, profession = ?, maritalStatus = ?, liveAlone = ?, healthyStatus = ?");
        sql.append(", qqNumber = ?, wechatNumber = ?, address = ?, memo = ?, updateBy = ?,doctorId=?,doctorName=?, updateTime = ?");
        sql.append(" where id = ?");
        params.add(cust.getSex(), Hibernate.INTEGER).add(cust.getBirthday(), Hibernate.DATE)
                .add(cust.getNational(), Hibernate.STRING).add(cust.getContactPhone(), Hibernate.STRING)
                .add(cust.getDegree(), Hibernate.INTEGER).add(cust.getProfession(), Hibernate.STRING)
                .add(cust.getMaritalStatus(), Hibernate.INTEGER).add(cust.getLiveAlone(), Hibernate.INTEGER)
                .add(cust.getHealthyStatus(), Hibernate.INTEGER).add(cust.getQqNumber(), Hibernate.STRING)
                .add(cust.getWechatNumber(), Hibernate.STRING).add(cust.getAddress(), Hibernate.STRING)
                .add(cust.getMemo(), Hibernate.STRING).add(cust.getUpdateBy(), Hibernate.STRING)
                .add(cust.getDoctorId(), Hibernate.STRING).add(cust.getDoctorName(), Hibernate.STRING)
                .add(cust.getUpdateTime(), Hibernate.TIMESTAMP).add(cust.getUserId(), Hibernate.STRING);
        int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

    /**
     * 加载客户信息
     */
    public Customer load(String id) throws Exception
    {
        return super.get(id);
    }

    /**
     * 分页查询客户数据
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageModel<Customer> query(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<Customer> pageModel = new PageModel<Customer>();
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from sys_customer t");
        mSql.append(" inner join sys_user tu on tu.id = t.user_id");
        mSql.append(" left join sys_org tor on tor.id = tu.org_id");
        mSql.append(" where 1=1");
        // 查询条件
        String name = (String) filter.get("name");
        if (StringUtil.isNotEmpty(name))
        {
            mSql.append(" and tu.name_ like ").append(params.addLikePart(name));
        }
        String idCard = (String) filter.get("idCard");
        if (StringUtil.isNotEmpty(idCard))
        {
            mSql.append(" and tu.id_card like ").append(params.addLikePart(idCard));
        }
        // 权限过滤
        User operUser = (User) filter.get("operUser");
        if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
        {
            String orgId = operUser.getOrgId();
            // 正常情况下医生所属组织不会为空
            if (StringUtil.isNotEmpty(orgId))
            {
                mSql.append(" and tu.org_id = ?");
                params.add(orgId, Hibernate.STRING);
            }
            else
            {
                return pageModel;
            }
        }
        else if (Constant.USER_TYPE_ADMIN.equals(operUser.getType()))
        {
            Collection<String> roles = operUser.getRoles();
            // 不是系统管理员，添加组织限制
            if (roles != null && !roles.contains(ConstRole.ADMIN))
            {
                mSql.append(" and tor.full_id like ").append(params.addLikePart("/" + operUser.getOrgId() + "/"));
            }
        }
        else if (!Constant.USER_TYPE_ADMIN.equals(operUser.getType()))
        {
            return pageModel;
        }
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(t.user_id) as c ").append(mSql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.user_id, t.sex, t.birthday, t.member_id");
        pSql.append(", tu.name_, tu.id_card, tu.phone, tor.orgName as org_name");
        pSql.append(", (select su.name_ from svb_member_info sm");
        pSql.append(" inner join sys_user su on su.id = sm.doctor_id");
        pSql.append(" where sm.id = t.member_id) as doctor_name");
        pSql.append(mSql);
        // pSql.append(" order by t.update_time desc");
        pSql.append(" limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .addScalar("user_id", Hibernate.STRING).addScalar("sex", Hibernate.INTEGER)
                .addScalar("birthday", Hibernate.DATE).addScalar("member_id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
                .addScalar("phone", Hibernate.STRING).addScalar("org_name", Hibernate.STRING)
                .addScalar("doctor_name", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
        List<Customer> list = new ArrayList<Customer>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Customer cust = new Customer();
            idx = 0;
            cust.setUserId((String) objs[idx]);
            idx++;
            cust.setSex((Integer) objs[idx]);
            idx++;
            cust.setBirthday((Date) objs[idx]);
            idx++;
            cust.setMemberId((String) objs[idx]);
            idx++;
            cust.setName((String) objs[idx]);
            idx++;
            cust.setIdCard((String) objs[idx]);
            idx++;
            cust.setPhone((String) objs[idx]);
            idx++;
            cust.setOrgName((String) objs[idx]);
            idx++;
            cust.setDoctorName((String) objs[idx]);
            idx++;
            list.add(cust);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 客户端医生分页查询客户数据
     * @param filter 过滤条件
     * @param sysTime 同步时间点
     * @param dir 同步方式，大于0：取时间点后的数据，小于0取时间点前的数据
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageTran<Customer> queryTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.user_id, t.sex, t.birthday, t.create_time");
        sql.append(", tu.name_, tu.id_card, tu.phone");
        sql.append(" from sys_customer t");
        sql.append(" inner join sys_user tu on tu.id = t.user_id");
        sql.append(" where 1=1");
        // 查询条件 1：全部，2：会员，3：非会员
        String patientTypeStr = (String) filter.get("patientType");
        Integer patientType = Integer.parseInt(patientTypeStr);
        String userId = (String) filter.get("userId");
        User operUser = (User) filter.get("operUser");
        if (patientType == 1)
        {
            
            if (operUser.getOrgId().equals("1"))
            {
                sql.append(" and (tu.org_id = ? or tu.org_id is null or tu.org_id = '' ) ");
                params.add(operUser.getOrgId(), Hibernate.STRING);
            }
            else
            {
                sql.append(" and (tu.org_id = ? ) ");
                params.add(operUser.getOrgId(), Hibernate.STRING);
            }
        }
        else if (patientType == 2)
        {
            // 权限过滤 与医生相同组织的会员
            sql.append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = t.user_id and smi.doctor_id = ? and smi.state = ?)");
            params.add(userId, Hibernate.STRING);
            params.add(Constant.CUSTOMER_MEMBER_STATUS_YES, Hibernate.INTEGER);
        }
        /**查询条件非会员没有完成，需要自己写
        else if (patientType == 3)
        {
            //在医生相同组织的和没有组织的非会员
            sql.append(" and tu.org_id in ((select sur.org_id from sys_user sur where sur.id = ? ),'')");
            params.add(userId, Hibernate.STRING);
            sql.append(" t.member_status = ?");
            params.add(Constant.CUSTOMER_MEMBER_STATUS_NO, Hibernate.INTEGER);
        }
        **/
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
                .addScalar("user_id", Hibernate.STRING).addScalar("sex", Hibernate.INTEGER)
                .addScalar("birthday", Hibernate.DATE).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("name_", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
                .addScalar("phone", Hibernate.STRING).setParameters(params.getVals(), params.getTypes())
                .setMaxResults(count).list();
        List<Customer> list = new ArrayList<Customer>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Customer cust = new Customer();
            idx = 0;
            cust.setUserId((String) objs[idx]);
            idx++;
            cust.setSex((Integer) objs[idx]);
            idx++;
            cust.setBirthday((Date) objs[idx]);
            idx++;
            cust.setCreateTime((Timestamp) objs[idx]);
            idx++;
            cust.setName((String) objs[idx]);
            idx++;
            cust.setIdCard((String) objs[idx]);
            idx++;
            cust.setPhone((String) objs[idx]);
            idx++;
            list.add(cust);
        }
        return new PageTran<Customer>(list);
    }

    /**
     * 查询客户列表，用于生成客户分析报告
     * @param filter
     * @param startRow
     * @param maxSize
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Customer> getCustomers(Map<String, Object> filter, int startRow, int maxSize)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.user_id, t.member_id");
        sql.append(" from sys_customer t");
        sql.append(" where 1=1");
        Date maxCreateTime = (Date) filter.get("maxCreateTime");
        if (maxCreateTime != null)
        {
            sql.append(" and t.create_time <= ?");
            params.add(maxCreateTime, Hibernate.DATE);
        }
        sql.append(" order by t.user_id asc");
        sql.append(" limit ?, ?");
        params.add(startRow, Hibernate.INTEGER);
        params.add(maxSize, Hibernate.INTEGER);
        List<Object[]> list = super.getSession().createSQLQuery(sql.toString()).addScalar("user_id", Hibernate.STRING)
                .addScalar("member_id", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
        List<Customer> custs = new ArrayList<Customer>();
        if (list != null && !list.isEmpty())
        {
            int i = 0;
            for (Object[] objs : list)
            {
                i = 0;
                Customer cust = new Customer();
                cust.setUserId((String) objs[i]);
                i++;
                custs.add(cust);
            }
        }
        return custs;
    }

    /**
     * 修改账号是修改客户信息
     * @return
     */
    public int updateByAccount(Customer cust)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update Customer set sex = ?, birthday = ?, address = ?, updateBy = ?, updateTime = ?");
        sql.append(" where id = ?");
        params.add(cust.getSex(), Hibernate.INTEGER).add(cust.getBirthday(), Hibernate.DATE)
                .add(cust.getAddress(), Hibernate.STRING).add(cust.getUpdateBy(), Hibernate.STRING)
                .add(cust.getUpdateTime(), Hibernate.TIMESTAMP).add(cust.getUserId(), Hibernate.STRING);
        int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

    /**
     * 分页查询会员数据
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageModel<Customer> queryMember(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<Customer> pageModel = new PageModel<Customer>();
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from sys_customer t");
        mSql.append(" inner join sys_user tu on tu.id = t.user_id");
        mSql.append(" left join sys_org tor on tor.id = tu.org_id");
        mSql.append(" where 1=1");
        // 查询条件
        String name = (String) filter.get("name");
        if (StringUtil.isNotEmpty(name))
        {
            mSql.append(" and tu.name_ like ").append(params.addLikePart(name));
        }
        String idCard = (String) filter.get("idCard");
        if (StringUtil.isNotEmpty(idCard))
        {
            mSql.append(" and tu.id_card like ").append(params.addLikePart(idCard));
        }
        Integer healthyStatus = (Integer) filter.get("healthyStatus");
        if (healthyStatus != null)
        {
            mSql.append(" and t.healthy_status = ?");
            params.add(healthyStatus, Hibernate.INTEGER);
        }
        //权限控制
        // 操作人
        User user = (User) filter.get("operUser");
        // 权限
        if (Constant.USER_TYPE_DOCTOR.equals(user.getType()))
        // 医生查询自己的会员记录
        {
            mSql.append(" and exists(select smi.id from svb_member_info smi where smi.state = ? and smi.cust_id = t.user_id and smi.doctor_id = ?)");
            params.add(Constant.CUSTOMER_MEMBER_STATUS_YES, Hibernate.INTEGER);
            params.add(user.getId(), Hibernate.STRING);
        }
        else if (Constant.USER_TYPE_CUSTOMER.equals(user.getType()))
        // 普通客户查询自己的记录
        {
            mSql.append(" and t.user_id = ?");
            params.add(user.getId(), Hibernate.STRING);
        }
        else
        // 其它非管理员用户不能查询到记录
        {
            return pageModel;
        }
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(t.user_id) as c ").append(mSql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.user_id, tu.sex, t.birthday, t.member_id, t.healthy_status");
        pSql.append(", tu.name_, tu.id_card, tu.phone, tor.orgName as org_name, '' as member_id");
        pSql.append(", (select max(smi.start_day) from svb_member_info smi where smi.cust_id = t.user_id and smi.state = ");
        pSql.append(Constant.CUSTOMER_MEMBER_STATUS_YES).append(") as start_day");
        pSql.append(", (select max(smi.end_day) from svb_member_info smi where smi.cust_id = t.user_id and smi.state = ");
        pSql.append(Constant.CUSTOMER_MEMBER_STATUS_YES).append(") as end_day");
        pSql.append(mSql);
        pSql.append(" order by t.update_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .addScalar("user_id", Hibernate.STRING).addScalar("sex", Hibernate.INTEGER)
                .addScalar("birthday", Hibernate.DATE).addScalar("member_id", Hibernate.STRING)
                .addScalar("healthy_status", Hibernate.INTEGER).addScalar("name_", Hibernate.STRING)
                .addScalar("id_card", Hibernate.STRING).addScalar("phone", Hibernate.STRING)
                .addScalar("org_name", Hibernate.STRING).addScalar("member_id", Hibernate.STRING)
                .addScalar("start_day", Hibernate.DATE).addScalar("end_day", Hibernate.DATE)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Customer> list = new ArrayList<Customer>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Customer cust = new Customer();
            idx = 0;
            cust.setUserId((String) objs[idx]);
            idx++;
            cust.setSex((Integer) objs[idx]);
            idx++;
            cust.setBirthday((Date) objs[idx]);
            idx++;
            cust.setMemberId((String) objs[idx]);
            idx++;
            cust.setHealthyStatus((Integer) objs[idx]);
            idx++;
            cust.setName((String) objs[idx]);
            idx++;
            cust.setIdCard((String) objs[idx]);
            idx++;
            cust.setPhone((String) objs[idx]);
            idx++;
            cust.setOrgName((String) objs[idx]);
            idx++;
            cust.setMemberId((String) objs[idx]);
            idx++;
            cust.setStartDay((Date) objs[idx]);
            idx++;
            cust.setEndDay((Date) objs[idx]);
            idx++;
            list.add(cust);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    @SuppressWarnings("unchecked")
    public Customer getMemberInfo(String id) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from sys_customer t ");
        sql.append(" inner join svb_member_info tmi on tmi.cust_id = t.user_id");
        sql.append(" where 1=1 and tmi.state=1 and t.user_id = ?");
        params.add(id, Hibernate.STRING);
        StringBuilder mSql = new StringBuilder();
        mSql.append(" select tmi.id as member_id, t.user_id").append(sql);
        List<Object[]> objsList = super.getSession().createSQLQuery(mSql.toString())
                .addScalar("member_id", Hibernate.STRING).addScalar("user_id", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        Customer customer = null;
        if (objsList != null && objsList.size() > 0)
        {
            customer = new Customer();
            Object[] objs = objsList.get(0);
            int idx = 0;
            customer.setMemberId((String) objs[idx]);
            idx++;
            customer.setUserId((String) objs[idx]);
            idx++;
        }
        return customer;
    }

    @SuppressWarnings("unchecked")
    public User getUserByPhone(String phone)
    {

        HibernateParams params = new HibernateParams();
        String hql = "select t from User t where t.phone = ? ";
        params.add(phone, Hibernate.STRING);
        List<User> users = super.getSession().createQuery(hql).setParameters(params.getVals(), params.getTypes())
                .list();
        return users != null && !users.isEmpty() ? users.get(0) : null;

    }

    @SuppressWarnings("unchecked")
    public MemberInfo getMemberInfoByCustId(String custId)
    {

        HibernateParams params = new HibernateParams();
        String hql = "select t from MemberInfo t where t.custId = ? ";
        params.add(custId, Hibernate.STRING);
        List<MemberInfo> memberInfos = super.getSession().createQuery(hql)
                .setParameters(params.getVals(), params.getTypes()).list();
        return memberInfos != null && !memberInfos.isEmpty() ? memberInfos.get(0) : null;

    }

    @SuppressWarnings("unchecked")
    public List<CustFamilyAudit> getCustFamilyAuditByMemberId(String memberId)
    {
        HibernateParams params = new HibernateParams();
        String hql = "select t from CustFamilyAudit t where t.memberId = ? ";
        params.add(memberId, Hibernate.STRING);
        List<CustFamilyAudit> custFamilyAudits = super.getSession().createQuery(hql)
                .setParameters(params.getVals(), params.getTypes()).list();
        return custFamilyAudits != null && !custFamilyAudits.isEmpty() ? custFamilyAudits : null;
    }

    /**
     * 分页查询客户数据
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @SuppressWarnings(
    {"unchecked"})
    public PageModel<CustFamilyAuditVO> queryFamily(int page, int pageSize, String custId)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from sys_user u,sys_customer c,sys_cust_family_audit f");
        mSql.append(" where u.id=f.cust_id and u.id=c.user_id");
        // 查询条件
        if (StringUtil.isNotEmpty(custId))
        {
            mSql.append(" and f.cust_id = ?");
            params.add(custId, Hibernate.STRING);
        }

        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(u.id) as c ").append(mSql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();

        pSql.append("select u.name_, u.id_card ,f.member_phone,f.state,f.audit_time,f.apply_time,f.id,f.apply_context,c.sex,c.birthday,f.member_homeowner_relation");
        pSql.append(mSql);
        pSql.append(" order by f.audit_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .addScalar("name_", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
                .addScalar("member_phone", Hibernate.STRING).addScalar("state", Hibernate.INTEGER)
                .addScalar("audit_time", Hibernate.TIMESTAMP).addScalar("apply_time", Hibernate.TIMESTAMP)
                .addScalar("id", Hibernate.STRING).addScalar("apply_context", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<CustFamilyAuditVO> list = new ArrayList<CustFamilyAuditVO>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            CustFamilyAuditVO custFamilyAuditVO = new CustFamilyAuditVO();
            //Customer cust = new Customer();
            idx = 0;
            custFamilyAuditVO.setName((String) objs[idx]);
            idx++;
            Object object2 = objs[idx];
            if (object2 != null && object2 != "")
            {
                custFamilyAuditVO.setIdCard((String) objs[idx]);
            }
            else
            {
                custFamilyAuditVO.setIdCard(null);
            }
            idx++;
            custFamilyAuditVO.setPhone((String) objs[idx]);
            idx++;
            custFamilyAuditVO.setState((Integer) objs[idx]);
            idx++;
            custFamilyAuditVO.setAuditTime((Timestamp) objs[idx]);
            idx++;
            Object object = objs[idx];
            if (object != null && object != "")
            {
                custFamilyAuditVO.setApplyTime((Timestamp) objs[idx]);
            }
            else
            {
                custFamilyAuditVO.setApplyTime(null);
            }
            idx++;
            custFamilyAuditVO.setId((String) objs[idx]);
            idx++;
            custFamilyAuditVO.setApplyContext((String) objs[idx]);
            idx++;
            custFamilyAuditVO.setSex((Integer) objs[idx]);
            idx++;
            Date birthday = (Date) objs[idx];
            if (birthday != null)
            {
                custFamilyAuditVO.setAge(DateUtil.getAge(birthday, null));
                idx++;
            }
            else
            {
                custFamilyAuditVO.setAge(null);
            }
            custFamilyAuditVO.setRelation((Integer) objs[idx]);
            idx++;
            list.add(custFamilyAuditVO);
            list.add(custFamilyAuditVO);
        }
        PageModel<CustFamilyAuditVO> pageModel = new PageModel<CustFamilyAuditVO>();
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 更新客户信息
     */
    public int updateByAchive(Customer cust) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update Customer set sex = ?, birthday = ?, national = ?,profession=?");
        sql.append(", address = ?, memo = ?, updateBy = ?, updateTime = ?");
        sql.append(" where userId = ?");
        params.add(cust.getSex(), Hibernate.INTEGER).add(cust.getBirthday(), Hibernate.DATE)
                .add(cust.getNational(), Hibernate.STRING).add(cust.getProfession(), Hibernate.STRING)
                .add(cust.getAddress(), Hibernate.STRING).add(cust.getMemo(), Hibernate.STRING)
                .add(cust.getUpdateBy(), Hibernate.STRING).add(cust.getUpdateTime(), Hibernate.TIMESTAMP)
                .add(cust.getUserId(), Hibernate.STRING);
        int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

    /**
     *  加入会员时修改客户信息
     */
    public int updateByMember(Customer cust) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update Customer set memberId = ?");
        sql.append(" where userId = ?");
        params.add(cust.getMemberId(), Hibernate.STRING).add(cust.getUserId(), Hibernate.STRING);
        int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

    /**
     * 用户家庭圈查询
     * @author h
     * @return
     * @param filter
     * 2015.6.19
     * */
    @SuppressWarnings("unchecked")
    public List<Customer> queryCustTran(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.user_id, t.sex, t.birthday, t.create_time");
        sql.append(", tu.name_, tu.id_card, tu.phone, sfd.path");
        sql.append(" from sys_customer t");
        sql.append(" inner join sys_user tu on tu.id = t.user_id");
        sql.append(" left join sys_file_desc sfd on sfd.id = tu.img_file_id");
        sql.append(" where 1=1");
        // 查询条件：姓名或是电话号码
        String userId = (String) filter.get("userId");
        String keyword = (String) filter.get("keyword");
        // 是否要排除指定ID
        if (StringUtil.isNotEmpty(userId))
        {
            sql.append(" and t.user_id <> ?");
            params.add(userId, Hibernate.STRING);
        }
        if (StringUtil.isNotEmpty(keyword))
        {
            sql.append(" and (tu.name_ like ").append(params.addLikePart(keyword));
            sql.append(" or tu.phone = ? )");
            params.add(keyword, Hibernate.STRING);
        }
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("user_id", Hibernate.STRING).addScalar("sex", Hibernate.INTEGER)
                .addScalar("birthday", Hibernate.DATE).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("name_", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
                .addScalar("phone", Hibernate.STRING).addScalar("path", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Customer> list = new ArrayList<Customer>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Customer cust = new Customer();
            idx = 0;
            cust.setUserId((String) objs[idx]);
            idx++;
            cust.setSex((Integer) objs[idx]);
            idx++;
            cust.setBirthday((Date) objs[idx]);
            idx++;
            cust.setCreateTime((Timestamp) objs[idx]);
            idx++;
            cust.setName((String) objs[idx]);
            idx++;
            cust.setIdCard((String) objs[idx]);
            idx++;
            cust.setPhone((String) objs[idx]);
            idx++;
            cust.setPath((String) objs[idx]);
            idx++;
            list.add(cust);
        }
        return list;
    }

    /**
     * 不同机构
     * 统计用户每天注册用户数，现在暂时不分平台还是客户端还是一体机
     * @author h
     * 2015.7.24
     * */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryRegisterNum(Map<String, Object> filter)
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from sys_customer t");
        sql.append(" inner join sys_user su on su.id = t.user_id");
        sql.append(" inner join sys_org so on so.id = su.org_id");
        sql.append(" where 1 = 1 ");
        //查询条件
        // 权限过滤
        User operUser = (User) filter.get("operUser");
        if (Constant.USER_TYPE_ADMIN.equals(operUser.getType()))
        {
            Collection<String> roles = operUser.getRoles();
            // 不是系统管理员，添加组织限制
            if (roles != null && !roles.contains(ConstRole.ADMIN))
            {
                sql.append(" and so.full_id like ").append(params.addLikePart("/" + operUser.getOrgId() + "/"));
            }
        }
        else if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
        {
            String orgId = operUser.getOrgId();
            // 正常情况下医生所属组织不会为空
            if (StringUtil.isNotEmpty(orgId))
            {
                sql.append(" and so.org_id = ?");
                params.add(orgId, Hibernate.STRING);
            }
            else
            {
                return list;
            }
        }
        else if (!Constant.USER_TYPE_ADMIN.equals(operUser.getType()))
        {
            return list;
        }
        String orgId = (String) filter.get("orgId");
        if (orgId != null)
        {
            sql.append(" and su.org_id = ? ");
            params.add(Integer.parseInt(orgId), Hibernate.STRING);
        }
        Date startDate = (Date) filter.get("startDate");
        if (startDate != null)
        {
            sql.append(" and t.create_time >= ?");
            params.add(startDate, Hibernate.DATE);
        }
        Date endDate = (Date) filter.get("endDate");
        if (endDate != null)
        {
            sql.append(" and t.create_time < ?");
            params.add(endDate, Hibernate.DATE);
        }
        /*sql.append(" GROUP BY left(t.create_time,10)");*/
        sql.append(" GROUP BY su.org_id ");
        StringBuilder mSql = new StringBuilder();
        /*mSql.append("select count(t.user_id) as c, left(t.create_time,10) as createTime ").append(sql);
         *mSql.append(" order by t.create_time desc ");*/
        mSql.append("select count(t.user_id) as c, so.name_ as orgName ").append(sql);
        mSql.append(" order by so.create_time desc ");
        List<Object[]> objsList = super.getSession().createSQLQuery(mSql.toString()).addScalar("c", Hibernate.STRING)
                .addScalar("orgName", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
        if (objsList != null)
        {
            int idx = 0;
            for (Object[] objs : objsList)
            {
                idx = 0;
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("c", objs[idx] != null ? objs[idx] : Integer.valueOf(0));
                idx++;
                map.put("orgName", objs[idx] != null ? objs[idx] : Integer.valueOf(0));
                idx++;
                list.add(map);
            }
        }
        return list;
    }

}
