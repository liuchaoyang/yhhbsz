package com.yzxt.yh.module.rpt.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.rpt.bean.CheckReport;
import com.yzxt.yh.module.rpt.service.CheckReportService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.FileUtil;

public class CheckReportAction extends BaseAction
{
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(CheckReportAction.class);

    private CheckReportService checkReportService;

    public CheckReportService getCheckReportService()
    {
        return checkReportService;
    }

    public void setCheckReportService(CheckReportService checkReportService)
    {
        this.checkReportService = checkReportService;
    }

    /**
     * 生成客户当时的身体指标生成体检报告图片
     */
    public void gen()
    {
        Result r = null;
        try
        {
            User operUser = (User) super.getCurrentUser();
            String custId = request.getParameter("custId");
            CheckReport checkReport = new CheckReport();
            checkReport.setCustId(custId);
            checkReport.setCreateBy(operUser.getId());
            r = checkReportService.doGen(checkReport);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "生成体检报告错误。", null);
            logger.error("生成体检报告错误。", e);
        }
        super.write(r);
    }

    /**
     * 平台查询体检报告
     * @author h
     * @param filter
     * 2015.8.19
     */
    public void query()
    {
        User operUser = (User) super.getCurrentUser();
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            String custName = (String) request.getParameter("custName");
            filter.put("custName", custName);
            String createTime = (String) request.getParameter("createTime");
            filter.put("createTime", createTime);
            filter.put("operUser", operUser);
            PageModel<CheckReport> pageModel = checkReportService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询体检列表失败", e);
        }
    }

    /**
     * 平台查看某人的体检报告的图片
     * @author h
     * @param filter
     * 2015.8.19
     */
    public String toDetail()
    {
        User operUser = (User) super.getCurrentUser();
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            String reportId = (String) request.getParameter("reportId");
            filter.put("reportId", reportId);
            filter.put("operUser", operUser);
            CheckReport checkReport = checkReportService.getReportPic(filter);
            checkReport.setReportFilePath(FileUtil.encodePath(checkReport.getReportFilePath()));
            request.setAttribute("checkReport", checkReport);
        }
        catch (Exception e)
        {
            logger.error("查看报告失败", e);
        }
        return "view";
    }
}
