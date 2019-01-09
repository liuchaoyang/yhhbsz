package com.yzxt.yh.module.cli.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.server.Config;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.cli.bean.CustPosition;
import com.yzxt.yh.module.cli.bean.Remind;
import com.yzxt.yh.module.cli.bean.Sleep;
import com.yzxt.yh.module.cli.bean.SleepQuality;
import com.yzxt.yh.module.cli.bean.Step;
import com.yzxt.yh.module.cli.service.CustPositionService;
import com.yzxt.yh.module.cli.service.RemindService;
import com.yzxt.yh.module.cli.service.SleepQualityService;
import com.yzxt.yh.module.cli.service.SleepService;
import com.yzxt.yh.module.cli.service.StepService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.UserService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

public class CustLifeAction extends BaseAction
{
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(CustLifeAction.class);

    private UserService userService;
    private StepService stepService;
    private SleepService sleepService;
    private SleepQualityService sleepQualityService;
    private RemindService remindService;
    private CustPositionService custPositionService;

    public UserService getUserService()
    {
        return userService;
    }

    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

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

    public CustPositionService getCustPositionService()
    {
        return custPositionService;
    }

    public void setCustPositionService(CustPositionService custPositionService)
    {
        this.custPositionService = custPositionService;
    }

    /**
     * 跳转到客户生活信息明细页面
     * @return
     */
    public String toDetail()
    {
        User user = (User) super.getCurrentUser();
        try
        {
            String custId = request.getParameter("custId");
            if (StringUtil.isEmpty(custId) && Constant.USER_TYPE_CUSTOMER.equals(user.getType()))
            {
                custId = user.getId();
                request.setAttribute("custName", user.getName());
            }
            else
            {
                User custUser = userService.getUser(custId);
                request.setAttribute("custName", custUser.getName());
            }
            String mapWebBaseUrl = Config.getInstance().getString("map.web.base.url");
            request.setAttribute("mapWebBaseUrl", mapWebBaseUrl);
            request.setAttribute("custId", custId);
        }
        catch (Exception e)
        {
            logger.error("跳转到生活信息明细出错。", e);
        }
        return "detail";
    }

    /**
     * 平台查询计步记录
     * @author h
     * 2015.11.6
     */
    public void queryStep()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("startDate", DateUtil.getDateFromHtml(request.getParameter("jbStartDate")));
            filter.put("endDate", DateUtil.getDateFromHtml(request.getParameter("jbEndDate")));
            filter.put("custId", request.getParameter("custId"));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<Step> pageModel = stepService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("平台查询计步出现异常。", e);
        }
    }

    /**
     * 平台查询睡眠记录
     * @author h
     * 2015.11.6
     */
    public void querySleep()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("startDate", DateUtil.getDateFromHtml(request.getParameter("smStartDate")));
            filter.put("endDate", DateUtil.getDateFromHtml(request.getParameter("smEndDate")));
            filter.put("custId", request.getParameter("custId"));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<Sleep> pageModel = sleepService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("平台查询睡眠出现异常。", e);
        }
    }

    /**
     * 查询提醒数据记录列表
     * @return
     * @author h
     * 2015.11.6
     */
    public void queryRemind()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("startDate", DateUtil.getDateFromHtml(request.getParameter("txStartDate")));
            filter.put("endDate", DateUtil.getDateFromHtml(request.getParameter("txEndDate")));
            filter.put("custId", request.getParameter("custId"));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<Remind> pageModel = remindService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("平台查询提醒记录出现异常", e);
        }
    }

    /**
     * 查询睡眠质量数据记录列表
     * @return
     * @author h
     * 2015.11.6
     */
    public void querySleepQuality()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("startDate", DateUtil.getDateFromHtml(request.getParameter("smzlStartDate")));
            filter.put("endDate", DateUtil.getDateFromHtml(request.getParameter("smzlEndDate")));
            filter.put("custId", request.getParameter("custId"));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<SleepQuality> pageModel = sleepQualityService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("平台查询睡眠质量记录出现异常", e);
        }
    }

    /**
     * 查询定位记录列表
     * @return
     * @author h
     * 2015.11.10
     */
    public void queryPosition()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("positionMode", request.getParameter("positionMode"));
            filter.put("startDate", DateUtil.getDateFromHtml(request.getParameter("dwStartDate")));
            filter.put("endDate", DateUtil.getDateFromHtml(request.getParameter("dwEndDate")));
            filter.put("custId", request.getParameter("custId"));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<CustPosition> pageModel = custPositionService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("平台查询定位记录出现服务器异常", e);
        }
    }

}
