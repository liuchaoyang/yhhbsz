package com.yzxt.yh.module.sys.bean;

import java.util.Date;

public class CustFamily implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;
    private String custId;
    private String memberId;
    private String memo;

    //扩展字段
    private User user;
    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	private String applyName;
    private String path;
    private String idCard;
    private Integer sex;
    private Date birthday;
    private String phone;

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

    public String getMemberId()
    {
        return memberId;
    }

    public void setMemberId(String memberId)
    {
        this.memberId = memberId;
    }

    public String getMemo()
    {
        return memo;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public String getApplyName()
    {
        return applyName;
    }

    public void setApplyName(String applyName)
    {
        this.applyName = applyName;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
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

    public Date getBirthday()
    {
        return birthday;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

}