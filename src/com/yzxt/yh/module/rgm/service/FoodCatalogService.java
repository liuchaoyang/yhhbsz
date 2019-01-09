package com.yzxt.yh.module.rgm.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.rgm.bean.FoodCatalog;
import com.yzxt.yh.module.rgm.dao.FoodCatalogDao;

@Transactional(ConstTM.DEFAULT)
public class FoodCatalogService
{
    private FoodCatalogDao foodCatalogDao;

    public FoodCatalogDao getFoodCatalogDao()
    {
        return foodCatalogDao;
    }

    public void setFoodCatalogDao(FoodCatalogDao foodCatalogDao)
    {
        this.foodCatalogDao = foodCatalogDao;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<FoodCatalog> getFoodCatalogForClient()
    {
        return foodCatalogDao.getFoodCatalogForClient();
    }
}
