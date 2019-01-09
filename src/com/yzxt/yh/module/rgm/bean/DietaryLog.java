package com.yzxt.yh.module.rgm.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class DietaryLog implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private Integer dietaryType;
    private Timestamp dietaryTime;
    private String foodId;
    private Double foodWeight;
    private String userId;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;
   
    //其他属性
    private Double intakeEnergy;
    private String name;
    private List<Food> foodList;
    //管理膳食分类
    private String foodCatalogId;
    private String dietaryCatalogName;
    private String dietaryParentId;

    public List<Food> getFoodList()
    {
        return foodList;
    }

    public void setFoodList(List<Food> foodList)
    {
        this.foodList = foodList;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getFoodCatalogId()
    {
        return foodCatalogId;
    }

    public void setFoodCatalogId(String foodCatalogId)
    {
        this.foodCatalogId = foodCatalogId;
    }

    public Double getIntakeEnergy()
    {
        return intakeEnergy;
    }

    public void setIntakeEnergy(Double intakeEnergy)
    {
        this.intakeEnergy = intakeEnergy;
    }

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Integer getDietaryType()
    {
        return this.dietaryType;
    }

    public void setDietaryType(Integer dietaryType)
    {
        this.dietaryType = dietaryType;
    }

    public Timestamp getDietaryTime()
    {
        return this.dietaryTime;
    }

    public void setDietaryTime(Timestamp dietaryTime)
    {
        this.dietaryTime = dietaryTime;
    }

    public String getFoodId()
    {
        return this.foodId;
    }

    public void setFoodId(String foodId)
    {
        this.foodId = foodId;
    }

    public Double getFoodWeight()
    {
        return foodWeight;
    }

    public void setFoodWeight(Double foodWeight)
    {
        this.foodWeight = foodWeight;
    }

    public String getCreateBy()
    {
        return this.createBy;
    }

    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }

    public Timestamp getCreateTime()
    {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    public String getUpdateBy()
    {
        return this.updateBy;
    }

    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy;
    }

    public Timestamp getUpdateTime()
    {
        return this.updateTime;
    }

    public void setUpdateTime(Timestamp updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

}