package com.yzxt.yh.module.rgm.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Food implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String catalogId;
    private Double foodHeat;
    private Double protein;
    private Double fat;
    private Double carbohydrate;
    private Integer state;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;
    
    public Double getProtein()
    {
        return protein;
    }

    public void setProtein(Double protein)
    {
        this.protein = protein;
    }

    public Double getFat()
    {
        return fat;
    }

    public void setFat(Double fat)
    {
        this.fat = fat;
    }

    //非持久化对象
    private String parentCatalogId;
    private String catalogFullName;

    public String getParentCatalogId()
    {
        return parentCatalogId;
    }

    public void setParentCatalogId(String parentCatalogId)
    {
        this.parentCatalogId = parentCatalogId;
    }

    public String getCatalogFullName()
    {
        return catalogFullName;
    }

    public void setCatalogFullName(String catalogFullName)
    {
        this.catalogFullName = catalogFullName;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCatalogId()
    {
        return this.catalogId;
    }

    public void setCatalogId(String catalogId)
    {
        this.catalogId = catalogId;
    }

    public Double getFoodHeat()
    {
        return this.foodHeat;
    }

    public void setFoodHeat(Double foodHeat)
    {
        this.foodHeat = foodHeat;
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

    public Double getCarbohydrate()
    {
        return carbohydrate;
    }

    public void setCarbohydrate(Double carbohydrate)
    {
        this.carbohydrate = carbohydrate;
    }

}