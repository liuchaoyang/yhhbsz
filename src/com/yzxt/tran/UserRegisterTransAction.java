package com.yzxt.tran;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.server.SessionListener;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.constant.ConstFilePath;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.svb.bean.MemberInfo;
import com.yzxt.yh.module.svb.service.MemberInfoService;
import com.yzxt.yh.module.sys.bean.Codes;
import com.yzxt.yh.module.sys.bean.CustFamily;
import com.yzxt.yh.module.sys.bean.CustFamilyAudit;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.FileDesc;
import com.yzxt.yh.module.sys.bean.Org;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.bean.UserSession;
import com.yzxt.yh.module.sys.service.CodeService;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.module.sys.service.FamilyService;
import com.yzxt.yh.module.sys.service.OrgService;
import com.yzxt.yh.module.sys.service.UserService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.FileUtil;
import com.yzxt.yh.util.StringUtil;

/**
 *用户注册接口
 *2018.08.19
 */
public class UserRegisterTransAction extends BaseTranAction
{
    private static final long serialVersionUID = 1L;
    
    
    private Logger logger = Logger.getLogger(UserRegisterTransAction.class);

    private Customer customer;

    private CustomerService customerService;
    
    private FamilyService familyService;
    
    private MemberInfoService memberInfoService;
    private CodeService codeService;

    public CodeService getCodeService() {
		return codeService;
	}

	public void setCodeService(CodeService codeService) {
		this.codeService = codeService;
	}

	public MemberInfoService getMemberInfoService() {
		return memberInfoService;
	}

	public void setMemberInfoService(MemberInfoService memberInfoService) {
		this.memberInfoService = memberInfoService;
	}

	public FamilyService getFamilyService() {
		return familyService;
	}

	public void setFamilyService(FamilyService familyService) {
		this.familyService = familyService;
	}
	private UserService userService = null;

