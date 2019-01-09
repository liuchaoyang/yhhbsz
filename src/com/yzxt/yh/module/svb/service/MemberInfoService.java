package com.yzxt.yh.module.svb.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.svb.bean.MemberInfo;
import com.yzxt.yh.module.svb.dao.MemberInfoDao;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.Doctor;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.dao.UserDao;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class MemberInfoService
{
    private MemberInfoDao memberInfoDao;
    private UserDao userDao;
    private CustomerService customerService;

    public MemberInfoDao getMemberInfoDao()
    {
        return memberInfoDao;
    }

    public void setMemberInfoDao(MemberInfoDao memberInfoDao)
    {
        this.memberInfoDao = memberInfoDao;
    }

    public UserDao getUserDao()
    {
        return userDao;
    }

    public void setUserDao(UserDao userDao)
    {
        this.userDao = userDao;
    }

    public CustomerService getCustomerService()
    {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    /**
     * 加载签约信息
     * @param id
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public MemberInfo load(String id) throws Exception
    {
        MemberInfo memberInfo = memberInfoDao.get(id);
        if (memberInfo != null)
        {
            if (StringUtil.isNotEmpty(memberInfo.getDoctorId()))
            {
                User doctor = userDao.get(memberInfo.getDoctorId());
                memberInfo.setDoctorName(doctor != null ? doctor.getName() : "");
            }
        }
        return memberInfo;
    }

    /**
     * 查询客户签约信息
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws ParseException
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Customer> queryCust(Map<String, Object> filter, int page, int pageSize) throws ParseException
    {
        return memberInfoDao.queryCust(filter, page, pageSize);
    }

    /**
     * 分页查询客户数据
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws ParseException 
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Customer> query(Map<String, Object> filter, int page, int pageSize) throws ParseException
    {
        return memberInfoDao.query(filter, page, pageSize);
    }

    /**
     * 根据客户ID获取签约信息
     * @param id
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public MemberInfo getMemberInfo(String id) throws Exception
    {
        return memberInfoDao.getMemberInfo(id);
    }

    /**
     * 用户签约
     * @param cust
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result add(MemberInfo memberInfo) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        // 签约客户
        String custId = memberInfo.getCustId();
        String doctorId = memberInfo.getDoctorId();
        // 判断该客户是否已经签约
        if (memberInfoDao.getIsMember(custId,doctorId))
        {
            return new Result(Result.STATE_FAIL, "绑定错误，该客户已经绑定该医生。", null);
        }
        // 如果未指定签约开始日期，默认为当天
        if (memberInfo.getStartDay() == null)
        {
            memberInfo.setStartDay(DateUtil.truncDay(now));
        }
        //健康管理师
        User doctor = userDao.load(memberInfo.getDoctorId());
        memberInfo.setOrgId(doctor.getOrgId());
        memberInfo.setState(Constant.CUSTOMER_MEMBER_STATUS_YES);
        //memberInfo.setType(2);
        String memberId = memberInfoDao.insert(memberInfo);
        Customer cust = new Customer();
        cust.setUserId(custId);
        cust.setMemberId(memberId);
        User user = new User();
        user.setOrgId(doctor.getOrgId());
        cust.setUser(user);
        // 根据签约信息修改客户信息
        customerService.updateByMember(cust);
        return new Result(Result.STATE_SUCESS, "签约成功。", memberInfo.getId());
    }

    /**
     * 修改签约，修改签约有效期
     * @param cust
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result update(MemberInfo memberInfo) throws Exception
    {
        String id = memberInfo.getId();
        MemberInfo oldMemberInfo = memberInfoDao.get(id);
        if (oldMemberInfo == null || !Constant.CUSTOMER_MEMBER_STATUS_YES.equals(oldMemberInfo.getState()))
        {
            return new Result(Result.STATE_FAIL, "修改签约信息错误，该签约信息不存在或已失效。", null);
        }
        //健康管理师
        User doctor = userDao.load(memberInfo.getDoctorId());
        memberInfo.setOrgId(doctor.getOrgId());
        memberInfoDao.update(memberInfo);
        Customer cust = new Customer();
        cust.setUserId(oldMemberInfo.getCustId());
        cust.setMemberId(id);
        User user = new User();
        user.setOrgId(doctor.getOrgId());
        cust.setUser(user);
        // 根据签约信息修改客户信息
        customerService.updateByMember(cust);
        return new Result(Result.STATE_SUCESS, "修改签约成功。", memberInfo.getId());
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<User> queryDocSel(Map<String, Object> filter, int page, int pageSize) throws ParseException
    {
        return memberInfoDao.queryDocSel(filter, page, pageSize);
    }

    /**
     * 通过客户得到MemberInfo
     * @param custID
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public MemberInfo getMemberInfoByCustId(String custID)
    {
        return memberInfoDao.getMemberInfoByCustId(custID);
    }

    /**
     * 通过custID得到MemberInfo
     * @param custID
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public MemberInfo getMemberInfoByMemberId(String id)
    {
        return memberInfoDao.get(id);
    }

    /**
     * 签约过期处理
     * 查询签约表，判断签约是否过期，如果过期，则将签约信息改为失效
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateStatusByOutDate(Date now)
    {
        return memberInfoDao.updateStatusByOutDate(now);
    }

    /**
     * 客户端查询客户签约信息
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<Customer> queryCustTran(Map<String, Object> filter, Timestamp sysTime, int pageSize)
    {
        return memberInfoDao.queryCustTran(filter, sysTime, pageSize);
    }
    /**
     * 客户端查询签约医生信息
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<Doctor> queryDocTran(Map<String, Object> filter, Timestamp sysTime, int pageSize)
    {
        return memberInfoDao.queryDocTran(filter, sysTime, pageSize);
    }
    
    /**
     * 删除客户与医生的关联关系
     * @param info
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
	public void delete(MemberInfo info) throws Exception {
    	memberInfoDao.delete(info);
	}
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public MemberInfo getMember(String uid, String doctorId) {
		// TODO Auto-generated method stub
		return memberInfoDao.getMember(uid, doctorId);
	}
    
}
