package com.yzxt.tran;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;
import com.yzxt.fw.server.SessionListener;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.constant.ConstDevice;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.PhysiologIndex;
import com.yzxt.yh.module.chk.service.PhysiologIndexService;
import com.yzxt.yh.module.svb.service.MemberInfoService;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.FileDesc;
import com.yzxt.yh.module.sys.bean.LoginRecord;
import com.yzxt.yh.module.sys.bean.PushUser;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.bean.UserSession;
import com.yzxt.yh.module.sys.service.CodeService;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.module.sys.service.FamilyService;
import com.yzxt.yh.module.sys.service.FileDescService;
import com.yzxt.yh.module.sys.service.LoginRecordService;
import com.yzxt.yh.module.sys.service.OrgService;
import com.yzxt.yh.module.sys.service.PushUserService;
import com.yzxt.yh.module.sys.service.UserService;
import com.yzxt.yh.util.FileUtil;
import com.yzxt.yh.util.StringUtil;

public abstract class WelcomeBaseTranAction extends BaseTranAction
{
    private static final long serialVersionUID = 1L;

    protected UserService userService;
    protected CodeService codeService;
    public CodeService getCodeService() {
		return codeService;
	}

	public void setCodeService(CodeService codeService) {
		this.codeService = codeService;
	}

	protected FileDescService fileDescService;
    protected PhysiologIndexService physiologIndexService;
    protected CustomerService customerService;
    protected OrgService orgService;
    protected LoginRecordService loginRecordService;
    protected PushUserService pushUserService;
    
    protected MemberInfoService memberInfoService;
    protected FamilyService familyService;

    public FamilyService getFamilyService() {
		return familyService;
	}

	public void setFamilyService(FamilyService familyService) {
		this.familyService = familyService;
	}

	public MemberInfoService getMemberInfoService() {
		return memberInfoService;
	}

	public void setMemberInfoService(MemberInfoService memberInfoService) {
		this.memberInfoService = memberInfoService;
	}

	public UserService getUserService()
    {
        return userService;
    }

    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    public FileDescService getFileDescService()
    {
        return fileDescService;
    }

    public PhysiologIndexService getPhysiologIndexService()
    {
        return physiologIndexService;
    }

    public void setPhysiologIndexService(PhysiologIndexService physiologIndexService)
    {
        this.physiologIndexService = physiologIndexService;
    }

    public void setFileDescService(FileDescService fileDescService)
    {
        this.fileDescService = fileDescService;
    }

    public CustomerService getCustomerService()
    {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    public OrgService getOrgService()
    {
        return orgService;
    }

    public void setOrgService(OrgService orgService)
    {
        this.orgService = orgService;
    }

    public LoginRecordService getLoginRecordService()
    {
        return loginRecordService;
    }

    public void setLoginRecordService(LoginRecordService loginRecordService)
    {
        this.loginRecordService = loginRecordService;
    }

    public PushUserService getPushUserService()
    {
        return pushUserService;
    }

    public void setPushUserService(PushUserService pushUserService)
    {
        this.pushUserService = pushUserService;
    }

    /**
     * 登录操作
     */
    public abstract void login();

    /**
     * 处理登录成功后的操作
     * @throws Exception 
     */
    protected Map<String, Object> handleLogin(User user, JsonObject obj) throws Exception
    {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("userAccount", user.getAccount());
        data.put("userName", user.getName());
        data.put("userId", user.getId());
        data.put("ticket", sessionId);
        data.put("userType", user.getType());
        data.put("loginCode", 1);
        if (Constant.USER_TYPE_CUSTOMER.equals(user.getType()))
        {
            Customer cust = customerService.get(user.getId());
            data.put("cardNo", user.getIdCard());
            data.put("mobile", user.getPhone());
            data.put("birthday", cust.getBirthday());
            data.put("email", user.getEmail());
            data.put("sex", cust.getSex());
            // 生理指标
            PhysiologIndex pslIdx = physiologIndexService.get(user.getId());
            if (pslIdx != null)
            {
                data.put("height", pslIdx.getHeight());
                data.put("weight", pslIdx.getWeight());
            }
        }
        else if (Constant.USER_TYPE_DOCTOR.equals(user.getType()))
        {
            // 无需要操作
        }
        // 用户头像
        if (StringUtil.isNotEmpty(user.getImgFileId()))
        {
            FileDesc imgFile = fileDescService.get(user.getImgFileId());
            data.put("headIcon", "pub/cf_img.do?pt=" + FileUtil.encodePath(imgFile.getPath()));
        }
        // 补充会话信息
        SessionListener.put(session.getId(), session, user);
        // 将回话信息持久化到数据库
        UserSession userSession = new UserSession();
        userSession.setTicket(sessionId);
        userSession.setUserId(user.getId());
        userService.saveSession(userSession);
        // 登录推送用户信息
        String pushChannelId = GsonUtil.toString(obj.get("pushChannelId"));
        String pushUserId = GsonUtil.toString(obj.get("pushUserId"));
        String deviceTypeStr = GsonUtil.toString(obj.get("deviceType"));
        Integer deviceType = StringUtil.isNotEmpty(deviceTypeStr) ? Integer.valueOf(deviceTypeStr)
                : ConstDevice.DEVICE_TYPE_ANDROID;
        if (StringUtil.isNotEmpty(pushChannelId))
        {
            PushUser pushUser = new PushUser();
            pushUser.setUserId(user.getId());
            pushUser.setPushChannelId(pushChannelId);
            pushUser.setPushUserId(pushUserId);
            pushUser.setDeviceType(deviceType);
            pushUserService.save(pushUser);
        }
        Timestamp now = userSession.getCreateTime();
        // 登录记录
        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setUserId(user.getId());
        loginRecord.setType(Constant.LOGIN_TYPE_CLIENT);
        loginRecord.setCreateTime(now);
        loginRecordService.add(loginRecord);
        // 返回登录结果
        return data;
    }

}
