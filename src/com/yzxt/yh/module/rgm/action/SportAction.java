package com.yzxt.yh.module.rgm.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.module.rgm.bean.Sport;
import com.yzxt.yh.module.rgm.service.SportService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

/**
 * @author huanggang
 * 维护运动
 */
public class SportAction extends BaseAction
{

    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(Sport.class);
    private SportService sportService;

    private Sport sport;

    public SportService getSportService()
    {
        return sportService;
    }

    public void setSportService(SportService sportService)
    {
        this.sportService = sportService;
    }

    public Sport getSport()
    {
        return sport;
    }

    public void setSport(Sport sport)
    {
        this.sport = sport;
    }

    public String list() throws Exception
    {
        String resInfo = "";
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            String keyword = request.getParameter("keyword");
            filter.put("keyword", keyword != null ? keyword.trim() : null);
            PageModel<Sport> list = sportService.getSportByPage(filter, getPage(), getRows());
            /*resInfo = getListByJson(list);*/
            super.write(list);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    //增加运动项目
    public String addSport() throws Exception
    {
        String msg = "0";
        try
        {
            User user = (User) super.getCurrentUser();
            int r = sportService.addSport(sport);
            //运动名已经存在
            if (r == -11)
            {
                msg = "-11";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            msg = "-3";
        }
        super.write(msg);
        return null;
    }

    public String updateSport() throws Exception
    {
        String msg = "0";
        try
        {
            User user = (User) super.getCurrentUser();
            int r = sportService.updateSport(sport);
            /*  //运动名已经存在
              if(r == -11){
                  msg="-11";
              }*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
            msg = "-3";
        }
        super.write(msg);
        return null;
    }

    public String deleteSport()
    {
        String msg = "0";
        try
        {
            User user = (User) super.getCurrentUser();
            int r = sportService.updateSportState(sport);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            msg = "-3";
        }
        super.write(msg);
        return null;
    }

    /**
     * 运动级联
     */

    public String getNativeJson()
    {

        String json = "";
        String sportId = getRequest().getParameter("sportId");
        String sportLevel = getRequest().getParameter("sportType");
        // 运动类型---1 运动名称---2
        try
        {
            List<Sport> sports = (List<Sport>) sportService.getSports(sportId, sportLevel);
            /*if (sports != null)
            {
                json = createJson(sports);
            }*/
            if (sports != null)
            {
                if (sportLevel != null)
                {
                    /*if(sportLevel.equals("1")||sportLevel.equals("2")){*/
                    json = createJson2(sports);
                    /*}*/
                }

            }
        }
        catch (Exception e)
        {
            json = "[]";
            e.printStackTrace();
        }
        logger.debug("==========================NativeJson=============================" + json);
        super.write(json);
        return null;

    }

    public String getNativeJson2()
    {

        String json = "";
        String sportId = getRequest().getParameter("sportId");
        String sportLevel = getRequest().getParameter("sportLevel");
        // 运动类型---1 运动名称---2
        try
        {
            List<Sport> sports = (List<Sport>) sportService.getSports(sportId, sportLevel);
            /*            if (sports != null)
                        {
                            json = createJson(sports);
                        }*/
            if (sports != null)
            {
                if (sportLevel != null)
                {
                    if (sportLevel.equals("1"))
                    {
                        json = createJson2(sports);
                    }
                }

            }
        }
        catch (Exception e)
        {
            json = "[]";
            e.printStackTrace();
        }
        logger.debug("==========================NativeJson=============================" + json);
        super.write(json);
        return null;

    }

    /**
     * 创建JSON数据
     * @param list
     * @param opt 是否是可选择项
     * @return
     */
    @SuppressWarnings("unused")
    private String createJson(List<Sport> list)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        int len = list != null ? list.size() : 0;
        int count = 0;
        for (int i = 0; i < len; i++)
        {
            Sport sport = list.get(i);
            if (count > 0)
            {
                sb.append(",");
            }
            sb.append("{");
            sb.append("\"sportId\":").append(GsonUtil.strToJsonArr(sport.getId(), ","));
            sb.append("\"sportType\":").append(GsonUtil.strToJsonArr(sport.getSportType().toString(), null));

            sb.append("}");
            count++;
        }
        sb.append("]");
        return sb.toString();
    }

    private String createJson2(List<Sport> list)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        int len = list != null ? list.size() : 0;
        int count = 0;

        for (int i = 0; i < len; i++)
        {
            Sport sport = list.get(i);
            if (count > 0)
            {
                sb.append(",");
            }
            sb.append("{");
            sb.append("\"sportId\":").append(GsonUtil.strToJsonArr(sport.getId(), ","));
            sb.append("\"sportName\":").append(GsonUtil.strToJsonArr(sport.getName(), null));

            sb.append("}");
            count++;
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 显示子目录JSON数据
     */
    public void listChildrenAskCatalogsByJson()
    {
        String sportType = request.getParameter("sportType");
        List<Sport> children = null;
        if (StringUtil.isNotEmpty(sportType))
        {
            children = sportService.getChildrenSports(sportType);
        }
        JsonArray retJson = new JsonArray();
        if (children != null && children.size() > 0)
        {
            for (Sport cata : children)
            {
                JsonObject jsonObj = new JsonObject();
                jsonObj.addProperty("id", cata.getId());
                jsonObj.addProperty("name", cata.getName());
                retJson.add(jsonObj);
            }
        }
        super.write(retJson);
    }

    public void getTypeList() throws Exception
    {
        String resInfo = "";
        try
        {

            Map<String, Object> filter = new HashMap<String, Object>();
            String keyword = request.getParameter("keyword");
            filter.put("keyword", keyword != null ? keyword.trim() : null);
            /*   ListPageBean list = sportService.getSportByPage(filter,getPage(),getRows());*/
            List<Sport> list = sportService.getTypeList(filter);
            /*List<Integer> queNums = new ArrayList<Integer>(list);*/
            JsonArray retJson = new JsonArray();
            if (list != null && list.size() > 0)
            {
                /*for(int i=0;i<list.size();i++){
                    JsonObject jsonObj = new JsonObject();
                    Sport sport= new  Sport();
                    Integer type = list.get(i);

                    jsonObj.addProperty("Type", type);
                    retJson.add(jsonObj);
                }*/
                for (Sport cata : list)
                {
                    JsonObject jsonObj = new JsonObject();
                    jsonObj.addProperty("id", cata.getId());
                    jsonObj.addProperty("name", cata.getSportType());
                    retJson.add(jsonObj);
                }

            }
            super.write(retJson);
            /*this.outHtmlDatas(resInfo);*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * 获得所有的食物信息 This method is used to get sport information
     * @author huangGang
     * 2014.12.2
     * @return
     */
    public String getSportInfo()
    {
        String res = "";
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            Integer sportType = Integer.parseInt(getRequest().getParameter("sportType"));
            filter.put("sportType", sportType);
            List<Sport> list = this.sportService.getSportInfo(filter);
           /* res = JSONUtil.listToJson(list, null);
            logger.debug("============all food.res=" + res);*/
            super.write(list);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
}
