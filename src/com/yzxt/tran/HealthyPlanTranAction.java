package com.yzxt.tran;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.module.rgm.bean.HealthyPlan;
import com.yzxt.yh.module.rgm.service.HealthyPlanService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

/**
 * 自主健康管理客户端Action类
 * @author f
 *
 */
public class HealthyPlanTranAction extends BaseTranAction
{
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(HealthyPlanTranAction.class);

    private HealthyPlanService healthyPlanService;

   /* private ExamDataService examDataService;*/

    public HealthyPlanService getHealthyPlanService()
    {
        return healthyPlanService;
    }

    public void setHealthyPlanService(HealthyPlanService healthyPlanService)
    {
        this.healthyPlanService = healthyPlanService;
    }

  /*  public ExamDataService getExamDataService()
    {
        return examDataService;
    }

    public void setExamDataService(ExamDataService examDataService)
    {
        this.examDataService = examDataService;
    }*/
    
    /*
     * 添加健康计划
     * @author h
     * 2015.9.19
     * */
    public void addHealthPlan()
    {
        Result r = null;
        try{
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                return;
            }
            // 解析参数，添加健康计划
            User user = super.getOperUser();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            String healthyPlanId=null;
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String name= GsonUtil.toString(paramJson.get("title"));
            Integer type = GsonUtil.toInteger(paramJson.get("type"));
            String startDateStr = GsonUtil.toString(paramJson.get("startTime"))+" 00:00:00 000";
            Timestamp startDate = null;
            if(StringUtil.isNotEmpty(startDateStr)){
                startDate = DateUtil.getSynTimeByStr(startDateStr);
            }
            String endDateStr = GsonUtil.toString(paramJson.get("endTime"))+" 00:00:00 000";
            Timestamp endDate = null;
            if(StringUtil.isNotEmpty(endDateStr)){
                endDate = DateUtil.getSynTimeByStr(endDateStr);
            }
            String startData = GsonUtil.toString(paramJson.get("startData"));
            String endData = GsonUtil.toString(paramJson.get("endData"));
            if (StringUtil.isEmpty(name))
            {
                super.write(ResultTran.STATE_ERROR, "标题不能为空.", null);
                return;
            }
            HealthyPlan healthyPlan = new HealthyPlan();
            healthyPlan.setUserId(userId);
            healthyPlan.setName(name);
            healthyPlan.setType(type);
            healthyPlan.setStartDate(startDate);
            healthyPlan.setEndDate(endDate);
            healthyPlan.setState(2);
            healthyPlan.setTargetValue(startData);
            healthyPlan.setTargetValue2(endData);
            healthyPlan.setCreateTime(now);
            healthyPlan.setUpdateTime(now);
            if(StringUtil.isEmpty(healthyPlanId)){
                healthyPlan.setCreateBy(user.getId()); 
                healthyPlan.setUpdateBy(user.getId());
                r = healthyPlanService.add(healthyPlan);
                healthyPlanId = (String) r.getData();
            } else
            {
                healthyPlan.setUpdateBy(user.getId());
               /* healthyPlanService.update(healthyPlan);*/
            }
            JsonObject retJson = new JsonObject();
            retJson.addProperty("id", healthyPlanId);
            /*retJson.addProperty("time", DateUtil.toSynTimeStr(now));*/
            super.write(ResultTran.STATE_OK, "操作成功", retJson);
        }catch(Exception e){
            logger.error("客户端保存问题失败.", e);
            JsonArray json = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端异常", json);
        }
    }
    
    /*
     * 查询健康管理
     * @author h
     * 2015.9.19
     * */
    public void listPlans(){
        try{
            JsonObject paramJson = super.getParams();
            if(paramJson==null){
                return;
            }
            User user = super.getOperUser();
            /*int pageSize = paramJson.get("pageSize").getAsInt();*/
            Map<String, Object> filterMap = new HashMap<String, Object>();
            filterMap.put("user", user);
            /*filterMap.put("maxSize", pageSize);*/
            List<HealthyPlan> healthyPlans = healthyPlanService.queryHealthyPlansBySyn(filterMap);
            // 返回值
            JsonArray retJson = new JsonArray();
            if (healthyPlans != null && healthyPlans.size() > 0)
            {
                for (HealthyPlan healthyPlan : healthyPlans)
                {
                    JsonObject planJson = new JsonObject();
                    planJson.addProperty("id", healthyPlan.getId());
                    planJson.addProperty("userId", healthyPlan.getUserId());
                    planJson.addProperty("title", healthyPlan.getName());
                    planJson.addProperty("type", healthyPlan.getType());
                    planJson.addProperty("status", healthyPlan.getState());
                    String startTimeStr = null;
                    if (healthyPlan.getStartDate() != null)
                    {
                        startTimeStr = DateUtil.toSynTimeStr(new Timestamp(healthyPlan.getStartDate().getTime()));
                        startTimeStr = startTimeStr.substring(0,10);
                    }
                    planJson.addProperty("startTime", startTimeStr);
                    String endTimeStr = null;
                    if (healthyPlan.getEndDate() != null)
                    {
                        endTimeStr = DateUtil.toSynTimeStr(new Timestamp(healthyPlan.getEndDate().getTime()));
                        endTimeStr = endTimeStr.substring(0, 10);
                    }
                    planJson.addProperty("endTime", endTimeStr);
                    planJson.addProperty("startData", healthyPlan.getTargetValue());
                    planJson.addProperty("endData", healthyPlan.getTargetValue2());
                    retJson.add(planJson);
                }
            }
            super.write(ResultTran.STATE_OK, "操作成功", retJson);
            /*sendResponseData(ReturnTransInfo.STATEOK, ReturnTransInfo.STATEOKINFO, retJson);*/
        }catch(Exception e) {
            logger.error("客户端获取问题列表失败.", e);
            JsonArray json = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端异常", json);
            /*sendResponseData(ReturnTransInfo.STATEERR, ReturnTransInfo.STATEERRINFO, null);*/
        }
        
    }
    
    /* 
     *删除自主健康管理计划
     * @author h
     * 2015.9.19
     * 
     */
    public void deleteHealthyPlan(){
        try{
            JsonObject paramJson = super.getParams();
            if(paramJson==null){
                return;
            }
            String idsStr = GsonUtil.toString(paramJson.get("id"));
            String[] ids = StringUtil.splitWithoutEmpty(idsStr, ",");
            healthyPlanService.delete(ids);
            JsonArray retJson = new JsonArray();
            super.write(ResultTran.STATE_OK, "操作成功", retJson);
            /*sendResponseData(ReturnTransInfo.STATEOK, ReturnTransInfo.STATEOKINFO, retJson);*/
        }catch(Exception e){
            logger.error("客户端删除失败.", e);
            JsonArray json = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端异常", json);
            /*sendResponseData(ReturnTransInfo.STATEERR, ReturnTransInfo.STATEERRINFO, null);*/
        }
    }
    
    /* 
     *查看自主健康管理计划
     * @author huangGang
     * 2015.9.19
     * 
     */
    public void checkHealthPlan()
    {
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                return;
            }
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String idCard = GsonUtil.toString(paramJson.get("cardNo"));
            Integer pageSize = GsonUtil.toInteger(paramJson.get("pageSize"));
            Timestamp synTime = DateUtil.getSynTimeByStr(GsonUtil.toString(paramJson.get("lastSynTime"))
                    + " 00:00:00 000");
            String id = GsonUtil.toString(paramJson.get("planId"));
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("userId", userId);
            filter.put("synTime", synTime);
            filter.put("pageSize", pageSize);
            HealthyPlan healthyPlan = healthyPlanService.load(id);
            JsonArray retJson = new JsonArray();
            /*if(healthyPlan==null){
                exceptionRespData();
                return;
            }
            //健康计划的类型，1：血压，2：血糖，3：肥胖
            
            if (healthyPlan.getType() == 1)
            {
                List<String[]> datas = new ArrayList<String[]>();
                //收缩压maxsbp, minsbp, 舒张压maxdbp, mindbp,total,avgsbp,avgdbp
                List<Map<String, Object>> tableDatas = examDataService.getXyTableDataxxx(id, idCard);
                for (Map<String, Object> mapObj : tableDatas)
                {
                    String[] sbpInfo = new String[5];
                    sbpInfo[0] = "收缩压";
                    sbpInfo[1] = mapObj.get("total").toString();
                    sbpInfo[2] = mapObj.get("maxsbp").toString();
                    sbpInfo[3] = mapObj.get("minsbp").toString();
                    sbpInfo[4] = mapObj.get("avgsbp").toString();
                    datas.add(sbpInfo);

                    String[] dbpInfo = new String[5];
                    dbpInfo[0] = "舒张压";
                    dbpInfo[1] = mapObj.get("total").toString();
                    dbpInfo[2] = mapObj.get("maxdbp").toString();
                    dbpInfo[3] = mapObj.get("mindbp").toString();
                    dbpInfo[4] = mapObj.get("avgdbp").toString();
                    datas.add(dbpInfo);
                }
                List<?> linkDatas = examDataService.getXyReportLinkData(idCard,
                        CommonUtil.dateToString(healthyPlan.getStartDate(), "yyyy-MM-dd"),
                        CommonUtil.dateToString(healthyPlan.getEndDate(), "yyyy-MM-dd"), null);
                // 组装JSON
                String[] xAxis =
                {"统计时间", "类型", "测量次数", "最大值", "最小值", "平均值"};
                String[] yAxis =
                {"早间(06:00-09:59)", "白天(10:00-18:59)", "晚间(19:00-05:59)", "全天(00:00-23:59)"};
                JsonObject jsonObj = new JsonObject();
                jsonObj.add("xAxis", getGson().toJsonTree(xAxis));
                jsonObj.add("yAxis", getGson().toJsonTree(yAxis));
                jsonObj.add("datas", getGson().toJsonTree(datas));
                JsonArray linkJsonArray = new JsonArray();
                if (linkDatas != null && linkDatas.size() > 0)
                {
                    for (Object linkDataObj : linkDatas)
                    {
                        Object[] objs = (Object[]) linkDataObj;
                        JsonObject linkJson = new JsonObject();
                        linkJson.addProperty("dbp", objs[0] != null ? ((Double) objs[0]).intValue() : 0);
                        linkJson.addProperty("sbp", objs[1] != null ? ((Double) objs[1]).intValue() : 0);
                        String checkDate = (String) objs[2];
                        if (StringUtil.isNotEmpty(checkDate))
                        {
                            checkDate = checkDate.replace('-', '/');
                        }
                        linkJson.addProperty("checkTime", checkDate);
                        linkJsonArray.add(linkJson);
                    }
                }
                jsonObj.add("bpDatas", linkJsonArray);
                succussRespData(jsonObj);
            }
            else if (healthyPlan.getType() == 2)
            {

            }*/
            super.write(ResultTran.STATE_OK, "操作成功", retJson);
            /*sendResponseData(ReturnTransInfo.STATEOK, ReturnTransInfo.STATEOKINFO, retJson);*/
        }
        catch (Exception e)
        {
            logger.error("客户端删除失败.", e);
            JsonArray json = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端异常", json);
            /*sendResponseData(ReturnTransInfo.STATEERR, ReturnTransInfo.STATEERRINFO, null);*/
        }
    }
    
}
