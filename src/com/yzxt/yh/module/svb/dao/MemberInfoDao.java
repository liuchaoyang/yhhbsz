package com.yzxt.yh.module.svb.dao;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.Doctor;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

public class MemberInfoDao extends HibernateSupport<MemberInfo> implements BaseDao<MemberInfo>
{
    public String insert(MemberInfo memberInfo) throws Exception
    {
        super.save(memberInfo);
        return memberInfo.getId();
    }

    /**
     * 更新会员信息
     */
    public int update(MemberInfo memberInfo)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update MemberInfo set endDay = ?, doctorId = ?, org_id = ?");
        sql.append(" where id = ?");
        params.add(memberInfo.getEndDay(), Hibernate.DATE);
        params.add(memberInfo.getDoctorId(), Hibernate.STRING);
        params.add(memberInfo.getOrgId(), Hibernate.STRING);
        params.add(memberInfo.getId(), Hibernate.STRING);
        int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

    /**
     * 查询客户会员信息
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public PageModel<Customer> queryCust(Map<String, Object> filter, int page, int pageSize) throws ParseException
    {
        PageModel<Customer> pageModel = new PageModel<Customer>();
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from sys_customer t");
        mSql.append(" inner join sys_user tu on tu.id = t.user_id");
        mSql.append(" left join svb_member_info tmi on tmi.state = ").append(Constant.CUSTOMER_MEMBER_STATUS_YES)
                .append(" and tmi.id = t.member_id");
        //mSql.append(" inner join sys_org tor on tor.id = tu.org_id");
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
        String memberStatus = (String) filter.get("memberStatus");
        if (StringUtil.isNotEmpty(memberStatus))
        {
            if (memberStatus.equals("Y"))
            {
                mSql.append(" and (t.member_id <> '' and t.member_id is not null)");
            }
            if (memberStatus.equals("N"))
            {
                mSql.append(" and (t.member_id = '' or t.member_id is null)");
            }
        }
        // 权限
        User operUser = (User) filter.get("operUser");
        /*if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
        {
            // 正常情况下医生所属组织不会为空
            mSql.append(" and tu.org_id = ?");
            params.add(operUser.getOrgId(), Hibernate.STRING);
        }
        else if (Constant.USER_TYPE_ADMIN.equals(operUser.getType()))
        {
            // 不是系统管理员，添加组织限制
            Collection<String> roles = operUser.getRoles();
            if (roles == null || !roles.contains(ConstRole.ADMIN))
            {
                mSql.append(" and tor.full_id like ").append(params.addLikePart("/" + operUser.getOrgId() + "/"));
            }
        }
        else
        {
            return pageModel;
        }*/
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(t.user_id) as c ").append(mSql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.user_id, t.sex, t.birthday, t.member_id");
        pSql.append(", tu.name_, tu.id_card, tu.phone, tmi.start_day, tmi.end_day");
        //pSql.append(", tor.name_ as org_name");
        pSql.append(mSql);
        pSql.append(" order by t.update_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .addScalar("user_id", Hibernate.STRING).addScalar("sex", Hibernate.INTEGER)
                .addScalar("birthday", Hibernate.DATE).addScalar("member_id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
                .addScalar("phone", Hibernate.STRING).addScalar("start_day", Hibernate.DATE)
                .addScalar("end_day", Hibernate.DATE)/*.addScalar("org_name", Hibernate.STRING)*/
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Customer> list = new ArrayList<Customer>();
        int i = 0;
        for (Object[] objs : objsList)
        {
            Customer cust = new Customer();
            i = 0;
            cust.setUserId((String) objs[i]);
            i++;
            cust.setSex((Integer) objs[i]);
            i++;
            cust.setBirthday((Date) objs[i]);
            i++;
            cust.setMemberId((String) objs[i]);
            i++;
            cust.setName((String) objs[i]);
            i++;
            cust.setIdCard((String) objs[i]);
            i++;
            cust.setPhone((String) objs[i]);
            i++;
            cust.setStartDay((Date) objs[i]);
            i++;
            cust.setEndDay((Date) objs[i]);
            i++;
            /*cust.setOrgName((String) objs[i]);
            i++;*/
            list.add(cust);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 分页查询客户数据
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws ParseException 
     */
    @SuppressWarnings("unchecked")
    public PageModel<Customer> query(Map<String, Object> filter, int page, int pageSize) throws ParseException
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
        String memberStatus = (String) filter.get("memberStatus");
        if (StringUtil.isNotEmpty(memberStatus))
        {
            if (memberStatus.equals("1"))
            {
                mSql.append(" and (t.member_id <> '' and t.member_id is not null)");
            }
            if (memberStatus.equals("2"))
            {
                mSql.append(" and (t.member_id = '' or t.member_id is null)");
            }
        }
        //权限控制
        // 操作人
        User user = (User) filter.get("operUser");
        // 权限
        if (Constant.USER_TYPE_DOCTOR.equals(user.getType()))
        // 医生查询自己的会员记录
        {
            mSql.append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = t.user_id and smi.doctor_id = ? and smi.state = ?)");
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
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(t.user_id) as c ").append(mSql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.user_id, t.sex, t.birthday, t.member_id");
        pSql.append(", tu.name_, tu.id_card, tu.phone");
        pSql.append(mSql);
        pSql.append(" order by t.update_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .addScalar("user_id", Hibernate.STRING).addScalar("sex", Hibernate.INTEGER)
                .addScalar("birthday", Hibernate.DATE).addScalar("member_id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
                .addScalar("phone", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
        List<Customer> list = new ArrayList<Customer>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Customer cust = new Customer();
            idx = 0;
            cust.setUserId((String) objs[idx]);
            MemberInfo memberInfo = getMemberInfoByCustId((String) objs[idx]);
            if (memberInfo != null)
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if (memberInfo.getStartDay() != null)
                {
                    String htmlDate = DateUtil.toHtmlDate(memberInfo.getStartDay());
                    Date parse = sdf.parse(htmlDate);
                    cust.setStartDay(parse);
                }
                if (memberInfo.getEndDay() != null)
                {
                    String htmlDate = DateUtil.toHtmlDate(memberInfo.getEndDay());
                    Date parse2 = sdf.parse(htmlDate);
                    cust.setEndDay(parse2);
                }

            }
            else
            {
                cust.setStartDay(null);
                cust.setEndDay(null);
            }
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
            list.add(cust);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 根据客户ID获取客户会员信息
     * @param id
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public MemberInfo getMemberInfo(String id) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from svb_member_info smi");
        /*sql.append(" inner join sys_user on sur.id = smi.cust_id");*/
        sql.append(" inner join sys_user su on su.id = smi.doctor_id");
        sql.append(" where smi.state= ? and smi.cust_id = ?");
        params.add(Constant.USER_STATE_EFFECTIVE, Hibernate.INTEGER);
        params.add(id, Hibernate.STRING);
        StringBuilder mSql = new StringBuilder();
        mSql.append(" select smi.id, smi.cust_id, smi.doctor_id, su.name_ as doctorName, smi.state").append(sql);
        List<Object[]> objsList = super.getSession().createSQLQuery(mSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("doctor_id", Hibernate.STRING)
                .addScalar("doctorName", Hibernate.STRING).addScalar("state", Hibernate.INTEGER)
                .setParameters(params.getVals(), params.getTypes()).list();
        MemberInfo memberInfo = null;
        if (objsList != null && objsList.size() > 0)
        {
            memberInfo = new MemberInfo();
            Object[] objs = objsList.get(0);
            int idx = 0;
            memberInfo.setId((String) objs[idx]);
            idx++;
            memberInfo.setCustId((String) objs[idx]);
            idx++;
            memberInfo.setDoctorId((String) objs[idx]);
            idx++;
            memberInfo.setDoctorName((String) objs[idx]);
            idx++;
            memberInfo.setState((Integer) objs[idx]);
            idx++;
        }
        return memberInfo;
    }

    /**
     * 根据客户ID获取会员信息
     * @param custID
     * @return
     */
    @SuppressWarnings("unchecked")
    public MemberInfo getMemberInfoByCustId(String custID)
    {
        MemberInfo memberInfo = new MemberInfo();
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.start_day, t.end_day, t.id, t.state, t.doctor_id from svb_member_info t where 1=1 ");
        sql.append(" and t.state = ? and t.cust_id = ?");
        params.add(Constant.CUSTOMER_MEMBER_STATUS_YES, Hibernate.INTEGER);
        params.add(custID.toLowerCase(), Hibernate.STRING);
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("start_day", Hibernate.DATE).addScalar("end_day", Hibernate.DATE)
                .addScalar("id", Hibernate.STRING).addScalar("state", Hibernate.INTEGER)
                .addScalar("doctor_id", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
        if (objsList != null && objsList.size() > 0)
        {
            memberInfo = new MemberInfo();
            Object[] objs = objsList.get(0);
            int i = 0;
            memberInfo.setStartDay((Date) objs[i]);
            i++;
            memberInfo.setEndDay((Date) objs[i]);
            i++;
            memberInfo.setId((String) objs[i]);
            i++;
            memberInfo.setState((Integer) objs[i]);
            i++;
            memberInfo.setDoctorId((String) objs[i]);
            i++;
        }
        // 查询结果nate
        return memberInfo;
    }

    /**
     * 修改会员状态
     * @param now
     * @return
     */
    public int updateStatusByOutDate(Date now)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update svb_member_info t, sys_customer tc");
        sql.append(" set t.state = ?, tc.member_id = null");
        sql.append(" where t.cust_id = tc.user_id and t.end_day < ? and t.state = ?");
        params.add(Constant.CUSTOMER_MEMBER_STATUS_NO, Hibernate.INTEGER);
        params.add(DateUtil.truncDay(now), Hibernate.DATE);
        params.add(Constant.CUSTOMER_MEMBER_STATUS_YES, Hibernate.INTEGER);
        int c = super.getSession().createSQLQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

    /**
     * 分页查询客户数据
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws ParseException 
     */
    @SuppressWarnings("unchecked")
    public PageModel<User> queryDocSel(Map<String, Object> filter, int page, int pageSize) throws ParseException
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from sys_user t");
        mSql.append(" inner join sys_org tor on tor.id = t.org_id");
        mSql.append(" where 1=1");
        // 查询条件
        String orgId = (String) filter.get("orgId");
        if (StringUtil.isNotEmpty(orgId))
        {
            mSql.append(" and t.org_id = ?");
            params.add(orgId, Hibernate.STRING);
        }
        String name = (String) filter.get("name");
        if (StringUtil.isNotEmpty(name))
        {
            mSql.append(" and t.name_ like ").append(params.addLikePart(name));
        }
        mSql.append(" and t.type_ = ").append(Constant.USER_TYPE_DOCTOR);
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(t.id) as c ").append(mSql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.name_, t.phone, tor.orgName as org_name");
        pSql.append(mSql);
        pSql.append(" order by t.update_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("phone", Hibernate.STRING)
                .addScalar("org_name", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
        List<User> list = new ArrayList<User>();
        for (Object[] objs : objsList)
        {
            int i = 0;
            User user = new User();
            user.setId((String) objs[i]);
            i++;
            user.setName((String) objs[i]);
            i++;
            user.setPhone((String) objs[i]);
            i++;
            user.setOrgName((String) objs[i]);
            i++;
            list.add(user);
        }
        PageModel<User> pageModel = new PageModel<User>();
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 判读客户是否是会员
     * @param custId
     * @return
     */
    public boolean getIsMember(String custId,String doctorId)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(t.id) as c from svb_member_info t");
        sql.append(" where t.state = ? and t.cust_id = ? and doctor_id = ?");
        params.add(Constant.CUSTOMER_MEMBER_STATUS_YES, Hibernate.INTEGER);
        params.add(custId, Hibernate.STRING);
        params.add(doctorId, Hibernate.STRING);
        Integer c = (Integer) super.getSession().createSQLQuery(sql.toString()).addScalar("c", Hibernate.INTEGER)
                .setParameters(params.getVals(), params.getTypes()).uniqueResult();
        return c.intValue() > 0;
    }
    /**
     * 判读客户是否是会员
     * @param custId
     * @return
     */
    public MemberInfo getMember(String custId,String doctorId)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from MemberInfo t");
        sql.append(" where t.custId = ? and t.doctorId = ?");
        params.add(custId, Hibernate.STRING);
        params.add(doctorId, Hibernate.STRING);
        return  (MemberInfo) super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).uniqueResult();
    }

    /**
     * 客户端查询客户会员信息
     */
    @SuppressWarnings("unchecked")
    public PageTran<Customer> queryCustTran(Map<String, Object> filter, Timestamp sysTime, int pageSize)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.user_id, t.sex, t.birthday, t.member_id");
        sql.append(", tu.name_, tu.id_card, tu.phone, tmi.start_day, tmi.end_day");
        sql.append(", tor.orgName as org_name");
        sql.append(", t.create_time");
        sql.append(" from sys_customer t");
        sql.append(" inner join sys_user tu on tu.id = t.user_id");
        sql.append(" left join svb_member_info tmi on tmi.state = ").append(Constant.CUSTOMER_MEMBER_STATUS_YES)
                .append(" and tmi.id = t.member_id");
        sql.append(" inner join sys_org tor on tor.id = tu.org_id");
        sql.append(" where 1=1");
        // 查询条件
        String memberStatus = (String) filter.get("memberStatus");
        if (StringUtil.isNotEmpty(memberStatus))
        {
            if (memberStatus.equals("Y"))
            {
                sql.append(" and (t.member_id <> '' and t.member_id is not null)");
            }
            if (memberStatus.equals("N"))
            {
                sql.append(" and (t.member_id = '' or t.member_id is null)");
            }
        }
        // 权限
        User operUser = (User) filter.get("operUser");
        if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
        {
            // 正常情况下医生所属组织不会为空
            sql.append(" and tu.org_id = ?");
            params.add(operUser.getOrgId(), Hibernate.STRING);
        }
        else if (!Constant.USER_TYPE_ADMIN.equals(operUser.getType()))
        {
            return new PageTran<Customer>();
        }
        // 同步方式
        if (sysTime == null)
        {
            sql.append(" order by t.create_time desc");
        }
        else
        {
            sql.append(" and t.create_time < ? order by t.create_time desc");
            params.add(sysTime, Hibernate.TIMESTAMP);
        }
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("user_id", Hibernate.STRING).addScalar("sex", Hibernate.INTEGER)
                .addScalar("birthday", Hibernate.DATE).addScalar("member_id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
                .addScalar("phone", Hibernate.STRING).addScalar("start_day", Hibernate.DATE)
                .addScalar("end_day", Hibernate.DATE).addScalar("org_name", Hibernate.STRING)
                .addScalar("create_time", Hibernate.TIMESTAMP).setParameters(params.getVals(), params.getTypes())
                .list();
        List<Customer> list = new ArrayList<Customer>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Customer c = new Customer();
            idx = 0;
            c.setUserId((String) objs[idx]);
            idx++;
            c.setSex((Integer) objs[idx]);
            idx++;
            c.setBirthday((Date) objs[idx]);
            idx++;
            c.setMemberId((String) objs[idx]);
            idx++;
            c.setName((String) objs[idx]);
            idx++;
            c.setIdCard((String) objs[idx]);
            idx++;
            c.setPhone((String) objs[idx]);
            idx++;
            c.setStartDay((Date) objs[idx]);
            idx++;
            c.setEndDay((Date) objs[idx]);
            idx++;
            c.setOrgName((String) objs[idx]);
            idx++;
            c.setCreateTime((Timestamp) objs[idx]);
            idx++;
            list.add(c);
        }
        return new PageTran<Customer>(list);
    }
    
    /**
     * 
     * 我的家庭医生
     * @param filter
     * @param sysTime
     * @param pageSize
     * @return
     */
	public PageTran<Doctor> queryDocTran(Map<String, Object> filter, Timestamp sysTime, int pageSize) {
		 {
		        HibernateParams params = new HibernateParams();
		        StringBuilder sql = new StringBuilder();
		        sql.append("select t.user_id, t.state,t.sex, t.birthday, t.price,t.yprice,t.profession_title,t.dept_name,t.describe_,t.skill_info,t.type");
		        sql.append(", tu.name_, tu.id_card, tu.phone, tmi.start_day, tmi.end_day");
		        sql.append(", tor.orgName");
		        sql.append(", t.create_time");
		        sql.append(" from sys_doctor t");
		        sql.append(" inner join sys_user tu on tu.id = t.user_id");
		        sql.append(" left join svb_member_info tmi on tmi.state = ").append(Constant.CUSTOMER_MEMBER_STATUS_YES)
		        .append(" and t.user_id = tmi.doctor_id");
		        sql.append(" inner join sys_org tor on tor.id = tu.org_id");
		        sql.append(" where 1=1");
		       /* // 查询条件
		        String userId = (String) filter.get("userId");
		        if (StringUtil.isNotEmpty(memberStatus))
		        {
		            if (memberStatus.equals("Y"))
		            {
		                sql.append(" and (t.member_id <> '' and t.member_id is not null)");
		            }
		            if (memberStatus.equals("N"))
		            {
		                sql.append(" and (t.member_id = '' or t.member_id is null)");
		            }
		        }*/
		        // 权限
		        String userId = (String) filter.get("userId");
		        if(StringUtil.isNotEmpty(userId)){
		        	sql.append(" and tmi.cust_id = ?");
		        }
	            params.add(userId, Hibernate.STRING);
		       /* if (Constant.USER_TYPE_ADMIN.equals(operUser.getType()))
		        {
		            // 正常情况下医生所属组织不会为空
		            sql.append(" and tu.org_id = ?");
		            params.add(operUser.getOrgId(), Hibernate.STRING);
		        }
		        else if (!Constant.USER_TYPE_ADMIN.equals(operUser.getType()))
		        {
		            return new PageTran<Doctor>();
		        }*/
		        // 同步方式
		        if (sysTime == null)
		        {
		            sql.append(" order by tmi.start_day asc");
		        }
		        else
		        {
		            sql.append(" and tmi.start_day < ? order by tmi.start_day asc");
		            params.add(sysTime, Hibernate.TIMESTAMP);
		        }
		        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
		                .addScalar("user_id", Hibernate.STRING).addScalar("state", Hibernate.INTEGER)
		                .addScalar("sex", Hibernate.INTEGER)
		                .addScalar("birthday", Hibernate.DATE).addScalar("price", Hibernate.INTEGER)
		                .addScalar("yprice", Hibernate.INTEGER)
		                .addScalar("name_", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
		                .addScalar("phone", Hibernate.STRING).addScalar("start_day", Hibernate.DATE)
		                .addScalar("end_day", Hibernate.DATE).addScalar("orgName", Hibernate.STRING)
		                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("profession_title", Hibernate.STRING)
		                .addScalar("dept_name", Hibernate.STRING).addScalar("type", Hibernate.INTEGER)
		                .addScalar("skill_info", Hibernate.STRING).addScalar("describe_", Hibernate.STRING)
		                .setParameters(params.getVals(), params.getTypes())
		                .list();
		        List<Doctor> list = new ArrayList<Doctor>();
		        int idx = 0;
		        for (Object[] objs : objsList)
		        {
		        	Doctor c = new Doctor();
		            idx = 0;
		            c.setUserId((String) objs[idx]);
		            idx++;
		            c.setState((Integer) objs[idx]);
		            idx++;
		            c.setSex((Integer) objs[idx]);
		            idx++;
		            c.setBirthday((Date) objs[idx]);
		            idx++;
		            c.setPrice(objs[idx] != null ? Integer.parseInt(objs[idx].toString()) : null);
					idx++;
					c.setYprice(objs[idx] != null ? Integer.parseInt(objs[idx].toString()) : null);
					idx++;
		            c.setDoctorName((String) objs[idx]);
		            idx++;
		            c.setIdCard((String) objs[idx]);
		            idx++;
		            c.setPhone((String) objs[idx]);
		            idx++;
		            c.setStartDay((Date) objs[idx]);
		            idx++;
		            c.setEndDay((Date) objs[idx]);
		            idx++;
		            c.setOrgName((String) objs[idx]);
		            idx++;
		            c.setCreateTime((Timestamp) objs[idx]);
		            idx++;
		            c.setProfessionTitle((String) objs[idx]);
		            idx++;
		            c.setDeptName((String) objs[idx]);
		            idx++;
		            c.setType(objs[idx] != null ? Integer.parseInt(objs[idx].toString()) : null);
		            idx++;
		            c.setSkillInfo((String) objs[idx]);
		            idx++;
		            c.setDescribe((String) objs[idx]);
		            idx++;
		            list.add(c);
		        }
		        return new PageTran<Doctor>(list);
		    }
	}
	

}
