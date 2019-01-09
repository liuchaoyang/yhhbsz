package com.yzxt.yh.module.chk.bean;

import java.sql.Timestamp;

import com.google.gson.annotations.SerializedName;

public class AnalysisUricAcid implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String custId;
    private String deviceName;
    private String deviceMac;
    private Double latitude;
    private Double longitude;
    private String unit;
    private String leu;
    private String nit;
    private String pro;
    private String glu;
    private String ket;
    private String ubg;
    private String bil;
    private Double ph;
    private Double sg;
    private String bld;
    private String vc;
    private Integer level;
    private String descript;
    private Timestamp checkTime;
    @SerializedName("uploadTime")
    private Timestamp createTime;
    private Integer checkType;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCustId()
    {
        return custId;
    }

    public void setCustId(String custId)
    {
        this.custId = custId;
    }

    public String getDeviceName()
    {
        return deviceName;
    }

    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }

    public String getDeviceMac()
    {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac)
    {
        this.deviceMac = deviceMac;
    }

    public Double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    public Double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getLeu()
    {
        return leu;
    }

    public void setLeu(String leu)
    {
        this.leu = leu;
    }

    public String getNit()
    {
        return nit;
    }

    public void setNit(String nit)
    {
        this.nit = nit;
    }

    public String getPro()
    {
        return pro;
    }

    public void setPro(String pro)
    {
        this.pro = pro;
    }

    public String getGlu()
    {
        return glu;
    }

    public void setGlu(String glu)
    {
        this.glu = glu;
    }

    public String getKet()
    {
        return ket;
    }

    public void setKet(String ket)
    {
        this.ket = ket;
    }

    public String getUbg()
    {
        return ubg;
    }

    public void setUbg(String ubg)
    {
        this.ubg = ubg;
    }

    public String getBil()
    {
        return bil;
    }

    public void setBil(String bil)
    {
        this.bil = bil;
    }

    public Double getPh()
    {
        return ph;
    }

    public void setPh(Double ph)
    {
        this.ph = ph;
    }

    public Double getSg()
    {
        return sg;
    }

    public void setSg(Double sg)
    {
        this.sg = sg;
    }

    public String getBld()
    {
        return bld;
    }

    public void setBld(String bld)
    {
        this.bld = bld;
    }

    public String getVc()
    {
        return vc;
    }

    public void setVc(String vc)
    {
        this.vc = vc;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public String getDescript()
    {
        return descript;
    }

    public void setDescript(String descript)
    {
        this.descript = descript;
    }

    public Timestamp getCheckTime()
    {
        return checkTime;
    }

    public void setCheckTime(Timestamp checkTime)
    {
        this.checkTime = checkTime;
    }

    public Timestamp getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    public Integer getCheckType()
    {
        return checkType;
    }

    public void setCheckType(Integer checkType)
    {
        this.checkType = checkType;
    }

}