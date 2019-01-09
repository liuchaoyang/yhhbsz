package com.yzxt.tran;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.server.SessionListener;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.svb.bean.MemberInfo;
import com.yzxt.yh.module.sys.bean.Codes;
import com.yzxt.yh.module.sys.bean.CustFamily;
import com.yzxt.yh.module.sys.bean.CustFamilyAudit;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.bean.UserPassword;
import com.yzxt.yh.module.sys.bean.UserSession;
import com.yzxt.yh.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class LoginTranAction extends WelcomeBaseTranAction
{
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(LoginTranAction.class);
    //private static String code = smsCode();
	/**
     * 客户端登录
     */
    public void login()
    {
        try
        {
            JsonObject obj = super.getParams();
            String phone = GsonUtil.toString(obj.get("phone"));
            String password = GsonUtil.toString(obj.get("password"));
            if (StringUtil.isEmpty(phone))
            {
                super.write(ResultTran.STATE_ERROR, "电话号码为空。", null);
                return;
            }
            if (StringUtil.isEmpty(password))
            {
                super.write(ResultTran.STATE_ERROR, "密码为空。", null);
            }
            User user = null;
            user = userService.getUserByPhone(phone);
            if (user == null)
            {
                super.write(ResultTran.STATE_LOGIN_USER_NOT_EXIST, "用户不存在。", null);
            }
            if (user.getState() == null || user.getState() != 1)
            {
                super.write(ResultTran.STATE_ERROR, "用户处于冻结状态。", null);
            }
            if (userService.getPasswordValid(user.getId(), password))
            {
                Map<String, Object> data = super.handleLogin(user,obj);
                super.write(ResultTran.STATE_OK, "登录成功。",data);
                request.getSession().setAttribute("user", user);
                
            }
            else
            {
                super.write(ResultTran.STATE_LOGIN_PWD_ERROR, "用户密码错误。", null);
            }
        }
        catch (Exception e)
        {
            super.write(ResultTran.STATE_ERROR, "客户端登录出错。", null);
            logger.error("客户端登录出错。", e);
        }
    }
    
    /**
     * 
     * 使用验证码登录
     */
    public void loginCode(){
        try
        {
            JsonObject obj = super.getParams();
            String phone = GsonUtil.toString(obj.get("phone"));
            String code = GsonUtil.toString(obj.get("code"));
            if (StringUtil.isEmpty(phone))
            {
                super.write(ResultTran.STATE_ERROR, "电话号码为空。", null);
                return;
            }
            if (StringUtil.isEmpty(code))
            {
                super.write(ResultTran.STATE_ERROR, "验证码为空。", null);
                return;
            }
            User user = null;
            user = userService.getUserByPhone(phone);
            if (user == null)
            {
                //super.write(ResultTran.STATE_LOGIN_USER_NOT_EXIST, "用户不存在。", null);
                this.register(phone,"123456");
            	User user2= userService.getUserByPhone(phone);
            	Map<String, Object> data = super.handleLogin(user2,obj);
            	 MemberInfo member = new MemberInfo();
                 User users = userService.getUserByAcount("doctor1");
                 //member.setDoctorName(user.getName());
                 member.setDoctorId(users.getId());
                 member.setCustId(user2.getId());
                 //member.setState(Constant.CUSTOMER_MEMBER_STATUS_YES);
                 memberInfoService.add(member);
            	super.write(ResultTran.STATE_OK, "登录成功。",data);
            	request.getSession().setAttribute("user", user2);
            	
            }else{
            	if(!code.equals(user.getCode())){
            		super.write(ResultTran.STATE_ERROR, "验信验证码错误", null);
            	}
            	Timestamp now = new Timestamp(System.currentTimeMillis());
    			long nd = 1000 * 24 * 60 * 60;
    			long nh = 1000 * 60 * 60;
    			long nm = 1000 * 60;
    			long diff = now.getTime()-user.getUpdateTime() .getTime();
    			long min = diff/ nm;
    			if(min>5){
    				super.write(ResultTran.STATE_ERROR, "验信验证码已过期请重新获取", null);
                    return ;
    			}
    			MemberInfo member = new MemberInfo();
                User users = userService.getUserByAcount("doctor1");
                //member.setDoctorName(user.getName());
                MemberInfo mem = memberInfoService.getMember(user.getId(), users.getId());
                if(mem==null){
            	   member.setDoctorId(users.getId());
                   member.setCustId(user.getId());
                   //member.setState(Constant.CUSTOMER_MEMBER_STATUS_YES);
                   memberInfoService.add(member);
                }
                //完善申请表信息
                CustFamilyAudit familyUser = familyService.getFamilyUser(user.getId());
                if(familyUser==null){
                	CustFamilyAudit custf = new CustFamilyAudit();
                    custf.setCustId(user.getId());
                    custf.setMemberId(user.getId());
                    familyService.addApply(custf);
                }
                CustFamily familyUserId = familyService.getFamilyUserId(user.getId());
                if(familyUserId==null){
                	CustFamily custFamily = new CustFamily();
                    custFamily.setCustId(user.getId());
                    custFamily.setMemberId(user.getId());
                    familyService.addFamily(custFamily);
                }
            	Map<String, Object> data = super.handleLogin(user,obj);
                super.write(ResultTran.STATE_OK, "登录成功。",data);
                request.getSession().setAttribute("user", user);
            }
    	   
        }
        catch (Exception e)
        {
            super.write(ResultTran.STATE_ERROR, "客户端登录出错。", null);
            logger.error("客户端登录出错。", e);
        }

    }
    /**
     * 忘记密码
     * 
     */
    public void retrieve() {
    	
    	try {
    		 JsonObject obj = super.getParams();
             String phone = GsonUtil.toString(obj.get("phone"));
            
             if(StringUtil.isEmpty(phone)){
            	 super.write(ResultTran.STATE_ERROR, "电话号码为空。", null);
                 return;
             }
             User user = userService.getUserByPhone(phone);
             if(user==null){
            	 super.write(ResultTran.STATE_ERROR, "手机号未注册。", null);
                 return;
             }
             ApiTest t = new ApiTest();
             //普通短信
             t.sendsms(phone);
             //super.write(ResultTran.STATE_OK,"密码找回成功",phone);
             UserPassword u = userService.getPassword(user.getId());
             /*JsonObject object = new JsonObject();
             object.addProperty("password", u.getPassword());*/
             super.write(ResultTran.STATE_OK, "密码找回成功。",u.getPassword());
		} catch (Exception e) {
			 super.write(ResultTran.STATE_ERROR, "服务器出错。", null);
	         logger.error("服务器出错。", e);
		}
    }
    @SuppressWarnings("null")
	public void codes() throws Exception{
    	JsonObject paramJson = super.getParams();
    	Result r = null;
        if (paramJson == null)
        {
            return;
        }
        
    	String phone = GsonUtil.toString(paramJson.get("phone"));
        if(StringUtil.isEmpty(phone)){
        	super.write(ResultTran.STATE_ERROR, "电话号码为空。", null);
            return;
        }
        ApiTest t = new ApiTest();
        //普通短信
        Codes co = new Codes();
        String codes = t.sendsms(phone);
        Codes code = codeService.getCodeByPhone(phone);
        if(code==null){
        	co.setCode(codes);
        	co.setPhone(phone);
        	codeService.saveCode(co);
         }else{
        	 code.setCode(codes);
        	codeService.updateCode(code);
        }
        super.write(ResultTran.STATE_OK, "获取成功",null);
    }
    /**
     * 获取验证码
     */
    public void code() throws Exception{
    	JsonObject paramJson = super.getParams();
    	Result r = null;
        if (paramJson == null)
        {
            return;
        }
        
    	String phone = GsonUtil.toString(paramJson.get("phone"));
        if(StringUtil.isEmpty(phone)){
        	super.write(ResultTran.STATE_ERROR, "电话号码为空。", null);
            return;
        }
        ApiTest t = new ApiTest();
        //普通短信
        User u = new User();
        Customer cust = new Customer();
        String codes = t.sendsms(phone);
        User user = userService.getUserByPhone(phone);
        if(user==null){
        	u.setCode(codes);
        	u.setPhone(phone);
        	cust.setUser(u);
            r = customerService.add(cust);        
         }else{
        	Timestamp now = new Timestamp(System.currentTimeMillis());
        	user.setUpdateTime(now);
        	user.setCode(codes);
        	userService.updateCode(user);
        }
        super.write(ResultTran.STATE_OK, "获取成功",null);
    }

    public void register(String telephone,String password)
    {
        Result r = null;
        try
        {
            
            // if(operUser == null)
            // {
            //     operUser = userService.getUser(clientUserId);
            // }
            /*String birthdayStr = GsonUtil.toString(paramJson.get("birthday"))+" 00:00:00 000";
            Timestamp birthday = DateUtil.getSynTimeByStr(birthdayStr);*/
            Customer cust = new Customer();
            User addUser = new User();
            addUser.setPhone(telephone);
            // 设置客户组织ID，先从参数中取，再从操作医生中取，最后取默认组织
            // 先从参数中找组织信息
            // 如果是医生添加用户
            cust.setUser(addUser);
            cust.setPassword(password);
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
                // 返回登录结果
                //String dataStr = gson.toJson(data);
                //super.write(ResultTran.STATE_OK, "注册成功。", data);
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
    
   
}
