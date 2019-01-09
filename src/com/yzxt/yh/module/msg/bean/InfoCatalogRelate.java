package com.yzxt.yh.module.msg.bean;

public class InfoCatalogRelate implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String infoId;
    private String catalogId;

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getInfoId()
    {
        return this.infoId;
    }

    public void setInfoId(String infoId)
    {
        this.infoId = infoId;
    }

    public String getCatalogId()
    {
        return this.catalogId;
    }

    public void setCatalogId(String catalogId)
    {
        this.catalogId = catalogId;
    }

}