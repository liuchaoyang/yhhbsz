package com.yzxt.yh.module.chr.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chr.bean.Visit;
import com.yzxt.yh.module.chr.service.ChrVisitService;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

import common.Logger;

public class ChrVisitAction extends BaseAction
{
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(ChrVisitAction.class);

    private Visit chrVisit;

    private ChrVisitService chrVisitService;

    private CustomerService customerService;

    public Visit getChrVisit()
    {
        return chrVisit;
    }

    public void setChrVisit(Visit chrVisit)
    {
        this.chrVisit = chrVisit;
    }

    public ChrVisitService getChrVisitService()
    {
        return chrVisitService;
    }

    public void setChrVisitService(ChrVisitService chrVisitService)
    {
        this.chrVisitService = chrVisitService;
    }

    public CustomerService getCustomerService()
    {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    /**
     * 增加随访
     * @author huangGang
     * 2015.4.25
     * */
    public void addVisitPlan()
    {
        Result r = null;
        try
        {
            User curOper = (User) super.getCurrentUser();
            chrVisit.setCreateBy(curOper.getId());
            chrVisit.setUpdateBy(curOper.getId());
            chrVisit.setDoctorId(curOper.getId());
            r = chrVisitService.addVisit(chrVisit);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "下达随访计划失败", null);
            logger.error("下达随访失败。", e);
        }
        super.write(r);
    }

    /**
     * 调整随访
     * @author h
     * 2015.7.17
     * */
    public void updateVisitPlan()
    {
        Result r = null;
        try
        {
            User curOper = (User) super.getCurrentUser();
            chrVisit.setUpdateBy(curOper.getId());
            chrVisit.setDoctorId(curOper.getId());
            r = chrVisitService.updateVisit(chrVisit);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "调整随访计划失败", null);
            logger.error("调整随访失败。", e);
        }
        super.write(r);
    }
    
    /**
     * 得到随访计划列表
     * @author huangGang
     * 2015.4.23
     * */
    public void getVisitList()
    {
        try
        {
            User curOper = (User) super.getCurrentUser();
            Map<String, Object> filter = new HashMap<String, Object>();
            String custId = request.getParameter("custId");
            if (StringUtil.isNotEmpty(custId) && !custId.equals("null"))
            {
                filter.put("custId", custId);
            }
            filter.put("memberName", request.getParameter("memberName"));
            filter.put("ishandled", request.getParameter("ishandled"));
            filter.put("curOper", curOper);
            PageModel<Visit> pageModel = chrVisitService.getVisitList(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("获取随访计划列表失败", e);
        }
    }

    /**
     * 跳转到处理血糖随访页面
     * */
    public String doVisit()
    {
        try
        {
            String id = request.getParameter("id");
            chrVisit = chrVisitService.getVisitById(id);
            Customer cust = customerService.load(chrVisit.getCustId());
            User user = cust.getUser();
            chrVisit.setMemberName(user.getName());
            chrVisit.setFlupDateStr(DateUtil.toHtmlDate(new Date()));
            request.setAttribute("chrVisit", chrVisit);
            request.setAttribute("opt", 1);
            if (chrVisit.getType() == 1)
            {
                return "xy";
            }
            else if (chrVisit.getType() == 2)
            {
                return "xt";
            }
            else
            {
                return "xnxg";
            }
        }
        catch (Exception e)
        {
            logger.error("跳转至处理随访明细页面出错。", e);
            return "error";
        }
    }

    /**
     * 跳转到处理血糖随访页面
     * */
    public String toMyVisit()
    {
        User curOper = (User) super.getCurrentUser();
        String custId = request.getParameter("custId");
        if (Constant.USER_TYPE_CUSTOMER.equals(curOper.getType()) && custId == null)
        {
            custId = curOper.getId();
        }
        request.setAttribute("custId", custId);
        return "myVisit";
    }
}
