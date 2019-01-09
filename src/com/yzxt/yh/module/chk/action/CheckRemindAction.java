package com.yzxt.yh.module.chk.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.chk.service.CheckRemindService;
import com.yzxt.yh.module.msg.bean.CheckRemind;
import com.yzxt.yh.util.StringUtil;

public class CheckRemindAction extends BaseAction
{
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(CheckRemindAction.class);
    private CheckRemindService checkRemindService;

    public CheckRemindService getCheckRemindService()
    {
        return checkRemindService;
    }

    public void setCheckRemindService(CheckRemindService checkRemindService)
    {
        this.checkRemindService = checkRemindService;
    }

    /**
     * 查询提醒会员列表
     * @return
     */
    public void queryRemindSet()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("custName", StringUtil.trim(request.getParameter("custName")));
            filter.put("idCard", StringUtil.trim(request.getParameter("idCard")));
            String minNoCheckDayStr = request.getParameter("minNoCheckDay");
            if (StringUtil.isNotEmpty(minNoCheckDayStr))
            {
                filter.put("minNoCheckDay", Integer.valueOf(minNoCheckDayStr));
            }
            String remindIntervalDayStr = request.getParameter("remindIntervalDay");
            if (StringUtil.isNotEmpty(remindIntervalDayStr))
            {
                filter.put("remindIntervalDay", Integer.valueOf(remindIntervalDayStr));
            }
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<CheckRemind> pageModel = checkRemindService.queryRemindSet(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询检测未做检测的会员记录错误。", e);
        }
    }

    /**
     * 保存检测提醒设置
     */
    public void saveSet()
    {
        Result r = null;
        try
        {
            CheckRemind checkRemind = new CheckRemind();
            String custIdsStr = request.getParameter("custIds");
            String remindIntervalDayStr = request.getParameter("intervalDay");
            checkRemind.setCustIds(StringUtil.splitWithoutEmpty(custIdsStr, ","));
            checkRemind.setRemindIntervalDay(Integer.valueOf(remindIntervalDayStr));
            r = checkRemindService.saveSet(checkRemind);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "保存错误。", null);
            logger.error("保存检测提醒错误。", e);
        }
        super.write(r);
    }

}
