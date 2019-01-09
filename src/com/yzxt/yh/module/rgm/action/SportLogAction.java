package com.yzxt.yh.module.rgm.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.module.rgm.bean.Sport;
import com.yzxt.yh.module.rgm.bean.SportLog;
import com.yzxt.yh.module.rgm.service.SportLogService;
import com.yzxt.yh.module.rgm.service.SportService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;


/**
 * @author huanggang
 * 运动日志 
 * 2014.9.29
 */
public class SportLogAction extends BaseAction
{
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(SportLog.class);
    private SportService sportService;
    private SportLogService sportLogService;
    private SportLog sportLog;
    
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
     * @author huanggang
     * 运动日志总列表 
     * 2014.9.29
     */ 
    public void listSportLog(){
        try{
            User user  = (User)super.getCurrentUser();
            Map<String,Object> filter = new HashMap<String,Object>();
            String beginTime = request.getParameter("beginTime");
            filter.put("beginTime",beginTime != null ? beginTime.trim() : null);
            String endTime = request.getParameter("endTime");
            filter.put("endTime", endTime != null ? endTime.trim(): null);
            filter.put("createBy", user.getId());
            PageModel<SportLog> list = sportLogService.getSportLogList(filter,getPage(),getRows());
            /*resInfo = getListByJson(list);*/
            super.write(list);
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
        String sportTime = request.getParameter("sportTime");
        sportTime = sportTime.substring(0,10);
        /*SportLog sportLog = sportLogService.getSportLog(sportTime);*/
        request.setAttribute("sportTime", sportTime);
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
        String sportTimeStr = request.getParameter("sportTime");
        sportTimeStr = sportTimeStr+" 00:00:00";
        Timestamp sportTime = (Timestamp)DateUtil.getTimeByStr(sportTimeStr);
        Map<String, Object> filter = new HashMap<String, Object>();
        /*filter.put("sportTime", sportTime);
        filter.put("createBy", user.getId());*/
        SportLog sportLog = new SportLog();
        sportLog.setUserId(createBy);
        sportLog.setCreateBy(createBy);
        sportLog.setSportTime(sportTime);
        List<SportLog> detailsLog = sportLogService.getDetailsLog(sportLog);
        /*List<String> jsonList = new ArrayList<String>();
        int len = detailsLog != null ? detailsLog.size() : 0;
        for (int i = 0; i < len; i++)
        {
            String jsonStr = sonUtil.jsonArrToString(detailsLog.get(i), ",");
            jsonList.add(jsonStr);
        }
        filter.put("rows", jsonList);
        JSONObject js = JSONObject.fromObject(filter);*/
        super.write(detailsLog);

        return null;
    }
    
    /*
     * 添加运动日志
     * @author huangGang
     * 2014.10.14
     * */
    public String add(){
        int state = 0;
        String msg = "";
        try{
            User user  = (User)super.getCurrentUser();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            String sportId = request.getParameter("sportId");
            String sportTypeStr = request.getParameter("sportType");
            Integer sportType = Integer.parseInt(sportTypeStr);
            String sportTimeStr = request.getParameter("time");
            sportTimeStr = sportTimeStr+" 00:00:00";
            Timestamp sportTime = (Timestamp)DateUtil.getTimeByStr(sportTimeStr);
            String timeSpanStr = request.getParameter("timeSpan");
            Integer timeSpan = Integer.parseInt(timeSpanStr);
            Double sportHeat= sportService.getEnergyById(sportId);
            Double sportEnergy = sportHeat*timeSpan;
            SportLog sportLog = new SportLog();
            sportLog.setSportId(sportId);
            sportLog.setSportTime(sportTime);
            sportLog.setTimeSpan(timeSpan);
            sportLog.setUserId(user.getId());
            sportLog.setCreateBy(user.getId());
            sportLog.setCreateTime(now);
            sportLog.setUpdateBy(user.getId());
            sportLog.setUpdateTime(now);
            Result r = sportLogService.addSportLog(sportLog);
            String id = (String) r.getData();
        }catch(Exception e){
            state = -1;
            msg = "保存失败！";
            logger.error(msg, e);
        }
        state=1;
        Result result = new Result();
        result.setState(state);
        result.setMsg(msg);
        super.write(result);
        return "addLog";
    }
    
    /**
     * 跳转到填写日志页面
     * @author huangGang
     * 2014.12.2
     * @return
     * @throws Exception 
     */
    public String toEdit() throws Exception
    {
        String sportTimeStr = request.getParameter("sportTime");
        User user  = (User)super.getCurrentUser();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp sportTime = null;
        if (StringUtil.isEmpty(sportTimeStr))
        {
            sportTime = new Timestamp(DateUtil.truncDay(new Date()).getTime());
        }
        else
        {
            sportTime = new Timestamp(DateUtil.getDateFromHtml(sportTimeStr).getTime());
        }
        // 查询条件
        SportLog sportLog = new SportLog();
        sportLog.setUserId(user.getId());
        sportLog.setSportTime(new Timestamp(sportTime.getTime()));
        List<SportLog> sportLogs = sportLogService.getDetailsLog(sportLog);
        /* List<FoodCatalog> foodCatalogs = sportService.getfoodCatalog();*/
        //查询相对应的食物分类对应的食物列表
        for (int i = 0; i < sportLogs.size(); i++)
        {
            /*Map<String, Object> filter = new HashMap<String, Object>();
            String foodcatalogId = sportLogs.get(i).getFoodCatalogId();
            filter.put("catalogId", foodcatalogId);*/
            List<Sport> sportList = sportService.getSportForClient();
            sportLogs.get(i).setSportList(sportList);
            /*sportLogs.get(i).setFoodList(sportList);*/
        }
        request.setAttribute("sportTime", sportTime);
        request.setAttribute("now", now);
        request.setAttribute("sportLogs", sportLogs);
        /*request.setAttribute("foodCatalogs", foodCatalogs);*/
        return "edit";
    }
    
    /*
     * 添加膳食日志 通过表单
     * @author huangGang
     * 2014.10.13
     * */
    public String addSportLogs(){
        int state = 0;
        User user  = (User)super.getCurrentUser();
        /*String dietaryLogs = request.getParameter("dietaryLogs");*/
        String sportTimeStr = request.getParameter("sportTime");
        sportTimeStr = sportTimeStr+" 00:00:00";
        Timestamp sportTime = (Timestamp)DateUtil.getTimeByStr(sportTimeStr);
        //先删除原来保存的膳食日志，然后再添加
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("createBy", user.getId());
        filter.put("sportTime", sportTime);
        sportLogService.deleteSportLogs(filter);
        //添加运动日志
        String[] sportTypes = request.getParameterValues("sportType");
        String[] sportIds = request.getParameterValues("sportId");
        String[] timeSpans = request.getParameterValues("timeSpan");
        String msg="0";
        System.out.println(sportTypes.length);
        try{

            Timestamp now = new Timestamp(System.currentTimeMillis());
            if(sportTypes.length>0){
                for(int i=0;i<sportTypes.length;i++){
                    SportLog sportLog = new SportLog();
                    sportLog.setSportTime(sportTime);
                    sportLog.setSportType(Integer.parseInt(sportTypes[i]));
                    sportLog.setSportId(sportIds[i]);
                    sportLog.setUserId(user.getId());
                    sportLog.setTimeSpan(Integer.parseInt(timeSpans[i]));
                    sportLog.setCreateTime(now);
                    sportLog.setUpdateTime(now);
                    sportLog.setCreateBy(user.getId());
                    sportLog.setUpdateBy(user.getId());
                    Result r = sportLogService.addSportLog(sportLog);
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
