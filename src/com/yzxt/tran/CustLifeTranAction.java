package com.yzxt.tran;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.cli.bean.Remind;
import com.yzxt.yh.module.cli.bean.Sleep;
import com.yzxt.yh.module.cli.bean.SleepQuality;
import com.yzxt.yh.module.cli.bean.Step;
import com.yzxt.yh.module.cli.service.RemindService;
import com.yzxt.yh.module.cli.service.SleepQualityService;
import com.yzxt.yh.module.cli.service.SleepService;
import com.yzxt.yh.module.cli.service.StepService;
import com.yzxt.yh.module.sys.bean.CustFamily;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.FamilyService;

import common.Logger;

/**
 * 客户生活信息类：包括睡眠质量，计步，定位等功能
 * @author h
 * 2015.11.5
 */
public class CustLifeTranAction extends BaseTranAction
{

    private static final long serialVersionUID = 1L;
    Logger logger = Logger.getLogger(CustLifeTranAction.class);

    private StepService stepService;
    private SleepService sleepService;
    private SleepQualityService sleepQualityService;
    private RemindService remindService;
    private String customerId;
    private FamilyService familyService;

    public StepService getStepService()
    {
        return stepService;
    }

    public void setStepService(StepService stepService)
    {
        this.stepService = stepService;
    }

    public SleepService getSleepService()
    {
        return sleepService;
    }

    public void setSleepService(SleepService sleepService)
    {
        this.sleepService = sleepService;
    }

    public SleepQualityService getSleepQualityService()
    {
        return sleepQualityService;
    }

    public void setSleepQualityService(SleepQualityService sleepQualityService)
    {
        this.sleepQualityService = sleepQualityService;
    }

    public RemindService getRemindService()
    {
        return remindService;
    }

    public void setRemindService(RemindService remindService)
    {
        this.remindService = remindService;
    }

    public FamilyService getFamilyService()
    {
        return familyService;
    }

    public void setFamilyService(FamilyService familyService)
    {
        this.familyService = familyService;
    }

