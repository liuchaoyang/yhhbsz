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
import com.yzxt.yh.module.msg.bean.Knowledge;
import com.yzxt.yh.module.msg.bean.KnowledgeCatalog;
import com.yzxt.yh.module.msg.service.KnowledgeCatalogService;
import com.yzxt.yh.module.msg.service.KnowledgeService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

/**
 * 健康知识客户端接口类
 * @author f
 *
 */
public class NlgTransAction extends BaseTranAction
{
    private static Logger logger = Logger.getLogger(NlgTransAction.class);

    private static final long serialVersionUID = 1L;

    private KnowledgeService knowledgeService;

    private KnowledgeCatalogService knowledgeCatalogService;

    public KnowledgeService getKnowledgeService()
    {
        return knowledgeService;
    }

    public void setKnowledgeService(KnowledgeService knowledgeService)
    {
        this.knowledgeService = knowledgeService;
    }

    public KnowledgeCatalogService getKnowledgeCatalogService()
    {
        return knowledgeCatalogService;
    }

    public void setKnowledgeCatalogService(KnowledgeCatalogService knowledgeCatalogService)
    {
        this.knowledgeCatalogService = knowledgeCatalogService;
    }

    /*    *//**
                            * 将知识列表转化为JSON对象
                            * @param know
                            * @return
                            */
    /*
    private JsonElement knowledgesToJsonArray(List<Knowledge> knows)
    {
     JsonArray retJson = new JsonArray();
     if (knows != null && knows.size() > 0)
     {
         String pathPre = "resources/file/file/";
         for (Knowledge know : knows)
         {
             JsonObject jsonObj = new JsonObject();
             jsonObj.addProperty("knowlId", know.getId());
             jsonObj.addProperty("title", know.getTitle());
             jsonObj.addProperty("content", know.getSummary());
             jsonObj.addProperty("isFavorite", know.getSummary());
             jsonObj.addProperty("url", "mobile/cnlg_viewInfo.do?id=" + know.getId());
             jsonObj.addProperty("createTime", DateUtil.toJsonStr(know.getUpdateTime()));
             retJson.add(jsonObj);
         }
     }
     return retJson;
    }*/

    /**
     * 分页查询健康知识列表
     * @throws Exception
     */
    public void listNlgs()
    {
        try
        {
            JsonObject paramJson = getParams();
            if (paramJson == null)
            {
                return;
            }
            JsonElement knowledgeCataIdEle = paramJson.get("parentId");
            String knowledgeCataId = knowledgeCataIdEle.getAsString();
            int pageSize = GsonUtil.toInt(paramJson.get("pageSize"), 10);
            if (StringUtil.isEmpty(knowledgeCataId))
            {
                super.write(ResultTran.STATE_ERROR, "健康知识目录ID不能为空.", null);
                return;
            }
            Timestamp synTime = DateUtil.getSynTimeByStr(GsonUtil.toString(paramJson.get("lastSynTime")));
            //返回值
            JsonArray retJson = new JsonArray();
            Map<String, Object> filterMap = new HashMap<String, Object>();
            filterMap.put("sysTime", synTime);
            filterMap.put("nlgCatalogId", knowledgeCataId);
            filterMap.put("maxSize", pageSize);
            List<Knowledge> knows = knowledgeService.queryKnowledgesBySyn(filterMap);
            if (knows != null && knows.size() > 0)
            {
                for (Knowledge know : knows)
                {
                    JsonObject jsonObj = new JsonObject();
                    jsonObj.addProperty("knowlId", know.getId());
                    jsonObj.addProperty("title", know.getTitle());
                    jsonObj.addProperty("intro", know.getSummary());
                    jsonObj.addProperty("parentId", know.getCatalogId());
                    jsonObj.addProperty("isFavorite", 0);
                    jsonObj.addProperty("url", "mobile/cnlg_viewNlg.do?id=" + know.getId());
                    jsonObj.addProperty("lastSynTime", DateUtil.toSynTimeStr(know.getUpdateTime()));
                    retJson.add(jsonObj);
                }
            }
            super.write(ResultTran.STATE_OK, "成功", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端获取知识列表失败.", e);
            super.write(ResultTran.STATE_ERROR, "失败", null);
        }
    }

    /**
     * 分页查询健康知识目录列表
     * @throws Exception
     */
    public void listCatas()
    {
        try
        {
            JsonObject paramJson = getParams();
            if (paramJson == null)
            {
                return;
            }
            Map<String, Object> filter = new HashMap<String, Object>();
            // Date synTime = getSynTime(paramJson.get("lastSynTime"));
            // filter.put("sysTime", synTime != null ? new Timestamp(synTime.getTime()) : null);
            // int pageSize = GsonUtil.toInt(paramJson.get("pageSize"), 10);
            String parentId = paramJson.get("parentId").getAsString();
            // filter.put("maxSize", pageSize);
            filter.put("parentId", parentId);
            List<KnowledgeCatalog> catalogs = knowledgeCatalogService.queryKnowledgeCatalogsBySyn(filter);
            // 返回值
            JsonArray retJson = new JsonArray();
            if (catalogs != null && catalogs.size() > 0)
            {
                for (KnowledgeCatalog nlgCata : catalogs)
                {
                    //knowlId 目录id 和知识id 对于返回知识列表，返回的是知识列表ID，对于返回的目录列表，返回的是目录ID
                    JsonObject jsonObj = new JsonObject();
                    jsonObj.addProperty("knowlId", nlgCata.getId());
                    jsonObj.addProperty("parentId", nlgCata.getParentId());
                    jsonObj.addProperty("title", nlgCata.getName());
                    jsonObj.addProperty("haveChildList", nlgCata.getIsLeaf());
                    jsonObj.addProperty("listLevel", nlgCata.getLevel());
                    retJson.add(jsonObj);
                }
            }
            super.write(ResultTran.STATE_OK, "成功", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端获取知识目录列表失败.", e);
            super.write(ResultTran.STATE_ERROR, "失败", null);
        }
    }

    /**
     * 查看健康知识信息，只显示
     * @throws IOException 
     * @throws Exception
     */
    public String viewNlg() throws IOException
    {

        try
        {
            HttpServletRequest request = super.request;
            String id = request.getParameter("id");
            Knowledge knowledge = knowledgeService.load(id);
            request.setAttribute("knowledge", knowledge);
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

}
