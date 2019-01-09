package com.yzxt.yh.module.chr.bean;

import com.google.gson.annotations.SerializedName;

public class GlyDrug implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    @SerializedName("glyDrugsName")
    private String hbpDrugsName;
    @SerializedName("glyDrugsFrency")
    private Integer hbpDrugsFy;
    @SerializedName("glyDrugsCount")
    private Integer hbpDrugsCount;
    private Integer type;
    private String BId;
    private String glyInsulinType;
    private String glyInsulinUseMethod;

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getHbpDrugsName()
    {
        return this.hbpDrugsName;
    }

    public void setHbpDrugsName(String hbpDrugsName)
    {
        this.hbpDrugsName = hbpDrugsName;
    }

    public Integer getHbpDrugsFy()
    {
        return this.hbpDrugsFy;
    }

    public void setHbpDrugsFy(Integer hbpDrugsFy)
    {
        this.hbpDrugsFy = hbpDrugsFy;
    }

    public Integer getHbpDrugsCount()
    {
        return this.hbpDrugsCount;
    }

    public void setHbpDrugsCount(Integer hbpDrugsCount)
    {
        this.hbpDrugsCount = hbpDrugsCount;
    }

    public Integer getType()
    {
        return this.type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public String getBId()
    {
        return this.BId;
    }

    public void setBId(String BId)
    {
        this.BId = BId;
    }

    public String getGlyInsulinType()
    {
        return this.glyInsulinType;
    }

    public void setGlyInsulinType(String glyInsulinType)
    {
        this.glyInsulinType = glyInsulinType;
    }

    public String getGlyInsulinUseMethod()
    {
        return this.glyInsulinUseMethod;
    }

    public void setGlyInsulinUseMethod(String glyInsulinUseMethod)
    {
        this.glyInsulinUseMethod = glyInsulinUseMethod;
    }

}