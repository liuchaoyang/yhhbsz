package com.yzxt.yh.module.svb.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.svb.bean.MemberInfo;
import com.yzxt.yh.module.svb.service.MemberInfoService;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.util.StringUtil;

public class MemberInfoAction extends BaseAction
{
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(MemberInfoAction.class);

    private MemberInfoService memberInfoService;
    private CustomerService customerService;

    private MemberInfo memberInfo;

    public MemberInfoService getMemberInfoService()
    {
        return memberInfoService;
    }

    public void setMemberInfoService(MemberInfoService memberInfoService)
    {
        this.memberInfoService = memberInfoService;
    }

    public MemberInfo getMemberInfo()
    {
        return memberInfo;
    }

    public void setMemberInfo(MemberInfo memberInfo)
    {
        this.memberInfo = memberInfo;
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
     * 查询客户签约信息
     */
    public void queryCust()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("name", StringUtil.trim(request.getParameter("name")));
            filter.put("idCard", StringUtil.trim(request.getParameter("idCard")));
            filter.put("memberStatus", StringUtil.trim(request.getParameter("memberStatus")));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<Customer> pageModel = memberInfoService.queryCust(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询客户签约信息错误。", e);
        }
    }

    /**
     * 跳转到签约管理明细页面
     */
    public String toDetail()
    {
        try
        {
            String operType = request.getParameter("operType");
            if ("view".equals(operType) || "update".equals(operType))
            {
                String id = request.getParameter("id");
                memberInfo = memberInfoService.load(id);
            }
            else
            {
                String custId = request.getParameter("custId");
                memberInfo = new MemberInfo();
                memberInfo.setCustId(custId);
                memberInfo.setStartDay(new Date());
            }
            Customer cust = customerService.load(memberInfo.getCustId());
            memberInfo.setMemberName(cust.getUser().getName());
            memberInfo.setIdCard(cust.getUser().getIdCard());
            request.setAttribute("operType", operType);
            request.setAttribute("memberInfo", memberInfo);
            if ("view".equals(operType))
            {
                return "view";
            }
            else
            {
                return "edit";
            }
        }
        catch (Exception e)
        {
            logger.error("跳转至签约明细页面出错。", e);
            return "error";
        }
    }

    /**
     * 新增客户签约信息
     */
    public void add()
    {
        Result r = null;
        try
        {
            r = memberInfoService.add(memberInfo);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "签约错误。", null);
            logger.error("签约错误。", e);
        }
        super.write(r);
    }

    /**
     * 修改客户签约信息
     */
    public void update()
    {
        Result r = null;
        try
        {
            r = memberInfoService.update(memberInfo);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "修改签约错误。", null);
            logger.error("修改签约错误。", e);
        }
        super.write(r);
    }

    /**
     * 查询客户
     * @param cust
     */
    public void query()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            User operUser = (User) super.getCurrentUser();
            filter.put("operUser", operUser);
            filter.put("name", StringUtil.trim(request.getParameter("name")));
            filter.put("idCard", StringUtil.trim(request.getParameter("idCard")));
            String parameter = request.getParameter("memberStatus");
            String memberStatus = null;
            if (parameter != null && parameter != "")
            {
                if (parameter.equals("是"))
                {
                    memberStatus = "1";
                    filter.put("memberStatus", memberStatus);
                }
                else if (parameter.equals("否"))
                {
                    memberStatus = "2";
                    filter.put("memberStatus", memberStatus);

                }
            }
            PageModel<Customer> pageModel = memberInfoService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询客户错误。", e);
        }
    }

    /**
     * 跳转到查询客户能够选择的健康管理师
     * @return
     */
    public String toDocSel()
    {
        try
        {
            String custId = request.getParameter("custId");
            if (StringUtil.isNotEmpty(custId))
            {
                Customer cust = customerService.load(custId);
                request.setAttribute("orgId", cust != null ? cust.getUser().getOrgId() : null);
            }
        }
        catch (Exception e)
        {
            logger.error("跳转到查询客户能够选择的健康管理师错误。", e);
        }
        return "docSel";
    }

    /**
     * 查询客户能够选择的健康管理师
     */
    public void queryDocSel()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("name", StringUtil.trim(request.getParameter("name")));
            filter.put("orgId", request.getParameter("orgId"));
            PageModel<User> pageModel = memberInfoService.queryDocSel(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询客户能够选择的健康管理师错误。", e);
        }
    }

}
