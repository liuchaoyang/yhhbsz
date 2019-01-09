package com.yzxt.tran;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.msg.bean.InfoCatalog;
import com.yzxt.yh.module.msg.bean.Information;
import com.yzxt.yh.module.msg.service.InfoCatalogService;
import com.yzxt.yh.module.msg.service.InformationService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.FileUtil;
import com.yzxt.yh.util.StringUtil;

/**
 * 信息资讯客户端接口类
 * @author f
 *
 */
public class InfoTransAction extends BaseTranAction
{
    private static Logger logger = Logger.getLogger(InfoTransAction.class);

    private static final long serialVersionUID = 1L;

    private InformationService informationService;

    private InfoCatalogService infoCatalogService;

    public InformationService getInformationService()
    {
        return informationService;
    }

    public void setInformationService(InformationService informationService)
    {
        this.informationService = informationService;
    }

    public InfoCatalogService getInfoCatalogService()
    {
        return infoCatalogService;
    }

    public void setInfoCatalogService(InfoCatalogService infoCatalogService)
    {
        this.infoCatalogService = infoCatalogService;
    }

    /**
     * 查询当前用户下的栏目列表
     * @throws Exception
     */
    public void getCols()
    {
        try
        {
            JsonObject paramJson = getParams();
            if (paramJson == null)
            {
                return;
            }
            User user = super.getOperUser();
            Integer userType = user != null ? user.getType() : null;
            if (userType == null)
            {
                userType = Constant.USER_TYPE_CUSTOMER;
            }
            List<InfoCatalog> columns = infoCatalogService.getInfoColumnsByUserType(userType, true);
            JsonArray retJson = new JsonArray();
            for (InfoCatalog cata : columns)
            {
                JsonObject jsonObj = new JsonObject();
                jsonObj.addProperty("id", cata.getId());
                jsonObj.addProperty("name", cata.getName());
                retJson.add(jsonObj);
            }
            super.write(ResultTran.STATE_OK, "成功", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端获取栏目列表失败.", e);
            super.write("-1", "参数错误,请重新输入！");
        }
    }

    /**
     * 将资讯列表转化为JSON对象
     * @param info
     * @return
     */
    private JsonElement infomationsToJsonArray(List<Information> infos)
    {
        JsonArray retJson = new JsonArray();
        if (infos != null && infos.size() > 0)
        {
            for (Information info : infos)
            {
                JsonObject jsonObj = new JsonObject();
                jsonObj.addProperty("id", info.getId());
                jsonObj.addProperty("title", info.getTitle());
                jsonObj.addProperty("author", info.getUpdateByName());
                jsonObj.addProperty("src", info.getSrc());
                jsonObj.addProperty("content", info.getSummary());
                jsonObj.addProperty("url", "mobile/cinfo_viewInfo.do?id=" + info.getId());
                jsonObj.addProperty("createTime", DateUtil.toSynTimeStr(info.getUpdateTime()));
                jsonObj.addProperty("imageUrl", "pub/cf_img.do?pt=" + FileUtil.encodePath(info.getIconFilePath()));
                jsonObj.addProperty("lastSynTime", DateUtil.toSynTimeStr(info.getUpdateTime()));
                retJson.add(jsonObj);
            }
        }
        return retJson;
    }

    /**
     * 分页查询查询栏目或专题下的资讯列表
     * @throws Exception
     */
    public void listInfos()
    {
        try
        {
            JsonObject paramJson = getParams();
            if (paramJson == null)
            {
                return;
            }
            User user = super.getOperUser();
            //JsonElement infoCataIdEle = paramJson.get("infoType");
            //String infoCataId = paramJson.get("infoType").getAsString();
            String infoCataId = GsonUtil.toString(paramJson.get("infoType"));
            if (StringUtil.isEmpty(infoCataId))
            {
                super.write(ResultTran.STATE_ERROR, "栏目或专题ID不能为空.", null);
                return;
            }
            Timestamp synTime = DateUtil.getSynTimeByStr(GsonUtil.toString(paramJson.get("lastSynTime")));
            InfoCatalog infoCatalog = infoCatalogService.getInfoCatalog(infoCataId);
            if (infoCatalog == null)
            {
                super.write(ResultTran.STATE_ERROR, "栏目或专题ID不能不存在.", null);
                return;
            }
            // 返回值
            JsonObject retJson = new JsonObject();
            // 如果是栏目，并且加载第一页，加载置顶资讯
            if (synTime == null && Constant.INFO_CATALOG_TYPE_COLUMN.equals(infoCatalog.getType()))
            {
                Map<String, Object> topFilterMap = new HashMap<String, Object>();
                topFilterMap.put("infoCatalogId", infoCataId);
                topFilterMap.put("infoCatalogType", Constant.INFO_CATALOG_TYPE_COLUMN);
                topFilterMap.put("infoLevel", Constant.INFO_LEVEL_TOP);
                topFilterMap.put("maxSize", 10);
                List<Information> topInfos = informationService.queryInfomationsBySyn(topFilterMap);
                retJson.add("head", infomationsToJsonArray(topInfos));
            }
            int pageSize = GsonUtil.toInt(paramJson.get("pageSize"), 10);
            // 普通列表信息
            Map<String, Object> filterMap = new HashMap<String, Object>();
            // 所属栏目或专题
            filterMap.put("infoCatalogId", infoCataId);
            // 权限参数
            filterMap.put("infoCatalogType", infoCatalog.getType());
            if (Constant.INFO_CATALOG_TYPE_TOPIC.equals(infoCatalog.getType()))
            {
                filterMap.put("userType", user.getType());
            }
            // 是否置顶
            filterMap.put("infoLevel", Constant.INFO_LEVEL_COMMON);
            // 拉取时间
            filterMap.put("sysTime", synTime);
            filterMap.put("maxSize", pageSize);
            List<Information> infos = informationService.queryInfomationsBySyn(filterMap);
            retJson.add("list", infomationsToJsonArray(infos));
            super.write(ResultTran.STATE_OK, "成功", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端获取资讯列表失败.", e);
            super.write(ResultTran.STATE_ERROR, "失败", null);
        }
    }

    /**
     * 分页查询查专题列表
     * @throws Exception
     */
    public void listTopics()
    {
        try
        {
            JsonObject paramJson = getParams();
            if (paramJson == null)
            {
                return;
            }
            Timestamp synTime = DateUtil.getSynTimeByStr(GsonUtil.toString(paramJson.get("lastSynTime")));
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("sysTime", synTime);
            int pageSize = GsonUtil.toInt(paramJson.get("pageSize"), 10);
            filter.put("maxSize", pageSize);
            List<InfoCatalog> topics = informationService.queryTopicsBySyn(filter);
            // 返回值
            JsonArray retJson = new JsonArray();
            // 如果是栏目，并且加载第一页，加载置顶资讯
            if (topics != null && topics.size() > 0)
            {
                for (InfoCatalog topic : topics)
                {
                    JsonObject jsonObj = new JsonObject();
                    jsonObj.addProperty("id", topic.getId());
                    jsonObj.addProperty("title", topic.getName());
                    jsonObj.addProperty("content", topic.getDetail());
                    jsonObj.addProperty("imageUrl", "pub/cf_img.do?pt=" + FileUtil.encodePath(topic.getIconFilePath()));
                    jsonObj.addProperty("createTime", DateUtil.toSynTimeStr(topic.getUpdateTime()));
                    jsonObj.addProperty("lastSynTime", DateUtil.toSynTimeStr(topic.getUpdateTime()));
                    retJson.add(jsonObj);
                }
            }
            super.write(ResultTran.STATE_OK, "成功", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端获取专题列表失败.", e);
            super.write(ResultTran.STATE_ERROR, "失败", null);
        }
    }

    /**
     * 查看资讯信息，只显示
     * @throws IOException 
     * @throws Exception
     */
    public String viewInfo() throws IOException
    {
        try
        {
            HttpServletRequest request = super.request;
            String id = request.getParameter("id");
            Information information = informationService.load(id);
            request.setAttribute("information", information);
            request.setAttribute("editable", false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            HttpServletResponse response = super.response;
            response.getWriter().write("出错了！");
        }
        return "view";
    }
    /**
     * 查看资讯信息，只显示
     * @throws IOException 
     * @throws Exception
     */
    public void view() throws IOException
    {
        try
        {
        	JsonObject obj = super.getParams();
            initQuery(obj);
            String id = GsonUtil.toString(obj.get("id"));
            Information information = informationService.load(id);
            
            super.write(ResultTran.STATE_OK, "成功", information);
        }
        catch (Exception e)
        {
        	 logger.error("查看详情失败.", e);
             super.write(ResultTran.STATE_ERROR, "失败", null); 
        }
    }
}
