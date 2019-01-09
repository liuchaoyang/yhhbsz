package com.yzxt.yh.module.rgm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.rgm.bean.Food;
import com.yzxt.yh.module.rgm.bean.FoodCatalog;
import com.yzxt.yh.module.rgm.service.FoodService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

public class FoodAction extends BaseAction
{
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(FoodAction.class);

    private FoodService foodService;

    private Food food;

    public FoodService getFoodService()
    {
        return foodService;
    }

    public void setFoodService(FoodService foodService)
    {
        this.foodService = foodService;
    }

    public Food getFood()
    {
        return food;
    }

    public void setFood(Food food)
    {
        this.food = food;
    }

    /**
     * 跳转到食物列表页面
     * @return
     */
    public String toList()
    {
        List<FoodCatalog> topLevelFoodCatalogs = foodService.getTopLevelFoodCatalogs();
        request.setAttribute("topLevelFoodCatalogs", topLevelFoodCatalogs);
        return "list";
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

    /**
     * 查询食物列表数据
     */
    public void list()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            String keyword = request.getParameter("keyword");
            String topLevelCatalogId = request.getParameter("topLevelCatalogId");
            String secondLevelCatalogId = request.getParameter("secondLevelCatalogId");
            filter.put("keyword", keyword != null ? keyword.trim() : null);
            filter.put("topLevelCatalogId", topLevelCatalogId);
            filter.put("secondLevelCatalogId", secondLevelCatalogId);
            PageModel<Food> page = foodService.queryFoodByPage(filter, getPage(), getRows());
            super.write(page);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 添加食物信息
     * @return
     * @throws Exception
     */
    public void add()
    {
        int state = 0;
        String msg = "";
        String newFoodId = null;
        try
        {
            // 用户
            User user  = (User)super.getCurrentUser();
            food.setCreateBy(user.getId());
            food.setUpdateBy(user.getId());
            Result r = foodService.add(food);
            state = r.getState();
            msg = r.getMsg();
            newFoodId = (String) r.getData();
        }
        catch (Exception e)
        {
            state = -1;
            msg = "保存失败！";
            logger.error(msg, e);
        }
        Result result = new Result();
        result.setState(state);
        result.setMsg(msg);
        result.setData(newFoodId);
        super.write(result);
    }

    /**
     * 更新食物信息
     * @return
     * @throws Exception
     */
    public void update()
    {
        int state = 0;
        String msg = "";
        try
        {
            // 用户
            User user  = (User)super.getCurrentUser();
            food.setUpdateBy(user.getId());
            // 更新
            Result r = foodService.update(food);
            state = r.getState();
            msg = r.getMsg();
        }
        catch (Exception e)
        {
            state = -1;
            msg = "保存出错！";
            logger.error(msg, e);
        }
        Result result = new Result();
        result.setData(food.getId());
        result.setState(state);
        result.setMsg(msg);
        super.write(result);
    }

    /**
     * 删除食物信息
     * @return
     * @throws Exception
     */
    public void delete()
    {
        int state = 0;
        String msg = "";
        try
        {
            String ids = request.getParameter("ids");
            // 验证通过，开始删除数据
            if (StringUtil.isEmpty(msg))
            {
                foodService.delete(StringUtil.splitWithoutEmpty(ids, ","));
                state = 1;
            }
        }
        catch (Exception e)
        {
            state = -1;
            msg = "删除出错！";
            logger.error(msg, e);
        }
        Result result = new Result();
        result.setState(state);
        result.setMsg(msg);
        super.write(result);
    }
    
    /**
     * 获得所有的食物分类信息 This method is used to get foodCatalog information
     * 
     * @return
     */
    public String getfoodCatalog() {
        String res = "";
        try {
            List<FoodCatalog> list = this.foodService.getfoodCatalog();
            /*res = JSONUtil.listToJson(list, null);
            logger.debug("============all foodCatalog.res=" + res);*/
            super.write(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * 获得所有的食物信息 This method is used to get food information
     * 
     * @return
     */
    public String getfood() {
        String res = "";
        try {
            Map<String, Object> filter = new HashMap<String, Object>();
            String catalogId = getRequest().getParameter("catalogId");
            filter.put("catalogId", catalogId != null ? catalogId.trim() : null);
            filter.put("catalogId", catalogId);
            List<Food> list = this.foodService.getfood(filter);
            /*res = JSONUtil.listToJson(list, null);
            logger.debug("============all food.res=" + res);*/
            super.write(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
