package com.yzxt.yh.module.ach.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.ach.bean.Emr;
import com.yzxt.yh.module.ach.service.EmrService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.UserService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

/**
 * 电子病历action类
 *
 */
public class EmrAction extends BaseAction
{
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(EmrAction.class);

    private EmrService emrService;
    private UserService userService;
    private Emr emr;

    public EmrService getEmrService()
    {
        return emrService;
    }

    public void setEmrService(EmrService emrService)
    {
        this.emrService = emrService;
    }

    public UserService getUserService()
    {
        return userService;
    }

    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    public Emr getEmr()
    {
        return emr;
    }

    public void setEmr(Emr emr)
    {
        this.emr = emr;
    }

    /**
     * 跳转的客户的电子病历列表
     * @return
     */
    public String toCustEmrs()
    {
        try
        {
            User operOper = (User) super.getCurrentUser();
            String custId = request.getParameter("custId");
            if (StringUtil.isEmpty(custId) && Constant.USER_TYPE_CUSTOMER.equals(operOper.getType()))
            {
                custId = operOper.getId();
            }
            User cust = userService.getUser(custId);
            request.setAttribute("custId", custId);
            request.setAttribute("custName", cust.getName());
            request.setAttribute("editable", Constant.USER_TYPE_DOCTOR.equals(operOper.getType()));
            return "custEmrs";
        }
        catch (Exception e)
        {
            logger.error("跳转到客户电子病历错误。", e);
        }
        return "error";
    }

    public void save()
    {
        Result r = null;
        try
        {
            // 当前操作人
            User curOper = (User) super.getCurrentUser();
            emr.setUpdateBy(curOper.getId());
            String id = emr.getId();
            if (StringUtil.isEmpty(id))
            {
                emr.setDoctorId(curOper.getId());
            }
            r = emrService.save(emr);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "保存电子病历错误。", null);
            logger.error("保存电子病历错误。", e);
        }
        super.write(r);
    }

    /**
     * 查询电子病历列表
     */
    public void queryCustEmr()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            filter.put("custId", request.getParameter("custId"));
            filter.put("startTreatDate", DateUtil.getDateFromHtml(request.getParameter("startTreatDate")));
            filter.put("endTreatDate", DateUtil.getDateFromHtml(request.getParameter("endTreatDate")));
            PageModel<Emr> pageModel = emrService.queryCustEmr(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询客户电子病历错误。", e);
        }
    }

    /**
     * 电子病历明细
     */
    public String toDetail()
    {
        try
        {
            String operType = request.getParameter("operType");
            Emr emr = null;
            if ("view".equals(operType) || "update".equals(operType))
            {
                String id = request.getParameter("id");
                emr = emrService.load(id);
                String doctorId = emr.getDoctorId();
                if (StringUtil.isNotEmpty(doctorId))
                {
                    User doctor = userService.getUser(doctorId);
                    emr.setDoctorName(doctor.getName());
                }
            }
            else
            {
                String custId = request.getParameter("custId");
                User cust = userService.getUser(custId);
                emr = new Emr();
                emr.setCustId(custId);
                emr.setCustName(cust.getName());
                emr.setTreatDate(new Date());
            }
            request.setAttribute("operType", operType);
            request.setAttribute("emr", emr);
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
            logger.error("查看电子病历错误。", e);
        }
        return "error";
    }

}
