package com.yzxt.tran;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.module.chr.bean.Manage;
import com.yzxt.yh.module.chr.service.ChrManageService;
import com.yzxt.yh.util.DateUtil;
import common.Logger;

/*
 * 慢病接口：获取慢病列表
 * 2015.5.9
 * */

public class ChrManageTransAction extends BaseTranAction
{

    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(ChrManageTransAction.class);

    private Manage manage;

    private ChrManageService chrManageService;
    
    public Manage getManage()
    {
        return manage;
    }

    public void setManage(Manage manage)
    {
        this.manage = manage;
    }

    public ChrManageService getChrManageService()
    {
        return chrManageService;
    }

    public void setChrManageService(ChrManageService chrManageService)
    {
        this.chrManageService = chrManageService;
    }

    /**
     * 查询慢病人员列表
     * @return
     */
    public void mnglist()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            Map<String, Object> filter = new HashMap<String, Object>();
            JsonElement uObj = obj.get("userId");
            String userId = uObj.getAsString();
            filter.put("userId", userId);
            PageTran<Manage> pageTran = chrManageService.queryChrTran(filter, synTime, synType, pageSize);
            List<Manage> list = pageTran.getData();
            JsonArray ja = new JsonArray();
            for (Manage m : list)
            {
                JsonObject jObj = new JsonObject();
                jObj.addProperty("mngId", m.getId());
                jObj.addProperty("userName", m.getMemberName());
                jObj.addProperty("cardNo", m.getIdCard());
                jObj.addProperty("warningType", m.getType());
                jObj.addProperty("warningGrade", m.getGrade());
                jObj.addProperty("manageTime", DateUtil.getTranTime(m.getCreateTime()));
                jObj.addProperty("preFlupTime", DateUtil.getTranTime(m.getLastFlupTime()));
                jObj.addProperty("preFlupGrade", m.getPreFlupGrade());
                jObj.addProperty("nextFlupTime", DateUtil.getTranTime(m.getNextFlupTime()));
                jObj.addProperty("uploadTime", DateUtil.getTranTime(m.getCreateTime()));
                ja.add(jObj);
            }
            super.write(ResultTran.STATE_OK, null, ja);
        }
        catch (Exception e)
        {
            logger.error("查询慢病人员错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询慢病人员错误。");
        }
    }

    /**
     * 纳入慢病管理
     * @param type
     * @author h
     * @return
     * 2015.8.31
     */
    public void addChr()
    {
        JsonObject paramJson = super.getParams();
        try
        {
            JsonArray retJson = new JsonArray();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数为空", retJson);
                return;
            }
            String userId = (String) GsonUtil.toString(paramJson.get("userId"));
            String custId = (String) GsonUtil.toString(paramJson.get("custId"));
            String type = (String) GsonUtil.toString(paramJson.get("type"));
            Integer grade = (Integer) GsonUtil.toInteger(paramJson.get("grade"));
            String mngType[] = new String[1];
            mngType[0] = type;
            Manage chrMng = new Manage();
            chrMng.setCustId(custId);
            chrMng.setMngType(mngType);
            chrMng.setCreateBy(userId);
            chrMng.setGrade(grade);
            Result r = chrManageService.addChr(chrMng);
            if (r.getState() == 1)
            {
                super.write(ResultTran.STATE_OK, "纳入成功", r.getData());
            }
            else
            {
                super.write(ResultTran.STATE_ERROR, "纳入失败", r.getData());
            }
        }
        catch (Exception e)
        {
            logger.error("增加慢病人员错误。", e);
            JsonArray errJson = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端出现异常", errJson);
        }
    }
}
