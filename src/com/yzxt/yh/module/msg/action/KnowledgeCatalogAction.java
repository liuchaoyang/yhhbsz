package com.yzxt.yh.module.msg.action;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.BaseAction;
import com.yzxt.yh.module.msg.bean.KnowledgeCatalog;
import com.yzxt.yh.module.msg.service.KnowledgeCatalogService;
import com.yzxt.yh.util.StringUtil;

/**
 * 健康知识目录Action类
 * @author f
 *
 */
public class KnowledgeCatalogAction extends BaseAction
{
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(KnowledgeCatalogAction.class);

    private KnowledgeCatalogService knowledgeCatalogService;

    private KnowledgeCatalog knowledgeCatalog;

    public KnowledgeCatalogService getKnowledgeCatalogService()
    {
        return knowledgeCatalogService;
    }

    public void setKnowledgeCatalogService(KnowledgeCatalogService knowledgeCatalogService)
    {
        this.knowledgeCatalogService = knowledgeCatalogService;
    }

    public KnowledgeCatalog getKnowledgeCatalog()
    {
        return knowledgeCatalog;
    }

    public void setKnowledgeCatalog(KnowledgeCatalog knowledgeCatalog)
    {
        this.knowledgeCatalog = knowledgeCatalog;
    }

    /**
     * 将目录转变为json对象
     * @param cata
     * @return
     */
    private JsonObject treeNodeToJson(KnowledgeCatalog cata)
    {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("id", cata.getId());
        jsonObj.addProperty("text", cata.getName());
        jsonObj.addProperty("state", cata.getIsLeaf() != null && cata.getIsLeaf() > 0 ? "open" : "closed");
        JsonObject attrsObj = new JsonObject();
        attrsObj.addProperty("isLeaf", cata.getIsLeaf());
        attrsObj.addProperty("fullId", cata.getFullId());
        jsonObj.add("attributes", attrsObj);
        return jsonObj;
    }

    /**
     * 目录树获取子节点
     * easyui树节点属性
     *   id：节点ID，对加载远程数据很重要。 
     *   text：显示节点文本。
     *   state：节点状态，'open' 或 'closed'，默认：'open'。如果为'closed'的时候，将不自动展开该节点。
     *   checked：表示该节点是否被选中。
     *   attributes: 被添加到节点的自定义属性。
     *   children: 一个节点数组声明了若干节点。
     */
    public void getTreeNodeChildren()
    {
        String parentId = request.getParameter("id");
        try
        {
            List<KnowledgeCatalog> children = knowledgeCatalogService.getTreeNodeChildren(parentId);
            JsonArray retJson = new JsonArray();
            int len = children != null ? children.size() : 0;
            // 如果是展示根节点，并且一级节点的数量小时10，则同时展示二级节点
            boolean showSubChild = StringUtil.isEmpty(parentId) || len <= 10;
            for (int i = 0; i < len; i++)
            {
                KnowledgeCatalog cata = children.get(i);
                JsonObject jsonObj = treeNodeToJson(cata);
                if (showSubChild)
                {
                    JsonArray subChildrenJson = new JsonArray();
                    List<KnowledgeCatalog> subChildren = knowledgeCatalogService.getTreeNodeChildren(cata.getId());
                    for (KnowledgeCatalog subChild : subChildren)
                    {
                        subChildrenJson.add(treeNodeToJson(subChild));
                    }
                    jsonObj.add("children", subChildrenJson);
                }
                retJson.add(jsonObj);
            }
            super.write(retJson);
        }
        catch (Exception e)
        {
            logger.error("获取知识目录ID:" + parentId + "子目录失败.", e);
        }
    }

}
