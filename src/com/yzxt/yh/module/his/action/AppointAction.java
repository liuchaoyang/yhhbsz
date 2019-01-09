package com.yzxt.yh.module.his.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.his.bean.Appoint;
import com.yzxt.yh.module.his.bean.BaseArea;
import com.yzxt.yh.module.his.bean.Dept;
import com.yzxt.yh.module.his.service.AppointService;
import com.yzxt.yh.module.sys.bean.Doctor;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

import common.Logger;


/**
 * 
 */
public class AppointAction extends BaseAction
{

    private static final long serialVersionUID = 1L;
    
    private Logger logger  =  Logger.getLogger(AppointAction.class);
    
    private Appoint appoint;
    
    private BaseArea area;
    public BaseArea getArea() {
		return area;
	}

	public void setArea(BaseArea area) {
		this.area = area;
	}

	private AppointService appointService;

    public Appoint getAppoint()
    {
        return appoint;
    }

    public void setAppoint(Appoint appoint)
    {
        this.appoint = appoint;
    }

    public AppointService getAppointService()
    {
        return appointService;
    }

    public void setAppointService(AppointService appointService)
    {
        this.appointService = appointService;
    }
    
    /**
     * 增加预约功能
     * @author h
     * 2016.1.21
     * */
    public void addAppoint()
    {
        Result r = null;
        try
        {
         // 当前操作人
            User curOper = (User) super.getCurrentUser();
            appoint.setCreateBy(curOper.getId());
            appoint.setUpdateBy(curOper.getId());
            r = appointService.addAppoint(appoint);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "新增预约服务器异常。", null);
            logger.error("新增预约服务器异常。", e);
        }
        super.write(r);
    }
    
    /**
     * 查询预约挂号列表
     * @return
     * @author h
     * 2016.1.21
     */
    public void queryAppoint()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            //查询条件
            String beginDate = getRequest().getParameter("beginDate");
            String endDate = getRequest().getParameter("endDate");
            filter.put("beginDate", StringUtil.trim(beginDate));
            filter.put("endDate", StringUtil.trim(endDate));
            filter.put("custName", request.getParameter("custName"));
            filter.put("deptName", request.getParameter("deptName"));
            filter.put("status", request.getParameter("status"));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<Appoint> pageModel = appointService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("平台查询提醒记录出现异常", e);
        }
    }
    
    public String toAdd() throws Exception
    {
        Map<String,Object> filter = new HashMap<String,Object>();
        String parentId = request.getParameter("parentId");
        filter.put("parentId", parentId);
        List<Dept> depts = appointService.queryDept(filter);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("parentId", "0");
        List<BaseArea> areas = appointService.queryArea(map);
        User user = null;
       /* if (StringUtil.isNotEmpty(custId))
        {
            Customer cust = customerService.load(custId);
            user = cust.getUser();
        }
        else
        {

        }*/
        request.setAttribute("areas", areas);
        request.setAttribute("depts", depts);
        request.setAttribute("user", user);
        return "edit";
    }
    
    /**
     * 
     * 获取地区
     */
    public void listArea(){
    	 String parentId = request.getParameter("pId");
         List<BaseArea> areas = null;
         Map<String,Object> filter = new HashMap<String,Object>();
         if (StringUtil.isNotEmpty(parentId))
         {
             filter.put("parentId", parentId);
             areas = appointService.queryArea(filter);
             
         }
         JsonArray retJson = new JsonArray();
         if (areas != null && areas.size() > 0)
         {
             for (BaseArea area : areas)
             {
                 JsonObject jsonObj = new JsonObject();
                 jsonObj.addProperty("codeId", area.getCodeId());
                 jsonObj.addProperty("parentId", area.getParentId());
                 jsonObj.addProperty("cityName", area.getCityName());
                 retJson.add(jsonObj);
             }
         }
         super.write(retJson);
    }
    /**
     * 用户绑定选择设备后设备配置户
     * */
    public void listConfigByJson()
    {
        String parentId = request.getParameter("parentId");
        List<Dept> departs = null;
        Map<String,Object> filter = new HashMap<String,Object>();
        if (StringUtil.isNotEmpty(parentId))
        {
            filter.put("parentId", parentId);
            departs = appointService.queryDept(filter);
        }
        JsonArray retJson = new JsonArray();
        if (departs != null && departs.size() > 0)
        {
            for (Dept depart : departs)
            {
                JsonObject jsonObj = new JsonObject();
                jsonObj.addProperty("id", depart.getId());
                jsonObj.addProperty("configName", depart.getName());
                jsonObj.addProperty("configVal", depart.getParentId());
                retJson.add(jsonObj);
            }
        }
        super.write(retJson);
    }
    
    /**
     * 跳转到查看页面
     * */
    public String toCheck() throws Exception
    {
        Appoint appoint = new Appoint();
        try
        {
            String operType = request.getParameter("operType");
            if ("detail".equals(operType) || "update".equals(operType))
            {
                String id = request.getParameter("id");
                appoint = appointService.getAppointById(id);
            }
            else
            {
               
            }
            request.setAttribute("appoint", appoint);
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
     * 处理预约信息
     *@author huangGang
     * @return 
     * 2016.2.23
     * */
    public void update()
    {
        Result r = null;
        try
        {
            // 当前操作人
            User curOper = (User) super.getCurrentUser();
            appoint.setUpdateBy(curOper.getId());
            r = appointService.updateAppoint(appoint);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "保存预约结果错误。", null);
            logger.error("预约结果保存错误。", e);
        }
        super.write(r);
    }
    
    /**
     * 查询当前医生下的挂号人员列表
     * @return
     * @author h
     * 2016.1.21
     */
    public void queryCustmerName()
    {
    	try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            //查询条件
            filter.put("status", request.getParameter("status"));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            List <Appoint> appoint = appointService.queryCustmerName(filter);
            super.write(appoint);
        }
        catch (Exception e)
        {
            logger.error("平台查询提醒记录出现异常", e);
        }
    }
}
