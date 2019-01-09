package com.yzxt.yh.module.rgm.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Sport implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private Integer sportType;
    private Integer isAerobicExercise;
    private Double sportHeat;
    private Integer state;
    private Timestamp createTime;
    private Timestamp updateTime;
    
    public Integer getIsAerobicExercise()
    {
        return isAerobicExercise;
    }

    public void setIsAerobicExercise(Integer isAerobicExercise)
    {
        this.isAerobicExercise = isAerobicExercise;
    }

   
    public Double getSportHeat()
    {
        return sportHeat;
    }

    public void setSportHeat(Double sportHeat)
    {
        this.sportHeat = sportHeat;
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

    public Integer getSportType()
    {
        return this.sportType;
    }

    public void setSportType(Integer sportType)
    {
        this.sportType = sportType;
    }

    public Timestamp getCreateTime()
    {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }


    public Timestamp getUpdateTime()
    {
        return this.updateTime;
    }

    public void setUpdateTime(Timestamp updateTime)
    {
        this.updateTime = updateTime;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

}