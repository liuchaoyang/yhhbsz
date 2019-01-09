package com.yzxt.yh.module.msg.bean;

import java.sql.Timestamp;

public class InfoCatalog implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;
    private String id;
    private Integer type;
    private String name;
    private String detail;
    private Integer predefined;
    private Integer state;
    private Integer seq;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;

    // 非持久化字段
    // 专题图标路径
    private String[] userTypes;
    private String iconFilePath;

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Integer getType()
    {
        return this.type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDetail()
    {
        return this.detail;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    public Integer getPredefined()
    {
        return this.predefined;
    }

    public void setPredefined(Integer predefined)
    {
        this.predefined = predefined;
    }

    public Integer getState()
    {
        return this.state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public Integer getSeq()
    {
        return this.seq;
    }

    public void setSeq(Integer seq)
    {
        this.seq = seq;
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

    public String[] getUserTypes()
    {
        return userTypes;
    }

    public void setUserTypes(String[] userTypes)
    {
        this.userTypes = userTypes;
    }

    public String getIconFilePath()
    {
        return iconFilePath;
    }

    public void setIconFilePath(String iconFilePath)
    {
        this.iconFilePath = iconFilePath;
    }

}