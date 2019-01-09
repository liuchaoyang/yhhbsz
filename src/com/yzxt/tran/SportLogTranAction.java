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
import com.yzxt.yh.module.rgm.bean.Sport;
import com.yzxt.yh.module.rgm.bean.SportLog;
import com.yzxt.yh.module.rgm.service.SportLogService;
import com.yzxt.yh.module.rgm.service.SportService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

/**
 * 健康日志运动客户端接口类
 * @author h
 *
 */
public class SportLogTranAction extends BaseTranAction
{
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(SportLogTranAction.class);
    private SportLog sportLog;
    private SportLogService sportLogService;
    private SportService sportService;

    public SportService getSportService()
    {
        return sportService;
    }

    public void setSportService(SportService sportService)
    {
        this.sportService = sportService;
    }

    public SportLogService getSportLogService()
    {
        return sportLogService;
    }

    public void setSportLogService(SportLogService sportLogService)
    {
        this.sportLogService = sportLogService;
    }

    public SportLog getSportLog()
    {
        return sportLog;
    }

    public void setSportLog(SportLog sportLog)
    {
        this.sportLog = sportLog;
    }

    /**
     * 添加运动日志
     * @author huanggang
     */
    public void addSportLog() throws Exception
    {
        Result r = null;
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                return;
            }
            User user = super.getOperUser();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            JsonElement data = paramJson.getAsJsonArray("datas");
            JsonArray datas = data.getAsJsonArray();
            JsonObject retJson = new JsonObject();
            JsonArray json = new JsonArray();
            String ids[]= new String[datas.size()];
            for (int i = 0; i < datas.size(); i++)
            {
                JsonObject paramJsons = datas.get(i).getAsJsonObject();
                //解析参数，添加运动日志
                String sportId = GsonUtil.toString(paramJsons.get("sportId"));
                String sportTimeStr = GsonUtil.toString(paramJsons.get("sportTime"))+" 00:00:00 000";
                Timestamp sportTime = DateUtil.getSynTimeByStr(sportTimeStr);
                /*Timestamp sportTime = DateUtil.getSynTimeByStr(GsonUtil.toString(paramJsons.get("sportTime")));*/
                Integer timeSpan = GsonUtil.toInteger(paramJsons.get("sportDuration"));
                SportLog sportLog = new SportLog();
                sportLog.setSportId(sportId);
                if(StringUtil.isNotEmpty(custId))
                {
                    sportLog.setUserId(custId);
                }
                else
                {
                    sportLog.setUserId(userId);
                }
                sportLog.setSportTime(sportTime);
                sportLog.setTimeSpan(timeSpan);
                sportLog.setCreateTime(now);
                sportLog.setUpdateTime(now);
                sportLog.setCreateBy(user.getId());
                sportLog.setUpdateBy(user.getId());
                r = sportLogService.addSportLog(sportLog);
                String id = (String) r.getData();
                ids[i] = id;
                JsonObject idJson = new JsonObject();
                idJson.addProperty("sportLogId", ids[i]);
                json.add(idJson);
                String createTimeStr = null;
                if (sportLog.getCreateTime() != null)
                {
                    createTimeStr = DateUtil.toSynTimeStr(new Timestamp(sportLog.getCreateTime().getTime()));
                    createTimeStr = createTimeStr.replace('-', '/');
                    createTimeStr = createTimeStr.substring(0, 10);
                }
                retJson.addProperty("createTime", createTimeStr);//DateUtil.toSynTimeStr(now)
            }
            //返回参数  首先是插入的参数
            retJson.add("datas", json); 

            
            String startTimeStr = null;
            if (datas.get(0).getAsJsonObject().get("sportTime") != null)
            {
                startTimeStr = GsonUtil.toString(datas.get(0).getAsJsonObject().get("sportTime"));
                startTimeStr = startTimeStr.replace('-', '/');
                startTimeStr = startTimeStr.substring(0, 10);
            }
            retJson.addProperty("sportTime", startTimeStr);
            //返回参数 其次是总的参数  查询插入运动日志后所运动时间的总能量和总时长
            Map<String, Object> filter = new HashMap<String, Object>();
            if(StringUtil.isNotEmpty(custId))
            {
                filter.put("createBy", custId);
            }
            else
            {
                filter.put("createBy", userId);
            }
            filter.put("sysTime", DateUtil.getSynTimeByStr(GsonUtil.toString(datas.get(0).getAsJsonObject().get("sportTime"))+" 00:00:00 000"));
            SportLog dayLog = sportLogService.getLogByDay(filter);
            if (dayLog != null)
            {
                Integer daySportDuration = dayLog.getTimeSpan();
                retJson.addProperty("daySportDuration", daySportDuration);
                Double dayConsumeEnergy = dayLog.getDayConsumeEnergy();
                retJson.addProperty("dayConsumeEnergy", (dayConsumeEnergy).intValue());
            }
            super.write(ResultTran.STATE_OK, r.getMsg(), retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端保存问题失败.", e);
            JsonArray json = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端异常", json);
        }
    }

    /**
     * 运动日志列表
     * @author h
     * 客户端查询
     */
    public void listSportLog() throws Exception
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
            Integer pageSize = GsonUtil.toInteger(paramJson.get("pageSize"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            /* String lastSynTime = getLastSynTime(paramJson.get("lastSynTime"));
             Timestamp synTime=new Timestamp(CommonUtil.olineStrToDate(lastSynTime + " 00:00:00").getTime());
             *//* if (lastSynTime != null)
               {
                  lastSynTime = lastSynTime.replace('/', '-');
                 ConditionType condition1 = ConditionType.ge("createTime",
                          new Timestamp(CommonUtil.olineStrToDate(lastSynTime + " 00:00:00").getTime()));
               
                  }*/
            
            /* Timestamp synTime = DateUtil.getSynTimeByStr(GsonUtil.toString(lastSynTime));*/
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
            List<SportLog> sportLogs = sportLogService.getListSportLog(filter);
            if (sportLogs != null && sportLogs.size() > 0)
            {
                for (SportLog sportLog : sportLogs)
                {
                    JsonObject jsonObj = new JsonObject();
                    /*jsonObj.addProperty("userName", (sportLog.getCustName()));*/
                    jsonObj.addProperty("dayConsumeEnergy", (sportLog.getDayConsumeEnergy()).intValue());
                    jsonObj.addProperty("daySportDuration", sportLog.getTimeSpan());
                    String startTimeStr = null;
                    if (sportLog.getSportTime() != null)
                    {
                        startTimeStr = DateUtil.toSynTimeStr(new Timestamp(sportLog.getSportTime().getTime()));
                        startTimeStr = startTimeStr.replace('-', '/');
                        startTimeStr = startTimeStr.substring(0, 10);
                    }
                    jsonObj.addProperty("sportTime", startTimeStr);
                    retJson.add(jsonObj);
                }
            }
            super.write(ResultTran.STATE_OK, "查詢成功", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端查询运动日志发生异常.", e);
            JsonArray json = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端异常", json);
        }
    }

    /**
     * 运动日志详细列表
     * @author h
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
            Integer pageSize = GsonUtil.toInteger(paramJson.get("pageSize"));
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String sportTimeS = GsonUtil.toString(paramJson.get("sportTime"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            Date sportTime = DateUtil.getDateFromHtml(sportTimeS);
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
            filter.put("maxSize", pageSize);
            filter.put("sportTime", sportTime);
            List<SportLog> sportLogs = sportLogService.getDetailLog(filter);
            if (sportLogs != null && sportLogs.size() > 0)
            {
                for (SportLog sportLog : sportLogs)
                {
                    JsonObject jsonOjb = new JsonObject();
                    jsonOjb.addProperty("sportLogId", sportLog.getId());
                    jsonOjb.addProperty("sportId", sportLog.getSportId());
                    jsonOjb.addProperty("sportType", sportLog.getSportType());
                    String sportTimeStr = null;
                    if (sportLog.getSportTime() != null)
                    {
                        sportTimeStr = DateUtil.toSynTimeStr(new Timestamp(sportLog.getSportTime().getTime()));
                        sportTimeStr = sportTimeStr.replace('-', '/');
                        sportTimeStr = sportTimeStr.substring(0, 10);
                    }
                    jsonOjb.addProperty("sportTime", sportTimeStr);
                    jsonOjb.addProperty("sportDuration", sportLog.getTimeSpan());
                    jsonOjb.addProperty("consumeEnergy", (sportLog.getDayConsumeEnergy()).intValue());
                    String createTimeStr = null;
                    if (sportLog.getCreateTime() != null)
                    {
                        createTimeStr = DateUtil.toSynTimeStr(new Timestamp(sportLog.getCreateTime().getTime()));
                        createTimeStr = createTimeStr.replace('-', '/');
                        createTimeStr = createTimeStr.substring(0, 10);
                    }
                    jsonOjb.addProperty("createTime", createTimeStr);
                    retJson.add(jsonOjb);
                }
            }
            super.write(ResultTran.STATE_OK, "查詢成功", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端查询运动详细日志发生异常.", e);
            JsonArray json = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端异常", json);
        }
    }

    /**
     * 根据时间段来查询运动日志
     * @author h
     * 客户端查询
     */
    public void querySportLog() throws Exception
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
            Timestamp beginDate = DateUtil.getSynTimeByStr(GsonUtil.toString(paramJson.get("beginDate")));
            Timestamp endDate = DateUtil.getSynTimeByStr(GsonUtil.toString(paramJson.get("endDate")));
            int pageSize = paramJson.get("pageSize").getAsInt();
            JsonArray retJson = new JsonArray();
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("createBy", userId);
            filter.put("maxSize", pageSize);
            filter.put("beginDate", beginDate);
            filter.put("endDate", endDate);
            List<SportLog> sportLogs = sportLogService.querySportLog(filter);
            if (sportLogs != null && sportLogs.size() > 0)
            {
                for (SportLog sportLog : sportLogs)
                {
                    JsonObject jsonObj = new JsonObject();
                    /*jsonObj.addProperty("dayConsumeEnergy", sportLog.getSportEnergy());*/
                    jsonObj.addProperty("daySportDuration", sportLog.getTimeSpan());
                    String sportTimeStr = null;
                    if (sportLog.getCreateTime() != null)
                    {
                        sportTimeStr = DateUtil.toSynTimeStr(new Timestamp(sportLog.getCreateTime().getTime()));
                        sportTimeStr = sportTimeStr.replace('-', '/');
                        sportTimeStr = sportTimeStr.substring(0, 10);
                    }
                    jsonObj.addProperty("sportTime", sportTimeStr);
                    retJson.add(jsonObj);
                }
            }
            super.write(ResultTran.STATE_OK, "查詢成功", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端查询运动日志发生异常.", e);
            JsonArray json = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端异常", json);
        }
    }
    
    /**
     * 删除一天运动日志
     * @author h
     * 客户端删除
     */
    public void deteleSportLogs() throws Exception
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
            String sportTimeS = GsonUtil.toString(paramJson.get("sportTime"));
            Date sportTime = DateUtil.getDateFromHtml(sportTimeS);
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
            filter.put("sportTime", sportTime);
            sportLogService.deleteSportLogs(filter);
            JsonObject jsonObj = new JsonObject();
            retJson.add(jsonObj);
            super.write(ResultTran.STATE_OK, "删除成功", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端删除运动日志发生异常.", e);
            JsonArray json = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端异常", json);
        }
    }
    
    /**
     * 删除一条运动日志
     * @author huanggang
     * 客户端删除
     */
    public void deteleSportLog() throws Exception
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
            String sportLogId = GsonUtil.toString(paramJson.get("sportLogId"));
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
            filter.put("sportLogId", sportLogId);
            sportLogService.deleteSportLog(filter);
            super.write(ResultTran.STATE_OK, "删除成功", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端删除失败.", e);
            JsonArray json = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端异常", json);
        }
    }
    
    /**
     *更新运动数据接口
     * @author huangGang
     * 2014.11.19
     */
    public void updateSport(){
        try{
            JsonObject paramJson= super.getParams();
            if(paramJson == null){
                return ;
            }
            String userId = GsonUtil.toString(paramJson.get("userId"));
            JsonObject retJson = new JsonObject();
            
            List<Sport> sports = sportService.getSportForClient();
            JsonArray sportsJson = new JsonArray();
            JsonArray sportTypesJson = new JsonArray();
            if (sports != null && sports.size() > 0)
            {
                for (Sport sport : sports)
                {
                    JsonObject sportJson = new JsonObject();
                    sportJson.addProperty("id", sport.getId());
                    sportJson.addProperty("sportName", sport.getName());
                    sportJson.addProperty("typeId", sport.getSportType());
                    sportJson.addProperty("isAerobic", sport.getIsAerobicExercise());
                    sportJson.addProperty("consumeEnergy", sport.getSportHeat());
                    sportJson.addProperty("state", sport.getState());
                    String createTimeStr = null;
                    if (sport.getCreateTime() != null)
                    {
                        createTimeStr = DateUtil.toSynTimeStr(new Timestamp(sport.getCreateTime().getTime()));
                        createTimeStr = createTimeStr.replace('-', '/');
                        createTimeStr = createTimeStr.substring(0, 10);
                    }
                    sportJson.addProperty("createTime", createTimeStr);
                    String updateTimeStr = null;
                    if (sport.getUpdateTime() != null)
                    {
                        updateTimeStr = DateUtil.toSynTimeStr(new Timestamp(sport.getUpdateTime().getTime()));
                        updateTimeStr = updateTimeStr.replace('-', '/');
                        updateTimeStr = updateTimeStr.substring(0, 10);
                    }
                    sportJson.addProperty("updateTime", updateTimeStr);
                    sportsJson.add(sportJson);
                    JsonObject sportTypeJson = new JsonObject();
                    sportTypeJson.addProperty("typeId", sport.getSportType());
                    String typeName="";
                    if(sport.getSportType()==1){
                        typeName= "轻度";
                    }else if(sport.getSportType()==2){
                        typeName= "中度";
                    }else if(sport.getSportType()==3){
                        typeName= "稍强度";   
                    }
                    sportTypeJson.addProperty("sportType",typeName);
                    sportTypeJson.addProperty("state", sport.getState());
                    sportTypesJson.add(sportTypeJson);
                }
                retJson.add("sportList", sportsJson);
                retJson.add("sportTypeList", sportTypesJson);
            }
            super.write(ResultTran.STATE_OK, "更新成功", retJson);
            /*sendResponseData(ReturnTransInfo.STATEOK, ReturnTransInfo.STATEOKINFO, retJson);*/
        }catch(Exception e){
            logger.error("客户端更新运动数据失败.", e);
            JsonArray json = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端异常", json);
            /*sendResponseData(ReturnTransInfo.STATEERR, ReturnTransInfo.STATEERRINFO, null);*/
        }
    }
    
    /**
     *更新运动日志接口
     * @author huangGang
     * 2014.11.21
     */
    public void updateSportLog(){
        try{
            JsonObject paramJson= super.getParams();
            if(paramJson == null){
                return ;
            }
            User user = super.getOperUser();
            String userId = GsonUtil.toString(paramJson.get("userId"));
            Timestamp sportTime2 = DateUtil.getSynTimeByStr(GsonUtil.toString(paramJson.getAsJsonArray("datas").getAsJsonArray().get(0).getAsJsonObject().get("sportTime"))+" 00:00:00 000");
            String custId = GsonUtil.toString(paramJson.get("custId"));
            Map<String, Object> filter2 = new HashMap<String, Object>();
            if(StringUtil.isNotEmpty(custId))
            {
                filter2.put("createBy", custId);
            }
            else
            {
                filter2.put("createBy", userId);
            }
            filter2.put("sportTime", sportTime2);
            sportLogService.deleteSportLogs(filter2);
            //先删除数据，再增加数据
            Timestamp now = new Timestamp(System.currentTimeMillis());
            JsonElement data = paramJson.getAsJsonArray("datas");
            JsonArray datas = data.getAsJsonArray();
            JsonObject retJson = new JsonObject();
            JsonArray json = new JsonArray();
            String ids[]= new String[datas.size()];
            for (int i = 0; i < datas.size(); i++)
            {
                JsonObject paramJsons = datas.get(i).getAsJsonObject();
                //解析参数，添加运动日志
                String sportId = GsonUtil.toString(paramJsons.get("sportId"));
                String sportTimeStr = GsonUtil.toString(paramJsons.get("sportTime"))+" 00:00:00 000";
                Timestamp sportTime = DateUtil.getSynTimeByStr(sportTimeStr);
                /*Timestamp sportTime = DateUtil.getSynTimeByStr(GsonUtil.toString(paramJsons.get("sportTime")));*/
                Integer timeSpan = GsonUtil.toInteger(paramJsons.get("sportDuration"));
                SportLog sportLog = new SportLog();
                sportLog.setSportId(sportId);
                if(StringUtil.isNotEmpty(custId))
                {
                    sportLog.setUserId(custId);
                }
                else
                {
                    sportLog.setUserId(userId);
                }
                sportLog.setSportTime(sportTime);
                sportLog.setTimeSpan(timeSpan);
                sportLog.setCreateTime(now);
                sportLog.setUpdateTime(now);
                sportLog.setCreateBy(user.getId());
                sportLog.setUpdateBy(user.getId());
                Result r = sportLogService.addSportLog(sportLog);
                String id = (String) r.getData();
                ids[i] = id;
                JsonObject idJson = new JsonObject();
                idJson.addProperty("sportLogId", ids[i]);
                json.add(idJson);
                String createTimeStr = null;
                if (sportLog.getCreateTime() != null)
                {
                    createTimeStr = DateUtil.toSynTimeStr(new Timestamp(sportLog.getCreateTime().getTime()));
                    createTimeStr = createTimeStr.replace('-', '/');
                    createTimeStr = createTimeStr.substring(0, 10);
                }
                retJson.addProperty("createTime", createTimeStr);//DateUtil.toSynTimeStr(now)
            }
            //返回参数  首先是插入的参数
            retJson.add("datas", json); 

            
            String startTimeStr = null;
            if (datas.get(0).getAsJsonObject().get("sportTime") != null)
            {
                startTimeStr = GsonUtil.toString(datas.get(0).getAsJsonObject().get("sportTime"));
                startTimeStr = startTimeStr.replace('-', '/');
                startTimeStr = startTimeStr.substring(0, 10);
            }
            retJson.addProperty("sportTime", startTimeStr);
            //返回参数 其次是总的参数  查询插入运动日志后所运动时间的总能量和总时长
            Map<String, Object> filter = new HashMap<String, Object>();
            if(StringUtil.isNotEmpty(custId))
            {
                filter.put("createBy", custId);
            }
            else
            {
                filter.put("createBy", userId);
            }
            filter.put("sysTime", DateUtil.getSynTimeByStr(GsonUtil.toString(datas.get(0).getAsJsonObject().get("sportTime"))+" 00:00:00 000"));
            SportLog dayLog = sportLogService.getLogByDay(filter);
            if (dayLog != null)
            {
                Integer daySportDuration = dayLog.getTimeSpan();
                retJson.addProperty("daySportDuration", daySportDuration);
                Double dayConsumeEnergy = dayLog.getDayConsumeEnergy();
                retJson.addProperty("dayConsumeEnergy", (dayConsumeEnergy).intValue());
            }
            super.write(ResultTran.STATE_OK, "更新成功", retJson);
        }catch(Exception e){
            logger.error("客户端更新运动日志失败.", e);
            JsonArray json = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端异常", json);
        }
    }
}
