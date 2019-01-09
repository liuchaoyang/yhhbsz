package com.yzxt.yh.module.sys.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.his.bean.BaseArea;
import com.yzxt.yh.module.sys.bean.Doctor;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.DoctorService;
import com.yzxt.yh.module.sys.service.UserService;

public class DoctorAction extends BaseAction
{
    private Logger logger = Logger.getLogger(DoctorAction.class);

    private static final long serialVersionUID = 1L;

    private Doctor doctor;

    private User user;

    private DoctorService doctorService;

    private UserService userService;

    public Doctor getDoctor()
    {
        return doctor;
    }

    public void setDoctor(Doctor doctor)
    {
        this.doctor = doctor;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public DoctorService getDoctorService()
    {
        return doctorService;
    }

    public void setDoctorService(DoctorService doctorService)
    {
        this.doctorService = doctorService;
    }

    public UserService getUserService()
    {
        return userService;
    }

    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    /**
     * 医生列表
     *@author huangGang
     * @return 
     * 2015.4.3
     * */
    public void list()
    {
        try
        {
            String name = request.getParameter("name");
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("name", name != null ? name.trim() : null);
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<Doctor> pageModel = doctorService.getList(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询医生错误。", e);
        }
    }
    
    /**
     * 根据科室id获取医生列表
     */
    public void deptDocList(){
    	 try
         {
             String deptId = request.getParameter("deptId");
             List<Doctor> doctors = null;
             Map<String, Object> filter = new HashMap<String, Object>();
             filter.put("deptId", deptId);
             // 查询人
             //filter.put("operUser", super.getCurrentUser());
             doctors = doctorService.deptDocList(filter);
             JsonArray retJson = new JsonArray();
             if (doctors != null && doctors.size() > 0)
             {
                 for (Doctor doc : doctors)
                 {
                     JsonObject jsonObj = new JsonObject();
                     jsonObj.addProperty("userId", doc.getUserId());
                     jsonObj.addProperty("doctorName",doc.getDoctorName());
                     retJson.add(jsonObj);
                 }
             }
             super.write(retJson);
         }
         catch (Exception e)
         {
             logger.error("查询医生错误。", e);
         }
    }
    /**
     * 增加医生
     *@author huangGang
     * @return 
     * 2015.4.3
     * */
    public void addDoctor()
    {
        Result r = null;
        try
        {
            // 当前操作人
            User curOper = (User) super.getCurrentUser();
            doctor.setCreateBy(curOper.getId());
            doctor.setUpdateBy(curOper.getId());
            r = doctorService.addDoctor(doctor);

        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "新增医生错误。", null);
            logger.error("添加医生错误。", e);
        }
        super.write(r);
    }

    /**
     * 修改医生信息
     *@author huangGang
     * @return 
     * 2015.4.3
     * */
    public void updateDoctor()
    {
        Result r = null;
        try
        {
            // 当前操作人
            User curOper = (User) super.getCurrentUser();
            doctor.setUpdateBy(curOper.getId());
            r = doctorService.update(doctor);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "回复保存失败。", null);
            logger.error("回复保存失败。", e);
        }
        super.write(r);
    }
}
