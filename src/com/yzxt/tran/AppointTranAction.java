package com.yzxt.tran;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.module.his.bean.Appoint;
import com.yzxt.yh.module.his.bean.Dept;
import com.yzxt.yh.module.his.service.AppointService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.FamilyService;
import com.yzxt.yh.util.DateUtil;

public class AppointTranAction extends BaseTranAction
{
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(AppointTranAction.class);

    private Dept appointDept;
    private Appoint appoint;
    private AppointService appointService;
    private FamilyService familyService;

    public Dept getAppointDept()
    {
        return appointDept;
    }

    public void setAppointDept(Dept appointDept)
    {
        this.appointDept = appointDept;
    }

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

    public FamilyService getFamilyService()
    {
        return familyService;
    }

    public void setFamilyService(FamilyService familyService)
    {
        this.familyService = familyService;
    }

    /**
     * 客户端增加预约
     * @author h
     * 2016.1.22
     * */
    public void addAppoint()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject obj = super.getParams();
            User operUser = super.getOperUser();
            Appoint bean = new Appoint();
            bean = gson.fromJson(obj, Appoint.class);
            /*String custId = GsonUtil.toString(obj.get("custId"));
            String departtId = GsonUtil.toString(obj.get("departtId"));
            String deptId = GsonUtil.toString(obj.get("deptId"));
            String selfSymptom = GsonUtil.toString(obj.get("selfSymptom"));
            String firstVisit = GsonUtil.toString(obj.get("firstVisit"));
            String memo = GsonUtil.toString(obj.get("memo"));
            String appointTime = GsonUtil.toString(obj.get("appointTime"));
            Integer detailTime = GsonUtil.toInteger(obj.get("detailTime"));
            */
            bean.setCreateBy(operUser.getId());
            bean.setUpdateBy(operUser.getId());
            Result r = appointService.addAppoint(bean);
            if (r.getState() == 1)
            {
                super.write(ResultTran.STATE_OK, r.getMsg(), r.getData());
            }
            else
            {
                super.write(ResultTran.STATE_ERROR, r.getMsg(), r.getData());
            }
        }
        catch (Exception e)
        {
            logger.error("增加预约的时候发生服务器异常", e);
            super.write(ResultTran.STATE_ERROR, "增加预约的时候发生服务器异常", jObj);
        }
    }

    /**
     * 客户端查询预约列表
     * @author h
     * 2016.1.22
     * */
    public void appointList()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            Map<String, Object> filter = new HashMap<String, Object>();
            String custId = GsonUtil.toString(obj.get("userId"));
            filter.put("custId", custId);
            PageTran<Appoint> pageTran = appointService.queryTran(filter, synTime, synType, pageSize);
            if (pageTran != null)
            {
                super.write(ResultTran.STATE_OK, "预约列表查询成功", pageTran);
            }
            else
            {
                super.write(ResultTran.STATE_OK, "预约列表查询成功", jObj);
            }
        }
        catch (Exception e)
        {
            logger.error("客户端查询计步列表失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 客户端处理预约
     * @author h
     * 2016.1.22
     * */
    public void updateAppoint()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject obj = super.getParams();
            User operUser = super.getOperUser();
            Appoint bean = new Appoint();
            bean.setUpdateBy(operUser.getId());
            String appointId = GsonUtil.toString(obj.get("appointId"));
            String resultExplain = GsonUtil.toString(obj.get("resultExplain"));
            Integer status = GsonUtil.toInteger(obj.get("status"));
            bean.setId(appointId);
            bean.setResultExplain(resultExplain);
            bean.setStatus(status);
            Result r = appointService.updateAppoint(bean);
            if (r.getState() == 1)
            {
                super.write(ResultTran.STATE_OK, r.getMsg(), r.getData());
            }
            else
            {
                super.write(ResultTran.STATE_ERROR, r.getMsg(), r.getData());
            }
        }
        catch (Exception e)
        {
            logger.error("处理预约时出现服务器异常", e);
            super.write(ResultTran.STATE_ERROR, "处理预约时出现服务器异常", jObj);
        }
    }

    /**
     * 客户端查询科室列表
     * @author h
     * 2016.1.22
     * */
    public void queryDept()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            Map<String, Object> filter = new HashMap<String, Object>();
            PageTran<Dept> pageTran = appointService.queryDeptTran(filter, synTime, synType, pageSize);
            List<Dept> list = pageTran.getData();
            if (list != null)
            {
                JsonArray ja = new JsonArray();
                for (Dept dept : list)
                {
                    JsonObject jObjDept = new JsonObject();
                    jObjDept.addProperty("hospitalId", dept.getId());
                    jObjDept.addProperty("hospitalName", dept.getName());
                    jObjDept.addProperty("memo", dept.getMemo());
                    jObjDept.addProperty("uploadTime", DateUtil.getTranTime(dept.getCreateTime()));
                    ja.add(jObjDept);
                }
                super.write(ResultTran.STATE_OK, "查询成功", ja);
            }
            else
            {
                super.write(ResultTran.STATE_OK, "查询成功", jObj);
            }
        }
        catch (Exception e)
        {
            logger.error("客户端查询医院列表失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 客户端查询医院列表
     * @author h
     * 2016.1.22
     * */
    public void queryDepart()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            Map<String, Object> filter = new HashMap<String, Object>();
            String parentId = GsonUtil.toString(obj.get("hospitalId"));
            filter.put("parentId", parentId);
            PageTran<Dept> pageTran = appointService.queryDepartTran(filter, synTime, synType, pageSize);
            List<Dept> list = pageTran.getData();
            if (list != null)
            {
                JsonArray ja = new JsonArray();
                for (Dept dept : list)
                {
                    JsonObject jObjDepart = new JsonObject();
                    jObjDepart.addProperty("departmentId", dept.getId());
                    jObjDepart.addProperty("parentId", dept.getParentId());
                    jObjDepart.addProperty("departName", dept.getName());
                    jObjDepart.addProperty("level", dept.getLevel());
                    jObjDepart.addProperty("uploadTime", DateUtil.getTranTime(dept.getCreateTime()));
                    ja.add(jObjDepart);
                }
                super.write(ResultTran.STATE_OK, "查询成功", ja);
            }
            else
            {
                super.write(ResultTran.STATE_OK, "查询成功", jObj);
            }
        }
        catch (Exception e)
        {
            logger.error("客户端查询医院列表失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }
}
