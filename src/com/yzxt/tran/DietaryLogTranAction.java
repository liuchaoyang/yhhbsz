package com.yzxt.tran;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.module.rgm.bean.DietaryLog;
import com.yzxt.yh.module.rgm.bean.Food;
import com.yzxt.yh.module.rgm.bean.FoodCatalog;
import com.yzxt.yh.module.rgm.service.DietaryLogService;
import com.yzxt.yh.module.rgm.service.FoodCatalogService;
import com.yzxt.yh.module.rgm.service.FoodService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

/**
 * 健康日志膳食客户端接口类
 * @author h
 *
 */
public class DietaryLogTranAction extends BaseTranAction
{
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(DietaryLogTranAction.class);
    private DietaryLog dietaryLog;
    private DietaryLogService dietaryLogService;
    private FoodService foodService;
    private FoodCatalog foodCatalog;
    private FoodCatalogService foodCatalogService;
    
    
    public FoodCatalogService getFoodCatalogService()
    {
        return foodCatalogService;
    }

    public void setFoodCatalogService(FoodCatalogService foodCatalogService)
    {
        this.foodCatalogService = foodCatalogService;
    }

    public DietaryLog getDietaryLog()
    {
        return dietaryLog;
    }

    public void setDietaryLog(DietaryLog dietaryLog)
    {
        this.dietaryLog = dietaryLog;
    }

    public DietaryLogService getDietaryLogService()
    {
        return dietaryLogService;
    }

    public void setDietaryLogService(DietaryLogService dietaryLogService)
    {
        this.dietaryLogService = dietaryLogService;
    }

    public FoodService getFoodService()
    {
        return foodService;
    }

    public void setFoodService(FoodService foodService)
    {
        this.foodService = foodService;
    }
    
    public FoodCatalog getFoodCatalog()
    {
        return foodCatalog;
    }

    public void setFoodCatalog(FoodCatalog foodCatalog)
    {
        this.foodCatalog = foodCatalog;
    }
    
