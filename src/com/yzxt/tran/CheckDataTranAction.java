package com.yzxt.tran;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.AnalysisUricAcid;
import com.yzxt.yh.module.chk.bean.BloodOxygen;
import com.yzxt.yh.module.chk.bean.BloodSugar;
import com.yzxt.yh.module.chk.bean.BodyFat;
import com.yzxt.yh.module.chk.bean.PressurePulse;
import com.yzxt.yh.module.chk.bean.Pulse;
import com.yzxt.yh.module.chk.bean.Temperature;
import com.yzxt.yh.module.chk.bean.TotalCholesterol;
import com.yzxt.yh.module.chk.bean.UricAcid;
import com.yzxt.yh.module.chk.service.AnalysisUricAcidService;
import com.yzxt.yh.module.chk.service.BloodOxygenService;
import com.yzxt.yh.module.chk.service.BloodSugarService;
import com.yzxt.yh.module.chk.service.BodyFatService;
import com.yzxt.yh.module.chk.service.PressurePulseService;
import com.yzxt.yh.module.chk.service.PulseService;
import com.yzxt.yh.module.chk.service.TemperatureService;
import com.yzxt.yh.module.chk.service.TotalCholesterolService;
import com.yzxt.yh.module.chk.service.UricAcidService;
import com.yzxt.yh.module.rpt.bean.AnalysisReport;
import com.yzxt.yh.module.rpt.service.AnalysisReportService;
import com.yzxt.yh.module.sys.bean.CustFamily;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.FamilyService;
import com.yzxt.yh.module.sys.service.UserService;
import com.yzxt.yh.sch.job.AnalysisReportJob;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

public class CheckDataTranAction extends BaseTranAction
{
    private static final long serialVersionUID = 1L;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat simpleDf = new SimpleDateFormat("yyyy/MM/dd");
    private static Logger logger = Logger.getLogger(CheckDataTranAction.class);
    private PulseService pulseService;
    private PressurePulseService pressurePulseService;
    private BloodSugarService bloodSugarService;
    private BloodOxygenService bloodOxygenService;
    private TemperatureService temperatureService;
    private TotalCholesterolService totalCholesterolService;
    private UricAcidService uricAcidService;
    private AnalysisUricAcidService analysisUricAcidService;
    private BodyFatService bodyFatService;
    private UserService userService;
    private String customerId;
    private FamilyService familyService;
    private AnalysisReportService analysisReportService;
    
    private String preId;

    public String getPreId() {
		return preId;
	}

	public void setPreId(String preId) {
		this.preId = preId;
	}

	public AnalysisReportService getAnalysisReportService() {
		return analysisReportService;
	}

	public void setAnalysisReportService(AnalysisReportService analysisReportService) {
		this.analysisReportService = analysisReportService;
	}

	public PulseService getPulseService()
    {
        return pulseService;
    }

    public void setPulseService(PulseService pulseService)
    {
        this.pulseService = pulseService;
    }

    public PressurePulseService getPressurePulseService()
    {
        return pressurePulseService;
    }

    public void setPressurePulseService(PressurePulseService pressurePulseService)
    {
        this.pressurePulseService = pressurePulseService;
    }

    public BloodSugarService getBloodSugarService()
    {
        return bloodSugarService;
    }

    public void setBloodSugarService(BloodSugarService bloodSugarService)
    {
        this.bloodSugarService = bloodSugarService;
    }

    public BloodOxygenService getBloodOxygenService()
    {
        return bloodOxygenService;
    }

    public void setBloodOxygenService(BloodOxygenService bloodOxygenService)
    {
        this.bloodOxygenService = bloodOxygenService;
    }

    public TemperatureService getTemperatureService()
    {
        return temperatureService;
    }

    public void setTemperatureService(TemperatureService temperatureService)
    {
        this.temperatureService = temperatureService;
    }

    public TotalCholesterolService getTotalCholesterolService()
    {
        return totalCholesterolService;
    }

    public void setTotalCholesterolService(TotalCholesterolService totalCholesterolService)
    {
        this.totalCholesterolService = totalCholesterolService;
    }

    public UricAcidService getUricAcidService()
    {
        return uricAcidService;
    }

    public void setUricAcidService(UricAcidService uricAcidService)
    {
        this.uricAcidService = uricAcidService;
    }

    public AnalysisUricAcidService getAnalysisUricAcidService()
    {
        return analysisUricAcidService;
    }

