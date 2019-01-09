package com.yzxt.yh.module.cli.bean;
import java.sql.Timestamp;

/**
* 腕表配置修改类
* 2015.11.24
* @author h
*/
public class WbUpdate implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;
    private String wbId;
    private String configItem;
    private Integer status;
    private String configData;
    private Timestamp updateTime;
    private String updateBy;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getWbId()
    {
        return wbId;
    }

    public void setWbId(String wbId)
    {
        this.wbId = wbId;
    }

    public String getConfigItem()
    {
        return configItem;
    }

    public void setConfigItem(String configItem)
    {
        this.configItem = configItem;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getConfigData()
    {
        return configData;
    }

    public void setConfigData(String configData)
    {
        this.configData = configData;
    }

    public Timestamp getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getUpdateBy()
    {
        return updateBy;
    }

    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy;
    }

}