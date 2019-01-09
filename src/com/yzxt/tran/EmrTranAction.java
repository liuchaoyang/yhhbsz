package com.yzxt.tran;

import java.util.Date;
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
import com.yzxt.yh.module.ach.bean.Emr;
import com.yzxt.yh.module.ach.service.EmrService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

public class EmrTranAction extends BaseTranAction
{
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(EmrTranAction.class);

    private EmrService emrService;

    public EmrService getEmrService()
    {
        return emrService;
    }

    public void setEmrService(EmrService emrService)
    {
        this.emrService = emrService;
    }

    /**
     * 添加电子病历
     */
    public void add()
    {
        try
        {
            JsonObject obj = super.getParams();
            User operUser = super.getOperUser();
            String custId = GsonUtil.toString(obj.get("custId"));
            Date treatDate = DateUtil.getFromTranDate(GsonUtil.toString(obj.get("treatDate")));
            String diagnosis = GsonUtil.toString(obj.get("diagnosis"));
            String test = GsonUtil.toString(obj.get("test"));
            String other = GsonUtil.toString(obj.get("other"));
            String doctorId = GsonUtil.toString(obj.get("doctorId"));
            Emr emr = new Emr();
            emr.setCustId(custId);
            emr.setTreatDate(treatDate);
            emr.setDiagnosis(diagnosis);
            emr.setTest(test);
            emr.setOther(other);
            emr.setUpdateBy(operUser.getId());
            emr.setDoctorId(StringUtil.isNotEmpty(doctorId) ? doctorId : operUser.getId());
            Result r = emrService.save(emr);
            if (r.getState() == Result.STATE_SUCESS)
            {
                super.write(ResultTran.STATE_OK, "保存成功");
            }
            else
            {
                super.write(ResultTran.STATE_ERROR, StringUtil.isNotEmpty(r.getMsg()) ? r.getMsg() : "保存失败");
            }
        }
        catch (Exception e)
        {
            logger.error("添加电子病历错误。", e);
            super.write(ResultTran.STATE_ERROR, "添加电子病历错误。");
        }
    }

    /**
     * 电子病历列表
     */
    public void list()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            Map<String, Object> filter = new HashMap<String, Object>();
            // 客户ID
            filter.put("operUser", super.getOperUser());
            filter.put("custId", GsonUtil.toString(obj.get("custId")));
            filter.put("type", GsonUtil.toInteger(obj.get("type")));
            PageTran<Emr> pageTran = emrService.queryTran(filter, synTime, pageSize);
            JsonArray ja = new JsonArray();
            List<Emr> list = pageTran.getData();
            if (list != null && !list.isEmpty())
            {
                for (Emr e : list)
                {
                    JsonObject jo = new JsonObject();
                    jo.addProperty("id", e.getId());
                    jo.addProperty("custId", e.getCustId());
                    jo.addProperty("doctorId", e.getDoctorId());
                    jo.addProperty("doctorName", e.getDoctorName());
                    jo.addProperty("treatDate", DateUtil.getTranDate(e.getTreatDate()));
                    jo.addProperty("diagnosis", e.getDiagnosis());
                    jo.addProperty("test", e.getTest());
                    jo.addProperty("other", e.getOther());
                    jo.addProperty("uploadTime", DateUtil.toSynTimeStr(e.getUpdateTime()));
                    ja.add(jo);
                }
            }
            super.write(ResultTran.STATE_OK, null, ja);
        }
        catch (Exception e)
        {
            logger.error("查询电子病历列表错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询电子病历列表错误。");
        }
    }

}
