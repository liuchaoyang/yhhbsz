package com.yzxt.yh.module.sys.vo;

import java.sql.Timestamp;
import java.util.Date;

public class CustFamilyAuditVO implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    private String name;
    private String idCard;
    private Integer sex;
    private Integer age;
    private Date birthday;
    private String phone;
    private Integer state;
    private Timestamp auditTime;
    private Timestamp applyTime;
    private String id;
    private String applyContext;
    private Integer relation;
    private String userId;

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public Integer getRelation()
    {
        return relation;
    }

    public void setRelation(Integer relation)
    {
        this.relation = relation;
    }

    public String getApplyContext()
    {
        return applyContext;
    }

    public void setApplyContext(String applyContext)
    {
        this.applyContext = applyContext;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Date getBirthday()
    {
        return birthday;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
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

    public Integer getSex()
    {
        return sex;
    }

    public void setSex(Integer sex)
    {
        this.sex = sex;
    }

    public Integer getAge()
    {
        return age;
    }

    public void setAge(Integer age)
    {
        this.age = age;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public Timestamp getAuditTime()
    {
        return auditTime;
    }

    public void setAuditTime(Timestamp auditTime)
    {
        this.auditTime = auditTime;
    }

    public Timestamp getApplyTime()
    {
        return applyTime;
    }

    public void setApplyTime(Timestamp applyTime)
    {
        this.applyTime = applyTime;
    }

}