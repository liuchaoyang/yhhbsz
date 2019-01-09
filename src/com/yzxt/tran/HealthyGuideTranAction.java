package com.yzxt.tran;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.module.msg.bean.HealthyGuide;
import com.yzxt.yh.module.msg.service.HealthyGuideService;
import com.yzxt.yh.util.DateUtil;

import common.Logger;

/**
 * 健康指导相关功能接口类
 * 2015.9.22
 * @author h
 * */
public class HealthyGuideTranAction extends BaseTranAction
{

    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(HealthyGuideTranAction.class);

    private HealthyGuideService healthyGuideService;

    public HealthyGuideService getHealthyGuideService()
    {
        return healthyGuideService;
    }

    public void setHealthyGuideService(HealthyGuideService healthyGuideService)
    {
        this.healthyGuideService = healthyGuideService;
    }

    /**
     * 增加健康指导
     * @author h
     * 2015.9.22
     * */
    public void addGuide()
    {
        Result r = null;
        try
        {
            JsonObject paramJson = super.getParams();
            JsonArray retJson = new JsonArray();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数错误", retJson);
            }
            else
            {
                String userId = GsonUtil.toString(paramJson.get("userId"));
                String custId = GsonUtil.toString(paramJson.get("custId"));
                String directReason = GsonUtil.toString(paramJson.get("directReason"));
                String sportDirect = GsonUtil.toString(paramJson.get("sportDirect"));
                String foodDirect = GsonUtil.toString(paramJson.get("foodDirect"));
                String drugSuggest = GsonUtil.toString(paramJson.get("drugSuggest"));
                String memo = GsonUtil.toString(paramJson.get("memo"));
                HealthyGuide healthyGuide = new HealthyGuide();
                healthyGuide.setCustId(custId);
                healthyGuide.setDirectReason(directReason);
                healthyGuide.setSportDirect(sportDirect);
                healthyGuide.setFoodDirect(foodDirect);
                healthyGuide.setDrugSuggest(drugSuggest);
                healthyGuide.setMemo(memo);
                healthyGuide.setCreateBy(userId);
                r = healthyGuideService.add(healthyGuide);
                super.write(String.valueOf(r.getState()), r.getMsg(), r.getData());
            }
        }
        catch (Exception e)
        {
            logger.error("服务器异常", e);
            JsonArray retJson = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务端异常", retJson);
        }
    }

    /**
     * 获取医生自己的指导列表
     * @author h
     * 2015.9.22
     * */
    public void acquireList()
    {
        try
        {
            /*User operUser = super.getOperUser();*/
            JsonObject paramJson = super.getParams();
            JsonArray retJson = new JsonArray();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客服端参数错误", retJson);
            }
            else
            {
                String userId = GsonUtil.toString(paramJson.get("userId"));
                initQuery(paramJson);
                Map<String, Object> filter = new HashMap<String, Object>();
                filter.put("userId", userId);
                PageTran<HealthyGuide> pageTran = healthyGuideService
                        .queryGuideTran(filter, synTime, synType, pageSize);
                List<HealthyGuide> list = pageTran.getData();
                for (HealthyGuide healthyGuide : list)
                {
                    JsonObject jObj = new JsonObject();
                    jObj.addProperty("id", healthyGuide.getId());
                    jObj.addProperty("custId", healthyGuide.getCustId());
                    jObj.addProperty("userName", healthyGuide.getMemberName());
                    jObj.addProperty("directReason", healthyGuide.getDirectReason());
                    jObj.addProperty("sportDirect", healthyGuide.getSportDirect());
                    jObj.addProperty("foodDirect", healthyGuide.getFoodDirect());
                    jObj.addProperty("drugSuggest", healthyGuide.getDrugSuggest());
                    jObj.addProperty("memo", healthyGuide.getMemo());
                    jObj.addProperty("uploadTime", DateUtil.getTranTime(healthyGuide.getCreateTime()));
                    retJson.add(jObj);
                }
                super.write(ResultTran.STATE_OK, "查询成功", retJson);
            }
        }
        catch (Exception e)
        {
            logger.error("服务器端异常", e);
            JsonArray retJson = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务端异常", retJson);
        }
    }

    /**
     * 医生修改自己的指导
     * @author h
     * 2015.9.22
     * */
    public void updateGuideTran()
    {

    }

    /**
     * 医生查看自己的指导
     * @author h
     * 2015.9.22
     * */
    public void checkGuideTran()
    {
        try
        {
            JsonObject paramJson = super.getParams();
            JsonArray retJson = new JsonArray();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数错误", retJson);
            }
            else
            {
                String guideId = GsonUtil.toString(paramJson.get("guideId"));
                HealthyGuide healthyGuide = healthyGuideService.getDetail(guideId);
                JsonObject jObj = new JsonObject();
                jObj.addProperty("id", healthyGuide.getId());
                jObj.addProperty("custId", healthyGuide.getCustId());
                jObj.addProperty("userName", healthyGuide.getMemberName());
                jObj.addProperty("directReason", healthyGuide.getDirectReason());
                jObj.addProperty("sportDirect", healthyGuide.getSportDirect());
                jObj.addProperty("foodDirect", healthyGuide.getFoodDirect());
                jObj.addProperty("drugSuggest", healthyGuide.getDrugSuggest());
                jObj.addProperty("memo", healthyGuide.getMemo());
                jObj.addProperty("createTime", DateUtil.getTranTime(healthyGuide.getCreateTime()));
                retJson.add(jObj);
                super.write(ResultTran.STATE_OK, "获取数据成功", retJson);
            }
        }
        catch (Exception e)
        {
            logger.error("服务器错误", e);
            JsonArray retJson = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务端异常", retJson);
        }
    }
    /**
     * 
     * 查看医生建议
     */
    public void lookSuggest()
    {
        try
        {
            JsonObject paramJson = super.getParams();
            JsonArray retJson = new JsonArray();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数错误", retJson);
            }
            else
            {
                String custId = GsonUtil.toString(paramJson.get("custId"));
                String id = GsonUtil.toString(paramJson.get("id"));
                HealthyGuide healthyGuide = healthyGuideService.lookSuggest(custId,id);
                if(healthyGuide!=null){
                	 JsonObject jObj = new JsonObject();
                     jObj.addProperty("custId", healthyGuide.getCustId());
                     jObj.addProperty("userName", healthyGuide.getMemberName());
                     jObj.addProperty("directReason", healthyGuide.getDirectReason());
                     jObj.addProperty("sportDirect", healthyGuide.getSportDirect());
                     jObj.addProperty("foodDirect", healthyGuide.getFoodDirect());
                     jObj.addProperty("drugSuggest", healthyGuide.getDrugSuggest());
                     //jObj.addProperty("memo", healthyGuide.getMemo());
                     jObj.addProperty("createTime", DateUtil.getTranTime(healthyGuide.getCreateTime()));
                     retJson.add(jObj);
                     super.write(ResultTran.STATE_OK, "获取数据成功", retJson);
                }else{
                	 super.write(ResultTran.STATE_ERROR, "医生未回复", null);
                }
            }
        }
        catch (Exception e)
        {
            logger.error("服务器错误", e);
            super.write(ResultTran.STATE_ERROR, "服务器异常", null);
        }
    }
}
