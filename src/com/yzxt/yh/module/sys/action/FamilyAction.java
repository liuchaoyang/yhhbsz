package com.yzxt.yh.module.sys.action;

import java.util.HashMap;
import java.util.Map;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.service.PhysiologIndexService;
import com.yzxt.yh.module.sys.bean.CustFamily;
import com.yzxt.yh.module.sys.bean.CustFamilyAudit;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.module.sys.service.FamilyService;
import com.yzxt.yh.module.sys.service.UserService;

import common.Logger;

/**
 * 平台家庭管理类
 */
public class FamilyAction extends BaseAction
{
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(FamilyAction.class);

    private CustFamilyAudit custFamilyAudit;

    private FamilyService familyService;

    private CustomerService customerService;

    private UserService userService;

    private PhysiologIndexService physiologIndexService;

    public CustFamilyAudit getCustFamilyAudit()
    {
        return custFamilyAudit;
    }

    public void setCustFamilyAudit(CustFamilyAudit custFamilyAudit)
    {
        this.custFamilyAudit = custFamilyAudit;
    }

    public FamilyService getFamilyService()
    {
        return familyService;
    }

    public void setFamilyService(FamilyService familyService)
    {
        this.familyService = familyService;
    }

    public CustomerService getCustomerService()
    {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    public UserService getUserService()
    {
        return userService;
    }

    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    public PhysiologIndexService getPhysiologIndexService()
    {
        return physiologIndexService;
    }

    public void setPhysiologIndexService(PhysiologIndexService physiologIndexService)
    {
        this.physiologIndexService = physiologIndexService;
    }

    /**
     * 申请添加家庭成员
     * 2015.7.7 
     * @author h
     * */
    public void addApply()
    {
        Result r = null;
        try
        {
            User operUser = (User) super.getCurrentUser();
            //判断电话号码是否有？是否是自己的。 判断是否已经是家庭成员
            User inviteUser = userService.getUserByPhone(custFamilyAudit.getMemberPhone());
            if (inviteUser != null)
            {
                custFamilyAudit.setCustId(operUser.getId());
                custFamilyAudit.setMemberId(inviteUser.getId());
                Map<String, Object> filter = new HashMap<String, Object>();
                filter.put("custId", operUser.getId());
                filter.put("memberId", inviteUser.getId());
                CustFamily custF = familyService.getCustFamily(filter);
                if (custF != null)
                {
                    r = new Result(Result.STATE_FAIL, "该成员已经是你的家庭成员了", null);
                }
                else
                {
                    r = familyService.addApply(custFamilyAudit);
                }
            }
            else
            {
                r = new Result(Result.STATE_FAIL, "此手机号码用户未注册，请注册", null);
            }
        }
        catch (Exception e)
        {
            logger.error("申请家庭成员错误", e);
        }
        super.write(r);
    }

    /**
     * 审核家庭成员：同意，拒绝
     * 2015.7.7 
     * @author h
     * */
    public void addAudit()
    {
        Result r = null;
        try
        {
            User operUser = (User) super.getCurrentUser();
            //插入二条数据，修改申请表的状态,此处只是同意
            CustFamily custf1 = new CustFamily();
            custf1.setCustId(custFamilyAudit.getMemberId());
            custf1.setMemberId(custFamilyAudit.getCustId());
            custf1.setMemo(custFamilyAudit.getApplyName());
            CustFamily custf2 = new CustFamily();
            custf2.setCustId(custFamilyAudit.getCustId());
            custf2.setMemberId(custFamilyAudit.getMemberId());
            custf2.setMemo(custFamilyAudit.getMemo());
            custFamilyAudit.setState(Constant.FAMILY_STATE_APPLY_SUCCESS);
            Result r1 = familyService.addFamily(custf1);
            Result r2 = familyService.addFamily(custf2);
            Result r3 = familyService.updateStateForAudit(custFamilyAudit);
            if (r1.getState() == 1 && r2.getState() == 1 && r3.getState() == 1)
            {
                r = new Result(Result.STATE_SUCESS, "审核通过", r3.getData());
            }
            else
            {
                r = new Result(Result.STATE_FAIL, "审核出错", r3.getData());
            }
        }
        catch (Exception e)
        {
            logger.error("申请家庭成员错误", e);
        }
        super.write(r);
    }

    /**
     * 审核家庭成员：同意，拒绝
     * 2015.7.7 
     * @author h
     * */
    public void updateAudit()
    {
        Result r = null;
        try
        {
            User operUser = (User) super.getCurrentUser();
            //插入二条数据，修改申请表的状态,此处只是拒绝
            custFamilyAudit.setState(Constant.FAMILY_STATE_APPLY_FAIL);
            r = familyService.updateStateForAudit(custFamilyAudit);
        }
        catch (Exception e)
        {
            logger.error("申请家庭成员错误", e);
        }
        super.write(r);
    }

    /**
     * 删除家庭成员
     * 2015.7.7 
     * @author h
     * */
    public void deleteMem()
    {
        Result r = null;
        try
        {
            User operUser = (User) super.getCurrentUser();
            String id = request.getParameter("id");
            CustFamily custf = familyService.get(id);
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("custId", custf.getCustId());
            filter.put("memberId", custf.getMemberId());
            CustFamily custF = familyService.getCustFamily(filter);
            String[] ids = new String[2];
            ids[0] = custF.getId();
            Map<String, Object> filter2 = new HashMap<String, Object>();
            filter2.put("custId", custf.getMemberId());
            filter2.put("memberId", custf.getCustId());
            CustFamily custF2 = familyService.getCustFamily(filter2);
            ids[1] = custF2.getId();
            for (String memId : ids)
            {
                r = familyService.deleteMem(memId);
            }
            if (r.getState() == 1)
            {
                r = new Result(Result.STATE_SUCESS, r.getMsg(), r.getData());
            }
            else
            {
                r = new Result(Result.STATE_FAIL, r.getMsg(), r.getData());
            }

        }
        catch (Exception e)
        {
            logger.error("申请家庭成员错误", e);
        }
        super.write(r);
    }

    /**
     * 查询家庭申请情况
     * 2015.7.8 
     * @author h
     * */
    public void queryApply()
    {
        try
        {
            User user = (User) super.getCurrentUser();
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("userId", user.getId());
            PageModel<CustFamilyAudit> pageModel = familyService.queryApply(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询错误", e);
        }
    }

    /**
     * 查询家庭申请情况
     * 2015.7.8 
     * @author h
     * */
    public void queryAudit()
    {
        try
        {
            User user = (User) super.getCurrentUser();
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("userId", user.getId());
            PageModel<CustFamilyAudit> pageModel = familyService.queryAudit(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询错误", e);
        }
    }

    /**
     * 查询家庭列表
     * 2015.7.8 
     * @author h
     * */
    public void queryFamily()
    {
        try
        {
            User user = (User) super.getCurrentUser();
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("userId", user.getId());
            PageModel<CustFamily> pageModel = familyService.queryFamily(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询错误", e);
        }
    }

}