    /**
     * 添加膳食日志
     * @author huangGang
     * update 2014.11.17
     */
    public void addFoodLog() throws Exception
    {
        try
        {
            JsonObject paramJson = getParams();
            if (paramJson == null)
            {
                return;
            }
            User user = (User) super.getOperUser();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            JsonElement data = paramJson.getAsJsonArray("datas");
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            JsonArray datas = data.getAsJsonArray();
            JsonObject retJson = new JsonObject();
            JsonArray json = new JsonArray();
            String ids[]= new String[datas.size()];
            for (int i = 0; i < datas.size(); i++)
            {
                JsonObject paramJsons = datas.get(i).getAsJsonObject();
                //解析参数，添加膳食日志
                Integer dietaryType = GsonUtil.toInteger(paramJsons.get("mealType"));
                Integer foodWeight = GsonUtil.toInteger(paramJsons.get("intakeAmount"));
                String foodId = GsonUtil.toString(paramJsons.get("intakeFoodId"));
                String intakeTime = GsonUtil.toString(paramJsons.get("intakeTime"))+" 00:00:00 000";
                Timestamp dietaryTime = DateUtil.getSynTimeByStr(intakeTime);
                //根据膳食名称从数据库中获取膳食id
                //根据膳食id,查询出食物目录
                /*Food food = foodService.get(foodId);
                FoodCatalog foodCatalog = foodCatalogService*/
                DietaryLog dietaryLog = new DietaryLog();
                dietaryLog.setDietaryType(dietaryType);
                dietaryLog.setDietaryTime(dietaryTime);
                dietaryLog.setFoodId(foodId);
                dietaryLog.setUserId(userId);
                dietaryLog.setFoodWeight((double) foodWeight);
                if(StringUtil.isNotEmpty(custId))
                {
                    dietaryLog.setUserId(custId);
                }
                else
                {
                    dietaryLog.setUserId(userId);
                    
                }
                dietaryLog.setCreateBy(user.getId());
                dietaryLog.setCreateTime(now);
                dietaryLog.setUpdateBy(user.getId());
                dietaryLog.setUpdateTime(now);
                Result r = dietaryLogService.addDietaryLog(dietaryLog);
                String id = (String) r.getData();
                
                ids[i]= id;
                JsonObject idJson = new JsonObject();
                idJson.addProperty("foodLogId", ids[i]);
                json.add(idJson);
                String startTimeStr = null;
                if (dietaryLog.getDietaryTime() != null)
                {
                    startTimeStr = DateUtil.toSynTimeStr(new Timestamp(dietaryLog.getDietaryTime().getTime()));
                    startTimeStr = startTimeStr.replace('-', '/');
                    startTimeStr = startTimeStr.substring(0, 10);
                }
                retJson.addProperty("intakeTime", startTimeStr);
            }
            //返回参数  首先是插入膳食日志的id数组
            retJson.add("datas", json);        
            //返回参数 其次是总的参数  查询插入膳食日志后所膳食时间的总能量
            Map<String, Object> filter = new HashMap<String, Object>();
            if(StringUtil.isNotEmpty(custId))
            {
                filter.put("createBy", custId);
            }
            else
            {
                filter.put("createBy", userId);
                
            }
            filter.put("sysTime", DateUtil.getSynTimeByStr(GsonUtil.toString(datas.get(0).getAsJsonObject().get("intakeTime"))+" 00:00:00 000"));
            DietaryLog dayLog = dietaryLogService.getLogByDay(filter);
            if (dayLog != null)
            {
                Double dayIntakeEnergy = dayLog.getIntakeEnergy();
                retJson.addProperty("dayIntakeEnergy", (dayIntakeEnergy).intValue());
            }
            retJson.addProperty("createTime", DateUtil.toSynTimeStr(now));
            super.write(ResultTran.STATE_OK, "", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端保存问题失败.", e);
            JsonArray json = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端异常", json);
        }
    }

    /**
     * 膳食日志列表
     * @author huangGang
     * update 2014.11.17
     * 客户端查询
     */
    public void listFoodLog() throws Exception
    {
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                return;
            }
            User user = (User) super.getOperUser();
            int pageSize = paramJson.get("pageSize").getAsInt();
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            /* String lastSynTime = getLastSynTime(paramJson.get("lastSynTime"));
             Timestamp synTime=new Timestamp(CommonUtil.olineStrToDate(lastSynTime + " 00:00:00").getTime());
             *//* if (lastSynTime != null)
               {
                  lastSynTime = lastSynTime.replace('/', '-');
                 ConditionType condition1 = ConditionType.ge("createTime",
                          new Timestamp(CommonUtil.olineStrToDate(lastSynTime + " 00:00:00").getTime()));
               
                  }*/

            /* Timestamp synTime = DateUtil.getSynTimeByStr(JSONUtil.jsonElementToString(lastSynTime));*/
            //返回参数接口数据CommonUtil.olineStrToDate(lastSynTime + " 00:00:00").getTime())

            //此时的lastSynTime的时间格式是yyyy/MM/dd hh:mm:ss 000
            Timestamp synTime = DateUtil.getSynTimeByStr(GsonUtil.toString(paramJson.get("lastSynTime")));
            // 同步类型,1表示同步时间点之后的数据，2表示同步时间点之前的数据
            /* int synType = (paramJson.get("synType") == null || paramJson.get("synType").getAsString().equals("")) ? 2 : paramJson.get("synType")
                      .getAsInt();*/
            JsonArray retJson = new JsonArray();
            Map<String, Object> filter = new HashMap<String, Object>();
            if(StringUtil.isNotEmpty(custId))
            {
                filter.put("createBy", custId);
            }
            else
            {
                filter.put("createBy", userId);
            }
            // 拉取时间
            filter.put("sysTime", synTime);
            /*            filter.put("synType", synType);*/
            filter.put("maxSize", pageSize);
            List<DietaryLog> dietaryLogs = dietaryLogService.getListDietaryLog(filter);
            if (dietaryLogs != null && dietaryLogs.size() > 0)
            {
                for (DietaryLog dietaryLog : dietaryLogs)
                {
                    JsonObject jsonObj = new JsonObject();
                    jsonObj.addProperty("dayIntakeEnergy", (dietaryLog.getIntakeEnergy()).intValue());
                    String startTimeStr = null;
                    if (dietaryLog.getDietaryTime() != null)
                    {
                        startTimeStr = DateUtil.toSynTimeStr(new Timestamp(dietaryLog.getDietaryTime().getTime()));
                        startTimeStr = startTimeStr.replace('-', '/');
                        startTimeStr = startTimeStr.substring(0, 10);
                    }
                    jsonObj.addProperty("intakeTime", startTimeStr);
                    retJson.add(jsonObj);
                }
            }
            super.write(ResultTran.STATE_OK, "", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端刷新失败.", e);
            JsonArray json = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端异常", json);
        }
    }