    public void setAnalysisUricAcidService(AnalysisUricAcidService analysisUricAcidService)
    {
        this.analysisUricAcidService = analysisUricAcidService;
    }

    public BodyFatService getBodyFatService()
    {
        return bodyFatService;
    }

    public void setBodyFatService(BodyFatService bodyFatService)
    {
        this.bodyFatService = bodyFatService;
    }

    public UserService getUserService()
    {
        return userService;
    }

    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    public Timestamp getSynTime()
    {
        return synTime;
    }

    public void setSynTime(Timestamp synTime)
    {
        this.synTime = synTime;
    }

    public int getSynType()
    {
        return synType;
    }

    public void setSynType(int synType)
    {
        this.synType = synType;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    public static long getSerialversionuid()
    {
        return serialVersionUID;
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
     * 初始查询参数
     * @param obj
     */
    protected void initQuery(JsonObject obj)
    {
        super.initQuery(obj);
        if (obj != null)
        {
            JsonElement cObj = obj.get("custId");
            if (cObj != null)
            {
                customerId = cObj.getAsString();
            }
        }
    }
     
    /**
     * 上传血压脉率
     */
    public void prepulse()
    {
        try
        {
            JsonObject obj = super.getParams();
            String idCard = GsonUtil.toString(obj.get("cardNo"));
            PressurePulse bean = gson.fromJson(obj, PressurePulse.class);
            Timestamp now = new Timestamp(System.currentTimeMillis());
            if (StringUtil.isEmpty(bean.getCustId()))
            {

                if (StringUtil.isEmpty(idCard))
                {
                    super.write(ResultTran.STATE_ERROR, "上传血压错误，用户ID和身份证为空。");
                    return;
                }
                User user = userService.getUserByIdCard(idCard);
                if (user == null)
                {
                    super.write(ResultTran.STATE_ERROR, "上传血压错误，没有身份证为：" + idCard + "用户。");
                    return;
                }
                bean.setCustId(user.getId());
            }
            pressurePulseService.save(bean);
            AnalysisReport report = new AnalysisReport();
            report.setCustId(bean.getCustId());
            report.setCreateTime(now);
            report.setReportType(1);
            report.setPressId(bean.getId());
            Date dt=new Date();
            //SimpleDateFormat matter=new SimpleDateFormat("yyyy-MM-dd");
            report.setExamBeginTime(dt);
            report.setExamEndTime(DateUtil.addDay(dt, 7));
            analysisReportService.save(report);
           preId = bean.getId();
            //this.gen();
            this.list();
            super.write(ResultTran.STATE_OK,"生成分析报告");
        }
        catch (Exception e)
        {
            logger.error("上传血压错误。", e);
            super.write(ResultTran.STATE_ERROR, "上传血压错误。");
        }
    }

   

    /**
     * 上传血糖
     */
    public void bloodglu()
    {
        try
        {
            JsonObject obj = super.getParams();
            String idCard = GsonUtil.toString(obj.get("cardNo"));
            BloodSugar bean = gson.fromJson(obj, BloodSugar.class);
            if (StringUtil.isEmpty(bean.getCustId()))
            {

                if (StringUtil.isEmpty(idCard))
                {
                    super.write(ResultTran.STATE_ERROR, "上传血糖错误，用户ID和身份证为空。");
                    return;
                }
                User user = userService.getUserByIdCard(idCard);
                if (user == null)
                {
                    super.write(ResultTran.STATE_ERROR, "上传血糖错误，没有身份证为：" + idCard + "用户。");
                    return;
                }
                bean.setCustId(user.getId());
            }
            bloodSugarService.save(bean);
            this.gen();
            super.write(ResultTran.STATE_OK,"生成分析报告",null);
           
        }
        catch (Exception e)
        {
            logger.error("上传血糖错误。", e);
            super.write(ResultTran.STATE_ERROR, "上传血糖错误。");
        }
    }

    /**
     * 上传心率
     */
    public void pulse()
    {
        try
        {
            JsonObject obj = super.getParams();
            Pulse bean = gson.fromJson(obj, Pulse.class);
            String idCard = GsonUtil.toString(obj.get("cardNo"));
            if (StringUtil.isEmpty(bean.getCustId()))
            {
                if (StringUtil.isEmpty(idCard))
                {
                    super.write(ResultTran.STATE_ERROR, "上传心率错误，用户ID和身份证为空。");
                    return;
                }
                User user = userService.getUserByIdCard(idCard);
                if (user == null)
                {
                    super.write(ResultTran.STATE_ERROR, "上传心率错误，没有身份证为：" + idCard + "用户。");
                    return;
                }
                bean.setCustId(user.getId());
            }
            pulseService.save(bean);
            super.write(ResultTran.STATE_OK);
        }
        catch (Exception e)
        {
            logger.error("上传心率错误。", e);
            super.write(ResultTran.STATE_ERROR, "上传心率错误。");
        }
    }

    /**
     * 上传血氧
     */
    public void oxyblood()
    {
        try
        {
            JsonObject obj = super.getParams();
            String idCard = GsonUtil.toString(obj.get("cardNo"));
            BloodOxygen bean = gson.fromJson(obj, BloodOxygen.class);
            if (StringUtil.isEmpty(bean.getCustId()))
            {
                if (StringUtil.isEmpty(idCard))
                {
                    super.write(ResultTran.STATE_ERROR, "上传血氧错误，用户ID和身份证为空。");
                    return;
                }
                User user = userService.getUserByIdCard(idCard);
                if (user == null)
                {
                    super.write(ResultTran.STATE_ERROR, "上传血氧错误，没有身份证为：" + idCard + "用户。");
                    return;
                }
                bean.setCustId(user.getId());
            }
            bloodOxygenService.save(bean);
            super.write(ResultTran.STATE_OK);
        }
        catch (Exception e)
        {
            logger.error("上传血氧错误。", e);
            super.write(ResultTran.STATE_ERROR, "上传血氧错误。");
        }
    }

    /**
     * 上传体温
     */
    public void tempe()
    {
        try
        {
            JsonObject obj = super.getParams();
            String idCard = GsonUtil.toString(obj.get("cardNo"));
            Temperature bean = gson.fromJson(obj, Temperature.class);
            if (StringUtil.isEmpty(bean.getCustId()))
            {

                if (StringUtil.isEmpty(idCard))
                {
                    super.write(ResultTran.STATE_ERROR, "上传体温错误，用户ID和身份证为空。");
                    return;
                }
                User user = userService.getUserByIdCard(idCard);
                if (user == null)
                {
                    super.write(ResultTran.STATE_ERROR, "上传体温错误，没有身份证为：" + idCard + "用户。");
                    return;
                }
                bean.setCustId(user.getId());
            }
            temperatureService.save(bean);
            super.write(ResultTran.STATE_OK);
        }
        catch (Exception e)
        {
            logger.error("上传体温错误。", e);
            super.write(ResultTran.STATE_ERROR, "上传体温错误。");
        }
    }

    /**
     * 上传总胆固醇
     */
    public void cholesterol()
    {
        try
        {
            JsonObject obj = super.getParams();
            String idCard = GsonUtil.toString(obj.get("cardNo"));
            TotalCholesterol bean = gson.fromJson(obj, TotalCholesterol.class);
            if (StringUtil.isEmpty(bean.getCustId()))
            {
                if (StringUtil.isEmpty(idCard))
                {
                    super.write(ResultTran.STATE_ERROR, "上传总胆固醇错误，用户ID和身份证为空。");
                    return;
                }
                User user = userService.getUserByIdCard(idCard);
                if (user == null)
                {
                    super.write(ResultTran.STATE_ERROR, "上传总胆固醇错误，没有身份证为：" + idCard + "用户。");
                    return;
                }
                bean.setCustId(user.getId());
            }
            totalCholesterolService.save(bean);
            super.write(ResultTran.STATE_OK);
        }
        catch (Exception e)
        {
            logger.error("上传总胆固醇错误。", e);
            super.write(ResultTran.STATE_ERROR, "上传总胆固醇错误。");
        }
    }

    /**
     * 上传尿酸
     */
    public void uricacid()
    {
        try
        {
            JsonObject obj = super.getParams();
            String idCard = GsonUtil.toString(obj.get("cardNo"));
            UricAcid bean = gson.fromJson(obj, UricAcid.class);
            if (StringUtil.isEmpty(bean.getCustId()))
            {
                if (StringUtil.isEmpty(idCard))
                {
                    super.write(ResultTran.STATE_ERROR, "上传尿酸错误，用户ID和身份证为空。");
                    return;
                }
                User user = userService.getUserByIdCard(idCard);
                if (user == null)
                {
                    super.write(ResultTran.STATE_ERROR, "上传尿酸错误，没有身份证为：" + idCard + "用户。");
                    return;
                }
                bean.setCustId(user.getId());
            }
            uricAcidService.save(bean);
            super.write(ResultTran.STATE_OK);
        }
        catch (Exception e)
        {
            logger.error("上传尿酸错误。", e);
            super.write(ResultTran.STATE_ERROR, "上传尿酸错误。");
        }
    }

    /**
     * 上传尿液分析
     */
    public void analyacid()
    {
        try
        {
            JsonObject obj = super.getParams();
            String idCard = GsonUtil.toString(obj.get("cardNo"));
            AnalysisUricAcid bean = gson.fromJson(obj, AnalysisUricAcid.class);
            if (StringUtil.isEmpty(bean.getCustId()))
            {
                if (StringUtil.isEmpty(idCard))
                {
                    super.write(ResultTran.STATE_ERROR, "上传尿液分析错误，用户ID和身份证为空。");
                    return;
                }
                User user = userService.getUserByIdCard(idCard);
                if (user == null)
                {
                    super.write(ResultTran.STATE_ERROR, "上传尿液分析错误，没有身份证为：" + idCard + "用户。");
                    return;
                }
                bean.setCustId(user.getId());
            }
            analysisUricAcidService.save(bean);
            super.write(ResultTran.STATE_OK);
        }
        catch (Exception e)
        {
            logger.error("上传尿液分析错误。", e);
            super.write(ResultTran.STATE_ERROR, "上传尿液分析错误。");
        }
    }

    /**
     * 个人血压脉率列表
     */
    public void prepulselists()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            List<User> dd = new ArrayList<>();
            //判断是否是家庭成员，判断是家人还是医生查看
            String userId = "";
            User operUser = (User) super.getOperUser();
            if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
            {
                Map<String, Object> filter = new HashMap<String, Object>();
                filter.put("custId", customerId);
                PageTran<PressurePulse> pageTran = pressurePulseService.queryTra(filter, synTime, synType, pageSize);
                super.write(ResultTran.STATE_OK, null, pageTran);
            }
            else if (Constant.USER_TYPE_ADMIN.equals(operUser.getType()))
            {
                userId = GsonUtil.toString(obj.get("userId"));
                // 客户查询自己的数据
                if (StringUtil.isEmpty(customerId) || customerId.equals(userId))
                {
                    Map<String, Object> filter = new HashMap<String, Object>();
                    filter.put("userId", userId);
                    PageTran<PressurePulse> pageTran = pressurePulseService.queryTra(filter, synTime, synType,
                            pageSize);
                    super.write(ResultTran.STATE_OK, null, pageTran);
                }
                else
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
                        PageTran<PressurePulse> pageTran = pressurePulseService.queryTra(filter, synTime, synType,
                                pageSize);
                        super.write(ResultTran.STATE_OK, null, pageTran);
                    }
                    else
                    {
                        JsonArray retJson = new JsonArray();
                        super.write(ResultTran.STATE_ERROR, "该用户不是你的家庭成员，无法查看", retJson);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("查询血压心率错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询血压心率错误。");
        }
    }
    /**
     * 血压脉率列表
     */
    public void prepulselist()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            String userId = GsonUtil.toString(obj.get("userId"));
            String custId = GsonUtil.toString(obj.get("custId"));
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("userId", userId);
            filter.put("custId", custId);
            PageTran<PressurePulse> pageTran = pressurePulseService.queryTran(filter,synTime, synType, pageSize);
            
            super.write(ResultTran.STATE_OK, "获取成功", pageTran);
        }
        catch (Exception e)
        {
            logger.error("查询血压心率错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询血压心率错误。");
        }
    }
    /**
     * 血压个人脉率列表
     */
    public void prepulseMember()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            String custId = GsonUtil.toString(obj.get("custId"));
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("custId", custId);
            PageTran<PressurePulse> pageTran = pressurePulseService.queryMember(filter,synTime, synType, pageSize);
            
            super.write(ResultTran.STATE_OK, "获取成功", pageTran);
        }
        catch (Exception e)
        {
            logger.error("查询血压心率错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询血压心率错误。");
        }
    }
    /**
     * 血糖列表
     */
    public void bloodglulist()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            String userId = "";
            User operUser = (User) super.getOperUser();
            if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
            {
                Map<String, Object> filter = new HashMap<String, Object>();
                filter.put("custId", customerId);
                PageTran<BloodSugar> pageTran = bloodSugarService.queryTran(filter, synTime, synType, pageSize);
                super.write(ResultTran.STATE_OK, null, pageTran);
            }
            else if (Constant.USER_TYPE_CUSTOMER.equals(operUser.getType()))
            {
                userId = GsonUtil.toString(obj.get("userId"));
                if (StringUtil.isEmpty(customerId) || customerId.equals(userId))
                {
                    Map<String, Object> filter = new HashMap<String, Object>();
                    filter.put("custId", userId);
                    PageTran<BloodSugar> pageTran = bloodSugarService.queryTran(filter, synTime, synType, pageSize);
                    super.write(ResultTran.STATE_OK, null, pageTran);
                }
                else
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
                        PageTran<BloodSugar> pageTran = bloodSugarService.queryTran(filter, synTime, synType, pageSize);
                        super.write(ResultTran.STATE_OK, null, pageTran);
                    }
                    else
                    {
                        JsonArray retJson = new JsonArray();
                        super.write(ResultTran.STATE_ERROR, "该用户不是你的家庭成员，无法查看", retJson);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("查询血糖错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询血糖错误。");
        }
    }

    /**
     * 心率列表
     */
    public void pulselist()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            String userId = "";
            User operUser = (User) super.getOperUser();
            if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
            {
                Map<String, Object> filter = new HashMap<String, Object>();
                filter.put("custId", customerId);
                PageTran<Pulse> pageTran = pulseService.queryTran(filter, synTime, synType, pageSize);
                super.write(ResultTran.STATE_OK, null, pageTran);
            }
            else if (Constant.USER_TYPE_CUSTOMER.equals(operUser.getType()))
            {
                userId = GsonUtil.toString(obj.get("userId"));
                if (StringUtil.isEmpty(customerId) || customerId.equals(userId))
                {
                    Map<String, Object> filter = new HashMap<String, Object>();
                    filter.put("custId", userId);
                    PageTran<Pulse> pageTran = pulseService.queryTran(filter, synTime, synType, pageSize);
                    super.write(ResultTran.STATE_OK, null, pageTran);
                }
                else
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
                        PageTran<Pulse> pageTran = pulseService.queryTran(filter, synTime, synType, pageSize);
                        super.write(ResultTran.STATE_OK, null, pageTran);
                    }
                    else
                    {
                        JsonArray retJson = new JsonArray();
                        super.write(ResultTran.STATE_ERROR, "该用户不是你的家庭成员，无法查看", retJson);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("查询心率错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询心率错误。");
        }
    }

    /**
     * 血氧列表
     */
    public void oxybloodlist()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            String userId = "";
            User operUser = (User) super.getOperUser();
            if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
            {
                Map<String, Object> filter = new HashMap<String, Object>();
                filter.put("custId", customerId);
                PageTran<BloodOxygen> pageTran = bloodOxygenService.queryTran(filter, synTime, synType, pageSize);
                super.write(ResultTran.STATE_OK, null, pageTran);
            }
            else if (Constant.USER_TYPE_CUSTOMER.equals(operUser.getType()))
            {
                userId = GsonUtil.toString(obj.get("userId"));
                if (StringUtil.isEmpty(customerId) || customerId.equals(userId))
                {
                    Map<String, Object> filter = new HashMap<String, Object>();
                    filter.put("custId", userId);
                    PageTran<BloodOxygen> pageTran = bloodOxygenService.queryTran(filter, synTime, synType, pageSize);
                    super.write(ResultTran.STATE_OK, null, pageTran);
                }
                else
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
                        PageTran<BloodOxygen> pageTran = bloodOxygenService.queryTran(filter, synTime, synType,
                                pageSize);
                        super.write(ResultTran.STATE_OK, null, pageTran);
                    }
                    else
                    {
                        JsonArray retJson = new JsonArray();
                        super.write(ResultTran.STATE_ERROR, "该用户不是你的家庭成员，无法查看", retJson);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("查询血氧错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询血氧错误。");
        }
    }

    /**
     * 体温列表
     */
    public void tempelist()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            String userId = "";
            User operUser = (User) super.getOperUser();
            if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
            {
                Map<String, Object> filter = new HashMap<String, Object>();
                filter.put("custId", customerId);
                PageTran<Temperature> pageTran = temperatureService.queryTran(filter, synTime, synType, pageSize);
                super.write(ResultTran.STATE_OK, null, pageTran);
            }
            else if (Constant.USER_TYPE_CUSTOMER.equals(operUser.getType()))
            {
                userId = GsonUtil.toString(obj.get("userId"));
                if (StringUtil.isEmpty(customerId) || customerId.equals(userId))
                {
                    Map<String, Object> filter = new HashMap<String, Object>();
                    filter.put("custId", userId);
                    PageTran<Temperature> pageTran = temperatureService.queryTran(filter, synTime, synType, pageSize);
                    super.write(ResultTran.STATE_OK, null, pageTran);
                }
                else
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
                        PageTran<Temperature> pageTran = temperatureService.queryTran(filter, synTime, synType,
                                pageSize);
                        super.write(ResultTran.STATE_OK, null, pageTran);
                    }
                    else
                    {
                        JsonArray retJson = new JsonArray();
                        super.write(ResultTran.STATE_ERROR, "该用户不是你的家庭成员，无法查看", retJson);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("查询体温错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询体温错误。");
        }
    }

    /**
     * 总胆固醇列表
     */
    public void cholesterollist()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            String userId = "";
            User operUser = (User) super.getOperUser();
            if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
            {
                Map<String, Object> filter = new HashMap<String, Object>();
                filter.put("custId", customerId);
                PageTran<TotalCholesterol> pageTran = totalCholesterolService.queryTran(filter, synTime, synType,
                        pageSize);
                super.write(ResultTran.STATE_OK, null, pageTran);
            }
            else if (Constant.USER_TYPE_CUSTOMER.equals(operUser.getType()))
            {
                userId = GsonUtil.toString(obj.get("userId"));
                if (StringUtil.isEmpty(customerId) || customerId.equals(userId))
                {
                    Map<String, Object> filter = new HashMap<String, Object>();
                    filter.put("custId", userId);
                    PageTran<TotalCholesterol> pageTran = totalCholesterolService.queryTran(filter, synTime, synType,
                            pageSize);
                    super.write(ResultTran.STATE_OK, null, pageTran);
                }
                else
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
                        PageTran<TotalCholesterol> pageTran = totalCholesterolService.queryTran(filter, synTime,
                                synType, pageSize);
                        super.write(ResultTran.STATE_OK, null, pageTran);
                    }
                    else
                    {
                        JsonArray retJson = new JsonArray();
                        super.write(ResultTran.STATE_ERROR, "该用户不是你的家庭成员，无法查看", retJson);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("查询总胆固醇错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询总胆固醇错误。");
        }
    }

    /**
     * 尿酸列表
     * @return
     */
    public void uricacidlist()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            String userId = "";
            User operUser = (User) super.getOperUser();
            if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
            {
                Map<String, Object> filter = new HashMap<String, Object>();
                filter.put("custId", customerId);
                PageTran<UricAcid> pageTran = uricAcidService.queryTran(filter, synTime, synType, pageSize);
                super.write(ResultTran.STATE_OK, null, pageTran);
            }
            else if (Constant.USER_TYPE_CUSTOMER.equals(operUser.getType()))
            {
                userId = GsonUtil.toString(obj.get("userId"));
                if (StringUtil.isEmpty(customerId) || customerId.equals(userId))
                {
                    Map<String, Object> filter = new HashMap<String, Object>();
                    filter.put("custId", userId);
                    PageTran<UricAcid> pageTran = uricAcidService.queryTran(filter, synTime, synType, pageSize);
                    super.write(ResultTran.STATE_OK, null, pageTran);
                }
                else
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
                        PageTran<UricAcid> pageTran = uricAcidService.queryTran(filter, synTime, synType, pageSize);
                        super.write(ResultTran.STATE_OK, null, pageTran);
                    }
                    else
                    {
                        JsonArray retJson = new JsonArray();
                        super.write(ResultTran.STATE_ERROR, "该用户不是你的家庭成员，无法查看", retJson);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("查询尿酸错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询尿酸错误。");
        }
    }

    /**
     * 尿常规列表
     * @return
     */
    public void analyacidlist()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            String userId = "";
            User operUser = (User) super.getOperUser();
            if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
            {
                Map<String, Object> filter = new HashMap<String, Object>();
                filter.put("custId", customerId);
                PageTran<AnalysisUricAcid> pageTran = analysisUricAcidService.queryTran(filter, synTime, synType,
                        pageSize);
                if (pageTran.getData().size() > 0)
                {
                    super.write(ResultTran.STATE_OK, null, pageTran);
                }
                else
                {
                    JsonArray jsa = new JsonArray();
                    super.write(ResultTran.STATE_OK, null, jsa);
                }
            }
            else if (Constant.USER_TYPE_CUSTOMER.equals(operUser.getType()))
            {
                userId = GsonUtil.toString(obj.get("userId"));
                if (StringUtil.isEmpty(customerId) || customerId.equals(userId))
                {
                    Map<String, Object> filter = new HashMap<String, Object>();
                    filter.put("custId", userId);
                    PageTran<AnalysisUricAcid> pageTran = analysisUricAcidService.queryTran(filter, synTime, synType,
                            pageSize);
                    if (pageTran.getData().size() > 0)
                    {
                        super.write(ResultTran.STATE_OK, null, pageTran);
                    }
                    else
                    {
                        JsonArray jsa = new JsonArray();
                        super.write(ResultTran.STATE_OK, null, jsa);
                    }
                }
                else
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
                        PageTran<AnalysisUricAcid> pageTran = analysisUricAcidService.queryTran(filter, synTime,
                                synType, pageSize);
                        if (pageTran.getData().size() > 0)
                        {
                            super.write(ResultTran.STATE_OK, null, pageTran);
                        }
                        else
                        {
                            JsonArray jsa = new JsonArray();
                            super.write(ResultTran.STATE_OK, null, jsa);
                        }
                    }
                    else
                    {
                        JsonArray retJson = new JsonArray();
                        super.write(ResultTran.STATE_ERROR, "该用户不是你的家庭成员，无法查看", retJson);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("查询尿常规错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询尿常规错误。");
        }
    }

    /**
     * 上传体脂参数：体脂参数包括：体重，身高，皮下脂肪率，内脏脂肪率，肌肉率，基础代谢率
     * @author h
     * 2015.8.10
     */
    public void bodyFat()
    {
        try
        {
            JsonObject obj = super.getParams();
            String idCard = GsonUtil.toString(obj.get("cardNo"));
            BodyFat bean = gson.fromJson(obj, BodyFat.class);
            if (StringUtil.isEmpty(bean.getCustId()))
            {
                if (StringUtil.isEmpty(idCard))
                {
                    super.write(ResultTran.STATE_ERROR, "上传血脂错误，用户ID和身份证为空。");
                    return;
                }
                User user = userService.getUserByIdCard(idCard);
                if (user == null)
                {
                    super.write(ResultTran.STATE_ERROR, "上传血脂错误，没有身份证为：" + idCard + "用户。");
                    return;
                }
                bean.setCustId(user.getId());
            }
            bodyFatService.save(bean);
            super.write(ResultTran.STATE_OK);
        }
        catch (Exception e)
        {
            logger.error("上传体脂参数错误。", e);
            e.printStackTrace();
            super.write(ResultTran.STATE_ERROR, "上传体脂参数错误。");
        }
    }

    /**
     * 查询体脂检测数据
     * @author h
     * @param filter 过滤条件
     * @param synTime 同步时间点
     * @param synType 同步方式，大于0：取时间点后的数据，小于0取时间点前的数据
     * 2015.8.10
     * @return
     * */
    public void bodyFatlist()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            //判断是否是家庭成员，判断是家人还是医生查看
            String userId = "";
            User operUser = (User) super.getOperUser();
            if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
            {
                Map<String, Object> filter = new HashMap<String, Object>();
                filter.put("custId", customerId);
                PageTran<BodyFat> pageTran = bodyFatService.queryTran(filter, synTime, synType, pageSize);
                super.write(ResultTran.STATE_OK, null, pageTran);
            }
            else if (Constant.USER_TYPE_CUSTOMER.equals(operUser.getType()))
            {
                userId = GsonUtil.toString(obj.get("userId"));
                if (StringUtil.isEmpty(customerId) || customerId.equals(userId))
                {
                    Map<String, Object> filter = new HashMap<String, Object>();
                    filter.put("custId", userId);
                    PageTran<BodyFat> pageTran = bodyFatService.queryTran(filter, synTime, synType, pageSize);
                    super.write(ResultTran.STATE_OK, null, pageTran);
                }
                else
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
                        PageTran<BodyFat> pageTran = bodyFatService.queryTran(filter, synTime, synType, pageSize);
                        super.write(ResultTran.STATE_OK, null, pageTran);
                    }
                    else
                    {
                        JsonArray retJson = new JsonArray();
                        super.write(ResultTran.STATE_ERROR, "该用户不是你的家庭成员，无法查看", retJson);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("查询体脂数据错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询体脂数据错误。");
        }
    }
    /**
     * 
     * 生成分析报告
     */
    public void gen()
    {
        AnalysisReportJob job = new AnalysisReportJob();
        Date now = new Date();
        try
        {
            job.genMuchWeek(DateUtil.addDay(now, -7), DateUtil.addDay(now, 0));
        }
        catch (Exception e)
        {
            logger.error("临时生成分析报告出错。", e);
        }
    }
    public void list()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            JsonObject jObj;
            Map<String, Object> filter = new HashMap<String, Object>();
            // 客户ID
            JsonElement cObj = obj.get("custId");
            String custId = obj.get("custId").getAsString();
            if (cObj != null)
            {
                filter.put("custId", custId);
            }
            JsonElement typeObj = obj.get("type");
            if (typeObj != null && StringUtil.isNotEmpty(typeObj.getAsString()))
            {
                filter.put("reportType", typeObj.getAsInt());
            }
            PageTran<AnalysisReport> pageTran = analysisReportService.queryMyTran(filter, synTime, synType, pageSize);
            User user = userService.getUser(custId);
            JsonArray jArray = new JsonArray();
           
            List<AnalysisReport> list = pageTran.getData();
            for (AnalysisReport rpt : list)
            {   
            	  
            	jObj = new JsonObject();
            	jObj.addProperty("id",rpt.getPressId());
                jObj.addProperty("reportId", rpt.getId());
                jObj.addProperty("type", rpt.getReportType());
                /*jObj.addProperty("stEdTime",
                        simpleDf.format(rpt.getExamBeginTime()) + "-" + simpleDf.format(rpt.getExamEndTime()));*/
                jObj.addProperty("createTime", DateUtil.toHtmlDate(rpt.getCreateTime()));
                jObj.addProperty("uploadTime", DateUtil.getTranTime(rpt.getCreateTime()));
                jObj.addProperty("name",user.getName());
                jObj.addProperty("cardNo",user.getIdCard());
                jObj.addProperty("sex",user.getSex());
                jObj.addProperty("age", DateUtil.getAgeByIDNumber(user.getIdCard()));
                jArray.add(jObj);
            }
            super.write(ResultTran.STATE_OK, "获取成功", jArray);
        }
        catch (Exception e)
        {
            logger.error("查询分析报告列表错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询分析报告列表错误。");
        }
    }
    /**
     * 上传血压报告给医生
     */
    public void pressReport()
    {
        try
        {
            JsonObject obj = super.getParams();
            String custId = GsonUtil.toString(obj.get("custId"));
            String id = GsonUtil.toString(obj.get("id"));
            List <PressurePulse> list = pressurePulseService.queryByCustId(custId,id);
            for (PressurePulse pressurePulse : list) {
            	if(pressurePulse.getType()==1){
            		pressurePulse.setType(2);
                	Result r = pressurePulseService.updatePre(pressurePulse);
            	}
			}
          
            super.write(ResultTran.STATE_OK,"上传成功");
        }
        catch (Exception e)
        {
            logger.error("上传错误。", e);
            super.write(ResultTran.STATE_ERROR, "上传错误。");
        }
    }
    
    /**
     * 客户已读
     */
    public void read(){
    	 try
         {
             JsonObject obj = super.getParams();
             String custId = GsonUtil.toString(obj.get("custId"));
             String id = GsonUtil.toString(obj.get("id"));
             Integer type = GsonUtil.toInteger(obj.get("type"));
             List <PressurePulse> list = pressurePulseService.queryByCustId(custId,id);
             for (PressurePulse pressurePulse : list) {
             	if(pressurePulse.getType()==3){
             		pressurePulse.setType(type);
                 	Result r = pressurePulseService.updatePre(pressurePulse);
             	}
 			}
           
             super.write(ResultTran.STATE_OK,"成功");
         }
         catch (Exception e)
         {
             logger.error("上传错误。", e);
             super.write(ResultTran.STATE_ERROR, "上传错误。");
         }
    }
}
