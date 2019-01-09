package com.yzxt.yh.module.chk.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class CheckWarn implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String custId;
    private String type;
    private Integer level;
    private String descript;
    private Integer state;
    private Timestamp warnTime;

    //擴展字段
    private String descript1;
    private Integer sex;
    private Date birthday;
    private Integer healthyStatus;
    private String name;
    private String idCard;
    private Integer noDealNum;
    private String phone;
    private String phone1;

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

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
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

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public Timestamp getWarnTime()
    {
        return warnTime;
    }

    public void setWarnTime(Timestamp warnTime)
    {
        this.warnTime = warnTime;
    }

    public String getDescript1()
    {
        return descript1;
    }

    public void setDescript1(String descript1)
    {
        this.descript1 = descript1;
    }

    public Integer getSex()
    {
        return sex;
    }

    public void setSex(Integer sex)
    {
        this.sex = sex;
    }

    public Date getBirthday()
    {
        return birthday;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public Integer getHealthyStatus()
    {
        return healthyStatus;
    }

    public void setHealthyStatus(Integer healthyStatus)
    {
        this.healthyStatus = healthyStatus;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public Integer getNoDealNum()
    {
        return noDealNum;
    }

    public void setNoDealNum(Integer noDealNum)
    {
        this.noDealNum = noDealNum;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone1()
    {
        return phone1;
    }

    public void setPhone1(String phone1)
    {
        this.phone1 = phone1;
    }

}