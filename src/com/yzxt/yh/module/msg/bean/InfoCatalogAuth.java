package com.yzxt.yh.module.msg.bean;

public class InfoCatalogAuth implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String columnId;
    private Integer authUserType;

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getColumnId()
    {
        return this.columnId;
    }

    public void setColumnId(String columnId)
    {
        this.columnId = columnId;
    }

    public Integer getAuthUserType()
    {
        return this.authUserType;
    }

    public void setAuthUserType(Integer authUserType)
    {
        this.authUserType = authUserType;
    }

}