    private OrgService orgService;

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public CustomerService getCustomerService()
    {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    public UserService getUserService()
    {
        return userService;
    }

    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    public OrgService getOrgService()
    {
        return orgService;
    }

    public void setOrgService(OrgService orgService)
    {
        this.orgService = orgService;
    }

    /**
     * 客户端注册接口
     * 2018.08.17
     * */
    public void register()
    {
        Result r = null;
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                return;
            }
            String organization = GsonUtil.toString(paramJson.get("organization"));
            //解析参数，注册用户  userAccount 帐号，userName 昵称
            // String clientUserId = GsonUtil.toString(paramJson.get("userId"));
            JsonArray retJson = new JsonArray();
            User operUser = (User) super.getOperUser();
            // if(operUser == null)
            // {
            //     operUser = userService.getUser(clientUserId);
            // }
            String userAccount = GsonUtil.toString(paramJson.get("userAccount"));
            String userName = GsonUtil.toString(paramJson.get("userName"));
            String code = GsonUtil.toString(paramJson.get("code"));
            String idCard = GsonUtil.toString(paramJson.get("cardNo"));
            String password = GsonUtil.toString(paramJson.get("password"));
            String email = GsonUtil.toString(paramJson.get("email"));
            String telephone = GsonUtil.toString(paramJson.get("phone"));
            String address = GsonUtil.toString(paramJson.get("address"));
            String memo = GsonUtil.toString(paramJson.get("memo"));
            String headIcon = GsonUtil.toString(paramJson.get("headIcon"));
            String imgPath = "";
            /*String birthdayStr = GsonUtil.toString(paramJson.get("birthday"))+" 00:00:00 000";
            Timestamp birthday = DateUtil.getSynTimeByStr(birthdayStr);*/
            Date birthday = DateUtil.getFromTranDate(GsonUtil.toString(paramJson.get("birthday")));
            Integer sex = GsonUtil.toInteger(paramJson.get("sex"));
			Codes codeByPhone = codeService.getCodeByPhone(telephone);
			if(!code.equals(codeByPhone.getCode())){
				super.write(ResultTran.STATE_ERROR, "验信验证码错误", null);
                return ;
			}
			Timestamp now = new Timestamp(System.currentTimeMillis());
			long nd = 1000 * 24 * 60 * 60;
			long nh = 1000 * 60 * 60;
			long nm = 1000 * 60;
			long diff = now.getTime()-codeByPhone.getCreateTime() .getTime();
			long min = diff/ nm;
			if(min>5){
				super.write(ResultTran.STATE_ERROR, "验信验证码已过期请重新获取", null);
                return ;
			}
            Customer cust = new Customer();
            User addUser = new User();
            addUser.setEmail(email);
            addUser.setPhone(telephone);
            addUser.setAddress(address);
            FileDesc imgFileDesc = null;
            if (StringUtil.isNotEmpty(headIcon))
            {
                String[] fileInfo = SysTransAction.decodeFileInfo(headIcon);
                if (fileInfo != null)
                {
                    String temFileFullPath = FileUtil.getFullPath(fileInfo[0]);
                    File temFile = new File(temFileFullPath);
                    if (temFile.exists())
                    {
                        imgFileDesc = FileUtil.save(temFile, fileInfo[1], ConstFilePath.USER_IMG_FOLDER, true);
                        imgPath = "pub/cf_img.do?pt=" + FileUtil.encodePath(imgFileDesc.getPath());
                    }
                }
            }
            addUser.setImgFilePath(imgPath);
            String showOrgId = organization;
            boolean orgFinded = false;
            // 设置客户组织ID，先从参数中取，再从操作医生中取，最后取默认组织
            String ticket = GsonUtil.toString(paramJson.get("ticket"));
            String operUserId = GsonUtil.toString(paramJson.get("userId"));
            // 先从参数中找组织信息
            if (StringUtil.isNotEmpty(showOrgId))
            {
                Org cOrg = orgService.getShowOrg(showOrgId);
                if (cOrg != null)
                {
                    addUser.setOrgId(cOrg.getId());
                    orgFinded = true;
                }
            }
            // 如果是医生添加用户
            if (!orgFinded)
            {
                if (operUser == null && StringUtil.isNotEmpty(ticket) && StringUtil.isNotEmpty(operUserId))
                {
                    UserSession userSession = userService.getValidSession(ticket, operUserId);
                    if (userSession != null)
                    {
                        operUser = userService.getUser(operUserId);
                    }
                }
                if (operUser != null && Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
                {
                    addUser.setOrgId(operUser.getOrgId());
                    addUser.setCreateBy(operUser.getId());
                    addUser.setUpdateBy(operUser.getId());
                    orgFinded = true;
                }
            }
          /*  if (!orgFinded)
            {
                Org org = orgService.getShowOrg(null);
                addUser.setOrgId(org.getId());
            }*/
            addUser.setIdCard(idCard);
            if (StringUtil.isNotEmpty(userName))
            {
                addUser.setName(userName);
            }
            else
            {
                addUser.setName(userAccount);
            }
            cust.setUser(addUser);
            cust.setPassword(password);
            cust.setSex(sex);
            cust.setMemo(memo);
            cust.setBirthday(birthday);
           
            r = customerService.add(cust);
            int state = r.getState();
            if (Result.STATE_SUCESS == state)
            {
                HttpSession session = request.getSession();
                String sessionId = session.getId();
                String userId = (String) r.getData();
                // 补充会话信息
                Map<String, Object> data = new HashMap<String, Object>();
                //data.put("userAccount", addUser.getAccount());
                //data.put("userName", addUser.getName());
                data.put("userId", userId);
                data.put("ticket", sessionId);
                data.put("userType", addUser.getType());
                data.put("loginCode", 1);
                if (Constant.USER_TYPE_ADMIN.equals(addUser.getType()))
                {
                    data.put("cardNo", addUser.getIdCard());
                    data.put("phone", addUser.getPhone());
                    data.put("birthday", cust.getBirthday());
                    data.put("email", addUser.getEmail());
                    data.put("address",addUser.getAddress());
                    data.put("sex", cust.getSex());
                    data.put("memo", cust.getMemo());
                    // 生理指标
                    data.put("height", "");
                    data.put("weight", "");
                }
                // 用户头像
                //data.put("headIcon", "");
                // 补充会话信息
                SessionListener.put(session.getId(), session, addUser);
                // 将回话信息持久化到数据库
                UserSession userSession = new UserSession();
                userSession.setTicket(sessionId);
                userSession.setUserId(userId);
                userService.saveSession(userSession);
                MemberInfo member = new MemberInfo();
                User user = userService.getUserByAcount("doctor1");
                //member.setDoctorName(user.getName());
                member.setDoctorId(user.getId());
                member.setCustId(userId);
                //member.setState(Constant.CUSTOMER_MEMBER_STATUS_YES);
                memberInfoService.add(member);
              //完善申请表信息
                CustFamilyAudit custf = new CustFamilyAudit();
                custf.setCustId(userId);
                custf.setMemberId(userId);
                custf.setMemo(memo);
                familyService.addApply(custf);
                CustFamily custFamily = new CustFamily();
                custFamily.setCustId(userId);
                custFamily.setMemberId(userId);
                custFamily.setMemo(memo);
                familyService.addFamily(custFamily);
                // 返回登录结果
                //String dataStr = gson.toJson(data);
                super.write(ResultTran.STATE_OK, "注册成功。", data);
            }
            else
            {
                if ("客户帐号重复。".equals(r.getMsg()))
                {
                    super.write(ResultTran.STATEUSEREXIST, ResultTran.STATEUSEREXISTINFO, null);
                }
                else if ("身份证号码重复。".equals(r.getMsg()))
                {
                    super.write(ResultTran.STATEIDCARDEXIST, ResultTran.STATEIDCARDEXISTINFO, null);
                }
                else if ("手机号重复。".equals(r.getMsg()))
                {
                    super.write(ResultTran.STATEPHONEEXIST, ResultTran.STATEPHONEEXISTINFO, null);
                }
                else
                {
                    super.write(ResultTran.STATE_ERROR, "出错了", null);
                }
            }
        }
        catch (Exception e)
        {
            logger.error("客户端保存问题失败.", e);
            JsonArray json = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端异常", null);
        }
    }
    
   
    /**
     * 完善用户信息
     */
    
