package com.yzxt.yh.module.rgm.action;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.rgm.bean.DietaryLog;
import com.yzxt.yh.module.rgm.bean.Food;
import com.yzxt.yh.module.rgm.bean.FoodCatalog;
import com.yzxt.yh.module.rgm.service.DietaryLogService;
import com.yzxt.yh.module.rgm.service.FoodService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

/**
 * @author huanggang
 * 运动日志 
 * 2014.9.29
 */
public class DietaryLogAction extends BaseAction
{
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(DietaryLog.class);
    private DietaryLog dietaryLog;
    private DietaryLogService dietaryLogService;
    private FoodService foodService;
    
    
    public FoodService getFoodService()
    {
        return foodService;
    }

    public void setFoodService(FoodService foodService)
    {
        this.foodService = foodService;
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

    /**
     * @author h
     * 运动日志总列表 
     * 2014.10.10
     */ 
    public void listDietaryLog(){
        try{
            User user  = (User)super.getCurrentUser();
            Map<String,Object> filter = new HashMap<String,Object>();
            String beginTime = request.getParameter("beginTime");
            filter.put("beginTime",beginTime != null ? beginTime.trim() : null);
            String endTime = request.getParameter("endTime");
            filter.put("endTime", endTime != null ? endTime.trim(): null);
           /* String keyword = request.getParameter("keyword");
            if(keyword!=null&&keyword.trim()==null){
                filter.put("keyword", keyword);
            }*/
            filter.put("createBy", user.getId());
            PageModel<DietaryLog> dietaryLog = dietaryLogService.getDietaryLogList(filter,getPage(),getRows());
            super.write(dietaryLog);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * 跳转到具体时间日志页面
     * @return
     */
    public String showDetails()
    {
        String dietaryTime = request.getParameter("dietaryTime");
        dietaryTime = dietaryTime.substring(0,10);
        /*SportLog sportLog = sportLogService.getSportLog(sportTime);*/
        request.setAttribute("dietaryTime", dietaryTime);
        /*Dict dict = dictService.getDict(dictCode);
        request.setAttribute("dict", dict);*/
        return "details";
    }
    
    /**
     * 显示具体时间的明细日志
     * @return
     * @throws Exception 
     */
    public String getLogDetails() throws Exception
    {
        User user  = (User)super.getCurrentUser();
        String createBy = user.getId();
        String dietaryTimeStr = request.getParameter("dietaryTime");
        dietaryTimeStr = dietaryTimeStr+" 00:00:00";
        Timestamp dietaryTime = (Timestamp)DateUtil.getTimeByStr(dietaryTimeStr);
        Map<String, Object> filter = new HashMap<String, Object>();
        /*filter.put("sportTime", sportTime);
        filter.put("createBy", user.getId());*/
        DietaryLog dietaryLog = new DietaryLog();
        dietaryLog.setUserId(createBy);
        dietaryLog.setDietaryTime(dietaryTime);
        List<DietaryLog> detailsLog = dietaryLogService.getDetailsLog(dietaryLog);
        /*List<String> jsonList = new ArrayList<String>();
        int len = detailsLog != null ? detailsLog.size() : 0;
        for (int i = 0; i < len; i++)
        {
            String jsonStr = JSONHandleUtil.readJsonObject(detailsLog.get(i), DietaryLog.class, 2);
            jsonList.add(jsonStr);
        }
        filter.put("rows", jsonList);
        JSONObject js = JSONObject.fromObject(filter);*/
        super.write(detailsLog);

        return null;
    }
    
    /**
     * 显示子目录JSON数据
     */
    public void listChildrenFoodCatalogsByJson()
    {
        String catalogId = request.getParameter("catalogId");
        List<FoodCatalog> children = null;
        if (StringUtil.isNotEmpty(catalogId))
        {
            children = foodService.getChildrenFoodCatalogs(catalogId);
        }
        JsonArray retJson = new JsonArray();
        if (children != null && children.size() > 0)
        {
            for (FoodCatalog cata : children)
            {
                JsonObject jsonObj = new JsonObject();
                jsonObj.addProperty("id", cata.getId());
                jsonObj.addProperty("name", cata.getName());
                retJson.add(jsonObj);
            }
        }
        super.write(retJson);
    }
    
    /*
     * 添加膳食日志
     * @author huangGang
     * 2014.10.13
     * */
    public String addDietaryLog(){
        String msg="0";
        try{
            User user  = (User)super.getCurrentUser();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            DietaryLog dietaryLog = new DietaryLog();
            dietaryLog.setCreateTime(now);
            dietaryLog.setUpdateTime(now);
            dietaryLog.setCreateBy(user.getId());
            dietaryLog.setUpdateBy(user.getId());
            Result r = dietaryLogService.addDietaryLog(dietaryLog);
            String id = (String) r.getData();
        }catch(Exception e){
            e.printStackTrace();
            msg="-3";
        }
        super.write(msg);
        return null;
    }
    
    /**
     * 跳转到填写日志页面
     * 
     * @return
     * @throws Exception 
     */
    public String toEdit() throws Exception
    {
        String dietaryTimeStr = request.getParameter("dietaryTime");
        User user  = (User)super.getCurrentUser();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp dietaryTime = null;
        if (StringUtil.isEmpty(dietaryTimeStr))
        {
            dietaryTime = new Timestamp(DateUtil.truncDay(new Date()).getTime());
        }
        else
        {
            dietaryTime = new Timestamp(DateUtil.getDateFromHtml(dietaryTimeStr).getTime());
        }
        // 查询条件
        DietaryLog dietaryLog = new DietaryLog();
        dietaryLog.setUserId(user.getId());
        dietaryLog.setDietaryTime(new Timestamp(dietaryTime.getTime()));
        List<DietaryLog> detailLogs = dietaryLogService.getDetailsLog(dietaryLog);
        List<FoodCatalog> foodCatalogs = foodService.getfoodCatalog();
        //查询相对应的食物分类对应的食物列表
        for(int i=0;i<detailLogs.size();i++){
            Map<String, Object> filter = new HashMap<String, Object>();
            String foodcatalogId = detailLogs.get(i).getFoodCatalogId();
            filter.put("catalogId", foodcatalogId);
            List<Food> foodList = foodService.getfood(filter);
            detailLogs.get(i).setFoodList(foodList);
        }
        request.setAttribute("dietaryTime", dietaryTime);
        request.setAttribute("now", now);
        request.setAttribute("detailLogs", detailLogs);
        request.setAttribute("foodCatalogs", foodCatalogs);
        return "edit";
    }
    
    /*
     * 添加膳食日志 通过表单
     * @author huangGang
     * 2014.10.13
     * */
    public String addDietaryLogs(){
        int state = 0;
        User user  = (User)super.getCurrentUser();
        /*String dietaryLogs = request.getParameter("dietaryLogs");*/
        String dietaryTimeStr = request.getParameter("dietaryTime");
        dietaryTimeStr = dietaryTimeStr+" 00:00:00";
        Timestamp dietaryTime = (Timestamp)DateUtil.getTimeByStr(dietaryTimeStr);
        //先删除原来保存的膳食日志，然后再添加
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("createBy", user.getId());
        filter.put("intakeTime", dietaryTime);
        dietaryLogService.deleteDietaryLogs(filter);
        //添加膳食
        String[] dietaryTypes = request.getParameterValues("foodType");
        String[] foodIds = request.getParameterValues("foodId");
        String[] foodWeights = request.getParameterValues("foodweight");
        String msg="0";
        System.out.println(dietaryTypes.length);
        try{

            Timestamp now = new Timestamp(System.currentTimeMillis());
            if(dietaryTypes.length>0){
                for(int i=0;i<dietaryTypes.length;i++){
                    DietaryLog dietaryLog = new DietaryLog();
                    dietaryLog.setDietaryTime(dietaryTime);
                    dietaryLog.setDietaryType(Integer.parseInt(dietaryTypes[i]));
                    dietaryLog.setFoodId(foodIds[i]);
                    dietaryLog.setUserId(user.getId());
                    dietaryLog.setFoodWeight(Double.parseDouble(foodWeights[i]));
                    dietaryLog.setCreateTime(now);
                    dietaryLog.setUpdateTime(now);
                    dietaryLog.setCreateBy(user.getId());
                    dietaryLog.setUpdateBy(user.getId());
                    Result r = dietaryLogService.addDietaryLog(dietaryLog);
                    String id = (String) r.getData(); 
                }
            }
           msg="1";
        }catch(Exception e){
            e.printStackTrace();
            msg="-3";
        }
        state=1;
        Result result = new Result();
        result.setState(state);
        result.setMsg(msg);
        super.write(result);
        return null;
    }
    
}
