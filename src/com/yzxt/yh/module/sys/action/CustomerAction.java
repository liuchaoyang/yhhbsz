package com.yzxt.yh.module.sys.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstDict;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.DictDetail;
import com.yzxt.yh.module.sys.bean.Org;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.module.sys.service.DictService;
import com.yzxt.yh.module.sys.service.OrgService;
import com.yzxt.yh.util.StringUtil;

public class CustomerAction extends BaseAction
{
    private Logger logger = Logger.getLogger(CustomerAction.class);

    private static final long serialVersionUID = 1L;
    private CustomerService customerService;
    private OrgService orgService;
    private DictService dictService;
    private Customer cust;
    private User user;

    public CustomerService getCustomerService()
    {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    public OrgService getOrgService()
    {
        return orgService;
    }

    public void setOrgService(OrgService orgService)
    {
        this.orgService = orgService;
    }

    public DictService getDictService()
    {
        return dictService;
    }

    public void setDictService(DictService dictService)
    {
        this.dictService = dictService;
    }

    public Customer getCust()
    {
        return cust;
    }

    public void setCust(Customer cust)
    {
        this.cust = cust;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    /**
     * 跳转到客户明细页面
     * @throws Exception 
     */
    public String toDetail() throws Exception
    {
        try
        {
            User operUser = (User) super.getCurrentUser();

            String operType = request.getParameter("operType");
            if ("view".equals(operType) || "update".equals(operType))
            {
                String id = request.getParameter("id");
                cust = customerService.load(id);
            }
            else
            {
                cust = new Customer();
                cust.setUser(new User());
                // 医生新增客户，客户与医生组织相同
                if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
                {
                    cust.getUser().setOrgId(operUser.getOrgId());
                }
            }
            String orgId = cust.getUser().getOrgId();
            if (StringUtil.isNotEmpty(orgId))
            {
                Org org = orgService.getOrg(orgId);
                cust.setOrgName(org.getName());
            }
            if ("view".equals(operType))
            {
                if (cust != null && StringUtil.isNotEmpty(cust.getNational()))
                {
                    DictDetail nationalDictDetail = dictService.getDictDetailByCode(ConstDict.NATIONAL,
                            cust.getNational());
                    cust.setNationalName(nationalDictDetail != null ? nationalDictDetail.getDictDetailName() : "");
                }
            }
            else
            {
                List<DictDetail> nationals = dictService.getDictDetails(ConstDict.NATIONAL);
                request.setAttribute("nationals", nationals);
            }
            request.setAttribute("operType", operType);
            request.setAttribute("cust", cust);
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
            logger.error("跳转至客户明细页面出错。", e);
            return "error";
        }
    }

    /**
     * 添加客户
     * @param cust
     */
    public void add()
    {
        Result r = null;
        try
        {
            // 当前操作人
            User curOper = (User) super.getCurrentUser();
            cust.setCreateBy(curOper.getId());
            cust.setOperUser(curOper);
            r = customerService.add(cust);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "新增客户错误。", null);
            logger.error("添加客户错误。", e);
        }
        super.write(r);
    }

    /**
     * 修改客户
     * @param cust
     */
    public void update()
    {
        Result r = null;
        try
        {
            // 当前操作人
            User curOper = (User) super.getCurrentUser();
            cust.setUpdateBy(curOper.getId());
            r = customerService.update(cust);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "修改客户错误。", null);
            logger.error("修改客户错误。", e);
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
            filter.put("name", StringUtil.trim(request.getParameter("name")));
            filter.put("idCard", StringUtil.trim(request.getParameter("idCard")));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<Customer> pageModel = customerService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询客户错误。", e);
        }
    }

    /**
     * 跳转到客户账户页面
     * @return
     * @throws Exception
     */
    public String toAccount() throws Exception
    {
        String forward = "edit".equals(request.getParameter("operType")) ? "accountEdit" : "accountView";
        User user = (User) super.getCurrentUser();
        String id = user.getId();
        Customer cust = customerService.load(id);
        request.setAttribute("cust", cust);
        request.setAttribute("operUser", user);
        return forward;
    }

    /**
     * 保存个人账户信息
     * @return
     * @throws Exception
     */
    public void saveAccount() throws Exception
    {
        Result r = null;
        try
        {
            User operUser = (User) super.getCurrentUser();
            cust.setUpdateBy(operUser.getId());
            r = customerService.saveAccount(cust);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "保存错误。", null);
            logger.error("保存客户账号信息错误。", e);
        }
        super.write(r);
    }

    /**
     * 查询会员
     * @param cust
     */
    public void memberList()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            String name = (String) request.getParameter("userName");
            String idCard = (String) request.getParameter("idCard");
            String healthyStatus = (String) request.getParameter("healthyState");
            filter.put("name", name != null ? name.trim() : null);
            filter.put("idCard", idCard != null ? idCard.trim() : null);
            filter.put("healthyStatus", StringUtil.isNotEmpty(healthyStatus) ? Integer.valueOf(healthyStatus) : null);
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<Customer> pageModel = customerService.queryMember(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {

        }
    }

    /**
     * 跳转到会员信息的查看页面
     * @throws Exception 
     */
    public String toPersonalDetail() throws Exception
    {
        try
        {
            String operType = request.getParameter("operType");
            if ("pd".equals(operType))
            {
                String id = request.getParameter("id");
                cust = customerService.load(id);
            }
            else
            {
                cust = new Customer();
                cust.setUser(new User());
            }
            request.setAttribute("operType", operType);
            request.setAttribute("cust", cust);
            if ("pd".equals(operType))
            {
                return "pd";
            }
            else
            {
                return "pd";
            }
        }
        catch (Exception e)
        {
            logger.error("跳转至客户明细页面出错。", e);
            return "error";
        }
    }

    /**
     * 查询客户
     * @param cust
     */
    public void queryFamily()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("name", StringUtil.trim(request.getParameter("name")));
            filter.put("idCard", StringUtil.trim(request.getParameter("idCard")));
            System.out.print(getPage());
            System.out.print(getRows());
            PageModel<Customer> pageModel = customerService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询客户错误。", e);
        }
    }

    /**
     * 跳转到客户明细页面
     * @throws Exception 
     */
    public String toDetailFamily() throws Exception
    {
        try
        {
            user = (User) super.getCurrentUser();
            cust = customerService.load(user.getId());

            request.setAttribute("user", user);
            request.setAttribute("cust", cust);
            return "detail";

        }
        catch (Exception e)
        {
            logger.error("跳转至客户明细页面出错。", e);
            return "error";
        }
    }

}
