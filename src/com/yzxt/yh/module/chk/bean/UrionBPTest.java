package com.yzxt.yh.module.chk.bean;

import java.sql.Timestamp;

public class UrionBPTest implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;
    private String imei;
    private String user;
    private Integer sys;
    private Integer dia;
    private Integer pul;
    private String ano;
    private Timestamp checkTime;
    private Timestamp createTime;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getImei()
    {
        return imei;
    }

    public void setImei(String imei)
    {
        this.imei = imei;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public Integer getSys()
    {
        return sys;
    }

    public void setSys(Integer sys)
    {
        this.sys = sys;
    }

    public Integer getDia()
    {
        return dia;
    }

    public void setDia(Integer dia)
    {
        this.dia = dia;
    }

    public Integer getPul()
    {
        return pul;
    }

    public void setPul(Integer pul)
    {
        this.pul = pul;
    }

    public String getAno()
    {
        return ano;
    }

    public void setAno(String ano)
    {
        this.ano = ano;
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

}