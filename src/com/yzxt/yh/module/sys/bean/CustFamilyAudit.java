package com.yzxt.yh.module.sys.bean;

import java.sql.Timestamp;
import java.util.Date;

public class CustFamilyAudit implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    private String id;
    private String custId;
    private String memberId;
    private String memberPhone;
    private String applyContext;
    private Integer state;
    private Timestamp applyTime;
    private Timestamp auditTime;
    private String memo;

    //扩展字段
    private String applyName;
    private String idCard;
    private String path;
    private Integer sex;
    private Date birthday;
    private String eamai;
    private String address;
    private Integer type;
    
   
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getEamai() {
		return eamai;
	}

	public void setEamai(String eamai) {
		this.eamai = eamai;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	private User user;
    

    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCustId()
    {
        return this.custId;
    }

    public void setCustId(String custId)
    {
        this.custId = custId;
    }

    public String getMemberId()
    {
        return this.memberId;
    }

    public void setMemberId(String memberId)
    {
        this.memberId = memberId;
    }

    public String getMemberPhone()
    {
        return this.memberPhone;
    }

    public void setMemberPhone(String memberPhone)
    {
        this.memberPhone = memberPhone;
    }

    public String getApplyContext()
    {
        return this.applyContext;
    }

    public void setApplyContext(String applyContext)
    {
        this.applyContext = applyContext;
    }

    public Integer getState()
    {
        return this.state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public Timestamp getApplyTime()
    {
        return this.applyTime;
    }

    public void setApplyTime(Timestamp applyTime)
    {
        this.applyTime = applyTime;
    }

    public Timestamp getAuditTime()
    {
        return this.auditTime;
    }

    public void setAuditTime(Timestamp auditTime)
    {
        this.auditTime = auditTime;
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

    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
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

}