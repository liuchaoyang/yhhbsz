package com.yzxt.yh.module.chk.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.AnalysisUricAcid;
import com.yzxt.yh.module.chk.bean.BloodOxygen;
import com.yzxt.yh.module.chk.bean.BloodSugar;
import com.yzxt.yh.module.chk.bean.BodyFat;
import com.yzxt.yh.module.chk.bean.CheckData;
import com.yzxt.yh.module.chk.bean.PressurePulse;
import com.yzxt.yh.module.chk.bean.Pulse;
import com.yzxt.yh.module.chk.bean.Temperature;
import com.yzxt.yh.module.chk.bean.TotalCholesterol;
import com.yzxt.yh.module.chk.bean.UricAcid;
import com.yzxt.yh.module.chk.service.AnalysisUricAcidService;
import com.yzxt.yh.module.chk.service.BloodOxygenService;
import com.yzxt.yh.module.chk.service.BloodSugarService;
import com.yzxt.yh.module.chk.service.BodyFatService;
import com.yzxt.yh.module.chk.service.CheckDataService;
import com.yzxt.yh.module.chk.service.PressurePulseService;
import com.yzxt.yh.module.chk.service.PulseService;
import com.yzxt.yh.module.chk.service.TemperatureService;
import com.yzxt.yh.module.chk.service.TotalCholesterolService;
import com.yzxt.yh.module.chk.service.UricAcidService;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

public class CheckDataAction extends BaseAction
{
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(CheckDataAction.class);
    private CheckDataService checkDataService;
    private PulseService pulseService;
    private PressurePulseService pressurePulseService;
    private BloodSugarService bloodSugarService;
    private BloodOxygenService bloodOxygenService;
    private TemperatureService temperatureService;
    private TotalCholesterolService totalCholesterolService;
    private UricAcidService uricAcidService;
    private AnalysisUricAcidService analysisUricAcidService;
    private CustomerService customerService;
    private BodyFatService bodyFatService;

    public Logger getLogger()
    {
        return logger;
    }

    public void setLogger(Logger logger)
    {
        this.logger = logger;
    }

    public CheckDataService getCheckDataService()
    {
        return checkDataService;
    }

    public void setCheckDataService(CheckDataService checkDataService)
    {
        this.checkDataService = checkDataService;
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

    public CustomerService getCustomerService()
    {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    public BodyFatService getBodyFatService()
    {
        return bodyFatService;
    }

    public void setBodyFatService(BodyFatService bodyFatService)
    {
        this.bodyFatService = bodyFatService;
    }

    /**
     * 获取尿常规数据
     */
    public String getAnalysisUricAcid()
    {
        String id = request.getParameter("id");
        try
        {
            AnalysisUricAcid analysisUricAcid = analysisUricAcidService.get(id);
            Customer cust = customerService.load(analysisUricAcid.getCustId());
            request.setAttribute("analysisUricAcid", analysisUricAcid);
            request.setAttribute("cust", cust);
            return "analysisUricAcidView";
        }
        catch (Exception e)
        {
            logger.error("查看尿常规错误，数据ID：" + id, e);
            return null;
        }
    }

    /**
     * 查询所有检测项目
     * @return
     */
    public void query()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("startDate", request.getParameter("startDate"));
            filter.put("endDate", request.getParameter("endDate"));
            filter.put("keyword", StringUtil.trim(request.getParameter("keyword")));
            filter.put("itemType", request.getParameter("itemType"));
            filter.put("warnRec", request.getParameter("warnRec"));
            // 查询一体机检测记录
            filter.put("qt", request.getParameter("qt"));
            filter.put("sn", request.getParameter("sn"));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<CheckData> pageModel = checkDataService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询检测记录综合查询错误。", e);
        }
    }

    /**
     * 跳转到客户检测数据明细页面
     * @return
     */
    public String custCheckDetail()
    {
        User user = (User) super.getCurrentUser();
        String custId = request.getParameter("custId");
        if (StringUtil.isEmpty(custId) && Constant.USER_TYPE_CUSTOMER.equals(user.getType()))
        {
            custId = user.getId();
        }
        request.setAttribute("custId", custId);
        return "custCheckDetail";
    }

