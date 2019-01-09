package com.yzxt.yh.module.sys.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstRole;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.PhysiologIndex;
import com.yzxt.yh.module.chk.dao.PhysiologIndexDao;
import com.yzxt.yh.module.svb.service.MemberInfoService;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.FileDesc;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.bean.UserRole;
import com.yzxt.yh.module.sys.dao.CustFamilyAuditDao;
import com.yzxt.yh.module.sys.dao.CustFamilyDao;
import com.yzxt.yh.module.sys.dao.CustomerDao;
import com.yzxt.yh.module.sys.dao.FileDescDao;
import com.yzxt.yh.module.sys.dao.UserDao;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class CustomerService
{
    private CustomerDao customerDao;
    private UserDao userDao;
    private PhysiologIndexDao physiologIndexDao;
    private CustFamilyAuditDao custFamilyAuditDao;
    private CustFamilyDao custFamilyDao;
    private FileDescDao fileDescDao;
    private UserRoleService userRoleService;
    private MemberInfoService memberInfoService;

    public CustomerDao getCustomerDao()
    {
        return customerDao;
    }

    public void setCustomerDao(CustomerDao customerDao)
    {
        this.customerDao = customerDao;
    }

    public UserDao getUserDao()
    {
        return userDao;
    }

    public void setUserDao(UserDao userDao)
    {
        this.userDao = userDao;
    }

    public PhysiologIndexDao getPhysiologIndexDao()
    {
        return physiologIndexDao;
    }

    public void setPhysiologIndexDao(PhysiologIndexDao physiologIndexDao)
    {
        this.physiologIndexDao = physiologIndexDao;
    }

    public CustFamilyAuditDao getCustFamilyAuditDao()
    {
        return custFamilyAuditDao;
    }

    public void setCustFamilyAuditDao(CustFamilyAuditDao custFamilyAuditDao)
    {
        this.custFamilyAuditDao = custFamilyAuditDao;
    }

    public CustFamilyDao getCustFamilyDao()
    {
        return custFamilyDao;
    }

    public void setCustFamilyDao(CustFamilyDao custFamilyDao)
    {
        this.custFamilyDao = custFamilyDao;
    }

    public FileDescDao getFileDescDao()
    {
        return fileDescDao;
    }

    public void setFileDescDao(FileDescDao fileDescDao)
    {
        this.fileDescDao = fileDescDao;
    }

    public UserRoleService getUserRoleService()
    {
        return userRoleService;
    }

    public void setUserRoleService(UserRoleService userRoleService)
    {
        this.userRoleService = userRoleService;
    }

    public MemberInfoService getMemberInfoService()
    {
        return memberInfoService;
    }

    public void setMemberInfoService(MemberInfoService memberInfoService)
    {
        this.memberInfoService = memberInfoService;
    }

    /**
     * 获取客户信息
     * @param cust
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Customer get(String custId) throws Exception
    {
        Customer cust = customerDao.load(custId);
        return cust;
    }

    /**
     * 加载客户信息
     * @param cust
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Customer load(String custId) throws Exception
    {
        Customer cust = customerDao.load(custId);
        User user = userDao.load(custId);
        cust.setUser(user);
        return cust;
    }

    /**
     * 新增客户
     * @param cust
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result add(Customer cust) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        User user = cust.getUser();
        String idCard = user.getIdCard();
        if (StringUtil.isNotEmpty(idCard) && userDao.getIdCardExist(idCard, null))
        {
            return new Result(Result.STATE_FAIL, "身份证号码重复。", null);
        }
        String account = user.getAccount();
        if (StringUtil.isNotEmpty(account)&&userDao.getAccountExist(account, null))
        {
            return new Result(Result.STATE_FAIL, "客户帐号重复。", null);
        }
        String email = user.getEmail();
        if (StringUtil.isNotEmpty(email) && userDao.getEmailExist(email, null))
        {
            return new Result(Result.STATE_FAIL, "Email重复。", null);
        }
        String phone = user.getPhone();
        if (StringUtil.isNotEmpty(phone) && userDao.getPhoneExist(phone, null))
        {
            return new Result(Result.STATE_FAIL, "手机号重复。", null);
        }
        user.setState(Constant.USER_STATE_EFFECTIVE);
        user.setType(Constant.USER_TYPE_ADMIN);
        user.setCreateTime(now);
        user.setUpdateBy(cust.getCreateBy());
        user.setUpdateTime(now);
        String userId = userDao.insert(user);
        // 保存客户密码
        if (StringUtil.isEmpty(cust.getPassword()) || cust.getOperUser() != null
                && !Constant.USER_TYPE_CUSTOMER.equals(cust.getOperUser().getType()))
        {
            userDao.addPassword(userId, Constant.USER_DEFAULT_PASSWROD);
        }
        else
        {
            userDao.addPassword(userId, cust.getPassword());
        }
        // 添加客户角色
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(ConstRole.ADMIN);
        userRoleService.add(userRole);
        // 保存客户信息
        cust.setUserId(userId);
        cust.setCreateTime(now);
        cust.setUpdateBy(cust.getCreateBy());
        cust.setUpdateTime(now);
        customerDao.insert(cust);
        // 增加客户身体指标记录
        PhysiologIndex physiologIndex = new PhysiologIndex();
        physiologIndex.setCustId(userId);
        physiologIndex.setLastCheckTime(now);
        // 将提醒间隔天数设置为小于0，表示不提醒
        physiologIndex.setRemindIntervalDay(-1);
        physiologIndexDao.insert(physiologIndex);
        return new Result(Result.STATE_SUCESS, "保存成功。", userId);
    }
    
    /**
     * 新增家庭成员
     * @param cust
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result addFamliy(Customer cust) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        User user = cust.getUser();
        String idCard = user.getIdCard();
        if (StringUtil.isNotEmpty(idCard) && userDao.getIdCardExist(idCard, null))
        {
            return new Result(Result.STATE_FAIL, "身份证号码重复。", null);
        }
        user.setState(Constant.USER_STATE_EFFECTIVE);
        user.setType(Constant.USER_TYPE_CUSTOMER);
        user.setCreateTime(now);
        user.setUpdateBy(cust.getCreateBy());
        user.setUpdateTime(now);
        String userId = userDao.insert(user);
        // 保存客户密码
        if (StringUtil.isEmpty(cust.getPassword()) || cust.getOperUser() != null
                && !Constant.USER_TYPE_CUSTOMER.equals(cust.getOperUser().getType()))
        {
            userDao.addPassword(userId, Constant.USER_DEFAULT_PASSWROD);
        }
        else
        {
            userDao.addPassword(userId, cust.getPassword());
        }
        // 添加客户角色
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(ConstRole.CUSTOMER);
        userRoleService.add(userRole);
        // 保存客户信息
        cust.setUserId(userId);
        cust.setCreateTime(now);
        cust.setUpdateBy(cust.getCreateBy());
        cust.setUpdateTime(now);
        customerDao.insert(cust);
        // 增加客户身体指标记录
        PhysiologIndex physiologIndex = new PhysiologIndex();
        physiologIndex.setCustId(userId);
        physiologIndex.setLastCheckTime(now);
        // 将提醒间隔天数设置为小于0，表示不提醒
        physiologIndex.setRemindIntervalDay(-1);
        physiologIndexDao.insert(physiologIndex);
        return new Result(Result.STATE_SUCESS, "保存成功。", userId);
    }
    /**
     * 更新客户信息
     * @param cust
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result update(Customer cust) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String userId = cust.getUserId();
        User user = cust.getUser();
        String email = user.getEmail();
        if (StringUtil.isNotEmpty(email) && userDao.getEmailExist(email, userId))
        {
            return new Result(Result.STATE_FAIL, "Email重复。", null);
        }
        String phone = user.getPhone();
        if (StringUtil.isNotEmpty(phone) && userDao.getPhoneExist(phone, userId))
        {
            return new Result(Result.STATE_FAIL, "手机号重复。", null);
        }
        String idCard = user.getIdCard();
        if (StringUtil.isNotEmpty(idCard) && userDao.getIdCardExist(idCard, userId))
        {
            return new Result(Result.STATE_FAIL, "身份证号码重复。", null);
        }
        user.setId(userId);
        user.setUpdateTime(now);
        user.setUpdateBy(cust.getUpdateBy());
        userDao.updateByCustomer(user);
        cust.setUpdateTime(now);
        customerDao.update(cust);
        return new Result(Result.STATE_SUCESS, "保存成功。", userId);
    }

    /**
     * 分页查询客户数据
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Customer> query(Map<String, Object> filter, int page, int pageSize)
    {
        return customerDao.query(filter, page, pageSize);
    }

    /**
     * 客户端分页查询客户数据
     * @param filter 过滤条件
     * @param sysTime 同步时间点
     * @param dir 同步方式，大于0：取时间点后的数据，小于0取时间点前的数据
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<Customer> queryTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        return customerDao.queryTran(filter, sysTime, dir, count);
    }

    /**
     * 查询客户列表，用于生成客户分析报告
     * @param filter
     * @param startRow
     * @param maxSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Customer> getCustomers(Map<String, Object> filter, int startRow, int maxSize)
    {
        return customerDao.getCustomers(filter, startRow, maxSize);
    }

    /**
     * 保存个人账户信息
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result saveAccount(Customer cust) throws Exception
    {
        User user = cust.getUser();
        String userId = cust.getUserId();
        User oldUser = userDao.get(userId);
        // 修改了用户账号
        String account = user.getAccount();
        if (StringUtil.isNotEmpty(account))
        {
            // 如果原来的账号不是身份证号不允许修改
            if (!oldUser.getAccount().equals(oldUser.getIdCard()))
            {
                return new Result(Result.STATE_FAIL, "原账号不是身份证号码不允许修改。", null);
            }
            // 判断账号是否存在
            if (userDao.getAccountExist(account, userId))
            {
                return new Result(Result.STATE_FAIL, "账号重复。", null);
            }
        }
        // 修改了身份证号码
        String idCard = user.getIdCard();
        if (StringUtil.isNotEmpty(idCard))
        {
            // 身份证号为空时可以修改一次
            if (StringUtil.isNotEmpty(oldUser.getIdCard()))
            {
                return new Result(Result.STATE_FAIL, "已填写的身份证号不允许修改。", null);
            }
            // 判断账号是否存在
            if (userDao.getIdCardExist(idCard, userId))
            {
                return new Result(Result.STATE_FAIL, "身份证号重复。", null);
            }
        }
        String phone = user.getPhone();
        if (StringUtil.isNotEmpty(phone) && userDao.getPhoneExist(phone, userId))
        {
            return new Result(Result.STATE_FAIL, "手机号重复。", null);
        }
        String email = user.getEmail();
        if (StringUtil.isNotEmpty(email) && userDao.getEmailExist(email, userId))
        {
            return new Result(Result.STATE_FAIL, "Email重复。", null);
        }
        // 更新信息
        Timestamp now = cust.getUpdateTime();
        cust.setUpdateTime(now);
        customerDao.updateByAccount(cust);
        user.setId(userId);
        user.setUpdateBy(cust.getUpdateBy());
        cust.setUpdateTime(now);
        userDao.updateByAccount(user);
        return new Result(Result.STATE_SUCESS, "保存成功。", null);
    }

    /**
     * 分页查询会员数据
     * 
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Customer> queryMember(Map<String, Object> filter, int page, int pageSize)
    {
        return customerDao.queryMember(filter, page, pageSize);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateTrans(Customer cusotmer, FileDesc imgFileDesc) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String userId = cusotmer.getUserId();
        User user = cusotmer.getUser();
        String email = user.getEmail();
        if (StringUtil.isNotEmpty(email) && userDao.getEmailExist(email, userId))
        {
            return new Result(Result.STATE_FAIL, "Email重复。", null);
        }
      /*  String phone = user.getPhone();
        if (StringUtil.isNotEmpty(phone) && userDao.getPhoneExist(phone, userId))
        {
            return new Result(Result.STATE_FAIL, "手机号重复。", null);
        }*/
       /* String idCard = user.getIdCard();
        if (StringUtil.isNotEmpty(idCard) && userDao.getIdCardExist(idCard, userId))
        {
            return new Result(Result.STATE_FAIL, "身份证号码重复。", null);
        }*/
        String imgFileId = null;
        // 保存图像文件
        if (imgFileDesc != null)
        {
            imgFileId = fileDescDao.insert(imgFileDesc);
        }
        User oldUser = userDao.get(userId);
        BeanUtils.copyProperties(user, oldUser, new String[]
        {"id", "state","type","account","phone", "imgFileId", "createBy", "createTime"});
        if (StringUtil.isNotEmpty(imgFileId))
        {
            oldUser.setImgFileId(imgFileId);
        }
        userDao.update(oldUser);
        cusotmer.setUpdateTime(now);
        customerDao.update(cusotmer);
        // 个人生理指标需要修改
        physiologIndexDao.update(cusotmer);
        return new Result(Result.STATE_SUCESS, "更新成功。", null);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateTran(Customer cusotmer, FileDesc imgFileDesc) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String userId = cusotmer.getUserId();
        User user = cusotmer.getUser();
        String email = user.getEmail();
        if (StringUtil.isNotEmpty(email) && userDao.getEmailExist(email, userId))
        {
            return new Result(Result.STATE_FAIL, "Email重复。", null);
        }
      /*  String phone = user.getPhone();
        if (StringUtil.isNotEmpty(phone) && userDao.getPhoneExist(phone, userId))
        {
            return new Result(Result.STATE_FAIL, "手机号重复。", null);
        }*/
       /* String idCard = user.getIdCard();
        if (StringUtil.isNotEmpty(idCard) && userDao.getIdCardExist(idCard, userId))
        {
            return new Result(Result.STATE_FAIL, "身份证号码重复。", null);
        }*/
        String imgFileId = null;
        // 保存图像文件
        if (imgFileDesc != null)
        {
            imgFileId = fileDescDao.insert(imgFileDesc);
        }
        User oldUser = userDao.get(userId);
        BeanUtils.copyProperties(user, oldUser, new String[]
        {"id", "state","account", "imgFileId","createBy", "createTime"});
        if (StringUtil.isNotEmpty(imgFileId))
        {
            oldUser.setImgFileId(imgFileId);
        }
        userDao.update(oldUser);
        cusotmer.setUpdateTime(now);
        customerDao.update(cusotmer);
        // 个人生理指标需要修改
        physiologIndexDao.update(cusotmer);
        return new Result(Result.STATE_SUCESS, "更新成功。", null);
    }


    /**
     * 新增或修改档案信息时对客户信息的修改
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateByAchive(Customer cust) throws Exception
    {
        Timestamp now = cust.getUpdateTime();
        User user = cust.getUser();
        customerDao.updateByAchive(cust);
        user.setId(cust.getUserId());
        user.setUpdateBy(cust.getUpdateBy());
        user.setUpdateTime(now);
        userDao.updateByAchive(user);
    }

    /**
     * 修改身高体重
     * 身高与生理体征表单位一致（m）
     * 体重与生理体征表单位一致（kg）
     * 
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateByHW(Customer cust) throws Exception
    {
        // 个人生理指标需要修改
        physiologIndexDao.update(cust);
    }

    /**
     * 加入会员时修改客户信息
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateByMember(Customer cust) throws Exception
    {
        customerDao.updateByMember(cust);
        User user = cust.getUser();
        user.setId(cust.getUserId());
        userDao.updateByMember(user);
    }

    /**
     * 用户家庭圈查询
     * @author h
     * @return
     * @param filter
     * 2015.6.19
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Customer> queryCustTran(Map<String, Object> filter)
    {
        return customerDao.queryCustTran(filter);
    }

    /**
     * 不同机构
     * 统计用户每天注册用户数，现在暂时不分平台还是客户端还是一体机
     * @author h
     * 2015.7.24
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> queryRegisterNum(Map<String, Object> filter)
    {
        return customerDao.queryRegisterNum(filter);
    }

}
