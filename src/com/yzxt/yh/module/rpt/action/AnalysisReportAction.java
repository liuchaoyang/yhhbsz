package com.yzxt.yh.module.rpt.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.rpt.bean.AnalysisReport;
import com.yzxt.yh.module.rpt.service.AnalysisReportService;
import com.yzxt.yh.module.svb.bean.MemberInfo;
import com.yzxt.yh.module.svb.service.MemberInfoService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.sch.job.AnalysisReportJob;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

public class AnalysisReportAction extends BaseAction
{
    private Logger logger = Logger.getLogger(AnalysisReportAction.class);

    private static final long serialVersionUID = 1L;

    private AnalysisReportService analysisReportService;

    private MemberInfoService memberInfoService;

    public AnalysisReportService getAnalysisReportService()
    {
        return analysisReportService;
    }

    public void setAnalysisReportService(AnalysisReportService analysisReportService)
    {
        this.analysisReportService = analysisReportService;
    }

    public MemberInfoService getMemberInfoService()
    {
        return memberInfoService;
    }

    public void setMemberInfoService(MemberInfoService memberInfoService)
    {
        this.memberInfoService = memberInfoService;
    }

    /**
     * 查询分析报告
     */
    public void query()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            String custId = request.getParameter("custId");
            if (StringUtil.isNotEmpty(custId))
            {
                filter.put("custId", custId);
            }
            filter.put("custName", StringUtil.trim(request.getParameter("custName")));
            filter.put("idCard", StringUtil.trim(request.getParameter("idCard")));
            String reportTypeStr = request.getParameter("reportType");
            if (StringUtil.isNotEmpty(reportTypeStr))
            {
                filter.put("reportType", Integer.valueOf(reportTypeStr));
            }
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<AnalysisReport> pageModel = analysisReportService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询分析报告错误。", e);
        }
    }

    /**
     * 跳转到分析报告明细页面
     * @throws Exception 
     */
    public String toDetail() throws Exception
    {
        try
        {
            String id = request.getParameter("id");
            String operType = request.getParameter("operType");
            AnalysisReport report = analysisReportService.load(id);
            request.setAttribute("report", report);
            User operUser = (User) super.getCurrentUser();
            // 查看页面，会员健康管理师可以输入建议
            if ("view".equals(operType))
            {
                MemberInfo memberInfo = memberInfoService.getMemberInfoByCustId(report.getCustId());
                request.setAttribute("editable", memberInfo != null
                        && operUser.getId().equals(memberInfo.getDoctorId()));
            }
            Integer reportType = report.getReportType();
            if (Constant.ANALYSIS_REPORT_TYPE_BLOOD_PRESSURE.equals(reportType))
            {
                if ("view".equals(operType))
                {
                    return "bpAnaRptView";
                }
                else
                {
                    return "bpAnaRptPrint";
                }
            }
            else if (Constant.ANALYSIS_REPORT_TYPE_BLOOD_SUGAR.equals(reportType))
            {
                if ("view".equals(operType))
                {
                    return "bsAnaRptView";
                }
                else
                {
                    return "bsAnaRptPrint";
                }
            }
            return "error";
        }
        catch (Exception e)
        {
            logger.error("跳转至分析报告明细页面出错。", e);
            return "error";
        }
    }

    /**
     * 保存分析报告评估建议
     */
    public void saveSuggest()
    {
        Result r = null;
        try
        {
            AnalysisReport report = new AnalysisReport();
            report.setId(request.getParameter("id"));
            report.setSuggest(request.getParameter("suggest"));
            // 当前操作人
            User operUser = (User) super.getCurrentUser();
            report.setUpdateBy(operUser.getId());
            r = analysisReportService.saveSuggest(report);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "保存分析报告评估建议错误。", null);
            logger.error("保存分析报告评估建议错误。", e);
        }
        super.write(r);
    }

    /**
     * 跳转到客户报告列表页面
     */
    public String toCustRpt()
    {
        User curOper = (User) super.getCurrentUser();
        String custId = request.getParameter("custId");
        if (StringUtil.isEmpty(custId) && Constant.USER_TYPE_CUSTOMER.equals(curOper.getType()))
        {
            custId = curOper.getId();
        }
        request.setAttribute("custId", custId);
        return "custRpt";
    }

    public void gen()
    {
        AnalysisReportJob job = new AnalysisReportJob();
        Date now = new Date();
        try
        {
            job.genMuchWeek(DateUtil.addDay(now, -7), DateUtil.addDay(now, 7));
        }
        catch (Exception e)
        {
            logger.error("临时生成分析报告出错。", e);
        }
    }

}
