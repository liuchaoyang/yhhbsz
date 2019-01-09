package com.yzxt.yh.module.rgm.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.rgm.bean.Food;
import com.yzxt.yh.module.rgm.bean.FoodCatalog;
import com.yzxt.yh.module.rgm.dao.FoodCatalogDao;
import com.yzxt.yh.module.rgm.dao.FoodDao;

@Transactional(ConstTM.DEFAULT)
public class FoodService
{
    private FoodDao foodDao;

    private FoodCatalogDao foodCatalogDao;

    public FoodDao getFoodDao()
    {
        return foodDao;
    }

    public void setFoodDao(FoodDao foodDao)
    {
        this.foodDao = foodDao;
    }

    public FoodCatalogDao getFoodCatalogDao()
    {
        return foodCatalogDao;
    }

    public void setFoodCatalogDao(FoodCatalogDao foodCatalogDao)
    {
        this.foodCatalogDao = foodCatalogDao;
    }

    /**
     * 获取食物顶级目录
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<FoodCatalog> getTopLevelFoodCatalogs()
    {
        return foodCatalogDao.getTopLevelFoodCatalogs();
    }

    /**
     * 获取子目录
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<FoodCatalog> getChildrenFoodCatalogs(String parentCatalogId)
    {
        return foodCatalogDao.getChildrenFoodCatalogs(parentCatalogId);
    }

    /**
     * 分页查询食物信息
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Food> queryFoodByPage(Map<String, Object> filter, int page, int pageSize) throws Exception
    {
        return foodDao.queryFoodByPage(filter, page, pageSize);
    }

    /**
     * 添加食物
     * @param food
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result add(Food food) throws Exception
    {
        Result result = new Result();
        // 验证
        // 判断名称是否存在
        boolean isExist = foodDao.isFoodNameExist(food.getName(), null);
        if (isExist)
        {
            result.setState(-1);
            result.setMsg("食物名称已经存在.");
            return result;
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        food.setCreateTime(now);
        food.setUpdateTime(now);
        food.setState(Constant.FOOD_STATE_ENABLE);
        String id = foodDao.insert(food);
        result.setData(id);
        result.setState(Result.STATE_SUCESS);
        return result;
    }

    /**
     * 更新食物
     * @param food
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result update(Food food) throws Exception
    {
        Result result = new Result();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String foodId = food.getId();
        // 查询未修改的资讯信息
        Food oldFood = foodDao.get(foodId);
        if (oldFood == null || !Constant.FOOD_STATE_ENABLE.equals(oldFood.getState()))
        {
            result.setState(-2);
            result.setMsg("修改出错，此食物信息删除！");
            return result;
        }
        food.setUpdateTime(now);
        food.setState(Constant.FOOD_STATE_ENABLE);
        // 保存食物
        foodDao.update(food);
        result.setState(Result.STATE_SUCESS);
        return result;
    }

    /**
     * 加载食物信息
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Food get(String id)
    {
        return foodDao.get(id);
    }

    /**
     * 删除食物信息
     * @param id
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int delete(String[] ids) throws Exception
    {
        int c = 0;
        for (String id : ids)
        {
            foodDao.delete(id);
            c++;
        }
        return c;
    }

    /**
     *客户端更新食物数据接口
     *查询到所有的食物分类和食物列表
     * @author huangGang
     * 2014.11.18
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Food> getFoodForClient()
    {
        return foodDao.getFoodForClient();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<FoodCatalog> getfoodCatalog()
    {
        return foodCatalogDao.getfoodCatalog();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Food> getfood(Map<String, Object> filter)
    {
        return foodDao.getFood(filter);
    }

}