    /**
     * 查询血压记录
     */
    public void queryPrepulse()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("startDate", DateUtil.getDateFromHtml(request.getParameter("xyStartDate")));
            filter.put("endDate", DateUtil.getDateFromHtml(request.getParameter("xyEndDate")));
            filter.put("custId", request.getParameter("custId"));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<PressurePulse> pageModel = pressurePulseService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询血压错误。", e);
        }
    }

    /**
     * 查询血糖记录
     */
    public void queryBloodSugar()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("startDate", DateUtil.getDateFromHtml(request.getParameter("xtStartDate")));
            filter.put("endDate", DateUtil.getDateFromHtml(request.getParameter("xtEndDate")));
            filter.put("custId", request.getParameter("custId"));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<BloodSugar> pageModel = bloodSugarService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询血糖错误。", e);
        }
    }

    /**
     * 查询心率记录
     */
    public void queryPulse()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            String pulseTypeStr = request.getParameter("pulseType");
            if (StringUtil.isNotEmpty(pulseTypeStr))
            {
                filter.put("pulseType", Integer.valueOf(pulseTypeStr));
            }
            filter.put("startDate", DateUtil.getDateFromHtml(request.getParameter("xlStartDate")));
            filter.put("endDate", DateUtil.getDateFromHtml(request.getParameter("xlEndDate")));
            filter.put("custId", request.getParameter("custId"));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<Pulse> pageModel = pulseService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询心率错误。", e);
        }
    }

    /**
     * 查询血氧记录
     */
    public void queryBloodOxygen()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("startDate", DateUtil.getDateFromHtml(request.getParameter("xoStartDate")));
            filter.put("endDate", DateUtil.getDateFromHtml(request.getParameter("xoEndDate")));
            filter.put("custId", request.getParameter("custId"));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<BloodOxygen> pageModel = bloodOxygenService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询血氧错误。", e);
        }
    }

    /**
     * 查询体温记录
     */
    public void queryTemperature()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("startDate", DateUtil.getDateFromHtml(request.getParameter("twStartDate")));
            filter.put("endDate", DateUtil.getDateFromHtml(request.getParameter("twEndDate")));
            filter.put("custId", request.getParameter("custId"));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<Temperature> pageModel = temperatureService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询体温错误。", e);
        }
    }

    /**
     * 查询总胆固醇记录
     */
    public void queryTotalCholesterol()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("startDate", DateUtil.getDateFromHtml(request.getParameter("dgcStartDate")));
            filter.put("endDate", DateUtil.getDateFromHtml(request.getParameter("dgcEndDate")));
            filter.put("custId", request.getParameter("custId"));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<TotalCholesterol> pageModel = totalCholesterolService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询总胆固醇错误。", e);
        }
    }

    /**
     * 查询尿酸记录
     */
    public void queryUricAcid()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("startDate", DateUtil.getDateFromHtml(request.getParameter("nsStartDate")));
            filter.put("endDate", DateUtil.getDateFromHtml(request.getParameter("nsEndDate")));
            filter.put("custId", request.getParameter("custId"));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<UricAcid> pageModel = uricAcidService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询尿酸错误。", e);
        }
    }

    /**
     * 查询尿常规记录
     */
    public void queryAnaUA()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("startDate", DateUtil.getDateFromHtml(request.getParameter("nsfxStartDate")));
            filter.put("endDate", DateUtil.getDateFromHtml(request.getParameter("nsfxEndDate")));
            filter.put("custId", request.getParameter("custId"));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<AnalysisUricAcid> pageModel = analysisUricAcidService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询尿常规错误。", e);
        }
    }

    /**
     * 平台端分页查询体脂检测数据
     * @author h
     * @param filter 过滤条件
     * @param getPage() 页
     * @param getRows() 每页显示条数
     * 2015.8.10
     * @throws Exception 
     * @return
     * */
    public void queryBodyFat()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("startDate", DateUtil.getDateFromHtml(request.getParameter("tzStartDate")));
            filter.put("endDate", DateUtil.getDateFromHtml(request.getParameter("tzEndDate")));
            filter.put("custId", request.getParameter("custId"));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<BodyFat> pageModel = bodyFatService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询血糖错误。", e);
        }
    }
}
