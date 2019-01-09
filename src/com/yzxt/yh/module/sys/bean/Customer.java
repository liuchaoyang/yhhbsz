package com.yzxt.yh.module.sys.bean;

import java.sql.Timestamp;
import java.util.Date;

public class Customer implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    private String userId;
    private String memberId;
    private String doctorId;
    public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	private Integer sex;
    private Date birthday;
    private String national;
    private String contactPhone;
    private Integer degree;
    private String profession;
    private Integer maritalStatus;
    private Integer liveAlone;
    private Integer healthyStatus;
    private String qqNumber;
    private String wechatNumber;
    private String address;
    private String memo;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;

    // 非持久化字段
    private User user;
    private String name;
    private String nationalName;
    private String idCard;
    private String phone;
    private String orgName;
    private String doctorName;
    private Date startDay;
    private Date endDay;

    private Double height;
    private Double weight;
    private User operUser;
    private String account;
    private String password;
    private String email;
    private String age;
    private String path;

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getMemberId()
    {
        return memberId;
    }

    public void setMemberId(String memberId)
    {
        this.memberId = memberId;
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

    public String getNational()
    {
        return national;
    }

    public void setNational(String national)
    {
        this.national = national;
    }

    public String getContactPhone()
    {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone)
    {
        this.contactPhone = contactPhone;
    }

    public Integer getDegree()
    {
        return degree;
    }

    public void setDegree(Integer degree)
    {
        this.degree = degree;
    }

    public String getProfession()
    {
        return profession;
    }

    public void setProfession(String profession)
    {
        this.profession = profession;
    }

    public Integer getMaritalStatus()
    {
        return maritalStatus;
    }

    public void setMaritalStatus(Integer maritalStatus)
    {
        this.maritalStatus = maritalStatus;
    }

    public Integer getLiveAlone()
    {
        return liveAlone;
    }

    public void setLiveAlone(Integer liveAlone)
    {
        this.liveAlone = liveAlone;
    }

    public Integer getHealthyStatus()
    {
        return healthyStatus;
    }

    public void setHealthyStatus(Integer healthyStatus)
    {
        this.healthyStatus = healthyStatus;
    }

    public String getQqNumber()
    {
        return qqNumber;
    }

    public void setQqNumber(String qqNumber)
    {
        this.qqNumber = qqNumber;
    }

    public String getWechatNumber()
    {
        return wechatNumber;
    }

    public void setWechatNumber(String wechatNumber)
    {
        this.wechatNumber = wechatNumber;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getMemo()
    {
        return memo;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public String getCreateBy()
    {
        return createBy;
    }

    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }

    public Timestamp getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    public String getUpdateBy()
    {
        return updateBy;
    }

    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy;
    }

    public Timestamp getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime)
    {
        this.updateTime = updateTime;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getNationalName()
    {
        return nationalName;
    }

    public void setNationalName(String nationalName)
    {
        this.nationalName = nationalName;
    }

    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getOrgName()
    {
        return orgName;
    }

    public void setOrgName(String orgName)
    {
        this.orgName = orgName;
    }

    public String getDoctorName()
    {
        return doctorName;
    }

    public void setDoctorName(String doctorName)
    {
        this.doctorName = doctorName;
    }

    public Date getStartDay()
    {
        return startDay;
    }

    public void setStartDay(Date startDay)
    {
        this.startDay = startDay;
    }

    public Date getEndDay()
    {
        return endDay;
    }

    public void setEndDay(Date endDay)
    {
        this.endDay = endDay;
    }

    public Double getHeight()
    {
        return height;
    }

    public void setHeight(Double height)
    {
        this.height = height;
    }

    public Double getWeight()
    {
        return weight;
    }

    public void setWeight(Double weight)
    {
        this.weight = weight;
    }

    public User getOperUser()
    {
        return operUser;
    }

    public void setOperUser(User operUser)
    {
        this.operUser = operUser;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getAge()
    {
        return age;
    }

    public void setAge(String age)
    {
        this.age = age;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

}