    public void perfect(){
    	try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                return;
            }
            // 解析参数，目前客户端支持添加，支持修改
            //解析参数，注册用户  userAccount 帐号，userName 昵称
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String cardNo = GsonUtil.toString(paramJson.get("cardNo"));
            String headIcon = GsonUtil.toString(paramJson.get("headIcon"));
            String address = GsonUtil.toString(paramJson.get("address"));
            String memo = GsonUtil.toString(paramJson.get("memo"));
            String imgPath = "";
            //通过userId来判断用户名是否存在以及身份证号码是否存在 11用户名已存在，12身份证号码已经存在
            //注释：此处暂时不判断idCard,只判断用户帐号，如果通过userId排除掉原来的存在的帐号
            //此处帐号没有改变
            String userName = GsonUtil.toString(paramJson.get("userName"));
            String sexStr = GsonUtil.toString(paramJson.get("sex"));
            Integer sex = null;
            if (!sexStr.equals("") && sexStr != null)
            {
                sex = GsonUtil.toInteger(paramJson.get("sex"));
            }
            Customer customer = new Customer();
            //完善用户基本信息
            User updateUser = new User();
            updateUser.setId(userId);
            updateUser.setIdCard(cardNo);
            updateUser.setName(userName);
            updateUser.setAddress(address);
            Timestamp now = new Timestamp(System.currentTimeMillis());
            updateUser.setUpdateTime(now);
            updateUser.setSex(sex);
            updateUser.setJjLinkman(1);
            //完善客户表基本信息
            customer.setUser(updateUser);
            customer.setUserId(userId);
            customer.setSex(sex);
            customer.setMemo(memo);
            CustFamilyAudit family = familyService.getFamilyUser(userId);
            if(family!=null){
            	family.setMemo(memo);
                familyService.updateMemInfo(family);
            }
            FileDesc imgFileDesc = null;
            if (StringUtil.isNotEmpty(headIcon))
            {
                String[] fileInfo = SysTransAction.decodeFileInfo(headIcon);
                if (fileInfo != null)
                {
                    String temFileFullPath = FileUtil.getFullPath(fileInfo[0]);
                    File temFile = new File(temFileFullPath);
                    if (temFile.exists())
                    {
                        imgFileDesc = FileUtil.save(temFile, fileInfo[1], ConstFilePath.USER_IMG_FOLDER, true);
                        imgPath = "pub/cf_img.do?pt=" + FileUtil.encodePath(imgFileDesc.getPath());
                    }
                }
            }
            Result r = customerService.updateTrans(customer, imgFileDesc);
            JsonObject retJson = new JsonObject();
            //retJson.addProperty("headIcon", imgPath);
            super.write(String.valueOf(r.getState()), r.getMsg());
            /*super.write(ResultTran.STATEUSERUPDATE, ResultTran.STATEUSERUPDATEINFO, retJson);*/
        }
        catch (Exception e)
        {
            logger.error("客户端保存问题失败.", e);
            super.write(ResultTran.STATE_ERROR, "失败", null);
        }
    }
    /**
     *用户基本信息修改接口
     *2018.07.27
     */
    public void doUpdate()
    {
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                return;
            }
            // 解析参数，目前客户端支持添加，支持修改
            //解析参数，注册用户  userAccount 帐号，userName 昵称
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String userAccount = GsonUtil.toString(paramJson.get("userAccount"));
            String idCard = GsonUtil.toString(paramJson.get("cardNo"));
            Integer type = GsonUtil.toInteger(paramJson.get("type"));
            String memo = GsonUtil.toString(paramJson.get("memo"));
            String headIcon = GsonUtil.toString(paramJson.get("headIcon"));
            String imgPath = "";
            //通过userId来判断用户名是否存在以及身份证号码是否存在 11用户名已存在，12身份证号码已经存在
            //注释：此处暂时不判断idCard,只判断用户帐号，如果通过userId排除掉原来的存在的帐号
            User user = userService.getUserByAcount(userAccount);
            if (user == null || (userAccount.equals(user.getAccount())))
            {
                //此处帐号没有改变
                String userName = GsonUtil.toString(paramJson.get("userName"));
                String address = GsonUtil.toString(paramJson.get("address"));
                String height = GsonUtil.toString(paramJson.get("height"));
                String weight = GsonUtil.toString(paramJson.get("weight"));
                String email = GsonUtil.toString(paramJson.get("email"));
                String telephone = GsonUtil.toString(paramJson.get("phone"));
               /* String birthdayStr = GsonUtil.toString(paramJson.get("birthday")) + " 00:00:00 000";
                Timestamp birthday = DateUtil.getSynTimeByStr(birthdayStr);*/
                String sexStr = GsonUtil.toString(paramJson.get("sex"));
                Integer sex = null;
                if (!sexStr.equals("") && sexStr != null)
                {
                    sex = GsonUtil.toInteger(paramJson.get("sex"));
                }
                Customer customer = new Customer();
                //修改用户表基本信息
                User updateUser = new User();
                updateUser.setAccount(userAccount);
                updateUser.setId(userId);
                updateUser.setType(type);
                updateUser.setIdCard(idCard);
                updateUser.setName(userName);
                updateUser.setSex(sex);
                updateUser.setEmail(email);
                updateUser.setPhone(telephone);
                Timestamp now = new Timestamp(System.currentTimeMillis());
                updateUser.setUpdateTime(now);
                updateUser.setAddress(address);
                //修改客户表基本信息
                customer.setUser(updateUser);
                customer.setUserId(userId);
                //customer.setSex(sex);
                customer.setMemo(memo);
                //customer.setBirthday(birthday);
                //修改客户表个人生理指标基本信息
               /* customer.setHeight(Double.parseDouble(height));
                customer.setWeight(Double.parseDouble(weight));
*/				
                CustFamilyAudit family = familyService.getFamilyUser(userId);
                if(family!=null){
                	family.setMemo(memo);
                    familyService.updateMemInfo(family);
                }
                FileDesc imgFileDesc = null;
                if (StringUtil.isNotEmpty(headIcon))
                {
                    String[] fileInfo = SysTransAction.decodeFileInfo(headIcon);
                    if (fileInfo != null)
                    {
                        String temFileFullPath = FileUtil.getFullPath(fileInfo[0]);
                        File temFile = new File(temFileFullPath);
                        if (temFile.exists())
                        {
                            imgFileDesc = FileUtil.save(temFile, fileInfo[1], ConstFilePath.USER_IMG_FOLDER, true);
                            imgPath = "pub/cf_img.do?pt=" + FileUtil.encodePath(imgFileDesc.getPath());
                        }
                    }
                }
                Result r = customerService.updateTran(customer, imgFileDesc);
                JsonObject retJson = new JsonObject();
                //retJson.addProperty("headIcon", imgPath);
                super.write(String.valueOf(r.getState()), r.getMsg());
                /*super.write(ResultTran.STATEUSERUPDATE, ResultTran.STATEUSERUPDATEINFO, retJson);*/
            }
            else
            {
                JsonObject retJson = new JsonObject();
                super.write(ResultTran.STATEUSEREXIST, ResultTran.STATEUSEREXISTINFO, retJson);
            }
        }
        catch (Exception e)
        {
            logger.error("客户端保存问题失败.", e);
            super.write(ResultTran.STATE_ERROR, "失败", null);
        }
    }

    /**
     * 判断指定居民是否存在
     */
    public void getRes()
    {
        try
        {
            JsonObject obj = super.getParams();
            String idCard = GsonUtil.toString(obj.get("idCard"));
            User user = userService.getUserByIdCard(idCard);
            super.write(ResultTran.STATE_OK, null, user != null ? user.getId() : "");
        }
        catch (Exception e)
        {
            super.write(ResultTran.STATE_ERROR, "判断用户身份证错误。");
        }
    }
    /**
     * 查看个人信息
     * @throws IOException 
     * @throws Exception
     */
    public void viewInfo() throws IOException
    {
        try
        {
        	JsonObject obj = super.getParams();
            initQuery(obj);
            String userId = GsonUtil.toString(obj.get("userId"));
            User user = userService.getUser(userId);
            Customer cust = customerService.get(userId);
            JsonArray catasJson = new JsonArray();
            JsonObject retJson = new JsonObject();
            retJson.addProperty("userName",user.getName());
            retJson.addProperty("sex",user.getSex());
            retJson.addProperty("address",user.getAddress());
            retJson.addProperty("memo",cust.getMemo());
            retJson.addProperty("phone",user.getPhone());
            retJson.addProperty("idCard",user.getIdCard());
            retJson.addProperty("type",user.getType());
            catasJson.add(retJson);
            super.write(ResultTran.STATE_OK, "成功", catasJson);
        }
        catch (Exception e)
        {
        	 logger.error("查看详情失败.", e);
             super.write(ResultTran.STATE_ERROR, "失败", null); 
        }
    }
    /**
     * 
     * sso求救
     * @throws Exception 
     */
    public void sso() throws Exception{
    	JsonObject paramJson = super.getParams();
        if (paramJson == null)
        {
            return;
        }
        
    	String custId = GsonUtil.toString(paramJson.get("custId"));
    	User user = userService.getUser(custId);
    	//CustFamilyAudit family = familyService.getFamilyUser(custId);
		Map<String, Object> filter2 = new HashMap<String, Object>();
		filter2.put("userId", custId);
		List<CustFamilyAudit> cfaList = familyService.getAudit(filter2);
		 StringBuilder sb=new StringBuilder();
		  /* for (int i = 0; i < cfaList.size(); i++) {
			   if(sb.length()>0){
				   sb.append(",");
				   
			   }
			sb.append(cfaList.get(i).getMemberPhone());
		}*/
  
		if(cfaList!=null){
			for (CustFamilyAudit cfa : cfaList) {
				   if(sb.length()>0){
					   sb.append(",");
					   
				   }
				   sb.append(cfa.getMemberPhone());
			}
		}
		ApiMessage message = new ApiMessage();
		message.sendsms(sb.toString(),user.getName());
		 super.write(ResultTran.STATE_OK, "发送成功");
    }
}
