package com.yzxt.yh.module.msg.action;

import java.util.HashMap;
import java.util.Map;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.msg.bean.ConsultGuide;
import com.yzxt.yh.module.msg.service.ConsultGuideService;
import com.yzxt.yh.module.svb.bean.MemberInfo;
import com.yzxt.yh.module.svb.service.MemberInfoService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

import common.Logger;

public class ConsultGuideAction extends BaseAction
{

    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(ConsultGuideAction.class);

    private ConsultGuide consultGuide;

    private MemberInfoService memberInfoService;

    private ConsultGuideService consultGuideService;

    public ConsultGuide getConsultGuide()
    {
        return consultGuide;
    }

    public void setConsultGuide(ConsultGuide consultGuide)
    {
        this.consultGuide = consultGuide;
    }

    public MemberInfoService getMemberInfoService()
    {
        return memberInfoService;
    }

    public void setMemberInfoService(MemberInfoService memberInfoService)
    {
        this.memberInfoService = memberInfoService;
    }

    public ConsultGuideService getConsultGuideService()
    {
        return consultGuideService;
    }

    public void setConsultGuideService(ConsultGuideService consultGuideService)
    {
        this.consultGuideService = consultGuideService;
    }

    /**
     * @author huangGang
     * 2015.4.21
     * */
    public void add()
    {
        Result r = null;
        try
        {
            // 当前操作人
            User curOper = (User) super.getCurrentUser();
            consultGuide.setCustId(curOper.getId());
            ConsultGuide consult = new ConsultGuide();
            consult.setConsultContext(consultGuide.getConsultContext());
            consult.setConsultTitle(consultGuide.getConsultTitle());
            consult.setCustId(curOper.getId());
            consult.setDoctorId(consultGuide.getDoctorId());
            consult.setDoctorName(consultGuide.getDoctorName());
            r = consultGuideService.add(consult);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "新增咨询错误。", null);
            logger.error("添加咨询错误。", e);
        }
        super.write(r);
    }

    /**
     * 修改医生信息
     *@author huangGang
     * @return 
     * 2015.4.3
     * */
    public void updateReply()
    {
        Result r = null;
        try
        {
            // 当前操作人
            User curOper = (User) super.getCurrentUser();
            /* consultGuide.setUpdateBy(curOper.getId());*/
            consultGuide.setDoctorId(curOper.getId());
            r = consultGuideService.update(consultGuide);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "回复咨询保存错误。", null);
            logger.error("回复咨询保存错误。", e);
        }
        super.write(r);
    }

    /**
     * 医生获取咨询自己的咨询列表
     * @author huangGang
     * 2015.4.21
     * @param
     * */
    public void getconsultList()
    {
        try
        {
            User curOper = (User) super.getCurrentUser();
            Map<String, Object> filter = new HashMap<String, Object>();
            String consultTitle = request.getParameter("consultTitle");
            String consultContext = request.getParameter("consultContext");
            String MemberName = request.getParameter("memberName");
            String consultState = request.getParameter("consultState");
            filter.put("curOper", curOper);
            filter.put("consultTitle", StringUtil.trim(consultTitle));
            filter.put("consultContext", StringUtil.trim(consultContext));
            filter.put("MemberName", StringUtil.trim(MemberName));
            filter.put("consultState", StringUtil.trim(consultState));
            PageModel<ConsultGuide> pageModel = consultGuideService.getConsultList(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("获取咨询列表失败", e);
        }
    }

    /**
     * 跳转到查看页面
     * */
    public String toCheck() throws Exception
    {
        try
        {
            String operType = request.getParameter("operType");
            if ("detail".equals(operType) || "update".equals(operType))
            {
                String id = request.getParameter("id");
                consultGuide = consultGuideService.getDetail(id);
            }
            else
            {
                consultGuide = new ConsultGuide();
            }
            request.setAttribute("consultGuide", consultGuide);
            if ("detail".equals(operType))
            {
                return "detail";
            }
            else
            {
                return "update";
            }
        }
        catch (Exception e)
        {
            logger.error("跳转至客户明细页面出错。", e);
            return "error";
        }

    }

    /**
     * 跳转至新增页面
     * */
    public String toAdd() throws Exception
    {
        try
        {
            User curOper = (User) super.getCurrentUser();
            String id = request.getParameter("id");
            MemberInfo memberInfo = memberInfoService.getMemberInfo(curOper.getId());
            /*consultGuide = consultGuideService.getDetail(id);*/
            if (memberInfo != null)
            {
                request.setAttribute("memberInfo", memberInfo);
            }
            else
            {
                memberInfo = new MemberInfo();
                request.setAttribute("memberInfo", memberInfo);
            }
        }
        catch (Exception e)
        {
            logger.error("跳转至新增咨询页面出错。", e);
            return "error";
        }
        return "edit";
    }

    /**
     * 跳转至我的咨询页面
     * */
    public String toMyConsult() throws Exception
    {
        try
        {
            User curOper = (User) super.getCurrentUser();
            MemberInfo memberInfo = memberInfoService.getMemberInfo(curOper.getId());
            if (memberInfo != null)
            {
                request.setAttribute("memberInfo", memberInfo);
            }
            else
            {
                memberInfo = new MemberInfo();
                request.setAttribute("memberInfo", memberInfo);
            }
        }
        catch (Exception e)
        {
            logger.error("跳转至我的咨询页面出错。", e);
            return "error";
        }
        return "myConsult";
    }
}
