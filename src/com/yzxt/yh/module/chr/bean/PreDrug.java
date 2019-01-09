package com.yzxt.yh.module.chr.bean;

import com.google.gson.annotations.SerializedName;

public class PreDrug implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String hbpDrugsName;
    @SerializedName("hbpDrugsFrency")
    private Integer hbpDrugsFy;
    private Integer hbpDrugsCount;
    private String HId;

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

    public String getHId()
    {
        return this.HId;
    }

    public void setHId(String HId)
    {
        this.HId = HId;
    }

}