package com.yzxt.yh.module.msg.bean;

import java.sql.Timestamp;

public class Knowledge implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String richtextId;
    private String catalogId;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;

    // 非持久化字段
    // 富文本信息
    private String summary;
    private String content;
    private String catalogName;
    private String updateByName;

    private int count;

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getRichtextId()
    {
        return this.richtextId;
    }

    public void setRichtextId(String richtextId)
    {
        this.richtextId = richtextId;
    }

    public String getCatalogId()
    {
        return this.catalogId;
    }

    public void setCatalogId(String catalogId)
    {
        this.catalogId = catalogId;
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

    public String getSummary()
    {
        return summary;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getCatalogName()
    {
        return catalogName;
    }

    public void setCatalogName(String catalogName)
    {
        this.catalogName = catalogName;
    }

    public String getUpdateByName()
    {
        return updateByName;
    }

    public void setUpdateByName(String updateByName)
    {
        this.updateByName = updateByName;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

}