    /**
     * 客户端查询计步列表
     * @author h
     * 2015.11.5
     * */
    public void steplist()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
          //判断是否是家庭成员，判断是家人还是医生查看
            String userId = "";
            User operUser = (User) super.getOperUser();
            String customerId = GsonUtil.toString(obj.get("custId"));
            if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
            {
                Map<String, Object> filter = new HashMap<String, Object>();
                filter.put("custId", customerId);
                PageTran<Step> pageTran = stepService.queryTran(filter, synTime, synType, pageSize);
                super.write(ResultTran.STATE_OK, null, pageTran);
            }
            else if (Constant.USER_TYPE_CUSTOMER.equals(operUser.getType()))
            {
                userId = GsonUtil.toString(obj.get("userId"));
                if (!customerId.equals(userId))
                {
                    //如果二个id相同，就是用户自己，如果不同，则需要判断是否家庭成员
                    Map<String, Object> filter2 = new HashMap<String, Object>();
                    filter2.put("custId", userId);
                    filter2.put("memberId", customerId);
                    CustFamily custF = familyService.getCustFamily(filter2);
                    if (custF != null)
                    {
                        Map<String, Object> filter = new HashMap<String, Object>();
                        filter.put("custId", customerId);
                        PageTran<Step> pageTran = stepService.queryTran(filter, synTime, synType,
                                pageSize);
                        super.write(ResultTran.STATE_OK, null, pageTran);
                    }
                    else
                    {
                        JsonArray retJson = new JsonArray();
                        super.write(ResultTran.STATE_ERROR, "该用户不是你的家庭成员，无法查看", retJson);
                    }
                }
                else
                {
                    Map<String, Object> filter = new HashMap<String, Object>();
                    filter.put("custId", customerId);
                    PageTran<Step> pageTran = stepService.queryTran(filter, synTime, synType,
                            pageSize);
                    super.write(ResultTran.STATE_OK, null, pageTran);
                }
            }
        }
        catch (Exception e)
        {
            logger.error("客户端查询计步列表失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 客户端查询睡眠列表
     * @author h
     * 2015.11.5
     * */
    public void sleeplist()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
          //判断是否是家庭成员，判断是家人还是医生查看
            String userId = "";
            User operUser = (User) super.getOperUser();
            String customerId = GsonUtil.toString(obj.get("custId"));
            if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
            {
                Map<String, Object> filter = new HashMap<String, Object>();
                filter.put("custId", customerId);
                PageTran<Sleep> pageTran = sleepService.queryTran(filter, synTime, synType, pageSize);
                super.write(ResultTran.STATE_OK, null, pageTran);
            }
            else if (Constant.USER_TYPE_CUSTOMER.equals(operUser.getType()))
            {
                userId = GsonUtil.toString(obj.get("userId"));
                if (!customerId.equals(userId))
                {
                    //如果二个id相同，就是用户自己，如果不同，则需要判断是否家庭成员
                    Map<String, Object> filter2 = new HashMap<String, Object>();
                    filter2.put("custId", userId);
                    filter2.put("memberId", customerId);
                    CustFamily custF = familyService.getCustFamily(filter2);
                    if (custF != null)
                    {
                        Map<String, Object> filter = new HashMap<String, Object>();
                        filter.put("custId", customerId);
                        PageTran<Sleep> pageTran = sleepService.queryTran(filter, synTime, synType,
                                pageSize);
                        super.write(ResultTran.STATE_OK, null, pageTran);
                    }
                    else
                    {
                        JsonArray retJson = new JsonArray();
                        super.write(ResultTran.STATE_ERROR, "该用户不是你的家庭成员，无法查看", retJson);
                    }
                }
                else
                {
                    Map<String, Object> filter = new HashMap<String, Object>();
                    filter.put("custId", customerId);
                    PageTran<Sleep> pageTran = sleepService.queryTran(filter, synTime, synType,
                            pageSize);
                    super.write(ResultTran.STATE_OK, null, pageTran);
                }
            }
        }
        catch (Exception e)
        {
            logger.error("客户端查询睡眠列表失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 客户端查询睡眠质量列表
     * @author h
     * 2015.11.5
     * */
    public void sleepqualitylist()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
          //判断是否是家庭成员，判断是家人还是医生查看
            String userId = "";
            User operUser = (User) super.getOperUser();
            String customerId = GsonUtil.toString(obj.get("custId"));
            if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
            {
                Map<String, Object> filter = new HashMap<String, Object>();
                filter.put("custId", customerId);
                PageTran<SleepQuality> pageTran = sleepQualityService.queryTran(filter, synTime, synType, pageSize);
                super.write(ResultTran.STATE_OK, null, pageTran);
            }
            else if (Constant.USER_TYPE_CUSTOMER.equals(operUser.getType()))
            {
                userId = GsonUtil.toString(obj.get("userId"));
                if (!customerId.equals(userId))
                {
                    //如果二个id相同，就是用户自己，如果不同，则需要判断是否家庭成员
                    Map<String, Object> filter2 = new HashMap<String, Object>();
                    filter2.put("custId", userId);
                    filter2.put("memberId", customerId);
                    CustFamily custF = familyService.getCustFamily(filter2);
                    if (custF != null)
                    {
                        Map<String, Object> filter = new HashMap<String, Object>();
                        filter.put("custId", customerId);
                        PageTran<SleepQuality> pageTran = sleepQualityService.queryTran(filter, synTime, synType,
                                pageSize);
                        super.write(ResultTran.STATE_OK, null, pageTran);
                    }
                    else
                    {
                        JsonArray retJson = new JsonArray();
                        super.write(ResultTran.STATE_ERROR, "该用户不是你的家庭成员，无法查看", retJson);
                    }
                }
                else
                {
                    Map<String, Object> filter = new HashMap<String, Object>();
                    filter.put("custId", customerId);
                    PageTran<SleepQuality> pageTran = sleepQualityService.queryTran(filter, synTime, synType,
                            pageSize);
                    super.write(ResultTran.STATE_OK, null, pageTran);
                }
            }
        }
        catch (Exception e)
        {
            logger.error("客户端查询睡眠质量列表失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }
    
    /**
     * 客户端查询提醒列表
     * @author h
     * 2015.11.5
     * */
    public void remind()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
          //判断是否是家庭成员，判断是家人还是医生查看
            String userId = "";
            User operUser = (User) super.getOperUser();
            String customerId = GsonUtil.toString(obj.get("custId"));
            if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
            {
                Map<String, Object> filter = new HashMap<String, Object>();
                filter.put("custId", customerId);
                PageTran<Remind> pageTran = remindService.queryTran(filter, synTime, synType, pageSize);
                super.write(ResultTran.STATE_OK, null, pageTran);
            }
            else if (Constant.USER_TYPE_CUSTOMER.equals(operUser.getType()))
            {
                userId = GsonUtil.toString(obj.get("userId"));
                if (!customerId.equals(userId))
                {
                    //如果二个id相同，就是用户自己，如果不同，则需要判断是否家庭成员
                    Map<String, Object> filter2 = new HashMap<String, Object>();
                    filter2.put("custId", userId);
                    filter2.put("memberId", customerId);
                    CustFamily custF = familyService.getCustFamily(filter2);
                    if (custF != null)
                    {
                        Map<String, Object> filter = new HashMap<String, Object>();
                        filter.put("custId", customerId);
                        PageTran<Remind> pageTran = remindService.queryTran(filter, synTime, synType,
                                pageSize);
                        super.write(ResultTran.STATE_OK, null, pageTran);
                    }
                    else
                    {
                        JsonArray retJson = new JsonArray();
                        super.write(ResultTran.STATE_ERROR, "该用户不是你的家庭成员，无法查看", retJson);
                    }
                }
                else
                {
                    Map<String, Object> filter = new HashMap<String, Object>();
                    filter.put("custId", customerId);
                    PageTran<Remind> pageTran = remindService.queryTran(filter, synTime, synType,
                            pageSize);
                    super.write(ResultTran.STATE_OK, null, pageTran);
                }
            }
        }
        catch (Exception e)
        {
            logger.error("客户端查询提醒列表失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }
}
