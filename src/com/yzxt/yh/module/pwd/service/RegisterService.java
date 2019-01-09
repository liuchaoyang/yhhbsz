package com.yzxt.yh.module.pwd.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.server.Config;
import com.yzxt.fw.util.EmailUtil;
import com.yzxt.yh.constant.ConfigKey;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.PhysiologIndex;
import com.yzxt.yh.module.chk.dao.PhysiologIndexDao;
import com.yzxt.yh.module.pwd.bean.FindPwd;
import com.yzxt.yh.module.pwd.dao.FindPwdDao;
import com.yzxt.yh.module.pwd.dao.RegisterDao;
import com.yzxt.yh.module.svb.service.MemberInfoService;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.Org;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.bean.UserPassword;
import com.yzxt.yh.module.sys.dao.CustomerDao;
import com.yzxt.yh.module.sys.dao.UserDao;
import com.yzxt.yh.module.sys.service.OrgService;
import com.yzxt.yh.module.sys.vo.RegisterVO;
import com.yzxt.yh.util.Base64Util;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class RegisterService
{

    private UserDao userDao;

    private RegisterDao registerDao;

    private CustomerDao customerDao;

    private FindPwdDao findPwdDao;

    private PhysiologIndexDao physiologIndexDao;

    private OrgService orgService;

    private MemberInfoService memberInfoService;

    public FindPwdDao getFindPwdDao()
    {
        return findPwdDao;
    }

    public void setFindPwdDao(FindPwdDao findPwdDao)
    {
        this.findPwdDao = findPwdDao;
    }

    public PhysiologIndexDao getPhysiologIndexDao()
    {
        return physiologIndexDao;
    }

    public void setPhysiologIndexDao(PhysiologIndexDao physiologIndexDao)
    {
        this.physiologIndexDao = physiologIndexDao;
    }

    public OrgService getOrgService()
    {
        return orgService;
    }

    public void setOrgService(OrgService orgService)
    {
        this.orgService = orgService;
    }

    public RegisterDao getRegisterDao()
    {
        return registerDao;
    }

    public void setRegisterDao(RegisterDao registerDao)
    {
        this.registerDao = registerDao;
    }

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

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUser(User user) throws Exception
    {
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public User getUser(String id) throws Exception
    {
        return null;
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
     * 通过帐号查找用户
     * @param account
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public User getUserByAcount(String account) throws Exception
    {
        return registerDao.getUserByAcount(account);
    }

    /**
     * 通过邮箱链接回调进行密码重置
     * @param userId
     * @param newPassword
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean updatePassword(String userId, String newPassword) throws Exception
    {
        return userDao.updatePassword(userId, newPassword) > 0;
    }

    /**
     * 判断帐号是否存在
     * @param roleName
     * @param exceptUserId
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public boolean getAccountExist(String account, String exceptUserId) throws Exception
    {
        return registerDao.getAccountExist(account, exceptUserId);
    }

    /**
     * 用户注册
     * @param registerVO
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result addCustomer(RegisterVO registerVO) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        User user = new User();
        String account = registerVO.getAccount();
        if (registerDao.getAccountExist(account, null))
        {
            return new Result(Result.STATE_FAIL, "客户帐号重复。", null);
        }
        String email = registerVO.getEmail();
        if (StringUtil.isNotEmpty(email) && registerDao.getEmailExist(email, null))
        {
            return new Result(Result.STATE_FAIL, "Email重复。", null);
        }
        String phone = registerVO.getPhone();
        if (StringUtil.isNotEmpty(phone) && registerDao.getPhoneExist(phone, null))
        {
            return new Result(Result.STATE_FAIL, "手机号重复。", null);
        }
        String idCard = registerVO.getIdCard();
        if (StringUtil.isNotEmpty(idCard) && registerDao.getIdCardExist(idCard, null))
        {
            return new Result(Result.STATE_FAIL, "身份证号码重复。", null);
        }
        user.setName(registerVO.getName());
        user.setAccount(account);
        user.setEmail(email);
        user.setPhone(phone);
        user.setIdCard(idCard);
        user.setState(Constant.USER_STATE_EFFECTIVE);
        user.setType(Constant.USER_TYPE_CUSTOMER);
        user.setCreateBy(registerVO.getName());
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setOrgId(registerVO.getOrgId());
        String userId = userDao.insert(user);
        // 保存客户密码
        userDao.addPassword(userId, registerVO.getPassWord());
        //保存用户角色信息
        String id = userId;
        registerDao.addRole(id, userId);
        // 保存客户信息
        Customer cust = new Customer();
        cust.setAddress(registerVO.getAddress());
        cust.setUserId(userId);
        cust.setCreateTime(now);
        cust.setUpdateBy(registerVO.getName());
        cust.setUpdateTime(now);
        cust.setSex(registerVO.getSex());
        cust.setBirthday(registerVO.getBirthDay());
        customerDao.insert(cust);

        // 增加客户身体指标记录
        PhysiologIndex physiologIndex = new PhysiologIndex();
        physiologIndex.setCustId(userId);
        physiologIndex.setLastCheckTime(now);
        // 将提醒间隔天数设置为小于0，表示不提醒
        physiologIndex.setRemindIntervalDay(-1);
        physiologIndexDao.insert(physiologIndex);
        return new Result(Result.STATE_SUCESS, "注册成功，请重新登录。", userId);
    }

    /**
     * 将重置密码链接发送到用户邮箱
     * @param name
     * @param email
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result updatePwdByMail(String name, String email) throws Exception
    {
        User user = registerDao.getUserByNameAndEmail(name, email);
        String secreKsy = UUID.randomUUID().toString();//密钥
        Timestamp outDate = new Timestamp(System.currentTimeMillis() + 60 * 60 * 1000);//1小时后过期
        Date date123 = outDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stoday = sdf.format(date123);
        Date dtoday = sdf.parse(stoday);
        // long date=outDate.getTime()/1000*1000;//忽略毫秒数 mysql取出时间是忽略毫秒数的
        FindPwd findPwd = new FindPwd();
        findPwd.setUserId(user.getId());
        findPwd.setValidateCode(secreKsy);
        findPwd.setOutDate(outDate);
        findPwdDao.save(findPwd);
        String encode = Base64Util.encode(secreKsy);
        String path = Config.getInstance().getString(ConfigKey.SERVER_URL);
        path = path + "pwd/register_updatePassWord.action";
        Org org = orgService.getShowOrg(user.getOrgId());
        path = path + "?orgId=" + org.getShowId();
        String urlAllString = path + "&name=" + name + "&key=" + encode;
        String emailContent = "亲爱的用户：您好！<br/><br/>" + "您收到这封这封电子邮件是因为您 (也可能是某人冒充您的名义) 申请了一个新的密码。"
                + "假如这不是您本人所申请, 请不用理会这封电子邮件, 但是如果您持续收到这类的信件骚扰, 请您尽快联络管理员。" + "要使用新的密码, 请使用以下链接启用密码。"
                + "<br/><br/><a href=" + urlAllString + " target='_BLANK'>" + urlAllString + "</a>" + "<br/><br/>"
                + "(如果无法点击该URL链接地址，请将它复制并粘帖到浏览器的地址输入框，然后单击回车即可。该链接使用后将立即失效。)<br/>" + "注意:请您在收到邮件1个小时内(" + dtoday
                + ")使用，否则该链接将会失效。" + "<br/><br/><br/><br/>" + "我们将一如既往、热忱的为您服务！";
        // 发送邮件
        EmailUtil emailUtil = EmailUtil.getInstance();
        boolean sucess = emailUtil.send(email, "密码重置", emailContent);
        if (sucess)
        {
            return new Result(Result.STATE_SUCESS, "请登录邮箱进行密码重置。", 1);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "发送邮件出错。", 1);
        }
    }

    /**
     * 根据用户名和邮箱得到用户信息
     * @param name
     * @param email
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public boolean getUserDetail(String name, String email)
    {
        return registerDao.getUserDetail(name, email);

    }

    /**
     * 根据邮箱地址判断链接是否合法
     * @param name
     * @param key
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result updatePwd(String name, String key) throws Exception
    {
        String decode = Base64Util.decode(key);
        name = new String(name.getBytes("UTF-8"), "UTF-8");
        FindPwd findPwd = registerDao.getCheck(name, decode);

        if (findPwd == null)
        {
            return new Result(Result.STATE_FAIL, "重置出错。", 1);
        }
        else
        {
            Timestamp outDate = findPwd.getOutDate();
            // Timestamp outDate = user.getOutDate();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            if (now.before(outDate))
            {
                return new Result(Result.STATE_SUCESS, "保存成功。", findPwd.getId());
            }
            else
            {
                return new Result(Result.STATE_FAIL, "时间已经失效请重新申请。", findPwd.getId());
            }
        }

    }

    /**
     * 根据key用户名查找user
     * @param name
     * @param key
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public User getUserDetailByKey(String name, String key) throws Exception
    {
        String decode = Base64Util.decode(key);
        FindPwd check = registerDao.getCheck(name, decode);
        String userId = check.getUserId();
        User load = userDao.load(userId);
        return load;
    }

    /**
     * 重置密码
     * @param userPassword
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updatePwdNew(UserPassword userPassword) throws Exception
    {
        int update = userDao.updatePassword(userPassword.getUserId(), userPassword.getPassword());
        return update;
    }

}
