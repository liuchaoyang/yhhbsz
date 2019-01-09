package com.yzxt.yh.module.msg.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.PressurePulse;
import com.yzxt.yh.module.chk.service.PressurePulseService;
import com.yzxt.yh.module.msg.bean.HealthyGuide;
import com.yzxt.yh.module.msg.service.HealthyGuideService;
import com.yzxt.yh.module.sys.bean.CustFamily;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.module.sys.service.FamilyService;
import com.yzxt.yh.util.StringUtil;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

public class HealthyGuideAction extends BaseAction
{
	
	private static String masterSecret = "a057d6dddc5bd011757dafff";
    private static String appKey = "75cf0844e5ee2b0a456e6f1a";
    private static final String ALERT = "医生已回复";
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(HealthyGuideAction.class);

    private CustomerService customerService;
    private FamilyService familyService;
    
    public FamilyService getFamilyService() {
		return familyService;
	}

	public void setFamilyService(FamilyService familyService) {
		this.familyService = familyService;
	}

	private PressurePulseService pressurePulseService;

    public PressurePulseService getPressurePulseService() {
		return pressurePulseService;
	}

	public void setPressurePulseService(PressurePulseService pressurePulseService) {
		this.pressurePulseService = pressurePulseService;
	}

	private HealthyGuide healthyGuide;

    private HealthyGuideService healthyGuideService;

    public CustomerService getCustomerService()
    {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    public HealthyGuide getHealthyGuide()
    {
        return healthyGuide;
    }

    public void setHealthyGuide(HealthyGuide healthyGuide)
    {
        this.healthyGuide = healthyGuide;
    }

    public HealthyGuideService getHealthyGuideService()
    {
        return healthyGuideService;
    }

    public void setHealthyGuideService(HealthyGuideService healthyGuideService)
    {
        this.healthyGuideService = healthyGuideService;
    }

    /**
     * 添加指导
     * @param cust
     */
    public void add()
    {
        Result r = null;
        try
        {
            // 当前操作人
            User curOper = (User) super.getCurrentUser();
            String id = request.getParameter("id");
            healthyGuide.setCreateBy(curOper.getId());
            healthyGuide.setPreId(id);
            r = healthyGuideService.add(healthyGuide);
            String custId =healthyGuide.getCustId();
            List <PressurePulse> list =null;
            if(StringUtil.isNotEmpty(custId)){
            	 list = pressurePulseService.queryByCustId(custId,id);
                 for (PressurePulse pressurePulse : list) {
                 	if(pressurePulse.getType()==2){
                		pressurePulse.setType(3);
                	}
                 	r=pressurePulseService.updatePres(pressurePulse);
     			}
            }
           if(list.get(0).getState()==null){
        	   this.push();
           }
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "新增健康指导异常。", null);
            logger.error("新增健康指导异常。", e);
        }
        super.write(r);
    }

    public void guideList()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            String custId = request.getParameter("custId");
            if (StringUtil.isNotEmpty(custId))
            {
                filter.put("custId", StringUtil.trim(custId));
            }
            String memberName = (String) request.getParameter("memberName");
            String directReason = (String) request.getParameter("directReason");
            String beginDate = getRequest().getParameter("beginDate");
            String endDate = getRequest().getParameter("endDate");
            filter.put("memberName", StringUtil.trim(memberName));
            filter.put("directReason", StringUtil.trim(directReason));
            filter.put("beginDate", StringUtil.trim(beginDate));
            filter.put("endDate", StringUtil.trim(endDate));
            filter.put("operUser", super.getCurrentUser());
            PageModel<HealthyGuide> pageModel = healthyGuideService.getList(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("获取会员健康指导列表失败", e);
        }
    }

    /**
     * 跳转到查看页面
     * */
    public String toCheck() throws Exception
    {
        try
        {
            String id = request.getParameter("id");
            healthyGuide = healthyGuideService.getDetail(id);
            if (healthyGuide != null)
            {
                request.setAttribute("healthyGuide", healthyGuide);
            }
            else
            {
                healthyGuide = new HealthyGuide();
                request.setAttribute("healthyGuide", healthyGuide);
            }
        }
        catch (Exception e)
        {
            logger.error("跳转至客户明细页面出错。", e);
            return "error";
        }
        return "detail";
    }

    /**
     * 得到个人的健康指导
     * */
    public void getPersonalList()
    {
        try
        {
            // 当前操作人
            User curOper = (User) super.getCurrentUser();
            Map<String, Object> filter = new HashMap<String, Object>();
            String userId = curOper.getId();
            /* String memberName = (String) request.getParameter("memberName");*/
            String directReason = (String) request.getParameter("directReason");
            String beginDate = getRequest().getParameter("beginDate");
            String endDate = getRequest().getParameter("endDate");
            /* filter.put("memberName", StringUtil.trim(memberName));*/
            filter.put(userId, StringUtil.trim(userId));
            filter.put("directReason", StringUtil.trim(directReason));
            filter.put("beginDate", StringUtil.trim(beginDate));
            filter.put("endDate", StringUtil.trim(endDate));
            filter.put("userId", StringUtil.trim(userId));
            PageModel<HealthyGuide> pageModel = healthyGuideService.getPersonalList(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("获取健康指导列表失败", e);
        }
    }

    /**
     * 跳转到健康管理里面的客户指导
     * 2015.5.2
     * */
    public String toCustGuide()
    {
        User curOper = (User) super.getCurrentUser();
        String custId = request.getParameter("custId");
        if (Constant.USER_TYPE_CUSTOMER.equals(curOper.getType()) && custId == null)
        {
            custId = curOper.getId();
        }
        request.setAttribute("custId", custId);
        return "custGuide";
    }

    public String toAdd() throws Exception
    {
        String custId = request.getParameter("custId");
        String id = request.getParameter("id");
        User user = null;
        if (StringUtil.isNotEmpty(custId))
        {
            Customer cust = customerService.load(custId);
            user = cust.getUser();
        }
        else
        {

        }
        request.setAttribute("user", user);
        request.setAttribute("id", id);
        return "edit";
    }
    
    public PushResult push() throws APIConnectionException, APIRequestException{
        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 0, null,clientConfig);
        //PushPayload payload = buildPushObject_android_tag_alertWithTitle();
        PushPayload payload = buildPushObject_all_alias_alert();
        return jpushClient.sendPush(payload);
    }
   /* public static PushPayload buildPushObject_all_all_alert() {
        return PushPayload.alertAll(ALERT);
    }*/
    public  PushPayload buildPushObject_all_alias_alert() {
    	String id = request.getParameter("id");
    	List<PressurePulse> queryById = pressurePulseService.queryById(id);
    	String custId = queryById.get(0).getCustId();
    	CustFamily family = familyService.getFamilyUserId(custId);
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(family.getCustId()))
                .setNotification(Notification.alert(ALERT))
                .build();
    }
}
