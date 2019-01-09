package com.yzxt.yh.module.chk.action;

import java.util.HashMap;
import java.util.Map;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.CheckWarn;
import com.yzxt.yh.module.chk.service.CheckWarnService;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;
import common.Logger;

public class CheckWarnAction extends BaseAction
{

    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(CheckWarnAction.class);

    private CheckWarn checkWarn;

    private CheckWarnService checkWarnService;

    private CustomerService customerService;

    public CheckWarn getCheckWarn()
    {
        return checkWarn;
    }

    public void setCheckWarn(CheckWarn checkWarn)
    {
        this.checkWarn = checkWarn;
    }

    public CheckWarnService getCheckWarnService()
    {
        return checkWarnService;
    }

    public void setCheckWarnService(CheckWarnService checkWarnService)
    {
        this.checkWarnService = checkWarnService;
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
     * @author huangGang
     * 2015.4.10
     * 会员预警信息的合并展示
     * @return 
     * */
    public void warningList()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            String operType = request.getParameter("operType");
            if ("Q".equals(operType))
            {
                filter.put("custName", StringUtil.trim(request.getParameter("custName")));
                filter.put("haveNotDealWarn", StringUtil.trim(request.getParameter("haveNotDealWarn")));
                String beginDateStr = request.getParameter("beginDate");
                if (StringUtil.isNotEmpty(beginDateStr))
                {
                    filter.put("beginDate", DateUtil.getDateFromHtml(beginDateStr));
                }
                String endDateStr = request.getParameter("endDate");
                if (StringUtil.isNotEmpty(endDateStr))
                {
                    filter.put("endDate", DateUtil.getDateFromHtml(endDateStr));
                }
            }
            else
            {
                filter.put("haveNotDealWarn", "Y");
            }
            // 操作人
            filter.put("operUser", super.getCurrentUser());
            PageModel<CheckWarn> pageModel = checkWarnService.queryWarnCust(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("获取会员预警列表失败", e);
        }
    }

    /**
     * 跳转到客户预警明细页面
     * @author huangGang
     * 2015.4.10
     * @throws Exception 
     */
    public String toDetailWarn() throws Exception
    {
        try
        {
            String id = request.getParameter("id");
            Customer cust = customerService.load(id);
            /*checkWarn = checkWarnService.getPersonal(id);*/
            CheckWarn checkWarn = new CheckWarn();
            checkWarn.setCustId(cust.getUserId());
            checkWarn.setIdCard(cust.getUser().getIdCard());
            checkWarn.setName(cust.getUser().getName());
            request.setAttribute("checkWarn", checkWarn);
        }
        catch (Exception e)
        {
            logger.error("跳转至客户明细页面出错。", e);
            return "error";
        }
        return "detail";
    }

    /**
     * @author huangGang
     * 2015.4.10
     * 一个会员的全部预警列表
     * @return 
     * */
    public void queryList()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            String id = (String) request.getParameter("id");
            filter.put("custId", StringUtil.trim(id));
            String dealState = (String) request.getParameter("dealState");
            String warnLevel = (String) request.getParameter("warnLevel");
            String beginDate = getRequest().getParameter("beginDate");
            String endDate = getRequest().getParameter("endDate");
            filter.put("dealState", StringUtil.trim(dealState));
            filter.put("warnLevel", StringUtil.trim(warnLevel));
            filter.put("beginDate", StringUtil.trim(beginDate));
            filter.put("endDate", StringUtil.trim(endDate));
            PageModel<CheckWarn> pageModel = checkWarnService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("获取会员预警列表失败", e);
        }
    }

    /**
     * @author huangGang
     * 2015.4.11
     * 处理一个会员的预警
     * @return 
     * */
    public void doTask()
    {
        Result r = null;
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            String id = (String) request.getParameter("warnId");
            String custId = (String) request.getParameter("custId");
            CheckWarn checkWarn = new CheckWarn();
            checkWarn.setId(id);
            checkWarn.setState(Constant.WARNING_STATE_DEAL);
            /*filter.put("id", StringUtil.trim(id));*/
            filter.put("custId", StringUtil.trim(custId));
            /*  filter.put("dealState", StringUtil.trim(dealState));*/
            r = checkWarnService.updateState(filter, checkWarn);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "处理失败。", null);
            logger.error("处理失败", e);
        }
        super.write(r);
    }

    /**
     * 个人预警
     * @author huangGang
     * 2015.4.14
     * */
    public void myWarnsList() throws Exception
    {
        try
        {
            User CurOper = (User) super.getCurrentUser();
            String custId = CurOper.getId();
            Map<String, Object> filter = new HashMap<String, Object>();
            String warnLevel = (String) request.getParameter("warnLevel");
            String beginDate = getRequest().getParameter("beginDate");
            String endDate = getRequest().getParameter("endDate");
            filter.put("custId", StringUtil.trim(custId));
            filter.put("warnLevel", StringUtil.trim(warnLevel));
            filter.put("beginDate", StringUtil.trim(beginDate));
            filter.put("endDate", StringUtil.trim(endDate));
            PageModel<CheckWarn> pageModel = checkWarnService.queryMyWarns(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("获取列表失败", e);
        }
    }

    /**
     * 医生首页查询有未处理告警的用户。
     */
    public void queryDocHomeWarn()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("haveNotDealWarn", "Y");
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<CheckWarn> pageModel = checkWarnService.queryWarnCust(filter, 1, 10);
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询医生首页预警用户列表错误。", e);
        }
    }

}
