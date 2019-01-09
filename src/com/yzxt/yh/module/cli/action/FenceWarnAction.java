package com.yzxt.yh.module.cli.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.cli.bean.FenceWarn;
import com.yzxt.yh.module.cli.service.FenceWarnService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

public class FenceWarnAction extends BaseAction
{
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(FenceWarnAction.class);

    private FenceWarnService fenceWarnService;

    public FenceWarnService getFenceWarnService()
    {
        return fenceWarnService;
    }

    public void setFenceWarnService(FenceWarnService fenceWarnService)
    {
        this.fenceWarnService = fenceWarnService;
    }

    /**
     * 查询围栏告警
     * @return
     */
    public void query()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            User operUser = (User) super.getCurrentUser();
            filter.put("custId", operUser.getId());
           /* String id = (String) request.getParameter("id");
            filter.put("custId", operUser));*/
            /*String dealState = (String) request.getParameter("dealState");
            String warnLevel = (String) request.getParameter("warnLevel");*/
            String beginDate = getRequest().getParameter("beginDate");
            String endDate = getRequest().getParameter("endDate");
            /*filter.put("dealState", StringUtil.trim(dealState));
            filter.put("warnLevel", StringUtil.trim(warnLevel));*/
            filter.put("beginDate", StringUtil.trim(beginDate));
            filter.put("endDate", StringUtil.trim(endDate));
            PageModel<FenceWarn> pageModel = fenceWarnService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询围栏告警异常。", e);
        }
    }

}