    /**
     * 膳食日志详细列表
     * @author huangGang
     * update time 2014.11.17
     * 客户端查询
     */
    public void listDetailLog() throws Exception
    {
        try
        {

            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                return;
            }
            User user = super.getOperUser();
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String intakeTimeStr = GsonUtil.toString(paramJson.get("intakeTime"));
            Date intakeTime = DateUtil.getDateFromHtml(intakeTimeStr);
            String custId = GsonUtil.toString(paramJson.get("custId"));
            JsonArray retJson = new JsonArray();
            Map<String, Object> filter = new HashMap<String, Object>();
            if(StringUtil.isNotEmpty(custId))
            {
                filter.put("creator", custId);
            }
            else
            {
                filter.put("creator", userId);
            }
            filter.put("intakeTime", intakeTime);
            List<DietaryLog> dietaryLogs = dietaryLogService.getDetailLog(filter);
            if (dietaryLogs != null && dietaryLogs.size() > 0)
            {
                for (DietaryLog dietaryLog : dietaryLogs)
                {
                    JsonObject jsonOjb = new JsonObject();
                    jsonOjb.addProperty("userId", dietaryLog.getUserId());
                    jsonOjb.addProperty("foodLogId", dietaryLog.getId());
                    jsonOjb.addProperty("mealType", dietaryLog.getDietaryType());
                    jsonOjb.addProperty("intakeAmount", dietaryLog.getFoodWeight());
                    jsonOjb.addProperty("intakeFoodId", dietaryLog.getFoodId());
                    String foodTimeStr = null;
                    if (dietaryLog.getDietaryTime() != null)
                    {
                        foodTimeStr = DateUtil.toSynTimeStr(new Timestamp(dietaryLog.getDietaryTime().getTime()));
                        foodTimeStr = foodTimeStr.replace('-', '/');
                        foodTimeStr = foodTimeStr.substring(0, 10);
                    }
                    jsonOjb.addProperty("intakeTime", foodTimeStr);
                    
                    String createTimeStr = null;
                    if (dietaryLog.getCreateTime() != null)
                    {
                        createTimeStr = DateUtil.toSynTimeStr(new Timestamp(dietaryLog.getCreateTime().getTime()));
                        createTimeStr = createTimeStr.replace('-', '/');
                        createTimeStr = createTimeStr.substring(0, 10);
                    }
                    jsonOjb.addProperty("createTime", createTimeStr);
                    retJson.add(jsonOjb);
                }
            }
            super.write(ResultTran.STATE_OK, "", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端刷新失败.", e);
            JsonArray json = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端异常", json);
        }
    }
    
    /**
     * 删除一天膳食日志
     * @author huanggang
     */
    public void deteleFoodLogs() throws Exception
    {
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                return;
            }
            User user = super.getOperUser();
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String intakeTimeStr = GsonUtil.toString(paramJson.get("intakeTime"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            Date intakeTime = DateUtil.getDateFromHtml(intakeTimeStr);
            JsonArray retJson = new JsonArray();
            Map<String, Object> filter = new HashMap<String, Object>();
            if(StringUtil.isNotEmpty(custId))
            {
                filter.put("createBy", custId);
            }
            else
            {
                filter.put("createBy", userId);
            }
            filter.put("intakeTime", intakeTime);
            dietaryLogService.deleteDietaryLogs(filter);
            JsonObject jsonObj = new JsonObject();
            retJson.add(jsonObj);
            super.write(ResultTran.STATE_OK, "", retJson);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            JsonArray json = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端异常", json);
        }
    }
    
    /**
     * 删除一条膳食日志
     * @author huanggang
     */
    public void deteleFoodLog() throws Exception
    {
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                return;
            }
            User user = super.getOperUser();
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String foodLogId = GsonUtil.toString(paramJson.get("foodLogId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            JsonArray retJson = new JsonArray();
            Map<String, Object> filter = new HashMap<String, Object>();
            if(StringUtil.isNotEmpty(custId))
            {
                filter.put("createBy", custId);
            }
            else
            {
                filter.put("createBy", userId);
            }
            filter.put("foodLogId", foodLogId);
            dietaryLogService.deleteDietaryLog(filter);
            super.write(ResultTran.STATE_OK, "", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端删除失败.", e);
            JsonArray json = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端异常", json);
        }
    }
    
    /**
     *更新食物数据接口
     * @author huangGang
     * 2014.11.18
     */
    public void updateFood()
    {
        try{
            JsonObject paramJson= super.getParams();
            if(paramJson == null){
                return ;
            }
            String userId = GsonUtil.toString(paramJson.get("userId"));
            JsonObject retJson = new JsonObject();
            
            List<Food> foods = foodService.getFoodForClient();
            JsonArray foodsJson = new JsonArray();
            if (foods != null && foods.size() > 0)
            {
                for (Food food : foods)
                {
                    JsonObject foodJson = new JsonObject();
                    foodJson.addProperty("id", food.getId());
                    foodJson.addProperty("foodName", food.getName());
                    foodJson.addProperty("catalogId", food.getCatalogId());
                    foodJson.addProperty("foodHeat", food.getFoodHeat());
                    foodJson.addProperty("state", food.getState());
                    foodJson.addProperty("createBy", food.getCreateBy());
                    String createTimeStr = null;
                    if (food.getCreateTime() != null)
                    {
                        createTimeStr = DateUtil.toSynTimeStr(new Timestamp(food.getCreateTime().getTime()));
                        createTimeStr = createTimeStr.replace('-', '/');
                        createTimeStr = createTimeStr.substring(0, 10);
                    }
                    foodJson.addProperty("createTime", createTimeStr);
                    foodJson.addProperty("updateBy", food.getUpdateBy());
                    String updateTimeStr = null;
                    if (food.getUpdateTime() != null)
                    {
                        updateTimeStr = DateUtil.toSynTimeStr(new Timestamp(food.getUpdateTime().getTime()));
                        updateTimeStr = updateTimeStr.replace('-', '/');
                        updateTimeStr = updateTimeStr.substring(0, 10);
                    }
                    foodJson.addProperty("updateTime", updateTimeStr);
                    foodsJson.add(foodJson);
                }
                retJson.add("foodList", foodsJson);
            }
            List<FoodCatalog> foodCatalogs = foodCatalogService.getFoodCatalogForClient();
            JsonArray foodCatalogsJson = new JsonArray();
            if (foodCatalogs != null && foodCatalogs.size() > 0)
            {
                for (FoodCatalog foodCatalog : foodCatalogs)
                {
                    JsonObject foodCatalogJson = new JsonObject();
                    foodCatalogJson.addProperty("id", foodCatalog.getId());
                    foodCatalogJson.addProperty("parentId", foodCatalog.getParentId());
                    foodCatalogJson.addProperty("level", foodCatalog.getLevel());
                    foodCatalogJson.addProperty("foodTypeName", foodCatalog.getName());
                    foodCatalogJson.addProperty("createBy", foodCatalog.getCreateBy());
                    String createTimeStr = null;
                    if (foodCatalog.getCreateTime() != null)
                    {
                        createTimeStr = DateUtil.toSynTimeStr(new Timestamp(foodCatalog.getCreateTime().getTime()));
                        createTimeStr = createTimeStr.replace('-', '/');
                        createTimeStr = createTimeStr.substring(0, 10);
                    }
                    foodCatalogJson.addProperty("createTime", createTimeStr);
                    foodCatalogJson.addProperty("updateBy", foodCatalog.getUpdateBy());
                    String updateTimeStr = null;
                    if (foodCatalog.getUpdateTime() != null)
                    {
                        updateTimeStr = DateUtil.toSynTimeStr(new Timestamp(foodCatalog.getUpdateTime().getTime()));
                        updateTimeStr = updateTimeStr.replace('-', '/');
                        updateTimeStr = updateTimeStr.substring(0, 10);
                    }
                    foodCatalogJson.addProperty("updateTime", updateTimeStr);
                    foodCatalogsJson.add(foodCatalogJson);
                }
                retJson.add("foodTypeList", foodCatalogsJson);
            }
            /*List<FoodCatalog> foodCatalog = foodCatalogService.getFoodForClient();*/
            /*Timestamp lastSynTime = DateUtil.getSynTimeByStr(JSONUtil.jsonElementToString(paramJson.get("lastSynTime")));
            // 返回值对象
            JsonObject retJson = new JsonObject();
            Timestamp needSynTime = askService.getLatestCatalogSynTime();
            retJson.addProperty("updateTime", DateUtil.toSynTimeStr(needSynTime));*/
            super.write(ResultTran.STATE_OK, "更新食物数据成功", retJson);
        }catch(Exception e){
            logger.error("客户端删除失败.", e);
            JsonArray json = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端异常", json);
        }
    }

    /**
     *更新食物数据日志接口
     * @author huangGang
     * 2014.11.18
     */
    public void updateFoodLog()
    {
        Result r = null;
        try{
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                return;
            }
            String userId = GsonUtil.toString(paramJson.get("userId"));
            Timestamp intakeTime = DateUtil.getSynTimeByStr(GsonUtil.toString(paramJson.getAsJsonArray("datas").getAsJsonArray().get(0).getAsJsonObject().get("intakeTime"))+" 00:00:00 000");
            String custId = GsonUtil.toString(paramJson.get("custId"));
            Map<String, Object> filter = new HashMap<String, Object>();
            if(StringUtil.isNotEmpty(custId))
            {
                filter.put("createBy", custId);
            }
            else
            {
                filter.put("createBy", userId);
            }
            filter.put("intakeTime", intakeTime);
            dietaryLogService.deleteDietaryLogs(filter);
            //先删除数据，再增加
            User user = (User) super.getOperUser();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            JsonElement data = paramJson.getAsJsonArray("datas");
            JsonArray datas = data.getAsJsonArray();
            JsonObject retJson = new JsonObject();
            JsonArray json = new JsonArray();
            String ids[]= new String[datas.size()];
            for (int i = 0; i < datas.size(); i++)
            {
                JsonObject paramJsons = datas.get(i).getAsJsonObject();
                //解析参数，添加膳食日志
                Integer dietaryType = GsonUtil.toInteger(paramJsons.get("mealType"));
                Integer foodWeight = GsonUtil.toInteger(paramJsons.get("intakeAmount"));
                String foodId = GsonUtil.toString(paramJsons.get("intakeFoodId"));
                System.out.println(paramJsons.get("intakeTime"));
                String intakeTime2= GsonUtil.toString(paramJsons.get("intakeTime"))+" 00:00:00 000";
                Timestamp dietaryTime = DateUtil.getSynTimeByStr(intakeTime2);
                System.out.println(paramJsons.get("intakeTime"));
                //根据膳食名称从数据库中获取膳食id
                //根据膳食id,查询出食物目录
                /*Food food = foodService.get(foodId);
                FoodCatalog foodCatalog = foodCatalogService*/
                DietaryLog dietaryLog = new DietaryLog();
                dietaryLog.setDietaryType(dietaryType);
                dietaryLog.setDietaryTime(dietaryTime);
                dietaryLog.setFoodId(foodId);
                if(StringUtil.isNotEmpty(custId))
                {
                    dietaryLog.setUserId(custId);
                }
                else
                {
                    dietaryLog.setUserId(userId);
                }
                dietaryLog.setFoodWeight((double) foodWeight);
                dietaryLog.setCreateBy(user.getId());
                dietaryLog.setCreateTime(now);
                dietaryLog.setUpdateBy(user.getId());
                dietaryLog.setUpdateTime(now);
                r = dietaryLogService.addDietaryLog(dietaryLog);
                String id = (String) r.getData();
                
                ids[i]= id;
                JsonObject idJson = new JsonObject();
                idJson.addProperty("foodLogId", ids[i]);
                json.add(idJson);
                String startTimeStr = null;
                if (dietaryLog.getDietaryTime() != null)
                {
                    startTimeStr = DateUtil.toSynTimeStr(new Timestamp(dietaryLog.getDietaryTime().getTime()));
                    startTimeStr = startTimeStr.replace('-', '/');
                    startTimeStr = startTimeStr.substring(0, 10);
                }
                retJson.addProperty("intakeTime", startTimeStr);
            }
            //返回参数  首先是插入膳食日志的id数组
            retJson.add("datas", json);        
            //返回参数 其次是总的参数  查询插入膳食日志后所膳食时间的总能量
            Map<String, Object> filter2 = new HashMap<String, Object>();
            if(StringUtil.isNotEmpty(custId))
            {
                filter2.put("createBy", custId);
            }
            else
            {
                filter2.put("createBy", userId);
            }
            filter2.put("sysTime", DateUtil.getSynTimeByStr(GsonUtil.toString(datas.get(0).getAsJsonObject().get("intakeTime"))+" 00:00:00 000"));
            DietaryLog dayLog = dietaryLogService.getLogByDay(filter2);
            if (dayLog != null)
            {
                Double dayIntakeEnergy = dayLog.getIntakeEnergy();
                retJson.addProperty("dayIntakeEnergy", (dayIntakeEnergy).intValue());
            }
            retJson.addProperty("createTime", DateUtil.toSynTimeStr(now));
            super.write(String.valueOf(r.getState()), r.getMsg(), retJson);
        }catch(Exception e){
            logger.error("客户端删除失败.", e);
            JsonArray json = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端异常", json);
        }
    }
    
}
